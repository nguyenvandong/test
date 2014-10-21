package pgc.data;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class BigValueMath implements Value {
	private Function f; // If non-null, this is a value of the form f(params);
	// If null, it's of the form x + y, x - y, ...
	private BigDecimal[] param;
	private Value x, y;
	private char op;

	/**
	 * Create a ValueMath object whose value is computed by applying an
	 * arithmetic operator the values of x and y.
	 * 
	 * @param op
	 *            The arithmetic operator that is to be applied to x and y. This
	 *            should be one of the characters '+', '-', '*', '/', or '^'.
	 *            (No error is thrown if another character is provided. It will
	 *            be treated as a '/').
	 */
	public BigValueMath(Value x, Value y, char op) {
		this.x = x;
		this.y = y;
		this.op = op;
	}

	/**
	 * Create a ValueMath object whose value is computed as f(x).
	 */
	public BigValueMath(Function f, Value x) {
		if (f.getArity() != 1)
			throw new IllegalArgumentException(
					"Internal Error:  The function in a ValueMath object must have arity 1.");
		this.f = f;
		this.x = x;
		param = new BigDecimal[1];
	}

	@Override
	public double getVal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getBigValue() {
		 if (f != null) {
	         param[0] = x.getBigValue();
	         return f.getBigValue(param);
	      }
	      else {
	         BigDecimal a = x.getBigValue();
	         BigDecimal b = y.getBigValue();
	         switch (op) {
	            case '+': return a.add(b);
	            case '-': return a.subtract(b);
	            case '*': return a.multiply(b);
	            case '/': return a.divide(b);
	            case '^': return a.pow(b.intValue());
	            default:  throw new IllegalArgumentException("Internal Error:  Unknown math operator.");
	         }
	      }
	}

}
