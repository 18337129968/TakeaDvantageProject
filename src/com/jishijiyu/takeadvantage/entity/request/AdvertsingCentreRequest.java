package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 投放中广告请求
 * 
 * @author shifeiyu
 * 
 */
public class AdvertsingCentreRequest {
	public String c = Constant.ADVERTSING_CENTRE_REQUEST_CODE;
	public Parameter p;

	public AdvertsingCentreRequest() {
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
