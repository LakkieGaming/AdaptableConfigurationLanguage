package net.lakkie.acl.lexer;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ACLPatternLoader {

	public static final Pattern commentMatcher;
	public static final Pattern tokenMatcher;

	private static Pattern readNextToken(Scanner scanner) {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.replaceAll("\\s", "").equals("") || line.startsWith("#")) {
				continue;
			}
			return Pattern.compile(line);
		}
		return null;
	}

	static {
		Scanner scanner = new Scanner(ACLPatternLoader.class.getResourceAsStream("/internal/patterns.txt"));
		commentMatcher = readNextToken(scanner);
		tokenMatcher = readNextToken(scanner);
		scanner.close();
	}

}
