package com.jishijiyu.takeadvantage.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral.ScoreStatisticsRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人账户适配器
 * 
 * @author baohan
 * 
 */
public class MyAccountAdapter extends BaseAdapter {
	private String jifen;
	private Context context;
	private List<ScoreStatisticsRecord> list;

	private LayoutInflater mInflater;
	RelativeLayout rl;

	public MyAccountAdapter(Context context, List<ScoreStatisticsRecord> list) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		return 0;

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	/**
	 * 加载数据
	 * 
	 * @param mData
	 */
	public void addAll(List<ScoreStatisticsRecord> mData) {
		this.list.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * 清除数据
	 */
	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.activity_account_item,
					null);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		float d2 = Float.parseFloat(list.get(position).createTime + "");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String data = dateFormat.format(d2);
		holder.tv_time.setText(data);
		if (list.get(position).num != null) {
			jifen = list.get(position).num;
		} else {
			jifen = "0";
		}
		if (list.get(position).type.equals("1")) {
			holder.tv_type.setText("看广告获得" + jifen + "拍币");
		} else if (list.get(position).type.equals("2")) {
			holder.tv_type.setText("任务获得" + jifen + "拍币");
		} else if (list.get(position).type.equals("3")) {
			holder.tv_type.setText("邀请获得" + jifen + "拍币");
		} else if (list.get(position).type.equals("4")) {
			holder.tv_type.setText("签到获得" + jifen + "拍币");
		} else if (list.get(position).type.equals("5")) {
			holder.tv_type.setText("系统赠送" + jifen + "拍币");
		} else if (list.get(position).type.equals("6")) {
			holder.tv_type.setText("金币兑换拍币获得" + jifen + "拍币");
		} else if (list.get(position).type.equals("7")) {
			holder.tv_type.setText("兑换扣除" + jifen + "拍币");
		} else if (list.get(position).type.equals("8")) {
			holder.tv_type.setText("摇奖扣除" + jifen + "拍币");
		} else if (list.get(position).type.equals("9")) {
			holder.tv_type.setText("一元拍摇奖扣除" + jifen + "拍币");
		}

		return convertView;
	}

	public class ViewHolder {
		public TextView tv_type, tv_time;
	}

}
