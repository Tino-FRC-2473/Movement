package org.usfirst.frc.team6038.framework.trackers;

import org.usfirst.frc.team6038.framework.components.Devices;

public class GyroTracker extends DeviceTracker {

	public GyroTracker(String key, int port) {
		super(key, Type.NUMERIC, port);
		setEvokeNumeric(() -> Devices.getInstance().getGyro(getPort()).getAngle());
	}

	public void resetGyro() {
		Devices.getInstance().getGyro(getPort()).calibrate();
	}
}