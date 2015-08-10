package com.jishijiyu.takeadvantage.activity.exchangemall;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.R.color;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.result.ExchangeDetailsPostdata;
import com.jishijiyu.takeadvantage.entity.result.ExchangeDetailsPostdata.BranchData;
import com.jishijiyu.takeadvantage.entity.result.ExchangmallResult;
import com.jishijiyu.takeadvantage.entity.result.ExchangmallResult.ExchangListData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.String_U;

/**
 * 
 * 兑换商城
 * 
 */
public class ExchangemallActivity extends HeadBaseActivity {
	private GridView mGridView;
	private PullToRefreshListView mListview;
	private MyAdapter<Images> gridAdapter;
	private MyAdapter<ExchangListData> adapter;
	private List<Images> images;
	int[] arraylist = { 11, 12, 13, 14, 17, 15, 16, 0 };
	private int mPage;
	private String tokenId, userId;
	private final int gvVerticalSpacing = 15;
	private final int gvNumColumn = 4;
	
	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.exchangemall));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ExchangemallActivity.this,
				R.layout.exchangemall, null);

		base_centent.addView(view);
		text = (TextView) findViewById(R.id.exchange_hot);
		text.setText("");
		AppManager.getAppManager().addActivity(this);
		mGridView = new GridView(mContext);
		mGridView.setNumColumns(gvNumColumn);
		mGridView.setVerticalSpacing(gvVerticalSpacing);
		mGridView.setSelector(android.R.color.transparent);
		mGridView.setBackgroundResource(R.drawable.exchange_mall_bg_title);
		
		TextView textView=new TextView(mContext);
		textView.setText("热门兑换");
		textView.setTextColor(getResources().getColor(R.color.wheat));
		textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_bg));
		
		mListview = (PullToRefreshListView) view
				.findViewById(R.id.lv_listCommodity);
		ListView listView = mListview.getListView();
		listView.addHeaderView(mGridView);
		listView.addHeaderView(textView);
		images = new ArrayList<Images>();
		tokenId = GetUserIdUtil.getTokenId(this);
		userId = GetUserIdUtil.getUserId(this);
		final int imgId[] = { R.drawable.btn1_cloth, R.drawable.btn2_phone,
				R.drawable.btn3_makeup, R.drawable.btn4_, R.drawable.btn5_sofa,
				R.drawable.btn6_bag, R.drawable.btn7_tv, R.drawable.btn8_all };

		int imgNameId[] = { R.string.btn1, R.string.btn2, R.string.btn3,
				R.string.btn4, R.string.btn5, R.string.btn6, R.string.btn7,
				R.string.btn8 };

		for (int i = 0; i < imgId.length; i++) {
			Images image = new Images();
			image.imgId = imgId[i];
			image.imgName = getResources().getString(imgNameId[i]);
			images.add(image);
		}
		// gridadapter的适配器
		gridAdapter = new MyAdapter<ExchangemallActivity.Images>(
				ExchangemallActivity.this, images, R.layout.exchangemall_item) {

			@SuppressLint("NewApi")
			@Override
			public void convert(ViewHolder helper, int posittion, Images item) {

				helper.setText(R.id.tv_physicalCommodity, item.imgName);
				Drawable drawable = ExchangemallActivity.this.getResources()
						.getDrawable(imgId[posittion]);
				((TextView) helper.getView(R.id.tv_physicalCommodity))
						.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
								drawable, null, null);

			}
		};
		mGridView.setAdapter(gridAdapter);
		setGridViewHeightBasedOnChildren(mGridView);

		// gridview的监听
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startForActivity(ExchangemallActivity.this,
						HomeAppliancesActivity.class, arraylist[arg2]);
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
						// ++mPage;
						// getHttpData(mPage, false);
						mListview.onRefreshComplete();
					}
				});

	}
	/**
	 * @param gridView
	 *            
	 */
	private void setGridViewHeightBasedOnChildren(GridView gridView) {

		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < ((listAdapter.getCount()-1)/gvNumColumn) + 1; i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		gridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, totalHeight+gvVerticalSpacing*2));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHttpData(0, true);
	}

	public void getHttpData(int page, final boolean isUpOrDown) {
		ExchangeDetailsPostdata postdata = new ExchangeDetailsPostdata();
		BranchData data = new BranchData();
		data.setPage(page + "");
		data.setPageSize("10");
		data.setTokenId(tokenId);
		data.setUserId(userId);
		postdata.setC(Constant.EXCHANGEMALL_CODE);
		postdata.setP(data);
		final Gson gson = new Gson();
		String json = gson.toJson(postdata);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				if (result != null && !String_U.equal(result, "")) {
					ExchangmallResult jsonobj = gson.fromJson(result,
							ExchangmallResult.class);
					if (jsonobj.getP().isTrue()) {
						if (jsonobj.getP().getHotExchangeList() != null
								&& jsonobj.getP().getHotExchangeList().size() > 0) {

							final List<ExchangListData> list = jsonobj.getP()
									.getHotExchangeList();
							if (adapter == null) {
								// listview的适配器
								adapter = new MyAdapter<ExchangListData>(
										ExchangemallActivity.this, list,
										R.layout.exchangemall_item2) {

									@Override
									public void convert(ViewHolder helper,
											final int position,
											ExchangListData item) {
										helper.setImageBitmap(
												R.id.ig_eImageview,
												list.get(position)
														.getGoodsImgUrl1());
										helper.setText(R.id.tv_eName,
												list.get(position)
														.getGoodsBrand());
										helper.setText(R.id.tv_eIntegral, list
												.get(position).getScore()
												+ "拍币");
										helper.setText(R.id.tv_eUserNum, "库存:"
												+ list.get(position)
														.getStockNumber());
										helper.setOnclick(R.id.tv_exchange,
												new OnClickListener() {

													@Override
													public void onClick(
															View arg0) {

														Intent intent = new Intent(
																ExchangemallActivity.this,
																FirmOrderActivity.class);
														intent.putExtra("type",
																"exchange");
														intent.putExtra(
																"goodsId",
																list.get(
																		position)
																		.getId());
														intent.putExtra(
																"name",
																list.get(
																		position)
																		.getGoodsName());
														intent.putExtra(
																"score",
																list.get(
																		position)
																		.getScore());
														intent.putExtra(
																"img",
																list.get(
																		position)
																		.getGoodsImgUrl1());
														intent.putExtra(
																"freight",
																list.get(
																		position)
																		.getFreight());
														startActivity(intent);
													}
												});
									}

								};
								mListview.setAdapter(adapter);
							} else {
								if (isUpOrDown) {
									adapter.upData(list);

								} else {
									adapter.AddData(list);
								}
								mListview.onRefreshComplete();
								adapter.notifyDataSetChanged();
							}
							mIntents(list);

						} else {
							mListview.onRefreshComplete();
						}
					} else {
						IntentActivity.mIntent(ExchangemallActivity.this);
					}
				}

			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				System.out.println("走下面");
					text.setText("热门兑换");
			}
		});
	}

	public void mIntents(final List<ExchangListData> list) {
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startForActivity(ExchangemallActivity.this,
						ExchangeDetailsActivity.class, list.get(arg2).getId());
			}
		});
	}

	AlertDialog alertDialog = null;
	private TextView text;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager()
					.finishActivity(ExchangemallActivity.this);
			break;

		}
	}

	private class Images {
		public int imgId;
		public String imgName;
	}
}
