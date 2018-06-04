package com.eiga.an.ui.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/30.
 */

public class QueryTDPayActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView salesTitleTv;

    @BindView(R.id.common_title_back)
    RelativeLayout salesTitleBack;
    private String TAG = getClass().getName();

    private BottomSheetDialog bottomSheetDialog;
    private Context mContext;
    private Intent intent=null;

    private String isHaveQueryTd;

    private AlertDialog.Builder mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_pay);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext=this;
        bottomSheetDialog=new BottomSheetDialog(this);

        isHaveQueryTd= (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd,"");

        mDialog=new android.app.AlertDialog.Builder(this);
        salesTitleTv.setText("支付确认");
    }

    /**
     * 弹出选择框
     */
    private void showBottomDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_pay, null);
        view.findViewById(R.id.activity_pay_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtils.toast(mContext,"微信支付");
                intent=new Intent(mContext,QueryTDPaySuccessActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
                finish();

            }
        });
        view.findViewById(R.id.activity_pay_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtils.toast(mContext,"支付宝支付");
                intent=new Intent(mContext,QueryTDPaySuccessActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
                finish();

            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @OnClick({R.id.common_title_back, R.id.querytd_pay_commit})
    public void onViewClicked(View view) {
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
            case R.id.querytd_pay_commit:
                showBottomDialog();
                break;
        }
    }
}