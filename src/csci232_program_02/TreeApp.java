package csci232_program_02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Authors:  Cory Johns, Justin Keeling, Alex Harry
 * Date: 2/14/2018
 * Overview:
 */
public class TreeApp {
	public static void main(String[] args) throws IOException {
		// define charset
		Charset charset = Charset.forName("US-ASCII");

		// set up file path
		Path input_path = FileSystems.getDefault().getPath("./input", "input_lab2.txt");

		// st_writer prints to console
		BufferedWriter st_writer = new BufferedWriter(new OutputStreamWriter(System.out));

		// add tree object
		Tree theTree = new Tree();

		try (BufferedReader reader = Files.newBufferedReader(input_path, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String command = "";

				if (line.indexOf(" ") != -1) {
					command = line.substring(0, line.indexOf(" "));
				}
				else {
					command = line.replaceAll("\n", "");
				}


				switch (command) {
					case "insert":
						String insertionNodes = line.substring(line.indexOf(" ") + 1);
						// print insertion nodes
						st_writer.write("Inserting: " + insertionNodes + "\n\n");
						// Actually insert the nodes
						insertFunction(insertionNodes, theTree);
						break;

					case "find":
						// get the code
						int code = getCode(line);
						// find the node
						Node found = theTree.find(code);
						if (found != null)
							// print the ode!
							st_writer.write("Found: " + found.displayNode() + "\n\n");
						else {
							st_writer.write(code + " not found!\n\n");
						}
						break;

					case "delete":
						code = getCode(line);
						//boolean deleted = theTree.delete(code);
						// TODO test this
					/*	boolean deleted = theTree.delete(code);
						if (deleted)
							st_writer.write("Deleted: " + code + "\n");
						else {
							st_writer.write(code + " not found!\n");
						}*/
						break;

					case "traverse":
						code = getCode(line);
						theTree.traverse(code, st_writer);
						st_writer.write("\n");
						break;
					case "min":
						Node min = theTree.findMin();
						st_writer.write("\nMin: " + min.displayNode() + "\n\n");
						break;
					case "max":
						Node max = theTree.findMax();
						st_writer.write("Max: " + max.displayNode() + "\n\n");
						break;
					case "show":
						theTree.display_tree(st_writer);
						break;
					default:
						st_writer.write("Invalid command!");
				}
			}
		} catch (IOException x) {
			System.out.println("IOException: " + x);
		}
		st_writer.close();

	} // end main()

	private static void insertFunction(String nodes, Tree tree) {
		// make an array of key codes in string format
		String[] strCodes = nodes.split(",");

		// insert each key in order, but don't bother if it is to short
		for (String tmp : strCodes) {
			if (tmp.length() > 0) {
				tree.insert(Integer.parseInt(tmp), Integer.parseInt(tmp) + 0.9);
			}
		}

	}

	private static int getCode(String line) {
		int spaceIndex = line.indexOf(" ");

		return Integer.parseInt(line.substring(spaceIndex + 1, line.length()));
	}


	private static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}

	private static int getChar() throws IOException {
		String s = getString();
		return s.charAt(0);
	}

	private static int getInt() throws IOException {
		String s = getString();
		return Integer.parseInt(s);
	}
}
