package org.usfirst.frc.team6038.robot;
import java.io.IOException;

/**
 * Main socket class that does everything. Constantly pings and can be configured to perform other actions like request values.
 * @author wang.patrick57@gmail.com
 * @author JosephM
 */
public class SocketPingThread extends Thread {
	/* Abbreviations of methods (used for stuffToDoEveryTick and variable names)
	 * requestValues(): rv
	 */
	
	private UtilitySocket us;
	private boolean alive;
	private long lastTimeRV;
	private long delayRV = 1000; //how often to request values
	private long currTime;
	private long startTime;
	private String[] stuffToDoEveryTick; //array that stores what the client will do every tick

	private static final int COORDINATES_WORD_LENGTH = 13;
	private static final String MV_COMP_IP = "10.19.80.131"; //fuhsd
	private static final String JETSON_IP = "10.19.48.81"; //fuhsd_guests
	
	/**
	 * Creates a UtilitySocket with the ip and port and sets up all the necessary variables.
	 * @throws IOException If an I/O error occurs when creating the socket
	 */
	public SocketPingThread() throws IOException {
		us = new UtilitySocket(JETSON_IP, 50007);
		System.out.println("connected to server");
		alive = true;
		lastTimeRV = System.currentTimeMillis();
		startTime = lastTimeRV;
	}
	
	/**
	 * The method that is run every tick. It pings the server no matter what, and it also runs all the commands in stuffToDoEveryTick
	 */
	@Override
	public void run(){
		while(alive) {
			currTime = System.currentTimeMillis();
			everyTick();
			String line = us.getLine();
			
			if(line != null) {
				System.out.println("received " + line);
				
				if(line.equals("s")) {
					System.out.println("server ping received");
					us.sendLine("c");
				} else {
					if (line.substring(0, COORDINATES_WORD_LENGTH).equals("coordinates: ")) {
						System.out.println("coordinates received");
					}
				}
			}
			
			if(currTime - startTime > 3000) {
				us.sendLine("done");
				end();
				alive = false;
			}
		}
		
		System.out.println("dead");
	}
	
	/**
	 * This method runs all the commands in stuffToDoEveryTick
	 */
	public void everyTick() {
		for (String s : stuffToDoEveryTick) {
			if (s.equals("rv")) {
				requestValues();
			}
		}
	}
	
	/**
	 * This method requests values from the server.
	 */
	public void requestValues() {
		if (currTime - lastTimeRV > delayRV) {
			lastTimeRV = currTime;
			us.sendLine("function triggered");
			System.out.println("requesting values");
		}
	}
	
	/**
	 * This method closes the UtilitySocket and kills the SocketPingThread
	 */
	public void end() {
		System.out.println("ENDING");
		alive = false;
		
		try {
			us.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}