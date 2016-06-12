package com.lenovo.cqsscai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.cqsscai.dao.AssetsDatabaseManager;
import com.lenovo.cqsscai.dao.ResultsDao;


/**
 * 查询统计数据
 * Created by Administrator on 2016/1/17.
 */
public class SearchActivity extends Activity {


    public void submit(View v) {
        String begin = tv_begin.getText().toString().trim();
        String end = tv_end.getText().toString().trim();
        if (begin.length() != 11 || end.length() != 11) {
            Toast.makeText(SearchActivity.this, "输入初始期号有误", Toast.LENGTH_SHORT).show();
        } else if (begin.length() == 11 && end.length() == 11) {
            long l_begin = Long.valueOf(begin);
            long l_end = Long.valueOf(end);
            if (l_begin >= l_end) {
                Toast.makeText(SearchActivity.this, "初始的期号>结束的期号", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SearchActivity.this, ResultAcivity.class);
                intent.putExtra(BEGIN_RESULT, begin);
                intent.putExtra(END_RESULT, end);
                intent.putExtra(KUNSUN, Double.valueOf(et_kunsun.getText().toString().trim()));
                startActivity(intent);
            }
        }


    }

    public void begin(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra("name", "year");
        startActivityForResult(intent, BEGIN);
    }

    public void end(View v) {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra("name", "end");
        startActivityForResult(intent, END);
    }

    public static final int BEGIN = 0;
    public static final int END = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            String str = data.getStringExtra("data");
            switch (requestCode) {
                case BEGIN:
                    tv_begin.setText(str);
                    break;
                case END:
                    tv_end.setText(str);
                    break;
            }
        }
    }

    public void count(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long count = new ResultsDao(SearchActivity.this).queryCount();
                Message message = new Message();
                message.what = 1;
                message.obj = count;
                handler.sendMessage(message);
            }
        }).start();
    }

    public static final String BEGIN_RESULT = "begin";
    public static final String END_RESULT = "end";
    public static final String KUNSUN = "kunsun";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long count = (long) msg.obj;
                    tv_count.setText("数量:" + count);
                    break;
            }
            return false;
        }
    });
    TextView tv;
    TextView tv_begin;
    TextView tv_end;
    ProgressDialog dialog;
    Button tv_count;
    EditText et_kunsun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(getApplication());
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.search_activity);
        tv_count = (Button) findViewById(R.id.tv_count);
        tv = (TextView) findViewById(R.id.tv);
        tv_begin = (TextView) findViewById(R.id.tv_begin);
        tv_end = (TextView) findViewById(R.id.tv_end);
        dialog = new ProgressDialog(SearchActivity.this);
        et_kunsun = (EditText) findViewById(R.id.et_kunsun);
        dialog.setCanceledOnTouchOutside(false);
        tv_count.setText("点击查询数据");
    }


}

