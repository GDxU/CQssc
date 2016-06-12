package com.lenovo.cqsscai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.cqsscai.dao.ResultsDao;
import com.lenovo.cqsscai.domain.Results;
import com.lenovo.cqsscai.domain.Shouyi;
import com.lenovo.cqsscai.domain.Utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/1/23.
 */
public class ResultAcivity extends Activity {
    public static final double i = 1900 / 2;//投资回报率(买大卖小的回报率都是这个数)

    public static final int REPEAT = 3;//最大重复投资次数
    public static int count = REPEAT;//最大重复投资次数
    //public double zhongjiangMoney;//本次中奖金额
    double subMoney = 0;//本周期收益
    double totalMoney = 0;//全部收益（开始到结束）
    //连续中出的期号
    Map<Integer, String> repeatMap = new HashMap<>();
    int money = 2;//每注金额
    //投入倍数
    int MULT = 1;
    //连中REPEAT次，获取的收益
    double MAX_PROFIT = Math.pow(1.9, REPEAT - 1) * i * money - 2000;
    public static Results perRes = new Results("", "");
    private static int id;
    static boolean flag = true;
    private Shouyi shouyi;
    static List<String> zhongjiangfangan;//中奖方案
    StringBuffer sb = new StringBuffer();
    int end_id = 0;
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
        if (MULT == 0) {
            MULT = 1;
        }
        double periodMoney = 0;
        while (end_id <= id) {
            if (!flag) {
                flag = true;
                count = REPEAT;
                res = new Results("", "");
            } else {
                id = new ResultsDao(this).queryResultByqihao(begin).getId();
                periodMoney = 0;
            }
            List<String> arrayToList = Utils.getArrayToList(Utils.getFromAssets(this, "touzhu.txt"));//决定的方案
            List<String> quedingfangan = Utils.quedingfangan(arrayToList);//第二个方案
            Shouyi s = new Shouyi();
            while (flag) {
                MULT = getMult(MULT, kunsun, periodMoney);
                if (MULT == 0) {
                    Toast.makeText(ResultAcivity.this, "mult=0", Toast.LENGTH_SHORT).show();
                    return;
                }
                money = money * MULT;
                if (res.getQihao().equals("") && "".equals(res.getResult())) {
                    // 第一期
                    repeatMap.clear();
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
                        repeatMap.put(count, resultByqihao.getQihao());
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
                        repeatMap.clear();
                    }
                } else {
                    count--;
                    //不是第一期
                    Results resultById = new ResultsDao(this).queryResultById(id);
                    String result = resultById.getResult();
                    res = resultById;
                    if (!zhongjiangfangan.contains(result)) {
                        sb.append("************************************\n");
                        res = new Results("", "");
                    }
                    if (arrayToList.contains(result)) {
                        //第一种方案中奖
                        zhongjiangfangan = arrayToList;
                        sb.append("第1个方案中奖了....\n");
                        sb.append("期号： ");
                        sb.append(resultById.getQihao());
                        sb.append("\n");
                        repeatMap.put(count, resultById.getQihao());
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
                        repeatMap.clear();
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
                periodMoney += s.getZhongjiangjine() - s.getTouziedu();
                if (count == 1 || !arrayToList.contains(new ResultsDao(this).queryResultById(id).getResult())) {
                    //如果是第一方案的最后一期，或者第二方案的第一期 >>结束
                    sb.append("本轮收益:" + subMoney + " 总收益:" + totalMoney + " 期间收益:" + periodMoney + "\n");
                    sb.append("###########################\n");
                }
                id--;

            }
            //如果第一种方案连续中出三次,则重新计算periodMoney
            if (repeatMap.size() == 3) {
                sb.append("装进口袋: " + periodMoney + "\n");
                periodMoney = 0;
            }
        }
    }

    private int getMult(int nativeMult, double kunsun, double periodMoney) {
        if (nativeMult == 0) {
            Toast.makeText(ResultAcivity.this, "投入倍数==0,请重新查询", Toast.LENGTH_SHORT).show();
            return 0;
        }
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        for (Map.Entry i : entries) {
            if (-(kunsun - periodMoney) >= nativeMult * MAX_PROFIT) {
                int key = (int) i.getKey() + 1;
                if (key > 3) return 0;
                return map.get(key);
            }
        }
        return nativeMult;
    }

    ProgressDialog progressDialog;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        progressDialog = new ProgressDialog(this);
        tv_result = (TextView) findViewById(R.id.tv_result);
        Intent intent = getIntent();
        if (null != intent) {
            final String begin = intent.getStringExtra(SearchActivity.BEGIN_RESULT);
            final String end = intent.getStringExtra(SearchActivity.END_RESULT);
            final Double kunsun = intent.getDoubleExtra(SearchActivity.KUNSUN, 0);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        start(perRes, begin, end, kunsun);
                        handler.sendEmptyMessage(0);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            tv_result.setText(sb.toString());
        }
    };
}
