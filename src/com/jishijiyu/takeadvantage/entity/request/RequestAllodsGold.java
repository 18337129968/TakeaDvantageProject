package com.jishijiyu.takeadvantage.entity.request;


/**
 * 提交任务请求
 * 
 * @author zm
 * 
 */
public class RequestAllodsGold {
	public String c   ;
	public RequestSonData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public RequestSonData getP() {
		return p;
	}

	public void setP(RequestSonData p) {
		this.p = p;
	}

	public static class RequestSonData {
		public String id;
		public String userId;
		public String tokenId;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getTokenId() {
			return tokenId;
		}

		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}

	}
}
