package compiler.main;

import java.io.BufferedReader;
import java.io.FileReader;

import compiler.generated.Lexer;
import compiler.generated.Parser;

/**
 * @author Jeanderson Candido - <a href="http://jeandersonbc.github.io"
 *         target="_blank">http://jeandersonbc.github.io</a>
 * 
 */
public class Main {

	public static void main(String[] args) {

		try {
			Lexer scanner = new Lexer(new BufferedReader(
					new FileReader(args[0])));

			Parser parser = new Parser(scanner);
			parser.parse();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
