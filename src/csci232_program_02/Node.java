package csci232_program_02;

/**
 * 
 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java (2 ed.). Sams, Indianapolis, IN, USA
 */
public class Node {
	public int key;           // data item (key)
	public double dData;        // data item
	public Node leftChild;      // this Node's left child
	public Node rightChild;     // this Node's right child
	
	public Node(int iData, double dData) {
		this.key = iData;
		this.dData = dData;
	}
	
	public Node getChild(boolean isLeft) {
		if (isLeft) {
			return leftChild;
		}
		else {
			return rightChild;
		}
	}
	
	public void setChild(boolean isLeft, Node value) {
		if (isLeft) {
			leftChild = value;
		}
		else {
			rightChild = value;
		}
	}

	public String displayNode() { // display ourself
		return "{" + key + ", " + dData + "} ";
	}
}
