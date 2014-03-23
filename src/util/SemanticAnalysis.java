package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import core.Expression;
import core.Function;
import core.Identifier;
import core.IfElse;
import core.Operation;
import core.Program;
import core.ScopedEntity;
import core.Type;
import core.Variable;

import compiler.generated.*;

public class SemanticAnalysis {

	public static Parser parser;
	
	private static final Type[] BASIC_TYPES = new Type[] {
		new Type("int"),
		new Type("float"),
		new Type("double"),
		new Type("long"),
		new Type("char"),
		new Type("void"),
		new Type("string"),
		new Type("bool")
		};

	public static ArrayList<String> variaveis = new ArrayList<String>();
	public static ArrayList<String> valores = new ArrayList<String>();
	private static SemanticAnalysis sAnalysis;
	
	public static SemanticAnalysis getInstance() {
		if (sAnalysis == null)
			sAnalysis = new SemanticAnalysis();
		return sAnalysis;
	}
	
	// Object Attributes
	
	private Program cProgram; // ...
	
	private Stack<ScopedEntity> scopeStack;

	private SemanticAnalysis(){
		scopeStack = new Stack<ScopedEntity>();
		cProgram = new Program();
	}
	
	// Operations ...
	
	private void createNewScope(ScopedEntity scope) {
		scopeStack.push(scope);
	}
	
	public void exitCurrentScope() {
		ScopedEntity scoped = scopeStack.pop();
		
		if (scoped instanceof Function)
			((Function) scoped).validateReturnedType();
	}
	
	public ScopedEntity getCurrentScope() {
		return scopeStack.peek();
	}
	
	public void addFunctionAndNewScope(Function f) {
		cProgram.checkOverload(f);
		cProgram.addFunction(f);
		createNewScope(f);
		
		// Add to assembly
		toAssembly((labels+=8)+": "+f.getName() + ":");
	}
	
	public void createIf(Expression e) {
		createNewScope(new IfElse(e));
	}
	
	public void createElse() {
		createNewScope(new IfElse());
	}
	
	public void addVariable(Variable v) {
		if (checkVariableNameCurrentScope(v.getName()))
			throw new SemanticException("Variable name already exists");
		
		if (scopeStack.peek() != null) {
			scopeStack.peek().addVariable(v);
		} else {
			cProgram.addVariable(v);
		}
	}
	
	public Identifier getIdentifier(String name) {
		if (!checkVariableNameAllScopes(name) && !checkFunctionName(name))
			throw new SemanticException("Identifier name doesn't exists: " + name);
		
		if (cProgram.getFunctions().get(name) != null)
			return cProgram.getFunctions().get(name);
		
		for (int i = scopeStack.size() - 1 ; i >= 0 ; i--)
			if (scopeStack.get(i).getVariable().get(name) != null)
				return scopeStack.get(i).getVariable().get(name);
		
		return cProgram.getVariable().get(name);
	}
	
	// Check Operations
	
	public void isFunction(Object o) {
		if (!(o instanceof Function))
			throw new SemanticException("Sorry, but " + o.toString() + " is not a function");
	}
	
	public boolean checkVariableNameCurrentScope(String name) {
		
		Set<String> variablesName;
		if (scopeStack.isEmpty())
			variablesName = cProgram.getVariable().keySet();
		else
			variablesName = scopeStack.peek().getVariable().keySet();
		
		return variablesName.contains(name);
	}
	
	public boolean checkVariableNameAllScopes(String name) {
		HashSet<String> variablesName = new HashSet<String>();
		if (scopeStack.isEmpty())
			variablesName.addAll(cProgram.getVariable().keySet());
		else {
			variablesName.addAll(scopeStack.peek().getVariable().keySet());
			
			for (int i = 0 ; i < scopeStack.size() - 1 ; i++) {
				for (String vName : scopeStack.get(i).getVariable().keySet()) {
					variablesName.add(vName);
				}
			}
		}
		return variablesName.contains(name);
	}
	
	public boolean checkTypeExists(Type type) {
		for (int i = 0 ; i < BASIC_TYPES.length ; i++)
			if (BASIC_TYPES[i].getName().equals(type.getName()))
				return true;
		
		for (int i = 0 ; i < scopeStack.size() ; i++) {
			if (scopeStack.get(i).getTypes().containsKey(type.getName())) {
				return true;
			}
		}
		return false;
	}

	public void checkFunctionCallException(String functionName) {
		if (!checkFunctionCall(functionName)) {
			throw new SemanticException("Calling function not declared: " + functionName + "()");
		}
		
		toAssembly(labels+": ADD SP, SP, #size");
		toAssembly(labels+8+": ST *ST, #" + labels+32);
		toAssembly(labels+16+": BR " + functionName);
		toAssembly(labels+32 + ":");
		toAssembly(labels+40+": SUB SP, SP, #size");
		labels= labels+56;
	}
	
	public void checkFunctionCallException(String functionName, Type[] types) {
		if (!checkFunctionCall(functionName, types)) {
			throw new SemanticException("Calling function not declared: " + functionName + " " + Arrays.toString(types));
		}
		
		toAssembly(labels+": ADD SP, SP, #size");
		toAssembly(labels+8+": ST *ST, #" + labels+32);
		toAssembly(labels+16+": BR " + functionName);
		toAssembly(labels +32+ ":");
		toAssembly(labels+40+": SUB SP, SP, #size");
		labels= labels+56;
	}
	
	public boolean checkFunctionName(String functionName) {
		Function f = cProgram.getFunctions().get(functionName);
		return f != null;
	}
	
	public boolean checkFunctionCall(String functionName) {
		Function f = cProgram.getFunctions().get(functionName);
		return f != null && f.getParameterTypes().length == 0;
	}
	
	public boolean checkFunctionCall(String functionName, Type[] types) {
		Function f = cProgram.getFunctions().get(functionName);
		if (f != null && f.getParameterTypes().length == types.length) {
			for (int i = 0 ; i < types.length ; i++) {
				if (!(types[i].getName().equals(f.getParameterTypes()[i].getName())))
					return false;
			}
			return true;
		}
		return false;
	}
	
	public void checkReturnedType(Object e) {
		Type typeToCheck;
		if (e instanceof Function)
			typeToCheck = ((Function)e).getReturnType();
		else
			typeToCheck = ((Expression) e).getType();
		
		
		Function f = null;
		if (scopeStack.peek() instanceof Function) {
			f = (Function) scopeStack.peek();
		} else {
			for (int i = scopeStack.size() - 1 ; i >= 0 ; i--) {
				if (scopeStack.get(i) instanceof Function) {
					f = (Function) scopeStack.get(i);
					break;
				}
			}
		}
		
		if (f == null)
			throw new SemanticException("Checking return type without function");
		
		if (!f.getReturnType().equals(typeToCheck)) {
			throw new SemanticException("Wrong return type: " + f.getReturnType() + " and " + typeToCheck);
		}
		
		f.setReturnedType(typeToCheck);
	}
	
	private boolean checkIsNumber(Type t) {
		return t.equals(new Type("int")) || t.equals(new Type("float"));
	}
	
	public Expression getExpressionForOperation(Operation op, Expression e1, Expression e2) {
		
		switch (op) {
		case AND_OP:
		case OR_OP:
			if (e1.getType().equals(new Type("int")))
				return new Expression(new Type("int")); // OK
		case EQ_OP:
		case GE_OP:
		case LE_OP:
		case LESS_THAN:
		case MORE_THAN:
		case NE_OP:
			if (checkIsNumber(e1.getType()) && checkIsNumber(e2.getType()) ||
					e1.getType().equals(e2.getType()))
				return new Expression(new Type("int"));
		case MINUS:
		case MULT:
		case PERC:
		case PLUS:
		case DIV:
			if (checkIsNumber(e1.getType()) && checkIsNumber(e2.getType()))
				return new Expression(e1.getType());
		}
		throw new SemanticException("Illegal Operation between " + e1.getType() + " and " + e2.getType());
	}
	
	public Expression getExpressionType(Expression e1) {
		return new Expression(e1.getType());
	}
	
	public static int labels = 100;
	public static int labelAux;
	public static String assemblyCode = "100: LD SP, 1000\n";

	public static void AssemblyGenerator(Operation op, Expression e1, Expression e2) {
		if (op.equals(Operation.PLUS)) {// +
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": ADD R1, R1, R2");
			labels = labels + 16;
		}
		else if (op.equals(Operation.MINUS)) {// -
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+": SUB R1, R1, R2");
			labels = labels + 16;
		}
		else if (op.equals(Operation.MULT)) {// *
			toAssembly(labels+  ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+": MUL R1, R1, R2");
			labels = labels + 16;
		}
		else if (op.equals(Operation.DIV)) {// /
			toAssembly(labels+   ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+": DIV R1, R1, R2");
			labels = labels + 16;
		}
		else if (op.equals(Operation.EQ_OP)) {// ==
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BEQZ R1, " + (labels + 56)); //if
			toAssembly(labels+40+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		}
		else if (op.equals(Operation.NE_OP)) {// !=
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BEQZ R1, " + (labels + 56)); //if
			toAssembly(labels+40+": LD R1, #1"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #0"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		}
		else if (op.equals(Operation.AND_OP)) {// &&
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
						toAssembly(labels+16+ ": BEQZ R1, " + (labels+56)); //if
						toAssembly(labels+32+ ": BEQZ R2, " + (labels+56)); //if
			toAssembly((labels)+40+": LD R1, #1"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #0"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		}
		else if (op.equals(Operation.OR_OP)) { // ||
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
						toAssembly(labels+16+ ": BNEZ R1, " + (labels+56)); //if
						toAssembly(labels+32+ ": BNEZ R2, " + (labels+56)); //if
			toAssembly((labels+40)+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		} else if (op.equals(Operation.LESS_THAN)) {// x - 3 < 0
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BLTZ R1, " + (labels + 56)); //if
			toAssembly(labels+40+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		} else if (op.equals(Operation.MORE_THAN)) {// >
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BGTZ R1, " + (labels + 52)); //if
			toAssembly(labels+40+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+52)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		} else if (op.equals(Operation.LE_OP)) {//  <=
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BLEZ R1, " + (labels + 56)); //if
			toAssembly(labels+40+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		} else if (op.equals(Operation.GE_OP)) {// >=
			toAssembly(labels+ ": LD R1, " + e1.getAssemblyValue());
			toAssembly(labels+8+ ": LD R2, " + e2.getAssemblyValue());
			toAssembly(labels+16+ ": SUB R1, R1, R2");
						toAssembly(labels+32+ ": BGEZ R1, " + (labels + 56)); //if
			toAssembly(labels+40+": LD R1, #0"); //else
			toAssembly(labels+48+ ": BR "+ (labels+64));    //else
						toAssembly((labels+56)+": LD R1, #1"); //if
			toAssembly(labels+64+":");		  //else
			labels = labels + 64;
		}
		
		
//		labels += 100; 
//		toAssembly("...");
	}
	
	public static void toAssembly(String assemblyString) {
		assemblyCode += assemblyString + "\n";
	}
}
