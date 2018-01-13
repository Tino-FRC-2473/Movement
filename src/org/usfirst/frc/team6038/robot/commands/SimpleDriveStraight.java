package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class SimpleDriveStraight extends Command {

	// TODO put maxPow in RobotMap
	private final double maxPow = 0.8;
	private double maxEncoder; // The maximum encoder count at which the robot
								// stops
	private double power;

	public SimpleDriveStraight(double encoder, double power) {
		requires(Robot.piDriveTrain);
		this.maxEncoder = encoder;
		if (Math.abs(power) > this.maxPow) {
			this.power = maxPow * Math.signum(power);
		} else {
			this.power = power;
		}
		System.out.println("Simple drive straight constructor passed.");
	}

	@Override
	protected void initialize() {
		System.out.println("initialize running..");
		Robot.piDriveTrain.stop();
		resetEncoders();
		Devices.getInstance().getNavXGyro().zeroYaw();
		Robot.piDriveTrain.setTargetAngle(Devices.getInstance().getNavXGyro().getAngle());
		System.out.println("running...");
	}

	public static void resetEncoders() {
		System.out.println("encoder reset.");
		resetOneEncoder(RobotMap.FRONT_LEFT);
		resetOneEncoder(RobotMap.FRONT_RIGHT);
	}

	private static void resetOneEncoder(int talonId) {
		Devices.getInstance().getTalon(talonId).changeControlMode(TalonControlMode.Position);
		Devices.getInstance().getTalon(talonId).setPosition(0);
		Devices.getInstance().getTalon(talonId).changeControlMode(TalonControlMode.PercentVbus);
		System.out.println(Devices.getInstance().getTalon(talonId).getPosition());
	}

	@Override
	protected void execute() {
		System.out.println("power: " + power);
		Robot.piDriveTrain.drive(power, Robot.piDriveTrain.getAngleRate());
	}

	private void setRightPow(double pow) {
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-pow);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-pow);
	}

	private void setLeftPow(double pow) {
		System.out.println("set left power: " + pow);
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(pow);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(pow);
	}

	private double getAverageEnc(int enc1, int enc2) {
		return (Devices.getInstance().getTalon(enc1).getPosition()
				+ Devices.getInstance().getTalon(enc2).getPosition()) / 2;
	}

	private static void changeAllTalonControlMode(TalonControlMode mode) {
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).changeControlMode(mode);
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).changeControlMode(mode);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).changeControlMode(mode);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).changeControlMode(mode);
	}

	@Override
	protected boolean isFinished() {
		System.out.println("Curr Encoder: " + this.getAverageEnc(RobotMap.FRONT_LEFT, RobotMap.FRONT_RIGHT) + ", " + maxEncoder);
		return this.getAverageEnc(RobotMap.FRONT_LEFT, RobotMap.FRONT_RIGHT) >= maxEncoder;
	}

	@Override
	protected void end() {
		setLeftPow(0);
		setRightPow(0);
		Robot.piDriveTrain.disable();
		System.out.println("DriveStraight ended.");
		resetEncoders();
	}

	@Override
	protected void interrupted() {
		Robot.piDriveTrain.disable();
	}

}