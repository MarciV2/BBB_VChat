package de.dhbwheidenheim.informatik.chatServer.model;

import de.dhbwheidenheim.informatik.chatServer.model.enums.PersonState;

public class Person {

	private String firstName;
	private String lastName;
	private String password;	//TODO Soll verschlüsselt werden 
	private PersonState state;
	
	
	public Person(String firstName, String lastName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password=password;
		this.state=PersonState.ONLINE;
	}
	
	@Override
	public String toString() {
		 return String.format(
			        "Person[firstName='%s', lastName='%s', state='%s']",
			        firstName, lastName,state);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getPassword() {
		return password;
	}

	public PersonState getState() {
		return state;
	}
	
	public void setState(PersonState state) {
		this.state=state;
	}

}
