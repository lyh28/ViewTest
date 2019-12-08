package com.lyh.viewtest.PathTest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

public class CircleTest extends View {
    private int num;
    private Path path;
    private Paint paint;
    private PathMeasure pathMeasure;
    private int r;
    private float perimeter;      //周长
    private float distance;     //每一小段距离
    private ValueAnimator valueAnimator;
    private float offset;
    public CircleTest(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        path=new Path();
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        num=3;
        r=50;
        offset=0;
        setAnim();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.addCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,r,Path.Direction.CW);
        pathMeasure=new PathMeasure(path,true);
        perimeter=(float) Math.PI*2*r;
        distance=perimeter/num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float []pos=new float[2];
        float []tan=new float[2];
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,r,paint);
        for(int i=0;i<=num;i++){
            pathMeasure.getPosTan(distance*i+offset,pos,tan);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(10);
            canvas.drawPoint(pos[0],pos[1],paint);
        }
    }
    private void setAnim(){
        valueAnimator=ValueAnimator.ofFloat(0,distance);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset=(float)animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setAnim();
            }
        });
        valueAnimator.start();
    }
    public void addNum() {
        num++;
        distance=perimeter/num;
        invalidate();
    }

    public void decreaseNum(){
        num--;
        distance=perimeter/num;
        invalidate();
    }
}
