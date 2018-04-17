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
import com.eiga.an.model.jsonModel.ApiMainModel;
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


                httpGetCode();
                break;
            case R.id.fg_main_tv_go:
                if (PhoneUtils.isMobile(userMainEtPhone.getText().toString())&& !TextUtils.isEmpty(userMainEtCode.getText().toString())){

                    //在这里请求接口   获取返回值的时候   获取一个用户是否已经评估过的字段   然后保存进share

                    SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Login_Name,userMainEtPhone.getText().toString());
                    SharedPreferencesUtils.putShared(UserLoginActivity.this,Constant.User_Is_Have_Evaluation,"");

                    if (TextUtils.isEmpty(isHaveEvaluation)){
                        intent=new Intent(UserLoginActivity.this, InfoCollectionActivity.class);
                    }else {
                        intent=new Intent(UserLoginActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();

                }else {
                    PhoneUtils.toast(UserLoginActivity.this,"信息有误,请检查后重试");
                }


                break;
        }
    }

    /**
     * 获取验证码
     */
    private void httpGetCode() {

        //showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Get_EMSCode, RequestMethod.GET);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);

                    ApiMainModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiMainModel.class);
                        if (model.Status==1){
                            //setHttpData(model.Data);
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
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }
}
