package com.jishijiyu.takeadvantage.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingBeforeResult.PostAgoList;
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
 * 投放前广告adapter
 * 
 * @author shifeiyu
 * 
 */
public class AdvertisingBeforeAdapter extends MyBaseAdapter {
	List<PostAgoList> postAgoList;
	Activity context;

	public AdvertisingBeforeAdapter(Activity context, List<PostAgoList> list) {
		super(context, list);
		this.postAgoList = list;
		this.context = context;
	}

	@Override
	public View initView(int position, View convertView) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.advertising_before_item, null);
			viewHolder = new ViewHolder();
			viewHolder.advert_image = (ImageView) convertView
					.findViewById(R.id.advert_image);
			viewHolder.advert_name_text = (TextView) convertView
					.findViewById(R.id.advert_name_text);
			viewHolder.advert_state_text = (TextView) convertView
					.findViewById(R.id.advert_state_text);
			viewHolder.advert_date_text = (TextView) convertView
					.findViewById(R.id.advert_date_text);
			viewHolder.advert_image_layout = (LinearLayout) convertView
					.findViewById(R.id.advert_image_layout);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (convertView != null) {
			ImageLoaderUtil.loadImage(postAgoList.get(position).posterImgUrl,
					viewHolder.advert_image);
			viewHolder.advert_date_text.setText(GetTime(postAgoList
					.get(position).createTime));
			viewHolder.advert_name_text
					.setText(postAgoList.get(position).posterTitle);
			switch (postAgoList.get(position).posterState) {
			case 0:
				viewHolder.advert_state_text.setText("待审核");
				break;
			case 1:
				viewHolder.advert_state_text.setText("审核通过");
				break;
			case 2:
				viewHolder.advert_state_text.setText("审核失败");
				break;

			default:
				break;
			}
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels / 5;
			int height = width;
			LayoutParams params = (LayoutParams) viewHolder.advert_image_layout
					.getLayoutParams();
			params.width = width;
			params.height = height;
			viewHolder.advert_image_layout.setLayoutParams(params);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView advert_name_text, advert_state_text, advert_date_text;
		ImageView advert_image;
		LinearLayout advert_image_layout;
	}

	public static String GetTime(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str_time = sdf.format(cc_time);
		return str_time;

	}

}
