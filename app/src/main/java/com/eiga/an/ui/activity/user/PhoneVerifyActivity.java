package com.eiga.an.ui.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetBankInfoModel;
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
 * Created by ASUS on 2018/3/22.
 */

public class PhoneVerifyActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.phone_verify_phone)
    EditText phoneVerifyPhone;
    @BindView(R.id.phone_verify_type)
    EditText phoneVerifyType;
    @BindView(R.id.phone_verify_checkbox)
    CheckBox phoneVerifyCheckbox;
    private String TAG = getClass().getName();

    private String token,phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        setImmersedNavigationBar(this,R.color.white);
        autoVirtualKeys();
        ButterKnife.bind(this);
        token= (String) SharedPreferencesUtils.getShared(PhoneVerifyActivity.this,Constant.User_Login_Token,"");
        phone= (String) SharedPreferencesUtils.getShared(PhoneVerifyActivity.this,Constant.User_Login_Name,"");
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("运营商认证");
    }

    @OnClick({R.id.common_title_back, R.id.phone_verify_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.phone_verify_commit:
                if (PhoneUtils.isMobile(phoneVerifyPhone.getText().toString())&&
                        !TextUtils.isEmpty(phoneVerifyType.getText().toString())){
                    if (phoneVerifyCheckbox.isChecked()){
                        httpPhoneVerify();
                    }else {
                        PhoneUtils.toast(PhoneVerifyActivity.this,"请先同意运营商协议");
                    }

                }else {
                    PhoneUtils.toast(PhoneVerifyActivity.this,"信息有误");
                }

                break;
        }
    }

    /**
     * 手机运营商认证
     */
    private void httpPhoneVerify() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_Bank, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",phone);
        mStringRequest.add("Token",token);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiGetBankInfoModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiGetBankInfoModel.class);
                        if (model.Status==1){
                            //setHttpData(model.BankCard);
                        }
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(PhoneVerifyActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }

}
