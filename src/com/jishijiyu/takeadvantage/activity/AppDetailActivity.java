package com.jishijiyu.takeadvantage.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dlnetwork.AdType;
import com.dlnetwork.DevInit;
import com.jishijiyu.takeadvantage.R;

public class AppDetailActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail_layout);
		Intent intent = getIntent();
		final String name = intent.getStringExtra("name");
		String title = intent.getStringExtra("title");
		String desc = intent.getStringExtra("description");
		String cate = intent.getStringExtra("cate");
		String number = intent.getStringExtra("number");
		String share_number=intent.getStringExtra("share_number");
		String money=intent.getStringExtra("money");
		final String pack_name=intent.getStringExtra("pack_name");
		
		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
		TextView tv_cate = (TextView) findViewById(R.id.tv_cate);
		
		tv_cate.setText(cate + "获取" + number + "拍币");

		tv_name.setText(name);
		tv_title.setText(title);
		tv_desc.setText(desc);
		 findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DevInit.download(AppDetailActivity.this,name,AdType.ADLIST,DlActivity.getDlActivity());
			}
		}); 
	}
}
