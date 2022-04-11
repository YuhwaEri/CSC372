package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;


public class Main {
	//private static Pattern var_assign = Pattern.compile("^(.+) = (.+)\\.$");
	private static Pattern bool_op = Pattern.compile("AND|OR|XOR|NOT|XAND");
	private static Pattern bool = Pattern.compile("true|false");
	private static Pattern digits = Pattern.compile("\\d+");
	private static Pattern comp = Pattern.compile("<|>|==|<=|>=");
	private static Pattern chr = Pattern.compile("([A-Z]|[a-z]){1}");
	/*private static Pattern bool_expr = Pattern.compile("(" + bool.pattern() + "){1}"+ "|" 
	+ "(" + bool.pattern() + "){1} (" + bool_op.pattern() + "){1}");*/
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print(">> ");
		String cmd = in.nextLine();
		while(!cmd.equals("exit")) {
			System.out.print(chr.matcher(cmd).matches());
			System.out.println();
			System.out.print(">> ");
		cmd = in.nextLine();
		}
		in.close();
	}
}
