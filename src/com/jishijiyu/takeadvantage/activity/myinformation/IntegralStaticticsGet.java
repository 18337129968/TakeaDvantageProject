package com.jishijiyu.takeadvantage.activity.myinformation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.adapter.MyAccountAdapter;
import com.jishijiyu.takeadvantage.entity.request.ExchangeGoldRequest;
import com.jishijiyu.takeadvantage.entity.request.QueryGoldNumRequest;
import com.jishijiyu.takeadvantage.entity.request.RequestAccountIntegral;
import com.jishijiyu.takeadvantage.entity.result.ExchangeGoldResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.QueryGoldNumResult;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral.Pramater;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral.ScoreStatistics;
import com.jishijiyu.takeadvantage.utils.Arith;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 账户拍币
 * 
 * @author baohan
 * 
 */
@SuppressLint("NewApi")
public class IntegralStaticticsGet extends Fragment implements OnClickListener {
	private String count;
	private int goldNum;
	private int userGoldNum;
	LinearLayout ll_integral_addview;
	TextView exchangeBtn, applationTask, advertisementTask, inviteTask,
			loginSign, systemPresent, goldExchange, totalGain, remainIntegral,
			exchangeConsumeIntegral, integralErnie, yiYuanErnie, totalConsume;
	View view;
	Button exchange;
	AlertDialog dialog1, dialog2;
	EditText edit_count;
	private Message msg;
	ScoreStatistics scoreStatistics;
	private ExchangeGoldResult exchangeGoldResult;
	private QueryGoldNumResult queryGoldNumResult;

	private static final int SUCCESS = 0;
	private static final int FAIL = 1;
	private static final int EXCHANGEGOLDSUCCESS = 2;
	private static final int EXCHANGEGOLDFAIL = 3;
	private Arith arith;
	private ResultAccountIntegral resultAccountIntegral = null;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// ToastUtils.makeText(getActivity(), "访问服务器成功!", 0).show();
				break;
			case FAIL:
				ToastUtils.makeText(getActivity(), "访问服务器失败!", 0).show();
				break;
			case EXCHANGEGOLDSUCCESS:
				ToastUtils.makeText(getActivity(), "兑换成功!", 0).show();
				init("0", "5");
				// 兑换成功查询金币数量,改变登录的时候给的金币数量
				break;
			case EXCHANGEGOLDFAIL:
				ToastUtils.makeText(getActivity(), "兑换失败!", 0).show();
				break;
			default:
				break;
			}

		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_account_integral, null);
		advertisementTask = (TextView) view.findViewById(R.id.title1);
		applationTask = (TextView) view.findViewById(R.id.tv2);
		inviteTask = (TextView) view.findViewById(R.id.title3);
		loginSign = (TextView) view.findViewById(R.id.title4);
		systemPresent = (TextView) view.findViewById(R.id.title5);
		goldExchange = (TextView) view.findViewById(R.id.title6);
		totalGain = (TextView) view.findViewById(R.id.title7);
		remainIntegral = (TextView) view.findViewById(R.id.title8);
		exchangeConsumeIntegral = (TextView) view.findViewById(R.id.title9);
		integralErnie = (TextView) view.findViewById(R.id.title10);
		yiYuanErnie = (TextView) view.findViewById(R.id.title11);
		totalConsume = (TextView) view.findViewById(R.id.title12);

		ll_integral_addview = (LinearLayout) view
				.findViewById(R.id.ll_integral_addview);
		exchangeBtn = (TextView) view.findViewById(R.id.exchange);
		edit_count = (EditText) view.findViewById(R.id.edit_count);

		init("0", "5");

		// 点击兑换按钮
		exchangeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 获取当前金币数量
				LoginUserResult login = GetUserIdUtil.getLogin(getActivity());
				goldNum = login.p.user.goldNum;
				// 请求服务器
				dialog1 = DialogUtils.showRechargeDialog(getActivity(), "兑换",
						3, goldNum, IntegralStaticticsGet.this);
			}

		});
		return view;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private void QueryGoldNum() {
		// TODO Auto-generated method stub
		QueryGoldNumRequest queryGoldNumRequest = new QueryGoldNumRequest();
		QueryGoldNumRequest.User user = queryGoldNumRequest.p;
		user.userId = GetUserIdUtil.getUserId(getActivity());
		user.tokenId = GetUserIdUtil.getTokenId(getActivity());
		final Gson gson = new Gson();
		String json = gson.toJson(queryGoldNumRequest);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				System.out.println("result:"+result);
				if (!TextUtils.isEmpty(result)) {
					queryGoldNumResult = gson.fromJson(result,
							QueryGoldNumResult.class);
					if (queryGoldNumResult.p.isTrue) {
						msg = new Message();
						msg.what = SUCCESS;
						handler.sendMessage(msg);
						// 获取金币数量
						userGoldNum = queryGoldNumResult.p.user.goldNum;
						LoginUserResult login = GetUserIdUtil
								.getLogin(getActivity());
						login.p.user.goldNum = userGoldNum;

						GetUserIdUtil.saveLoginUserResult(getActivity(), login);
					} else {
						IntentActivity.mIntent(getActivity());
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
				msg = new Message();
				msg.what = FAIL;
				handler.sendMessage(msg);
			}
		});

	};

	private void init(String page, String pageSize) {

		RequestAccountIntegral accountIntegral = new RequestAccountIntegral();
		RequestAccountIntegral.Pramater parameter = accountIntegral.p;

		parameter.userId = GetUserIdUtil.getUserId(getActivity());
		parameter.tokenId = GetUserIdUtil.getTokenId(getActivity());

		parameter.page = page;
		parameter.pageSize = pageSize;
		//
		final Gson gson = new Gson();
		String json = gson.toJson(accountIntegral);
		HttpConnectTool.update(json, getActivity().getApplicationContext(),
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub
						if (!TextUtils.isEmpty(result)) {
							resultAccountIntegral = gson.fromJson(result,
									ResultAccountIntegral.class);
							if (resultAccountIntegral.p.isTrue) {
								if (resultAccountIntegral.p.scoreStatistics != null) {
									ResultAccountIntegral.Pramater result1 = resultAccountIntegral.p;
									// 广告任务
									int lookPosterAddScore = result1.scoreStatistics.lookPosterAddScore;
									advertisementTask.setText("看广告:"
											+ lookPosterAddScore);

									// 应用任务
									int taskAddScore = result1.scoreStatistics.taskAddScore;

									applationTask.setText("应用任务:"
											+ taskAddScore);

									// 邀请好友
									int inviteUserAddScore = result1.scoreStatistics.inviteUserAddScore;
									inviteTask.setText("邀请好友:"
											+ inviteUserAddScore);

									// 登录签到
									int signAddScore = result1.scoreStatistics.signAddScore;
									loginSign.setText("登陆签到:" + signAddScore);
									// 系统赠送
									int systemAddScore = result1.scoreStatistics.systemAddScore;
									systemPresent.setText("系统赠送:"
											+ systemAddScore);

									// 金币兑换
									int exchangeAddScore = result1.scoreStatistics.exchangeAddScore;
									goldExchange.setText("金币兑换:"
											+ exchangeAddScore);

									// // 总共
									// double data1 = arith.add(Double
									// .parseDouble(inviteUserAddScore),
									// Double.parseDouble(signAddScore));
									// double data2 = arith.add(Double
									// .parseDouble(lookPosterAddScore),
									// Double.parseDouble(taskAddScore));
									// double data3 = arith.add(data1, data2);
									// double data4 = arith.add(Double
									// .parseDouble(exchangeAddScore),
									// Double.parseDouble(systemAddScore));
									// double total = arith.add(data3, data4);
									double total = lookPosterAddScore
											+ taskAddScore + inviteUserAddScore
											+ signAddScore + systemAddScore
											+ exchangeAddScore;
									totalGain.setText("总共获得:" + total);

									// 任务消耗拍币
									int exchangeDelScore = result1.scoreStatistics.exchangeDelScore;
									integralErnie.setText("兑换消耗拍币:"
											+ exchangeDelScore);
									int lotteryDelScore = result1.scoreStatistics.lotteryDelScore;
									exchangeConsumeIntegral.setText("拍币摇奖:"
											+ lotteryDelScore);
									int firstDelScore = result1.scoreStatistics.firstDelScore;
									yiYuanErnie
											.setText("一元摇奖:" + firstDelScore);

									// double del1 = arith.add(Double
									// .parseDouble(exchangeDelScore),
									// Double.parseDouble(lotteryDelScore));
									//
									// double del2 = arith.add(
									// Double.parseDouble(firstDelScore),
									// del1);
									// double sub = arith.sub(total, del2);
									double sub = total - exchangeDelScore
											- lotteryDelScore - firstDelScore;

									QueryGoldNum();
									remainIntegral.setText("用户所剩拍币:" + sub);
									msg = new Message();
									msg.what = SUCCESS;
									handler.sendMessage(msg);
								} else {
									advertisementTask.setText("看广告:" + 0);
									applationTask.setText("应用任务:" + 0);
									inviteTask.setText("邀请好友:" + 0);
									loginSign.setText("登陆签到:" + 0);
									systemPresent.setText("系统赠送:" + 0);
									goldExchange.setText("现金兑换:" + 0);
									totalGain.setText("总共获得:" + 0);
									integralErnie.setText("兑换消耗拍币:" + 0);
									exchangeConsumeIntegral
											.setText("拍币摇奖:" + 0);
									yiYuanErnie.setText("一元摇奖:" + 0);
									remainIntegral.setText("用户所剩拍币:" + 0);
								}
								if (resultAccountIntegral.p.scoreStatisticsRecord
										.size() > 0) {

									setAdapter();
								}

							} else {
								IntentActivity.mIntent(getActivity());
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
						msg = new Message();
						msg.what = FAIL;
						handler.sendMessage(msg);

					}
				});

	}

	public void setAdapter() {

		ListView list = (ListView) view.findViewById(R.id.integral_list);

		if (resultAccountIntegral != null
				&& resultAccountIntegral.p.scoreStatisticsRecord != null) {
			MyAccountAdapter adapter = new MyAccountAdapter(getActivity(),
					resultAccountIntegral.p.scoreStatisticsRecord);
			list.setAdapter(adapter);
		}
		setListViewHeightBasedOnChildren(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_cancel_btn:
			dialog1.dismiss();
			break;
		case R.id.dialog_ok_btn:
			EditText edit_count = (EditText) dialog1
					.findViewById(R.id.edit_count);
			count = edit_count.getText().toString();

			if (count.length() > 0) {
				int num = Integer.parseInt(count);
				if (num <= goldNum && num > 0) {
					dialog2 = DialogUtils.showDialog(getActivity(), "确定使用"
							+ num + "金币兑换" + num + "拍币", new int[] {
							R.string.ext, R.string.end },
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									switch (arg0.getId()) {
									case R.id.left:
										dialog2.dismiss();
										break;
									case R.id.right:
										// 兑换
										dialog2.dismiss();
										ExchangeGold(count);

										break;

									default:
										break;
									}

								}
							}, false);
					DialogUtils.setTitle("提示");
				} else if (num == 0) {
					ToastUtils.makeText(getActivity(), "兑换的拍币数量必须大于0拍币",
							ToastUtils.LENGTH_LONG).show();
				} else {
					ToastUtils.makeText(getActivity(), "亲,你们那么多金币，请你充值兑换!",
							ToastUtils.LENGTH_LONG).show();
				}
			} else {
				ToastUtils.makeText(getActivity(), "请你输入要兑换的金币!",
						ToastUtils.LENGTH_LONG).show();
			}

			break;
		default:
			break;
		}

	}

	protected void ExchangeGold(String num) {
		// TODO Auto-generated method stub
		ExchangeGoldRequest exchangeGoldRequest = new ExchangeGoldRequest();
		ExchangeGoldRequest.ExchangeGold exchangeGold = exchangeGoldRequest.p;
		exchangeGold.userId = GetUserIdUtil.getUserId(getActivity());
		exchangeGold.tokenId = GetUserIdUtil.getTokenId(getActivity());
		exchangeGold.goldNum = num;
		final Gson gson1 = new Gson();
		String json2 = gson1.toJson(exchangeGoldRequest);
		HttpConnectTool.update(json2, getActivity(), new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				exchangeGoldResult = gson1.fromJson(result,
						ExchangeGoldResult.class);
				if (!result.isEmpty()) {
					if (exchangeGoldResult.p.isTrue) {
						msg = new Message();
						msg.what = SUCCESS;
						handler.sendMessage(msg);
						if (exchangeGoldResult.p.isSucce) {
							msg = new Message();
							msg.what = EXCHANGEGOLDSUCCESS;
							handler.sendMessage(msg);
							dialog1.dismiss();
						} else {
							msg = new Message();
							msg.what = EXCHANGEGOLDFAIL;
							handler.sendMessage(msg);
							dialog1.dismiss();
						}

					} else {

						IntentActivity.mIntent(getActivity());
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
				msg = new Message();
				msg.what = FAIL;
				handler.sendMessage(msg);
			}
		});
	}

}
