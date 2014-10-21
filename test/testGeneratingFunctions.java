package pgc.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pgc.compute.GeneratingFunctions;
import pgc.compute.Polynomial;
import pgc.data.Variable;

public class testGeneratingFunctions {

	Polynomial p1, p2;
	Variable[] var = new Variable[3];

	@Before
	public void setUp() throws Exception {
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		var[2] = new Variable("z");
		String str = "3*x*y*z-3*x^2+5*x*y-1-x";
		String str2 = "2*x^2+4*x*y*z-y+2";
		p1 = new Polynomial(str, var);
		p2 = new Polynomial(str2, var);
	}

	@Test
	public void testMaxF() {
		Polynomial maxF = GeneratingFunctions.maxF(p1, p2);
		assertEquals("4.0*x*y*z+2.0*x^2.0+5.0*x*y+2.0", maxF.toString());
	}

	@Test
	public void testMinF() {
		Polynomial minF = GeneratingFunctions.minF(p1, p2);
		assertEquals("3.0*x*y*z-3.0*x^2.0-1.0-x-y", minF.toString());
	}

	@Test
	public void testDedup() {
		Polynomial dedup = GeneratingFunctions.dedup(p1);
		assertEquals("x*y*z+x*y", dedup.toString());
	}

	@Test
	public void testTrunc() {
		Polynomial trunc = GeneratingFunctions.trunc(p2, var[0], 1);
		assertEquals("4.0*x*y*z-y+2.0", trunc.toString());
	}

}
