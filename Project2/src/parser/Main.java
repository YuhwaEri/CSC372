package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;


public class Main {
	//private static Pattern var_assign = Pattern.compile("^(.+) = (.+)\\.$");
	private static Pattern bool_op = Pattern.compile("AND|OR|XOR|NOT|XAND");

	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print(">> ");
		String cmd = in.nextLine();
		while(!cmd.equals("exit")) {
			System.out.print(bool_op.matcher("AND").matches());
		cmd = in.nextLine();
		}
		
		in.close();

	}
}
