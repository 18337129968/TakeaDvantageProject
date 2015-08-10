package com.jishijiyu.takeadvantage.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
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
public class ImageLoaderUtil {
	private static DisplayImageOptions options;

	/**
	 * 不保存在本地的图片加载方法
	 * 
	 * @param uri
	 * @param imageview
	 */
	public static void loadImage(String uri, ImageView imageview) {
		loadImage(uri, imageview, false, null, null);
	}

	public static void loadImage1(String uri, ImageView imageview,
			final boolean isSave, final Context mContext, final String fileName) {
		if (TextUtils.isEmpty(uri) || uri.length() < 7) {
			if (imageview != null) {
				imageview.setBackgroundResource(R.drawable.ic_launcher);
			}
			return;
		}
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
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						if (isSave) {
							File file = mContext.getFilesDir();
							FileOutputStream fos;
							try {
								fos = new FileOutputStream(file + fileName);
								boolean success = loadedImage.compress(
										Bitmap.CompressFormat.PNG, 100, fos);
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

	/**
	 * 保存到本地的图片加载方法
	 * 
	 * @param uri
	 * @param imageview
	 * @param isSave
	 *            保存为true
	 * @param mContext
	 *            上下文Context
	 * @param fileName
	 *            保存的图片名字
	 * 
	 *            ImageView 加载本地图片的方法 BitmapFactory factory = new
	 *            BitmapFactory(); Bitmap bitmap =
	 *            factory.decodeFile(getFilesDir() + 图片名);
	 *            image.setImageBitmap(bitmap);
	 * 
	 */
	public static void loadImage(String uri, ImageView imageview,
			final boolean isSave, final Context mContext, final String fileName) {
		if (TextUtils.isEmpty(uri) || uri.length() < 7) {
			if (imageview != null) {
				imageview.setBackgroundResource(R.drawable.ic_launcher);
			}
			return;
		}
		if (isSave) {
			File file = new File(mContext.getFilesDir() + fileName);
			if (file.exists()) {
				Bitmap bitmap = new BitmapFactory().decodeFile(mContext
						.getFilesDir() + fileName);
				imageview.setImageBitmap(bitmap);
				return;
			}
		}

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
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						if (isSave) {
							File file = mContext.getFilesDir();
							FileOutputStream fos;
							try {
								fos = new FileOutputStream(file + fileName);
								boolean success = loadedImage.compress(
										Bitmap.CompressFormat.JPEG, 100, fos);
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
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
}
