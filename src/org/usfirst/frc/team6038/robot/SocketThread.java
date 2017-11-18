package org.usfirst.frc.team6038.robot;

import java.io.IOException;

public class SocketThread extends Thread{
	private Server server;
	public SocketThread(int port) throws IOException{
		server = new Server(port);
	}
	@Override
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			try {
				server.iterate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void kill() throws IOException{
		server.close();
		interrupt();
	}
}
