package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ernieonermb.RegistrationDetailActivity;
import com.jishijiyu.takeadvantage.entity.result.AwaitRoomListResult;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 待揭晓房间列表adapter
 * 
 * @author shifeiyu
 * 
 */
public class AwaitRoomListAdapter extends BaseAdapter {
	public Timecount timecount;
	List<AwaitRoomListResult.roomOnMealVOList> list;
	Activity context;
	long nowTime, countTime, startTime;
	public static final int ITEM_TYPE1 = 1; // 待开奖
	public static final int ITEM_TYPE2 = 2; // 已结束

	public AwaitRoomListAdapter(Activity context,
			List<AwaitRoomListResult.roomOnMealVOList> list, long nowTime) {

		this.context = context;
		this.list = list;
		this.nowTime = nowTime;
	}

	@Override
	public int getItemViewType(int position) {
		if (list.get(position).roomState == 2) {
			return ITEM_TYPE1;
		}
		if (list.get(position).roomState == 4) {
			return ITEM_TYPE2;
		} else {
			return 0;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 100;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (getItemViewType(position) == ITEM_TYPE1) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_await_room_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.await_meal_title = (TextView) convertView
					.findViewById(R.id.await_meal_title);
			viewHolder.await_room_title = (TextView) convertView
					.findViewById(R.id.await_room_title);
			viewHolder.await_perios = (TextView) convertView
					.findViewById(R.id.await_perios);
			viewHolder.await_prize_name = (TextView) convertView
					.findViewById(R.id.await_prize_name);
			viewHolder.await_room_state1 = (TextView) convertView
					.findViewById(R.id.await_room_state1);
			viewHolder.await_prize_img = (ImageView) convertView
					.findViewById(R.id.await_prize_img);
			viewHolder.await_room_list_item_layout = (LinearLayout) convertView
					.findViewById(R.id.await_room_list_item_layout);
			viewHolder.await_meal_title.setText(list.get(position).packageName
					+ " " + "(" + list.get(position).number + "人)");
			viewHolder.await_room_title.setText("房间"
					+ list.get(position).roomIssue);
			viewHolder.await_perios.setText("(第" + list.get(position).perios
					+ "期)");
			viewHolder.await_prize_name.setText(list.get(position).prizeName);
			viewHolder.await_room_state1.setVisibility(View.VISIBLE);
			long countTime = (list.get(position).startTime - nowTime)
					- (System.currentTimeMillis() - nowTime);
			timecount = new Timecount(countTime, 1000,
					viewHolder.await_room_state1);
			timecount.start();

			ImageLoaderUtil.loadImage(list.get(position).imgUrl,
					viewHolder.await_prize_img);
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels / 2 - 30;
			int height = width - 20;
			LayoutParams params = (LayoutParams) viewHolder.await_room_list_item_layout
					.getLayoutParams();
			params.width = width;
			params.height = height;
			viewHolder.await_room_list_item_layout.setLayoutParams(params);
			viewHolder.await_room_list_item_layout
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									RegistrationDetailActivity.class);
							intent.putExtra("Type", "Await");
							intent.putExtra("RoomId",
									String.valueOf(list.get(position).roomId));
							intent.putExtra("startTime",
									list.get(position).startTime);
							context.startActivity(intent);
						}
					});
		}
		if (getItemViewType(position) == ITEM_TYPE2) {

			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_await_room_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.await_meal_title = (TextView) convertView
						.findViewById(R.id.await_meal_title);
				viewHolder.await_room_title = (TextView) convertView
						.findViewById(R.id.await_room_title);
				viewHolder.await_perios = (TextView) convertView
						.findViewById(R.id.await_perios);
				viewHolder.await_prize_name = (TextView) convertView
						.findViewById(R.id.await_prize_name);
				viewHolder.await_room_state2 = (TextView) convertView
						.findViewById(R.id.await_room_state2);
				viewHolder.await_prize_img = (ImageView) convertView
						.findViewById(R.id.await_prize_img);
				viewHolder.await_room_list_item_layout = (LinearLayout) convertView
						.findViewById(R.id.await_room_list_item_layout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				viewHolder.await_meal_title
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder.await_room_title.setText("房间"
						+ list.get(position).roomIssue);
				viewHolder.await_perios.setText("(第"
						+ list.get(position).perios + "期)");
				viewHolder.await_prize_name
						.setText(list.get(position).prizeName);
				viewHolder.await_room_state2.setVisibility(View.VISIBLE);
				viewHolder.await_room_state2.setText("揭晓时间 "
						+ GetTimeUtil.GetTime3(list.get(position).startTime));
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder.await_prize_img);
				DisplayMetrics dm = new DisplayMetrics();
				context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = dm.widthPixels / 2 - 30;
				int height = width - 20;
				LayoutParams params = (LayoutParams) viewHolder.await_room_list_item_layout
						.getLayoutParams();
				params.width = width;
				params.height = height;
				viewHolder.await_room_list_item_layout.setLayoutParams(params);
				viewHolder.await_room_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										RegistrationDetailActivity.class);
								intent.putExtra("Type", "Await");
								intent.putExtra("RoomId", String.valueOf(list
										.get(position).roomId));
								intent.putExtra("startTime",
										list.get(position).startTime);
								context.startActivity(intent);
							}
						});
			}

		}

		return convertView;
	}

	public class ViewHolder {
		TextView await_room_title, await_meal_title, await_perios,
				await_prize_name, await_room_state1, await_room_state2;
		ImageView await_prize_img;
		LinearLayout await_room_list_item_layout;
	}

	public class Timecount extends CountDownTimer {
		TextView tv;

		public Timecount(long millisInFuture, long countDownInterval,
				TextView tv) {
			super(millisInFuture, countDownInterval);
			this.tv = tv;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv.setText("即将揭晓    " + GetTimeUtil.GetTime2(millisUntilFinished));
		}

		@Override
		public void onFinish() {
			tv.setText("待揭晓    00:00:00");
		}

	}

}
