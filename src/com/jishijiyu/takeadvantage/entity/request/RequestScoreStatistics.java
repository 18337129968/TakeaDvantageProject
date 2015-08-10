package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestScoreStatistics {
	public String c=Constant.REQUEST_SCORE_STATISTICS_CODE;
	public Parameter p;
	
	public RequestScoreStatistics(){
		p=new Parameter();
	}
	
	public class Parameter{
		public String userId;
		public String tokenId;
	}
}
