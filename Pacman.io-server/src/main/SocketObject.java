package main;

import java.util.UUID;

public class SocketObject {
	public String message;
	
	public SocketObject() {
		message = "";
	}
	
	public SocketObject(String message) {
		this.message = message;
	}
}

class PlayerData {
	public String x;
	public String y;
	public String angle;
	public String id;
	public String type;
	public String animation;
	
	public PlayerData() {
		
	}
	
	public PlayerData(String x, String y, String angle, String id, String type, String animation) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.id = id;
		this.type = type;
		this.animation = animation;
	}
}