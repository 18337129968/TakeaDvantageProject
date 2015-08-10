package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;

public class UserNotice implements Serializable {
	public int contentId;
	public int state;
	public String title;
	public String content;
	public int userId;
	public long sendTime;
}