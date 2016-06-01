package com.lenovo.cqsscai.domain;

import java.util.List;

public class Shouyi {
    private double touziedu;//投资金额
    private double zhongjiangjine;//中奖金额
    private List<String> touzifangan;

    public double getTouziedu() {
        return touziedu;
    }

    public void setTouziedu(double touziedu) {
        this.touziedu = touziedu;
    }

    public double getZhongjiangjine() {
        return zhongjiangjine;
    }

    public void setZhongjiangjine(double zhongjiangjine) {
        this.zhongjiangjine = zhongjiangjine;
    }

    public List<String> getTouzifangan() {
        return touzifangan;
    }

    public void setTouzifangan(List<String> touzifangan) {
        this.touzifangan = touzifangan;
    }

}
