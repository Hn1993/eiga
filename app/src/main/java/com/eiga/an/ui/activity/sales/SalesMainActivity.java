package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesMainModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/10.
 */

public class SalesMainActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.common_title_back)
    RelativeLayout commonBack;
    @BindView(R.id.sales_main_head)
    ImageView salesMainHead;
    @BindView(R.id.sales_main_name)
    TextView salesMainName;
    @BindView(R.id.sales_main_type)
    TextView salesMainType;
    private String TAG = getClass().getName();

    private String salesPhone, salesToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_main);
        ButterKnife.bind(this);
        autoVirtualKeys();
        EventBus.getDefault().register(this);
        setImmersedNavigationBar(this, R.color.white);

        salesPhone = (String) SharedPreferencesUtils.getShared(SalesMainActivity.this, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(SalesMainActivity.this, Constant.Sales_Login_Token, "");
        Log.e(TAG, "salesPhone=" + salesPhone);
        Log.e(TAG, "salesToken=" + salesToken);

        if (TextUtils.isEmpty(salesToken)) {
            gotoSalesLogin(this, true);
        }

        findViews();
        httpGetInfo();
    }

    private void httpGetInfo() {
        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Main, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesMainModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesMainModel.class);
                        if (model.Status == 1) {
                            setHttpData(model);
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
                PhoneUtils.toast(SalesMainActivity.this, "网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setHttpData(ApiSalesMainModel model) {
        SharedPreferencesUtils.putShared(SalesMainActivity.this,Constant.Sales_Login_Nickname,model.ClerkName);
        salesMainName.setText(model.ClerkName);
        salesMainType.setText(model.OfficialPartnerName);
        GlideUtils.getGlideUtils().glideCircleImage(SalesMainActivity.this,Constant.Url_Common+model.HeadSculpture,salesMainHead);
    }

    private void findViews() {
        commonTitleTv.setText("业务员后台");
        commonBack.setVisibility(View.GONE);
    }

    @OnClick({R.id.common_title_back, R.id.sales_main_setting, R.id.sales_main_work, R.id.sales_main_car_work, R.id.sales_main_car_client, R.id.sales_main_changeId})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.common_title_back:
                break;
            case R.id.sales_main_setting:
                intent = new Intent(SalesMainActivity.this, SalesSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_main_work:
                intent = new Intent(SalesMainActivity.this, SalesWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_main_car_work:
                break;
            case R.id.sales_main_car_client:
                intent = new Intent(SalesMainActivity.this, SalesCustomerActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_main_changeId://退出登录
                doLogout();
                break;
        }
    }

    private void doLogout() {
        SharedPreferencesUtils.clearSp(SalesMainActivity.this, Constant.Sales_Login_Token);
        Intent intent = new Intent(SalesMainActivity.this, SalesLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscriber(tag = "fix_sales_info")
    public void fixInfoEvent(String str){
        httpGetInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
