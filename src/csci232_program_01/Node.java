package csci232_program_01;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;

/**
 * List element in the Priority Queue, stores a letter and frequency of the
 * letter and is repurposed as leafs in the huffman tree later on.
 *
 * @author Cory Johns, Jistin Keeling, Alex Harry
 * @version 01.27.2018
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
     * Prints the node's descriptive information to the given writer
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
     * Prints the node's descriptive information to the given writer
     *
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

    public void assignCode(Node parent, String num) {
        if (parent.getCode() != null) {
           this.code = parent.getCode() + num;

        } else {
            this.code = num;
        }
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
