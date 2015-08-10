package com.jishijiyu.takeadvantage.entity.result;

public class AdvertisingResult {
	private String c;
	private AdvertisingData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public AdvertisingData getP() {
		return p;
	}

	public void setP(AdvertisingData p) {
		this.p = p;
	}

	public static class AdvertisingData {
		private Poster poster;
		private boolean isTrue;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public Poster getPoster() {
			return poster;
		}

		public void setPoster(Poster poster) {
			this.poster = poster;
		}

	}

	public static class Poster {
		private String answer;
		private String answerNum;
		private String id;
		private String lookNum;
		private String posterDescribe;
		private String posterImgUrl;
		private String posterTitle;
		private String questionDescribe;
		private String questionOption;
		private String questionType;

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public String getAnswerNum() {
			return answerNum;
		}

		public void setAnswerNum(String answerNum) {
			this.answerNum = answerNum;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLookNum() {
			return lookNum;
		}

		public void setLookNum(String lookNum) {
			this.lookNum = lookNum;
		}

		public String getPosterDescribe() {
			return posterDescribe;
		}

		public void setPosterDescribe(String posterDescribe) {
			this.posterDescribe = posterDescribe;
		}

		public String getPosterImgUrl() {
			return posterImgUrl;
		}

		public void setPosterImgUrl(String posterImgUrl) {
			this.posterImgUrl = posterImgUrl;
		}

		public String getPosterTitle() {
			return posterTitle;
		}

		public void setPosterTitle(String posterTitle) {
			this.posterTitle = posterTitle;
		}

		public String getQuestionDescribe() {
			return questionDescribe;
		}

		public void setQuestionDescribe(String questionDescribe) {
			this.questionDescribe = questionDescribe;
		}

		public String getQuestionOption() {
			return questionOption;
		}

		public void setQuestionOption(String questionOption) {
			this.questionOption = questionOption;
		}

		public String getQuestionType() {
			return questionType;
		}

		public void setQuestionType(String questionType) {
			this.questionType = questionType;
		}

	}
}
