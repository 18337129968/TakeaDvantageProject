package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingEndResult.PostAfterList;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 投放后广告adapter
 * 
 * @author shifeiyu
 * 
 */
public class AdvertisingEndAdapter extends MyBaseAdapter {
	List<PostAfterList> postAfterList;
	Activity context;

	public AdvertisingEndAdapter(Activity context, List<PostAfterList> list) {
		super(context, list);
		this.postAfterList = list;
		this.context = context;
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.advertising_item, null);
			viewHolder = new ViewHolder();
			viewHolder.item_img = (ImageView) convertView
					.findViewById(R.id.item_img);
			viewHolder.item_name = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.surplus_days_text = (TextView) convertView
					.findViewById(R.id.surplus_days_text);
			viewHolder.browse_count = (TextView) convertView
					.findViewById(R.id.browse_count);
			viewHolder.tasks_count = (TextView) convertView
					.findViewById(R.id.tasks_count);
			viewHolder.collect_count = (TextView) convertView
					.findViewById(R.id.collect_count);
			viewHolder.advert_image_layout1 = (LinearLayout) convertView
					.findViewById(R.id.advert_image_layout1);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (convertView != null) {
			ImageLoaderUtil.loadImage(postAfterList.get(position).posterImgUrl,
					viewHolder.item_img);
			viewHolder.item_name
					.setText(postAfterList.get(position).posterTitle);
			viewHolder.surplus_days_text.setText("距离过期：5天");
			viewHolder.browse_count.setText(postAfterList.get(position).lookNum
					+ "");
			viewHolder.tasks_count
					.setText(postAfterList.get(position).answerNum + "");
			viewHolder.collect_count
					.setText(postAfterList.get(position).collectNum + "");

			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels / 5;
			int height = width;
			LayoutParams params = (LayoutParams) viewHolder.advert_image_layout1
					.getLayoutParams();
			params.width = width;
			params.height = height;
			viewHolder.advert_image_layout1.setLayoutParams(params);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView item_name, surplus_days_text, browse_count, tasks_count,
				collect_count;
		ImageView item_img;
		LinearLayout advert_image_layout1;
	}

}
