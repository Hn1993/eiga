package com.eiga.an.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Left0ItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public Left0ItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        //outRect.top = space;
        outRect.bottom = space;
        //上下都设置边距  中间是有2倍边距  所以最上面一个litem设置上边距就ok
        if(parent.getChildPosition(view) == 0)
            outRect.top = space ;
        else
            outRect.top = 0;
    }
}
