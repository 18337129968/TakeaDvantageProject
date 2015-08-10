package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.CheckRegisterInfoResult;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 查询报名者信息adapter
 * 
 * @author shifeiyu
 * 
 */
public class CheckRegisterInfoAdapter extends BaseAdapter {
	List<CheckRegisterInfoResult.EnrollList> list;
	Activity context;

	public CheckRegisterInfoAdapter(Activity context,
			List<CheckRegisterInfoResult.EnrollList> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_reuse_list_item, null);
			viewholder = new ViewHolder();
			viewholder.reuse_item_img = (ImageView) convertView
					.findViewById(R.id.reuse_item_img);
			viewholder.reuse_item_text1 = (TextView) convertView
					.findViewById(R.id.reuse_item_text1);
			viewholder.reuse_item_text2 = (TextView) convertView
					.findViewById(R.id.reuse_item_text2);
			viewholder.reuse_item_text3 = (TextView) convertView
					.findViewById(R.id.reuse_item_text3);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		if (convertView != null) {
			ImageLoaderUtil.loadImage(list.get(position).headImgUrl,
					viewholder.reuse_item_img);
			viewholder.reuse_item_text1.setText("报名时间:"
					+ GetTimeUtil.GetTime(list.get(position).enrollTime));
			String nickName = "", province = "";
			if (TextUtils.isEmpty(list.get(position).userName)) {
				nickName = "未设置";
				viewholder.reuse_item_text2.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(list.get(position).userName)
					&& TextUtils.isEmpty(list.get(position).province)) {
				nickName = list.get(position).userName;
				viewholder.reuse_item_text2.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(list.get(position).userName)
					&& !TextUtils.isEmpty(list.get(position).province)) {
				nickName = list.get(position).userName;
				province = list.get(position).province;
				viewholder.reuse_item_text2.setText("昵称:" + nickName + " ("
						+ province + ")");
			}
			viewholder.reuse_item_text3.setText("用户ID:"
					+ list.get(position).userId);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView reuse_item_text1, reuse_item_text2, reuse_item_text3;
		ImageView reuse_item_img;
	}
}
