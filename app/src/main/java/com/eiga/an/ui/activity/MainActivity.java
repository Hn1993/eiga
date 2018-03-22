package com.eiga.an.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;


import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.ui.fragment.KeeperFragment;
import com.eiga.an.ui.fragment.MainFragment;
import com.eiga.an.ui.fragment.MallFragment;
import com.eiga.an.ui.fragment.MyCenterFragment;
import com.eiga.an.view.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.albert.autosystembar.SystemBarHelper;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_vp)
    NoScrollViewPager mainVp;
    @BindView(R.id.main_tab)
    android.support.design.widget.TabLayout mainTab;

    private android.support.design.widget.TabLayout.Tab tabMain,tabMall,TabKeeper,tabMyCenter;

    private String[] mTitles=new String[]{"首页","商城","信用管家","个人中心"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoVirtualKeys();//华为等底部虚拟按键的手机
        ButterKnife.bind(this);

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
                }else if (position==2){
                    return new KeeperFragment();
                }else {
                    return new MyCenterFragment();
                }

            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });

        mainTab.setupWithViewPager(mainVp);
        tabMain=mainTab.getTabAt(0);
        tabMyCenter=mainTab.getTabAt(1);
        tabMall=mainTab.getTabAt(2);
        TabKeeper=mainTab.getTabAt(3);

        tabMain.setIcon(R.drawable.tab_main);
        tabMyCenter.setIcon(R.drawable.tab_mall);
        tabMall.setIcon(R.drawable.tab_keeper);
        TabKeeper.setIcon(R.drawable.tab_mycenter);

    }
}
