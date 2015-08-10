package com.jishijiyu.takeadvantage.activity.myinformation;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.WinningPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.WinningPriceRequest.WinningData;
import com.jishijiyu.takeadvantage.entity.result.WinningPriceResult;
import com.jishijiyu.takeadvantage.entity.result.WinningPriceResult.WinningListData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.TimerUtil;

/**
 * 拍奖中奖
 * 
 * @author zm
 */
public class WinningPriceActivity extends HeadBaseActivity {
	private PullToRefreshListView mListview = null;
	private MyAdapter<WinningListData> adapter = null;
	private String userId, id, tokenId;
	List<WinningListData> mList = new ArrayList<WinningPriceResult.WinningListData>();
	int mPage;

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.winning_price);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(WinningPriceActivity.this,
				R.layout.winning_price, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		userId = GetUserIdUtil.getUserId(WinningPriceActivity.this);
		tokenId = GetUserIdUtil.getTokenId(this);
		mListview = (PullToRefreshListView) view.findViewById(R.id.listview);

		getHttpData(0, true);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startForActivity(mContext,
						WinningRecordGoodsDetailActivty.class,
						mList.get(position).getId());
			}
		});
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
	}

	public void getHttpData(int page, final boolean isDownOrUp) {
		WinningPriceRequest request = new WinningPriceRequest();
		WinningData data = new WinningData();
		data.setUserId(userId);
		data.setPage(page + "");
		data.setTokenId(tokenId);
		data.setPageSize("10");
		request.setC(Constant.WINNINGPRICE_CODE);
		request.setP(data);
		final Gson gson = new Gson();
		String json = gson.toJson(request);

		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (result != null) {
					WinningPriceResult jsonobj = gson.fromJson(result,
							WinningPriceResult.class);
					if (jsonobj.getP().isTrue()) {
						if (jsonobj.getP().getWinList() != null
								&& jsonobj.getP().getWinList().size() > 0) {
							final List<WinningListData> list = jsonobj.getP()
									.getWinList();
							mList.addAll(list);
							if (adapter == null) {
								adapter = new MyAdapter<WinningListData>(
										WinningPriceActivity.this, list,
										R.layout.winning_price_item) {

									@Override
									public void convert(ViewHolder helper,
											int position, WinningListData item) {
										helper.setText(R.id.tv_wName, "拍"
												+ list.get(position)
														.getWinGrade()
												+ "等奖:"
												+ list.get(position)
														.getPrizeName());
										helper.setText(
												R.id.tv_wTime,
												"开奖日期："
														+ TimerUtil.timeo(list
																.get(position)
																.getWinTime(),
																"yyyy年MM月dd日"));
										ImageLoaderUtil
												.loadImage(
														list.get(position)
																.getPrizeImgurl(),
														(ImageView) helper
																.getView(R.id.imv_wImageView));
									}

								};
								mListview.setAdapter(adapter);

							} else {
								if (isDownOrUp) {
									mList.clear();
									adapter.upData(list);
									mList.addAll(list);

								} else {
									adapter.AddData(list);
									mList.addAll(list);
								}
								mListview.onRefreshComplete();
								adapter.notifyDataSetChanged();
							}
						} else {
							mListview.onRefreshComplete();
						}
					} else {
						IntentActivity.mIntent(WinningPriceActivity.this);
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

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager()
					.finishActivity(WinningPriceActivity.this);
			break;

		default:
			break;
		}
	}

}
