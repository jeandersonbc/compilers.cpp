package core;

import java.util.HashMap;

import util.SemanticException;
/**
 *	C++ Program
 */
public class Program extends ScopedEntity {
	
	HashMap<String, Function> functions;
	
	public Program() {
		super("program");
		functions = new HashMap<String, Function>();
	}
	
	public HashMap<String, Function> getFunctions() {
		return functions;
	}

	public void addFunction(Function f) {
		functions.put(f.getName(), f);
	}

	public void checkOverload(Function f) {
		for (Function curF : functions.values()) {
			// There is already a function with that name,
			// we must now check its return type
			if (f.getName().equals(curF.getName())) {
				Type t1 = f.getReturnType();
				Type t2 = curF.getReturnType();
				if( !t1.equals(t2) ) {
					throw new SemanticException("Error overloading function: Two different return types " + t1 + " and " + t2);
				}
			}
		}
	}
}
