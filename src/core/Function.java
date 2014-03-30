package core;


import java.util.ArrayList;

import util.SemanticException;


/**
 *	C Function
 */
public class Function extends ScopedEntity implements Identifier {

	// void is the default return type
	private Type returnType = new Type("void"); 
	private ArrayList<Parameter> functionParameter;
	private Type returnedType = new Type("void");

	public Function(String name, ArrayList<Parameter> parameters) {
		super(name);
		if (parameters != null)
			functionParameter = parameters;
		else
			parameters = new ArrayList<Parameter>();

		for (Parameter parameter : parameters) {
			if (parameter instanceof Variable)
				addVariable((Variable) parameter);
		}
	}
	
	public Function(String name) {
		this(name, null);
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public Type[] getParameterTypes() {
		if (functionParameter == null)
			return new Type[]{};
		
		Type[] pTypes = new Type[functionParameter.size()];
		for (int i = 0 ; i < pTypes.length ; i++)
			pTypes[i] = functionParameter.get(i).getType();
		return pTypes;
	}


	public void setReturnType(Type type) {
		this.returnType = type;
	}
	
	@Override
	public String toString() {
		return "{ Function: " + getName() + " " + getReturnType() + " " + functionParameter + " }";
	}

	// Check if the returned type is equal to the type of the function
	public void validateReturnedType() { 
		if (!returnedType.equals(returnType))
			throw new SemanticException("Function " + getName() + " was supposed to return " + returnType);
	}

	public void setReturnedType(Type type) {
		this.returnedType = type;
	}
}
