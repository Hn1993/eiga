package com.eiga.an.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMyOrderListModel;
import com.eiga.an.ui.activity.WebActivity;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/16.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.about_version_info)
    TextView aboutVersionInfo;
    @BindView(R.id.about_version_code)
    TextView aboutVersionCode;
    private String TAG = getClass().getName();

    private Context context;
    private String token, phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        context=this;
        token = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Name, "");
        findViews();

        //httpGetUserProtocol();
    }

    private void httpGetUserProtocol() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Get_Confidentiality_Agreement, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiMyOrderListModel model = null;

                    try {
                        model = new Gson().fromJson(response.get(), ApiMyOrderListModel.class);
                        if (model.Status == 1) {
                            //setAdapterData(model.Data);

                        } else {
                            PhoneUtils.toast(context, model.Msg.toString());
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Exception=" + e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(context, "网络请求失败,请检查网络后重试");
            }
        });

    }


    private void findViews() {
        commonTitleTv.setText("关于");
        aboutVersionInfo.setText("版本号："+ PhoneUtils.getVersionCode(AboutActivity.this));
        aboutVersionCode.setText(PhoneUtils.getVersionCode(AboutActivity.this));
    }

    @OnClick({R.id.common_title_back, R.id.about_protocol, R.id.about_review})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.about_protocol:
                intent=new Intent(AboutActivity.this, WebActivity.class);
                intent.putExtra(Constant.WebUrl,Constant.Url_Sales_Get_Confidentiality_Agreement);
                intent.putExtra(Constant.WebTitle,"协议及申明");
                startActivity(intent);
                break;
            case R.id.about_review:
                break;
        }
    }
}
