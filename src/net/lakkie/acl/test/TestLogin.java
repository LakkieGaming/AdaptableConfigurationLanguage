package net.lakkie.acl.test;

import net.lakkie.acl.parser.ACLParser.ACLParserMachine;
import net.lakkie.acl.parser.IACLReader;

public class TestLogin implements IACLReader {

	public TestLoginDatabase db;
	public String username, password;
	public int userHash;

	public TestLogin(TestLoginDatabase db) {
		this.db = db;
		this.db.logins.add(this);
	}

	public void onToken(String token, ACLParserMachine machine) {
		if (token.equals("username")) {
			this.username = machine.readString();
		}
		if (token.equals("password")) {
			this.password = machine.readString();
		}
		if (token.equals("user_hash")) {
			this.userHash = machine.readInt();
		}
	}
	
	public String toString() {
		return String.format("[username=%s,password=%s,hash=%s]", this.username, this.password, this.userHash);
	}

}
