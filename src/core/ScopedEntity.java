package core;

import java.util.HashMap;
import java.util.Map;

/**
 *  Class that represents a scope in a program ( if statement scope, method scope...)
 */
public class ScopedEntity extends NamedEntity {

	private HashMap<String, Variable> variables;
	private HashMap<String, Type> types;
	
	public ScopedEntity(String name) {
		super(name);
		variables = new HashMap<String, Variable>();
	}

	public Map<String, Variable> getVariable() {
		return variables;
	}
	
	public void addVariable(Variable v) {
		this.variables.put(v.getName(), v);
	}
	
	public void addType(Type t) {
		this.types.put(t.getName(), t);
	}
	
	public Map<String, Type> getTypes() {
		return types;
	}
	
}
