package com.jishijiyu.takeadvantage.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ImageCache {
	// private static final String FILE_ANDROID_ASSET_APPLE_SAFARI_COMPASS_PNG =
	// "file:///android_asset/newyear_bg.png";
	final static int VIDEO_LIMIT = 40;
	public static final int IMAGE_IN_ERROR = 0;// url异常，或者其它情况
	public static final int IMAGE_IN_ASSETS = 1;// 在assets里
	public static final int IMAGE_IN_LOCAL_FILE = 2;// 在本地
	public static final int IMAGE_IN_SERVER = 3;// 在服务器
	public static final int PER_CSREEN_WIDTH_PRIME = 0;// 图片原始大小
	private static int screenDensity;

	/**
	 * 1、如果以上两种都为获取,从assets目录下读取 2、从本地目录按文件名读取文件 3、本地未获取到,根据url从网上获取
	 * 
	 * @param activity
	 * @param imgurl
	 * @param flag
	 *            参数，表示获取到的图片是满屏的，90%屏幕比例缩放，还是原始的像素尺寸显示.
	 * @return Drawable if Exception ,return null.
	 */
	public static Drawable getDrawableWithAssets(Context activity,
			String imgurl, int flag) {
		Bitmap bit = getBitmapWithAssets(activity, imgurl, flag);
		if (bit == null) {
			return null;
		}
		return bitmapToDrawable(bit);
	}

	/**
	 * 1、如果以上两种都为获取,从assets目录下读取 2、从本地目录按文件名读取文件 3、本地未获取到,根据url从网上获取
	 * 
	 * @param activity
	 * @param imgurl
	 * @param flag
	 *            参数，表示获取到的图片是满屏的，90%屏幕比例缩放，还是原始的像素尺寸显示.
	 * @return Bitmap if Exception ,return null.
	 */
	public static Bitmap getBitmapWithAssets(Context activity, String imgurl,
			int flag) {
		Bitmap bitmap = getTrueBitmap(activity, imgurl);
		if (bitmap == null) {
			return null;
		}
		Bitmap bit = scaleBitmap(activity, bitmap, flag);
		return bit;
	}

	public static Bitmap getTrueBitmap(Context activity, String imgurl) {
		if (imgurl == null || "".equals(imgurl)) {
			return null;
		}
		File cdir = activity.getCacheDir();
		String filename = getFilename(imgurl);
		Bitmap bit = null;
		Options opts = new BitmapFactory.Options();
		AssetManager am = activity.getAssets();
		InputStream ais = null;
		try {
			ais = am.open(filename);
			if (ais != null) {
				saveFile(ais, cdir.getPath() + "/" + filename);
				File file = new File(cdir.getPath() + "/" + filename);
				FileInputStream fis = new FileInputStream(file);

				bit = BitmapFactory.decodeStream(fis, null, opts);
				if (bit != null) {
					//D.i("get True Bitmap asset:" + imgurl);
					return bit;
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
			}
		}
		if (bit == null) {
			File[] files = cdir.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				File file = files[i];
				if (file.getName().equalsIgnoreCase(filename)) {
					FileInputStream fis = null;
					try {
//						D.e("read img from cache");
						fis = new FileInputStream(file);
						bit = BitmapFactory.decodeStream(fis, null, opts);
						if (bit != null) {
							//D.i("get True Bitmap file:" + imgurl);
							return bit;
						}
					} catch (FileNotFoundException e) {
//						e.printStackTrace();
					} finally {
						if (fis != null)
							try {
								fis.close();
							} catch (IOException e) {
//								e.printStackTrace();
							}
					}
				}
			}
		}
		if (bit == null) {
			try {
				URL url = new URL(imgurl);
				// NetCheck.getInstance(activity).checkProxy();
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(10 * 1000);
				conn.setReadTimeout(10 * 1000);
				conn.setUseCaches(true);
				conn.connect();
				try {
					InputStream is = conn.getInputStream();
					try {
						if (is != null) {
							saveFile(is, cdir.getPath() + "/" + filename);
							File file = new File(cdir.getPath() + "/"
									+ filename);
							FileInputStream fis = new FileInputStream(file);
							bit = BitmapFactory.decodeStream(fis, null, opts);
							if (bit != null) {
								//D.i("get True Bitmap URLConnection:" + imgurl);
								return bit;
							}
						}
					} catch (FileNotFoundException e) {
						// Log.i(LOG_TAG,
						// "the logo was not upload so lost the file name");
						// e.printStackTrace();
					} finally {
						if (is != null)
							is.close();
					}
				} finally {

				}
			} catch (Exception e) {
				// Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
		return null;
	}

	static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		} else {
			return null;
		}
	}

	public static String getFilename(String imgUrl) {
		if (imgUrl == null || "".equals(imgUrl))
			return null;
		try {
			String filename = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
			if ("".equals(filename)) {
				return null;
			}
			return filename;
		} catch (Exception e) {
			return null;
		}
	}

	private static void saveFile(InputStream is, String name) {
		FileOutputStream fos = null;
		ByteArrayOutputStream baos = null;
		try {
			fos = new FileOutputStream(new File(name));
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				fos.write(bytes, 0, c);
			}
			fos.flush();
		} catch (Exception e) {
			File f = new File(name);
			if (f != null)
				f.delete();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
	}

	@SuppressWarnings("rawtypes")
	public static class FileComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			File f1 = (File) arg0;
			File f2 = (File) arg1;
			return (int) (f1.lastModified() - f2.lastModified());
		}

	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	public static int getDensityDpi(Context context) {
		if (screenDensity == 0) {
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			screenDensity = (int) ((metrics.densityDpi));
		}
		return screenDensity;
	}

	protected static Bitmap scaleBitmap(Context activty, Bitmap bm, float scale) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;

	}

	private static float scale_full = -1;
	private static float scale_compass = -1;

	public static float getScale_full() {
		return scale_full;
	}

	public static float getScale_compass() {
		return scale_compass;
	}

	/**
	 * 返回图片目前的存储状态，此方法不会访问服务器。
	 * 
	 * @param context
	 * @param imgurl
	 * @return
	 */
	public static int getImageSavedStatus(Context context, String imgurl) {
		if (imgurl == null || "".equals(imgurl)) {
			return IMAGE_IN_ERROR;
		}
		File cdir = context.getCacheDir();
		String filename = getFilename(imgurl);
		Bitmap bit = null;
		Options opts = new BitmapFactory.Options();
		AssetManager am = context.getAssets();
		InputStream ais = null;
		try {
			ais = am.open(filename);
			if (ais != null) {
				saveFile(ais, cdir.getPath() + "/" + filename);
				File file = new File(cdir.getPath() + "/" + filename);
				FileInputStream fis = new FileInputStream(file);

				bit = BitmapFactory.decodeStream(fis, null, opts);
				if (bit != null) {
					return IMAGE_IN_ASSETS;
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
			}
		}
		if (bit == null) {
			File[] files = cdir.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				File file = files[i];
				if (file.getName().equalsIgnoreCase(filename)) {
					FileInputStream fis = null;
					try {
//						D.e("read img from cache");
						fis = new FileInputStream(file);
						bit = BitmapFactory.decodeStream(fis, null, opts);
						if (bit != null) {
							//D.i("get True Bitmap file:" + imgurl);
							return IMAGE_IN_LOCAL_FILE;
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						if (fis != null)
							try {
								fis.close();
							} catch (IOException e) {
//								e.printStackTrace();
							}
					}
				}
			}
		}
		return IMAGE_IN_SERVER;
	}

	/**
	 * 准备图片，没有就下载，返回图片的来源状态。
	 * 
	 * @param context
	 * @param imgurl
	 * @return
	 */
	public static int prepareImage(Context context, String imgurl) {
		if (imgurl == null || "".equals(imgurl)) {
			return IMAGE_IN_ERROR;
		}
		File cdir = context.getCacheDir();
		String filename = getFilename(imgurl);
		Bitmap bit = null;
		Options opts = new BitmapFactory.Options();
		// TypedValue value = new TypedValue();
		// context.getResources().openRawResource(R.drawable.icon_gps_on,
		// value);
		// BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inTargetDensity = getDensityDpi(context);
		;
		// opts.inTargetDensity= getDensityDpi(context);
		// opts.inSampleSize = getDensityDpi(context);
		AssetManager am = context.getAssets();
		InputStream ais = null;
		try {
			ais = am.open(filename);
			if (ais != null) {
				saveFile(ais, cdir.getPath() + "/" + filename);
				File file = new File(cdir.getPath() + "/" + filename);
				FileInputStream fis = new FileInputStream(file);

				bit = BitmapFactory.decodeStream(fis, null, opts);
				if (bit != null) {
					//D.i("get True Bitmap asset:" + imgurl);
					return IMAGE_IN_ASSETS;
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
			}
		}
		if (bit == null) {
			File[] files = cdir.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				File file = files[i];
				if (file.getName().equalsIgnoreCase(filename)) {
					FileInputStream fis = null;
					try {
//						D.e("read img from cache");
						fis = new FileInputStream(file);
						bit = BitmapFactory.decodeStream(fis, null, opts);
						if (bit != null) {
							//D.i("get True Bitmap file:" + imgurl);
							return IMAGE_IN_LOCAL_FILE;
						}
					} catch (FileNotFoundException e) {
//						e.printStackTrace();
					} finally {
						if (fis != null)
							try {
								fis.close();
							} catch (IOException e) {
//								e.printStackTrace();
							}
					}
				}
			}
		}
		if (bit == null) {
			try {
				URL url = new URL(imgurl);
				// NetCheck.getInstance(context).checkProxy();
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(10 * 1000);
				conn.setReadTimeout(10 * 1000);
				conn.setUseCaches(true);
				conn.connect();
				try {
					InputStream is = conn.getInputStream();
					try {
						if (is != null) {
							saveFile(is, cdir.getPath() + "/" + filename);
							File file = new File(cdir.getPath() + "/"
									+ filename);
							FileInputStream fis = new FileInputStream(file);
							bit = BitmapFactory.decodeStream(fis, null, opts);
							if (bit != null) {
								//D.i("get True Bitmap URLConnection:" + imgurl);
								return IMAGE_IN_SERVER;
							}
						}
					} catch (FileNotFoundException e) {
						// Log.i(LOG_TAG,
						// "the logo was not upload so lost the file name");
//						e.printStackTrace();
					} finally {
						if (is != null)
							is.close();
					}
				} finally {

				}
			} catch (Exception e) {
				// Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
		return IMAGE_IN_ERROR;

	}
	
	public static String getImagePath(Activity activity, String url,int type) {
		if (url == null)
			return "";
		String imagePath = "";
		String fileName = "";
		if (url != null && url.length() != 0) {
			fileName = url.substring(url.lastIndexOf("/") + 1);
		}

		String path = getSDPath();
		if (path != null && !path.trim().equals("")) {
			File tmpFile = new File(path + "/download");
			if (!tmpFile.exists()) {
				tmpFile.mkdirs();
			}
			File file = new File(path + "/download", fileName);

			if (!file.exists()) {
				try {
					byte[] data = readInputStream(getRequest(url));
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					float scale_width = ((getScreenWidth(activity)) * 1.0f)
							/ bitmap.getHeight();
					Bitmap sBitmap = scaleBitmap(bitmap, scale_width,
							scale_width,type);
					sBitmap.compress(CompressFormat.JPEG, 20,
							new FileOutputStream(file));
					imagePath = file.getAbsolutePath();
					bitmap.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				imagePath = file.getAbsolutePath();
			}
		}
		return imagePath;
	}

	/**
	 * 获得屏幕的高
	 * 
	 * @param activity
	 * @return
	 */
	static int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获得屏幕的宽
	 * 
	 * @param activity
	 * @return
	 */
	static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(10 * 1000);
		conn.setReadTimeout(10 * 1000);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		return outSteam.toByteArray();
	}
	protected static Bitmap scaleBitmap(Bitmap bm, float scaleX, float scaleY,int type) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		Matrix matrix = new Matrix();
		if(type==1){
			matrix.postScale(scaleX, scaleY);
			matrix.postRotate(90.0f);
		}
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
}