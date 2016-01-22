package com.dkhs.cqssc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.cqssc.dao.ResultsDao;
import com.dkhs.cqssc.db.AssetsDatabaseManager;
import com.dkhs.cqssc.domain.Results;
import com.dkhs.cqssc.domain.Shouyi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询统计数据
 * Created by Administrator on 2016/1/17.
 */
public class SearchActivity extends AppCompatActivity {
    EditText et;
    TextView tv;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        dialog = new ProgressDialog(SearchActivity.this);
        //   dialog.show();
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(getApplication());

    }

    public void submit(View v) {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start(perRes, et.getText().toString().trim());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }).start();

    }

    public static final double i = 1900 / 2;//投资回报率(买大卖小的回报率都是这个数)
    public static final double money = 2;//每注金额
    public static int count = 3;//最大重复投资次数
    //public double zhongjiangMoney;//本次中奖金额
    public double subMoney;//本次收益
    public static double totalMoney;//当前剩余资金
    public static Results perRes = new Results("", "");
    String strtouzhu = "002 004 006 009 010 011 012 013 016 017 018 019 021 022 023 024 026 027 035 039 043 047 049 051 052 053 056 059 063 064 065 066 068 069 071 072 076 079 081 083 084 085 091 093 098 100 101 102 103 104 105 107 108 115 123 124 126 128 130 134 135 136 138 139 140 143 144 151 153 154 155 158 160 161 162 165 167 169 170 174 175 176 177 178 179 180 183 185 187 188 189 191 196 197 198 199 202 204 207 208 210 214 217 218 221 222 223 224 227 229 234 238 240 243 244 245 246 248 250 258 260 265 268 269 275 276 278 279 280 281 282 284 285 286 288 289 290 291 292 293 296 297 299 303 306 310 314 315 316 318 319 321 322 324 326 327 328 332 333 338 341 343 344 345 346 347 352 353 355 358 359 360 363 365 367 368 370 372 373 374 377 379 380 381 382 384 386 388 390 394 395 398 399 400 401 402 404 406 410 411 412 413 415 416 418 419 420 421 422 425 427 429 432 433 434 435 436 437 440 441 443 444 446 448 449 450 452 453 456 457 458 460 461 463 464 468 473 476 478 481 482 483 484 487 488 489 491 493 495 497 499 500 502 503 507 510 511 512 513 519 521 522 523 524 525 526 527 528 530 532 533 536 537 538 541 542 546 547 548 549 552 553 554 556 557 558 560 564 565 566 567 571 572 574 576 577 578 580 585 587 589 591 592 594 596 598 600 601 603 604 607 609 610 611 612 614 617 619 620 622 624 629 630 632 634 638 639 642 643 645 649 651 653 654 655 656 658 662 663 665 670 672 673 676 678 680 681 683 684 685 687 688 689 690 692 696 698 700 702 703 707 708 710 711 714 718 719 720 721 724 726 727 730 734 735 738 740 743 745 751 753 756 760 761 763 764 768 771 772 777 778 781 783 784 785 786 793 796 799 801 803 804 805 807 808 812 817 818 819 820 822 823 826 828 830 834 835 836 838 840 841 843 844 847 850 851 854 855 856 862 863 864 865 866 867 869 871 872 876 878 879 880 881 882 883 885 888 889 891 894 898 901 903 904 906 907 908 910 918 921 922 923 924 926 927 929 930 933 936 940 943 945 946 950 951 953 954 956 958 959 963 964 966 967 968 972 973 975 976 977 981 982 983 984 987 993 994 995 998 999";
    private static int id;
    private Shouyi shouyi;
    static boolean flag = true;
    static List<String> zhongjiangfangan;//中奖方案

    /**
     * @param res   上期开奖对象
     * @param qihao 本期开奖号
     * @return 本期开奖对象
     * @throws SQLException
     */
    public void start(Results res, String qihao) throws SQLException {
        //Scanner sc = new Scanner(System.in);
        //Results reses=new Results();
      //  List<String> arrayToList = getArrayToList(strtouzhu);//决定的方案
        List<String> arrayToList = getArrayToList(getFromAssets("touzhu.txt"));//决定的方案
        List<String> quedingfangan = quedingfangan(arrayToList);//第二个方案
        Shouyi s = new Shouyi();
        while (flag) {

            if (res.getQihao().equals("") && "".equals(res.getResult())) {
                // 第一期
                count = 3;
                Results resultByqihao = new ResultsDao(this).queryResultByqihao(qihao);
                res = resultByqihao;
                String result = resultByqihao.getResult();
                id = resultByqihao.getId();
                if (arrayToList.contains(result)) {
                    //第一种方案中奖
                    Log.e("xue", "第1个方案中奖了....");
                    Log.e("xue", "期号： " + resultByqihao.getQihao());
                    // System.out.println("第1个方案中奖了....");
                    //  System.out.println("期号： " + resultByqihao.getQihao());
                    zhongjiangfangan = arrayToList;
                    s.setTouziedu(1000 * money);
                    s.setZhongjiangjine(money * i);
                    s.setTouzifangan(arrayToList);
                } else {
                    //第二种方案中奖
                    Log.e("xue", "第2个方案中奖了....");
                    Log.e("xue", "期号： " + resultByqihao.getQihao());
                    // System.out.println("第2个方案中奖了....");
                    //  System.out.println("期号： " + resultByqihao.getQihao());
                    zhongjiangfangan = quedingfangan;
                    s.setTouziedu(1000 * money);
                    s.setZhongjiangjine(money * i);
                    s.setTouzifangan(quedingfangan);
                }
            } else {
                Results resultById = new ResultsDao(this).queryResultById(id);
                String result = resultById.getResult();
                res = resultById;
                if (!zhongjiangfangan.equals(shouyi.getTouzifangan())) {
                    Log.e("xue", "***********************************************");
                    // System.out.println("***********************************************");
                    res = new Results("", "");
                }
                if (arrayToList.contains(result)) {
                    //第一种方案中奖
                    zhongjiangfangan = arrayToList;
                    Log.e("xue", "第1个方案中奖了....");
                    Log.e("xue", "期号： " + resultById.getQihao());
                    // System.out.println("第1个方案中奖了....");
                    ///   System.out.println("期号： " + resultById.getQihao());
                    s.setTouziedu(500 * money + shouyi.getZhongjiangjine());
                    s.setZhongjiangjine(shouyi.getZhongjiangjine() * i);
                    s.setTouzifangan(arrayToList);
                } else {
                    //第二种方案中奖
                    zhongjiangfangan = quedingfangan;
                    Log.e("xue", "第2个方案中奖了....");
                    Log.e("xue", "期号： " + resultById.getQihao());
                    //  System.out.println("第2个方案中奖了....");
                    //  System.out.println("期号： " + resultById.getQihao());
                    s.setTouziedu(1000 * money);
                    s.setZhongjiangjine(money * i);
                    s.setTouzifangan(quedingfangan);
                }

                count--;
                if (count == 0) {
                    flag = false;
                    //return;
                }

            }
            Log.e("xue", "投资额度：  " + s.getTouziedu() + "中奖金额：  " + s.getZhongjiangjine());
            Log.e("xue", "计数器：  " + count);
            //  System.out.println("投资额度：  " + s.getTouziedu() + "中奖金额：  " + s.getZhongjiangjine());
            //  System.out.println("计数器：  " + count);
            id--;
            shouyi = s;
        }
    }

    /**
     * 根据一给定的方案决定另一个方案
     *
     * @param nums 第一个投资方案
     * @return 第二个投资方案
     */
    public static List<String> quedingfangan(List<String> nums) {
        List<String> all = getAll(1000);
        all.removeAll(nums);
        return all;
    }

    /**
     * 规则所产生的所有结果
     *
     * @param num
     * @return
     */
    private static List<String> getAll(int num) {

        List<String> strs = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                for (int m = 0; m <= 9; m++) {
                    String str = "" + i + j + m;
                    strs.add(str);
                }
            }
        }
        return strs;
    }

    public static List<String> getArrayToList(String str) {
        String[] touzhus = str.split(" ");
        List<String> strs = new ArrayList<>();
        for (String s : touzhus) {
            strs.add(s);
        }
        return strs;

    }
    public String getFromAssets(String fileName) {
        try {

            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result+=line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}