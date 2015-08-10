package com.jishijiyu.takeadvantage.entity.request;


/**
 * 广告详情请求
 * 
 * @author zm
 * 
 * 
 */
public class AdvertisingDetails {
	public String c;
	public ParameterD p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public ParameterD getP() {
		return p;
	}

	public void setP(ParameterD p) {
		this.p = p;
	}

	public static class ParameterD {
		public String posterId;
		public String tokenId;
		public String userId;

		public String getPosterId() {
			return posterId;
		}

		public void setPosterId(String posterId) {
			this.posterId = posterId;
		}

		public String getTokenId() {
			return tokenId;
		}

		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

 
	}
}
