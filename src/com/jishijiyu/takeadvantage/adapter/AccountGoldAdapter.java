package com.jishijiyu.takeadvantage.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.adapter.MyAccountAdapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountGold.GoldStatisticsRecordList;
import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral.ScoreStatisticsRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountGoldAdapter extends BaseAdapter {
	private Context context;
	private List<GoldStatisticsRecordList> list;
	public String gold_num;

	private LayoutInflater mInflater;
	RelativeLayout rl;

	public AccountGoldAdapter(Context context,
			List<GoldStatisticsRecordList> list) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public final class ViewHolder {
		public TextView tv_type, tv_time;
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
		float d2 = Float.parseFloat(list.get(position).createTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String data = dateFormat.format(d2);

		holder.tv_time.setText(data);
		if (list.get(position).num != null) {
			gold_num = list.get(position).num;
		} else {
			gold_num = "0";
		}

		if (list.get(position).type.equals("1")) {
			holder.tv_type.setText("广告任务获得" + gold_num + "拍币");
		} else if (list.get(position).type.equals("2")) {
			holder.tv_type.setText("应用任务获得" + gold_num + "金币");
		} else if (list.get(position).type.equals("3")) {
			holder.tv_type.setText("兑换获得" + gold_num + "金币");
		} else if (list.get(position).type.equals("4")) {
			holder.tv_type.setText("购买锁消耗" + gold_num + "金币");
		} else if (list.get(position).type.equals("5")) {
			holder.tv_type.setText("提现消耗" + gold_num + "金币");
		} else if (list.get(position).type.equals("6")) {
			holder.tv_type.setText("兑换拍币消耗" + gold_num + "金币");
		} else if (list.get(position).type.equals("7")) {
			holder.tv_type.setText("一元拍消耗" + gold_num + "金币");
		}

		return convertView;
	}

}
