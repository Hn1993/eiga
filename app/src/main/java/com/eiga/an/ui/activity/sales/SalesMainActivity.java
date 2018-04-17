package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.utils.SharedPreferencesUtils;

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
    private String TAG = getClass().getName();

    private String salesName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_main);
        ButterKnife.bind(this);

        salesName = (String) SharedPreferencesUtils.getShared(SalesMainActivity.this, Constant.Sales_Login_Name, "");
        if (TextUtils.isEmpty(salesName)) {
            gotoSalesLogin(this, true);
        }

        findViews();

    }

    private void findViews() {
        commonTitleTv.setText("业务员后台");
        commonBack.setVisibility(View.GONE);
    }

    @OnClick({R.id.common_title_back, R.id.sales_main_setting, R.id.sales_main_work, R.id.sales_main_car_work, R.id.sales_main_car_client, R.id.sales_main_changeId})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                break;
            case R.id.sales_main_setting:
                intent =new Intent(SalesMainActivity.this,SalesSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_main_work:
                break;
            case R.id.sales_main_car_work:
                break;
            case R.id.sales_main_car_client:
                break;
            case R.id.sales_main_changeId://退出登录

                break;
        }
    }
}
