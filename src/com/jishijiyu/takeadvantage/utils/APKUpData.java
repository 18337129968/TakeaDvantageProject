package com.jishijiyu.takeadvantage.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.jishijiyu.takeadvantage.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class APKUpData {
	public Context mContext;
	private static PackageInfo info;
	private String apkUrl;
	private String des;
	private String apkCode;
	private MyTread myTread;
	private long timeEnd;
	private long timeStart;
	private boolean isShow;
	private boolean can;
	public static Handler mHandler;
	public static final int VERSION_UNLIKENESS_CODE = 0;
	public static final int VERSION_ISNEW_CODE = 1;

	public APKUpData(Context mContext) {
		this.mContext = mContext;
		timeStart = System.currentTimeMillis();
		can = true;
		if (can) {
			new LooperThread().start();
		}
	}

	public void checkUpData(boolean isShowToast) {
		isShow = isShowToast;
		if (can) {
			new MyTread().start();
		}
	}

	class LooperThread extends Thread {

		public void run() {
			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					LogUtil.i(can+"---msg.what:" + msg.what);
					switch (msg.what) {
					case VERSION_UNLIKENESS_CODE:
						showUpdateDialog();
						break;
					case VERSION_ISNEW_CODE:
						if (isShow) {
							ToastUtils.makeText(mContext, "当前是最新的版本", 0).show();
						}
						break;

					default:
						break;
					}
					can=true;
				}
			};

			Looper.loop();
		}
	}

	private String getVersionName() {
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(
					mContext.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("发现新版本:" + apkCode);
		builder.setMessage(des);
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	protected void download() {
		HttpUtils httpUtils = new HttpUtils();
		String str = "\\";
		if (apkUrl.contains(str)) {
			apkUrl = apkUrl.replace(str, "/");
		}
		apkUrl = apkUrl.contains("http://") ? apkUrl : "http://" + apkUrl;
		LogUtil.i("apkUrl" + apkUrl);
		httpUtils.download(apkUrl, "/mnt/sdcard/TakeaDvantageProject.apk",
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);

					}

					@Override
					public void onFailure(
							com.lidroid.xutils.exception.HttpException arg0,
							String arg1) {
						Toast.makeText(mContext, "下载失败", 0).show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						installApk();
					}
				});
	}

	protected void installApk() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File("/mnt/sdcard/TakeaDvantageProject.apk")),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	class MyTread extends Thread {

		@Override
		public void run() {
			try {
				URL url = new URL("http://192.168.8.12:8080/AppServer/appUpdate/update.txt");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();
				Message message = mHandler.obtainMessage();
				if (code == 200) {
					InputStream inputStream = conn.getInputStream();
					String json = StreamUtils.parserInputStream(inputStream);
					JSONObject jsonObject = new JSONObject(json);
					apkCode = jsonObject.getString("code");
					des = jsonObject.getString("des");
					apkUrl = jsonObject.getString("apkUrl");
					LogUtil.i(apkCode + "---" + des + "---" + apkUrl);
					if (!getVersionName().equals(apkCode)) {
						message.what = VERSION_UNLIKENESS_CODE;
					} else {
						message.what = VERSION_ISNEW_CODE;

					}
				} else {
					message.what = VERSION_ISNEW_CODE;
				}
				mHandler.sendMessage(message);
				timeEnd = System.currentTimeMillis();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
