package com.eiga.an.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/5/26.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public GridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        //outRect.top = space;
        outRect.bottom = 0;
        outRect.top = space*2 ;

    }
}
