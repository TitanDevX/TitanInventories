package me.titan.titaninvs.examples;

/**
 *
 * Example user class used for UsersGUI.
 *
 */
public class User {

	String name;
	String lastName;

	public User(String name, String lastName) {
		this.name = name;
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
