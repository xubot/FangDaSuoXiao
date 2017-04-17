package com.example.administrator.xubotao1502l20170417;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;

import static android.R.attr.width;
import static com.example.administrator.xubotao1502l20170417.R.attr.height;

/**
 * 用途：
 * 作者：xuBoTao
 * 时间：2017/4/17 08:51
 */

public class AutoView extends View {
    private int width;
    private int hight;
    private static final String TAG = "TJView";
    private int padding = 8;
    //画笔工具
    private Paint mPaint;
    //圆心坐标
    private float currentX = 0;
    private float currentY = 0;
    //大圆半径
    private float radiusBig = 100;
    //中圆半径
    private float radiusCenter = radiusBig / 2;
    //小圆半径
    private float radiusSmall = radiusCenter / 3;
    public AutoView(Context context) {
        this(context, null);
    }

    public AutoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //setAnimation();
    }
    //设置缩放动画
    private void setAnimation() {
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f);
        scaleAnimation2.setDuration(1000);// 设置持续时间
        scaleAnimation2.setRepeatCount(99999);// 设置重复次数
        scaleAnimation2.setFillAfter(true);// 保持动画结束时的状态
        //startAnimation(scaleAnimation2);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawLeftHalfCirle(canvas);
        drawTBCirle(canvas);
    }


    //画上下两个圆--中圆和小圆 @param canvas
    private void drawTBCirle(Canvas canvas) {
        //画上面的白中圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(currentX, currentY - radiusBig / 2, radiusCenter, mPaint);
        //画上面的黑小圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(currentX, currentY - radiusBig / 2, radiusSmall, mPaint);
        //画下面的黑中圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(currentX, currentY + radiusBig / 2, radiusCenter, mPaint);
        //画下面的白小圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(currentX, currentY + radiusBig / 2, radiusSmall, mPaint);
    }

    /**
     * 画左边半圆
     *
     * @param canvas
     */
    private void drawLeftHalfCirle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(currentX - radiusBig, currentY - radiusBig, currentX + radiusBig, currentY + radiusBig), 90, 180, true, mPaint);//90度开始画180度
    }

    /**
     * 画黑色的背景底板
     *
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(currentX, currentY, radiusBig + padding, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        hight = getHeight();
        currentX = View.MeasureSpec.getSize(widthMeasureSpec) / 2;
        currentY = MeasureSpec.getSize(heightMeasureSpec) / 2;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"currentX:"+currentX+"--currentY:"+currentY+"--width:"+width+"--hight:"+hight);
        currentX = event.getX();
        currentY = event.getY();
        //多点触控缩放
        int pointer = event.getPointerCount();
        int k = event.getAction();
        if (pointer==1){
            if(k == MotionEvent.ACTION_UP||k == MotionEvent.ACTION_MOVE){
                if ((currentX+radiusBig)<width&&(currentY+radiusBig)<hight&&(currentX-radiusBig)>0&&(currentY-radiusBig)>0){
                    invalidate();
                }
            }
        }else if(pointer==2&&k == MotionEvent.ACTION_MOVE){

            float distance = getDistance(event);
            Log.i(TAG,"距离"+distance);
            radiusBig=distance/2;
            Log.i(TAG,"222currentX:"+currentX+"--currentY:"+currentY+"--width:"+width+"--hight:"+hight+"-radiusBig--"+radiusBig);
            if ((currentX+radiusBig)<width&&(currentY+radiusBig)<hight&&(currentX-radiusBig)>0&&(currentY-radiusBig)>0){
                radiusCenter = radiusBig / 2;
                radiusSmall = radiusCenter / 3;
                invalidate();
            }
        }
        return true;
    }
    private float getDistance(MotionEvent event){

        float xOne = event.getX(0);
        float yOne = event.getY(0);
        float xTwo = event.getX(1);
        float yTwo = event.getY(1);
        float sqrt = (float) Math.sqrt((xOne - xTwo) * (xOne - xTwo) + (yOne - yTwo) * (yOne - yTwo));
        return sqrt;
    }
}
