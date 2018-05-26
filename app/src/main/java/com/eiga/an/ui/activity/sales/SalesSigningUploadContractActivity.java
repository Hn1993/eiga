package com.eiga.an.ui.activity.sales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.salesModel.ApiSalesCustomerProductModel;
import com.eiga.an.model.salesModel.ApiSalesUploadContractModel;
import com.eiga.an.model.salesModel.ApiSignUploadContractsModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.GridItemDecoration;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/22.
 */

public class SalesSigningUploadContractActivity extends BaseActivity {
    @BindView(R.id.sales_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.sales_title_notice)
    RelativeLayout salesTitleNotice;
    @BindView(R.id.sales_up_contract_rv)
    RecyclerView salesUpContractRv;
    @BindView(R.id.sales_up_contract_add)
    RelativeLayout salesUpContractAdd;
    @BindView(R.id.sales_up_contract_cb)
    CheckBox salesUpContractCb;
    @BindView(R.id.sales_up_contract_tv)
    TextView salesUpContractTv;
    private String TAG = getClass().getName();
    private Context context;

    private RecyclerAdapter<String> mGridAdapter;
    private List<String> imageList=new ArrayList<>();
    private List<String> selectList=new ArrayList<>();
    private List<String> showList=new ArrayList<>();
    private boolean haveEmpty=true;
    private AlertDialog.Builder mDialog;

    private String salesPhone, salesToken,salesUserId,SalesCreditProductId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_upload_contract);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);
        context = this;
        salesPhone = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(context, Constant.Sales_Login_Token, "");
        mDialog=new android.app.AlertDialog.Builder(this);
        salesUserId = getIntent().getStringExtra(Constant.Sales_User_Id);
        SalesCreditProductId = getIntent().getStringExtra(Constant.Sales_CreditProduct_Id);
        findViews();
    }

    private void findViews() {
        salesTitleTv.setText("上传资料");
        salesTitleNotice.setVisibility(View.GONE);
        salesUpContractRv.setLayoutManager(new GridLayoutManager(context,3));
        salesUpContractRv.addItemDecoration(new GridItemDecoration(30));

//        for (int i = 0; i < 2; i++) {
//            imageList.add("");
//        }
//        imageList.add("add");

        setAdapterData(imageList);

    }

    private void setAdapterData(final List<String> data) {
        if (data.size()<4){
            for (int i = data.size(); i < 3; i++) {
                data.add("");
            }

//            for (int i = 0; i < data.size(); i++) {
//                if (!TextUtils.isEmpty(data.get(i))){
//
//                }
//            }
        }
        if (!data.contains("add")){
            data.add("add");
        }
//        for (int i = 0; i < data.size(); i++) {
//
//        }


        mGridAdapter=new RecyclerAdapter<String>(R.layout.layout_sales_up_contract_item,data) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, final int position, String item) {
                ImageView icon = viewHolder.findViewById(R.id.sales_up_contract_item_icon);
                ImageView image = viewHolder.findViewById(R.id.sales_up_contract_item_image);
                RelativeLayout layout = viewHolder.findViewById(R.id.sales_up_contract_item_layout);

                if (item.equals("add")){
                    icon.setBackground(getResources().getDrawable(R.mipmap.icon_upload_contract_add));
                }else {
                    icon.setBackground(getResources().getDrawable(R.mipmap.icon_camera));
                }

                if (TextUtils.isEmpty(item)||item.equals("add")){
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
                        showBigImage((ArrayList<String>) showList);
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
                                        data.remove(position);
                                        if (data.size()<4){
                                            for (int i = data.size(); i < 4; i++) {
                                                data.add(i-1,"");
                                            }
                                        }
                                        setAdapterData(data);
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

        salesUpContractRv.setAdapter(mGridAdapter);
    }


    /**
     * 图片预览
     * @param showList
     */
    private void showBigImage(ArrayList<String> showList){
        //ArrayList preList=new ArrayList();
        if (showList==null){
            showList=new ArrayList<>();
        }

        for (int i = 0; i < showList.size(); i++) {
                //preList.add(Constant.Url_Common+showList.get(i));
                Log.e(TAG,"showList.get i="+showList.get(i));
        }


        Album.gallery(this)
                .requestCode(200)
                .checkedList(showList) // List of image to view: ArrayList<String>.
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
                            showList.add(result.get(i).getPath());
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
                            for (int i = 0; i < imageList.size(); i++) {
                                if (TextUtils.isEmpty(imageList.get(i))||imageList.get(i).equals("add")){
                                    imageList.set(i,model.FullPath);
                                    haveEmpty=true;
                                    break;
                                }
                            }
                            if (!haveEmpty){
                                imageList.add(model.FullPath);
                            }
                            setAdapterData(imageList);
                            
                            selectList.add(model.ContractPhoto);

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

    @OnClick({R.id.sales_title_back, R.id.sales_up_contract_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sales_title_back:
                finish();
                break;
            case R.id.sales_up_contract_commit:
                httpCommitImageContracts();
                break;
        }
    }

    //提交
    private void httpCommitImageContracts() {
        String photos = "";

        for (int i = 0; i < selectList.size(); i++) {
            photos+=selectList.get(i)+",";
        }
        photos=photos.substring(0,photos.length()-1);

        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Upload_Contracts, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        Log.e(TAG,"salesPhone="+salesPhone);
        Log.e(TAG,"salesToken="+salesToken);
        Log.e(TAG,"salesUserId="+salesUserId);
        Log.e(TAG,"photos="+photos);
        Log.e(TAG,"SalesCreditProductId="+SalesCreditProductId);
        mStringRequest.add("CellPhone", salesPhone);
        mStringRequest.add("UserId", salesUserId);
        mStringRequest.add("Token", salesToken);
        mStringRequest.add("CreditProcuctId", SalesCreditProductId);
        mStringRequest.add("ContractPhoto", photos);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiSignUploadContractsModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiSignUploadContractsModel.class);
                        if (model.Status == 1) {
                            finish();
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
}
