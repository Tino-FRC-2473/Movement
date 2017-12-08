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
	private double targetDistance_inches;
	private double cvAngle;
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
    	out.println(RobotMap.GET_VALUE);
		while (true) {
			if (in.hasNextDouble()) {
				distanceLeft_inches = in.nextDouble();
				cvAngle = in.nextDouble();
				break;
			}
		}
		
		// zero and get current angle
		Devices.getInstance().getNavXGyro().zeroYaw();
    	float currentAngle = Devices.getInstance().getNavXGyro().getYaw();
    	
    	if (currentAngle < cvAngle) {
    		// if robot needs to turn right
    		turnOnADimeCV(1);
    		while (currentAngle < cvAngle) {}
    		stopTurning();
    	} else if (currentAngle > cvAngle){
    		// if robot needs to turn left
    		turnOnADimeCV(-1);
    		while (currentAngle > cvAngle) {}
    		stopTurning();
    	}
    }

    private void turnOnADimeCV(int b) {
    	double power = RobotMap.TURN_POW_SEG;
    	double powDiff = power*RobotMap.DIFF_OVER_POW_SEG;
    	if (b > 0) {
    		// turnRight
    		setRightPow(power-powDiff);
    		setLeftPow(power+powDiff);
    	} else {
    		// turnLeft
    		setRightPow(power+powDiff);
    		setLeftPow(power-powDiff);
    	}
    }
    
	private void stopTurning() {
		setLeftPow(0);
		setRightPow(0);
	}
	
	private void setRightPow(double pow) {
		if (Math.abs(pow)>RobotMap.MAX_POW_SEG)
			pow = RobotMap.MAX_POW_SEG * Math.signum(pow);
		
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-pow);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-pow);
	}

	private void setLeftPow(double pow) {
		if (Math.abs(pow)>RobotMap.MAX_POW_SEG)
			pow = RobotMap.MAX_POW_SEG * Math.signum(pow);
		
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(pow);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(pow);
	}

	// Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// TODO makes the robot move forward. Distance is settled by isFinished()
    	double pow = targetDistance_inches * RobotMap.POWER_OVER_INCH;
    	setLeftPow(pow);
    	setRightPow(pow);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition()-
        		startingEncoder>= targetDistance_inches/RobotMap.INCH_OVER_ENCODER);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
