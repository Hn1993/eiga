package com.eiga.an.ui.activity.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;


import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.base.MyApplication;
import com.eiga.an.model.Constant;
import com.eiga.an.service.LoadService;
import com.eiga.an.ui.activity.sales.SalesMainActivity;
import com.eiga.an.ui.fragment.MainFragment;
import com.eiga.an.ui.fragment.MyCenterFragment;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.NoScrollViewPager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private String TAG=getClass().getName();
    @BindView(R.id.main_vp)
    NoScrollViewPager mainVp;
    @BindView(R.id.main_tab)
    android.support.design.widget.TabLayout mainTab;

    private android.support.design.widget.TabLayout.Tab tabMain,tabMall,TabKeeper,tabMyCenter;

    //,"信用管家"
    //private String[] mTitles=new String[]{"首页","商城","个人中心"};
    private String[] mTitles=new String[]{"首页","个人中心"};

    private String isFirstOpenApp,userName;
    private double versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoVirtualKeys();//华为等底部虚拟按键的手机
        ButterKnife.bind(this);
        setImmersedNavigationBar(this,R.color.white);
        EventBus.getDefault().register(this);

        isFirstOpenApp= (String) SharedPreferencesUtils.getShared(this,Constant.IsFirstOpenApp,"");
        Log.e(TAG,"isFirstOpenApp="+isFirstOpenApp);
        if (TextUtils.isEmpty(isFirstOpenApp)){
            Intent intent=new Intent(this, ChooseIdentityActivity.class);
            startActivity(intent);
            finish();

        }else {
            if (isFirstOpenApp.equals("1")){
                Intent intent=new Intent(this, SalesMainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        AndPermission.with(this)
                .permission(
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //成功
                        Log.e(TAG,"onGranted"+permissions.get(0).toString());
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //失败
                        Log.e(TAG,"onDenied"+permissions.get(0).toString());
                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        //失败后需要弹出一个dialog
                        // 如果用户继续：
                        executor.execute();
                        // 如果用户中断：
                        executor.cancel();
                    }
                }).start();

        findViews();

    }

    private void findViews() {

        mainVp.setScanScroll(false);
        mainVp.setOffscreenPageLimit(0);
        mainVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if (position==0){
                    return new MainFragment();
                }

//                else if (position==1){
//                    return new MallFragment();
//                }
//                else if (position==2){
//                    return new KeeperFragment();
//                }
                else {
                    return new MyCenterFragment();
                }

            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                //return mTitles[position];
                return "";
            }
        });

        mainTab.setupWithViewPager(mainVp);
        tabMain=mainTab.getTabAt(0);
        //tabMall=mainTab.getTabAt(1);
        tabMyCenter=mainTab.getTabAt(1);
        //TabKeeper=mainTab.getTabAt(3);

        tabMain.setIcon(R.drawable.tab_main);
        //tabMall.setIcon(R.drawable.tab_mall);
        tabMyCenter.setIcon(R.drawable.tab_mycenter);
        //TabKeeper.setIcon(R.drawable.tab_mycenter);



//        if (!TextUtils.isEmpty(PhoneUtils.getVersionCode(MyApplication.getInstance()))&&
//                !TextUtils.isEmpty((String) SharedPreferencesUtils.getShared(MainActivity.this,Constant.App_Version_Code,""))){
//            versionCode= (double) SharedPreferencesUtils.getShared(MainActivity.this,Constant.App_Version_Code,0.0);
//
//            Log.e(TAG,"versionCode="+versionCode);
//            Log.e(TAG,"PhoneUtils.getVersionCode(MyApplication.getInstance())="
//                    +Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance())));
//            //检查更新
//            if (versionCode>Double.valueOf(PhoneUtils.getVersionCode(MyApplication.getInstance()))){
//                Log.e(TAG,"VersionParams");
//                VersionParams.Builder builder = new VersionParams.Builder()
//                        .setRequestUrl(Constant.Url_Main)
//                        .setTitle("版本更新：")
//                        .setUpdateMsg("1111")
//                        .setDownloadUrl("https://www.pgyer.com/RtL8")
//                        .setService(LoadService.class);
//                AllenChecker.startVersionCheck(MainActivity.this, builder.build());
//
//            }
//
//        }

    }

    @Subscriber (tag = "up_grade")
    public void onUpGradeEvent(String str){
        mainVp.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
