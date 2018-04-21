package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_login);
        ButterKnife.bind(this);
        findViews();
    }

    private void findViews() {

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
                    SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.Sales_Login_Name, salesLoginName.getText().toString());
                    intent=new Intent(SalesLoginActivity.this,SalesMainActivity.class);
                    startActivity(intent);
                    finish();
                }


                break;
            case R.id.sales_login_forgot:
                break;
        }
    }
}
