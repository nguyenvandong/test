package pgc.data;

/**
 * A MathObject is just an object that has setName and getName methods.
 * MathObjects can be registered with a Parser (meaning that they are stored in
 * the SymbolTable associated with the Parser, and can be used in expressions
 * parsed by the Parser).
 */
public interface MathObject extends java.io.Serializable {
	/**
	 * Get the name of this object.
	 */
	public String getName();

	/**
	 * Set the name of this object. This should not be done if the MathObject is
	 * registered with a Parser.
	 * 
	 * @param name
	 */
	public void setName(String name);

}
