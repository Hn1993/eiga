package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerProductModel;
import com.eiga.an.model.salesModel.ChooseContractResult;
import com.eiga.an.utils.DateFormatUtils;
import com.eiga.an.utils.Left0ItemDecoration;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/27.
 */

public class SalesSigningActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.sales_signing_et_name)
    EditText salesSigningEtName;
    @BindView(R.id.sales_signing_et_sex)
    EditText salesSigningEtSex;
    @BindView(R.id.sales_signing_et_job)
    EditText salesSigningEtJob;
    @BindView(R.id.sales_signing_et_id_number)
    EditText salesSigningEtIdNumber;
    @BindView(R.id.sales_signing_et_id_address)
    EditText salesSigningEtIdAddress;
    @BindView(R.id.sales_signing_et_now_address)
    EditText salesSigningEtNowAddress;
    @BindView(R.id.sales_signing_et_origin)
    EditText salesSigningEtOrigin;




    @BindView(R.id.sales_signing_et_contract)
    EditText salesSigningEtContract;
    @BindView(R.id.sales_signing_et_car)
    EditText salesSigningEtCar;
    @BindView(R.id.sales_signing_et_stime)
    EditText salesSigningEtStime;
    @BindView(R.id.sales_signing_et_overdue)
    EditText salesSigningEtOverdue;
    @BindView(R.id.sales_signing_et_periods)
    EditText salesSigningEtPeriods;
    @BindView(R.id.sales_signing_et_money)
    EditText salesSigningEtMoney;
    @BindView(R.id.sales_signing_tv_product)
    TextView salesSigningTvProduct;
    @BindView(R.id.sales_signing_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.sales_signing_et_car_price)
    EditText salesSigningEtCarPrice;
    @BindView(R.id.sales_signing_et_first_price)
    EditText salesSigningEtFirstPrice;
    @BindView(R.id.sales_signing_et_car_brand)
    EditText salesSigningEtCarBrand;
    @BindView(R.id.sales_signing_et_car_type)
    EditText salesSigningEtCarType;
    @BindView(R.id.sales_signing_et_repayment_date)
    EditText salesSigningEtRepaymentDate;
    @BindView(R.id.sales_signing_et_principal)
    EditText salesSigningEtPrincipal;
    private String TAG = getClass().getName();

    private String salesUserId;
    private Context context;

    private String salesPhone, salesToken;

    private RecyclerAdapter<ApiSalesCustomerProductModel.DataBean> mAdapter;
    private List<ApiSalesCustomerProductModel.DataBean> mData = new ArrayList<>();

    private String contractP_path="",contractN_path="";

    private String chooseProductId;
    private String chooseCarModelId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_signing);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);
        EventBus.getDefault().register(this);
        salesUserId = getIntent().getStringExtra(Constant.Sales_User_Id);
        Log.e(TAG,"salesUserId="+salesUserId);
        context = this;
        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");

        findViews();
        httpGetUserProduct();
    }

    private void httpGetUserProduct() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Customer_Product, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("UserId", salesUserId);
        Log.e(TAG, "salesPhone=" + salesPhone);
        Log.e(TAG, "salesToken=" + salesToken);
        Log.e(TAG, "salesUserId=" + salesUserId);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesCustomerProductModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesCustomerProductModel.class);
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

    }

    private void setHttpData(List<ApiSalesCustomerProductModel.DataBean> data) {
        mData = data;
        mAdapter = new RecyclerAdapter<ApiSalesCustomerProductModel.DataBean>(R.layout.layout_sales_signing_item, data) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, final int position, final ApiSalesCustomerProductModel.DataBean item) {
                TextView name = viewHolder.findViewById(R.id.sales_customer_item_name);
                TextView money = viewHolder.findViewById(R.id.sales_customer_item_money);
                TextView status = viewHolder.findViewById(R.id.sales_customer_item_status);
                final TextView tv_choose = viewHolder.findViewById(R.id.sales_customer_item_signing);

                name.setText(item.Name);
                money.setText("最高可贷款金额:" + item.MaximumLoanAmount);
                status.setText(item.IsRecommand ? "推荐" : "不推荐");

                tv_choose.setBackgroundColor(mData.get(position).IsChoosed ? getResources().
                        getColor(R.color.light_blue) : getResources().getColor(R.color.light_grey));

                tv_choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setChooseButtonStatus(position, tv_choose);

                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    private void setChooseButtonStatus(int position, TextView tv_choose) {
        Log.e(TAG,"setChooseButtonStatus");
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).IsChoosed = false;
        }
        if (mData.get(position).IsChoosed == true) {
            mData.get(position).IsChoosed = false;
        } else {
            mData.get(position).IsChoosed = true;
        }
        for (int i = 0; i < mData.size(); i++) {
            Log.e(TAG,"IsChoosed="+mData.get(i).IsChoosed);
        }

        mAdapter.notifyDataSetChanged();

    }

    private void findViews() {
        commonTitleTv.setText("签单");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new Left0ItemDecoration(30));

    }

    @OnClick({R.id.common_title_back, R.id.sales_signing_rl_contract,
            R.id.sales_signing_rl_car,R.id.sales_signing_tv_commit})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.sales_signing_rl_contract:
                intent=new Intent(SalesSigningActivity.this,SalesChooseContractActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_signing_rl_car:
                intent=new Intent(SalesSigningActivity.this,SalesChooseCarActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_signing_tv_commit:

                for (int i = 0; i < mData.size(); i++) {

                    if (mData.get(i).IsChoosed==true){
                        chooseProductId=mData.get(i).CreditProductId;
                    }

                    Log.e(TAG,"sales_signing_tv_commit="+mData.get(i).IsChoosed);
                }

                Log.e(TAG,"chooseProductId="+chooseProductId);
                if (!TextUtils.isEmpty(salesSigningEtContract.getText().toString())){
                    if (!TextUtils.isEmpty(salesSigningEtCarPrice.getText().toString())){
                        if (!TextUtils.isEmpty(salesSigningEtFirstPrice.getText().toString())){
//                            if (!TextUtils.isEmpty(salesSigningEtCarBrand.getText().toString())){
//                                if (!TextUtils.isEmpty(salesSigningEtCarType.getText().toString())){

                            if (!TextUtils.isEmpty(salesSigningEtCar.getText().toString())){

                                    //验证时间格式
                                    /**
                                     * &&
                                     DateFormatUtils.String2long(salesSigningEtStime.getText().
                                     toString(),DateFormatUtils.PATTERN_BAR_TIME)
                                     >=System.currentTimeMillis()
                                     */
                                    if (!TextUtils.isEmpty(salesSigningEtStime.getText().toString())){
                                            //还款日期
                                            if (!TextUtils.isEmpty(salesSigningEtRepaymentDate.getText().toString())
                                                    &&0<Integer.valueOf(salesSigningEtRepaymentDate.getText().toString())&&
                                                    28>Integer.valueOf(salesSigningEtRepaymentDate.getText().toString())){
                                                if (!TextUtils.isEmpty(salesSigningEtPrincipal.getText().toString())&&
                                                        Double.valueOf(salesSigningEtPrincipal.getText().toString())>0&&
                                                        Double.valueOf(salesSigningEtPrincipal.getText().toString())<1){

                                                    if (!TextUtils.isEmpty(salesSigningEtOverdue.getText().toString()) &&
                                                            Double.valueOf(salesSigningEtOverdue.getText().toString())>0&&
                                                            Double.valueOf(salesSigningEtOverdue.getText().toString())<1){
                                                        //分期月数
                                                        if (!TextUtils.isEmpty(salesSigningEtPeriods.getText().toString())&&
                                                                Integer.valueOf(salesSigningEtPeriods.getText().toString())>0 ){
                                                                //贷款金额
                                                                if (!TextUtils.isEmpty(salesSigningEtMoney.getText().toString())&&
                                                                        Integer.valueOf(salesSigningEtMoney.getText().toString())>0 ){

                                                                        if (!TextUtils.isEmpty(chooseProductId)){
                                                                            httpCommitOrder();
                                                                        }else {
                                                                            PhoneUtils.toast(context,"请选择一款产品");
                                                                        }

                                                                }else {
                                                                    PhoneUtils.toast(context,"贷款金额填写错误");
                                                                }

                                                        }else {
                                                            PhoneUtils.toast(context,"贷款期数填写错误");
                                                        }
                                                    }else {
                                                        PhoneUtils.toast(context,"逾期利率填写错误");
                                                    }
                                                }else {
                                                    PhoneUtils.toast(context,"本金利率填写错误");
                                                }
                                            }else {
                                                PhoneUtils.toast(context,"还款日期填写错误");
                                            }
                                    }else {
                                        PhoneUtils.toast(context,"贷款开始时间填写错误");
                                    }

                            }else {
                                    PhoneUtils.toast(context,"请选择车辆");
                            }

//                                }else {
//                                    PhoneUtils.toast(context,"汽车型号不能为空");
//                                }
//                            }else {
//                                PhoneUtils.toast(context,"汽车品牌不能为空");
//                            }
                        }else {
                            PhoneUtils.toast(context,"首付金额不能为空");
                        }
                    }else {
                        PhoneUtils.toast(context,"车价不能为空");
                    }
                }else {
                    PhoneUtils.toast(context,"合同照片不能为空");
                }

                break;
        }
    }

    private void httpCommitOrder() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Commit_Customer_Product, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("UserId", salesUserId);
        mStringRequest.add("CarPrice", salesSigningEtCarPrice.getText().toString());
        mStringRequest.add("FirstPayment", salesSigningEtFirstPrice.getText().toString());
//        mStringRequest.add("CarBrand", salesSigningEtCarBrand.getText().toString());
//        mStringRequest.add("CarModel", salesSigningEtCarType.getText().toString());
        mStringRequest.add("CarModelId", chooseCarModelId);
        mStringRequest.add("ContractSnapshotOne", new FileBinary(new File(contractP_path)));
        mStringRequest.add("ContractSnapshotTwo", new FileBinary(new File(contractN_path)));
        mStringRequest.add("CreditProcuctId", chooseProductId);
        mStringRequest.add("StartDate", salesSigningEtStime.getText().toString());
        mStringRequest.add("RepaymentDate", salesSigningEtRepaymentDate.getText().toString());
        mStringRequest.add("InterestRate", salesSigningEtPrincipal.getText().toString());
        mStringRequest.add("OverdueRate", salesSigningEtOverdue.getText().toString());
        mStringRequest.add("AggregateAmount", salesSigningEtMoney.getText().toString());
        mStringRequest.add("LoanPeriod", salesSigningEtPeriods.getText().toString());

        Log.e(TAG, "CellPhone=" + salesPhone);
        Log.e(TAG, "Token=" + salesToken);
        Log.e(TAG, "UserId=" + salesUserId);
        Log.e(TAG, "CarPrice=" + salesSigningEtCarPrice.getText().toString());
        Log.e(TAG, "FirstPayment=" + salesSigningEtFirstPrice.getText().toString());
        Log.e(TAG, "CarBrand=" + salesSigningEtCarBrand.getText().toString());
        Log.e(TAG, "CarModel=" + salesSigningEtCarType.getText().toString());
        Log.e(TAG, "CarModelId=" + chooseCarModelId);
        Log.e(TAG, "CreditProcuctId=" + chooseProductId);
        Log.e(TAG, "StartDate=" + salesSigningEtStime.getText().toString());
        Log.e(TAG, "RepaymentDate=" + salesSigningEtRepaymentDate.getText().toString());
        Log.e(TAG, "InterestRate=" + salesSigningEtPrincipal.getText().toString());
        Log.e(TAG, "OverdueRate=" + salesSigningEtOverdue.getText().toString());
        Log.e(TAG, "AggregateAmount=" + salesSigningEtMoney.getText().toString());
        Log.e(TAG, "LoanPeriod=" + salesSigningEtPeriods.getText().toString());


        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesCustomerProductModel model = null;
                    model = new Gson().fromJson(response.get(), ApiSalesCustomerProductModel.class);

                    try {
                        if (model.Status == 1) {
                            //setHttpData(model.Data);
                            finish();
                        } else {
                            PhoneUtils.toast(context, model.Msg);
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


    @Subscriber (tag = "choose_contract")
    public void chooseContractEvent(ChooseContractResult result){
//        contractP_path=PhoneUtils.Bitmap2StrByBase64(BitmapFactory.decodeFile(result.contractP_path));
//        contractN_path=PhoneUtils.Bitmap2StrByBase64(BitmapFactory.decodeFile(result.contractN_path));
        contractP_path=result.contractP_path;
        contractN_path=result.contractN_path;
        Log.e(TAG,"contractP_path="+contractP_path);
        Log.e(TAG,"contractN_path="+contractN_path);

        if (!TextUtils.isEmpty(result.contractP_path)&&!TextUtils.isEmpty(result.contractN_path)){
            salesSigningEtContract.setText("选择合同照片成功");
        }
    }

    @Subscriber (tag = "choose_car")
    public void chooseContractEvent(String carModelId){
        Log.e(TAG,"carModelId="+carModelId);
        if (!TextUtils.isEmpty(carModelId)){
            salesSigningEtCar.setText("选择车辆成功");
            chooseCarModelId=carModelId;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
