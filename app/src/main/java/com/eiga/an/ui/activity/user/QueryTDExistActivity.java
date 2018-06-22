package com.eiga.an.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/30.
 */

public class QueryTDExistActivity extends BaseActivity {


    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.pay_exist_text)
    TextView payExistText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_exist);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);

        commonTitleTv.setText("信用查询");


    }


    @OnClick({R.id.common_title_back, R.id.pay_exist_his, R.id.pay_exist_main})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.pay_exist_his:
                intent=new Intent(QueryTDExistActivity.this,TdHisActivity.class);
                startActivity(intent);
                break;
            case R.id.pay_exist_main:
                finish();
                break;
        }
    }
}
