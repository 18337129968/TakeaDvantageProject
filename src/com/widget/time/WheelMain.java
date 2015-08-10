package com.widget.time;

import java.util.Arrays;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.VoiceUtil;
import com.jishijiyu.takeadvantage.view.SelectBirthdayPopupWindow;

import android.media.MediaPlayer;
import android.view.View;

public class WheelMain {
	SelectBirthdayPopupWindow sbpw;
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	public int screenheight;
	private int iTime;

	private static int START_YEAR = 1900, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(View view) {
		super();
		this.view = view;
		iTime = 0;
		setView(view);
	}

	public WheelMain(View view, int iTime) {
		super();
		this.view = view;
		this.iTime = iTime;
		setView(view);
	}

	public void initDateTimePicker1(int year, int month, int day) {
		this.initDateTimePicker1(year, month, day);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_day = (WheelView) view.findViewById(R.id.day);
		final MediaPlayer player =  MediaPlayer.create(view.getContext(), R.raw.tone);
		final boolean isvoice = VoiceUtil.isVoice(view.getContext());
		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				if (isvoice) {
					player.start();
				}
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};

		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				if (isvoice) {
					player.start();
				};
				// 判断大小月及是否闰年,用来确定"日"的数据
				wv_day.setCurrentItem(0);
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		
		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (isvoice) {
					player.start();
				}
			}
		};
			if (iTime == 1) {
			// 年
			wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
			wv_year.setCyclic(true);// 可循环滚动
			wv_year.setLabel("年");// 添加文字
			wv_year.setCurrentItem(year-START_YEAR);// 初始化时显示的数据

			// 月
			wv_month.setAdapter(new NumericWheelAdapter(1, 12));
			wv_month.setCyclic(true);
			wv_month.setLabel("月");
			wv_month.setCurrentItem(month);

			// 日
			wv_day.setCyclic(true);
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
			wv_day.setLabel("日");
			wv_day.setCurrentItem(day - 1);
			wv_day.addChangingListener(wheelListener_day);
			wv_year.addChangingListener(wheelListener_year);
			wv_month.addChangingListener(wheelListener_month);
			
		}

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (iTime == 0)
			textSize = (screenheight / 100) * 3;
		else if (iTime == 1)
			textSize = (screenheight / 100) * 4;
		else if (iTime == 2)
			textSize = (screenheight / 100) * 6;

		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		if (iTime == 1) {
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1));
			
		} 
		return sb.toString();
	}
}
