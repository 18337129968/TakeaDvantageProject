package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

public class RoomBillResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public ArrayList<InventoryVOList> InventoryVOList;
	}

	public class InventoryVOList {
		public int id;
		public int dollerRoomId;
		public int mealPerios;
		public int mealType;
		public String prizeImg;
		public String roomIssue;
		public boolean isSelected;
	}
}
