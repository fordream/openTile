package com.tistory.pflower.frontline;

import java.util.Timer;
import java.util.TimerTask;

//a class to manage bullets shooting coolDown/delay 
public class CoolDown {
	private boolean valid;
	private Timer timer;
	private static CoolDown instance = null;

	public static CoolDown getSharedInstance() {
		if (instance == null) {
			instance = new CoolDown();
		}
		return instance;
	}

	public CoolDown() {
		timer = new Timer();
		valid = true;
	}

	public boolean checkValidity(long delay) {
		if (valid) {
			valid = false;
			timer.schedule(new Task(), delay);
			return true;
		}
		return false;
	}

	class Task extends TimerTask {

		public void run() {
			valid = true;

		}

	}
}