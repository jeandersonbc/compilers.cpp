package util;

import compiler.generated.*;

public class SemanticException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;

	public SemanticException(String message) {
		super("Error at " + Lexer.curLine + "\n" + message);
	}

}
