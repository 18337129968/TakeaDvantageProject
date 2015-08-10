package com.jishijiyu.takeadvantage.utils;

import com.jishijiyu.takeadvantage.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.sax.StartElementListener;

/**
 * 判断应用是否开启声音
 * 
 * @author shifeiyu
 * 
 */
public class VoiceUtil {
	private static SharedPreferences sp;
	private static String isVoice;
	public static MediaPlayer mp;

	public static boolean isVoice(Context context) {

		sp = context.getSharedPreferences("SetVoiceMode", Context.MODE_PRIVATE);
		if (sp == null) {
			return false;
		}
		isVoice = sp.getString("isVoice", "On");
		if (isVoice.equals("On")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 播放摇奖音乐
	 * 
	 * @author shifeiyu
	 * @param context
	 */
	public static void playErnieMusic(Context context) {
		mp = new MediaPlayer();
		mp = MediaPlayer.create(context, R.raw.ernie_music);
		mp.start(); // 开始播放
		mp.setLooping(true); // 循环播放
	}

	/**
	 * 停止播放摇奖音乐
	 * 
	 * @param context
	 */
	public static void stopErnieMusic(Context context) {
		mp.stop();
		mp.release();
		mp = null;

	}

}
