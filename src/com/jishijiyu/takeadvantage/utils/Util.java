package com.jishijiyu.takeadvantage.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 检查app是否已经安装在本地
 */
public class Util {

	/**
	 * 32位小md5加密
	 * 
	 * @return md5(value) or ""
	 */
	public final static String md5(String val) {
		try {
			String result = null;
			if (val != null && val.length() > 0) {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(val.getBytes(), 0, val.length());
				result = String.format("%032x", new BigInteger(1, md5.digest()));
			}
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据包名检查手机中app是否已经存在
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean checkLocalAppExistOrNot(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
		}
		if (pi != null) {
			return true;
		}
		return false;
	}

	/**
	 * 根据包名启动指定的应用
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean startActivityByPackageName(Context context, String packageName) {
		try {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				Intent intent = pm.getLaunchIntentForPackage(packageName);
				if (intent != null) {
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					return true;
				}
			}
		} catch (Throwable e) {
		}
		return false;
	}

	// /**
	// * 这里尝试打开googleplay进行下载
	// *
	// * @param googlePlayUrl
	// * @return
	// */
	// public static boolean tryToOpenGooglePlayToDownload(Context context, String googlePlayUrl) {
	// try {
	// // 如果googleplay的跳转url不为空
	// if (googlePlayUrl != null && googlePlayUrl.trim().length() > 0) {
	//
	// // 先检查googleplay的服务在这台手机上面是否可用
	// int checkResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	//
	// switch (checkResult) {
	// case ConnectionResult.SUCCESS:
	// // 如果googleplay服务可用就进行跳转url到play上面进行下载
	// if (Util.openUrlWithChromeElseShowBrowserSelector(context, googlePlayUrl, 0, "Open Type")) {
	// return true;
	// }
	//
	// default:
	// GooglePlayServicesUtil.getErrorDialog(checkResult, (Activity) context, 0).show();
	// return false;
	// }
	// }
	// } catch (Throwable e) {
	// }
	// return false;
	// }
	//
	// /**
	// * 通过chrome打开url失败就弹出打开方式让用户选择用什么浏览器打开这个url
	// *
	// * @param context
	// * @param url
	// * @param flags
	// * @param title
	// * @return
	// */
	// public static boolean openUrlWithChromeElseShowBrowserSelector(Context context, String url, int flags, String
	// title) {
	// if (url == null || url.trim().length() <= 0) {
	// return false;
	// }
	// try {
	// // 先尝试使用chrome打开url
	// try {
	// Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.android.chrome");
	// if (intent != null) {
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.setData(Uri.parse(url));
	// context.startActivity(intent);
	// return true;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// // 失败之后弹出选择器让用户选择用哪个浏览器打开这个url
	// return startActivityByUriWithChooser(context, url, flags, title);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }
	//
	// /**
	// * 打开选择器，通过uri打开activity
	// *
	// * @param context
	// * @param uri
	// * @param flags
	// * @param title
	// * @return
	// */
	// public static boolean startActivityByUriWithChooser(Context context, String uri, int flags, String title) {
	// try {
	// Intent intent = Intent.parseUri(uri, flags);
	// if (intent == null) {
	// return false;
	// }
	//
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// Intent startIntent = Intent.createChooser(intent, title);
	// startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(startIntent);
	// return true;
	// } catch (Throwable e) {
	// }
	// return false;
	// }

}
