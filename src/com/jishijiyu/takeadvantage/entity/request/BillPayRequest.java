package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class BillPayRequest {
	
	public String c = Constant.BILL_PAY_REQUEST_CODE;
	public Parameter p;
	
	
	public BillPayRequest() {
		p = new Parameter();
	}


	public class Parameter {
		public int userId;
		public int tokenId;
		public int count;
		public int roomId;
		public int[] roomIds;
	}
	
}
