package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Segment extends Command {
	private double distanceLeft_inches;
	private double targetDistance_inches;
	private double cvAngle;
	
	private boolean isTesting;
	
    public Segment(double dist, double ang, boolean isTesting) {
        requires(Robot.piDriveTrain);
        distanceLeft_inches = dist;
        cvAngle = ang;
        
        this.isTesting = isTesting;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
		
		if (distanceLeft_inches < RobotMap.MIN_DISTANCE_TOLERANCE) {
			// finished
			CVSegments.getInstance().cancel();
		}
		
		targetDistance_inches = RobotMap.TARGET_OVER_TOTAL * distanceLeft_inches;
		
		if (targetDistance_inches < RobotMap.MIN_SEGMENT_TOLERANCE) {
			targetDistance_inches = RobotMap.MIN_SEGMENT_TOLERANCE;
		}
		
		// zero and get current angle
		Devices.getInstance().getNavXGyro().zeroYaw();
    	float currentAngle = Devices.getInstance().getNavXGyro().getYaw();
		
    	if (currentAngle < cvAngle) 
    	{
    		// if robot needs to turn right
    		turnOnADimeCV(1);
    		while (currentAngle < cvAngle) 
    		{
    			currentAngle = Devices.getInstance().getNavXGyro().getYaw();
    		}
    		stopMoving();
    	} else if (currentAngle > cvAngle){
    		// if robot needs to turn left
    		turnOnADimeCV(-1);
    		while (currentAngle > cvAngle) {
    			currentAngle = Devices.getInstance().getNavXGyro().getYaw();
    		}
    		stopMoving();
    	}
    	
    	AutoDriveStraight.resetEncoderCount();
    	System.out.println("Initialize has ended.");
    }

	private void turnOnADimeCV(int b) {
		System.out.println("Robot has started turning...");
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
    	System.out.println("Robot has finished turning.");
    }
    
	private void stopMoving() {
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(0);
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(0);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(0);
	}
	
	private void setRightPow(double pow) {
		double cappedPow = cap(pow);
		System.out.println("capped right power: " + cappedPow);
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-cappedPow);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-cappedPow);
	}

	private void setLeftPow(double pow) {
		double cappedPow = cap(pow);
		System.out.println("capped left power: " + cappedPow);
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(cappedPow);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(cappedPow);
	}

	// Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double pow = -targetDistance_inches * RobotMap.POWER_OVER_INCH;
    	setLeftPow(pow);
    	setRightPow(pow);
    	System.out.println("Distance Left: " + distanceLeft_inches);
    	System.out.println("Target Distance: " + targetDistance_inches);
    	System.out.println("CV Angle: " + cvAngle);
    	System.out.println("Enc: " + getAverage());
    }
    
    private double cap(double pow) {
    	if (Math.abs(pow)>RobotMap.MAX_POW_SEG) {
			return RobotMap.MAX_POW_SEG * Math.signum(pow);
    	} else if (Math.abs(pow)<RobotMap.MIN_POW_SEG) {
    		return RobotMap.MIN_POW_SEG * Math.signum(pow);
    	} else {
    		return pow;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return ((getAverage())>= targetDistance_inches/RobotMap.INCH_OVER_ENCODER);
    }
    
    private double getAverage() {
    	// TODO
    	changeAllTalonControlMode(TalonControlMode.Position);
    	double avg = (Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition()+
    			Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).getEncPosition()+
    			Devices.getInstance().getTalon(RobotMap.BACK_LEFT).getEncPosition()+
    			Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).getEncPosition())/4;
    	changeAllTalonControlMode(TalonControlMode.PercentVbus);
    	return avg;
    	
    }
    
    private static void changeAllTalonControlMode(TalonControlMode mode) {
    	Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).changeControlMode(mode);
    	Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).changeControlMode(mode);
    	Devices.getInstance().getTalon(RobotMap.BACK_LEFT).changeControlMode(mode);
    	Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).changeControlMode(mode);
    }

    // Called once after isFinished returns true
    protected void end() {
    	if (!isTesting) {
	    	CVSegments.getInstance().addSequential(new Segment(Database.getInstance().getNumeric(RobotMap.PEG_DISTANCE),
	    			Database.getInstance().getNumeric(RobotMap.PEG_ANGLE), false));
    	} else {
    		stopMoving();
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
