package com.dkhs.cqssc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/23.
 */
public class ResultAcivity extends AppCompatActivity {
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
