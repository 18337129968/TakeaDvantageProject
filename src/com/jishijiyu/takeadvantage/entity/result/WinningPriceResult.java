package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

public class WinningPriceResult {
	private String c;
	private WinningDataresult p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public WinningDataresult getP() {
		return p;
	}

	public void setP(WinningDataresult p) {
		this.p = p;
	}

	public static class WinningDataresult {
		private List<WinningListData> winList;
		private String number;
		private boolean isTrue;

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public List<WinningListData> getWinList() {
			return winList;
		}

		public void setWinList(List<WinningListData> winList) {
			this.winList = winList;
		}

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

	}

	public static class WinningListData implements Serializable {
		private String copperLockNum;
		private String createTime;
		private String ernieId;
		private String goldLockNum;
		private String id;
		private String isAward;
		private String prizeId;
		private String prizeImgurl;
		private String prizeName;
		private String silverLockNum;
		private String type;
		private String userId;
		private String winGrade;
		private String winTime;

		public String getCopperLockNum() {
			return copperLockNum;
		}

		public void setCopperLockNum(String copperLockNum) {
			this.copperLockNum = copperLockNum;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getErnieId() {
			return ernieId;
		}

		public void setErnieId(String ernieId) {
			this.ernieId = ernieId;
		}

		public String getGoldLockNum() {
			return goldLockNum;
		}

		public void setGoldLockNum(String goldLockNum) {
			this.goldLockNum = goldLockNum;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIsAward() {
			return isAward;
		}

		public void setIsAward(String isAward) {
			this.isAward = isAward;
		}

		public String getPrizeId() {
			return prizeId;
		}

		public void setPrizeId(String prizeId) {
			this.prizeId = prizeId;
		}

		public String getPrizeImgurl() {
			return prizeImgurl;
		}

		public void setPrizeImgurl(String prizeImgurl) {
			this.prizeImgurl = prizeImgurl;
		}

		public String getPrizeName() {
			return prizeName;
		}

		public void setPrizeName(String prizeName) {
			this.prizeName = prizeName;
		}

		public String getSilverLockNum() {
			return silverLockNum;
		}

		public void setSilverLockNum(String silverLockNum) {
			this.silverLockNum = silverLockNum;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getWinGrade() {
			return winGrade;
		}

		public void setWinGrade(String winGrade) {
			this.winGrade = winGrade;
		}

		public String getWinTime() {
			return winTime;
		}

		public void setWinTime(String winTime) {
			this.winTime = winTime;
		}

	}
}
