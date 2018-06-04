package com.eiga.an.ui.activity.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiUserLoginModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
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
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/3/22.
 */

public class IdCardVerifyActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.idcard_positive)
    ImageView idCardP;
    @BindView(R.id.idcard_negative)
    ImageView idCardN;
    @BindView(R.id.idcard_positive_choose)
    ImageView idCardPChoose;
    private String TAG = getClass().getName();


    private String front_path,back_path;
    private String phone, token;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        ButterKnife.bind(this);
        setImmersedNavigationBar(this, R.color.white);
        autoVirtualKeys();
        context=this;
        phone = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Name, "");
        token = (String) SharedPreferencesUtils.getShared(context, Constant.User_Login_Token, "");
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("身份认证");

    }

    @OnClick({R.id.common_title_back, R.id.idcard_positive, R.id.idcard_negative, R.id.idcard_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.idcard_positive:
                if (isHavePermission(IdCardVerifyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    singleChoosePhoto(IdCardVerifyActivity.this,1,idCardP,true);
                }else {
                    applyPermission(IdCardVerifyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.idcard_negative:
                if (isHavePermission(IdCardVerifyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    singleChoosePhoto(IdCardVerifyActivity.this,1,idCardN,false);
                }else {
                    applyPermission(IdCardVerifyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.idcard_commit:
                if (TextUtils.isEmpty(front_path)||TextUtils.isEmpty(back_path)){
                    PhoneUtils.toast(IdCardVerifyActivity.this,"请先上传身份证照片");
                }else {
                    httpCommitPhoto();
                }

                break;
        }
    }

    /**
     * 把身份证照片上传
     */
    private void httpCommitPhoto() {
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Upload_Idcard, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",phone);
        mStringRequest.add("Token",token);
        mStringRequest.add("FrontBase64",new FileBinary(new File(front_path))); //身份证前面的照片
        mStringRequest.add("BackBase64",new FileBinary(new File(back_path)));

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiUserLoginModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiUserLoginModel.class);
                        Intent intent=null;
                        if (model.Status==1){
                            //在这里请求接
                            //PhoneUtils.toast(IdCardVerifyActivity.this,"认证成功");
                            EventBus.getDefault().post("bond_success","bond_success");
                            finish();
                        }
                        PhoneUtils.toast(IdCardVerifyActivity.this,model.Msg);
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }

    /**
     * 判断有没有某个权限
     * @param context
     * @param permissionType
     * @return
     */
    private boolean isHavePermission(Context context,String permissionType){
        //Manifest.permission.WRITE_EXTERNAL_STORAGE  相册权限
        //Manifest.permission.CAMERA  相机权限
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permissionType)){
            return true;
        }
        return false;
    }


    private void singleChoosePhoto(final Context context, int count, final ImageView iv, final boolean isFront){
        Album.image(context)
                .singleChoice()
                .requestCode(200)
                .camera(true)
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
                        if (isFront){
                            front_path=result.get(0).getPath();
                        }else {
                            back_path=result.get(0).getPath();
                        }
                        Glide.with(IdCardVerifyActivity.this).load(Uri.fromFile(new File(result.get(0).getPath()))).into(iv);
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


    private void applyPermission(final Context context, final String permissionType){
        AndPermission.with(context)
                .permission(permissionType)
                .onDenied(new com.yanzhenjie.permission.Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (permissions.get(0).equals(permissionType)){
                            PhoneUtils.toast(context,"获取权限失败");
                        }
                    }
                })
                .onGranted(new com.yanzhenjie.permission.Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {

                    }
                })
                .start();
    }


}
