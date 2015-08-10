package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

public class AwardingGoodsData {
	private String c;
	private AwardingData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public AwardingData getP() {
		return p;
	}

	public void setP(AwardingData p) {
		this.p = p;
	}

	public static class AwardingData {
		private List<AwardingListData> list;
		private String number;
		private boolean isTrue;
		
		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public List<AwardingListData> getList() {
			return list;
		}

		public void setList(List<AwardingListData> list) {
			this.list = list;
		}

	}
	@SuppressWarnings("unused")
	public static class AwardingListData {
		private String area;
		private String city;
		private String createTime;
		private String detailedAddress;
		private String ernieId;
		private String goodsName;
		private String id;
		private String name;
		private String num;
		private String orderNum;
		private String orderTime;
		private String orderType;
		private String prizeId;
		private String province;
		private String receiveTime;
		private String score;
		private String sendTime;
		private int state;
		private String telephone;
		private String userId;
		private String goodsImgUrl;
		
		public String getGoodsImgUrl() {
			return goodsImgUrl;
		}

		public void setGoodsImgUrl(String goodsImgUrl) {
			this.goodsImgUrl = goodsImgUrl;
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

		public String getErnieId() {
			return ernieId;
		}

		public void setErnieId(String ernieId) {
			this.ernieId = ernieId;
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

		public String getPrizeId() {
			return prizeId;
		}

		public void setPrizeId(String prizeId) {
			this.prizeId = prizeId;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getReceiveTime() {
			return receiveTime;
		}

		public void setReceiveTime(String receiveTime) {
			this.receiveTime = receiveTime;
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

		public int getState() {
			return state;
		}

		public void setState(int state) {
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
