package com.eiga.an.ui.activity.user;

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
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiFixInfoModel;
import com.eiga.an.model.salesModel.ApiSalesInfoFixCommitModel;
import com.eiga.an.model.salesModel.ApiSalesUserInfoModel;
import com.eiga.an.ui.activity.sales.SalesFixInfoActivity;
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
 * 修改资料
 */

public class FixInfoActivity extends BaseActivity {
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.fix_head)
    ImageView fixHead;
    @BindView(R.id.fix_et_name)
    EditText fixEtName;
    @BindView(R.id.fix_et_address)
    EditText fixEtAddress;
    private String TAG = getClass().getName();

    private String userPhone,userToken,headBase64,userNickName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fixinfo);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.white);

        userPhone = (String) SharedPreferencesUtils.getShared(FixInfoActivity.this, Constant.User_Login_Name, "");
        userToken = (String) SharedPreferencesUtils.getShared(FixInfoActivity.this, Constant.User_Login_Token, "");
        userNickName = (String) SharedPreferencesUtils.getShared(FixInfoActivity.this, Constant.User_Login_Nickname, "");
        headBase64="";

        findViews();
        httpGetUserInfo();
    }

    private void findViews() {
        //GlideUtils.getGlideUtils().glideCircleImage(FixInfoActivity.this,"",fixHead);
        commonTitleTv.setText("修改资料");
        //fixHead.setBackground(getResources().getDrawable(R.mipmap.icon_default_head));

    }
    //获取用户资料
    private void httpGetUserInfo() {
        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_GetInfo, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",userPhone);
        mStringRequest.add("Token",userToken);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiFixInfoModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiFixInfoModel.class);
                        if (model.Status==1){
                            setHttpData(model);
                        }else {
                            PhoneUtils.toast(FixInfoActivity.this,model.Msg.toString());
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
                PhoneUtils.toast(FixInfoActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setHttpData(ApiFixInfoModel model) {
        fixEtName.setText(model.Nick);
        Editable ea = fixEtName.getText();
        fixEtName.setSelection(ea.length());
        //fixEtName.setSelection(model.UserName.length());
        GlideUtils.getGlideUtils().glideCircleImage(FixInfoActivity.this,Constant.Url_Common+model.HeadSculpture,fixHead);
        fixEtAddress.setText(model.Adress);
    }



    @OnClick({R.id.common_title_back, R.id.fix_commit,R.id.fix_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.fix_commit:

                if (TextUtils.isEmpty(headBase64)&&userNickName.equals(fixEtName.getText().toString())){
                    finish();
                }else {
                    if (TextUtils.isEmpty(fixEtName.getText().toString())){
                        PhoneUtils.toast(FixInfoActivity.this,"昵称不能为空");
                    }else {
                        httpCommitFix();
                    }
                }

                break;

            case R.id.fix_head:
                singleChoosePhoto(FixInfoActivity.this,1,fixHead);
                break;
        }
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
                        GlideUtils.getGlideUtils().glideCircleImage(FixInfoActivity.this, String.valueOf(Uri.fromFile(new File(result.get(0).getPath()))),iv);
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


    //提交修改
    private void httpCommitFix() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_FixInfo, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",userPhone);
        mStringRequest.add("Token",userToken);
        mStringRequest.add("HeadSculpture",headBase64);//头像的base 64
        mStringRequest.add("Nick",fixEtName.getText().toString());//昵称
        mStringRequest.add("Adress",fixEtAddress.getText().toString());//住址
        mStringRequest.add("Sex","男");//昵称

        Log.e(TAG,"CellPhone="+userPhone);
        Log.e(TAG,"Token="+userToken);
        Log.e(TAG,"HeadSculpture="+headBase64);
        Log.e(TAG,"Nick="+fixEtName.getText().toString());
        Log.e(TAG,"Adress="+fixEtAddress.getText().toString());
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
                            EventBus.getDefault().post("fix_user_info","fix_user_info");
                            finish();
                        }else {
                            PhoneUtils.toast(FixInfoActivity.this,model.Msg);
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
                PhoneUtils.toast(FixInfoActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }
}
