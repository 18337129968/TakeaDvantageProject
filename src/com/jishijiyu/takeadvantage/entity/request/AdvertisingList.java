package com.jishijiyu.takeadvantage.entity.request;


/**
 * 广告列表请求
 * 
 * @author baohan
 * 
 */
public class AdvertisingList {
	public String c;
	public ParameterA p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public ParameterA getP() {
		return p;
	}

	public void setP(ParameterA p) {
		this.p = p;
	}

	public class ParameterA {
		public String page;
		public String pageSize;
		public String type;
		public String tokenId;
		public String userId;

		public String getPage() {
			return page;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
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

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTokenId() {
			return tokenId;
		}

		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}

	}
}
