package com.eiga.an.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetCodeModel;
import com.eiga.an.model.jsonModel.ApiUserLoginModel;
import com.eiga.an.utils.DownTimer;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.albert.autosystembar.SystemBarHelper;

/**
 * Created by ASUS on 2018/4/13.
 */

public class UserLoginActivity extends BaseActivity {
    @BindView(R.id.user_main_et_phone)
    EditText userMainEtPhone;
    @BindView(R.id.user_main_et_code)
    EditText userMainEtCode;
    @BindView(R.id.user_main_et_recommend)
    EditText userMainEtRecommend;
    @BindView(R.id.fg_main_tv_go)
    TextView fgMainTvGo;
    @BindView(R.id.user_main_tv_timer)
    TextView downTimer;
    @BindView(R.id.user_main_tv_getcode)
    TextView getCode;
    private String TAG = getClass().getName();

    private String isHaveEvaluation;//判断是否评估过

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        ButterKnife.bind(this);
        new SystemBarHelper.Builder()
                .enableImmersedStatusBar(true)
                .enableImmersedNavigationBar(true)
                .into(this);
        autoVirtualKeys();



        findViews();
    }

    private void findViews() {
        userMainEtPhone.setText((String) SharedPreferencesUtils.getShared(UserLoginActivity.this,Constant.User_Login_Name,""));
    }

    @OnClick({R.id.user_main_tv_getcode, R.id.fg_main_tv_go})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.user_main_tv_getcode:
                DownTimer timer = new DownTimer();//实例化
                timer.setTotalTime(120*1000);//设置毫秒数
                timer.setIntervalTime(1000);//设置间隔数
                timer.setTimerLiener(new DownTimer.TimeListener() {
                    @Override
                    public void onFinish() {
                        //Toast.makeText(MainActivity.this, "完成倒计时", Toast.LENGTH_SHORT).show();
                        //完成倒计时 重新请求接口
                        downTimer.setVisibility(View.GONE);
                        getCode.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onInterval(long remainTime) {
                        getCode.setVisibility(View.GONE);
                        downTimer.setVisibility(View.VISIBLE);
                        downTimer.setText(remainTime / 1000 +" 秒后重新获取");
                    }
                });
                timer.start();

                if (PhoneUtils.isMobile(userMainEtPhone.getText().toString())){
                    httpGetCode();
                }else {
                    PhoneUtils.toast(UserLoginActivity.this,"请填写正确的手机号");
                }
                break;
            case R.id.fg_main_tv_go:
                if (PhoneUtils.isMobile(userMainEtPhone.getText().toString())&& !TextUtils.isEmpty(userMainEtCode.getText().toString())){
                    httpDoLogin();
                }else {
                    PhoneUtils.toast(UserLoginActivity.this,"信息有误,请检查后重试");
                }


                break;
        }
    }

    /**
     * 点击立即评估的操作
     */
    private void httpDoLogin() {

        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Login, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",userMainEtPhone.getText().toString());
        mStringRequest.add("VaildateCode",userMainEtCode.getText().toString());
        mStringRequest.add("AndroidUDID",PhoneUtils.getDeviceId(UserLoginActivity.this));
        mStringRequest.add("IOSUDID","");
        mStringRequest.add("ReferralCode",userMainEtRecommend.getText().toString());

        Log.e(TAG,"phone="+userMainEtPhone.getText().toString());
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
                            //在这里请求接口   获取返回值的时候   获取一个用户是否已经评估过的字段   然后保存进share

                            if (model.SimpleQuotaLimit!=null){
                                SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Is_Have_Evaluation,model.SimpleQuotaLimit);
                            }else {
                                SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Is_Have_Evaluation,"");
                            }

                            SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Login_Name,userMainEtPhone.getText().toString());
                            SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Login_Token,model.Token);

                            if (TextUtils.isEmpty(model.SimpleQuotaLimit)){
                                intent=new Intent(UserLoginActivity.this, InfoCollectionActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                intent=new Intent(UserLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        //PhoneUtils.toast(UserLoginActivity.this,model.Msg);
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                //Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }

    /**
     * 获取验证码
     */
    private void httpGetCode() {

        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_EMSCode, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",userMainEtPhone.getText().toString());

        Log.e(TAG,"phone="+userMainEtPhone.getText().toString());
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiGetCodeModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiGetCodeModel.class);
                        if (model.Status==1){
                            //PhoneUtils.toast(UserLoginActivity.this,model.Msg);
                        }
                        PhoneUtils.toast(UserLoginActivity.this,model.Msg);
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
}
