package com.lyh.viewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/*
    测试插值器
 */
public class TestAnimView1 extends View {
    private static final String TAG = "TestAnimView1";
    private int r=50;
    private int mcenterx=100;
    private int mcentery=200;
    private int malpha=100;
    private Paint paint;
    private boolean isfirst=true;

    public TestAnimView1(Context context) {
        super(context);
    }

    public TestAnimView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isfirst){
            Log.d(TAG, "onDraw: ");
            setTranslateAnim();
            isfirst=false;
        }
        paint.setAlpha(malpha);
        canvas.drawCircle(mcenterx,mcentery,r,paint);
    }
    //设置透明度动画
    private void SetAlphaAnim(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofInt(this,"malpha",malpha,0);
        objectAnimator.setDuration(2000);
        //objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                SetAlphaAnim();
            }
        });
    }
    //设置平移度动画
    private void setTranslateAnim(){
        PropertyValuesHolder x=PropertyValuesHolder.ofInt("mcentery",malpha,15);
        ValueAnimator objectAnimator=new ValueAnimator();
        objectAnimator.setValues(x);
        objectAnimator.setDuration(3000);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                malpha=100;
                setTranslateAnim();

            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                malpha=(int)animation.getAnimatedValue("mcentery");
                invalidate();
            }
        });
    }
    public int getMalpha() {
        return malpha;
    }

    public void setMalpha(int malpha) {
        this.malpha = malpha;
        invalidate();
    }
}
