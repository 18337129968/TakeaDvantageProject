package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元摇奖晒奖中奖记录
 * 
 * @author sunaoyang
 * 
 */
public class OnePrizeNoteRequest {
	public String c = Constant.ONE_PRIZE_NOTE;
	public Parameter p;

	public OnePrizeNoteRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
	}
}
