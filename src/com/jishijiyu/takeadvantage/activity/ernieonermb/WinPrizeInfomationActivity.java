package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.exchangemall.FirmOrderActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.adapter.CheckRegisterInfoAdapter;
import com.jishijiyu.takeadvantage.adapter.WinPrizeHistoryAdapter;
import com.jishijiyu.takeadvantage.entity.request.CheckRegisterInfoRequest;
import com.jishijiyu.takeadvantage.entity.request.JoinedRoomInfoRequest;
import com.jishijiyu.takeadvantage.entity.result.CheckRegisterInfoResult;
import com.jishijiyu.takeadvantage.entity.result.JoinedRoomInfoResult;
import com.jishijiyu.takeadvantage.receiver.MyReceiver;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 中奖信息界面
 * 
 * @author shifeiyu
 * 
 */
public class WinPrizeInfomationActivity extends OneRmbHomeActivity {
	BaseAdapter registedAdapter;
	List<CheckRegisterInfoResult.EnrollList> enrollList;
	int temp = 0, image1 = 0, image2 = 1, image3 = 2, image4 = 3, page = 0;
	JoinedRoomInfoRequest jrRequest;
	JoinedRoomInfoResult jrResult;
	int witch = 0;
	private String roomId, userId, tokenId;
	int prizesize, mealRecordId;
	private AlertDialog dialog;
	private String isCounttype;
	private Intent intent;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.getprize_now_btn:
			//
			// Intent intent = new Intent(mContext, FirmOrderActivity.class);
			// intent.putExtra("type", "getprize");
			// intent.putExtra("mealRecordId", mealRecordId + "");
			// startActivityForResult(intent, 123);

			// 晒图成功并进入奖品领取界面
			dialog = DialogUtils.showShowPrize(mContext, new OnClickListener() {
				private MyReceiver myReceiver;

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.btn_show:
						dialog.dismiss();
						ShareUtil shareUtil = new ShareUtil(
								WinPrizeInfomationActivity.this, mContext);
						shareUtil.postShare();
						myReceiver = new MyReceiver() {
							public void dynamic() {
								Intent intent = new Intent(mContext,
										FirmOrderActivity.class);
								intent.putExtra("type", "getprize");
								intent.putExtra("mealRecordId", mealRecordId
										+ "");
								startActivityForResult(intent, 123);
							};
						};
						IntentFilter dynamic_filter = new IntentFilter();
						dynamic_filter.addAction(Constant.DYNAMICACTION); //

						// 添加动态广播的Action
						registerReceiver(myReceiver, dynamic_filter); // 注册自定义动态广播消息
						break;
					case R.id.btn_cancel:
						dialog.dismiss();
						break;

					default:
						break;
					}

				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 123) {
			System.out.println("resultCode:" + resultCode);
			getprize_now_btn.setBackgroundResource(R.drawable.complete);
			getprize_now_btn.setFocusable(false);
			getprize_now_btn.setClickable(false);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		super.appHead(view);
		top_text.setText("中奖信息");
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		super.initReplaceView();
		listview1.addFooterView(vv);
		reuse_layout5.setVisibility(View.VISIBLE);
		System.out.println("IsCounttype:" + isCounttype);
		userId = GetUserIdUtil.getUserId(mContext);
		tokenId = GetUserIdUtil.getTokenId(mContext);
		Intent intent = getIntent();
		if (intent != null) {
			witch = intent.getIntExtra("witch", 0);
			roomId = intent.getStringExtra("RoomId");
			mealRecordId = intent.getIntExtra("mealRecordId", 0);
		}
		switch (witch) {
		case 1:
			reuse_layout2.setVisibility(View.VISIBLE);
			getprize_now_btn.setOnClickListener(this);
			break;
		case 2:
			reuse_layout1.setVisibility(View.VISIBLE);
			winprize_state.setText("未中奖");
			break;
		case 3:
			reuse_layout1.setVisibility(View.VISIBLE);
			winprize_state.setText("已领取");
			break;
		case 0:
			ToastUtils.makeText(mContext, "错误", 0).show();
			break;
		default:
			break;
		}
		getRoomInfo();
		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 0;
				if (enrollList.size() != 0) {
					enrollList.clear();
				}
				RegisterList();
				listview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				RegisterList();
				listview.onRefreshComplete();
			}

		});
		enrollList = new ArrayList<CheckRegisterInfoResult.EnrollList>();
	}

	// 设置奖品信息
	private void setPrizeInfo() {
		if (prizesize > 0) {
			switch (prizesize) {
			case 1:
				reuse_meal_layout1.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(0).img,
						meal1_prize1_img);
				meal1_prize1_img.setOnClickListener(new prizeOnclick(0));
				meal1_prize1_name.setText(prizeName(0));
				meal1_prize1_text.setText(getGrade(0));
				break;
			case 2:
				reuse_meal_layout2.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(0).img,
						meal2_prize1_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(1).img,
						meal2_prize2_img);
				meal2_prize1_img.setOnClickListener(new prizeOnclick(0));
				meal2_prize2_img.setOnClickListener(new prizeOnclick(1));
				meal2_prize1_name.setText(prizeName(0));
				meal2_prize2_name.setText(prizeName(1));
				meal2_prize1_text.setText(getGrade(0));
				meal2_prize2_text.setText(getGrade(1));
				break;
			case 3:
				reuse_meal_layout3.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(0).img,
						meal3_prize1_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(1).img,
						meal3_prize2_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(2).img,
						meal3_prize3_img);
				meal3_prize1_img.setOnClickListener(new prizeOnclick(0));
				meal3_prize1_name.setText(prizeName(0));
				meal3_prize2_name.setText(prizeName(1));
				meal3_prize3_name.setText(prizeName(2));
				meal3_prize1_text.setText(getGrade(0));
				meal3_prize2_text.setText(getGrade(1));
				meal3_prize3_text.setText(getGrade(2));
				meal3_prize2_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						temp = image1;
						image1 = image2;
						image2 = temp;
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image2).img,
								meal3_prize2_img);
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image1).img,
								meal3_prize1_img);
						meal3_prize1_img.setOnClickListener(new prizeOnclick(
								image1));
						meal3_prize1_text.setText(getGrade(image1));
						meal3_prize2_text.setText(getGrade(image2));
						meal3_prize1_name.setText(prizeName(image1));
						meal3_prize2_name.setText(prizeName(image2));
					}
				});
				meal3_prize3_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						temp = image1;
						image1 = image3;
						image3 = temp;
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image3).img,
								meal3_prize3_img);
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image1).img,
								meal3_prize1_img);
						meal3_prize1_img.setOnClickListener(new prizeOnclick(
								image1));
						meal3_prize1_text.setText(getGrade(image1));
						meal3_prize3_text.setText(getGrade(image3));
						meal3_prize1_name.setText(prizeName(image1));
						meal3_prize3_name.setText(prizeName(image3));
					}
				});

				break;
			case 4:
				reuse_meal_layout4.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(image1).img,
						meal4_prize1_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(image2).img,
						meal4_prize2_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(image3).img,
						meal4_prize3_img);
				ImageLoaderUtil.loadImage(jrResult.p.prizeList.get(image4).img,
						meal4_prize4_img);
				meal4_prize1_img.setOnClickListener(new prizeOnclick(temp));
				meal4_prize1_text.setText(getGrade(0));
				meal4_prize2_text.setText(getGrade(1));
				meal4_prize3_text.setText(getGrade(2));
				meal4_prize4_text.setText(getGrade(3));
				meal4_prize1_name.setText(prizeName(0));
				meal4_prize2_name.setText(prizeName(1));
				meal4_prize3_name.setText(prizeName(2));
				meal4_prize4_name.setText(prizeName(3));
				meal4_prize2_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						temp = image1;
						image1 = image2;
						image2 = temp;
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image2).img,
								meal4_prize2_img);
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image1).img,
								meal4_prize1_img);
						meal4_prize1_img.setOnClickListener(new prizeOnclick(
								image1));
						meal4_prize1_text.setText(getGrade(image1));
						meal4_prize2_text.setText(getGrade(image2));
						meal4_prize1_name.setText(prizeName(image1));
						meal4_prize2_name.setText(prizeName(image2));
					}
				});
				meal4_prize3_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						temp = image1;
						image1 = image3;
						image3 = temp;
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image3).img,
								meal4_prize3_img);
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image1).img,
								meal4_prize1_img);
						meal4_prize1_img.setOnClickListener(new prizeOnclick(
								image1));
						meal4_prize1_text.setText(getGrade(image1));
						meal4_prize3_text.setText(getGrade(image3));
						meal4_prize1_name.setText(prizeName(image1));
						meal4_prize3_name.setText(prizeName(image3));
					}
				});
				meal4_prize4_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						temp = image1;
						image1 = image4;
						image4 = temp;
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image4).img,
								meal4_prize4_img);
						ImageLoaderUtil.loadImage(
								jrResult.p.prizeList.get(image1).img,
								meal4_prize1_img);
						meal4_prize1_img.setOnClickListener(new prizeOnclick(
								image1));
						meal4_prize1_text.setText(getGrade(image1));
						meal4_prize4_text.setText(getGrade(image4));
						meal4_prize1_name.setText(prizeName(image1));
						meal4_prize4_name.setText(prizeName(image4));

					}
				});
				break;

			default:
				break;
			}
		}
	}

	// 获取房间信息
	private void getRoomInfo() {
		final Gson gson = new Gson();
		jrRequest = new JoinedRoomInfoRequest();
		jrRequest.p.roomId = roomId;
		jrRequest.p.userId = userId;
		jrRequest.p.tokenId = tokenId;
		String requestJson = gson.toJson(jrRequest);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				RegisterList();
				if (!TextUtils.isEmpty(result)) {
					jrResult = gson
							.fromJson(result, JoinedRoomInfoResult.class);
					if (jrResult.p.isTrue) {
						if (jrResult.p.prizeList != null) {
							prizesize = jrResult.p.prizeList.size();
							setPrizeInfo();
						}
						if (jrResult.p.dollerHistoryWinningVOList != null) {
							listview.setAdapter(new WinPrizeHistoryAdapter(
									WinPrizeInfomationActivity.this,
									jrResult.p.dollerHistoryWinningVOList));

							reuse_title2.setText("第"
									+ jrResult.p.dollerRoom.mealPerios + "期");
							reuse_title3.setText("开奖时间:"
									+ GetTimeUtil
											.GetTime(jrResult.p.dollerRoom.beginTime));
						}

						taocan_title.setText(jrResult.p.meal.mealName
								+ "("
								+ GetMealCountUtil
										.GetMealCount(jrResult.p.meal.mealType)
								+ "人)");
						top_text.setText("房间" + jrResult.p.dollerRoom.roomIssue);
					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
					}

				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "连接服务器失败", 0).show();
				RegisterList();
			}
		});
	}

	// 获取报名者信息
	private void RegisterList() {
		final Gson gson = new Gson();
		CheckRegisterInfoRequest checkRequest = new CheckRegisterInfoRequest();
		checkRequest.p.page = page + "";
		checkRequest.p.pageSize = "20";
		checkRequest.p.tokenId = tokenId;
		checkRequest.p.userId = userId;
		checkRequest.p.roomId = roomId;
		String json = gson.toJson(checkRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					CheckRegisterInfoResult checkResult = gson.fromJson(result,
							CheckRegisterInfoResult.class);
					if (checkResult.p.isTrue) {
						if (checkResult.p.enrollList.size() > 0) {
							for (int i = 0; i < checkResult.p.enrollList.size(); i++) {
								enrollList.add(checkResult.p.enrollList.get(i));
							}
							if (page == 0) {
								registedAdapter = new CheckRegisterInfoAdapter(
										WinPrizeInfomationActivity.this,
										enrollList);
								registed_listview.setAdapter(registedAdapter);
							} else {
								registedAdapter.notifyDataSetChanged();
							}

						}

					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
					}
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "连接服务器失败", 0).show();
			}
		});
	}

	// 奖品点击事件
	public class prizeOnclick implements OnClickListener {
		int position;

		public prizeOnclick(int witch) {
			this.position = witch;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, TaoCanPrizeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("companyId",
					String.valueOf(getCompanyId(position)));
			bundle.putString("prizeImg", jrResult.p.prizeList.get(position).img);
			bundle.putString("prizePrice",
					jrResult.p.prizeList.get(position).price + "");
			bundle.putString("prizeName",
					jrResult.p.prizeList.get(position).name);
			bundle.putString("prizeExplain",
					jrResult.p.prizeList.get(position).explain);
			bundle.putString("prizeId", jrResult.p.prizeList.get(position).id
					+ "");
			bundle.putString("packageId", jrResult.p.meal.id + "");
			bundle.putString("companyName",
					jrResult.p.prizeList.get(position).companyName);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 获取奖品名称
	private String prizeName(int position) {
		String prizeName = null;
		prizeName = jrResult.p.prizeList.get(position).name;
		return prizeName;

	}

	// 获取奖级
	private String getGrade(int position) {
		String gradeName = "";
		switch (jrResult.p.prizeList.get(position).grade) {
		case 1:
			gradeName = "一等奖";
			break;
		case 2:
			gradeName = "二等奖";
			break;
		case 3:
			gradeName = "三等奖";
			break;
		case 4:
			gradeName = "四等奖";
			break;

		}
		return gradeName;
	}

	// 获取商家ID
	private int getCompanyId(int position) {
		int CompanyId;
		CompanyId = jrResult.p.prizeList.get(position).companyId;
		return CompanyId;

	}

}
