package org.usfirst.frc.team6038.robot.commands;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraight_CV extends Command {

	private double maxPow = 0.8;
	private double wheelDeadzone = 0.04;
	private double throttleDeadzone = 0.05;
	// WARNING!!!!!!!!!!!!!!!!!!
	// Cannot run code without initializing maxWheelX value correctly
	private double maxWheelX = 1;
	// determine the values of a and b by testing
	private final double b = 1; // b should be half the length between the two wheels in meters
	private final double powToSpeedRatio = 35; // the ratio converting power to actual speed while driving straight
	private final double speedMaxThreshold = 15; // the threshold determining the max pow while turning
	private final double powCapMod = 0.7; // the modifier for reducing the power while turning
//	private double encoder;
	private double power;
	private float angle = 0;
	
	
	//socket stuff
	private final String JETSON_SOCKET = "filler ples change b0$$";
	private final int PORT_NUMBER = 6969;
	private boolean isAlive = true;
	
	//cv stuff
	private double distance = 0;
	private double CVangle = 0;
	private static int MAX_DISTANCE = 0; //not sure what unit the distance is in
	private static int DISTANCE_TOLERANCE = 0; //an amount of distance that is close enough to zero
	
	public DriveStraight_CV() {
		Thread cv = new Thread() {
			public void run() {
				Scanner in = null;
				Socket s = null;
				try {
					s = new Socket(JETSON_SOCKET, PORT_NUMBER);
					in = new Scanner(s.getInputStream());
					System.out.println("CV connection succesful.");
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(isAlive && in.hasNextLine()) {
					distance = in.nextDouble();
					CVangle = in.nextDouble();
				}
			}
		};
		cv.start();
		requires(Robot.piDriveTrain);
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
		if((CVangle<=180&&CVangle>=-180) && (distance<=MAX_DISTANCE&&distance>=0)) {
			Robot.piDriveTrain.setTargetAngle(CVangle);
			double pow = distance/MAX_DISTANCE;
			if(Math.abs(pow) >= maxPow) {
				pow = maxPow*Math.signum(pow);
			}
			Robot.piDriveTrain.drive(pow, Robot.piDriveTrain.getAngleRate());
		}else {
			//WTF DO WE DOOOOOOOO
		}
	}
	
	private double squareWithSign(double d) {
		if (d >= 0.04) {
			return -d * d;
		} else if (d <= -0.04) {
			return d * d;
		} else {
			return 0;
		}
	}

	private void turn(double pow, double turn) {
		if (Math.abs(pow) > maxPow) {
			pow = maxPow * Math.signum(pow);
		}
		
		if (Math.abs(pow) > throttleDeadzone) {
			double approxSpeed = getApproxSpeed(pow, turn); // the approximated speed in meters per second
			approxSpeed = this.cap(approxSpeed, turn);
			approxSpeed = approxSpeed / powToSpeedRatio;
			if (approxSpeed > 0) {
				setLeftPow(approxSpeed + b * turn); 
				setRightPow(approxSpeed - b * turn); 
			} else if (pow < 0) {
				setLeftPow(-(approxSpeed + b * turn));
				setRightPow(-(approxSpeed - b * turn));
			}
		}
	}
	
	private final double minTurnThreshold = 0.2;
	private final double minSpeedThreshold = 5;
	private final double a = 5; // a is the ratio of the value of power to the actual velocity
	// return the approximated current SPEED under the influence of friction and skidding
	private double getApproxSpeed(double pow, double turn) {
		/*
		 * TODO determine if this is the best fit. All we know now is that the higher the pow and the greater 
		 * the turn, the lower the actual speed.
		 */
		double speed = pow * powToSpeedRatio;
		if (Math.abs(turn) < minTurnThreshold || speed < minSpeedThreshold)
			return speed;
		return speed - (a / Math.abs(turn)) / speed;
	}
	
	// lower the power if the speed is too high
	private double cap(double speed, double turn) {
		/*
		 * TODO determine if this is the best fit. All we know now is that the higher the pow the greater the
		 * pow reduction, and the greater the turn the less the pow reduction
		 */
		if (speed > speedMaxThreshold && turn > minTurnThreshold)
			return powCapMod * speed / Math.abs(turn);
		return speed;
	}

	// the experimental version of turn
	private void turnExp(double pow, double turn) {
		
	}
	
//	private void record(double pow, double turn, double actualV) {
//		try {
//			fw.write(pow + " ");
//			fw.write(turn + " ");
//			fw.write(actualV + "\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void setRightPow(double pow) {
		double temp = pow;
		if (temp > maxPow)
			temp = maxPow;
		else if (temp < -maxPow)
			temp = -maxPow;
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-temp);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-temp);

	}

	private void setLeftPow(double pow) {
		double temp = pow;
		if (temp > maxPow)
			temp = maxPow;
		else if (temp < -maxPow)
			temp = -maxPow;
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(temp);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(temp);

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