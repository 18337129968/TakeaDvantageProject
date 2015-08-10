package com.jishijiyu.takeadvantage.utils;

import com.jishijiyu.takeadvantage.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast辅助工具类
 * 
 * @author zhaobin
 * 
 */
public class ToastUtils {
	private static Context context = null;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;
	private static Toast toast = null;

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		if (toast!=null) {
			toast.cancel();
			View view = LayoutInflater.from(context).inflate(R.layout.toast,
					null);
			TextView message = (TextView) view.findViewById(R.id.msg);
			message.setText(text);
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(duration);
			toast.setView(view);
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.toast,
					null);
			TextView message = (TextView) view.findViewById(R.id.msg);
			message.setText(text);

			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(duration);
			toast.setView(view);
		}
		return toast;
	}

	public static Toast makeText(Context context, int resId, int duration) {
		if (toast!=null) {
			toast.cancel();
			View view = LayoutInflater.from(context).inflate(R.layout.toast,
					null);
			TextView message = (TextView) view.findViewById(R.id.msg);
			message.setText(context.getResources().getString(resId));

			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(duration);
			toast.setView(view);
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.toast,
					null);
			TextView message = (TextView) view.findViewById(R.id.msg);
			message.setText(context.getResources().getString(resId));

			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(duration);
			toast.setView(view);
		}
		return toast;
	}
	public static void ToastCancel(Context context){
		if(toast!=null){
			toast.cancel();
		}
	}
}
