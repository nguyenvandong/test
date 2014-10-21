package pgc.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pgc.compute.Polynomial;
import pgc.data.Variable;

public class testPolynomial {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPolynomialStringVariable() {
		Variable var=new Variable("var");
		String str = "var+2*var^2+3+4*var-5var^2+7";
		Polynomial p1=new Polynomial(str, var);
		Assert.assertEquals("5.0*var-3.0*var^2.0+10.0", p1.toString());
	}

	@Test
	public void testPolynomialStringVariableArray() {
		Variable[] var = new Variable[2];
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		String str1 = "3*x*y^4+2*x^(2+3*2)*3*y^3*3+ 2*y^(2+2)*x+2*(4+3)+4+5";
		Polynomial p1 = new Polynomial(str1, var);
		Assert.assertEquals("5.0*x*y^4.0+18.0*x^8.0*y^3.0+23.0", p1.toString());
	}

}
