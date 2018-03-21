package com.eiga.an.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eiga.an.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.albert.autosystembar.SystemBarHelper;


public class KeeperFragment extends Fragment {


    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_keeper, null);
            new SystemBarHelper.Builder()
                    .enableImmersedStatusBar(true)
                    .enableImmersedNavigationBar(true)
                    .into(getActivity());

            findViews();
        }

        return mRootView;

    }


    private void findViews() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
