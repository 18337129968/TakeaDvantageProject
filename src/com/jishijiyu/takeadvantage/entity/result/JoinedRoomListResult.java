package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 已参与房间列表返回
 * 
 * @author shifeiyu
 * 
 */
public class JoinedRoomListResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public long nowTime;
		public List<roomOnMealVOList> roomOnMealVOList;
	}

	public class roomOnMealVOList {
		public int countNumber;
		public long endTime;
		public String imgUrl;
		public int number;
		public String packageName;
		public int perios;
		public String prizeName;
		public int roomId;
		public int roomIssue;
		public int roomState;
		public long startTime;
		public int userState;
		public int isShare;
		public int mealId;
		public int mealRecordId;
		public int grade;
	}
}
