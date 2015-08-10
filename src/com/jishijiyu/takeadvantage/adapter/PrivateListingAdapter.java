package com.jishijiyu.takeadvantage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.SlideView;
import com.jishijiyu.takeadvantage.activity.SlideView.OnSlideListener;
import com.jishijiyu.takeadvantage.activity.exchangemall.ModifyAddressActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.ResultAddressSlide;
import com.jishijiyu.takeadvantage.entity.request.DeleteAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.DeleteAddressResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

public class PrivateListingAdapter extends BaseAdapter implements
		OnSlideListener {
	/** 获取数据成功 */
	private static final int LOADING_DATA_SUCCESS = 1;
	/** 获取数据失败 */
	private static final int LOADING_DATA_FAILED = 2;
	private Gson gson;
	private String result;
	private Message message;
	private static Activity mContext;
	private LayoutInflater mInflater;
	DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
	DeleteAddressResult deleteAddressResult = new DeleteAddressResult();
	private List<ResultAddressSlide> mMessageItems = new ArrayList<ResultAddressSlide>();
	private SlideView mLastSlideViewWithStatusOn;
	private Boolean issuccess = false;

	public PrivateListingAdapter(Activity context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setmMessageItems(List<ResultAddressSlide> addrList) {
		this.mMessageItems = addrList;
	}

	@Override
	public int getCount() {
		if (mMessageItems == null) {
			mMessageItems = new ArrayList<ResultAddressSlide>();
		}
		return mMessageItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessageItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//
//			case LOADING_DATA_SUCCESS:
//			
//				break;
//			case LOADING_DATA_FAILED:
//				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};

	private boolean requestData(Object entry,final int position ) {
		
		gson = new Gson();
		String request = gson.toJson(entry);
		LogUtil.i(entry + "request" + request);
		HttpConnectTool.update(request, mContext, new ConnectListener() {
			
			@Override
			public void contectSuccess(String result) {
				deleteAddressResult = gson.fromJson(result,
						DeleteAddressResult.class);
               if(deleteAddressResult.p.isTrue){
				if (deleteAddressResult.p.isSucce) {
					mMessageItems.remove(position);
					notifyDataSetChanged();
					ToastUtils.makeText(mContext, "删除地址成功", Toast.LENGTH_SHORT)
							.show();
					issuccess = true;
				} else {
					ToastUtils.makeText(mContext, "删除数据失败", Toast.LENGTH_SHORT)
							.show();
					issuccess = false;
				}
               }else{
            	   Intent intent = new Intent(mContext,LoginActivity.class);
            	   mContext.startActivity(intent);
               }
			}
			
			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
				issuccess = false;
			}
		});
		return issuccess;
//		result = HttpConnectUtil.updata(request, false, null, null);
//		LogUtil.i("result" + result);
//		message = Message.obtain();
//		if (!TextUtils.isEmpty(result)) {
//			message.what = LOADING_DATA_SUCCESS;
//			handler.sendMessage(message);
//			return true;
//		} else {
//			message.what = LOADING_DATA_FAILED;
//
//			handler.sendMessage(message);
//			return false;
//		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		SlideView slideView = (SlideView) convertView;
		if (slideView == null) {
			View itemView = mInflater.inflate(R.layout.more_address_lv_item,
					null);

			slideView = new SlideView(mContext);
			slideView.setContentView(itemView);

			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(this);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}

		ResultAddressSlide item = mMessageItems.get(position);

		item.slideView = slideView;
		item.slideView.shrink();
		holder.nameText.setText(item.address.name);
		holder.addressText.setText(item.address.detailedAddress);
		holder.phoneNumText.setText(item.address.telephone);
		holder.deleteHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteAddressRequest.p.id = ""
						+ mMessageItems.get(position).address.id;
				String id = GetUserIdUtil.getUserId(mContext);
				if (id == null) {
					return;
				} else {
					deleteAddressRequest.p.userId = id;
				}
				String tokenId = GetUserIdUtil.getTokenId(mContext);
				deleteAddressRequest.p.tokenId = tokenId;
//				issuccess = requestData(deleteAddressRequest,position);
				requestData(deleteAddressRequest,position);
//				if (issuccess) {
//					mMessageItems.remove(position);
//					notifyDataSetChanged();
//				}
			}
		});
		holder.writeBtn.setVisibility(View.VISIBLE);
		holder.writeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						ModifyAddressActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey, "isdefult");
				intent.putExtra("address", mMessageItems.get(position).address);
				mContext.startActivity(intent);
				mContext.finish();
			}
		});
		if (item.address.type.equals("2")) {
			holder.isDefultText.setVisibility(View.VISIBLE);
		} else {
			holder.isDefultText.setVisibility(View.GONE); 
		}
		if (item.address.isChecked) {
			holder.isCheaked.setImageResource(R.drawable.shouhuo_btn_choose);
			holder.isCheakedItem
					.setBackgroundResource(R.drawable.shouhuo_choose_dizhi);

		} else {
			holder.isCheaked.setImageResource(R.drawable.shouhuo_btn_no_choose);
			holder.isCheakedItem
					.setBackgroundResource(R.drawable.shouhuo_unchoose_dizhi);


		}
		notifyDataSetChanged();
		return slideView;
	}

	private static class ViewHolder {
		TextView nameText;
		TextView phoneNumText;
		TextView addressText;
		TextView isDefultText;
		ImageView writeBtn;
		ImageView isCheaked;
		LinearLayout isCheakedItem;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
			nameText = (TextView) view
					.findViewById(R.id.more_address_lv_item_name_tv);
			phoneNumText = (TextView) view
					.findViewById(R.id.more_address_lv_item_phonenum_tv);
			addressText = (TextView) view
					.findViewById(R.id.more_address_lv_item_address);
			isDefultText = (TextView) view
					.findViewById(R.id.more_address_lv_item_tv_defult);
			writeBtn = (ImageView) view
					.findViewById(R.id.more_address_write_img);
			isCheaked = (ImageView) view
					.findViewById(R.id.more_address_lv_item_deflt_img);
			isCheakedItem = (LinearLayout) view
					.findViewById(R.id.more_address_lv_item_ll);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}

	}

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}

		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}
}
