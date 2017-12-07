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
	private final double cap = 0.8;
	private boolean increaseButtonPressed = false;
	private boolean decreaseButtonPressed = false;
	
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
		turn = Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X);
		throttle = Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z);
		
		// determines is the constant is increased or decreased
		if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_INCREASE_KEY)) {
			increaseButtonPressed = true;
		} else if(Database.getInstance().getConditional(ControlsMap.CONSTANT_BUTTON_DECREASE_KEY)) {
			decreaseButtonPressed = true;
		} else {
			// determines if the button is just released so that only one increment/decrement
			// goes into effect
			if (increaseButtonPressed) {
				driveConstant += jump;
				increaseButtonPressed = false;
			}
			
			if (decreaseButtonPressed) {
				driveConstant -= jump;
				decreaseButtonPressed = false;
			}
		}
		// check the constant value for TESTING 
//		System.out.println("Constant Value: " + driveConstant);
		System.out.println("Turn: " + turn);
		System.out.println("Throttle: " + throttle * 90);
		System.out.println("Constant: " + driveConstant);
		
		// calls drive code command
		test(throttle, turn);
	}

	private void test(double throttle, double turn) 
	{
		double diff = turn * driveConstant;
		if (throttle >= 0) {
			setLeftPow(throttle + diff); 
			setRightPow(throttle - diff); 
		} else if (throttle < 0) {
			setLeftPow(throttle - diff);
			setRightPow(throttle + diff);
		}
	}
	
	private void setRightPow(double pow) {
		if (-pow <= cap || -pow >= -cap) {
			Devices.getInstance().getTalon(RobotMap.FRONT_RIGHT).set(-pow);
			Devices.getInstance().getTalon(RobotMap.BACK_RIGHT).set(-pow);
		}
	}

	private void setLeftPow(double pow) {
		if (pow <= cap || pow >= -cap) {
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