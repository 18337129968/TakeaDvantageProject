package com.jishijiyu.takeadvantage.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.RoomDetailsResult;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 房间详情报名listadapter
 * 
 * @author shifeiyu
 * 
 */
public class RegistrationDetailsListAdapter extends MyBaseAdapter {
	List<RoomDetailsResult.enrollList> list;

	RoomDetailsResult.dollerEnrollVO vo;
	RoomDetailsResult.dollerRoom dollerRoom;

	public RegistrationDetailsListAdapter(Context context,
			RoomDetailsResult.dollerEnrollVO vo,
			List<RoomDetailsResult.enrollList> list) {
		super(context, list);
		this.list = list;
		this.vo = vo;
	}

	@Override
	public int getCount() {
		if (vo == null && list != null) {
			return list.size();
		} else {
			return list != null ? list.size() + 1 : 1;
		}
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder = null;
		if (vo != null) {
			if (position == 0) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_reuse_list_item, null);
				TextView reuse_item_title, reuse_item_text1, reuse_item_text2, reuse_item_text3;
				ImageView reuse_item_img = (ImageView) convertView
						.findViewById(R.id.reuse_item_img);
				reuse_item_title = (TextView) convertView
						.findViewById(R.id.reuse_item_title);
				reuse_item_text1 = (TextView) convertView
						.findViewById(R.id.reuse_item_text1);
				reuse_item_text2 = (TextView) convertView
						.findViewById(R.id.reuse_item_text2);
				reuse_item_text3 = (TextView) convertView
						.findViewById(R.id.reuse_item_text3);
				reuse_item_title.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(vo.headImgUrl, reuse_item_img);
				reuse_item_text1.setText("创建报名时间:" + GetTime(vo.enrollTime));
				String nickName = null, province = null;
				if (TextUtils.isEmpty(vo.userName)) {
					nickName = "未设置";
					reuse_item_text2.setText("昵称:" + nickName);
				}
				if (!TextUtils.isEmpty(vo.userName)
						&& TextUtils.isEmpty(vo.province)) {
					nickName = vo.userName;
					reuse_item_text2.setText("昵称:" + nickName);
				}
				if (!TextUtils.isEmpty(vo.userName)
						&& !TextUtils.isEmpty(vo.province)) {
					nickName = vo.userName;
					province = vo.province;
					reuse_item_text2.setText("昵称:" + nickName + "(" + province
							+ ")");
				}
				reuse_item_text3.setText("用户ID:" + vo.userId);

			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_reuse_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.reuse_item_img = (ImageView) convertView
						.findViewById(R.id.reuse_item_img);
				viewHolder.reuse_item_text1 = (TextView) convertView
						.findViewById(R.id.reuse_item_text1);
				viewHolder.reuse_item_text2 = (TextView) convertView
						.findViewById(R.id.reuse_item_text2);
				viewHolder.reuse_item_text3 = (TextView) convertView
						.findViewById(R.id.reuse_item_text3);
				ImageLoaderUtil.loadImage(list.get(position - 1).headImgUrl,
						viewHolder.reuse_item_img);
				viewHolder.reuse_item_text1.setText("报名时间:"
						+ GetTime(list.get(position - 1).enrollTime));
				String nickName = null, province = null;
				if (TextUtils.isEmpty(list.get(position - 1).userName)) {
					nickName = "未设置";
					viewHolder.reuse_item_text2.setText("昵称:" + nickName);
				}
				if (!TextUtils.isEmpty(list.get(position - 1).userName)
						&& TextUtils.isEmpty(list.get(position - 1).province)) {
					nickName = list.get(position - 1).userName;
					viewHolder.reuse_item_text2.setText("昵称:" + nickName);
				}
				if (!TextUtils.isEmpty(list.get(position - 1).userName)
						&& !TextUtils.isEmpty(list.get(position - 1).province)) {
					nickName = list.get(position - 1).userName;
					province = list.get(position - 1).province;
					viewHolder.reuse_item_text2.setText("昵称:" + nickName + "("
							+ province + ")");
				}
				viewHolder.reuse_item_text3.setText("用户ID:"
						+ list.get(position - 1).userId);
			}
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_reuse_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.reuse_item_img = (ImageView) convertView
					.findViewById(R.id.reuse_item_img);
			viewHolder.reuse_item_text1 = (TextView) convertView
					.findViewById(R.id.reuse_item_text1);
			viewHolder.reuse_item_text2 = (TextView) convertView
					.findViewById(R.id.reuse_item_text2);
			viewHolder.reuse_item_text3 = (TextView) convertView
					.findViewById(R.id.reuse_item_text3);
			ImageLoaderUtil.loadImage(list.get(position).headImgUrl,
					viewHolder.reuse_item_img);
			viewHolder.reuse_item_text1.setText("报名时间:"
					+ GetTime(list.get(position).enrollTime));
			String nickName = null, province = null;
			if (TextUtils.isEmpty(list.get(position).userName)) {
				nickName = "未设置";
				viewHolder.reuse_item_text2.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(list.get(position).userName)
					&& TextUtils.isEmpty(list.get(position).province)) {
				nickName = list.get(position).userName;
				viewHolder.reuse_item_text2.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(list.get(position).userName)
					&& !TextUtils.isEmpty(list.get(position).province)) {
				nickName = list.get(position).userName;
				province = list.get(position).province;
				viewHolder.reuse_item_text2.setText("昵称:" + nickName + "("
						+ province + ")");
			}

			viewHolder.reuse_item_text3.setText("用户ID:"
					+ list.get(position).userId);
		}

		return convertView;
	}

	public class ViewHolder {
		TextView reuse_item_text1, reuse_item_text2, reuse_item_text3;
		ImageView reuse_item_img;
	}

	public static String GetTime(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str_time = sdf.format(cc_time);
		return str_time;

	}

}
