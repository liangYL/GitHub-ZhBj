package com.boke.zhbj;

import android.app.Application;

import org.xutils.x;

/**
 * Created by administrator on 2017/4/6.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // 程序创建的时候执行
        System.out.println(".......onCreate......");
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        //Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        //Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        //Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        //Log.d(TAG, "onConfigurationChanged");
//        super.onConfigurationChanged(newConfig);
//    }
}
