package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 投放后广告请求
 * 
 * @author shifeiyu
 * 
 */
public class AdvertsingEndRequest {
	public String c = Constant.ADVERTSING_END_REQUEST_CODE;
	public Parameter p;

	public AdvertsingEndRequest() {
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
