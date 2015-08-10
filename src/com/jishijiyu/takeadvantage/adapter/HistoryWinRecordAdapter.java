package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.BeforeWinRecordResult.HistoryWinningList;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 往期中奖记录expandableadapter
 * 
 * @author shifeiyu
 * 
 */
public class HistoryWinRecordAdapter extends BaseExpandableListAdapter {
	Activity context;
	List<HistoryWinningList> mlist;

	public HistoryWinRecordAdapter(Activity context,
			List<HistoryWinningList> list) {
		this.context = context;
		this.mlist = list;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mlist.get(groupPosition).list.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		viewHolder viewholder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_reuse_list_item, null);
			viewholder = new viewHolder();
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
			viewholder = (viewHolder) convertView.getTag();
		}
		if (convertView != null) {
			ImageLoaderUtil
					.loadImage(
							mlist.get(groupPosition).list.get(childPosition).headImgUrl,
							viewholder.reuse_item_img);
			viewholder.reuse_item_text1.setText(GetMealCountUtil.GetGrade(mlist
					.get(groupPosition).list.get(childPosition).grade));
			viewholder.reuse_item_text2.setText("中奖时间:"
					+ GetTimeUtil.GetTime(mlist.get(groupPosition).list
							.get(childPosition).winTime));
			String nickName = "", province = "";
			if (TextUtils.isEmpty(mlist.get(groupPosition).list
					.get(childPosition).nickname)) {
				nickName = "未设置";
				viewholder.reuse_item_text3.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(mlist.get(groupPosition).list
					.get(childPosition).nickname)
					&& TextUtils.isEmpty(mlist.get(groupPosition).list
							.get(childPosition).province)) {
				nickName = mlist.get(groupPosition).list.get(childPosition).nickname;
				viewholder.reuse_item_text3.setText("昵称:" + nickName);
			}
			if (!TextUtils.isEmpty(mlist.get(groupPosition).list
					.get(childPosition).nickname)
					&& !TextUtils.isEmpty(mlist.get(groupPosition).list
							.get(childPosition).province)) {
				nickName = mlist.get(groupPosition).list.get(childPosition).nickname;
				province = mlist.get(groupPosition).list.get(childPosition).province;
				viewholder.reuse_item_text3.setText("昵称:" + nickName + "("
						+ province + ")");
			}

		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mlist.get(groupPosition).list.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mlist.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		viewHolder viewholder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_expandablelist_item, null);
			viewholder = new viewHolder();
			viewholder.expandable_text1 = (TextView) convertView
					.findViewById(R.id.expandable_text1);
			viewholder.expandable_text2 = (TextView) convertView
					.findViewById(R.id.expandable_text2);
			convertView.setTag(viewholder);

		} else {
			viewholder = (viewHolder) convertView.getTag();
		}
		if (convertView != null) {
			viewholder.expandable_text1.setText("第"
					+ mlist.get(groupPosition).perios + "期");
			viewholder.expandable_text2.setText("开奖时间:"
					+ GetTimeUtil.GetTime(mlist.get(groupPosition).beginTimep));
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public class viewHolder {
		TextView reuse_item_text1, reuse_item_text2, reuse_item_text3,
				expandable_text1, expandable_text2;
		ImageView reuse_item_img;
	}

}
