package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;

public class Server 
{

	public static boolean isRobotRunning = true;
	
	public Server() throws IOException
	{
		ServerSocket server = new ServerSocket(6968);
		System.out.println("Server socket established");
		Socket ss = server.accept();
		System.out.println("Server accepting");
		PrintStream p = new PrintStream(ss.getOutputStream());
		System.out.println("Printstream established");
		Scanner sc = new Scanner(System.in);
		System.out.println("Server Running... ");

		while (isRobotRunning)
		{
				// Right encoder Left encoder Front Right power Front Left power Throttle Steering wheel
				p.println(getData()[0]+" "+getData()[1]+" "+getData()[2]+" "+getData()[3]+ " "+getData()[4]+" "+getData()[5]);
		}
		server.close();
	}

	private double[] getData()
	{
		return new double[]
				{
						Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC), 
						Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC),
						Database.getInstance().getNumeric("pfr"),
						Database.getInstance().getNumeric("pfl"),
						Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z),
						Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X)
				};
	}
	
	public void turnOff()
	{
		isRobotRunning = false;
	}

>>>>>>> branch '2018' of https://github.com/Tino-FRC-2473/Movement.git
}