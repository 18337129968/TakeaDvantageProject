package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

public class OrderinQuiryResult {
	private String c;
	private Orderindata p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public Orderindata getP() {
		return p;
	}

	public void setP(Orderindata p) {
		this.p = p;
	}

	public static class Orderindata {
		private List<OrderListdata> list;
		private String number;
		private boolean isTrue;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public List<OrderListdata> getList() {
			return list;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public void setList(List<OrderListdata> list) {
			this.list = list;
		}

	}

	public static class OrderListdata {
		private String area;
		private String city;
		private String createTime;
		private String detailedAddress;
		private String goodsId;
		private String goodsName;
		private String mailCompany;
		private String goodsImgUrl;
		private String prizeId;
		private String id;
		private String name;
		private String num;
		private String orderNum;
		private String orderTime;
		private String orderType;
		private String province;
		private String score;
		private String sendTime;
		private String state;
		private String telephone;
		private String userId;

		public String getMailCompany() {
			return mailCompany;
		}

		public void setMailCompany(String mailCompany) {
			this.mailCompany = mailCompany;
		}

		public String getGoodsImgUrl() {
			return goodsImgUrl;
		}

		public void setGoodsImgUrl(String goodsImgUrl) {
			this.goodsImgUrl = goodsImgUrl;
		}

		public String getPrizeId() {
			return prizeId;
		}

		public void setPrizeId(String prizeId) {
			this.prizeId = prizeId;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getDetailedAddress() {
			return detailedAddress;
		}

		public void setDetailedAddress(String detailedAddress) {
			this.detailedAddress = detailedAddress;
		}

		public String getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getOrderNum() {
			return orderNum;
		}

		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}

		public String getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}

		public String getOrderType() {
			return orderType;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getSendTime() {
			return sendTime;
		}

		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}
}
