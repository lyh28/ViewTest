package com.lyh.viewtest.Scroller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class TestViewGroup extends TextView {
    private static final String TAG = "子控件";

    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
/*
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: "+ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent: DOWN");
                break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "onInterceptTouchEvent: MOVE");
                    return true;
        }
        return false;
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: MOVE");
                break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouchEvent: UP");

                    break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d(TAG, "onTouchEvent: cancel");
                        Log.d(TAG, "onTouchEvent: ");
        }
        return false;
    }
}
