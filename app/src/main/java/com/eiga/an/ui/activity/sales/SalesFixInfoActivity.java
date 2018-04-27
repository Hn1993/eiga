package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetBankInfoModel;
import com.eiga.an.model.salesModel.ApiSalesInfoFixCommitModel;
import com.eiga.an.model.salesModel.ApiSalesUserInfoModel;
import com.eiga.an.ui.activity.user.IdCardVerifyActivity;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/16.
 */

public class SalesFixInfoActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.fix_head)
    ImageView fixHead;
    @BindView(R.id.fix_et_name)
    EditText fixEtName;
    @BindView(R.id.fix_address_layout)
    RelativeLayout fixAddressLayout;
    private String TAG = getClass().getName();


    private String salesPhone,salesToken,headBase64,salesNickName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fixinfo);
        ButterKnife.bind(this);
        salesPhone = (String) SharedPreferencesUtils.getShared(SalesFixInfoActivity.this, Constant.Sales_Login_Phone, "");
        salesToken = (String) SharedPreferencesUtils.getShared(SalesFixInfoActivity.this, Constant.Sales_Login_Token, "");
        salesNickName = (String) SharedPreferencesUtils.getShared(SalesFixInfoActivity.this, Constant.Sales_Login_Nickname, "");
        headBase64="";
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        findViews();
        httpGetUserInfo();
    }

    private void findViews() {
        //GlideUtils.getGlideUtils().glideCircleImage(FixInfoActivity.this,"",fixHead);
        commonTitleTv.setText("修改资料");
        fixAddressLayout.setVisibility(View.GONE);
        //fixHead.setBackground(getResources().getDrawable(R.mipmap.icon_default_head));

    }

    @OnClick({R.id.common_title_back, R.id.fix_commit,R.id.fix_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.fix_head:
                singleChoosePhoto(SalesFixInfoActivity.this,1,fixHead);
                break;
            case R.id.fix_commit:
                if (TextUtils.isEmpty(headBase64)&&salesNickName.equals(fixEtName.getText().toString())){
                    finish();
                }else {
                    if (TextUtils.isEmpty(fixEtName.getText().toString())){
                        PhoneUtils.toast(SalesFixInfoActivity.this,"昵称不能为空");
                    }else {
                        httpCommitFix();
                    }
                }

                break;
        }
    }

    //提交修改
    private void httpCommitFix() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_FixInfo, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",salesPhone);
        mStringRequest.add("Token",salesToken);
        mStringRequest.add("HeadSculpture",headBase64);//头像的base 64
        mStringRequest.add("UserName",fixEtName.getText().toString());//昵称
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiSalesInfoFixCommitModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiSalesInfoFixCommitModel.class);
                        if (model.Status==1){
                            EventBus.getDefault().post("fix_sales_info","fix_sales_info");
                            finish();
                        }else {
                            PhoneUtils.toast(SalesFixInfoActivity.this,model.Msg);
                        }
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(SalesFixInfoActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }

    //获取用户资料
    private void httpGetUserInfo() {
        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_GetInfo, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",salesPhone);
        mStringRequest.add("Token",salesToken);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiSalesUserInfoModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiSalesUserInfoModel.class);
                        if (model.Status==1){
                            setHttpData(model);
                        }else {
                            PhoneUtils.toast(SalesFixInfoActivity.this,model.Msg.toString());
                        }
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(SalesFixInfoActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setHttpData(ApiSalesUserInfoModel model) {
        fixEtName.setText(model.UserName);
        Editable ea = fixEtName.getText();
        fixEtName.setSelection(ea.length());
        GlideUtils.getGlideUtils().glideCircleImage(SalesFixInfoActivity.this,Constant.Url_Common+model.HeadSculpture,fixHead);
    }


    private void singleChoosePhoto(final Context context, int count, final ImageView iv){
        Album.image(context)
                .singleChoice()
                .requestCode(200)
                .camera(false)
                .columnCount(2)
                .widget(Widget.newDarkBuilder(this)
                        .navigationBarColor(getResources().getColor(R.color.light_blue))
                        .statusBarColor(getResources().getColor(R.color.light_blue))
                        .toolBarColor(getResources().getColor(R.color.light_blue))
                        .title("选择图片")
                        .build())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            Log.e(TAG,"result="+result.get(i).getPath());
                        }
                        GlideUtils.getGlideUtils().glideCircleImage(SalesFixInfoActivity.this, String.valueOf(Uri.fromFile(new File(result.get(0).getPath()))),iv);
                        //Glide.with(SalesFixInfoActivity.this).load(Uri.fromFile(new File(result.get(0).getPath()))).into(iv);
                        imageFile2Base64(result.get(0).getPath());
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

    private void imageFile2Base64(String imagePath) {
        headBase64 = PhoneUtils.imageToBase64(imagePath);
        Log.e(TAG,"headBase64="+headBase64);
    }
}
