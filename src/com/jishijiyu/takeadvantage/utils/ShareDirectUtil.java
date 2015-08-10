package com.jishijiyu.takeadvantage.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.widget.ShareTool;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class ShareDirectUtil {
	public UMSocialService mController;
	public Activity activity;
	public Context context;
	private String shareUrl = null;

	public ShareDirectUtil(Activity activity, Context context) {
		this.activity = activity;
		this.context = context;
		addQQQZonePlatform();
		addWXPlatform();
		setShareData("拍得利，你来就赢！", null);

	}

	public ShareDirectUtil(Activity activity, Context context, String messages,
			String url) {
		this.activity = activity;
		this.context = context;
		shareUrl = url;
		addQQQZonePlatform();
		addWXPlatform();
		setShareData(messages, null);

	}

	private void setShareData(String msg, Object object) {
		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent(msg);
		// 设置分享图片, 参数2为图片的url地址
		if (object == null) {
			mController.setShareMedia(new UMImage(context,
					R.drawable.ic_launcher));
		}
		if (object instanceof Bitmap) {
			mController.setShareMedia(new UMImage(context, (Bitmap) object));
		} else if (object instanceof Integer) {
			mController.setShareMedia(new UMImage(context, (Integer) object));
		} else if (object instanceof File) {
			mController.setShareMedia(new UMImage(context, (File) object));
		} else if (object instanceof String) {
			mController.setShareMedia(new UMImage(context, object.toString()));
		} else if (object instanceof byte[]) {
			mController.setShareMedia(new UMImage(context, (byte[]) object));
		}

	}

	public void openShare() {
		mController.openShare(activity, false);
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	public void postShare(boolean isDisplay) {

		ShareTool shareBoard = new ShareTool(activity);
		shareBoard.initView(isDisplay);
		shareBoard.showAtLocation(activity.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);

	}

	public void postShare() {

		ShareTool shareBoard = new ShareTool(activity);
		shareBoard.initView(true);
		shareBoard.showAtLocation(activity.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);

	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		// String appId = "wx967daebe835fbeac";
		// String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// 公司的
		// String appId = "wx4d979f58d5966865";
		// String appSecret = "5d5cdafa1d67683abea1cbbcd2abe31b";
		// sunaoyang的
		String appId = "wx1f42a9c6643f6201";
		String appSecret = "41063fbaecbda8ed02ac7b941c4a72bb";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl:
	 *       用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, appId,
				appKey);
		qqSsoHandler.setTargetUrl(shareUrl);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId,
				appKey);
		qZoneSsoHandler.setTargetUrl(shareUrl);
		qZoneSsoHandler.addToSocialSDK();
	}

}
