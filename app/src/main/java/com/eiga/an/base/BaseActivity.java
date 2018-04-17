package com.eiga.an.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.eiga.an.R;
import com.eiga.an.ui.activity.sales.SalesLoginActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
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
    private ProgressDialog dialog;//请求的dialog

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
    public void gotoLogin(Context mContext, boolean isFinishThis){
        Intent intent=new Intent(mContext,UserLoginActivity.class);
        startActivity(intent);
        if (isFinishThis){
            finish();
        }
    }


    public void gotoSalesLogin(Context mContext, boolean isFinishThis){
        Intent intent=new Intent(mContext,SalesLoginActivity.class);
        startActivity(intent);
        if (isFinishThis){
            finish();
        }
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("数据加载中,请稍后...");
        dialog.show();
    }
    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

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
