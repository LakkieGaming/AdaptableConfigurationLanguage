package net.lakkie.acl.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ACLLexer {

	/**
	 * The string that temporarily holds the file name until further process
	 */
	private String fileValue;
	private String previous, current, next;
	private boolean hadFirstToken = false;
	private Map<String, List<IACLTokenCallback>> callbacks = new HashMap<String, List<IACLTokenCallback>>();

	public ACLLexer(InputStream input) {
		this.readFile(input);
		// Set up initial tokens
		if (this.fileValue.replaceAll("\\s", "").equals("")) {
			// File is empty
			return;
		}
		this.previous = null;
		this.current = this.readNext();
		try {
			this.next = this.readNext();
		} catch (Exception e) {
			// File only has 1 token, no important error here
		}
	}

	public String next() {
		return this.getNextToken();
	}

	public String next(int amount) {
		if (amount == 1) {
			return this.next();
		}
		if (amount < 1) {
			throw new IllegalArgumentException(
					"Amount of nexts must be equal to or greater than 1");
		}
		for (int i = 0; i < amount - 1; i++) {
			this.next();
		}
		return this.next();
	}

	public void waitForToken(IACLTokenCallback callback) {
		String token = callback.getReceivingToken();
		if (!this.callbacks.containsKey(token)) {
			this.callbacks.put(token, new ArrayList<IACLTokenCallback>());
		}
		this.callbacks.get(token).add(callback);
	}

	private void readFile(InputStream input) {
		Scanner scanner = new Scanner(input);
		StringBuilder result = new StringBuilder();
		while (scanner.hasNextLine()) {
			result.append(scanner.nextLine());
			result.append(System.lineSeparator());
		}
		scanner.close();
		this.fileValue = new String(result);
	}

	public boolean hasNext() {
		return this.next != null;
	}

	public void readAllTokens() {
		while (this.hasNext()) {
			this.getNextToken();
		}
	}

	public String getNextToken() {
		if (!this.hadFirstToken) {
			this.hadFirstToken = true;
			if (this.current.startsWith("#")) {
				return this.getNextToken();
			}
			return this.current;
		}
		if (!this.hasNext())
		{
			return null;
		}
		// Shift everything back
		this.previous = this.current;
		this.current = this.next;
		this.next = this.readNext();
		if (this.current == null) {
			return null;
		}
		if (this.callbacks.containsKey(this.current)) {
			for (IACLTokenCallback callback : this.callbacks
					.get(this.current)) {
				callback.onTokenReceived();
			}
		}
		if (this.current.startsWith("#")) {
			return this.getNextToken();
		}
		return this.current;
	}

	public String clearQuotes(String string) {
		if (!(string.startsWith("\"") && string.endsWith("\""))) {
			return string;
		}
		char[] chars = string.toCharArray();
		chars[0] = '\u16b6';
		chars[chars.length - 1] = '\u16b6';
		return new String(chars).replaceAll("\u16b6", "");
	}
	
	public String getPrevious() {
		return this.previous;
	}

	private String readNext() {
		Matcher matcher = ACLPatternLoader.tokenMatcher.matcher(this.fileValue);
		if (!matcher.find()) {
			return null;
		}
		String matchValue = matcher.group();
		// Remove match from string
		this.fileValue = matcher.replaceFirst("");
		return matchValue;
	}

}
