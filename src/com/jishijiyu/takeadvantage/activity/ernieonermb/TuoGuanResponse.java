package com.jishijiyu.takeadvantage.activity.ernieonermb;

public class TuoGuanResponse {
	public String c ;
	public Parameter p;
	
	public TuoGuanResponse() {
		
		this.p = new Parameter();
	}

	public class Parameter {
		public boolean isTrue;
		public boolean isSucce;
	}
}
