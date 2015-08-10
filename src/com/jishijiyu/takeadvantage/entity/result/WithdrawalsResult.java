package com.jishijiyu.takeadvantage.entity.result;

public class WithdrawalsResult {
	public String c;
	public Parameter p;

	public WithdrawalsResult() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public boolean isTrue;
		public boolean isSucce;
	}

}
