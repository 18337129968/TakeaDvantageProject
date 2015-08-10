package com.jishijiyu.takeadvantage.entity.request;


/**
 * 答题得拍币请求
 * 
 * @author baohan
 * 
 */
public class RequestAnswerToIntegral {
	public String c ;
	public ParameteSr p;

 

	public String getC() {
		return c;
	}



	public void setC(String c) {
		this.c = c;
	}



	public ParameteSr getP() {
		return p;
	}



	public void setP(ParameteSr p) {
		this.p = p;
	}



	public static class ParameteSr {
		public String answer;
		public String posterId;
		public String userId;
		 public String tokenId;
		public String getTokenId() {
			return tokenId;
		}
		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		public String getPosterId() {
			return posterId;
		}
		public void setPosterId(String posterId) {
			this.posterId = posterId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	}
}
