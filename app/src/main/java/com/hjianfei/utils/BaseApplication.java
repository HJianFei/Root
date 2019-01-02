package com.hjianfei.utils;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {


    public static String ChinaGamePath = "/data/data/com.tencent.tmgp.pubgmhd/lib/libUE4.so";
    public static String ChinaTempPath = "";
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
        ChinaTempPath = getCacheDir() + "/libUe4.so";
    }
}
