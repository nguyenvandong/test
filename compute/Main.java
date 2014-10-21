package pgc.compute;

import pgc.data.BinaryTree;
import pgc.data.ExpressionProgram;
import pgc.data.Parser;
import pgc.data.Variable;


public class Main {

	public static void main(String[] args) {
		/*Variable x = new Variable("x", 0);
		String exp = "1/(1-2*x)";
		Parser parser =new Parser();
		parser.add(x);
		ExpressionProgram ex = TaylorSeries.seies(exp, x, 0, 12);
		System.out.print(""+ex.toString());
//		ExpressionProgram ex = TaylorSeries.seies(exp, x, 0, 10);
*/		
		Variable x = new Variable("x");
		Variable y = new Variable("y");
		Parser parser = new Parser();
		parser.add(x);
		parser.add(y);
		String str = "1/(1-2*x)";
		ExpressionProgram ex = parser.parse(str);
		BinaryTree tree = new BinaryTree(ex);
		for(int i=0;i<12;i++){
			tree.root = tree.derivative();
		}
		System.out.print(""+ex.toString());
	}
}
