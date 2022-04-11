package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	//TODO: start stmt and var_assg
	//TODO: make parse method
	//private static Pattern var_assign = Pattern.compile("^(.+) = (.+)\\.$");
	private static ArrayList<Var> var_list = new ArrayList<Var>();
	private static Pattern prCmd = Pattern.compile("print\\((.*)\\)");
	private static Pattern bool_op = Pattern.compile("AND|OR|XOR|NOT|XAND");
	private static Pattern bool = Pattern.compile("true|false");
	private static Pattern digits = Pattern.compile("\\d+");
	private static Pattern comp = Pattern.compile("<|>|==|<=|>=");
	private static Pattern chr = Pattern.compile("'([A-Z]|[a-z]|\\d){1}'");
	private static Pattern str = Pattern.compile("([A-Z]|[a-z]|\\d)+$");
	private static Pattern var = Pattern.compile("\\w+");
	private static Pattern var_assg = Pattern.compile("(.+)=(.+)");
	/*private static Pattern bool_expr = Pattern.compile("(" + bool.pattern() + "){1}"+ "|" 
	+ "(" + bool.pattern() + "){1} (" + bool_op.pattern() + "){1}");*/
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print(">> ");
		String cmd = in.nextLine();
		while(!cmd.equals("exit")) {
			//System.out.println(var_assign(cmd));
			System.out.println(chr.matcher(cmd).matches());
			System.out.print(">> ");
		cmd = in.nextLine();
		}
		in.close();
	}
	
	private static void parse(String cmd) {
		Matcher lineMatch = prCmd.matcher(cmd);
		boolean match = lineMatch.find();
	}
	
	private static boolean var_assign(String cmd) {
		//TODO: this
		Matcher matched = var_assg.matcher(cmd);
		if (matched.find()) {
			String varName = matched.group(1).trim();
			String valS = matched.group(2).trim();
			if (digits.matcher(valS).matches()) {
				int val = Integer.parseInt(valS);
				var_list.add(new Var<Integer>(varName, val));
				return true;
			}
			System.out.println("Not a valid variable being assigned");
			return false;
		}
		System.out.println("Not a valid assignment");
		return false;
	}
	
	static Var get_var_obj(String name) {
		for(int i=0; i<var_list.size(); i++) {
			if(var_list.get(i).name == name){
				return var_list.get(i);
			}
		}
		return null;
	}
	
	boolean evaluateBExpr(String cmd) {
		// x = true
		return true;
	}
	
}
