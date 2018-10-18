package com.example.photopickerlirary.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.PathMotion;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

import com.example.photopickerlirary.entity.PhotoBean;

import java.io.Serializable;
import java.util.List;

public class ShareElementUtils {

    public static void transToNextWithAnim(Activity activity , Intent intent){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
            activity.startActivity(intent, transitionActivityOptions.toBundle());
        }else {
            activity.startActivity(intent);
        }
    }

    public static void transToNextWithElement(Activity activity , Intent intent , View shareView , String shareTag){
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//            Pair<View , String> [] pairs = TransitionHelper.createSafeTransitionParticipants(activity , false ,
//                    new Pair<>(shareView , shareTag));
//            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity , pairs);
//            activity.startActivity(intent , activityOptionsCompat.toBundle());
//        }else {
//            activity.startActivity(intent);
//        }
        //上面函数有bug 暂时不用
        activity.startActivity(intent);
    }

    /**
     * 设置activity切换动画效果
     */
    public static void setSlideAnim(Activity activity){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.setAllowEnterTransitionOverlap(false);
            window.setAllowReturnTransitionOverlap(false);
            Slide slide = new Slide();
            slide.setDuration(300);
            slide.setInterpolator(new AccelerateInterpolator());
            window.setEnterTransition(slide);
        }
    }

    /**
     * 设置activity切换动画效果
     */
    public static void setExplodeAnim(Activity activity){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.setAllowEnterTransitionOverlap(false);
            window.setAllowReturnTransitionOverlap(false);
            Explode explode = new Explode();
            explode.setDuration(300);
            explode.setInterpolator(new AccelerateInterpolator());
            window.setEnterTransition(explode);
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
