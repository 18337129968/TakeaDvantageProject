package com.jishijiyu.takeadvantage.activity;

import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.xkeuops.diy.AppSummaryDataInterface;
import com.android.xkeuops.diy.AppSummaryObjectList;
import com.android.xkeuops.diy.DiyOfferWallManager;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.adapter.ListViewAdapter;
import com.jishijiyu.takeadvantage.entity.CustomObject;

@SuppressLint("NewApi")
public class YMActivity extends Fragment implements OnItemClickListener, BitmapDownloadListener,AppSummaryDataInterface{
	private ListView listView = null;
	private final Handler mHandler = new Handler();
	private ArrayList<CustomObject> mListData; // 广告列表数据

	private ListViewAdapter mLvAdapter; // Adapter
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
		
		mLvAdapter = new ListViewAdapter(getActivity(), null); // 这里先让列表为空，待加载到数据再显示出来
		listView.setAdapter(mLvAdapter);
		listView.setOnItemClickListener(this);
		// 调用方式一：直接打开全屏拍币墙
		// OffersManager.getInstance(this).showOffersWall();

		// 调用方式二：直接打开全屏拍币墙，并且监听拍币墙退出的事件onDestory
//		OffersManager.getInstance(getActivity()).showOffersWall(
//				new Interface_ActivityListener() {
//					/**
//					 * 但拍币墙销毁的时候，即拍币墙的Activity调用了onDestory的时候回调
//					 */
//					@Override
//					public void onActivityDestroy(Context context) {
//						Toast.makeText(context, "全屏拍币墙退出了", Toast.LENGTH_SHORT)
//								.show();
//					}
//				});
		loadDataList(DiyOfferWallManager.REQUEST_SPECIAL_SORT);
	}
	/**
	 * 异步获取广告列表，并更新视图
	 * 
	 * @param requestType
	 *            请求数量
	 */
	private void loadDataList(final int requestType) {

		// （重要）如果使用SDK提供的异步方法请求列表页的数据，需要在请求之前先注册回调接口，并且记得在退出的时候（如activity的onDestory方法中）注销接口
		DiyOfferWallManager.getInstance(getActivity()).registerAppSummaryDataInterface(this);

		/**
		 * 异步加载拍币墙数据列表，有结果之后会回调上述注册的接口
		 * 
		 * @param requestType
		 *            请求类型 <br>
		 *            DiyOfferWallManager.REQUEST_ALL:请求所有广告，游戏先于应用展示
		 *            DiyOfferWallManager.REQUEST_GAME:只请求游戏广告
		 *            DiyOfferWallManager.REQUEST_APP:只请求应用广告
		 *            DiyOfferWallManager.REQUEST_SPECIAL_SORT:请求所有广告，应用先于游戏展示
		 *            DiyOfferWallManager.REQUEST_EXTRA_TASK:请求追加任务列表
		 *            DiyOfferWallManager.REQUEST_SIGN_IN:请求签到任务列表
		 * @param withAdDownloadUrl
		 *            广告是否携带url下载地址（主要用于实现广告列表页实现下载功能）可以不传入这个值 fasle:不携带（默认值） true:携带
		 */
		DiyOfferWallManager.getInstance(getActivity()).loadOfferWallAdList(requestType, true);
	}
	@Override
	public void onLoadAppSumDataFailed() {
		// TODO Auto-generated method stub
//		Toast.makeText(getActivity(), "请求失败, 请检查网络", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoadAppSumDataFailedWithErrorCode(int arg0) {
		// TODO Auto-generated method stub
//		Toast.makeText(getActivity(), String.format("请求错误，错误代码 ： %d， 请联系客服"), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoadAppSumDataSuccess(Context arg0, AppSummaryObjectList adList) {
		// TODO Auto-generated method stub
		if (adList != null && adList.size() > 0) {

			mListData = new ArrayList<CustomObject>();
			for (int k = 0; k < adList.size(); ++k) {

				// 如果请求的是追加任务的列表，demo将会把所有的追加任务独立为一个item项，因此需要把同一个appSummaryObject多次加入到列表中
//				if (requestType == DiyOfferWallManager.REQUEST_EXTRA_TASK) {
//					// 下面是判断是否追加任务，如果是的话就会在写入一次列表
//					AppSummaryObject appSummaryObject = adList.get(k);
//					AppExtraTaskObjectList extraTaskObjectList = appSummaryObject.getExtraTaskList();
//					for (int j = 0; j < extraTaskObjectList.size(); ++j) {
//						AppExtraTaskObject extraTaskObject = extraTaskObjectList.get(j);
//						if (extraTaskObject.getStatus() == AdExtraTaskStatus.NOT_START
//								|| extraTaskObject.getStatus() == AdExtraTaskStatus.IN_PROGRESS) {
//							CustomObject customObject = new CustomObject();
//							customObject.setAppSummaryObject(adList.get(k));
//							customObject.setAppicon(null);
//							customObject.setShowMultSameAd(true);
//							customObject.setShowExtraTaskIndex(j);
//							mListData.add(customObject);
//						}
//					}
//				} else {
					CustomObject customObject = new CustomObject();
					customObject.setAppSummaryObject(adList.get(k));
					customObject.setAppicon(null);
					mListData.add(customObject);
//				}
			}

//			Toast.makeText(getActivity(), String.format("请求成功，返回了 %d 个广告", adList.size()),
//					Toast.LENGTH_LONG).show();

			mLvAdapter.setData(mListData);
			mLvAdapter.notifyDataSetChanged();

			// 获取需要加载的图片url地址，然后加载
			String[] iconUrlArray = new String[mListData.size()];
			for (int i = 0; i < mListData.size(); i++) {
				iconUrlArray[i] = mListData.get(i).getAppSummaryObject().getIconUrl();
			}
			// 线程池异步加载图片
//			BitmapLoaderManager.loadBitmap(this, this, iconUrlArray);
		} else {
			Toast.makeText(getActivity(), "当前没有任务哦，晚点再在来吧~", Toast.LENGTH_LONG).show();

			mLvAdapter.setData(null);
			mLvAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoadBitmap(String url, Bitmap bm) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < mListData.size(); i++) { // 显示app_icon
				if (url == mLvAdapter.getItem(i).getAppSummaryObject().getIconUrl()) {
					mLvAdapter.getItem(i).setAppicon(bm);
					mLvAdapter.notifyDataSetChanged();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), DiyOfferAdDetailActivity.class);
		intent.putExtra("ad", mLvAdapter.getItem(position).getAppSummaryObject());
		startActivity(intent);
	}

}
