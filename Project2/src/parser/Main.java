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
import java.util.HashMap;
import java.util.Scanner;


public class Main {
	
	private static HashMap<String, String> var_map = new HashMap<String, String>();
	
	private static Pattern var_assign = Pattern.compile("([a-zA-Z0-9]+) = (.+)");
	private static Pattern cmd = Pattern.compile("^print\\s+(.+)");
	private static Pattern bool = Pattern.compile("true|false");
	private static Pattern digits = Pattern.compile("\\d+");
	private static Pattern str = Pattern.compile("\\s*(\".+\")\\s*");
	private static Pattern var = Pattern.compile("\\s*([a-zA-Z]{1}[a-zA-Z0-9]*).*");
	private static Pattern loop = Pattern.compile("^while\\s+(.+)");
	
	public static void main(String args[]) {
		BufferedReader reader;
		FileWriter writer;
		try {
			reader = new BufferedReader(new FileReader(
					"testInput.txt"));
			writer = new FileWriter("out.java");
			
			String line = reader.readLine();
			while(line != null) {
				String writeOut = parseLine(line, reader);
				writer.write(writeOut + "\n");
				line = reader.readLine();
			}
			reader.close();
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Distinguishes the type of statements and calls appropriate function
	public static String parseLine(String line, BufferedReader reader){
		
		if (line.matches(var_assign.pattern())){
			return read_var_assign(line);
		}
		if (line.matches(cmd.pattern())) {
			return read_cmd(line);
		}
		if (line.matches(loop.pattern())) {
			// To be implemented. Check read_loop
		}
		return "";
	}
	
	public static String read_loop(String cmd, BufferedReader reader) {
		// Reader object to read next lines, "end" string to signal nest end(maybe)
		return "";
	}
	
	public static String read_cmd(String line) {
		String jcmd = "System.out.print(";
		String jcmd2 = ")";
		String result = "";
		
		Matcher m = cmd.matcher(line);
		m.matches();
		String token[] = m.group(1).split("\\s");
		
		for(int i=0; i < token.length; i++) {
			if(token[i].matches(str.pattern())) {
				result += token[i]; 
			}
			if(token[i].equals("+")){
				result += " + ";
			}
			if(token[i].matches(var.pattern())){
				result += token[i];
			}
		}
		return jcmd + result + jcmd2;
	}
	
	public static String read_var_assign(String line) {
		if(var_assign.matcher(line).matches()) {
			Matcher m = var_assign.matcher(line);
			m.matches();
			String rhs = m.group(2);
			String type = getType(rhs);
			var_map.put(m.group(1), type);
			return type + " " + line;
		}
		return "";
	}
	
	public static String getType(String val) {
		if(val.matches(str.pattern())) return "String";
		if(val.matches(bool.pattern())) return "boolean";
		if(val.matches(digits.pattern())) return "int";
		if(val.matches(var.pattern())) {
			Matcher m = var.matcher(val);
			m.matches();
			return var_map.get(m.group(1));
		}
		else {
			System.out.println("Nothing found" + val);
		}
		return "";
	}
}
