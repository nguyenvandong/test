package pgc.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pgc.compute.TaylorSeries;
import pgc.data.ExpressionProgram;
import pgc.data.Variable;

public class testTaylorSeries {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSeies() {
		String exp = "cosx";
		Variable var = new Variable("x");
		ExpressionProgram ex = TaylorSeries.seies(exp, var, 0, 4);
		Assert.assertEquals("1.0+(-1/2)*x^2+(1/24)*x^4", ex.toString());
	}
	@Test
	public void testSeies1() {
		String exp = "sinx";
		Variable var = new Variable("x");
		ExpressionProgram ex = TaylorSeries.seies(exp, var, 0, 4);
		Assert.assertEquals("0.0+(1/1)*x+(-1/6)*x^3", ex.toString());
	}
	@Test
	public void testSeies2() {
		String exp = "e^x";
		Variable var = new Variable("x");
		ExpressionProgram ex = TaylorSeries.seies(exp, var, 0, 4);
		Assert.assertEquals("1.0+(1/1)*x+(1/2)*x^2+(1/6)*x^3+(1/24)*x^4", ex.toString());
	}

}
