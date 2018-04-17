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
import com.eiga.an.ui.activity.user.BankVerifyActivity;
import com.eiga.an.ui.activity.user.CarQuotaActivity;
import com.eiga.an.ui.activity.user.IdCardVerifyActivity;
import com.eiga.an.ui.activity.user.InfoCollectionActivity;
import com.eiga.an.ui.activity.user.MainActivity;
import com.eiga.an.ui.activity.user.PhoneVerifyActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

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
    @BindView(R.id.carquota_tv_phone)
    TextView carquotaTvPhone;
    @BindView(R.id.carquota_tv_bank)
    TextView carquotaTvBank;
    private View mRootView;

    private String TAG = getClass().getName();

    public LocationClient mLocationClient = null;//定位
    private BroadcastReceiver mConnectNetReceiver; //监听网络状态

    private String userName,isHaveEvaluation;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_main, null);
        }
//        setImmersedNavigationBar(getActivity(), R.color.white);

        userName= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Login_Name,"");
        isHaveEvaluation= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Is_Have_Evaluation,"");
        if (TextUtils.isEmpty(userName)){//判断是否登录
            startActivity(new Intent(getActivity(), UserLoginActivity.class));
            getActivity().finish();
        }else {//判断是否评估过
//            if (TextUtils.isEmpty(isHaveEvaluation)){
//
//            }
        }

        unbinder = ButterKnife.bind(this, mRootView);
        findViews();
        addBroadCastReceiver();
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
                    //httpTest();
                    break;
            }
        }
    };


    private void httpGetJiedaiData() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_Jd_jiedai, RequestMethod.POST);
        mStringRequest.add("appkey", Constant.Jd_key);
        mStringRequest.add("name", "蓝利明");
        mStringRequest.add("idCard", "332527198507184753");
        mStringRequest.add("mobile", "13732423950");
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                if (what == 101) {
                    Log.e(TAG, "jiedai_onSucceed==" + response.get());

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }

    private void httpGetMiGuanData() {
        StringRequest mStringRequest = new StringRequest(Constant.Url_Jd_miguan, RequestMethod.POST);
        mStringRequest.add("appkey", Constant.Jd_key);
        mStringRequest.add("name", "蓝利明");
        mStringRequest.add("idCard", "332527198507184753");
        mStringRequest.add("phone", "13732423950");
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                if (what == 101) {
                    Log.e(TAG, "onSucceed==" + response.get());

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                Log.i(TAG, "onFailed==" + response.get());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(mConnectNetReceiver);//手动取消网络广播监听
    }


}
