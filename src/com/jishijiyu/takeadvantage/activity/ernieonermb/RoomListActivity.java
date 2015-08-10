package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayMoneyActivity;
import com.jishijiyu.takeadvantage.entity.request.BillMutDelRequest;
import com.jishijiyu.takeadvantage.entity.request.BillSingleDelRequest;
import com.jishijiyu.takeadvantage.entity.request.RoomBillRequest;
import com.jishijiyu.takeadvantage.entity.request.SubmitBillRequest;
import com.jishijiyu.takeadvantage.entity.result.BillMutDelResult;
import com.jishijiyu.takeadvantage.entity.result.BillSingleDelResult;
import com.jishijiyu.takeadvantage.entity.result.RoomBillResult;
import com.jishijiyu.takeadvantage.entity.result.RoomBillResult.InventoryVOList;
import com.jishijiyu.takeadvantage.entity.result.RoomBillResult.Parameter;
import com.jishijiyu.takeadvantage.entity.result.SubmitBillResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 
 * @author zhengjianxiong
 * @content 清单界面
 * 
 */
public class RoomListActivity extends HeadBaseActivity {
	private Button back, right;
	private Context mContext = RoomListActivity.this;
	private TextView tv_des;
	private TextView statistics_count;
	private Button statistics_submit;
	private ListView statistics_list;
	private Intent intent;
	private RoomBillRequest roomBillRequest;
	private RoomBillResult roomBillResult;
	private String roomIds;
	private SubmitBillResult sumBillResult;
	private BillSingleDelResult billSingleDelResult;
	private BillMutDelResult billMutDelResult;
	private Parameter roomBillParameter;
	private MyListViewAdapter mAdapter;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText("清单");
		btn_left.setOnClickListener(this);

	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(RoomListActivity.this,
				R.layout.activity_room_list, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
		initData();
	}

	private void initView() {
		tv_des = (TextView) findViewById(R.id.tv_des);
		statistics_count = (TextView) findViewById(R.id.join_in_room_statistics_count);
		statistics_submit = (Button) findViewById(R.id.join_in_room_statistics_submit);
		statistics_list = (ListView) findViewById(R.id.join_in_room_statistics_list);
		// statistics_list.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// ToastUtils.makeText(mContext, "sadfdsa", Toast.LENGTH_LONG).show();
		// }
		// });
		statistics_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mAdapter != null && mAdapter.selectedCum > 0) {
					onSubmitBill();
				} else {
					ToastUtils.makeText(mContext, "请选择订单", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private void initData() {
		RoomBillRequest roomBillRequest = new RoomBillRequest();
		roomBillRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		roomBillRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		final Gson gson = new Gson();
		String requestJson = gson.toJson(roomBillRequest);
		HttpConnectTool.update(requestJson, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(result)) {
					roomBillResult = gson
							.fromJson(result, RoomBillResult.class);
					roomBillParameter = roomBillResult.p;
					if (roomBillParameter.isTrue) {
						if (roomBillParameter.InventoryVOList != null) {
							int selectNum = roomBillParameter.InventoryVOList
									.size();
							for (int i = 0; i < roomBillParameter.InventoryVOList
									.size(); i++) {
								roomBillParameter.InventoryVOList.get(i).isSelected = true;
							}
							mAdapter = new MyListViewAdapter(
									roomBillParameter.InventoryVOList,
									RoomListActivity.this, selectNum);
							statistics_list.setAdapter(mAdapter);
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
				// TODO Auto-generated method stub
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();
			}
		});
	}

	private void onSubmitBill() {
		roomIds = "";
		SubmitBillRequest sRequest = new SubmitBillRequest();
		sRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		sRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		if (mAdapter.selectedCum == 1) {
			for (int i = 0; i < mAdapter.list.size(); i++) {
				if (mAdapter.list.get(i).isSelected) {
					roomIds += mAdapter.list.get(i).dollerRoomId;
				}
			}
		} else if (mAdapter.selectedCum > 1) {
			for (int j = 0; j < mAdapter.list.size(); j++) {

				if (mAdapter.list.get(j).isSelected) {
					roomIds += mAdapter.list.get(j).dollerRoomId + ",";
				}
			}
			roomIds = roomIds.substring(0, roomIds.length() - 1);
		}
		sRequest.p.roomIds = roomIds;
		final Gson gson = new Gson();
		String request = gson.toJson(sRequest);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(result)) {
					sumBillResult = gson.fromJson(result,
							SubmitBillResult.class);
					if (sumBillResult.p.isTrue) {
						intent = new Intent();
						intent.setClass(RoomListActivity.this,
								PayMoneyActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("RoomId", roomIds);
						bundle.putString("UserId",
								GetUserIdUtil.getUserId(mContext));
						bundle.putString("TokenId",
								GetUserIdUtil.getTokenId(mContext));
						bundle.putString("checkedNum", mAdapter.selectedCum
								+ "");
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
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
				// TODO Auto-generated method stub
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();
			}
		});

	}

	private void delSingle(final int position) {
		BillSingleDelRequest bRequest = new BillSingleDelRequest();
		bRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		bRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		bRequest.p.inventoryId = mAdapter.list.get(position).dollerRoomId + "";
		final Gson gson = new Gson();
		String request = gson.toJson(bRequest);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(result)) {
					billSingleDelResult = gson.fromJson(result,
							BillSingleDelResult.class);
					if (billSingleDelResult.p.isTrue) {
						if (billSingleDelResult.p.isSucce) {

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
				// TODO Auto-generated method stub
				ToastUtils.makeText(mContext, "访问服务器失败", 0).show();
			}
		});

	}

	class MyListViewAdapter extends BaseAdapter {

		List<RoomBillResult.InventoryVOList> list;
		Activity context;
		int selectedCum = 0;

		public MyListViewAdapter(List<InventoryVOList> list, Activity context,
				int selectCum) {
			super();
			this.list = list;
			this.context = context;
			this.selectedCum = selectCum;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.layout_room_list_item, null);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll1);
				// holder.ll.setVisibility(View.GONE);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.is_check);
				holder.room_list_item_imag = (ImageView) convertView
						.findViewById(R.id.room_list_item_img);
				holder.room_list_item_prise = (TextView) convertView
						.findViewById(R.id.room_list_item_prise);
				holder.room_list_item_num = (TextView) convertView
						.findViewById(R.id.room_qishu);
				holder.room_list_item_type = (TextView) convertView
						.findViewById(R.id.tv_men);
				holder.room_list_item_count_total = (TextView) convertView
						.findViewById(R.id.tv_xuqiu);
				holder.del = (LinearLayout) convertView.findViewById(R.id.del);
				holder.room_list_item_shengyu = (TextView) convertView
						.findViewById(R.id.tv_shengyu);
				holder.room_list_item_count_remainder_num = (TextView) convertView
						.findViewById(R.id.tv_money);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			InventoryVOList inventory = list.get(position);
			holder.room_list_item_prise
					.setText("房间" + inventory.roomIssue + "");
			holder.room_list_item_num.setText("期数" + inventory.mealPerios + "");
			holder.room_list_item_type.setText(inventory.mealType + "");
			// holder.room_list_item_count_total.setText(inventory.roomIssue);
			// holder.room_list_item_shengyu.setText(inventory.roomIssue);
			ImageLoaderUtil.loadImage(inventory.prizeImg,
					holder.room_list_item_imag);
			if (list.get(position).isSelected) {
				holder.checkBox.setChecked(true);
			} else {
				holder.checkBox.setChecked(false);
			}
			tv_des.setText("共参加" + selectedCum + "个活动");
			statistics_count.setText("共计:" + selectedCum * 100 + "金币");
			holder.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (((CheckBox) v).isChecked()) {
						// checkedlist.add(position);
						selectedCum++;
						list.get(position).isSelected = true;
					} else {
						// checkedlist.remove((Object) position);
						selectedCum--;
						list.get(position).isSelected = false;
					}
					tv_des.setText("共参加" + selectedCum + "个活动");
					statistics_count.setText("共计:" + selectedCum * 100 + "金币");
				}
			});
			holder.del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (list.get(position).isSelected) {
						list.get(position).isSelected = false;
						selectedCum--;
						tv_des.setText("共参加" + selectedCum + "个活动");
						statistics_count.setText("共计:" + selectedCum * 100
								+ "金币");
					}
					delSingle(position);
					list.remove(position);
					notifyDataSetChanged();
				}
			});

			return convertView;
		}

		public void delMut() {
			String ids = "";
			if (selectedCum == 1) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).isSelected) {
						ids += list.get(i).id;
					}
				}
			} else if (selectedCum > 1) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).isSelected) {
						ids += list.get(i).id + ",";
					}
				}
				ids = ids.substring(0, ids.length() - 1);
			}
			BillMutDelRequest billMutDelRequest = new BillMutDelRequest();
			billMutDelRequest.p.inventoryIds = ids;
			billMutDelRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			billMutDelRequest.p.userId = GetUserIdUtil.getUserId(mContext);
			final Gson gson = new Gson();
			String request = gson.toJson(billMutDelRequest);
			HttpConnectTool.update(request, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					// TODO Auto-generated method stub
					if (!TextUtils.isEmpty(result)) {
						billMutDelResult = gson.fromJson(result,
								BillMutDelResult.class);
						if (billMutDelResult.p.isTrue) {
							if (billMutDelResult.p.isSucce) {
								List<RoomBillResult.InventoryVOList> temp = new ArrayList<RoomBillResult.InventoryVOList>();
								for (int i = 0; i < list.size(); i++) {
									if (!list.get(i).isSelected) {
										temp.add(list.get(i));
									}
								}
								list.clear();
								for (int j = 0; j < temp.size(); j++) {
									list.add(temp.get(j));
								}
								selectedCum = 0;
								tv_des.setText("共参加" + selectedCum + "个活动");
								statistics_count.setText("共计:" + selectedCum
										+ "元");
								// checkedlist.clear();
								// ToastUtils.makeText(mContext,
								// checkedlist.size()+"fsdf"+ list.size(),
								// Toast.LENGTH_LONG).show();
								notifyDataSetChanged();
							}
						} else {
							ToastUtils.makeText(mContext, "UUID失效", 0).show();
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

				}
			});
		}
	}

	class ViewHolder {
		CheckBox checkBox;
		ImageView room_list_item_imag;
		TextView room_list_item_prise;
		TextView room_list_item_num;
		TextView room_list_item_type;
		TextView room_list_item_count_total;
		TextView room_list_item_shengyu;
		TextView room_list_item_count_remainder_num;
		LinearLayout del;
		LinearLayout ll;
	}

}
