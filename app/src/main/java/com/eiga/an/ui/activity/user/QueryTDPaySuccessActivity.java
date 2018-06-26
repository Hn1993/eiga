package com.eiga.an.ui.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetTDReportModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
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

public class QueryTDPaySuccessActivity extends BaseActivity {

    private int TIME = 15000;//定时的时间间隔
    private Handler handler = new Handler();

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;

    @BindView(R.id.pay_success_waiting)
    TextView paySuccessWaiting;
    @BindView(R.id.pay_success_commit)
    TextView paySuccessCommit;
    @BindView(R.id.pay_success_look)
    TextView paySuccessLook;

    @BindView(R.id.pay_success_msg)
    TextView paySuccessMsg;
    @BindView(R.id.pay_success_image)
    ImageView paySuccessImage;
    private String TAG = getClass().getName();

    private Context mContext;


    private String isHaveQueryTd;

    private AlertDialog.Builder mDialog;

    private String webUrl;
    private String token, phone;


    private int payStatus = 0;//0,成功  1,支付成功  但是验证不成功  2,支付成功  报告正在生成

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_pay_success);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext = this;
        commonTitleTv.setText("报告结果");
        isHaveQueryTd = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd, "");
        token = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Name, "");
        mDialog = new AlertDialog.Builder(this);

        httpGetTdReport();

    }

    private void httpGetTdReport() {
        Log.e(TAG,"httpGetTdReport");
        handler.postDelayed(runnable, TIME); //延迟加载
        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_Td_Report, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiGetTDReportModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiGetTDReportModel.class);
                        if (model.Status == 1) {
                            //setHttpData(model.Data);
                            if (model.Success) {//同盾 + 身份验证成功
                                paySuccessWaiting.setVisibility(View.GONE);
                                paySuccessCommit.setVisibility(View.VISIBLE);

                                payStatus = 0;
                                getPayStatusAndDoNext(payStatus);

                            }
                        } else if (model.Status == 2) {
                            if (!model.Success) {
                                if (!TextUtils.isEmpty(model.ErrorMsg)) { // 支付成功  但是验证不成功
                                    payStatus = 1;
                                } else {//支付成功  报告正在生成
                                    payStatus = 2;

                                    handler.postDelayed(runnable, TIME); //每隔15s执行  重新请求

                                }

//                                Intent intent=new Intent(QueryTDPaySuccessActivity.this,QueryTDInfoActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                            getPayStatusAndDoNext(payStatus);
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
     * 根据  支付的状态是处理下一步的东西
     *
     * @param payStatus
     */
    private void getPayStatusAndDoNext(int payStatus) {
        Log.e(TAG,"payStatus="+payStatus);
        if (payStatus == 0) {
            paySuccessWaiting.setVisibility(View.GONE);
            paySuccessCommit.setVisibility(View.VISIBLE);
            paySuccessCommit.setText("查看信用报告");
            paySuccessLook.setText("去选择贷款产品");
            paySuccessLook.setVisibility(View.VISIBLE);

            paySuccessImage.setImageResource(R.mipmap.icon_pay_success);
            paySuccessMsg.setText("支付成功");
        } else if (payStatus == 1) {
            paySuccessWaiting.setVisibility(View.GONE);
            paySuccessCommit.setText("重新提交资料");
            paySuccessCommit.setVisibility(View.VISIBLE);
            paySuccessLook.setText("去选择贷款产品");
            paySuccessLook.setVisibility(View.GONE);

            paySuccessMsg.setText("资料验证失败");
            paySuccessImage.setImageResource(R.mipmap.icon_pay_fail);

        } else if (payStatus == 2){
            paySuccessImage.setImageResource(R.mipmap.icon_pay_success);
            paySuccessMsg.setText("支付成功");

            paySuccessWaiting.setVisibility(View.VISIBLE);
            paySuccessCommit.setText("查看信用报告");
            paySuccessCommit.setVisibility(View.GONE);
            paySuccessLook.setText("去选择贷款产品");
            paySuccessLook.setVisibility(View.VISIBLE);
        }

    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //needLoopFunction();
            httpGetTdReport();
        }
    };

    private void needLoopFunction() {
        Log.e("TAG", "====" + System.currentTimeMillis());
        handler.postDelayed(runnable, TIME); //延迟加载

    }


    @OnClick({R.id.common_title_back, R.id.pay_success_commit, R.id.pay_success_waiting, R.id.pay_success_look})
    public void onViewClicked(View view) {
        Intent intent = null;
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
            case R.id.pay_success_commit:
                //intent=new Intent(mContext,)
//                SharedPreferencesUtils.putShared(mContext, Constant.User_Is_Have_QueryTd, "yes");
//                webUrl = Constant.H5_TD_Report + "?CellPhone=" + phone + "&Token=" + token;
//                intent = new Intent(mContext, TDReportWebActivity.class);
//                intent.putExtra(Constant.WebUrl, webUrl);
//                intent.putExtra(Constant.WebTitle, "查看信用报告");
//                startActivity(intent);
//                finish();

                if (payStatus == 0) {//查看同盾报告
                    intent = new Intent(mContext, TDReportWebActivity.class);
                    startActivity(intent);
                } else if (payStatus == 1) {//重新提交资料
                    intent = new Intent(mContext, QueryTDInfoActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.pay_success_waiting:
                break;
            case R.id.pay_success_look:
                intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

}
