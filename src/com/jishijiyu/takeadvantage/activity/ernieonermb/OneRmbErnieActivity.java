package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshGridView;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.AwaitRoomListAdapter;
import com.jishijiyu.takeadvantage.adapter.JoinedListviewAdapter;
import com.jishijiyu.takeadvantage.adapter.OneRmbErniePagerAdapter;
import com.jishijiyu.takeadvantage.adapter.OneRmbRoomListAdapter;
import com.jishijiyu.takeadvantage.adapter.PrizeSearchRoomAdapter;
import com.jishijiyu.takeadvantage.entity.request.AwaitRoomListRequest;
import com.jishijiyu.takeadvantage.entity.request.JoinedRoomListRequest;
import com.jishijiyu.takeadvantage.entity.request.PrizeSearchRoomRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomBillRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomListRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomNumSearchRoomRequest;
import com.jishijiyu.takeadvantage.entity.result.AwaitRoomListResult;
import com.jishijiyu.takeadvantage.entity.result.JoinedRoomListResult;
import com.jishijiyu.takeadvantage.entity.result.PrizeSearchRoomResult;
import com.jishijiyu.takeadvantage.entity.result.RoomBillResult;
import com.jishijiyu.takeadvantage.entity.result.RoomListResult;
import com.jishijiyu.takeadvantage.entity.result.RoomListResult.Parameter;
import com.jishijiyu.takeadvantage.entity.result.RoomNumSearchRoomResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 一元摇奖主界面
 * 
 * @author shifeiyu
 * 
 */
public class OneRmbErnieActivity extends HeadBaseActivity {
	List<Integer> atList = new ArrayList<Integer>();
	BaseAdapter adapter, roomAdapter;
	AwaitRoomListAdapter awaitAdapter;
	int isFirstLoad = 0, roomListcount = 0, joinedListcount = 0,
			allRoomCount = 0, awaitListCount = 0;
	int page = 0, pageAwait = 0, pageRoom = 0, index = 0, pagesize = 20,
			temp = 0;
	EditText search_goods_edit;
	ImageView search_btn;
	Parameter parameterRoom;
	RoomListRequest roomRequest;
	RoomListResult roomResult;
	ShareUtil shareUtil;
	ViewPager one_rmb_ernie_viewpager;
	TextView one_rmb_home_btn, joined_btn, fast_search_btn, new_home_btn,
			room_count_text, await_btn;
	List<View> list;
	long nowTime = 0;
	long nowTimeAwait = 0;
	List<JoinedRoomListResult.roomOnMealVOList> joinedList;
	List<AwaitRoomListResult.roomOnMealVOList> awaitList;
	List<RoomListResult.RoomList> roomList;
	List<Map<String, Object>> home_list_item, joined_list_item;
	PullToRefreshGridView one_rmb_home_listview;
	PullToRefreshListView joined_listview = null;
	PullToRefreshGridView await_gridview;
	Map<String, Object> map;
	View v1, v2, v3;
	Dialog dialog;
	private Intent intent;
	private TextView tv_new_home_btn;
	public Message msg;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getListInfo(0);
				break;
			case 2:
				// room_count_text.setText("房间数:" + roomListcount + "/"
				// + allRoomCount);
				index = 1;
				break;
			case 3:
				Joined();
				break;
			case 4:
				roomAdapter = new OneRmbRoomListAdapter(
						OneRmbErnieActivity.this, roomList, atList);
				one_rmb_home_listview.setAdapter(roomAdapter);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 返回
		case R.id.btn_top_left:
			finish();
			break;
		// 清单
		case R.id.btn_top_right2:
			intent = new Intent(OneRmbErnieActivity.this,
					RoomListActivity.class);
			startActivity(intent);
			break;
		// 大厅
		case R.id.one_rmb_home_btn:
			one_rmb_home_btn.setBackgroundResource(R.drawable.btn_light);
			joined_btn.setBackgroundResource(R.drawable.btn_dark);
			await_btn.setBackgroundResource(R.drawable.btn_dark);
			one_rmb_ernie_viewpager.setCurrentItem(0);
			break;
		// 等待揭晓
		case R.id.await_btn:
			one_rmb_home_btn.setBackgroundResource(R.drawable.btn_dark);
			joined_btn.setBackgroundResource(R.drawable.btn_dark);
			await_btn.setBackgroundResource(R.drawable.btn_light);
			one_rmb_ernie_viewpager.setCurrentItem(1);
			break;
		// 已经参与
		case R.id.joined_btn:
			one_rmb_home_btn.setBackgroundResource(R.drawable.btn_dark);
			joined_btn.setBackgroundResource(R.drawable.btn_light);
			await_btn.setBackgroundResource(R.drawable.btn_dark);
			one_rmb_ernie_viewpager.setCurrentItem(2);
			break;
		// 商品查找房间
		case R.id.search_btn:
			if (!TextUtils.isEmpty(search_goods_edit.getText())) {
				GoodsNameSearchRoom();
			} else {
				ToastUtils.makeText(mContext, "请输入商品名称", 0).show();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_right2.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(this);
		btn_right2.setOnClickListener(this);
		top_text.setText("一元摇奖");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(OneRmbErnieActivity.this,
				R.layout.activity_one_rmb_ernie, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		shareUtil = new ShareUtil(OneRmbErnieActivity.this, mContext);
		IntentFilter ifilter = new IntentFilter("UpdateList");
		registerReceiver(bdReceiver, ifilter);
		initView();
		addPagerView(); // 添加pagerview
		ernieHome(); // 摇奖房间
		search_goods_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					ernieHome();
				}
			}
		});
	}

	// 用于接收广播更新room清单数目
	BroadcastReceiver bdReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			getListInfo(1);
		}

	};

	// 注册控件
	private void initView() {
		one_rmb_ernie_viewpager = (ViewPager) findViewById(R.id.one_rmb_ernie_viewpager);
		one_rmb_home_btn = (TextView) findViewById(R.id.one_rmb_home_btn);
		joined_btn = (TextView) findViewById(R.id.joined_btn);
		await_btn = (TextView) findViewById(R.id.await_btn);
		await_btn.setOnClickListener(this);
		one_rmb_home_btn.setOnClickListener(this);
		joined_btn.setOnClickListener(this);
		one_rmb_ernie_viewpager.setCurrentItem(0);
		one_rmb_home_btn.setBackgroundResource(R.drawable.btn_light);

	}

	// 添加pagerview
	private void addPagerView() {
		list = new ArrayList<View>();
		v1 = LayoutInflater.from(mContext).inflate(
				R.layout.activity_one_rmb_ernie_home, null);
		v2 = LayoutInflater.from(mContext).inflate(
				R.layout.activity_one_rmb_ernie_await, null);
		v3 = LayoutInflater.from(mContext).inflate(
				R.layout.activity_one_rmb_ernie_joined, null);
		list.add(v1);
		list.add(v2);
		list.add(v3);
		tv_new_home_btn = (TextView) v1.findViewById(R.id.new_home_btn);
		one_rmb_ernie_viewpager.setAdapter(new OneRmbErniePagerAdapter(list));
		one_rmb_ernie_viewpager
				.setOnPageChangeListener(new onPagerChangeListener());
		// 新建房间
		tv_new_home_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(OneRmbErnieActivity.this,
						NewRoomActivity.class);
				startActivity(intent);
			}
		});
		await_gridview = (PullToRefreshGridView) v2
				.findViewById(R.id.await_gridview);
		one_rmb_home_listview = (PullToRefreshGridView) v1
				.findViewById(R.id.one_rmb_home_listview);
		joined_listview = (PullToRefreshListView) v3
				.findViewById(R.id.joined_listview);
		joined_listview// 已参与listview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						page = 0;
						if (joinedList.size() != 0) {
							joinedList.clear();
						}
						Joined();
						joined_listview.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						page++;
						Joined();
						joined_listview.onRefreshComplete();
					}
				});
		await_gridview // 待揭晓listview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						pageAwait = 0;
						if (awaitList.size() != 0) {
							awaitList.clear();
						}
						AwaitRoom();
						await_gridview.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						pageAwait++;
						AwaitRoom();
						await_gridview.onRefreshComplete();
					}
				});
		one_rmb_home_listview// 摇奖大厅listview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						pageRoom = 0;
						if (roomList.size() != 0) {
							roomList.clear();
						}
						ernieHome();
						one_rmb_home_listview.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						pageRoom++;
						ernieHome();
						one_rmb_home_listview.onRefreshComplete();
					}
				});
		roomList = new ArrayList<RoomListResult.RoomList>();
		roomAdapter = new OneRmbRoomListAdapter(OneRmbErnieActivity.this,
				roomList, atList);
		one_rmb_home_listview.setAdapter(roomAdapter);

		joinedList = new ArrayList<JoinedRoomListResult.roomOnMealVOList>();

		awaitList = new ArrayList<AwaitRoomListResult.roomOnMealVOList>();
		awaitAdapter = new AwaitRoomListAdapter(OneRmbErnieActivity.this,
				awaitList, nowTimeAwait);
		await_gridview.setAdapter(awaitAdapter);
	}

	@Override
	protected void onResume() {
		if (index == 0) {

		} else {
			if (one_rmb_ernie_viewpager.getCurrentItem() == 0) {
				pageRoom = 0;
				if (roomList.size() != 0) {
					roomList.clear();
				}
				ernieHome();
			}
			if (one_rmb_ernie_viewpager.getCurrentItem() == 1) {
				pageAwait = 0;
				if (awaitList.size() != 0) {
					awaitList.clear();
				}
				AwaitRoom();
			}
			if (one_rmb_ernie_viewpager.getCurrentItem() == 2) {
				page = 0;
				if (joinedList.size() != 0) {
					joinedList.clear();
				}
				Joined();
			}
		}

		super.onResume();
	}

	// 摇奖大厅
	private void ernieHome() {
		room_count_text = (TextView) v1.findViewById(R.id.room_count_text);
		fast_search_btn = (TextView) v1.findViewById(R.id.fast_search_btn);
		new_home_btn = (TextView) v1.findViewById(R.id.new_home_btn);
		search_btn = (ImageView) v1.findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		search_goods_edit = (EditText) v1.findViewById(R.id.search_goods_edit);
		roomRequest = new RoomListRequest();
		roomRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		roomRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		roomRequest.p.page = pageRoom + "";
		roomRequest.p.pageSize = pagesize + "";
		final Gson gson = new Gson();
		String requestJson = gson.toJson(roomRequest);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (index == 0) {
					msg = msg.obtain();
					msg.what = 3;
					handler.sendMessage(msg);
				} else {
					msg = msg.obtain();
					msg.what = 1;
					handler.sendMessage(msg);
				}

				if (!TextUtils.isEmpty(result)) {
					roomResult = gson.fromJson(result, RoomListResult.class);
					parameterRoom = roomResult.p;
					if (roomResult.p.isTrue) {
						if (roomResult.p.roomList.size() > 0) {
							roomListcount = roomResult.p.roomList.size();
							for (int i = 0; i < roomResult.p.roomList.size(); i++) {
								roomList.add(roomResult.p.roomList.get(i));
							}
							roomAdapter.notifyDataSetChanged();
						}
						if (roomListcount == 0) {
							ToastUtils.makeText(mContext, "暂无房间数据", 0).show();
						}
						room_count_text.setText("房间数:" + roomResult.p.roomCount
								+ "/" + roomResult.p.roomCount);
					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
					}

				} else {
					ToastUtils.makeText(mContext, "获取房间信息失败", 0).show();
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				if (index == 0) {
					msg = msg.obtain();
					msg.what = 3;
					handler.sendMessage(msg);
				} else {
					msg = msg.obtain();
					msg.what = 1;
					handler.sendMessage(msg);
				}
				ToastUtils.makeText(mContext, "获取房间列表失败", 0).show();
			}
		});
		// 快速查找房间按钮
		fast_search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = DialogUtils.searchHomeDialog(mContext,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.search_home_join_btn) {
									RoomNumSearchRoom();
								}
							}
						});
			}
		});
	}

	// 等待揭晓
	private void AwaitRoom() {
		final Gson gson = new Gson();
		AwaitRoomListRequest arRequset = new AwaitRoomListRequest();
		arRequset.p.page = pageAwait + "";
		arRequset.p.pageSize = pagesize + "";
		arRequset.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		arRequset.p.userId = GetUserIdUtil.getUserId(mContext);
		String json = gson.toJson(arRequset);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				getListInfo(0);
				if (!TextUtils.isEmpty(result)) {

					AwaitRoomListResult arResult = gson.fromJson(result,
							AwaitRoomListResult.class);
					if (arResult.p.isTrue) {
						if (arResult != null) {
							if (arResult.p.roomOnMealVOList.size() > 0) {
								awaitListCount = arResult.p.roomOnMealVOList
										.size();
								for (int i = 0; i < arResult.p.roomOnMealVOList
										.size(); i++) {
									awaitList.add(arResult.p.roomOnMealVOList
											.get(i));
								}
								nowTimeAwait = arResult.p.nowTime;
								awaitAdapter.notifyDataSetChanged();
							}
						}
						if (awaitListCount == 0) {
							ToastUtils.makeText(mContext, "暂无待揭晓数据", 0).show();
						}

					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
					}

				} else {
					ToastUtils.makeText(mContext, "暂无待揭晓房间数据", 0).show();
				}

			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				getListInfo(0);
				ToastUtils.makeText(mContext, "连接服务器失败", 0).show();
			}
		});

	}

	// 我的参与
	private void Joined() {
		final Gson gson = new Gson();
		JoinedRoomListRequest jrRequset = new JoinedRoomListRequest();
		jrRequset.p.page = page + "";
		jrRequset.p.pageSize = pagesize + "";
		// jrRequset.p.pageSize = pagesize + "";
		jrRequset.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		jrRequset.p.userId = GetUserIdUtil.getUserId(mContext);
		String requestJson = gson.toJson(jrRequset);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@SuppressWarnings("unused")
			@Override
			public void contectSuccess(String result) {
				msg = msg.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
				if (!TextUtils.isEmpty(result)) {
					JoinedRoomListResult joinedRoomList = gson.fromJson(result,
							JoinedRoomListResult.class);
					if (joinedRoomList.p.isTrue) {
						if (joinedRoomList != null) {
							allRoomCount = joinedListcount + roomListcount;
							if (joinedRoomList.p.roomOnMealVOList.size() > 0) {
								joinedListcount = joinedRoomList.p.roomOnMealVOList
										.size();
								allRoomCount = joinedListcount + roomListcount;
								for (int i = 0; i < joinedRoomList.p.roomOnMealVOList
										.size(); i++) {
									joinedList
											.add(joinedRoomList.p.roomOnMealVOList
													.get(i));
								}
								nowTime = joinedRoomList.p.nowTime;
								adapter = new JoinedListviewAdapter(
										OneRmbErnieActivity.this, joinedList,
										nowTime);
								joined_listview.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}
						System.out.println(joinedListcount);
						if (joinedListcount == 0) {
							ToastUtils.makeText(mContext, "暂无已参与数据", 0).show();

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
				msg = msg.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
				ToastUtils.makeText(mContext, "获取已参与列表失败", 0).show();
			}
		});
	}

	// 获取清单信息
	public void getListInfo(final int RefreshList) {
		final Gson gson = new Gson();
		RoomBillRequest billrequest = new RoomBillRequest();
		billrequest.p.userId = GetUserIdUtil.getUserId(mContext);
		billrequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String json = gson.toJson(billrequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				msg = msg.obtain();
				msg.what = 2;
				handler.sendMessage(msg);
				if (!TextUtils.isEmpty(result)) {
					RoomBillResult billresult = gson.fromJson(result,
							RoomBillResult.class);
					if (billresult.p.InventoryVOList != null
							&& billresult.p.InventoryVOList.size() > 0) {
						btn_right_num.setVisibility(View.VISIBLE);
						btn_right_num.setText(billresult.p.InventoryVOList
								.size() + "");
						for (int i = 0; i < billresult.p.InventoryVOList.size(); i++) {
							atList.add(billresult.p.InventoryVOList.get(i).dollerRoomId);
						}
						if (RefreshList == 0) {
							msg = msg.obtain();
							msg.what = 4;
							handler.sendMessage(msg);
						}

					} else {
						btn_right_num.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				msg = msg.obtain();
				msg.what = 2;
				handler.sendMessage(msg);
			}
		});

	}

	// 根据房间号查找房间
	private void RoomNumSearchRoom() {
		if (TextUtils.isEmpty(DialogUtils.edit_home_code.getText())) {
			ToastUtils.makeText(mContext, "请输入房间号", 0).show();
			return;
		}
		if (!TextUtils.isEmpty(DialogUtils.edit_home_code.getText())) {
			final Gson gson = new Gson();
			RoomNumSearchRoomRequest request = new RoomNumSearchRoomRequest();
			request.p.roomNum = DialogUtils.edit_home_code.getText().toString();
			request.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			request.p.userId = GetUserIdUtil.getUserId(mContext);
			String requestJson = gson.toJson(request);
			HttpConnectTool.update(requestJson, mContext,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								RoomNumSearchRoomResult searchResult = gson
										.fromJson(result,
												RoomNumSearchRoomResult.class);
								if (searchResult.p.isTrue) {
									if (searchResult.p.dollerRoomTO != null) {
										String id = String
												.valueOf(searchResult.p.dollerRoomTO.id);
										Intent intent = new Intent(
												mContext,
												RegistrationDetailActivity.class);
										intent.putExtra("RoomId", id);// 房间ID
										intent.putExtra("Type",
												"NotRegistration");// 是否加入房间
										intent.putExtra(
												"joinNumber",
												searchResult.p.dollerRoomTO.joinNumber);// 已参与人数
										startActivity(intent);
										dialog.dismiss();
									} else {
										ToastUtils.makeText(mContext, "未找到该房间",
												0).show();
										dialog.dismiss();
									}
								} else {
									ToastUtils.makeText(mContext,
											R.string.again_login_text, 0)
											.show();
									startForActivity(mContext,
											LoginActivity.class, null);
									finish();
								}

							} else {
								ToastUtils.makeText(mContext, "请求失败", 0).show();
							}
						}

						@Override
						public void contectStarted() {
							// TODO Auto-generated method stub

						}

						@Override
						public void contectFailed(String path, String request) {
							ToastUtils.makeText(mContext, "访问服务器失败", 0).show();
						}
					});
		}

	}

	// 根据商品查找房间
	private void GoodsNameSearchRoom() {
		final Gson gson = new Gson();
		PrizeSearchRoomRequest request = new PrizeSearchRoomRequest();
		request.p.prizeName = search_goods_edit.getText().toString();
		request.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		request.p.userId = GetUserIdUtil.getUserId(mContext);
		String requestJson = gson.toJson(request);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					PrizeSearchRoomResult searchResult = gson.fromJson(result,
							PrizeSearchRoomResult.class);
					if (searchResult.p.isTrue) {
						if (searchResult.p.roomList != null
								&& searchResult.p.roomList.size() > 0) {
							ToastUtils.makeText(
									mContext,
									"共找到" + searchResult.p.roomList.size()
											+ "个房间", 0).show();
							one_rmb_home_listview
									.setAdapter(new PrizeSearchRoomAdapter(
											OneRmbErnieActivity.this,
											searchResult.p.roomList));
						} else {
							ToastUtils.makeText(mContext, "未找到该房间", 0).show();
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
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();
			}
		});
	}

	// pagerchange监听
	public class onPagerChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:// 选择摇奖大厅界面
				pageRoom = 0;
				if (roomList.size() != 0) {
					roomList.clear();
				}
				ernieHome();
				one_rmb_home_btn.setBackgroundResource(R.drawable.btn_light);
				joined_btn.setBackgroundResource(R.drawable.btn_dark);
				await_btn.setBackgroundResource(R.drawable.btn_dark);
				break;
			case 1:// 选择等待揭晓界面
				pageAwait = 0;
				if (awaitList.size() != 0) {
					awaitList.clear();
				}
				AwaitRoom();
				one_rmb_home_btn.setBackgroundResource(R.drawable.btn_dark);
				joined_btn.setBackgroundResource(R.drawable.btn_dark);
				await_btn.setBackgroundResource(R.drawable.btn_light);
				break;
			case 2:// 选择我的参与界面
				page = 0;
				if (joinedList.size() != 0) {
					joinedList.clear();
				}
				Joined();
				one_rmb_home_btn.setBackgroundResource(R.drawable.btn_dark);
				joined_btn.setBackgroundResource(R.drawable.btn_light);
				await_btn.setBackgroundResource(R.drawable.btn_dark);
				break;

			default:
				break;
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bdReceiver);
	}

}
