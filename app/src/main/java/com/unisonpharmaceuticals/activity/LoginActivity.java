package com.unisonpharmaceuticals.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.LoginResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;

    @BindView(R.id.inputUserId)
    TextInputLayout inputUserId;
    @BindView(R.id.edtUserId)
    EditText edtUserId;

    @BindView(R.id.llLoading)
    LinearLayout llLoading;

    @BindView(R.id.llUserLogin)LinearLayout llUserLogin;
    @BindView(R.id.inputPassword)
    TextInputLayout inputPassword;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.btnLogin)
    TextView btnLogin;

    @BindView(R.id.txtVersion)
    TextView txtVersion;

    //For Manager

    @BindView(R.id.llManagerLogin)LinearLayout llManagerLogin;
    @BindView(R.id.edtManageCode)EditText edtManageCode;
    @BindView(R.id.edtManagerPassword)EditText edtManagerPassword;
    @BindView(R.id.edtEmployeeCode)EditText edtEmployeeCode;
    @BindView(R.id.tvManageText)TextView tvManageText;
    @BindView(R.id.tvVersion)TextView tvVersion;

    private boolean isClickedManage = false;

    SharedPreferences preferences;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginPrefUnison";

    private  ApiInterface apiService;

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(this);
        try
        {
            String token = AppUtils.getValidAPIStringResponse(FirebaseInstanceId.getInstance().getToken());
            Log.v("TOKEN ID", token + " ");

            sessionManager.saveTokenId(token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        basicProcesses();
        Log.e("version mismatched >> ", "onCreate: "+SplashActivity.isVersionMismatch );
        if(SplashActivity.isVersionMismatch)
        {
            AppUtils.showVersionMismatchDialog(activity,SplashActivity.isForcefulUpdate);
        }
    }

    @Override
    public void initViews()
    {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewClick()
    {
        tvManageText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(llManagerLogin.getVisibility()==View.VISIBLE)
                {
                    llManagerLogin.setVisibility(View.GONE);
                    llUserLogin.setVisibility(View.VISIBLE);
                    tvManageText.setText("Click here to login as Manager");
                }
                else
                {
                    llUserLogin.setVisibility(View.GONE);
                    llManagerLogin.setVisibility(View.VISIBLE);
                    tvManageText.setText("Click here to login as User");
                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    AppUtils.hideKeyboard(edtUserId,activity);
                    if(sessionManager.isNetworkAvailable())
                    {
                        if(llManagerLogin.getVisibility()==View.VISIBLE)
                        {
                            if(edtManageCode.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide Manager code.");
                            }
                            else if(edtManagerPassword.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Enter your password.");
                            }
                            else if(edtEmployeeCode.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide employee code.");
                            }
                            else
                            {
                                checkStoragePermission();
                            }
                        }
                        else
                        {
                            if(edtUserId.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide Employee Code.");
                            }
                            else if(edtPassword.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Enter your password.");
                            }
                            else
                            {
                                checkStoragePermission();
                            }
                        }
                    }
                    else
                    {
                        showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try
                    {

                        if(sessionManager.isNetworkAvailable())
                        {
                            if(edtUserId.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide User Id, Email or Phone no.");
                            }
                            else if(edtPassword.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Enter your password.");
                            }
                            else
                            {
                                checkStoragePermission();
                            }
                        }
                        else
                        {
                            showToast(activity,activity.getString(R.string.network_failed_message));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return handled;
            }
        });

        edtEmployeeCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try
                    {
                        if(sessionManager.isNetworkAvailable())
                        {
                            if(edtManageCode.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide Manager code.");
                            }
                            else if(edtManagerPassword.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Enter your password.");
                            }
                            else if(edtEmployeeCode.getText().toString().trim().length()==0)
                            {
                                AppUtils.showToast(activity,"Please provide employee code.");
                            }
                            else
                            {
                                checkStoragePermission();
                            }
                        }
                        else
                        {
                            showToast(activity,activity.getString(R.string.network_failed_message));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return handled;
            }
        });
    }

    private void showAlreadyLogoutDialog(String message, final String userId)
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtConfirmation = dialog.findViewById(R.id.tvDescription);
            TextView tvOtherText = dialog.findViewById(R.id.tvOtherText);
            tvOtherText.setVisibility(View.VISIBLE);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Already Login");

            txtConfirmation.setText(message);

            btnNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }

                    if (sessionManager.isNetworkAvailable())
                    {

                        Log.e("****** ", "onClick: "+userId);

                        retrofit2.Call<CommonResponse> logoutCall = apiService.forceLogout(userId);
                        logoutCall.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<CommonResponse> call, Response<CommonResponse> response)
                            {
                                if(response.body().getSuccess()==1)
                                {
                                    AppUtils.showToast(activity, "Your have successfully logged out.");
                                }
                                else
                                {
                                    AppUtils.showToast(activity,activity.getString(R.string.api_failed_message));
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<CommonResponse> call, Throwable t) {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        });
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
            });
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUserField()
    {
        isClickedManage = true;
        llManagerLogin.setVisibility(View.GONE);
        edtManageCode.setText("");
        tvManageText.setText("Click here to login as User");
    }

    private void setManageField()
    {
        isClickedManage = false;
        llManagerLogin.setVisibility(View.VISIBLE);

    }

    @Override
    public void getDataFromServer() {

    }

    private void apiTaskLogin(final String account,final String password,final String employee_code)
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            Call<LoginResponse> loginCall = apiService.login(account,password,employee_code,"true",AppUtils.getDeviceInfo());
            loginCall.enqueue(new Callback<LoginResponse>()
            {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        showToast(activity,response.body().getMessage());

                        if(response.body().getSuccess()==1)
                        {
                            //response.body().setLocal_emp_login(false);

                            if(response.body().isLocal_emp_login())
                            {
                                LoginResponse.AdminBean bean = response.body().getAdmin();
                                sessionManager.createLoginSession(bean.getAdmin_id(),
                                        bean.getEmail(),
                                        bean.getUsername(),
                                        bean.getFirst_name(),
                                        bean.getLast_name(),
                                        bean.getPhone(),
                                        false);

                                sessionManager.setIsForLogoutOnDestroy(false);

                                sessionManager.setIsManagerLoggedIn(response.body().isIs_manager_login());

                                sessionManager.setDivisions(new Gson().toJson(bean.getTerritory_code()));

                                sessionManager.setOnLeave(bean.getForce_leave());

                                //Checking if only one territory code than select it default, no need to shoe select territory dialog for one item
                                JSONArray divArray = null;
                                try
                                {
                                    divArray = new JSONArray(sessionManager.getDivisions());
                                    if(divArray.length()>0 && divArray.length()==1)
                                    {

                                        JSONObject obj = (JSONObject) divArray.get(0);
                                        LoginResponse.AdminBean.TerritoryCodeBean tBean = new LoginResponse.AdminBean.TerritoryCodeBean();
                                        tBean.setCode(obj.getString("code"));
                                        tBean.setDesignation(obj.getString("designation"));
                                        tBean.setEmployee_id(obj.getString("employee_id"));
                                        tBean.setUser_type(obj.getString("user_type"));
                                        tBean.setIs_lock_stk(obj.getString("is_lock_stk"));
                                        tBean.setIs_stk_done(obj.getString("is_stk_done"));
                                        tBean.setIs_offday(obj.getString("is_offday"));

                                        tBean.setIs_day_end(obj.getBoolean("is_day_end"));
                                        tBean.setIs_sample_entry(obj.getString("is_sample_entry"));
                                        tBean.setIs_sample_report(obj.getString("is_sample_report"));
                                        tBean.setIs_sales_entry(obj.getString("is_sales_entry"));
                                        tBean.setIs_sales_report(obj.getString("is_sales_report"));
                                        tBean.setDivision(obj.getString("division"));

                                        sessionManager.setSalesPermission(tBean.getIs_sales_entry());
                                        sessionManager.setSalesReportPermission(tBean.getIs_sales_report());
                                        sessionManager.setSamplePermission(tBean.getIs_sample_entry());
                                        sessionManager.setSampleReportPermission(tBean.getIs_sample_report());
                                        sessionManager.setCanSTK(tBean.getIs_lock_stk());
                                        sessionManager.setOffDayOrAdminDay(tBean.getIs_offday());

                                        sessionManager.setDayEnd(String.valueOf(tBean.getIs_day_end()));
                                        sessionManager.setTerritoryCode(tBean.getCode());
                                        sessionManager.setUserId(tBean.getEmployee_id());
                                        sessionManager.setUserType(tBean.getUser_type());

                                        if(tBean.getIs_stk_done().equals("1"))
                                        {
                                            sessionManager.setIsSTKDone(true);
                                        }
                                        else
                                        {
                                            sessionManager.setIsSTKDone(false);
                                        }

                                        Log.e("usertype", "onResponse: "+tBean.getUser_type());

                                        AppUtils.updateDeviceTokenForFCM(activity,apiService);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                llLoading.setVisibility(View.GONE);
                                activity.finish();
                                if(divArray.length()>0 && divArray.length()==1)
                                {
                                    if(sessionManager.isOnLeave()==1)
                                    {
                                        Intent intent = new Intent(activity,PendingLeaveActivity.class);
                                        intent.putExtra("isFor","login");
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        startActivity(new Intent(activity,DashboardActivity.class));
                                    }
                                }
                                else
                                {
                                    startActivity(new Intent(activity,DashboardActivity.class));
                                }
                                AppUtils.startActivityAnimation(activity);
                            }
                            else
                            {
                                sessionManager.createLoginSession("",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        true);

                                activity.finish();
                                Intent intent = new Intent(activity,SpecialActivity.class);
                                startActivity(intent);
                                AppUtils.startActivityAnimation(activity);
                            }
                        }
                        else if(response.body().getSuccess()==2)
                        {
                            llLoading.setVisibility(View.GONE);
                            showAlreadyLogoutDialog(response.body().getMessage(),response.body().getUser_id());
                            return;
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
                            return;
                        }

                        llLoading.setVisibility(View.GONE);
                    }
                    else
                    {
                        AppUtils.apiFailedToast(activity);
                        llLoading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    llLoading.setVisibility(View.GONE);
                    AppUtils.apiFailedToast(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkStoragePermission()
    {
        try
        {
            int result;
            result = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED)
            {
                if(llManagerLogin.getVisibility() == View.VISIBLE)
                {
                    apiTaskLogin(edtManageCode.getText().toString().trim(),edtManagerPassword.getText().toString().trim(),edtEmployeeCode.getText().toString().trim());
                }
                else
                {
                    apiTaskLogin(edtUserId.getText().toString().trim(),edtPassword.getText().toString().trim(),"");
                }
            }
            else
            {
                ActivityCompat.requestPermissions(activity,new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },PERMISSION_REQUEST_CODE_STORAGE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE_STORAGE:
                try
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        if(llManagerLogin.getVisibility()==View.VISIBLE)
                        {
                            apiTaskLogin(edtManageCode.getText().toString().trim(),edtManagerPassword.getText().toString(),edtEmployeeCode.getText().toString().trim());
                        }
                        else
                        {
                            apiTaskLogin(edtUserId.getText().toString().trim(),edtPassword.getText().toString(),"");
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity,"Permissions Denied!");
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            ActivityCompat.finishAffinity(activity);
            if(sessionManager.isLoggedIn())
            {
                Log.e("User Logged In", "Called");
            }
            else
            {
                Log.e("User Logged out", "Called");
                finish();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onBackPressed();
    }
}
