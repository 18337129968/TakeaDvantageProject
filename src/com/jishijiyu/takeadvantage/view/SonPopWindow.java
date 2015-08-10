package com.jishijiyu.takeadvantage.view;

import com.jishijiyu.takeadvantage.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ListView;
import android.widget.PopupWindow;
/**
 * 选择职业子界面
 * @author shifeiyu
 * @version 2015年6月4日15:09:04
 *
 */
public class SonPopWindow extends PopupWindow{
	View mMenuPop;
	View vv;
	int w1;
	public ListView trade_list;
	public SonPopWindow(Activity context){
		super(context);
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuPop = inflater.inflate(R.layout.son_menu_pop_list, null);
		trade_list = (ListView) mMenuPop.findViewById(R.id.trade_list);
		this.setContentView(mMenuPop);
		
		 DisplayMetrics metric = new DisplayMetrics();
	     context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	     int screenheight = metric.heightPixels;
	     int screenwidth = metric.widthPixels;
	     
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		
		LayoutInflater inflater1 =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vv = inflater1.inflate(R.layout.layout_select_job, null);
		ViewTreeObserver vto = vv.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {    
				
				return false;
			}
		});
		if(vto.isAlive()){
			w1 = vv.findViewById(R.id.select_trade_text).getWidth();
			System.out.println(w1);
		}
//		System.out.println(w1);
//		vv.findViewById(R.id.select_trade_text).measure(w, h); 
//		vv.findViewById(R.id.pop_layout_job).measure(w, h);
//		int height = vv.findViewById(R.id.pop_layout_job).getMeasuredHeight();
//		int w1 = vv.findViewById(R.id.select_trade_text).getMeasuredWidth();
//		Log.e("*******height",""+w1);
		
		this.setWidth(screenwidth/2-30);
		this.setHeight(screenheight*2/3-150);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
//		this.setAnimationStyle(R.style.PopupAnimation);
		mMenuPop.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int left = mMenuPop.findViewById(R.id.trade_list).getLeft();
				int right = mMenuPop.findViewById(R.id.trade_list).getRight();
				int x=(int) event.getX();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(x<left||x>right){
						dismiss();
					}
				}				
				return true;
			}
		});
	}
	
}
