
package pgc.data;

import java.math.BigDecimal;

/**
 * A Variable is a Value object whose value can be changed. Usually, a Variable
 * will have a name, although that is not required unless Variable is going
 * to be registered with a Parser. A Variable can be used as a Value, an
 * Expression, or an ExpressionCommand. Since it is an ExpressionCommand, it can
 * occur as a command in an ExpressionProgram. In that case, it simply
 * represents a variable that occurs as part of an expression.
 * <p>
 * This class implements the Expression, ExpressionCommand, MathObject, and
 * Value interfaces (since Constant implements them).
 * <p>
 * Most methods in interfaces Value, Exprssion, ExpressionCommand, and
 * MathObject are inherited from class Constant. The following four methods
 * override methods inherited from that class: public Expression
 * derivative(Variable wrt); public void compileDerivative(ExpressionProgram
 * prog, int myIndex, ExpressionProgram deriv, Variable wrt); public boolean
 * dependsOn(Variable x); and public String toString().
 */
@SuppressWarnings("serial")
public class Variable extends Constant {
	public double exponent;

	/**
	 * Create an unnamed Variable with initial value 0.
	 */
	public Variable() {
		super(0);
	}

	/**
	 * Create a Variable with the given name and with initial value zero. (The
	 * name can be null.)
	 * @param name
	 */
	public Variable(String name) {
		super(name, 0);
	}

	/**
	 * Create a Variable with the given name and given initial value. (The name
	 * can be null.)
	 * @param name
	 * @param value
	 */
	public Variable(String name, double value) {
		super(name, value);
	}

	/**
	 * khoi tao bien voi ten va gia tri (big)
	 * @param name
	 * @param bigValue
	 */
	public Variable(String name, BigDecimal bigValue){
		super(name, bigValue);
	}
	/**
	 * Set the value of this Variable to the specified value.
	 */
	public void setVal(double value) {
		this.value = value;
	}
	
	/**
	 * Gan gia tri (kieu BigDemical) cho bien
	 * @param bigValue
	 */
	public void setBigValue(BigDecimal bigValue){
		this.bigValue=bigValue;
	}

	/**
	 * Set the exponent of this Variable
	 * @param exponent
	 */
	public void setExponent(double exponent) {
		this.exponent = exponent;
	}

	public double getExponent() {
		return exponent;
	}
	
	/**
	 * Convert Variable to String
	 * @return
	 */
	public String convertToString(){
		String var="";
		if(exponent!=1)
			var=name+"^"+String.valueOf(exponent);
		else
			var=name;
		return var;
	}

	/**
	 * Return the derivative of this Variable with respect to the Variable wrt.
	 * The answer is 1 if wrt is this Variable. Otherwise, the answer is 0.
	 * 
	 * @param wrt
	 *            "with respect to", i.e., the variable with respect to which to
	 *            take the derivative.
	 * @return a constant: 1 if wrt is this Variable, 0 otherwise.
	 */
	public Expression derivative(Variable wrt) {
		return new Constant((wrt == this) ? 1 : 0);
	}

	/**
	 * Add a command to deriv to evaluate the derivative of this Variable with
	 * respect to the Variable wrt. The derivative is a command for pushing
	 * either 1 or 0, depending on whether wrt is this Variable or some other
	 * Variable. This is not meant to be called directly.
	 */
	public void compileDerivative(ExpressionProgram prog, int myIndex,
			ExpressionProgram deriv, Variable wrt) {
		deriv.addConstant((wrt == this) ? 1 : 0);
	}

	/**
	 * Check whether the value of this variable depends on the value of x. This
	 * is true if x is this Variable, false otherwise.
	 */
	public boolean dependsOn(Variable x) {
		return this == x;
	}

	/**
	 * Return a print string representing this variable. The string is the name
	 * of the variable, if it has one. If not, the string "(unnamed variable)"
	 */
	public String toString() {
		String s = getName();
		return (s == null) ? "(unnamed variable)" : s;
	}

} // end class Variable

