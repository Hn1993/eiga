package com.eiga.an.ui.activity.sales;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.utils.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;

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


    private RecyclerAdapter<String> mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_work);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);

        findViews();
    }

    private void findViews() {

        salesTitleTv.setText("工作任务");
        TabLayout.Tab tab;
        tab=salesWorkTab.newTab();
        tab.setCustomView(R.layout.layout_sales_work_tab1);
        salesWorkTab.addTab(tab);

        tab=salesWorkTab.newTab();
        tab.setCustomView(R.layout.layout_sales_work_tab2);
        salesWorkTab.addTab(tab);

        tab=salesWorkTab.newTab();
        tab.setCustomView(R.layout.layout_sales_work_tab3);
        salesWorkTab.addTab(tab);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(30));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);

        mRecyclerView.getDefaultFootView().setLoadingHint("加载中");
        mRecyclerView.getDefaultFootView().setNoMoreHint("加载完成");
        mRecyclerView.setLimitNumberToCallLoadMore(10);
        mRecyclerView.setLoadingMoreEnabled(false);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {


                        if(mRecyclerView != null)
                            mRecyclerView.refreshComplete();
                            mAdapter.notifyDataSetChanged();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {

                        if(mRecyclerView != null)
                            mRecyclerView.setNoMore(true);
                            mAdapter.notifyDataSetChanged();
                    }

                }, 1000);
            }
        });


        setAdapterData(0);

        salesWorkTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    setAdapterData(0);
                }else if (tab.getPosition()==1){
                    setAdapterData(1);
                }else {
                    setAdapterData(2);
                }
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

    private void setAdapterData(final int tab_position) {
        List<String> data=new ArrayList();
        for (int i = 0; i < 5; i++) {
            data.add(String.valueOf(i));
        }

        mAdapter=new RecyclerAdapter<String>(R.layout.layout_sales_work_item,data) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, String item) {
                RelativeLayout layout=viewHolder.findViewById(R.id.sales_work_item_layout);
                ImageView head=viewHolder.findViewById(R.id.sales_work_item_head);
                TextView money=viewHolder.findViewById(R.id.sales_work_item_money);
                TextView order=viewHolder.findViewById(R.id.sales_work_item_order);
                TextView dkje=viewHolder.findViewById(R.id.sales_work_item_dkje);
                TextView status=viewHolder.findViewById(R.id.sales_work_item_status);

                //ViewGroup.LayoutParams params= layout.getLayoutParams();
                Log.e(TAG,"tab_position="+tab_position);
                if (tab_position==2){
                    dkje.setVisibility(View.VISIBLE);
                    money.setVisibility(View.VISIBLE);
                    head.setVisibility(View.GONE);
                    //params.height=350;
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,350));
                }else {
                    dkje.setVisibility(View.GONE);
                    money.setVisibility(View.GONE);
                    head.setVisibility(View.VISIBLE);
                    layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,250));
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
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
}
