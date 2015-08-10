package com.jishijiyu.takeadvantage.entity.result;

public class ExchangeDetailsPostdata {
	private String c;
	public BranchData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public BranchData getP() {
		return p;
	}

	public void setP(BranchData p) {
		this.p = p;
	}

	public static class BranchData {
		private String goodsId;
		private String page;
		private String pageSize;
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

		public String getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}

	}
}
