package util;


public class Logger {
	
	private static boolean activate_log = true;
	
	public static void print(String value) {
		if (activate_log)
			System.out.println(value);
	}

}
