package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.AddFriendSearchRequest;
import com.jishijiyu.takeadvantage.entity.request.AddMerchantSearchRequest;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult;
import com.jishijiyu.takeadvantage.entity.result.AddMerchantSearchResult;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 
 * 商家详情
 * 
 * @author zhaobin
 */
public class AddMerchantCodeActivity extends HeadBaseActivity {
	private EditText et_search = null;
	private ImageView btn_search = null;
	private LinearLayout tv_layout = null;
	private PullToRefreshListView listView = null;
	private MyAdapter<AddMerchantSearchResult.ComList> adapter = null;
	private int page = 1;
	private int pageSize = 9;
	private Gson gson = null;
	private AddMerchantSearchResult addMerchantSearchResult = null;
	private List<AddMerchantSearchResult.ComList> comLists = null;
	private List<AddMerchantSearchResult.ComList> comList = null;
	private String value = null;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.add_friend_top));
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AddMerchantCodeActivity.this,
				R.layout.add_friend, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initview(view);
	}

	private void initview(View view) {
		et_search = (EditText) getView(view, R.id.et_search);
		btn_search = (ImageView) getView(view, R.id.btn_search);
		tv_layout = (LinearLayout) getView(view, R.id.tv_layout);
		listView = (PullToRefreshListView) getView(view, R.id.listview);
		btn_search.setOnClickListener(this);
		et_search.setHint("商家号");
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 1;
				comLists = null;
				search(value);
				listView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				search(value);
				listView.onRefreshComplete();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startForActivity(AddMerchantCodeActivity.this,
						MerchantDatal2Activity.class, comLists.get(arg2));
			}
		});
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					AddMerchantCodeActivity.this);
			break;
		case R.id.btn_search:
			value = et_search.getText().toString().trim();
			if (!TextUtils.isEmpty(value)) {
				search(value);
			} else {
				ToastUtils.makeText(AddMerchantCodeActivity.this, "搜索内容不能为空！",
						ToastUtils.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void search(String value) {
		if (gson == null) {
			gson = new Gson();
		}
		AddMerchantSearchRequest request = new AddMerchantSearchRequest();
		AddMerchantSearchRequest.Pramater pramater = request.p;
		pramater.pageNum = page + "";
		pramater.pageSize = pageSize + "";
		pramater.keyWord = value;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					addMerchantSearchResult = gson.fromJson(result,
							AddMerchantSearchResult.class);
					if (addMerchantSearchResult != null) {
						if (addMerchantSearchResult.p.isTrue) {
							comList = addMerchantSearchResult.p.comList;
							if (comList != null && comList.size() > 0) {
								tv_layout.setVisibility(View.GONE);
								if (comLists == null) {
									comLists = comList;
								} else {
									for (int i = 0; i < comList.size(); i++) {
										comLists.add(comList.get(i));
									}
								}
								if (adapter == null) {
									adapter = new MyAdapter<AddMerchantSearchResult.ComList>(
											AddMerchantCodeActivity.this,
											comLists,
											R.layout.add_merchant_item) {

										@Override
										public void convert(
												ViewHolder helper,
												int position,
												final AddMerchantSearchResult.ComList item) {
											ImageLoaderUtil
													.loadImage(
															item.logoImgUrl,
															(ImageView) helper
																	.getView(R.id.headimg));
											helper.setText(R.id.tv_name,
													item.companyName);
											helper.setOnclick(R.id.btn_add,
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															Intent intent = new Intent();
															intent.setClass(
																	AddMerchantCodeActivity.this,
																	AddFriend_SendMsgActivity.class);
															intent.putExtra(
																	HeadBaseActivity.intentKey,
																	item);
															intent.putExtra(
																	"name",
																	"addmerchant");
															startActivity(intent);
														}
													});
										}
									};
									listView.setAdapter(adapter);
								} else {
									adapter.refresh(comLists);
								}
							} else {
								tv_layout.setVisibility(View.VISIBLE);
								listView.setAdapter(null);
							}
						} else {
							IntentActivity
									.mIntent(AddMerchantCodeActivity.this);
						}
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
