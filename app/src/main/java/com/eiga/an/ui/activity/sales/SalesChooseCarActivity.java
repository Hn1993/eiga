package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesChooseCarListModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/5.
 */

public class SalesChooseCarActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.sales_choose_car_brand)
    RecyclerView rv_salesChooseCarBrand;
    @BindView(R.id.sales_choose_car_brand_list)
    RecyclerView rv_salesChooseCarBrandList;
    private String TAG = getClass().getName();
    private String salesPhone, salesToken;
    private Context context;

    private RecyclerAdapter<ApiSalesChooseCarListModel.CarBrandInfoListBean> mCarBrandAdapter;
    private RecyclerAdapter<ApiSalesChooseCarListModel.CarBrandInfoListBean.CarModelListBean> mCarModelAdapter;
    private List<ApiSalesChooseCarListModel.CarBrandInfoListBean> mCarBrandData=new ArrayList<>();
    private List<ApiSalesChooseCarListModel.CarBrandInfoListBean.CarModelListBean> mCarModelData=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_choose_car);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        context = this;
        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");

        findViews();

        httpGetCarInfo();
    }

    private void httpGetCarInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Get_Car_List, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        Log.e(TAG, "salesPhone=" + salesPhone);
        Log.e(TAG, "salesToken=" + salesToken);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesChooseCarListModel model = null;

                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesChooseCarListModel.class);
                        if (model.Status == 1) {
                            setHttpData(model.CarBrandInfoList);

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

    }

    private void setHttpData(List<ApiSalesChooseCarListModel.CarBrandInfoListBean> carBrandInfoList) {
        mCarBrandData=carBrandInfoList;
        if (mCarBrandData.size()>0){
            mCarBrandData.get(0).isChoosed=true;
        }
        mCarBrandAdapter=new RecyclerAdapter<ApiSalesChooseCarListModel.CarBrandInfoListBean>(R.layout.layout_choose_car_brand_item,mCarBrandData) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, ApiSalesChooseCarListModel.CarBrandInfoListBean item) {
                ImageView car_image =viewHolder.findViewById(R.id.choose_car_brand_item_image);
                ImageView line_image=viewHolder.findViewById(R.id.choose_car_brand_item_line);
                RelativeLayout layout=viewHolder.findViewById(R.id.choose_car_brand_item_layout);

                GlideUtils.getGlideUtils().glideImage(context,Constant.Url_Common+item.CarBrandPicture,car_image);
                line_image.setVisibility(item.isChoosed? View.VISIBLE:View.GONE);
                layout.setSelected(item.isChoosed?true:false);


            }

        };

        rv_salesChooseCarBrand.setAdapter(mCarBrandAdapter);
        setItemAdapterData(mCarBrandData.get(0).CarModelList);

        mCarBrandAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i < mCarBrandData.size(); i++) {
                    if(i==position) {
                        mCarBrandData.get(i).isChoosed=true;
                    }else {
                        mCarBrandData.get(i).isChoosed=false;
                    }
                    //mCarBrandAdapter.notify();
                    mCarBrandData.get(i).isFirstLoad=false;
                }
                mCarBrandAdapter.notifyDataSetChanged();
                
                setItemAdapterData(mCarBrandData.get(position).CarModelList);
            }
        });
    }

    private void setItemAdapterData(List<ApiSalesChooseCarListModel.CarBrandInfoListBean.CarModelListBean> carModelList) {
        //mCarModelData.clear();
        mCarModelData=carModelList;
        Log.e(TAG,"carModelList="+carModelList.size());

        mCarModelAdapter=new RecyclerAdapter<ApiSalesChooseCarListModel.CarBrandInfoListBean.
                CarModelListBean>(R.layout.layout_choose_car_model_item,mCarModelData) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position,
                                      final ApiSalesChooseCarListModel.CarBrandInfoListBean.CarModelListBean item) {
                ImageView car_image=viewHolder.findViewById(R.id.choose_car_model_item_image);
                ImageView car_add=viewHolder.findViewById(R.id.choose_car_model_item_add);
                TextView car_title=viewHolder.findViewById(R.id.choose_car_model_item_title);
                TextView car_content=viewHolder.findViewById(R.id.choose_car_model_item_description);

                GlideUtils.getGlideUtils().glideImage(context,Constant.Url_Common+item.Picture,car_image);
                car_title.setText(item.CarBrandName+item.ModelName);
                car_content.setText("新车含购置税 全年车险");

                car_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG,"car_add");
                        EventBus.getDefault().post(item.Id,"choose_car");
                        finish();
                    }
                });

            }
        };
        rv_salesChooseCarBrandList.setAdapter(mCarModelAdapter);
        mCarModelAdapter.notifyDataSetChanged();


        mCarModelAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EventBus.getDefault().post(mCarModelData.get(position).Id,"choose_car");
                finish();
            }
        });
    }


    private void findViews() {

        commonTitleTv.setText("选择车型");
        rv_salesChooseCarBrand.setLayoutManager(new LinearLayoutManager(this));
        rv_salesChooseCarBrandList.setLayoutManager(new LinearLayoutManager(this));
        rv_salesChooseCarBrand.addItemDecoration(new SpacesItemDecoration(0));
        rv_salesChooseCarBrandList.addItemDecoration(new SpacesItemDecoration(0));

    }

    @OnClick(R.id.common_title_back)
    public void onViewClicked() {
        finish();
    }
}
