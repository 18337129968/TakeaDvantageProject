package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayMoneyActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.adapter.RegistrationDetailsListAdapter;
import com.jishijiyu.takeadvantage.entity.request.AddListRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomBillRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomDetailsRequest;
import com.jishijiyu.takeadvantage.entity.result.AddListResult;
import com.jishijiyu.takeadvantage.entity.result.RoomBillResult;
import com.jishijiyu.takeadvantage.entity.result.RoomDetailsResult;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetMealCountUtil;
import com.jishijiyu.takeadvantage.utils.GetTimeUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 报名详情界面
 * 
 * @author shifeiyu
 * 
 */
public class RegistrationDetailActivity extends OneRmbHomeActivity {
	int temp = 0, image1 = 0, image2 = 1, image3 = 2, image4 = 3;
	RoomDetailsResult rdResult;
	RoomDetailsResult.dollerEnrollVO vo;
	List<RoomDetailsResult.enrollList> list;
	public static int w, h;
	RoomDetailsRequest rdRequest;
	BaseAdapter roomDetailAdapter;
	Dialog dialog;
	TextView registration_need_text, registration_surpuls_text,
			registration_state_text, registration_reuse_btn, join_list_btn;
	private Intent intent1;
	String RoomId = null, UserId = null, TokenId = null, packageId = null;
	int prizesize, page = 0, mealNum, joinedNum, TYPE;
	long countDownTime, registedTime, startTime, nowTime;
	Message msg;
	private final int NotRegistration = 1;
	private final int Registed = 2;
	private final int RegistedOk = 3;
	private final int Await = 4;
	boolean sendBroadCast = false;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getListInfo();
				break;
			case 2:

				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 清单
		case R.id.btn_top_right2:
			Intent intent = new Intent(RegistrationDetailActivity.this,
					RoomListActivity.class);
			startActivity(intent);
			break;
		case R.id.registration_reuse_btn:

			if (registration_reuse_btn.getText().equals("加入房间")) {
				AddRoom();
				break;
			}
			if (registration_reuse_btn.getText().equals("邀请好友")) {
				startForActivity(mContext, InviteFriendsActivity.class, RoomId);
				break;
			}
			break;
		// 托管 按钮
		case R.id.btn_top_right:
			dialog = DialogUtils.showTrusteeshipDialog(mContext,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.trusteeship_ok_btn:
								if (DialogUtils.flag) {
									dialog.dismiss();
									// 实现托管请求
									getRequestData("1");
								} else {
									btn_right.setText("托管");
									dialog.dismiss();
									getRequestData("2");
								}
								btn_right.setText("已托管");
								break;

							default:
								break;
							}
						}
					});
			break;
		// 加入清单 按钮
		case R.id.join_list_btn:
			AddList();
			break;
		// 进入摇奖
		case R.id.join_to_ernie_btn:
			Intent intentToyiyuan = new Intent(mContext,
					YiYuanErnieActivity.class);
			intentToyiyuan.putExtra("RoomId", RoomId);
			startActivity(intentToyiyuan);
			break;
		default:
			break;
		}
	}

	public void getRequestData(String lock) {
		TuoGuanRequest tuoGuanRequest = new TuoGuanRequest();
		tuoGuanRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		tuoGuanRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		// RoomId = intent1.getStringExtra("RoomId");
		tuoGuanRequest.p.roomId = intent1.getStringExtra("RoomId");
		tuoGuanRequest.p.isLock = lock;
		final Gson gson = new Gson();
		String jsonString = gson.toJson(tuoGuanRequest);
		System.out.println("jsonString:" + jsonString);
		HttpConnectTool.update(jsonString, mContext, new ConnectListener() {

			@SuppressLint("ShowToast")
			@Override
			public void contectSuccess(String result) {
				System.out.println("托管界面result：" + result);
				if (result != null && !TextUtils.isEmpty(result)) {
					TuoGuanResponse tuoGuanResponse = gson.fromJson(result,
							TuoGuanResponse.class);
					boolean isTrue = tuoGuanResponse.p.isTrue;
					boolean isSuccess = tuoGuanResponse.p.isSucce;
					System.out.println("isTrue:" + isTrue);
					System.out.println("isSuccess:" + isSuccess);
					if (isTrue) {
						if (isSuccess) {
							System.out.println("恭喜你，托管成功");
							Toast.makeText(mContext, "恭喜你，托管成功", 1).show();
						}
					} else {
						Toast.makeText(mContext, "用户id失效，请重新登录", 1).show();
					}
				}

			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void appHead(View view) {
		super.appHead(view);
		top_text.setText("房间1");
		btn_right.setOnClickListener(this);

	}

	@Override
	public void initReplaceView() {
		super.initReplaceView();
		reuse_title1.setVisibility(View.GONE);
		registration_surpuls_text = (TextView) findViewById(R.id.registration_surpuls_text);
		registration_state_text = (TextView) findViewById(R.id.registration_state_text);
		registration_reuse_btn = (TextView) findViewById(R.id.registration_reuse_btn);
		join_list_btn = (TextView) findViewById(R.id.join_list_btn);
		join_list_btn.setOnClickListener(this);
		registration_reuse_btn.setOnClickListener(this);
		UserId = GetUserIdUtil.getUserId(mContext);
		TokenId = GetUserIdUtil.getTokenId(mContext);
		intent1 = getIntent();
		if (intent1 != null) {
			// 大厅跳转界面
			if (intent1.getStringExtra("Type").equals("NotRegistration")) {
				RoomId = intent1.getStringExtra("RoomId");
				TYPE = NotRegistration;
				// NotRegistration();
				getRoomInfo();
			}
			// 已参与跳转界面
			if (intent1.getStringExtra("Type").equals("Registed")) {
				RoomId = intent1.getStringExtra("RoomId");
				TYPE = Registed;
				Registed();
			}
			// 已报名跳转界面
			if (intent1.getStringExtra("Type").equals("RegistedOk")) {
				RoomId = intent1.getStringExtra("RoomId");
				TYPE = RegistedOk;
				startTime = intent1.getLongExtra("startTime", 0);
				IsregistedOk();
			}
			// 待揭晓跳转界面
			if (intent1.getStringExtra("Type").equals("Await")) {
				RoomId = intent1.getStringExtra("RoomId");
				TYPE = Await;
				startTime = intent1.getLongExtra("startTime", 0);
				Await();
			}

		}
		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 0;
				if (list.size() != 0) {
					list.clear();
				}
				getRoomInfo();
				listview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				getRoomInfo();
				listview.onRefreshComplete();
			}
		});

		list = new ArrayList<RoomDetailsResult.enrollList>();
		// roomDetailAdapter = new RegistrationDetailsListAdapter(
		// RegistrationDetailActivity.this, vo, list);
		// listview.setAdapter(roomDetailAdapter);

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	// 设置奖品信息
	private void setPrizeInfo() {
		if (prizesize > 0) {
			switch (prizesize) {
			case 1:
				reuse_meal_layout1.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(0).img,
						meal1_prize1_img);
				meal1_prize1_img.setOnClickListener(new prizeOnclick(0));
				meal1_prize1_name.setText(prizeName(0));
				meal1_prize1_text.setText(getGrade(0));
				break;
			case 2:
				reuse_meal_layout2.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(0).img,
						meal2_prize1_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(1).img,
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
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(0).img,
						meal3_prize1_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(1).img,
						meal3_prize2_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(2).img,
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
								rdResult.p.prizeList.get(image2).img,
								meal3_prize2_img);
						ImageLoaderUtil.loadImage(
								rdResult.p.prizeList.get(image1).img,
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
								rdResult.p.prizeList.get(image3).img,
								meal3_prize3_img);
						ImageLoaderUtil.loadImage(
								rdResult.p.prizeList.get(image1).img,
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
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(image1).img,
						meal4_prize1_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(image2).img,
						meal4_prize2_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(image3).img,
						meal4_prize3_img);
				ImageLoaderUtil.loadImage(rdResult.p.prizeList.get(image4).img,
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
								rdResult.p.prizeList.get(image2).img,
								meal4_prize2_img);
						ImageLoaderUtil.loadImage(
								rdResult.p.prizeList.get(image1).img,
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
								rdResult.p.prizeList.get(image3).img,
								meal4_prize3_img);
						ImageLoaderUtil.loadImage(
								rdResult.p.prizeList.get(image1).img,
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
								rdResult.p.prizeList.get(image4).img,
								meal4_prize4_img);
						ImageLoaderUtil.loadImage(
								rdResult.p.prizeList.get(image1).img,
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

	// 未参与
	private void NotRegistration() {
		reuse_layout3.setVisibility(View.VISIBLE);
		join_list_btn.setVisibility(View.VISIBLE);
		registration_reuse_btn.setText("加入房间");
		reuse_title2.setText("当前报名详情");
		btn_right2.setVisibility(View.VISIBLE);
		btn_right2.setOnClickListener(this);

	}

	// 已报名,待开奖
	private void IsregistedOk() {
		reuse_layout4.setVisibility(View.VISIBLE);
		registed_ok_text1.setVisibility(View.VISIBLE);
		btn_right.setText("托管");
		btn_right.setVisibility(View.VISIBLE);
		getRoomInfo();
		countDownTime = startTime - nowTime;
		new Timecount(countDownTime, 1000, stay_reward_text).start();
		reward_text.setText("开奖时间:" + GetTimeUtil.GetTime(startTime));
		join_to_ernie_btn.setOnClickListener(this);
	}

	// 已参与
	private void Registed() {
		registration_reuse_btn.setVisibility(View.GONE);
		reuse_layout3.setVisibility(View.VISIBLE);
		registration_state_text.setVisibility(View.VISIBLE);
		reuse_title2.setText("当前报名详情");
		btn_right.setText("托管");
		btn_right.setVisibility(View.VISIBLE);
		getRoomInfo();
	}

	// 已参与 （大厅跳转）
	private void RegistedForHome() {
		registration_reuse_btn.setVisibility(View.GONE);
		reuse_layout3.setVisibility(View.VISIBLE);
		registration_state_text.setVisibility(View.VISIBLE);
		reuse_title2.setText("当前报名详情");
		btn_right.setText("托管");
		btn_right.setVisibility(View.VISIBLE);
	}

	// 待揭晓
	private void Await() {
		reuse_layout4.setVisibility(View.VISIBLE);
		reuse_title2.setText("当前报名详情");
		getRoomInfo();
		countDownTime = startTime - nowTime;
		System.out.println("countDownTime" + countDownTime + "startTime"
				+ startTime + "nowTime" + nowTime);
		new Timecount(countDownTime, 1000, stay_reward_text).start();
		reward_text.setText("开奖时间:" + GetTimeUtil.GetTime(startTime));
		join_to_ernie_btn.setVisibility(View.INVISIBLE);
	}

	// 获取房间信息
	private void getRoomInfo() {
		final Gson gson = new Gson();
		rdRequest = new RoomDetailsRequest();
		rdRequest.p.roomId = RoomId;
		rdRequest.p.userId = UserId;
		rdRequest.p.tokenId = TokenId;
		rdRequest.p.page = page + "";
		rdRequest.p.pageSize = "10";
		String requestJson = gson.toJson(rdRequest);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (TYPE == NotRegistration) {
					getListInfo();
				}
				if (!TextUtils.isEmpty(result)) {
					rdResult = gson.fromJson(result, RoomDetailsResult.class);
					nowTime = rdResult.p.nowTime;
					if (rdResult.p.isTrue) {

						if (rdResult.p.prizeList.size() > 0) {
							prizesize = rdResult.p.prizeList.size();
							System.out.println(prizesize);
							setPrizeInfo();
						}
						if (rdResult.p.dollerEnrollVO == null
								&& rdResult.p.enrollList == null) {

						} else {
							vo = rdResult.p.dollerEnrollVO;
							for (int i = 0; i < rdResult.p.enrollList.size(); i++) {
								list.add(rdResult.p.enrollList.get(i));
							}
							if (page == 0) {
								roomDetailAdapter = new RegistrationDetailsListAdapter(
										RegistrationDetailActivity.this, vo,
										list);
								listview.setAdapter(roomDetailAdapter);
							} else {
								roomDetailAdapter.notifyDataSetChanged();
							}

						}
						if (rdResult.p.meal != null
								&& rdResult.p.dollerRoom != null) {
							mealNum = GetMealCountUtil
									.GetMealCount(rdResult.p.meal.mealType);
							joinedNum = rdResult.p.dollerRoom.joinNumber;
							taocan_title.setText(rdResult.p.meal.mealName
									+ "("
									+ GetMealCountUtil
											.GetMealCount(rdResult.p.meal.mealType)
									+ "人)");
							registration_surpuls_text.setText("剩余人数："
									+ (mealNum - joinedNum));
							top_text.setText("房间"
									+ rdResult.p.dollerRoom.roomIssue);
							reuse_title3.setText("当前报名人数："
									+ rdResult.p.dollerRoom.joinNumber);
						}
						if (TYPE == NotRegistration) {
							if (rdResult.p.isDollerEnroll) {
								RegistedForHome();
							} else {
								NotRegistration();
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
				if (TYPE == NotRegistration) {
					getListInfo();
				}
			}
		});
	}

	// 加入清单
	private void AddList() {
		final Gson gson = new Gson();
		AddListRequest request = new AddListRequest();
		request.p.roomId = RoomId;
		request.p.userId = UserId;
		request.p.tokenId = TokenId;
		String requestJson = gson.toJson(request);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					AddListResult addResult = gson.fromJson(result,
							AddListResult.class);
					if (addResult.p.isTrue) {
						switch (addResult.p.type) {
						case 0:
							sendBroadCast = true;
							msg = msg.obtain();
							msg.what = 1;
							handler.sendMessage(msg);
							ToastUtils.makeText(mContext, "已加入清单", 0).show();
							join_list_btn.setText("已加入清单");
							join_list_btn
									.setBackgroundResource(R.drawable.btn_reget_code);
							join_list_btn.setClickable(false);
							break;
						case 1:
							join_list_btn.setText("已加入清单");
							join_list_btn
									.setBackgroundResource(R.drawable.btn_reget_code);
							join_list_btn.setClickable(false);
							ToastUtils.makeText(mContext, "已加入清单\n不可重复添加", 0)
									.show();
							break;
						case 2:
							ToastUtils.makeText(mContext, "加入清单失败", 0).show();
							break;

						default:
							break;
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
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();

			}
		});
	}

	// 加入房间
	private void AddRoom() {
		final Gson gson = new Gson();
		AddListRequest request = new AddListRequest();
		request.p.roomId = RoomId;
		request.p.userId = UserId;
		request.p.tokenId = TokenId;
		String requestJson = gson.toJson(request);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					AddListResult addResult = gson.fromJson(result,
							AddListResult.class);
					if (addResult.p.isTrue) {
						switch (addResult.p.type) {
						case 0:
							sendBroadCast = true;
							msg = msg.obtain();
							msg.what = 3;
							handler.sendMessage(msg);
							Intent intent2 = new Intent();
							Bundle bundle = new Bundle();
							intent2.setClass(RegistrationDetailActivity.this,
									PayMoneyActivity.class);
							bundle.putString("RoomId", rdResult.p.meal.id + "");
							bundle.putString("UserId", UserId);
							bundle.putString("TokenId", TokenId);
							intent2.putExtras(bundle);
							startActivity(intent2);
							RegistrationDetailActivity.this.finish();
							break;
						case 1:
							ToastUtils.makeText(mContext, "已加入房间\n不可重复添加", 0)
									.show();
							break;
						case 2:
							ToastUtils.makeText(mContext, "加入房间失败", 0).show();
							break;

						default:
							break;
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
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();

			}
		});
	}

	// 获取清单信息
	public void getListInfo() {
		final Gson gson = new Gson();
		RoomBillRequest billrequest = new RoomBillRequest();
		billrequest.p.userId = GetUserIdUtil.getUserId(mContext);
		billrequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String json = gson.toJson(billrequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (sendBroadCast) {
					Intent intent = new Intent("UpdateList");
					sendBroadcast(intent);
				}
				if (!TextUtils.isEmpty(result)) {
					RoomBillResult billresult = gson.fromJson(result,
							RoomBillResult.class);
					if (billresult.p.InventoryVOList != null
							&& billresult.p.InventoryVOList.size() > 0) {
						btn_right_num.setVisibility(View.VISIBLE);
						btn_right_num.setText(billresult.p.InventoryVOList
								.size() + "");
						for (int i = 0; i < billresult.p.InventoryVOList.size(); i++) {
							if (billresult.p.InventoryVOList.get(i).dollerRoomId == Integer
									.parseInt(RoomId)) {
								join_list_btn.setText("已加入清单");
								join_list_btn
										.setBackgroundResource(R.drawable.btn_reget_code);
								join_list_btn.setClickable(false);
							}
						}
					} else {
						btn_right_num.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				if (sendBroadCast) {
					Intent intent = new Intent("UpdateList");
					sendBroadcast(intent);
				}
				ToastUtils.makeText(mContext, "获取清单信息失败", 0).show();
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
			bundle.putString("prizeImg", rdResult.p.prizeList.get(position).img);
			bundle.putString("prizePrice",
					rdResult.p.prizeList.get(position).price + "");
			bundle.putString("prizeName",
					rdResult.p.prizeList.get(position).name);
			bundle.putString("prizeExplain",
					rdResult.p.prizeList.get(position).explain);
			bundle.putString("prizeId", rdResult.p.prizeList.get(position).id
					+ "");
			bundle.putString("packageId", rdResult.p.meal.id + "");
			bundle.putString("companyName",
					rdResult.p.prizeList.get(position).companyName);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 获取奖品名称
	private String prizeName(int position) {
		String prizeName = null;
		prizeName = rdResult.p.prizeList.get(position).name;
		return prizeName;

	}

	// 获取奖级
	private String getGrade(int position) {
		String gradeName = "";
		switch (rdResult.p.prizeList.get(position).grade) {
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
		CompanyId = rdResult.p.prizeList.get(position).companyId;
		return CompanyId;

	}

	// 倒计时
	class Timecount extends CountDownTimer {
		TextView tv;

		public Timecount(long millisInFuture, long countDownInterval,
				TextView tv) {
			super(millisInFuture, countDownInterval);
			this.tv = tv;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv.setText("待开奖:" + GetTimeUtil.GetTime2(millisUntilFinished));
		}

		@Override
		public void onFinish() {
			tv.setText("待开奖: 00:00:00");
		}

	}
}
