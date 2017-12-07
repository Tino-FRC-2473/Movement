package org.usfirst.frc.team6038.robot.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CVSegments extends CommandGroup {

	private final double MAX_POW = 0.8;
	private final double MIN_POW = 0.1;

	// socket stuff
	private final String JETSON_SOCKET = "filler ples change b0$$";
	private final int PORT_NUMBER = 6969;

	// cv stuff
	private double distance = 0;
	private double CVAngle = 0;
	private double lastCVAngle = 0;
	private double tempCVAngle = 0;
	private final int MAX_DISTANCE = 0; // not sure what unit the distance is in
	private final int DISTANCE_TOLERANCE = 0; // an amount of distance that is
												// close enough to zero
	private double initialDistance = 0;

	private Scanner in;
	private Socket s;
	private PrintStream out;

	public CVSegments() {
		initSocket();
		
		// TODO init segment command
	}

	private void initSocket() {
		in = null;
		s = null;
		out = null;
		try {
			s = new Socket(JETSON_SOCKET, PORT_NUMBER);
			in = new Scanner(s.getInputStream());
			System.out.println("CV connection succesful.");
			out = new PrintStream(s.getOutputStream(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
