package core;

import util.SemanticException;

public class IfElse extends ScopedEntity{
	
	private Expression expression;
	
	public IfElse() {
		super("if");
		
	}

	public IfElse(Expression expression) {
		super("if");
		checkExpression(expression);
		this.expression = expression;
	}

	public void setExpression(Expression e) {
		checkExpression(e);
		this.expression = e;
	}

	public Expression getExpression() {
		return expression;
	}
	
	private void checkExpression(Expression e) {
		if (!e.getType().equals(new Type("int")) && !e.getType().equals(new Type("bool")))
			throw new SemanticException("If expression not 'int' or 'bool', instead is " + e.getType());
	}

}
