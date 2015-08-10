package com.jishijiyu.takeadvantage.receiver;

import com.jishijiyu.takeadvantage.utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (Integer.parseInt(intent.getAction())) {
		case 0:
			dynamic();
			break;
		case 1:
			ernieBegintime();
			break;
		case 2:
			ernieEndtime();
			break;
		case 3:
			joinBegintime();
			break;
		case 4:
			joinEndtime();
			break;
		}
	}

	/**
	 * 分享成功
	 * 
	 * @return void
	 * @throws
	 */
	public void dynamic() {
	};

	/**
	 * 摇奖开始时间
	 * 
	 * @return void
	 * @throws
	 */
	public void ernieBegintime() {
	};

	/**
	 * 摇奖结束时间
	 * 
	 * @return void
	 * @throws
	 */
	public void ernieEndtime() {
	};

	/**
	 * 报名开始时间
	 * 
	 * @return void
	 * @throws
	 */
	public void joinBegintime() {
	};

	/**
	 * 报名结束时间
	 * 
	 * @return void
	 * @throws
	 */
	public void joinEndtime() {
	};

}
