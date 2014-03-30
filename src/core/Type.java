package core;

public class Type extends NamedEntity implements Parameter {

	public Type(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return "{ Type " + getName() + " }";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Type))
			return false;
		return getName().equals(((Type) obj).getName());
	}
	
	public Type getType() {
		return this;
	}
}
