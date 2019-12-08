package com.lyh.viewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button smile=findViewById(R.id.to_smile);
        Button loading=findViewById(R.id.to_loading);
        Button search=findViewById(R.id.to_search);

        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(SmileViewActivity.class);
            }
        });
        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoadingViewActivity.class);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(SearchViewActivity.class);
            }
        });

    }

    //跳转activity
    private void toActivity(Class activity){
        startActivity(new Intent(this,activity));
    }
}
