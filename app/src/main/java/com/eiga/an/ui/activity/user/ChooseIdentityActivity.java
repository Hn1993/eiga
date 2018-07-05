package com.eiga.an.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.sales.SalesMainActivity;
import com.eiga.an.utils.ActivityManager;
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
        setImmersedNavigationBar(this,R.color.light_blue);
        //保留DActivity,其余全部关闭
        ActivityManager.getInstance().finishAllActivityByWhitelist(ChooseIdentityActivity.class);
    }

    @OnClick({R.id.choose_id_user, R.id.choose_id_sales})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.choose_id_user:
                intent=new Intent(ChooseIdentityActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.putShared(ChooseIdentityActivity.this, Constant.IsFirstOpenApp,"0");//0 - user
                //finish();
                break;
            case R.id.choose_id_sales:
                intent=new Intent(ChooseIdentityActivity.this, SalesMainActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.putShared(ChooseIdentityActivity.this, Constant.IsFirstOpenApp,"1");//1 - sales
                //finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Log.e(TAG,"key_back");
            SharedPreferencesUtils.clearSp(ChooseIdentityActivity.this,Constant.IsFirstOpenApp); //清除这里的值
        }

        return super.onKeyDown(keyCode, event);
    }
}
