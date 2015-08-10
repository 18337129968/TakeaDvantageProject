package com.jishijiyu.takeadvantage.activity.makemoney;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshGridView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingList;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingList.ParameterA;
import com.jishijiyu.takeadvantage.entity.result.AdvertisingListResult;
import com.jishijiyu.takeadvantage.entity.result.AdvertisingListResult.PosterList;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;

/**
 * 拍币榜
 * 
 * @author zm
 * 
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class IntegralistFragment extends Fragment implements IEarnPointsRequest{

	private Gson gson;
	private Context context;
	String data;
	String tokenId, userId;
	String type;
	private PullToRefreshGridView gridview;
	private MyAdapter<PosterList> adapter = null;
	private int mPage;
	private boolean flag1 = true ,flag2 = true;
	List<PosterList> mList = new ArrayList<AdvertisingListResult.PosterList>();

	public IntegralistFragment(Context mContext, String type) {
		this.context = mContext;
		this.type = type;
		gson = new Gson();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.earn_points_gridview, null);
		gridview = (PullToRefreshGridView) view.findViewById(R.id.gridview);
		tokenId = GetUserIdUtil.getTokenId(context);
		userId = GetUserIdUtil.getUserId(context);
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				"data", Activity.MODE_PRIVATE);
		data = mySharedPreferences.getString("result", null);

		gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				mPage = 0;
				intergraNewList(mPage, true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				mPage++;
				intergraNewList(mPage, false);
			}
		});
		return view;
	}

	@Override
	public void request() {
		// TODO Auto-generated method stub
		intergraNewList(0, true);
		if (type.equals("1")) {
			flag1 = false;
		}else if (type.equals("2")) {
			flag2 = false;
		}
	}

	@Override
	public boolean isFirst() {
		if (type.equals("1")) {
			return flag1;
		}else if (type.equals("2")) {
			return flag2;
		}else {
			return true;
		}
	}

	public void intergraNewList(int page, final boolean isUpOrDown) {
		AdvertisingList parameter = new AdvertisingList();
		parameter.setC(Constant.ADVERTISING_LIST);
		ParameterA parameterA = parameter.new ParameterA();
		parameterA.setPage(page + "");
		parameterA.setPageSize("10");
		parameterA.setType(type);
		parameterA.setTokenId(tokenId);
		parameterA.setUserId(userId);
		parameter.setP(parameterA);
		String json = gson.toJson(parameter);
		HttpConnectTool.update(json, false, context, new ConnectListener() {

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectSuccess(String result) {

				if (!TextUtils.isEmpty(result)) {

					final AdvertisingListResult advertisingListResult = gson
							.fromJson(result, AdvertisingListResult.class);
					if (advertisingListResult.getP().isTrue) {
					final List<PosterList> list = advertisingListResult.getP()
							.getPosterList();
						if (list != null && list.size() > 0) {
							
							if (adapter == null) {
								mList.addAll(list);
								adapter = new MyAdapter<AdvertisingListResult.PosterList>(
										context, list, R.layout.earnpoints_item) {

									@Override
									public void convert(ViewHolder helper,
											int position, PosterList item) {
										helper.setText(R.id.earn_points_title,
												list.get(position).posterTitle);
										String s = ""
												+ (list.get(position).percent
														* list.get(position).posterPrice
														* advertisingListResult.p.exchangeRate / 10000);
										helper.setText(R.id.earn_points_number,
												"拍币  " + s);

										helper.setImageBitmap(
												R.id.earn_points_picture,
												list.get(position).posterImgUrl);

									}
								};
								gridview.setAdapter(adapter);
							} else {
								if (isUpOrDown) {
									mList.clear();
									adapter.upData(list);
									mList.addAll(list);
								} else {
									adapter.AddData(list);
									mList.addAll(list);
								}
								gridview.onRefreshComplete();
								adapter.notifyDataSetChanged();
							}
							mItemClick(advertisingListResult);
						}else{
							gridview.onRefreshComplete();
						}

					} else {
						IntentActivity.mIntent(context);
					}
				}

			}

			@Override
			public void contectFailed(String path, String request) {

			}

		});

	}

	public void mItemClick(final AdvertisingListResult advertisingListResult) {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String s = ""
						+ (mList.get(position).getPercent()
								* mList.get(position).getPosterPrice()
								* advertisingListResult.getP()
										.getExchangeRate() / 10000);
				Intent intent = new Intent();
				intent.setClass(getActivity(), WatchAdvertisementActivity.class);
				intent.putExtra("position", mList.get(position).getId());
				intent.putExtra("poster", s);
				startActivity(intent);

			}
		});
	}

}
