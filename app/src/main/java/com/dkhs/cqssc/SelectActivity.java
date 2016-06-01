package com.dkhs.cqssc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class SelectActivity extends Activity implements AdapterView.OnItemClickListener {
    GridView lv;
    List<String> list;
    MyAdapter adapter;
    String str;
    TextView title;
    StringBuffer sb = new StringBuffer();
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        title = (TextView) findViewById(R.id.title);
        lv = (GridView) findViewById(R.id.list);
        list = new ArrayList<>();
        name = getIntent().getStringExtra("name");
        str = "year";
        title.setText("请选择年份");
        initData();

        adapter = new MyAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //setResult(4, new Intent());
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        //intent.putExtra("data", list.get(position));
        switch (str) {
            case "year":
                sb.append(list.get(position));
                list.clear();
                str = "month";
                initData();
                adapter.bindData(list);
                title.setText("请选择月份");
                //setResult(SearchActivity.YEAR, intent);
                break;
            case "month":
                sb.append(list.get(position));
                list.clear();
                str = "day";
                initData();
                adapter.bindData(list);
                title.setText("请选择日期");
                //setResult(SearchActivity.MONTH, intent);
                break;
            case "day":
                sb.append(list.get(position));
                list.clear();
                str = "qihao";
                initData();
                adapter.bindData(list);
                title.setText("请选择期号");
                //setResult(SearchActivity.DAY, intent);
                break;
            case "qihao":
                sb.append(list.get(position));
                intent.putExtra("data", sb.toString());
                if("year".equals(name)){
                    setResult(SearchActivity.BEGIN, intent);
                }else if("end".equals(name)){
                    setResult(SearchActivity.END, intent);
                }

                finish();
                break;
        }


    }
}
