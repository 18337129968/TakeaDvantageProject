package com.jishijiyu.takeadvantage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jishijiyu.takeadvantage.R;

/**
 * 详情页的截图显示
 * 
 * @author youmi
 * @date 2015-4-24 上午9:27:10
 */
public class GridViewAdapter extends BaseAdapter {

	private ArrayList<Bitmap> lists;

	private final Context context;

	public GridViewAdapter(Context context, ArrayList<Bitmap> lists) {
		this.context = context;
		this.lists = lists;
	}

	public void setData(ArrayList<Bitmap> lists) {
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists == null ? 0 : lists.size();
	}

	@Override
	public Bitmap getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.gv_item, null);
			viewHolder.screenShot = (ImageView) convertView.findViewById(R.id.gv_detailpage_appscreenshot);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.screenShot.setImageBitmap(getItem(position));
		return convertView;
	}

	private static class ViewHolder {

		ImageView screenShot;
	}
}
