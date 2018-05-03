package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerListModel;
import com.eiga.an.model.salesModel.ApiSalesOrderInfoModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.xyz.step.FlowViewVertical;
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
 * Created by ASUS on 2018/4/28.
 */

public class SalesOrderInfoActivity extends BaseActivity {
    @BindView(R.id.sales_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.sales_title_notice)
    RelativeLayout salesTitleNotice;
    @BindView(R.id.sales_order_info_image)
    ImageView salesOrderInfoImage;
    @BindView(R.id.sales_order_info_car_name)
    TextView salesOrderInfoCarName;
    @BindView(R.id.sales_order_info_features)
    TextView salesOrderInfoFeatures;
    @BindView(R.id.sales_order_info_car_price)
    TextView salesOrderInfoCarPrice;
    @BindView(R.id.sales_order_info_product)
    TextView salesOrderInfoProduct;
    @BindView(R.id.sales_order_info_product_image)
    ImageView salesOrderInfoProductImage;

    @BindView(R.id.sales_order_info_step)
    FlowViewVertical salesOrderInfoStep;
    @BindView(R.id.sales_order_info_first_money)
    TextView salesOrderInfoFirstMoney;
    @BindView(R.id.sales_order_info_loan_amount)
    TextView salesOrderInfoLoanAmount;
    @BindView(R.id.sales_order_info_month)
    TextView salesOrderInfoMonth;
    @BindView(R.id.sales_order_info_order_number)
    TextView salesOrderInfoOrderNumber;
    @BindView(R.id.sales_order_info_time)
    TextView salesOrderInfoTime;
    @BindView(R.id.sales_order_info_cancel)
    TextView salesOrderInfoCancel;
    private String TAG = getClass().getName();

    private String orderId;
    private String salesPhone, salesToken;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_info);
        orderId=getIntent().getStringExtra(Constant.Sales_Order_Id);
        Log.e(TAG,"orderId="+orderId);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);
        context = this;

        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");
        httpGetSalesOrderInfo();

        findViews();
    }

    private void httpGetSalesOrderInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Work_Order_Info, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("Id", orderId);
        Log.e(TAG, "salesPhone=" + salesPhone);
        Log.e(TAG, "salesToken=" + salesToken);
        Log.e(TAG, "Id=" + orderId);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesOrderInfoModel model = null;

                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesOrderInfoModel.class);
                        if (model.Status == 1) {
                            setHttpData(model);

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

    private void setHttpData(ApiSalesOrderInfoModel model) {

        GlideUtils.getGlideUtils().glideCircleImage(context,Constant.Url_Common+model.CreditProductPicture,salesOrderInfoProductImage);
        salesOrderInfoFirstMoney.setText(model.FirstPayment+"%");
        salesOrderInfoLoanAmount.setText(String.valueOf(model.LoanPeriod));

        salesOrderInfoCarName.setText(model.CarBrand+"\t"+model.CarModel);
        salesOrderInfoCarPrice.setText(String.valueOf(model.CarPrice));
        //salesOrderInfoCarName
    }

    private void findViews() {
        salesTitleNotice.setVisibility(View.GONE);
        salesTitleTv.setText("订单详情");

        setStepData();
    }

    private void setStepData() {
        List steps=new ArrayList();
        steps.add("等待审核");
        steps.add("审核通过");
        steps.add("等待放款");
        steps.add("放款成功");
        String []titles=new String[4];
        String []times=new String[4];


        times[0]="2018/09/01";
        times[1]="2018/09/02";
        times[2]="2018/09/03";
        times[3]="2018/09/04";

        titles[0]="等待审核"+"\n"+times[0];
        titles[1]="审核通过"+"\n"+times[1];
        titles[2]="等待放款"+"\n"+times[2];
        titles[3]="放款成功"+"\n"+times[3];
        salesOrderInfoStep.setProgress(3,4,titles,null);

    }

    @OnClick({R.id.sales_title_back, R.id.sales_order_info_info, R.id.sales_order_info_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sales_title_back:
                finish();
                break;
            case R.id.sales_order_info_info:
                break;
            case R.id.sales_order_info_cancel:
                break;
        }
    }
}
