package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 广告列表返回
 * 
 * @author zm
 * 
 */
public class AdvertisingListResult {
	public int c;
	public Parameter p;

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public Parameter getP() {
		return p;
	}

	public void setP(Parameter p) {
		this.p = p;
	}

	public static class Parameter {
		public List<PosterList> posterList;
		public int exchangeRate;
		public boolean isTrue;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public int getExchangeRate() {
			return exchangeRate;
		}

		public void setExchangeRate(int exchangeRate) {
			this.exchangeRate = exchangeRate;
		}

		public List<PosterList> getPosterList() {
			return posterList;
		}

		public void setPosterList(List<PosterList> posterList) {
			this.posterList = posterList;
		}

	}

	public static class PosterList {
		public int id;
		public String posterImgUrl;
		public int posterPrice;
		public String posterTitle;
		public int percent;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getPosterImgUrl() {
			return posterImgUrl;
		}

		public void setPosterImgUrl(String posterImgUrl) {
			this.posterImgUrl = posterImgUrl;
		}

		public int getPosterPrice() {
			return posterPrice;
		}

		public void setPosterPrice(int posterPrice) {
			this.posterPrice = posterPrice;
		}

		public String getPosterTitle() {
			return posterTitle;
		}

		public void setPosterTitle(String posterTitle) {
			this.posterTitle = posterTitle;
		}

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

	}
}
