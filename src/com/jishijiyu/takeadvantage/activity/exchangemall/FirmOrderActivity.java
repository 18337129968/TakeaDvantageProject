package com.jishijiyu.takeadvantage.activity.exchangemall;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.R.color;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.GetPrizeDataRequest;
import com.jishijiyu.takeadvantage.activity.ernieonermb.GetPrizeDataResponse;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.AcceptOrderRequest;
import com.jishijiyu.takeadvantage.entity.request.AcceptPrizeRequest;
import com.jishijiyu.takeadvantage.entity.request.DefultAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.AcceptOrderResult;
import com.jishijiyu.takeadvantage.entity.result.AcceptPrizeResult;
import com.jishijiyu.takeadvantage.entity.result.Address;
import com.jishijiyu.takeadvantage.entity.result.DefultAddressResult;
import com.jishijiyu.takeadvantage.entity.result.WinningPriceResult.WinningListData;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 确认订单
 * 
 * @author 张翠玲
 * 
 */
public class FirmOrderActivity extends HeadBaseActivity {
	private static final int GET_CODE = 0;
	TextView name_tv, phoneNum_tv, detailAddress_tv, goodsDetail, goodsGold,
			freight_tv;
	ImageView goodsImg;
	LinearLayout changeAddress;
	Button confirm, accept;
	Dialog dialog, successDialog;
	private Gson gson;
	private String addressResult, province, city, area, name, phoneNum,
			detailAddress, score, freight;
	ShareUtil share;
	private AcceptPrizeRequest acceptPrizeRequest = new AcceptPrizeRequest();
	private AcceptPrizeResult acceptPrizeResult = new AcceptPrizeResult();
	private DefultAddressRequest defultAddressRequest = new DefultAddressRequest();
	private AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest();
	private AcceptOrderResult acceptOrderResult = new AcceptOrderResult();
	private DefultAddressResult defultAddressResult;
	private Boolean issuccess, isSuccess;
	String request, addressRequest, type_intent, goodsId;
	private WinningListData listData;
	Address address;
	int type;
	String point;
	private String awardId;
	private Intent intent;

	@Override
	public void appHead(View view) {

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
		View view = View.inflate(this, R.layout.activity_firm_order, null);
		base_centent.addView(view);
		share = new ShareUtil(this, mContext);
		address = (Address) getIntent().getSerializableExtra("Address");
		type_intent = getIntent().getStringExtra("type");
		goodsId = getIntent().getStringExtra("goodsId");

		if (type_intent != null && type_intent.equals("exchange")) {
			top_text.setText(getResources().getString(R.string.firm_exchange));
		} else {
			top_text.setText(getResources().getString(R.string.firm_reward));
		}
		init();
		if (type_intent != null && type_intent.equals("exchange")) {
			if (address == null) {
				 getAddressData();
				requestDefultAddress(defultAddressRequest);
			} else {
				name = address.name;
				phoneNum = address.telephone;
				detailAddress = address.detailedAddress;
				name_tv.setText(name);
				phoneNum_tv.setText(phoneNum);
				detailAddress_tv.setText(detailAddress);
				province = address.province;
				city = address.city;
				area = address.area;
				// goodsId = "" + address.id;
			}
		}
		if (type_intent != null && type_intent.equals("getprize")) {
			if (address == null) {
				//getSurePrizeData();
				
			} else {
				name = address.name;
				phoneNum = address.telephone;
				detailAddress = address.detailedAddress;
				name_tv.setText(name);
				phoneNum_tv.setText(phoneNum);
				detailAddress_tv.setText(detailAddress);
				province = address.province;
				city = address.city;
				area = address.area;
				
			}
			System.out.println("走这里.....");
			getSurePrizeData();
		}

		if (freight != null) {
			freight_tv.setText(freight + "元");
		}
		// 从服务端获取确认领奖信息

		//getSurePrizeData();


	}

	private void getSurePrizeData() {
		ErnieSureGetPageRequest ernieSureGetPageRequest = new ErnieSureGetPageRequest();
		ernieSureGetPageRequest.p.tokenId = GetUserIdUtil.getTokenId(this);
		ernieSureGetPageRequest.p.userId = GetUserIdUtil.getUserId(this);
		Intent intent=getIntent();
		String awardId=intent.getStringExtra("mealRecordId");
		System.out.println("awardId:"+awardId);
		ernieSureGetPageRequest.p.awardId=awardId;
		gson = new Gson();
		String jsonString = gson.toJson(ernieSureGetPageRequest);
		HttpConnectTool.update(jsonString, FirmOrderActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub
						System.out.println("确认领奖，从服务器端获取页面信息：" + result);

						ErnieSureGetPageResponse ernieSureGetPageResponse = gson
								.fromJson(result,
										ErnieSureGetPageResponse.class);
						if (ernieSureGetPageResponse.p.isTrue) {

							// ArrayList<getAward> prizeInfo = new
							// ArrayList<getAward>();
							// System.out
							// .println(ernieSureGetPageResponse.p.getAward);
							// for (int i = 0; i <
							// ernieSureGetPageResponse.p.getAward
							// .size(); i++) {
							// prizeInfo
							// .add(ernieSureGetPageResponse.p.getAward
							// .get(i));
							// System.out.println("prizeInfo:"+prizeInfo);
							// }
							ImageLoaderUtil
									.loadImage(
											ernieSureGetPageResponse.p.getAward.awardImg,
											goodsImg);
							goodsDetail
									.setText(ernieSureGetPageResponse.p.getAward.awardName);
							freight_tv
									.setText(ernieSureGetPageResponse.p.getAward.freight
											+ "");
						}
						getAddressData();
						requestDefultAddress(defultAddressRequest);
					}

					@Override
					public void contectStarted() {
						// TODO Auto-generated method stub

					}

					@Override
					public void contectFailed(String path, String request) {
						// TODO Auto-generated method stub
						System.out.println("6020连接失败");
						getAddressData();
						requestDefultAddress(defultAddressRequest);
					}
				});

	}

	private void getAddressData() {
		String userId = GetUserIdUtil.getUserId(this);
		String tokenId = GetUserIdUtil.getTokenId(this);
		if (userId == null) {
			return;
		} else {
			defultAddressRequest.p.userId = userId;
		}
		if (tokenId == null) {
			return;
		} else {
			defultAddressRequest.p.tokenId = tokenId;
		}

	}

	/**
	 * 请求默认收货地址
	 * 
	 * @param entry
	 */
	private void requestDefultAddress(Object entry) {
		gson = new Gson();
		addressRequest = gson.toJson(entry);
		LogUtil.i(entry + "addressRequest" + addressRequest);
		HttpConnectTool.update(addressRequest, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				
				System.out.println("确认领奖界面：" + result);
				defultAddressResult = gson.fromJson(result,
						DefultAddressResult.class);
				if (defultAddressResult.p.isTrue) {
					System.out.println("提取数据成功了");
					if (defultAddressResult.p.receiveAddress != null) {
						name = defultAddressResult.p.receiveAddress.name;
						System.out.println("name:" + name);
						phoneNum = defultAddressResult.p.receiveAddress.telephone;
						System.out.println("name:" + phoneNum);
						detailAddress = defultAddressResult.p.receiveAddress.detailedAddress;
						name_tv.setText(name);
						phoneNum_tv.setText(phoneNum);
						detailAddress_tv.setText(detailAddress);
						province = defultAddressResult.p.receiveAddress.province;
						city = defultAddressResult.p.receiveAddress.city;
						area = defultAddressResult.p.receiveAddress.area;
					} else {
						name_tv.setText("请选择");
						phoneNum_tv.setText("");
						detailAddress_tv.setText("请选择");
					}
				} else {
					startForActivity(mContext, LoginActivity.class, null);
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
			
				Toast.makeText(mContext, "请求默认地址失败", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void requestData(Object entry) {
		gson = new Gson();
		request = gson.toJson(entry);
		LogUtil.i(entry + "request" + request);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				acceptPrizeResult = gson.fromJson(result,
						AcceptPrizeResult.class);
				if (acceptPrizeResult.p.isTrue) {
					issuccess = acceptPrizeResult.p.isSucce;
					if (issuccess) {
						ToastUtils.makeText(mContext, "领取成功",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						ToastUtils.makeText(mContext, "领取失败",
								Toast.LENGTH_SHORT).show();
					}

					init();
				} else {
					startForActivity(mContext, LoginActivity.class, null);
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();

			}
		});
	}

	private void getData() {
		String userId = GetUserIdUtil.getUserId(this);
		if (userId == null) {
			return;
		} else {
			acceptPrizeRequest.p.userId = userId;
		}
		String tokenId = GetUserIdUtil.getTokenId(this);
		acceptPrizeRequest.p.tokenId = tokenId;
		acceptPrizeRequest.p.name = name_tv.getText().toString();
		acceptPrizeRequest.p.telephone = phoneNum_tv.getText().toString();
		acceptPrizeRequest.p.detailedAddress = detailAddress_tv.getText()
				.toString();
		if (listData != null) {
			acceptPrizeRequest.p.id = listData.getId();
		} else if (goodsId != null) {
			acceptPrizeRequest.p.id = goodsId;
		}
		acceptPrizeRequest.p.num = "1";
		acceptPrizeRequest.p.province = province;
		acceptPrizeRequest.p.city = city;
		acceptPrizeRequest.p.area = area;
	}

	private void init() {
		name_tv = (TextView) findViewById(R.id.more_address_lv_item_name_tv);
		phoneNum_tv = (TextView) findViewById(R.id.more_address_lv_item_phonenum_tv);
		detailAddress_tv = (TextView) findViewById(R.id.more_address_lv_item_address);
		confirm = (Button) findViewById(R.id.firm_order_btn);
		accept = (Button) findViewById(R.id.firm_Prize_btn);
		changeAddress = (LinearLayout) findViewById(R.id.firm_order_ll);
		goodsDetail = (TextView) findViewById(R.id.firm_order_detail_tv);
		goodsImg = (ImageView) findViewById(R.id.firm_order_img);
		goodsGold = (TextView) findViewById(R.id.firm_order_gold);
		freight_tv = (TextView) findViewById(R.id.firm_order_freight);
		if (type_intent != null && type_intent.equals("exchange")) {
			confirm.setVisibility(View.VISIBLE);
		} else {
			accept.setVisibility(View.VISIBLE);
		}
		changeAddress.setOnClickListener(this);
		confirm.setOnClickListener(this);
		accept.setOnClickListener(this);
		if (type_intent != null && type_intent.equals("exchange")) {
			String name = getIntent().getStringExtra("name");
			goodsDetail.setText(name);
			score = getIntent().getStringExtra("score");
			goodsGold.setText(goodsGold.getText() + score);
			String img = getIntent().getStringExtra("img");
			if (img != null) {
				ImageLoaderUtil.loadImage(img, goodsImg);
			}
			freight = getIntent().getStringExtra("freight");
			freight_tv.setText(freight + "元");
		}
		if (type_intent != null && type_intent.equals("getprize")) {
			goodsDetail.setText(getIntent().getStringExtra("prizename"));
			goodsGold.setText(getIntent().getStringExtra("prizecode"));
		} else {
			listData = (WinningListData) getIntent().getSerializableExtra(
					HeadBaseActivity.intentKey);
			if (listData != null) {
				goodsDetail.setText(listData.getPrizeName());
				goodsGold.setText(listData.getWinGrade() + "等奖");
				goodsGold.setTextColor(color.common);
				ImageLoaderUtil.loadImage(listData.getPrizeImgurl(), goodsImg);
			}
		}

	}

	private void requestExchangeData(Object entry) {
		gson = new Gson();
		request = gson.toJson(entry);
		System.out.println("确认领奖界面：" + request);
		// result = HttpConnectUtil.updata(request, false, null, null);
		HttpConnectTool.update(request, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				LogUtil.i("firm_order_result====" + result);
				System.out.println("确认领奖界面：" + result);
				acceptOrderResult = gson.fromJson(result,
						AcceptOrderResult.class);
				if (acceptOrderResult.p.isTrue) {
					type = acceptOrderResult.p.type;
					isSuccess = acceptOrderResult.p.isSucce;
					if (isSuccess) {
						dialog.dismiss();
						IntegralOperationfUtil.minusIntegral(
								FirmOrderActivity.this, Integer.parseInt(score));
						showExchangeSuccessDialog();

					} else {
						switch (type) {
						case 1:
							Toast.makeText(FirmOrderActivity.this,
									"兑换失败，请稍后再试", Toast.LENGTH_SHORT).show();
							break;
						case 2:
							Toast.makeText(FirmOrderActivity.this, "没有找到该商品",
									Toast.LENGTH_SHORT).show();
							break;
						case 3:
							Toast.makeText(FirmOrderActivity.this, "商品数量不足",
									Toast.LENGTH_SHORT).show();
							break;
						case 4:
							Toast.makeText(FirmOrderActivity.this, "商品已下架",
									Toast.LENGTH_SHORT).show();
							break;
						case 5:
							Toast.makeText(FirmOrderActivity.this, "拍币不足",
									Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				} else {
					startForActivity(mContext, LoginActivity.class, null);
				}

			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(FirmOrderActivity.this, "访问网络失败",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showExchangeDialog() {
		dialog = DialogUtils.showDialog(this, "是否确认消耗" + score + "拍币", "",
				new int[] { R.string.firm, R.string.cancel },
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.left:
							
							if(GetUserIdUtil.getSorce(mContext)>=Integer.parseInt(score)){
								System.out.println("当前拍币数量："+GetUserIdUtil.getSorce(mContext));
								getExchangeData();
								requestExchangeData(acceptOrderRequest);
							}else{
								Toast.makeText(mContext, "亲，您的拍币数量不足哦", 1).show();
							}
							
							// dialog.cancel();
							break;
						case R.id.right:

							dialog.cancel();
							break;
						}
					}
				});
		dialog.setCanceledOnTouchOutside(false);
	}

	private void showExchangeSuccessDialog() {
		successDialog = DialogUtils.showDialog(this, "兑换成功", "", new int[] {
				R.string.sure, R.string.share }, new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.left:

					finish();
					// dialog.cancel();
					break;
				case R.id.right:
					share.postShare();
					dialog.cancel();
					successDialog.cancel();
					break;
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
	}

	private void getExchangeData() {
		String userId = GetUserIdUtil.getUserId(this);
		String tokenId = GetUserIdUtil.getTokenId(this);
		if (userId == null) {
			return;
		} else {
			acceptOrderRequest.p.userId = userId;
		}
		if (tokenId == null) {
			return;
		} else {
			acceptOrderRequest.p.tokenId = tokenId;
		}

		acceptOrderRequest.p.area = area;
		acceptOrderRequest.p.city = city;
		acceptOrderRequest.p.detailedAddress = detailAddress;
		acceptOrderRequest.p.name = name;
		acceptOrderRequest.p.num = "1";
		acceptOrderRequest.p.province = province;
		acceptOrderRequest.p.goodsId = goodsId;
		acceptOrderRequest.p.telephone = phoneNum;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firm_order_ll:// 选择收货地址
			Intent intent1 = new Intent(this, MoreAddressActivity.class);
			if (defultAddressResult.p.receiveAddress == null) {
				startActivityForResult(intent1, GET_CODE);
				return;
			} else {
				point = defultAddressResult.p.receiveAddress.id;
				if (point != null) {
					intent1.putExtra("point", Integer.valueOf(point).intValue());
				} else {
					intent1.putExtra("point", 0);
				}
			}
			startActivityForResult(intent1, GET_CODE);
			// finish();
			break;
		case R.id.firm_order_btn:// 确认订单按钮
			if (name_tv.getText().equals("请选择")
					|| phoneNum_tv.getText().equals("")
					|| detailAddress_tv.getText().equals("请选择")) {
				Toast.makeText(mContext, "请填写完整的收货信息", Toast.LENGTH_LONG)
						.show();
				return;
			}

			showExchangeDialog();
			break;
		case R.id.firm_Prize_btn:// 确认领奖按钮
			if (type_intent.equals("getprize")) {
				//ToastUtils.makeText(mContext, "这是领奖按钮", 0).show();
				getPrizeData();
			} else {
				getData();
				requestData(acceptPrizeRequest);
			}
			break;
		default:
			break;
		}

	}
	//向服务端请求数据，判断该奖品是否已经领取
		public void getPrizeData(){
			GetPrizeDataRequest getPrizeDataRequest=new GetPrizeDataRequest();
			getPrizeDataRequest.p.userId=GetUserIdUtil.getUserId(mContext);
			getPrizeDataRequest.p.tokenId=GetUserIdUtil.getTokenId(mContext);
			intent = getIntent(); 
			if(intent !=null){
				awardId = intent.getStringExtra("mealRecordId");
				System.out.println("awardId"+awardId);
			}
			getPrizeDataRequest.p.awardId=awardId;
			final Gson gson=new Gson();
			String jsonData=gson.toJson(getPrizeDataRequest);
			System.out.println("请求参数："+jsonData);
			HttpConnectTool.update(jsonData, mContext, new ConnectListener() {
				
				@Override
				public void contectSuccess(String result) {
					GetPrizeDataResponse getPrizeDataResponse=gson.fromJson(result, GetPrizeDataResponse.class);
					System.out.println("领取按钮的返回信息result："+result);
					if(getPrizeDataResponse.p.isTrue){
						if(getPrizeDataResponse.p.isSucce){
							Toast.makeText(mContext, "恭喜您，您的奖品领取成功", 1).show();
							Timer timer=new Timer();
							TimerTask task=new TimerTask() {
								
								@Override
								public void run() {
									Intent intent=new Intent();
									setResult(123);
									finish();
								}
							};
							timer.schedule(task, 2000);
						}else{
							Toast.makeText(mContext, "您的奖品领取失败", 1).show();
						}
					}else{
						Toast.makeText(mContext, "您的身份信息过期，请重新登录", 1).show();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		if (requestCode == GET_CODE) {

			if (resultCode == RESULT_OK) {

				address = (Address) data.getSerializableExtra("Address");
				// type_intent = data.getStringExtra("type");
				name = address.name;
				phoneNum = address.telephone;
				detailAddress = address.detailedAddress;
				name_tv.setText(name);
				phoneNum_tv.setText(phoneNum);
				detailAddress_tv.setText(detailAddress);
				province = address.province;
				city = address.city;
				area = address.area;
				// goodsId = "" + address.id;
			}

		}
	}
}
