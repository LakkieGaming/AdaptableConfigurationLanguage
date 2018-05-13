package net.lakkie.acl.parser;

public class ACLKeyPair {

	public String key;
	public Object value;

	public ACLKeyPair(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getString() {
		return (String) this.value;
	}

	public double getDouble() {
		return (double) this.value;
	}
	
	public int getInt() {
		return (int) this.value;
	}
	
}
