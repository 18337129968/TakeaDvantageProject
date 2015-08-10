package com.jishijiyu.takeadvantage.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;

public class TaskUtil {
	private static Timer timer;
	private static TimerTask task;
	public static void afterTimeRun(final Dialog dialog,int when){
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				dialog.dismiss();
			}
		};
		timer.schedule(task, when*1000);
		
	}
}
