package com.dkhs.cqssc.domain;

/**
 * Created by Administrator on 2016/1/17.
 */
public class Zhongjiang {
    public int zhongjianhao;
    public double zhongjiangMoney;

    public Zhongjiang(int zhongjianhao, double zhongjiangMoney) {
        this.zhongjianhao = zhongjianhao;
        this.zhongjiangMoney = zhongjiangMoney;
    }

    public Zhongjiang() {
    }

    public int getZhongjianhao() {
        return zhongjianhao;
    }

    public void setZhongjianhao(int zhongjianhao) {
        this.zhongjianhao = zhongjianhao;
    }

    public double getZhongjiangMoney() {
        return zhongjiangMoney;
    }

    public void setZhongjiangMoney(double zhongjiangMoney) {
        this.zhongjiangMoney = zhongjiangMoney;
    }
}
