package de.dhbwheidenheim.informatik.chatServer.model;

import java.util.ArrayList;
import java.util.List;

import de.dhbwheidenheim.informatik.chatServer.model.enums.CallState;
import de.dhbwheidenheim.informatik.chatServer.model.enums.CallType;
import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;

public class VideoCall {

	private ChatRoom chatRoom;
	private CallType callType;
	private CallState callState;
	private Person organizer; 
	private List<Person> invitees;
	private List<Person> attendees;


	public VideoCall(Person organizer, ChatRoom chatRoom ,CallType callType) {
		this.organizer=organizer;
		this.chatRoom=chatRoom;
		this.callType=callType;
		this.callState=CallState.IN_PROGRESS;
		invitees=new ArrayList<Person>();
		invitees.add(organizer);
		attendees=new ArrayList<Person>();
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
		
	public CallType getCallType() {
		return callType;
	}

	public CallState getCallState() {
		return callState;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
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
			if(callType==CallType.PRIVATE) {
				if(!invitees.contains(joiner)) {
					//Bei PRIVAT sollen nicht eingeladene Personen nicht beitreten können
					return false;
				}
			}
			
			attendees.add(joiner);
			return true;
			
		}else return false;
	}
	
	
	/**
	 * Event, wenn Person den Raum verlässt. Wenn letzte Person geht, wird der Anruf beendet und der Raum wieder freigegeben
	 * @param leaver verlassende Person
	 */
	public void leaveRoom(Person leaver) {
		attendees.remove(leaver);
		if(attendees.size()==0) {
			callState=CallState.OVER;
			chatRoom.setState(RoomState.FREE);
		}
	}
	
	
}
