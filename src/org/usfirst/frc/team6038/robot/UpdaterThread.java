package org.usfirst.frc.team6038.robot;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.robot.Robot;

public class UpdaterThread extends Thread {

	public UpdaterThread() {
		super.setDaemon(true);
	}

	public void endUpdater() {
		System.out.println("updater ended");
		this.interrupt();
	}

	private double[] getData() {
		System.out.println("got data");
		return new double[] {
				// Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC),
				// Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC),
				Database.getInstance().getNumeric("pfr"),
				Database.getInstance().getNumeric("pfl"),
				// Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z),
				// Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X),
				// Database.getInstance().getNumeric(RobotMap.GYRO_YAW)
		};
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("big loop running");
			double value[];
			String str;
			// if (isRobotMoving())
			// alive = true;
			// else
			// alive = false;
			// trial code
//			if (!isRobotMoving()) {
//				Robot.tempData.add(RobotMap.NO_TRIALS);
//				// alive = false;
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			System.out.println(isRobotMoving());
//			while (!isRobotMoving()) {
//				Robot.tempData.add("");
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}

			value = getData();
			str = "";
			for (double v : value) {
				str += (roundTo(v, 2) + ",");
			}
			System.out.println("Updater Thread running");
			System.out.println(str);

			// System.out.println("Updater added - " + str);
			// System.out.println("Size: " + Robot.tempData.size());
			Robot.tempData.add(str);
			System.out.println(Robot.tempData.size());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("updater interrupted");
				e.printStackTrace();
			}

		}
	}

	private double roundTo(double val, int decimals) {
		double a = Math.pow(10, decimals);
		double newVal;
		newVal = Math.round(val * a) / a;
		return newVal;
	}

	private static boolean isRobotMoving() {
		boolean frrunning = Database.getInstance().getNumeric("pfr") != 0;
		boolean flrunning = Database.getInstance().getNumeric("pfl") != 0;
		boolean brrunning = Database.getInstance().getNumeric("pbr") != 0;
		boolean blrunning = Database.getInstance().getNumeric("pbl") != 0;
		return frrunning || flrunning || brrunning || blrunning;
	}
}