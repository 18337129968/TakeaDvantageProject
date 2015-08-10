package com.jishijiyu.takeadvantage.activity.ernie;

import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.WinPrizeInfomationActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.FirmOrderActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshGridView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.SetPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.WinningPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.WinningPriceRequest.WinningData;
import com.jishijiyu.takeadvantage.entity.result.SetPriceResult;
import com.jishijiyu.takeadvantage.entity.result.WinningPriceResult;
import com.jishijiyu.takeadvantage.entity.result.WinningPriceResult.WinningListData;
import com.jishijiyu.takeadvantage.receiver.MyReceiver;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

/**
 * 
 * 我的中奖
 * 
 * @author zhaobin
 */
public class MyPriceActivity extends HeadBaseActivity {
	private PullToRefreshGridView mGridView = null;
	private Button mBtn_get_price = null;
	private Button mBtn_set_price = null;
	private MyAdapter<WinningListData> gridAdapter = null;
	private ShareUtil shareUtil = null;
	private AlertDialog alertDialog = null;
	private int position = -1;
	private int btnId;
	private String userId = null;
	private List<WinningListData> list = null;
	private Gson gson = null;
	private int page = 0;
	private int pageSize = 9;
	private MyReceiver myReceiver = null;
	private Button mBtn_share_price;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.my_price));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MyPriceActivity.this, R.layout.my_price, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		shareUtil = new ShareUtil(this, this);
		initView(view);
		setOnclick();
		myReceiver = new MyReceiver() {
			@Override
			public void dynamic() {
				switch (btnId) {
				case R.id.btn_get_price:
					startForActivity(MyPriceActivity.this,
							FirmOrderActivity.class, list.get(position));
					AppManager.getAppManager().finishActivity(
							MyPriceActivity.this);
					break;
				case R.id.btn_set_price:
					if (alertDialog != null && alertDialog.isShowing()) {
						alertDialog.dismiss();
					}
					ToastUtils.makeText(MyPriceActivity.this, "赠送成功！",
							Toast.LENGTH_SHORT);
					break;
				}
			}
		};
		IntentFilter dynamic_filter = new IntentFilter();
		dynamic_filter.addAction(Constant.DYNAMICACTION); // 添加动态广播的Action
		registerReceiver(myReceiver, dynamic_filter); // 注册自定义动态广播消息
	}

	@Override
	protected void onResume() {
		super.onResume();

		userId = GetUserIdUtil.getUserId(MyPriceActivity.this);
		page = 0;
		getHttpData(userId);
	}

	/**
	 * 获取未领取奖品信息
	 * 
	 * @author zhaobin
	 * @param userId
	 *            用户Id
	 * @return void
	 * @throws
	 */
	public void getHttpData(String userId) {
		WinningPriceRequest request = new WinningPriceRequest();
		WinningData data = new WinningData();
		data.setUserId(userId);
		data.setPage(page + "");
		data.setPageSize(pageSize + "");
		data.setTokenId(GetUserIdUtil.getTokenId(MyPriceActivity.this));
		request.setC(Constant.MY_PRICE_CODE);
		request.setP(data);
		if (gson == null) {
			gson = new Gson();
		}
		String json = gson.toJson(request);
		HttpConnectTool.update(json, MyPriceActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						if (result != null) {
							WinningPriceResult jsonobj = gson.fromJson(result,
									WinningPriceResult.class);
							if (jsonobj.getP().isTrue()) {
								if (jsonobj.getP().getWinList() != null
										&& jsonobj.getP().getWinList().size() > 0) {
									if (page != 0) {
										if (list != null && list.size() > 0) {
											for (int i = 0; i < jsonobj.getP()
													.getWinList().size(); i++) {
												list.add(jsonobj.getP()
														.getWinList().get(i));
											}
											gridAdapter.refresh(list);
										}
									} else {
										list = jsonobj.getP().getWinList();
										setView();
									}
								}
							} else {
								if (page > 0) {
									page = page - 1;
								}
							}
						} else {
							IntentActivity.mIntent(MyPriceActivity.this);
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

	private void setView() {

		if (list != null && list.size() > 0) {
			gridAdapter = new MyAdapter<WinningListData>(MyPriceActivity.this,
					list, R.layout.my_price_item) {
				@Override
				public void convert(ViewHolder helper, int position,
						WinningListData item) {
					helper.setImageBitmap(R.id.item_img, item.getPrizeImgurl());
					helper.setText(R.id.item_name, item.getPrizeName());
					switch (Integer.parseInt(item.getWinGrade())) {
					case 1:
						helper.setText(R.id.item_price, "一等奖");
						break;
					case 2:
						helper.setText(R.id.item_price, "二等奖");
						break;
					case 3:
						helper.setText(R.id.item_price, "三等奖");
						break;
					case 4:
						helper.setText(R.id.item_price, "四等奖");
						break;
					case 5:
						helper.setText(R.id.item_price, "五等奖");
						break;
					}
					if (MyPriceActivity.this.position >= 0
							&& MyPriceActivity.this.position == position) {
						helper.setVisibility(R.id.item_check, View.VISIBLE);
					} else {
						helper.setVisibility(R.id.item_check, View.INVISIBLE);
					}

				}
			};
			mGridView.setAdapter(gridAdapter);
		}
	}

	/**
	 * 赠送奖品
	 * 
	 * @author zhaobin
	 * @param mobile
	 *            手机号
	 * @return int 状态 0:成功 1:手机号码不正确 2:被赠送者不是本公司的会员 3:没有获取到这条中奖纪录
	 *         4:用户已经领取过该奖品，-1:请求网络失败
	 * @throws
	 */
	public void setPrice(String mobile) {
		SetPriceRequest request = new SetPriceRequest();
		SetPriceRequest.Pramater p = request.p;
		p.mobile = mobile;
		p.userId = list.get(position).getUserId();
		p.winningRecordId = list.get(position).getPrizeId();
		p.tokenId = GetUserIdUtil.getTokenId(MyPriceActivity.this);
		final Gson gson = new Gson();
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (result != null) {
					SetPriceResult jsonobj = gson.fromJson(result,
							SetPriceResult.class);
					if (jsonobj.p.isTrue) {
						switch (Integer.parseInt(jsonobj.p.type)) {
						default:
							if (alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							ToastUtils.makeText(MyPriceActivity.this, "赠送失败！",
									Toast.LENGTH_SHORT).show();
							break;
						case 0:
							if (alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							ToastUtils.makeText(MyPriceActivity.this, "赠送成功！",
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							ToastUtils.makeText(MyPriceActivity.this,
									"手机号码不正确！", Toast.LENGTH_SHORT).show();
							break;
						case 2:
							if (alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							shareUtil.postShare(false);
							break;
						case 3:
							if (alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							ToastUtils.makeText(MyPriceActivity.this,
									"没有获取到这条中奖纪录！", Toast.LENGTH_SHORT).show();
							break;
						case 4:
							if (alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							ToastUtils.makeText(MyPriceActivity.this,
									"用户已经领取过该奖品！", Toast.LENGTH_SHORT).show();
							break;
						}
					} else {
						IntentActivity.mIntent(MyPriceActivity.this);
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

	public void initView(View view) {
		mGridView = (PullToRefreshGridView) view.findViewById(R.id.gridview);
		mBtn_get_price = (Button) view.findViewById(R.id.btn_get_price);
		mBtn_set_price = (Button) view.findViewById(R.id.btn_set_price);
		mBtn_share_price = (Button) findViewById(R.id.btn_share_price);
	}

	public void setOnclick() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WinningListData data = list.get(position);

				if (MyPriceActivity.this.position < 0
						|| MyPriceActivity.this.position != position) {
					MyPriceActivity.this.position = position;
					alertDialog = DialogUtils.showDialog(MyPriceActivity.this,
							data.getPrizeName(), data.getPrizeId(),
							data.getPrizeImgurl(),
							getResources().getString(R.string.btn_check),
							MyPriceActivity.this, true);
				} else {
					alertDialog = DialogUtils.showDialog(MyPriceActivity.this,
							data.getPrizeName(), data.getPrizeId(),
							data.getPrizeImgurl(),
							getResources().getString(R.string.btn_no_check),
							MyPriceActivity.this, true);
				}
				if (alertDialog == null) {
					ToastUtils.makeText(MyPriceActivity.this, "奖品详细信息不存在！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		mGridView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page = 0;
						getHttpData(userId);
						mGridView.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page++;
						getHttpData(userId);
						mGridView.onRefreshComplete();
					}
				});
		mBtn_get_price.setOnClickListener(this);
		mBtn_set_price.setOnClickListener(this);
		mBtn_share_price.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Button button = null;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(MyPriceActivity.this);
			break;
		case R.id.btn_get_price:
			btnId = R.id.btn_get_price;
			if (position >= 0) {
				// startForActivity(MyPriceActivity.this,
				// FirmOrderActivity.class,
				// list.get(position));
				shareUtil.postShare();
			} else {
				ToastUtils.makeText(MyPriceActivity.this,
						getResources().getString(R.string.check_toast),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_set_price:
			btnId = R.id.btn_set_price;
			if (position >= 0) {
				alertDialog = DialogUtils.showDialog(MyPriceActivity.this,
						MyPriceActivity.this);
			} else {
				ToastUtils.makeText(MyPriceActivity.this,
						getResources().getString(R.string.check_toast),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_close:
			button = (Button) alertDialog.findViewById(R.id.consult);
			if (button.getText().equals(
					getResources().getString(R.string.btn_check))) {
				MyPriceActivity.this.position = -1;
			}
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		case R.id.consult:
			button = (Button) alertDialog.findViewById(R.id.consult);
			if (button.getText().equals(
					getResources().getString(R.string.btn_no_check))) {
				MyPriceActivity.this.position = -1;
			}
			gridAdapter.notifyDataSetChanged();
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		case R.id.btn:
			EditText editText = (EditText) alertDialog
					.findViewById(R.id.editText);
			String phone = editText.getText().toString().trim();
			setPrice(phone);
			break;
		case R.id.close:
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}

			break;
		case R.id.btn_share_price:
			ShareUtil shareUtil = new ShareUtil(
					MyPriceActivity.this, mContext);
			shareUtil.postShare();
			myReceiver = new MyReceiver() {
				public void dynamic() {
					
				};
			};
			IntentFilter dynamic_filter = new IntentFilter();
			dynamic_filter.addAction(Constant.DYNAMICACTION); //
	
			// 添加动态广播的Action
			registerReceiver(myReceiver, dynamic_filter); // 注册自定义动态广播消息

			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
		}
		AppManager.getAppManager().finishActivity(this);
	}
}
