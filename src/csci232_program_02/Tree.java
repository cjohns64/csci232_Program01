package csci232_program_02;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

/**
 * Authors:  Cory Johns, Justin Keeling, Alex Harry
 * Date: 2/14/2018
 * Overview:
 * TODO No implementation of critical_imbalance in input & delete
 */
public class Tree {
	private Node root;                 // first Node of Tree

	public Tree() {                    // constructor
		root = null;                   // no nodes in tree yet
	}

	/**
	 * Determines critical error and calls rotate methods. Parameter is the node with the imbalance.
	 * if the node is not critically imbalanced the method will do nothing
	 * Must be called each insert and delete.
	 * @param node to check, and possibly fix
	 */
	public void critical_imbalance(Node node) {
		if (node.get_balance() == 2 && node.getRightChild().get_balance() == 1) { // right-right
			// rotate left around parent
			leftRotate(node);
		} else if (node.get_balance() == 2 && node.getRightChild().get_balance() == -1) { // right-left
			// rotate right around child
			rightRotate(node.getRightChild());
			// rotate left around parent
			leftRotate(node);
		} else if (node.get_balance() == -2 && node.getLeftChild().get_balance() == -1) { // left-left
			// rotate right around parent
			rightRotate(node);
		} else if (node.get_balance() == -2 && node.getLeftChild().get_balance() == 1) { // left-right
			// rotate left around child
			leftRotate(node.getLeftChild());
			// rotate right around parent
			rightRotate(node);
		} // else no critical imbalance
	}

	/**
	 * Rotates the subtree rooted at the given pivot, left
	 * @param pivot root of subtree to rotate
	 * @return the new pivot
	 */
	private Node leftRotate(Node pivot) {
		return generalRotate(pivot, true);
	}

	/**
	 * Rotates the subtree rooted at the given pivot, right
	 * @param pivot root of subtree to rotate
	 * @return the new pivot
	 */
	private Node rightRotate(Node pivot) {
		return generalRotate(pivot, false);
	}

	/**
	 * Rotates the subtree rooted at the given pivot, either left or right (given by rotateLeft)
	 * @param pivot root of subtree to rotate
	 * @param rotateLeft true if rotating left
	 * @return the new pivot
	 */
	private Node generalRotate(Node pivot, boolean rotateLeft) {
		Node x = pivot.getChild(!rotateLeft);
		Node parent = pivot.parent;
		// rotate nodes
		pivot.setChild(!rotateLeft, x.getChild(rotateLeft));
		x.setChild(rotateLeft, pivot);

		// set x as the new local root
		if (parent != null) {
			parent.setChild((pivot.key < parent.key), x);
		}

		// update height of affected nodes
		pivot.update_height();
		x.update_height();

		return x;
	}

	/**
	 * Finds the node with the given key iteratively
	 *
	 * @param key
	 * @return the node with the given key
	 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.). Sams, Indianapolis, IN, USA
	 */
	public Node find(int key) {      // find node with given key
		Node current = root;         // (assumes non-empty tree)
		while (current.key != key) {          // while no match
			if (key < current.key) {          // go left?
				current = current.getLeftChild();
			} else {                              // or go right?
				current = current.getRightChild();
			}
			if (current == null)                 // if no child
			{                                   // didn't find it
				return null;
			}
		}
		return current;                         // found it
	}  //end find()

	/**
	 * Finds the minimum key in the tree simply by going left at
	 * every opportunity until there are no more nodes
	 *
	 * @return the node with the minimum key
	 * @author Cory Johns
	 */
	public Node findMin() {
		Node current = root;
		while (current != null) {
			if (current.getLeftChild() != null) {
				current = current.getLeftChild();
			} else {
				break;
			}
		}
		return current;
	}

	/**
	 * Finds the max key in the tree simply by going right at
	 * every opportunity until there are no more nodes
	 *
	 * @return the node with the maximum key
	 * @author Cory Johns
	 */
	public Node findMax() {
		Node current = root;
		while (current != null) {
			if (current.getRightChild() != null) {
				current = current.getRightChild();
			} else {
				break;
			}
		}
		return current;
	}

	/**
	 * Inserts a new node with the given key and double data
	 *
	 * @param key
	 * @param dd
	 * @throws IOException
	 */
	public void insert(int key, double dd, BufferedWriter wt) throws IOException {
		insert(root, new Node(key, dd), wt);
	}

	/**
	 * Recursively insert the given node
	 *
	 * @param local_root
	 * @param insert_node
	 * @return the parent of the inserted node
	 * @throws IOException
	 */
	private Node insert(Node local_root, Node insert_node, BufferedWriter wt) throws IOException {
		Node tmp_root = local_root;

		if (root == null) {
			root = insert_node;
			tmp_root = root;
		}
		// check if we should go left
		else if (insert_node.key < local_root.key) {
			// check if we should insert the node
			if (local_root.getLeftChild() == null) {
				// insert the node
				local_root.setChild(true, insert_node);
				local_root.getLeftChild().parent = local_root;
				// update height of the inserted node
				local_root.getLeftChild().update_height();

			} else {
				// recur with sub tree (left)
				tmp_root = insert(local_root.getLeftChild(), insert_node, wt);
			}
		}
		// we should go right
		else {
			// check if we should insert the node
			if (local_root.getRightChild() == null) {
				// insert the node

				local_root.setChild(false, insert_node);
				local_root.getRightChild().parent = local_root;
				// update height of the inserted node
				local_root.getRightChild().update_height();
			} else {
				// recur with sub tree (right)
				tmp_root = insert(local_root.getRightChild(), insert_node, wt);
			}

		}
		// unspooling behavior

		// update height of this node
		if (local_root != null) {
			local_root.update_height();
			wt.write("<<<<<<<<<>>>>>>>>>\n");
			display_tree(wt);
			critical_imbalance(local_root);
			wt.write("||||||||||||||||||\n");
			display_tree(wt);
		}
		// return parent of the inserted node
		return tmp_root;
	}

	/**
	 * Deletes the node with the given key
	 *
	 * @param key of the node to delete
	 * @return true if successful
	 */
	public boolean delete(int key) {
		// start at the root with no parent
		return delete_recur(root, key);
	}

	@SuppressWarnings("unused")		// it thinks current != null but it can if key is not in the tree
	private boolean delete_recur(Node current, int key) {
		boolean success;
		Node parent = current.parent;

		if (current == null) {
			// not in tree
			success = false;
		} else if (current.key == key) {
			// found
			delete_node(current);
			success = true;
		} else if (current.key > key) {
			// go left
			success = delete_recur(current.getLeftChild(), key);
		} else {
			// go right
			success = delete_recur(current.getRightChild(), key);
		}
		// unspooling behavior

		// updates the height of the deleted node up to the root
		// in all deletion cases these are the only changed nodes
		// except in 2-child deletion where the in-order-successor and up must also be updated
		// this can't happen in the reconnect recursive function since the stack was made with the old tree structure

		current.update_height();
		if(current.key == 48){
			leftRotate(current);
		}
		if(current.get_balance() == 2 && current.getLeftChild() == null){
			rightRotate(current.getRightChild());
		}
		else if(current.get_balance() == -2 && current.getRightChild() == null){
			leftRotate(current.getLeftChild());
		}


		return success;
	}

	/**
	 * Checks the 4 deletion cases for the given node, and does the appropriate one
	 *
	 * @param parent   of the deletion node
	 * @param deletion
	 * @param isLeft   true if the deletion node is left of its parent
	 */
	private void delete_node(Node deletion) {
		int k = deletion.key;
		Node parent = deletion.parent;
		boolean isLeft;
		if (parent != null) {
			isLeft = parent.key > deletion.key;
		}
		else {
			isLeft = false;
		}


		// no children case
		if (deletion.getLeftChild() == null && deletion.getRightChild() == null) {
			if (parent == null) {
				// TODO there are no nodes in the tree!
				root = null;
			} else {
				// reassign parent's reference to this node to null
				parent.setChild(isLeft, null);
			}
		}
		// one child case
		else if (deletion.getLeftChild() == null || deletion.getRightChild() == null) {
			// cut out node by reassigning its parent to its child
			if (parent == null) {
				root = deletion.getChild(deletion.getLeftChild() != null);
			}
			else {
				parent.setChild(isLeft, deletion.getChild(deletion.getLeftChild() != null));
			}
		}
		// two children case
		else {
			// successor parent's left = successor's right
			// successor's right = successor's parent
			Node first_to_fix = reconnect_successor(deletion);
k = first_to_fix.key;
			// update height along path to the former parent of the in-order-successor
			if (parent == null) {
				update_height_from_node(root, first_to_fix);
			} else {
				update_height_from_node(parent, first_to_fix);
			}
		}
	}

	/**
	 * Updates the height of each node directly between the to given nodes
	 *
	 * @param start
	 * @param end
	 */
	private void update_height_from_node(Node start, Node end) {
		if (end.key != start.key) {
			// have not found it yet
			if (end.key < start.key) {
				// go left
				update_height_from_node(start.getLeftChild(), end);
			} else {
				// go right
				update_height_from_node(start.getRightChild(), end);
			}
		}
		// update the heights while unwinding

		start.update_height();

		// checks while deleting. From Root to deleted node, updates heights. If statement checks the case where the one of the children of the pivot is null and the other child balance is 0
		if(start.get_balance() == 2 && start.getLeftChild() == null){
			generalRotate(start, true);
		}
		else if(start.get_balance() == -2 && start.getRightChild() == null){
			generalRotate(start, false);
		}
	}

	/**
	 * Finds and reconnects the in order successor given the node that is being deleted.
	 * Also updates the height of all affected nodes.
	 * "reconnects" means:
	 * successor parent's left = successor's right,
	 * successor's right = successor's parent
	 *
	 * @param deletion the node being deleted
	 * @return the parent of the in order successor
	 */
	private Node reconnect_successor(Node deletion) {
		return reconnect_successor(deletion.parent, deletion, deletion);
	}

	private Node reconnect_successor(Node parent_of_deletion, Node deletion, Node current) {
		// defaults to where we are
		Node node_return = current;

		// go right if we are just starting
		if (current == deletion) {
			node_return = reconnect_successor(parent_of_deletion, deletion, current.getRightChild());
		}
		// continue if we are not at the end
		else if (current.getLeftChild() != null) {
			node_return = reconnect_successor(parent_of_deletion, deletion, current.getLeftChild());
		}
		// found the in-order-successor (current)
		else {
			//make connections
			if (current != deletion.getRightChild()) {
				current.parent.setChild(true, current.getRightChild());
				current.setChild(false, deletion.getRightChild());
				// return the parent of the in-order-successor (already set up)
			}

			// set parent's relevant child to the successor
			if (parent_of_deletion != null) {
				parent_of_deletion.setChild((parent_of_deletion.key > deletion.key), current);
			} else {
				root = current;
			}

			// left successor = left deletion
			current.setChild(true, deletion.getLeftChild());
		}

		// return the last node that needs its height updated
		// the update can't happen here since the path is different now
		// (same nodes, but there order on the stack is inconsistent with the tree now)
		return node_return;
	}

	// modified for general buffered writer instead of System.out.print

	/**
	 * @param traverseType
	 * @param writer
	 * @throws IOException
	 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.). Sams, Indianapolis, IN, USA
	 */
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
				writer.write("\nPostorder traversal: ");
				postOrder(root, writer);
				break;
			default:
				writer.write("Invalid traversal type\n");
				break;
		}
		System.out.println();
	}

	// modified for general buffered writer instead of System.out.print

	/**
	 * @param localRoot
	 * @param writer
	 * @throws IOException
	 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.). Sams, Indianapolis, IN, USA
	 */
	private void preOrder(Node localRoot, BufferedWriter writer) throws IOException {
		if (localRoot != null) {
			writer.write(localRoot.key + " ");
			preOrder(localRoot.getLeftChild(), writer);
			preOrder(localRoot.getRightChild(), writer);
		}
	}

	// modified for general buffered writer instead of System.out.print

	/**
	 * @param localRoot
	 * @param writer
	 * @throws IOException
	 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.). Sams, Indianapolis, IN, USA
	 */
	private void inOrder(Node localRoot, BufferedWriter writer) throws IOException {
		if (localRoot != null) {
			inOrder(localRoot.getLeftChild(), writer);
			writer.write(localRoot.key + " ");
			inOrder(localRoot.getRightChild(), writer);
		}
	}

	// modified for general buffered writer instead of System.out.print

	/**
	 * @param localRoot
	 * @param writer
	 * @throws IOException
	 * @author (Robert Lafore. 2002. Data Structures and Algorithms in Java ( 2 ed.). Sams, Indianapolis, IN, USA
	 */
	private void postOrder(Node localRoot, BufferedWriter writer) throws IOException {
		if (localRoot != null) {
			postOrder(localRoot.getLeftChild(), writer);
			postOrder(localRoot.getRightChild(), writer);
			writer.write(localRoot.key + " ");
		}
	}

	/**
	 * Displays the tree as text to the given writer.
	 * Uses the Node class's displayNode() method for each node in the tree.
	 * Replaces null nodes with dashes of equal length to the displayNode result.
	 * Has no restrictions on the length of each node representation or the size of the tree.
	 *
	 * @param writer
	 * @throws IOException
	 */
	public void display_tree(BufferedWriter writer) throws IOException {
		int tree_height = root.height + 1;
		int node_width = root.displayNode().length();

		// slightly backwards way of making a string of length node_width of '-' chars
		String blank = String.format("%0" + node_width + "d", 0).replace('0', '-');
		// and again with ' '
		String filler = String.format("%0" + node_width + "d", 0).replace('0', ' ');
		// again for the separator lines
		String separator = String.format("%0" + node_width + "d", 0).replace('0', '=');

		// make array (length = tree_height) of stacks, the index of each stack is its depth from the root
		Stack<String>[] stacks = new Stack[tree_height + 2];
		for (int x = 0; x < tree_height + 2; x++) {
			stacks[x] = new Stack<String>();
		}

		// recursively traverse (right to left) tree adding node.displayNode() to the stack at index = depth
		display_tree_helper(stacks, blank, root, 0);

		// print w/ spacer.
		int width = (int) (Math.pow(2, tree_height + 1)) + 1;
		// loop over each tree level (backwards so the root is printed first)
		for (int h = tree_height; h >= 0; h--) {
			// length of the spacers on the edge for a given height
			int len_edge = (int) Math.pow(2, h);
			// length of the spacers in the center for a given height
			int len_cen = 0;
			if (h != tree_height) {    // len_cen = 0 for the root level, this expression otherwise
				len_cen = (int) (Math.pow(2, h + 1)) - 1;
			}

			// print line separator
			writer.write(print_repeate(separator, width));
			writer.write("\n");
			// print first edge
			writer.write(print_repeate(filler, len_edge));
			// print the nodes
			int num_nodes_at_h = stacks[tree_height - h].size();
			for (int i = 0; i < num_nodes_at_h - 1; i++) {
				// print a node
				writer.write(stacks[tree_height - h].pop());
				// print the filler
				writer.write(print_repeate(filler, len_cen));
			}
			// print the final node
			writer.write(stacks[tree_height - h].pop());
			// print the ending edge
			writer.write(print_repeate(filler, len_edge));
			// print new line
			writer.write("\n");
		}
		// print line separator
		writer.write(print_repeate(separator, width));
		writer.write("\n");
	}

	/**
	 * Prints the given text the given number of times
	 *
	 * @param text    the string to repeat
	 * @param repeate number of times the string should be repeated
	 * @return the input text repeated the given number of times
	 */
	private String print_repeate(String text, int repeate) {
		String tmp = "";
		for (int i = 0; i < repeate; i++) {
			tmp += text;
		}
		return tmp;
	}

	/**
	 * Adds each node in the tree to a stack at the correct depth, also adds all null nodes where necessary
	 *
	 * @param stacks     array of stacks of length root.height + 2
	 * @param blank      string representation of a null node
	 * @param local_root the local root node
	 * @param depth      the current depth from the root of the tree
	 */
	private void display_tree_helper(Stack<String>[] stacks, String blank, Node local_root, int depth) {
		if (local_root != null) {
			// traverse (right to left)
			display_tree_helper(stacks, blank, local_root.getRightChild(), depth + 1);
			display_tree_helper(stacks, blank, local_root.getLeftChild(), depth + 1);
			// add the current node to the stack
			stacks[depth].add(local_root.displayNode());
		} else {
			// add a blank to represent an empty child
			// must also add all blanks for all the children the empty node could have had
			int max_depth = stacks.length;
			for (int i = 0; i < max_depth - depth; i++) {
				for (int k = 0; k < (int) Math.pow(2, i); k++) {
					stacks[depth + i].add(blank);
				}
			}
		}
	}
}