package com.jishijiyu.takeadvantage.activity.news;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.ResultNewsDes;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

public class MyMessageDesActivity extends Activity {
	private Context mContext;
	private String title;
	private String des;
	private long time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_des);
		mContext=MyMessageDesActivity.this;
		InitData();
		Button back=(Button) findViewById(R.id.img_btn_top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("详情");
	}

	private void InitData() {
		Intent intent=getIntent();
		if(intent==null){
			ToastUtils.makeText(mContext, "当前无消息", Toast.LENGTH_SHORT).show();
		}
		title = intent.getStringExtra("title");
		des = intent.getStringExtra("des");
		time = intent.getLongExtra("time", 0);
		initView();
	}
	private void initView() {
		TextView news_des_title=(TextView) findViewById(R.id.news_des_title);
		TextView news_des_des=(TextView) findViewById(R.id.news_des_des);
		TextView news_des_date=(TextView) findViewById(R.id.news_des_date);
		news_des_title.setText(title);
		news_des_des.setText(des);
		Date date=new Date(time);
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		news_des_date.setText(sdr.format(date));
	}
}
