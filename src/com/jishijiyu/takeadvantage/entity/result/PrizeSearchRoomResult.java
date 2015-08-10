package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 奖品查找房间返回
 * 
 * @author shifeiyu
 * 
 */
public class PrizeSearchRoomResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<roomList> roomList;
	}

	public class roomList {
		public int id;
		public int joinNumber;
		public int mealId;
		public String mealName;
		public int mealType;
		public int prizeId;
		public String prizeName;
		public String prizeURL;
		public int roomIssue;
		public int state;
		public int type;
	}
}
