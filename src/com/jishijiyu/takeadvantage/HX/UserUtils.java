package com.jishijiyu.takeadvantage.HX;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.ImageView;

public class UserUtils {
	/**
	 * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
	 * 
	 * @param username
	 * @return
	 */
	public static User getUserInfo(String username) {
		User user = App.getInstance().getContactList().get(username);
		if (user == null) {
			user = new User(username);
		}

		if (user != null) {
			// demo没有这些数据，临时填充
			user.setNick(username);
			// user.setAvatar("http://downloads.easemob.com/downloads/57.png");
		}
		return user;
	}

	/**
	 * 设置对方头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username,
			ImageView imageView, String toChatAvatar) {
		User user = getUserInfo(username);
		if (user != null) {
			// Picasso.with(context).load(user.getAvatar())
			// .placeholder(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(toChatAvatar, imageView);
		} else {
			// Picasso.with(context).load(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(toChatAvatar, imageView);
		}
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar2(Context context, String username,
			ImageView imageView, String avatar) {
		User user = getUserInfo(username);
		if (user != null) {
			// Picasso.with(context).load(user.getAvatar())
			// .placeholder(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(avatar, imageView);
		} else {
			// Picasso.with(context).load(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(avatar, imageView);
		}
	}

	/**
	 * 设置自己头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar3(Context context, String username,
			ImageView imageView, String myAvatar) {
		User user = getUserInfo(username);
		if (user != null) {
			// Picasso.with(context).load(user.getAvatar())
			// .placeholder(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(myAvatar, imageView);
		} else {
			// Picasso.with(context).load(R.drawable.ic_launcher).into(imageView);
			ImageLoaderUtil.loadImage(myAvatar, imageView);
		}
	}
}
