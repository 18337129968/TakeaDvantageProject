package com.jishijiyu.takeadvantage.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.jishijiyu.takeadvantage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片加载工具类
 * 
 * @author tml
 */
public class LoadImage_U {
	private static DisplayImageOptions options;

	public static void loadImage1(String uri,ImageView imageview,
			final boolean isSave, final Context mContext, final String fileName) {

		initializeImageLoader();
		String str = "\\";
		if (uri.contains(str)) {
			uri = uri.replace(str, "/");
		}
		ImageLoader.getInstance().displayImage(
				uri.substring(0, 7).equals("http://") ? uri : "http://" + uri,
				imageview, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						System.out.println("imageUri:" + imageUri);
						ToastUtils.makeText(mContext, "头像加载中……", 0).show();
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						ToastUtils.makeText(mContext, "头像加载失败", 0).show();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ToastUtils.makeText(mContext, "头像加载成功", 0).show();
						if (isSave) {
							File file = mContext.getFilesDir();
							FileOutputStream fos;
							try {
								fos = new FileOutputStream(file + fileName);
								boolean success = loadedImage.compress(
										Bitmap.CompressFormat.JPEG, 100, fos);
								LogUtil.i(fileName + " saveSuccess:" + success);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
					}
				});
	}

	public static void initializeImageLoader() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pr_figure)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.pr_figure).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
}
