package com.eiga.an.ui.activity.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMallUploadLoadModel;
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
 * Created by ASUS on 2018/7/17.
 */

public class PropertyCalculatorActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.property_calculator_property_money)
    EditText propertyCalculatorPropertyMoney;
    @BindView(R.id.property_calculator_cb_6)
    CheckBox propertyCalculatorCb6;
    @BindView(R.id.property_calculator_margin)
    TextView propertyCalculatorMargin;//保证金
    @BindView(R.id.property_calculator_month_interest)
    TextView propertyCalculatorMonthInterest;//月利息
    private String TAG = getClass().getName();

    private String productId, productName;
    private AlertDialog.Builder mAlertDialog;
    private String token, phone;
    private int productType; //新车 - 0

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_calculator);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        token = (String) SharedPreferencesUtils.getShared(this, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(this, Constant.User_Login_Name, "");

        mAlertDialog = new AlertDialog.Builder(this);
        productName = getIntent().getStringExtra(Constant.Main_Product_Name);
        productId = getIntent().getStringExtra(Constant.Main_Product_Id);
        if (productName.contains("新车")) {
            productType = 0;
        } else if (productName.contains("二手车")) {
            productType = 1;
        } else if (productName.contains("房")) {
            productType = 2;
        } else if (productName.contains("融资")) {
            productType = 3;
        }

        commonTitleTv.setText(productName);
        propertyCalculatorCb6.setChecked(true);

        propertyCalculatorPropertyMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(propertyCalculatorPropertyMoney.getText().toString())){
                    countPropertyMoney(Integer.valueOf(propertyCalculatorPropertyMoney.getText().toString()));
                }

            }
        });
    }

    //计算利息等等
    private void countPropertyMoney(int inputMoney) {
        propertyCalculatorMargin.setText(get2DecimalPlaces(inputMoney*0.8*0.05));
        propertyCalculatorMonthInterest.setText(get2DecimalPlaces(inputMoney*0.8*0.015));
    }

    @OnClick({R.id.common_title_back, R.id.loan_calculator_ok, R.id.loan_calculator_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.loan_calculator_ok:
                if (!TextUtils.isEmpty(propertyCalculatorPropertyMoney.getText().toString())){
                    httpUploadPropertyInfo();
                }else {
                    PhoneUtils.toast(PropertyCalculatorActivity.this,"亲，房产价格不能为空");
                }

//                mAlertDialog.setTitle("提交成功,请等待业务人员与您联系.");
//                mAlertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        PropertyCalculatorActivity.this.finish();
//                    }
//                });
//                mAlertDialog.show();
                break;
            case R.id.loan_calculator_cancel:
                finish();
                break;
        }
    }

    private void httpUploadPropertyInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Commit_Property_Product, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);

        mStringRequest.add("CreditTypeId", productId);
        mStringRequest.add("HouseWorth", propertyCalculatorPropertyMoney.getText().toString()); // 房产价值
        mStringRequest.add("CreditLimit", "6"); // 贷款的期数
        mStringRequest.add("CashDeposit", propertyCalculatorMargin.getText().toString()); // 保证金
        mStringRequest.add("MonthlyRate", propertyCalculatorMonthInterest.getText().toString()); // 月利息

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiMallUploadLoadModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiMallUploadLoadModel.class);
                        if (model.Status == 1) {

//                            EventBus.getDefault().post("bond_success", "bond_success");
//                            commonTitleTv.setText("我期望的贷款金额");

                            mAlertDialog.setTitle("提交成功,请等待业务人员与您联系.");
                            mAlertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    PropertyCalculatorActivity.this.finish();
                                }
                            });
                            mAlertDialog.show();



                        } else {
                            PhoneUtils.toast(PropertyCalculatorActivity.this, model.Msg.toString());
                        }
                        //
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
                //PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });
    }
}
