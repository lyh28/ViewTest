package com.lyh.viewtest.Scroller;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/*
    父布局 垂直布局
 */
public class TestLayout extends ViewGroup {
    private static final String TAG = "父控件";
    Scroller scroller;
    int minLen;     //判断移动的距离
    float lastDown;
    float lastMove;
    int upBorder;       //上边界
    int downBorder;     //下边界

    int viewHeight;         //高度
    int offset=0;       //偏移量
    int initPos=1;        //初始位置
    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
        ViewConfiguration viewConfiguration=ViewConfiguration.get(context);
        minLen=ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childcount=getChildCount();
        int childH=0;
        int childW=0;
        for(int i=0;i<childcount;i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        if(childcount!=0){
            childW=getChildAt(0).getMeasuredWidth();
            childH=getChildAt(0).getMeasuredHeight();
        }
        viewHeight=childH;
        setMeasuredDimension(childW,childH*2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childcount=getChildCount();
        int h=0;
        for(int i=0;i<childcount;i++){
            View view=getChildAt(i);
            view.layout(0,h,view.getMeasuredWidth(),h+view.getMeasuredHeight());
            h+=view.getMeasuredHeight();
        }
        upBorder=getChildAt(0).getTop();
        downBorder=getChildAt(childcount-1).getBottom();
        scrollTo(0,upBorder+viewHeight*initPos);
    }

    /**
     *  移除最后view，添加至第一个
     */
    private void addPreView(){
        for(int i=0;i<getChildCount();i++){
            TestViewGroup testViewGroup=(TestViewGroup)getChildAt(i);
            Log.d(TAG, "addPreView: 原本："+testViewGroup.getText().toString());
        }
        if(getChildCount()!=0){
            View view=getChildAt(getChildCount()-1);
            removeViewAt(getChildCount()-1);
            Log.d(TAG, "addPreView: 移除view "+((TestViewGroup)view).getText().toString()+"   "+getChildCount());
            addView(view,0);
        }
        for(int i=0;i<getChildCount();i++){
            TestViewGroup testViewGroup=(TestViewGroup)getChildAt(i);
            Log.d(TAG, "addPreView    移到最前:  "+i+"  "+testViewGroup.getText().toString());
        }
    }

    /**
     * 移除第一个view，添加至最后一个
     */
    private void addLastView(){
        for(int i=0;i<getChildCount();i++){
            TestViewGroup testViewGroup=(TestViewGroup)getChildAt(i);
            Log.d(TAG, "addPreView: 原本："+testViewGroup.getText().toString());
        }
        if(getChildCount()!=0){
            View view=getChildAt(0);
            removeViewAt(0);
            Log.d(TAG, "addPreView: 移除view "+((TestViewGroup)view).getText().toString()+"   "+getChildCount());
            addView(view,getChildCount());
        }
        for(int i=0;i<getChildCount();i++){
            TestViewGroup testViewGroup=(TestViewGroup)getChildAt(i);
            Log.d(TAG, "addLastView   移到最后:  "+i+"  "+testViewGroup.getText().toString());
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastDown=ev.getRawY();
                break;
                case MotionEvent.ACTION_MOVE:
                    float len=Math.abs(ev.getRawX()-lastDown);
                    if(len>minLen){
                        lastMove=ev.getRawY();
                        return true;
                    }
                    break;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastMove=event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                /*if(!isstart)
                    break;*/
                float newMove=event.getRawY();
                float diffX=lastMove-newMove;
                offset+=diffX;
                if(offset>=viewHeight){
                    offset-=viewHeight;
                    addLastView();
                }else if(offset<=-viewHeight){
                    offset+=viewHeight;
                    addPreView();
                }
                //判断有无出边界
                /*if(diffX+getScrollY()<upBorder){
                    scrollTo(0,-upBorder);

                    return true;
                }else if(diffX+getScrollY()+getMeasuredHeight()>downBorder){
                    scrollTo(0,downBorder-getMeasuredHeight());
                    return true;
                }*/
                scrollBy(0,(int)diffX);
                lastMove=newMove;
                break;
            case MotionEvent.ACTION_UP:
                //跳转子项中

                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
