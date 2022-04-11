package parser;

public class Var<T> {
	String name;
	T value;
	
	public Var(String name){
		this.name = name;
	}
	
	public Var(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public T getValue() {
		return this.value;
	}
}
