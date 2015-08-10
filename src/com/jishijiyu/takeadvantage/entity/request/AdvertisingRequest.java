package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 发布广告请求
 * 
 * @author shifeiyu
 * 
 */
public class AdvertisingRequest {
	public String c = Constant.ADVERTSING_REQUEST_CODE;
	public Parameter p;

	public AdvertisingRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String heartMemery;
		public String posterSign;
		public String posterTitle;
		public String tokenId;
		public String userId;
		public String posterDescribe;
		public String posterImgUrl_1;
		public String posterImgUrl_2;
		public String posterImgUrl_3;
		public String posterImgUrl_4;
		public String posterImgUrl_5;
		public String questionDescribe;
		public String questionOption_1;
		public String questionOption_2;
		public String questionOption_3;
		public String answer;
	}
}
