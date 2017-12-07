package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import org.usfirst.frc.team2473.robot.commands.ExampleCommand;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.framework.components.Trackers;
import org.usfirst.frc.team6038.framework.readers.ControlsReader;
import org.usfirst.frc.team6038.framework.readers.DeviceReader;
import org.usfirst.frc.team6038.framework.trackers.ButtonTracker;
import org.usfirst.frc.team6038.framework.trackers.EncoderTracker;
import org.usfirst.frc.team6038.framework.trackers.JoystickTracker;
import org.usfirst.frc.team6038.framework.trackers.JoystickTracker.JoystickType;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker.NavXTarget;
import org.usfirst.frc.team6038.framework.trackers.TalonTracker;
import org.usfirst.frc.team6038.framework.trackers.TalonTracker.Target;
import org.usfirst.frc.team6038.robot.commands.DriveStraight_Auto;
import org.usfirst.frc.team6038.robot.commands.TestDrive;
import org.usfirst.frc.team6038.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team6038.robot.subsystems.PIDriveTrain;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	public static PIDriveTrain piDriveTrain;

	public static UtilitySocket CVSocket; 

	public static Server server;
	
	public static ArrayBlockingQueue<String> tempData;
	
	public static DeviceReader deviceReader;
	
	private static final double AUTO_ENCODER_LIMIT = 100000;
	private static final double AUTO_POW = 0.6;

	Command autonomousCommand;
	Command teleopCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		Robot.addDevices();
		Robot.addTrackers();
		deviceReader = new DeviceReader();
		deviceReader.start();
		ControlsReader.getInstance().init();
		oi = new OI();
		tempData = new ArrayBlockingQueue<String>(500);
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		piDriveTrain = new PIDriveTrain();
		teleopCommand = new TestDrive();
		try 
		{
			server = new Server(2017);
			//CVSocket = new UtilitySocket("Jetson name filler", 5050);//pls change cuz these parameters are fake af
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() 
	{
		server.closeServer();
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
		piDriveTrain.enable();
		System.out.println("autonomous init");
		autonomousCommand = new DriveStraight_Auto(AUTO_ENCODER_LIMIT, AUTO_POW);
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
		if (autonomousCommand != null){
			autonomousCommand.cancel();
			piDriveTrain.disable();
		}
		teleopCommand.start();
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
//		Trackers.getInstance().addTracker(new JoystickTracker(ControlsMap.STEERING_WHEEL_X,ControlsMap.STEERING_WHEEL,JoystickType.X));
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_RIGHT_ENC, RobotMap.FRONT_RIGHT));
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_LEFT_ENC, RobotMap.FRONT_LEFT));
//		Trackers.getInstance().addTracker(new JoystickTracker(ControlsMap.THROTTLE_Z,ControlsMap.THROTTLE, JoystickType.Z));
		Trackers.getInstance().addTracker(new TalonTracker("pfr",RobotMap.FRONT_RIGHT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pfl",RobotMap.FRONT_LEFT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pbr",RobotMap.BACK_RIGHT,Target.POWER));
		Trackers.getInstance().addTracker(new TalonTracker("pbl",RobotMap.BACK_LEFT,Target.POWER));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_YAW, NavXTarget.YAW));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_RATE, NavXTarget.RATE));
//		Trackers.getInstance().addTracker(new JoystickTracker(ControlsMap.THROTTLE_KEY, ControlsMap.THROTTLE, JoystickType.Z));
//		Trackers.getInstance().addTracker(new ButtonTracker(ControlsMap.CONSTANT_BUTTON_DECREASE_KEY, ControlsMap.THROTTLE, ControlsMap.CONSTANT_BUTTON_DECREASE_NUMBER));
//		Trackers.getInstance().addTracker(new ButtonTracker(ControlsMap.CONSTANT_BUTTON_INCREASE_KEY, ControlsMap.THROTTLE, ControlsMap.CONSTANT_BUTTON_INCREASE_NUMBER));
	}
}
