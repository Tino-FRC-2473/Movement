package org.usfirst.frc.team6038.robot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;

public class DriveCodeClient 
{

	public static void main(String[] args) throws IOException 
	{
		String ip = "172.22.11.2"; 
		int port_number = 6968;
		File f = new File("data.txt");
		FileWriter fw1 = new FileWriter(f);

		Socket s = new Socket(ip, port_number);
		Scanner scan = new Scanner(s.getInputStream());
		System.out.println("Connected.");
		while (scan.hasNextLine()) 
		{
			String a = scan.nextLine();
			System.out.println(a);
			fw1.write(a);
		}
		s.close();
		scan.close();
		fw1.close();
	}
	
	
	private static boolean isRobotMoving(){
		boolean frrunning = Database.getInstance().getNumeric("pfr")>0;
		boolean flrunning = Database.getInstance().getNumeric("pfl")>0;
		boolean brrunning = Database.getInstance().getNumeric("pbr")>0;
		boolean blrunning = Database.getInstance().getNumeric("pbl")>0;
		return frrunning||flrunning||brrunning||blrunning;
	}
}

