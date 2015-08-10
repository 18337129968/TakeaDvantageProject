package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.myfriend.MerchantDatalActivity;
import com.jishijiyu.takeadvantage.adapter.HistoryWinRecordAdapter;
import com.jishijiyu.takeadvantage.entity.request.BeforeWinRecordRequest;
import com.jishijiyu.takeadvantage.entity.result.BeforeWinRecordResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 奖品详情界面
 * 
 * @author shifeiyu
 * 
 */
public class TaoCanPrizeActivity extends HeadBaseActivity {
	View v;
	String CompanyId = "", userId = "", tokenId = "", PrizeName, PrizePrice,
			PrizeImg, PrizeExplain, PrizeId, PackageId, CompanyName;
	List<Map<String, Object>> list;
	Map<String, Object> map;
	ImageView image, taocan_prize_image;
	TextView tv1, tv2, tv3, tv4, tv5, taocan_prize_name, taocan_prize_price,
			taocan_prize_merchant, check_merchant_btn;
	ExpandableListView exListview;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.btn_top_left:
			finish();
			break;
		// 查看商家
		case R.id.check_merchant_btn:
			startForActivity(mContext, MerchantDatalActivity.class, null);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("奖品详情");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(TaoCanPrizeActivity.this,
				R.layout.activity_taocan_prize, null);
		base_centent.addView(view);
		exListview = (ExpandableListView) findViewById(R.id.expandableListView1);
		v = LayoutInflater.from(mContext).inflate(R.layout.layout_taocan_prize,
				null);
		exListview.addHeaderView(v);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {

		taocan_prize_image = (ImageView) v
				.findViewById(R.id.taocan_prize_image);
		taocan_prize_name = (TextView) v.findViewById(R.id.taocan_prize_name);
		taocan_prize_price = (TextView) v.findViewById(R.id.taocan_prize_price);
		taocan_prize_merchant = (TextView) v
				.findViewById(R.id.taocan_prize_merchant);
		check_merchant_btn = (TextView) v.findViewById(R.id.check_merchant_btn);
		check_merchant_btn.setOnClickListener(this);
		userId = GetUserIdUtil.getUserId(mContext);
		tokenId = GetUserIdUtil.getTokenId(mContext);
		Bundle bundle = getIntent().getExtras();
		System.out.println(bundle.get("prizeImg"));
		if (bundle != null) {
			CompanyId = bundle.getString("companyId");
			PrizeImg = bundle.getString("prizeImg");
			PrizePrice = bundle.getString("prizePrice");
			PrizeName = bundle.getString("prizeName");
			PrizeExplain = bundle.getString("prizeExplain");
			PrizeId = bundle.getString("prizeId");
			PackageId = bundle.getString("packageId");
			CompanyName = bundle.getString("companyName");
			setPrizeInfo();
			getWinRecordInfo();
		}

	}

	// 设置奖品信息
	private void setPrizeInfo() {
		ImageLoaderUtil.loadImage(PrizeImg, taocan_prize_image);
		taocan_prize_name.setText(PrizeName + "\n" + PrizeExplain);
		taocan_prize_price.setText("市场价：￥" + PrizePrice);
		taocan_prize_merchant.setText("销售商家:" + CompanyName);
	}

	// 获取中奖记录信息
	private void getWinRecordInfo() {
		final Gson gson = new Gson();
		BeforeWinRecordRequest winRequest = new BeforeWinRecordRequest();
		winRequest.p.packageId = PackageId;
		winRequest.p.prizeId = PrizeId;
		winRequest.p.page = "0";
		winRequest.p.pageSize = "30";
		winRequest.p.tokenId = tokenId;
		winRequest.p.userId = userId;
		String json = gson.toJson(winRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					BeforeWinRecordResult winResult = gson.fromJson(result,
							BeforeWinRecordResult.class);
					if (winResult.p.isTrue) {
						if (winResult.p.isSucce) {
							if (winResult.p.historyWinningList != null
									&& winResult.p.historyWinningList.size() > 0
									&& winResult.p.historyWinningList.get(0).list
											.size() > 0) {
								exListview.setGroupIndicator(null);
								exListview
										.setAdapter(new HistoryWinRecordAdapter(
												TaoCanPrizeActivity.this,
												winResult.p.historyWinningList));
								exListview.expandGroup(0);
								// int count = exListview.getCount();
								// for (int i = 0; i < count; i++) {
								// exListview.expandGroup(i);
								// }

							} else {
								exListview
										.setAdapter(new HistoryWinRecordAdapter(
												TaoCanPrizeActivity.this,
												winResult.p.historyWinningList));
								ToastUtils.makeText(mContext, "暂无历史中奖记录", 0)
										.show();
							}
						} else {
							ToastUtils.makeText(mContext, "暂无历史中奖记录", 0).show();
						}

					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
					}
				}

			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "服务器连接失败", 0).show();
			}
		});
	}
}
