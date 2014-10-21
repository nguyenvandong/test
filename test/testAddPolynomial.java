package pgc.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pgc.compute.MathPolynomial;
import pgc.compute.Polynomial;
import pgc.data.Variable;

public class testAddPolynomial {

	Polynomial p1,p2;
	Variable[] var = new Variable[3];
	@Before
	public void setUp() throws Exception {
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		var[2] = new Variable("z");
	}

	@Test
	public void testAdd0() {
		String str = "3*x*y*z-3*x^2+5*x*y-1";
		String str2 = "2*x^2+4*x*y*z-y+2";
		p1 = new Polynomial(str, var);
		p2 = new Polynomial(str2, var);
		Polynomial sum = MathPolynomial.add(p1, p2);
		assertEquals("7.0*x*y*z-x^2.0+5.0*x*y+1.0-y", sum.toString());
	}

	@Test
	public void testAdd1() {
		String str = "3*y*x*z-3*x^2+5*y*z-1";
		String str2 = "2*x^2+4*z*y*x-y+2";
		p1 = new Polynomial(str, var);
		p2 = new Polynomial(str2, var);
		Polynomial sum = MathPolynomial.add(p1, p2);
		assertEquals("7.0*y*x*z-x^2.0+5.0*y*z+1.0-y", sum.toString());
	}
}
