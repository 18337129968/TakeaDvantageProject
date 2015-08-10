package com.jishijiyu.takeadvantage.entity.result;

/**
 * 
 * @author zhengjianxiong
 * @content 商户提现的实体类
 * 
 */
public class AccountWithdrawalsResult {
	public String c;
	public Parameter p;

	public AccountWithdrawalsResult() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public boolean isSucce;
		public boolean isTrue;
	}
}
