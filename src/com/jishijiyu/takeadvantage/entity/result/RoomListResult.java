package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

public class RoomListResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<RoomList> roomList;
		public int roomCount;
	}

	public class RoomList {
		public int id;
		public int mealId;
		public String mealName;
		public int mealType;
		public int prizeId;
		public String prizeName;
		public String prizeURL;
		public int roomIssue;
		public int userId;
		public int joinNumber;
		public int state;
		public int type;

	}
}
