package com.eiga.an.ui.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiQueryInfoModel;
import com.eiga.an.model.jsonModel.EventBusBankResultModel;
import com.eiga.an.model.jsonModel.EventBusIDCardResultModel;
import com.eiga.an.ui.activity.WebActivity;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

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

    @BindView(R.id.carquota_tv_phone)
    TextView carquotaTvPhone;
    @BindView(R.id.carquota_tv_idcard)
    TextView carquotaTvIdcard;
    @BindView(R.id.carquota_tv_bank)
    TextView carquotaTvBank;
    @BindView(R.id.query_td_cb_layout)
    LinearLayout queryTdCbLayout;
    @BindView(R.id.query_td_commit)
    TextView queryTdCommit;

    private String TAG = getClass().getName();

    private Context mContext;
    private String isHaveQueryTd;

    private AlertDialog.Builder mDialog;
    private String token, phone;

    private String phoneNumber,realName,bankName,bankCard,frontPhoto,backPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querytd_info);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        EventBus.getDefault().register(this);
        mContext = this;
        isHaveQueryTd = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Is_Have_QueryTd, "");
        token = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Name, "");

        mDialog = new AlertDialog.Builder(this);

        findViews();
    }

    private void findViews() {
        //salesTitleBack.setVisibility(View.GONE);
        salesTitleTv.setText("基本信息");

    }

    @Subscriber (tag = "upload_idcard")
    public void getIdCardInfo(EventBusIDCardResultModel model){
        Log.e(TAG,"upload_idcard="+model.toString());
        carquotaTvIdcard.setText("添加成功");
        carquotaTvIdcard.setBackground(getResources().getDrawable(R.drawable.button_selector_radius_2px));
        frontPhoto=model.frontBase64;
        backPhoto=model.backBase64;
    };

    @Subscriber (tag = "upload_bank")
    public void getBankInfo(EventBusBankResultModel model){
        Log.e(TAG,"upload_bank="+model.toString());
        carquotaTvBank.setText("添加成功");
        carquotaTvBank.setBackground(getResources().getDrawable(R.drawable.button_selector_radius_2px));
        realName=model.bankUserName;
        bankCard=model.bankCard;
        bankName=model.bankName;
    };

    @Subscriber (tag = "upload_phone")
    public void getPhoneInfo(String phone){
        Log.e(TAG,"upload_phone="+phone);
        carquotaTvPhone.setText("添加成功");
        carquotaTvPhone.setBackground(getResources().getDrawable(R.drawable.button_selector_radius_2px));
        phoneNumber=phone;
    };


    @OnClick({R.id.common_title_back, R.id.query_td_commit, R.id.query_td_cb,
            R.id.query_td_cb_layout,R.id.carquota_tv_phone, R.id.carquota_tv_idcard, R.id.carquota_tv_bank})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.common_title_back:
                if (TextUtils.isEmpty(isHaveQueryTd)) {
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
                } else {
                    finish();
                }

                break;
            case R.id.query_td_commit:
                if (carquotaTvPhone.getText().toString().equals("添加成功")
                        &&carquotaTvIdcard.getText().toString().equals("添加成功")
                        &&carquotaTvBank.getText().toString().equals("添加成功")&&queryTdCb.isChecked()) {
                    httpUploadInfo();

                }else {
                    PhoneUtils.toast(mContext, "亲,请先添加个人信息并勾选免责协议~");
                }


//                else {
//                    Log.e(TAG,"carquotaTvIdcard.getText().toString()="+carquotaTvIdcard.getText().toString());
//                    if (!carquotaTvIdcard.getText().toString().equals("添加成功")) {
//                        if (!carquotaTvBank.getText().toString().equals("添加成功")) {
//                            if (queryTdCb.isChecked()) {
//                                httpUploadInfo();
//                            } else {
//                                PhoneUtils.toast(mContext, "亲,请先勾选同意数据分析免责协议~");
//                            }
//                        } else {
//                            PhoneUtils.toast(mContext, "亲,请先添加银行卡信息~");
//                        }
//                    } else {
//                        PhoneUtils.toast(mContext, "亲,请先添加身份证照片信息~");
//                    }
//                }
                break;
            case R.id.query_td_cb:
                break;
            case R.id.query_td_cb_layout:
                String url=Constant.H5_Eiga_Protocol+"1";
                intent=new Intent(mContext, WebActivity.class);
                intent.putExtra(Constant.WebUrl,url);
                intent.putExtra(Constant.WebTitle,"数据分析免责协议");
                startActivity(intent);
                break;

            case R.id.carquota_tv_phone:
                intent=new Intent(QueryTDInfoActivity.this,PhoneVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_idcard:
                intent=new Intent(QueryTDInfoActivity.this,IdCardVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_bank:
                intent=new Intent(QueryTDInfoActivity.this,BankVerifyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void httpUploadInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Query_Info, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        mStringRequest.add("RealName", realName);
        mStringRequest.add("BankCard", bankCard);
        mStringRequest.add("BankName", bankName);
        mStringRequest.add("FrontBase64", new FileBinary(new File(frontPhoto)));
        mStringRequest.add("BackBase64", new FileBinary(new File(backPhoto)));
        mStringRequest.add("VaildateCellPhone", phoneNumber);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    Log.e(TAG, response.get());
                    ApiQueryInfoModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiQueryInfoModel.class);
                        if (model.Status == 1) {
                            //setHttpData(model.Data);
                            if (model.InvokePay){//提交资料或者再次提交资料  如果之前没支付的  InvokePay=true
                                Intent intent = new Intent(mContext, QueryTDPayActivity.class);
                                startActivity(intent);
                            }else {//已经支付过的   直接跳到支付成功界面
                                Intent intent = new Intent(mContext, QueryTDPaySuccessActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }else if(model.Status==2){
                            if (model.InvokePay){//提交资料或者再次提交资料  如果之前没支付的  InvokePay=true
                                Intent intent = new Intent(mContext, QueryTDPayActivity.class);
                                startActivity(intent);
                            }else {//已经支付过的   直接跳到支付成功界面
                                Intent intent = new Intent(mContext, QueryTDPaySuccessActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                        PhoneUtils.toast(mContext, model.Msg.toString());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
