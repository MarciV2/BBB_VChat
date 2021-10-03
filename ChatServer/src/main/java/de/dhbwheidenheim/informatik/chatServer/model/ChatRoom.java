package de.dhbwheidenheim.informatik.chatServer.model;

import java.io.Serializable;
import java.net.URL;

import de.dhbwheidenheim.informatik.chatServer.model.enums.RoomState;


public class ChatRoom implements Serializable{
	private static final long serialVersionUID = 1L;
		private int id;
	private URL roomURL;
	private RoomState state;

	public ChatRoom(int id,URL roomURL) {
		this.id=id;
		this.roomURL=roomURL;
		state=RoomState.FREE;
	}
	
	public int getId() {
		return id;
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
