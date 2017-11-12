package org.usfirst.frc.team6038.robot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTemplate 
{
	public static void main(String[] args) throws IOException 
	{
		String ip = "172.20.10.9"; //  "10.19.88.151"Gary's pc IP
		int port_number = 1776;
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
}