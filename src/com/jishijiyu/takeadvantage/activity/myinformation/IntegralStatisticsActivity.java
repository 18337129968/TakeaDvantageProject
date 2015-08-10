package com.jishijiyu.takeadvantage.activity.myinformation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernie.ErnieActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.lidroid.xutils.ViewUtils;

/**
 * 个人账户
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class IntegralStatisticsActivity extends HeadBaseActivity implements
		OnClickListener {
	private Button img_btn_top_left;
	private Button btn_integral_statictics_get;
	private Button btn_integral_statictics_consume;
	private FragmentManager manager;
	private FragmentTransaction transaction;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.my_acount));
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(IntegralStatisticsActivity.this,
				R.layout.activity_integral_statictics, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		btn_integral_statictics_get = (Button) findViewById(R.id.btn_integral_statictics_get);
		btn_integral_statictics_consume = (Button) findViewById(R.id.btn_integral_statictics_consume);

		btn_integral_statictics_get.setOnClickListener(this);
		btn_integral_statictics_consume.setOnClickListener(this);
		btn_integral_statictics_get.setSelected(true);
		btn_integral_statictics_consume.setSelected(false);
		manager = getFragmentManager();
		transaction = manager.beginTransaction();
		IntegralStaticticsGet getFragment = new IntegralStaticticsGet();
		transaction.add(R.id.rl_integral_statictics_des, getFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		transaction = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(this);
			break;
		case R.id.btn_integral_statictics_get:
			IntegralStaticticsGet getFragment = new IntegralStaticticsGet();
			transaction.replace(R.id.rl_integral_statictics_des, getFragment);
			btn_integral_statictics_get.setSelected(true);
			btn_integral_statictics_consume.setSelected(false);
			break;
		case R.id.btn_integral_statictics_consume:
			IntegralStaticticsConsume consumeFragment = new IntegralStaticticsConsume();
			transaction.replace(R.id.rl_integral_statictics_des,
					consumeFragment);
			btn_integral_statictics_get.setSelected(false);
			btn_integral_statictics_consume.setSelected(true);
			break;
		default:
			break;
		}
		transaction.commit();
	}

}
