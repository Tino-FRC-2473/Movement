package org.usfirst.frc.team6038.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static final int FRONT_RIGHT = 4;
	public static final int FRONT_LEFT = 2;
	public static final int BACK_RIGHT = 5;
	public static final int BACK_LEFT = 3;
	
	public static final String FRONT_RIGHT_ENC = "FR_ENC";
	public static final String FRONT_LEFT_ENC = "FL_ENC";
	public static final String BACK_RIGHT_ENC = "BR_ENC";
	public static final String BACK_LEFT_ENC = "BL_ENC";
	
	public static final int MOTOR = 6;
	public static final int SERVO = 0;
	public static final int GYRO = 0;

	//keys
	public static final String MOTOR_ENCODER_KEY = "encoder key";
	public static final String MOTOR_VOLTAGE_KEY = "voltage key";
	public static final String MOTOR_CURRENT_KEY = "current key";
	public static final String MOTOR_POWER_KEY = "power key";
	public static final String MOTOR_SPEED_KEY = "speed key";
	public static final String SERVO_POSITION_KEY = "servo position key";
	public static final String SERVO_POWER_KEY = "servo power key";
	public static final String GYRO_HEADING_KEY = "gyro key";
	
	public static final String PEG_DISTANCE = "peg distance";
	public static final String PEG_ANGLE = "peg angle";
	public static final String FUNCTION_TRIGGER = "function trigger";
	public static final String STOP_TRIGGER = "stop trigger";
}