package net.lakkie.acl.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.lakkie.acl.lexer.ACLLexer;

public class ACLParser {

	private IACLReader reader;
	private ACLLexer lexer;
	private ACLParserMachine machine;

	public ACLParser(ACLLexer lexer) {
		this.lexer = lexer;
		this.machine = new ACLParserMachine();
	}

	public ACLParser(InputStream input) {
		this(new ACLLexer(input));
	}

	public ACLParser(String resource) {
		this(ACLParser.class.getResourceAsStream(resource));
	}

	public ACLParser(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public ACLLexer getLexer() {
		return this.lexer;
	}

	public IACLReader getReader() {
		return this.reader;
	}

	public void setReader(IACLReader reader) {
		this.reader = reader;
	}

	public void readFile() {
		while (this.getLexer().hasNext()) {
			this.reader.onToken(this.getLexer().getNextToken(), this.machine);
		}
		this.reader.onFinish();
	}
	
	public class ACLParserMachine {

		public ACLParser getParser() {
			return ACLParser.this;
		}

		public List<String> readStringList() {
			this.getParser().getLexer().next(2);
			List<String> result = new ArrayList<String>();
			String token;
			while (!(token = this.getParser().getLexer().next()).equals("}")) {
				result.add(this.getParser().getLexer().clearQuotes(token));
			}
			return result;
		}

		public String readString() {
			// Skip "="
			String value = this.getParser().getLexer()
					.clearQuotes(this.getParser().getLexer().next(2));
			return value;
		}

		public double readDouble() {
			return Double.parseDouble(this.readString());
		}
		
		public float readFloat() {
			return Float.parseFloat(this.readString());
		}

		public int readInt() {
			return Integer.parseInt(this.readString());
		}

		public void enterSection() {
			this.getParser().getLexer().next(2);
		}

		public void readSection(IACLReader reader) {
			this.enterSection();
			String token = null;
			// This will stop AT the "}" token or EOF
			while (true) {
				token = this.getParser().getLexer().getNextToken();
				if (token == null || token.equals("}")) {
					break;
				}
				reader.onToken(token, this);
			}
			reader.onFinish();
		}

		public void skipSection() {
			this.enterSection();
			while (!this.getParser().getLexer().getNextToken().equals("}")) {
			}
		}

	}

}
