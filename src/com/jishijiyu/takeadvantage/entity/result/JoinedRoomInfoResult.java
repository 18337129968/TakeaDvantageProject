package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 已参与房间详情返回
 * 
 * @author shifeiyu
 * 
 */
public class JoinedRoomInfoResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public List<DollerHistoryWinningVOList> dollerHistoryWinningVOList;
		public DollerRoom dollerRoom;
		public boolean isTrue;
		public Meal meal;
		public List<PrizeList> prizeList;
	}

	public class PrizeList {
		public int companyId;
		public long createTime;
		public String explain;
		public String companyName;
		public int freight;
		public int grade;
		public int id;
		public String img;
		public String name;
		public int places;
		public int price;

	}

	public class Meal {
		public long createTime;
		public int first1Id;
		public int first2Id;
		public int first3Id;
		public int first4Id;
		public int id;
		public int lastPerios;
		public int mealState;
		public int mealType;
		public String mealName;
	}

	public class DollerRoom {
		public long beginTime;
		public long createTime;
		public long endTime;
		public int id;
		public int joinNumber;
		public int lotteryState;
		public int mealId;
		public int mealPerios;
		public int roomIssue;
		public int startErnieType;
		public int state;
		public int type;
		public int userId;
	}

	public class DollerHistoryWinningVOList {
		public long beginTimep;
		public int grade;
		public String headImgUrl;
		public int id;
		public String nickname;
		public int perios;
		public String province;
		public long winTime;

	}
}
