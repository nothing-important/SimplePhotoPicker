package com.example.photopickerlirary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class GlideUtil {

    private static GlideUtil glideUtil;
    private Context context;
    private RequestOptions requestOptions;

    @SuppressLint("CheckResult")
    private GlideUtil (){
        requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(false);
        requestOptions.override(Target.SIZE_ORIGINAL/3 , Target.SIZE_ORIGINAL/3);
    }

    public static GlideUtil getGlideUtil(){
        if (glideUtil == null){
            synchronized (GlideUtil.class){
                if (glideUtil == null){
                    glideUtil = new GlideUtil();
                }
            }
        }
        return glideUtil;
    }

    public GlideUtil with(Context context){
        this.context = context;
        return this;
    }

    public void loadImageNormal(String imageUrl , ImageView imageView){
        if (context == null || requestOptions == null)return;
        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imageUrl).into(imageView);
    }

    public void loadImageNormal(int imageUrl , ImageView imageView){
        if (context == null || requestOptions == null)return;
        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imageUrl).into(imageView);
    }

}
