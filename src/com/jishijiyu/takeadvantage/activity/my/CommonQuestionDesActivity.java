package com.jishijiyu.takeadvantage.activity.my;

import java.util.ArrayList;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.Question;
import com.umeng.socialize.controller.impl.InitializeController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 问题详情
 * @author temulu
 */
public class CommonQuestionDesActivity extends Activity { 
	private ArrayList<Question> arrayList;
	private String headLine;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_question_des);
		mContext=CommonQuestionDesActivity.this;
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		headLine = bundle.getString("HeadLine");
		arrayList = bundle.getParcelableArrayList("QuestionDes");
		initHead();
		initData();
	}

	private void initHead() {
		TextView top_text=(TextView) findViewById(R.id.top_text);
		top_text.setText(headLine);
		Button back=(Button) findViewById(R.id.img_btn_top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		ListView listView=(ListView) findViewById(R.id.lv_problem);
		MyAdapter adapter=new MyAdapter();
		listView.setAdapter(adapter);
	}
	class MyAdapter extends BaseAdapter{

		private TextView from_which_exchange_goods;
		private TextView any_specified_manufacturer;

		@Override
		public int getCount() {
			return arrayList.size();
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
			View view=null;
			if (view==null) {
				view=View.inflate(mContext, R.layout.common_question_des_item, null);
				from_which_exchange_goods = (TextView) view.findViewById(R.id.from_which_exchange_goods);
				any_specified_manufacturer = (TextView) view.findViewById(R.id.any_specified_manufacturer);
			}else {
				view=convertView;
			}
			from_which_exchange_goods.setText(arrayList.get(position).title);
			any_specified_manufacturer.setText(arrayList.get(position).des);
			return view;
		}
		
	}
}
