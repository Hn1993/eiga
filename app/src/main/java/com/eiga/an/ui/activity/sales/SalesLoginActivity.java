package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetBankInfoModel;
import com.eiga.an.model.salesModel.ApiSalesLoginModel;
import com.eiga.an.ui.activity.user.PhoneVerifyActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
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
 * Created by ASUS on 2018/4/10.
 */

public class SalesLoginActivity extends BaseActivity {
    @BindView(R.id.sales_login_name)
    EditText salesLoginName;
    @BindView(R.id.sales_login_psw)
    EditText salesLoginPsw;
    private String TAG = getClass().getName();

    private String salesPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_login);
        ButterKnife.bind(this);
        salesPhone= (String) SharedPreferencesUtils.getShared(SalesLoginActivity.this,Constant.Sales_Login_Phone,"");
        findViews();
    }

    private void findViews() {
        salesLoginName.setText(salesPhone);
        salesLoginName.setSelection(salesPhone.length());
    }

    @OnClick({R.id.sales_login_finish, R.id.sales_login_go, R.id.sales_login_forgot})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.sales_login_finish:
                finish();
                break;
            case R.id.sales_login_go:

                if (PhoneUtils.isMobile(salesLoginName.getText().toString())&& !TextUtils.isEmpty(salesLoginPsw.getText().toString())) {

                    //在这里请求接口
                    httpRequestLogin();

                }else {
                    PhoneUtils.toast(SalesLoginActivity.this,"信息有误,请检查");
                }


                break;
            case R.id.sales_login_forgot:
                break;
        }
    }

    private void httpRequestLogin() {

        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Login, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",salesLoginName.getText().toString());
        mStringRequest.add("Password",salesLoginPsw.getText().toString());
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiSalesLoginModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiSalesLoginModel.class);
                        if (model.Status==1){
                            //setHttpData(model.BankCard);
                            SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.Sales_Login_Phone, salesLoginName.getText().toString());
                            SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.Sales_Login_Token, model.Token);
                            Intent intent=new Intent(SalesLoginActivity.this,SalesMainActivity.class);
                            startActivity(intent);
                            finish();
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
                PhoneUtils.toast(SalesLoginActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }
}
