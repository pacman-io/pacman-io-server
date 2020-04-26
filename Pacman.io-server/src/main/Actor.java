package main;

import java.util.UUID;

class Actor {
	public double x;
	public double y;
	public int angle = 0;
	public int id;
	
	public Actor(double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
}

class Player extends Actor {
	public UUID uuid;
	
	public Player(double x, double y, int id, UUID uuid) {
		super(x, y, id);
		this.uuid = uuid;
	}
	
}

class Pacman extends Player {
	public int lives;
	public int score;
	
	public Pacman(double x, double y, int id, UUID uuid) {
		super(x, y, id, uuid);
		lives = 99;
		score = 0;
	}
	
}

class Ghost extends Player {
	public enum GhostState{
		scatter, chase, dead
	}
	public enum GhostType{
		blinky, speedy, inky, clyde
	}
	
	public int kills;
	public GhostState state;
	public GhostType type;
	public boolean isUnderControl;
	
	
	public Ghost(double x, double y, int id, UUID uuid, GhostType type) {
		super(x, y, id, uuid);
		this.type = type;
		kills = 0;
		state = GhostState.chase;
		isUnderControl = false;
	}
	
}