package com.jishijiyu.takeadvantage.activity.myinformation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jishijiyu.takeadvange.alipay.PayResult;
import com.jishijiyu.takeadvange.alipay.SignUtils;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayOrderActivity;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Arith;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.Util;

/**
 * 充值金币
 * 
 * @author baohan
 * 
 */
public class RechargeGoldActivity extends HeadBaseActivity implements
		OnCheckedChangeListener {
	EditText edit_count_re;
	TextView rechargeGold, rechargeNowBtn, recharge_money_text2;
	private CheckBox rbtn_one;
	private CheckBox rbtn_two;
	private CheckBox rbtn_three;
	private PayOrderActivity mPayOrderActivity;
	public String UserId;
	private LoginUserResult login5;
	public int cbox_type = 1;
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
					ToastUtils.makeText(RechargeGoldActivity.this, "支付成功!", 0)
							.show();
					// QueryGoldNum();
					RechargeGoldActivity.this.finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtils.makeText(RechargeGoldActivity.this,
								"支付结果确认中!", 0).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtils.makeText(RechargeGoldActivity.this, "支付失败!",
								0).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(RechargeGoldActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;

		case R.id.recharge_now_btn:

			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == rbtn_one && isChecked) {
			rbtn_two.setChecked(false);
			rbtn_three.setChecked(false);
			cbox_type = 1; // 支付宝方式方式充值
		} else if (buttonView == rbtn_two && isChecked) {
			rbtn_one.setChecked(false);
			rbtn_three.setChecked(false);
			cbox_type = 2; // 微信支付方式充值
		} else if (buttonView == rbtn_three && isChecked) {
			rbtn_one.setChecked(false);
			rbtn_two.setChecked(false);
			cbox_type = 3; // 银联支付方式充值
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.recharge_gold);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(RechargeGoldActivity.this,
				R.layout.activity_recharge_gold, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		rechargeGold = (TextView) findViewById(R.id.recharge_gold_text);

		rechargeNowBtn = (TextView) findViewById(R.id.recharge_now_btn1);

		recharge_money_text2 = (TextView) findViewById(R.id.recharge_money_text2);

		rbtn_one = (CheckBox) this.findViewById(R.id.rbtn_one);
		rbtn_two = (CheckBox) this.findViewById(R.id.rbtn_two);
		rbtn_three = (CheckBox) this.findViewById(R.id.rbtn_three);
		rbtn_one.setOnCheckedChangeListener(this);
		rbtn_two.setOnCheckedChangeListener(this);
		rbtn_three.setOnCheckedChangeListener(this);

		UserId = GetUserIdUtil.getUserId(this);
		rechargeNowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cbox_type == 1) {
					// 支付宝支付
					if (Util.checkLocalAppExistOrNot(RechargeGoldActivity.this,
							"com.eg.android.AlipayGphone")) {
						pay("购买金币",
								"购买金币",
								Arith.div(Double.parseDouble(edit_count_re
										.getText().toString()), 100)
										+ "", UserId);
					} else {
						ToastUtils.makeText(RechargeGoldActivity.this,
								"没有支付宝应用，请你到软件商店下载!", 0).show();
					}
				} else if (cbox_type == 2) {
					// 微信支付

				} else if (cbox_type == 3) {
					// 银联支付
				}

			}
		});

		edit_count_re = (EditText) findViewById(R.id.edit_count_re);
		edit_count_re.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

				switch (arg2.getAction()) {
				case KeyEvent.ACTION_UP:
					String str = edit_count_re.getText().toString();
					if (str == null || str.equals("")) {
						recharge_money_text2.setText("0.0");
					} else {
						int num = Integer.valueOf(str.trim());
						float money = num * 100;
						recharge_money_text2.setText(money + "");
					}
					break;

				case KeyEvent.ACTION_DOWN:

					break;
				}
				return false;
			}
		});

	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String commName, String commDes, String commPrice,
			String userId) {
		// 订单
		String orderInfo = getOrderInfo(commName, commDes, commPrice, userId);

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
				PayTask alipay = new PayTask(RechargeGoldActivity.this);
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
				PayTask payTask = new PayTask(RechargeGoldActivity.this);
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
		PayTask payTask = new PayTask(RechargeGoldActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(RechargeGoldActivity.this, version, Toast.LENGTH_SHORT)
				.show();
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
