
package org.usfirst.frc.team6038.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.framework.components.Trackers;
import org.usfirst.frc.team6038.framework.trackers.DeviceTracker.Type;
import org.usfirst.frc.team6038.framework.trackers.EncoderTracker;
import org.usfirst.frc.team6038.framework.trackers.JoystickTracker;
import org.usfirst.frc.team6038.framework.trackers.TalonTracker;
import org.usfirst.frc.team6038.framework.trackers.TalonTracker.Target;
import org.usfirst.frc.team6038.framework.trackers.JoystickTracker;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker.NavXTarget;
import org.usfirst.frc.team6038.robot.commands.ExampleCommand;
import org.usfirst.frc.team6038.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team6038.robot.subsystems.PIDriveTrain;
import org.usfirst.frc.team6038.framework.trackers.JoystickTracker.JoystickType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	
	public static PIDriveTrain driveTrain;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		driveTrain = new PIDriveTrain();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	private static void addDevices(){
		Devices.getInstance().addTalon(RobotMap.BACK_RIGHT);
		Devices.getInstance().addTalon(RobotMap.BACK_LEFT);
		Devices.getInstance().addTalon(RobotMap.FRONT_LEFT);
		Devices.getInstance().addTalon(RobotMap.FRONT_RIGHT);
	}
	
	private static void addTrackers(){
//		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.BACK_RIGHT_ENC, RobotMap.BACK_RIGHT));
//		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.BACK_LEFT_ENC, RobotMap.BACK_LEFT));
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_RIGHT_ENC, RobotMap.FRONT_RIGHT));
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_LEFT_ENC, RobotMap.FRONT_LEFT));
		Trackers.getInstance().addTracker(new JoystickTracker(ControlsMap.THROTTLE_Z,ControlsMap.THROTTLE, JoystickType.THROTTLE));
		Trackers.getInstance().addTracker(new TalonTracker("pfr",RobotMap.FRONT_RIGHT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pfl",RobotMap.FRONT_LEFT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pbr",RobotMap.BACK_RIGHT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pbl",RobotMap.BACK_LEFT,Target.POWER));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_YAW, NavXTarget.YAW));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_RATE, NavXTarget.RATE));
		Trackers.getInstance().addTracker(new JoystickTracker(ControlsMap.THROTTLE_KEY, ControlsMap.THROTTLE, JoystickType.Z));
	}
}
