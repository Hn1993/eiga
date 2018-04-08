package com.eiga.an.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eiga.an.R;
import com.eiga.an.utils.AndroidWorkaround;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.albert.autosystembar.SystemBarHelper;

public class BaseActivity extends AutoLayoutActivity {

    private static BaseActivity mActivity;
    //在base activity 里定义请求队列
    private RequestQueue queue;

    public static Activity getBaseActivity(){
        if (mActivity == null){
            mActivity=new BaseActivity();
        }
        return mActivity;
    }


    // 提供给子类请求使用。
    public <T> void StringRequest(int what, Request<String> request, SimpleResponseListener<String> listener) {
        queue.add(what, request, listener);
    }

    /**
     * 跳转到登陆页
     * @param mContext
     * @param isFinishThis
     */
//    public void gotoLogin(Context mContext,boolean isFinishThis){
//        Intent intent=new Intent(mContext,LoginActivity.class);
//        startActivity(intent);
//        if (isFinishThis){
//            finish();
//        }
//    }

    /**
     * 设置沉浸式状态栏
     * @param context
     */
    public void setImmersedNavigationBar(Activity context,int colorId){
        new SystemBarHelper.Builder()
                .enableImmersedNavigationBar(false)
                .statusBarColor(getResources().getColor(colorId))
                .enableAutoSystemBar(false)
                .into(context);
    }

    public void autoVirtualKeys(){
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = NoHttp.newRequestQueue();
        autoVirtualKeys();
    }
}
