package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectProductActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;

    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;

    private String isFor =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        activity = this;
        ButterKnife.bind(activity);
        sessionManager = new SessionManager(activity);
        isFor = getIntent().getStringExtra("isFor");



        initViews();

    }

    private void initViews()
    {
        rvProduct.setLayoutManager(new LinearLayoutManager(activity));

    }
}
