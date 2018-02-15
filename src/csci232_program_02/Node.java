package csci232_program_02;

/**
 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java (2 ed.). Sams, Indianapolis, IN, USA
 */
public class Node {
	public int key;           	// data item (key)
	public double dData;        // data item
	public int height;			// the height of this node
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
	
	/**
	 * Updates the height of the given node given all the children have correct height values or are null.
	 * @author Cory Johns
	 */
	public void update_height() {
		int tmp_height = 0;
		int tmp_left = -1;
		int tmp_right = -1;
		
		// set height of null children to -1
		if (leftChild != null) {
			tmp_left = leftChild.height;
		}
		if (rightChild != null) {
			tmp_right = rightChild.height;
		}
		
		// find highest child
		if (tmp_left > tmp_right) {
			tmp_height = tmp_left;
		}
		else {
			tmp_height = tmp_right;
		}
		
		// update this node's height
		height = tmp_height + 1;
	}
	
	/**
	 * Returns the balance of the node, negative for left heavy
	 * @author Cory Johns
	 * @return int representing balance
	 */
	public int get_balance() {
		int tmp_height = 0;
		int tmp_left = 0;
		int tmp_right = 0;
		
		// set height of null children to 0
		if (leftChild != null) {
			tmp_left = leftChild.height;
		}
		if (rightChild != null) {
			tmp_right = rightChild.height;
		}
		
		// left heavy is negative
		return tmp_right - tmp_left;
	}

	public String displayNode() { // display ourself
		return "" + key + "," + height;
	}
}
