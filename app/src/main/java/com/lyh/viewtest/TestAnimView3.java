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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

//测试波浪
public class TestAnimView3 extends View {
    private static final String TAG = "TestAnimView3";
    private int angle;      //角度
    private int len;        //一半的长度
    private int viewWidth;      //宽
    private int viewHeight;     //高
    private int pointY;         //波浪的水平线高度
    private int num;            //波浪个数
    private int offset;     //偏移长度
    private Paint paint;
    private Path path;
    private boolean isfirst=true;
    public TestAnimView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        angle=15;
        len=350;
        offset=0;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(20);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        path=new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
        num=(int)Math.ceil((viewWidth/(len*2f)))+1;
        pointY=viewHeight/3*2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pointX=-2*len+offset;        //最左的起始点
        int height=(int)(len/2*Math.abs(Math.tan(angle*Math.PI/180)));
        path.reset();
        path.moveTo(pointX,pointY);
        for(int i=0;i<num;i++){
            //上半波浪
            path.rQuadTo(len/2,-height,len,0);
            //下半波浪
            path.rQuadTo(len/2,height,len,0);
        }
        path.lineTo(viewWidth,viewHeight);
        path.lineTo(0,viewHeight);
        path.close();
        canvas.drawPath(path,paint);
        if(isfirst){
            setAnim();
            isfirst=false;
        }
    }

    //设置动画
    private void setAnim(){
        PropertyValuesHolder propertyValuesHolder=PropertyValuesHolder.ofInt("height",pointY,pointY-20,pointY);
        PropertyValuesHolder propertyValuesHolder1=PropertyValuesHolder.ofInt("offset",offset,len*2);
        ValueAnimator valueAnimator=new ValueAnimator();
        valueAnimator.setValues(propertyValuesHolder,propertyValuesHolder1);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset=(int)animation.getAnimatedValue("offset");
                pointY=(int)animation.getAnimatedValue("height");
                Log.d(TAG, "onAnimationUpdate: ");
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                offset=0;
                setAnim();
            }
        });
        valueAnimator.start();
    }
}
