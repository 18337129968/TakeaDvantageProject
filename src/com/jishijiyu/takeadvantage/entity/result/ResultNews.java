package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

public class ResultNews {
	public String c;
	public Pramater p;
	
	public ResultNews(){
		p=new Pramater();
	}
	
	public class Pramater{
		public boolean isTrue;
		public ArrayList<UserNotice> userNoticeList;
	}
}
