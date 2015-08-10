package com.jishijiyu.takeadvantage.entity.request;

public class WinningPriceRequest {
	private String c;
	private WinningData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public WinningData getP() {
		return p;
	}

	public void setP(WinningData p) {
		this.p = p;
	}

	public static class WinningData {
		private String page;
		private String pageSize;
		private String userId;
		private String tokenId;

		public String getTokenId() {
			return tokenId;
		}

		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public String getPageSize() {
			return pageSize;
		}

		public void setPageSize(String pageSize) {
			this.pageSize = pageSize;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}

}
