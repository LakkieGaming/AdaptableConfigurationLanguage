package net.lakkie.acl.lexer;

public interface IACLTokenCallback {

	void onTokenReceived();

	String getReceivingToken();

}
