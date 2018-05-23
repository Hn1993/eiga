package com.eiga.an.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/5/23.
 */

public class PreImageActivity extends BaseActivity {
    @BindView(R.id.sales_title_tv)
    TextView salesTitleTv;
    @BindView(R.id.sales_title_notice)
    RelativeLayout salesTitleNotice;
    @BindView(R.id.preimage_position)
    TextView preimagePosition;
    @BindView(R.id.preimage_vp)
    ViewPager preimageVp;
    private String TAG = getClass().getName();

    private List<View> viewList=new ArrayList<>();
    private ArrayList<String> mPhotos=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preimage);
        ButterKnife.bind(this);
        autoVirtualKeys();
        setImmersedNavigationBar(this, R.color.light_blue);

        findViews();
    }

    private void findViews() {

        salesTitleTv.setText("预览图片");
        salesTitleNotice.setVisibility(View.GONE);

    }

    private PagerAdapter mAdapter=new PagerAdapter() {


        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };


    @OnClick(R.id.sales_title_back)
    public void onViewClicked() {
        finish();
    }
}
