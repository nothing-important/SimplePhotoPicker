package com.example.photopickerlirary.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class OnDoubleClick implements View.OnClickListener {
    private static final String TAG = "OnDoubleClick";

    private final int MIN_CLICK_TIME = 500;
    private long lastClickTime = 0 , currentClickTime = 0;
    private OnDoubleClickListener onDoubleClickListener;
    private Timer timer;
    private TimerTask timerTask;
    private boolean result;
    private boolean isTimerStart;
    private View clickView;

    public OnDoubleClick(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    @Override
    public void onClick(View v) {
        clickView = v;
        if (onDoubleClickListener != null){
            if (isDoubleClick()){
                onDoubleClickListener.onDoubleClick(v);
            }
        }
    }

    private boolean isDoubleClick(){
        if (isTimerStart){
            stopTimer();
            result = true;
        }else {
            lastClickTime = Calendar.getInstance().getTimeInMillis();
            startTimer();
            result = false;
        }
        return result;
    }

    public interface OnDoubleClickListener{
        void onDoubleClick(View view);
        void onSingleClick(View view);
    }

    private void startTimer(){
        isTimerStart = true;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask , 200);
    }

    private void stopTimer(){
        isTimerStart = false;
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        if (timerTask != null){
            timerTask = null;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                result = false;
                stopTimer();
                onDoubleClickListener.onSingleClick(clickView);
            }
        }
    };


}
