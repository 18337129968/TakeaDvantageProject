package com.jishijiyu.takeadvantage.activity.widget;

import android.app.Activity;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public abstract class SetSnsPostListener implements SnsPostListener {
	public boolean isSuccess;
	private Activity activity;
	public  SetSnsPostListener(Activity activity) {
		this.activity = activity;

	}
	@Override
	public void onComplete(SHARE_MEDIA platform, int eCode,
			SocializeEntity entity) {
		String showText = platform.toString();
		if (eCode == StatusCode.ST_CODE_SUCCESSED) {
			// shareUtil.isShareSuccess =true;

//			setShareState(true);
			get();
			showText += "平台分享成功";
		} else {
			// shareUtil.isShareSuccess=false;
			setShareState(false);
			showText += "平台分享失败";
		}
		Toast.makeText(activity, showText, Toast.LENGTH_SHORT).show();
//		pop.dismiss();
	}

	@Override
	public void onStart() {

	}
	public boolean isShare() {
		return isSuccess;
	}

	public void setShareState(boolean isSuccess) {
		
		this.isSuccess = isSuccess;
	}
	abstract void get();
}
