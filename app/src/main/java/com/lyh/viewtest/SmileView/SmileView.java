package com.lyh.viewtest.SmileView;

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
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SmileView extends View {
    private static final String TAG = "CircleTest";
    private Paint paint;
    private Path path;
    private Path des;
    private int r;
    private int screenH;
    private int screenW;
    private PathMeasure pathMeasure;
    private int docuration = 2000;
    private float animValue;
    private int state;                      // 圈数
    private final int FIRST = 1;
    private final int SECOND = 2;
    private ValueAnimator valueAnimator;
    private float leftEyePos[];             //左眼坐标
    private float rightEyePos[];            //右眼坐标
    private boolean isLeftEyeShow;
    private boolean isRightEyeShow;
    private float pointPos[];               //移动点的坐标
    private float pathLen;                  //path长度

    public SmileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        des = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        int blueColor = Color.rgb(178, 216, 244);
        paint.setColor(blueColor);

        leftEyePos = new float[2];
        rightEyePos = new float[2];
        pointPos = new float[2];
        state = FIRST;
        animValue = 0f;
        isLeftEyeShow = false;
        isRightEyeShow = false;
        r=100;
        setAnim();
    }

    /**
     * 设置动画
     */
    private void setAnim() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(state==FIRST)
                    state=SECOND;
                else state=FIRST;
                setAnim();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(docuration);
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;
        path.addCircle(screenW / 2, screenH / 2, r, Path.Direction.CW);
        pathMeasure = new PathMeasure(path, true);
        pathLen = pathMeasure.getLength();
        pathMeasure.getPosTan(pathLen / 8 * 5, leftEyePos, null);
        pathMeasure.getPosTan(pathLen / 8 * 7, rightEyePos, null);
        int stroke = (int) paint.getStrokeWidth();
        leftEyePos[0] += stroke >> 2;
        leftEyePos[1] += stroke >> 2;
        rightEyePos[0] -= stroke >> 2;
        rightEyePos[1] += stroke >> 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int endlen = (int) (animValue * pathLen);
        pathMeasure.getPosTan(endlen, pointPos, null);
        //先根据圈数判断
        if (state == FIRST) {
            Log.d(TAG, "onDraw: 第一圈");
            //绘制点
            canvas.drawPoint(pointPos[0], pointPos[1], paint);
            if (isExceedLeftEye()) isLeftEyeShow = true;
            if (isExceedRightEye()&&isLeftEyeShow) isRightEyeShow = true;
        } else if (state == SECOND) {
            Log.d(TAG, "onDraw: 第二圈");
            //绘制线段
            des.reset();
            float start = (2 * animValue - 1) * pathLen;
            pathMeasure.getSegment(start, endlen, des, true);
            Log.d(TAG, "onDraw: 开始点"+start);
            canvas.drawPath(des, paint);
            if (isExceedLeftEye()) isLeftEyeShow = false;
            if (isExceedRightEye()&&!isLeftEyeShow) isRightEyeShow = false;
        }
        if (isLeftEyeShow) canvas.drawPoint(leftEyePos[0], leftEyePos[1], paint);
        if (isRightEyeShow) canvas.drawPoint(rightEyePos[0], rightEyePos[1], paint);

    }

    //判断是否超过左眼的坐标
    private boolean isExceedLeftEye() {
        return pointPos[0] > leftEyePos[0] && pointPos[1] < leftEyePos[1];
    }

    //判断是否超过右眼的坐标
    private boolean isExceedRightEye() {
        return pointPos[0] > rightEyePos[0] && pointPos[1] > rightEyePos[1];
    }

}
