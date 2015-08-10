package com.jishijiyu.takeadvantage.activity.ernieonermb;

public class GetPrizeDataResponse {
	public String c ;
	public Parameter p;
	
	public GetPrizeDataResponse() {
		this.p = new Parameter();
	}

	public class Parameter {
		public boolean isTrue;
		public boolean isSucce;
	}
}
