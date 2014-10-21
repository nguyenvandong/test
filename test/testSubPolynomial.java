package pgc.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pgc.compute.MathPolynomial;
import pgc.compute.Polynomial;
import pgc.data.Variable;

public class testSubPolynomial {

	Polynomial p1, p2;
	Variable[] var = new Variable[3];

	@Before
	public void setUp() throws Exception {
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		var[2] = new Variable("z");
		String str = "3*x*y*z-3*x^2+5*x*y-1";
		String str2 = "2*x^2+4*x*y*z-y+2";
		p1 = new Polynomial(str, var);
		p2 = new Polynomial(str2, var);
	}

	@Test
	public void testSub1() {
		Polynomial sub = MathPolynomial.sub(p1, p2);
		assertEquals("-x*y*z-5.0*x^2.0+5.0*x*y-3.0+y", sub.toString());
	}

	@Test
	public void testSub2() {
		Polynomial sub = MathPolynomial.sub(p2, p1);
		assertEquals("5.0*x^2.0+x*y*z-y+3.0-5.0*x*y", sub.toString());
	}

}
