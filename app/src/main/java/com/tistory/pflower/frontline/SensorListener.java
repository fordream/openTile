package com.tistory.pflower.frontline;

import com.tistory.pflower.frontline.scene.GameScene;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class SensorListener implements SensorEventListener {

	static SensorListener instance;
	private float accelerometerSpeedX;

	public static SensorListener getSharedInstance() {
		if (instance == null)
			instance = new SensorListener();
		return instance;
	}

	private SensorListener() {
		instance = this;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				 //Log.v("Jimvaders",
				 //"SensorListener onSensorChanged() accelerometerSpeedX = "+event.values[1]);
				setAccelerometerSpeedX(event.values[1]);
				break;
			default:
				break;
			}
		}

	}

	public float getAccelerometerSpeedX() {
		return accelerometerSpeedX;
	}

	public void setAccelerometerSpeedX(float accelerometerSpeedX) {
		this.accelerometerSpeedX = accelerometerSpeedX;
	}

}
