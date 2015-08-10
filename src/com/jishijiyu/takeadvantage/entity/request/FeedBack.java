package com.jishijiyu.takeadvantage.entity.request;


/**
 * 意见反馈请求
 * 
 * @author baohan
 * 
 */
public class FeedBack {
	public String c   ;
	public Paramet p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public Paramet getP() {
		return p;
	}

	public void setP(Paramet p) {
		this.p = p;
	}

	public FeedBack() {
		this.p = new Paramet();
	}

	public static  class Paramet {
		public String contents;
		public String userId;
		 public String tokenId;
		 
		public String getTokenId() {
			return tokenId;
		}
		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}
		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	}
}
