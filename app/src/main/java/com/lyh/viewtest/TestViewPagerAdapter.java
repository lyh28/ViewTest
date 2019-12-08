package com.lyh.viewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "TestViewPagerAdapter";
    List<Integer> viewList;
    List<View> imageViews;
    Context context;
    public TestViewPagerAdapter(List<Integer> list,Context context) {
        super();
        viewList=list;
        this.context=context;
        imageViews=new ArrayList<>();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: ");
        position=position%viewList.size();

        ImageView imageView=new ImageView(context);
        imageView.setImageResource(viewList.get(position));
        container.addView(imageView);
        if(!imageViews.contains(imageView)) imageViews.add(imageView);
        /*TextView text=new TextView(context);
        text.setText(position+"个页面");
        container.addView(text);
        if(!imageViews.contains(text)) imageViews.add(text);*/
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        position=position%viewList.size();
        container.removeView(imageViews.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }
}
