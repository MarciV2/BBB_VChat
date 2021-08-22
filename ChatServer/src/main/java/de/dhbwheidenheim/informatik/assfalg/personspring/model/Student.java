package de.dhbwheidenheim.informatik.assfalg.personspring.model;

public class Student extends Person {
	int matNr;

	/*public Student(String n, int a, int m) {
		name = n;
		alter = a;
		matNr = m;
	}
	*/
	public Student(String n, int a, int m) {
		super(n,a);	// �bernimmt n, a aus �bergeordneter Klasse Person!
				// ruft Konstruktor f�r diese Variablen aus Oberklasse auf!
		matNr = m;
	}
	
	public String toString() {
		return ( super.toString() + " MatNr.: " + matNr);
	}

}
