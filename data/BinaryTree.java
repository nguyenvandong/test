package pgc.data;

import java.util.ArrayList;

public class BinaryTree {

	public Node root;

	public BinaryTree() {
	}

	public BinaryTree(Node root) {
		this.root = root;
	}

	public BinaryTree(ExpressionProgram exprog) {
		int length = exprog.progCt;
		ArrayList<Integer> prog = new ArrayList<Integer>();
		ArrayList<Node> sub_root = new ArrayList<Node>();
		for (int i = 0; i < length; i++) {
			if (exprog.prog[i] < 0 && exprog.prog[i] >= -5) {
				Node temp_subroot = new Node(exprog.prog[i]);
				if (exprog.prog[i - 1] >= 0 && exprog.prog[i - 2] >= 0) {
					temp_subroot.leftChild = new Node(exprog.prog[i - 2]);
					if (exprog.prog[i - 2] < 0x3FFFFFFF)
						temp_subroot.leftChild.value = exprog.constant[exprog.prog[i - 2]];
					temp_subroot.rightChild = new Node(exprog.prog[i - 1]);
					if (exprog.prog[i - 1] < 0x3FFFFFFF)
						temp_subroot.rightChild.value = exprog.constant[exprog.prog[i - 1]];
					for (int j = 0; j < 2; j++) {
						prog.remove(prog.size() - 1);
					}
				} else {
					if (!prog.isEmpty()) {
						temp_subroot.leftChild = new Node(
								prog.get(prog.size() - 1));
						if (temp_subroot.leftChild.index >= 0
								&& temp_subroot.leftChild.index < 0x3FFFFFFF)
							temp_subroot.leftChild.value = exprog.constant[temp_subroot.leftChild.index];
						prog.remove(prog.size() - 1);
						temp_subroot.rightChild = sub_root
								.get(sub_root.size() - 1);
						if (temp_subroot.rightChild.index >= 0
								&& temp_subroot.rightChild.index < 0x3FFFFFFF)
							temp_subroot.rightChild.value = exprog.constant[temp_subroot.rightChild.index];
						sub_root.remove(sub_root.size() - 1);
					} else {
						temp_subroot.rightChild = sub_root
								.get(sub_root.size() - 1);
						sub_root.remove(sub_root.size() - 1);
						temp_subroot.leftChild = sub_root
								.get(sub_root.size() - 1);
						sub_root.remove(sub_root.size() - 1);
					}
				}
				sub_root.add(temp_subroot);
			} else if (exprog.prog[i] == -15) {
				if (!prog.isEmpty()) {
					Node temp = new Node(prog.get(prog.size()-1));
					temp.value = exprog.constant[prog.get(prog.size()-1)];
				}else{
					Node temp = new Node(-3);
					temp.leftChild = new Node(0);
					temp.leftChild.value = -1.0;
					temp.rightChild = sub_root.get(sub_root.size()-1);
					sub_root.remove(sub_root.size()-1);
					sub_root.add(temp);
				}
			} else
				prog.add(exprog.prog[i]);
		}
		root = sub_root.get(sub_root.size() - 1);
	}

	public Node derivative() {
		Node deriv=root.derivativeNode();
		deriv.simplification();
		return deriv;
	}
}
