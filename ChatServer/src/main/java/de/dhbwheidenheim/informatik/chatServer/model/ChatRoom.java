package de.dhbwheidenheim.informatik.chatServer.model;

import java.net.URL;

import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;


public class ChatRoom {
		
	private URL roomURL;
	private RoomState state;

	public ChatRoom(URL roomURL) {
		this.roomURL=roomURL;
		state=RoomState.FREE;
	}
	
	public URL getRoomURL() {
		return roomURL;
	}

	public RoomState getState() {
		return state;
	}
	
	
	public void setState(RoomState state) {
		this.state=state;
	}
	
	
	

	
	
}
