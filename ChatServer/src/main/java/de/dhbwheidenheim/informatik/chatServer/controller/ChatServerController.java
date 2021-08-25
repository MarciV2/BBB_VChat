package de.dhbwheidenheim.informatik.chatServer.controller;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwheidenheim.informatik.chatServer.model.*;
import de.dhbwheidenheim.informatik.chatServer.model.enums.CallState;
import de.dhbwheidenheim.informatik.chatServer.model.enums.CallType;
import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;

@RestController
@RequestMapping("")
public class ChatServerController {
	static Map<Long,Person> personList;
	static Map<Long,VideoCall>callsList;
	static Map<Long,ChatRoom>roomsList;

	public static void init() {
		personList=new HashMap<Long,Person>();
		callsList=new HashMap<Long,VideoCall>();
		roomsList=new HashMap<Long,ChatRoom>();
	}



	/**
	 * Registriert eine neue Person
	 * @param firstName Vorname
	 * @param lastName Nachname
	 * @param password PAsswort
	 * @return ID des Nutzers
	 */
	@GetMapping("/registerPerson")
	public @ResponseBody long registerPerson(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String password) {
		//get nexst id
		long id=(long) personList.size();
		personList.put(id,new Person(firstName, lastName, password));
		return id;
	}

	/**
	 * Erstellt einen neuen Anruf
	 * @param CallType Ob privat oder public
	 * @param organizerID Person, die den Anruf startet
	 * @return CallID, null wenn organisator nicht gefunden wurde oder kein freier raum verfügbar
	 */
	@GetMapping("/newCall")
	public @ResponseBody Object newCall(@RequestParam String callType, @RequestParam String organizerID) {
		//Organisator identifizieren
		Person org=personList.get(Long.parseLong(organizerID));
		if(org!=null) {
			//Prüfen, ob freier raum verfügbar ist
			for(ChatRoom cr:roomsList.values()) {
				if(cr.getState()==RoomState.FREE) {
					VideoCall nCall=new VideoCall(org, cr,CallType.valueOf(callType.toUpperCase()));
					cr.setState(RoomState.OCCUPIED);
					long id=(long) callsList.size();
					callsList.put(id,nCall);
					System.out.println(id);
					return id;
				}
			} System.out.println("kein freier Raum");

		}else System.out.println("Organisator nicht gefunden");
		return null;
	}

	/**
	 * Person tritt einem bestehenden Anruf bei 
	 * @param callID AnrufID
	 * @param joinerID Person, die beitritt werden soll
	 * @return Raum-URL, wenn erfolgreich, null wenn Anruf oder beitretende Person nicht gefunden wurden, person nicht eingeladen wurde oder anruf bereits vorbei ist
	 */
	@GetMapping("/joinCall")
	public @ResponseBody String joinCall(@RequestParam String callID, @RequestParam String joinerID) {
		//person identifizieren
		Person joiner=personList.get(Long.parseLong(joinerID));
		if(joiner!=null) {
			//Anruf identifizieren
			VideoCall call=callsList.get(Long.parseLong(callID));
			if(call!=null) {
				//personJoins enthält abfrage bezüglich der berechtigung
				if(call.personJoins(joiner))return call.getChatRoom().getRoomURL().toString();
			}
		}
		return null;
	}

	/**
	 * Person tritt einem bestehenden Anruf bei 
	 * @param callID AnrufID
	 * @param joinerID Person, die beitritt werden soll
	 * @return false, wenn Anruf oder Person nicht gefunden oder Anruf bereits vorbei
	 */
	@GetMapping("/inviteUserToCall")
	public @ResponseBody boolean inviteUserToCall(@RequestParam String callID, @RequestParam String inviteeID) {
		//person identifizieren
		Person invitee=personList.get(Long.parseLong(inviteeID));
		if(invitee!=null) {
			//Anruf identifizieren
			VideoCall call=callsList.get(Long.parseLong(callID));
			if(call!=null) {
				return call.invitePerson(invitee);
			}
		}
		return false;
	}

	/**
	 * Legt einen neuen Chatraum an
	 * @param roomURL trusted Link zu BBB-Raum
	 * @return RoomID, null wenn URL nicht gültig ist oder raum bereits vorhanden
	 */
	@GetMapping("/newRoom")
	public @ResponseBody Object newRoom(@RequestParam String roomURL) {
		try {
			ChatRoom nRoom=new ChatRoom(new URL(roomURL));

			//Prüfen, dass Raum nicht bereits existiert
			if(roomsList.containsValue(nRoom)) return false;
			long id=(long) roomsList.size();
			roomsList.put(id, nRoom);
			return id;

		} catch (MalformedURLException e) {
			//Fehler bei ungültiger URL abfangen
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Prüft, ob Person gerade angerufen wird
	 * @param userID Fragende Person
	 * @return null wenn kein anruf oder nutzer nicht existiert, sonst der Anruf
	 */
	@RequestMapping("/amICalled")
	public VideoCall amICalled(@RequestParam String userID) {
		//identify asking user
		Person asker=personList.get(Long.parseLong(userID));
		if(asker==null) return null;
		for(VideoCall vc:callsList.values()) {
			//Nur aktive oder wartende anrufe prüfen
			if(vc.getCallState()!=CallState.OVER)
				//teilnehmer auf frangenden durchsuchen
				if(vc.getInvitees().contains(asker)) return vc;
		}
		return null;
	}

	@RequestMapping("/listPersons")
	public Map<Long,Person> getPersons() {
		return personList;
	}

	@RequestMapping("/listRooms")
	public Map<Long,ChatRoom> getRooms() {
		return roomsList;
	}

	@RequestMapping("/count")
	public int count() {
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