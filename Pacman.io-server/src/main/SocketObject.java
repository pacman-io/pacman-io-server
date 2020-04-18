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


class Player {
	public double x;
	public double y;
	public int angle = 0;
	public int id;
	public UUID uuid;
	
	public Player(double x, double y, int id, UUID uuid) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.uuid = uuid;
	}
}

class PlayerData {
	public String x;
	public String y;
	public String angle;
	public String id;
	
	public PlayerData() {
		
	}
	
	public PlayerData(String x, String y, String angle, String id) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.id = id;
	}
}