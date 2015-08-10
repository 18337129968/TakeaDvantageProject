package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 拍币摇奖晒奖中奖记录
 * 
 * @author sunaoyang
 * 
 */
public class IntegerPrizeNoteRequest {
	public String c = Constant.INTEGRAL_PRIZE_NOTE;
	public Parameter p;

	public IntegerPrizeNoteRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
	}
}
