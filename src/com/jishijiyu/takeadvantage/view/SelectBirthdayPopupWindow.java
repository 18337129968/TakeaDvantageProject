package com.jishijiyu.takeadvantage.view;

import java.io.File;
import java.util.Calendar;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.myinformation.MyBasicInfomationActivity;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 选择生日界面
 * 
 * @author shifeiyu
 * @version 2015年6月4日16:21:51
 * 
 */
public class SelectBirthdayPopupWindow extends PopupWindow {
	public static WheelMain wheelMain;
	public MyBasicInfomationActivity basic;
	private View mybirthWindow;
	public int year;
	public int month;
	public int day, hour, min;
	public TextView my_birth_btn_cancle, my_birth_btn_ok;
	public SelectBirthdayPopupWindow(Activity context,
			OnClickListener itemsOnclick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mybirthWindow = inflater.inflate(R.layout.layout_date_picker, null);
		my_birth_btn_ok = (TextView) mybirthWindow
				.findViewById(R.id.my_birth_btn_ok1);
		my_birth_btn_cancle = (TextView) mybirthWindow
				.findViewById(R.id.my_birth_btn_cancle);
		my_birth_btn_cancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
		Calendar c = Calendar.getInstance();
		basic = new MyBasicInfomationActivity();
		year = basic.year2;
		month = basic.month2 - 1;
		day = basic.day2;
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		ScreenInfo screenInfo = new ScreenInfo(context);

		wheelMain = new WheelMain(mybirthWindow, 1);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day);

		my_birth_btn_ok.setOnClickListener(itemsOnclick);
		this.setContentView(mybirthWindow);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mybirthWindow.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mybirthWindow.findViewById(R.id.pop_layout_birth)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

}
