package com.jishijiyu.takeadvantage.activity.myinformation;

import java.util.List;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.my.OrderInquiryActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.OrderinquiryRequest;
import com.jishijiyu.takeadvantage.entity.request.OrderinquiryRequest.InquiryData;
import com.jishijiyu.takeadvantage.entity.result.AwardingGoodsData;
import com.jishijiyu.takeadvantage.entity.result.AwardingGoodsData.AwardingListData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.TimerUtil;
import com.tencent.a.a.a.a.h;

/**
 * 兑奖商品
 * 
 * @author zm
 */
public class AwardingGoodsActivity extends HeadBaseActivity {
	private PullToRefreshListView mListview = null;
	private MyAdapter<AwardingListData> adapter = null;
	String userId, tokenId;
	int mPage;
	private TextView tv_awaNum;

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.exchange_products);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AwardingGoodsActivity.this,
				R.layout.awarding_goods_item, null);
		base_centent.addView(view);
		userId = GetUserIdUtil.getUserId(AwardingGoodsActivity.this);
		tokenId = GetUserIdUtil.getTokenId(this);
		AppManager.getAppManager().addActivity(this);
		tv_awaNum = (TextView) view.findViewById(R.id.tv_awaNum);
		mListview = (PullToRefreshListView) view.findViewById(R.id.listview);
		mListview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mPage = 0;
						getHttpData(mPage, true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						++mPage;
						getHttpData(mPage, false);
					}
				});
		getHttpData(0, true);

	}

	public void getHttpData(int page, final boolean isDownOrUp) {
		OrderinquiryRequest orderinquiryRequest = new OrderinquiryRequest();
		InquiryData data = new InquiryData();
		data.setUserId(userId);
		data.setPage(page + "");
		data.setPageSize("10");
		data.setTokenId(tokenId);
		orderinquiryRequest.setC(Constant.AWARDINGGOODS_CODE);
		orderinquiryRequest.setP(data);
		final Gson gson = new Gson();
		String json = gson.toJson(orderinquiryRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				if (result != null) {
					AwardingGoodsData jsonobj = gson.fromJson(result,
							AwardingGoodsData.class);
					tv_awaNum.setText(jsonobj.getP().getNumber());
					if (jsonobj.getP().isTrue()) {
						if (jsonobj.getP().getList() != null
								&& jsonobj.getP().getList().size() > 0) {
							final List<AwardingListData> list = jsonobj.getP()
									.getList();

							if (adapter == null) {
								adapter = new MyAdapter<AwardingListData>(
										AwardingGoodsActivity.this, list,
										R.layout.awarding_goods) {

									@Override
									public void convert(ViewHolder helper,
											int position, AwardingListData item) {
										helper.setImageBitmap(
												R.id.imv_aImageView,
												list.get(position)
														.getGoodsImgUrl());
										helper.setText(R.id.tv_aName, "商品名称："
												+ list.get(position)
														.getGoodsName());
										helper.setText(R.id.tv_aScore, "消耗拍币："
												+ list.get(position).getScore());
										helper.setText(R.id.tv_aNum, "商品数量："
												+ list.get(position).getNum());
										helper.setText(
												R.id.tv_aTime,
												"收货日期："
														+ TimerUtil
																.timeo(list
																		.get(position)
																		.getOrderTime(),
																		"yyyy年MM月dd日"));
										helper.setText(R.id.tv_aState, "已收货");

									}

								};
								mListview.setAdapter(adapter);
							} else {
								if (isDownOrUp) {
									adapter.upData(list);

								} else {
									adapter.AddData(list);
								}
								mListview.onRefreshComplete();
								adapter.notifyDataSetChanged();
							}

						} else {
							mListview.onRefreshComplete();

						}
					} else {
						IntentActivity.mIntent(AwardingGoodsActivity.this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
