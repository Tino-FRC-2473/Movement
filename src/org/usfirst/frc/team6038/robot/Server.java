package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.framework.components.Trackers;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(6968);
		System.out.println("Server socket established");
		Socket ss = server.accept();
		System.out.println("Server accepting");
		PrintStream p = new PrintStream(ss.getOutputStream());
		System.out.println("Printstream established");
		Scanner sc = new Scanner(System.in);
		System.out.println("Server Running... ");
		
		while (isRobotRunning()){
			p.println("Right Encoder: "+getData()[0]);
			p.println("Left Encoder: "+getData()[1]);
		}
		
		server.close();
	}
	
	private static double[] getData(){
		return new double[]{Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC), Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC)};
	}
	private static boolean isRobotRunning(){
		boolean frrunning = Database.getInstance().getNumeric("pfr")>0;
		boolean flrunning = Database.getInstance().getNumeric("pfl")>0;
		boolean brrunning = Database.getInstance().getNumeric("pbr")>0;
		boolean blrunning = Database.getInstance().getNumeric("pbl")>0;
		return frrunning||flrunning||brrunning||blrunning;
	}
	
}