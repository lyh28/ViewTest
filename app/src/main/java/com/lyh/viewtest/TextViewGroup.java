package com.lyh.viewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TextViewGroup extends ViewGroup {
    private static final String TAG = "TextViewGroup";
    private List<Integer> maxWidth=new ArrayList<>();
    public TextViewGroup(Context context) {
        super(context);
    }

    public TextViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        maxWidth.add(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int limitheight=MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: limitheight"+limitheight);
        int width=0,height=0;
        int resWidth=0;
        int resHeight=0;
        int windex=1;
        Log.d(TAG, "onMeasure: 数量"+getChildCount());
        for(int i=0;i<getChildCount();i++) {
            View view=getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            //Log.d(TAG, "onMeasure: "+view.getMeasuredWidth());
            if(height+view.getMeasuredHeight()>limitheight){
                Log.d(TAG, "onMeasure: 另起一列");
                //另起一列
                windex++;
                width=resWidth+10;
                height=0;
            }
           // Log.d(TAG, "onMeasure: 宽"+resWidth);
            resWidth=(resWidth<(width+view.getMeasuredWidth()))?(width+view.getMeasuredWidth()):resWidth;
            //Log.d(TAG, "onMeasure: 宽"+resWidth);
            if(windex>=maxWidth.size())
                maxWidth.add(resWidth);
            else
                maxWidth.set(windex,resWidth);
            height+=view.getMeasuredHeight();
            resHeight=(resHeight<height)?height:resHeight;
        }
        widthMeasureSpec=getMeasureSpec(widthMeasureSpec,resWidth);
        heightMeasureSpec=getMeasureSpec(heightMeasureSpec,resHeight);
        /*widthMeasureSpec=MeasureSpec.makeMeasureSpec(resWidth,MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(resHeight,MeasureSpec.getMode(heightMeasureSpec));*/
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
    //判断content或者parent
    private int getMeasureSpec(int measureSpec,int other){
        switch (MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                return measureSpec;
            case MeasureSpec.AT_MOST:
                return MeasureSpec.makeMeasureSpec(other,MeasureSpec.getMode(measureSpec));
                default:
                    return measureSpec;
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int windex=0;
        int height=0;
        int limitheight=getMeasuredHeight();
        int width=maxWidth.get(windex);
        for(int i=0;i<getChildCount();i++){
            View view=getChildAt(i);

            if(height+view.getMeasuredHeight()>limitheight){
                //另起一列
                height=0;
                windex++;
                width=maxWidth.get(windex);
            }
            //Log.d(TAG, "onLayout: "+i+"  "+width+"  "+height);
            view.layout(width,height,width+view.getMeasuredWidth(),height+view.getMeasuredHeight());
            height+=view.getMeasuredHeight();
        }
    }
}
