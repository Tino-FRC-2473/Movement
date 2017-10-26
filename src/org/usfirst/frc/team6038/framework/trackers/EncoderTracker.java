package org.usfirst.frc.team6038.framework.trackers;

import org.usfirst.frc.team6038.framework.components.Devices;

import com.ctre.CANTalon.TalonControlMode;

public class EncoderTracker extends DeviceTracker {

	public EncoderTracker(String key, int port) {
		super(key, Type.NUMERIC, port);
		setEvokeNumeric(() -> Devices.getInstance().getTalon(getPort()).getPosition());
	}
	
	public void resetEncoder() {
		Devices.getInstance().getTalon(getPort()).changeControlMode(TalonControlMode.Position);
		Devices.getInstance().getTalon(getPort()).setPosition(0);
		Devices.getInstance().getTalon(getPort()).changeControlMode(TalonControlMode.PercentVbus);
	}
}