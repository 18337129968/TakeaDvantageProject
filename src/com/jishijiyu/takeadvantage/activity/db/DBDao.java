package com.jishijiyu.takeadvantage.activity.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBDao {

	private DBOpenHelper dbOpenHelper;
	SharedPreferences sp;
	String user;

	public DBDao(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
		// sp = context.getSharedPreferences("userInfo",
		// Context.MODE_WORLD_READABLE);
		// user = sp.getString("username", "");
	}

	/**
	 * 增加好友信息
	 * 
	 * @param fileItem
	 */
	public void addFriend(Note note) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("userid", note.userid);
		values.put("nikname", note.nikname);
		values.put("avatar", note.avatar);
		db.insert("friend", null, values);
		db.close();
	}

	/**
	 * 根据id查询好友
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public Note searchFriend(String userid) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("friend", new String[] { "userid", "nikname",
				"avatar" }, " userid = ?", new String[] { userid }, null, null,
				null);

		Note note = new Note();
		while (cursor.moveToNext()) {
			Log.e("date", "db new Note() = ");
			note.userid = cursor.getString(cursor.getColumnIndex("userid"));
			note.nikname = cursor.getString(cursor.getColumnIndex("nikname"));
			note.avatar = cursor.getString(cursor.getColumnIndex("avatar"));

		}
		db.close();
		return note;
	}

	/**
	 * 是否存在userid
	 * 
	 * @param
	 * @return
	 */
	public boolean searchFriendIfExist(String userid) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("friend", new String[] { "userid" },
				"userid=?", new String[] { userid }, null, null, null);

		if (cursor.moveToNext()) {
			db.close();
			return false;
		} else {
			db.close();
			return true;

		}
	}
	// // /**
	// // * ���id��ü���
	// // *
	// // * @param date
	// // * ����
	// // * @return
	// // */
	// // public Note searchNoteById(int id) {
	// // SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// //
	// // Cursor cursor = db.query("note", new String[] { "_id", "title",
	// // "content", "date", "time", "complete", "cost", "client",
	// // "address", "noteid" }, " _id =?",
	// // new String[] { String.valueOf(id) }, null, null, "_id asc");
	// // Note note = null;
	// //
	// // while (cursor.moveToNext()) {
	// // note = new Note();
	// // note.id = cursor.getInt(cursor.getColumnIndex("_id"));
	// // note.title = cursor.getString(cursor.getColumnIndex("title"));
	// // note.content = cursor.getString(cursor.getColumnIndex("content"));
	// // note.date = cursor.getString(cursor.getColumnIndex("date"));
	// // note.time = cursor.getString(cursor.getColumnIndex("time"));
	// // note.complete = cursor.getString(cursor.getColumnIndex("complete"));
	// // note.cost = cursor.getString(cursor.getColumnIndex("cost"));
	// // note.client = cursor.getString(cursor.getColumnIndex("client"));
	// // note.address = cursor.getString(cursor.getColumnIndex("address"));
	// // note.noteid = cursor.getString(cursor.getColumnIndex("noteid"));
	// // note.imessage = cursor.getString(cursor.getColumnIndex("imessage"));
	// // }
	// // db.close();
	// // return note;
	// // }
	//
	// /**
	// * ���idɾ��ĳ������
	// *
	// * @param id
	// */
	// public void deleteNoteByDate(int id) {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// db.delete("note", "_id=?", new String[] { String.valueOf(id) });
	// }
	//
	// /**
	// * �޸�ĳ������
	// *
	// * @param context
	// * @param note
	// */
	// public void updateNote(Note note) {
	//
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put("title", note.title);
	// values.put("content", note.content);
	// values.put("date", note.date);
	// values.put("time", note.time);
	// values.put("complete", note.complete);
	// values.put("cost", note.cost);
	// values.put("client", note.client);
	// db.update("note", values, "_id=?",
	// new String[] { String.valueOf(note.id) });
	// }
	//
	// // /**
	// // * ���֪ͨ�б�
	// // *
	// // * @param
	// // * @return
	// // */
	// // public ArrayList<Note> searchNoteByIMessage(String imessage) {
	// // SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// //
	// // Cursor cursor = db.query("note", new String[] { "_id", "date",
	// "title",
	// // "content", "time", "complete", "imessage" }, " imessage != ''",
	// // null, null, null, null);
	// // ArrayList<Note> list = new ArrayList<Note>();
	// //
	// // Log.e("date", "db noteDate = " + imessage);
	// //
	// // while (cursor.moveToNext()) {
	// // Log.e("date", "db new Note() = ");
	// // Note note = new Note();
	// // note.id = cursor.getInt(cursor.getColumnIndex("_id"));
	// // ;
	// // note.title = cursor.getString(cursor.getColumnIndex("title"));
	// // note.content = cursor.getString(cursor.getColumnIndex("content"));
	// // note.date = cursor.getString(cursor.getColumnIndex("date"));
	// // note.time = cursor.getString(cursor.getColumnIndex("time"));
	// // note.complete = cursor.getString(cursor.getColumnIndex("complete"));
	// // note.imessage = cursor.getString(cursor.getColumnIndex("imessage"));
	// // list.add(note);
	// // }
	// // db.close();
	// // return list;
	// // }
	//
	// /**
	// * ���֪ͨ����
	// *
	// * @param fileItem
	// */
	// public void addTzgg(Message tzgg) {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put("id", tzgg.getTzid());
	// values.put("username", user);
	// values.put("title", tzgg.getTztitle());
	// values.put("time", tzgg.getTztime());
	// values.put("content", tzgg.getTzcontent());
	// values.put("read", "0");
	// values.put("type", "tzgg");
	// db.insert("tzgg", "", values);
	// db.close();
	// }
	//
	// /**
	// * �Ƿ����tzgg
	// *
	// * @param
	// * @return
	// */
	// public boolean searchTzggIfExist(String id, String user) {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// Cursor cursor = db.query("tzgg", new String[] { "id", "username" },
	// "id=? and username=?", new String[] { id, user }, null, null,
	// "id asc");
	//
	// if (cursor.moveToNext()) {
	// db.close();
	// return false;
	// } else {
	// db.close();
	// return true;
	//
	// }
	// }
	//
	// /**
	// * ���tzgg
	// *
	// * @return
	// */
	// public ArrayList<Map<String, Object>> searchTzgg(String user) {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// ArrayList<Map<String, Object>> mData = new ArrayList<Map<String,
	// Object>>();
	// Cursor cursor = db.query("tzgg", new String[] { "*" }, "username=?",
	// new String[] { user }, null, null, "time desc");
	// while (cursor.moveToNext()) {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("tzgg_id", cursor.getString(cursor.getColumnIndex("id")));
	// map.put("tzgg_title",
	// cursor.getString(cursor.getColumnIndex("title")));
	// map.put("tzgg_time",
	// cursor.getString(cursor.getColumnIndex("time")));
	// map.put("tzgg_content",
	// cursor.getString(cursor.getColumnIndex("content")));
	// map.put("tzgg_read",
	// cursor.getString(cursor.getColumnIndex("read")));
	// mData.add(map);
	// }
	// db.close();
	// return mData;
	// }
	//
	// /**
	// * �޸�tzgg
	// *
	// * @param context
	// * @param note
	// */
	// public void updateTzgg(String id, String user, String read) {
	//
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put("read", read);
	// db.update("tzgg", values, "id=? and username=?", new String[] { id,
	// user });
	// db.close();
	// }
	//
	// // /**
	// // * ȡ��tzgg content
	// // *
	// // * @param
	// // * @return
	// // */
	// // public String searchTzggById(String id, String user) {
	// // SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// // String url = null;
	// // Cursor cursor = db.query("tzgg", new String[] { "id", "username",
	// // "content" }, "id=? and username=?", new String[] { id, user },
	// // null, null, null);
	// // while (cursor.moveToNext()) {
	// // url = cursor.getString(cursor.getColumnIndex("content"));
	// // }
	// // db.close();
	// // return url;
	// // }
	//
	// /**
	// * �Ƿ������
	// *
	// * @param
	// * @return
	// */
	// public boolean TzggIfExist(String user) {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// Cursor cursor = db
	// .query("tzgg", new String[] { "username" }, "username=?",
	// new String[] { user }, null, null, "username asc");
	//
	// if (cursor.moveToNext()) {
	// db.close();
	// return false;
	// } else {
	// db.close();
	// return true;
	//
	// }
	// }
	//
	// /**
	// * δ��
	// */
	// public ArrayList<Map<String, String>> searchTableUnread(String username)
	// {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// ArrayList<Map<String, String>> data = new ArrayList<Map<String,
	// String>>();
	// Cursor cursor = db.query("tzgg", new String[] { "id", "username",
	// "title", "time", "content" }, "username=? and read='0'",
	// new String[] { username }, null, null, null);
	// while (cursor.moveToNext()) {
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("id", cursor.getString(cursor.getColumnIndex("id")));
	// map.put("title", cursor.getString(cursor.getColumnIndex("title")));
	// map.put("time", cursor.getString(cursor.getColumnIndex("time")));
	// map.put("content",
	// cursor.getString(cursor.getColumnIndex("content")));
	// data.add(map);
	// }
	// db.close();
	// return data;
	// }
}
