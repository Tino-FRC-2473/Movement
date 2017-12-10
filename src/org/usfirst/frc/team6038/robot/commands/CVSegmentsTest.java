package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CVSegmentsTest extends CommandGroup {
	private static CVSegments segments;

	static {
		segments = new CVSegments();
	}

	public static CVSegments getInstance() {
		return segments;
	}

	public CVSegmentsTest() {
		this.addSequential(new Segment(Database.getInstance().getNumeric(RobotMap.PEG_DISTANCE),
				Database.getInstance().getNumeric(RobotMap.PEG_ANGLE), false));
	}
}
