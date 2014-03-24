package core;

public class Variable extends Expression implements Parameter, Identifier {

	public Variable(String name, Type type) {
		super(name);
		setType(type);
	}
	
	@Override
	public String getAssemblyValue() {
		return getName();
	};
	
	@Override
	public String toString() {
		return getName();
	}
}
