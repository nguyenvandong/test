package pgc.data;

public class Node {
	int index;
	Node leftChild;
	Node rightChild;
	Double value;

	public Node() {
		// TODO Auto-generated constructor stub
	}

	public Node(int index) {
		this.leftChild = null;
		this.rightChild = null;
		this.index = index;
	}

	/**
	 * Tinh dao ham cua mot Node
	 * 
	 * @return
	 */
	public Node derivativeNode() {
		Node result = new Node();
		if (index < 0 && index >= -5) {
			switch (index) {
			case -1:
				result.index = -1;
				result.leftChild = this.leftChild.derivativeNode();
				result.rightChild = this.rightChild.derivativeNode();
				break;
			case -2:
				result.index = -2;
				result.leftChild = this.leftChild.derivativeNode();
				result.rightChild = this.rightChild.derivativeNode();
				break;
			case -3:
				result.index = -1;
				result.leftChild = new Node(-3);
				result.leftChild.leftChild = this.leftChild.derivativeNode();
				result.leftChild.rightChild = this.rightChild;
				result.rightChild = new Node(-3);
				result.rightChild.leftChild = this.rightChild.derivativeNode();
				result.rightChild.rightChild = this.leftChild;
				break;
			case -4:
				result.index = -4;
				result.rightChild = new Node(-5);
				result.rightChild.leftChild = this.rightChild;
				result.rightChild.rightChild = new Node(2);
				result.rightChild.rightChild.value = 2.0;

				result.leftChild = new Node(-2);

				result.leftChild.leftChild = new Node(-3);
				result.leftChild.leftChild.leftChild = this.leftChild
						.derivativeNode();
				result.leftChild.leftChild.rightChild = this.rightChild;

				result.leftChild.rightChild = new Node(-3);
				result.leftChild.rightChild.leftChild = this.rightChild
						.derivativeNode();
				result.leftChild.rightChild.rightChild = this.leftChild;
				break;
			case -5:
				result.index = -3;
				result.leftChild = new Node(-3);
				result.leftChild.leftChild = this.rightChild;
				result.leftChild.rightChild = new Node(-5);
				result.leftChild.rightChild.leftChild = this.leftChild;
				result.leftChild.rightChild.rightChild = new Node(0);
				result.leftChild.rightChild.rightChild.value = this.rightChild.value - 1;
				result.rightChild = this.leftChild.derivativeNode();
				break;
			default:
				break;
			}
		} else if (index >= 0 && index < 0x3FFFFFFF) {
			result.index = 0;
			result.value = 0.0;
		} else if (index >= 0x3FFFFFFF) {
			result.index = 0;
			result.value = 1.0;
		}
		return result;
	}

	public void simplification() {
		if (this.index < 0 && this.index >= -5) {
			switch (this.index) {
			case -1:
				if (this.leftChild.index < 0 && this.leftChild.index >= -5)
					this.leftChild.simplification();
				if (this.rightChild.index < 0 && this.rightChild.index >= -5)
					this.rightChild.simplification();
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF
						&& this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					computeConstant(-1);
					break;
				}
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF) {
					if (this.leftChild.value == 0.0) {
						nodeRight();
						break;
					}
				}
				if (this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					if (this.rightChild.value == 0.0) {
						nodeLeft();
					}
				}
				break;
			case -2:
				if (this.leftChild.index < 0 && this.leftChild.index >= -5)
					this.leftChild.simplification();
				if (this.rightChild.index < 0 && this.rightChild.index >= -5)
					this.rightChild.simplification();
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF
						&& this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					computeConstant(-2);
					break;
				}

				// if (this.leftChild.value == 0.0)// can bo xung

				if (this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					if (this.rightChild.value == 0.0) {
						nodeLeft();
					}
				}
				break;
			case -3:
				if (this.leftChild.index < 0 && this.leftChild.index >= -5)
					this.leftChild.simplification();
				if (this.rightChild.index < 0 && this.rightChild.index >= -5)
					this.rightChild.simplification();
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF
						&& this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					computeConstant(-3);
					break;
				}
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF) {
					if (this.leftChild.value == 0.0) {
						node0();
						break;
					} else if (this.leftChild.value == 1.0) {
						nodeRight();
						break;
					}
				}
				if (this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					if (this.rightChild.value == 0.0) {
						node0();
						break;
					} else if (this.rightChild.value == 1.0) {
						nodeLeft();
						break;
					}
				}
				break;
			case -4:
				if (this.leftChild.index < 0 && this.leftChild.index >= -5)
					this.leftChild.simplification();
				if (this.rightChild.index < 0 && this.rightChild.index >= -5)
					this.rightChild.simplification();
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF
						&& this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					computeConstant(-4);
					break;
				}
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF) {
					if (this.leftChild.value == 0.0) {
						node0();
						break;
					}
				}
				if (this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					if (this.rightChild.value == 1.0) {
						nodeLeft();
					}
				}
				break;
			case -5:
				if (this.leftChild.index < 0 && this.leftChild.index >= -5)
					this.leftChild.simplification();
				if (this.rightChild.index < 0 && this.rightChild.index >= -5)
					this.rightChild.simplification();
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF
						&& this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					computeConstant(-5);
					break;
				}
				if (this.leftChild.index >= 0
						&& this.leftChild.index < 0x3FFFFFFF) {
					if (this.leftChild.value == 0.0) {
						node0();
					}
				}
				if (this.rightChild.index >= 0
						&& this.rightChild.index < 0x3FFFFFFF) {
					if (this.rightChild.value == 0.0) {
						this.index = 0;
						this.value = 1.0;
						this.leftChild = null;
						this.rightChild = null;
						break;
					} else if (this.rightChild.value == 1.0) {
						nodeLeft();
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private void node0() {
		this.index = 0;
		this.value = 0.0;
		this.leftChild = null;
		this.rightChild = null;
	}

	private void nodeLeft() {
		this.index = this.leftChild.index;
		this.value = this.leftChild.value;
		this.rightChild = this.leftChild.rightChild;
		this.leftChild = this.leftChild.leftChild;
	}

	private void nodeRight() {
		this.index = this.rightChild.index;
		this.value = this.rightChild.value;
		this.leftChild = this.rightChild.leftChild;
		this.rightChild = this.rightChild.rightChild;
	}

	private void computeConstant(int index) {
		this.index = 0;
		switch (index) {
		case -1:
			this.value = this.leftChild.value + this.rightChild.value;
			this.leftChild = null;
			this.rightChild = null;
			break;
		case -2:
			this.value = this.leftChild.value - this.rightChild.value;
			this.leftChild = null;
			this.rightChild = null;
			break;
		case -3:
			this.value = this.leftChild.value * this.rightChild.value;
			this.leftChild = null;
			this.rightChild = null;
			break;
		case -4:
				this.value = this.leftChild.value / this.rightChild.value;
				this.leftChild = null;
				this.rightChild = null;
			break;
		case -5:
			this.value = Math.pow(this.leftChild.value, this.rightChild.value);
			this.leftChild = null;
			this.rightChild = null;
			break;
		default:
			break;
		}
	}
}
