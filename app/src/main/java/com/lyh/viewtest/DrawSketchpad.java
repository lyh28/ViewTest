package com.lyh.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DrawSketchpad extends View {
    Paint paint;
    public DrawSketchpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int sideLength=30;
        float viewH=getMeasuredHeight();
        float viewW=getMeasuredWidth();
        int wNum=(int)viewW/sideLength;
        int hNum=(int)viewH/sideLength;
        paint.setColor(Color.RED);
        for(int i=1;i<=wNum;i++){
            canvas.drawLine(i*sideLength,0,i*sideLength,viewH,paint);
        }
        for(int i=1;i<=hNum;i++){
            canvas.drawLine(0,i*sideLength,viewW,i*sideLength,paint);
        }

    }


}
