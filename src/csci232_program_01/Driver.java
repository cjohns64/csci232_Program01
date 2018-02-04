package csci232_program_01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PriorityQueue;

/**
 * Driver for CSCI 232 Program 1 Reads the original message from input.txt and
 * is displays it to the console Displays the frequency table to the console for
 * each letter in the input file Displays the huffman code table to the console
 * Encodes and displays the contents of input.txt as binary to the console
 * Decodes binary message and writes the decode message to output.txt
 *
 * @authors Cory Johns, Justin Keeling, Alex Harry
 * @version 01.28.2018
 */
public class Driver {

    public static void main(String[] args) {
        // define charset
        Charset charset = Charset.forName("US-ASCII");

        // set up file paths
        Path input_path = FileSystems.getDefault().getPath("./input", "input.txt");
        Path output_path = FileSystems.getDefault().getPath("./output", "output.txt");

        // set up a writer that will print to console
        BufferedWriter st_writer = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
        	// set up the reader
            BufferedReader reader = Files.newBufferedReader(input_path, charset);
            // set up a writer that will print to output.txt
            BufferedWriter file_writer = Files.newBufferedWriter(output_path, charset);

            st_writer.write("\tOriginal message:\n");
            String line = null;
            // read in a line and write it to the console
            while ((line = reader.readLine()) != null) {
                st_writer.write(line + "\n");
            }

            // generate the huffman tree, and print the frequency table
            st_writer.write("\tFrequency Table:\n");
            Tree huffman_tree = generate_huffman_tree(input_path, charset, st_writer);

            // encode the tree into binary by following the branches to each letter
            // Display the huffman code table to the console
            st_writer.write("\n\tCode Table: \n");
            generate_encode(huffman_tree, st_writer);
            
            // Encode and display the contents of input.txt as binary to the console
            st_writer.write("\n\tEncoded input file (new lines inserted after 100 chars for readability):\n");
            String binary_code = compress_file(input_path, huffman_tree, st_writer, charset);
            
            // Decode the message from binary back to text by using '0's as a left and '1's as a right
            // Decodes binary message and writes the decode message to output.txt
            file_writer.write(huffman_tree.decode(binary_code));
            
            //huffman_tree.displayTree(st_writer);
            
            // close the writers to update the output
            st_writer.close();
            file_writer.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

    }

    /**
     * Sets all the codes for each letter in the tree
     * @param tree
     * @param writer
     * @throws IOException
     */
    private static void generate_encode(Tree tree, BufferedWriter writer) throws IOException {
        tree.set_codes(writer);
    }

    /**
     * Compresses the input file using the given huffman tree and writes the result to the given writer
     * also returns the output as a string
     * @param file the input file
     * @param code_tree the huffman tree
     * @param writer the buffered writer to use as ouput
     * @param charset
     * @return the output encoded file as a string
     * @throws IOException
     */
    public static String compress_file(Path file, Tree code_tree, BufferedWriter writer, Charset charset) throws IOException {
        String text = "";
        String line;
        Node node;
        
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) { 
        	// read the next line, reader ignores \n chars so we will need to add them specially
            while ((line = reader.readLine()) != null) {

            	// read each char, and find the code
                for (char letter : line.toCharArray()) {
                	// find char in tree
                	node = code_tree.find_letter(letter);
                	// check that it was found
                	if (node != null) {
                		// add code to output
                		text += node.code;
                	}
                	else {
                		// error!
                		System.out.println("Letter '" + letter + "' was not found in tree");
                	}
                }
                // end of a line, append ignored \n
                // find char in tree
            	node = code_tree.find_letter('\n');
            	// check that it was found
            	if (node != null) {
            		// add code to output
            		text += node.code;
            	}
            	else {
            		// error!
            		System.out.println("Letter '\\n' was not found in tree");
            	}
                
            }
            // print the result 100 chars at a time
            int i = 0;
        	int line_len = 100;
            try {
            	// this will throw a StringIndexOutOfBoundsException when it can't print another 100 chars
            	while (true) {
            		// write the next 100 chars
            		writer.write(text, i, line_len);
            		// append a new line
            		writer.write("\n");
            		i += line_len;
                }
            } catch (StringIndexOutOfBoundsException e) {
            	// this means we are done, so print the remainder
            	writer.write(text, i, text.length() - i);
            	writer.write("\n");
            }
            
        } catch (IOException xx) {
            System.err.format("IOException: %s%n", xx);
        }
        // return the encoded string
        return text;
    }

    /**
     * Generates a huffman tree for the given text file and prints the letter
     * frequency chart. The generated tree is generated with an aggregation of
     * each character in a linked list and then translated into a priority
     * queue. This is done to allow each character to by dynamically added and
     * to eliminate duplicates (adding to frequency instead).
     *
     * @param input_path the path of the input file
     * @param charset the character set of the input file
     * @param writer the output to send all print statements to
     * @return the finished huffman tree or null if there was an error
     */
    private static Tree generate_huffman_tree(Path input_path, Charset charset, BufferedWriter writer) {
        // make a new priority queue, the nodes will compare frequencies directly thanks to a compare @Override
        Node comparer = new Node('\0');// null character
        PriorityQueue<Node> frequency_queue = new PriorityQueue<Node>(comparer);

        // read in the input file line by line to get the letter frequency and put it in a linked list
        // the linked list is achieved by using only the right child branches of the nodes
        Node head = null;
        Node last = null;
        try (BufferedReader reader = Files.newBufferedReader(input_path, charset)) {
            String line = null;
            // read in a line
            while ((line = reader.readLine()) != null) {
                // split line into chars
                for (char letter : line.toCharArray()) {
                    // no head yet, make one
                    if (head == null) {
                        head = new Node(letter);
                        last = head;
                    } // check if letter is already in the list
                    else if (contains(letter, head)) {
                        getElement(letter, head).frequency++;
                    } // not in list, add it
                    else {
                        last.rightChild = new Node(letter);
                        last = last.rightChild;
                    }

                }
                // buffered reader ignores the trailing '\n' char
                if (contains('\n', head)) {
                    getElement('\n', head).frequency++;
                } else {
                    last.rightChild = new Node('\n');
                    last = last.rightChild;
                }
            }

            // transfer linked list to priority queue
            Node current = head;
            while (current != null) {
                // add node to queue
                frequency_queue.add(current);
                // clear right child
                Node tmp = current.rightChild;
                current.rightChild = null;
                // move head to next node in the list
                current = tmp;
            }

            // make the huffman tree
            // grab the two lowest frequency nodes, set them as the children of a new node,
            // put the new node back in the que and repeat until there are no more nodes
            Node first = frequency_queue.poll();
            Node second;
            while ((second = frequency_queue.poll()) != null) {
                // make internal node with frequency equal to the sum of its children
                Node tmp = new Node(first.frequency + second.frequency);

                // set new node as parent of both selected nodes
                tmp.leftChild = first;
                tmp.rightChild = second;
                // put new node group back in queue
                frequency_queue.add(tmp);

                // print part of the frequency chart
                first.printNode(writer, false);
                second.printNode(writer, false);

                // pull lowest frequency node out of queue
                first = frequency_queue.poll();
            }

            // return the tree object
            return new Tree(first);

        } catch (IOException x) {
            System.out.println("IOException: " + x);
        }
        return null;
    }

    /**
     * gets the element in the given linked list with the given letter
     *
     * @param letter the letter to look for
     * @param head start of the linked list to search
     * @return the element containing the letter, or null if it is not in the
     * list
     */
    private static boolean contains(char letter, Node head) {
        Node current = head;
        while (current != null) {
            if (current.letter == letter) {
                return true;
            }
            current = current.rightChild;
        }
        return false;
    }

    /**
     * gets the element in the given linked list with the given letter
     *
     * @param letter the letter to look for
     * @param head start of the linked list to search
     * @return the element containing the letter, or null if it is not in the
     * list
     */
    private static Node getElement(char letter, Node head) {
        Node current = head;
        while (current != null) {
            if (current.letter == letter) {
                return current;
            }
            current = current.rightChild;
        }
        return null;
    }

}
