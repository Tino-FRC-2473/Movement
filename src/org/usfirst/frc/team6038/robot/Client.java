package org.usfirst.frc.team6038.robot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;

public class Client 
{

	public static void main(String[] args) throws IOException 
	{
		String ip = "10.24.73.2"; 
		int port_number = 1939;
		int trialNumber = 2;
		File f = new File("data.txt");
		FileWriter fw1 = new FileWriter(f, false);

		Socket s = new Socket(ip, port_number);
		Scanner scan = new Scanner(s.getInputStream());
		System.out.println("Connected.");
		while (scan.hasNextLine()) 
		{
			String a = scan.nextLine();
			System.out.println("Socket receiving: " + a);
			
			if(!a.equals(RobotMap.NO_TRIALS)){
				fw1.write(a+"\n");
			}
			else
			{
				System.out.println("Trial " + trialNumber + "\n");
				trialNumber++;
			}
		}
		s.close();
		scan.close();
		fw1.close();
	}
}

