package com.jishijiyu.takeadvantage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.xkeuops.diy.AdExtraTaskStatus;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.TaskDescObject;

/**
 * 详情页面的任务描述列表Adapter
 * @author youmi
 * @date 2015-4-24 上午9:13:02
 */
public class ListViewAdapter_TaskDesc extends BaseAdapter {

	List<TaskDescObject> mList;
	private final Context context;

	public ListViewAdapter_TaskDesc(Context context, ArrayList<TaskDescObject> list) {
		this.context = context;
		mList = list;
	}

	public void setData(List<TaskDescObject> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public TaskDescObject getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_task_desc, null);
			viewHolder.tv_id = (TextView) convertView.findViewById(R.id.lvitem_tv_ext_id);
			viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.lvitem_tv_ext_desc);
			viewHolder.tv_status = (TextView) convertView.findViewById(R.id.lvitem_tv_ext_status);
			viewHolder.tv_points = (TextView) convertView.findViewById(R.id.lvitem_tv_ext_points);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TaskDescObject descObject = getItem(position);

		// 设置左边的数字序号
		viewHolder.tv_id.setText(String.valueOf(position + 1));
		// 设置任务赚取描述
		viewHolder.tv_desc.setText(descObject.getDesc());
		// 设置拍币或者已完成
		viewHolder.tv_points.setText("+" + descObject.getPoints());

		switch (descObject.getStatus()) {

		// 任务未开始
		case AdExtraTaskStatus.NOT_START:
			viewHolder.tv_id.setTextColor(context.getResources().getColor(R.color.task_status_not_complete_text_color));
			viewHolder.tv_id.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cannotdo_bg));

			viewHolder.tv_desc.setTextColor(context.getResources().getColor(R.color.task_status_cannotdo_text_color));

			viewHolder.tv_points.setTextColor(context.getResources().getColor(
					R.color.task_status_not_complete_text_color));
			viewHolder.tv_points.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cannotdo_bg));

			viewHolder.tv_status.setText("任务未开始（请勿卸载）");
			break;

		// 任务可以做
		case AdExtraTaskStatus.IN_PROGRESS:
			viewHolder.tv_id.setTextColor(Color.WHITE);
			viewHolder.tv_id.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cando_bg));

			viewHolder.tv_desc.setTextColor(Color.BLACK);

			viewHolder.tv_points.setTextColor(Color.WHITE);
			viewHolder.tv_points.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cando_bg));

			viewHolder.tv_status.setText("任务可进行");
			break;

		// 任务已完成
		case AdExtraTaskStatus.COMPLETE:
			viewHolder.tv_id.setTextColor(context.getResources().getColor(R.color.task_status_not_complete_text_color));
			viewHolder.tv_id.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cannotdo_bg));

			viewHolder.tv_desc.setTextColor(context.getResources().getColor(R.color.task_status_cannotdo_text_color));
			viewHolder.tv_desc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			viewHolder.tv_points.setText("完成");
			viewHolder.tv_points.setTextColor(Color.WHITE);
			viewHolder.tv_points.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_complete_bg));

			viewHolder.tv_status.setText("任务已完成（请勿卸载）");
			break;

		// 任务已过时
		case AdExtraTaskStatus.OUT_OF_DATE:
			viewHolder.tv_id.setTextColor(context.getResources().getColor(R.color.task_status_not_complete_text_color));
			viewHolder.tv_id.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cannotdo_bg));

			viewHolder.tv_desc.setTextColor(context.getResources().getColor(R.color.task_status_cannotdo_text_color));
			viewHolder.tv_desc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			viewHolder.tv_points.setText("过期");
			viewHolder.tv_points.setTextColor(context.getResources().getColor(
					R.color.task_status_not_complete_text_color));
			viewHolder.tv_points.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tv_task_cannotdo_bg));

			viewHolder.tv_status.setText("任务已过期");
			break;

		}

		return convertView;
	}

	private static class ViewHolder {
		TextView tv_id;
		TextView tv_desc;
		TextView tv_status;
		TextView tv_points;
	}
}
