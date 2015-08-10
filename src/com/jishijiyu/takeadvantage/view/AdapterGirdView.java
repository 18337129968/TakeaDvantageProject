package com.jishijiyu.takeadvantage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class AdapterGirdView extends GridView {

	public AdapterGirdView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AdapterGirdView(Context context) {
		super(context);
	}

	public AdapterGirdView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
