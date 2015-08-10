package com.jishijiyu.takeadvantage.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ernieonermb.RegistrationDetailActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.AddListRequest;
import com.jishijiyu.takeadvantage.entity.result.AddListResult;
import com.jishijiyu.takeadvantage.entity.result.RoomListResult;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 一元摇奖大厅界面listadapter
 * 
 * @author shifeiyu
 * 
 */
public class OneRmbRoomListAdapter extends MyBaseAdapter {
	List<RoomListResult.RoomList> list;
	Activity context;
	String action = "UpdateList";
	List<Integer> atList;

	public OneRmbRoomListAdapter(Activity context,
			List<RoomListResult.RoomList> list, List<Integer> atList) {
		super(context, list);
		this.list = list;
		this.context = context;
		this.atList = atList;
	}

	@Override
	public View initView(final int position, View convertView) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_one_rmb_ernie_home_item, null);
			viewHolder = new ViewHolder();
			viewHolder.list_item_home_title_text = (TextView) convertView
					.findViewById(R.id.list_item_home_title_text);
			viewHolder.list_item_taocan_title_text = (TextView) convertView
					.findViewById(R.id.list_item_taocan_title_text);
			viewHolder.list_item_prize_name_text = (TextView) convertView
					.findViewById(R.id.list_item_prize_name_text);
			viewHolder.list_item_joinlist_btn = (TextView) convertView
					.findViewById(R.id.list_item_joinlist_btn);
			viewHolder.list_item_prize_img = (ImageView) convertView
					.findViewById(R.id.list_item_prize_img);
			viewHolder.list_item_progressbar = (ProgressBar) convertView
					.findViewById(R.id.list_item_progressbar);
			viewHolder.room_list_item_layout = (LinearLayout) convertView
					.findViewById(R.id.room_list_item_layout);
			viewHolder.progress_text = (TextView) convertView
					.findViewById(R.id.progress_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (convertView != null) {
			viewHolder.list_item_home_title_text.setText("房间"
					+ list.get(position).roomIssue);
			viewHolder.list_item_prize_name_text
					.setText(list.get(position).prizeName);
			viewHolder.list_item_taocan_title_text
					.setText(list.get(position).mealName);
			ImageLoaderUtil.loadImage(list.get(position).prizeURL,
					viewHolder.list_item_prize_img);
			viewHolder.list_item_progressbar.setMax(GetMealCountUtil
					.GetMealCount(list.get(position).mealType));
			viewHolder.list_item_progressbar
					.setProgress(list.get(position).joinNumber);
			float progressNum, mealNum, joinNum;
			mealNum = GetMealCountUtil
					.GetMealCount(list.get(position).mealType);
			joinNum = list.get(position).joinNumber;
			progressNum = joinNum / mealNum;
			DecimalFormat df = new DecimalFormat("0%");
			viewHolder.progress_text.setText("开奖进度:" + df.format(progressNum));

			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = (dm.widthPixels / 2 - 30);
			int height = (width - 20);
			LayoutParams params = (LayoutParams) viewHolder.room_list_item_layout
					.getLayoutParams();
			params.width = width;
			params.height = height;
			viewHolder.room_list_item_layout.setLayoutParams(params);
			viewHolder.room_list_item_layout
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String id = String.valueOf(list.get(position).id);
							Intent intent = new Intent(context,
									RegistrationDetailActivity.class);
							intent.putExtra("RoomId", id);// 房间ID
							intent.putExtra("Type", "NotRegistration");// 是否加入房间
							context.startActivity(intent);
						}
					});
			if (atList.size() > 0 && atList.contains(list.get(position).id)) {
				viewHolder.list_item_joinlist_btn.setText("已加入清单");
				viewHolder.list_item_joinlist_btn
						.setBackgroundResource(R.drawable.btn_reget_code);
				viewHolder.list_item_joinlist_btn.setClickable(isEmpty());
				viewHolder.list_item_joinlist_btn.setFocusable(true);
				viewHolder.list_item_joinlist_btn.setOnClickListener(null);

			} else if (!atList.contains(atList.contains(list.get(position).id))) {
				// 加入清单
				viewHolder.list_item_joinlist_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final Gson gson = new Gson();
								AddListRequest request = new AddListRequest();
								request.p.roomId = String.valueOf(list
										.get(position).id);
								request.p.userId = GetUserIdUtil
										.getUserId(context);
								request.p.tokenId = GetUserIdUtil
										.getTokenId(context);
								String requestJson = gson.toJson(request);
								HttpConnectTool.update(requestJson, context,
										new ConnectListener() {

											@Override
											public void contectSuccess(
													String result) {
												if (!TextUtils.isEmpty(result)) {
													AddListResult addResult = gson
															.fromJson(
																	result,
																	AddListResult.class);
													if (addResult.p.isTrue) {
														switch (addResult.p.type) {
														case 0:
															viewHolder.list_item_joinlist_btn
																	.setText("已加入清单");
															viewHolder.list_item_joinlist_btn
																	.setBackgroundResource(R.drawable.btn_reget_code);
															Intent intent = new Intent(
																	action);
															context.sendBroadcast(intent);
															ToastUtils
																	.makeText(
																			context,
																			"加入清单成功",
																			0)
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
															ToastUtils
																	.makeText(
																			context,
																			"加入清单失败",
																			0)
																	.show();
															break;

														default:
															break;
														}
													} else {
														ToastUtils
																.makeText(
																		context,
																		R.string.again_login_text,
																		0)
																.show();
														Intent intent = new Intent(
																context,
																LoginActivity.class);
														context.startActivity(intent);
														context.finish();
													}
												}
											}

											@Override
											public void contectStarted() {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void contectFailed(
													String path, String request) {
												ToastUtils.makeText(context,
														"连接服务器失败", 0).show();
											}
										});
							}
						});
			}

		}
		return convertView;
	}

	public class ViewHolder {
		TextView list_item_home_title_text, list_item_taocan_title_text,
				list_item_prize_name_text, list_item_joinlist_btn,
				progress_text;
		ImageView list_item_prize_img;
		ProgressBar list_item_progressbar;
		LinearLayout room_list_item_layout;
	}

}
