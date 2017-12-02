package org.usfirst.frc.team6038.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.ControlsMap;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraight_Auto extends Command {

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
	private double encoder;
	private double power;
	private float angle = 0;
	//private double encoders = 0;
	
	public DriveStraight_Auto(double encoder, double power) {
		requires(Robot.piDriveTrain);
		angle = Devices.getInstance().getNavXGyro().getYaw();
		this.encoder = encoder; 
		if(Math.abs(power) > this.maxPow){
			this.power = maxPow*Math.signum(power);
		}else{
			this.power = power;
		}
	}

	@Override
	protected void initialize() {
		resetEncoders();
		Robot.piDriveTrain.enable();
		Robot.piDriveTrain.setTargetAngle(Database.getInstance().getNumeric(RobotMap.GYRO_YAW));
	}

	private void resetEncoders() {
		Devices.getInstance().getTalon(2).reset();
		Devices.getInstance().getTalon(3).reset();
		Devices.getInstance().getTalon(4).reset();
		Devices.getInstance().getTalon(5).reset();
	}

	@Override
	protected void execute() {
		Robot.piDriveTrain.drive(power, Robot.piDriveTrain.getAngleRate());
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
		return ((Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC) + Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC))/2)<=encoder;
	}

	@Override
	protected void end() {
		Robot.piDriveTrain.disable();
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(0);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(0);
		System.out.println("DriveStraight ended. 🙂");
	}

	@Override
	protected void interrupted() {
		Robot.piDriveTrain.disable();
	}

}