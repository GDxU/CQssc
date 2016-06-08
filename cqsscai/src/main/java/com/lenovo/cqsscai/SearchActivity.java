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
import com.lenovo.cqsscai.domain.Results;
import com.lenovo.cqsscai.domain.Shouyi;
import com.lenovo.cqsscai.domain.Utils;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 查询统计数据
 * Created by Administrator on 2016/1/17.
 */
public class SearchActivity extends Activity {
    public static final double i = 1900 / 2;//投资回报率(买大卖小的回报率都是这个数)
    public static double money = 2;//每注金额
    public static final int REPEAT = 3;//最大重复投资次数
    public static int count = REPEAT;//最大重复投资次数
    //public double zhongjiangMoney;//本次中奖金额
    public double subMoney;//本周期收益
    public static double totalMoney;//全部收益（开始到结束）
    public static Results perRes = new Results("", "");
    private static int id;
    static boolean flag = true;
    private Shouyi shouyi;
    static List<String> zhongjiangfangan;//中奖方案
    StringBuffer sb = new StringBuffer();
    int end_id = 0;
    private int fund = 0;
    //投入倍数
    private  int MULT = 1;
    //连中REPEAT次，获取的收益
    private double MAX_PROFIT = Math.pow(1.9, REPEAT - 1) * i * money - 2000;
    static Map<Integer, Integer> map;

    static {
        map = new LinkedHashMap<>();
        map.put(1, 1);
        map.put(2, 4);
        map.put(3, 20);
    }

    /**
     * @param res   上期开奖对象
     * @param begin 本期开奖号
     * @return 本期开奖对象
     * @throws SQLException
     */
    public void start(Results res, String begin, String end, double kunsun) throws SQLException {

        id = new ResultsDao(this).queryResultByqihao(begin).getId();
        end_id = new ResultsDao(this).queryResultByqihao(end).getId();
        totalMoney = 0;
        while (end_id <= id) {
            if (!flag) {
                flag = true;
                count = REPEAT;
                res = new Results("", "");
            } else {
                id = new ResultsDao(this).queryResultByqihao(begin).getId();
            }
            List<String> arrayToList = Utils.getArrayToList(Utils.getFromAssets(this, "touzhu.txt"));//决定的方案
            List<String> quedingfangan = Utils.quedingfangan(arrayToList);//第二个方案
            Shouyi s = new Shouyi();
            while (flag) {
                MULT = getMult(MULT, kunsun);
                if (MULT == 0) return;
                money = money * MULT;
                if (res.getQihao().equals("") && "".equals(res.getResult())) {
                    // 第一期
                    sb.append("###########################\n");
                    subMoney = 0;
                    count = REPEAT;
                    Results resultByqihao = new ResultsDao(this).queryResultById(id);
                    res = resultByqihao;
                    String result = resultByqihao.getResult();
                    id = resultByqihao.getId();
                    if (arrayToList.contains(result)) {
                        //第一种方案中奖
                        sb.append("第1个方案中奖了....\n");
                        sb.append("期号： ");
                        sb.append(resultByqihao.getQihao());
                        sb.append("\n");
                        zhongjiangfangan = arrayToList;
                        s.setTouziedu(1000 * money);
                        s.setZhongjiangjine(money * i);
                        s.setTouzifangan(arrayToList);
                    } else {
                        //第二种方案中奖
                        sb.append("第2个方案中奖了....\n");
                        sb.append("期号： ");
                        sb.append(resultByqihao.getQihao());
                        sb.append("\n");
                        zhongjiangfangan = quedingfangan;
                        s.setTouziedu(1000 * money);
                        s.setZhongjiangjine(money * i);
                        s.setTouzifangan(quedingfangan);
                        count = REPEAT;
                        res = new Results("", "");
                    }
                } else {
                    count--;
                    //不是第一期
                    Results resultById = new ResultsDao(this).queryResultById(id);
                    String result = resultById.getResult();
                    res = resultById;
                    if (!zhongjiangfangan.contains(result)) {
                        sb.append("***********************************************\n");
                        res = new Results("", "");
                    }
                    if (arrayToList.contains(result)) {
                        //第一种方案中奖
                        zhongjiangfangan = arrayToList;
                        sb.append("第1个方案中奖了....\n");
                        sb.append("期号： ");
                        sb.append(resultById.getQihao());
                        sb.append("\n");
                        s.setTouziedu(shouyi.getZhongjiangjine());
                        //s.setTouziedu(500 * money + shouyi.getZhongjiangjine());
                        s.setZhongjiangjine(shouyi.getZhongjiangjine() * 1.9);
                        s.setTouzifangan(arrayToList);
                    } else {
                        //第二种方案中奖
                        zhongjiangfangan = quedingfangan;
                        sb.append("第2个方案中奖了....\n");
                        sb.append("期号： ");
                        sb.append(resultById.getQihao());
                        sb.append("\n");
                        if (count != REPEAT) {
                            s.setTouziedu(shouyi.getZhongjiangjine());
                            s.setZhongjiangjine(0);
                        } else {
                            s.setTouziedu(1000 * money);
                            s.setZhongjiangjine(money * i);
                        }
                        s.setTouzifangan(quedingfangan);
                    }

                    if (count == 1) {
                        flag = false;
                    }
                }
                sb.append("投资额度：  ");
                sb.append(s.getTouziedu());
                sb.append("中奖金额：  ");
                sb.append(Utils.get2Value(s.getZhongjiangjine()) + " 本次:" + (s.getZhongjiangjine() - s.getTouziedu()));
                sb.append("\n计数器：  " + count + "\n");
                if (id <= end_id) {
                    flag = false;
                }
                shouyi = s;
                subMoney += s.getZhongjiangjine() - s.getTouziedu();
                totalMoney += s.getZhongjiangjine() - s.getTouziedu();
                if (count == 1 || !arrayToList.contains(new ResultsDao(this).queryResultById(id).getResult())) {
                    //如果是第一方案的最后一期，或者第二方案的第一期 >>结束
                    sb.append("本轮收益:" + subMoney + " 总收益:" + totalMoney + "\n");
                    sb.append("###########################\n");
                }
                id--;
            }
        }

    }

    private int getMult(int nativeMult, double kunsun) {
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        for (Map.Entry i : entries) {
            //if(kunsun<=i.getKey())
            if (-(kunsun-totalMoney) >= nativeMult * MAX_PROFIT) {
                int key = (int) i.getKey() + 1;
                if (key > 3) return 0;
                return map.get(key);


            }
        }

        return nativeMult;
    }

    public void submit(View v) {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String begin = tv_begin.getText().toString().trim();
                    String end = tv_end.getText().toString().trim();
                    if (begin.length() != 11 || end.length() != 11) {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        return;
                    } else if (begin.length() == 11 && end.length() == 11) {
                        long l_begin = Long.valueOf(begin);
                        long l_end = Long.valueOf(end);
                        if (l_begin >= l_end) {
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                        } else {
                            dialog.dismiss();
                            start(perRes, begin, end,
                                    Double.valueOf(et_kunsun.getText().toString().trim()));
                            Message message = new Message();
                            message.what = 4;
                            handler.sendMessage(message);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

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
        flag = true;
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long count = (long) msg.obj;
                    tv_count.setText("数量:" + count);
                    break;
                case 2:
                    Toast.makeText(SearchActivity.this, "输入初始期号有误", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(SearchActivity.this, "初始的期号>结束的期号", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Intent intent = new Intent(SearchActivity.this, ResultAcivity.class);
                    intent.putExtra("data", sb.toString());
                    startActivity(intent);
                    sb.delete(0, sb.length());
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

