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
		while(alive) {
			try {
				double value[];
				String str;
				if(isRobotMoving()){
					value = getData();
					str = "";
					for (Double v : value) { 
						str += (v + " ");
					}
				}else{
					str = RobotMap.NO_TRIALS;
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
	
	private static boolean isRobotMoving(){
		boolean frrunning = Database.getInstance().getNumeric("pfr")>0;
		boolean flrunning = Database.getInstance().getNumeric("pfl")>0;
		boolean brrunning = Database.getInstance().getNumeric("pbr")>0;
		boolean blrunning = Database.getInstance().getNumeric("pbl")>0;
		return frrunning||flrunning||brrunning||blrunning;
	}
}
