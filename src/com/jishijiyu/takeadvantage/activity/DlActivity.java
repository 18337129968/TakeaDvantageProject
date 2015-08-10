package com.jishijiyu.takeadvantage.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dlnetwork.AdType;
import com.dlnetwork.DevInit;
import com.dlnetwork.GetAdListListener;
import com.dlnetwork.OnAddPointsListener;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.ImageCache;

@SuppressLint("NewApi")
public class DlActivity extends Fragment implements OnAddPointsListener{
	private ListView listView = null;
	private final Handler mHandler = new Handler();
	private static boolean clickType = false;
	private String message = "" ;
	private static DlActivity dl;
	ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
	
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
//		listView.setVisibility(View.GONE);
//		DevInit.showOffers(getActivity());
		
		clickType = false;
		showOrUpdateDianjoyOffer();
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				HashMap<String, String> map = (HashMap) arrayList.get(i);
				// 深度任务列表
				if (clickType) {
					if (Integer.valueOf(map.get("task_type")) == 0) {
						DevInit.download(getActivity(),
								(String) map.get("task_id"),
								AdType.ADSIGNTASKLIST,
								DlActivity.this);
					} else if (Integer.valueOf(map.get("task_type")) == 1) {
						DevInit.download(getActivity(),
								(String) map.get("task_id"),
								AdType.ADTIMETASKLIST,
								DlActivity.this);
					} else if (Integer.valueOf(map.get("task_type")) == 2) {
						DevInit.download(getActivity(),
								(String) map.get("task_id"),
								AdType.ADINSTALLTASKLIST,
								DlActivity.this);
					}

				} else {
					// 广告列表
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							AppDetailActivity.class);
					// 广告名字
					intent.putExtra("name", (String) map.get("name"));
					// 广告标语
					intent.putExtra("title", (String) map.get("text"));
					// 广告详情
					intent.putExtra("description",
							(String) map.get("description"));
					// 大小，字符串
					intent.putExtra("size", (String) map.get("size"));
					// 图标url
					intent.putExtra("icon", (String) map.get("icon"));
					// 版本号
					intent.putExtra("ver", (String) map.get("ver"));
					// “试用”，“注册”
					intent.putExtra("cate", (String) map.get("cate"));
					// 分数
					intent.putExtra("number", String.valueOf(map.get("number")));

					intent.putExtra("pack_name",map.get("pack_name"));
					
					intent.putExtra("share_number",map.get("share_number"));
					
					intent.putExtra("money", map.get("money"));
					startActivity(intent);
				}
			}
		});
	}
	
	private void showOrUpdateDianjoyOffer() {
//		progressBar.setVisibility(View.VISIBLE);

		/**
		 * 调用此接口会从后台获取最新的广告列表并刷新.
		 */
		DevInit.getList(getActivity(),1,20,new GetAdListListener() {

			@Override
			public void getAdListSucceeded(List adList) {
				arrayList.clear();
				arrayList.addAll(adList);
				listView.setAdapter(new DataAdapter(getActivity(), arrayList));
				((BaseAdapter) listView.getAdapter())
						.notifyDataSetInvalidated();
//				progressBar.setVisibility(View.GONE);
//				mTextView.setText("加载列表成功!");
			}

			@Override
			public void getAdListFailed(String error) {
				// TODO Auto-generated method stub
				arrayList.clear();
				listView.setAdapter(new DataAdapter(getActivity(), arrayList));
				((BaseAdapter) listView.getAdapter())
						.notifyDataSetInvalidated();
				message = error;
//				mTextView.setText(message);
//				progressBar.setVisibility(View.GONE);
			}
		});
	}
	
	
	@Override
	public void addPointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPointsSucceeded(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public static DlActivity getDlActivity(){
		return dl;
	}
	/**
	 * listview每一行的布局
	 * */
	public class DataAdapter extends BaseAdapter {
		public ArrayList<HashMap<String, Object>> itemList = null;
		public Context context;
		private ViewHolder holder;

		public DataAdapter(Context ctex,
				ArrayList<HashMap<String, Object>> itemList) {
			this.context = ctex;
			this.itemList = itemList;
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.app_list_item, null);

				holder.textView_appname = (TextView) convertView
						.findViewById(R.id.textView_appname);
				holder.textView_adtext = (TextView) convertView
						.findViewById(R.id.textView_adtext);
				holder.textView_beanNum = (TextView) convertView
						.findViewById(R.id.textView_beanNum);
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.imageView_icon);
				holder.textView_adTaskText = (TextView) convertView
						.findViewById(R.id.textView_adTaskText);
				holder.textView_adPoints = (TextView) convertView
						.findViewById(R.id.textView_adPoints);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (String.valueOf(itemList.get(position).get("task_count"))
					.equals("-1")) {
				holder.textView_appname.setText(itemList.get(position)
						.get("name").toString());
				holder.textView_adTaskText.setText("");
				// holder.textView_adPoints.setText("奖励明细:"+"激活可得"+itemList.get(position)
				// .get("number").toString()+"拍币");
			} else {
				if (clickType) {
					holder.textView_appname.setText(itemList.get(position)
							.get("name").toString());
				} else {
					holder.textView_appname.setText(itemList.get(position).get(
							"name")
							+ "(有深度任务)");
					JSONArray taskDesc = null;
					try {
						taskDesc = new JSONArray(itemList.get(position)
								.get("tasks").toString());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String taskList = null;
					for (int i = 0; i < taskDesc.length(); i++) {
						JSONObject taskOffer;
						try {

							taskOffer = (JSONObject) taskDesc.get(i);
							if (taskList != null) {
								taskList = taskList + taskOffer.get("name")
										+ taskOffer.get("step_rmb")
										+ "拍币".toString();
							} else {
								taskList = taskOffer.get("name").toString()
										+ taskOffer.get("step_rmb").toString()
										+ "拍币" + "\n";
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					holder.textView_adTaskText.setText(taskList);
				}
			}

			holder.textView_adtext.setText(itemList.get(position).get("text")
					.toString());
			holder.textView_beanNum.setText(String.valueOf(itemList.get(
					position).get("number")));

			Bitmap bm = ImageCache.getTrueBitmap(getActivity(), itemList.get(position)
					.get("icon").toString());
			if (bm != null) {
				holder.appIcon.setImageBitmap(bm);
			}
			// convertView.setBackgroundResource(R.drawable.cell);
			return convertView;
		}

		class ViewHolder {
			TextView textView_appname;
			TextView textView_adtext;
			TextView textView_beanNum;
			TextView textView_adTaskText;
			TextView textView_adPoints;
			ImageView appIcon;
		}
	}
}
