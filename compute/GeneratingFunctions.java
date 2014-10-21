package pgc.compute;

import pgc.data.Variable;

public class GeneratingFunctions {

	/**
	 * F(z) = MAXF(F1(z); F2(z)) tuong ung [z^i]F(z)= MAX([z^i]F1(z),[z^i]F2(z))
	 * voi moi i>=0 Vi du: MAXF(1+z+z^2, 1+2z^2) = 1+z+2z^2
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Polynomial maxF(Polynomial p1, Polynomial p2) {
		Polynomial result = new Polynomial();
		boolean[] check_p2 = new boolean[p2.polynomial.size()];
		for (int i = 0; i < p1.polynomial.size(); i++) {
			boolean check_p1 = false;
			Monomial m1 = p1.polynomial.get(i);
			for (int j = 0; j < p2.polynomial.size(); j++) {
				Monomial m2 = p2.polynomial.get(j);
				if (MathPolynomial.compare(m1, m2)) {
					result.polynomial.add(findMax(m1, m2));
					check_p1 = true;
					check_p2[j] = true;
				}
			}
			if (!check_p1 && m1.getFactor() > 0)
				result.polynomial.add(m1);
		}
		for (int i = 0; i < check_p2.length; i++) {
			if (!check_p2[i] && p2.polynomial.get(i).getFactor() > 0)
				result.polynomial.add(p2.polynomial.get(i));
		}
		return result;
	}

	/**
	 * Tra ve don thuc co he so lon hon trong hai don thuc dong dang
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	private static Monomial findMax(Monomial m1, Monomial m2) {
		Monomial result_ = new Monomial();
		if (m1.factor >= m2.factor)
			result_ = m1;
		else
			result_ = m2;
		return result_;
	}

	/**
	 * F(z) = MINF(F1(z); F2(z)) tuong ung [z^i]F(z)= MIN([z^i]F1(z),[z^i]F2(z))
	 * voi moi i >= 0 Vi du: MINF(1+z+z^2, 1+2z^2) = 1+z^2
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Polynomial minF(Polynomial p1, Polynomial p2) {
		// Tinh toan toi uu
		Polynomial result = new Polynomial();
		boolean[] check_p2 = new boolean[p2.polynomial.size()];
		for (int i = 0; i < p1.polynomial.size(); i++) {
			Monomial m1 = p1.polynomial.get(i);
			boolean check_p1 = false;
			for (int j = 0; j < p2.polynomial.size(); j++) {
				Monomial m2 = p2.polynomial.get(j);
				if (MathPolynomial.compare(m1, m2)) {
					result.polynomial.add(findMin(m1, m2));
					check_p1 = true;
					check_p2[j] = true;
				}
			}
			if (!check_p1 && m1.getFactor() < 0)
				result.polynomial.add(m1);
		}
		for (int i = 0; i < check_p2.length; i++) {
			if (!check_p2[i] && p2.polynomial.get(i).getFactor() < 0)
				result.polynomial.add(p2.polynomial.get(i));
		}
		return result;
	}

	/**
	 * Tra ve don thuc co he so nho hon trong hai don thuc dong dang
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	private static Monomial findMin(Monomial m1, Monomial m2) {
		Monomial result = new Monomial();
		if (m1.factor <= m2.factor)
			result = m1;
		else
			result = m2;
		return result;
	}

	/**
	 * DEDUP(F(z)) = ∑ z^i. [z^i]F(z)>0 Ham DEDUP se tra ve mot da thuc moi ma
	 * trong do cac don thuc co he so la 1 neu [z^i]F(z)>0 VD: DEDUP(G(z)) =
	 * 1+z+z^2+z^3+ .. = 1/(1 − z)
	 * 
	 * @param poly
	 * @return
	 */
	public static Polynomial dedup(Polynomial poly) {
		Polynomial result = new Polynomial();
		for (int i = 0; i < poly.polynomial.size(); i++) {
			if (poly.polynomial.get(i).factor > 0) {
				poly.polynomial.get(i).setFactor(1);
				result.polynomial.add(poly.polynomial.get(i));
			}
		}
		return result;
	}

	/**
	 * Ham su dung de loai bo cac don thuc co chua bien "var" va so mu cua bien
	 * "var" lon hon limit VD: (p1) - F(x,y)=2*x^(2+4)+3y^3-4x^2-1 => trunc(p1,
	 * x, 3) = 3y^3-4x^2-1
	 * 
	 * @param p1
	 * @param var
	 * @param limit
	 * @return
	 */
	public static Polynomial trunc(Polynomial p1, Variable var, int limit) {
		Polynomial result = new Polynomial();
		for (int i = 0; i < p1.polynomial.size(); i++) {
			Monomial m1 = p1.polynomial.get(i);
			int check = m1.dependOn(var);
			if (check != -1) {
				if (m1.arrVariable.get(check).getExponent() <= limit)
					result.polynomial.add(m1);
			} else {
				result.polynomial.add(m1);
			}
		}
		return result;
	}

}
