package com.lyh.viewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/*
    球波浪
    https://www.kancloud.cn/digest/wingscustomview/129811
 */
public class LoadingView extends View {
    private static final String TAG = "TestAnimView4";

    private int color;      //波浪颜色
    private int alpha;      //透明度
    private int r;          //球的半径
    private int offset;
    private int maxOffset;      //可最大的偏移量
    private int wavelen;    //一半波浪的长度
    private int waveY;      //波浪的水平线的Y
    private int waveH;      //波浪起复的高度
    private int percent;         //百分比
    private int circleX;
    private int circleY;

    private Paint paint;
    private Path wavePath;
    private Path circlePath;
    private Path upPath;

    private boolean isfirst=true;
    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        percent=0;
        offset=0;

        color=Color.YELLOW;
        alpha=175;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);


        wavePath=new Path();
        circlePath=new Path();
        upPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        get(widthMeasureSpec);
        get(heightMeasureSpec);
        Log.d(TAG, "onMeasure: "+width+"  "+height);
        int res=Math.min(width,height);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(res,MeasureSpec.getMode(widthMeasureSpec))
                ,MeasureSpec.makeMeasureSpec(res,MeasureSpec.getMode(heightMeasureSpec)));
    }
    private void get(int M){
        switch (MeasureSpec.getMode(M)){
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "onMeasure: AT_MOST");  break;
                case MeasureSpec.EXACTLY:
                    Log.d(TAG, "onMeasure: EXACTLY");  break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "onMeasure: UNSPECIFIED");  break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int tmp=Math.min(w,h);
        circleX=w/2;
        circleY=h/2;

        Log.d(TAG, "onsize中的onMeasure: "+w+"  "+h);
        r=tmp/2;
        waveY=getWaveY();
        wavelen=r*3/2;
        maxOffset=2*wavelen-2*r;
        waveH=(int)(wavelen/2*Math.tan(15*Math.PI/180));
        Log.d(TAG, "onSizeChanged: "+circleX+"  "+circleY+"  "+r);

        //设置圆的path
        circlePath.addCircle(circleX,circleY,r,Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //保存状态
        canvas.save();
        //裁剪出圆
        canvas.clipPath(circlePath);

        //绘制波浪
        UpdateWavePath(-30);
        paint.setColor(color);
        paint.setAlpha(alpha-70);
        canvas.drawPath(wavePath,paint);

        UpdateWavePath(-15);
        paint.setColor(color);
        paint.setAlpha(alpha-50);
        canvas.drawPath(wavePath,paint);

        UpdateWavePath(0);
        paint.setColor(color);
        paint.setAlpha(alpha);
        canvas.drawPath(wavePath,paint);


        upPath.op(circlePath,wavePath, Path.Op.DIFFERENCE);
        paint.setColor(Color.WHITE);
        paint.setAlpha(alpha);
        canvas.drawPath(upPath,paint);

        //绘制文字
        paint.setColor(Color.BLACK);
        paint.setTextSize(100f);
        canvas.drawText(percent+"%",circleX-paint.measureText(percent+"%")/2,circleY,paint);

        if(isfirst){
            setAnim();
            isfirst=false;
        }
        //还原状态
        canvas.restore();



        /*
        //另一种绘制方式
        //绘制圆
        //需要新建图层
        canvas.saveLayer(circleX-r,circleY-r,circleX+r,circleY+r,null,Canvas.ALL_SAVE_FLAG);
        paint.setColor(Color.WHITE);
        paint.setAlpha(alpha);
        canvas.drawPath(circlePath,paint);
        //设置混合模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint.setColor(color);
        paint.setAlpha(alpha);
        UpdateWavePath(0);
        canvas.drawPath(wavePath,paint);
        paint.setXfermode(null);
        canvas.restore();
        if(isfirst){
            setAnim();
            isfirst=false;
        }*/
    }
    //得到波浪区域的path
    private void UpdateWavePath(int i){
        wavePath.reset();
        //起点
        wavePath.moveTo(circleX-r-maxOffset+offset,getWaveY()+i);
        wavePath.rQuadTo(wavelen/2,waveH,wavelen,0);
        wavePath.rQuadTo(wavelen/2,-waveH,wavelen,0);
        wavePath.lineTo(circleX+r,circleY+r);
        wavePath.lineTo(circleX-r,circleY+r);
        wavePath.close();
    }
    //设置动画
    private void setAnim(){
        ValueAnimator valueAnimator=new ValueAnimator().setDuration(2000);
        PropertyValuesHolder offsetValues=PropertyValuesHolder.ofInt("offset",0,maxOffset,0);
        //PropertyValuesHolder waveYValues=PropertyValuesHolder.ofInt("waveY",waveY,waveY-30,waveY);

        valueAnimator.setValues(offsetValues);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setAnim();
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset=(int)animation.getAnimatedValue("offset");
                //waveY=(int)animation.getAnimatedValue("waveY");
                invalidate();
            }
        });
        valueAnimator.start();
    }
    private int getWaveY(){
        return circleY+r-(int)(percent/100f*r*2);
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setPercent(int percent){
        this.percent=percent;
        Log.d(TAG, "setPercent: "+percent);
    }
}
