package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter extends BaseAdapter{

  	protected Context context;
    protected LayoutInflater inflate;
    protected List<?> list;
    public MyBaseAdapter(Context context, List<?> list){
        this.list = list;
        this.context = context;
        inflate = LayoutInflater.from(context);
}

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
        // 重写view方法
        return initView(position, convertView);

}

/**
 * getView方法�?���?
 * 
* @param position
 * @param convertview
 * @return
 */
public abstract View initView(int position, View convertView);

}

