package com.eiga.an.ui.activity.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;


import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.ChooseIdentityActivity;
import com.eiga.an.ui.activity.sales.SalesMainActivity;
import com.eiga.an.ui.fragment.KeeperFragment;
import com.eiga.an.ui.fragment.MainFragment;
import com.eiga.an.ui.fragment.MallFragment;
import com.eiga.an.ui.fragment.MyCenterFragment;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.NoScrollViewPager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.albert.autosystembar.SystemBarHelper;

public class MainActivity extends BaseActivity {

    private String TAG=getClass().getName();
    @BindView(R.id.main_vp)
    NoScrollViewPager mainVp;
    @BindView(R.id.main_tab)
    android.support.design.widget.TabLayout mainTab;

    private android.support.design.widget.TabLayout.Tab tabMain,tabMall,TabKeeper,tabMyCenter;

    //,"信用管家"
    private String[] mTitles=new String[]{"首页","商城","个人中心"};

    private String isFirstOpenApp,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoVirtualKeys();//华为等底部虚拟按键的手机
        ButterKnife.bind(this);
        setImmersedNavigationBar(this,R.color.white);

        isFirstOpenApp= (String) SharedPreferencesUtils.getShared(this,Constant.IsFirstOpenApp,"");
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
        mainVp.setOffscreenPageLimit(3);
        mainVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if (position==0){
                    return new MainFragment();
                }else if (position==1){
                    return new MallFragment();
                }
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
        tabMall=mainTab.getTabAt(1);
        tabMyCenter=mainTab.getTabAt(2);
        //TabKeeper=mainTab.getTabAt(3);

        tabMain.setIcon(R.drawable.tab_main);
        tabMall.setIcon(R.drawable.tab_mall);
        tabMyCenter.setIcon(R.drawable.tab_mycenter);
        //TabKeeper.setIcon(R.drawable.tab_mycenter);

    }
}
