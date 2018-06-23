package com.eiga.an.ui.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiShowTdReportModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.CircularProgressbarView;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;

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

    //信贷逾期
    @BindView(R.id.td_report_xindai_num)
    TextView tdReportXindaiNum; // 信贷平台数量
    @BindView(R.id.td_report_xindai_overtime_num)
    TextView tdReportXindaiOvertimeNum; // 信贷逾期次数
    @BindView(R.id.td_report_xindai_layout)
    RelativeLayout tdReportXindaiLayout;
    @BindView(R.id.td_report_xindai_no_risk)
    RelativeLayout tdReportXindaiNoRisk;

    //多头借贷
    @BindView(R.id.td_report_duotoujiedai_7days)
    TextView tdReportDuotoujiedai7days;
    @BindView(R.id.td_report_duotoujiedai_30days)
    TextView tdReportDuotoujiedai30days;
    @BindView(R.id.td_report_duotoujiedai_90days)
    TextView tdReportDuotoujiedai90days;
    @BindView(R.id.td_report_duotoujiedai_layout)
    RelativeLayout tdReportDuotoujiedaiLayout;
    @BindView(R.id.td_report_duotoujiedai_no_risk)
    RelativeLayout tdReportDuotoujiedaiNoRisk;

    // 法院风险检测
    @BindView(R.id.td_report_fengxian_layout)
    RelativeLayout tdReportFengxianLayout;
    @BindView(R.id.td_report_fengxian_no_risk)
    RelativeLayout tdReportFengxianNoRisk;
    @BindView(R.id.td_report_fengxian_shixin)
    TextView tdReportFengxian_shixin;
    @BindView(R.id.td_report_fengxian_zhixing)
    TextView tdReportFengxian_zhixing;
    @BindView(R.id.td_report_fengxian_jiean)
    TextView tdReportFengxian_jiean;

    private String TAG = getClass().getName();


    private String TdReportId;

    private String token, phone;
    private Context mContext;

    private ApiShowTdReportModel.ReportContentBean.ResultDescBean.ANTIFRAUDBean mAntfraud;

    //信贷列表 ---> TD 信贷逾期记录
    private List<ApiShowTdReportModel.ReportContentBean.ResultDescBean.ANTIFRAUDBean.
            RiskItemsBean.RiskDetailBean> mXinDaiList = new ArrayList<>();
    //多平台贷款
    private List<ApiShowTdReportModel.ReportContentBean.ResultDescBean.ANTIFRAUDBean.
            RiskItemsBean.RiskDetailBean> mDuotoujiedaiList = new ArrayList<>();
    //风险行为检测
    private List<ApiShowTdReportModel.ReportContentBean.ResultDescBean.ANTIFRAUDBean.
            RiskItemsBean.RiskDetailBean> mFengxianList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_report);
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        autoVirtualKeys();
        TdReportId = getIntent().getStringExtra(Constant.User_Td_id);
        mContext = this;
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

        if (TextUtils.isEmpty(TdReportId)) {
            mStringRequest.add("ReportType", 0);
        } else {
            mStringRequest.add("ReportType", 0);
            mStringRequest.add("ReportId", TdReportId);
        }

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 2000, TAG);
                    //Log.e(TAG,"response="+response.get());
                    ApiShowTdReportModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiShowTdReportModel.class);
                        if (model.Status == 1) {
                            setHttpData(model);
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

    private void setHttpData(ApiShowTdReportModel model) {
        mAntfraud = model.ReportContent.result_desc.ANTIFRAUD;
        tdReportProgress.setProgress(mAntfraud.final_score);//最终决策分

        tdReportInfoPhone.setText("手机号码: " + model.VaildateCellPhone);
        tdReportInfoReportTime.setText("报告时间: " + model.ReportDate);
        tdReportInfoIdcard.setText("身份证号: " + model.IdentityId);
        tdReportInfoName.setText(model.RealName);
        tdReportInfoMarriage.setVisibility(View.GONE);

        for (int i = 0; i < mAntfraud.risk_items.size(); i++) {
            //信贷记录  (TD --> 信贷逾期统计)
            if (mAntfraud.risk_items.get(i).risk_detail.get(0).type.equals("discredit_count")) {
                mXinDaiList.add(mAntfraud.risk_items.get(i).risk_detail.get(0));
            }

            //多平台借贷
            if (mAntfraud.risk_items.get(i).risk_detail.get(0).type.equals("platform_detail")) {
                mDuotoujiedaiList.add(mAntfraud.risk_items.get(i).risk_detail.get(0));
            }

            //行为风险检测
            if (mAntfraud.risk_items.get(i).risk_detail.get(0).type.equals("black_list")) {
                mFengxianList.add(mAntfraud.risk_items.get(i).risk_detail.get(0));
            }




        }

        //信贷记录  (TD --> 信贷逾期统计)
        if (mXinDaiList.size() == 0) { //无信贷逾期记录
            tdReportXindaiNoRisk.setVisibility(View.VISIBLE);
            tdReportXindaiLayout.setVisibility(View.GONE);
        } else {
            tdReportXindaiNoRisk.setVisibility(View.GONE);
            tdReportXindaiLayout.setVisibility(View.VISIBLE);

            tdReportXindaiNum.setText(mXinDaiList.get(0).platform_count);//信贷平台个数
            tdReportXindaiOvertimeNum.setText(mXinDaiList.get(0).discredit_times);//信贷逾期次数
        }

        //多平台借贷
        if (mDuotoujiedaiList.size() == 0) {
            tdReportDuotoujiedaiLayout.setVisibility(View.GONE);
            tdReportDuotoujiedaiNoRisk.setVisibility(View.VISIBLE);
        } else {
            tdReportDuotoujiedaiLayout.setVisibility(View.VISIBLE);
            tdReportDuotoujiedaiNoRisk.setVisibility(View.GONE);

            for (int i = 0; i < mDuotoujiedaiList.size(); i++) {
                if (mDuotoujiedaiList.get(i).description.equals("7天内申请人在多个平台申请借款")) {//7天内多头借贷
                    tdReportDuotoujiedai7days.setText("7天内申请借款平台数量: " + mDuotoujiedaiList.get(i).platform_count);
                }

                if (mDuotoujiedaiList.get(i).description.equals("1个月内申请人在多个平台申请借款")) {//30天内多头借贷
                    tdReportDuotoujiedai30days.setText("一个月内申请借款平台数量: " + mDuotoujiedaiList.get(i).platform_count);
                }

                if (mDuotoujiedaiList.get(i).description.equals("3个月内申请人在多个平台申请借款")) {//30天内多头借贷
                    tdReportDuotoujiedai90days.setText("三个月内申请借款平台数量: " + mDuotoujiedaiList.get(i).platform_count);
                }

            }
        }


        //行为风险检测

        if (mFengxianList.size() == 0) {
            tdReportFengxianLayout.setVisibility(View.GONE);
            tdReportFengxianNoRisk.setVisibility(View.VISIBLE);
        } else {
            tdReportFengxianLayout.setVisibility(View.GONE);
            tdReportFengxianNoRisk.setVisibility(View.VISIBLE);

            for (int i = 0; i < mFengxianList.size(); i++) {
                if (mFengxianList.get(i).description.equals("身份证命中法院失信名单")){
                    tdReportFengxian_shixin.setText("是否列入法院失信名单: "+"是");
                }else {
                    tdReportFengxian_shixin.setText("是否列入法院失信名单: "+"否");
                }

                if (mFengxianList.get(i).description.equals("身份证命中法院执行名单")){
                    tdReportFengxian_shixin.setText("是否列入法院执行名单: "+"是");
                }else {
                    tdReportFengxian_shixin.setText("是否列入法院执行名单: "+"否");
                }

                if (mFengxianList.get(i).description.equals("身份证命中法院结案名单")){
                    tdReportFengxian_shixin.setText("是否列入法院结案名单: "+"是");
                }else {
                    tdReportFengxian_shixin.setText("是否列入法院结案名单: "+"否");
                }
            }
        }



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
