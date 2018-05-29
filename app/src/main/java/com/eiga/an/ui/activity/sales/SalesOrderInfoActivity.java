package com.eiga.an.ui.activity.sales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerListModel;
import com.eiga.an.model.salesModel.ApiSalesOrderInfoModel;
import com.eiga.an.model.salesModel.ApiSalesUploadContractModel;
import com.eiga.an.ui.activity.WebActivity;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.GridItemDecoration;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
import com.xyz.step.FlowViewVertical;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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

    @BindView(R.id.sales_order_info_step_view)
    VerticalStepView salesOrderInfoStepView;
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

    @BindView(R.id.sales_order_info_back_layout)
    LinearLayout backLayout;
    @BindView(R.id.sales_order_info_back_reason)
    TextView backReason;
    @BindView(R.id.sales_order_info_call)
    TextView salesInfoCall;
    @BindView(R.id.sales_order_info_back_rv)
    RecyclerView backRecyclerView;


    @BindView(R.id.sales_order_info_cancel)
    TextView salesOrderInfoCancel;

    private String TAG = getClass().getName();

    private String orderId;
    private String salesPhone, salesToken;
    private Context context;

    private Double monthPrice=0.0;


    private String httpUrl,type,webUrl="";
    private AlertDialog.Builder mDialog;
    private int status;
    private String tel;

    private RecyclerAdapter<String> mGridAdapter;
    private List<ApiSalesOrderInfoModel.ContractSnapshotBean> mImageList=new ArrayList<>();
    private List<String> showList=new ArrayList<>();
    private List<String> reIdsList=new ArrayList<>();
    private List<String> rePhotosList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_info);
        orderId=getIntent().getStringExtra(Constant.Sales_Order_Id);
        type=getIntent().getStringExtra(Constant.Order_Info_Type);
        status=getIntent().getIntExtra(Constant.Order_Info_Status,0);
        mDialog=new android.app.AlertDialog.Builder(this);

        context = this;
        if (type.equals("sales")){
            httpUrl=Constant.Url_Sales_Work_Order_Info;
            salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
            salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");
        }else {
            httpUrl=Constant.Url_User_Order_Info;
            salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Name, "");
            salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Token, "");
        }
        Log.e(TAG,"orderId="+orderId);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);



        httpGetSalesOrderInfo();

        findViews();
    }

    private void httpGetSalesOrderInfo() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(httpUrl, RequestMethod.POST);
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

                        }
                        PhoneUtils.toast(context, model.Msg.toString());
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

        GlideUtils.getGlideUtils().glideImage(context,Constant.Url_Common+model.CreditProductPicture,salesOrderInfoProductImage);

        if (type.equals("sales")){
            tel=model.UserCellPhone;
        }else {
            tel=model.ClerkCellPhone;
        }
//
//        salesOrderInfoProduct.setText("金融产品:"+model.CreditProduct);
//
//        monthPrice=PhoneUtils.get2Decimal(Double.valueOf(model.AggregateAmount)*(1+Double.valueOf(model.InterestRate))/model.LoanPeriod);
//
//        salesOrderInfoMonth.setText(monthPrice+"*"+model.LoanPeriod+"期");
//
//        salesOrderInfoTime.setText(model.CreateDate);
//
//        salesOrderInfoOrderNumber.setText(model.OrderId);
//
//        GlideUtils.getGlideUtils().glideImage(context,Constant.Url_Common+model.CarPicture,salesOrderInfoImage);
//
//        times[0]=model.CreateDate.substring(0,10);
//        times[1]=model.StartDate;
//        times[2]=model.EndDate;

        webUrl+=Constant.Url_Sales_Info_Process_Details+"FlowId="+orderId;
        backReason.setText(model.BackMsg);
        setStepData(model.OrderStatus,model.CreditFlowLog);
        if (model.ContractSnapshot!=null){
            mImageList=model.ContractSnapshot;
            for (int i = 0; i < model.ContractSnapshot.size(); i++) {
                showList.add(model.ContractSnapshot.get(i).ContractPhoto);
            }
        }
        setContractsPhoto(showList);
    }

    private void setContractsPhoto(final List<String> showList) {
        Log.e(TAG,"showList="+showList.size());
        for (int i = 0; i < showList.size(); i++) {
            Log.e(TAG,"showList.get(i)="+showList.get(i));
        }

        mGridAdapter=new RecyclerAdapter<String>(R.layout.layout_sales_up_contract_item,showList) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, final int position, String item) {
                ImageView icon = viewHolder.findViewById(R.id.sales_up_contract_item_icon);
                ImageView image = viewHolder.findViewById(R.id.sales_up_contract_item_image);
                RelativeLayout layout = viewHolder.findViewById(R.id.sales_up_contract_item_layout);

//                if (item.equals("add")){
//                    icon.setBackground(getResources().getDrawable(R.mipmap.icon_upload_contract_add));
//                }else {
//                    icon.setBackground(getResources().getDrawable(R.mipmap.icon_camera));
//                }
                icon.setBackground(getResources().getDrawable(R.mipmap.icon_upload_contract_add));
                if (TextUtils.isEmpty(item)){
                    layout.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                }else {
                    image.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    GlideUtils.getGlideUtils().glideImage(context,Constant.Url_Common+item,image);
                }

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBigImage(Constant.Url_Common+showList.get(position));
                    }
                });

                image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        mDialog
                                //.setTitle("删除订单？")//设置对话框标题
                                .setMessage("确定删除这张照片？")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        //showList.remove(position);
                                        showList.set(position,"");
                                        setContractsPhoto(showList);
                                        reIdsList.add(mImageList.get(position).Id);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框


                        return true;
                    }
                });

            }
        };
        mGridAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.e(TAG,"setOnItemClickListener");
                goSelectImage();
            }
        });
        backRecyclerView.setAdapter(mGridAdapter);
    }

    /**
     * 去相册选择照片
     */
    private void goSelectImage() {

        Album.image(context)
                .singleChoice()
                .requestCode(200)
                .camera(true)
                .columnCount(2)
                .widget(Widget.newDarkBuilder(this)
                        .navigationBarColor(getResources().getColor(R.color.light_blue))
                        .statusBarColor(getResources().getColor(R.color.light_blue))
                        .toolBarColor(getResources().getColor(R.color.light_blue))
                        .title("选择合同照片")
                        .build())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            Log.e(TAG,"result="+result.get(i).getPath());
                            //showList.add(result.get(i).getPath());
                            httpUploadImageContract(result.get(i).getPath());
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        PhoneUtils.toast(context,"取消选择");
                    }
                })
                .start();
    }


    //单张图片上传到服务器
    private void httpUploadImageContract(String path) {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Upload_Single_Contract, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        Log.e(TAG,"salesPhone="+salesPhone);
        Log.e(TAG,"salesToken="+salesToken);
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("ContractPhoto", new FileBinary(new File(path)));
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesUploadContractModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesUploadContractModel.class);
                        if (model.Status == 1) {
                            for (int i = 0; i < showList.size(); i++) {
                                if (TextUtils.isEmpty(showList.get(i))){
                                    showList.set(i,model.FullPath);
                                    rePhotosList.add(model.ContractPhoto);
                                    setContractsPhoto(showList);
                                    break;
                                }
                            }

                            //setAdapterData(imageList);

                            //selectList.add(model.ContractPhoto);

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


    private void findViews() {
        salesTitleNotice.setVisibility(View.GONE);
        salesTitleTv.setText("订单详情");

        backRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
        backRecyclerView.addItemDecoration(new GridItemDecoration(30));
        backLayout.setVisibility(status==3?View.VISIBLE:View.GONE);

        if (type.equals("sales")){
            salesInfoCall.setText("联系用户");
        }else {
            salesInfoCall.setText("联系业务员");
        }
        //salesInfoCall.
    }


    /**
     * 图片预览
     * @param
     */
    private void showBigImage(String imageUrl){
        ArrayList preList=new ArrayList();
        preList.add(imageUrl);

        Log.e(TAG,"preList="+preList.get(0));

        Album.gallery(this)
                .requestCode(200)
                .checkedList(preList) // List of image to view: ArrayList<String>.
                .checkable(false) // Whether there is a selection function.
                .widget(Widget.newLightBuilder(this)
                        .navigationBarColor(getResources().getColor(R.color.light_blue))
                        .statusBarColor(getResources().getColor(R.color.light_blue))
                        .toolBarColor(getResources().getColor(R.color.light_blue))
                        .title("合同照片预览")
                        .build())
                .onResult(new Action<ArrayList<String>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<String> result) {

                    } // If checkable(false), action not required.
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }

                })
                .start();
    }

    private void setStepData(int orderStatus,List<ApiSalesOrderInfoModel.CreditFlowLogBean> logList) {
        List<String> stepList=new ArrayList<>();
        Collections.reverse(logList);
        for (int i = 0; i < logList.size(); i++) {
            stepList.add(logList.get(i).StepName+"\t\t"+logList.get(i).CreateDateString);
        }

        salesOrderInfoStepView.setStepsViewIndicatorComplectingPosition(stepList.size())//设置完成的步数
                .reverseDraw(false)//default is true
                .setTextSize(12)
                .setStepViewTexts(stepList)//总步骤
                .setStepsViewIndicatorCompletedLineColor(getResources().getColor(R.color.light_blue))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(getResources().getColor(R.color.light_grey))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(getResources().getColor(R.color.light_blue))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(getResources().getColor(R.color.light_grey))//设置StepsView text未完成线的颜色

                .setStepsViewIndicatorCompleteIcon(getResources().getDrawable(R.drawable.icon_light_blue))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(getResources().getDrawable(R.drawable.icon_light_grey))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(getResources().getDrawable(R.drawable.icon_light_blue));//设置StepsViewIndicator AttentionIcon


//        String []titles=new String[logList.size()];
//        Log.e(TAG,"titles="+titles.length);
//        Log.e(TAG,"logList.size()="+logList.size());
//        for (int i = 0; i < logList.size(); i++) {
//            titles[i]=logList.get(i).StepName+"\n"+logList.get(i).CreateDateString;
//        }
//        //salesOrderInfoStep.s
//        salesOrderInfoStep.setProgress(logList.size(),logList.size(),titles,null);
//        //salesOrderInfoStep.setProgress(6,titles.length,titles,null);
    }

    @OnClick({R.id.sales_title_back, R.id.sales_order_info_info,
            R.id.sales_order_info_cancel,R.id.sales_order_info_back_commit,
    R.id.sales_order_info_call})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.sales_title_back:
                finish();
                break;
            case R.id.sales_order_info_info:
                intent=new Intent(context, WebActivity.class);
                intent.putExtra(Constant.WebUrl,webUrl);
                intent.putExtra(Constant.WebTitle,"查看完整流程");
                startActivity(intent);
                break;
            case R.id.sales_order_info_cancel:
                break;
            case R.id.sales_order_info_call:
                //电话
                PhoneUtils.toast(this,"电话");
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel));//跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
                break;
            case R.id.sales_order_info_back_commit://重新提交

                httpFixContractPhoto();
                break;
        }
    }

    private void httpFixContractPhoto() {
        String fixContractString="";
        for (int i = 0; i < reIdsList.size(); i++) {
            fixContractString+=reIdsList.get(i)+":"+rePhotosList.get(i)+",";
        }

        if (TextUtils.isEmpty(fixContractString)){
            PhoneUtils.toast(context,"请更换图片之后重新提交");
            return;
        }else {
            fixContractString=fixContractString.substring(0,fixContractString.length()-1);
        }

        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Upload_Contracts_Recommit, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        Log.e(TAG,"CellPhone="+salesPhone);
        Log.e(TAG,"Token="+salesToken);
        Log.e(TAG,"FlowId="+orderId);
        Log.e(TAG,"ContractPhoto="+fixContractString);
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("FlowId", orderId);
        mStringRequest.add("ContractPhoto", fixContractString);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSalesUploadContractModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSalesUploadContractModel.class);
                        if (model.Status == 1) {
                            EventBus.getDefault().post("recommit","recommit");
                            finish();
                        }
                        PhoneUtils.toast(context, model.Msg.toString());

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
}
