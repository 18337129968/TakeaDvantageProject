package com.jishijiyu.takeadvantage.activity.exchangemall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.widget.CustomShareBoard;
import com.jishijiyu.takeadvantage.entity.request.AcceptOrderRequest;
import com.jishijiyu.takeadvantage.entity.result.AcceptOrderResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;

/**
 * 
 * @author 确认订单，兑换成功弹窗
 * 
 */
public class FirmOrderPopActivity extends Activity implements OnClickListener {
	private Button cancel, sure;
	private TextView firstTv, points, lastTv;
	private Gson gson;
	private String result, province, city, area, name, detailAddress, num,score;
	private AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest();
	private AcceptOrderResult acceptOrderResult = new AcceptOrderResult();
	ShareUtil share;
	private Message message;
	private int type;
	private boolean isSuccess;
	String request, addressRequest, goodsId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_pop_firm_order);
		share = new ShareUtil(this, this);
		score = getIntent().getStringExtra("score");
		init();

	}


	private void init() {
		cancel = (Button) findViewById(R.id.firm_order_btn_cancel);
		sure = (Button) findViewById(R.id.firm_order_btn_sure);
		firstTv = (TextView) findViewById(R.id.firm_order_gold_first);
		points = (TextView) findViewById(R.id.firm_order_point_tv_middle);
		lastTv = (TextView) findViewById(R.id.firm_order_gold_last);
		points.setText(score);
		cancel.setOnClickListener(this);
		sure.setOnClickListener(this);
	};

	private void requestData(Object entry) {
		gson = new Gson();
		request = gson.toJson(entry);
		LogUtil.i(entry + "request" + request);
//		result = HttpConnectUtil.updata(request, false, null, null);
		HttpConnectTool.update(request, this, new ConnectListener() {
			
			@Override
			public void contectSuccess(String result) {
				acceptOrderResult = gson.fromJson(result,
						AcceptOrderResult.class);
				type = acceptOrderResult.p.type;
				isSuccess = acceptOrderResult.p.isSucce;
				if (isSuccess) {
					firstTv.setText("兑换成功");
					points.setVisibility(View.GONE);
					lastTv.setVisibility(View.GONE);
					cancel.setText("分享好友");
					sure.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							finish();
							
						}
					});
					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
//							finish();
							share.postShare();

						}
					});
				} else {
					switch (type) {
					case 1:
						Toast.makeText(FirmOrderPopActivity.this, "兑换失败，请稍后再试",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(FirmOrderPopActivity.this, "没有找到该商品",
								Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(FirmOrderPopActivity.this, "商品数量不足",
								Toast.LENGTH_SHORT).show();
						break;
					case 4:
						Toast.makeText(FirmOrderPopActivity.this, "商品已下架",
								Toast.LENGTH_SHORT).show();
						break;
					case 5:
						Toast.makeText(FirmOrderPopActivity.this, "拍币不足",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				}

			}
			
			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(FirmOrderPopActivity.this, "访问网络失败",Toast.LENGTH_SHORT).show();
			}
		});
//		LogUtil.i("result" + result);
//		message = Message.obtain();
//		if (!TextUtils.isEmpty(result)) {
//			message.what = LOADING_DATA_SUCCESS;
//		} else {
//			message.what = LOADING_DATA_FAILED;
//		}
//		handler.sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firm_order_btn_cancel:
			finish();
			break;
		case R.id.firm_order_btn_sure:
			getData();
			requestData(acceptOrderRequest);

			break;
		default:
			break;
		}

	}

	private void getData() {
		String userId = GetUserIdUtil.getUserId(this);
		if (userId == null) {
			return;
		} else {
			acceptOrderRequest.p.userId = userId;
		}
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		intent.getStringExtra("telephone");
		detailAddress = intent.getStringExtra("detailedAddress");
		area = intent.getStringExtra("area");
		city = intent.getStringExtra("city");
		province = intent.getStringExtra("province");
		num = intent.getStringExtra("num");
		goodsId = intent.getStringExtra("goodsId");
	
		acceptOrderRequest.p.area = area;
		acceptOrderRequest.p.city = city;
		acceptOrderRequest.p.detailedAddress = detailAddress;
		acceptOrderRequest.p.name = name;
		acceptOrderRequest.p.num = num;
		acceptOrderRequest.p.province = province;
		acceptOrderRequest.p.goodsId = goodsId;

	}
}
