package com.jishijiyu.takeadvantage.view;

import com.jishijiyu.takeadvantage.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 选择图片界面 witch 1 选择头像 witch 2 选择广告图片
 * 
 * @author shifeiyu
 * 
 */
public class SelectHeadPicPopupWindow extends PopupWindow {

	public TextView from_pic_btn, from_camera_btn, cancle_btn;
	private View mMenuView;
	public TextView preview_btn, advert_from_pic_btn, advert_from_camera_btn,
			advert_delete_image, advert_cancle_btn2;
	public LinearLayout pop_layout1;

	public SelectHeadPicPopupWindow(Activity context,
			OnClickListener itemsOnClick, int witch) {
		super(context);
		/**
		 * 选择头像
		 */
		if (witch == 1) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(
					R.layout.layout_select_headpic_popwindow, null);
			from_pic_btn = (TextView) mMenuView.findViewById(R.id.from_pic_btn);
			from_camera_btn = (TextView) mMenuView
					.findViewById(R.id.from_camera_btn);
			cancle_btn = (TextView) mMenuView.findViewById(R.id.cancle_btn2);
			cancle_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			from_pic_btn.setOnClickListener(itemsOnClick);
			from_camera_btn.setOnClickListener(itemsOnClick);
		}
		/**
		 * 选择广告图片
		 */
		if (witch == 2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.layout_select_advert_image,
					null);
			preview_btn = (TextView) mMenuView.findViewById(R.id.preview_btn);
			advert_from_pic_btn = (TextView) mMenuView
					.findViewById(R.id.advert_from_pic_btn);
			advert_from_camera_btn = (TextView) mMenuView
					.findViewById(R.id.advert_from_camera_btn);
			advert_delete_image = (TextView) mMenuView
					.findViewById(R.id.advert_delete_image);
			advert_cancle_btn2 = (TextView) mMenuView
					.findViewById(R.id.advert_cancle_btn2);
			pop_layout1 = (LinearLayout) mMenuView
					.findViewById(R.id.pop_layout1);
			advert_cancle_btn2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			preview_btn.setOnClickListener(itemsOnClick);
			advert_from_pic_btn.setOnClickListener(itemsOnClick);
			advert_from_camera_btn.setOnClickListener(itemsOnClick);
			advert_delete_image.setOnClickListener(itemsOnClick);
		}

		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout1).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
