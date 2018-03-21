package com.eiga.an.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseFragment;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.InfoCollectionActivity;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.albert.autosystembar.SystemBarHelper;

/**
 * 首页
 */
public class MainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.fg_main_et_phone)
    EditText fgMainEtPhone;
    @BindView(R.id.fg_main_et_code)
    EditText fgMainEtCode;
    @BindView(R.id.fg_main_tv_getcode)
    TextView fgMainTvGetcode;
    private View mRootView;

    private String TAG = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView==null){
            mRootView = inflater.inflate(R.layout.fragment_main, null);
        }
        new SystemBarHelper.Builder()
                .enableImmersedStatusBar(true)
                .enableImmersedNavigationBar(true)
                .into(getActivity());
        unbinder = ButterKnife.bind(this, mRootView);
        findViews();
        return mRootView;

    }


    private void findViews() {
//        fgMainInputImage.setImageDrawable(getResources().getDrawable(R.mipmap.white_bg));
//        fgMainInputImage.setImageShadowColor(getResources().getColor(R.color.color_shadow));
        //fgMainInputImage.setImageRadius(2);
        //httpGetMiGuanData();
        //httpGetJiedaiData();
    }

    private void httpGetJiedaiData() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_Jd_jiedai, RequestMethod.POST);
        mStringRequest.add("appkey", Constant.Jd_key);
        mStringRequest.add("name", "蓝利明");
        mStringRequest.add("idCard", "332527198507184753");
        mStringRequest.add("mobile", "13732423950");
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                if (what == 101) {
                    Log.e(TAG, "jiedai_onSucceed==" + response.get());

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }

    private void httpGetMiGuanData() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_Jd_miguan, RequestMethod.POST);
        mStringRequest.add("appkey", Constant.Jd_key);
        mStringRequest.add("name", "蓝利明");
        mStringRequest.add("idCard", "332527198507184753");
        mStringRequest.add("phone", "13732423950");
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                if (what == 101) {
                    Log.e(TAG, "onSucceed==" + response.get());

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.fg_main_tv_getcode, R.id.fg_main_tv_go})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.fg_main_tv_getcode: //获取验证码
                break;
            case R.id.fg_main_tv_go:// 评估
                intent=new Intent(getActivity(), InfoCollectionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
