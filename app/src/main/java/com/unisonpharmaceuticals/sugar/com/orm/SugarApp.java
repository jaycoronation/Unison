package com.unisonpharmaceuticals.sugar.com.orm;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class SugarApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
