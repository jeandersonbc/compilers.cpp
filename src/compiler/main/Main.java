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

	/**
	 * Minimum number of input
	 */
	private static final int MIN_INPUT_FILES = 1;

	public static void main(String[] args) {
		if (args.length < MIN_INPUT_FILES) {
			displayHelpMessage();

		} else {
			for (String filePath : args) {
				startCompilationFor(filePath);
			}
		}
	}

	private static void displayHelpMessage() {
		System.out
				.println("Usage: java -jar compiler.jar file [file2 file3...]");
	}

	private static void startCompilationFor(String filePath) {
		try {
			System.out.println("Start compiling \"" + filePath + "\"...");
			Lexer scanner = new Lexer(new BufferedReader(new FileReader(
					filePath)));

			Parser parser = new Parser(scanner);
			parser.parse();

		} catch (Exception e) {
			System.err.println("Failed to compile \"" + filePath + "\":");
			System.err.println(e.getMessage());
		}
	}

}
