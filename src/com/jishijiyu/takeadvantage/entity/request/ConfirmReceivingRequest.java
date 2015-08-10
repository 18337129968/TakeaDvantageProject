package com.jishijiyu.takeadvantage.entity.request;

public class ConfirmReceivingRequest {
	// {"c":"4001","p":{"id":"40","userId":"1"}}
	private String c;
	private Confirmdata p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public Confirmdata getP() {
		return p;
	}

	public void setP(Confirmdata p) {
		this.p = p;
	}

	public static class Confirmdata {
		private String id;
		private String userId;
		private String tokenId;

		public String getId() {
			return id;
		}

		 
		public String getTokenId() {
			return tokenId;
		}


		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
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

	}
}
