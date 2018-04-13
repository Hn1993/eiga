package com.eiga.an.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.utils.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.albert.autosystembar.SystemBarHelper;

/**
 * Created by ASUS on 2018/4/13.
 */

public class UserMainActivity extends BaseActivity {
    @BindView(R.id.user_main_et_phone)
    EditText userMainEtPhone;
    @BindView(R.id.user_main_et_code)
    EditText userMainEtCode;
    @BindView(R.id.user_main_et_recommend)
    EditText userMainEtRecommend;
    @BindView(R.id.fg_main_tv_go)
    TextView fgMainTvGo;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        ButterKnife.bind(this);
        new SystemBarHelper.Builder()
                .enableImmersedStatusBar(true)
                .enableImmersedNavigationBar(true)
                .into(this);
        autoVirtualKeys();


        findViews();
    }

    private void findViews() {

    }

    @OnClick({R.id.user_main_tv_getcode, R.id.fg_main_tv_go})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.user_main_tv_getcode:
                break;
            case R.id.fg_main_tv_go:
                intent=new Intent(UserMainActivity.this, InfoCollectionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
