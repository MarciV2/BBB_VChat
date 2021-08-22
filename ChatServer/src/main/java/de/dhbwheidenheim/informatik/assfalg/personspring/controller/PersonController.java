package de.dhbwheidenheim.informatik.assfalg.personspring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import de.dhbwheidenheim.informatik.assfalg.personspring.model.*;

@RestController
@RequestMapping("")
public class PersonController {
	static List<Person> personList;

	@RequestMapping("/add")
	public boolean addPersons() {
		if (personList == null)
			personList = new ArrayList<Person>();
		personList.add(new Person("Peter", 26));
		personList.add(new Person("Monika", 42));
		return true;
	}

	@RequestMapping(value = "/addPersonMitPfad/{name}/{alter}", method = RequestMethod.GET)
	public @ResponseBody boolean addPersonMitPfad(@PathVariable("name") String name,
			@PathVariable("alter") String alter) {
		if (personList == null)
			personList = new ArrayList<Person>();
		personList.add(new Person(name, Integer.parseInt(alter)));
		return true;
	}

	@GetMapping("/addPersonMitGETQuery")
	public @ResponseBody boolean addPersonMitGETQuery(@RequestParam String name, @RequestParam String alter) {
		if (personList == null)
			personList = new ArrayList<Person>();
		personList.add(new Person(name, Integer.parseInt(alter)));
		return true;
	}

	// Tut leider nicht, wegen CSRF-Sicherheitseinstellungen, es sei denn man 
	// modifiziert die Sicherheitseinstellungen (siehe Klasse: WebSecurityConfig)
	@PostMapping("/addPersonMitPOSTQuery")
	public boolean addPersonMitPOSTQuery(@RequestParam String name, @RequestParam String alter) {
		if (personList == null)
			personList = new ArrayList<Person>();
		personList.add(new Person(name, Integer.parseInt(alter)));
		return true;
	}
	

	@RequestMapping("/list")
	public List<Person> getPersons() {
		if (personList == null)
			personList = new ArrayList<Person>();
		return personList;
	}

	@RequestMapping("/count")
	public int count() {
		if (personList == null)
			return (0);
		else
			return (personList.size());
	}

	@RequestMapping("/")
	public String home() {
		return ("<html>Hallo Welt, hier ist Rolf<br>\n" 
				+ "GET:<br>\n" 
				+ "<form action=\"/addPersonMitGETQuery\" Method=\"GET\">\n"
				+ "Name: <input name=\"name\" type=\"input\"/><br>\n" 
				+ "Alter: <input name=\"alter\" type=\"input\"/><br>\n"
				+ "<input name=\"speichern\" type=\"submit\"/><br>\n" 
				+ "</form>\n"

				+ "POST (funktioniert evtl. nicht wegen CSRF-Policy):<br>\n" 
				+ "<form action=\"/addPersonMitPOSTQuery\" Method=\"POST\">\n"
				+ "Name: <input name=\"name\" type=\"input\"/><br>\n" 
				+ "Alter: <input name=\"alter\" type=\"input\"/><br>\n"
				+ "<input name=\"speichern\" type=\"submit\"/>\n" 
				+ "</form><br>\n"
				+ "</html>\n");
	}

}