package com.jishijiyu.takeadvantage.entity.result;

public class RefuseInviteResult {
	public String c;
	public Parameter p;

	public RefuseInviteResult() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public boolean isTrue;
		public boolean isSucce;
	}
}
