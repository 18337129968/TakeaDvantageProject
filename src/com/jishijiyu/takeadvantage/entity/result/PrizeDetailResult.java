package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

public class PrizeDetailResult {
	private String c;
	private PrizeDetailData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public PrizeDetailData getP() {
		return p;
	}

	public void setP(PrizeDetailData p) {
		this.p = p;
	}

	public class PrizeDetailData {
		public String isTrue;
		public List<PrizeData> list;

		public String getIsTrue() {
			return isTrue;
		}

		public void setIsTrue(String isTrue) {
			this.isTrue = isTrue;
		}

		public List<PrizeData> getList() {
			return list;
		}

		public void setList(List<PrizeData> list) {
			this.list = list;
		}

	}

	public class PrizeData implements Serializable {
		public String companyId;
		public String companyName;
		public String createTime;
		public String freight;
		public String grade;

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getFreight() {
			return freight;
		}

		public void setFreight(String freight) {
			this.freight = freight;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPlaces() {
			return places;
		}

		public void setPlaces(String places) {
			this.places = places;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getPrizeExplain() {
			return prizeExplain;
		}

		public void setPrizeExplain(String prizeExplain) {
			this.prizeExplain = prizeExplain;
		}

		public String id;
		public String imgUrl;
		public String name;
		public String places;
		public String price;
		public String prizeExplain;
	}
}
