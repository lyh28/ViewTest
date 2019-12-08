package com.lyh.viewtest;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestBezier extends View {
    private static final String TAG = "TestBezier";
    private Paint paint;
    private Path path;
    private Camera camera=new Camera();
    private float angle;
    private final float maxAngle=15f;
    private Bitmap bitmap;
    private final int UP=1;
    private final int DOWN=2;
    private final int LEFT=3;
    private final int RIGHT=4;
    private final int CENTER=5;
    private final int NOTHING=6;
    private Rect src;
    private Rect des;
    private int state=0;
    private ValueAnimator valueAnimator;
    private int viewH;
    private int viewW;
    public TestBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        path=new Path();
        angle=0f;
        BitmapFactory.Options o=new BitmapFactory.Options();
        bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.boy1,o);
        valueAnimator=new ValueAnimator().setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float change=(float)animation.getAnimatedValue("parameter");
                switch (state){
                    case CENTER:
                        break;
                        default:
                            Log.d(TAG, "onAnimationUpdate: "+angle);
                            angle=change;
                            break;
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW=w;
        viewH=h;
        src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        des=new Rect(0,0,viewW,viewH);

    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLUE);
        Matrix matrix=new Matrix();
        camera.save();
        setAnimAction();
        //恢复中心
        camera.getMatrix(matrix);
        Log.d(TAG, "onDraw: "+viewW/bitmap.getWidth()+"  "+viewH/bitmap.getHeight());
        //matrix.postScale(viewW/(float)bitmap.getWidth(),viewH/(float)bitmap.getHeight(),0,0);
        matrix.preTranslate(-viewW/2,-viewH/2);
        matrix.postTranslate(viewW/2,viewH/2);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap,src,des,paint);
        canvas.restore();
        camera.restore();
    }

    //设置动画参数
    private void setAnimAction(){
        switch (state){
            case CENTER:
                break;
            case LEFT:
                camera.rotateY(-angle);
                break;
            case RIGHT:
                camera.rotateY(angle);
                break;
            case NOTHING:
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //判断点击区域
                checkPointDown(event.getX(),event.getY());
                //启动动画

                break;
            case MotionEvent.ACTION_UP:
                //启动动画
                startAnim(maxAngle,0);
                break;
        }
        return true;
    }
    private void startAnim(float... array){
        switch (state){
            case CENTER:
                break;
                default:
                    PropertyValuesHolder propertyValuesHolder=PropertyValuesHolder.ofFloat("parameter",array);
                    valueAnimator.setValues(propertyValuesHolder);
                    break;
        }
        valueAnimator.start();
    }
    //判断点击
    private void checkPointDown(float x,float y){
        //如果点击中间
        if(viewW/3<x&&viewW/3*2>x&&viewH/3<y&&viewH/3*2>y){
            Log.d(TAG, "checkPointDown: 中间");
            state=CENTER;
        }else if(x<viewW/2&&(x/y<viewW/viewH&&x/(viewH-y)<viewW/viewH)){
            state=LEFT;
            startAnim(0,maxAngle);
            Log.d(TAG, "checkPointDown: 左边");

        }else if(x>viewW/2&&((viewW-x)/y<viewW/viewH&&(viewW-x)/(viewH-y)<viewW/viewH)){
            state=RIGHT;
            startAnim(0,maxAngle);
            Log.d(TAG, "checkPointDown: 右边");
        }else{
            state=NOTHING;
            Log.d(TAG, "checkPointDown: 其他");
        }
    }
}
