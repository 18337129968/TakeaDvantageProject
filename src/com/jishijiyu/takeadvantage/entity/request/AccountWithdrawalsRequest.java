package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * @author zhengjianxiong
 * @content 商户提现请求服务器
 * 
 */
public class AccountWithdrawalsRequest {
	public String c = Constant.ACCOUNTWITHDRAWALS;
	public Parameter p;

	public AccountWithdrawalsRequest() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public String accountName;
		public String bankCard;
		public String drawCost;
		public String drawFee;
		public String tokenId;
		public String userId;
		public String bankName;
	}
}
