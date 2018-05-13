package net.lakkie.acl.test;

import java.util.ArrayList;
import java.util.List;

import net.lakkie.acl.parser.ACLParser;
import net.lakkie.acl.parser.ACLParser.ACLParserMachine;
import net.lakkie.acl.parser.IACLReader;

public class TestLoginDatabase implements IACLReader {

	public int databaseId;
	public final List<TestLogin> logins = new ArrayList<TestLogin>();

	public TestLoginDatabase(ACLParser parser) {
		parser.setReader(this);
		parser.readFile();
	}

	public void onToken(String token, ACLParserMachine machine) {
		if (token.equals("database_id")) {
			this.databaseId = machine.readInt();
		}
		if (token.equals("login")) {
			machine.readSection(new TestLogin(this));
		}
	}
	
	public String toString() {
		String loginList = "";
		for (TestLogin login : this.logins) {
			loginList += "," + login.toString();
		}
		loginList = loginList.replaceFirst(",", "");
		return String.format("[id=%s,logins=%s]", this.databaseId, loginList);
	}

}
