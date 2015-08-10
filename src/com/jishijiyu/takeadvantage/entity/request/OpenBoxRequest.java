package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 开宝箱请求
 * 
 * @author shifeiyu
 * 
 */
public class OpenBoxRequest {
	public String c = Constant.OPEN_BOX_REQUEST_CODE;
	public Parameter p;

	public OpenBoxRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String activityId;
		public String id;
		public String tokenId;
		public String userId;

	}
}
