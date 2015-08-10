package com.jishijiyu.takeadvantage.activity.myinformation;

import java.util.ArrayList;
import java.util.List;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ListViewCompat;
import com.jishijiyu.takeadvantage.activity.exchangemall.AddAddressActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.adapter.PrivateListingAdapter;
import com.jishijiyu.takeadvantage.entity.ResultAddressSlide;
import com.jishijiyu.takeadvantage.entity.request.RequestAddress;
import com.jishijiyu.takeadvantage.entity.result.ResultAddress;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 请求收货地址&删除收货地址
 * 
 * @author zcl
 * 
 */
public class MoreAddressCanRemoveActivity extends HeadBaseActivity {

	List<ResultAddressSlide> list = new ArrayList<ResultAddressSlide>();
	private Gson gson;
	RequestAddress requestAddress = new RequestAddress();
	ResultAddress resultAddress = new ResultAddress();
	int position;
	private ListViewCompat lv;
	PrivateListingAdapter mAdapter;
	public String postalCode;
	public int userId;
	public int id;
	public String province;
	public String city;
	public String area;
	public String detailedAddress;
	public String name;
	public String telephone;
	public String type;
	public Boolean isCheack;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_address_add_btn:
			startForActivity(mContext, AddAddressActivity.class, "isdefult");
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.more_address));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this,
				R.layout.activity_more_address_canremove, null);
		base_centent.addView(view);
		mAdapter = new PrivateListingAdapter(this);
		init();
		setdata();

	}

	private void init() {
		lv = (ListViewCompat) findViewById(R.id.add_address_canremove_lv);
		Button add = (Button) findViewById(R.id.more_address_add_btn);
		mAdapter.setmMessageItems(list);
		lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				for (int i = 0; i < list.size(); i++) {
					list.get(i).address.isChecked = false;
					if (!list.get(position).address.isChecked) {
						list.get(position).address.isChecked = true;
						mAdapter.notifyDataSetChanged();
					}

				}
			}
		});
		add.setOnClickListener(this);
	}

	private void requestData(Object entry) {
		gson = new Gson();
		String request = gson.toJson(entry);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				ResultAddress resultAddress = gson.fromJson(result,
						ResultAddress.class);
				if (resultAddress.p.isTrue) {
					for (int i = 0; i < resultAddress.p.addrList.size(); i++) {
						ResultAddressSlide resultAddressSlide = new ResultAddressSlide();
						if (resultAddress.p.addrList.get(i).type.equals("2")) {
							resultAddress.p.addrList.get(i).isChecked = true;
						}
						name = resultAddress.p.addrList.get(i).name;
						detailedAddress = resultAddress.p.addrList.get(i).detailedAddress;
						telephone = resultAddress.p.addrList.get(i).telephone;
						province = resultAddress.p.addrList.get(i).province;
						city = resultAddress.p.addrList.get(i).city;
						area = resultAddress.p.addrList.get(i).area;
						id = resultAddress.p.addrList.get(i).id;
						postalCode = resultAddress.p.addrList.get(i).postalCode;
						type = resultAddress.p.addrList.get(i).type;
						resultAddressSlide.address = resultAddress.p.addrList
								.get(i);

						list.add(resultAddressSlide);

					}
				} else {
					startForActivity(mContext, LoginActivity.class, null);
				}
				init();
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void setdata() {

		String id = GetUserIdUtil.getUserId(this);
		if (id == null) {
			return;
		} else {
			requestAddress.p.userId = id;
		}
		String token = GetUserIdUtil.getTokenId(this);
		requestAddress.p.tokenId = token;
		requestData(requestAddress);

	}
}
