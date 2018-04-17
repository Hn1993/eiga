package com.eiga.an.ui.activity.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/17.
 * 修改密码
 */

public class SalesFixPswActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.fix_psw_et)
    EditText fixPswEt;
    @BindView(R.id.fix_psw_et_confirm)
    EditText fixPswEtConfirm;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_psw);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("修改密码");
    }

    @OnClick({R.id.common_title_back, R.id.fix_psw_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.fix_psw_commit:

                break;
        }
    }
}
