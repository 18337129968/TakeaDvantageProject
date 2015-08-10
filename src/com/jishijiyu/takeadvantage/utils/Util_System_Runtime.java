package com.jishijiyu.takeadvantage.utils;

import android.os.Handler;
import android.os.Looper;

public class Util_System_Runtime {

	private static Util_System_Runtime mInstance;

	private Handler mUiHandler;

	private Util_System_Runtime() {
		mUiHandler = new Handler(Looper.getMainLooper());
	}

	public synchronized static Util_System_Runtime getInstance() {
		try {
			if (mInstance == null) {
				mInstance = new Util_System_Runtime();
			}
		} catch (Throwable e) {
		}
		return mInstance;
	}

	public static boolean isInUIThread() {
		try {
			return Looper.myLooper() == Looper.getMainLooper();
		} catch (Throwable e) {
		}
		return false;
	}

	public boolean runInUiThread(Runnable runnable) {
		try {
			if (runnable == null) {
				return false;
			}
			return mUiHandler.post(runnable);
		} catch (Throwable e) {
		}
		return false;
	}

	public boolean runInUiThreadDelayed_ms(Runnable runnable, long delayTime_ms) {
		try {
			if (runnable == null) {
				return false;
			}
			return mUiHandler.postDelayed(runnable, delayTime_ms);
		} catch (Throwable e) {
		}
		return false;
	}
}
