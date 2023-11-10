package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class AuthenticateActivity extends AppCompatActivity
{
    private Activity activity;
    private OneTimeSession oneTimeSession;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    @BindView(R.id.edtOTP)EditText edtOTP;
    @BindView(R.id.btnSubmit)TextView btnSubmit;
    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.pbTime)ProgressBar pbTime;
    @BindView(R.id.txtTimer)TextView txtTimer;
    @BindView(R.id.llChangeMobileNumber)LinearLayout llChangeMobileNumber;

    private CountDownTimer mCountDownTimer;
    int i = 0,secondsLeft = 0;

    private boolean isMobileVerified = false;

    private long mLastClickTime = 0;

    public static boolean isActivityRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        activity = AuthenticateActivity.this;
        isActivityRunning = true;
        sessionManager = new SessionManager(activity);
        oneTimeSession = new OneTimeSession(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(activity);
        setupData();
       /* SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText)
            {
                if(!oneTimeSession.isAuthenticate())
                {
                    if(edtOTP!=null &&
                            llLoading!=null &&
                            oneTimeSession!=null &&
                            sessionManager!=null &&
                            activity!=null
                            && btnSubmit!=null )
                    {
                        edtOTP.setText(messageText);
                        checkOTP();
                    }
                }
            }
        });*/
    }
    @OnClick({R.id.btnSubmit,R.id.llChangeMobileNumber})
    public void onClick(View view)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (view.getId())
        {
            case R.id.btnSubmit:
                if(edtOTP.getText().toString().trim().length()==0)
                {
                    AppUtils.showToast(activity,"Please enter One Time Password.");
                }
                else
                {
                    checkOTP();
                }
                break;
            case R.id.llChangeMobileNumber:
                activity.finish();
                AppUtils.startActivityAnimation(activity);
                break;
        }
    }

    private void checkOTP()
    {
        if(sessionManager.isNetworkAvailable())
        {
            AppUtils.hideKeyboard(btnSubmit,activity);
            llLoading.setVisibility(View.VISIBLE);
            Call<CommonResponse> checkOTP = apiService.checkMobileOTP(oneTimeSession.getMobileNumber(),edtOTP.getText().toString().trim());
            checkOTP.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body().getSuccess()==1)
                        {
                            isMobileVerified = true;
                            oneTimeSession.setIsAuthenticate(true);
                            activity.finish();
                            startActivity(new Intent(activity,LoginActivity.class));
                            AppUtils.startActivityAnimation(activity);
                        }
                        else
                        {
                            AppUtils.showToast(activity,response.body().getMessage());
                        }
                    }
                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    AppUtils.showToast(activity,"Something went wrong.");
                    llLoading.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
    }


    private void setupData()
    {
        /*txtTimer.setText("1:30");*/
        txtTimer.setText("15:00");

        pbTime.setProgress(i);
        pbTime.getProgressDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);



        mCountDownTimer = new CountDownTimer(900000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                try
                {
                    long now = millisUntilFinished;
                    secondsLeft = (int) Math.floor((float)now / 1000.0f);
                    txtTimer.setText(timeConversion(secondsLeft));
                    i++;
                    pbTime.setProgress(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish()
            {
                try {
                    txtTimer.setText("00:00");
                    i++;
                    pbTime.setProgress(i);
                    if(mCountDownTimer != null)
                    {
                        mCountDownTimer.cancel();
                        mCountDownTimer = null;
                    }

                    AppUtils.showToast(activity,"OTP Failed ");

                    activity.finish();
                    AppUtils.finishActivityAnimation(activity);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mCountDownTimer.start();
    }


    private String timeConversion(int seconds) {

        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        return  minutes + ":" + seconds;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityRunning = false;
        if(mCountDownTimer != null)
        {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
