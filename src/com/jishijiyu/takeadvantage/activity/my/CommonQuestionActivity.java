package com.jishijiyu.takeadvantage.activity.my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.AboutPaiDeLi;
import com.jishijiyu.takeadvantage.entity.result.Question;
import com.jishijiyu.takeadvantage.utils.LogUtil;

/**
 * 常见问题
 * 
 * @author tml
 * 
 */
public class CommonQuestionActivity extends Activity {
	private Context mContext;
	private AboutPaiDeLi paiDeLi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_question);
		mContext=CommonQuestionActivity.this;
		initHead();
		initData();
	}
	
	private void initHead() {
		TextView top_text=(TextView) findViewById(R.id.top_text);
		top_text.setText("常见问题");
		Button img_btn_top_left=(Button) findViewById(R.id.img_btn_top_left);
		img_btn_top_left.setVisibility(View.VISIBLE);
		img_btn_top_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		InputStream file=getResources().openRawResource(R.raw.about_paideli);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(file));
		StringBuilder builder=new StringBuilder();
		int len=0;
		char[] chars=new char[1024];
		try {
			while ((len=bufferedReader.read(chars))!=-1) {
				builder.append(chars, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		LogUtil.i("builder:"+builder.toString());
		
		Gson gson=new Gson();
		paiDeLi = gson.fromJson(builder.toString(), AboutPaiDeLi.class);
		ListView lv_headline=(ListView) findViewById(R.id.lv_headline);
		MyAdapter myAdapter=new MyAdapter();
		lv_headline.setAdapter(myAdapter);
		lv_headline.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(mContext, CommonQuestionDesActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("HeadLine", paiDeLi.list.get(position).headline);
				bundle.putParcelableArrayList("QuestionDes", (ArrayList<Question>) paiDeLi.list.get(position).questList);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	class MyAdapter extends BaseAdapter{

		private TextView exchange_products;
		private ImageView exchange_products_arrow;

		@Override
		public int getCount() {
			return paiDeLi.list.size();
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
				view=View.inflate(mContext, R.layout.common_headline_item, null);
				exchange_products = (TextView) view.findViewById(R.id.exchange_products);
				exchange_products_arrow = (ImageView)view.findViewById(R.id.exchange_products_arrow);
			}else {
				view=convertView;
			}
			exchange_products.setText(paiDeLi.list.get(position).headline);
			return view;
		}
		
	}
	

}
