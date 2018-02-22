package com.yakov.weber.devintensive.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jannyjacky on 2/22/18.
 */

@SuppressLint("AppCompatCustomView")
public class CircularImageView extends ImageView{

    private int borderWidth = 5;
    private int viewWidth ;
    private int viewHeight;
    private Bitmap image;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CircularImageView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        setBorderColor(Color.BLUE);
        paintBorder.setAntiAlias(true);
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (paintBorder != null){
            this.borderWidth = borderColor;
            this.invalidate();
        }
    }

    private void loadBitmap(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null){
            image = bitmapDrawable.getBitmap();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loadBitmap();

        if (image != null){
            shader = new BitmapShader(Bitmap.createScaledBitmap
                    (image,canvas.getWidth(),canvas.getHeight(),false)
                    , Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            paint.setShader(shader);

            int circleCenter = getWidth() / 2;


            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth
            ,circleCenter + borderWidth,paintBorder);

            canvas.drawCircle(circleCenter + borderWidth
                    , circleCenter + borderWidth,circleCenter,paintBorder);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width,height);

    }

    private int measureHeight(int heightMeasureSpec, int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = viewHeight;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = viewWidth;
        }
        return result;
    }
}



















