package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

public class AboutPaiDeLi {
	public ArrayList<HeadLine> list;

	public class HeadLine {
		public String headline;
		public ArrayList<Question> questList;
	}
}
