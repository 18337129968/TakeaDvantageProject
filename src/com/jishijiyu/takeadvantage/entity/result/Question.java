package com.jishijiyu.takeadvantage.entity.result;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
	public String title;
	public String des;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(des);
	}

	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
		public Question createFromParcel(Parcel in) {
			return new Question(in);
		}

		public Question[] newArray(int size) {
			return new Question[size];
		}
	};

	private Question(Parcel in) {
		title = in.readString();
		des = in.readString();
	}
}
