package org.usfirst.frc.team6038.framework.trackers;

import org.usfirst.frc.team6038.framework.components.Controls;
import org.usfirst.frc.team6038.framework.trackers.DeviceTracker.Type;

public class ButtonTracker extends DeviceTracker {

	public ButtonTracker(String key, int joystick, int button) {
		super(key, Type.CONDITIONAL, button);
		setEvokeConditional(() -> Controls.getInstance().getJoystick(joystick).getRawButton(button));
	}
}