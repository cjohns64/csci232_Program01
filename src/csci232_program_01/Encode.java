package csci232_program_01;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Justin Keeling
 */
public class Encode extends Node{
    public String code;
    public BufferedWriter writer;
    public String compact = "";
    public Encode(BufferedWriter inWriter ,String inCode, char inLetter){
        super(inLetter);
        code = inCode;
        writer = inWriter;
    }
    @Override
    public String getCode(){
        return code;
    }
     public void printCode() throws IOException {
        String printletter;
        if (letter == '\n') {
            printletter = "\\n";
        } else {
            printletter = "" + super.letter;
        }
        writer.write("{'" + printletter + "', " + getCode() + " }\n");
    }
     public void letter(char letter){
       compact = compact+ super.letter;
     }
}

