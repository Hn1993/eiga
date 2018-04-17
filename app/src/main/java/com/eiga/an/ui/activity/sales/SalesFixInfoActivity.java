package com.eiga.an.ui.activity.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/16.
 */

public class SalesFixInfoActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.fix_head)
    ImageView fixHead;
    @BindView(R.id.fix_et_name)
    EditText fixEtName;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fixinfo);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        findViews();
    }

    private void findViews() {
        //GlideUtils.getGlideUtils().glideCircleImage(FixInfoActivity.this,"",fixHead);
        commonTitleTv.setText("修改资料");
        fixHead.setBackground(getResources().getDrawable(R.mipmap.icon_default_head));
    }

    @OnClick({R.id.common_title_back, R.id.fix_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.fix_commit:
                break;
        }
    }
}
