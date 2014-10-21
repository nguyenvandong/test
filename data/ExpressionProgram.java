package pgc.data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * An ExprssionProgram represents a mathematical expression such as "3" or
 * "sin(x^2)", stored in the form of a program for a stack machine. The program
 * consists of a sequence of commands that, when executed, will compute the
 * value of the expression.
 * <p>
 * Each command is encoded as an integer. There are three types of commands that
 * can occur: (1) A negative integer must be one of the 36 constant values PLUS,
 * MINUS,..., CUBERT. These constants represent unary and binary operators and
 * standard functions. (2) An integer in the range 0 <= n < 0x3FFFFFFF encodes
 * an operation of the form "push a constant onto the stack". The constant that
 * is being pushed is encoded as an index in the array "constant", which is a
 * private member of this class that holds all the constants that occur in this
 * ExpressionProgram. (3) An integer >= 0x3FFFFFFF represents an
 * ExpressionCommand object. When 0x3FFFFFFF is subtracted from the integer, the
 * result is an index into the array "command", which is a private member of
 * this class that holds all the ExpressionCommands that occur in this
 * ExpressionProgram. Mot ExpressionProgram dai dien cho mot bieu thuc toan hoc
 * nhu 3 or sin(x^2), duoc luu tru duoi hinh thuc nhu cua chuong trinh stack
 * machine. Chuong trinh bao gom mot chuoi cac cau lenh, khi duoc thuc hien se
 * tinh toan gia tri cua bieu thuc. Moi cau lenh duoc ma hoa la mot so nguyen.
 * Co 3 loai cau lenh + so nguyen am co gia tri trong doan [-36; -1] tuong ung
 * voi cac toan tu va cac ham co ban + so nguyen 0 <= n < 0x3FFFFFFF ma hoa mot
 * toan tu hang so xuat hien trong bieu thuc + mot so nguyen >= 0x3FFFFFFF tuong
 * ung voi mot doi tuong ExpressionCommand
 */

@SuppressWarnings("serial")
public class ExpressionProgram implements Expression {

	private static BigDecimal ZERO;
	private static BigDecimal ONE;

	/**
	 * Ma cua toan tu mot ngoi, hai ngoi hoac cac ham co ban.
	 */
	public static final int

	PLUS = -1, MINUS = -2, TIMES = -3, DIVIDE = -4, POWER = -5, EQ = -6,
			NE = -7, LT = -8, GT = -9, LE = -10, GE = -11, AND = -12, OR = -13,
			NOT = -14, UNARY_MINUS = -15, FACTORIAL = -16, SIN = -17,
			COS = -18, TAN = -19, COT = -20, SEC = -21, CSC = -22,
			ARCSIN = -23, ARCCOS = -24, ARCTAN = -25, ABS = -26, SQRT = -27,
			EXP = -28, LN = -29, LOG2 = -30, LOG10 = -31, TRUNC = -32,
			ROUND = -33, FLOOR = -34, CEILING = -35, CUBERT = -36;

	/**
	 * If this is non-null, it is used as the print string for this expression
	 * in the toString() method. (When an expression is created by a Parser by
	 * parsing a string, the parse stores that string in this variable.) Neu no
	 * khong rong no se duoc su dung nhu la chuoi in cho bieu thuc trong phuong
	 * thuc toString().(khi mot bieu thuc duoc tao ra bang cach phan tich mot
	 * chuoi, phan tich nay se luu tru chuoi trong bien)
	 */
	public String sourceString;
	/**
	 * mang cac lenh cua chuong trinh, dung de chua cac lenh cua chong trinh cac
	 * hang so va cac ExpressionProgram thi duoc ma hoa thanh cac so duong trong
	 * khi cac so am ma hoa opCodes tu danh sach tren
	 */
	public int[] prog = new int[1];

	/**
	 * So luong lenh trong chuong trinh
	 */
	public int progCt; // The number of commands in prog.

	// --------------------- Methods for creating the program
	// ------------------------------

	/**
	 * Default constructor creates an initially empty program.
	 */
	public ExpressionProgram() {
	}

	/**
	 * Them mot lenh moi vao chuong trinh.VD "com" co the la mot bien hay mot
	 * hang so Trong truong hop do y nghia cua cac lenh la hoat dong cua ngan
	 * xep "push (value of com)".
	 * 
	 * @param com
	 *            them vao nhu lenh tiep theo cua chuong trinh
	 */
	public void addCommandObject(ExpressionCommand com) {
		int loc = findCommand(com);
		addCommandCode(loc + 0x3FFFFFFF);
	}

	/**
	 * Add the number d as the next command in the program. The meaning of this
	 * command is actually the stack operation "push d".
	 *
	 * @param d
	 *            added as next command in program.
	 */
	public void addConstant(double d) {
		int loc = findConstant(d);
		addCommandCode(loc);
	}

	/**
	 * Add a command code to the program, where code is one of the opCode
	 * constants that are public final members of this class, from CUBERT to
	 * PLUS. Each code represents either a binary or unary operator or a
	 * standard function that operates on the stack by poping its argument(s)
	 * from the stack, perfroming the operation, and pushing the result back
	 * onto the stack.
	 */
	public void addCommand(int code) {
		if (code >= 0 || code < CUBERT)
			throw new IllegalArgumentException(
					"Internal Error.  Illegal command code.");
		addCommandCode(code);
	}

	/**
	 * Dung de tiet kiem bo nho, Cat mang xuong con chieu dai thuc te. Nen thuc
	 * hien sau khi ket thuc chuong trinh
	 */
	public void trim() {
		if (progCt != prog.length) {
			int[] temp = new int[progCt];
			System.arraycopy(prog, 0, temp, 0, progCt);
			prog = temp;
		}
		if (commandCt != command.length) {
			ExpressionCommand[] temp = new ExpressionCommand[commandCt];
			System.arraycopy(command, 0, temp, 0, commandCt);
			command = temp;
		}
		if (constantCt != constant.length) {
			double[] temp = new double[constantCt];
			System.arraycopy(constant, 0, temp, 0, constantCt);
			constant = temp;
		}
	}

	/**
	 * Them lenh vao mang lenh
	 * 
	 * @param code
	 */
	private void addCommandCode(int code) {
		if (progCt == prog.length) {
			int[] temp = new int[prog.length * 2];
			System.arraycopy(prog, 0, temp, 0, progCt);
			prog = temp;
		}
		prog[progCt++] = code;
	}

	// ------------------- Methods for evaluating the program
	// -----------------------

	private Cases cases; // If this is non-null while the expression is being
							// evaluated, then
							// information about "cases" is recorded in it as
							// the evaluation is
							// done. See the Cases class for more information.

	private StackOfDouble stack = new StackOfDouble();

	// An ExpressionProgram is a program for a stack machine. This is the
	// stack that is used when the program is executed.
	/**
	 * Run the ExprssionProgram and return the value that it computes.
	 */
	synchronized public double getVal() {
		cases = null;
		return basicGetValue();
	}

	private double basicGetValue() {
		// Run the program and return its value.
		stack.makeEmpty();
		for (int pc = 0; pc < progCt; pc++) {
			int code = prog[pc];
			if (code < 0) {
				double ans = applyCommandCode(code);
				if (Double.isNaN(ans) || Double.isInfinite(ans)) {
					if (cases != null)
						cases.addCase(0);
					return Double.NaN;
				}
				stack.push(ans);
			} else if (code < 0x3FFFFFFF) {
				stack.push(constant[code]);
			} else {
				command[code - 0x3FFFFFFF].apply(stack, cases);
			}
		}
		if (stack.size() != 1)
			throw new IllegalArgumentException(
					"Internal Error: Improper stack state after expression evaluation.");
		double val = stack.pop();
		if (cases != null)
			cases.addCase(Double.isNaN(val) ? 0 : 1);
		return val;
	}

	/**
	 * Apply the stack operation represented by code (a number < 0) to the
	 * stack.
	 */
	protected double applyCommandCode(int code) {
		double ans;
		if (code < OR)
			ans = eval(code, stack.pop());
		else
			ans = eval(code, stack.pop(), stack.pop());
		return ans;
	}

	private void addCase(int c) {
		// If the member variable cases is not null, record c as the next item
		// of case information in cases.
		if (cases != null)
			cases.addCase(c);
	}

	/**
	 * Run the ExprssionProgram and return the value that it computes. If the
	 * Cases object, c, is non-null, then information about "cases" is recorded
	 * in c. This information can be used to help detect possible
	 * "discontinuities" between two evaluations. See the Cases class for more
	 * information.
	 */
	synchronized public double getValueWithCases(Cases c) {
		cases = c;
		double d = basicGetValue();
		cases = null;
		return d;
	}

	private double eval(int commandCode, double x) {
		// Compute the value of the unary operator represented by commandCode,
		// when
		// applied to the value x.
		switch (commandCode) {
		case NOT:
			return (x == 0) ? 1 : 0;
		case UNARY_MINUS:
			return -x;
		case FACTORIAL:
			return factorial(x);
		case SIN:
			return Math.sin(x);
		case COS:
			return Math.cos(x);
		case TAN:
			addCase((int) Math.floor((x - Math.PI / 2.0) / Math.PI));
			return Math.tan(x);
		case COT:
			addCase((int) Math.floor(x / Math.PI));
			return Math.cos(x) / Math.sin(x);
		case SEC:
			addCase((int) Math.floor((x - Math.PI / 2.0) / Math.PI));
			return 1 / Math.cos(x);
		case CSC:
			addCase((int) Math.floor(x / Math.PI));
			return 1 / Math.sin(x);
		case ARCSIN:
			return Math.asin(x);
		case ARCCOS:
			return Math.acos(x);
		case ARCTAN:
			return Math.atan(x);
		case ABS:
			addCase((x > 0) ? 1 : ((x < 0) ? -1 : 0));
			return Math.abs(x);
		case SQRT:
			return (x < 0) ? Double.NaN : Math.sqrt(x);
		case EXP:
			return Math.exp(x);
		case LN:
			return (x <= 0) ? Double.NaN : Math.log(x);
		case LOG2:
			return (x <= 0) ? Double.NaN : Math.log(x) / Math.log(2);
		case LOG10:
			return (x <= 0) ? Double.NaN : Math.log(x) / Math.log(10);
		case TRUNC:
			addCase((int) x);
			return (long) x;
		case ROUND:
			addCase((int) Math.floor(x + 0.5));
			return Math.floor(x + 0.5);
		case FLOOR:
			addCase((int) Math.floor(x));
			return Math.floor(x);
		case CEILING:
			addCase((int) Math.floor(x));
			return Math.ceil(x);
		case CUBERT:
			addCase((x > 0) ? 1 : -1);
			return (x >= 0) ? Math.pow(x, 1.0 / 3.0) : -Math.pow(-x, 1.0 / 3.0);
		default:
			return Double.NaN;
		}
	}

	private double factorial(double x) {
		if (x <= -0.5 || x > 170.5) {
			addCase(-1);
			return Double.NaN;
		}
		int n = (int) x;
		addCase(n);
		double ans = 1;
		for (int i = 1; i <= n; i++)
			ans *= i;
		return ans;
	}

	private double eval(int commandCode, double y, double x) {
		switch (commandCode) {
		case PLUS:
			return x + y;
		case MINUS:
			return x - y;
		case TIMES:
			return x * y;
		case DIVIDE:
			addCase((y > 0) ? 1 : ((y < 0) ? -1 : 0));
			return x / y;
		case POWER:
			return Math.pow(x, y);
		case EQ:
			return (x == y) ? 1 : 0;
		case NE:
			return (x != y) ? 1 : 0;
		case GT:
			return (x > y) ? 1 : 0;
		case LT:
			return (x < y) ? 1 : 0;
		case GE:
			return (x >= y) ? 1 : 0;
		case LE:
			return (x <= y) ? 1 : 0;
		case AND:
			return ((x != 0) && (y != 0)) ? 1 : 0;
		case OR:
			return ((x != 0) || (y != 0)) ? 1 : 0;
		default:
			return Double.NaN;
		}
	}

	// -----------------Tinh gia tri cua bieu thuc voi so lon
	// (BigDemical)--------------------

	private StackOfBigDecimal bigStack = new StackOfBigDecimal();

	synchronized public BigDecimal getBigValue() {
		cases = null;
		return basicGetBigValue();
	}

	synchronized public BigDecimal getBigValueWithCases(Cases c) {
		cases = c;
		BigDecimal d = basicGetBigValue();
		cases = null;
		return d;
	}

	private BigDecimal basicGetBigValue() {
		// Run the program and return its value.
		bigStack.makeEmpty();
		for (int pc = 0; pc < progCt; pc++) {
			int code = prog[pc];
			if (code < 0) {
				BigDecimal ans = applyBigCommandCode(code);
				bigStack.push(ans);
			} else if (code < 0x3FFFFFFF) {
				Locale locale = new Locale("en", "UK");
				String pattern = "###.####################################";
				DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
						.getNumberInstance(locale);
				decimalFormat.applyPattern(pattern);
				String ct = decimalFormat.format(constant[code]);
				bigStack.push(new BigDecimal(ct));
			} else {
				command[code - 0x3FFFFFFF].apply(bigStack, cases);
			}
		}
		if (bigStack.size() != 1)
			throw new IllegalArgumentException(
					"Internal Error:  Improper stack state after expression evaluation.");
		BigDecimal val = bigStack.pop();
		if (cases != null)
			cases.addCase(1);
		return val;
	}

	protected BigDecimal applyBigCommandCode(int code) {
		BigDecimal ans;
		if (code < OR)
			ans = bigEval(code, bigStack.pop());
		else
			ans = bigEval(code, bigStack.pop(), bigStack.pop());
		return ans;
	}

	private BigDecimal bigEval(int commandCode, BigDecimal x) {
		BigDecimal result = null;
		switch (commandCode) {
		case NOT:
			return (x.compareTo(ZERO) == 0) ? ONE : ZERO;
		case UNARY_MINUS:
			return x.negate();
		case FACTORIAL:
			return factorial(x);
		case SIN:
			return new BigDecimal(Math.sin(x.doubleValue()));
		case COS:
			return new BigDecimal(Math.cos(x.doubleValue()));
		case TAN:
			addCase((int) Math.floor((x.doubleValue() - Math.PI / 2.0)
					/ Math.PI));
			return new BigDecimal(Math.tan(x.doubleValue()));
		case COT:
			addCase((int) Math.floor(x.doubleValue() / Math.PI));
			return new BigDecimal(Math.sin(x.doubleValue()))
					.divide(new BigDecimal(Math.cos(x.doubleValue())));
		case SEC:
			addCase((int) Math.floor((x.doubleValue() - Math.PI / 2.0)
					/ Math.PI));
			return ONE.divide(new BigDecimal(Math.cos(x.doubleValue())));
		case CSC:
			addCase((int) Math.floor(x.doubleValue() / Math.PI));
			return ONE.divide(new BigDecimal(Math.sin(x.doubleValue())));
		case ARCSIN:
			return new BigDecimal(Math.asin(x.doubleValue()));
		case ARCCOS:
			return new BigDecimal(Math.acos(x.doubleValue()));
		case ARCTAN:
			return new BigDecimal(Math.atan(x.doubleValue()));
		case ABS:
			addCase((x.compareTo(ZERO) == 1) ? 1
					: ((x.compareTo(ZERO) == -1) ? -1 : 0));
			return new BigDecimal(Math.abs(x.doubleValue()));
		case SQRT:
			try {
				result = new BigDecimal(Math.sqrt(x.doubleValue()));
			} catch (Exception e) {
				System.out.print(e.toString());
			}
			return result;
		case EXP:
			return new BigDecimal(Math.exp(x.doubleValue()));
		case LN:
			try {
				result = new BigDecimal(Math.log(x.doubleValue()));
			} catch (Exception e) {
				System.out.print(e.toString());
			}
			return result;
		case LOG2:
			try {
				result = new BigDecimal(Math.log(x.doubleValue()) / Math.log(2));
			} catch (Exception e) {
				System.out.print(e.toString());
			}
			return result;
		case LOG10:
			try {
				result = new BigDecimal(Math.log(x.doubleValue())
						/ Math.log(10));
			} catch (Exception e) {
				System.out.print(e.toString());
			}
			return result;
		case TRUNC:
			addCase(x.intValue());
			return x;
		case ROUND:
			addCase((int) Math.floor(x.doubleValue() + 0.5));
			return new BigDecimal(Math.floor(x.doubleValue() + 0.5));
		case FLOOR:
			addCase((int) Math.floor(x.doubleValue()));
			return new BigDecimal(Math.floor(x.doubleValue()));
		case CEILING:
			addCase((int) Math.floor(x.doubleValue()));
			return new BigDecimal(Math.ceil(x.doubleValue()));
		case CUBERT:
			addCase((x.compareTo(ZERO) == 1) ? 1 : -1);
			return ((x.compareTo(ZERO) == 1) || (x.compareTo(ZERO) == 0)) ? new BigDecimal(
					Math.pow(x.doubleValue(), 1.0 / 3.0)) : new BigDecimal(
					Math.pow(x.negate().doubleValue(), 1.0 / 3.0));
		default:
			return null;
		}
	}

	private BigDecimal factorial(BigDecimal x) {
		BigDecimal f = ONE;
		while (x.compareTo(ONE) == 1) {
			f = f.multiply(x);
			x = x.subtract(ONE);
		}
		return f;
	}

	private BigDecimal bigEval(int commandCode, BigDecimal y, BigDecimal x) {
		switch (commandCode) {
		case PLUS:
			return x.add(y);
		case MINUS:
			return x.subtract(y);
		case TIMES:
			return x.multiply(y);
		case DIVIDE:
			// addCase((y.compareTo(ZERO) == 1) ? 1: ((y.compareTo(ZERO) == -1)
			// ? -1 : 0));
			return x.divide(y);
		case POWER:
			return x.pow(y.intValue());
		case EQ:
			return (x.compareTo(y) == 0) ? ONE : ZERO;
		case NE:
			return (x.compareTo(y) != 0) ? ONE : ZERO;
		case GT:
			return (x.compareTo(y) == 1) ? ONE : ZERO;
		case LT:
			return (x.compareTo(y) == -1) ? ONE : ZERO;
		case GE:
			return (x.compareTo(y) == 1 || x.compareTo(y) == 0) ? ONE : ZERO;
		case LE:
			return (x.compareTo(y) == -1 || x.compareTo(y) == 0) ? ONE : ZERO;
		case AND:
			return (x.compareTo(y) != 0 && x.compareTo(y) != 0) ? ONE : ZERO;
		case OR:
			return (x.compareTo(y) != 0 || x.compareTo(y) != 0) ? ONE : ZERO;
		default:
			return null;
		}
	}

	// ------------- Getting the print string of this expression
	// ------------------------

	/**
	 * If a source string has been saved, use it as the print string. (When a
	 * Parser creates an expression by parsing a string, it saves the source
	 * string in the ExpressionProgram.) Otherwise, construct the print string
	 * based on the commands in the program.
	 */
	public String toString() {
		if (sourceString != null)
			return sourceString;
		else {
			StringBuffer buffer = new StringBuffer();
			appendOutputString(progCt - 1, buffer);
			return buffer.toString();
		}
	}

	/**
	 * Add a string representing part of the expression to the output buffer.
	 * You probably are only interested in this if you write a ParserExtension
	 * or ExpressionCommand. (The command at position index in the program
	 * represents a subexpression. It could be a constant or a variable, for
	 * example, which is complete subexpression in itself. Or it could be the
	 * final operator in a larger subexpression. In that case, the operands,
	 * which are located in lower positions in the program, are considered to be
	 * part of the expression. This routine appends a print string for the
	 * entire subexpression to the buffer. When this is called with index =
	 * progCt-1 (the last command in the program), it processes the entire
	 * program. Note that the hard part here is deciding when to put in
	 * parentheses. This is done based on the precedence of the operators. The
	 * result is not always pretty.
	 */
	public void appendOutputString(int index, StringBuffer buffer) {
		if (prog[index] >= 0x3FFFFFFF) {
			command[prog[index] - 0x3FFFFFFF].appendOutputString(this, index,
					buffer);
		} else if (prog[index] >= 0) {
			buffer.append(NumUtils.realToString(constant[prog[index]]));
		} else if (prog[index] >= OR) {
			int op2 = index - 1; // Position of command representing the second
									// operand.
			int op1 = op2 - extent(op2); // Position of command representing the
											// first operand.
			if (precedence(prog[op1]) < precedence(prog[index])
					|| (prog[index] == POWER && precedence(prog[op1]) == precedence(prog[index]))) {
				buffer.append('(');
				appendOutputString(op1, buffer);
				buffer.append(')');
			} else
				appendOutputString(op1, buffer);
			switch (prog[index]) {
			case PLUS:
				buffer.append(" + ");
				break;
			case MINUS:
				buffer.append(" - ");
				break;
			case TIMES:
				buffer.append("*");
				break;
			case DIVIDE:
				buffer.append("/");
				break;
			case POWER:
				buffer.append("^");
				break;
			case AND:
				buffer.append(" AND ");
				break;
			case OR:
				buffer.append(" OR ");
				break;
			case EQ:
				buffer.append(" = ");
				break;
			case NE:
				buffer.append(" <> ");
				break;
			case GE:
				buffer.append(" >= ");
				break;
			case LE:
				buffer.append(" <= ");
				break;
			case GT:
				buffer.append(" > ");
				break;
			case LT:
				buffer.append(" < ");
				break;
			}
			if (prog[op2] == UNARY_MINUS
					|| precedence(prog[op2]) < precedence(prog[index])
					|| ((prog[index] == MINUS || prog[index] == DIVIDE) && precedence(prog[op2]) == precedence(prog[index]))) {
				buffer.append('(');
				appendOutputString(op2, buffer);
				buffer.append(')');
			} else
				appendOutputString(op2, buffer);
		} else if (prog[index] <= SIN) {
			buffer.append(StandardFunction.standardFunctionName(prog[index]));
			buffer.append('(');
			appendOutputString(index - 1, buffer);
			buffer.append(')');
		} else if (prog[index] == UNARY_MINUS) {
			buffer.append('-');
			if (precedence(prog[index - 1]) <= precedence(UNARY_MINUS)) {
				buffer.append('(');
				appendOutputString(index - 1, buffer);
				buffer.append(')');
			} else
				appendOutputString(index - 1, buffer);
		} else if (prog[index] == NOT) {
			buffer.append("NOT (");
			appendOutputString(index - 1, buffer);
			buffer.append(')');
		} else if (prog[index] == FACTORIAL) {
			if (prog[index - 1] >= 0
					&& (prog[index - 1] < 0x3FFFFFFF
							|| command[prog[index - 1] - 0x3FFFFFFF] instanceof Variable || command[prog[index - 1] - 0x3FFFFFFF] instanceof Constant)) {
				appendOutputString(index - 1, buffer);
			} else {
				buffer.append('(');
				appendOutputString(index - 1, buffer);
				buffer.append(')');
			}
			buffer.append('!');
		}
	}

	private int precedence(int opcode) {
		// Return the precedence of the operator. This is used
		// by the prceding method to decide when parentheses are needed.
		if (opcode >= 0) {
			if (opcode >= 0x3FFFFFFF
					&& (command[opcode - 0x3FFFFFFF] instanceof ConditionalExpression))
				return 0;
			else
				return 7;
		} else
			switch (opcode) {
			case FACTORIAL:
			case OR:
			case AND:
				return 1;
			case GE:
			case LE:
			case GT:
			case EQ:
			case NE:
			case LT:
				return 2;
			case PLUS:
			case MINUS:
			case UNARY_MINUS:
				return 3;
			case TIMES:
			case DIVIDE:
				return 4;
			case POWER:
				return 6;
			default:
				return 7;
			}
	}

	// ----------------------- Methods for computing the derivative
	// -------------------------

	/**
	 * Compute the derivative of this expression with respect to the Variable
	 * wrt. The value returned is actually an ExpressionProgram. Tinh dao ham
	 * cua mbieu thuc theo bien wrt. Gia tri tra ve la mot bieu thuc
	 */
	public ExpressionProgram derivative(Variable wrt) {
		ExpressionProgram deriv = new ExpressionProgram();
		compileDerivative(progCt - 1, deriv, wrt);
		deriv.trim();
		simplifications(deriv);
		return deriv;
	}

	/**
	 * The command at position index in the program represents a subexpression
	 * of the whole expression. This routine adds commands to deriv for
	 * computing the derivative of that subexpression with respect to the
	 * variable wrt. You probably are not interested in this unless you write a
	 * ParserExtension or an ExpressionCommand.
	 */
	public void compileDerivative(int index, ExpressionProgram deriv,
			Variable wrt) {
		if (!dependsOn(index, wrt))
			deriv.addConstant(0); // neu bieu thuc khong phu thuoc x
		else if (prog[index] >= 0x3FFFFFFF)
			command[prog[index] - 0x3FFFFFFF].compileDerivative(this, index,
					deriv, wrt);
		else if (prog[index] >= 0)
			deriv.addConstant(0);
		else if (prog[index] >= POWER) { // prog[index]: +, -, *, /, ^
			int indexOp2 = index - 1;
			int indexOp1 = indexOp2 - extent(indexOp2);
			doBinDeriv(prog[index], indexOp1, indexOp2, deriv, wrt);
		} else if (prog[index] <= SIN) {
			doFuncDeriv(prog[index], index - 1, deriv, wrt);
		} else if (prog[index] == UNARY_MINUS) {
			compileDerivative(index - 1, deriv, wrt);
			deriv.addCommand(UNARY_MINUS);
		} else if (prog[index] == FACTORIAL) {
			deriv.addConstant(Double.NaN);
		} else if (prog[index] >= NOT)
			throw new IllegalArgumentException(
					"Internal Error: Attempt to take the derivative of a logical-valued expression.");
		else
			throw new IllegalArgumentException(
					"Internal Error: Unknown opcode.");
	}

	/**
	 * Cau lenh tai vi tri index trong chuong trinh tuong ung mot subexpression
	 * cua bieu thuc Ham nay se tim va tra ve so luong cau lenh co trong mot
	 * subexpression. Do la subexpression chiem mot phan bieu thuc giua cac
	 * index - extent + 1 and index
	 */
	public int extent(int index) {
		if (prog[index] <= NOT) {
			return 1 + extent(index - 1);
		} else {
			if (prog[index] < 0) {
				// pham vi cua toan hoang thu 2 ke tu index
				int extentOp1 = extent(index - 1);
				int extentOp2 = extent(index - 1 - extentOp1);
				return extentOp1 + extentOp2 + 1;
			} else {
				if (prog[index] < 0x3FFFFFFF)
					return 1;
				else
					return command[prog[index] - 0x3FFFFFFF]
							.extent(this, index);
			}
		}

	}

	/**
	 * The command at position index in the program represents a subexpression
	 * of the whole expression. This routine copies the commands for the entire
	 * subexpression to the destination program. You probably are not interested
	 * in this unless you write a ParserExtension or an ExpressionCommand.
	 */
	public void copyExpression(int index, ExpressionProgram destination) {
		int size = extent(index);
		for (int i = index - size + 1; i <= index; i++) {
			if (prog[i] < 0)
				destination.addCommand(prog[i]);
			else if (prog[i] >= 0x3FFFFFFF)
				destination.addCommandObject(command[prog[i] - 0x3FFFFFFF]);
			else
				destination.addConstant(constant[prog[i]]);
		}
	}

	/**
	 * The command at position index in the program represents a subexpression
	 * of the whole expression. If that subexpression includes some dependence
	 * on the variable x, then true is returned. If the subexpression is
	 * constant with respect to the variable x, then false is returned. You
	 * probably are not interested in this unless you write a ParserExtension or
	 * an ExpressionCommand.
	 */
	public boolean dependsOn(int index, Variable x) {
		int size = extent(index);
		for (int i = index - size + 1; i <= index; i++) {
			if (prog[i] >= 0x3FFFFFFF) {
				ExpressionCommand c = command[prog[i] - 0x3FFFFFFF];
				if (c == x || c.dependsOn(x))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the expression as a whole has any dependence on the
	 * variable x.
	 */
	public boolean dependsOn(Variable x) {
		return dependsOn(progCt - 1, x);
	}

	private void doBinDeriv(int opCode, int op1, int op2,
			ExpressionProgram deriv, Variable wrt) {
		switch (opCode) {
		case PLUS:
			if (!dependsOn(op1, wrt))
				compileDerivative(op2, deriv, wrt);
			else if (!dependsOn(op2, wrt))
				compileDerivative(op1, deriv, wrt);
			else {
				compileDerivative(op1, deriv, wrt);
				compileDerivative(op2, deriv, wrt);
				deriv.addCommand(PLUS);
			}
			break;
		case MINUS:
			if (!dependsOn(op1, wrt)) {
				compileDerivative(op2, deriv, wrt);
				deriv.addCommand(UNARY_MINUS);
			} else if (!dependsOn(op2, wrt))
				compileDerivative(op1, deriv, wrt);
			else {
				compileDerivative(op1, deriv, wrt);
				compileDerivative(op2, deriv, wrt);
				deriv.addCommand(MINUS);
			}
			break;
		case TIMES: {
			int cases = 0;
			if (dependsOn(op2, wrt)) {
				copyExpression(op1, deriv);
				if (prog[op2] < 0x3FFFFFFF
						|| command[prog[op2] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op2, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				cases++;
			}
			if (dependsOn(op1, wrt)) {
				copyExpression(op2, deriv);
				if (prog[op1] < 0x3FFFFFFF
						|| command[prog[op1] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op1, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				cases++;
			}
			if (cases == 2)
				deriv.addCommand(PLUS);
		}
			break;
		case DIVIDE:
			if (!dependsOn(op2, wrt)) {
				compileDerivative(op1, deriv, wrt);
				copyExpression(op2, deriv);
				deriv.addCommand(DIVIDE);
			} else if (!dependsOn(op1, wrt)) {
				copyExpression(op1, deriv);
				deriv.addCommand(UNARY_MINUS);
				copyExpression(op2, deriv);
				deriv.addConstant(2);
				deriv.addCommand(POWER);
				deriv.addCommand(DIVIDE);
				if (prog[op2] < 0x3FFFFFFF
						|| command[prog[op2] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op2, deriv, wrt);
					deriv.addCommand(TIMES);
				}
			} else {
				copyExpression(op2, deriv);
				if (prog[op1] < 0x3FFFFFFF
						|| command[prog[op1] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op1, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				copyExpression(op1, deriv);
				if (prog[op2] < 0x3FFFFFFF
						|| command[prog[op2] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op2, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				deriv.addCommand(MINUS);
				copyExpression(op2, deriv);
				deriv.addConstant(2);
				deriv.addCommand(POWER);
				deriv.addCommand(DIVIDE);
			}
			break;
		case POWER:
			if (!dependsOn(op2, wrt)) {
				copyExpression(op2, deriv);
				copyExpression(op1, deriv);
				if (prog[op2] >= 0 && prog[op2] < 0x3FFFFFFF) {
					if (constant[prog[op2]] != 2) {
						deriv.addConstant(constant[prog[op2]] - 1);
						deriv.addCommand(POWER);
					}
				} else if (prog[op2] == UNARY_MINUS && prog[op2 - 1] >= 0
						&& prog[op2 - 1] < 0x3FFFFFFF) {
					deriv.addConstant(constant[prog[op2 - 1]] + 1);
					deriv.addCommand(UNARY_MINUS);
					deriv.addCommand(POWER);
				} else {
					copyExpression(op2, deriv);
					deriv.addConstant(1);
					deriv.addCommand(MINUS);
					deriv.addCommand(POWER);
				}
				deriv.addCommand(TIMES);
				if (prog[op1] < 0x3FFFFFFF
						|| command[prog[op1] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op1, deriv, wrt);
					deriv.addCommand(TIMES);
				}
			} else if (!dependsOn(op1, wrt)) {
				copyExpression(op1, deriv);
				copyExpression(op2, deriv);
				deriv.addCommand(POWER);
				if (!(prog[op1] >= 0x3FFFFFFF
						&& command[prog[op1] - 0x3FFFFFFF] instanceof Constant && ((Constant) command[prog[op1] - 0x3FFFFFFF])
						.getVal() == Math.E)) {
					copyExpression(op1, deriv);
					deriv.addCommand(LN);
					deriv.addCommand(TIMES);
				}
				if (prog[op2] < 0x3FFFFFFF
						|| command[prog[op2] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op2, deriv, wrt);
					deriv.addCommand(TIMES);
				}
			} else {
				copyExpression(op1, deriv);
				copyExpression(op2, deriv);
				deriv.addCommand(POWER);
				boolean eq = true;
				int ext1 = extent(op1);
				int ext2 = extent(op2);
				if (ext1 != ext2)
					eq = false;
				else
					for (int i = 0; i < ext1; i++)
						if (prog[op1 - i] != prog[op2 - i]) {
							eq = false;
							break;
						}
				if (eq)
					deriv.addConstant(1);
				else {
					copyExpression(op2, deriv);
					copyExpression(op1, deriv);
					deriv.addCommand(DIVIDE);
				}
				if (prog[op1] < 0x3FFFFFFF
						|| command[prog[op1] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op1, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				copyExpression(op1, deriv);
				deriv.addCommand(LN);
				if (prog[op2] < 0x3FFFFFFF
						|| command[prog[op2] - 0x3FFFFFFF] != wrt) {
					compileDerivative(op2, deriv, wrt);
					deriv.addCommand(TIMES);
				}
				deriv.addCommand(PLUS);
				deriv.addCommand(TIMES);
			}
			break;
		}
	}

	/**
	 * toi gian hoa bieu thuc
	 * 
	 * @param derivative
	 */
	private void simplifications(ExpressionProgram derivative) {
		int length = derivative.progCt;
		ArrayList<Integer> com = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			int a = derivative.prog[i];
			com.add(a);
			if (a == -15 && com.get(com.size() - 2) >= 0
					&& com.get(com.size() - 2) < 0x3FFFFFFF) {
				double c1 = derivative.constant[com.get(com.size() - 2)];
				c1 = c1 * -1;
				int constant = derivative.findConstant(c1);
				com.remove(com.size() - 1);
				com.remove(com.size() - 1);
				com.add(constant);
			}
			if (a < 0 && a >= -5) {
				if (com.get(com.size() - 2) >= 0
						&& com.get(com.size() - 2) < 0x3FFFFFFF
						&& com.get(com.size() - 3) < 0x3FFFFFFF
						&& com.get(com.size() - 3) >= 0) {
					double c1 = derivative.constant[com.get(com.size() - 2)];
					double c2 = derivative.constant[com.get(com.size() - 3)];
					switch (a) {
					case PLUS:
						c1 += c2;
						break;
					case MINUS:
						c1 -= c2;
						break;
					case TIMES:
						c1 *= c2;
						break;
					case DIVIDE:
						c1 /= c2;
						break;
					}
					int constant = derivative.findConstant(c1);
					for (int k = 0; k < 3; k++) {
						com.remove(com.size() - 1);
					}
					com.add(constant);
				}
			}
		}
		int list[] = new int[com.size()];
		for (int i = 0; i < com.size(); i++) {
			list[i] = com.get(i);
		}
		derivative.progCt = list.length;
		derivative.prog = list;
	}

	private void doFuncDeriv(int opCode, int op, ExpressionProgram deriv,
			Variable wrt) {
		// Add commands to deriv to compute the derivative of a subexpression of
		// this program
		// with respect to the variable wrt, where the main operation in the
		// subexpression is
		// the standard function represented by opCode, and the position of the
		// operand of the
		// standard function is at the index op in the program.
		/*
		 * if (opCode == TRUNC || opCode == ROUND || opCode == FLOOR || opCode
		 * == CEILING) { // I'm pretending that the derivative is zero, even
		 * though it's undefined at // certain values. deriv.addConstant(0);
		 * return; }
		 */
		switch (opCode) {
		case FLOOR:
		case CEILING:
		case TRUNC:
		case ROUND:
			copyExpression(op, deriv);
			if (opCode == ROUND) {
				deriv.addConstant(0.5);
				deriv.addCommand(PLUS);
			}
			deriv.addCommand(ROUND);
			copyExpression(op, deriv);
			if (opCode == ROUND) {
				deriv.addConstant(0.5);
				deriv.addCommand(PLUS);
			}
			deriv.addCommand(NE);
			if (opCode == TRUNC) {
				copyExpression(op, deriv);
				deriv.addConstant(0);
				deriv.addCommand(EQ);
				deriv.addCommand(OR);
			}
			ExpressionProgram zero = new ExpressionProgram();
			zero.addConstant(0);
			deriv.addCommandObject(new ConditionalExpression(zero, null));
			return;
		case SIN:
			copyExpression(op, deriv);
			deriv.addCommand(COS);
			break;
		case COS:
			copyExpression(op, deriv);
			deriv.addCommand(SIN);
			deriv.addCommand(UNARY_MINUS);
			break;
		case TAN:
			copyExpression(op, deriv);
			deriv.addCommand(SEC);
			deriv.addConstant(2);
			deriv.addCommand(POWER);
			break;
		case COT:
			copyExpression(op, deriv);
			deriv.addCommand(CSC);
			deriv.addConstant(2);
			deriv.addCommand(POWER);
			deriv.addCommand(UNARY_MINUS);
			break;
		case SEC:
			copyExpression(op, deriv);
			deriv.addCommand(SEC);
			copyExpression(op, deriv);
			deriv.addCommand(TAN);
			deriv.addCommand(TIMES);
			break;
		case CSC:
			copyExpression(op, deriv);
			deriv.addCommand(CSC);
			copyExpression(op, deriv);
			deriv.addCommand(COT);
			deriv.addCommand(TIMES);
			deriv.addCommand(UNARY_MINUS);
			break;
		case ARCSIN:
		case ARCCOS:
			deriv.addConstant(1);
			if (opCode == ARCCOS)
				deriv.addCommand(UNARY_MINUS);
			deriv.addConstant(1);
			copyExpression(op, deriv);
			deriv.addConstant(2);
			deriv.addCommand(POWER);
			deriv.addCommand(MINUS);
			deriv.addCommand(SQRT);
			deriv.addCommand(DIVIDE);
			break;
		case ARCTAN:
			deriv.addConstant(1);
			deriv.addConstant(1);
			copyExpression(op, deriv);
			deriv.addConstant(2);
			deriv.addCommand(POWER);
			deriv.addCommand(PLUS);
			deriv.addCommand(DIVIDE);
			break;
		case ABS: {
			ExpressionProgram pos = new ExpressionProgram();
			ExpressionProgram neg = new ExpressionProgram();
			compileDerivative(op, pos, wrt);
			compileDerivative(op, neg, wrt);
			neg.addCommand(UNARY_MINUS);
			ExpressionProgram negTest = new ExpressionProgram();
			copyExpression(op, negTest);
			negTest.addConstant(0);
			negTest.addCommand(LT);
			negTest.addCommandObject(new ConditionalExpression(neg, null));
			copyExpression(op, deriv);
			deriv.addConstant(0);
			deriv.addCommand(GT);
			deriv.addCommandObject(new ConditionalExpression(pos, negTest));
		}
			return;
		case SQRT:
			deriv.addConstant(1);
			deriv.addConstant(2);
			copyExpression(op, deriv);
			deriv.addCommand(SQRT);
			deriv.addCommand(TIMES);
			deriv.addCommand(DIVIDE);
			break;
		case EXP:
			copyExpression(op, deriv);
			deriv.addCommand(EXP);
			break;
		case LN:
		case LOG2:
		case LOG10:
			ExpressionProgram d = new ExpressionProgram();
			d.addConstant(1);
			copyExpression(op, d);
			d.addCommand(DIVIDE);
			if (opCode != LN) {
				d.addConstant((opCode == LOG2) ? 2 : 10);
				d.addCommand(LN);
				d.addCommand(DIVIDE);
			}
			copyExpression(op, deriv);
			deriv.addConstant(0);
			deriv.addCommand(GT);
			deriv.addCommandObject(new ConditionalExpression(d, null));
			break;
		case CUBERT:
			deriv.addConstant(1);
			deriv.addConstant(3);
			copyExpression(op, deriv);
			deriv.addConstant(2);
			deriv.addCommand(POWER);
			deriv.addCommand(CUBERT);
			deriv.addCommand(TIMES);
			deriv.addCommand(DIVIDE);
			break;
		}
		if (prog[op] < 0x3FFFFFFF || command[prog[op] - 0x3FFFFFFF] != wrt) {
			compileDerivative(op, deriv, wrt);
			deriv.addCommand(TIMES);
		}
	}

	// --------- private stuff for storing constants and ExpressionCommands
	// ----------

	public double[] constant = new double[1];
	public int constantCt; // so luong hang so trong mang hang so
	public ExpressionCommand[] command = new ExpressionCommand[1];
	public int commandCt; // so luong cac lenh trong mang lenh

	/**
	 * 
	 * @param d
	 * @return
	 */
	public int findConstant(double d) {
		// tim chi so cua hang so d trong mang ca hang so, them hang so do vao
		// mang neu no chua xuat hien trong mang.
		// Note: mang se duoc mo rong neu can thiet.
		for (int i = 0; i < constantCt; i++)
			if (constant[i] == d) {
				return i;
			}
		if (constantCt == constant.length) {
			double[] temp = new double[constant.length * 2];
			System.arraycopy(constant, 0, temp, 0, constantCt);
			constant = temp;
		}
		constant[constantCt++] = d;
		return constantCt - 1;
	}

	private int findCommand(ExpressionCommand com) {
		// Find the index of com in the command array, adding it if it is not
		// already there.
		// The array will be expanded if necessary.
		for (int i = 0; i < commandCt; i++)
			if (command[i] == com)
				return i;
		if (commandCt == command.length) {
			ExpressionCommand[] temp = new ExpressionCommand[command.length * 2];
			System.arraycopy(command, 0, temp, 0, commandCt);
			command = temp;
		}
		command[commandCt++] = com;
		return commandCt - 1;
	}
} // end class ExpressionProgram

