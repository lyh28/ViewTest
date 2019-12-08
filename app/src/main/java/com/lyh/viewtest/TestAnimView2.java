package com.lyh.viewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
/*
    canvas和Path的测试
 */
public class TestAnimView2 extends View {
    private static final String TAG = "TestAnimView2";
    private Bitmap boy;
    private Paint paint;
    private float scalex;
    private float oldscalex;
    private float scaley;
    private Path path;
    private boolean isfirst=true;
    public TestAnimView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        paint=new Paint();
        paint.setColor(Color.BLUE);
        boy=BitmapFactory.decodeResource(context.getResources(),R.drawable.boy1);
        scalex=1f;
        scaley=2f;
        oldscalex=0f;
        path=new Path();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect imgrect=new Rect(0,0,boy.getWidth(),boy.getHeight());
        Rect posrect=new Rect(100,100,400,400);
        Rect posrect1=new Rect(100,500,400,800);

        //Path
        //path.moveTo(100,100);
        path.lineTo(400,400);
        /*path.lineTo(300,350);
        path.close();*/
        paint.setStrokeWidth(10f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(posrect,paint);
        canvas.drawPath(path,paint);
        //path.reset();
        /*path.moveTo(400,400);
        path.lineTo(600,600);
        path.close();
        paint.setColor(Color.YELLOW);
        canvas.drawPath(path,paint);*/

        //canvas.scale(0.5f,0.5f,250,250);
        /*paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawRect(posrect,paint);

        canvas.skew(1f,0f);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(posrect,paint);
        canvas.restore();
        canvas.drawRect(posrect1,paint);*/
        //canvas.drawBitmap(boy,imgrect,posrect,paint);
        if(isfirst){
            isfirst=false;
            //setAnim();
        }
    }
    //设置动画
    private void setAnim(){
        PropertyValuesHolder x=PropertyValuesHolder.ofFloat("scalex",scalex,-scalex);
        ValueAnimator valueAnimator=new ValueAnimator().setDuration(3000);
        valueAnimator.setValues(x);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                oldscalex=scalex;
                scalex=(float)animation.getAnimatedValue("scalex");
                Log.d(TAG, "onAnimationUpdate: ");
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
}
