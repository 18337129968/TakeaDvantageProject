package com.jishijiyu.takeadvantage.activity;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;

public abstract class HeadBaseActivity extends Activity implements
		OnClickListener {
	public Context mContext = null;
	public TextView top_text = null;
	public Button btn_right = null;
	public Button btn_left = null;
	public RelativeLayout layout = null;
	public FrameLayout btn_right2 = null;
	public TextView btn_right_num = null;
	public static final String intentKey = "value";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 强制转换竖屏为了防止android中每次屏幕的切换都会重启Activity
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_base);
		mContext = HeadBaseActivity.this;
		initView();
	}

	private void initView() {
		top_text = (TextView) findViewById(R.id.top_text);
		btn_right = (Button) findViewById(R.id.btn_top_right);
		btn_left = (Button) findViewById(R.id.btn_top_left);
		layout = (RelativeLayout) findViewById(R.id.top_layout);
		btn_right2 = (FrameLayout) findViewById(R.id.btn_top_right2);
		btn_right_num = (TextView) findViewById(R.id.num);
		appHead(layout);
		initReplaceView();
	}

	public View getView(View view, int id) {
		return view.findViewById(id);
	}

	/**
	 * top顶部工能实现
	 * 
	 * @author zhaobin
	 * @param
	 * @return void
	 * @throws
	 */
	public abstract void appHead(View view);

	/**
	 * 界面功能实现
	 * 
	 * @author zhaobin
	 * @param
	 * @return void\
	 * 
	 * @throws
	 */
	public abstract void initReplaceView();

	/**
	 * 
	 * 
	 * @author zhaobin
	 * @param context
	 *            当前activity
	 * @param classs
	 *            要跳转到的activity
	 * @param value
	 *            传递的值
	 * @return void
	 * @throws
	 */
	public void startForActivity(Context context, Class<?> classs,
			Serializable value) {
		Intent intent = new Intent(context, classs);
		if (value != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(intentKey, value);
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}

	// 获取点击事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	// 判定是否需要隐藏
	public boolean isHideInput(View v, MotionEvent ev) {
		// view不为空并且view类型为edittext
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 }; // 创建数组容器
			v.getLocationInWindow(l); // 获取view窗口所在的位置
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left // l[0]为获取的控件X坐标
																					// l[1]为Y坐标
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top // 获取点击焦点大于控件的x坐标，小于x+控件宽度***
																			// 点击控件内区域返回false
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
