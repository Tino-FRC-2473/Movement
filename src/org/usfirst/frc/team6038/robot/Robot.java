package org.usfirst.frc.team6038.robot;
import org.usfirst.frc.team6038.framework.components.Devices;
import org.usfirst.frc.team6038.framework.components.Trackers;
import org.usfirst.frc.team6038.framework.readers.DeviceReader;
import org.usfirst.frc.team6038.framework.trackers.EncoderTracker;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker;
import org.usfirst.frc.team6038.framework.trackers.NavXTracker.NavXTarget;
import org.usfirst.frc.team6038.robot.commands.SimpleDriveStraight;
import org.usfirst.frc.team6038.robot.subsystems.PIDriveTrain;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	
	Preferences prefTest;
	double test;
	public static OI oi;
	
	public static boolean isNetwork = false;

	public static PIDriveTrain piDriveTrain;
	
	public static DeviceReader deviceReader;
	
	private static final double AUTO_ENCODER_LIMIT = 100000;
	private static final double AUTO_POW = 0.5;

	Command autonomousCommand;
	Command teleopCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		Robot.addDevices();
		Robot.addTrackers();
		prefTest = Preferences.getInstance();
		test = prefTest.getDouble("Test", 5);
		System.out.println(test);
		System.out.println("Test pref done");
		piDriveTrain = new PIDriveTrain();
		System.out.println("PIDrivetrain initialized");
		autonomousCommand = new SimpleDriveStraight(AUTO_ENCODER_LIMIT, AUTO_POW);
		System.out.println("auto command initialized");
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		SimpleDriveStraight.resetEncoders();
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
		System.out.println("Autonomous Init started...");
		
		if (autonomousCommand != null)
			autonomousCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
//		Robot.piDriveTrain.drive(0.6, 0);
	}

	@Override
	public void teleopInit() {
		System.out.println("teleop init");
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			piDriveTrain.disable();
		}
		Devices.getInstance().getTalon((RobotMap.FRONT_LEFT)).reset();
//		teleopCommand.start();

	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		Devices.getInstance().getTalon((RobotMap.FRONT_LEFT)).set(0.1);
		System.out.println(Devices.getInstance().getTalon(RobotMap.FRONT_LEFT).getPosition());
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
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_RIGHT_ENC, RobotMap.FRONT_RIGHT));
		Trackers.getInstance().addTracker(new EncoderTracker(RobotMap.FRONT_LEFT_ENC, RobotMap.FRONT_LEFT));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_YAW, NavXTarget.YAW));
		Trackers.getInstance().addTracker(new NavXTracker(RobotMap.GYRO_RATE, NavXTarget.RATE));
	}
}
