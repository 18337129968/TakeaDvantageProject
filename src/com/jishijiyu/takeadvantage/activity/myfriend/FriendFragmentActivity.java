package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.util.EMLog;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.HX.HXSDKHelper;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.db.DBDao;
import com.jishijiyu.takeadvantage.activity.db.Note;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.entity.request.MyFriendRequest;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.view.CharacterParser;
import com.jishijiyu.takeadvantage.view.PinyinComparator;
import com.jishijiyu.takeadvantage.view.SideBar;
import com.jishijiyu.takeadvantage.view.SideBar.OnTouchingLetterChangedListener;
import com.jishijiyu.takeadvantage.view.SortModel;

/**
 * 
 * 好友
 * 
 * @author zhaobin
 */
@SuppressLint("NewApi")
public class FriendFragmentActivity extends Fragment {
	private static final String TAG = "FriendFragmentActivity";
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
	private LinearLayout newFridenLayout = null;
	private LinearLayout addFridenLayout = null;
	private LinearLayout merchant_code_layout = null;
	private TextView unreadMsgView = null;
	private List<MyFriendResult.MyFriendsList> friendResults = null;
	private List<MyFriendResult.MyFriendsList> friendResults2 = null;
	private int page = 1;
	private int pageSize = 9;
	private Gson gson = null;
	public static final int type = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_friend, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		init();
	}

	HXContactSyncListener contactSyncListener;

	public void init() {

		SourceDateList = new ArrayList<SortModel>();
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		this.sideBar = (SideBar) getActivity().findViewById(R.id.sidrbar);
		this.listView = (PullToRefreshListView) getActivity().findViewById(
				R.id.friend_list);
		this.newFridenLayout = (LinearLayout) getActivity().findViewById(
				R.id.new_layout);
		this.addFridenLayout = (LinearLayout) getActivity().findViewById(
				R.id.add_layout);
		this.merchant_code_layout = (LinearLayout) getActivity().findViewById(
				R.id.merchant_code_layout);
		this.unreadMsgView = (TextView) getActivity().findViewById(
				R.id.myfriend_unread_msg_number);
		contactSyncListener = new HXContactSyncListener();
		HXSDKHelper.getInstance().addSyncContactListener(contactSyncListener);
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
				Intent intent = new Intent(FriendFragmentActivity.this
						.getActivity(), FriendDatelActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey,
						friendResults2.get(arg2));
				startActivity(intent);
				friendResults2 = null;
				page = 1;
				SourceDateList = new ArrayList<SortModel>();
			}
		});
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 1;
				friendResults2 = null;
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
		this.newFridenLayout.setOnClickListener(clickListener);
		this.addFridenLayout.setOnClickListener(clickListener);
		this.merchant_code_layout.setOnClickListener(clickListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		getContact();
		refresh();
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
		friendResults = null;
		MyFriendRequest myFriendRequest = new MyFriendRequest();
		MyFriendRequest.Pramater pramater = myFriendRequest.p;
		pramater.pageNum = page + "";
		pramater.pageSize = pageSize + "";
		pramater.userId = GetUserIdUtil.getUserId(getActivity());
		pramater.tokenId = GetUserIdUtil.getTokenId(getActivity());
		String json = gson.toJson(myFriendRequest);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					MyFriendResult friendResult = gson.fromJson(result,
							MyFriendResult.class);
					if (friendResult.p.isTrue) {
						friendResults = friendResult.p.myFriendsList;
						if (friendResults != null && friendResults.size() > 0) {
							if (friendResults2 == null) {
								friendResults2 = friendResults;

								for (int i = 0; i < friendResults.size(); i++) {
									DBDao db = new DBDao(getActivity());
									Note note = new Note();
									if (db.searchFriendIfExist(friendResults
											.get(i).userId)) {
										note.userid = friendResults.get(i).userId;
										note.nikname = friendResults.get(i).nickname;
										note.avatar = friendResults.get(i).headImgUrl;
										db.addFriend(note);
									}
								}
							} else {
								for (int i = 0; i < friendResults.size(); i++) {
									friendResults2.add(friendResults.get(i));
								}
							}
							SourceDateList = new ArrayList<SortModel>();
							for (int i = 0; i < friendResults2.size(); i++) {
								SortModel sortModel = new SortModel();
								sortModel.setObject(friendResults2.get(i));
								// 汉字转换成拼音
								String pinyin = characterParser
										.getSelling(friendResults2.get(i).nickname);
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

						}
						if (adapter == null) {
							adapter = new SortAdapter(getActivity(),
									SourceDateList, type);
							listView.setAdapter(adapter);
						} else {
							adapter.refresh(SourceDateList);
						}
					} else {
						IntentActivity.mIntent(getActivity());
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

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.new_layout:
				page = 1;
				friendResults2 = null;
				SourceDateList = new ArrayList<SortModel>();
				intent = new Intent(FriendFragmentActivity.this.getActivity(),
						NewFriendActivity.class);
				startActivity(intent);
				break;
			case R.id.add_layout:
				page = 1;
				friendResults2 = null;
				SourceDateList = new ArrayList<SortModel>();
				intent = new Intent(FriendFragmentActivity.this.getActivity(),
						AddFriendActivity.class);
				startActivity(intent);
				break;
			case R.id.merchant_code_layout:
				page = 1;
				friendResults2 = null;
				SourceDateList = new ArrayList<SortModel>();
				intent = new Intent(FriendFragmentActivity.this.getActivity(),
						MerchantCodeActivity.class);
				startActivity(intent);
				break;
			}
		}
	};

	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					User user = getContact();
					if (user.getUnreadMsgCount() > 0) {
						unreadMsgView.setVisibility(View.VISIBLE);

						// unreadMsgView.setText(user.getUnreadMsgCount() + "");
					} else {
						unreadMsgView.setVisibility(View.INVISIBLE);
					}
					friendResults2 = null;
					page = 1;
					SourceDateList = new ArrayList<SortModel>();
					getFriendList();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	private User getContact() {
		// 获取本地好友列表
		Map<String, User> users = App.getInstance().getContactList();

		// 把"申请与通知"添加到首位
		if (users.get(Constant.NEW_FRIENDS_USERNAME) != null) {
			return users.get(Constant.NEW_FRIENDS_USERNAME);
		}
		return null;
	}

	class HXContactSyncListener implements
			com.jishijiyu.takeadvantage.HX.HXSDKHelper.HXSyncListener {
		@Override
		public void onSyncSucess(final boolean success) {
			EMLog.d(TAG, "on contact list sync success:" + success);
			FriendFragmentActivity.this.getActivity().runOnUiThread(
					new Runnable() {
						public void run() {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (success) {
										refresh();
									} else {
										String s1 = "获取失败,请检查网络或稍后再试";
										Toast.makeText(getActivity(), s1, 1)
												.show();
									}
								}

							});
						}
					});
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (((FriendActivity) getActivity()).isConflict) {
			outState.putBoolean("isConflict", true);
		} else if (((FriendActivity) getActivity()).getCurrentAccountRemoved()) {
			outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
		}

	}

	@Override
	public void onDestroy() {
		if (contactSyncListener != null) {
			HXSDKHelper.getInstance().removeSyncContactListener(
					contactSyncListener);
			contactSyncListener = null;
		}

		super.onDestroy();
	}

}
