package com.eiga.an.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseFragment;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMyCenterModel;
import com.eiga.an.model.salesModel.ApiSalesUserInfoModel;
import com.eiga.an.ui.activity.user.FixInfoActivity;
import com.eiga.an.ui.activity.user.SettingActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MyCenterFragment extends BaseFragment {
    @BindView(R.id.common_title_back)
    RelativeLayout commonTitleBack;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.my_head)
    ImageView myHead;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.my_grade)
    TextView myGrade;
    Unbinder unbinder;
    private View mRootView;

    private String userPhone,userToken,grade;
    private String TAG=getClass().getName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mycenter, null);
        }
        EventBus.getDefault().register(this);
        userPhone = (String) SharedPreferencesUtils.getShared(getActivity(), Constant.User_Login_Name, "");
        userToken = (String) SharedPreferencesUtils.getShared(getActivity(), Constant.User_Login_Token, "");
        unbinder = ButterKnife.bind(this, mRootView);
        findViews();
        httpGetUserInfo();
        return mRootView;
    }

    private void httpGetUserInfo() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_MyCenter, RequestMethod.POST);
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
                    ApiMyCenterModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiMyCenterModel.class);
                        if (model.Status==1){
                            setHttpData(model);
                        }else {
                            PhoneUtils.toast(getActivity(),model.Msg.toString());
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
                PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setHttpData(ApiMyCenterModel model) {
        myName.setText(TextUtils.isEmpty(model.UserName)?model.Nick: model.UserName);
        GlideUtils.getGlideUtils().glideCircleImage(getActivity(),Constant.Url_Common+model.HeadSculpture,myHead);

        if (0<=Integer.valueOf(model.FinalScore)&&Integer.valueOf(model.FinalScore)<20){
            grade="信用等级:A";
        }else if (20<=Integer.valueOf(model.FinalScore)&&Integer.valueOf(model.FinalScore)<60){
            grade="信用等级:B";
        }else {
            grade="信用等级:C";
        }
        myGrade.setText(TextUtils.isEmpty(model.FinalScore)?"信用等级:无":grade);
    }

    private void findViews() {
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("个人中心");
    }


    @Subscriber(tag = "fix_user_info")
    public void fixInfoEvent(String str){
       httpGetUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.my_up_grade, R.id.my_car_layout,R.id.my_setting,
            R.id.my_insurance_layout,R.id.my_logout})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.my_setting:
                intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_logout:
                doLogout();
                break;
            case R.id.my_up_grade:
                break;
            case R.id.my_car_layout:

                break;
            case R.id.my_insurance_layout:
                break;
        }
    }

    /**
     * 退出登录的事件
     */
    private void doLogout() {
        SharedPreferencesUtils.clearSp(getActivity(),Constant.User_Login_Token);
        Intent intent=new Intent(getActivity(), UserLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
