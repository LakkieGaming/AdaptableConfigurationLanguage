package net.lakkie.acl;

import net.lakkie.acl.parser.ACLParser;
import net.lakkie.acl.test.TestLoginDatabase;

public class ACLMain {

	public static void main(String[] args) {
		TestLoginDatabase db = new TestLoginDatabase(new ACLParser("/tests/test_main.txt"));
		System.out.println(db.toString());
	}

}
