package org.usfirst.frc.team6038.framework.trackers;

import org.usfirst.frc.team6038.framework.components.Controls;

public class JoystickTracker extends DeviceTracker {

	public enum JoystickType {
		X, Y, Z, THROTTLE, TWIST
	}
	
	public JoystickTracker(String key,int port, JoystickType type) {
		super(key, DeviceTracker.Type.NUMERIC, port);
		switch(type) {
			case X:
				setEvokeNumeric(() -> Controls.getInstance().getJoystick(getPort()).getX());
				break;
			case Y:
				setEvokeNumeric(() -> Controls.getInstance().getJoystick(getPort()).getY());
				break;
			case Z:
				setEvokeNumeric(() -> Controls.getInstance().getJoystick(getPort()).getZ());
				break;
			case THROTTLE:
				setEvokeNumeric(() -> Controls.getInstance().getJoystick(getPort()).getThrottle());
				break;
			case TWIST:
				setEvokeNumeric(() -> Controls.getInstance().getJoystick(getPort()).getTwist());				break;
			default:
				throw new IllegalArgumentException("Insufficient getter argument(joystick)");
		}
	}	
}