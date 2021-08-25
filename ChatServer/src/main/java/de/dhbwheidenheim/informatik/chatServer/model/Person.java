package de.dhbwheidenheim.informatik.chatServer.model;

public class Person {

	private String firstName;
	private String lastName;
	private String password;	//TODO Soll verschlüsselt werden 
	
	
	public Person(String firstName, String lastName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password=password;
	}
	
	@Override
	public String toString() {
		 return String.format(
			        "Customer[firstName='%s', lastName='%s']",
			        firstName, lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String password() {
		return password;
	}

	

}
