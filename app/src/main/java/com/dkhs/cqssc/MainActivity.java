package com.dkhs.cqssc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dkhs.cqssc.dao.ResultsDao;
import com.dkhs.cqssc.domain.Results;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 把文件写到数据库
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(MainActivity.this);
    }

    ProgressDialog dialog;

    public void add(View v) {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFromAssets("ssc.txt");
                dialog.dismiss();
            }
        }).start();
        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
    }

    ResultsDao dao;

    public String getFromAssets(String fileName) {
        try {
            dao = new ResultsDao(this);
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            List<Results> list = new ArrayList<>();
            int i = 1;
            while ((line = bufReader.readLine()) != null) {
                String[] len = line.split("\t");
                Results results = new Results();
                results.setQihao(len[0].replace("-", ""));
                results.setResult(len[1].substring(2));
                list.add(results);
                if (i >= 1000) {
                    i = 0;
                    dao.addAll(list);
                    list.clear();
                }
                i++;
            }
            System.out.println(">>>>");
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
