package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.ui.activity.user.ChooseIdentityActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/16.
 */

public class SalesSettingActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        findViews();
    }


    private void findViews() {
        commonTitleTv.setText("设置");
    }

    @OnClick({R.id.common_title_back, R.id.setting_user_info, R.id.setting_change_psw, R.id.setting_about, R.id.setting_change_id})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.setting_user_info:
                intent=new Intent(this,SalesFixInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_change_psw:
                intent=new Intent(this,SalesFixPswActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_about:
//                intent=new Intent(this,AboutActivity.class);
//                startActivity(intent);
                break;
            case R.id.setting_change_id:
                intent=new Intent(this,ChooseIdentityActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
