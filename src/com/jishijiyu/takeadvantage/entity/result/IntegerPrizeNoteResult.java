package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

/**
 * 拍币摇奖晒奖中奖记录
 * 
 * @author sunaoyang
 * 
 */
public class IntegerPrizeNoteResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public ArrayList<WrsList> wrsList;
		public boolean isTrue;
	}

	public class WrsList {
		public Integer awardGrade;
		public Integer awardId;
		public String awardName;
		public String periods;
	}
}
