package com.lyh.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

public class TestImageView extends ImageView {
    Path path;
    RectF rectF;
    float rx=25;
    float ry=25;
    public TestImageView(Context context) {
        super(context);
        path=new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF=new RectF(0,0,w,h);
        path.addRoundRect(rectF,rx,ry,Path.Direction.CW);
        path.addCircle(w/2,h/2,50,Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.restore();
    }

}
