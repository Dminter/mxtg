package com.zncm.mxtg.ui;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public Context ctx;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        this.ctx = this.getApplicationContext();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
