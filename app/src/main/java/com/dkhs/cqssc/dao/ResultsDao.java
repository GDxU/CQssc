package com.dkhs.cqssc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dkhs.cqssc.DatabaseHelper;
import com.dkhs.cqssc.db.AssetsDatabaseManager;
import com.dkhs.cqssc.domain.Results;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ResultsDao {
    private Context context;
    private Dao<Results, Integer> resultsDaoOpe;
    private DatabaseHelper helper;
    SQLiteDatabase db1;

    public ResultsDao(Context context) {
        this.context = context;
      /*  try {
            helper = DatabaseHelper.getHelper(context);
            resultsDaoOpe = helper.getDao(Results.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }*/
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        db1 = mg.getDatabase("ssc.db");
    }



    /**
     * 根据期号查询Result
     */
    public Results queryResultByqihao(String qihao) {
        Cursor cursor = db1.rawQuery("select * from Results where qihao=" + qihao, null);
        Results results = new Results();
        if (cursor.moveToNext()) {
            results.setId(cursor.getInt(cursor.getColumnIndex("id")));
            results.setQihao(cursor.getString(cursor.getColumnIndex("qihao")));
            results.setResult(cursor.getString(cursor.getColumnIndex("result")));
        }
       /* try {
            List<Results> resultse = resultsDaoOpe.queryBuilder().where().eq("qihao", qihao).query();
            return resultse.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return results;
    }
    /**
     * 查询数量
     */
    public long queryCount() {
        Cursor cursor = db1.rawQuery("select count(*) from Results", null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }

    /**
     * 根据期号查询Result
     */
    public Results queryResultById(int id) {
        Cursor cursor = db1.rawQuery("select * from Results where id=" + id, null);
        Results results = new Results();
        if (cursor.moveToNext()) {
            results.setId(cursor.getInt(cursor.getColumnIndex("id")));
            results.setQihao(cursor.getString(cursor.getColumnIndex("qihao")));
            results.setResult(cursor.getString(cursor.getColumnIndex("result")));
        }


        /*try {
            List<Results> resultses = resultsDaoOpe.queryBuilder().where().eq("id", id).query();
            return resultses.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return results;
    }

    /**
     * 增加一个数据
     *
     * @param results
     */
    public void add(Results results) {
        try {
            //  resultsDaoOpe.
            resultsDaoOpe.create(results);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量增加数据
     *
     * @param list
     */
    public void addAll(final List<Results> list) {

        try {
            TransactionManager.callInTransaction(resultsDaoOpe.getConnectionSource(),

                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            for (int i = 0; i < list.size(); i++) {
                                StringBuffer sb = new StringBuffer();
                                sb.append("insert into Results('result','qihao') values('" + list.get(i).getResult()
                                        + "','" + list.get(i).getQihao() + "')");
                                resultsDaoOpe.executeRawNoArgs(sb.toString());
                                if (i == list.size() - 1) {
                                    Log.e("xue", "qihao= " + list.get(i).getQihao());
                                }
                            }
                            return null;
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
