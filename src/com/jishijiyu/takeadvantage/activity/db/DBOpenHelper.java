package com.jishijiyu.takeadvantage.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String NAME = "friend.db";

	private static final int VERSION = 3;

	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//
		db.execSQL("CREATE TABLE friend(userid varchar(20),nikname varchar(20),avatar varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS note");

		onCreate(db);
	}

}
