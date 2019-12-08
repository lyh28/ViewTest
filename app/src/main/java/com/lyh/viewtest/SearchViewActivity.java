package com.lyh.viewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lyh.viewtest.SearchView.SearchView;

public class SearchViewActivity extends AppCompatActivity implements SearchView.SearchTouch {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        searchView=findViewById(R.id.search);

    }

    @Override
    public void openTouch() {
        searchView.startOpen();
    }

    @Override
    public void SearchTouch() {
        Toast.makeText(this,"搜索",Toast.LENGTH_SHORT).show();
    }
}
