package com.jishijiyu.takeadvantage.utils;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.activity.my.OrderInquiryActivity;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.UserNotice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * 获取UserId
 * 
 * @author tml
 * 
 *         失败返回null 需要自己处理
 */
public class GetUserIdUtil {
	public static String getUserId(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		String userId = result.p.user.id + "";
		if (TextUtils.isEmpty(userId)) {
			return null;
		}
		return userId;
	}

	// 金币数量
	public static int goldNum(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int goldNum = result.p.user.goldNum;
		return goldNum;

	}

	// 邀请人数量
	public static int inviteUserNum(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int inviteUserNum = result.p.user.inviteUserNum;
		return inviteUserNum;

	}

	// 金锁数量
	public static int goldLockNum(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int goldLockNum = result.p.user.goldLockNum;
		return goldLockNum;

	}

	// 银锁数量
	public static int silverLockNum(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int silverLockNum = result.p.user.silverLockNum;
		return silverLockNum;

	}

	// 铜锁数量
	public static int copperLockNum(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int copperLockNum = result.p.user.copperLockNum;
		return copperLockNum;

	}

	// 邀请码
	public static String invitationCode(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		String invitationCode = result.p.user.inviteCode;
		return invitationCode;

	}

	// 商家状态
	public static int merchantState(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		int merchantState = result.p.user.type;
		return merchantState;
	}
		// 获取拍币数量
		public static int getSorce(Context mContext) {
			LoginUserResult result = getLogin(mContext);
			int Sorce = result.p.user.score;
			return Sorce;

	}

	/**
	 * @param mContext
	 * @return
	 */
	public static void getEnterForState(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		if (result.p.enroll == null) {
			SPUtils.put(mContext, Constant.GETENTER_FOR_STATE, false);
		} else {
			SPUtils.put(mContext, Constant.GETENTER_FOR_STATE, true);
		}
	}

	public static String getUserMobile(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		String userMobile = result.p.user.mobile + "";
		if (TextUtils.isEmpty(userMobile)) {
			return null;
		}
		return userMobile;
	}

	public static String getErnieId(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		String ernieId = result.p.ernie.id + "";
		if (TextUtils.isEmpty(ernieId)) {
			return null;
		}
		return ernieId;
	}

	/**
	 * 获取tokenId
	 * 
	 * @param mContext
	 * @return tokenId
	 */
	public static String getTokenId(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		String tokenId = result.p.tokenId;
		if (TextUtils.isEmpty(tokenId)) {
			return null;
		}
		return tokenId;
	}

	/**
	 * 保存消息列表
	 * 
	 * @param mContext
	 * @param list
	 */
	public static void saveList(Context mContext, ArrayList list) {
		Gson gson = new Gson();
		String string = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		LoginUserResult result = gson.fromJson(string, LoginUserResult.class);
		result.p.notReadNoticeList = list;
		string = gson.toJson(result);
		SPUtils.put(mContext, Constant.USER_INFO_FILE_NAME, string);
	}

	/**
	 * 获取消息列表
	 * 
	 * @param mContext
	 * @return 消息列表
	 */
	public static ArrayList<UserNotice> getList(Context mContext) {
		LoginUserResult result = getLogin(mContext);
		return result.p.notReadNoticeList;
	}

	/**
	 * 登录信息
	 * 
	 * @param mContext
	 * @return 消息列表
	 */
	public static LoginUserResult getLogin(Context mContext) {
		Gson gson = new Gson();
		String string = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		LoginUserResult result = gson.fromJson(string, LoginUserResult.class);
		return result;
	}

	/**
	 * 保存登录信息
	 * 
	 * @param mContext
	 * @param LoginUserResult
	 */
	public static void saveLoginUserResult(Context mContext,
			LoginUserResult loginUserResult) {
		Gson gson = new Gson();
		String string = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		string = gson.toJson(loginUserResult);
		SPUtils.put(mContext, Constant.USER_INFO_FILE_NAME, string);
	}
}
