package org.usfirst.frc.team6038.robot.commands;

import java.io.PrintStream;
import java.util.Scanner;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Segment extends Command {
	private PrintStream out;
	private Scanner in;
	private double angle;
	private double distanceLeft_inches;
	private double distance_inches;
	private final double MINIMUM_SEGMENT_INCHES = 0;
	private double startingEncoder;
	
    public Segment(PrintStream out, Scanner in) {
        requires(Robot.piDriveTrain);
        this.out = out;
        this.in = in;
        startingEncoder = Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// TODO set the robot to the right angle (perhaps using gyro)
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// TODO makes the robot move forward. Distance is settled by isFinished()
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition()-
        		startingEncoder>= distance_inches/RobotMap.INCH_OVER_ENCODER);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
