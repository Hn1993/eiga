package com.eiga.an.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiga.an.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MallFragment extends Fragment {


    @BindView(R.id.common_title_back)
    RelativeLayout commonTitleBack;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    Unbinder unbinder;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mall, null);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        findViews();
        return mRootView;

    }


    private void findViews() {
        commonTitleBack.setVisibility(View.GONE);
        commonTitleTv.setText("商城");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();


    }
}
