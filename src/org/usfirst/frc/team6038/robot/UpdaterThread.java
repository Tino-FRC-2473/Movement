package org.usfirst.frc.team6038.robot;

import org.usfirst.frc.team6038.framework.Database;
import org.usfirst.frc.team6038.robot.Robot;

public class UpdaterThread extends Thread {
	boolean alive = true;

	public UpdaterThread() {
		super.setDaemon(true);
	}

	public void end() {
		alive = false;
	}
	
	private double[] getData()
	{
		return new double[]
				{
						Database.getInstance().getNumeric(RobotMap.FRONT_RIGHT_ENC), 
						Database.getInstance().getNumeric(RobotMap.FRONT_LEFT_ENC),
						Database.getInstance().getNumeric("pfr"),
						Database.getInstance().getNumeric("pfl"),
						Database.getInstance().getNumeric(ControlsMap.THROTTLE_Z),
						Database.getInstance().getNumeric(ControlsMap.STEERING_WHEEL_X),
						Database.getInstance().getNumeric(RobotMap.GYRO_YAW)
				};
	}

	@Override
	public void run() {
		while (true) {
			try {
				double value[];
				String str;
//				if (isRobotMoving())
//					alive = true;
//				else 
//					alive = false;
				if (!isRobotMoving()) {
					str = RobotMap.NO_TRIALS;
//					alive = false;
					Thread.sleep(10);
				}
					
				while (!isRobotMoving()) {
					Thread.sleep(10);
					Robot.tempData.add("");
				}
				
				value = getData();
				str = "";
				for (double v : value) {
					str += (roundTo(v, 2) + " ");
				}
					
				//System.out.println("Updater added - " + str);
				//System.out.println("Size: " + Robot.tempData.size());
				Robot.tempData.add(str);
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	private double roundTo(double val, int decimals) {
		double a = Math.pow(10,decimals);
		double newVal;
		newVal = Math.round(val * a) / a;
		return newVal;
	}
	
	private static boolean isRobotMoving(){
		boolean frrunning = Database.getInstance().getNumeric("pfr")>0;
		boolean flrunning = Database.getInstance().getNumeric("pfl")>0;
		boolean brrunning = Database.getInstance().getNumeric("pbr")>0;
		boolean blrunning = Database.getInstance().getNumeric("pbl")>0;
		return frrunning||flrunning||brrunning||blrunning;
	}
}
