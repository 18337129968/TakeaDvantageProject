package com.jishijiyu.takeadvantage.view;

import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;

public class SortModel {

	private Object object; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
