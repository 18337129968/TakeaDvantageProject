package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

public class NewRoomDataResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<PackageVOList> packageVOList;
		public List<roomTypeList> roomTypeList;
	}

	public class PackageVOList implements Serializable {
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

	public class roomTypeList implements Serializable {
		public String id;
		public String type;
		public String remark;
		public String typeValue;
	}
}
