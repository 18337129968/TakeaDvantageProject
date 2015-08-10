package com.jishijiyu.takeadvantage.activity.myinformation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
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
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.jishijiyu.takeadvange.alipay.PayResult;
import com.jishijiyu.takeadvange.alipay.SignUtils;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.merchant_account.AccountWithdrawalsActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.IntegralStaticsPayMoneyActivity;
import com.jishijiyu.takeadvantage.adapter.AccountGoldAdapter;
import com.jishijiyu.takeadvantage.entity.request.QueryGoldNumRequest;
import com.jishijiyu.takeadvantage.entity.request.RequestAccountGold;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.QueryGoldNumResult;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountGold;
import com.jishijiyu.takeadvantage.utils.Arith;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 账户金币
 * 
 * @author baohan
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class IntegralStaticticsConsume extends Fragment implements
		OnClickListener {
	private ResultAccountGold accountGoldJson;
	private Arith arith = null;
	private Message msg = null;
	private int goldNum;
	private LoginUserResult login5;
	private int dataNum = 0;
	private int userGoldNum;
	View view;
	LinearLayout ll_integral_addview;
	Button gold_recharge_btn, withdrawals_btn;
	AlertDialog dialog;
	TextView advertisementTask, applocationTask, exchangeGold, totalGet,
			surplusGold, purchaseLock, withdrawalsGold, exchangeIntegral,
			yiYuan, totalConsum;
	private String mone;

	private static final int SUCCESS = 0;
	private static final int FAIL = 1;
	private static final int EXCHANGEGOLDSUCCESS = 2;
	private static final int EXCHANGEGOLDFAIL = 3;

	private static final int SUCC = 4;
	private static final int SUCCFALI = 5;

	private static final int QUERYSUCCESS = 6;

	private QueryGoldNumResult queryGoldNumResult;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SUCCESS:
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
			case SUCC:
				ToastUtils.makeText(getActivity(), "兑换失败!", 0).show();
				break;
			case SUCCFALI:
				ToastUtils.makeText(getActivity(), "兑换失败!", 0).show();
				break;
			case QUERYSUCCESS:
				// ToastUtils.makeText(getActivity(), "获取金币数量成功!", 0).show();
				break;
			default:
				break;
			}

		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_account_gold, null);
		advertisementTask = (TextView) view.findViewById(R.id.title1);
		applocationTask = (TextView) view.findViewById(R.id.title2);
		exchangeGold = (TextView) view.findViewById(R.id.title3);
		totalGet = (TextView) view.findViewById(R.id.title4);
		surplusGold = (TextView) view.findViewById(R.id.title8);
		purchaseLock = (TextView) view.findViewById(R.id.title5);
		withdrawalsGold = (TextView) view.findViewById(R.id.title6);
		exchangeIntegral = (TextView) view.findViewById(R.id.title11);
		yiYuan = (TextView) view.findViewById(R.id.title7);
		totalConsum = (TextView) view.findViewById(R.id.title12);
		gold_recharge_btn = (Button) view.findViewById(R.id.recharge);
		gold_recharge_btn.setOnClickListener(this);
		withdrawals_btn = (Button) view.findViewById(R.id.withdrawals);
		withdrawals_btn.setOnClickListener(this);
		init("0", "5");
		return view;
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

				if (!TextUtils.isEmpty(result)) {
					queryGoldNumResult = gson.fromJson(result,
							QueryGoldNumResult.class);
					if (queryGoldNumResult.p.isTrue) {
						// 获取金币数量
						userGoldNum = queryGoldNumResult.p.user.goldNum;
						login5 = GetUserIdUtil.getLogin(getActivity());
						login5.p.user.goldNum = userGoldNum;
						GetUserIdUtil
								.saveLoginUserResult(getActivity(), login5);
						msg = new Message();
						msg.what = QUERYSUCCESS;
						handler.sendMessage(msg);
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
		// TODO Auto-generated method stub
		RequestAccountGold accountGold = new RequestAccountGold();
		RequestAccountGold.Pramater accountGoldPramater = accountGold.p;
		accountGoldPramater.userId = GetUserIdUtil.getUserId(getActivity());
		accountGoldPramater.tokenId = GetUserIdUtil.getTokenId(getActivity());
		accountGoldPramater.page = page;
		accountGoldPramater.pageSize = pageSize;
		final Gson gson = new Gson();
		String json = gson.toJson(accountGold);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(result)) {
					System.out.println(result);
					accountGoldJson = gson.fromJson(result,
							ResultAccountGold.class);
					if (accountGoldJson.p.isTrue) {
						ResultAccountGold.Pramater accountGoldPramater = accountGoldJson.p;
						if (accountGoldPramater != null) {
							if (accountGoldPramater.goldStatistics != null) {
								// 广告任务
								int lookPosterAddGold = accountGoldPramater.goldStatistics.lookPosterAddGold;
								advertisementTask.setText("广告任务:"
										+ lookPosterAddGold);
								// 应用任务
								int taskAddGold = accountGoldPramater.goldStatistics.taskAddGold;
								applocationTask.setText("应用任务:" + taskAddGold);

								// 邀请好友获得
								// String inviteUserAddScore =
								// accountGoldPramater.goldStatistics.;

								// 兑换金币
								int exchangeAddGold = accountGoldPramater.goldStatistics.exchangeAddGold;
								exchangeGold.setText("兑换金币:" + exchangeAddGold);

								// // 计算总共获得金币
								// @SuppressWarnings("static-access")
								// double data1 = arith.add(
								// Double.parseDouble(lookPosterAddGold),
								// Double.parseDouble(taskAddGold));
								// @SuppressWarnings("static-access")
								// double data2 = arith.add(data1,
								// Double.parseDouble(exchangeAddGold));
								double data2 = lookPosterAddGold + taskAddGold
										+ exchangeAddGold;
								totalGet.setText("总共获得:" + data2);

								// 消费的金币，购买锁
								int buyDelGold = accountGoldPramater.goldStatistics.buyDelGold;
								purchaseLock.setText("购买锁:" + buyDelGold);
								// 金币提现
								int advanceDelGold = accountGoldPramater.goldStatistics.advanceDelGold;
								exchangeIntegral.setText("提现金币:"
										+ advanceDelGold);
								// 拍币兑换
								int exchangeDelGold = accountGoldPramater.goldStatistics.exchangeDelGold;
								withdrawalsGold.setText("兑换拍币:"
										+ exchangeDelGold);
								// 一元拍
								int dollerDelGold = accountGoldPramater.goldStatistics.dollerDelGold;
								yiYuan.setText("一元拍:" + dollerDelGold);

								// // 计算总共消费
								// @SuppressWarnings("static-access")
								// double data3 = arith.add(
								// Double.parseDouble(buyDelGold),
								// Double.parseDouble(advanceDelGold));
								// @SuppressWarnings("static-access")
								// double data4 = arith.add(
								// Double.parseDouble(exchangeDelGold),
								// Double.parseDouble(dollerDelGold));
								// @SuppressWarnings("static-access")
								// double data5 = arith.add(data3, data4);
								double data5 = buyDelGold + advanceDelGold
										+ exchangeDelGold + dollerDelGold;
								totalConsum.setText("总共消耗:" + data5);

								// // 用户剩余金币
								// double data6 = Arith.sub(data2, data5);
								double data6 = data2 - data5;
								surplusGold.setText("用户剩余金币" + data6);
								QueryGoldNum();
								msg = new Message();
								msg.what = SUCCESS;
								handler.sendMessage(msg);
								setAdapter();
							} else {
								advertisementTask.setText("广告任务:" + 0);
								applocationTask.setText("应用任务:" + 0);
								exchangeGold.setText("兑换金币:" + 0);
								totalGet.setText("总共获得:" + 0);
								purchaseLock.setText("购买锁:" + 0);
								exchangeIntegral.setText("提现金币:" + 0);
								withdrawalsGold.setText("兑换拍币:" + 0);
								yiYuan.setText("一元拍:" + 0);
								totalConsum.setText("总共消耗:" + 0);
								surplusGold.setText("用户剩余金币:" + 0);
							}

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

		ListView list = (ListView) view.findViewById(R.id.gold_list);
		if (accountGoldJson.p.getGoldStatisticsRecordList() != null) {
			AccountGoldAdapter adapter = new AccountGoldAdapter(getActivity(),
					accountGoldJson.p.getGoldStatisticsRecordList());
			list.setAdapter(adapter);
		}
		setListViewHeightBasedOnChildren(list);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_top_left:
			getActivity().getApplication().fileList();
			break;
		case R.id.recharge:
			// 获取当前金币数量
			QueryGoldNum();
			// LoginUserResult login = GetUserIdUtil.getLogin(getActivity());
			login5 = GetUserIdUtil.getLogin(getActivity());
			goldNum = login5.p.user.goldNum;
			Intent i = new Intent(getActivity(), RechargeGoldActivity.class);
			startActivity(i);
			/*
			 * dialog = DialogUtils.showRechargeDialog1(getActivity(), "充值", 2,
			 * goldNum, IntegralStaticticsConsume.this);
			 */
			break;
		case R.id.withdrawals:
			// 获取当前金币数量
			// LoginUserResult login = GetUserIdUtil.getLogin(getActivity());
			login5 = GetUserIdUtil.getLogin(getActivity());
			goldNum = login5.p.user.goldNum;
			// 请求服务器
			dialog = DialogUtils.showRechargeDialog(getActivity(), "提现", 1,
					goldNum, IntegralStaticticsConsume.this);
			break;
		case R.id.dialog_cancel_btn:
			dialog.dismiss();
			break;
		case R.id.dialog_ok_btn:
			final EditText edit_count = (EditText) dialog
					.findViewById(R.id.edit_count);
			edit_count.setHint("请输入提现金额");
			if (edit_count.getText().toString().length() <= 0) {
				ToastUtils.makeText(getActivity(), "请输入提现金额!", 0).show();
			} else {
				dataNum = Integer.parseInt(edit_count.getText().toString());
				if (dataNum == 0) {
					ToastUtils.makeText(getActivity(), "提现金额不能为0!", 0).show();
					dialog.dismiss();
				} else {
					if (dataNum % 100 == 0 && dataNum != 0 && dataNum >= 0) {
						Intent intent = new Intent(getActivity(),
								AccountWithdrawalsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("num", edit_count.getText().toString());
						intent.putExtras(bundle);
						startActivity(intent);
						dialog.dismiss();
					} else if (goldNum < dataNum) {
						ToastUtils.makeText(getActivity(),
								"金币不够，请你赚取金币后，再来提现!", 0).show();
						dialog.dismiss();
					} else {
						dialog.dismiss();
						ToastUtils.makeText(getActivity(), "提现金额必须是100的整数!", 0)
								.show();

					}
				}

			}
			break;
		case R.id.dialog_cancel_btn1:
			dialog.dismiss();
			break;
		case R.id.dialog_ok_btn1:
			EditText edit_count1 = (EditText) dialog
					.findViewById(R.id.edit_count1);
			mone = edit_count1.getText().toString();
			int data = Integer.parseInt(mone);
			if (mone.length() <= 0) {
				ToastUtils.makeText(getActivity(), "请输入充值金额!", 0).show();
				dialog.dismiss();
			} else {
				if (data == 0) {
					ToastUtils.makeText(getActivity(), "输入金额不能为0!", 0).show();
					dialog.dismiss();
				} else {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					intent.setClass(getActivity(),
							IntegralStaticsPayMoneyActivity.class);
					bundle.putString("gold", edit_count1.getText().toString());
					bundle.putString("UserId",
							GetUserIdUtil.getUserId(getActivity()));
					intent.putExtras(bundle);
					startActivity(intent);
					dialog.dismiss();
				}
			}
			break;
		default:
			break;
		}
	}

	// 支付
	// 商户PID
	public static final String PARTNER = "2088911869365628";
	// 商户收款账号
	public static final String SELLER = "jishijiyu2015@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMgPFtsXAjDbRyv2CCWF9sHbHpPaAZtMVV09bdZkuoDUTi05U0kF8OgBfcv3ez/tIuQ71nEyzOkF6A7LeAMNjNNxDN0YGojIzXec1RX4qIOUDocf934o7c5NpWBxs9sKqwkUM2pymdhHYaPkqJdW54MCmz8PfX58azoPoQTzYceHAgMBAAECgYBarLwnQR51Fm4DtteqajWVV8NcMAtaYBaw1A7chXsiuRdV2A2vo0m1XjeuItVx/AE8gQKI/AVz+IFdc0tJ1vJkdksamfrvEIb7d3kjmrO5KDHGd7R/UUjJSjFSkvIUQpRQ3Vt/ur1qEdTPwC1vFnFAykUlhfvDPU//OAD+vswPgQJBAOMVz1jV7Fv5AC6VkLjrcJghh1+ZfsDUhiIqQtfAMQA4OfsTjSEzpu6FOG1mZ2lb/64TcLRclSFrpD/9K+sv208CQQDhiFEzz0EcCnyLyWNLQi1tCt1VivWiokHeuG/0yKXuy7iJvwTon9winAJ8UOEWkMJ0iibtAluFt7QUK+QQuGJJAkAeIMuktqUoQq4CgDd2QU5r8K7cQSou/UNajw7VPxuBMvSGWj60M/0m0if9Y8O+l8UhXFTsqQfkKjrdfoJwLqZDAkAx+JtNaHmbtfMcofWfQj2AcKzT/GMqstr3d1RPH3osq/TqiGmVBX6oKHsggmctMcv0OHwhfak7upVq9sKktcYJAkB39WTFpPcBzP7ALuVTb2qsZEprT9rOLKob9UiNDkWHwHVbeFtteTQmNE3Zi5C7zhBEp5LNX9syXKidRWoU3Rkb";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					ToastUtils.makeText(getActivity(), "支付成功!", 0).show();
					dialog.dismiss();
					// 充值成功
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtils.makeText(getActivity(), "支付结果确认中!", 0)
								.show();
						dialog.dismiss();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtils.makeText(getActivity(), "支付失败!", 0).show();
						dialog.dismiss();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}

	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String commName, String commDes, String commPrice,
			String UserId) {
		// 订单
		String orderInfo = getOrderInfo(commName, commDes, commPrice, UserId);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(getActivity());

				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(getActivity());
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(getActivity());
		String version = payTask.getVersion();
		Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price,
			String UserId) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "H" + UserId
				+ "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url="
				+ "\""
				+ "http://103.44.145.243:34461/AppServer/alipayExchangeGoldNotifyUrl.html"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
