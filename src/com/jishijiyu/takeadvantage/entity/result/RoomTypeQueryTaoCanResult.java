package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

public class RoomTypeQueryTaoCanResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<PackageVOList> packageVOList;
	}

	public class PackageVOList {
		public String gradFour;
		public String gradOne;
		public String gradThree;
		public String gradTwo;
		public String id;
		public String imgUrlFour;
		public String imgUrlOne;
		public String imgUrlThree;
		public String imgUrlTwo;
		public String name;
		public String roomTypeId;
	}

}
