package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 房间详情返回
 * 
 * @author shifeiyu
 * 
 */
public class RoomDetailsResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public long nowTime;
		public dollerEnrollVO dollerEnrollVO;
		public List<enrollList> enrollList;
		public dollerRoom dollerRoom;
		public boolean isTrue;
		public boolean isSucce;
		public boolean isDollerEnroll;
		public meal meal;
		public List<prizeList> prizeList;
	}

	public class dollerEnrollVO {
		public long enrollTime;
		public int userId;
		public String userName;
		public String headImgUrl;
		public String province;
	}

	public class enrollList {
		public long enrollTime;
		public int userId;
		public String userName;
		public String headImgUrl;
		public String province;

	}

	public class meal {
		public String mealName;
		public int mealType;
		public long createTime;
		public int first1Id;
		public int first2Id;
		public int first3Id;
		public int first4Id;
		public int id;
		public int lastPerios;
		public int mealState;
	}

	public class prizeList {
		public int grade;
		public String img;
		public String name;
		public int places;
		public int companyId;
		public long createTime;
		public String explain;
		public int freight;
		public int id;
		public int price;
		public String companyName;

	}

	public class dollerRoom {
		public long createTime;
		public int id;
		public int joinNumber;
		public int lotteryState;
		public int mealId;
		public int mealPerios;
		public int roomIssue;
		public int state;
		public int type;
		public int userId;
	}
}
