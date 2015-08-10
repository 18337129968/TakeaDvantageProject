package com.jishijiyu.takeadvantage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.AddAddressActivity;
import com.jishijiyu.takeadvantage.entity.ReceivingAddress;

public class MoreAddressLvAdapter extends MyBaseAdapter {
	List<ReceivingAddress> list = new ArrayList<ReceivingAddress>();
	ReceivingAddress address;
    private Context context;
	public MoreAddressLvAdapter(Context context, List<ReceivingAddress> list) {
		super(context, list);
		this.list = list;
		this.context = context;
	}

	@SuppressLint("InflateParams")
	@Override
	public View initView(final int position, View convertView) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflate.inflate(R.layout.more_address_lv_item, null);
			viewHolder = new ViewHolder();
			viewHolder.nameText = (TextView) convertView
					.findViewById(R.id.more_address_lv_item_name_tv);
			viewHolder.phoneNumText = (TextView) convertView
					.findViewById(R.id.more_address_lv_item_phonenum_tv);
			viewHolder.addressText = (TextView) convertView
					.findViewById(R.id.more_address_lv_item_address);
			viewHolder.isDefultText = (TextView) convertView
					.findViewById(R.id.more_address_lv_item_tv_defult);
			viewHolder.writeBtn = (ImageView) convertView
					.findViewById(R.id.more_address_write_img);
			viewHolder.isCheaked = (ImageView) convertView
					.findViewById(R.id.more_address_lv_item_deflt_img);
			viewHolder.isCheakedItem = (LinearLayout) convertView
					.findViewById(R.id.more_address_lv_item_ll);
		
			viewHolder.writeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					address = list.get(position);
					Intent intent = new Intent(context,AddAddressActivity.class);
					intent.putExtra(HeadBaseActivity.intentKey, "isdefult");
					intent.putExtra("address", address);
					context.startActivity(intent);
				
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (convertView != null) {
			viewHolder.nameText.setText(list.get(position).getName());
			viewHolder.phoneNumText.setText(list.get(position).getTelephone());
			viewHolder.addressText.setText(list.get(position)
					.getDetailedAddress());
			viewHolder.writeBtn.setVisibility(View.VISIBLE);
			if (list.get(position).isIsdefult()) {
				viewHolder.isDefultText.setVisibility(View.VISIBLE);
			} else {
				viewHolder.isDefultText.setVisibility(View.GONE);
			}
			if (list.get(position).isChecked()) {
				viewHolder.isCheaked
						.setImageResource(R.drawable.shouhuo_btn_choose);
				viewHolder.isCheakedItem
						.setBackgroundResource(R.drawable.shouhuo_choose_dizhi);
			
			} else {
				viewHolder.isCheaked
						.setImageResource(R.drawable.shouhuo_btn_no_choose);
				viewHolder.isCheakedItem
						.setBackgroundResource(R.drawable.shouhuo_choose_dizhi);
			
			}
			notifyDataSetChanged();
		}

		return convertView;
	}

	public class ViewHolder {
		TextView nameText;
		TextView phoneNumText;
		TextView addressText;
		TextView isDefultText;
		ImageView writeBtn;
		ImageView isCheaked;
		LinearLayout isCheakedItem;
	}
}
