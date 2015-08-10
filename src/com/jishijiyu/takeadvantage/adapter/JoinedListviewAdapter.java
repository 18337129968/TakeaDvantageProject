package com.jishijiyu.takeadvantage.adapter;

import java.text.DecimalFormat;
import java.util.List;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ernieonermb.RegistrationDetailActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.WinPrizeInfomationActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.YiYuanErnieActivity;
import com.jishijiyu.takeadvantage.entity.result.AboutPaiDeLi;
import com.jishijiyu.takeadvantage.entity.result.JoinedRoomListResult;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 已参与房间列表listadapter
 * 
 * @author shifeiyu
 * 
 */
public class JoinedListviewAdapter extends MyBaseAdapter {
	ShareUtil share;
	List<JoinedRoomListResult.roomOnMealVOList> list;
	Activity context;
	public static final int ITEM_TYPE1 = 1; // 组团中
	public static final int ITEM_TYPE2 = 2; // 待开奖
	public static final int ITEM_TYPE3 = 3; // 已中奖 未领取
	public static final int ITEM_TYPE4 = 4; // 已中奖 已领取
	public static final int ITEM_TYPE5 = 5; // 未中奖
	public static final int ITEM_TYPE6 = 6; // 摇奖中
	public long nowTime;

	public JoinedListviewAdapter(Activity context,
			List<JoinedRoomListResult.roomOnMealVOList> list, long nowTime) {
		super(context, list);
		this.context = context;
		this.list = list;
		this.nowTime = nowTime;
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
	public int getItemViewType(int position) {
		if (list.get(position).roomState == 1) {
			return ITEM_TYPE1;
		}
		if (list.get(position).roomState == 2) {
			return ITEM_TYPE2;
		}
		if (list.get(position).roomState == 3) {
			return ITEM_TYPE6;
		}
		if (list.get(position).userState == 1) {
			return ITEM_TYPE3;
		}
		if (list.get(position).userState == 2) {
			return ITEM_TYPE4;
		} else {
			return ITEM_TYPE5;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 1000;
	}

	@Override
	public View initView(final int position, View convertView) {
		Type1ViewHolder viewHolder1;
		Type2ViewHolder viewHolder2;
		Type3ViewHolder viewHolder3;
		Type4ViewHolder viewHolder4;
		Type5ViewHolder viewHolder5;
		if (getItemViewType(position) == ITEM_TYPE1) {
			if (convertView == null) {
				viewHolder1 = new Type1ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_one_rmb_ernie_joined_item1, null);
				viewHolder1.type1_img = (ImageView) convertView
						.findViewById(R.id.type1_img);
				viewHolder1.type1_textView1 = (TextView) convertView
						.findViewById(R.id.type1_textView1);
				viewHolder1.type1_textView2 = (TextView) convertView
						.findViewById(R.id.type1_textView2);
				viewHolder1.type1_textView3 = (TextView) convertView
						.findViewById(R.id.type1_textView3);
				viewHolder1.type1_join_home_btn = (TextView) convertView
						.findViewById(R.id.type1_join_home_btn);
				viewHolder1.type1_progressBar1 = (ProgressBar) convertView
						.findViewById(R.id.type1_progressBar1);
				viewHolder1.type1_progress_text = (TextView) convertView
						.findViewById(R.id.type1_progress_text);
				convertView.setTag(viewHolder1);
			} else {
				viewHolder1 = (Type1ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder1.type1_img);
				viewHolder1.type1_textView1.setText("房间"
						+ list.get(position).roomIssue);
				viewHolder1.type1_textView2.setText("第"
						+ list.get(position).perios + "期");
				viewHolder1.type1_textView3
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder1.type1_progressBar1
						.setMax(list.get(position).number);
				viewHolder1.type1_progressBar1
						.setProgress(list.get(position).countNumber);
				float progressNum, mealNum, joinNum;
				mealNum = list.get(position).number;
				joinNum = list.get(position).countNumber;
				progressNum = joinNum / mealNum;
				DecimalFormat df = new DecimalFormat("0%");
				viewHolder1.type1_progress_text.setText("开奖进度:"
						+ df.format(progressNum));
				viewHolder1.type1_join_home_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										RegistrationDetailActivity.class);
								intent.putExtra("RoomId", String.valueOf(list
										.get(position).roomId));
								intent.putExtra("Type", "Registed");
								context.startActivity(intent);
							}
						});
			}
		}
		if (getItemViewType(position) == ITEM_TYPE2) {
			viewHolder2 = new Type2ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_one_rmb_ernie_joined_item2, null);
			viewHolder2.type2_imageView1 = (ImageView) convertView
					.findViewById(R.id.type2_imageView1);
			viewHolder2.type2_join_ernie_btn = (TextView) convertView
					.findViewById(R.id.type2_join_ernie_btn);
			viewHolder2.type2_textView1 = (TextView) convertView
					.findViewById(R.id.type2_textView1);
			viewHolder2.type2_textView2 = (TextView) convertView
					.findViewById(R.id.type2_textView2);
			viewHolder2.type2_textView3 = (TextView) convertView
					.findViewById(R.id.type2_textView3);
			viewHolder2.type2_textView5 = (TextView) convertView
					.findViewById(R.id.type2_textView5);
			ImageLoaderUtil.loadImage(list.get(position).imgUrl,
					viewHolder2.type2_imageView1);
			viewHolder2.type2_textView1.setText("房间"
					+ list.get(position).roomIssue);
			viewHolder2.type2_textView2.setText("第" + list.get(position).perios
					+ "期");
			viewHolder2.type2_textView3.setText(list.get(position).packageName
					+ " " + "(" + list.get(position).number + "人)");

			long countTime = (list.get(position).startTime - nowTime)
					- (System.currentTimeMillis() - nowTime);
			new Timecount(countTime, 1000, viewHolder2.type2_textView5).start();

			viewHolder2.type2_join_ernie_btn.setText("进入房间");
			viewHolder2.type2_join_ernie_btn
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									RegistrationDetailActivity.class);
							intent.putExtra("RoomId", list.get(position).roomId
									+ "");
							intent.putExtra("Type", "RegistedOk");
							intent.putExtra(
									"countDownTime",
									Math.abs(list.get(position).startTime
											- nowTime));
							intent.putExtra("startTime",
									list.get(position).startTime);
							context.startActivity(intent);
						}
					});
		}
		if (getItemViewType(position) == ITEM_TYPE3) {
			if (convertView == null) {
				viewHolder3 = new Type3ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_one_rmb_ernie_joined_item3, null);
				viewHolder3.type3_imageView1 = (ImageView) convertView
						.findViewById(R.id.type3_imageView1);
				viewHolder3.type3_getprize_btn = (TextView) convertView
						.findViewById(R.id.type3_getprize_btn);
				viewHolder3.type3_textView3 = (TextView) convertView
						.findViewById(R.id.type3_textView3);
				viewHolder3.type3_textView4 = (TextView) convertView
						.findViewById(R.id.type3_textView4);
				viewHolder3.type3_textView5 = (TextView) convertView
						.findViewById(R.id.type3_textView5);
				viewHolder3.type3_textView6 = (TextView) convertView
						.findViewById(R.id.type3_textView6);
				viewHolder3.type3_textView7 = (TextView) convertView
						.findViewById(R.id.type3_textView7);
				convertView.setTag(viewHolder3);
			} else {
				viewHolder3 = (Type3ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder3.type3_imageView1);
				viewHolder3.type3_textView3.setText(GetMealCountUtil
						.GetGrade(list.get(position).grade));
				viewHolder3.type3_textView4.setText("第"
						+ list.get(position).perios + "期");
				viewHolder3.type3_textView5
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder3.type3_textView6
						.setText(list.get(position).prizeName);
				// 分享/领取
				viewHolder3.type3_getprize_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// share = new ShareUtil(context, context);
								// share.postShare();
								Intent intent = new Intent(context,
										WinPrizeInfomationActivity.class);
								intent.putExtra("witch", 1);
								intent.putExtra("RoomId",
										list.get(position).roomId + "");
								intent.putExtra("mealRecordId",
										list.get(position).mealRecordId);
								context.startActivity(intent);
							}
						});
			}
		}
		if (getItemViewType(position) == ITEM_TYPE4) {
			if (convertView == null) {
				viewHolder4 = new Type4ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_one_rmb_ernie_joined_item4, null);
				viewHolder4.type4_imageView1 = (ImageView) convertView
						.findViewById(R.id.type4_imageView1);
				viewHolder4.type4_textView3 = (TextView) convertView
						.findViewById(R.id.type4_textView3);
				viewHolder4.type4_textView4 = (TextView) convertView
						.findViewById(R.id.type4_textView4);
				viewHolder4.type4_textView5 = (TextView) convertView
						.findViewById(R.id.type4_textView5);
				viewHolder4.type4_textView6 = (TextView) convertView
						.findViewById(R.id.type4_textView6);
				viewHolder4.type4_gotprize_layout = (LinearLayout) convertView
						.findViewById(R.id.type4_gotprize_layout);
				convertView.setTag(viewHolder4);
			} else {
				viewHolder4 = (Type4ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder4.type4_imageView1);
				viewHolder4.type4_textView3.setText(GetMealCountUtil
						.GetGrade(list.get(position).grade));
				viewHolder4.type4_textView4.setText("第"
						+ list.get(position).perios + "期");
				viewHolder4.type4_textView5
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder4.type4_textView6
						.setText(list.get(position).prizeName);
				// 已领取layout点击
				viewHolder4.type4_gotprize_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										WinPrizeInfomationActivity.class);
								intent.putExtra("witch", 3);
								intent.putExtra("RoomId",
										list.get(position).roomId + "");
								context.startActivity(intent);
							}
						});
			}
		}
		if (getItemViewType(position) == ITEM_TYPE5) {
			if (convertView == null) {
				viewHolder5 = new Type5ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_one_rmb_ernie_joined_item5, null);
				viewHolder5.type5_imageView1 = (ImageView) convertView
						.findViewById(R.id.type5_imageView1);
				viewHolder5.type5_textView4 = (TextView) convertView
						.findViewById(R.id.type5_textView4);
				viewHolder5.type5_textView5 = (TextView) convertView
						.findViewById(R.id.type5_textView5);
				viewHolder5.type5_textView6 = (TextView) convertView
						.findViewById(R.id.type5_textView6);
				viewHolder5.type5_not_prize_layout = (LinearLayout) convertView
						.findViewById(R.id.type5_not_prize_layout);
				convertView.setTag(viewHolder5);
			} else {
				viewHolder5 = (Type5ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder5.type5_imageView1);
				viewHolder5.type5_textView4.setText("第"
						+ list.get(position).perios + "期");
				viewHolder5.type5_textView5
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder5.type5_textView6
						.setText(list.get(position).prizeName);
				// 未中奖layout点击
				viewHolder5.type5_not_prize_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										WinPrizeInfomationActivity.class);
								intent.putExtra("witch", 2);
								intent.putExtra("RoomId",
										list.get(position).roomId + "");
								context.startActivity(intent);
							}
						});
			}
		}
		if (getItemViewType(position) == ITEM_TYPE6) {
			if (convertView == null) {
				viewHolder2 = new Type2ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_one_rmb_ernie_joined_item2, null);
				viewHolder2.type2_imageView1 = (ImageView) convertView
						.findViewById(R.id.type2_imageView1);
				viewHolder2.type2_join_ernie_btn = (TextView) convertView
						.findViewById(R.id.type2_join_ernie_btn);
				viewHolder2.type2_textView1 = (TextView) convertView
						.findViewById(R.id.type2_textView1);
				viewHolder2.type2_textView2 = (TextView) convertView
						.findViewById(R.id.type2_textView2);
				viewHolder2.type2_textView3 = (TextView) convertView
						.findViewById(R.id.type2_textView3);
				viewHolder2.type2_textView4 = (TextView) convertView
						.findViewById(R.id.type2_textView4);
				viewHolder2.type2_textView5 = (TextView) convertView
						.findViewById(R.id.type2_textView5);
				viewHolder2.type2_textView6 = (TextView) convertView
						.findViewById(R.id.type2_textView6);
				convertView.setTag(viewHolder2);
			} else {
				viewHolder2 = (Type2ViewHolder) convertView.getTag();
			}
			if (convertView != null) {
				ImageLoaderUtil.loadImage(list.get(position).imgUrl,
						viewHolder2.type2_imageView1);
				viewHolder2.type2_textView1.setText("房间"
						+ list.get(position).roomIssue);
				viewHolder2.type2_textView2.setText("第"
						+ list.get(position).perios + "期");
				viewHolder2.type2_textView3
						.setText(list.get(position).packageName + " " + "("
								+ list.get(position).number + "人)");
				viewHolder2.type2_textView4.setVisibility(View.GONE);
				viewHolder2.type2_textView5.setText("正在进行摇奖");
				viewHolder2.type2_textView6.setText("摇奖中");
				viewHolder2.type2_join_ernie_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										YiYuanErnieActivity.class);
								intent.putExtra("RoomId",
										list.get(position).roomId + "");

								context.startActivity(intent);
							}
						});
			}
		}
		return convertView;
	}

	public class Type1ViewHolder {
		ImageView type1_img;
		TextView type1_textView1, type1_textView2, type1_textView3,
				type1_join_home_btn, type1_progress_text;
		ProgressBar type1_progressBar1;
	}

	public class Type2ViewHolder {
		ImageView type2_imageView1;
		TextView type2_textView1, type2_textView2, type2_textView3,
				type2_textView4, type2_textView5, type2_textView6,
				type2_join_ernie_btn;
	}

	public class Type3ViewHolder {
		ImageView type3_imageView1;
		TextView type3_textView3, type3_textView4, type3_textView7,
				type3_textView5, type3_textView6, type3_getprize_btn;
	}

	public class Type4ViewHolder {
		ImageView type4_imageView1;
		TextView type4_textView3, type4_textView4, type4_textView5,
				type4_textView6;
		LinearLayout type4_gotprize_layout;
	}

	public class Type5ViewHolder {
		ImageView type5_imageView1;
		TextView type5_textView4, type5_textView5, type5_textView6;
		LinearLayout type5_not_prize_layout;
	}

	class Timecount extends CountDownTimer {
		TextView tv;

		public Timecount(long millisInFuture, long countDownInterval,
				TextView tv) {
			super(millisInFuture, countDownInterval);
			this.tv = tv;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv.setText(GetTimeUtil.GetTime2(millisUntilFinished));
		}

		@Override
		public void onFinish() {
			tv.setText("00:00:00");
		}

	}

}
