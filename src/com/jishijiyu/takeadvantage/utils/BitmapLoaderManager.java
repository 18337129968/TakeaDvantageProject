package com.jishijiyu.takeadvantage.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

import com.jishijiyu.takeadvantage.activity.BitmapDownloadListener;

/**
 * 线程池 异步加载图片示例（仅供参考，实际使用时，建议开发者自行处理图片加载部分）
 */
public class BitmapLoaderManager {
	public static ExecutorService executorService;

	private final static ExecutorService Execotor() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		}
		return executorService;
	}

	public static void loadBitmap(final Context context, final BitmapDownloadListener listener, final String[] bmUrl) {
		if (bmUrl != null) {
			for (int i = 0; i < bmUrl.length; i++) {
				Execotor().execute(new BitmapLoader_Runnable(context, listener, bmUrl[i]));
			}
		}
	}
	// public static void loadBitmap(final Context context, final BitmapDownloadListener listener, final String[] bmUrl)
	// {
	// if (bmUrl != null) {
	// Util.Execotor().execute(new BitmapLoader_Runnable1(context, listener, bmUrl));
	// }
	// }
}
