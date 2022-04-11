package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	public static void main(String args[]) {
		BufferedReader reader;
		FileWriter writer;
		try {
			reader = new BufferedReader(new FileReader(
					"testInput.txt"));
			writer = new FileWriter("out.java");
			
			String line = reader.readLine();
			while(line != null) {
				System.out.println(line);
				writer.write(line);
				line = reader.readLine();
			}
			reader.close();
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
