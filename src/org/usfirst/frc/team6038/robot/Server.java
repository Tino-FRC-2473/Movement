package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	private PrintStream p;
	private Socket ss;
	private ServerSocket serverSocket;
	private FlusherThread flusher;
	private UpdaterThread updater;
	
	public Server(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		System.out.println("Server socket established");
		ss = serverSocket.accept();
		System.out.println("Server accepting");
		p = new PrintStream(ss.getOutputStream(), false);
		System.out.println("Server Running... ");
		flusher = new FlusherThread(p);
		flusher.start();
		updater = new UpdaterThread();
		updater.start();
	}

//	private double[] getData()
//	{
//		return new double[]
//				{
//						Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC), 
//						Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC),
//						Database.getInstance().getNumeric("pfr"),
//						Database.getInstance().getNumeric("pfl"),
//						Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z),
//						Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X),
//						Database.getInstance().getNumeric(RobotMap.GYRO_YAW)
//				};
//	}
	
	public void closeServer()
	{
		p.flush();
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flusher.endFlusher();
		updater.endUpdater();
	}
}