package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerProductModel;
import com.eiga.an.model.salesModel.ApiSalesWorkListModel;
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

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by ASUS on 2018/4/20.
 */

public class SalesWorkActivity extends BaseActivity {
    @BindView(R.id.sales_title_back)
    RelativeLayout salesTitleBack;
    @BindView(R.id.sales_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.sales_title_notice)
    RelativeLayout salesTitleNotice;
    @BindView(R.id.sales_work_tab)
    TabLayout salesWorkTab;
    @BindView(R.id.sales_work_rv)
    XRecyclerView mRecyclerView;
    @BindView(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    private String TAG = getClass().getName();


    private RecyclerAdapter<ApiSalesWorkListModel.DataBean> mAdapter;
    private String salesPhone, salesToken;

    private Context context;
    private int status=0,offset=0,limit=10;
    List<ApiSalesWorkListModel.DataBean> data=new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_work);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);
        EventBus.getDefault().register(this);

        context = this;
        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");
        findViews();
        httpGetSalesWorkList(true,-1);
    }

    /**
     *
     * @param isShowLoading
     * @param moreStatus  用来判断是加载更多还是下拉刷新   0刷新  1加载  -1 不做任何处理
     */
    private void httpGetSalesWorkList(boolean isShowLoading, final int moreStatus) {
        if (isShowLoading){
            showLoading();
        }
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Work_Order_List, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("Status", status);
        mStringRequest.add("Offset", offset);
        mStringRequest.add("Limit", limit);
        Log.e(TAG, "salesPhone=" + salesPhone);
        Log.e(TAG, "salesToken=" + salesToken);
        Log.e(TAG, "Status=" + status);
        Log.e(TAG, "Offset=" + offset);
        Log.e(TAG, "Limit=" + limit);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesWorkListModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesWorkListModel.class);
                        if (model.Status == 1) {
                            setAdapterData(model.Data);
                        } else {
                            PhoneUtils.toast(context, model.Msg.toString());
                        }

                        if (moreStatus==0){
                            mRecyclerView.refreshComplete();
                        }else if (moreStatus==1){
                            mRecyclerView.loadMoreComplete();
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
    }

    private void findViews() {

        salesTitleNotice.setVisibility(View.GONE);

        salesTitleTv.setText("工作任务");
        TabLayout.Tab tab;
        tab=salesWorkTab.newTab();
        tab.setCustomView(R.layout.layout_sales_work_tab1);
        salesWorkTab.addTab(tab);

        tab=salesWorkTab.newTab();
        tab.setCustomView(R.layout.layout_sales_work_tab2);
        salesWorkTab.addTab(tab);

//        tab=salesWorkTab.newTab();
//        tab.setCustomView(R.layout.layout_sales_work_tab3);
//        salesWorkTab.addTab(tab);

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
        mRecyclerView.setPullRefreshEnabled(true);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                data.clear();
                offset=0;
                httpGetSalesWorkList(false,0);
            }

            @Override
            public void onLoadMore() {
                offset+=1;
                httpGetSalesWorkList(false,1);
            }
        });



        salesWorkTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    status=0; //正常
                }else if (tab.getPosition()==1){
                    status=3; //被驳回
                }else {
                    //status=5; //已还款

                }
                data.clear();
                offset=0;
                limit=10;
                httpGetSalesWorkList(false,-1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.refreshComplete();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

    }

    private void setAdapterData(final List<ApiSalesWorkListModel.DataBean> list) {
        if (list.size()<=limit){
            mRecyclerView.setLoadingMoreEnabled(false);
        }
        data.clear();
        data.addAll(list);

        mAdapter=new RecyclerAdapter<ApiSalesWorkListModel.DataBean>(R.layout.layout_sales_work_item,data) {

            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, ApiSalesWorkListModel.DataBean item) {
                RelativeLayout layout=viewHolder.findViewById(R.id.sales_work_item_layout);
                ImageView head=viewHolder.findViewById(R.id.sales_work_item_head);
                TextView money=viewHolder.findViewById(R.id.sales_work_item_money);
                TextView order=viewHolder.findViewById(R.id.sales_work_item_order);
                TextView dkje=viewHolder.findViewById(R.id.sales_work_item_dkje);
                TextView tv_status=viewHolder.findViewById(R.id.sales_work_item_status);
                TextView name=viewHolder.findViewById(R.id.sales_work_item_name);
                TextView recommit=viewHolder.findViewById(R.id.sales_work_item_recommit);

                recommit.setVisibility(status==0?View.GONE:View.GONE);

                GlideUtils.getGlideUtils().glideCircleImage(context,Constant.Url_Common+item.UserHeadSculpture,head);
                order.setText("产品:"+item.CreditProductName);
                name.setText("贷款人:"+item.UserName);
                tv_status.setText(item.StatusName);


                dkje.setVisibility(View.GONE);
                money.setVisibility(View.GONE);
                head.setVisibility(View.VISIBLE);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,250));

//                Log.e(TAG,"tab_position="+tab_position);
//                if (tab_position==2){
//                    dkje.setVisibility(View.VISIBLE);
//                    money.setVisibility(View.VISIBLE);
//                    head.setVisibility(View.GONE);
//                    //params.height=350;
//                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,350));
//                }else {
//                    dkje.setVisibility(View.GONE);
//                    money.setVisibility(View.GONE);
//                    head.setVisibility(View.VISIBLE);
//                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,250));
//                }


            }
        };
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(SalesWorkActivity.this,SalesOrderInfoActivity.class);
                intent.putExtra(Constant.Sales_Order_Id,data.get(position).Id);
                intent.putExtra(Constant.Order_Info_Type,"sales");
                intent.putExtra(Constant.Order_Info_Status,status);
                Log.e(TAG,"id="+data.get(position).Id);
                startActivity(intent);

//                Intent intent=new Intent(SalesWorkActivity.this,SalesOrderInfoTest.class);
//                startActivity(intent);
            }
        });

    }

    @OnClick({R.id.sales_title_back, R.id.sales_title_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sales_title_back:
                finish();
                break;
            case R.id.sales_title_notice:
                break;
        }
    }


    @Subscriber(tag = "recommit")
    public void chooseContractEvent(String reason){
        Log.e(TAG,"reason="+reason);
        httpGetSalesWorkList(false,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
