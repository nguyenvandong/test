package pgc.compute;

import java.math.BigDecimal;
import java.util.ArrayList;

import pgc.data.Variable;

public class Monomial {
	public double factor; // he so
	public BigDecimal bigFactor; //he so la BigDemical
	// Mang cac bien
	public ArrayList<Variable> arrVariable = new ArrayList<Variable>();

	/**
	 * ham tao khong tham so
	 */
	public Monomial() {

	}

	/**
	 * Ham khoi tao he so(double) cua don thuc
	 * 
	 * @param factor
	 */
	public Monomial(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Ham tao voi tham so bigFactor
	 * @param bigFactor
	 */
	public Monomial(BigDecimal bigFactor){
		this.bigFactor=bigFactor;
	}

	/**
	 * Ham khoi tao he so va mang cac bien
	 * 
	 * @param factor
	 * @param variable
	 */
	public Monomial(Double factor, ArrayList<Variable> arrVariable) {
		this.factor = factor;
		this.arrVariable = arrVariable;
	}
	
	/**
	 * 
	 * @param bigFactor
	 * @param arrVariable
	 */
	public Monomial(BigDecimal bigFactor, ArrayList<Variable> arrVariable) {
		this.bigFactor = bigFactor;
		this.arrVariable = arrVariable;
	}

	/**
	 * Ham khoi tao mang cac bien
	 * 
	 * @param arrVariable
	 */
	public Monomial(ArrayList<Variable> arrVariable) {
		this.arrVariable = arrVariable;
	}

	/**
	 * Tra ve he so cua don thuc
	 * 
	 * @return
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getBigFactor() {
		return bigFactor;
	}
	/**
	 * Gan he so cho don thuc
	 * 
	 * @param factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}
	
	/**
	 * 
	 * @param factor
	 */
	public void setBigFactor(BigDecimal bigFactor) {
		this.bigFactor = bigFactor;
	}

	/**
	 * Kiem tra xem don thuc co phu thuoc vao bien var hay khong Neu co tra ve
	 * gia tri "true"
	 * 
	 * @param var
	 * @return
	 */
	public int dependOn(Variable var) {
		int check = -1;
		for (int i = 0; i < arrVariable.size(); i++) {
			if (arrVariable.get(i).getName().equals(var.getName()))
				check = i;
		}
		return check;
	}

	/**
	 * Chuyen don thuc sang dang String
	 * 
	 * @return
	 */
	public String convertToString() {
		String uni = "";
		if (arrVariable.size() != 0) {
			for (int i = 0; i < arrVariable.size(); i++) {
				if (i != arrVariable.size() - 1)
					uni += arrVariable.get(i).convertToString() + "*";
				else
					uni += arrVariable.get(i).convertToString();
			}
			if (factor != 1 && factor != -1) {
				uni = factor + "*" + uni;
			} else {
				if (factor == -1)
					uni = "-" + uni;
			}
		} else
			uni += factor;

		return uni;
	}
}
