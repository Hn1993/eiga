package com.eiga.an.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

public class BaseFragment extends Fragment {

    //在base activity 里定义请求队列
    private RequestQueue queue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

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
}

