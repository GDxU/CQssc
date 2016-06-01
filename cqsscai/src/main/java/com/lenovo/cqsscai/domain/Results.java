package com.lenovo.cqsscai.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Results")
public class Results {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String qihao;//开奖期号
    @DatabaseField
    private String result;//开奖结果

    public Results(String qihao, String result) {
        super();
        this.qihao = qihao;
        this.result = result;
    }

    public Results() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getQihao() {
        return qihao;
    }

    public void setQihao(String qihao) {
        this.qihao = qihao;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Results{" +
                "id=" + id +
                ", qihao='" + qihao + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
