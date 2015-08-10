package com.jishijiyu.takeadvantage.activity.ernie;

import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshGridView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.AddErnieRequest;
import com.jishijiyu.takeadvantage.entity.request.CheckPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.AddErnieRequest.Pramater;
import com.jishijiyu.takeadvantage.entity.result.AddErnieResult;
import com.jishijiyu.takeadvantage.entity.result.CheckPriceResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.CheckPriceResult.PrizeList;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult.Enroll;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.Constants;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 选择奖品
 * 
 * @author zhaobin
 */
public class CheckPriceActivity extends HeadBaseActivity {
	private PullToRefreshGridView mGridView = null;
	private Button btn_bm = null;
	private TextView tv_check_price = null;
	private MyAdapter<CheckPriceResult.PrizeList> gridAdapter = null;
	private int position = -1;
	private AlertDialog alertDialog = null;
	private CheckPriceResult priceResult = null;
	private List<CheckPriceResult.PrizeList> prizeLists = null;
	private String userId = null;
	private static final int num = 500;
	private int page = 0;
	private int pageSize = 9;
	private Gson gson = null;
	private String text = null;
	private String text1 = null;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.check_price));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(CheckPriceActivity.this, R.layout.check_price,
				null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		text = getResources().getString(R.string.check_price_text);
		text1 = getResources().getString(R.string.check_price_text1);

		initView(view);
		initOnclick();
	}

	@Override
	protected void onResume() {
		super.onResume();
		userId = GetUserIdUtil.getUserId(CheckPriceActivity.this);
		page = 0;
		getResult();
	}

	public void initView(View view) {
		mGridView = (PullToRefreshGridView) view.findViewById(R.id.gridview);
		btn_bm = (Button) view.findViewById(R.id.btn_bmcj);
		tv_check_price = (TextView) view.findViewById(R.id.tv_check_price);
		tv_check_price.setText(text);
	}

	public void initOnclick() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PrizeList prizeList = prizeLists.get(position);

				if (CheckPriceActivity.this.position < 0
						|| CheckPriceActivity.this.position != position) {
					CheckPriceActivity.this.position = position;
					alertDialog = DialogUtils.showDialog(
							CheckPriceActivity.this, prizeList.name,
							prizeList.id, prizeList.prizeImg, getResources()
									.getString(R.string.btn_check),
							CheckPriceActivity.this, true);
				} else {
					alertDialog = DialogUtils.showDialog(
							CheckPriceActivity.this, prizeList.name,
							prizeList.id, prizeList.prizeImg, getResources()
									.getString(R.string.btn_no_check),
							CheckPriceActivity.this, true);
				}
			}
		});
		mGridView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page = 0;
						getResult();
						mGridView.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page++;
						getResult();
						mGridView.onRefreshComplete();
					}
				});
		btn_bm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Button button = null;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(CheckPriceActivity.this);
			break;
		case R.id.btn_bmcj:
			if (position >= 0) {
				alertDialog = DialogUtils.showDialog(CheckPriceActivity.this,
						"参加摇奖扣除拍币" + num, "提示：报名后为参加摇奖，系统将自动抽奖，并扣除托管拍币50",
						new int[] { R.string.ext, R.string.end },
						CheckPriceActivity.this);
			} else {
				ToastUtils.makeText(CheckPriceActivity.this,
						getResources().getString(R.string.check_toast),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_close:
			button = (Button) alertDialog.findViewById(R.id.consult);
			if (button.getText().equals(
					getResources().getString(R.string.btn_check))) {
				CheckPriceActivity.this.position = -1;
				tv_check_price.setText(text);
			}
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		case R.id.consult:
			button = (Button) alertDialog.findViewById(R.id.consult);
			if (button.getText().equals(
					getResources().getString(R.string.btn_no_check))) {
				CheckPriceActivity.this.position = -1;
				tv_check_price.setText(text);
			} else {
				tv_check_price.setText(text1 + prizeLists.get(position).name);
			}
			gridAdapter.notifyDataSetChanged();
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		case R.id.left:
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		case R.id.right:
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			// TODO
			CheckPriceResult.PrizeList prizeList = prizeLists.get(position);
			AddErnieRequest addErnieRequest = new AddErnieRequest();
			Pramater pramater = addErnieRequest.p;
			pramater.ernieId = prizeList.ernieId;
			pramater.prizeId = prizeList.id;
			pramater.userId = userId;
			pramater.tokenId = GetUserIdUtil
					.getTokenId(CheckPriceActivity.this);
			if (gson == null) {
				gson = new Gson();
			}
			String json = gson.toJson(addErnieRequest);
			Log.e("Request", json);
			HttpConnectTool.update(json, this, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						Log.e("Result", result);
						AddErnieResult addErnieResult = gson.fromJson(result,
								AddErnieResult.class);
						if (addErnieResult.p.isTrue) {
							if (addErnieResult.p.type != null) {
								switch (Integer.parseInt(addErnieResult.p.type)) {
								case 0:
									if (addErnieResult.p.enroll != null) {
										applyWin(result, addErnieResult);
									}
									break;
								case 1:
									ToastUtils.makeText(
											CheckPriceActivity.this, "没有当前用户！",
											Toast.LENGTH_SHORT).show();
									break;
								case 2:
									ToastUtils.makeText(
											CheckPriceActivity.this, "用户已报名！",
											Toast.LENGTH_SHORT).show();
									Constants.isApply = true;
									AppManager.getAppManager().finishActivity(
											CheckPriceActivity.this);
									break;
								case 3:
									ToastUtils
											.makeText(CheckPriceActivity.this,
													"没有当前奖品，请重新选择！",
													Toast.LENGTH_SHORT).show();
									break;
								case 4:
									ToastUtils.makeText(
											CheckPriceActivity.this,
											"用户的拍币不足，不能选择该商品报名！",
											Toast.LENGTH_SHORT).show();
									break;
								case 5:
									ToastUtils.makeText(
											CheckPriceActivity.this,
											"摇奖表ID不对！", Toast.LENGTH_SHORT)
											.show();
									break;
								case 6:
									ToastUtils.makeText(
											CheckPriceActivity.this,
											"报名时间还没有开始 ！", Toast.LENGTH_SHORT)
											.show();
									break;
								case 7:
									ToastUtils.makeText(
											CheckPriceActivity.this,
											"报名时间已经结束！", Toast.LENGTH_SHORT)
											.show();
									AppManager.getAppManager().finishActivity(
											CheckPriceActivity.this);
									break;
								}
								CheckPriceActivity.this.position = -1;
								gridAdapter.notifyDataSetChanged();
							} else if (addErnieResult.p.enroll != null) {
								applyWin(result, addErnieResult);
							}
						} else {
							IntentActivity.mIntent(CheckPriceActivity.this);
						}
					}
				}

				@Override
				public void contectStarted() {

				}

				@Override
				public void contectFailed(String path, String request) {
					ToastUtils.makeText(CheckPriceActivity.this, "报名失败！",
							Toast.LENGTH_SHORT).show();
				}
			});
			break;
		}
	}

	private void getResult() {
		final Gson gson = new Gson();
		CheckPriceRequest priceRequest = new CheckPriceRequest();
		CheckPriceRequest.Pramater pramater = priceRequest.p;
		pramater.ernieId = GetUserIdUtil.getErnieId(CheckPriceActivity.this);
		pramater.page = page + "";
		pramater.pageSize = pageSize + "";
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(priceRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					priceResult = gson.fromJson(result, CheckPriceResult.class);
					if (priceResult != null) {
						if (priceResult.p.isTrue) {
							switch (Integer.parseInt(priceResult.p.type)) {
							case 0:
								if (priceResult.p.prizeList != null
										&& priceResult.p.prizeList.size() >= 0) {
									if (page != 0) {
										if (prizeLists != null
												&& prizeLists.size() > 0) {
											for (int i = 0; i < priceResult.p.prizeList
													.size(); i++) {
												prizeLists
														.add(priceResult.p.prizeList
																.get(i));
											}
											gridAdapter.refresh(prizeLists);
										}
									} else {
										prizeLists = priceResult.p.prizeList;
										gridAdapter = new MyAdapter<CheckPriceResult.PrizeList>(
												CheckPriceActivity.this,
												prizeLists,
												R.layout.check_price_item) {

											@Override
											public void convert(
													ViewHolder helper,
													int position,
													CheckPriceResult.PrizeList item) {
												helper.setImageBitmap(
														R.id.item_img,
														item.prizeImg);
												helper.setText(R.id.item_tv,
														item.name);
												if (CheckPriceActivity.this.position >= 0
														&& CheckPriceActivity.this.position == position) {
													helper.setVisibility(
															R.id.item_bg,
															View.VISIBLE);
												} else {
													helper.setVisibility(
															R.id.item_bg,
															View.GONE);
												}
											}
										};
										mGridView.setAdapter(gridAdapter);
									}
								} else {
									if (page > 0) {
										page = page - 1;
									}
								}
								break;
							case 1:
								ToastUtils.makeText(CheckPriceActivity.this,
										"没有当前摇奖对象!", Toast.LENGTH_SHORT).show();
								break;
							case 2:
								if (prizeLists == null
										|| prizeLists.size() <= 0) {
									ToastUtils.makeText(
											CheckPriceActivity.this,
											" 没有当前摇奖期数的奖品信息!",
											Toast.LENGTH_SHORT).show();
								}
								break;
							}
						} else {
							IntentActivity.mIntent(CheckPriceActivity.this);
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

	public void applyWin(String result, AddErnieResult addErnieResult) {
		String login = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		// getHttpData(userId);
		if (!TextUtils.isEmpty(result)) {
			if (gson == null) {
				gson = new Gson();
			}
			Log.e("Result", result);
			LoginUserResult loginUserResult = gson.fromJson(login,
					LoginUserResult.class);
			if (loginUserResult != null && addErnieResult != null
					&& addErnieResult.p.enroll != null) {
				LoginUserResult.Enroll enroll = new Enroll();
				AddErnieResult.Enroll enroll2 = addErnieResult.p.enroll;
				enroll.createTime = enroll2.createTime;
				enroll.ernieId = Integer.parseInt(enroll2.ernieId);
				enroll.id = Integer.parseInt(enroll2.id);
				enroll.joinBegintime = enroll2.joinBegintime;
				enroll.prizeId = Integer.parseInt(enroll2.prizeId);
				enroll.userId = Integer.parseInt(enroll2.userId);
				loginUserResult.p.enroll = enroll;
				SPUtils.put(mContext, Constant.USER_INFO_FILE_NAME,
						gson.toJson(loginUserResult));
			}
		}
		Constants.isApply = true;
		if (IntegralOperationfUtil.minusIntegral(CheckPriceActivity.this, num)) {
			AppManager.getAppManager().finishActivity(CheckPriceActivity.this);
			ToastUtils.makeText(CheckPriceActivity.this, "报名成功！",
					Toast.LENGTH_SHORT).show();
		} else {
			ToastUtils.makeText(CheckPriceActivity.this, "拍币不足！",
					Toast.LENGTH_SHORT).show();
		}
	}
}
