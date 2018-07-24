package com.eiga.an.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.canyinghao.caneffect.CanShadowDrawable;
import com.eiga.an.R;
import com.eiga.an.base.BaseFragment;
import com.eiga.an.base.MyApplication;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMainModel;
import com.eiga.an.model.jsonModel.ApiMallLoadTypeModel;
import com.eiga.an.model.jsonModel.ApiMallUploadLoadModel;
import com.eiga.an.service.LoadService;
import com.eiga.an.ui.activity.WebActivity;
import com.eiga.an.ui.activity.user.BankVerifyActivity;
import com.eiga.an.ui.activity.user.IdCardVerifyActivity;
import com.eiga.an.ui.activity.user.InfoCollectionActivity;
import com.eiga.an.ui.activity.user.LoanCalculatorActivity;
import com.eiga.an.ui.activity.user.PhoneVerifyActivity;
import com.eiga.an.ui.activity.user.PropertyCalculatorActivity;
import com.eiga.an.ui.activity.user.QueryTDExistActivity;
import com.eiga.an.ui.activity.user.QueryTDInfoActivity;
import com.eiga.an.ui.activity.user.QueryTDPayActivity;
import com.eiga.an.ui.activity.user.TdHisActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.EasyPickerView;
import com.eiga.an.view.ZoomOutPageTransformer;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.carquota_tv_recommit_td)
    TextView reQueryTd;
    @BindView(R.id.carquota_tv_td_his)
    TextView seeTdHis;
    @BindView(R.id.main_product_vp)
    ViewPager mProductVp;
    @BindView(R.id.ll_gallery_outer)
    RelativeLayout mProductVpOuter;
    private View mRootView;

    private String TAG = getClass().getName();

    public LocationClient mLocationClient = null;//定位
    private BroadcastReceiver mConnectNetReceiver; //监听网络状态

    private String token,phone,isHaveEvaluation,isHaveQueryTd,locationLat,locationLng;

    private List<ApiMallLoadTypeModel.CreditTypesBean> mTypeList = new ArrayList<>();

    private BottomSheetDialog mDialog;
    private EasyPickerView mPickView;
    private String loadLimit;

    private boolean isTdReportTiming;
    private boolean isChooseProduct=false;

    private AlertDialog.Builder mAlertDialog;

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
        isHaveQueryTd= (String) SharedPreferencesUtils.getShared(getActivity(),Constant.User_Is_Have_QueryTd,"");
        if (TextUtils.isEmpty(token)||TextUtils.isEmpty(phone)){//判断是否登录
            startActivity(new Intent(getActivity(), UserLoginActivity.class));
            getActivity().finish();
        }else {//判断是否评估过
//            if (TextUtils.isEmpty(isHaveEvaluation)){
//                startActivity(new Intent(getActivity(), InfoCollectionActivity.class));
//                getActivity().finish();
//            }
            Log.e(TAG,"isHaveQueryTd="+isHaveQueryTd);
            if (TextUtils.isEmpty(isHaveQueryTd)){
                Intent intent=new Intent(getActivity(), QueryTDInfoActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            else {
                findViews();
                addBroadCastReceiver();
            }



        }


        //httpTest();
        return mRootView;

    }


    public int dp2Px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void findViews() {
//        fgMainInputImage.setImageDrawable(getResources().getDrawable(R.mipmap.white_bg));
//        fgMainInputImage.setImageShadowColor(getResources().getColor(R.color.color_shadow));
        //fgMainInputImage.setImageRadius(2);
        //httpGetMiGuanData();
        //httpGetJiedaiData();
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("最高可贷");
        mAlertDialog=new AlertDialog.Builder(getActivity());





    }


    private void httpGetProductType() {

        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Loan_Type, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    Log.e(TAG,response.get() );
                    ApiMallLoadTypeModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiMallLoadTypeModel.class);
                        if (model.Status == 1) {
                            setProductData(model);
                        } else {
                            //PhoneUtils.toast(getActivity(),model.Msg.toString());
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
                //PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setProductData(ApiMallLoadTypeModel model) {
        mTypeList = model.CreditTypes;

        mProductVp.setAdapter(new MyPagerAdapter(mTypeList));
        mProductVp.setOffscreenPageLimit(4);
        mProductVp.setPageMargin(10);
        mProductVp.setPageTransformer(true,new ZoomOutPageTransformer());
        mProductVpOuter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mProductVp.dispatchTouchEvent(motionEvent);
            }
        });

    }


    private class MyPagerAdapter extends PagerAdapter {

        private List<ApiMallLoadTypeModel.CreditTypesBean> pagerData;
        public MyPagerAdapter(List<ApiMallLoadTypeModel.CreditTypesBean> data) {
            super();
            if (data!=null){
                pagerData=data;
            }
        }

        @Override
        public int getCount() {
            return pagerData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setImageResource(R.color.black);
//            container.addView(imageView);
//            return imageView;


            View view=getLayoutInflater().inflate(R.layout.mall_item,null,false);
            ImageView imageView = view.findViewById(R.id.mall_item_image);
            TextView textView = view.findViewById(R.id.mall_item_tv);
            LinearLayout item_layout = view.findViewById(R.id.mall_item_layout);




            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideUtils.getGlideUtils().glideImage(getActivity(), Constant.Url_Common + pagerData.get(position).Photo, imageView);
            textView.setText(pagerData.get(position).TypeName);

            CanShadowDrawable.Builder.on(item_layout)
                    .bgColor(getResources().getColor(R.color.white))
                    .radius(dp2Px(0))
                    .shadowColor(Color.parseColor("#E3E5E6"))

                    .shadowRange(dp2Px(5))
                    .offsetTop(dp2Px(5))
                    .offsetBottom(dp2Px(5))
                    .offsetLeft(dp2Px(5))
                    .offsetRight(dp2Px(5))
                    .create();


            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showChooseMoneyDialog(position);
                    if (isChooseProduct){
                        PhoneUtils.toast(getActivity(),"亲,请不要重复提交贷款产品~");
                    }else {
                        Intent intent = null;
                        if (mTypeList.get(position).TypeName.contains("房")||mTypeList.get(position).TypeName.contains("融资")){
                            intent = new Intent(getActivity(), PropertyCalculatorActivity.class);
                        }else {
                            intent = new Intent(getActivity(), LoanCalculatorActivity.class);
                        }
                        intent.putExtra(Constant.Main_Product_Id,mTypeList.get(position).Id);
                        intent.putExtra(Constant.Main_Product_Name,mTypeList.get(position).TypeName);
                        startActivity(intent);

                    }

                }
            });




            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void showChooseMoneyDialog(final int position) {


        View view=getLayoutInflater().inflate(R.layout.fragment_main_dialog,null);

        mDialog=new BottomSheetDialog(getActivity());
        mDialog.setContentView(view);
        mDialog.setTitle("请选择你期望的贷款额度");
        mDialog.show();


        mPickView= (EasyPickerView) view.findViewById(R.id.main_dialog_pick_view);
        final ArrayList<String> mMoneyList=new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            mMoneyList.add(i+"");
        }
        mPickView.setDataList(mMoneyList);
        mPickView.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                //Log.e(TAG,"onScrollChanged curIndex="+mMoneyList.get(curIndex));
                //loadLimit=mMoneyList.get(curIndex);
            }

            @Override
            public void onScrollFinished(int curIndex) {
                Log.e(TAG,"onScrollFinished curIndex="+mMoneyList.get(curIndex));
                loadLimit=mMoneyList.get(curIndex);
            }
        });


        view.findViewById(R.id.main_dialog_pick_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        view.findViewById(R.id.main_dialog_pick_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

                if (TextUtils.isEmpty(loadLimit)){
                    loadLimit="1";
                }
                int limit=Integer.valueOf(loadLimit)*10000;
                httpUploadLoad(String.valueOf(mTypeList.get(position).Id),String.valueOf(limit));


            }
        });
    }


    /**
     *
     * 上传想要贷款的额度
     * @param typeId
     * @param loadLimit
     */
    private void httpUploadLoad(String typeId, String loadLimit) {
        //showLoading(getActivity());
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Input_Loan, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", phone);
        mStringRequest.add("Token", token);
        mStringRequest.add("CreditTypeId", typeId);
        mStringRequest.add("SimpleQuotaLimit", loadLimit);

        Log.e(TAG, "CreditTypeId=" + typeId);
        Log.e(TAG, "SimpleQuotaLimit=" + loadLimit);
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(), 3900, TAG);
                    ApiMallUploadLoadModel model = null;
                    try {
                        model = new Gson().fromJson(response.get(), ApiMallUploadLoadModel.class);
                        if (model.Status == 1) {
                            EventBus.getDefault().post("bond_success", "bond_success");
                            commonTitleTv.setText("我期望的贷款金额");

                            mAlertDialog.show();
                            mAlertDialog.setTitle("选择成功,请等待业务人员与您联系.");
                            mAlertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            mAlertDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

//                            new MaterialDialog.Builder(getActivity()).title("选择成功,请等待业务人员与您联系.").
//                                    onPositive(new MaterialDialog.SingleButtonCallback() {
//                                        @Override
//                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                            dialog.dismiss();
//
//                                        }
//                                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();

                        } else {

                        }
                        PhoneUtils.toast(getActivity(), model.Msg.toString());
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
                //PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });

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

    @OnClick({ R.id.carquota_tv_recommit,R.id.carquota_tv_recommit_td,R.id.carquota_tv_td_his,
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
            case R.id.carquota_tv_recommit_td:

                httpGetTdStatus();



                break;
            case R.id.carquota_tv_td_his://历史记录
                intent=new Intent(getActivity(),TdHisActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 查看一下同盾报告的状态
     */
    private void httpGetTdStatus() {
        showLoading(getActivity());
        StringRequest mStringRequest = new StringRequest(Constant.Url_Main, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",phone);
        mStringRequest.add("Token",token);
        mStringRequest.add("Longitude",locationLng);
        mStringRequest.add("Latitude",locationLat);

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
                        Intent intent = null;
                        if (model.Status==1){
                            //setHttpData(model);
                            isTdReportTiming=model.IsExistTongDunReport;
                            if (isTdReportTiming){//存在同盾报告  或者查询过同盾报告  还未过期
                                intent=new Intent(getActivity(),QueryTDExistActivity.class);
                                startActivity(intent);
                            }else { //否则  重新调用支付接口  重新支付
                                intent=new Intent(getActivity(),QueryTDPayActivity.class);
                                startActivity(intent);
                            }

                        }else if (model.Status==2){
                            if (model.NeedReLogin){
                                gotoLogin(getActivity(),true);
                            }
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
                //PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });

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

                            httpGetProductType();//获取贷款列表
                        }else if (model.Status==2){
                                if (model.NeedReLogin){
                                    gotoLogin(getActivity(),true);
                            }
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
                //PhoneUtils.toast(getActivity(),"网络请求失败,请检查网络后重试");
            }
        });
    }

    /**
     * 设置网络数据
     * @param model
     */
    private void setHttpData(final ApiMainModel model) {
        if (model.IsVaildateBaseInfo){
            SharedPreferencesUtils.putShared(getActivity(),Constant.Is_Exist_Td_Info,"yes");
        }

        if (model.IsExistTongDunReport){
            SharedPreferencesUtils.putShared(getActivity(),Constant.User_Is_Have_QueryTd,"yes");
        }


        if (model.IsUpdateExpectation){
            isChooseProduct=true;
        }

        if (model.User.SimpleQuotaLimit>0){
            carquotaTvPrice.setText("￥"+String.valueOf(model.User.SimpleQuotaLimit));

        }else {
            carquotaTvPrice.setText("￥"+String.valueOf(model.MaxAmount));
        }
        Log.e(TAG,"isChooseProduct="+isChooseProduct);

        isTdReportTiming=model.IsExistTongDunReport;

        if (model.User.IdentityCardOCRAuthentication==true){
            carquotaIdCard.setText("认证成功");
            //carquotaIdCard.setClickable(false);
        }else {
            carquotaIdCard.setText("立即认证");
        }

        if (model.User.Carrieroperator!=null){
            carquotaTvPhone.setText("认证成功");
            //carquotaTvPhone.setClickable(false);
        }else {
            carquotaTvPhone.setText("立即认证");
        }

        if (model.BankCardIsAuthentication==true){
            carquotaTvBank.setText("认证成功");
            //carquotaTvBank.setClickable(false);
        }else {
            carquotaTvBank.setText("立即认证");
        }

//        Log.e(TAG,"model.AppVersion.Version="+model.AppVersion.Version);
//        Log.e(TAG,"Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance()))="+
//                Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance())));

        //SharedPreferencesUtils.putShared(getActivity(),Constant.App_Version_Code,model.AppVersion.Version);
        Log.e(TAG,"PhoneUtils.getVersionCode(MyApplication.getInstance()="+PhoneUtils.getVersionCode(MyApplication.getInstance()));
        if (!TextUtils.isEmpty(PhoneUtils.getVersionCode(MyApplication.getInstance()))){
            //versionCode= (double) SharedPreferencesUtils.getShared(MainActivity.this,Constant.App_Version_Code,0.0);
            //检查更新
            if (Double.valueOf(model.AppVersion.Version)>Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance()))) {
                Log.e(TAG, "VersionParams");
                Log.e(TAG,"versionCode="+Double.valueOf(model.AppVersion.Version));
                        Log.e(TAG,"PhoneUtils.getVersionCode(MyApplication.getInstance())="
                                +Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance())));
//                VersionParams.Builder builder = new VersionParams.Builder()
//                        .setRequestUrl("https://www.pgyer.com/RtL8")
//                        .setTitle("版本更新："+model.AppVersion.VersionName)
//                        .setUpdateMsg(model.AppVersion.HighLight)
//                        .setDownloadUrl(model.AppVersion.DownloadURL)
//                        .setService(LoadService.class);
//                AllenChecker.startVersionCheck(MyApplication.getInstance().getApplicationContext(), builder.build());

                mAlertDialog.setTitle("发现新版本,立即更新?");
                mAlertDialog.setMessage("更新说明:"+model.AppVersion.HighLight);
                mAlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent intent=new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra(Constant.WebUrl,model.AppVersion.DownloadURL);
//                        intent.putExtra(Constant.WebTitle,"下载App");
//                        startActivity(intent);
                        Uri uri = Uri.parse(model.AppVersion.DownloadURL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mAlertDialog.show();


            }
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
