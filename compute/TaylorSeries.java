package pgc.compute;

import java.math.BigDecimal;

import pgc.data.ExpressionProgram;
import pgc.data.Parser;
import pgc.data.Variable;

public class TaylorSeries {

	public TaylorSeries() {

	}

	/**
	 * Tinh Taylor cua ham 1 bien
	 * 
	 * @param str_exp
	 * @param var
	 * @param a
	 * @param n
	 * @return
	 */
	public static ExpressionProgram seies(String str_exp, Variable var, int a,
			int n) {
		var.setVal(a);
		Parser parser = new Parser();
		parser.add(var);
		ExpressionProgram expg = parser.parse(str_exp);
		double p1 = expg.getVal();
		String result = Double.toString(p1);
		for (int i = 1; i <= n; i++) {
			expg = expg.derivative(var);
			BigDecimal deriv = expg.getBigValue();// j
			BigDecimal factorial = bigFactorial(new BigDecimal(i));
			BigDecimal factor = deriv.divide(factorial);
			if(a != 0)
				result += "+" + factor.toString() + "*" + "(" + var.getName() + "-"
						+ Integer.toString(a) + ")^" + Integer.toString(i);
			else
				result += "+" + factor.toString() + "*" + var.getName()+ "^" 
						+ Integer.toString(i);
		}
		ExpressionProgram exprog = parser.parse(result);
		return exprog;
	}

	/**
	 * Tinh giai thua
	 * 
	 * @param n
	 * @return
	 */
	private static BigDecimal bigFactorial(BigDecimal n) {
		if (n.compareTo(BigDecimal.ONE) == 0
				|| n.compareTo(BigDecimal.ONE) == -1)
			return BigDecimal.ONE;
		else
			return n.multiply(bigFactorial(n.subtract(BigDecimal.ONE)));

	}
}
