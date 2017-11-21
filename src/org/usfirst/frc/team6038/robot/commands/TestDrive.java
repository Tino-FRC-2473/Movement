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
			System.out.println("Constant Value: " + driveConstant);
		
		// calls drive code command
		test(throttle, turn);
	}

	private void test(double pow, double turn) 
	{
		double diff = turn * driveConstant;
		if (pow > 0) {
			setLeftPow(pow + diff); 
			setRightPow(pow - diff); 
		} else if (pow < 0) {
			setLeftPow(pow - diff);
			setRightPow(pow + diff);
		}
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
		driveConstant += num;
	}
	
	@Override
	public void end() {
		setRightPow(0);
		setLeftPow(0);
	}
}