package com.jishijiyu.takeadvantage.utils;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 手机晃动工具类
 */
public class SensorManagerHelper implements SensorEventListener {

	// 速度阈值，当摇晃速度达到这值后产生作用
	private static final int SPEED_SHRESHOLD = 5000;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 80;
	// 传感器管理器
	private SensorManager sensorManager;
	// 传感器
	private Sensor sensor;
	// 重力感应监听器
	private OnShakeListener onShakeListener;
	// 上下文对象context
	private Context context;
	// 手机上一个位置时重力感应坐标
	private float lastX;
	private float lastY;
	private float lastZ;
	// 上次检测时间
	private long lastUpdateTime;
	private boolean isStart = false;
	private boolean isStop = false;
	ArrayList<OnShakeListener> mListeners;

	// 构造器
	public SensorManagerHelper(Context context) {
		// 获得监听对象
		this.context = context;
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mListeners = new ArrayList<OnShakeListener>();
		start();
	}

	/**
	 * 启动摇晃检测
	 */
	public void start() {
		if (sensorManager == null) {
			throw new UnsupportedOperationException();
		}
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (sensor == null) {
			throw new UnsupportedOperationException();
		}
		boolean success = sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		if (!success) {
			throw new UnsupportedOperationException();
		}
	}

	// 停止检测
	public void stop() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
			sensorManager = null;
		}
	}

	// 摇晃监听接口
	public interface OnShakeListener {
		public void onShake();
	}

	// 设置重力感应监听器
	public void setOnShakeListener(OnShakeListener listener) {
		if (mListeners.contains(listener))
			return;
		mListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	/*
	 * 重力感应器感应获得变化数据
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// 现在检测时间
		long currentUpdateTime = System.currentTimeMillis();
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - lastUpdateTime;
		// 判断是否达到了检测时间间隔
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		// 现在的时间变成last时间
		lastUpdateTime = currentUpdateTime;
		// 获得x,y,z坐标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// 获得x,y,z的变化值
		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;
		// 将现在的坐标变成last坐标
		lastX = x;
		lastY = y;
		lastZ = z;
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		// 达到速度阀值，发出提示
		if (speed >= SPEED_SHRESHOLD) {
			isStart = true;
		}
		if (isStart) {
			if (speed < SPEED_SHRESHOLD) {
				isStart = false;
				this.notifyListeners();
			}
		}
	}

	/**
	 * 当摇晃事件发生时，通知所有的listener
	 */
	private void notifyListeners() {
		for (OnShakeListener listener : mListeners) {
			listener.onShake();
		}
	}
}