package com.eiga.an.ui.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;

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
    private String TAG = getClass().getName();


    private String productId,productName;

    private double carMoney=0;
    private int loanCoefficient = 494;//贷款系数--默认24期

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        productName=getIntent().getStringExtra(Constant.Main_Product_Name);
        productId=getIntent().getStringExtra(Constant.Main_Product_Id);

        findViews();
    }

    private void findViews() {
        commonTitleTv.setText(productName);
        loanCalculatorCb24.setChecked(true);

        loanCalculatorCb24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"loanCalculatorCb24="+loanCalculatorCb24.isChecked());
                //loanCalculatorCb24.setChecked(loanCalculatorCb24.isClickable()?false:true);
                if (loanCalculatorCb24.isChecked()){
                    loanCalculatorCb24.setChecked(true);
                    loanCalculatorCb36.setChecked(false);
                }else {
                    loanCalculatorCb24.setChecked(false);
                    loanCalculatorCb36.setChecked(true);
                }
                setLoanCoefficient();
                calculationMoney();
            }
        });
//        loanCalculatorCb24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setLoanCoefficient();
//                if (!isChecked){
//                    loanCalculatorCb24.setChecked(true);
//                    loanCalculatorCb36.setChecked(false);
//                }else {
//                    loanCalculatorCb24.setChecked(false);
//                    loanCalculatorCb36.setChecked(true);
//                }
//                calculationMoney();
//            }
//        });
        loanCalculatorCb36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loanCalculatorCb36.setChecked(loanCalculatorCb24.isClickable()?false:true);
                if (loanCalculatorCb36.isChecked()){
                    loanCalculatorCb36.setChecked(true);
                    loanCalculatorCb24.setChecked(false);
                }
                else {
                    loanCalculatorCb36.setChecked(false);
                    loanCalculatorCb24.setChecked(true);
                }
                setLoanCoefficient();
                calculationMoney();

            }
        });

//        loanCalculatorCb36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setLoanCoefficient();
//                if (!isChecked){
//                    loanCalculatorCb36.setChecked(true);
//                    loanCalculatorCb24.setChecked(false);
//                }else {
//                    loanCalculatorCb24.setChecked(true);
//                    loanCalculatorCb36.setChecked(false);
//                }
//                calculationMoney();
//            }
//        });


        loanCalculatorCarMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculationMoney();
            }
        });
    }

    private void calculationMoney() {
        if (!TextUtils.isEmpty(loanCalculatorCarMoney.getText().toString())){
            carMoney=Double.valueOf(loanCalculatorCarMoney.getText().toString());
        }

        //loanCalculatorFirstMoney.setText(String.valueOf(carMoney*1.1*0.3));
        loanCalculatorFirstMoney.setText(get2DecimalPlaces(carMoney*1.1*0.3));
        //loanCalculatorMonthMoney.setText(String.valueOf(carMoney*1.1*0.7*loanCoefficient/10000));
        loanCalculatorMonthMoney.setText(get2DecimalPlaces(carMoney*1.1*0.7*loanCoefficient/10000));

    }


    private void setLoanCoefficient(){
        if (loanCalculatorCb24.isChecked()){
            loanCoefficient = 494; //固定系数
        }else {
            loanCoefficient = 341; //固定系数
        }
    }

    @OnClick({R.id.common_title_back, R.id.loan_calculator_ok, R.id.loan_calculator_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.loan_calculator_ok:
                break;
            case R.id.loan_calculator_cancel:
                finish();
                break;
        }
    }
}
