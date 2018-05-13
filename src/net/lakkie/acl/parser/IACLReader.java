package net.lakkie.acl.parser;

import net.lakkie.acl.parser.ACLParser.ACLParserMachine;

public interface IACLReader {

	void onToken(String token, ACLParserMachine machine);

	default void onFinish() {
	}
}
