package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 投放前广告请求
 * 
 * @author shifeiyu
 * 
 */
public class AdvertsingBeforeRequest {
	public String c = Constant.ADVERTSING_BEFORE_REQUEST_CODE;
	public Parameter p;

	public AdvertsingBeforeRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String companyId;
		public String pageNum;
		public String pageSize;
		public String tokenId;
		public String userId;
	}
}
