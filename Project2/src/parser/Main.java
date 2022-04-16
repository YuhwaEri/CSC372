package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
	
	private static HashMap<String, String> var_map = new HashMap<String, String>();
	
	private static Pattern var_assign = Pattern.compile("([a-zA-Z0-9]+) = (.+)");
	private static Pattern cmd = Pattern.compile("^print\\s+(.+)");
	private static Pattern bool = Pattern.compile("true|false");
	private static Pattern bool_op = Pattern.compile(".*(AND|OR|NOT).*");
	private static Pattern comp = Pattern.compile("<|<=|==|!=|>=|>");
	private static Pattern digits = Pattern.compile("\\d+");
	private static Pattern deci = Pattern.compile("\\d+.\\d+");
	private static Pattern str = Pattern.compile("\\s*(\".+\")\\s*");
	private static Pattern var = Pattern.compile("\\s*([a-zA-Z]{1}[a-zA-Z0-9]*).*");
	private static Pattern loop = Pattern.compile("^(while)\\s+(.+)(<|<=|==|!=|>=|>)(.+)");
	private static Pattern cond = Pattern.compile("^(if)\\s+(.+)(<|<=|==|!=|>=|>)(.+)");
	private static Pattern mathOp = Pattern.compile("\\+|-|/|\\*|%");
	
	public static void main(String args[]) throws Exception {
		BufferedReader reader;
		FileWriter writer;
		try {
			if (args.length == 0) throw new Exception("Command line input required");
			//String input = args[0];		//for implementation
			String input = "testInput.txt";	//for testing
			String output = "out";
			reader = new BufferedReader(new FileReader(
					input));
			writer = new FileWriter(output + ".java");
			writer.write("public class " + output + "{\n");
			writer.write("\tpublic static void main(String args[]) {\n");
			String line = reader.readLine();
			String writeOut;
			while(line != null) {
				//System.out.println(line); //comment out when finished
				if (line.trim().equals("")) writeOut = "";//empty lines
				else writeOut = parseLine(line, reader);
				if(!writeOut.equals("")) writer.write("\t\t" + writeOut + "\n");
				line = reader.readLine();
			}
			writer.write("\t}\n}");
			reader.close();
			writer.close();
			System.out.println("Parsing finished!");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Distinguishes the type of statements and calls appropriate function
	public static String parseLine(String line, BufferedReader reader) throws IOException{
		
		if (line.matches(var_assign.pattern())){
			return read_var_assign(line) + ";";
		}
		if (line.matches(cmd.pattern())) {
			return read_cmd(line) + ";";
		}
		if (line.matches(cond.pattern())) { //Calls itself to read through nested code
			Matcher m = cond.matcher(line);
			m.matches();
			String rhs = m.group(2) + m.group(3) + m.group(4);
			if (invalidV(m.group(4).trim())) {
				System.out.println("No match in: " + line);
				return "";
			}
			String result = m.group(1) + "(" + read_bool_expr(rhs) + "){\n";
			String line2 = reader.readLine().replaceAll("\\t", "");
			while(!line2.equals("end")) {
				result+= "\t\t\t";
				result+= parseLine(line2, reader).replaceAll("\n", "\n\t");
				//System.out.println(result);
				result+= "\n";
				line2 = reader.readLine().replaceAll("\\t", "");
			}
			result+= "\t\t}";
			return result;
		}
		if (line.matches(loop.pattern())) {
			Matcher m = loop.matcher(line);
			m.matches();
			String rhs = m.group(2) + m.group(3) + m.group(4);
			if (invalidV(m.group(4).trim())) {//checks for syntax error in loop condition
				System.out.println("No match in: " + line);
				return "";
			}
			return m.group(1) + "(" + rhs + "){\n" + read_loop(reader) + "\t}";
		}
		else {
			System.out.println("No match in: " + line);
		}
		return "";
	}
	
	public static String read_bool_expr(String line) {
		String result = "";
		String token[] = line.split("\\s");
		for(int i=0; i<token.length; i++) {
			if(token[i].matches(bool_op.pattern())) {
				Matcher bm = bool_op.matcher(token[i]);
				result+= get_bool_op(bm.group(1));
			}
			if(token[i].matches(comp.pattern())) {
				result+= token[i];
			}
			
			if(token[i].matches(digits.pattern())) {
				result+= token[i];
			}
			
			if(token[i].matches(var.pattern())) {
				result+= token[i];
			}
		}
		
		return result;
	}
	
	static String get_bool_op(String bool_op) {
		if(bool_op.equals("AND")) return "&&";
		if(bool_op.equals("NOT")) return "!";
		if(bool_op.equals("OR")) return "||";
		return "";
	}
	
	public static String read_loop(BufferedReader reader) throws IOException {
		String nextLine = reader.readLine();
		String result = "";
		if (nextLine == null) throw new IOException("Loop statement requires end block");
		nextLine = nextLine.replaceAll("\\t", "");
		while (!nextLine.equals("end")) {
			if (!nextLine.equals("")) result += "\t" + parseLine(nextLine, reader) + "\n";
			
			nextLine = reader.readLine();
			if (nextLine == null) {
				throw new IOException("Loop statement requires end block");
			}
			nextLine = nextLine.replaceAll("\\t", "");
		}
		return result;
	}
	
	public static String read_cmd(String line) {
		String prefix = "System.out.print(";
		String suffix = ")";
		String result = "";
		
		Matcher m = cmd.matcher(line);
		m.matches();
		
		String token[] = tokenize(m.group(1));
		
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
			if(token[i].matches(digits.pattern())) {
				result += "\"" + token[i] + "\"";
			}
		}
		return prefix + result + suffix;
	}
	
	// Splits string by internal string and whitespace
	// Ex.      "foo bar" + x + y
	// Becomes ["foo bar", +, x, +, y]
	public static String[] tokenize(String rhs) {
		String token[] = rhs.split("(?=\"\\w*\")");
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i=0; i<token.length; i++){
			if(!token[i].matches(str.pattern())) {
				String[] tmp2 = token[i].split("\\s");
				for (int j=0; j<tmp2.length; j++) {
					tmp.add(tmp2[j]);
				}
			}
			else {
				tmp.add(token[i]);
			}
		}
		
		String[] result = new String[tmp.size()];
		tmp.toArray(result);
		return result;
	}
	
	public static String read_var_assign(String line) {
		Matcher m = var_assign.matcher(line);
		m.matches();
		String rhs = m.group(2);
		String type = getType(rhs);
		String lhsType = type;
		String result = m.group(1) + " =";
		
		if(var_map.containsKey(m.group(1))) {
			lhsType = var_map.get(m.group(1));
			if(!lhsType.equals(type)) {
				System.out.printf("Error, can not convert from \s to \s\n", lhsType, type);
				System.exit(1);
			}
			lhsType = "";
		}
		
		String token[] = tokenize(rhs);
		for(int i=0;i<token.length;i++) {
			result += " ";
			//System.out.println(token[i]);
			if(token[i].matches(var.pattern()) && !token[i].matches(bool.pattern())) {
				if(var_map.get(token[i]).equals("int") && type.equals("double")) {
					result += "(double)" + token[i];
				}
				else result += token[i];
			}
			else {
				result += token[i];
			}
		}
		
		var_map.put(m.group(1), type);
		if(!lhsType.equals("")) result = " " + result;
		return lhsType + result;
	}
	
	// Evaluates the right hand side of assignments and determines type.
	public static String getType(String val) {
		String token[] = tokenize(val);
		boolean hasFloat = false;
		boolean hasString = false;
		boolean hasInt = false;
		boolean hasMath = false;
		boolean plusOnly = true;
		
		for(int i=0; i<token.length;i++) {
			//Simples
			if(token[i].matches(comp.pattern())) return "boolean";
			if(token[i].matches(deci.pattern())) hasFloat = true;
			if(token[i].matches(str.pattern())) hasString = true;
			if(token[i].matches(digits.pattern())) hasInt = true;
			if(token[i].matches(mathOp.pattern())) {
				hasMath = true;
				if(!token[i].contains("+")) plusOnly = false;
			}
			
			//Variables
			if(token[i].matches(var.pattern())) {
				if(var_map.get(token[i]) == "boolean") return "boolean"; 
				if(var_map.get(token[i]) == "double") hasFloat = true;
				if(var_map.get(token[i]) == "String") hasString = true;
				if(var_map.get(token[i]) == "int") hasInt = true;
			}
		}
		if(!hasMath) plusOnly = false;
		
		if(hasString && !hasFloat && !hasInt && plusOnly) return "String";
		if(hasFloat && hasMath) return "double";
		return "int";
		
	}
	
	public static boolean invalidV(String v) {
		return !var.matcher(v).find() && !bool.matcher(v).find() &&
				!digits.matcher(v).find() && !str.matcher(v).find();
	}
}
	
