package com.jishijiyu.takeadvantage.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.request.AddListRequest;
import com.jishijiyu.takeadvantage.entity.result.AddListResult;
import com.jishijiyu.takeadvantage.entity.result.PrizeSearchRoomResult;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 根据奖品搜索房间listadapter
 * 
 * @author shifeiyu
 * 
 */
public class PrizeSearchRoomAdapter extends MyBaseAdapter {
	List<PrizeSearchRoomResult.roomList> list;
	Activity context;
	String action = "UpdateList";

	public PrizeSearchRoomAdapter(Activity context,
			List<PrizeSearchRoomResult.roomList> list) {
		super(context, list);
		this.list = list;
		this.context = context;
	}

	@Override
	public View initView(final int position, View convertView) {
		ViewHolder1 viewHolder1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_one_rmb_ernie_home_item, null);
			viewHolder1 = new ViewHolder1();
			viewHolder1.list_item_home_title_text = (TextView) convertView
					.findViewById(R.id.list_item_home_title_text);
			viewHolder1.list_item_taocan_title_text = (TextView) convertView
					.findViewById(R.id.list_item_taocan_title_text);
			viewHolder1.list_item_prize_name_text = (TextView) convertView
					.findViewById(R.id.list_item_prize_name_text);
			viewHolder1.list_item_joinlist_btn = (TextView) convertView
					.findViewById(R.id.list_item_joinlist_btn);
			viewHolder1.list_item_prize_img = (ImageView) convertView
					.findViewById(R.id.list_item_prize_img);
			viewHolder1.list_item_progressbar = (ProgressBar) convertView
					.findViewById(R.id.list_item_progressbar);
			viewHolder1.room_list_item_layout = (LinearLayout) convertView
					.findViewById(R.id.room_list_item_layout);
			viewHolder1.progress_text = (TextView) convertView
					.findViewById(R.id.progress_text);
			convertView.setTag(viewHolder1);
		} else {
			viewHolder1 = (ViewHolder1) convertView.getTag();
		}
		if (convertView != null) {
			viewHolder1.list_item_home_title_text
					.setText("房间" + (position + 1));
			viewHolder1.list_item_prize_name_text
					.setText(list.get(position).prizeName);
			viewHolder1.list_item_taocan_title_text
					.setText(list.get(position).mealName);
			ImageLoaderUtil.loadImage(list.get(position).prizeURL,
					viewHolder1.list_item_prize_img);
			float progressNum, mealNum, joinNum;
			mealNum = GetMealCountUtil
					.GetMealCount(list.get(position).mealType);
			joinNum = list.get(position).joinNumber;
			progressNum = joinNum / mealNum;
			DecimalFormat df = new DecimalFormat("0%");
			viewHolder1.progress_text.setText("开奖进度:" + df.format(progressNum));
			viewHolder1.list_item_progressbar.setMax(GetMealCountUtil
					.GetMealCount(list.get(position).mealType));
			viewHolder1.list_item_progressbar
					.setProgress(list.get(position).joinNumber);
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels / 2 - 20;
			int height = width;
			LayoutParams params = (LayoutParams) viewHolder1.room_list_item_layout
					.getLayoutParams();
			params.width = width;
			params.height = height;
			viewHolder1.room_list_item_layout.setLayoutParams(params);
			// 加入清单
			viewHolder1.list_item_joinlist_btn
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final Gson gson = new Gson();
							AddListRequest request = new AddListRequest();
							request.p.roomId = String.valueOf(list
									.get(position).id);
							request.p.userId = GetUserIdUtil.getUserId(context);
							request.p.tokenId = GetUserIdUtil
									.getTokenId(context);
							String requestJson = gson.toJson(request);
							HttpConnectTool.update(requestJson, context,
									new ConnectListener() {

										@Override
										public void contectSuccess(String result) {
											if (!TextUtils.isEmpty(result)) {
												AddListResult addResult = gson
														.fromJson(
																result,
																AddListResult.class);
												if (addResult.p.isTrue) {
													switch (addResult.p.type) {
													case 0:
														Intent intent = new Intent(
																action);
														context.sendBroadcast(intent);
														ToastUtils.makeText(
																context,
																"加入清单成功", 0)
																.show();
														break;
													case 1:
														ToastUtils
																.makeText(
																		context,
																		"已加入清单\n不可重复添加",
																		0)
																.show();
														break;
													case 2:
														ToastUtils.makeText(
																context,
																"加入清单失败", 0)
																.show();
														break;

													default:
														break;
													}
												} else {
													ToastUtils.makeText(
															context, "UUID失效",
															0).show();
												}
											}
										}

										@Override
										public void contectStarted() {
											// TODO Auto-generated method stub

										}

										@Override
										public void contectFailed(String path,
												String request) {
											// TODO Auto-generated method stub

										}
									});
						}
					});

		}
		return convertView;
	}

	public class ViewHolder1 {
		TextView list_item_home_title_text, list_item_taocan_title_text,
				list_item_prize_name_text, list_item_joinlist_btn,
				progress_text;
		ImageView list_item_prize_img;
		LinearLayout room_list_item_layout;
		ProgressBar list_item_progressbar;
	}

}
