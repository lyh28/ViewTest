package com.lyh.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class DrawPadBezier extends View {
    private static final String TAG = "DrawPadBezier";
    private float mX;
    private float mY;
    private float offset = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private Paint mPaint;
    private Path mPath;
    private Path path = new Path();

    public DrawPadBezier(Context context) {
        super(context);
    }

    public DrawPadBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
    }

    public DrawPadBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                float x = event.getX();
                float y = event.getY();
                mX = x;
                mY = y;
                mPath.moveTo(x, y);
                Log.d(TAG, "onTouchEvent down: " + x + "  " + y);
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                float y1 = event.getY();

                float preX = mX;
                float preY = mY;
                float dx = Math.abs(x1 - preX);
                float dy = Math.abs(y1 - preY);
                if (dx >= offset || dy >= offset) {
                    Log.d(TAG, "onTouchEvent: " + x1 + "  " + y1);
                    // 贝塞尔曲线的控制点为起点和终点的中点
                    float cX = (x1 + preX) / 2;
                    float cY = (y1 + preY) / 2;
                    mPath.quadTo(preX, preY, cX, cY);
                    //mPath.lineTo(cX, cY);
                    mX = x1;
                    mY = y1;
                }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x[] = new float[]{270.98438f, 267.98438f, 285.24896f, 302.3129f, 358.16144f};
        float y[] = new float[]{220.97656f, 239.8894f, 263.5936f, 266.95312f, 229.96094f};
        mPath.moveTo(x[0], y[0]);
        mPaint.setColor(Color.RED);
        canvas.drawPoint(x[0],y[0],mPaint);
        for (int i = 1; i <= 4; i++) {
            float cX = (x[i] + x[i - 1]) / 2;
            float cY = (y[i] + y[i - 1]) / 2;
            canvas.drawPoint(x[i],y[i],mPaint);
            mPath.quadTo(cX, cY, x[i], y[i]);
        }
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPath, mPaint);
    }
}
