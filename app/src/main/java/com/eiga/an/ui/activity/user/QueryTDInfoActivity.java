package com.eiga.an.ui.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/30.
 */

public class QueryTDInfoActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.common_title_back)
    RelativeLayout salesTitleBack;
    @BindView(R.id.query_td_et_name)
    EditText queryTdEtName;
    @BindView(R.id.query_td_et_phone)
    EditText queryTdEtPhone;
    @BindView(R.id.query_td_et_idcard)
    EditText queryTdEtIdcard;
    @BindView(R.id.query_td_cb)
    CheckBox queryTdCb;
    private String TAG = getClass().getName();

    private Context mContext;
    private String isHaveQueryTd;

    private AlertDialog.Builder mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_info);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext=this;
        isHaveQueryTd= (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd,"");

        mDialog=new android.app.AlertDialog.Builder(this);

        findViews();
    }

    private void findViews() {
        //salesTitleBack.setVisibility(View.GONE);
        salesTitleTv.setText("基本信息");

    }

    @OnClick({R.id.common_title_back, R.id.query_td_commit, R.id.query_td_cb, R.id.query_td_cb_layout})
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
            case R.id.query_td_commit:
                if (TextUtils.isEmpty(queryTdEtName.getText().toString())){
                    PhoneUtils.toast(mContext,"亲,真实姓名不能为空哦~");
                }else {
                    if (!TextUtils.isEmpty(queryTdEtPhone.getText().toString())&&PhoneUtils.isMobile(queryTdEtPhone.getText().toString())){
                        if (!TextUtils.isEmpty(queryTdEtIdcard.getText().toString())&&queryTdEtIdcard.getText().toString().length()==18){
                            if (queryTdCb.isChecked()){
                                httpUploadInfo();
                            }else {
                                PhoneUtils.toast(mContext,"亲,请先勾选同意数据分析免责协议~");
                            }
                        }else {
                            PhoneUtils.toast(mContext,"亲,请填写正确的身份证号~");
                        }
                    }else {
                        PhoneUtils.toast(mContext,"亲,请填写正确的手机号码~");
                    }
                }
                break;
            case R.id.query_td_cb:
                break;
            case R.id.query_td_cb_layout:
                break;
        }
    }

    private void httpUploadInfo() {
        Intent intent=new Intent(mContext,QueryTDPayActivity.class);
        startActivity(intent);
        finish();
    }


    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
//    private long mExitTime;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //判断用户是否点击了“返回键”
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //与上次点击返回键时刻作差
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                //大于2000ms则认为是误操作，使用Toast进行提示
//                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                //并记录下本次点击“返回键”的时刻，以便下次进行判断
//                mExitTime = System.currentTimeMillis();
//            } else {
//                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
