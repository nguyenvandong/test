package pgc.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pgc.compute.MathPolynomial;
import pgc.compute.Polynomial;
import pgc.data.Variable;

public class testMultiPolynomial {
	Polynomial p1, p2;
	Variable[] var = new Variable[2];

	@Before
	public void setUp() throws Exception {
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		String str = "3*x*y-3*x^2+5*x-1";
		String str2 = "2*x^2+4*x*y+2";
		p1 = new Polynomial(str, var);
		p2 = new Polynomial(str2, var);
	}

	@Test
	public void testMulti() {
		Polynomial multi = MathPolynomial.multi(p1, p2);
		String result = "6.0*x^3.0*y+12.0*x^2.0*y^2.0+6.0*x*y-6.0*x^4.0-"
				+ "12.0*x^3.0*y-6.0*x^2.0+10.0*x^3.0+20.0*x^2.0*y+"
				+ "10.0*x-2.0*x^2.0-4.0*x*y-2.0";
		assertEquals(result, multi.toString());
	}

}
