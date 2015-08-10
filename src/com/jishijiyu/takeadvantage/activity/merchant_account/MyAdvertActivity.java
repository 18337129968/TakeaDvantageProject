package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.AdvertisingBeforeAdapter;
import com.jishijiyu.takeadvantage.adapter.AdvertisingCentreAdapter;
import com.jishijiyu.takeadvantage.adapter.AdvertisingEndAdapter;
import com.jishijiyu.takeadvantage.adapter.OneRmbErniePagerAdapter;
import com.jishijiyu.takeadvantage.entity.request.AdvertsingBeforeRequest;
import com.jishijiyu.takeadvantage.entity.request.AdvertsingCentreRequest;
import com.jishijiyu.takeadvantage.entity.request.AdvertsingEndRequest;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingBeforeResult;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingCentreResult;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingEndResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 我的广告界面
 * 
 * @author shifeiyu
 * 
 */
public class MyAdvertActivity extends HeadBaseActivity {
	int pageBefore = 1, pageCentre = 1, pageEnd = 1, pageSize = 10,
			beforeListCount = 0, centreListcount = 0, endListCount = 0;
	AdvertisingBeforeAdapter AdvertisingBeforeAdapter;
	AdvertisingCentreAdapter AdvertisingCentreAdapter;
	AdvertisingEndAdapter AdvertisingEndAdapter;
	List<AdvertsingBeforeResult.PostAgoList> beforeList;
	List<AdvertsingCentreResult.PostJustList> centreList;
	List<AdvertsingEndResult.PostAfterList> endList;

	View view;
	View v1, v2, v3;
	ViewPager my_advert_viewpager;
	List<View> mList;
	PullToRefreshListView listview1, listview2, listview3;
	TextView advertising_before_btn, advertising_center_btn,
			advertising_end_btn;
	List<Map<String, Object>> itemList;
	Map<String, Object> map;
	String UserId, CopId, TokenId, companyId = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.advertising_before_btn:
			my_advert_viewpager.setCurrentItem(0);
			break;
		case R.id.advertising_center_btn:
			my_advert_viewpager.setCurrentItem(1);
			break;
		case R.id.advertising_end_btn:
			my_advert_viewpager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText("我的广告");
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MyAdvertActivity.this,
				R.layout.activity_my_advert, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (!TextUtils.isEmpty(intent.getStringExtra("companyId"))) {
			companyId = intent.getStringExtra("companyId");
		}
		initView();
		initClick();
		UserId = GetUserIdUtil.getUserId(mContext);
		TokenId = GetUserIdUtil.getTokenId(mContext);
		my_advert_viewpager.setAdapter(new OneRmbErniePagerAdapter(
				getPagerView()));
		PagerChangeListener(my_advert_viewpager);
		my_advert_viewpager.setCurrentItem(0);
		advertising_before_btn.setBackgroundResource(R.drawable.btn_light);
		AdvertisingBefore();

	}

	private void initView() {
		my_advert_viewpager = (ViewPager) findViewById(R.id.my_advert_viewpager);
		advertising_before_btn = (TextView) findViewById(R.id.advertising_before_btn);
		advertising_center_btn = (TextView) findViewById(R.id.advertising_center_btn);
		advertising_end_btn = (TextView) findViewById(R.id.advertising_end_btn);
		LayoutInflater inflater = getLayoutInflater().from(mContext);
		v1 = LayoutInflater.from(mContext).inflate(R.layout.advertising, null);
		v2 = LayoutInflater.from(mContext).inflate(R.layout.advertising, null);
		v3 = LayoutInflater.from(mContext).inflate(R.layout.advertising, null);
		listview1 = (PullToRefreshListView) v1
				.findViewById(R.id.advertising_listview);
		listview2 = (PullToRefreshListView) v2
				.findViewById(R.id.advertising_listview);
		listview3 = (PullToRefreshListView) v3
				.findViewById(R.id.advertising_listview);
		listview1
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageBefore = 0;
						if (beforeList.size() != 0) {
							beforeList.clear();
						}
						AdvertisingBefore();
						listview1.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageBefore++;
						AdvertisingBefore();
						listview1.onRefreshComplete();
					}
				});
		listview2
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageCentre = 0;
						if (centreList.size() != 0) {
							centreList.clear();
						}
						AdvertisingCentre();
						listview2.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageCentre++;
						AdvertisingCentre();
						listview2.onRefreshComplete();
					}
				});
		listview3
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageEnd = 0;
						if (endList.size() != 0) {
							endList.clear();
						}
						AdvertisingEnd();
						listview3.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageEnd++;
						AdvertisingEnd();
						listview3.onRefreshComplete();
					}
				});

		beforeList = new ArrayList<AdvertsingBeforeResult.PostAgoList>();
		centreList = new ArrayList<AdvertsingCentreResult.PostJustList>();
		endList = new ArrayList<AdvertsingEndResult.PostAfterList>();
		AdvertisingBeforeAdapter = new AdvertisingBeforeAdapter(
				MyAdvertActivity.this, beforeList);
		AdvertisingCentreAdapter = new AdvertisingCentreAdapter(
				MyAdvertActivity.this, centreList);
		AdvertisingEndAdapter = new AdvertisingEndAdapter(
				MyAdvertActivity.this, endList);
		listview1.setAdapter(AdvertisingBeforeAdapter);
		listview2.setAdapter(AdvertisingCentreAdapter);
		listview3.setAdapter(AdvertisingEndAdapter);

	}

	private void initClick() {
		advertising_before_btn.setOnClickListener(this);
		advertising_center_btn.setOnClickListener(this);
		advertising_end_btn.setOnClickListener(this);
	}

	private List<View> getPagerView() {
		mList = new ArrayList<View>();
		mList.add(v1);
		mList.add(v2);
		mList.add(v3);
		return mList;

	}

	// 投放前广告
	private void AdvertisingBefore() {
		final Gson gson = new Gson();
		AdvertsingBeforeRequest beforeRequest = new AdvertsingBeforeRequest();
		beforeRequest.p.userId = UserId;
		beforeRequest.p.tokenId = TokenId;
		beforeRequest.p.companyId = companyId;
		beforeRequest.p.pageNum = pageBefore + "";
		beforeRequest.p.pageSize = pageSize + "";
		String json = gson.toJson(beforeRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					AdvertsingBeforeResult beforeResult = gson.fromJson(result,
							AdvertsingBeforeResult.class);
					if (beforeResult.p.isTrue) {
						if (beforeResult.p.postAgoList.size() > 0) {
							beforeListCount = beforeResult.p.postAgoList.size();
							for (int i = 0; i < beforeResult.p.postAgoList
									.size(); i++) {
								beforeList.add(beforeResult.p.postAgoList
										.get(i));
							}
							AdvertisingBeforeAdapter.notifyDataSetChanged();
							// listview1.setAdapter(new
							// AdvertisingBeforeAdapter(
							// MyAdvertActivity.this,
							// beforeResult.p.postAgoList));
						}
						if (beforeListCount == 0) {
							ToastUtils.makeText(mContext, "无投放前广告", 0).show();
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
				// TODO Auto-generated method stub

			}
		});
	}

	// 投放中广告
	private void AdvertisingCentre() {
		final Gson gson = new Gson();
		AdvertsingCentreRequest centreRequest = new AdvertsingCentreRequest();
		centreRequest.p.userId = UserId;
		centreRequest.p.tokenId = TokenId;
		centreRequest.p.companyId = companyId;
		centreRequest.p.pageNum = pageCentre + "";
		centreRequest.p.pageSize = pageSize + "";
		String json = gson.toJson(centreRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					AdvertsingCentreResult centreResult = gson.fromJson(result,
							AdvertsingCentreResult.class);
					if (centreResult.p.isTrue) {
						if (centreResult.p.postJustList.size() > 0) {
							centreListcount = centreResult.p.postJustList
									.size();
							for (int i = 0; i < centreResult.p.postJustList
									.size(); i++) {
								centreList.add(centreResult.p.postJustList
										.get(i));
							}
							AdvertisingCentreAdapter.notifyDataSetChanged();

							// listview2.setAdapter(new
							// AdvertisingCentreAdapter(
							// MyAdvertActivity.this,
							// centreResult.p.postJustList));
						}
						if (centreListcount == 0) {
							ToastUtils.makeText(mContext, "无投放中广告", 0).show();
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
				// TODO Auto-generated method stub

			}
		});
	}

	// 投放后广告
	private void AdvertisingEnd() {
		final Gson gson = new Gson();
		AdvertsingEndRequest endRequest = new AdvertsingEndRequest();
		endRequest.p.userId = UserId;
		endRequest.p.tokenId = TokenId;
		endRequest.p.companyId = companyId;
		endRequest.p.pageNum = pageEnd + "";
		endRequest.p.pageSize = pageSize + "";
		String json = gson.toJson(endRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					AdvertsingEndResult endResult = gson.fromJson(result,
							AdvertsingEndResult.class);
					if (endResult.p.isTrue) {
						if (endResult.p.postAfterList.size() > 0) {
							endListCount = endResult.p.postAfterList.size();
							for (int i = 0; i < endResult.p.postAfterList
									.size(); i++) {
								endList.add(endResult.p.postAfterList.get(i));
							}
							AdvertisingEndAdapter.notifyDataSetChanged();
							// listview3.setAdapter(new AdvertisingEndAdapter(
							// MyAdvertActivity.this,
							// endResult.p.postAfterList));
						}
						if (endListCount == 0) {
							ToastUtils.makeText(mContext, "无投放后广告", 0).show();
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
				// TODO Auto-generated method stub

			}
		});
	}

	private void PagerChangeListener(ViewPager vp) {
		if (vp.getCurrentItem() == 0) {

		}
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					pageBefore = 1;
					if (beforeList.size() != 0) {
						beforeList.clear();
					}
					AdvertisingBefore();
					advertising_before_btn
							.setBackgroundResource(R.drawable.btn_light);
					advertising_center_btn
							.setBackgroundResource(R.drawable.btn_dark);
					advertising_end_btn
							.setBackgroundResource(R.drawable.btn_dark);
					break;
				case 1:
					pageCentre = 1;
					if (centreList.size() != 0) {
						centreList.clear();
					}
					AdvertisingCentre();
					advertising_before_btn
							.setBackgroundResource(R.drawable.btn_dark);
					advertising_center_btn
							.setBackgroundResource(R.drawable.btn_light);
					advertising_end_btn
							.setBackgroundResource(R.drawable.btn_dark);
					break;
				case 2:
					pageEnd = 1;
					if (endList.size() != 0) {
						endList.clear();
					}
					AdvertisingEnd();
					advertising_before_btn
							.setBackgroundResource(R.drawable.btn_dark);
					advertising_center_btn
							.setBackgroundResource(R.drawable.btn_dark);
					advertising_end_btn
							.setBackgroundResource(R.drawable.btn_light);
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
