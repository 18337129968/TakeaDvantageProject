package com.jishijiyu.takeadvantage.activity.ernieonermb;
import com.jishijiyu.takeadvantage.utils.Constant;
public class GetPrizeDataRequest {
	public String c = Constant.ERNIE_GET_PRIZE;
	public Parameter p;
	
	public GetPrizeDataRequest() {
		// TODO Auto-generated constructor stub
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String awardId;
		
	}
}
