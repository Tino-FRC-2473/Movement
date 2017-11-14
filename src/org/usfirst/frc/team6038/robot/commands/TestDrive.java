package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.ControlsMap;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class TestDrive extends Command 
{

	private final double diffCap = 0.3;
	private final double skiddingScale = 0.1;
	private final double throttleScale = 0.15;
	private static double driveConstant = 1;
	
	public TestDrive() 
	{
//		requires(Robot.piDriveTrain); TODO
	}
	
	private final double turnDeadZone = 3.6;
	private final double throttleDeadZone = 0.04;
	
	@Override
	protected void execute() 
	{
		System.out.println(Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z) + " " + Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X)*90);
		double turn = Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X)*90;
		double throttle = -Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z);
		//TODO
		//Database.getInstance().getNumeric()
		if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_INCREASE_KEY)==true)
			driveConstant++;
		else if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_DECREASE_KEY)==true)
			driveConstant--;
	}

	private void test(double pow, double turn) 
	{
		double diff = turn * driveConstant;
		System.out.println(pow+","+turn);
		setLeftPow(pow);
		setRightPow(pow);
	}
	
	private void setRightPow(double pow) {
		Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-pow);
		Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-pow);

	}

	private void setLeftPow(double pow) {
		Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(pow);
		Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(pow);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
	public static void changeConstant(int num)
	{
		driveConstant+=num;
	}
	
	@Override
	public void end() {
		setRightPow(0);
		setLeftPow(0);
	}
}