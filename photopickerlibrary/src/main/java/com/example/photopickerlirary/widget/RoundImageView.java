package com.example.photopickerlirary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.photopickerlirary.R;

/**
 * Created by tkc on 2018/3/30.
 */

public class RoundImageView extends ImageView {

    private static final String TAG = "RoundImageView";

    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int DEFAULT_TYPE = TYPE_CIRCLE;
    private static final int DEFAULT_RADIUS = 5;

    private int bitmapType;//图片类型
    private int bitmapRadius;//弧度大小
    private int borderColor;//边框颜色
    private int borderWidth;//边框宽度

    private Paint bitmapPaint;//内容
    private Paint borderPaint;//边框
    private Matrix matrix;//图片变换
    private Bitmap bitmap;//要画的图片
    private BitmapShader bitmapShader;
    private RectF roundRect;

    private int minWidth;

    private int radius;//圆形图片半径

    public RoundImageView(Context context) {
        this(context , null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context , attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs , R.styleable.RoundImageView);
        bitmapType = typedArray.getInt(R.styleable.RoundImageView_imageViewType, DEFAULT_TYPE);
        bitmapRadius = (int) typedArray.getDimension(R.styleable.RoundImageView_roundborderRadius , dp2px(context , DEFAULT_RADIUS));
        borderColor = typedArray.getColor(R.styleable.RoundImageView_borderColor , context.getResources().getColor(R.color.white));
        borderWidth = (int) typedArray.getDimension(R.styleable.RoundImageView_borderWidth , 0);
        typedArray.recycle();
        matrix = new Matrix();
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (bitmapType == TYPE_CIRCLE){
            minWidth = Math.min(getMeasuredWidth() , getMeasuredHeight());
            radius = minWidth / 2;
            setMeasuredDimension(minWidth , minWidth);
            return;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmapType == TYPE_ROUND){
            roundRect = new RectF(borderWidth, borderWidth , w - borderWidth, h - borderWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null){
            Log.e(TAG, "onDraw: drawable is null");
            return;
        }
        setBitmapShader();
        if (bitmapType == TYPE_ROUND){
            canvas.drawRoundRect(roundRect , bitmapRadius , bitmapRadius , bitmapPaint);
            if (borderWidth != 0){
                canvas.drawRoundRect(roundRect, bitmapRadius , bitmapRadius , borderPaint);
            }
        }else if (bitmapType == TYPE_CIRCLE){
            canvas.drawCircle(getWidth() / 2 , getHeight() / 2 , radius - borderWidth , bitmapPaint);
            if (borderWidth != 0){
                canvas.drawCircle(getWidth() / 2 , getHeight() / 2 , radius - borderWidth , borderPaint);
            }
        }
    }

    private void setBitmapShader(){
        Drawable drawable = getDrawable();
        if (drawable == null){
            Log.e(TAG, "setBitmapShader: drawable is null");
            return;
        }
        bitmap = drawableToBitmap(drawable);
        if (bitmap == null){
            Log.e(TAG, "setBitmapShader: drawable is null");
            return;
        }
        bitmapShader = new BitmapShader(bitmap , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP);
        float scaleSize = 1.0f;
        if (bitmapType == TYPE_CIRCLE){
            //获得bitmap宽高的最小值
            int bitmapSize = Math.min(bitmap.getWidth() , bitmap.getHeight());
            scaleSize = minWidth * 1.0f / bitmapSize;
        }else if (bitmapType == TYPE_ROUND){
            if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())){
                //当图片的大小和控件大小不一致的时候 取最大值进行缩放
                float widthScale = getWidth() * 1.0f / bitmap.getWidth();
                float heightScale = getHeight() * 1.0f / bitmap.getHeight();
                scaleSize = Math.max(widthScale , heightScale);
            }
        }
        //将图片移到控件中心
        //matrix.postTranslate(getWidth() / 2 - bitmap.getWidth() / 2 , getHeight() / 2 - bitmap.getHeight());
        //对图片进行缩放
        matrix.postScale(scaleSize , scaleSize);
        //设置矩阵
        bitmapShader.setLocalMatrix(matrix);
        bitmapPaint.setShader(bitmapShader);
    }

    /**
     *drawable准换bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0 , 0 , width , height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * dp转px
     */
    private int dp2px(Context context , int dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*density + 0.5f);
    }
}
