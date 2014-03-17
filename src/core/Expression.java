package core;

import util.SemanticException;




public class Expression extends NamedEntity { // Not Really a named entity, but...
	
	private Type type;
	private String value;
	
	public Expression(String name) { // For Variables..
		super(name);
		type = new Type("UNKNOWN");
	}
	
	public Expression(Type t) {
		super(null);
		this.type = t;
	}
	
	public Expression(Type t, String value) {
		super(null);
		this.type = t;
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		if (!getType().equals(new Type("UNKNOWN")) && !type.equals(getType()))
			throw new SemanticException("Illegal Type Assignment " + type + " and " + getType());
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getAssemblyValue() {
		return value == null ? "R1" : value;
	}
	
	public void setValue(Expression exp) {
		if (!exp.getType().equals(new Type("UNKNOWN")))
			setType(exp.getType());
		this.value = exp.getValue();
	}
	
	public String toString() {
//		return "{ Expression: " + getType() + " " + getValue() + "  }";
		return getValue();
	}
}
