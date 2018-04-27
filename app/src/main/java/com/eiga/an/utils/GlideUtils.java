package com.eiga.an.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.eiga.an.R;
import com.eiga.an.base.MyApplication;

/**
 * Created by Administrator on 2017/8/15.
 */

public class GlideUtils {

    private static GlideUtils glideUtils;

    public static GlideUtils getGlideUtils(){
        if (glideUtils==null){
            glideUtils=new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 加载普通图片
     * @param url
     * @param imageView
     */
    public void glideImage(Context context,String url, ImageView imageView){
        RequestOptions options = new RequestOptions()
                //.placeholder(R.mipmap.glide_placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载圆角图片
     * @param url
     * @param imageView
     */
    public void glideCircleImage(Context context,String url, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.icon_default_head)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }


}
