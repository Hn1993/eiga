package com.eiga.an.ui.activity.user;

import android.content.Intent;
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
import com.eiga.an.ui.activity.WebActivity;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

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
        commonTitleTv.setText("上传手机号信息");
    }

    @OnClick({R.id.common_title_back, R.id.phone_verify_commit,R.id.phone_verify_protocol})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.phone_verify_protocol:
                String url=Constant.H5_Eiga_Protocol+"2";
                intent=new Intent(PhoneVerifyActivity.this, WebActivity.class);
                intent.putExtra(Constant.WebUrl,url);
                intent.putExtra(Constant.WebTitle,"运营商协议");
                startActivity(intent);
                break;
            case R.id.phone_verify_commit:
                if (PhoneUtils.isMobile(phoneVerifyPhone.getText().toString())){
                    if (phoneVerifyCheckbox.isChecked()){
                        //httpPhoneVerify();
                        EventBus.getDefault().post(phoneVerifyPhone.getText().toString(),"upload_phone");
                        finish();
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
        StringRequest mStringRequest = new StringRequest(Constant.Url_Upload_Phoneinfo, RequestMethod.POST);
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
                            PhoneUtils.toast(PhoneVerifyActivity.this,"认证成功");
                            EventBus.getDefault().post("bond_success","bond_success");
                            finish();
                        }
                        PhoneUtils.toast(PhoneVerifyActivity.this,model.Msg);
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
