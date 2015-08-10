package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.*;
import com.jishijiyu.takeadvantage.activity.widget.*;
import com.jishijiyu.takeadvantage.adapter.*;
import com.jishijiyu.takeadvantage.entity.request.AddFriendSearchRequest;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * 添加好友
 * 
 * @author zhaobin
 */
public class AddFriendActivity extends HeadBaseActivity {
	private EditText et_search = null;
	private ImageView btn_search = null;
	private LinearLayout tv_layout = null;
	private PullToRefreshListView listView = null;
	private MyAdapter<AddFriendSearchResult.FriendList> adapter = null;
	private int page = 1;
	private int pageSize = 9;
	private Gson gson = null;
	private AddFriendSearchResult addFriendSearchResult = null;
	private List<AddFriendSearchResult.FriendList> friendLists = null;
	private List<AddFriendSearchResult.FriendList> friendList = null;
	private String value = null;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.add_friend_top));
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AddFriendActivity.this, R.layout.add_friend,
				null);
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
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 1;
				friendLists = null;
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
				Intent intent = new Intent();
				intent.setClass(AddFriendActivity.this,
						FriendDatel2Activity.class);
				intent.putExtra(HeadBaseActivity.intentKey,
						friendLists.get(arg2));
				intent.putExtra("name", "addfriend");
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(AddFriendActivity.this);
			break;
		case R.id.btn_search:
			value = et_search.getText().toString().trim();
			if (!TextUtils.isEmpty(value)) {
				friendLists = null;
				search(value);
			} else {
				ToastUtils.makeText(AddFriendActivity.this, "搜索内容不能为空！",
						ToastUtils.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void search(String value) {
		if (gson == null) {
			gson = new Gson();
		}

		AddFriendSearchRequest request = new AddFriendSearchRequest();
		AddFriendSearchRequest.Pramater pramater = request.p;
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
					addFriendSearchResult = gson.fromJson(result,
							AddFriendSearchResult.class);
					if (addFriendSearchResult != null) {
						if (addFriendSearchResult.p.isTrue) {
							friendList = addFriendSearchResult.p.friendList;
							if (friendList != null && friendList.size() > 0) {
								tv_layout.setVisibility(View.GONE);
								if (friendLists == null) {
									friendLists = friendList;
								} else {
									for (int i = 0; i < friendList.size(); i++) {
										friendLists.add(friendList.get(i));
									}
								}
								if (adapter == null) {
									adapter = new MyAdapter<AddFriendSearchResult.FriendList>(
											AddFriendActivity.this,
											friendLists,
											R.layout.add_friend_item) {

										@Override
										public void convert(
												ViewHolder helper,
												int position,
												final AddFriendSearchResult.FriendList item) {
											helper.setText(R.id.tv_name,
													item.nickname);
											ImageLoaderUtil
													.loadImage(
															item.headImgUrl,
															(ImageView) helper
																	.getView(R.id.headimg));
											String sex = null;
											if (!TextUtils.isEmpty(item.sex)) {
												switch (Integer
														.parseInt(item.sex)) {
												case 0:
													sex = "男";
													break;
												case 1:
													sex = "女";
													break;
												}
											}
											helper.setText(R.id.tv_sex, sex);
											helper.setText(R.id.tv_address,
													item.province);
											if (!TextUtils.isEmpty(item.job)) {
												helper.setText(R.id.tv_version,
														item.job);
											} else {
												helper.setText(R.id.tv_version,
														"无");
											}

											helper.setOnclick(R.id.btn_add,
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															Intent intent = new Intent();
															intent.setClass(
																	AddFriendActivity.this,
																	AddFriend_SendMsgActivity.class);
															intent.putExtra(
																	HeadBaseActivity.intentKey,
																	item);
															intent.putExtra(
																	"name",
																	"addfriend");
															startActivity(intent);
														}
													});
										}
									};
									listView.setAdapter(adapter);
								} else {
									adapter.refresh(friendLists);
								}
							} else {
								tv_layout.setVisibility(View.VISIBLE);
								listView.setAdapter(null);
							}
						} else {
							IntentActivity.mIntent(AddFriendActivity.this);
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
