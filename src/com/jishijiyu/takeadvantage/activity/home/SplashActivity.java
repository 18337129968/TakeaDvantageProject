package com.jishijiyu.takeadvantage.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.android.xkeuops.CommonManager;
import com.dlnetwork.DevInit;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.SPUtils;

/**
 * 欢迎界面
 * @author temulu
 */
public class SplashActivity extends Activity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mContext = SplashActivity.this;
		initView();
		
		// 在应用每次打开都执行一次初始化，主要用于初始化appid之类的信息  有米广告
		CommonManager.getInstance(this).init("cfdbdd2786ea88ea",
				"d8edde7d10dd0073", false);
		
		// 用户数据统计功能，开启这个功能后，开发者可以在后台查看开发者应用的启动用户数、新增用户数、活跃用户数
		// 必须在应用启动的第一个activity中调用
		CommonManager.getInstance(this).setUserDataCollect(true);
		
		DevInit.initDianleContext(this, "072cb4d9d9d5dfd23ed2981e5e33fe59");//
		DevInit.setCurrentUserID("1234");
	}

	private void initView() {
		ImageView splash_image = (ImageView) findViewById(R.id.iv_splash_image);
		
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		splash_image.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				goToNextActivity();
			}
		});
	}
	
	private void goToNextActivity() {
		boolean isFirst = (Boolean) SPUtils.get(mContext, "isFist",
				true);
		if (isFirst) {
			Intent intent = new Intent(mContext, GuiActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra("Login", Constant.LOGIN_TO_HOME_CODE);
			startActivity(intent);
		}
		finish();
	}

}
