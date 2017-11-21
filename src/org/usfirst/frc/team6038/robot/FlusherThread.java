package org.usfirst.frc.team6038.robot;

import java.io.PrintStream;
import java.util.ArrayList;

public class FlusherThread extends Thread {
	boolean alive;

	PrintStream out;
	
	
	public FlusherThread(PrintStream out) {
		this.out = out;
		alive = true;
		super.setDaemon(false);
		System.out.println("Flusher created.");
	}

	public void end() {
		alive = false;
	}

	@Override
	public void run() {
		while (alive) {
			ArrayList<String> arr = new ArrayList<>();
			if (Robot.tempData.size() == 10)
				Robot.tempData.drainTo(arr);
			
			for(int i = 0; i < arr.size(); i++) {
				out.println(arr.get(i));
			}
			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}