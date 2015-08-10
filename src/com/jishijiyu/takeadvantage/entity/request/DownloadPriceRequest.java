package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 获取拍币
 * 
 * @author zhaobin
 */
public class DownloadPriceRequest {

	public String c = Constant.DownloadPriceCODE;
	public Parameter p;

	public DownloadPriceRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String appScore;
		public String userId;
		public String tokenId;
	}
}
