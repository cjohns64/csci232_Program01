package csci232_program_02;

/**
 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java (2 ed.). Sams, Indianapolis, IN, USA
 */
public class Node {
    public int key;           	// data item (key)
    public double dData;        // data item
    public int height;			// the height of this node
    // do to parent field all assignments to the children must go through setChild
    private Node leftChild;      // this Node's left child
    private Node rightChild;     // this Node's right child
    public Node parent;         // this Node's parent
    // whether this node is to the left or right can be found by comparing keys
    
    public Node(int iData, double dData) {
        this.key = iData;
        this.dData = dData;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    public int getI(){
        return key;
    }

    /**
     * Gets the left child of the current node
     * @return left child
     */
    public Node getLeftChild() {
    	return getChild(true);
    }
    
    /**
     * Gets the right child of the current node
     * @return right child
     */
    public Node getRightChild() {
    	return getChild(false);
    }
    
    /**
     * Gets either the right or the left child of the current node (given by isLeft)
     * @param isLeft true if getting the left child
     * @return left or right child
     */
    public Node getChild(boolean isLeft) {
        if (isLeft) {
            return leftChild;
        }
        else {
            return rightChild;
        }
    }

    /**
     * Sets the left or right child (given by isLeft) of this node to the given value
     * @param isLeft true if it is the left child
     * @param value to set child to
     */
    public void setChild(boolean isLeft, Node value) {
    	// System.out.println(this + ", " + value + ", " + leftChild + ", " + rightChild);
        if (isLeft) {
            leftChild = value;
            if (value != null) leftChild.parent = this;
        }
        else {
            rightChild = value;
            if (value != null) rightChild.parent = this;
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
        int tmp_left = -1;
        int tmp_right = -1;

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
        return "" + key + "-" + height + "-" + get_balance();
    }
}
