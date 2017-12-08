package org.usfirst.frc.team6038.robot.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class CVDriveStraight extends Command {

	private final double MAX_POW = 0.8;
	private final double MIN_POW = 0.1;	
	
	//socket stuff
	private final String JETSON_SOCKET = "filler ples change b0$$";
	private final int PORT_NUMBER = 6969;
	private boolean isAlive = true;
	
	//cv stuff
	private double distance = 0;
	private double CVAngle = 0;
	private double lastCVAngle = 0;
	private double tempCVAngle = 0;
	private final double MAX_DISTANCE = 0; //not sure what unit the distance is in
	private final double DISTANCE_TOLERANCE = 0; //an amount of distance that is close enough to zero
	private double initialDistance = 0;

	private PrintStream out;
	
	public CVDriveStraight() {
		requires(Robot.piDriveTrain);
		// for retrieving info from CV
		Thread cv = new Thread() {
			public void run() {
				Scanner in = null;
				Socket s = null;
				out = null;
				try {
					s = new Socket(JETSON_SOCKET, PORT_NUMBER);
					in = new Scanner(s.getInputStream());
					System.out.println("CV connection succesful.");
					out = new PrintStream(s.getOutputStream(), false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				initialDistance = in.nextDouble();
				while (isAlive) {
					out.println(RobotMap.GET_VALUE);
					while (true) {
						if (in.hasNextDouble()) {
							distance = in.nextDouble();
							CVAngle = in.nextDouble();
							break;
						}
					}
				}
			}
		};
		cv.start();
	}

	@Override
	protected void initialize() {
		resetEncoders();
		Robot.piDriveTrain.enable();
		Devices.getInstance().getNavXGyro().zeroYaw();
	}

	private void resetEncoders() {
		resetOneEncoder(RobotMap.FRONT_LEFT);
		resetOneEncoder(RobotMap.FRONT_RIGHT);
		resetOneEncoder(RobotMap.BACK_LEFT);
		resetOneEncoder(RobotMap.BACK_RIGHT);
	}
	
	private void resetOneEncoder(int talonId) {
		Devices.getInstance().getTalon(talonId).changeControlMode(TalonControlMode.Position);
		Devices.getInstance().getTalon(talonId).setPosition(0);
		Devices.getInstance().getTalon(talonId).changeControlMode(TalonControlMode.PercentVbus);
	}

	@Override
	protected void execute() {
		// TODO change this statement if CV gives us some condition for out of sight
		if (distance<=MAX_DISTANCE&&distance>=0) {
			lastCVAngle = CVAngle;
			tempCVAngle = CVAngle;
		} else {
			tempCVAngle = lastCVAngle;
		}
		
		Robot.piDriveTrain.setTargetAngle(tempCVAngle);
		double pow = distance/initialDistance;
		if(Math.abs(pow) >= MAX_POW) {
			pow = MAX_POW*Math.signum(pow);
		}
		if (Math.abs(pow) <= MIN_POW) {
			pow = MIN_POW*Math.signum(pow);
		}
		Robot.piDriveTrain.drive(pow, Robot.piDriveTrain.getAngleRate());
	}

	@Override
	protected boolean isFinished() {
		return distance <= DISTANCE_TOLERANCE;
	}

	@Override
	protected void end() {
		Robot.piDriveTrain.disable();
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(0);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(0);
		System.out.println("DriveStraight ended. 🙂");
		isAlive = false;
	}

	@Override
	protected void interrupted() {
		Robot.piDriveTrain.disable();
	}

}