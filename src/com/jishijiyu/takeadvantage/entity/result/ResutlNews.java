package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;


public class ResutlNews {
	public String c;
	public Pramater p;
	
	public class Pramater{
		public ArrayList<UserNotice> userNoticeList;
	}
	public class UserNotice{
		public int contentId;
		public int state;
		public String title;
		public int userId;
	}
}
