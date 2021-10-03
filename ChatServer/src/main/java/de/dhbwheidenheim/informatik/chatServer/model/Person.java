package de.dhbwheidenheim.informatik.chatServer.model;

import java.io.Serializable;

import de.dhbwheidenheim.informatik.chatServer.model.enums.PersonState;

public class Person implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private PersonState state;
	
	
	public Person(String username, String password) {
		this.username = username;
		this.password=password;
		this.state=PersonState.OFFLINE;
	}
	
	@Override
	public String toString() {
		 return String.format(
			        "Person[username='%s', state='%s']",
			         username,state);
	}

	public String getUsername() {
		return username;
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
