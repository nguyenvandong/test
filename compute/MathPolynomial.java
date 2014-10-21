package pgc.compute;

import java.util.ArrayList;

import pgc.data.Variable;

public class MathPolynomial {
	public MathPolynomial() {

	}

	/**
	 * Cong hai da thuc
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Polynomial add(Polynomial p1, Polynomial p2) {
		Polynomial sum = new Polynomial();
		sum.polynomial = Compute(p1, p2);
		return sum;
	}

	/**
	 * Tru 2 da thuc
	 */

	public static Polynomial sub(Polynomial p1, Polynomial p2) {
		Polynomial minus = new Polynomial();
		for (int i = 0; i < p2.polynomial.size(); i++) {
			p2.polynomial.get(i).factor *= (-1);
		}
		minus.polynomial = Compute(p1, p2);
		for (int i = 0; i < p2.polynomial.size(); i++) {
			p2.polynomial.get(i).factor *= (-1);
		}
		return minus;
	}

	/**
	 * Nhan hai da thuc
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Polynomial multi(Polynomial p1, Polynomial p2) {
		Polynomial multi = new Polynomial();
		for (int i = 0; i < p1.polynomial.size(); i++) {
			for (int j = 0; j < p2.polynomial.size(); j++) {
				multi.polynomial.add(multiMonomial(p1.polynomial.get(i),
						p2.polynomial.get(j)));
			}
		}
		return multi;
	}

	/**
	 * Nhan hai don thuc
	 * 
	 * @param u1
	 * @param u2
	 * @return
	 */
	public static Monomial multiMonomial(Monomial u1, Monomial u2) {
		Monomial result = new Monomial();
		result.arrVariable = new ArrayList<Variable>();
		result.factor = u1.factor * u2.factor;
		boolean[] check_u2 = new boolean[u2.arrVariable.size()];
		for (int i = 0; i < u1.arrVariable.size(); i++) {
			boolean check = false;
			for (int j = 0; j < u2.arrVariable.size(); j++) {
				if (u1.arrVariable.get(i).name
						.equals(u2.arrVariable.get(j).name)) {
					check = true;
					check_u2[j] = true;
					Variable a = new Variable();
					a.name = u1.arrVariable.get(i).getName();
					a.exponent = u1.arrVariable.get(i).exponent
							+ u2.arrVariable.get(j).exponent;
					result.arrVariable.add(a);
					break;
				}
			}
			if (!check)
				result.arrVariable.add(u1.arrVariable.get(i));
		}
		for (int i = 0; i < u2.arrVariable.size(); i++) {
			if (!check_u2[i])
				result.arrVariable.add(u2.arrVariable.get(i));
		}
		return result;
	}

	/**
	 * Thuc hien phep tinh cong hai da thuc
	 * 
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public static ArrayList<Monomial> Compute(Polynomial poly1, Polynomial poly2) {
		int numOfUni_1 = poly1.polynomial.size();
		int numOfUni_2 = poly2.polynomial.size();
		ArrayList<Monomial> result_poly = new ArrayList<Monomial>();
		boolean[] temp_j = new boolean[numOfUni_2];
		for (int i = 0; i < numOfUni_1; i++) {
			boolean check = false;
			Monomial u1 = poly1.polynomial.get(i);
			for (int j = 0; j < numOfUni_2; j++) {
				Monomial u2 = poly2.polynomial.get(j);
				check = compare(u1, u2);
				if (check) {
					temp_j[j] = true;
					double factor = u1.getFactor() + u2.getFactor();
					result_poly.add(new Monomial(factor, u1.arrVariable));
					break;
				}
			}
			if (!check) {
				Monomial a = new Monomial(poly1.polynomial.get(i).factor,
						poly1.polynomial.get(i).arrVariable);
				result_poly.add(a);
			}
		}
		for (int i = 0; i < temp_j.length; i++) {
			if (!temp_j[i]) {
				Monomial b = new Monomial(poly2.polynomial.get(i).factor,
						poly2.polynomial.get(i).arrVariable);
				result_poly.add(b);
			}
		}
		return result_poly;
	}

	/**
	 * Kiem tra xem hai don thuc co dong dang hay khong
	 * 
	 * @param u1
	 * @param u2
	 * @return
	 */
	public static boolean compare(Monomial u1, Monomial u2) {
		int size1 = u1.arrVariable.size();
		int size2 = u2.arrVariable.size();
		boolean check = false;
		if (size1 == size2) {
			boolean[] flag = new boolean[size1];
			check = true;
			for (int i = 0; i < size1; i++) {
				String varName1 = u1.arrVariable.get(i).getName();
				double varExponent1 = u1.arrVariable.get(i).getExponent();
				for (int j = 0; j < size2; j++) {
					String varName2 = u2.arrVariable.get(j).getName();
					double varExponent2 = u2.arrVariable.get(j).getExponent();
					if (varName1.equals(varName2)
							&& varExponent1 == varExponent2) {
						flag[i] = true;
						break;
					} else
						flag[i] = false;
				}
			}
			for (int i = 0; i < size1; i++) {
				check = check && flag[i];
				if (!check)
					break;
			}
		}
		return check;
	}
}
