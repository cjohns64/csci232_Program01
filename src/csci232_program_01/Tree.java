package csci232_program_01;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

/**
 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.).
 * Sams, Indianapolis, IN, USA
 */
public class Tree {
    private static String inputString;
    private Node root;                 // first Node of Tree

    public Tree() {                    // constructor
        root = null;                   // no nodes in tree yet
    }

    /**
     * Constructor for a known root
     *
     * @param root
     */
    public Tree(Node root) {
        this.root = root;
    }

    public Node find(int frequency) {      // find node with given frequency
        Node current = root;         // (assumes non-empty tree)
        while (current.frequency != frequency) {          // while no match
            if (frequency < current.frequency) {          // go left?
                current = current.leftChild;
            } else {                              // or go right?
                current = current.rightChild;
            }
            if (current == null) // if no child
            {                                   // didn't find it
                return null;
            }
        }
        return current;                         // found it
    }  //end find()

    /**
     * Finds the minimum key in the tree simply by going left at every
     * opportunity until there are no more nodes
     *
     * @return the node with the minimum key
     * @author Cory Johns
     */
    public Node findMin() {
        Node current = root;
        while (current != null) {
            if (current.leftChild != null) {
                current = current.leftChild;
            } else {
                break;
            }
        }
        return current;
    }

    /**
     * Finds the max key in the tree simply by going right at every opportunity
     * until there are no more nodes
     *
     * @return the node with the maximum key
     * @author Cory Johns
     */
    public Node findMax() {
        Node current = root;
        while (current != null) {
            if (current.rightChild != null) {
                current = current.rightChild;
            } else {
                break;
            }
        }
        return current;
    }

    // modified to take a pre-constructed Node instead of making one
    public void insert(Node newNode) {
        if (root == null) {            // no node in root
            root = newNode;
        } else {                        // root occupied
            Node current = root;      // start at root  
            Node parent;
            while (true) {            // exits internally			
                parent = current;
                if (newNode.frequency < current.frequency) {              // go left?
                    current = current.leftChild;
                    if (current == null) {             // if the end of the line        
                        parent.leftChild = newNode;   // insert on left
                        return;
                    }
                } //end if go left
                else {                                // or go right?
                    current = current.rightChild;
                    if (current == null) // if the end of the line
                    {                                 // insert on right
                        parent.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    } // end insert()

    public boolean delete(int frequency) {             // delete node with given frequency
        Node current = root;                     // (assumes non-empty list)
        Node parent = root;
        boolean isLeftChild = true;

        while (current.frequency != frequency) {           // search for Node
            parent = current;
            if (frequency < current.frequency) {           // go left?
                isLeftChild = true;
                current = current.leftChild;
            } else {                               // or go right?
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null) {                // end of the line,                             
                return false;                    // didn't find it
            }
        }
        //found the node to delete

        //if no children, simply delete it
        if (current.leftChild == null && current.rightChild == null) {
            if (current == root) {              // if root,
                root = null;                    // tree is empty
            } else if (isLeftChild) {
                parent.leftChild = null;        // disconnect
            } // from parent
            else {
                parent.rightChild = null;
            }
        } //if no right child, replace with left subtree
        else if (current.rightChild == null) {
            if (current == root) {
                root = current.leftChild;
            } else if (isLeftChild) {
                parent.leftChild = current.leftChild;
            } else {
                parent.rightChild = current.leftChild;
            }
        } //if no left child, replace with right subtree
        else if (current.leftChild == null) {
            if (current == root) {
                root = current.rightChild;
            } else if (isLeftChild) {
                parent.leftChild = current.rightChild;
            } else {
                parent.rightChild = current.rightChild;
            }
        } else { // two children, so replace with inorder successor
            // get successor of node to delete (current)
            Node successor = getSuccessor(current);

            // connect parent of current to successor instead
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.leftChild = successor;
            } else {
                parent.rightChild = successor;
            }

            //connect successor to current's left child
            successor.leftChild = current.leftChild;
        } // end else two children
        // (successor cannot have a left child)
        return true;              // success
    }// end delete()

    //returns node with next-highest value after delNode
    //goes right child, then right child's left descendants
    private Node getSuccessor(Node delNode) {
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.rightChild;        // go to the right child
        while (current != null) {                 // until no more
            successorParent = successor;          // left children
            successor = current;
            current = current.leftChild;
        }

        if (successor != delNode.rightChild) {    // if successor not right child,
            //make connections
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }

    // modified for general buffered writer instead of System.out.print
    public void traverse(int traverseType, BufferedWriter writer) throws IOException {
        switch (traverseType) {
            case 1:
                writer.write("\nPreorder traversal: ");
                preOrder(root, writer);
                break;
            case 2:
                writer.write("\nInorder traversal: ");
                inOrder(root, writer);
                break;
            case 3:
                writer.write("\nEncoded frequency: \n");
                postOrder(root, writer, "");

                break;
            default:
                writer.write("Invalid traversal type\n");
                break;
        }
        System.out.println();
    }

    // modified for general buffered writer instead of System.out.print
    private void preOrder(Node localRoot, BufferedWriter writer) throws IOException {
        if (localRoot != null) {
            localRoot.printNode(writer);
            writer.write(" ");
            preOrder(localRoot.leftChild, writer);
            preOrder(localRoot.rightChild, writer);
        }
    }

    // modified for general buffered writer instead of System.out.print
    private void inOrder(Node localRoot, BufferedWriter writer) throws IOException {
        if (localRoot != null) {
            inOrder(localRoot.leftChild, writer);
            localRoot.printNode(writer);
            writer.write(" ");
            inOrder(localRoot.rightChild, writer);
        }
    }

    public boolean isLeaf(Node child, String code, BufferedWriter writer) throws IOException {
        if (child.leftChild == null && child.rightChild == null) {
            return true;
        } else {
            return false;
        }
    }

    // modified for general buffered writer instead of System.out.print
// e, o , g, , n, i, \n, l, h, y, 
    private void postOrder(Node localParent, BufferedWriter writer, String code) throws IOException {
        if (!isLeaf(localParent.leftChild, code, writer)) {
            inputString += code;
            postOrder(localParent.leftChild, writer, (code + "0"));
        } else if (isLeaf(localParent.leftChild, code, writer)) {
            code = code.substring(0);
            code = code + "0";
            if (inputString == null){
                inputString = code;
            }
            else {
                inputString += code;
            }

            writer.write(code + " " + localParent.leftChild.getLetter() + "\n");
            localParent.assignCode(localParent.leftChild, code);
            code = code.substring(0, code.length() - 1);
        }
        if (!isLeaf(localParent.rightChild, code, writer)) {
            postOrder(localParent.rightChild, writer, (code + "1"));
        } else if (isLeaf(localParent.rightChild, code, writer)) {
            code = code.substring(0);
            code = code + "1";
            inputString += code;
            writer.write(code + " " + localParent.rightChild.getLetter() + "\n");
            localParent.assignCode(localParent.rightChild, code);
            code = code.substring(0, code.length() - 1);
        }
        //   localParent.printNode(writer);
        //   writer.write(" ");
    }

    // modified for general buffered writer instead of System.out.print
    // and adjusted to use printNode()
    public void displayTree(BufferedWriter writer) throws IOException {
        Stack<Node> globalStack = new Stack<Node>();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        writer.write(".................................................................\n");
        while (isRowEmpty == false) {
            Stack<Node> localStack = new Stack<Node>();
            isRowEmpty = true;

            for (int j = 0; j < nBlanks; j++) {
                writer.write(' ');
            }

            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    temp.printNode(writer);
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);
                    if (temp.leftChild != null
                            || temp.rightChild != null) {
                        isRowEmpty = false;
                    }
                } else {
                    writer.write("--");
                    localStack.push(null);
                    localStack.push(null);
                }

                for (int j = 0; j < nBlanks * 2 - 2; j++) {
                    writer.write(' ');
                }
            } // end while globalStack not empty
            writer.write("\n");
            nBlanks /= 2;
            while (localStack.isEmpty() == false) {
                globalStack.push(localStack.pop());
            } // end while isRowEmpty is false
            writer.write(".................................................................\n");
        } // end displayTree()
    }

    public void printInputCode() {
        System.out.println("input code " + inputString);
    }
}