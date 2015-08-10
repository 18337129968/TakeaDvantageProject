package com.jishijiyu.takeadvantage.entity.request;

public class HomeAppliancesRequest {
	private String c;
	private HomeAppData p;

	// {"c":"2005","p":{"page":"0","pageSize":"3","type1":"1"}}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public HomeAppData getP() {
		return p;
	}

	public void setP(HomeAppData p) {
		this.p = p;
	}

	public static class HomeAppData {
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

		public String getType1() {
			return type1;
		}

		public void setType1(String type1) {
			this.type1 = type1;
		}

		private String page;
		private String pageSize;
		private String type1;
		 public String tokenId;
			public String userId;
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
