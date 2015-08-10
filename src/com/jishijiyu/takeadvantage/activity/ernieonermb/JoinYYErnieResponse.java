package com.jishijiyu.takeadvantage.activity.ernieonermb;

public class JoinYYErnieResponse {
	public String c ;
	public Parameter p;

	public JoinYYErnieResponse() {
		this.p = new Parameter();
	}

	public class Parameter {
		public int[] winningNum;
		public String startErnieType;
		public boolean isTrue;
	}
}
