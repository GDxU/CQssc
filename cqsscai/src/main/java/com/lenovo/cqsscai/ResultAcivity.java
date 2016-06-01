package com.lenovo.cqsscai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/23.
 */
public class ResultAcivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        TextView tv_result = (TextView) findViewById(R.id.tv_result);
        Intent intent = getIntent();
        if(null!=intent){
            tv_result.setText(intent.getStringExtra("data"));
        }
    }
}
