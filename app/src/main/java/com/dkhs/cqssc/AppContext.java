package com.dkhs.cqssc;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Administrator on 2016/1/17.
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
