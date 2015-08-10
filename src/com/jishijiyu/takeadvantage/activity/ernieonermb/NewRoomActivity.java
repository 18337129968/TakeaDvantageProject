package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.NewRoomRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomTypeQueryTaoCanRequest;
import com.jishijiyu.takeadvantage.entity.result.NewRoomDataResult;
import com.jishijiyu.takeadvantage.entity.result.RoomTypeQueryTaoCanResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 
 * @author zhengjianxiong
 * @content 新建房间
 * @time 2014-7-14
 * 
 */
public class NewRoomActivity extends HeadBaseActivity {
	private TextView tv_5000, tv_2000, tv_1000, tv_500;
	private ListView listView;
	private TextView type1, type2, type3, type4;
	// 消息
	public Message msg;
	// 请求成功，失败
	private static final int SUCCESS = 0;
	private static final int FAIL = 1;
	// 适配器
	public Myadapter adapter;
	public NewRoomDataResult newRoomDataResult;

	// 获取基本数据
	public String RoomId = null, UserId = null, TokenId = null;
	// 根据房间类型查询套餐详情
	public RoomTypeQueryTaoCanResult roomTypeQueryTaoCanResult;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				break;
			case FAIL:
				ToastUtils.makeText(NewRoomActivity.this, "访问服务器失败!", 0).show();
				break;
			default:
				break;
			}

		}

	};

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.tv_5000:
			tv_5000.setBackgroundResource(R.drawable.btn_light);
			tv_2000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_1000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_500.setBackgroundResource(R.drawable.btn_headabove_dark);
			initNewRoom();
			break;
		case R.id.tv_2000:
			tv_2000.setBackgroundResource(R.drawable.btn_light);
			tv_5000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_1000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_500.setBackgroundResource(R.drawable.btn_headabove_dark);
			RoomTypeQueryTaoCan(type2.getText().toString());
			break;
		case R.id.tv_1000:
			tv_1000.setBackgroundResource(R.drawable.btn_light);
			tv_2000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_5000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_500.setBackgroundResource(R.drawable.btn_headabove_dark);
			RoomTypeQueryTaoCan(type3.getText().toString());
			break;
		case R.id.tv_500:
			tv_500.setBackgroundResource(R.drawable.btn_light);
			tv_2000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_1000.setBackgroundResource(R.drawable.btn_headabove_dark);
			tv_5000.setBackgroundResource(R.drawable.btn_headabove_dark);
			RoomTypeQueryTaoCan(type4.getText().toString());
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
		top_text.setText("新建房间");
	}

	@SuppressLint("NewApi")
	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(NewRoomActivity.this,
				R.layout.activity_new_room, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
		initNewRoom();
		initOnclick();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(NewRoomActivity.this,
						TaoCanDetailActivity.class);
				bundle.putString("roomTypeId",
						newRoomDataResult.p.packageVOList.get(arg2).roomTypeId);
				bundle.putString("packageId",
						newRoomDataResult.p.packageVOList.get(arg2).id);
				bundle.putString("UserId",
						GetUserIdUtil.getUserId(NewRoomActivity.this));
				bundle.putString("TokenId",
						GetUserIdUtil.getTokenId(NewRoomActivity.this));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	// 根据房间类型查询
	public void RoomTypeQueryTaoCan(String type) {
		RoomTypeQueryTaoCanRequest roomTypeQueryTaoCanRequest = new RoomTypeQueryTaoCanRequest();
		RoomTypeQueryTaoCanRequest.Pramater pramater = roomTypeQueryTaoCanRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(NewRoomActivity.this);
		pramater.tokenId = GetUserIdUtil.getTokenId(NewRoomActivity.this);
		pramater.roomTypeId = type;
		final Gson gson = new Gson();
		String json = gson.toJson(roomTypeQueryTaoCanRequest);
		HttpConnectTool.update(json, NewRoomActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub

						if (!TextUtils.isEmpty(result)) {
							roomTypeQueryTaoCanResult = gson.fromJson(result,
									RoomTypeQueryTaoCanResult.class);
							if (roomTypeQueryTaoCanResult.p.isTrue) {
								if (roomTypeQueryTaoCanResult.p.packageVOList != null) {
									adapter = new Myadapter(
											NewRoomActivity.this,
											roomTypeQueryTaoCanResult.p.packageVOList);
									listView.setAdapter(adapter);
								}

							} else {
								IntentActivity.mIntent(NewRoomActivity.this);
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
						msg = new Message();
						msg.what = FAIL;
						handler.sendMessage(msg);
					}
				});
	}

	private void initNewRoom() {
		// TODO Auto-generated method stub
		NewRoomRequest newRoomRequest = new NewRoomRequest();
		NewRoomRequest.Parameter parameter = newRoomRequest.p;
		parameter.userId = GetUserIdUtil.getUserId(NewRoomActivity.this);
		parameter.tokenId = GetUserIdUtil.getTokenId(NewRoomActivity.this);
		final Gson gson = new Gson();
		String json = gson.toJson(newRoomRequest);
		HttpConnectTool.update(json, NewRoomActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub

						if (!TextUtils.isEmpty(result)) {
							newRoomDataResult = gson.fromJson(result,
									NewRoomDataResult.class);
							if (newRoomDataResult.p.isTrue) {
								if (newRoomDataResult != null) {
									tv_5000.setText(newRoomDataResult.p.roomTypeList
											.get(3).typeValue);
									type1.setText(newRoomDataResult.p.roomTypeList
											.get(3).id);
									tv_2000.setText(newRoomDataResult.p.roomTypeList
											.get(2).typeValue);
									type2.setText(newRoomDataResult.p.roomTypeList
											.get(2).id);
									tv_1000.setText(newRoomDataResult.p.roomTypeList
											.get(1).typeValue);
									type3.setText(newRoomDataResult.p.roomTypeList
											.get(1).id);
									tv_500.setText(newRoomDataResult.p.roomTypeList
											.get(0).typeValue);
									type4.setText(newRoomDataResult.p.roomTypeList
											.get(0).id);
									if (newRoomDataResult.p.packageVOList != null) {
										adapter = new Myadapter(
												NewRoomActivity.this,
												newRoomDataResult.p.packageVOList);
										listView.setAdapter(adapter);
									}
								}

							} else {
								IntentActivity.mIntent(NewRoomActivity.this);
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
						msg = new Message();
						msg.what = FAIL;
						handler.sendMessage(msg);
					}
				});
	};

	public void initView() {
		tv_5000 = (TextView) findViewById(R.id.tv_5000);
		tv_2000 = (TextView) findViewById(R.id.tv_2000);
		tv_1000 = (TextView) findViewById(R.id.tv_1000);
		tv_500 = (TextView) findViewById(R.id.tv_500);
		listView = (ListView) findViewById(R.id.listview);
		type1 = (TextView) findViewById(R.id.type1);
		type2 = (TextView) findViewById(R.id.type2);
		type3 = (TextView) findViewById(R.id.type3);
		type4 = (TextView) findViewById(R.id.type4);
	}

	public void initOnclick() {
		tv_5000.setOnClickListener(this);
		tv_5000.setBackgroundResource(R.drawable.btn_light);
		tv_2000.setOnClickListener(this);
		tv_1000.setOnClickListener(this);
		tv_500.setOnClickListener(this);
	}

	// 创建房间的适配器
	public class Myadapter<T> extends BaseAdapter {
		public List<T> list;
		public Context context;

		public Myadapter(Context context, List<T> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.new_rom_taocan, null);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
				holder.iv1 = (ImageView) convertView.findViewById(R.id.iv1);
				holder.iv2 = (ImageView) convertView.findViewById(R.id.iv2);
				holder.iv3 = (ImageView) convertView.findViewById(R.id.iv3);
				holder.iv4 = (ImageView) convertView.findViewById(R.id.iv4);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			System.out
					.println(newRoomDataResult.p.packageVOList.get(position).name);
			if (newRoomDataResult.p.packageVOList.get(position).name != null) {
				holder.tv_title.setText(newRoomDataResult.p.packageVOList
						.get(position).name);
			}

			if (newRoomDataResult.p.packageVOList.get(position).gradOne != null
					&& newRoomDataResult.p.packageVOList.get(position).imgUrlOne != null
					&& !newRoomDataResult.p.packageVOList.get(position).gradOne
							.equals("0")) {
				holder.tv1.setText("一等奖");
				ImageLoaderUtil
						.loadImage(
								newRoomDataResult.p.packageVOList.get(position).imgUrlOne,
								holder.iv1);
			}
			if (newRoomDataResult.p.packageVOList.get(position).gradTwo != null
					&& newRoomDataResult.p.packageVOList.get(position).imgUrlTwo != null
					&& !newRoomDataResult.p.packageVOList.get(position).gradTwo
							.equals("0")) {
				holder.tv2.setText("二等奖");
				ImageLoaderUtil
						.loadImage(
								newRoomDataResult.p.packageVOList.get(position).imgUrlTwo,
								holder.iv2);

			}
			if (newRoomDataResult.p.packageVOList.get(position).gradThree != null
					&& newRoomDataResult.p.packageVOList.get(position).imgUrlThree != null
					&& !newRoomDataResult.p.packageVOList.get(position).gradThree
							.equals("0")) {
				holder.tv3.setText("三等奖");
				ImageLoaderUtil
						.loadImage(
								newRoomDataResult.p.packageVOList.get(position).imgUrlThree,
								holder.iv3);
			}
			if (newRoomDataResult.p.packageVOList.get(position).gradFour != null
					&& newRoomDataResult.p.packageVOList.get(position).gradFour != null
					&& !newRoomDataResult.p.packageVOList.get(position).gradFour
							.equals("0")) {
				holder.tv4.setText("四等奖");

				ImageLoaderUtil
						.loadImage(
								newRoomDataResult.p.packageVOList.get(position).imgUrlFour,
								holder.iv4);
			}

			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		ImageView iv1, iv2, iv3, iv4;
	}

}
