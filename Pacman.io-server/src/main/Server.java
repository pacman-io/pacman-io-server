package main;

import com.corundumstudio.socketio.Configuration;

import java.lang.Math;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Calendar;

public class Server {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(9092);
		

		ServerThread st = new ServerThread(config);
		st.start();
	}
	
	
	public void createServer(int port) {
		
	}

}

class ServerThread extends Thread {
	private SocketIOServer server;
	private boolean isTerminated = false;
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private HashMap<Integer, Pacman> pacmans = new HashMap<Integer, Pacman>();
	private HashMap<Integer, Ghost> ghosts = new HashMap<Integer, Ghost>();
	private Vector<Coordinate> removedDots = new Vector<Coordinate>();
	private int playerID = 1;

	public ServerThread(Configuration config) {
		server = new SocketIOServer(config);

		server.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {
				System.out.println("Connected: " + client.getRemoteAddress());
			}
		});

		server.addDisconnectListener(new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				for(Player p : players.values()) {
					if(client.getSessionId().equals(p.uuid)) {
						players.remove(p.id);
						if(p.getClass() == Pacman.class) {
							pacmans.remove(p.id);
							server.getBroadcastOperations().sendEvent("remove", p.id, "pacman");
						}
							
						else if(p.getClass() == Ghost.class) {
							ghosts.remove(p.id);
							server.getBroadcastOperations().sendEvent("remove", p.id, "ghost");
						}
					}
				}
			}
		});

		server.addEventListener("newplayer", SocketObject.class, new DataListener<SocketObject>() {
			@Override
			public void onData(SocketIOClient client, SocketObject data, AckRequest ackSender) throws Exception {
				if(players.isEmpty()) {
					Pacman p = new Pacman(13.0 * 16.0 + 8.0, 26.0 * 16.0 + 8.0, playerID, client.getSessionId());
					players.put(playerID, p);
					pacmans.put(playerID++, p);
					client.sendEvent("allpacmans", pacmans.values());
					client.sendEvent("allghosts", ghosts.values());
					server.getBroadcastOperations().sendEvent("newpacman", client, p);
				}
				else {
					Ghost g = new Ghost(13.0 * 16.0 + 8.0, 26.0 * 16.0 + 8.0, playerID, client.getSessionId());
					players.put(playerID, g);
					ghosts.put(playerID++, g);
					client.sendEvent("allpacmans", pacmans.values());
					client.sendEvent("allghosts", ghosts.values());
					server.getBroadcastOperations().sendEvent("newghost", client, g);
				}
				client.sendEvent("removealldots", removedDots);
			}
		});

		server.addEventListener("playermovemnet", PlayerData.class, new DataListener<PlayerData>() {
			@Override
			public void onData(SocketIOClient client, PlayerData data, AckRequest ackSender) throws Exception {
				System.out.println(Util.getTime());
				Player p;
				if((p = players.get(Integer.parseInt(data.id))) != null) {
					p.x = Double.parseDouble(data.x);
					p.y = Double.parseDouble(data.y);
					p.angle = Integer.parseInt(data.angle);
					server.getBroadcastOperations().sendEvent("playermovemnet", client, p, data.type);
				}

			}
		});

		server.addEventListener("removedot", PlayerData.class, new DataListener<PlayerData>() {
			@Override
			public void onData(SocketIOClient client, PlayerData data, AckRequest ackSender) throws Exception {
				Coordinate c = new Coordinate(Double.parseDouble(data.x), Double.parseDouble(data.y));
				removedDots.add(c);
				server.getBroadcastOperations().sendEvent("removedot", client, c);
			}
		});
	}

	public void run() {
		server.start();
		while (!isTerminated) {

		}
		server.stop();
	}
}

class Util {
	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		String time = "[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND) + "." + cal.get(Calendar.MILLISECOND) + "] ";
		return time;
	}
}