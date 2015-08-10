package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

public class MerchantMapActivity extends HeadBaseActivity{

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView merchantname,location;
	private Button save;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		top_text.setText(getResources().getString(R.string.merchant_map));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this, R.layout.activity_merchantmap, null);
		base_centent.addView(view);
		mMapView = (MapView)view.findViewById(R.id.mapView);
		init();
	}
	
	private void init() {
		mBaiduMap = mMapView.getMap();
		merchantname = (TextView) findViewById(R.id.merchantname);
		location = (TextView) findViewById(R.id.location);
		save = (Button) findViewById(R.id.save);
		
		save.setOnClickListener(this);
		merchantname.setText("商家："+getIntent().getStringExtra("merchantName"));
		
		mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				MarkerOptions option = new MarkerOptions().icon(bdA).position(arg0);
				mBaiduMap.addOverlay(option);
				location.setText(arg0.latitude+","+arg0.longitude);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.save:
			SharedPreferences sp = getSharedPreferences("merchant_location", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("location", location.getText().toString());
			editor.commit();
			ToastUtils.makeText(mContext, "已保存", 0).show();
			break;
		default:
			break;
		}
	}
}
