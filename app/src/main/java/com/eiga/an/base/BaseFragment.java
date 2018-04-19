package com.eiga.an.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import cn.albert.autosystembar.SystemBarHelper;

public class BaseFragment extends Fragment {
    private ProgressDialog dialog;//请求的dialog
    //在base activity 里定义请求队列
    private RequestQueue queue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * 设置沉浸式状态栏
     * @param context
     */
    public void setImmersedNavigationBar(Activity context, int colorId){
        new SystemBarHelper.Builder()
                .enableImmersedNavigationBar(false)
                .statusBarColor(getResources().getColor(colorId))
                .enableAutoSystemBar(false)
                .into(context);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue!=null){
            queue.stop();
        }
    }

    //提供给子类请求使用。
    public <T> void StringRequest(int what, Request<String> request, SimpleResponseListener<String> listener) {
        queue = NoHttp.newRequestQueue();
        queue.add(what, request, listener);
    }

    // 提供给子类请求使用。
    public <T> void EntityRequest(int what, Request<T> request, SimpleResponseListener<T> listener) {

        queue = NoHttp.newRequestQueue();
        queue.add(what, request, listener);
    }

    public void showLoading(Context context) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(context);
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
}

