package com.lyh.viewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Test extends View {
    private static final String TAG = "Test";
    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onMeasure: "+w+"  "+h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p=new Paint();
        p.setColor(Color.YELLOW);
        p.setStrokeWidth(30f);

        //canvas.drawLine(0,100,300,100,p);
        Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.boy1);
        Rect src=new Rect(0,0,b.getWidth(),b.getHeight());
        Rect target=new Rect(0,0,300,300);
        canvas.drawBitmap(b,src,target,p);
    }
}
