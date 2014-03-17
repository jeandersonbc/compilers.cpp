package core;

import util.SemanticException;

/**
 * A C While-statement<br>
 * It has an expression which is checked every iteration. Ths expression return
 * type must be a integer (0 == false, not 0 == true)
 */
public class While extends ScopedEntity {

	private Expression expression;

	public While(Expression expression) {
		super("while");
		checkExpression(expression);
		this.expression = expression;
	}
	
	public While() {
		super("while");
	}
	
	public void setExpression(Expression e) {
		checkExpression(e);
		this.expression = e;
	}

	public Expression getExpression() {
		return expression;
	}
	
	private void checkExpression(Expression e) {
		if (!e.getType().equals(new Type("int")))
			throw new SemanticException("While is not from type int, instead is type " + e.getType());
	}
}
