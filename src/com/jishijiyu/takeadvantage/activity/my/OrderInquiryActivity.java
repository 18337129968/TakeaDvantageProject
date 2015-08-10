package com.jishijiyu.takeadvantage.activity.my;

import java.util.List;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.myinformation.MyInfomationActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.ConfirmReceivingRequest;
import com.jishijiyu.takeadvantage.entity.request.ConfirmReceivingRequest.Confirmdata;
import com.jishijiyu.takeadvantage.entity.request.OrderinquiryRequest;
import com.jishijiyu.takeadvantage.entity.request.OrderinquiryRequest.InquiryData;
import com.jishijiyu.takeadvantage.entity.result.ConfirmReceivingResult;
import com.jishijiyu.takeadvantage.entity.result.OrderinQuiryResult;
import com.jishijiyu.takeadvantage.entity.result.OrderinQuiryResult.OrderListdata;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.TimerUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.String_U;
import com.tencent.wxop.stat.t;

/**
 * 订单查询
 * 
 * @author zm
 * 
 */
public class OrderInquiryActivity extends HeadBaseActivity {
	private PullToRefreshListView OrderInquiryListview;
	private TextView sendOrderNumber, tv_newNohave;
	@SuppressWarnings("rawtypes")
	private MyAdapter adapter;
	AlertDialog dialogs;
	int mPage;
	private String tokenId;

	@Override
	public void onClick(View v) {

	}

	private String userId;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.order_inquiry));
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
		View view = View.inflate(OrderInquiryActivity.this,
				R.layout.order_inquiry, null);
		userId = GetUserIdUtil.getUserId(OrderInquiryActivity.this);
		tokenId = GetUserIdUtil.getTokenId(this);
		base_centent.addView(view);
		OrderInquiryListview = (PullToRefreshListView) view
				.findViewById(R.id.lv_listview);

		sendOrderNumber = (TextView) view.findViewById(R.id.send_order_number);
		tv_newNohave = (TextView) view.findViewById(R.id.tv_newNohave);
		OrderInquiryListview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mPage = 0;
						getOrderInData(mPage, true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						++mPage;
						getOrderInData(mPage, false);
					}
				});
		getOrderInData(0, true);
	}

	/**
	 * 访问网络请求数据
	 */
	public void getOrderInData(int page, final boolean isDownOrUp) {
		OrderinquiryRequest orderinquiryRequest = new OrderinquiryRequest();
		InquiryData data = new InquiryData();
		data.setUserId(userId);
		data.setPage(page + "");
		data.setTokenId(tokenId);
		data.setPageSize("10");
		orderinquiryRequest.setC(Constant.QRDERIN_QUIRY_CODE);
		orderinquiryRequest.setP(data);
		final Gson gson = new Gson();
		String json = gson.toJson(orderinquiryRequest);

		HttpConnectTool.update(json, this, new ConnectListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void contectSuccess(String result) {
				if (result != null) {
					OrderinQuiryResult jsonobj = gson.fromJson(result,
							OrderinQuiryResult.class);
					sendOrderNumber.setText(jsonobj.getP().getNumber());
					final List<OrderListdata> list = jsonobj.getP().getList();
					if (jsonobj.getP().isTrue()) {
						if (list != null && list.size() > 0) {

							if (adapter == null) {

								adapter = new MyAdapter(
										OrderInquiryActivity.this, list,
										R.layout.order_inquiry_item) {

									@Override
									public void convert(ViewHolder helper,
											final int position, Object item) {
										helper.setImageBitmap(
												R.id.order_inquiry_img, list
														.get(position)
														.getGoodsImgUrl());

										helper.setText(R.id.commodity_name,
												"商品名称："
														+ list.get(position)
																.getGoodsName());
										helper.setText(R.id.consume_integral,
												"消耗拍币:"
														+ list.get(position)
																.getScore());
										helper.setText(R.id.waybill_num,
												"商品数量："
														+ list.get(position)
																.getNum());
										helper.setText(
												R.id.bill_data,
												"下单日期:"
														+ TimerUtil.timeo(list
																.get(position)
																.getOrderTime()
																+ "",
																"yyyy年MM月dd日"));
										// 1：未发货 2：已发货 3：已收货
										if (String_U.equal(list.get(position)
												.getState(), "1")) {
											helper.setText(
													R.id.expressage_category,
													"未发货");
										} else if (String_U.equal(
												list.get(position).getState(),
												"2")) {
											helper.setText(
													R.id.expressage_category,
													list.get(position)
															.getMailCompany()
															+ ":" + "已发货");
										}  
										helper.setOnclick(R.id.firm_order_btn,
												new OnClickListener() {

													@Override
													public void onClick(
															View arg0) {

														confirmDialog(list.get(
																position)
																.getId());
													}
												});

									}
								};
								OrderInquiryListview.setAdapter(adapter);
							} else {
								if (isDownOrUp) {
									adapter.upData(list);

								} else {
									adapter.AddData(list);
								}
								OrderInquiryListview.onRefreshComplete();
								adapter.notifyDataSetChanged();
							}

						} else {
							// tv_newNohave.setVisibility(View.VISIBLE);
							// OrderInquiryListview.setVisibility(View.GONE);
							// sendOrderNumber.setText("0");
							OrderInquiryListview.onRefreshComplete();
						}
					} else {
						IntentActivity.mIntent(OrderInquiryActivity.this);
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

	/** 确定按钮的操作 */
	public void confirmDialog(final String id) {

		dialogs = DialogUtils.showDialog(OrderInquiryActivity.this, "是否要确定收货?",
				new int[] { R.string.ext, R.string.end },
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						switch (arg0.getId()) {
						case R.id.left:
							dialogs.dismiss();
							break;
						case R.id.right:
							confirmReceiving(id);
							dialogs.dismiss();
							break;

						default:
							break;
						}

					}
				}, false);
		DialogUtils.setTitle("提示");

	}

	/** 收货 */
	public void confirmReceiving(String id) {
		ConfirmReceivingRequest receivingRequest = new ConfirmReceivingRequest();
		receivingRequest.setC(Constant.CONFIRM_CODE);
		Confirmdata confirmdata = new Confirmdata();
		confirmdata.setId(id);
		confirmdata.setUserId(userId);
		confirmdata.setTokenId(GetUserIdUtil.getTokenId(this));
		receivingRequest.setP(confirmdata);
		final Gson gson = new Gson();
		String json = gson.toJson(receivingRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				if (result != null) {
					ConfirmReceivingResult jsonobj = gson.fromJson(result,
							ConfirmReceivingResult.class);
					if (jsonobj.getP().isTrue()) {

						if (jsonobj.getP().isSucce()) {
							getOrderInData(0, true);
						} else {
							ToastUtils.makeText(OrderInquiryActivity.this, "提交失败请重新提交！", 0).show();
						}
					} else {
						IntentActivity.mIntent(OrderInquiryActivity.this);
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
}
