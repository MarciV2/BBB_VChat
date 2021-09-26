package de.dhbwheidenheim.informatik.chatServer.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
	 * @param password Passwort
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
	 * Ändert den Status eines Benutzer
	 * @param userID UserID des Benutzers
	 * @param state Status, den der Nutzer setzen möchte
	 * @return true wenn erfolgreich, false wenn Person nicht gefunden wurde
	 */
	@GetMapping("/setPersonState")
	public @ResponseBody boolean setPersonState(@RequestParam String userID, @RequestParam String state) {
		//Person identifizieren
		Person p=personList.get(Long.parseLong(userID));
		if(p!=null) {
			p.setState(PersonState.valueOf(state.toUpperCase()));
			return true;
		}return false;
	}


	/**
	 * Erstellt einen neuen Anruf
	 * @param CallType Ob privat oder public
	 * @param organizerID Person, die den Anruf startet
	 * @return CallID, null wenn Organisator nicht gefunden wurde oder kein freier Raum verfügbar ist
	 */
	@GetMapping("/newCall")
	public @ResponseBody Object newCall(@RequestParam String callType, @RequestParam String organizerID) {
		//Organisator identifizieren
		Person org=personList.get(Long.parseLong(organizerID));
		if(org!=null) {
			//Prüfen, ob ein freier Raum verfügbar ist
			for(ChatRoom cr:roomsList.values()) {
				if(cr.getState()==RoomState.FREE) {
					boolean privateCall=callType.equalsIgnoreCase("private")? true : false;
					VideoCall nCall=new VideoCall(org, cr,privateCall);
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
	 * Nutzer verlässt Anruf
	 * @param leaverID Verlassende Person
	 * @param callID zu verlassender Anruf
	 * @return true wenn erfolgreich, false wenn Person oder Anruf nicht gefunden wurden.
	 */
	@GetMapping("/leaveCall")
	public @ResponseBody boolean leaveCall(@RequestParam String leaverID, @RequestParam String callID) {
		//Person identifizieren
		Person p=personList.get(Long.parseLong(leaverID));
		if(p!=null) {
			//Anruf identifizieren
			VideoCall call=callsList.get(Long.parseLong(callID));
			if(call!=null) {
				call.personLeaves(p);
				//Status der Person aktualisieren
				updatePersonState(p);
				return true;
			}

		}	
		return false;
	}

	/**
	 * Prüft, ob Person noch in einem Anruf ist und setzt den Status von Busy zu Online, wenn nicht
	 * @param p
	 */
	public void updatePersonState(Person p) {
		if(p.getState().equals(PersonState.BUSY)) {
			boolean personInCall=false;
			for(VideoCall vc : callsList.values()) {
				if(vc.getAttendees().contains(p)) personInCall=true;
			}
			if(!personInCall) p.setState(PersonState.ONLINE);
		}
	}

	/**
	 * Person tritt einem bestehenden Anruf bei
	 * @param callID AnrufID
	 * @param joinerID Person, die beitritt
	 * @return Raum-URL, wenn erfolgreich, null wenn Anruf oder beitretende Person nicht gefunden wurden, Person nicht eingeladen wurde oder anruf bereits vorbei ist
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
				if(call.personJoins(joiner)) {
					joiner.setState(PersonState.BUSY);
					return call.getChatRoom().getRoomURL().toString();
				}
			}
		}
		return null;
	}

	/**
	 * Lädt Person zu einem Anruf ein
	 * @param callID AnrufID
	 * @param joinerID Person, die eingeladen werden soll
	 * @return false, wenn Anruf oder Person nicht gefunden oder Anruf bereits vorbei oder Person nicht Online ist
	 */
	@GetMapping("/inviteUserToCall")
	public @ResponseBody boolean inviteUserToCall(@RequestParam String callID, @RequestParam String inviteeID) {
		//person identifizieren
		Person invitee=personList.get(Long.parseLong(inviteeID));
		if(invitee!=null) {
			//Prüfen, ob Person online ist (andernfalls abbrechen)
			if(!invitee.getState().equals(PersonState.ONLINE)) {return false;}
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
		//anfragende Person identifizieren
		Person asker=personList.get(Long.parseLong(userID));
		if(asker==null) return null;
		for(VideoCall vc:callsList.values()) {
			//Nur aktive oder wartende Anrufe prüfen
			if(vc.getCallState()!=CallState.OVER)
				//Teilnehmer auf frangenden durchsuchen
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
	
	@RequestMapping("/listCalls")
	public Map<Long,VideoCall> getCalls() {
		return callsList;
	}
	

	@RequestMapping("/")
	public String home() {
		return ("<html>" 
				+ "<h1>BBB-Chat-Server</12>" 

				+ "<h2>Person registrieren:</h2>" 
				+ "<form action=\"/registerPerson\" Method=\"GET\">"
				+ "Vorname: <input name=\"firstName\" type=\"input\"/><br>" 
				+ "Nachname: <input name=\"lastName\" type=\"input\"/><br>"
				+ "Passwort: <input name=\"password\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"

				+ "<h2>Raum anlegen:</h2>" 
				+ "<form action=\"/newRoom\" Method=\"GET\">"
				+ "Raum-URL: <input name=\"roomURL\" type=\"input\"/><br>" 
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"

				+ "<h2>Anruf starten:</h2>" 
				+ "<form action=\"/newCall\" Method=\"GET\">"
				+ "Anruf-Typ:<br> <input type=\"checkbox\" id=\"public\" name=\"callType\" value=\"public\">"
				+ "  <label for=\"public\"> Öffentlich</label><br>"
				+ "  <input type=\"checkbox\" id=\"private\" name=\"callType\" value=\"private\">"
				+ "  <label for=\"private\"> Privat</label><br>" 
				+ "Organisator-ID: <input name=\"organizerID\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"

				+ "<h2>Werde ich angerufen?</h2>" 
				+ "<form action=\"/amICalled\" Method=\"GET\">"
				+ "User-ID: <input name=\"userID\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Personen auflisten:</h2>" 
				+ "<form action=\"/listPersons\" Method=\"GET\">"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Räume auflisten:</h2>" 
				+ "<form action=\"/listRooms\" Method=\"GET\">"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Anrufe auflisten:</h2>" 
				+ "<form action=\"/listCalls\" Method=\"GET\">"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Anruf beitreten:</h2>" 
				+ "<form action=\"/joinCall\" Method=\"GET\">"
				+ "Anruf-ID: <input name=\"callID\" type=\"input\"/><br>" 
				+ "User-ID: <input name=\"joinerID\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Person zu Anruf einladen:</h2>" 
				+ "<form action=\"/inviteUserToCall\" Method=\"GET\">"
				+ "Anruf-ID: <input name=\"callID\" type=\"input\"/><br>" 
				+ "User-ID: <input name=\"inviteeID\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Anruf verlassen:</h2>" 
				+ "<form action=\"/leaveCall\" Method=\"GET\">"
				+ "Anruf-ID: <input name=\"callID\" type=\"input\"/><br>" 
				+ "User-ID: <input name=\"leaverID\" type=\"input\"/><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "<h2>Status ändern:</h2>" 
				+ "<form action=\"/setPersonState\" Method=\"GET\">"
				+ "User-ID: <input name=\"userID\" type=\"input\"/><br>" 
				+ "Status: <input type=\"radio\" id=\"online\" name=\"state\" value=\"online\">"
				+ "  <label for=\"online\">ONLINE</label><br>"
				+ "  <input type=\"radio\" id=\"offline\" name=\"state\" value=\"offline\">"
				+ "  <label for=\"offline\">OFFLINE</label><br>"
				+ "  <input type=\"radio\" id=\"doNotDisturb\" name=\"state\" value=\"doNotDisturb\">"
				+ "  <label for=\"doNotDisturb\">Nicht Stören</label><br>"
				+ "<input type=\"submit\"/><br>" 
				+ "</form>"
				
				+ "</html>\n");
	}

}