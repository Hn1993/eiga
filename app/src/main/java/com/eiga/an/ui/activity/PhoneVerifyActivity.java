package com.eiga.an.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        setImmersedNavigationBar(this,R.color.white);
        autoVirtualKeys();
        ButterKnife.bind(this);
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("运营商认证");
    }

    @OnClick({R.id.common_title_back, R.id.phone_verify_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.phone_verify_commit:
                finish();
                break;
        }
    }
}
