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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.alipay.Alipay;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiQueryToPayModel;
import com.eiga.an.model.jsonModel.ApiWxPayModel;
import com.eiga.an.model.salesModel.ApiSalesCustomerListModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

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
    private String token,phone;

    private int payType=0;

    private ApiWxPayModel mWxParams;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_pay);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext=this;
        bottomSheetDialog=new BottomSheetDialog(this);
        token= (String) SharedPreferencesUtils.getShared(mContext,Constant.User_Login_Token,"");
        phone= (String) SharedPreferencesUtils.getShared(mContext,Constant.User_Login_Name,"");
        isHaveQueryTd= (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd,"");

        mDialog=new android.app.AlertDialog.Builder(this);
        salesTitleTv.setText("支付确认");


    }

    /**
     *
     * @param payType  == 1 ali, 2 wx
     */
    private void httpGetPayParams(final int payType) {

        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Query_Pay, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        mStringRequest.add("PayType", payType);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiQueryToPayModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiQueryToPayModel.class);
                        if (model.Status == 1) {
                            //setHttpData(model.Data);
                            if (payType==1){
                                //String str=model.body.replace("&amp;","&");
                                Log.e(TAG,"str="+model.body);
                                new Alipay(mContext, model.body, new Alipay.AlipayResultCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e(TAG,"支付成功");
                                        SharedPreferencesUtils.putShared(mContext, Constant.User_Is_Have_QueryTd,"yes");
                                        intent= new Intent(mContext,QueryTDPaySuccessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onDealing() {

                                    }
                                    @Override
                                    public void onError(int error_code) {
                                        Log.e(TAG,"支付失败");
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.e(TAG,"支付取消");
                                        SharedPreferencesUtils.putShared(mContext, Constant.User_Is_Have_QueryTd,"yes");
                                        intent= new Intent(mContext,QueryTDPaySuccessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).doPay();
                            }else if (payType==2){
                                mWxParams.appid=model.appid;
                                mWxParams.noncestr=model.noncestr;
                                mWxParams.packageX=model.packageX;
                                mWxParams.partnerid=model.partnerid;
                                mWxParams.sign=model.sign;
                                mWxParams.timestamp=model.timestamp;
                                mWxParams.prepayid=model.prepayid;


                            }




                        } else {
                            PhoneUtils.toast(mContext, model.Msg.toString());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception=" + e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(mContext, "网络请求失败,请检查网络后重试");
            }
        });
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
//                intent=new Intent(mContext,QueryTDPaySuccessActivity.class);
//                startActivity(intent);
                bottomSheetDialog.dismiss();
                payType=2;
                httpGetPayParams(payType);

            }
        });
        view.findViewById(R.id.activity_pay_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtils.toast(mContext,"支付宝支付");
                bottomSheetDialog.dismiss();
                payType=1;
                httpGetPayParams(payType);
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @OnClick({R.id.common_title_back, R.id.querytd_pay_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
//                if (TextUtils.isEmpty(isHaveQueryTd)){
//                    mDialog
//                            //.setTitle("删除订单？")//设置对话框标题
//                            .setMessage("确定要退出 咖喱分期？")//设置显示的内容
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//                                    dialog.dismiss();
//                                    finish();
//                                }
//                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {//响应事件
//                            dialog.dismiss();
//                        }
//                    }).show();//在按键响应事件中显示此对话框
//                }else {
//                    finish();
//                }
                finish();
                break;
            case R.id.querytd_pay_commit:
                showBottomDialog();
                break;
        }
    }
}
