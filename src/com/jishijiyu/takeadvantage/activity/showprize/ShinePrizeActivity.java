package com.jishijiyu.takeadvantage.activity.showprize;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase.Mode;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.entity.request.RequestOneRMBShinePrizeList;
import com.jishijiyu.takeadvantage.entity.request.RequestShinePrizeList;
import com.jishijiyu.takeadvantage.entity.result.ResultPrizeList;
import com.jishijiyu.takeadvantage.entity.result.ResultPrizeList.PrizeInfo;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;

public class ShinePrizeActivity extends HeadBaseActivity {
	private final static int REQUEST_NEW = 0;
	private final static int REQUEST_MORE = 1;
	private final static int REFRESH_LISTVIEW = 3;
	private final static int REQUEST_NEW2 = 4;
	private final static int REQUEST_MORE2 = 5;
	private final static int REFRESH_LISTVIEW2 = 6;
	private final static int AGAIN_LOGIN_CODE2 = 7;
	private Button btn_jf_showprice;
	private Button btn_yy_showprice;
	private PullToRefreshListView pl_prize_list;
	private PullToRefreshListView pl_prize_list2;
	private Button btn_show_my_prize;
	private Context mContext;
	private MyPLAdapter myPLAdapter;
	private MyPLAdapter2 myPLAdapter2;
	private Gson gson;
	private int count = 1;// 拍币摇奖页码
	private int count2 = 1;// 一元摇奖页码
	private RequestShinePrizeList requestShinePrizeList;
	private ResultPrizeList resultPrizeList;
	private RequestOneRMBShinePrizeList requestOneRMBShinePrizeList;
	private List<PrizeInfo> list = new ArrayList<PrizeInfo>();
	private List<PrizeInfo> list2 = new ArrayList<PrizeInfo>();
	private int btnId = R.id.btn_jf_showprice;
	// 区分拍币摇奖和一元摇奖
	private int prizeType = 1;
	private final static int INTEGER_PRIZE = 1;
	private final static int ONERMB_PRIZE = 2;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.mine_show_prize));
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(
						ShinePrizeActivity.this);
			}
		});
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ShinePrizeActivity.this,
				R.layout.activity_show_prize, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		mContext = ShinePrizeActivity.this;
		gson = new Gson();
		getJFPrizeList();
		initView(view);
		initView2(view);
		pl_prize_list.setVisibility(View.VISIBLE);
		pl_prize_list2.setVisibility(View.GONE);
		initOnclick();
	}

	private void initOnclick() {
		btn_jf_showprice.setOnClickListener(this);
		btn_yy_showprice.setOnClickListener(this);
		btn_show_my_prize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isHave = true;
				// 跳转到上传晒奖照片界面
				if (isHave) {
					if (prizeType == INTEGER_PRIZE) {
						Intent intent = new Intent(mContext,
								MineShinePrizeDesActivity.class);
						startActivityForResult(intent, REFRESH_LISTVIEW);
					} else if (prizeType == ONERMB_PRIZE) {
						Intent intent = new Intent(mContext,
								MineOneShinePrizeDesActivity.class);
						startActivityForResult(intent, REFRESH_LISTVIEW);
					}
				} else {
					Toast.makeText(mContext, "您没有奖品需要分享", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void getYYPrizeList() {
		requestOneRMBShinePrizeList = new RequestOneRMBShinePrizeList();
		requestOneRMBShinePrizeList.p.pageNum = "" + 1;
		requestOneRMBShinePrizeList.p.pageSize = "10";
		requestOneRMBShinePrizeList.p.userId = GetUserIdUtil
				.getUserId(mContext);
		requestOneRMBShinePrizeList.p.tokenId = GetUserIdUtil
				.getTokenId(mContext);
		String request = gson.toJson(requestOneRMBShinePrizeList);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					resultPrizeList = gson.fromJson(result,
							ResultPrizeList.class);
					if (resultPrizeList.p.isTrue) {
						if (resultPrizeList.p.saOneList.size() <= 0) {
							return;
						}
						list2.clear();
						count2 = 1;
						for (int i = 0; i < resultPrizeList.p.saOneList.size(); i++) {
							list2.add(resultPrizeList.p.saOneList.get(i));
						}
						myPLAdapter2 = new MyPLAdapter2();
						pl_prize_list2.setAdapter(myPLAdapter2);
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent,
								Constant.AGAIN_LOGIN_CODE);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				LogUtil.i(path + "---" + request);
			}
		});
	};

	private void getYYMorePrizeList() {
		requestOneRMBShinePrizeList = new RequestOneRMBShinePrizeList();
		requestOneRMBShinePrizeList.p.pageNum = "" + count2;
		requestOneRMBShinePrizeList.p.pageSize = "10";
		requestOneRMBShinePrizeList.p.tokenId = GetUserIdUtil
				.getTokenId(mContext);
		requestOneRMBShinePrizeList.p.userId = GetUserIdUtil
				.getUserId(mContext);
		String request = gson.toJson(requestOneRMBShinePrizeList);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					if (resultPrizeList.p.isTrue) {
						resultPrizeList = gson.fromJson(result,
								ResultPrizeList.class);
						for (int i = 0; i < resultPrizeList.p.saOneList.size(); i++) {
							list2.add(resultPrizeList.p.saOneList.get(i));
						}
						if (resultPrizeList.p.saOneList.size() <= 0) {
							return;
						}
						myPLAdapter2.notifyDataSetChanged();
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent, AGAIN_LOGIN_CODE2);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				LogUtil.i(path + "---" + request);
			}
		});
	}

	private void getJFPrizeList() {
		requestShinePrizeList = new RequestShinePrizeList();
		requestShinePrizeList.p.pageNum = "" + 1;
		requestShinePrizeList.p.pageSize = "10";
		requestShinePrizeList.p.userId = GetUserIdUtil.getUserId(mContext);
		requestShinePrizeList.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String request = gson.toJson(requestShinePrizeList);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					resultPrizeList = gson.fromJson(result,
							ResultPrizeList.class);
					if (resultPrizeList.p.isTrue) {
						if (resultPrizeList.p.saPointsList.size() <= 0) {
							return;
						}
						list.clear();
						count = 1;
						for (int i = 0; i < resultPrizeList.p.saPointsList
								.size(); i++) {
							list.add(resultPrizeList.p.saPointsList.get(i));
						}
						// LogUtil.i("resultPrizeList:"
						// + resultPrizeList.p.saPointsList.get(1).imgUrl);
						myPLAdapter = new MyPLAdapter();
						pl_prize_list.setAdapter(myPLAdapter);
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent, AGAIN_LOGIN_CODE2);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				LogUtil.i(path + "---" + request);
			}
		});
	}

	private void getMorePrizeList() {
		requestShinePrizeList = new RequestShinePrizeList();
		requestShinePrizeList.p.pageNum = "" + count;
		requestShinePrizeList.p.pageSize = "10";
		requestShinePrizeList.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		requestShinePrizeList.p.userId = GetUserIdUtil.getUserId(mContext);
		String request = gson.toJson(requestShinePrizeList);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					if (resultPrizeList.p.isTrue) {
						resultPrizeList = gson.fromJson(result,
								ResultPrizeList.class);
						for (int i = 0; i < resultPrizeList.p.saPointsList
								.size(); i++) {
							list.add(resultPrizeList.p.saPointsList.get(i));
						}
						if (resultPrizeList.p.saPointsList.size() <= 0) {
							return;
						}
						myPLAdapter.notifyDataSetChanged();
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent,
								Constant.AGAIN_LOGIN_CODE);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				LogUtil.i(path + "---" + request);
			}
		});
	}

	private void initView(View view) {
		btn_jf_showprice = (Button) getView(view, R.id.btn_jf_showprice);
		btn_yy_showprice = (Button) getView(view, R.id.btn_yy_showprice);
		pl_prize_list = (PullToRefreshListView) getView(view,
				R.id.pl_prize_list);
		btn_show_my_prize = (Button) getView(view, R.id.btn_show_my_prize);
		if (list.size() % 10 > 0 && list.size() % 10 < 10) {
			pl_prize_list.setMode(Mode.PULL_FROM_START);// 只可以下拉刷新
		}

		pl_prize_list
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// if (prizeType == 1) {
						new GetDataTask().execute(REQUEST_NEW);
						// } else if (prizeType == 2) {
						// new GetDataTask().execute(REQUEST_NEW2);
						// }
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// if (prizeType == 1) {
						new GetDataTask().execute(REQUEST_MORE);
						// } else if (prizeType == 2) {
						// new GetDataTask().execute(REQUEST_MORE2);
						// }
					}
				});
		pl_prize_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("position" + position);
				if (TextUtils.isEmpty(list.get(position).imgUrl)) {
					Toast.makeText(mContext, "查询详情失败", 0).show();
					return;
				}
				// if (prizeType == INTEGER_PRIZE) {
				Intent intent = new Intent(mContext,
						ShinePrizeDesActivity.class);
				Bundle bundle = new Bundle();
				LogUtil.i("---------" + position);
				bundle.putString("id", list.get(position).id);
				intent.putExtras(bundle);
				startActivity(intent);
				// } else if (prizeType == ONERMB_PRIZE) {
				// Intent intent = new Intent(mContext,
				// ShineOneRMBPrizeDesActivity.class);
				// Bundle bundle = new Bundle();
				// LogUtil.i("---------" + position);
				// bundle.putString("id", list.get(position).id);
				// intent.putExtras(bundle);
				// startActivity(intent);
				// }

			}
		});
	}

	private void initView2(View view) {
		btn_jf_showprice = (Button) getView(view, R.id.btn_jf_showprice);
		btn_yy_showprice = (Button) getView(view, R.id.btn_yy_showprice);
		pl_prize_list2 = (PullToRefreshListView) getView(view,
				R.id.pl_prize_list2);
		btn_show_my_prize = (Button) getView(view, R.id.btn_show_my_prize);
		if (list2.size() % 10 > 0 && list2.size() % 10 < 10) {
			pl_prize_list2.setMode(Mode.PULL_FROM_START);// 只可以下拉刷新
		}

		pl_prize_list2
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// if (prizeType == 1) {
						// new GetDataTask().execute(REQUEST_NEW);
						// } else if (prizeType == 2) {
						new GetDataTask().execute(REQUEST_NEW2);
						// }
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// if (prizeType == 1) {
						// new GetDataTask().execute(REQUEST_MORE);
						// } else if (prizeType == 2) {
						new GetDataTask().execute(REQUEST_MORE2);
						// }
					}
				});
		pl_prize_list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("position" + position);
				if (TextUtils.isEmpty(list2.get(position).imgUrl)) {
					Toast.makeText(mContext, "查询详情失败", 0).show();
					return;
				}
				// if (prizeType == INTEGER_PRIZE) {
				// Intent intent = new Intent(mContext,
				// ShinePrizeDesActivity.class);
				// Bundle bundle = new Bundle();
				// LogUtil.i("---------" + position);
				// bundle.putString("id", list.get(position).id);
				// intent.putExtras(bundle);
				// startActivity(intent);
				// } else if (prizeType == ONERMB_PRIZE) {
				Intent intent = new Intent(mContext,
						ShineOneRMBPrizeDesActivity.class);
				Bundle bundle = new Bundle();
				LogUtil.i("---------" + position);
				bundle.putString("id", list2.get(position).id);
				intent.putExtras(bundle);
				startActivity(intent);
				// }

			}
		});
	}

	private class GetDataTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			return params[0];
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case REQUEST_NEW:
				getJFPrizeList();
				break;
			case REQUEST_MORE:
				getMorePrizeList();
				count++;
				break;
			case REQUEST_NEW2:
				getYYPrizeList();
				break;
			case REQUEST_MORE2:
				getYYMorePrizeList();
				count2++;
				break;

			default:
				break;
			}

			pl_prize_list.onRefreshComplete();
			pl_prize_list2.onRefreshComplete();

		}
	}

	class MyPLAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			View view = null;
			if (view == null) {
				view = View.inflate(mContext, R.layout.layout_show_prize_item,
						null);
				holder.iv_prize_image = (ImageView) view
						.findViewById(R.id.iv_prize_image);
				holder.tv_prize_item_title = (TextView) view
						.findViewById(R.id.tv_prize_item_title);
				holder.tv_prize_item_name = (TextView) view
						.findViewById(R.id.tv_prize_item_name);
				holder.tv_prize_item_des = (TextView) view
						.findViewById(R.id.tv_prize_item_des);
			} else {
				view = convertView;
			}
			String imgUrl = list.get(position).imgUrl;
			if (TextUtils.isEmpty(imgUrl)) {
				imgUrl = Constant.CONNECT_URL;
			}
			LogUtil.i("imgUrl:" + imgUrl);
			ImageLoaderUtil.loadImage(imgUrl, holder.iv_prize_image);
			// if (prizeType == INTEGER_PRIZE) {
			holder.tv_prize_item_title.setText("商品名称:"
					+ list.get(position).awardName);
			// holder.tv_prize_item_name.setText(list.get(position).nikName);
			holder.tv_prize_item_des.setText("获奖感言:"
					+ list.get(position).awardContent);
			// } else if (prizeType == ONERMB_PRIZE) {
			// String award = "";
			// List<PrizeInfo> l = list;
			// PrizeInfo p = l.get(position);
			// int grade = p.awardGrade;
			// if (grade == 1) {
			// award = "一等奖";
			// } else if (grade == 2) {
			// award = "二等奖";
			// } else if (grade == 3) {
			// award = "三等奖";
			// } else if (grade == 4) {
			// award = "四等奖";
			// } else if (grade == 5) {
			// award = "五等奖";
			// }
			// holder.tv_prize_item_title.setText(award + ":"
			// + list.get(position).awardName);
			// holder.tv_prize_item_name.setText("获奖人:"
			// + list.get(position).nikName);
			// holder.tv_prize_item_des.setText("获奖感言:"
			// + list.get(position).awardContent);
			// }
			return view;
		}
	}

	class MyPLAdapter2 extends BaseAdapter {

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			View view = null;
			if (view == null) {
				view = View.inflate(mContext, R.layout.layout_show_prize_item,
						null);
				holder.iv_prize_image = (ImageView) view
						.findViewById(R.id.iv_prize_image);
				holder.tv_prize_item_title = (TextView) view
						.findViewById(R.id.tv_prize_item_title);
				holder.tv_prize_item_name = (TextView) view
						.findViewById(R.id.tv_prize_item_name);
				holder.tv_prize_item_des = (TextView) view
						.findViewById(R.id.tv_prize_item_des);
			} else {
				view = convertView;
			}
			String imgUrl = list2.get(position).imgUrl;
			if (TextUtils.isEmpty(imgUrl)) {
				imgUrl = Constant.CONNECT_URL;
			}
			LogUtil.i("imgUrl:" + imgUrl);
			ImageLoaderUtil.loadImage(imgUrl, holder.iv_prize_image);

			String award = "";
			List<PrizeInfo> l = list2;
			PrizeInfo p = l.get(position);
			int grade = p.awardGrade;
			if (grade == 1) {
				award = "一等奖";
			} else if (grade == 2) {
				award = "二等奖";
			} else if (grade == 3) {
				award = "三等奖";
			} else if (grade == 4) {
				award = "四等奖";
			} else if (grade == 5) {
				award = "五等奖";
			}
			holder.tv_prize_item_title.setText(award + ":"
					+ list2.get(position).awardName);
			holder.tv_prize_item_name.setText("获奖人:"
					+ list2.get(position).nikName);
			holder.tv_prize_item_des.setText("获奖感言:"
					+ list2.get(position).awardContent);

			return view;
		}
	}

	static class ViewHolder {
		ImageView iv_prize_image;
		TextView tv_prize_item_title;
		TextView tv_prize_item_name;
		TextView tv_prize_item_des;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("resultCode:" + resultCode);
		switch (resultCode) {
		case REFRESH_LISTVIEW:
			getJFPrizeList();
			myPLAdapter.notifyDataSetChanged();
			break;
		case Constant.AGAIN_LOGIN_CODE:
			getJFPrizeList();
			myPLAdapter.notifyDataSetChanged();
			break;
		case REFRESH_LISTVIEW2:
			getYYPrizeList();
			myPLAdapter2.notifyDataSetChanged();
			break;
		case AGAIN_LOGIN_CODE2:
			getYYPrizeList();
			myPLAdapter2.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(ShinePrizeActivity.this);
			break;
		case R.id.btn_jf_showprice:
			if (btnId != R.id.btn_jf_showprice) {
				btnId = R.id.btn_jf_showprice;
				btn_yy_showprice
						.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_jf_showprice
						.setBackgroundResource(R.drawable.btn_headabove_light);
				getJFPrizeList();
				prizeType = INTEGER_PRIZE;
				pl_prize_list.setVisibility(View.VISIBLE);
				pl_prize_list2.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_yy_showprice:
			if (btnId != R.id.btn_yy_showprice) {
				btnId = R.id.btn_yy_showprice;
				btn_yy_showprice
						.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_jf_showprice
						.setBackgroundResource(R.drawable.btn_headabove_dark);
				getYYPrizeList();
				prizeType = ONERMB_PRIZE;
				pl_prize_list.setVisibility(View.GONE);
				pl_prize_list2.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

}
