package com.eiga.an.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eiga.an.R;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiMallLoadTypeModel;
import com.eiga.an.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RyanLee on 2017/12/7.
 */

public class MallRecyclerAdapter extends RecyclerView.Adapter<MallRecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<ApiMallLoadTypeModel.CreditTypesBean> mDatas=new ArrayList<>();
    private ViewGroup mParent;
    private String TAG=getClass().getName();

    public MallRecyclerAdapter(Context mContext, List<ApiMallLoadTypeModel.CreditTypesBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        Log.e(TAG,"mDatas="+mDatas.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mParent = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.mall_item, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        // 需要增加此代码修改每一页的宽高
        //GalleryAdapterHelper.newInstance().setItemLayoutParams(mParent, holder.itemView, position, getItemCount());
        GlideUtils.getGlideUtils().glideImage(mContext, Constant.Url_Common + mDatas.get(position).Photo, holder.mView);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public final ImageView mView;

        public MyHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.mall_item_image);
        }
    }

    /**
     * 获取position位置的resId
     * @param position
     * @return
     */
//    public int getResId(int position) {
//        return mDatas == null ? 0 : mDatas.get(position);
//    }
}
