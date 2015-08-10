package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 房间号查找房间返回信息
 * 
 * @author shifeiyu
 * 
 */
public class RoomNumSearchRoomResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public DollerRoomTO dollerRoomTO;
	}

	public class DollerRoomTO {
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
		public int pageSize;
		public int startNum;
	}
}
