package com.eiga.an.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.model.Constant;
import com.eiga.an.ui.activity.user.SettingActivity;
import com.eiga.an.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MyCenterFragment extends Fragment {
    @BindView(R.id.common_title_back)
    RelativeLayout commonTitleBack;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.my_head)
    ImageView myHead;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.my_grade)
    TextView myGrade;
    Unbinder unbinder;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mycenter, null);
        }


        unbinder = ButterKnife.bind(this, mRootView);
        findViews();
        return mRootView;
    }

    private void findViews() {
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("个人中心");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.my_up_grade, R.id.my_car_layout,R.id.my_setting,
            R.id.my_insurance_layout,R.id.my_logout})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.my_setting:
                intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_logout:
                break;
            case R.id.my_up_grade:
                break;
            case R.id.my_car_layout:
                doLogout();
                break;
            case R.id.my_insurance_layout:
                break;
        }
    }

    /**
     * 退出登录的事件
     */
    private void doLogout() {
        SharedPreferencesUtils.clearSp(getActivity(),Constant.User_Login_Name);
    }

}
