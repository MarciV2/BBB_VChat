package de.dhbwheidenheim.informatik.chatServer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.dhbwheidenheim.informatik.chatServer.model.enums.CallState;
import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class VideoCall implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private ChatRoom chatRoom;
	private boolean privateCall;
	private CallState callState;
	private Person organizer; 
	private List<Person> invitees;
	private List<Person> attendees;


	public VideoCall(int id,Person organizer, ChatRoom chatRoom ,boolean privateCall) {
		this.id=id;
		this.organizer=organizer;
		this.chatRoom=chatRoom;
		this.privateCall=privateCall;
		this.callState=CallState.IN_PROGRESS;
		invitees=new ArrayList<Person>();
		invitees.add(organizer);
		attendees=new ArrayList<Person>();
	}
	@Override
	public String toString() {
		System.out.println("toString");
		return null;
	}
	public int getId() {
		return id;
	}
	public Person getOrganizer() {
		return organizer;
	}

	public List<Person> getInvitees() {
		return invitees;
	}

	public List<Person> getAttendees() {
		return attendees;
	}

	public boolean isCallPrivate() {
		return privateCall;
	}

	public CallState getCallState() {
		return callState;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setCallState(CallState state) {
		this.callState=state;
	}


	/**
	 * Lädt einzelne Person dem Anruf ein
	 * @param invitee
	 */
	public boolean invitePerson(Person invitee) {
		if(callState==CallState.OVER) return false;
		invitees.add(invitee);
		return true;
	}


	public boolean personJoins(Person joiner) {
		if(callState!=CallState.OVER) {
			//Bei BEENDET sollen keine Personen mehr beitreten können
			if(privateCall) {
				if(!invitees.contains(joiner)) {
					//Bei PRIVAT sollen nicht eingeladene Personen nicht beitreten können
					return false;
				}
			}
			invitees.remove(joiner);
			attendees.add(joiner);
			return true;

		}else return false;
	}


	/**
	 * Event, wenn Person den Raum verlässt. Wenn letzte Person geht, wird der Anruf beendet und der Raum wieder freigegeben
	 * @param leaver verlassende Person
	 */
	public void personLeaves(Person leaver) {
		invitees.remove(leaver);
		attendees.remove(leaver);
		if(attendees.size()==0) {
			callState=CallState.OVER;
			chatRoom.setState(RoomState.FREE);
		}
	}


}
