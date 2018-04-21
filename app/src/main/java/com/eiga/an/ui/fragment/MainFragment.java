package com.eiga.an.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.eiga.an.R;
import com.eiga.an.base.BaseFragment;
import com.eiga.an.base.MyApplication;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiInfoCollectModel;
import com.eiga.an.model.jsonModel.ApiMainModel;
import com.eiga.an.ui.activity.user.BankVerifyActivity;
import com.eiga.an.ui.activity.user.IdCardVerifyActivity;
import com.eiga.an.ui.activity.user.InfoCollectionActivity;
import com.eiga.an.ui.activity.user.PhoneVerifyActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
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

/**
 * 首页
 */
public class MainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.common_title_back)
    RelativeLayout commonTitleBack;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.carquota_tv_price)
    TextView carquotaTvPrice;
    @BindView(R.id.carquota_tv_idcard)
    TextView carquotaIdCard;
    @BindView(R.id.carquota_tv_phone)
    TextView carquotaTvPhone;
    @BindView(R.id.carquota_tv_bank)
    TextView carquotaTvBank;
    private View mRootView;

    private String TAG = getClass().getName();

    public LocationClient mLocationClient = null;//定位
    private BroadcastReceiver mConnectNetReceiver; //监听网络状态

    private String token,phone,isHaveEvaluation,locationLat,locationLng;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_main, null);
        }
//        setImmersedNavigationBar(getActivity(), R.color.white);
        unbinder = ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);
        token= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Login_Token,"");
        phone= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Login_Name,"");
        isHaveEvaluation= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Is_Have_Evaluation,"");
        if (TextUtils.isEmpty(token)||TextUtils.isEmpty(phone)){//判断是否登录
            startActivity(new Intent(getActivity(), UserLoginActivity.class));
            getActivity().finish();
        }else {//判断是否评估过
            if (TextUtils.isEmpty(isHaveEvaluation)){
                startActivity(new Intent(getActivity(), InfoCollectionActivity.class));
                getActivity().finish();
            }else {
                findViews();
                addBroadCastReceiver();
            }

        }


        //httpTest();
        return mRootView;

    }


    private void findViews() {
//        fgMainInputImage.setImageDrawable(getResources().getDrawable(R.mipmap.white_bg));
//        fgMainInputImage.setImageShadowColor(getResources().getColor(R.color.color_shadow));
        //fgMainInputImage.setImageRadius(2);
        //httpGetMiGuanData();
        //httpGetJiedaiData();
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("我的贷款额度");
    }

    /**
     * 添加广播监听
     */
    private void addBroadCastReceiver() {
        mConnectNetReceiver = new ConnectChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络广播
        getActivity().registerReceiver(mConnectNetReceiver, mFilter);
    }

    @OnClick({ R.id.carquota_tv_recommit,
            R.id.carquota_tv_idcard, R.id.carquota_tv_phone, R.id.carquota_tv_bank})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.carquota_tv_recommit:
                intent=new Intent(getActivity(),InfoCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_idcard:
                intent=new Intent(getActivity(),IdCardVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_phone:
                intent=new Intent(getActivity(),PhoneVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.carquota_tv_bank:
                intent=new Intent(getActivity(),BankVerifyActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 网络状态广播
     */
    private class ConnectChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.i(TAG, "网络状态已经改变");
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    Log.i(TAG, "网络已连接");
                    getBDLocation();
                } else {//没网状态
                    Log.i(TAG, "网络已断开");
                    //显示无网络 Dialog
                    PhoneUtils.toast(getActivity(), "网络无连接,请检查网络设置...");
                    //在按键响应事件中显示此对话框
                }
            }
        }
    }


    //百度定位
    private void getBDLocation() {
        mLocationClient = new LocationClient(MyApplication.getInstance());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        //调用相关配置方法
        mLocationClient.start();//开启定位
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);


    }


    public BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //定位异常
            if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                Log.e(TAG, "定位异常:失败");
            } else {
                //定位成功
                String addrCity = null;
                String addrStreet = null;

                Log.e(TAG, "地址信息:" + bdLocation.getAddrStr());
                Log.e(TAG, "纬度:" + bdLocation.getLatitude());
                Log.e(TAG, "经度:" + bdLocation.getLongitude());
                //保存 位置  纬度 经度(double 类型)
                locationLat= String.valueOf(bdLocation.getLatitude());
                locationLng= String.valueOf(bdLocation.getLongitude());

                SharedPreferencesUtils.putShared(getActivity(),
                        Constant.LocationLongitude, String.valueOf(bdLocation.getLongitude()));
                SharedPreferencesUtils.putShared(getActivity(),
                        Constant.LocationLatitude, String.valueOf(bdLocation.getLatitude()));
                SharedPreferencesUtils.putShared(getActivity(),
                        Constant.LocationDistrictCity, String.valueOf(bdLocation.getDistrict()));

                addrStreet = bdLocation.getStreet();//街道
                addrCity = bdLocation.getDistrict();//县级市 县


                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //getShareCityInfo();
                    httpGetMainData(true);
                    break;
            }
        }
    };

    /**
     * 获取首页数据
     */
    private void httpGetMainData(boolean isShowLoading) {

        showLoading(getActivity());
        StringRequest mStringRequest = new StringRequest(Constant.Url_Main, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",phone);
        mStringRequest.add("Token",token);
        mStringRequest.add("Longitude",locationLng);
        mStringRequest.add("Latitude",locationLat);

        Log.e(TAG,"phone="+phone);
        Log.e(TAG,"token="+token);
        Log.e(TAG,"locationLng="+locationLng);
        Log.e(TAG,"locationLat="+locationLat);

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
                            setHttpData(model);
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

    /**
     * 设置网络数据
     * @param model
     */
    private void setHttpData(ApiMainModel model) {
        carquotaTvPrice.setText("￥"+String.valueOf(model.User.SimpleQuotaLimit));
        if (model.User.IdentityCardOCRAuthentication==true){
            carquotaIdCard.setText("认证成功");
            carquotaIdCard.setClickable(false);
        }else {
            carquotaIdCard.setText("立即认证");
        }

        if (model.User.Carrieroperator!=null){
            carquotaTvPhone.setText("认证成功");
            carquotaTvPhone.setClickable(false);
        }else {
            carquotaTvPhone.setText("立即认证");
        }

        if (model.BankCardIsAuthentication==true){
            carquotaTvBank.setText("认证成功");
            carquotaTvBank.setClickable(false);
        }else {
            carquotaTvBank.setText("立即认证");
        }
    }

    /**
     * 绑定成功  银行卡  身份证  运营商 都要去重新请求网络
     * @param str
     */
    @Subscriber (tag="bond_success")
    private void bondSuccess(String str){
        Log.e(TAG,"bond_success="+str);
        httpGetMainData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        if (mConnectNetReceiver!=null){
            getActivity().unregisterReceiver(mConnectNetReceiver);//手动取消网络广播监听
        }
    }


}
