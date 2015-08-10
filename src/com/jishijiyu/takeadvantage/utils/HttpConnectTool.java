package com.jishijiyu.takeadvantage.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import kankan.wheel.widget.MyProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 访问网络工具类
 * 
 * @author temulu
 */
public class HttpConnectTool {
	private static ConnectListener mConnectListener;
	private static String mRequestJson;
	private static Handler mHandler;
	private static String path = Constant.CONNECT_URL;
	public static final int HTTP_CONNECT_START = 0;
	public static final int HTTP_CONNECT_SUCCESS = 1;
	public static final int HTTP_CONNECT_FAILED = 2;
	static HttpHandler<String> handler = null;
	static HttpUtils httpUtils = null;

	/**
	 * @param requestJson
	 *            要传递给服务器的json
	 * @param connectListener
	 *            访问网络的监听类，有3个未实现的方法 contectStarted()开始访问 contectSuccess()访问成功
	 *            contectFailed()访问失败
	 */
	public static void update(final String requestJson, Context context,
			final ConnectListener connectListener) {
		update(requestJson, true, context, connectListener);
	}

	// public static void update(final String requestJson,
	// final boolean showDialog, Context context,
	// final ConnectListener connectListener) {
	// boolean b = NetUtils.isConnected(context);
	// if (!b) {
	// Toast.makeText(context, "未连接网络，请检查", Toast.LENGTH_SHORT).show();
	// }
	// final MyProgressDialog progressDialog = new MyProgressDialog(context);
	// mRequestJson = requestJson;
	// Log.e("请求参数：", requestJson);
	// mConnectListener = connectListener;
	// mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case HTTP_CONNECT_START:
	// if (showDialog) {
	// progressDialog.show();
	// }
	// mConnectListener.contectStarted();
	// break;
	// case HTTP_CONNECT_SUCCESS:
	// if (showDialog) {
	// progressDialog.cancel();
	// }
	// mConnectListener.contectSuccess(msg.getData().getString(
	// "result"));
	// break;
	// case HTTP_CONNECT_FAILED:
	// if (showDialog) {
	// progressDialog.cancel();
	// }
	// mConnectListener.contectFailed(path, mRequestJson);
	// break;
	//
	// default:
	// break;
	// }
	// }
	// };
	// new ConnectThread().start();
	// }

	public static void update(final String requestJson,
			final boolean showDialog, Context context,
			final ConnectListener connectListener) {
		boolean b = NetUtils.isConnected(context);
		if (!b) {
			Toast.makeText(context, "未连接网络，请检查", Toast.LENGTH_SHORT).show();
		}
		final MyProgressDialog progressDialog = new MyProgressDialog(context);
		Log.e("请求参数：", requestJson);
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(requestJson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			ToastUtils.makeText(context, "请求失败！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		// 如果之前的线程没有完成
		if (handler != null && handler.getState() != State.FAILURE
				&& handler.getState() != State.SUCCESS
				&& handler.getState() != State.CANCELLED) {
			// 关闭handler后 onStart()和onLoading()还是会执行
			handler.cancel();
		}
		httpUtils = new HttpUtils();
		// 设置当前请求的缓存时间
		httpUtils.configCurrentHttpCacheExpiry(1000);
		// 设置默认请求的缓存时间
		httpUtils.configDefaultHttpCacheExpiry(0);
		// 设置线程数
		httpUtils.configRequestThreadPoolSize(10);
		RequestParams params = new RequestParams();
		params.setBodyEntity(stringEntity);
		httpUtils.send(HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (showDialog) {
							progressDialog.cancel();
						}
						connectListener.contectFailed(arg0.getMessage(), arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (showDialog) {
							progressDialog.cancel();
						}
						connectListener.contectSuccess(arg0.result);
						Log.i("result", "" + arg0.result);
					}

					@Override
					public void onStart() {
						super.onStart();
						if (showDialog) {
							progressDialog.show();
						}
						connectListener.contectStarted();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						progressDialog.show();
					}
				});
	}

	static class ConnectThread extends Thread {

		public void run() {
			Message message = Message.obtain();
			message.what = HTTP_CONNECT_START;
			mHandler.sendMessage(message);
			try {
				URL url = new URL(path);
				LogUtil.e("url:" + path);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(10000);
				connection.setDoOutput(true);
				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(mRequestJson.getBytes());
				outputStream.flush();
				outputStream.close();
				Message message1 = Message.obtain();
				if (connection.getResponseCode() == 200) {
					InputStream inputStream = connection.getInputStream();
					String result = StreamUtils.parserInputStream(inputStream);
					Log.e("result:", result);
					if (!TextUtils.isEmpty(result)) {
						message1.what = HTTP_CONNECT_SUCCESS;
						Bundle bundle = new Bundle();
						bundle.putString("result", result);
						message1.setData(bundle);
					} else {
						message1.what = HTTP_CONNECT_FAILED;
					}
				} else {
					message1.what = HTTP_CONNECT_FAILED;
				}
				mHandler.sendMessage(message1);
			} catch (IOException e) {
				Message message2 = Message.obtain();
				message2.what = HTTP_CONNECT_FAILED;
				mHandler.sendMessage(message2);
			}
		}
	}

	public interface ConnectListener {
		public void contectStarted();

		public void contectSuccess(String result);

		public void contectFailed(String error, String request);
	}

}
