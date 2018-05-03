package com.eiga.an.ui.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMyOrderListModel;
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
 * Created by ASUS on 2018/5/3.
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.my_order_rv)
    XRecyclerView mRecyclerView;
    private String TAG = getClass().getName();


    private Context context;
    private String token, phone;
    private int status = -1, offset = 0, limit = 10;

    private RecyclerAdapter<ApiMyOrderListModel.DataBean> mAdapter;

    private List<ApiMyOrderListModel.DataBean> mData=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        context = this;
        token = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Token, "");
        phone = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Name, "");

        findViews();

        httpGetOrderList(true,-1);
    }

    /**
     *
     * @param isShowLoading
     * @param loadStatus  标识是  加载更多还是刷新   -1啥都不干
     */
    private void httpGetOrderList(boolean isShowLoading, final int loadStatus) {
        if (isShowLoading) {
            showLoading();
        }

        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Order_List, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        mStringRequest.add("Status", status);
        mStringRequest.add("Offset", offset);
        mStringRequest.add("Limit", limit);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiMyOrderListModel model = null;

                    try {
                        model = new Gson().fromJson(response.get(), ApiMyOrderListModel.class);
                        if (model.Status == 1) {
                            setAdapterData(model.Data);

                        } else {
                            PhoneUtils.toast(context, model.Msg.toString());
                        }

                        if (loadStatus==0){
                            mRecyclerView.refreshComplete();
                        }else if (loadStatus==1){
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

    private void setAdapterData(List<ApiMyOrderListModel.DataBean> data) {
        if (data.size()<=limit){
            mRecyclerView.setLoadingMoreEnabled(false);
        }
        mData.addAll(data);

        mAdapter=new RecyclerAdapter<ApiMyOrderListModel.DataBean>(R.layout.layout_myorder_item,mData) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, ApiMyOrderListModel.DataBean item) {
                TextView carName=viewHolder.findViewById(R.id.my_order_item_name);
                TextView carNameType=viewHolder.findViewById(R.id.my_order_item_name_type);
                TextView status=viewHolder.findViewById(R.id.my_order_item_status);
                TextView price=viewHolder.findViewById(R.id.my_order_item_car_price);
                ImageView carImage=viewHolder.findViewById(R.id.my_order_item_image);


                status.setText(item.StatusName);
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }


    private void findViews() {
        commonTitleTv.setText("我的订单");


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
                mData.clear();
                offset=0;
                httpGetOrderList(false,0);
            }

            @Override
            public void onLoadMore() {
                offset+=1;
                httpGetOrderList(false,1);
            }
        });
    }

    @OnClick(R.id.common_title_back)
    public void onViewClicked() {
        finish();
    }
}
