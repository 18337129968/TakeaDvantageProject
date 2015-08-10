package com.jishijiyu.takeadvantage.activity;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.exchangemall.MoreAddressActivity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
/**
 * 
 * 测试界面
 * @author zhaobin
 */
public class MainActivity extends HeadBaseActivity {
    public TextView textView=null;
	@Override
	public void appHead(View view) {
		btn_right.setText("确定");
		top_text.setText("");
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MainActivity.this, R.layout.main, null);
		base_centent.addView(view);
		textView=(TextView) view.findViewById(R.id.text);
		textView.setText("测试界面");
		startForActivity(this, MoreAddressActivity.class,null );
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
