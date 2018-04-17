package com.eiga.an.ui.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.utils.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/16.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.about_version_info)
    TextView aboutVersionInfo;
    @BindView(R.id.about_version_code)
    TextView aboutVersionCode;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        findViews();
    }


    private void findViews() {
        commonTitleTv.setText("关于");
        aboutVersionInfo.setText("版本号："+ PhoneUtils.getVersionCode(AboutActivity.this));
        aboutVersionCode.setText(PhoneUtils.getVersionCode(AboutActivity.this));
    }

    @OnClick({R.id.common_title_back, R.id.about_protocol, R.id.about_review})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.about_protocol:
                break;
            case R.id.about_review:
                break;
        }
    }
}
