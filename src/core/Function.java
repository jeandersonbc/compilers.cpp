package core;


import java.util.ArrayList;

import util.SemanticException;


/**
 *	C Function
 */
public class Function extends ScopedEntity implements Identifier {
	
	private Type returnType = new Type("void"); // Default Return Type
	private ArrayList<Parameter> functionParameter;
	private Type returnedType = new Type("void"); // Default Return Type

//	public Function(String name, Type returnType, ArrayList<Variable> parameters) {
//		super(name);
//		this.returnType = returnType;
//		
//		if (parameters != null)
//			functionParameter = parameters;
//		else
//			parameters = new ArrayList<Variable>();
//
//		for (Variable parameter : parameters) {
//			addVariable(parameter);
//		}
//	}
	
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


	public void validateReturnedType() { // Checks if the function returned what it was supposed to..
		if (!returnedType.equals(returnType))
			throw new SemanticException("Function " + getName() + " was supposed to return " + returnType);
	}


	public void setReturnedType(Type type) {
		this.returnedType = type;
	}
}
