package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.for_sugar.DBDoctorResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

public class TempActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    private boolean isAreaFound = false;
    private boolean isSuccessApiCalled = true;

    private List<DBDoctorResponse.DoctorsBean> listDoctorMain = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }
}
