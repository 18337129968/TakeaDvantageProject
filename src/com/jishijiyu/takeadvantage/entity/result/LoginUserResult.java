package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

/**
 * 用户登录返回
 * 
 * @author shifeiyu
 * @version 2015年5月29日11:08:18
 * 
 */
public class LoginUserResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isSucce;
		public int answerMaxScore;
		public ArrayList<BannerList> bannerList;
		public ArrayList<UserNotice> notReadNoticeList;
		public long nowTime;
		public User user;
		public Ernie ernie;
		public Enroll enroll;
		public int inviteScore;
		public String tokenId;
	}

	public class BannerList {
		public long createTime;
		public int id;
		public String imgName;
		public String imgSize;
		public String imgUrl;
		public String linkUrl;
		public int status;

	}

	public static class Enroll {
		public long createTime;
		public long joinBegintime;
		public int ernieId;
		public int id;
		public int prizeId;
		public int userId;

	}

	public class Ernie {
		public int createPerson;
		public long createTime;
		public long ernieBegintime;
		public long ernieEndtime;
		public String ernieStatus;
		public int id;
		public long joinBegintime;
		public long joinEndtime;
		public int joinNumber;
		public int periods;
		public String releaseStatus;
	}

	public class User {
		public int answerTodyScore;
		public int copperLockNum;
		public long createTime;
		public int goldLockNum;
		public int silverLockNum;
		public int goldNum;
		public int id;
		public String inviteCode;
		public int inviteUserNum;
		public int lastAddScoreDay;
		public long lastLoginTime;
		public String mobile;
		public String password;
		public int score;
		public int state;
		public int todayScore;
		public int totalScore;
		public int type;
		public int vipLevel;
	}

}
