package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.entity.request.MyFriendRequest;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;
import com.jishijiyu.takeadvantage.entity.result.MyMerchantFriendResult;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.view.CharacterParser;
import com.jishijiyu.takeadvantage.view.PinyinComparator;
import com.jishijiyu.takeadvantage.view.SideBar;
import com.jishijiyu.takeadvantage.view.SortModel;
import com.jishijiyu.takeadvantage.view.SideBar.OnTouchingLetterChangedListener;

/**
 * 
 * 商家号
 * 
 * @author zhaobin
 */
public class MerchantCodeActivity extends HeadBaseActivity {
	private SideBar sideBar = null;
	private PullToRefreshListView listView = null;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private SortAdapter adapter;
	public static final int type = 1;
	private Gson gson = null;
	private int page = 1;
	private int pageSize = 9;
	private List<MyMerchantFriendResult.MyComList> myComLists = null;
	private List<MyMerchantFriendResult.MyComList> myComLists2 = null;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.merchant_code_top));
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(R.string.add);
		btn_right.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MerchantCodeActivity.this,
				R.layout.merchant_code, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initview(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		getFriendList();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initview(View view) {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		this.sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		this.listView = (PullToRefreshListView) view
				.findViewById(R.id.friend_list);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.getRefreshableView().setSelection(position);
				}

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MerchantCodeActivity.this,
						MerchantDatalActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey,
						myComLists2.get(arg2));
				startActivity(intent);
				myComLists2 = null;
				page = 1;
				SourceDateList = new ArrayList<SortModel>();
			}
		});
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 1;
				myComLists2 = null;
				SourceDateList = new ArrayList<SortModel>();
				getFriendList();
				listView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				getFriendList();
				listView.onRefreshComplete();
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager()
					.finishActivity(MerchantCodeActivity.this);
			break;
		case R.id.btn_top_right:
			startForActivity(MerchantCodeActivity.this,
					AddMerchantCodeActivity.class, null);
			myComLists2 = null;
			page = 1;
			SourceDateList = new ArrayList<SortModel>();
			break;
		}
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private void getFriendList() {
		if (gson == null) {
			gson = new Gson();
		}
		myComLists = null;
		MyFriendRequest myFriendRequest = new MyFriendRequest();
		myFriendRequest.c = Constant.MERCHANT_CODE;
		MyFriendRequest.Pramater pramater = myFriendRequest.p;
		pramater.pageNum = page + "";
		pramater.pageSize = pageSize + "";
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(myFriendRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					MyMerchantFriendResult friendResult = gson.fromJson(result,
							MyMerchantFriendResult.class);
					if (friendResult.p.isTrue) {
						myComLists = friendResult.p.myComList;
						if (myComLists != null && myComLists.size() > 0) {
							if (myComLists2 == null) {
								myComLists2 = myComLists;
							} else {
								for (int i = 0; i < myComLists.size(); i++) {
									myComLists2.add(myComLists.get(i));
								}
							}
							SourceDateList = new ArrayList<SortModel>();
							for (int i = 0; i < myComLists2.size(); i++) {
								SortModel sortModel = new SortModel();
								sortModel.setObject(myComLists2.get(i));
								// 汉字转换成拼音
								String pinyin = characterParser
										.getSelling(myComLists2.get(i).companyName);
								String sortString = pinyin.substring(0, 1)
										.toUpperCase();

								// 正则表达式，判断首字母是否是英文字母
								if (sortString.matches("[A-Z]")) {
									sortModel.setSortLetters(sortString
											.toUpperCase());
								} else {
									sortModel.setSortLetters("#");
								}

								SourceDateList.add(sortModel);
							}
							// 根据a-z进行排序源数据
							Collections.sort(SourceDateList, pinyinComparator);
							if (adapter == null) {
								adapter = new SortAdapter(
										MerchantCodeActivity.this,
										SourceDateList, type);
								listView.setAdapter(adapter);
							} else {
								adapter.refresh(SourceDateList);
							}

						}
					} else {
						IntentActivity.mIntent(MerchantCodeActivity.this);
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
