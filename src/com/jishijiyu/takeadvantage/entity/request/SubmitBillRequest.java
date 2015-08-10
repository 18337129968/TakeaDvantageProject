package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class SubmitBillRequest {

	public String c = Constant.SUBMIT_BILL_REQUEST_CODE;
	public Parameter p;
	public SubmitBillRequest(){
		p = new Parameter();
	}
	public class Parameter{
		public String roomIds;
		public String tokenId;
		public String userId;
	}
}
