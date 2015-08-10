package com.jishijiyu.takeadvantage.view;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.DensityUtils;
import com.jishijiyu.takeadvantage.utils.LogUtil;

public class DropDownListBox extends View {
	private Context mContext;
	private TextView textView;
	private List<String> mList;
	private PopupWindow popupWindow;

	public DropDownListBox(Context context) {
		super(context);
		mContext = context;
	}

	public DropDownListBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public View initView(List list) {
		mList = list;
		View view = View.inflate(mContext, R.layout.drop_down_list_box, null);
		textView = (TextView) view.findViewById(R.id.tv_select_des);
		ImageButton imageButton = (ImageButton) view
				.findViewById(R.id.iv_select_icon);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPop();
			}
		});
		return view;

	}

	protected void showPop() {
		View view = View.inflate(mContext, R.layout.drop_down_list_pop, null);
		ListView lv_drop_down_list = (ListView) view
				.findViewById(R.id.lv_drop_down_list);
		lv_drop_down_list.setAdapter(new MyAdapter());
		popupWindow = new PopupWindow(view,
				(int) (textView.getWidth() - DensityUtils.dp2px(mContext, 8)),
				DensityUtils.dp2px(mContext, 300));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(textView, 4, 0);
		lv_drop_down_list.setOnItemClickListener(new MyOnItemClickListener());
	}

	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			textView.setText(mList.get(position));
			popupWindow.dismiss();
		}

	}

	class MyAdapter extends BaseAdapter {

		private TextView popListText;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (view == null) {
				view = View.inflate(mContext, R.layout.drop_down_list_pop_item, null);
				popListText = (TextView) view.findViewById(R.id.tv_drop_town_item_des);
			}else {
				view=convertView;
			}
			popListText.setText(mList.get(position));
			return view;
		}

	}
}
