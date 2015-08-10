package com.jishijiyu.takeadvantage.utils;

import android.content.Context;
import android.content.Intent;

import com.jishijiyu.takeadvantage.activity.login.LoginActivity;

/**
 * 调转到登录页面的工具类，包括把所有的activity干掉的功能
 * 
 * @author zhangmeng
 * 
 */
public class IntentActivity {

	@SuppressWarnings("static-access")
	public static void mIntent(Context context) {
		ToastUtils.makeText(context, "你的账号异常！请重新登陆", 0).show();
		AppManager.getAppManager().finishAllActivity();
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		context.startActivity(intent);
	}

}
