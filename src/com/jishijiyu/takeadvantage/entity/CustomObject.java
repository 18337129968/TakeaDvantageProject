package com.jishijiyu.takeadvantage.entity;

import com.android.xkeuops.diy.AppSummaryObject;
import android.graphics.Bitmap;

/**
 * 自定义广告列表数据，因为广告列表是没有帮开发者处理图片方面的下载的，所以开发者如果要显示广告图片的话，需要自定义一个新的Object来存放图片，并实现相关的图片下载功能
 */
public class CustomObject {

	private AppSummaryObject appSummaryObject;
	private Bitmap appicon;
	private boolean isShowMultSameAd = false;
	private int extraTaskIndex;

	/**
	 * 获取广告数据
	 * 
	 * @return
	 */
	public AppSummaryObject getAppSummaryObject() {
		return appSummaryObject;
	}

	public void setAppSummaryObject(AppSummaryObject appSummaryObject) {
		this.appSummaryObject = appSummaryObject;
	}

	/**
	 * 获取该广告的图标
	 * 
	 * @return
	 */
	public Bitmap getAppicon() {
		return appicon;
	}

	public void setAppicon(Bitmap appicon) {
		this.appicon = appicon;
	}

	/**
	 * 获取需要展示的追加任务的index
	 * 
	 * @return
	 */
	public int getShowExtraTaskIndex() {
		return extraTaskIndex;
	}

	/**
	 * 设置需要展示的追加任务的index
	 * 
	 * @param extraTaskIndex
	 */
	public void setShowExtraTaskIndex(int extraTaskIndex) {
		this.extraTaskIndex = extraTaskIndex;
	}

	/**
	 * 是否展示同一个广告多次（应用在请求追加任务列表中，一般配合{@link getShowExtraTaskIndex}的使用）
	 * 
	 * @return
	 */
	public boolean isShowMultSameAd() {
		return isShowMultSameAd;
	}

	/**
	 * 设置是否展示同一个广告多次（应用在请求追加任务列表中，一般配合{@link setShowExtraTaskIndex}的使用）
	 * 
	 * @return
	 */
	public void setShowMultSameAd(boolean isShowMultSameAd) {
		this.isShowMultSameAd = isShowMultSameAd;
	}

}
