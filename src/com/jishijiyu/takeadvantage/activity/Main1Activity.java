package com.jishijiyu.takeadvantage.activity;


import com.android.xkeuops.CommonManager;
import com.dlnetwork.DevInit;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ernie.CheckPriceActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

import cn.waps.AppConnect;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class Main1Activity extends HeadBaseActivity {
	private Button btn_wp = null;
	private Button btn_dl = null;
	private Button btn_ym = null;
	private FrameLayout frameLayout = null;
	private FragmentManager fragmentManager = null;
	private int btnId = R.id.btn_wp;

	private WpActivity wpActivity = null;

	// private YMActivity ymActivity = null;
	private DlActivity dlActivity = null;

	@Override
	public void appHead(View view) {
		top_text.setText("赚取金币");
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
//		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
//		View view = View.inflate(Main1Activity.this, R.layout.get_gold, null);
//		base_centent.addView(view);
//		AppManager.getAppManager().addActivity(this);
//		setOfferBrowserConfig();
//		initDL();
//		initWP();
//		initYm();
//		initview();
	}

	private void initWP() {
		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect
				.getInstance("739adb7718dd0350751cf44bb20dd926", "waps", this);
		// 预加载自定义广告内容（仅在使用了自定义广告、抽屉广告或迷你广告的情况，才需要添加）
		AppConnect.getInstance(this).initAdInfo();
		// (可选)关闭有米log输出，建议开发者在嵌入有米过程中不要关闭，以方便随时捕捉输出信息，出问题时可以快速定位问题
		// AdManager.getInstance(Context context).setEnableDebugLog(false);
	}

	private void initDL() {
		// 在应用的入口处设置context
		// 如果是Activity,在第一个启动的Acitivty里面的onCreate的开始写上下面的代码；
		// 如果没有Acitivity，只有Service，在onBind或者onStart方法的开始写上下面的代码
		// 设置app id
		// "072cb4d9d9d5dfd23ed2981e5e33fe59"，是你的应用在点乐服务器注册生成的key,详情请看文档。
		// mDefaultCid : 渠道号，默认的是，可以不要这个参数，原来的DevInit.initDevInitContext(this,
		// "072cb4d9d9d5dfd23ed2981e5e33fe59")方法也有效。。
		
	}

	private void initYm() {
		
		// (可选)关闭有米log输出，建议开发者在嵌入有米过程中不要关闭，以方便随时捕捉输出信息，出问题时可以快速定位问题
		// AdManager.getInstance(Context context).setEnableDebugLog(false);

		// 初始化接口，应用启动的时候调用，参数：appId, appSecret
//		AdManager.getInstance(this)
//				.init("cfdbdd2786ea88ea", "d8edde7d10dd0073");

		// (可选)开启用户数据统计服务,(sdk v4.08之后新增功能)默认不开启，传入false值也不开启，只有传入true才会调用
//		AdManager.getInstance(this).setUserDataCollect(true);

		// 如果开发者使用拍币墙的服务器回调
		// 1.需要告诉sdk，现在采用服务器回调
		// 2.建议开发者传入自己系统中用户id（如：邮箱账号之类的）（请限制在50个字符串以内）
		// OffersManager.setUsingServerCallBack(true);
		// OffersManager.getInstance(this).setCustomUserId("user_id");

		// 如果使用拍币广告，请务必调用拍币广告的初始化接口:
//		OffersManager.getInstance(this).onAppLaunch();
		// （可选）注册拍币监听-随时随地获得拍币的变动情况
		// PointsManager.getInstance(this).registerNotify(this);

		// （可选）注册拍币订单赚取监听（sdk v4.10版本新增功能）
		// PointsManager.getInstance(this).registerPointsEarnNotify(this);

		// (可选)设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
		// AdManager.setIsDownloadTipsDisplayOnNotification(false);
		
		// (可选)设置安装完成后是否在通知栏显示已安装成功的通知。默认为true，标识开启；设置为false则关闭。（sdk
		// v4.10版本新增功能）
		// AdManager.setIsInstallationSuccessTipsDisplayOnNotification(false);
		
		// (可选)设置是否在通知栏显示拍币赚取提示。默认为true，标识开启；设置为false则关闭。
		// PointsManager.setEnableEarnPointsNotification(false);
		
		// (可选)设置是否开启拍币赚取的Toast提示。默认为true，标识开启；设置为false这关闭。
		// PointsManager.setEnableEarnPointsToastTips(false);
		// 有米android 拍币墙sdk 5.0.0之后支持定制浏览器顶部标题栏的部分UI
		// setOfferBrowserConfig();
	}

	@SuppressLint("NewApi")
	private void initview() {
		this.btn_dl = (Button) findViewById(R.id.btn_dl);
		this.btn_ym = (Button) findViewById(R.id.btn_ym);
		this.btn_wp = (Button) findViewById(R.id.btn_wp);
		this.frameLayout = (FrameLayout) findViewById(R.id.frame);
		this.btn_dl.setOnClickListener(this);
		this.btn_ym.setOnClickListener(this);
		this.btn_wp.setOnClickListener(this);
		DlActivity dlActivity = new DlActivity();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.frame, dlActivity);
		fragmentTransaction.commit();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		FragmentTransaction fragmentTransaction = null;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(Main1Activity.this);
			break;
		case R.id.btn_dl:
			if (btnId != R.id.btn_dl) {
				btnId = R.id.btn_dl;
				btn_dl.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_ym.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_wp.setBackgroundResource(R.drawable.btn_headabove_light);
				fragmentTransaction = getFragmentManager().beginTransaction();
				dlActivity = new DlActivity();
				fragmentTransaction.replace(R.id.frame, dlActivity);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			break;
		case R.id.btn_ym:
			if (btnId != R.id.btn_ym) {
				btnId = R.id.btn_ym;
				btn_ym.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_dl.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_wp.setBackgroundResource(R.drawable.btn_headabove_light);
				fragmentTransaction = getFragmentManager().beginTransaction();
				YMActivity ymActivity = new YMActivity();
				fragmentTransaction.replace(R.id.frame, ymActivity);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			break;
		case R.id.btn_wp:
			if (btnId != R.id.btn_wp) {
				btnId = R.id.btn_wp;
				btn_wp.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_ym.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_dl.setBackgroundResource(R.drawable.btn_headabove_light);
				WpActivity wpActivity = new WpActivity();
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame, wpActivity);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			break;
		}
	}

	/**
	 * 设置拍币墙浏览器标题栏样式
	 */
//	private void setOfferBrowserConfig() {
//
//		// 设置标题栏——标题
//		OffersBrowserConfig.setBrowserTitleText("拍的利");
//
//		// 设置标题栏——背景颜色（ps：拍币墙标题栏默认背景颜色为#FFBB34）
//		OffersBrowserConfig.setBrowserTitleBackgroundColor(Color.RED);
//
//		// 设置标题栏——是否显示拍币墙右上角拍币余额区域 true：是 false：否
//		OffersBrowserConfig.setPointsLayoutVisibility(true);
//
//	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AppManager.getAppManager().finishActivity(Main1Activity.this);
	}
}
