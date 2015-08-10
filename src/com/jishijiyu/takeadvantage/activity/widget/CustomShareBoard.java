/**
 * 
 */

package com.jishijiyu.takeadvantage.activity.widget;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.Constants;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {
	View rootView;
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);
	private Activity mActivity;

	public CustomShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
	}

	@SuppressWarnings("deprecation")
	public void initView(boolean isDisplay) {
		rootView = LayoutInflater.from(mActivity).inflate(R.layout.pop_share,
				null);
		if (!isDisplay) {
			rootView.findViewById(R.id.friend_request_qzone).setVisibility(
					View.GONE);
			rootView.findViewById(R.id.friend_request_wechat).setVisibility(
					View.GONE);
			rootView.findViewById(R.id.friend_request_sina).setVisibility(
					View.GONE);
		}
		rootView.findViewById(R.id.friend_request_wechat).setOnClickListener(
				this);
		rootView.findViewById(R.id.friend_request_sina)
				.setOnClickListener(this);
		rootView.findViewById(R.id.friend_request_wechat_circle)
				.setOnClickListener(this);
		rootView.findViewById(R.id.friend_request_qq).setOnClickListener(this);
		rootView.findViewById(R.id.friend_request_qzone).setOnClickListener(
				this);
		rootView.findViewById(R.id.pop_share_close).setOnClickListener(this);
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		// setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.friend_request_wechat:
			performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.friend_request_wechat_circle:
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.friend_request_sina:
			performShare(SHARE_MEDIA.SINA);
			break;
		case R.id.friend_request_qq:
			performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.friend_request_qzone:
			performShare(SHARE_MEDIA.QZONE);
			break;
		case R.id.pop_share_close:
			dismiss();
			break;
		default:
			break;
		}
	}

	public void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					Intent intent1 = new Intent();
					intent1.setAction(Constant.DYNAMICACTION);
					mActivity.sendBroadcast(intent1);
					showText += "平台分享成功";
				} else {
					showText += "平台分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}

}
