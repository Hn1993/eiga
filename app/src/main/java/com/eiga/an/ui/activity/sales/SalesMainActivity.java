package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import com.eiga.an.model.JsonTest;
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

        //httpTest();
    }

//    private void httpTest() {
//        Log.e(TAG,"httpTest");
//
//        PhoneUtils.base64ToFile("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" , Environment.getExternalStorageDirectory()+"/111.jpg");
////        StringRequest mStringRequest = new StringRequest("https://way.jd.com/hangzhoushumaikeji/eid_image_get", RequestMethod.POST);
////        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
////        mStringRequest.add("appkey", "54ae1b27174cf9b5263ec47f50259138");
////        mStringRequest.add("body", "name=黄安&idcard=360681199306032210");
//////        mStringRequest.add("idcard", "522501198607155556");
////        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
////            @Override
////            public void onSucceed(int what, Response<String> response) {
////                super.onSucceed(what, response);
////                dismissLoading();
////                if (what == 101) {
////                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
////                    JsonTest model = null;
////                    try {
////                        model = new Gson().fromJson(response.get(), JsonTest.class);
//////                        if (model.Status == 1) {
//////                            setHttpData(model);
//////                        }
////
////                        PhoneUtils.base64ToFile(model.result.result.photo, Environment.getExternalStorageDirectory().getPath()+"11.jpg");
////                    } catch (Exception e) {
////                        Log.e(TAG, "Exception=" + e);
////                    }
////                }
////            }
////
////            @Override
////            public void onFailed(int what, Response<String> response) {
////                super.onFailed(what, response);
////                dismissLoading();
////                Log.e(TAG, "onFailed==" + response.get());
////                PhoneUtils.toast(SalesMainActivity.this, "网络请求失败,请检查网络后重试");
////            }
////        });
//
//    }

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
        Log.e(TAG,"str="+str);
        httpGetInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
