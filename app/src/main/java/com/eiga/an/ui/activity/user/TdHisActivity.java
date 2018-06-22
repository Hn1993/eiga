package com.eiga.an.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetTDReportModel;
import com.eiga.an.model.jsonModel.ApiHisReportModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
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
 * Created by ASUS on 2018/6/22.
 */

public class TdHisActivity extends BaseActivity {

    private String TAG=getClass().getName();

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.td_his_rv)
    RecyclerView mRecyclerView;
    private Context mContext;

    private String token, phone;

    private RecyclerAdapter<ApiHisReportModel.ReportListBean> mAdapter;
    private List<ApiHisReportModel.ReportListBean> mData=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_his);
        autoVirtualKeys();
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        mContext = this;
        token = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(mContext, Constant.User_Login_Name, "");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
        commonTitleTv.setText("历史信用记录");
        httpGetTdReportHis();
    }

    private void httpGetTdReportHis() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_Td_Report_His, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
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
                            setAdapterData(model);
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

    private void setAdapterData(ApiHisReportModel model) {
        Log.e(TAG,"setAdapterData="+model.ReportList.size());
        mData=model.ReportList;
        mAdapter=new RecyclerAdapter<ApiHisReportModel.ReportListBean>(R.layout.layout_tdhis_item,model.ReportList) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, ApiHisReportModel.ReportListBean item) {
                TextView tv_id=viewHolder.findViewById(R.id.tdhis_id);
                TextView tv_name=viewHolder.findViewById(R.id.tdhis_name);
                TextView tv_time=viewHolder.findViewById(R.id.tdhis_time);

                tv_id.setText("报告编号:"+item.ReportId);
                tv_name.setText(item.RealName);
                tv_time.setText("查询日期:"+item.ReportDate);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TdHisActivity.this,ShowTdReportActivity.class);
                intent.putExtra(Constant.User_Td_id,mData.get(position).ReportId);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.common_title_back)
    public void onViewClicked() {
        finish();
    }
}
