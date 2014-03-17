package core;

public class Variable extends Expression implements Parameter, Identifier {

	private VariableModifier modifier;
	
	public Variable(String name, Type type) {
		super(name);
		setType(type);
	}
	
	public void setModifier(VariableModifier modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public String getAssemblyValue() {
		return getName();
	};
	
	public VariableModifier getModifier() {
		return modifier;
	}
	
	@Override
	public String toString() {
//		return "{Variable " + getName() + " " + getType() + "}";
		return getName();
	}
}
