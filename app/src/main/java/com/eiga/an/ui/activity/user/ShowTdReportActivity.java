package com.eiga.an.ui.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiHisReportModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.CircularProgressbarView;
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
 * Created by ASUS on 2018/6/21.
 */

public class ShowTdReportActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.td_report_progress)
    CircularProgressbarView tdReportProgress;
    @BindView(R.id.td_report_info_name)
    TextView tdReportInfoName;
    @BindView(R.id.td_report_info_marriage)
    TextView tdReportInfoMarriage;
    @BindView(R.id.td_report_info_phone)
    TextView tdReportInfoPhone;
    @BindView(R.id.td_report_info_idcard)
    TextView tdReportInfoIdcard;
    @BindView(R.id.td_report_info_report_time)
    TextView tdReportInfoReportTime;
    private String TAG = getClass().getName();


    private String TdReportId;

    private String token, phone;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_report);
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        autoVirtualKeys();
        TdReportId=getIntent().getStringExtra(Constant.User_Td_id);
        mContext=this;
        token = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Name, "");

        findViews();


        httpGetTdReport();
    }

    private void httpGetTdReport() {

        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_Td_Report_Json, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);

        if (TextUtils.isEmpty(TdReportId)){
            mStringRequest.add("ReportType", 0);
        }else {
            mStringRequest.add("ReportType", 0);
            mStringRequest.add("ReportId", TdReportId);
        }

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    Log.e(TAG,"response="+response.get());
                    ApiHisReportModel model = null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiHisReportModel.class);
                        if(model.Status==1){
                            //setAdapterData(model);
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

    private void findViews() {
        commonTitleTv.setText("查看信用报告");
        tdReportProgress.setProgress(80);
    }

    @OnClick(R.id.common_title_back)
    public void onViewClicked() {
        finish();
    }
}
