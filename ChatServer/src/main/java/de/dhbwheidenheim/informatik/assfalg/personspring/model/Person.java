package de.dhbwheidenheim.informatik.assfalg.personspring.model;

public class Person {

	
	String name;
	int alter;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	private Person istVerheiratetMit = null;
	
	boolean istVolljaehrig() {
		/*boolean b;
		if(alter >= 18)
			b = true;
		else
			b = false;
		return b;*/
		/* alternativ zu obigem Konstrukt: */
		return(alter >= 18);
	}
	
	public void verheirateMit(Person partner) {
		partner.istVerheiratetMit = this;
		this.istVerheiratetMit = partner;
		return;
	}
	
	
	
	
	
	public String toString() {
		return(name + " -Alter: " + alter);
	}

	public Person(String name, int alter) {
		this.name = name;
		this.alter = alter;
	}
	public Person(String name, double alter) {
		this.name = name;
		this.alter = (int)alter;
	}
	
	public Person(String name) {
		this.name = name;
		this.alter = 0;
	}
	
	public Person() {}

}
