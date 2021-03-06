package de.dhbwheidenheim.informatik.chatServer.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwheidenheim.informatik.chatServer.model.ChatRoom;
import de.dhbwheidenheim.informatik.chatServer.model.Person;
import de.dhbwheidenheim.informatik.chatServer.model.VideoCall;
import de.dhbwheidenheim.informatik.chatServer.model.enums.CallState;
import de.dhbwheidenheim.informatik.chatServer.model.enums.PersonState;
import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("")
public class ChatServerController {
	static List<Person> personList;
	static List<VideoCall> callsList;
	static List<ChatRoom> roomsList;

	public static void init() {
		personList = read("./persons.txt");
		if (personList == null)
			personList = new ArrayList<Person>();
		else
			for (Person p : personList)
				p.setState(PersonState.OFFLINE);

		callsList = read("./calls.txt");
		if (callsList == null)
			callsList = new ArrayList<VideoCall>();
		else
			for (VideoCall vc : callsList)
				vc.setCallState(CallState.OVER);

		roomsList = read("./rooms.txt");
		if (roomsList == null)
			roomsList = new ArrayList<ChatRoom>();
		else
			for (ChatRoom cr : roomsList)
				cr.setState(RoomState.FREE);

	}

	public static void saveData() {
		save(personList, "./persons.txt");
		save(callsList, "./calls.txt");
		save(roomsList, "./rooms.txt");
	}

	public static void save(List list, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
			System.out.println("Daten gespeichert!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List read(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			List list = (List) ois.readObject();
			fis.close();
			ois.close();
			return list;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Registriert eine neue Person
	 * 
	 * @param firstName Vorname
	 * @param lastName  Nachname
	 * @param password  Passwort
	 * @return username oder "" wenn name nicht verf??gbar
	 */
	@GetMapping("/registerPerson")
	public @ResponseBody String registerPerson(@RequestParam String username, @RequestParam String password) {
		// username auf verf??gbarkeit pr??fen
		for (Person p : personList)
			if (p.getUsername().equals(username))
				return "Fehler der Username ist bereits vergeben";
		personList.add(new Person(username, password));
		saveData();
		return username;
	}

	/**
	 * Pr??ft die Korrektheit des Passworts
	 * 
	 * @param userID   Vorname
	 * @param password Passwort
	 * @return true, wenn Passwort korrekt ist, sonst false
	 */
	@GetMapping("/login")
	public @ResponseBody boolean login(@RequestParam String username, @RequestParam String password) {
		// Person identifizieren
		for (Person p : personList)
			if (p.getUsername().equals(username))
				if (p.getPassword().equals(password)) {
					p.setState(PersonState.ONLINE);
					return true;
				}

		return false;

	}

	/**
	 * ??ndert den Status eines Benutzer
	 * 
	 * @param username Benutzername
	 * @param state    Status, den der Nutzer setzen m??chte
	 * @return true wenn erfolgreich, false wenn Person nicht gefunden wurde
	 */
	@GetMapping("/setPersonState")
	public @ResponseBody boolean setPersonState(@RequestParam String username, @RequestParam String state) {

		for (Person p : personList) {
			if (p.getUsername().equals(username)) {
				p.setState(PersonState.valueOf(state.toUpperCase()));
				return true;
			}
		}
		return false;
	}

	/**
	 * Erstellt einen neuen Anruf
	 * 
	 * @param CallType      Ob privat oder public
	 * @param organizername Username der Person, die den Anruf startet
	 * @return CallID, null wenn Organisator nicht gefunden wurde oder kein freier
	 *         Raum verf??gbar ist
	 */
	@GetMapping("/newCall")
	public @ResponseBody Object newCall(@RequestParam String callType, @RequestParam String organizername,
			@RequestParam String[] invitees) {
		// Organisator identifizieren
		Person org = null;
		for (Person p : personList) {
			if (p.getUsername().equals(organizername)) {
				org = p;
				break;
			}
		}
		if (org != null) {
			// Pr??fen, ob ein freier Raum verf??gbar ist
			for (ChatRoom cr : roomsList) {
				if (cr.getState() == RoomState.FREE) {
					boolean privateCall = callType.equalsIgnoreCase("private") ? true : false;
					VideoCall nCall = new VideoCall(callsList.size(), org, cr, privateCall);
					cr.setState(RoomState.OCCUPIED);
					for (String s : invitees) {
						// Person identifizieren
						for (Person p : personList)
							if (p.getUsername().equals(s)) {
								nCall.invitePerson(p);
							}
					}
					callsList.add(nCall);
					saveData();
					return nCall.getId();
				}
			}
			System.out.println("kein freier Raum");

		} else
			System.out.println("Organisator nicht gefunden");
		return null;
	}

	/**
	 * Nutzer verl??sst Anruf
	 * 
	 * @param username Verlassende Person
	 * @param callID   zu verlassender Anruf
	 * @return true wenn erfolgreich, false wenn Person oder Anruf nicht gefunden
	 *         wurden.
	 */
	@GetMapping("/leaveCall")
	public @ResponseBody boolean leaveCall(@RequestParam String username, @RequestParam String callID) {
		// Person identifizieren
		Person user = null;
		for (Person p : personList) {
			if (p.getUsername().equals(username)) {

				// Anruf identifizieren
				for (VideoCall call : callsList) {
					if (call.getId() == Integer.parseInt(callID)) {
						call.personLeaves(p);
						// Status der Person aktualisieren
						updatePersonState(p);
						return true;
					}
				}
				return false;
			}
		}

		return false;
	}

	/**
	 * Pr??ft, ob Person noch in einem Anruf ist und setzt den Status von Busy zu
	 * Online, wenn nicht
	 * 
	 * @param p
	 */
	public void updatePersonState(Person p) {
		if (p.getState().equals(PersonState.BUSY)) {
			boolean personInCall = false;
			for (VideoCall vc : callsList) {
				if (vc.getAttendees().contains(p))
					personInCall = true;
			}
			if (!personInCall)
				p.setState(PersonState.ONLINE);
		}
	}

	/**
	 * Person tritt einem bestehenden Anruf bei
	 * 
	 * @param callID   AnrufID
	 * @param username Person, die beitritt
	 * @return Raum-URL, wenn erfolgreich, null wenn Anruf oder beitretende Person
	 *         nicht gefunden wurden, Person nicht eingeladen wurde oder anruf
	 *         bereits vorbei ist
	 */
	@GetMapping("/joinCall")
	public @ResponseBody String joinCall(@RequestParam String callID, @RequestParam String username) {
		// person identifizieren
		for (Person p : personList)
			if (p.getUsername().equals(username))
				// Anruf identifizieren
				for (VideoCall vc : callsList)
					if (vc.getId() == Integer.parseInt(callID))
						// personJoins enth??lt abfrage bez??glich der berechtigung
						if (vc.personJoins(p)) {
							p.setState(PersonState.BUSY);
							return vc.getChatRoom().getRoomURL().toString();
						}
		return null;
	}

	/**
	 * L??dt Person zu einem Anruf ein
	 * 
	 * @param callID   AnrufID
	 * @param joinerID Person, die eingeladen werden soll
	 * @return false, wenn Anruf oder Person nicht gefunden oder Anruf bereits
	 *         vorbei oder Person nicht Online ist
	 */
	@GetMapping("/inviteUserToCall")
	public @ResponseBody boolean inviteUserToCall(@RequestParam String callID, @RequestParam String inviteeUsername) {
		// person identifizieren
		for (Person p : personList)
			if (p.getUsername().equals(inviteeUsername) && p.getState().equals(PersonState.ONLINE))
				for (VideoCall call : callsList)
					if (call.getId() == Integer.parseInt(callID)) // callState wird in invitePerson gepr??ft
						return call.invitePerson(p);
					else
						break;
			else
				break;
		return false;
	}

	/**
	 * Legt einen neuen Chatraum an
	 * 
	 * @param roomURL trusted Link zu BBB-Raum
	 * @return RoomID, null wenn URL nicht g??ltig ist oder raum bereits vorhanden
	 */
	@GetMapping("/newRoom")
	public @ResponseBody Object newRoom(@RequestParam String roomURL) {
		try {
			ChatRoom nRoom = new ChatRoom(roomsList.size(), new URL(roomURL));

			roomsList.add(nRoom);
			saveData();
			return nRoom.getId();

		} catch (MalformedURLException e) {
			// Fehler bei ung??ltiger URL abfangen
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Pr??ft, ob Person gerade angerufen wird
	 * 
	 * @param userID Fragende Person
	 * @return null wenn kein anruf oder nutzer nicht existiert, sonst der Anruf
	 */
	@RequestMapping("/amICalled")
	public VideoCall amICalled(@RequestParam String username) {
		// anfragende Person identifizieren
		for (Person p : personList)
			if (p.getUsername().equals(username))
				for (VideoCall vc : callsList) {
					// Nur aktive oder wartende Anrufe pr??fen
					if (vc.getCallState() != CallState.OVER)
						// Teilnehmer auf frangenden durchsuchen
						if (vc.getInvitees().contains(p))
							return vc;
				}

		return null;
	}

	/**
	 * Liste ??ber alle Personen
	 * 
	 * @return JSON-Array mit allen Benutzernamen und jeweiligem Status
	 */
	@RequestMapping("/listPersons")
	public String getPersons() {
		JSONArray persons = new JSONArray();
		for (Person p : personList) {
			JSONObject obj = new JSONObject();
			obj.appendField("username", p.getUsername());
			obj.appendField("state", p.getState());
			persons.add(obj);
		}
		return persons.toJSONString();
	}

	@RequestMapping("/listRooms")
	public List<ChatRoom> getRooms() {
		return roomsList;
	}

	@RequestMapping("/listCalls")
	public List<VideoCall> getCalls() {
		return callsList;
	}

	@RequestMapping("/")
	public String home() {
		return "";
	}

}