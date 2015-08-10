package com.jishijiyu.takeadvantage.entity.result;

public class QueryGoldNumResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public User user;
	}

	public class User {
		public String answerTodyScore;
		public String copperLockNum;
		public long createTime;
		public String goldLockNum;
		public int goldNum;
		public String id;
		public String inviteCode;
		public String inviteUserNum;
		public String lastAddScoreDay;
		public long lastLoginTime;
		public String mobile;
		public String password;
		public String score;
		public String silverLockNum;
		public String state;
		public String todayScore;
		public String type;
		public String vipLevel;
	}
}
