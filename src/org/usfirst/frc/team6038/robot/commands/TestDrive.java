package org.usfirst.frc.team6038.robot.commands;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.robot.ControlsMap;
import org.usfirst.frc.team6038.robot.Robot;
import org.usfirst.frc.team6038.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class TestDrive extends Command 
{
	public static double driveConstant = 0.5;
	private final double jump = 0.05;
	
	private double turn;
	private double throttle;
	
	public TestDrive() 
	{
		requires(Robot.piDriveTrain);
	}
	
	@Override
	protected void execute() 
	{		
		// retrieves control values
		turn = Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X)*90;
		throttle = -Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z);
		
		// determines is the constant is increased or decreased
		if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_INCREASE_KEY))
			driveConstant += jump;
		else if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_DECREASE_KEY))
			driveConstant -= jump;
		
		// check the constant value for TESTING 
//		System.out.println("Constant Value: " + driveConstant);
		System.out.println("Turn: " + turn);
		System.out.println("Throttle: " + throttle);
		
		// calls drive code command
		test(throttle, turn);
	}

	private void test(double throttle, double turn) 
	{
		double diff = turn * driveConstant;
		if (throttle > 0) {
			setLeftPow(throttle + diff); 
			setRightPow(throttle - diff); 
		} else if (throttle < 0) {
			setLeftPow(throttle - diff);
			setRightPow(throttle + diff);
		}
	}
	
	private void setRightPow(double pow) {
		if (-pow <= 1 || -pow >= -1) {
			Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-pow);
			Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-pow);
		}
	}

	private void setLeftPow(double pow) {
		if (pow <= 1 || pow >= -1) {
			Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).set(pow);
			Devices.getInstance().getTalon(RobotMap.BACK_LEFT).set(pow);
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
	public static void changeConstant(int num)
	{
		driveConstant += num;
	}
	
	@Override
	public void end() {
		setRightPow(0);
		setLeftPow(0);
	}
}