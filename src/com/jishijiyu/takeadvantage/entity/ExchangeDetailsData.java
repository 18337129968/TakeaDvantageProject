package com.jishijiyu.takeadvantage.entity;

public class ExchangeDetailsData {
	private String c;
	private ExchangeDetailsP p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public ExchangeDetailsP getP() {
		return p;
	}

	public void setP(ExchangeDetailsP p) {
		this.p = p;
	}

	public static class ExchangeDetailsP {
		private ExchangeDetailsGoodId goodsInfo;
		private boolean isTrue;

		public ExchangeDetailsGoodId getGoodsInfo() {
			return goodsInfo;
		}

	 

		public boolean isTrue() {
			return isTrue;
		}



		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}



		public void setGoodsInfo(ExchangeDetailsGoodId goodsInfo) {
			this.goodsInfo = goodsInfo;
		}

	}

	public static class ExchangeDetailsGoodId {
		private String createPersion;
		private long createTime;
		private long deadline;
		private String freight;
		private String goodsBrand;
		private String goodsDetails;
		private String goodsImgUrl1;
		private String goodsImgUrl2;
		private String goodsImgUrl3;
		private String goodsImgUrl4;
		private String goodsImgUrl5;
		private String goodsName;
		private String goodsType1;
		private String goodsType2;
		private String goodsType3;
		private String id;
		private String price;
		private String procurePrice;
		private String score;
		private String shopId;
		private String specification;
		private String state;
		private String stockNumber;
		private String updatePersion;
		private String updateTime;
		private String companyName;

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getCreatePersion() {
			return createPersion;
		}

		public void setCreatePersion(String createPersion) {
			this.createPersion = createPersion;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}

		public long getDeadline() {
			return deadline;
		}

		public void setDeadline(long deadline) {
			this.deadline = deadline;
		}

		public String getFreight() {
			return freight;
		}

		public void setFreight(String freight) {
			this.freight = freight;
		}

		public String getGoodsBrand() {
			return goodsBrand;
		}

		public void setGoodsBrand(String goodsBrand) {
			this.goodsBrand = goodsBrand;
		}

		public String getGoodsDetails() {
			return goodsDetails;
		}

		public void setGoodsDetails(String goodsDetails) {
			this.goodsDetails = goodsDetails;
		}

		public String getGoodsImgUrl1() {
			return goodsImgUrl1;
		}

		public void setGoodsImgUrl1(String goodsImgUrl1) {
			this.goodsImgUrl1 = goodsImgUrl1;
		}

		public String getGoodsImgUrl2() {
			return goodsImgUrl2;
		}

		public void setGoodsImgUrl2(String goodsImgUrl2) {
			this.goodsImgUrl2 = goodsImgUrl2;
		}

		public String getGoodsImgUrl3() {
			return goodsImgUrl3;
		}

		public void setGoodsImgUrl3(String goodsImgUrl3) {
			this.goodsImgUrl3 = goodsImgUrl3;
		}

		public String getGoodsImgUrl4() {
			return goodsImgUrl4;
		}

		public void setGoodsImgUrl4(String goodsImgUrl4) {
			this.goodsImgUrl4 = goodsImgUrl4;
		}

		public String getGoodsImgUrl5() {
			return goodsImgUrl5;
		}

		public void setGoodsImgUrl5(String goodsImgUrl5) {
			this.goodsImgUrl5 = goodsImgUrl5;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public String getGoodsType1() {
			return goodsType1;
		}

		public void setGoodsType1(String goodsType1) {
			this.goodsType1 = goodsType1;
		}

		public String getGoodsType2() {
			return goodsType2;
		}

		public void setGoodsType2(String goodsType2) {
			this.goodsType2 = goodsType2;
		}

		public String getGoodsType3() {
			return goodsType3;
		}

		public void setGoodsType3(String goodsType3) {
			this.goodsType3 = goodsType3;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getProcurePrice() {
			return procurePrice;
		}

		public void setProcurePrice(String procurePrice) {
			this.procurePrice = procurePrice;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getShopId() {
			return shopId;
		}

		public void setShopId(String shopId) {
			this.shopId = shopId;
		}

		public String getSpecification() {
			return specification;
		}

		public void setSpecification(String specification) {
			this.specification = specification;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getStockNumber() {
			return stockNumber;
		}

		public void setStockNumber(String stockNumber) {
			this.stockNumber = stockNumber;
		}

		public String getUpdatePersion() {
			return updatePersion;
		}

		public void setUpdatePersion(String updatePersion) {
			this.updatePersion = updatePersion;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

	}
}
