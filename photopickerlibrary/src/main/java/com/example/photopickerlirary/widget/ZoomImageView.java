package com.example.photopickerlirary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "ZoomImageView";
    private boolean isFirstIn = true;
    private Matrix scaleMatrix ;
    private int bitmapWidth , bitmapHeight , viewWidth , viewHeight;
    private float lastX , lastY;
    private float[] matrixValues;
    private float initScaleSize;//默认缩放倍数
    private float resultScaleSize;//最终缩放倍数
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean shouldTouch = true;
    private boolean shouldMove = true;
    private Timer timer;
    private TimerTask timerTask;
    private SingleClick singleClick;

    public ZoomImageView(Context context) {
        this(context , null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(getContext() , onGestureListener);
        scaleGestureDetector = new ScaleGestureDetector(getContext() , this);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private void initData() {
        Drawable drawable = getDrawable();
        if (drawable == null){
            return;
        }
        scaleMatrix = getImageMatrix();
        bitmapWidth = drawable.getIntrinsicWidth();
        bitmapHeight = drawable.getIntrinsicHeight();
        viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    private void translateToCenter(){
        scaleMatrix.reset();
        int transWidth = viewWidth/2 - bitmapWidth/2;
        int transHeight = viewHeight/2 - bitmapHeight/2;
        scaleMatrix.postTranslate(transWidth , transHeight);
        setImageMatrix(scaleMatrix);
    }

    private void scaleToFitScreen(){
        float scaleWidth = ((viewWidth*100)/bitmapWidth)*0.01f;
        float scaleHeight = ((viewHeight*100)/bitmapHeight)*0.01f;
//        initScaleSize = Math.min(scaleWidth , scaleHeight);
//        if (initScaleSize > 3){//要放大3倍才能使一边填充屏幕，说明图片过小，为避免图片失真，当<3时不进行缩放设为1
//            resultScaleSize = initScaleSize;
//        }else {
//            resultScaleSize = initScaleSize;
//        }
        initScaleSize = Math.min(scaleWidth , scaleHeight);
        resultScaleSize = initScaleSize + 2;
        scaleMatrix.postScale(initScaleSize, initScaleSize, viewWidth/2 , viewHeight/2);
        setImageMatrix(scaleMatrix);
    }

    public void zoomView(){
        if (getDrawable() == null)return;
        if (getCurrentScale() - initScaleSize < 2){
            scaleMatrix.postScale(2f , 2f, viewWidth/2 , viewHeight/2);
            setImageMatrix(scaleMatrix);
        }else {
            resetView();
        }
    }

    public void resetView(){
        if (getDrawable() == null)return;
        scaleMatrix.reset();
        translateToCenter();
        scaleToFitScreen();
    }

    @Override
    public void onGlobalLayout() {
        if (getDrawable() == null){
            return;
        }
        if (isFirstIn){
            initData();
            //translateToCenter();
            //scaleToFitScreen();
            isFirstIn = false;
        }
        translateToCenter();
        scaleToFitScreen();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)){
            return true;
        }
        if (shouldTouch){//防止双指缩放时抖动
            scaleGestureDetector.onTouchEvent(event);
        }
        float currentX = 0;
        float currentY = 0;
        float disX = 0;
        float disY = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                disX = currentX - lastX;
                disY = currentY - lastY;
                if (getCurrentScale() > initScaleSize){//说明图片的某一条边已经放大到超过屏幕，此时允许拖动
                    RectF drawableRect = getDrawableRect();
                    if (drawableRect == null)return true;
                    if (getCurrentScale() > initScaleSize){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    if (drawableRect.left >= -5 || drawableRect.right <= viewWidth + 5 || drawableRect.top >= -5 || drawableRect.bottom <= viewHeight + 5){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (disX > 0){//向右滑动
                        if (drawableRect.left > -5){
                            shouldMove = false;
                        }
                    }else {//向左滑动
                        if (drawableRect.right < viewWidth + 5){
                            shouldMove = false;
                        }
                    }
                    if (disY > 0){//向下滑动
                        if (drawableRect.top > -5){
                            shouldMove = false;
                        }
                    }else {//向上滑动
                        if (drawableRect.bottom < viewHeight + 5){
                            shouldMove = false;
                        }
                    }
                    if (shouldMove){
                        //防止快速滑动时超出边界
                        if (drawableRect.left + disX >= -5){
                            disX = -(5 + drawableRect.left);
                        }
                        if (drawableRect.right + disX <= viewWidth + 5){
                            disX = viewWidth + 5 - drawableRect.right;
                        }
                        if (drawableRect.top + disY >= -5){
                            disY = -(5 + drawableRect.top);
                        }
                        if (drawableRect.bottom + disY <= viewHeight + 5){
                            disY = viewHeight + 5 - drawableRect.bottom;
                        }
                        scaleMatrix.postTranslate(disX , disY);
                        setImageMatrix(scaleMatrix);
                        lastX = currentX;
                        lastY = currentY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                shouldTouch = true;
                shouldMove = true;
                Log.e(TAG, "onTouchEvent: ---->up");
                break;
        }
        return true;
    }

    private float getCurrentScale(){
        float scaleSize = 0;
        matrixValues = new float[9];
        if (scaleMatrix != null){
            scaleMatrix.getValues(matrixValues);
            scaleSize = matrixValues[Matrix.MSCALE_X];
        }else {
            scaleSize = initScaleSize;
        }
        return scaleSize;
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            handler.removeMessages(0);
            stopTimer();
            zoomView();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            startTimer();
            return super.onSingleTapUp(e);
        }
    };

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scale = getCurrentScale();
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        if (getDrawable() == null){
            return true;
        }
        if ((scale < resultScaleSize && scaleFactor > 1.0f) || (scale > initScaleSize && scaleFactor < 1.0f)) {
            if (scaleFactor * scale < initScaleSize) {
                scaleFactor = initScaleSize / scale;
            }
            if (scaleFactor * scale > resultScaleSize) {
                scaleFactor = resultScaleSize / scale;
            }
            scaleMatrix.postScale(scaleFactor, scaleFactor,
                    viewWidth/2, viewHeight/2);
            setImageMatrix(scaleMatrix);
        }else if (scale <= initScaleSize){
            resetView();
            shouldTouch = false;
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        //getParent().requestDisallowInterceptTouchEvent(true);
        Log.e(TAG, "onScaleBegin: ");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        //getParent().requestDisallowInterceptTouchEvent(false);
        Log.e(TAG, "onScaleEnd: ");
    }

    private RectF getDrawableRect(){
        Drawable drawable = getDrawable();
        if (drawable == null)return null;
        Matrix matrix = scaleMatrix;
        RectF rectF = new RectF();
        rectF.set(0 , 0 , drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight());
        matrix.mapRect(rectF);
        return rectF;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                if (singleClick == null)return;
                singleClick.onSingleClick();
            }
        }
    };

    private void startTimer(){
        stopTimer();
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
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    private int getWindowWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getWindowHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public interface SingleClick{
        void onSingleClick();
    }

    public void setOnSingleClickListener(SingleClick singleClick){
        this.singleClick = singleClick;
    }
}
