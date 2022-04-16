package parser;

public class Var {
	String name;
	String type;
	String f_body = "";
	
	public Var(String name){
		this.name = name;
	}
	
	public Var(String name, String type) {
		this.name = name;
		this.type = type;
	}
}
