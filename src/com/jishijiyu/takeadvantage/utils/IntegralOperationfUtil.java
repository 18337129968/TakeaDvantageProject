package com.jishijiyu.takeadvantage.utils;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;

import android.content.Context;
import android.content.Intent;

/**
 * 拍币修改本地工具类
 * 
 * @author tml
 */
public class IntegralOperationfUtil {

	/**
	 * 加拍币
	 * 
	 * @param addScore
	 *            的拍币数值
	 */
	public static void addIntegral(Context context, int addScore) {
		Gson gson = new Gson();
		String userInfo = (String) SPUtils.get(context,
				Constant.USER_INFO_FILE_NAME, "");
		LoginUserResult result = gson.fromJson(userInfo, LoginUserResult.class);
		result.p.user.todayScore += addScore;
		result.p.user.totalScore += addScore;
		result.p.user.score += addScore;
		String changed = gson.toJson(result);
		SPUtils.put(context, Constant.USER_INFO_FILE_NAME, changed);
		Intent intent = new Intent("userInfoChanged");
		context.sendBroadcast(intent);
	}

	/**
	 * 减拍币
	 * 
	 * @param minusScore
	 *            要减的拍币数值
	 */
	public static boolean minusIntegral(Context context, int minusScore) {
		Gson gson = new Gson();
		String userInfo = (String) SPUtils.get(context,
				Constant.USER_INFO_FILE_NAME, "");
		LoginUserResult result = gson.fromJson(userInfo, LoginUserResult.class);
		result.p.user.score -= minusScore;
		if (result.p.user.score<0) {
			return false;
		}else {
			String changed = gson.toJson(result);
			SPUtils.put(context, Constant.USER_INFO_FILE_NAME, changed);
			Intent intent = new Intent("userInfoChanged");
			context.sendBroadcast(intent);
			return true;
		}
	}
	
	/**
	 * 答题加拍币
	 * @param context
	 * @param addScore
	 * @return true 加拍币成功  false 失败
	 */
	public static boolean addAnswerIntegral(Context context, int addScore) {
		Gson gson = new Gson();
		String userInfo = (String) SPUtils.get(context,
				Constant.USER_INFO_FILE_NAME, "");
		LoginUserResult result = gson.fromJson(userInfo, LoginUserResult.class);
		LogUtil.i(result.p.user.answerTodyScore+"---"+result.p.answerMaxScore);
		if (result.p.user.answerTodyScore<result.p.answerMaxScore) {
			result.p.user.answerTodyScore+=addScore;
			result.p.user.todayScore += addScore;
			result.p.user.totalScore += addScore;
			result.p.user.score += addScore;
			String changed = gson.toJson(result);
			SPUtils.put(context, Constant.USER_INFO_FILE_NAME, changed);
			Intent intent = new Intent("userInfoChanged");
			context.sendBroadcast(intent);
			return true;
		}else {
			return false;
		}

	}
}
