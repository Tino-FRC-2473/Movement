package org.usfirst.frc.team6038.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class FlusherThread extends Thread {
	boolean alive;

	PrintStream out;
	
	
	public FlusherThread(PrintStream out) {
		this.out = out;
		alive = true;
		super.setDaemon(false);
	}

	public void end() {
		alive = false;
	}

	@Override
	public void run() {
		while (alive) {
			ArrayList<String> arr = new ArrayList<>();
			Robot.tempData.drainTo(arr);
			
			for(int i = 0; i < arr.size(); i++) {
				out.println(arr.get(i));
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}