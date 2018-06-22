package com.eiga.an.ui.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiBankVerifyModel;
import com.eiga.an.model.jsonModel.ApiGetBankInfoModel;
import com.eiga.an.model.jsonModel.ApiMainModel;
import com.eiga.an.model.jsonModel.EventBusBankResultModel;
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

public class BankVerifyActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.bank_verify_name)
    EditText bankVerifyName;
    @BindView(R.id.bank_verify_bank)
    EditText bankVerifyBank;
    @BindView(R.id.bank_verify_bank_name)
    EditText bankVerifyBankName;
    private String TAG = getClass().getName();

    private String token,phone;

    private EventBusBankResultModel mBankModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_verify);
        setImmersedNavigationBar(this,R.color.white);
        autoVirtualKeys();
        ButterKnife.bind(this);
        token= (String) SharedPreferencesUtils.getShared(BankVerifyActivity.this,Constant.User_Login_Token,"");
        phone= (String) SharedPreferencesUtils.getShared(BankVerifyActivity.this,Constant.User_Login_Name,"");
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("上传银行卡信息");
        //httpGetBankInfo();
    }

    @OnClick({R.id.common_title_back, R.id.bank_verify_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.bank_verify_commit:
                if (TextUtils.isEmpty(bankVerifyBank.getText().toString())||
                        TextUtils.isEmpty(bankVerifyBankName.getText().toString())||
                        TextUtils.isEmpty(bankVerifyName.getText().toString())){
                    PhoneUtils.toast(BankVerifyActivity.this,"信息不能为空");
                }else {
                    //httpUploadBankInfo();
                    mBankModel=new EventBusBankResultModel();
                    mBankModel.bankCard=bankVerifyBank.getText().toString();
                    mBankModel.bankName=bankVerifyBankName.getText().toString();
                    mBankModel.bankUserName=bankVerifyName.getText().toString();

                    EventBus.getDefault().post(mBankModel,"upload_bank");
                    finish();
                }

                break;
        }
    }


    /**
     * 获取绑定的银行卡信息
     */
    private void httpGetBankInfo() {
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
                            setHttpData(model.BankCard);
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
                PhoneUtils.toast(BankVerifyActivity.this,"网络请求失败,请检查网络后重试");
            }
        });

    }

    private void setHttpData(ApiGetBankInfoModel.BankCardBean bankCard) {
        bankVerifyName.setText(bankCard.CardHolderName);
        bankVerifyBank.setText(bankCard.CardId);
        bankVerifyBankName.setText(bankCard.BankName);
    }

    /**
     * 提交银行卡信息
     */
    private void httpUploadBankInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Bond_Bank, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",phone);
        mStringRequest.add("Token",token);
        mStringRequest.add("BankName",bankVerifyBankName.getText().toString());
        mStringRequest.add("CardHolderName",bankVerifyName.getText().toString());
        mStringRequest.add("CardId",bankVerifyBank.getText().toString());

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiBankVerifyModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiBankVerifyModel.class);
                        if (model.Status==1){
                            EventBus.getDefault().post("bond_success","bond_success");
                            finish();
                            PhoneUtils.toast(BankVerifyActivity.this,"认证成功");
                        }
                        PhoneUtils.toast(BankVerifyActivity.this,model.Msg);
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
                PhoneUtils.toast(BankVerifyActivity.this,"网络请求失败,请检查网络后重试");
            }
        });

    }
}
