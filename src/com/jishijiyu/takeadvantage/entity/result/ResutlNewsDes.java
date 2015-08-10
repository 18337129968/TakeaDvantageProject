package com.jishijiyu.takeadvantage.entity.result;

public class ResutlNewsDes {
	public String c;
	public Pramater p;
	
	public class Pramater{
		public Content content;
	}
	public class Content{
		public String content;
		public long createTime;
		public long endTime;
		public int id;
		public int isUrl;
		public int state;
		public String imgUrl;
		public String title;
	}
}
