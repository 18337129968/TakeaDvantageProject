package com.jishijiyu.takeadvantage.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author tml 
 * 	访问网络调用这个方法：updata（），能够等到服务器返回的json字符串
 * 
 *         注意： 关于访问网络的工具类 跳转界面卡顿的bug解决方案： 通过handler.sendMessageDelayed(message,
 *         1000);延时1秒执行解决 在延時1秒的这段时间应该显示一个加载动画
 */
public class HttpConnectUtil {
	/**
	 * @param json
	 *            上传数据
	 * @param isSave
	 *            是否保存本地
	 * @param mContext
	 *            本地保存需要传递上下文Context
	 * @param saveName
	 *            本地保存名字
	 * @return 返回数据
	 */
	public static String updata(String json, boolean isSave,Context mContext, String saveName) {
		// boolean isConnect =NetUtils.isConnected(App.mApplication);
		// boolean isWifi=NetUtils.isWifi(App.mApplication);
		// if (!isConnect) {
		// Toast.makeText(App.mApplication,"网络断开，请先联网", 0).show();
		// return null;
		// }
		// if (!isWifi) {
		// Toast.makeText(App.mApplication,"WiFi断开，请先联网", 0).show();
		// return null;
		// }
		Log.i("HttpConnectUtil", "request:"+json);
		HttpThread httpThread = new HttpThread(json, isSave, mContext,saveName);
		httpThread.start();
		try {
			httpThread.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		Log.i("HttpConnectUtil", "result:"+httpThread.getResult());
		return httpThread.getResult();
	}

	static class HttpThread extends Thread {

		private String json;
		private boolean isSave;
		private String saveName;
		private String result;
		private Context mContext;

		public HttpThread(String json, boolean isSave,Context mContext, String saveName) {
			this.json = json;
			this.isSave = isSave;
			this.saveName = saveName;
			this.mContext=mContext;
		}

		public String getResult() {
			return result;
		}

		@Override
		public void run() {
			result = contect(json, isSave,mContext,saveName);
		}
	}

	private static String contect(String json, boolean isSave, Context mContext,String saveName) {
		Log.e("请求参数:", json);
		try {
			URL url = new URL(Constant.CONNECT_URL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(json.getBytes());
			outputStream.flush();
			outputStream.close();
			if (connection.getResponseCode() == 200) {
				InputStream inputStream = connection.getInputStream();
				String result = StreamUtils.parserInputStream(inputStream);
				if (isSave) {
					if (!TextUtils.isEmpty(saveName)) {
						FileUtil.writeFile(mContext, saveName,
								result.toString());
					} else {
						Log.e("HttpConnectUtil", "空的文件名");
					}
				}
				Log.e("返回结果:", result);
				return result;
			}
		} catch (IOException e) {
			Log.e("HttpConnectUtil", "访问网络失败");
			return null;
		}
		return null;
	}

}
