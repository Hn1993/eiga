package com.eiga.an.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eiga.an.R;
import com.eiga.an.adapter.MallRecyclerAdapter;
import com.eiga.an.base.BaseFragment;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMallLoadTypeModel;
import com.eiga.an.model.jsonModel.ApiMallUploadLoadModel;
import com.eiga.an.utils.GlideUtils;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.eiga.an.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MallFragment extends BaseFragment {


    @BindView(R.id.common_title_back)
    RelativeLayout commonTitleBack;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;

    @BindView(R.id.chanpin1_image)
    ImageView chanpin1Image;
    @BindView(R.id.chanpin2_image)
    ImageView chanpin2Image;
//    @BindView(R.id.mall_gallery_rv)
//    GalleryRecyclerView mallGalleryRv;

    Unbinder unbinder;
    private View mRootView;
    private String userPhone, userToken;
    private String typeId, typeMoney;

    private List<ApiMallLoadTypeModel.CreditTypesBean> mTypeList = new ArrayList<>();
    private String TAG = getClass().getName();

    private RecyclerAdapter<ApiMallLoadTypeModel.CreditTypesBean> mRvAdapter;

    private MallRecyclerAdapter mallRecyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mall, null);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        userPhone = (String) SharedPreferencesUtils.getShared(getActivity(), Constant.User_Login_Name, "");
        userToken = (String) SharedPreferencesUtils.getShared(getActivity(), Constant.User_Login_Token, "");
        findViews();
        return mRootView;

    }


    private void findViews() {
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("商城");



        httpGetProductType();
    }

    private void httpGetProductType() {

        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Loan_Type, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", userPhone);
        mStringRequest.add("Token", userToken);
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
                            setHttpData(model);
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

    private void setHttpData(ApiMallLoadTypeModel model) {
        mTypeList = model.CreditTypes;
        Log.e(TAG,"model.CreditTypes="+model.CreditTypes.size());
//        GlideUtils.getGlideUtils().glideImage(getActivity(), Constant.Url_Common + model.CreditTypes.get(0).Photo, chanpin1Image);
//        GlideUtils.getGlideUtils().glideImage(getActivity(), Constant.Url_Common + model.CreditTypes.get(1).Photo, chanpin2Image);


//        mRvAdapter=new RecyclerAdapter<ApiMallLoadTypeModel.CreditTypesBean>(R.layout.mall_item,model.CreditTypes) {
//            @Override
//            protected void onBindData(RecyclerViewHolder viewHolder, int position, ApiMallLoadTypeModel.CreditTypesBean item) {
//
//                ImageView imageView=viewHolder.findViewById(R.id.mall_item_image);
//                GlideUtils.getGlideUtils().glideImage(getActivity(), Constant.Url_Common + item.Photo, imageView);
//            }
//        };




//        mallRecyclerAdapter=new MallRecyclerAdapter(getActivity(),model.CreditTypes);
//        mallGalleryRv=mRootView.findViewById(R.id.mall_gallery_rv);
//        mallGalleryRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        //mallGalleryRv.addItemDecoration(new SpacesItemDecoration(10));
//        mallGalleryRv.setAdapter(mallRecyclerAdapter);
//        mallGalleryRv.initFlingSpeed(9000)                                   // 设置滑动速度（像素/s）
//                .initPageParams(0, 60)     // 设置页边距和左右图片的可见宽度，单位dp
//                .setAnimFactor(0.15f)                                   // 设置切换动画的参数因子
//                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)            // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
//                .setOnItemClickListener(new GalleryRecyclerView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int i) {
//
//                    }
//                });                          // 设置点击事件

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();


    }

    @OnClick({R.id.chanpin1, R.id.chanpin2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chanpin1:
                //httpUploadLoad();
                //.content(R.string.input_content)
                new MaterialDialog.Builder(getActivity())
                        .title("请输入你期望的贷款额度")

                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("请输入你期望的贷款额度", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                typeMoney = input.toString();
                            }
                        }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!TextUtils.isEmpty(dialog.getInputEditText().getText().toString())) {
                            httpUploadLoad(String.valueOf(mTypeList.get(0).Id), dialog.getInputEditText().getText().toString());
                        } else {
                            PhoneUtils.toast(getActivity(), "请输入您期望的贷款额度");
                        }
                    }
                })
                        .show();
                break;
            case R.id.chanpin2:
                new MaterialDialog.Builder(getActivity())
                        .title("请输入你期望的贷款额度")

                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("请输入你期望的贷款额度", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                //typeMoney=input.toString();
                            }
                        }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (!TextUtils.isEmpty(dialog.getInputEditText().getText().toString())) {
                            httpUploadLoad(String.valueOf(mTypeList.get(1).Id), dialog.getInputEditText().getText().toString());
                        } else {
                            PhoneUtils.toast(getActivity(), "请输入您期望的贷款额度");
                        }

                    }
                })
                        .show();
                break;
        }
    }

    private void httpUploadLoad(String typeId, String loadLimit) {
        //showLoading(getActivity());
        StringRequest mStringRequest = new StringRequest(Constant.Url_User_Input_Loan, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone", userPhone);
        mStringRequest.add("Token", userToken);
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
}
