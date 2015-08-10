package com.jishijiyu.takeadvantage.activity.exchangemall;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.ExchangeDetailsData;
import com.jishijiyu.takeadvantage.entity.ExchangeDetailsData.ExchangeDetailsGoodId;
import com.jishijiyu.takeadvantage.entity.request.AcceptOrderRequest;
import com.jishijiyu.takeadvantage.entity.result.AcceptOrderResult;
import com.jishijiyu.takeadvantage.entity.result.ExchangeDetailsPostdata;
import com.jishijiyu.takeadvantage.entity.result.ExchangeDetailsPostdata.BranchData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.TimerUtil;

/**
 * 兑换详情
 * 
 * @author zm
 */
public class ExchangeDetailsActivity extends HeadBaseActivity {
	private TextView tv_conversion;
	private TextView tv_integral;
	private TextView tv_waresname;
	private TextView tv_waresprice;
	private TextView tv_waresaddress;
	private TextView tv_groundingtime;
	private TextView tv_undercarriagetime;
	private TextView tv_completenum;
	private TextView tv_surplusnum;
	private ImageView img_warespicture;
	private String goodsId, score, name, img, freight;
	private String userId, tokenId;
	private AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest();
	private AcceptOrderResult acceptOrderResult = new AcceptOrderResult();
	private String addressResult, province, city, area, phoneNum,
			detailAddress;
	private Gson gson;
	private Boolean issuccess, isSuccess;
	Dialog dialog, successDialog;
	String request, addressRequest, type_intent;
	int type;
	ShareUtil share;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.exchangedetails));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {

		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ExchangeDetailsActivity.this,
				R.layout.exchange_details, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		tokenId = GetUserIdUtil.getTokenId(this);
		userId = GetUserIdUtil.getUserId(this);

		tv_conversion = (TextView) view.findViewById(R.id.tv_conversion);
		tv_surplusnum = (TextView) view.findViewById(R.id.tv_surplusnum);
		tv_completenum = (TextView) view.findViewById(R.id.tv_completenum);
		tv_groundingtime = (TextView) view.findViewById(R.id.tv_groundingtime);
		tv_undercarriagetime = (TextView) view
				.findViewById(R.id.tv_undercarriagetime);
		tv_integral = (TextView) view.findViewById(R.id.tv_integral);
		tv_waresname = (TextView) view.findViewById(R.id.tv_waresname);
		tv_waresprice = (TextView) view.findViewById(R.id.tv_waresprice);
		tv_waresaddress = (TextView) view.findViewById(R.id.tv_waresaddress);
		img_warespicture = (ImageView) view.findViewById(R.id.img_warespicture);
		tv_conversion.setOnClickListener(this);
		goodsId = (String) getIntent().getSerializableExtra(
				HeadBaseActivity.intentKey);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getDetailsData();
	}

	/**
	 * 请求数据
	 */
	public void getDetailsData() {
		ExchangeDetailsPostdata postdata = new ExchangeDetailsPostdata();
		BranchData branchData = new BranchData();
		branchData.setGoodsId(goodsId);
		branchData.setTokenId(tokenId);
		branchData.setUserId(userId);
		postdata.setC(Constant.EXCHANGEDATAILSPOST_DATA);
		postdata.setP(branchData);

		final Gson gson = new Gson();
		String json = gson.toJson(postdata);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (result != null) {

					ExchangeDetailsData jsonobj = gson.fromJson(result,
							ExchangeDetailsData.class);
					if (jsonobj.getP().isTrue()) {
						ExchangeDetailsGoodId goodsInfo = jsonobj.getP()
								.getGoodsInfo();

						name = goodsInfo.getGoodsName();
						freight = goodsInfo.getFreight();
						tv_waresname.setText(name
								+ goodsInfo.getSpecification());
						tv_waresprice.setText("市场价：¥" + goodsInfo.getPrice());
						tv_waresaddress.setText("销售商家："
								+ goodsInfo.getCompanyName());
						tv_groundingtime.setText("上架时间："
								+ TimerUtil.timeo(goodsInfo.getCreateTime()
										+ "", "yyyy-MM-dd"));
						tv_undercarriagetime.setText("下架时间："
								+ TimerUtil.timeo(goodsInfo.getDeadline() + "",
										"yyyy-MM-dd"));

						// tv_completenum.setText("已兑换件数：" + "100000" + "件");
						tv_surplusnum.setText("剩余件数："
								+ goodsInfo.getStockNumber() + "件");
						img = goodsInfo.getGoodsImgUrl1();
						if (img != null) {

							ImageLoaderUtil.loadImage(img, img_warespicture);
						} else {
							ImageLoaderUtil.loadImage(
									"http://192.168.0.88/mallImg/null.jpg",
									img_warespicture);
						}
						score = goodsInfo.getScore();
						tv_integral.setText(name + "  " + score + "拍币");
					} else {
						IntentActivity.mIntent(ExchangeDetailsActivity.this);
					}
				} else {
					return;
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
		// String result = HttpConnectUtil.updata(json, false,
		// ExchangeDetailsActivity.this, null);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					ExchangeDetailsActivity.this);
			break;
		// 兑换
		case R.id.tv_conversion:
			dialog = DialogUtils.showDialog(this, "是否确认消耗" + score + "拍币", "",
					new int[] { R.string.firm, R.string.cancel },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.left:
								getExchangeData();
								requestExchangeData(acceptOrderRequest);
								// dialog.cancel();
								break;
							case R.id.right:

								dialog.cancel();
								break;
							}
						}
					});
			dialog.setCanceledOnTouchOutside(false);
			// Intent intent = new Intent(ExchangeDetailsActivity.this,
			// FirmOrderActivity.class);
			// intent.putExtra("type", "exchange");
			// intent.putExtra("goodsId", goodsId);
			// intent.putExtra("score", score);
			// intent.putExtra("name", name);
			// intent.putExtra("img", img);
			// intent.putExtra("freight", freight);
			// startActivity(intent);
			// finish();
			// startForActivity(ExchangeDetailsActivity.this,
			// FirmOrderActivity.class, null);
			break;
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
								ExchangeDetailsActivity.this,
								Integer.parseInt(score));
						showExchangeSuccessDialog();

					} else {
						switch (type) {
						case 1:
							Toast.makeText(ExchangeDetailsActivity.this,
									"兑换失败，请稍后再试", Toast.LENGTH_SHORT).show();
							break;
						case 2:
							Toast.makeText(ExchangeDetailsActivity.this,
									"没有找到该商品", Toast.LENGTH_SHORT).show();
							break;
						case 3:
							Toast.makeText(ExchangeDetailsActivity.this,
									"商品数量不足", Toast.LENGTH_SHORT).show();
							break;
						case 4:
							Toast.makeText(ExchangeDetailsActivity.this,
									"商品已下架", Toast.LENGTH_SHORT).show();
							break;
						case 5:
							Toast.makeText(ExchangeDetailsActivity.this,
									"拍币不足", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(ExchangeDetailsActivity.this, "访问网络失败",
						Toast.LENGTH_SHORT).show();
			}
		});
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
}
