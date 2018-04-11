package com.eiga.an.ui.activity.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

/**
 * Created by ASUS on 2018/4/10.
 */

public class SalesLoginActivity extends BaseActivity {
    private String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_main);
    }

}
