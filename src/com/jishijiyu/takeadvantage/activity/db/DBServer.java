package com.jishijiyu.takeadvantage.activity.db;

import android.content.Context;

/**
 * 
 * 
 * @author sunaoyang
 * 
 */
public class DBServer {

	/**
	 * 
	 * 
	 * @param fileItem
	 */
	public static void addFriend(Context context, Note note) {
		DBDao dbDao = new DBDao(context);
		dbDao.addFriend(note);
	}

	/**
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static Note searchFriend(Context context, String userid) {
		DBDao dbDao = new DBDao(context);
		return dbDao.searchFriend(userid);
	}

	/**
	 * 是否存在userid
	 * 
	 * @return
	 */
	public static boolean searchFriendIfExist(Context context, String userid) {
		DBDao dbDao = new DBDao(context);
		return dbDao.searchFriendIfExist(userid);
	}
	// // /**
	// // * ���֪ͨ�б�
	// // *
	// // * @param date
	// // * ����
	// // * @return
	// // */
	// // public static ArrayList<Note> searchNoteByIMessage(Context context,
	// // String imessage) {
	// // DBDao dbDao = new DBDao(context);
	// // return dbDao.searchNoteByIMessage(imessage);
	// // }
	//
	// // /**
	// // * ���id��ü���
	// // *
	// // * @param context
	// // * @param note
	// // */
	// // public static Note searchNoteById(Context context, int id) {
	// // DBDao dbDao = new DBDao(context);
	// // return dbDao.searchNoteById(id);
	// // }
	//
	// /**
	// * �޸�ĳ������
	// *
	// * @param context
	// * @param note
	// */
	// public static void updateNote(Context context, Note note) {
	// DBDao dbDao = new DBDao(context);
	// dbDao.updateNote(note);
	// }
	//
	// /**
	// * ���idɾ��ĳ������
	// *
	// * @param id
	// */
	// public static void deleteNoteByDate(Context context, int id) {
	// DBDao dbDao = new DBDao(context);
	// dbDao.deleteNoteByDate(id);
	//
	// }
	//
	// /**
	// * ���tzgg
	// *
	// * @param fileItem
	// */
	// public static void addTzgg(Context context, Message tzgg) {
	// DBDao dbDao = new DBDao(context);
	// dbDao.addTzgg(tzgg);
	// }
	//
	// /**
	// * �Ƿ����tzgg
	// *
	// * @return
	// */
	// public static boolean searchTzggIfExist(Context context, String code,
	// String user) {
	// DBDao dbDao = new DBDao(context);
	// return dbDao.searchTzggIfExist(code, user);
	// }
	//
	// /**
	// * ���tzgg
	// *
	// * @return
	// */
	// public static ArrayList<Map<String, Object>> searchTzgg(Context context,
	// String user) {
	// DBDao dbDao = new DBDao(context);
	// return dbDao.searchTzgg(user);
	// }
	//
	// /**
	// * �޸�tzgg
	// *
	// * @param context
	// * @param note
	// */
	// public static void updateTzgg(Context context, String id, String user,
	// String read) {
	// DBDao dbDao = new DBDao(context);
	// dbDao.updateTzgg(id, user, read);
	// }
	//
	// // /**
	// // * tzgg content�Ƿ���ֵ
	// // *
	// // * @return
	// // */
	// // public static String searchTzggById(Context context, String code,
	// // String user) {
	// // DBDao dbDao = new DBDao(context);
	// // return dbDao.searchTzggById(code, user);
	// // }
	//
	// /**
	// * �Ƿ����tzgg
	// *
	// * @return
	// */
	// public static boolean TzggIfExist(Context context, String user) {
	// DBDao dbDao = new DBDao(context);
	// return dbDao.TzggIfExist(user);
	// }
	//
	// /**
	// * ���δ��
	// *
	// * @return
	// */
	// public static ArrayList<Map<String, String>> searchTableUnread(
	// Context context, String user) {
	// DBDao dbDao = new DBDao(context);
	// return dbDao.searchTableUnread(user);
	// }

}
