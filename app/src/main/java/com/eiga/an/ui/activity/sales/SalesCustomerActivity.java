package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerListModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
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
 * Created by ASUS on 2018/4/26.
 * 客户管理
 */

public class SalesCustomerActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.sales_customer_rv)
    XRecyclerView mRecyclerView;
    private String TAG = getClass().getName();
    private Context context;

    private String salesPhone, salesToken;

    private RecyclerAdapter<ApiSalesCustomerListModel.DataBean> mAdapter;
    private List<ApiSalesCustomerListModel.DataBean> mData=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_customer);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        context = this;

        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");
        findViews();
        httpGetCustomerList();
    }

    //获取列表
    private void httpGetCustomerList() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Customer, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesCustomerListModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesCustomerListModel.class);
                        if (model.Status == 1) {
                            setHttpData(model.Data);

                        } else {
                            PhoneUtils.toast(context, model.Msg.toString());
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
                PhoneUtils.toast(context, "网络请求失败,请检查网络后重试");
            }
        });

        if(mRecyclerView != null)
            mRecyclerView.refreshComplete();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    private void setHttpData(List<ApiSalesCustomerListModel.DataBean> data) {
        mData=data;
        mAdapter=new RecyclerAdapter<ApiSalesCustomerListModel.DataBean>(R.layout.layout_sales_customer_item,mData) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, final ApiSalesCustomerListModel.DataBean item) {
                ImageView head=viewHolder.findViewById(R.id.sales_customer_item_head);
                TextView name=viewHolder.findViewById(R.id.sales_customer_item_name);
                TextView phone=viewHolder.findViewById(R.id.sales_customer_item_phone);

                GlideUtils.getGlideUtils().glideCircleImage(SalesCustomerActivity.this,Constant.Url_Common+item.HeadSculpture,head);
                name.setText("姓名:"+item.RealName);
                phone.setText("电话:"+item.CellPhone);

                viewHolder.findViewById(R.id.sales_customer_item_signing).
                        setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        Intent intent=new Intent(SalesCustomerActivity.this,SalesSigningActivity.class);
                        intent.putExtra(Constant.Sales_User_Id,item.UserId);
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.refresh();
    }

    private void findViews() {
        commonTitleTv.setText("客户管理");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(30));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);

        mRecyclerView.getDefaultFootView().setLoadingHint("加载中...");
        mRecyclerView.getDefaultFootView().setNoMoreHint("加载完成");
        mRecyclerView.setLimitNumberToCallLoadMore(10);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(true);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                httpGetCustomerList();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }


    @OnClick(R.id.common_title_back)
    public void onViewClicked() {
        finish();
    }
}
