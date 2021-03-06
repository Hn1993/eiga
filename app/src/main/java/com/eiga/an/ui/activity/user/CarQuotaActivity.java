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
 * Created by ASUS on 2018/3/21.
 * 汽车贷款评估界面
 */

public class CarQuotaActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.carquota_tv_price)
    TextView carquotaTvPrice;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carquota);
        setImmersedNavigationBar(this,R.color.white);
        ButterKnife.bind(this);
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("我的贷款额度");
    }

    @OnClick({R.id.common_title_back, R.id.carquota_tv_recommit, R.id.ac_carquota_evaluation,
            R.id.carquota_tv_idcard, R.id.carquota_tv_phone, R.id.carquota_tv_bank})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.carquota_tv_recommit:
                intent=new Intent(CarQuotaActivity.this,InfoCollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ac_carquota_evaluation:
                break;
            case R.id.carquota_tv_idcard:
                intent=new Intent(CarQuotaActivity.this,IdCardVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_phone:
                intent=new Intent(CarQuotaActivity.this,PhoneVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_bank:
                intent=new Intent(CarQuotaActivity.this,BankVerifyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
