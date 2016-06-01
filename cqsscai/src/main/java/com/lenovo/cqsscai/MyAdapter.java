package com.lenovo.cqsscai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xuetong1 on 2016/6/1.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }
    public void bindData(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.text, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(list.get(position));
        return convertView;
    }
}
