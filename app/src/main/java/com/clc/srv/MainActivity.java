package com.clc.srv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.clc.srv.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private PageRecyclerView mPageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPageRecyclerView = mainBinding.prvList;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ViewAdapter adapter = new ViewAdapter(this);
        mPageRecyclerView.setLayoutManager(linearLayoutManager);
        mPageRecyclerView.setAdapter(adapter);
    }
}