package com.dkhs.cqssc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class SelectActivity extends AppCompatActivity {
    ListView lv;
    List<String> list;
    MyAdapter adapter;
    String str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        lv = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        str = getIntent().getStringExtra("tv_year");
        initData();

        adapter = new MyAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("data", list.get(position));
                switch (str) {
                    case "year":
                        setResult(SearchActivity.YEAR, intent);
                        break;
                    case "month":
                        setResult(SearchActivity.MONTH, intent);
                        break;
                    case "day":
                        setResult(SearchActivity.DAY, intent);
                        break;
                    case "qihao":
                        setResult(SearchActivity.QIHAO, intent);
                        break;
                }

                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(4, new Intent());
    }

    private void initData() {
        switch (str) {
            case "year":
                for (int i = 2007; i < 2017; i++) {
                    list.add("" + i);
                }
                break;
            case "day":
                for (int i = 1; i < 32; i++) {
                    if (i < 10) {
                        list.add("0" + i);
                    } else {
                        list.add("" + i);
                    }
                }
                break;
            case "month":
                for (int i = 1; i < 13; i++) {
                    if (i < 10) {
                        list.add("0" + (i));
                    } else {
                        list.add("" + i);
                    }
                }
                break;
            case "qihao":
                for (int i = 1; i < 121; i++) {
                    if (i < 10) {
                        list.add("00" + (i));
                    } else if ((i >= 10) && i < 100) {
                        list.add("0" + i);
                    } else if ((i >= 100) && (i < 121)) {
                        list.add("" + i);
                    }
                }
                break;
        }


    }

    class MyAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(SelectActivity.this).inflate(R.layout.text, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(list.get(position));
            return convertView;
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


    }

}
