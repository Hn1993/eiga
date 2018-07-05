package com.eiga.an.base;

import android.app.Application;
import android.content.Intent;

import com.eiga.an.service.AppInitIntentService;
import com.eiga.an.utils.ActivityManager;
import com.mob.MobSDK;
import com.yanzhenjie.nohttp.NoHttp;


public class MyApplication extends Application {

    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();

        NoHttp.initialize(this);//1. 默认初始化 nohttp
        //Stetho.initializeWithDefaults(this);
        myApplication=this;
        MobSDK.init(this);
        //注册管理器
        ActivityManager.getInstance().register(this);
        Intent intentService = new Intent(this, AppInitIntentService.class);
        startService(intentService);
    }

    public static Application getInstance(){
        return myApplication;
    }
}
