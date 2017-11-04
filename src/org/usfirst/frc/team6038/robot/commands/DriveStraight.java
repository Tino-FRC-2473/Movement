package org.usfirst.frc.team6038.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.ControlsMap;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

public class DriveStraight extends Command {

	public DriveStraight() {
		requires(Robot.driveTrain);
	}

	@Override
	protected void initialize() {
		Robot.driveTrain.setTargetAngle(Robot.driveTrain.getGyro().getYaw());
		Robot.driveTrain.enable();
	}

	@Override
	protected void execute() {
		if (Math.abs(Database.getInstance().getNumeric(ControlsMap.THROTTLE_KEY)) < 0.2) {
			Robot.driveTrain.disable();
			Robot.driveTrain.stop();
		} else {
			if (!Robot.driveTrain.getPIDController().isEnabled()) {
				Robot.driveTrain.enable();
			}
			Robot.driveTrain.drive(Database.getInstance().getNumeric(ControlsMap.THROTTLE_KEY),
					Robot.driveTrain.getAngleRate());
			System.out.println(Robot.driveTrain.getAngleRate());
			System.out.println(Devices.getInstance().getTalon(RobotMap.BACK_LEFT).getOutputCurrent());
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.driveTrain.stop();
		Robot.driveTrain.disable();
	}

	@Override
	protected void interrupted() {
		Robot.driveTrain.stop();
		Robot.driveTrain.disable();
	}

}