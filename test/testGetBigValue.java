package pgc.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import pgc.data.ExpressionProgram;
import pgc.data.Parser;
import pgc.data.Variable;

public class testGetBigValue {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Variable x= new Variable("x", new BigDecimal("9788712165345097346587526345945654676"));
		Variable y= new Variable("y",24);
		Parser parser = new Parser();
		parser.add(x);
		parser.add(y);
		String str = "y*x";
		ExpressionProgram exprog = parser.parse(str);
		assertEquals(new BigDecimal("234929091968282336318100632302695712224"),exprog.getBigValue());
	}

}
