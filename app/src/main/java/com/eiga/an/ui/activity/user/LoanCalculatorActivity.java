package com.eiga.an.ui.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

/**
 * Created by ASUS on 2018/7/4.
 */

public class LoanCalculatorActivity extends BaseActivity {

    private String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);


    }
}
