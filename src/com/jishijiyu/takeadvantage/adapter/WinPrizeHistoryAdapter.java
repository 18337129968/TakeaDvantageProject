package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.JoinedRoomInfoResult.DollerHistoryWinningVOList;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
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
 * 本期中奖记录
 * 
 * @author shifeiyu
 * 
 */
public class WinPrizeHistoryAdapter extends BaseAdapter {
	List<DollerHistoryWinningVOList> dollerHistoryWinningVOList;
	Activity context;

	public WinPrizeHistoryAdapter(Activity context,
			List<DollerHistoryWinningVOList> list) {
		this.dollerHistoryWinningVOList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dollerHistoryWinningVOList.size();
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
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_reuse_list_item, null);
			viewHolder.reuse_item_img = (ImageView) convertView
					.findViewById(R.id.reuse_item_img);
			viewHolder.reuse_item_text1 = (TextView) convertView
					.findViewById(R.id.reuse_item_text1);
			viewHolder.reuse_item_text2 = (TextView) convertView
					.findViewById(R.id.reuse_item_text2);
			viewHolder.reuse_item_text3 = (TextView) convertView
					.findViewById(R.id.reuse_item_text3);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (convertView != null) {
			ImageLoaderUtil.loadImage(
					dollerHistoryWinningVOList.get(position).headImgUrl,
					viewHolder.reuse_item_img);
			viewHolder.reuse_item_text1.setText(GetMealCountUtil
					.GetGrade(dollerHistoryWinningVOList.get(position).grade));
			viewHolder.reuse_item_text2.setText("中奖时间:"
					+ GetTimeUtil.GetTime(dollerHistoryWinningVOList
							.get(position).winTime));
			String nickName = null, province = null;
			if (TextUtils
					.isEmpty(dollerHistoryWinningVOList.get(position).nickname)) {
				nickName = "未设置";
				viewHolder.reuse_item_text3.setText("昵称:" + nickName);
			}
			if (!TextUtils
					.isEmpty(dollerHistoryWinningVOList.get(position).nickname)
					&& TextUtils.isEmpty(dollerHistoryWinningVOList
							.get(position).province)) {
				nickName = dollerHistoryWinningVOList.get(position).nickname;
				viewHolder.reuse_item_text3.setText("昵称:" + nickName);
			}
			if (!TextUtils
					.isEmpty(dollerHistoryWinningVOList.get(position).nickname)
					&& !TextUtils.isEmpty(dollerHistoryWinningVOList
							.get(position).province)) {
				nickName = dollerHistoryWinningVOList.get(position).nickname;
				province = dollerHistoryWinningVOList.get(position).province;
				viewHolder.reuse_item_text3.setText("昵称:" + nickName + "("
						+ province + ")");
			}

		}
		return convertView;
	}

	public class ViewHolder {
		TextView reuse_item_text1, reuse_item_text2, reuse_item_text3;
		ImageView reuse_item_img;
	}

}
