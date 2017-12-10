package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Segment extends Command {
	private double distanceLeft_inches;
	private double targetDistance_inches;
	private double cvAngle;
	private double startingEncoder;
	
    public Segment(double dist, double ang) {
        requires(Robot.piDriveTrain);
        distanceLeft_inches = dist;
        cvAngle = ang;
        startingEncoder = Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition();
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	// TODO set the robot to the right angle (perhaps using gyro)
		
		if (distanceLeft_inches <= RobotMap.MIN_DISTANCE_TOLERANCE) {
			CVSegments.getInstance().cancel();
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
    		stopTurning();
    	} else if (currentAngle > cvAngle){
    		// if robot needs to turn left
    		turnOnADimeCV(-1);
    		while (currentAngle > cvAngle) {
    			currentAngle = Devices.getInstance().getNavXGyro().getYaw();
    		}
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
		double cappedPow = cap(pow);
		
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-cappedPow);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-cappedPow);
	}

	private void setLeftPow(double pow) {
		double cappedPow = cap(pow);
				
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(cappedPow);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(cappedPow);
	}

	// Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double pow = targetDistance_inches * RobotMap.POWER_OVER_INCH;
    	// TODO add this back
    	setLeftPow(pow);
    	setRightPow(pow);
    	Devices.getInstance().getTalon(RobotMap.TESTING_ENC).set(cap(pow));
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
        return (Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getEncPosition()-
        		startingEncoder>= targetDistance_inches/RobotMap.INCH_OVER_ENCODER);
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	CVSegments.getInstance().addSequential(new Segment(Database.getInstance().getNumeric(RobotMap.PEG_DISTANCE),
    			Database.getInstance().getNumeric(RobotMap.PEG_ANGLE)));
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
