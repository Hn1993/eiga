package com.eiga.an.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.adapter.TagAdapter;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.view.tagView.FlowTagLayout;
import com.eiga.an.view.tagView.OnTagSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.albert.autosystembar.SystemBarHelper;

public class InfoCollectionActivity extends BaseActivity {

    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.ac_info_vp)
    ViewPager acInfoVp;
    @BindView(R.id.ac_info_vp_dot)
    LinearLayout acInfoVpDot;
    @BindView(R.id.ac_info_tv_go)
    TextView acInfoTvGo;
    private String TAG = getClass().getName();

    private List<View> pagerView = new ArrayList<>();

    private ImageView[] dotViews;

    private FlowTagLayout vp1TagTop, vp1TagBottom, vp2TagTop, vp3TagTop, vp2TagBottom, vp3TagBottom, vp3TagCenter;
    private TagAdapter<String> vp1topTagAdapter;
    private TagAdapter<String> vp2topTagAdapter;
    private TagAdapter<String> vp3topTagAdapter;
    private TagAdapter<String> vp1bottomTagAdapter;
    private TagAdapter<String> vp2bottomTagAdapter;
    private TagAdapter<String> vp3bottomTagAdapter;
    private TagAdapter<String> vp3centerTagAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infocollection);
        autoVirtualKeys();
        new SystemBarHelper.Builder()
                .enableImmersedNavigationBar(true)
                .enableImmersedStatusBar(false)
                .statusBarColor(getResources().getColor(R.color.white))
                .into(this);
        ButterKnife.bind(this);
        findViews();
    }

    private void findViews() {
        commonTitleTv.setText("汽车贷款评估");
        pagerView.add(getLayoutInflater().inflate(R.layout.layout_info_vp1, null));
        pagerView.add(getLayoutInflater().inflate(R.layout.layout_info_vp2, null));
        pagerView.add(getLayoutInflater().inflate(R.layout.layout_info_vp3, null));

        //初始化小原点
        dotViews = new ImageView[pagerView.size()];
        initDots(pagerView, acInfoVpDot, dotViews);

        acInfoVp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pagerView.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
            //对超出范围的资源进行销毁


            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                //super.destroyItem(container, position, object);
                container.removeView(pagerView.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                //return super.instantiateItem(container, position);
                container.addView(pagerView.get(position));
                return pagerView.get(position);
            }
        });

        acInfoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        commonTitleTv.setText("汽车贷款评估");
                        break;
                    case 1:
                        commonTitleTv.setText("选择我的个人资质");
                        break;
                    case 2:
                        commonTitleTv.setText("选择我的购车方案");
                        break;
                }
                for (int i = 0; i < dotViews.length; i++) {
                    if (position == i)//必须要用取余  不然会越界
                    {
                        dotViews[i].setSelected(true);
                    } else {
                        dotViews[i].setSelected(false);
                    }
                }

                if (position==2){
                    acInfoVpDot.setVisibility(View.GONE);
                    acInfoTvGo.setVisibility(View.VISIBLE);
                }else {
                    acInfoVpDot.setVisibility(View.VISIBLE);
                    acInfoTvGo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp1TagTop = pagerView.get(0).findViewById(R.id.vp1_tag_top);
        vp2TagTop = pagerView.get(1).findViewById(R.id.vp2_tag_top);
        vp3TagTop = pagerView.get(2).findViewById(R.id.vp3_tag_top);
        vp1TagBottom = pagerView.get(0).findViewById(R.id.vp1_tag_bottom);
        vp2TagBottom = pagerView.get(1).findViewById(R.id.vp2_tag_bottom);
        vp3TagBottom = pagerView.get(2).findViewById(R.id.vp3_tag_bottom);
        vp3TagCenter = pagerView.get(2).findViewById(R.id.vp3_tag_center);

        vp1topTagAdapter = new TagAdapter<>(this);
        vp2topTagAdapter = new TagAdapter<>(this);
        vp3topTagAdapter = new TagAdapter<>(this);
        vp1bottomTagAdapter = new TagAdapter<>(this);
        vp2bottomTagAdapter = new TagAdapter<>(this);
        vp3bottomTagAdapter = new TagAdapter<>(this);
        vp3centerTagAdapter = new TagAdapter<>(this);

        vp1TagTop.setAdapter(vp1topTagAdapter);
        vp2TagTop.setAdapter(vp2topTagAdapter);
        vp3TagTop.setAdapter(vp3topTagAdapter);
        vp1TagBottom.setAdapter(vp1bottomTagAdapter);
        vp2TagBottom.setAdapter(vp2bottomTagAdapter);
        vp3TagBottom.setAdapter(vp3bottomTagAdapter);
        vp3TagCenter.setAdapter(vp3centerTagAdapter);
        vp1TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp2TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp1TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp2TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagCenter.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);

        vp1TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp2TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp3TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp1TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp2TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp3TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });
        vp3TagCenter.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

            }
        });


        initVp1TopTagData();
        initVp2TopTagData();
        initVp3TopTagData();
        initVp1BottomTagData();
        initVp2BottomTagData();
        initVp3BottomTagData();
        initVp3CenterTagData();
    }

    private void initVp3CenterTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("信用良好");
        dataSource.add("无信用记录");
        dataSource.add("少数逾期");
        dataSource.add("多次逾期");
        vp3centerTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp3BottomTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("有社保");
        dataSource.add("无社保");
        vp3bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp2BottomTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("3千以下");
        dataSource.add("3-5千");
        dataSource.add("5-8千");
        dataSource.add("8千-1万2");
        dataSource.add("1万2-2万");
        dataSource.add("2万以上");
        vp2bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp3TopTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("租房");
        dataSource.add("有房有贷");
        dataSource.add("有房无贷");
        vp3topTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp2TopTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("上班族");
        dataSource.add("事业单位");
        dataSource.add("企业主");
        dataSource.add("个体工商");
        dataSource.add("自由职业");
        vp2topTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp1BottomTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("10%");
        dataSource.add("20%");
        dataSource.add("30%");
        dataSource.add("40%");
        dataSource.add("50%");
        dataSource.add("60%");
        vp1bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp1TopTagData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("5万以下");
        dataSource.add("5-10万");
        dataSource.add("10-15万");
        dataSource.add("15-20万");
        dataSource.add("20-30万");
        dataSource.add("30万以上");
        vp1topTagAdapter.onlyAddAll(dataSource);
    }


    /**
     * 根据引导页的数量，动态生成相应数量的导航小圆点，并添加到LinearLayout中显示。
     */
    private void initDots(List imageData, LinearLayout dotLayout, ImageView[] dotViews) {
        Log.i(TAG, "initDots " + imageData.size());
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(40, 0, 40, 0);//设置小圆点左右之间的间隔
        //判断有没有添加过  避免一直添加
        //if (dotLayout.get){
        dotLayout.removeAllViews();
        for (int i = 0; i < imageData.size(); i++) {
            ImageView imageView = new ImageView(InfoCollectionActivity.this);
            //ImageView imageView = new ImageView(BaseActivity.getBaseActivity());
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.vp_selector);
            if (i == 0) {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            dotLayout.addView(imageView);//添加到布局里面显示
        }


    }


    @OnClick({R.id.common_title_back, R.id.ac_info_tv_go})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            case R.id.ac_info_tv_go:
                intent=new Intent(InfoCollectionActivity.this,CarQuotaActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
