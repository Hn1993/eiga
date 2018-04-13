package com.eiga.an.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.sales.SalesLoginActivity;
import com.eiga.an.ui.activity.sales.SalesMainActivity;
import com.eiga.an.ui.activity.user.MainActivity;
import com.eiga.an.utils.SharedPreferencesUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/10.
 */

public class ChooseIdentityActivity extends BaseActivity {
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_id);
        ButterKnife.bind(this);
        autoVirtualKeys();//华为等底部虚拟按键的手机
    }

    @OnClick({R.id.choose_id_user, R.id.choose_id_sales})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.choose_id_user:
                intent=new Intent(ChooseIdentityActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.putShared(ChooseIdentityActivity.this, Constant.IsFirstOpenApp,"0");//0 - user
                finish();
                break;
            case R.id.choose_id_sales:
                intent=new Intent(ChooseIdentityActivity.this, SalesLoginActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.putShared(ChooseIdentityActivity.this, Constant.IsFirstOpenApp,"1");//1 - sales
                finish();
                break;
        }
    }
}