package com.jishijiyu.takeadvantage.view;
import com.jishijiyu.*;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
/**
 * 显示大图窗口
 * @author shifeiyu
 *
 */
public class BigImagePopupWindow extends PopupWindow{
	View mPopview;
	ImageView big_imageview;
	public BigImagePopupWindow(Activity context,String imageUrl,Bitmap bitmap){
		super(context);
		LayoutInflater layoutinflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopview = layoutinflate.inflate(R.layout.layout_bigimage_popwindow,null);
		big_imageview = (ImageView) mPopview.findViewById(R.id.big_imageview);
		if(bitmap!=null){
			big_imageview.setImageBitmap(bitmap);
		}
		if(bitmap==null){
			ImageLoaderUtil.loadImage(imageUrl, big_imageview);
		}
		this.setContentView(mPopview);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.pop_menu_animation);
		mPopview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP){
					dismiss();
				}
				return true;
			}
		});
	}
}
