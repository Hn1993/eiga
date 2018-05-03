package com.eiga.an.ui.activity.sales;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.salesModel.ChooseContractResult;
import com.eiga.an.ui.activity.user.IdCardVerifyActivity;
import com.eiga.an.utils.PhoneUtils;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/27.
 */

public class SalesChooseContractActivity extends BaseActivity {

    @BindView(R.id.sales_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.sales_title_notice)
    RelativeLayout salesTitleNotice;
    @BindView(R.id.sales_choose_contract_p)
    ImageView salesChooseContractP;
    @BindView(R.id.sales_choose_contract_n)
    ImageView salesChooseContractN;
    private String TAG = getClass().getName();

    private String contractP_path="",contractN_path="";

    private ChooseContractResult mChooseContractResult=new ChooseContractResult();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_choose_contract);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);

        findViews();
    }

    private void findViews() {
        salesTitleNotice.setVisibility(View.GONE);
        salesTitleTv.setText("上传合同");

    }

    @OnClick({R.id.sales_title_back, R.id.sales_choose_contract_p_layout, R.id.sales_choose_contract_n_layout, R.id.sales_choose_contract_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sales_title_back:
                finish();
                break;
            case R.id.sales_choose_contract_p_layout:
                singleChoosePhoto(SalesChooseContractActivity.this,"选择合同正面照",salesChooseContractP);
                break;
            case R.id.sales_choose_contract_n_layout:
                singleChoosePhoto(SalesChooseContractActivity.this,"选择合同反面照",salesChooseContractN);
                break;
            case R.id.sales_choose_contract_commit:
                if (TextUtils.isEmpty(contractP_path)||TextUtils.isEmpty(contractN_path)){
                    PhoneUtils.toast(SalesChooseContractActivity.this,"合同照片不能为空");
                }else {
                    //EventBus  把照片传回去
                    EventBus.getDefault().post(mChooseContractResult,"choose_contract");
                    finish();
                }
                break;
        }
    }

    private void singleChoosePhoto(final Context context, final String title, final ImageView iv){
        Album.image(context)
                .singleChoice()
                .requestCode(200)
                .camera(true)
                .columnCount(2)
                .widget(Widget.newDarkBuilder(this)
                        .navigationBarColor(getResources().getColor(R.color.light_blue))
                        .statusBarColor(getResources().getColor(R.color.light_blue))
                        .toolBarColor(getResources().getColor(R.color.light_blue))
                        .title(title)
                        .build())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            Log.e(TAG,"result="+result.get(i).getPath());
                        }
                        Glide.with(SalesChooseContractActivity.this).load(Uri.fromFile(new File(result.get(0).getPath()))).into(iv);
                        Log.e(TAG,"title="+title);
                        if (title.equals("选择合同正面照")){
                            contractP_path=result.get(0).getPath();
                        }else {
                            contractN_path=result.get(0).getPath();
                        }
                        mChooseContractResult.contractP_path=contractP_path;
                        mChooseContractResult.contractN_path=contractN_path;
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
}
