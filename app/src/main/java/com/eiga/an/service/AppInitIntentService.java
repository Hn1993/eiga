package com.eiga.an.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Administrator on 2017/7/1.
 */

public class AppInitIntentService extends IntentService {

    private final String TAG = getClass().getName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AppInitIntentService(String name) {
        super(name);
    }

    public AppInitIntentService(){
        super("AppInitIntentService");

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"AppIntentService--->onCreate");
        System.out.println("AppIntentService--->onCreate");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"AppIntentService--->onDestroy");
        System.out.println("AppIntentService--->onDestroy");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //初始化fb数据库调试
//        Stetho.initializeWithDefaults(this);
//        LiteOrmDBUtil.createDb(this, "nyl.db");
//        MobSDK.init(this, "2077d5d9795b2", "7ad413f46ed328d5e60a8ec8e369d3a7");
//        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);//设置统计类型

    }
}
