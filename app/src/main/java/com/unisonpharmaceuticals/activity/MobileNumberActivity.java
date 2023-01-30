package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.OneTimeSession;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private OneTimeSession oneTimeSession;
    private ApiInterface apiService;
    @BindView(R.id.inputMobile)
    TextInputLayout inputMobile;
    @BindView(R.id.edtMobile)EditText edtMobile;
    @BindView(R.id.btnSubmit)TextView btnSubmit;
    @BindView(R.id.llLoading)LinearLayout llLoading;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        activity = this;
        ButterKnife.bind(activity);
        sessionManager = new SessionManager(activity);
        oneTimeSession = new OneTimeSession(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initView();
        if(SplashActivity.isVersionMismatch)
        {
            AppUtils.showVersionMismatchDialog(activity,SplashActivity.isForcefulUpdate);
        }
    }

    private void initView() {

    }

    @OnClick(R.id.btnSubmit)
    public void Clicks(View view)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(edtMobile.getText().toString().equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please enter your mobile number");
        }
        else if(edtMobile.getText().toString().trim().length()!=10)
        {
            AppUtils.showToast(activity,"Please enter valid mobile number");
        }
        else
        {
            if(sessionManager.isNetworkAvailable())
            {
                AppUtils.hideKeyboard(edtMobile,activity);

                llLoading.setVisibility(View.VISIBLE);
                Call<CommonResponse> call = apiService.sendMobileOTP(edtMobile.getText().toString().trim());
                call.enqueue(new Callback<CommonResponse>()
                {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        if(response.isSuccessful())
                        {
                            if(response.body()!=null)
                            {
                                AppUtils.showToast(activity,response.body().getMessage());
                                if(response.body().getSuccess()==1)
                                {
                                    oneTimeSession.setMobileNumber(edtMobile.getText().toString().trim());
                                    Intent intent = new Intent(activity,AuthenticateActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.api_failed_message));
                            }
                        }
                        llLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.api_failed_message));
                        llLoading.setVisibility(View.GONE);
                    }
                });
            }
            else
            {
                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
            }
        }
    }
}
