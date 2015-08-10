package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 一元摇奖晒奖中奖记录
 * 
 * @author sunaoyang
 * 
 */
public class OnePrizeNoteResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public List<OneWrslist> oneWrslist;
		public boolean isTrue;
	}

	public class OneWrslist {
		public Integer awardGrade;
		public Integer awardId;
		public String awardName;
		public String mealName;
		public String periods;
		public String roomType;
	}
}
