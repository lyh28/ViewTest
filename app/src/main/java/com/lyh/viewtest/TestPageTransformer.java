package com.lyh.viewtest;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class TestPageTransformer implements ViewPager.PageTransformer {
    private static final String TAG = "TestPageTransformer";
    @Override
    public void transformPage(@NonNull View view, float v) {
        Log.d(TAG, "transformPage: "+view.toString()+"  "+v);
        /*if(v<=0){
            view.setAlpha(0.25f*v+1f);
            Log.d(TAG, "transformPage: "+(0.1f*v+1f));
            view.setScaleX(0.1f*v+1f);
            view.setScaleY(0.1f*v+1f);
        }else{
            view.setAlpha(-0.25f*v+1f);
            Log.d(TAG, "transformPage: "+(-0.1f*v+1f));
            view.setScaleX(-0.1f*v+1f);
            view.setScaleY(-0.1f*v+1f);
        }*/


        /*if(v<-1){
           // view.setAlpha(0.75f);
        }else if(v<=1){
            if(v<=0){
                //view.setAlpha(0.25f*v+1f);
                view.setScaleX(0.25f*v+0.75f);
                view.setScaleY(0.25f*v+0.75f);

            }else{
                //view.setAlpha(0.25f*v+1f);
                view.setScaleX(-0.25f*v+0.75f);
                view.setScaleY(0.25f*v+0.75f);

            }
        }else{
           // view.setAlpha(0.75f);
        }*/
    }
}
