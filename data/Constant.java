package pgc.data;

import java.math.BigDecimal;

/**
 * A Constant is a Value that represents a constant real number. (The value
 * doesn't have to be constant in sub-classes, since the member that stores the
 * value is protected, not private.) A Constant doesn't necessarily need a name.
 * If the name is null, then the print string for the Constant is the value of
 * the constant. If it has a non-null name, then the print string is the name.
 * (Note that, as for any MathObject, if the name is null, than the Constant
 * can't be added to a Parser.) Constant objects are used to represent the
 * mathematical constants pi and e. A Constant is both an Expression and an
 * ExpressionCommand. Since it is an ExpressionCommand, it can occur as a
 * command in an ExpressionProgram. In that case, it simply represents a named
 * constant occurs in an expression.
 */
@SuppressWarnings("serial")
public class Constant implements Expression, ExpressionCommand, MathObject {
	// Also implements Value, which is a subinterface of Expression.

	public String name; // This Constant's name, possibly null.

	/**
	 * The value of this Constant.
	 */
	protected double value;
	protected BigDecimal bigValue;

	/**
	 * Create an unnamed Constant with the given value and null name.
	 */
	public Constant(double value) {
		this.value = value;
	}

	public Constant(BigDecimal bigValue) {
		this.bigValue = bigValue;
	}

	/**
	 * Create a Constant with the given name and value. The name can be null.
	 * 
	 * @param name
	 * @param value
	 */
	public Constant(String name, double value) {
		setName(name);
		this.value = value;
	}

	/**
	 * Tao mot hang so voi ten va gia tri BigDemical
	 * 
	 * @param name
	 * @param bigValue
	 */
	public Constant(String name, BigDecimal bigValue) {
		setName(name);
		this.bigValue = bigValue;
	}

	// -------------------- Methods from the MathObject interface
	// -------------------------

	/**
	 * Return the name of this Constant. It can be null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this Constant. (Note that this should not be done if the
	 * Constant has been registered with a Parser.)
	 */
	public void setName(String name) {
		this.name = name;
	}

	// -------------- Method from the Value interface (inherited through
	// Expression) ------

	/**
	 * Return the value of this Constant.
	 */
	public double getVal() {
		return value;
	}

	public BigDecimal getBigValue() {
		return bigValue;
	};

	// ----------------------- Methods from the Expression interface
	// ---------------------

	/**
	 * Return the value of the Constant. Since a constant is continuous
	 * function, there is only one "case", so no case information needs to be
	 * recorded in cases.
	 */
	public double getValueWithCases(Cases cases) {
		return value;
	}

	/**
	 * Return the derivative of this Constant with respect to the variable wrt.
	 * The derivative is another Constant with value zero.
	 */
	public Expression derivative(Variable wrt) {
		return new Constant(0);
	}

	/**
	 * Return the print string representing this Constant. The string is the
	 * name of the constant, if that is non-null. Otherwise, it is the value of
	 * the constant.
	 */
	public String toString() {
		if (name == null)
			return NumUtils.realToString(value);
		else
			return name;
	}

	// -------------------- Methods from the ExpressionCommand interface
	// -----------------

	/**
	 * Them mot hang so vao Stack. Dieu nay duoc thuc hien bang cach them gia
	 * tri cua hang so vao stack.. Gia tri cua hang so se khong co bat ky
	 * "cases" nen khong can ghi lai thong tin trong cases
	 */
	public void apply(StackOfDouble stack, Cases cases) {
		stack.push(getVal());
	}

	public void apply(StackOfBigDecimal stackOfBigDecimal, Cases cases) {
		if(bigValue == null){
			stackOfBigDecimal.push(new BigDecimal(getVal()));
		}else
			stackOfBigDecimal.push(getBigValue());
	}

	/**
	 * Add a commands to deriv to evaluate the derivative of this Constant with
	 * respect to the variable. The derivative is 0, so the only command is the
	 * constant 0 (which really represents the stack operation "push 0"). The
	 * program and the position of the Constant in that program are irrelevant.
	 */
	public void compileDerivative(ExpressionProgram prog, int myIndex,
			ExpressionProgram deriv, Variable wrt) {
		deriv.addConstant(0);
	}

	/**
	 * Return the number of locations that this Constant uses in the program.
	 * The value is always 1, since the constant is a complete sub-expression in
	 * itself.
	 */
	public int extent(ExpressionProgram prog, int myIndex) {
		return 1;
	}

	/**
	 * Retrun false, since the value of this Constant is independent of the
	 * value of x.
	 */
	public boolean dependsOn(Variable x) {
		return false;
	}

	/**
	 * Append the print string for this Constant to the buffer. (The values of
	 * prog and myIndex are irrelevant.)
	 */
	public void appendOutputString(ExpressionProgram prog, int myIndex,
			StringBuffer buffer) {
		buffer.append(toString());
	}

	@Override
	public BigDecimal getBigValueWithCases(Cases cases) {
		// TODO Auto-generated method stub
		return null;
	}

} // end class Constant

