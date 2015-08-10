package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;

import com.jishijiyu.takeadvantage.activity.ernieonermb.JoinYYErnieRequest.Parameter;
import com.jishijiyu.takeadvantage.utils.Constant;

public class JoinErniePrizeListRequest {
	public String c = Constant.ERNIE_PRIZE_LIST;
	public Parameter p;
	
	public JoinErniePrizeListRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomId;
	}
}
