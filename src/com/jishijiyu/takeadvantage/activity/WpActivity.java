package com.jishijiyu.takeadvantage.activity;

import java.util.List;

import com.jishijiyu.takeadvantage.R;

import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WpActivity extends Fragment {
	private ListView listView = null;
	private final Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wp, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	private void init() {
		listView = (ListView) getActivity().findViewById(R.id.listview);
		// 异步加载自定义广告数据
		new GetDiyAdTask(getActivity(), listView).execute();
	}

	private class MyAdapter extends BaseAdapter {
		Context context;
		List<AdInfo> list;
		public MyAdapter(Context context, List<AdInfo> list) {
			this.context = context;
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final AdInfo adInfo = list.get(position);

			View adatperView = null;

			try {
				adatperView = AppItemView.getInstance().getAdapterView(context,
						adInfo, 0, 0);
				convertView = adatperView;
				convertView.setTag(adatperView);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return adatperView;
		}
	}

	private class GetDiyAdTask extends AsyncTask<Void, Void, Boolean> {

		Context context;
		ListView listView;
		List<AdInfo> list;
		GetDiyAdTask(Context context, ListView listView) {
			this.context = context;
			this.listView = listView;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				int i = 0;
				while (true) {
					if (i > 10) {
						i = 0;
						break;
					}
					if (!new SDKUtils(context).isConnect()) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, "数据获取失败,请检查网络重新加载",
										Toast.LENGTH_LONG).show();
								((Activity) context).finish();
							}
						});
						break;
					}
					list = AppConnect.getInstance(context).getAdInfoList();
					if (list != null && !list.isEmpty()) {

						mHandler.post(new Runnable() {

							@Override
							public void run() {
								listView.setAdapter(new MyAdapter(context, list));
							}
						});
						break;
					}
					i++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
