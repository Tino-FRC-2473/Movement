package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CVSegments extends CommandGroup {

	private static CVSegments segments;
	
	static {
		segments = new CVSegments();
	}
	
	public static CVSegments getInstance() {
		return segments;
	}

	public double LawOfCosinesGiveCenteredAngle(double CVDistance, double CVAngle)
	{
		double inRadians = Math.toRadians(90+CVAngle);
		
		double gyroDistance = Math.sqrt(Math.pow(CVDistance,2)+Math.pow(RobotMap.GYRO_CAMERA_DIST,2)-2*CVDistance*RobotMap.GYRO_CAMERA_DIST*Math.cos(inRadians));
		
		double gyroAngleRadians = Math.asin(CVDistance*Math.sin(inRadians)/gyroDistance);
		
		return Math.toDegrees(gyroAngleRadians);
	}
	
	public double normalTrigGiveCenteredAngle(double CVDistance, double CVAngle)
	{
		double horizontal = CVDistance*Math.cos(Math.toRadians(CVAngle));
		
		double vertical = CVDistance*Math.sin(Math.toRadians(CVAngle));
		
		double totalVertical = vertical + RobotMap.GYRO_CAMERA_DIST;
		
		double gyroAngleRadians = Math.atan(totalVertical/horizontal);
		
		return Math.toDegrees(gyroAngleRadians);
	}
	
	public CVSegments() {
		this.addSequential(new Segment(Database.getInstance().getNumeric(RobotMap.PEG_DISTANCE),
				Database.getInstance().getNumeric(RobotMap.PEG_ANGLE), false));
	}
}
