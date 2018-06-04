package com.eiga.an.ui.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/30.
 */

public class QueryTDPaySuccessActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    private String TAG = getClass().getName();

    private Context mContext;


    private String isHaveQueryTd;

    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_pay_success);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext = this;
        commonTitleTv.setText("报告结果");
        isHaveQueryTd= (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd,"");

        mDialog=new android.app.AlertDialog.Builder(this);


    }


    @OnClick({R.id.common_title_back, R.id.pay_success_commit})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                if (TextUtils.isEmpty(isHaveQueryTd)){
                    mDialog
                            //.setTitle("删除订单？")//设置对话框标题
                            .setMessage("确定要退出 咖喱分期？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }else {
                    finish();
                }
                break;
            case R.id.pay_success_commit:
                //intent=new Intent(mContext,)
                SharedPreferencesUtils.putShared(mContext, Constant.User_Is_Have_QueryTd,"yes");
                intent=new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
