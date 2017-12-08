package org.usfirst.frc.team6038.robot.subsystems;

import org.usfirst.frc.team6038.framework.Database;

import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;
import org.usfirst.frc.team6038.robot.commands.TeleopDriveStraight;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDriveTrain extends PIDSubsystem {

	// KP, KI, and KD values used for PID
	private static final double KP = 0.0035; // 0.0035
	private static final double KI = 0.000035; //0.000035;
	private static final double KD = 0.001; //0.001;

//	private static final double KP = 0.01; // 0.0035
//	private static final double KI = 0.0005; //0.000035;
//	private static final double KD = 0.002; //0.001;
//	
	private RobotDrive driver;

	private double rotateToAngleRate; // the value changed by PID

	private CANTalon frontLeft, frontRight, backLeft, backRight;

	private static final double K_TOLERANCE_DEGREES = 2.0f; // the absolute error that is tolerable in the PID system

	public PIDriveTrain() {
		super(KP, KI, KD); // creates a PID controller with the KP, KI, and KD values

		rotateToAngleRate = 0;

		Devices.getInstance().getNavXGyro().zeroYaw();

		setInputRange(-180.0f, 180.0f); // sets the maximum and minimum values expected from the gyro
		setOutputRange(-1.0, 1.0); // sets the maximum and minimum output values
		setAbsoluteTolerance(K_TOLERANCE_DEGREES); // sets the absolute error that is tolerable in the PID system
		getPIDController().setContinuous(true); // sets the PID Controller to think of the gyro input as continuous
		
		// Creates a robot driver
		driver = new RobotDrive(Devices.getInstance().getTalon(RobotMap.FRONT_LEFT), Devices.getInstance().getTalon(RobotMap.BACK_LEFT), Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT), Devices.getInstance().getTalon(RobotMap.BACK_RIGHT));
//		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).changeControlMode(TalonControlMode.);
	}

	public void initDefaultCommand() {
//		setDefaultCommand(new DriveStraight(1000));
	}

	/**
	 * Returns the input used in the PID calculations
	 * 
	 * @return the input used in the PID calculations
	 */
	protected double returnPIDInput() {
		return Database.getInstance().getNumeric(RobotMap.GYRO_YAW);
	}

	/**
	 * Uses KP, KI, KD, and the input from returnPIDInput() to change and output
	 * value
	 */
	protected void usePIDOutput(double output) {
		double out = output;

		if (Math.abs(out) < 0.01) {
			out = 0;
		} else if (Math.abs(out) < 0.2) {
			out = 0.2 * Math.signum(out);
		}

		rotateToAngleRate = out;
	}

	/**
	 * Drives the robot given the speed and the rotation angle.
	 */
	public void drive(double speed, double rotation) {
		driver.arcadeDrive(-speed, rotation);
	}

	/**
	 * Stops the robot
	 */
	public void stop() {
		driver.arcadeDrive(0, 0);
	}

	/**
	 * @return the gyro object
	 */
	public AHRS getGyro() {
		return Devices.getInstance().getNavXGyro();
	}

	/**
	 * @return the value changed by the PID system
	 */
	public double getAngleRate() {
		return rotateToAngleRate;
	}

	/**
	 * Sets the desired angle in the PID system
	 * 
	 * @param angle
	 */
	public void setTargetAngle(double angle) {
		setSetpoint(angle);
		Robot.piDriveTrain.disable();
		Robot.piDriveTrain.enable();
	}
}