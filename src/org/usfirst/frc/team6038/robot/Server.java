package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import org.usfirst.frc.team6038.framework.Database;

public class Server 
{
	private PrintStream p;
	private Socket ss;
	private ServerSocket server;
	
	public Server(int port) throws IOException
	{
		server = new ServerSocket(port);
		System.out.println("Server socket established");
		ss = server.accept();
		System.out.println("Server accepting");
		p = new PrintStream(ss.getOutputStream(), false);
		Robot.tempData = new ArrayBlockingQueue<String>(6);
		System.out.println("Server Running... ");
		FlusherThread flusher = new FlusherThread(p);
		flusher.start();
		UpdaterThread updater = new UpdaterThread();
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
	
	public void close() throws IOException
	{
		p.flush();
		server.close();
	}
}