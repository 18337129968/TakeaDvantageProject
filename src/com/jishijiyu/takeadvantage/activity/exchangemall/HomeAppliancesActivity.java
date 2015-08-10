package com.jishijiyu.takeadvantage.activity.exchangemall;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshGridView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.HomeAppliancesRequest;
import com.jishijiyu.takeadvantage.entity.request.HomeAppliancesRequest.HomeAppData;
import com.jishijiyu.takeadvantage.entity.result.HomeAppliancesResult;
import com.jishijiyu.takeadvantage.entity.result.HomeAppliancesResult.HomeappListData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.String_U;

/**
 * 
 * 家电
 * 
 * @author zm
 */
public class HomeAppliancesActivity extends HeadBaseActivity {
	private PullToRefreshGridView mGridView = null;
	private MyAdapter<HomeappListData> gridAdapter = null;
	int mPage;
	int mType;
	private String tokenId, userId;
	List<HomeappListData> listData=new ArrayList<HomeAppliancesResult.HomeappListData>();
	@Override
	public void appHead(View view) {
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getHttpData(0, true);
	}

	@Override
	public void initReplaceView() {

		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(HomeAppliancesActivity.this,
				R.layout.commodity, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		tokenId = GetUserIdUtil.getTokenId(this);
		userId = GetUserIdUtil.getUserId(this);
		mGridView = (PullToRefreshGridView) view.findViewById(R.id.gridview);
		mType = (Integer) getIntent().getSerializableExtra(
				HeadBaseActivity.intentKey);
		switch (mType) {
		case 11:
			top_text.setText(getResources().getString(R.string.btn1));
			break;
		case 12:
			top_text.setText(getResources().getString(R.string.btn2));
			break;
		case 13:
			top_text.setText(getResources().getString(R.string.btn3));
			break;
		case 14:
			top_text.setText(getResources().getString(R.string.btn4));
			break;
		case 15:
			top_text.setText(getResources().getString(R.string.btn6));
			break;
		case 16:
			top_text.setText(getResources().getString(R.string.btn7));
			break;
		case 17:
			top_text.setText(getResources().getString(R.string.btn5));
			break;
		case 0:
			top_text.setText(getResources().getString(R.string.btn8));
			break;

		default:
			break;
		}
		mGridView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						mPage = 0;
						getHttpData(mPage, true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						mPage++;
						getHttpData(mPage, false);
					}
				});
	}

	public void getHttpData(int page, final boolean isUpOrDown) {
		HomeAppliancesRequest postdata = new HomeAppliancesRequest();
		HomeAppData appData = new HomeAppData();
		appData.setPage(page + "");
		appData.setPageSize("15");
		appData.setUserId(userId);
		appData.setTokenId(tokenId);
		appData.setType1(mType + "");
		postdata.setC(Constant.HOMEAPPLIANCE_CODE);
		postdata.setP(appData);
		final Gson gson = new Gson();
		String json = gson.toJson(postdata);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (result != null && !String_U.equal(result, "")) {
					HomeAppliancesResult jsonobj = gson.fromJson(result,
							HomeAppliancesResult.class);
					if(jsonobj.getP().isTrue()){
					if (jsonobj.getP().getMallList() != null
							&& jsonobj.getP().getMallList().size() > 0) {
						final List<HomeappListData> data = jsonobj.getP()
								.getMallList();
					
						if (gridAdapter == null) {
							listData.addAll(data);
							gridAdapter = new MyAdapter<HomeappListData>(
									HomeAppliancesActivity.this, data,
									R.layout.commodity_item) {

								@Override
								public void convert(ViewHolder helper,
										int position, HomeappListData item) {
									if (data.get(position).getGoodsImgUrl1() != null) {
										helper.setImageBitmap(
												R.id.imv_commodity,
												data.get(position)
														.getGoodsImgUrl1());
									} else {
										helper.setImageBitmap(
												R.id.imv_commodity,
												"http://192.168.0.88/mallImg/null.jpg");
									}
									helper.setText(R.id.tv_commodityName, data
											.get(position).getGoodsName());
									helper.setText(R.id.tv_integralNum, data
											.get(position).getScore() + "拍币");
								}

							};
							mGridView.setAdapter(gridAdapter);
						} else {
							if (isUpOrDown) {
								listData.clear();
								gridAdapter.upData(data);
								listData.addAll(data);

							} else {
								gridAdapter.AddData(data);
								listData.addAll(data);
							}
							mGridView.onRefreshComplete();
							gridAdapter.notifyDataSetChanged();
						}
						mItemClick();
					}else{
						mGridView.onRefreshComplete();
					}
				}else{
					IntentActivity.mIntent(HomeAppliancesActivity.this);
				}}

			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});

	}

	public void mItemClick( ) {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startForActivity(HomeAppliancesActivity.this,
						ExchangeDetailsActivity.class, listData.get(position)
								.getId());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			this.finish();
			break;
		}
	}

}
