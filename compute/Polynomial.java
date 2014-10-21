package pgc.compute;

import java.util.ArrayList;

import pgc.data.ExpressionProgram;
import pgc.data.Parser;
import pgc.data.StackOfInt;
import pgc.data.Variable;

/**
 * Da thuc la mang cac don thuc
 */

public class Polynomial {
	public ExpressionProgram exprog;
	// Stack luu tru thong tin cua don thuc
	private ArrayList<StackOfInt> arrStackOfInt = new ArrayList<StackOfInt>();
	public ArrayList<Monomial> polynomial = new ArrayList<Monomial>();
	public ArrayList<Monomial> tempPolynomial = new ArrayList<Monomial>();

	public Polynomial() {

	}

	/**
	 * @param strPolynomial
	 * @param var
	 */
	public Polynomial(String strPolynomial, Variable var) {
		Parser parser = new Parser();
		parser.add(var);
		this.exprog = parser.parse(strPolynomial);
		simplifyExponent();
		setPolynomial();
	}

	/**
	 * 
	 * @param strPolynomial
	 * @param var
	 */
	public Polynomial(String strPolynomial, Variable[] var) {
		Parser parser = new Parser();
		for (int i = 0; i < var.length; i++) {
			parser.add(var[i]);
		}
		this.exprog = parser.parse(strPolynomial);
		simplifyExponent();
		setPolynomial();
	}

	/**
	 * Rut gon so mua cua cac don thuc
	 */
	public void simplifyExponent() {
		ArrayList<Integer> prog = new ArrayList<Integer>();
		for (int i = 0; i < exprog.progCt; i++) {
			prog.add(exprog.prog[i]);
			if (prog.get(prog.size() - 1) < 0) {
				if(prog.get(prog.size() - 1) == -15){
					double temp = exprog.constant[prog.get(prog.size()-2)];
					int constant = exprog.findConstant(temp*-1);
					prog.remove(prog.size()-1);
					prog.remove(prog.size()-1);
					prog.add(constant);
				}else{
					double[] check = previous(prog, prog.size(), exprog.constant);
					if (check[0] != 0) { // chua hoan thanh
						int sizeProg = prog.size();
						for (int j = sizeProg - 1; j >= sizeProg - 3; j--) {
							prog.remove(j);
						}
						int constant = exprog.findConstant(check[1]);
						prog.add(constant);
					}
				}
			}
		}
		int[] prog_re = new int[prog.size()];
		for (int i = 0; i < prog.size(); i++) {
			prog_re[i] = prog.get(i);
		}
		exprog.prog = prog_re;
		exprog.progCt = exprog.prog.length;
	}

	/**
	 * Neu phan tu thu i cua "prog" la mot phep toan (i<0) thi kiem tra 2 phan
	 * tu ngay truoc i. Neu 2 phan tu nay la hang so thi thuc hien phep toan va
	 * tra ve ket qua
	 * 
	 * @param prog
	 * @param i
	 * @param constant
	 * @return
	 */
	public double[] previous(ArrayList<Integer> prog, int i, double[] constant) {
		double[] result = new double[2];
		if ((prog.get(i - 2) >= 0 && prog.get(i - 2) < 0x3FFFFFFF)
				&& (prog.get(i - 3) >= 0 && prog.get(i - 3) < 0x3FFFFFFF)) {
			result[0] = 1;
			double a = constant[prog.get(i - 2)];
			double b = constant[prog.get(i - 3)];
			switch (prog.get(i - 1)) {
			case -1:
				result[1] = a + b;
				break;
			case -2:
				result[1] = b - a;
				break;
			case -3:
				result[1] = a * b;
				break;
			case -4:
				result[1] = b / a;
				break;
			case -5:
				result[1] = Math.pow(b, a);
				break;
			}
		} else
			result[0] = 0;
		return result;
	}

	/**
	 * Thiet lap da thuc
	 */
	private void setPolynomial() {
		parser(exprog.prog);
		int index_Of_arrStackOfInt = arrStackOfInt.size();
		for (int i = 0; i < index_Of_arrStackOfInt; i++) {
			parserMonomial(arrStackOfInt.get(i));
		}
		for (int i = 0; i < tempPolynomial.size() - 1; i++) {
			for (int j = i + 1; j < tempPolynomial.size(); j++) {
				if (MathPolynomial.compare(tempPolynomial.get(i),
						tempPolynomial.get(j))) {
					tempPolynomial.get(i).factor += tempPolynomial.get(j).factor;
					tempPolynomial.remove(j);
					j--;
				}
			}
		}
		polynomial = tempPolynomial;
	}

	/**
	 * Phan tich mang so nguyen luu tru thong tin cua don thuc de tim ra cac
	 * thanh phan (he so, cac bien va luy thua) cua don thuc
	 * 
	 * @param stackInt
	 */
	private void parserMonomial(StackOfInt stackInt) {
		if (stackInt.data[0] != -1 && stackInt.data[0] != -2) {
			double factor = 1; // he so cua don thuc
			ArrayList<Variable> arrVariable = new ArrayList<Variable>();
			if (stackInt.size() == 1) { // neu mang co mot phan tu
				int temp = stackInt.data[0];
				if (temp >= 0x3FFFFFFF) {
					arrVariable.add(new Variable());
					String name = "" + exprog.command[temp - 0x3FFFFFFF];
					setVar(arrVariable.get(arrVariable.size() - 1), 1, name);
				} else {
					if (temp >= 0) {
						factor = exprog.constant[temp];
					}
				}
			} else {
				for (int j = 0; j < stackInt.size(); j++) {
					int temp = stackInt.data[j];
					if (temp >= 0x3FFFFFFF) { // neu temp la mot bien
						arrVariable.add(new Variable());
						int next = getNext(stackInt.data, j);
						if (next < 0 || next >= 0x3FFFFFFF) {
							String name = ""
									+ exprog.command[stackInt.data[j] - 0x3FFFFFFF];
							setVar(arrVariable.get(arrVariable.size() - 1), 1,
									name);
						} else { // la constant
							if (getNext(stackInt.data, j + 1) == -5) {
								double exponent = exprog.constant[stackInt.data[j + 1]];
								String name = ""
										+ exprog.command[stackInt.data[j] - 0x3FFFFFFF];
								setVar(arrVariable.get(arrVariable.size() - 1),
										exponent, name);
							}
						}
					} else {
						if (temp >= 0 && getNext(stackInt.data, j) != -5) {
							factor *= exprog.constant[temp];
						}
						if (temp == -15) {
							factor = -factor;
						}
					}
				}
			}
			tempPolynomial.add(new Monomial(factor, arrVariable));
		} else {
			if (stackInt.size() == 1 && stackInt.data[0] == -2) {
				int index = tempPolynomial.size();
				tempPolynomial.get(index - 1).factor = tempPolynomial
						.get(index - 1).factor * (-1);
			}
		}
	}

	/**
	 * Phan tich mang prog de tim cac don thuc
	 * 
	 * @param prog
	 */
	private void parser(int[] prog) {
		StackOfInt temp = new StackOfInt();
		for (int i = 0; i < prog.length; i++) {
			if (prog[i] != -3 && prog[i] != -4 && prog[i] != -5
					&& prog[i] != -15) {
				arrStackOfInt.add(new StackOfInt());
				arrStackOfInt.get(arrStackOfInt.size() - 1).push(prog[i]);
			} else {
				if (prog[i] == -15) {
					arrStackOfInt.get(arrStackOfInt.size() - 1).push(prog[i]);
				} else {
					temp.push(prog[i]);
					for (int j = 0; j < 2; j++) {
						pushData(temp,
								arrStackOfInt.get(arrStackOfInt.size() - 1));
					}
					arrStackOfInt.remove(arrStackOfInt.size() - 1);
				}
			}
			int sizeTemp = temp.size();
			for (int k = 0; k < sizeTemp; k++) {
				arrStackOfInt.get(arrStackOfInt.size() - 1).push(temp.pop());
			}
		}
	}

	/**
	 * Hien thi da thuc duoi dang chuoi
	 */
	public String toString() {
		String string_result = "";
		for (int i = 0; i < polynomial.size(); i++) {
			if (polynomial.get(i).factor != 0) {
				if (polynomial.get(i).factor < 0)
					string_result += polynomial.get(i).convertToString();
				else {
					if (i != 0)
						string_result += "+"
								+ polynomial.get(i).convertToString();
					else
						string_result += polynomial.get(i).convertToString();
				}
			}
		}
		// System.out.print("" + string_result + "\n");
		return string_result;
	}

	/**
	 * Sao chep du lieu tu stack array sang stack temp
	 * 
	 * @param temp
	 * @param array
	 */
	private void pushData(StackOfInt temp, StackOfInt array) {
		int size = array.size();
		for (int i = 0; i < size; i++) {
			int a = array.pop();
			temp.push(a);
		}
	}

	/**
	 * Lay ra phan tu tiep theo trong mang
	 * 
	 * @param array
	 * @param index
	 * @return
	 */
	private int getNext(int[] array, int index) {
		index += 1;
		int i = array[index];
		return i;
	}

	/**
	 * @param variable
	 * @param exponent
	 * @param name
	 */
	private void setVar(Variable variable, double exponent, String name) {
		variable.setName(name);
		variable.setExponent(exponent);
	}
}
