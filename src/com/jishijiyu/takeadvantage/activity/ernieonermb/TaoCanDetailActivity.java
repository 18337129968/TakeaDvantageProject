package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayMoneyActivity;
import com.jishijiyu.takeadvantage.entity.request.TaoCanDetailARequest;
import com.jishijiyu.takeadvantage.entity.result.TaoCanDetailResult;
import com.jishijiyu.takeadvantage.entity.result.TaoCanDetailResult.prizeCompanyList;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

public class TaoCanDetailActivity extends HeadBaseActivity {
	private ListView listView;
	private Button btn_new;
	private Intent intent;
	public Myadapter adapter;
	public TaoCanDetailResult taoCanDetailResult;
	public String packageId;
	// 获取基本数据
	public String RoomId = null, UserId = null, TokenId = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 返回
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.btn_new:
			Bundle bundle = new Bundle();
			intent = new Intent();
			intent.setClass(this, RoomListActivity.class);
			bundle.putString("RoomId", packageId);
			bundle.putString("UserId", UserId);
			bundle.putString("TokenId", TokenId);
			intent.setClass(TaoCanDetailActivity.this, PayMoneyActivity.class);
			startActivity(intent);
			TaoCanDetailActivity.this.finish();
			break;
		default:
			break;
		}
	}  
	 
	
	
	    

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		btn_left.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_left.setText("返回");
		top_text.setText("套餐详情");
	}

	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(TaoCanDetailActivity.this,
				R.layout.activity_taocan_detail, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		packageId = bundle.getString("packageId");
		UserId = bundle.getString("UserId");
		TokenId = bundle.getString("TokenId");
		RoomId = bundle.getString("RoomId");
		initView();
		initOnclick();
		// 获取订单详情
		TaoCanDetail(packageId);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(TaoCanDetailActivity.this,
						TaoCanPrizeActivity.class);
				bundle.putString(
						"companyId",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).companyId);
				bundle.putString("prizeImg",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).imgUrl);
				bundle.putString("prizePrice",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).price);
				bundle.putString("prizeName",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).name);
				bundle.putString(
						"prizeExplain",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).prizeExplain);
				bundle.putString("prizeId",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).id);
				bundle.putString("packageId", packageId);
				bundle.putString(
						"companyName",
						taoCanDetailResult.p.prizeCompanyList.get(arg2).companyName);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void TaoCanDetail(String packageId) {
		// TODO Auto-generated method stub
		TaoCanDetailARequest taoCanDetailARequest = new TaoCanDetailARequest();
		TaoCanDetailARequest.Parameter parameter = taoCanDetailARequest.p;
		parameter.userId = GetUserIdUtil.getUserId(TaoCanDetailActivity.this);
		parameter.tokenId = GetUserIdUtil.getTokenId(TaoCanDetailActivity.this);
		parameter.packageId = packageId;
		final Gson gson = new Gson();
		String json = gson.toJson(taoCanDetailARequest);
		HttpConnectTool.update(json, TaoCanDetailActivity.this,
				new ConnectListener() {

					public void contectSuccess(String result) {

						if (!TextUtils.isEmpty(result)) {
							taoCanDetailResult = gson.fromJson(result,
									TaoCanDetailResult.class);
							if (taoCanDetailResult.p.isTrue) {
								if (taoCanDetailResult.p.prizeCompanyList != null) {
									adapter = new Myadapter(
											taoCanDetailResult.p.prizeCompanyList);
									listView.setAdapter(adapter);
								}
							} else {
								IntentActivity
										.mIntent(TaoCanDetailActivity.this);
							}
						}
					}

					public void contectStarted() {

					}

					public void contectFailed(String path, String request) {

					}
				});
	}

	public void initView() {
		listView = (ListView) findViewById(R.id.new_listview);
		btn_new = (Button) findViewById(R.id.btn_new);
	}

	public void initOnclick() {
		btn_new.setOnClickListener(this);
	}

	// 创建套餐详情的适配器
	public class Myadapter extends BaseAdapter {
		private List<prizeCompanyList> list;

		public Myadapter(List<prizeCompanyList> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(
						R.layout.activity_taocan_detail_istview_item, null);
				holder.tv_tc_money = (TextView) convertView
						.findViewById(R.id.tv_tc_money);
				holder.tv_aword = (TextView) convertView
						.findViewById(R.id.tv_aword);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tc_img = (ImageView) convertView
						.findViewById(R.id.tc_img);
				holder.tv_miaoshu = (TextView) convertView
						.findViewById(R.id.tv_miaoshu);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_tc_money
					.setText("市场价："
							+ taoCanDetailResult.p.prizeCompanyList
									.get(position).price);
			if (taoCanDetailResult.p.prizeCompanyList.get(position).grade
					.equals("1")) {
				holder.tv_aword.setText("一等奖");
			} else if (taoCanDetailResult.p.prizeCompanyList.get(position).grade
					.equals("2")) {
				holder.tv_aword.setText("二等奖");
			} else if (taoCanDetailResult.p.prizeCompanyList.get(position).grade
					.equals("3")) {
				holder.tv_aword.setText("三等奖");
			} else {
				holder.tv_aword.setText("四等奖");
			}
			holder.tv_miaoshu.setText(taoCanDetailResult.p.prizeCompanyList
					.get(position).prizeExplain);
			holder.tv_content.setText(taoCanDetailResult.p.prizeCompanyList
					.get(position).name);
			ImageLoaderUtil.loadImage(
					taoCanDetailResult.p.prizeCompanyList.get(position).imgUrl,
					holder.tc_img);
			return convertView;
		}

		class ViewHolder {
			TextView tv_tc_money;
			TextView tv_aword;
			TextView tv_content, tv_miaoshu;
			ImageView tc_img;
		}
	}
}
