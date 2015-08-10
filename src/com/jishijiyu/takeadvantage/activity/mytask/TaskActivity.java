package com.jishijiyu.takeadvantage.activity.mytask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ShowGoldAnimActivity;
import com.jishijiyu.takeadvantage.activity.ernie.MyPriceActivity;
import com.jishijiyu.takeadvantage.activity.ernie.ShowPriceActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.ExchangemallActivity;
import com.jishijiyu.takeadvantage.activity.makemoney.EarnPointsActivity;
import com.jishijiyu.takeadvantage.activity.my.FriendRequestActivity;
import com.jishijiyu.takeadvantage.activity.my.FriendRequestFragmentActivity;
import com.jishijiyu.takeadvantage.activity.myinformation.MyBasicInfomationActivity;
import com.jishijiyu.takeadvantage.activity.news.MyMessageActivity;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.OpenBoxRequest;
import com.jishijiyu.takeadvantage.entity.request.RequestAllodsGold;
import com.jishijiyu.takeadvantage.entity.request.RequestAllodsGold.RequestSonData;
import com.jishijiyu.takeadvantage.entity.request.RequestTaskList;
import com.jishijiyu.takeadvantage.entity.request.RequestTaskList.TaskSonData;
import com.jishijiyu.takeadvantage.entity.result.AllodsGoldResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.OpenBoxResult;
import com.jishijiyu.takeadvantage.entity.result.TaskListResult;
import com.jishijiyu.takeadvantage.entity.result.TaskListResult.ActivityList;
import com.jishijiyu.takeadvantage.entity.result.TaskListResult.TaskList;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 我的任务
 * 
 * @author zm
 */
public class TaskActivity extends HeadBaseActivity {
	OpenBoxResult openResult;
	private ListView taskListview;
	RequestTaskList requestTaskList;
	TaskListResult taskListResult = new TaskListResult();
	List<TaskList> taskList = new ArrayList<TaskList>();
	private String userId, tokenId, activityId, taskid;
	private Gson gson;
	ImageView imv_imageview;
	FrameLayout fl_framelayout;
	View views;
	Dialog dialogs, dialogs1;
	private MyAdapter<TaskList> myAdapter;
	Message msg;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				taskList();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onClick(View v) {

	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.mytask));
		btn_right.setVisibility(View.INVISIBLE);

		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		views = View.inflate(TaskActivity.this, R.layout.my_task_list, null);
		base_centent.addView(views);
		taskListview = (ListView) views.findViewById(R.id.my_news_listview);

		// fl_framelayout = (FrameLayout)
		// views.findViewById(R.id.fl_framelayout);
		// imv_imageview = (ImageView) views.findViewById(R.id.imv_imageview);
		userId = GetUserIdUtil.getUserId(TaskActivity.this);
		tokenId = GetUserIdUtil.getTokenId(this);
		AppManager.getAppManager().addActivity(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		taskList();
	}

	public void taskList() {
		requestTaskList = new RequestTaskList();
		requestTaskList.setC(Constant.TASK_LIST);
		TaskSonData data = new TaskSonData();
		data.setTokenId(tokenId);
		data.setUserId(userId);
		requestTaskList.setP(data);
		gson = new Gson();
		String json = gson.toJson(requestTaskList);
		HttpConnectTool.update(json, true, TaskActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						TaskListResult listResult = gson.fromJson(result,
								TaskListResult.class);
						final List<TaskList> list = listResult.getP()
								.getTaskList();
						final List<ActivityList> aclist = listResult.getP().activityList;
						if (listResult.getP().isTrue()) {
							if (list != null && list.size() > 0) {
								taskid = String.valueOf(list.get(0).getId());
								myAdapter = new MyAdapter<TaskList>(mContext,
										list, R.layout.mytask_item) {
									@Override
									public void convert(
											final ViewHolder helper,
											final int position, TaskList item) {
										if (position == 0) {
											if (aclist.size() > 0
													&& aclist != null) {
												activityId = String
														.valueOf(aclist.get(0).id);
												helper.setImageResource(
														R.id.activity_imageview,
														R.drawable.activity);

												helper.setText(
														R.id.exercise_time,
														"(送锁活动"
																+ GetTime(aclist
																		.get(position).startTime)
																+ " ~ "
																+ GetTime(aclist
																		.get(position).endTime)
																+ ")");
												if (list.get(0).getState() == 1) {
													helper.setText(
															R.id.to_invite,
															"前往");
													helper.getView(
															R.id.to_invite)
															.setBackgroundResource(
																	R.drawable.come_btn);

												} else if (list.get(0)
														.getState() == 2) {
													helper.setText(
															R.id.to_invite,
															"领取");
													helper.getView(
															R.id.to_invite)
															.setBackgroundResource(
																	R.drawable.come_btn);

												} else if (list.get(0)
														.getState() == 3) {
													helper.setText(
															R.id.to_invite,
															"已完成");
													helper.getView(
															R.id.to_invite)
															.setBackgroundResource(
																	R.drawable.complete);
												}
											} else {
												helper.setImageResource(
														R.id.activity_imageview,
														0);

												helper.setText(
														R.id.exercise_time, "");
											}

										} else {
											helper.setImageResource(
													R.id.activity_imageview, 0);

											helper.setText(R.id.exercise_time,
													"(+20拍币)");
											if (list.get(position).getState() == 1) {
												helper.setText(R.id.to_invite,
														"前往");
												helper.getView(R.id.to_invite)
														.setBackgroundResource(
																R.drawable.come_btn);

											} else if (list.get(position)
													.getState() == 2) {
												helper.setText(R.id.to_invite,
														"领拍币");
												helper.getView(R.id.to_invite)
														.setBackgroundResource(
																R.drawable.come_btn);
											} else if (list.get(position)
													.getState() == 3) {
												helper.setText(R.id.to_invite,
														"已完成");
												helper.getView(R.id.to_invite)
														.setBackgroundResource(
																R.drawable.complete);
											}
										}
										helper.setText(R.id.mytask_name, list
												.get(position).getTaskName());
										helper.setText(R.id.mytask_count, "("
												+ list.get(position)
														.getTargetDoingNum()
												+ "/"
												+ list.get(position)
														.getTargetNum() + ")");

										helper.getView(R.id.to_invite)
												.setOnClickListener(
														new OnClickListener() {

															@Override
															public void onClick(
																	View arg0) {
																if (list.get(
																		position)
																		.getState() == 1) {
																	get(position);
																} else if (list
																		.get(position)
																		.getState() == 2) {
																	if (aclist
																			.size() > 0
																			&& position == 0) {
																		get(6);
																	} else {
																		list.get(
																				position)
																				.setState(
																						3);

																		getPrize(
																				list.get(
																						position)
																						.getId()
																						+ "",
																				list.get(
																						position)
																						.getAwardScore(),
																				helper);
																	}

																}

															}
														});

									}
								};
								taskListview.setAdapter(myAdapter);
							}
						} else {
							// IntentActivity.mIntent(TaskActivity.this);
							ToastUtils.makeText(mContext, "UUID失效", 0).show();
						}
					}

					@Override
					public void contectStarted() {

					}

					@Override
					public void contectFailed(String path, String request) {

					}
				});

	}

	public void mAlphaAnimation_fl() {
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(1000);
		fl_framelayout.setAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			public void onAnimationEnd(Animation animation) {
				imv_imageview.setVisibility(View.VISIBLE);
				mAlphaAnimation_imv();
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationStart(Animation animation) {

			}

		});
	}

	public void mAlphaAnimation_imv() {
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(3000);
		imv_imageview.setAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			public void onAnimationEnd(Animation animation) {
				imv_imageview.setVisibility(View.GONE);
				fl_framelayout.setVisibility(View.GONE);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationStart(Animation animation) {

			}

		});
	}

	public void get(int position) {

		switch (position) {
		case 0:
			startForActivity(TaskActivity.this, FriendRequestActivity.class,
					null);
			// fl_framelayout.setVisibility(View.VISIBLE);
			// mAlphaAnimation_fl();

			break;
		case 1:

			startForActivity(TaskActivity.this, EarnPointsActivity.class, null);
			break;
		case 2:
			startForActivity(TaskActivity.this,
					MyBasicInfomationActivity.class, null);
			break;
		case 3:
			startForActivity(TaskActivity.this, ExchangemallActivity.class,
					null);
			break;
		case 4:

			startForActivity(TaskActivity.this, ShowPriceActivity.class,
					"TaskActivity");
			break;
		case 5:
			startForActivity(TaskActivity.this, MyPriceActivity.class, null);
			break;
		case 6:
			final Gson gson = new Gson();
			OpenBoxRequest openRequest = new OpenBoxRequest();
			openRequest.p.activityId = activityId;
			openRequest.p.id = taskid;
			openRequest.p.tokenId = tokenId;
			openRequest.p.userId = userId;
			String json = gson.toJson(openRequest);
			HttpConnectTool.update(json, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						openResult = gson.fromJson(result, OpenBoxResult.class);
						if (openResult.p.isTrue) {
							if (openResult.p.isSucce) {
								if (openResult.p.type == 0) {
									if (openResult.p.lock == 1) {
										LoginUserResult loginResult = GetUserIdUtil
												.getLogin(mContext);
										loginResult.p.user.goldLockNum = loginResult.p.user.goldLockNum + 1;
										GetUserIdUtil.saveLoginUserResult(
												mContext, loginResult);
										showBoxDialog(1);
									}
									if (openResult.p.lock == 2) {
										LoginUserResult loginResult = GetUserIdUtil
												.getLogin(mContext);
										loginResult.p.user.silverLockNum = loginResult.p.user.silverLockNum + 1;
										GetUserIdUtil.saveLoginUserResult(
												mContext, loginResult);
										showBoxDialog(2);
									}
									if (openResult.p.lock == 3) {
										LoginUserResult loginResult = GetUserIdUtil
												.getLogin(mContext);
										loginResult.p.user.copperLockNum = loginResult.p.user.copperLockNum + 1;
										GetUserIdUtil.saveLoginUserResult(
												mContext, loginResult);
										showBoxDialog(3);
									}
								}
								if (openResult.p.type == 1) {
									ToastUtils.makeText(mContext, "活动未开始", 0)
											.show();
								}
								if (openResult.p.type == 2) {
									ToastUtils.makeText(mContext, "活动已结束", 0)
											.show();
								}
								if (openResult.p.type == 3) {
									ToastUtils
											.makeText(mContext, "今天已开启过宝箱", 0)
											.show();
								}
							} else {
								ToastUtils.makeText(mContext, "已领取", 0).show();
							}
						} else {
							ToastUtils.makeText(mContext, "UUID失效", 0).show();
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
			break;
		default:
			break;
		}
	}

	private void showBoxDialog(final int witch) {
		dialogs = DialogUtils.TreasureBoxDialog(mContext,
				R.drawable.treasure_box, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogs1 = DialogUtils.exchangeLockDialog(mContext,
								R.drawable.img_lock_1, 1,
								GetUserIdUtil.goldLockNum(mContext),
								GetUserIdUtil.silverLockNum(mContext),
								GetUserIdUtil.copperLockNum(mContext), witch,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										switch (v.getId()) {
										case R.id.cancel_btn:
											if (openResult.p.state == 1) {
												msg = msg.obtain();
												msg.what = 1;
												handler.sendMessage(msg);
												dialogs1.dismiss();
												dialogs.dismiss();
											}
											if (openResult.p.state == 2) {
												msg = msg.obtain();
												msg.what = 1;
												handler.sendMessage(msg);
												dialogs1.dismiss();
												dialogs.dismiss();
											}

											break;

										default:
											break;
										}
									}
								});

					}

				});
	}

	private void getPrize(String id, final int score, final ViewHolder helper) {

		RequestAllodsGold requestAllGold = new RequestAllodsGold();
		requestAllGold.setC(Constant.ALLODS_GOLD);
		RequestSonData data = new RequestSonData();
		data.setId(id);
		data.setTokenId(tokenId);
		data.setUserId(userId);
		requestAllGold.setP(data);
		Gson gson1 = new Gson();
		String json1 = gson1.toJson(requestAllGold);
		HttpConnectTool.update(json1, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (TextUtils.isEmpty(result)) {
					ToastUtils.makeText(TaskActivity.this, "提交失败！", 0).show();
				} else {
					AllodsGoldResult allodsGoldResult = gson.fromJson(result,
							AllodsGoldResult.class);
					if (allodsGoldResult.getP().isTrue) {

						if (allodsGoldResult.getP().isSucce) {
							helper.setText(R.id.to_invite, "已完成");
							helper.getView(R.id.to_invite)
									.setBackgroundResource(R.drawable.complete);
							IntegralOperationfUtil.addIntegral(mContext, score);
							ShowGoldAnimActivity activity = new ShowGoldAnimActivity(
									TaskActivity.this);
							activity.showPopWindows(TaskActivity.this, views,
									score + "", false);
						} else {
							ToastUtils.makeText(TaskActivity.this, "提交失败！", 0)
									.show();
						}
					} else {
						IntentActivity.mIntent(TaskActivity.this);
					}
				}

			}

			@Override
			public void contectStarted() {
			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});

	}

	public static String GetTime(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str_time = sdf.format(cc_time);
		return str_time;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
