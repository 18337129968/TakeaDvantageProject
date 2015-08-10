package com.jishijiyu.takeadvantage.activity.paymoney;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.jishijiyu.takeadvange.alipay.PayResult;
import com.jishijiyu.takeadvange.alipay.SignUtils;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.GoldLocksRequest;
import com.jishijiyu.takeadvantage.entity.result.GoldLocksResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 
 * 换锁支付订单-银铜
 * 
 * @author sunaoyang
 */
public class PayOrderSKActivity extends HeadBaseActivity {
	private CheckBox have_invite_radio;
	private CheckBox have_gold_radio;
	private Button btn_add = null;
	private Button btn_del = null;
	private TextView tv_sk;
	private TextView tv_sk2;
	private TextView edt_num;
	private TextView tv_gold;
	private TextView tv_gold2;
	private TextView tv_gold3;
	private TextView tv_lock;
	private Button btn_exit = null;
	private Button btn_conversion = null;
	private Boolean goldCheck = false;
	private int needNum = 1;
	private int remainNum = 1;
	private int goldExchangeNum = 0;
	private int goldNum = 0;
	private int goldLockNum = 0;
	private int silverLockNum = 0;
	private int copperLockNum = 0;
	private GoldLocksRequest request = null;
	private Gson gson = null;
	private AlertDialog alertDialog = null;
	private int num = 0;// 支付用到的锁的数量
	private int lockType = 0;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.pay_order_top));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(PayOrderSKActivity.this,
				R.layout.pay_order_sk, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			request = (GoldLocksRequest) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
		}
		goldNum = GetUserIdUtil.goldNum(this);
		goldLockNum = GetUserIdUtil.goldLockNum(this);
		silverLockNum = GetUserIdUtil.silverLockNum(this);
		copperLockNum = GetUserIdUtil.copperLockNum(this);

		initView(view);
		initData();
		initOnclick();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void initView(View view) {
		tv_sk2 = (TextView) view.findViewById(R.id.tv_sk2);
		tv_sk = (TextView) view.findViewById(R.id.tv_sk);
		edt_num = (TextView) view.findViewById(R.id.edt_num);
		tv_gold = (TextView) view.findViewById(R.id.tv_gold);
		tv_gold2 = (TextView) view.findViewById(R.id.tv_gold2);
		tv_gold3 = (TextView) view.findViewById(R.id.tv_gold3);
		tv_lock = (TextView) view.findViewById(R.id.tv_lock);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_del = (Button) view.findViewById(R.id.btn_del);
		have_invite_radio = (CheckBox) view
				.findViewById(R.id.have_invite_radio);
		have_gold_radio = (CheckBox) view.findViewById(R.id.have_gold_radio);
		btn_conversion = (Button) view.findViewById(R.id.btn_conversion);
		btn_exit = (Button) view.findViewById(R.id.btn_exit);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		lockType = Integer.parseInt(bundle.getString("lock"));

	}

	public void initOnclick() {
		btn_add.setOnClickListener(this);
		btn_del.setOnClickListener(this);
		have_gold_radio.setOnClickListener(this);
		btn_conversion.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
	}

	public void initData() {
		if (lockType == 2) {
			tv_sk.setText("银锁");
			tv_sk2.setText("30金币=1把");
			tv_gold.setText(goldNum / 30 + "");
			tv_gold2.setText(goldNum + "");
			tv_gold3.setText(remainNum * 30 + "");
			tv_lock.setText(remainNum + "");
		} else if (lockType == 3) {
			tv_sk.setText("铜锁");
			tv_sk2.setText("10金币=1把");
			tv_gold.setText(goldNum / 10 + "");
			tv_gold2.setText(goldNum + "");
			tv_gold3.setText(remainNum * 10 + "");
			tv_lock.setText(remainNum + "");
		}
	}

	@Override
	public void onClick(View v) {
		String newNeedLock = null;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(PayOrderSKActivity.this);
			break;
		case R.id.btn_add:
			newNeedLock = edt_num.getText().toString().trim();
			if (!TextUtils.isEmpty(newNeedLock)) {
				needNum = Integer.parseInt(newNeedLock);
				needNum += 1;
				edt_num.setText(needNum + "");
				remainNum = needNum - goldExchangeNum;
				if (remainNum < 0) {
					remainNum = 0;
				}
				tv_lock.setText(remainNum + "");
				if (lockType == 2) {
					tv_gold3.setText(30 * remainNum + "");
				} else if (lockType == 3) {
					tv_gold3.setText(10 * remainNum + "");
				}
			}
			break;
		case R.id.btn_del:
			newNeedLock = edt_num.getText().toString().trim();
			if (!TextUtils.isEmpty(newNeedLock)) {
				needNum = Integer.parseInt(newNeedLock);
				if (needNum > 1) {
					needNum -= 1;
				}
				edt_num.setText(needNum + "");
				remainNum = needNum - goldExchangeNum;
				if (remainNum < 0) {
					remainNum = 0;
				}
				tv_lock.setText(remainNum + "");
				if (lockType == 2) {
					tv_gold3.setText(30 * remainNum + "");
				} else if (lockType == 3) {
					tv_gold3.setText(10 * remainNum + "");
				}
			}
			break;

		case R.id.have_gold_radio:
			if (goldCheck == false) {
				goldCheck = true;
				have_gold_radio.setChecked(true);
				if (lockType == 2) {
					goldExchangeNum = goldNum / 30;
				} else if (lockType == 3) {
					goldExchangeNum = goldNum / 10;
				}
				remainNum = needNum - goldExchangeNum;
				if (remainNum < 0) {
					remainNum = 0;
				}
				tv_lock.setText(remainNum + "");
				if (lockType == 2) {
					tv_gold3.setText(30 * remainNum + "");
				} else if (lockType == 3) {
					tv_gold3.setText(10 * remainNum + "");
				}
			} else if (goldCheck == true) {
				goldCheck = false;
				have_gold_radio.setChecked(false);
				goldExchangeNum = 0;
				remainNum = needNum - goldExchangeNum;
				if (remainNum < 0) {
					remainNum = 0;
				}
				tv_lock.setText(remainNum + "");
				if (lockType == 2) {
					tv_gold3.setText(30 * remainNum + "");
				} else if (lockType == 3) {
					tv_gold3.setText(10 * remainNum + "");
				}

			}
			break;
		case R.id.btn_conversion:

			alertDialog = DialogUtils.payLockDialog(PayOrderSKActivity.this,
					this);

			break;
		case R.id.btn_exit:

			AppManager.getAppManager().finishActivity(PayOrderSKActivity.this);

			break;
		case R.id.btn_yes:
			if (goldCheck == false) {
				// remain RMB支付
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				num = remainNum;
				if (lockType == 2) {
					pay("换锁", "支付换银锁", "0.01", GetUserIdUtil.getUserId(this),
							num + "", "1");
				} else if (lockType == 3) {
					pay("换锁", "支付换铜锁", "0.01", GetUserIdUtil.getUserId(this),
							num + "", "1");
				}

			} else if (goldCheck == true) {
				if (needNum - goldExchangeNum > 0) {

					// goldExchangeNum 金币全部支付
					num = goldExchangeNum;
					gold_conversion();

					// remain RMB支付
					// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					num = remainNum;
					if (lockType == 2) {
						pay("换锁", "支付换银锁", "0.01",
								GetUserIdUtil.getUserId(this), num + "", "1");
					} else if (lockType == 3) {
						pay("换锁", "支付换铜锁", "0.01",
								GetUserIdUtil.getUserId(this), num + "", "1");
					}
				} else {

					// goldExchangeNum 金币支付
					num = needNum;
					gold_conversion();
				}
			}
			break;
		case R.id.btn_no:
			if (alertDialog != null) {
				alertDialog.dismiss();
			}
			break;

		}
	}

	private void gold_conversion() {

		gson = new Gson();
		GoldLocksRequest goldlocksrequest = new GoldLocksRequest();
		GoldLocksRequest.Pramater pramater = goldlocksrequest.p;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		if (lockType == 2) {
			pramater.lock = "2";
		} else if (lockType == 3) {
			pramater.lock = "3";
		}
		pramater.num = num + "";
		String json = gson.toJson(goldlocksrequest);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					GoldLocksResult goldlocksresult = gson.fromJson(result,
							GoldLocksResult.class);
					if (goldlocksresult != null) {
						if (goldlocksresult.p.isTrue) {
							if (goldlocksresult.p.isSucce) {
								ToastUtils.makeText(PayOrderSKActivity.this,
										"兑换成功！", ToastUtils.LENGTH_SHORT)
										.show();
								LoginUserResult loginUserResult = GetUserIdUtil
										.getLogin(PayOrderSKActivity.this);
								if (lockType == 2) {
									goldNum = goldNum - num * 30;
									silverLockNum = silverLockNum + num;
									loginUserResult.p.user.silverLockNum = silverLockNum;
								} else if (lockType == 3) {
									goldNum = goldNum - num * 10;
									copperLockNum = copperLockNum + num;
									loginUserResult.p.user.copperLockNum = copperLockNum;
								}
								loginUserResult.p.user.goldNum = goldNum;
								GetUserIdUtil.saveLoginUserResult(
										PayOrderSKActivity.this,
										loginUserResult);
								initData();
								AppManager.getAppManager().finishActivity(
										PayOrderSKActivity.this);
							}
						} else {
							IntentActivity.mIntent(PayOrderSKActivity.this);
						}
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(PayOrderSKActivity.this, "兑换失败！",
						ToastUtils.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String commName, String commDes, String commPrice,
			String userId, String num, String lock) {
		// 订单
		String orderInfo = getOrderInfo(commName, commDes, commPrice, userId,
				num, lock);

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
				PayTask alipay = new PayTask(PayOrderSKActivity.this);
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
				PayTask payTask = new PayTask(PayOrderSKActivity.this);
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
		PayTask payTask = new PayTask(PayOrderSKActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(PayOrderSKActivity.this, version, Toast.LENGTH_SHORT)
				.show();
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
					ToastUtils.makeText(PayOrderSKActivity.this, "支付成功!", 0)
							.show();

					// if (needNum - goldNum / 50 > 0) {
					// remainNum = needNum - goldNum / 50;
					// }
					LoginUserResult loginUserResult = GetUserIdUtil
							.getLogin(PayOrderSKActivity.this);
					if (lockType == 2) {
						silverLockNum = silverLockNum + num;
						loginUserResult.p.user.silverLockNum = silverLockNum;
					} else if (lockType == 3) {
						copperLockNum = copperLockNum + num;
						loginUserResult.p.user.copperLockNum = copperLockNum;
					}
					GetUserIdUtil.saveLoginUserResult(PayOrderSKActivity.this,
							loginUserResult);
					initData();
					AppManager.getAppManager().finishActivity(
							PayOrderSKActivity.this);
					// Intent intent = new Intent();
					// intent.setClass(PayOrderActivity.this,
					// OneRmbErnieActivity.class);
					// startActivity(intent);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtils.makeText(PayOrderSKActivity.this,
								"支付结果确认中!", 0).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtils
								.makeText(PayOrderSKActivity.this, "支付失败!", 0)
								.show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayOrderSKActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}

	};

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price,
			String userId, String num, String lock) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "H" + userId
				+ "H" + num + "H" + lock + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url="
				+ "\""
				+ "http://103.44.145.243:34461/AppServer/alipayByLockNotifyUrl.html"
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
