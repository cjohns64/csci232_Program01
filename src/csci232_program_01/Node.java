package csci232_program_01;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;

/**
 * Authors: Justin Keeling, Cory Johns, Alex Harry 
 * Date: 2/4/2018 
 * Overview:
 * List element in the Priority Queue & nodes in the huffman tree,
 * stores a letter and frequency of the letter and 
 * is repurposed as leafs in the huffman tree later on.
 */
public class Node implements Comparator<Node> {

    public char letter;
    public int frequency;
    public Node leftChild;
    public Node rightChild;
    public String code;

    /**
     * Constructor for a known letter
     *
     * @param letter
     */
    public Node(char letter) {
        this.letter = letter;
        this.frequency = 1;
    }

    /**
     * Constructor for a known frequency
     *
     * @param frequency
     */
    public Node(int frequency) {
        this.letter = '\0';
        this.frequency = frequency;
    }
    public char getLetter(){
        return letter;
    }

    /**
     * Prints the node's descriptive information and frequency to the given writer
     *
     * @param writer
     * @throws IOException
     */
    public void printNode(BufferedWriter writer) throws IOException {
        String printletter;
        if (letter == '\n') {
            printletter = "\\n";
        } else {
            printletter = "" + letter;
        }
        writer.write("{'" + printletter + "', " + frequency + " }");
    }

    /**
     * Prints the node's descriptive information and frequency to the given writer
     * will not print nodes with letter values of '\0' print_null_nodes is false
     * @param writer
     * @param print_null_nodes will not print nodes with letter values of '\0'
     * if false
     * @throws IOException
     */
    public void printNode(BufferedWriter writer, boolean print_null_nodes) throws IOException {
        String printletter;
        if (print_null_nodes || (!print_null_nodes && letter != '\0')) {
            if (letter == '\n') {
                printletter = "\\n";
            } else {
                printletter = "" + letter;
            }
            writer.write("{'" + printletter + "', " + frequency + " }\n");
        }
    }

    /**
     * prints the node's descriptive information and huffman code to the given writer
     * @param writer
     * @throws IOException
     */
    public void printCode(BufferedWriter writer) throws IOException {
        String printletter;
        if (letter == '\n') {
            printletter = "\\n";
        } else {
            printletter = "" + letter;
        }
        writer.write("{'" + printletter + "', " + getCode() + " }\n");
    }

    public String getCode() {
        return code;
    }

    @Override
    public int compare(Node o1, Node o2) {
        if (o1.frequency < o2.frequency) {
            return -1;
        } else if (o1.frequency > o2.frequency) {
            return 1;
        } else {
            return 0;
        }
    }
}
