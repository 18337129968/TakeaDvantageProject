package com.jishijiyu.takeadvantage.entity.request;


/**
 * 任务列表
 * 
 * @author zm
 * 
 */
public class RequestTaskList {
	public String c ;
	public TaskSonData p;

 

	public String getC() {
		return c;
	}



	public void setC(String c) {
		this.c = c;
	}



	public TaskSonData getP() {
		return p;
	}



	public void setP(TaskSonData p) {
		this.p = p;
	}



	public static class TaskSonData {
		public String userId;
		 public String tokenId;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getTokenId() {
			return tokenId;
		}
		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}
		 
	}
}
