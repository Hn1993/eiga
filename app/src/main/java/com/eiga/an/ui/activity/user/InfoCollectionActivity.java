package com.eiga.an.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.adapter.TagAdapter;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiInfoUploadModel;
import com.eiga.an.model.jsonModel.ApiInfoCollectModel;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.view.tagView.FlowTagLayout;
import com.eiga.an.view.tagView.OnTagSelectListener;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择条件  评估
 */
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

    private FlowTagLayout vp1TagTop0, vp1TagTop, vp1TagBottom, vp2TagTop, vp3TagTop, vp2TagBottom, vp3TagBottom, vp3TagCenter;
    private TagAdapter<String> vp1topTagAdapter;
    private TagAdapter<String> vp1top0TagAdapter;
    private TagAdapter<String> vp2topTagAdapter;
    private TagAdapter<String> vp3topTagAdapter;
    private TagAdapter<String> vp1bottomTagAdapter;
    private TagAdapter<String> vp2bottomTagAdapter;
    private TagAdapter<String> vp3bottomTagAdapter;
    private TagAdapter<String> vp3centerTagAdapter;

    private TextView vp1TagTitle0,vp1TagTitle1,vp1TagTitle2,vp2TagTitle0,
            vp2TagTitle1,vp3TagTitle0,vp3TagTitle1,vp3TagTitle2;

    private Switch mSwitch;

    private List<String> selectIds=new ArrayList<>();

    private List<ApiInfoCollectModel.DataBean> dataList=new ArrayList<>();
    private HashMap<String,String> map=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infocollection);
        setImmersedNavigationBar(this,R.color.white);
        autoVirtualKeys();
        ButterKnife.bind(this);
        findViews();

        httpGetInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void httpGetInfo() {
        Log.e(TAG,"httpGetInfo=");
        showLoading();
        StringRequest mStringRequest = new StringRequest(Constant.Url_Info_Collection, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);

                    ApiInfoCollectModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiInfoCollectModel.class);
                        if (model.Status==1){
                            setHttpData(model.Data);
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
                PhoneUtils.toast(InfoCollectionActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }

    private void setHttpData(List<ApiInfoCollectModel.DataBean> data) {
        dataList=data;


        Log.e(TAG,"data.size="+data.size());

        vp1TagTitle0.setText(data.get(0).CateGoryName);
        vp1TagTitle1.setText(data.get(1).CateGoryName);
        vp1TagTitle2.setText(data.get(2).CateGoryName);

        initVp1Top0TagData(data.get(0).CateGoryName,data.get(0).QuotaItemList);
        initVp1TopTagData(data.get(1).CateGoryName,data.get(1).QuotaItemList);
        initVp1BottomTagData(data.get(2).CateGoryName,data.get(2).QuotaItemList);

        vp2TagTitle0.setText(data.get(3).CateGoryName);
        vp2TagTitle1.setText(data.get(4).CateGoryName);
        initVp2TopTagData(data.get(3).CateGoryName,data.get(3).QuotaItemList);
        initVp2BottomTagData(data.get(4).CateGoryName,data.get(4).QuotaItemList);



        vp3TagTitle0.setText(data.get(5).CateGoryName);
        vp3TagTitle1.setText(data.get(6).CateGoryName);
        vp3TagTitle2.setText(data.get(7).CateGoryName);
        initVp3TopTagData(data.get(5).CateGoryName,data.get(5).QuotaItemList);
        initVp3CenterTagData(data.get(6).CateGoryName,data.get(6).QuotaItemList);
        initVp3BottomTagData(data.get(7).CateGoryName,data.get(7).QuotaItemList);


    }

    /**
     * 根据三个参数来改变map里的值
     * @param title  获取map里的list
     * @param title_index  获取第几个tag
     * @param index 获取tag里的list的第几个的值
     */
    private void updateSelectIds(String title,int title_index,int index) {
        map.put(title,String.valueOf(dataList.get(title_index).QuotaItemList.get(index).Id));
        Log.e(TAG,"map="+map.get(title));
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

        mSwitch=pagerView.get(0).findViewById(R.id.vp1_switch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"isChecked="+isChecked);
                if (isChecked){
                    updateSelectIds(dataList.get(0).CateGoryName,0,1);
                }else {
                    updateSelectIds(dataList.get(0).CateGoryName,0,0);
                }

            }
        });

        vp1TagTop = pagerView.get(0).findViewById(R.id.vp1_tag_top);
        vp1TagTop0 = pagerView.get(0).findViewById(R.id.vp1_tag_top0);
        vp2TagTop = pagerView.get(1).findViewById(R.id.vp2_tag_top);
        vp3TagTop = pagerView.get(2).findViewById(R.id.vp3_tag_top);
        vp1TagBottom = pagerView.get(0).findViewById(R.id.vp1_tag_bottom);
        vp2TagBottom = pagerView.get(1).findViewById(R.id.vp2_tag_bottom);
        vp3TagBottom = pagerView.get(2).findViewById(R.id.vp3_tag_bottom);
        vp3TagCenter = pagerView.get(2).findViewById(R.id.vp3_tag_center);

        vp1TagTitle0=pagerView.get(0).findViewById(R.id.vp1_tv_top0);
        vp1TagTitle1=pagerView.get(0).findViewById(R.id.vp1_tv_top);
        vp1TagTitle2=pagerView.get(0).findViewById(R.id.vp1_tv_bottom);
        vp2TagTitle0=pagerView.get(1).findViewById(R.id.vp2_tv_top);
        vp2TagTitle1=pagerView.get(1).findViewById(R.id.vp2_tv_bottom);
        vp3TagTitle0=pagerView.get(2).findViewById(R.id.vp3_tv_top);
        vp3TagTitle1=pagerView.get(2).findViewById(R.id.vp3_tv_center);
        vp3TagTitle2=pagerView.get(2).findViewById(R.id.vp3_tv_bottom);

        vp1topTagAdapter = new TagAdapter<>(this);
        vp1top0TagAdapter = new TagAdapter<>(this);
        vp2topTagAdapter = new TagAdapter<>(this);
        vp3topTagAdapter = new TagAdapter<>(this);
        vp1bottomTagAdapter = new TagAdapter<>(this);
        vp2bottomTagAdapter = new TagAdapter<>(this);
        vp3bottomTagAdapter = new TagAdapter<>(this);
        vp3centerTagAdapter = new TagAdapter<>(this);

        vp1TagTop.setAdapter(vp1topTagAdapter);
        vp1TagTop0.setAdapter(vp1top0TagAdapter);
        vp2TagTop.setAdapter(vp2topTagAdapter);
        vp3TagTop.setAdapter(vp3topTagAdapter);
        vp1TagBottom.setAdapter(vp1bottomTagAdapter);
        vp2TagBottom.setAdapter(vp2bottomTagAdapter);
        vp3TagBottom.setAdapter(vp3bottomTagAdapter);
        vp3TagCenter.setAdapter(vp3centerTagAdapter);

        vp1TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp1TagTop0.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp2TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagTop.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp1TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp2TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagBottom.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        vp3TagCenter.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);

        vp1TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                Log.e(TAG,"selectedList="+selectedList.get(0));
                //selectIds.set(0,)
                updateSelectIds(dataList.get(1).CateGoryName,1,selectedList.get(0));
            }
        });

        vp1TagTop0.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                Log.e(TAG,"selectedList="+selectedList.get(0));
                updateSelectIds(dataList.get(0).CateGoryName,0,selectedList.get(0));
            }
        });

        vp2TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                updateSelectIds(dataList.get(3).CateGoryName,3,selectedList.get(0));
            }
        });
        vp3TagTop.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                updateSelectIds(dataList.get(5).CateGoryName,5,selectedList.get(0));
            }
        });
        vp1TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                Log.e(TAG,"selectedList="+selectedList.get(0));
                updateSelectIds(dataList.get(2).CateGoryName,2,selectedList.get(0));
            }
        });
        vp2TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                updateSelectIds(dataList.get(4).CateGoryName,4,selectedList.get(0));
            }
        });
        vp3TagBottom.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                updateSelectIds(dataList.get(7).CateGoryName,7,selectedList.get(0));
            }
        });
        vp3TagCenter.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                updateSelectIds(dataList.get(6).CateGoryName,6,selectedList.get(0));
            }
        });




    }



    private void initVp1Top0TagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp1top0TagAdapter.onlyAddAll(dataSource);

    }

    private void initVp3CenterTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp3centerTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp3BottomTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp3bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp2BottomTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp2bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp3TopTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp3topTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp2TopTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        String selectId;
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        selectId=data.get(0).Id;
        map.put(title,selectId);
        vp2topTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp1BottomTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        String selectId=data.get(0).Id;
        map.put(title,selectId);
        vp1bottomTagAdapter.onlyAddAll(dataSource);
    }

    private void initVp1TopTagData(String title,List<ApiInfoCollectModel.DataBean.QuotaItemListBean> data) {
        List<String> dataSource = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            dataSource.add(data.get(i).QuotaItemName);
        }
        String selectId=data.get(0).Id;
        map.put(title,selectId);
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
                httpUploadChoosedInfo();
                break;
        }
    }

    /**
     * 上传选中的  信息的id
     */
    private void httpUploadChoosedInfo() {
        String ids="";
        for (int i = 0; i < dataList.size(); i++) {
            ids+=map.get(dataList.get(i).CateGoryName)+",";
        }
        Log.e(TAG,"ids="+(ids.substring(0,ids.length()-1)));
        Log.e(TAG,"phone="+(String) SharedPreferencesUtils.getShared(InfoCollectionActivity.this,Constant.User_Login_Name,""));
        Log.e(TAG,"token="+(String) SharedPreferencesUtils.getShared(InfoCollectionActivity.this,Constant.User_Login_Token,""));

        StringRequest mStringRequest = new StringRequest(Constant.Url_Info_Collection_Upload, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", (String) SharedPreferencesUtils.getShared(InfoCollectionActivity.this,Constant.User_Login_Name,""));
        mStringRequest.add("Token", (String) SharedPreferencesUtils.getShared(InfoCollectionActivity.this,Constant.User_Login_Token,""));
        mStringRequest.add("QuotaItemIds", ids.substring(0,ids.length()-1));

        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);

                    ApiInfoUploadModel model=null;

                    try {
                        model=new Gson().fromJson(response.get(),ApiInfoUploadModel.class);
                        if (model.Status==1){
                            EventBus.getDefault().post("bond_success","bond_success");

                            SharedPreferencesUtils.putShared(InfoCollectionActivity.this,Constant.User_Is_Have_Evaluation,model.Quota);
                            Intent intent=new Intent(InfoCollectionActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (model.Status==2){
                            //SharedPreferencesUtils.putShared(InfoCollectionActivity.this,Constant.User_Is_Have_Evaluation,model.Quota);
                            Intent intent=new Intent(InfoCollectionActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            PhoneUtils.toast(InfoCollectionActivity.this,model.Msg);
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
                PhoneUtils.toast(InfoCollectionActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }
}
