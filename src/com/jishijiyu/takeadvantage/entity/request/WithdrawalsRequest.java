package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class WithdrawalsRequest {
    public String c = Constant.WITHDRAWALS;
    public Parameter p;
    public WithdrawalsRequest() {
		// TODO Auto-generated constructor stub
    	p = new Parameter();
	}
    public class Parameter{
    	public String userId;
    	public String goldNum;
    	public String tokenId;
    	public String accountName;
    	public String brandName;
    	public String brankCard;
    	public String feeNum;
    }
}
