package com.example.photopickerlirary.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;

import com.example.photopickerlirary.entity.PhotoBean;

import java.io.Serializable;
import java.util.List;

public class ShareElementUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startWithShareElement(Activity activity , Class targetClass , List<PhotoBean> urlExtra , View shareView , String shareTag){
        Intent intent = new Intent(activity , targetClass);
        intent.putExtra("urlExtra" , (Serializable) urlExtra);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity , shareView ,shareTag);
        activity.startActivity(intent , activityOptions.toBundle());
    }

    public static void applyShareElement(Activity activity){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.setAllowReturnTransitionOverlap(false);
            window.setAllowEnterTransitionOverlap(false);
        }
    }

    public static void closeActivity(Activity activity){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            activity.finishAfterTransition();
        }else {
            activity.finish();
        }
    }

}
