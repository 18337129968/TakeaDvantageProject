package com.jishijiyu.takeadvantage.activity.news;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.request.RequestNewsDelete;
import com.jishijiyu.takeadvantage.entity.request.RequestNewsDes;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.ResultNews;
import com.jishijiyu.takeadvantage.entity.result.ResultNewsDelete;
import com.jishijiyu.takeadvantage.entity.result.ResultNewsDes;
import com.jishijiyu.takeadvantage.entity.result.UserNotice;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.FileUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 我的消息
 */
public class MyMessageActivity extends Activity {
	private String saveName = "NewsLocalList";
	private Context mContext;
	private ArrayList<UserNotice> list;
	private ArrayList<UserNotice> saveList;
	private TextView top_text;
	private PopupWindow popupWindow;
	private Gson gson;
	private MyAdapter myAdapter;
	private ResultNews resultNews;
	private ListView news_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		mContext = MyMessageActivity.this;
		saveName = "NewsLocalList" + GetUserIdUtil.getUserId(mContext);
		gson = new Gson();
		initData();
	}

	private void initData() {
		list = new ArrayList<UserNotice>();
		ArrayList<UserNotice> notices = GetUserIdUtil.getList(mContext);
		for (int i = 0; i < notices.size(); i++) {
			if (notices.get(i).state == 1) {
				list.add(notices.get(i));
			}
		}
		String str = FileUtil.readFile(mContext, saveName);
		if (!TextUtils.isEmpty(str)) {
			resultNews = gson.fromJson(str, ResultNews.class);
			if (resultNews.p.userNoticeList.size() > 0) {
				saveList = resultNews.p.userNoticeList;
				for (int i = 0; i < saveList.size(); i++) {
					list.add(saveList.get(i));

				}
			}
		}
		if (list.size() > 0) {
			initView();
		} else {
			Intent intent = new Intent(mContext, MyMessageDesActivity.class);
			startActivity(intent);
			
			//finish();
		}

	}

	private void exchangeList(ArrayList<UserNotice> notice) {
		String str = FileUtil.readFile(mContext, saveName);
		resultNews = gson.fromJson(str, ResultNews.class);
		resultNews.p.userNoticeList = notice;
		String saveStr = gson.toJson(resultNews);
		LogUtil.i("saveStr:" + saveStr);
		FileUtil.writeFile(mContext, saveName, saveStr);
	}

	private void saveList(UserNotice notice) {
		if (saveList == null) {
			saveList = new ArrayList<UserNotice>();
		}
		if (saveList.size() > 0) {
			for (int i = 0; i < saveList.size(); i++) {
				if (saveList.get(i).contentId != notice.contentId
						&& i == saveList.size() - 1) {
					saveList.add(notice);
				}
			}
		} else {
			saveList.add(notice);
		}
		if (resultNews == null) {
			resultNews = new ResultNews();
		}
		resultNews.c = Constant.REQUEST_NEWS_CODE;
		resultNews.p.isTrue = true;
		resultNews.p.userNoticeList = saveList;
		String saveStr = gson.toJson(resultNews);
		FileUtil.writeFile(mContext, saveName, saveStr);
	}

	private void initView() {
		Button back = (Button) findViewById(R.id.img_btn_top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("我的消息");
		news_list = (ListView) findViewById(R.id.my_news_listview);
		myAdapter = new MyAdapter();
		news_list.setAdapter(myAdapter);
		news_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				moveToDes(position);
			}

		});
		news_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showPop(position);
				return true;
			}

		});
	}

	private void moveToDes(final int position) {
		if (list.get(position).state == 1) {
			RequestNewsDes requestNewsDes = new RequestNewsDes();
			requestNewsDes.p.contentId = "" + list.get(position).contentId;
			requestNewsDes.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			requestNewsDes.p.userId = GetUserIdUtil.getUserId(mContext);
			String requestJson = gson.toJson(requestNewsDes);
			HttpConnectTool.update(requestJson, mContext,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							ResultNewsDes des = gson.fromJson(result,
									ResultNewsDes.class);
							if (des.p.isTrue) {
								list.get(position).state = 2;
								myAdapter.notifyDataSetChanged();
								saveList(list.get(position));
								GetUserIdUtil.saveList(mContext, list);
								Intent intent = new Intent(mContext,
										MyMessageDesActivity.class);
								intent.putExtra("title",
										list.get(position).title);
								intent.putExtra("des",
										list.get(position).content);
								intent.putExtra("time",
										list.get(position).sendTime);
								startActivity(intent);
							} else {
								IntentActivity.mIntent(mContext);
							}
						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							Toast.makeText(mContext, "查询消息详情失败", 0).show();
						}
					});
		} else {
			Intent intent = new Intent(mContext, MyMessageDesActivity.class);
			intent.putExtra("title", list.get(position).title);
			intent.putExtra("des", list.get(position).content);
			intent.putExtra("time", list.get(position).sendTime);
			startActivity(intent);
		}
	}

	private void showPop(final int position) {
		View popView = View.inflate(mContext, R.layout.pop_news_delete, null);
		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(top_text);
		Button delete = (Button) popView.findViewById(R.id.btn_delete_news);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).state == 1) {
					RequestNewsDelete newsDelete = new RequestNewsDelete();
					newsDelete.p.contentId = "" + list.get(position).contentId;
					newsDelete.p.userId = GetUserIdUtil.getUserId(mContext);
					newsDelete.p.tokenId = GetUserIdUtil.getTokenId(mContext);
					String requestJson = gson.toJson(newsDelete);
					HttpConnectTool.update(requestJson, mContext,
							new ConnectListener() {

								@Override
								public void contectSuccess(String result) {
									ResultNewsDelete resultNewsDelete = gson
											.fromJson(result,
													ResultNewsDelete.class);
									if (resultNewsDelete.p.isTrue) {
										if (resultNewsDelete.p.isSucce) {
											list.remove(position);
											GetUserIdUtil.saveList(mContext,
													list);
											myAdapter.notifyDataSetChanged();
											if (list.size() <= 0) {
												finish();
											}
										}
									} else {

										IntentActivity.mIntent(mContext);
									}
								}

								@Override
								public void contectStarted() {
									// TODO Auto-generated method stub

								}

								@Override
								public void contectFailed(String path,
										String request) {
									// TODO Auto-generated method stub

								}
							});
				} else {
					if (saveList.size() > 0) {
						for (int i = 0; i < saveList.size(); i++) {
							if (list.get(position).contentId == saveList.get(i).contentId) {
								saveList.remove(i);
							}
						}
					}
					list.remove(position);
					exchangeList(saveList);
					GetUserIdUtil.saveList(mContext, list);
					myAdapter.notifyDataSetChanged();
				}
				popupWindow.dismiss();
				if (list.size() <= 0) {
					finish();
				}
			}
		});
		RelativeLayout rl_pop_news = (RelativeLayout) popView
				.findViewById(R.id.rl_pop_news);
		rl_pop_news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			ViewHolder holder = null;
			if (holder == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.activity_news_list_item, null);
				holder.img = (ImageView) convertView
						.findViewById(R.id.iv_news_item_new);
				holder.date = (TextView) convertView
						.findViewById(R.id.tv_news_item_date);
				holder.des = (TextView) convertView
						.findViewById(R.id.tv_news_item_des);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_news_item_title);
				holder.more = (TextView) convertView
						.findViewById(R.id.tv_news_item_more);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			if (list.get(position).state == 1) {
				holder.img.setVisibility(View.VISIBLE);
			} else if (list.get(position).state == 2) {
				holder.img.setVisibility(View.GONE);
			}
			holder.title.setText(list.get(position).title);
			holder.des.setText(list.get(position).content);
			Date date = new Date(list.get(position).sendTime);
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			holder.date.setText(sdr.format(date));
			return convertView;
		}

	}

	class ViewHolder {
		public ImageView img;
		public TextView date;
		public TextView title;
		public TextView des;
		public TextView more;
	}
}
