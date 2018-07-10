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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
 * Created by ASUS on 2018/7/4.
 */

public class LoanCalculatorActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.loan_calculator_car_money)
    EditText loanCalculatorCarMoney;
    @BindView(R.id.loan_calculator_cb_24)
    CheckBox loanCalculatorCb24;
    @BindView(R.id.loan_calculator_cb_36)
    CheckBox loanCalculatorCb36;
    @BindView(R.id.loan_calculator_first_money)
    TextView loanCalculatorFirstMoney;
    @BindView(R.id.loan_calculator_month_money)
    TextView loanCalculatorMonthMoney;
    @BindView(R.id.loan_calculator_first_money_percent)
    TextView loanCalculatorFirstMoneyPercent;
    @BindView(R.id.loan_calculator_first_money_percent_layout)
    RelativeLayout loanCalculatorFirstMoneyPercentLayout;
    private String TAG = getClass().getName();


    private String productId, productName;

    private double carMoney = 0;
    private double carLoanCoefficient = 494;//二手车贷款系数--默认24期
    private double newCarFirstMoneyPercent = 30;//新车首付比例


    private int productType; //新车 - 0
//    private int oldCarType=1; //二手车 -1
//    private int roomCarType=2; //房抵押贷款 -2
//    private int leaseCarType=3; //融资租赁 -3

    private int loadNumbers=24;

    private AlertDialog.Builder mAlertDialog;
    private String token,phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        token= (String) SharedPreferencesUtils.getShared(this,Constant.User_Login_Token,"");
        phone= (String) SharedPreferencesUtils.getShared(this,Constant.User_Login_Name,"");

        mAlertDialog=new AlertDialog.Builder(this);

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

        findViews();
    }

    private void findViews() {
        commonTitleTv.setText(productName);
        loanCalculatorCb24.setChecked(true);
        setCarLoanCoefficient();

        loanCalculatorFirstMoneyPercentLayout.setVisibility(productType==0?View.VISIBLE:View.GONE);

        loanCalculatorCb24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "loanCalculatorCb24=" + loanCalculatorCb24.isChecked());
                //loanCalculatorCb24.setChecked(loanCalculatorCb24.isClickable()?false:true);
                if (loanCalculatorCb24.isChecked()) {
                    loanCalculatorCb24.setChecked(true);
                    loanCalculatorCb36.setChecked(false);
                } else {
                    loanCalculatorCb24.setChecked(false);
                    loanCalculatorCb36.setChecked(true);
                }

                setCarLoanCoefficient();

                if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())){
                    calculationMoney(newCarFirstMoneyPercent/100);
                }

            }
        });

        loanCalculatorCb36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loanCalculatorCb36.setChecked(loanCalculatorCb24.isClickable()?false:true);
                if (loanCalculatorCb36.isChecked()) {
                    loanCalculatorCb36.setChecked(true);
                    loanCalculatorCb24.setChecked(false);
                } else {
                    loanCalculatorCb36.setChecked(false);
                    loanCalculatorCb24.setChecked(true);
                }

                setCarLoanCoefficient();

                if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())){
                    calculationMoney(newCarFirstMoneyPercent/100);
                }


            }
        });


        loanCalculatorCarMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculationMoney(newCarFirstMoneyPercent/100);
            }
        });
    }

    private void calculationMoney(double newCarPercent) {
        if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())) {
            carMoney = Double.valueOf(loanCalculatorCarMoney.getText().toString());
        }


        if (productType==1){//二手车
            //loanCalculatorFirstMoney.setText(String.valueOf(carMoney*1.1*0.3));
            loanCalculatorFirstMoney.setText(get2DecimalPlaces(carMoney * 1.1 * newCarPercent));
            //loanCalculatorMonthMoney.setText(String.valueOf(carMoney*1.1*0.7*loanCoefficient/10000));
            loanCalculatorMonthMoney.setText(get2DecimalPlaces(carMoney * 1.1 * (1-newCarPercent) * carLoanCoefficient / 10000));
        }else if (productType==0){//新车
            carMoney = carMoney + 4400 + carMoney * 0.05 + carMoney * 0.0855 ;
            Log.e(TAG,"new carmoney="+carMoney);
            loanCalculatorFirstMoney.setText(get2DecimalPlaces(carMoney * newCarPercent));
            loanCalculatorMonthMoney.setText(get2DecimalPlaces(carMoney * (1-newCarPercent) * carLoanCoefficient / 10000));
        }

    }


    private void setCarLoanCoefficient() {
        if (loanCalculatorCb24.isChecked()) {
            if (productType == 0) {
                carLoanCoefficient = 460.9;
            } else if (productType == 1) {
                carLoanCoefficient = 494; //固定系数
            }

        } else {
            if (productType == 0) {
                carLoanCoefficient = 322.11;
            } else if (productType == 1) {
                carLoanCoefficient = 342; //固定系数
            }
        }
    }

    @OnClick({R.id.common_title_back, R.id.loan_calculator_ok, R.id.loan_calculator_cancel,R.id.loan_calculator_choose_percent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.loan_calculator_ok:

                if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())){
                    httpUploadLoadInfo();
                }else {
                    PhoneUtils.toast(LoanCalculatorActivity.this,"贷款金额不能为空.");
                }

                break;
            case R.id.loan_calculator_cancel:
                finish();

            case R.id.loan_calculator_choose_percent:
                //finish();
                showChooseCarMoneyPercentDialog();
                break;
        }
    }

    //上传客户的贷款信息
    private void httpUploadLoadInfo() {

//        mAlertDialog.setTitle("提交成功,请等待业务人员与您联系.");
//        mAlertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                LoanCalculatorActivity.this.finish();
//            }
//        });
//        mAlertDialog.show();

        if (loanCalculatorCb24.isChecked()){
            loadNumbers=24;
        }else {
            loadNumbers=36;
        }

        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Input_Loan, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);

        mStringRequest.add("CreditTypeId", productId);
        mStringRequest.add("ApplyCarPrice", loanCalculatorCarMoney.getText().toString()); // 车价
        mStringRequest.add("CreditLimit", loadNumbers); // 贷款的期数
        mStringRequest.add("FirstPayProportion", (int) newCarFirstMoneyPercent); // 首付比例
        mStringRequest.add("FirstPayAmount", loanCalculatorFirstMoney.getText().toString()); // 首付金额
        mStringRequest.add("MonthlyPay", loanCalculatorMonthMoney.getText().toString()); //  月供

        Log.e(TAG, "CellPhone=" + phone);
        Log.e(TAG, "Token=" + token);
        Log.e(TAG, "CreditTypeId=" + productId);
        Log.e(TAG, "ApplyCarPrice=" + loanCalculatorCarMoney.getText().toString());
        Log.e(TAG, "CreditLimit=" + loadNumbers);
        Log.e(TAG, "FirstPayProportion=" + newCarFirstMoneyPercent);
        Log.e(TAG, "FirstPayAmount=" + loanCalculatorFirstMoney.getText().toString());
        Log.e(TAG, "MonthlyPay=" + loanCalculatorMonthMoney.getText().toString());

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
                            EventBus.getDefault().post("bond_success", "bond_success");
                            commonTitleTv.setText("我期望的贷款金额");


                            mAlertDialog.setTitle("提交成功,请等待业务人员与您联系.");
                            mAlertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    LoanCalculatorActivity.this.finish();
                                }
                            });
                            mAlertDialog.show();



                        } else {
                            PhoneUtils.toast(LoanCalculatorActivity.this, model.Msg.toString());
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

    private void showChooseCarMoneyPercentDialog() {

        new MaterialDialog.Builder(this)
                .title("请选择首付比例")
                .items(R.array.newCarPercent)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        Log.e(TAG,"which="+which);
                        Log.e(TAG,"text="+text);

                        newCarFirstMoneyPercent = Integer.valueOf(String.valueOf(text.subSequence(0,2)));
                        Log.e(TAG,"newCarFirstMoneyPercent="+newCarFirstMoneyPercent);
                        loanCalculatorFirstMoneyPercent.setText(text);

                        if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())){
                            calculationMoney(newCarFirstMoneyPercent/100);
                        }

                        return true;
                    }
                })
                .positiveText("确定")
                .show();

    }

}
