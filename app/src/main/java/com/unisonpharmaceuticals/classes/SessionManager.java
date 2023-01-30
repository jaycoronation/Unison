package com.unisonpharmaceuticals.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.LoginActivity;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.service.MYService;
import com.unisonpharmaceuticals.service.MYServiceForThreeHoursInterval;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.JobSchedulerHelper;

import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint({"CommitPrefEdits", "UseValueOf"})
public class SessionManager {
    private SharedPreferences preferences;
    private Editor editor;
    private Activity activity;
    private Context context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPrefUnisonNew";
    private static final String IS_LOGIN = "unisonLoginStatus";

    /*New Added*/
    private static final String KEY_EMAIL = "unisonEmail";
    private static final String KEY_FIRSTNAME = "unisonFirstName";
    private static final String KEY_LASTNAME = "unisonLastName";
    private static final String KEY_CONTACT_NUMBER = "unisonContactNumber";

    private static final String KEY_USERNAME = "unisonUserName";
    private static final String KEY_USER_PHONE_NUMBER = "unisonPhoneNumber";
    private static final String KEY_EMP_ID = "unisonEmpId";
    private static final String IS_FIRST_TIME = "FirstTime";
    private static final String CAN_APPROVE_LEAVE = "CanApproveLeave";
    private static final String KEY_TERRITORY_CODE = "unisonTerritoryCode";
    private static final String KEY_DIV_ID = "unisonDivId";
    private static final String KEY_MKT_ID = "mktId";
    private static final String KEY_MKT_CODE = "mktCode";
    private static final String KEY_MKT_PLACE_NAME = "mktPlaceName";
    private static final String KEY_PARENT_MKT_ID = "parentMktId";
    private static final String KEY_STAFF_ID = "staffId";
    private static final String KEY_DIV_NAME = "divName";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_MANAGER_ID = "managerId";
    private static final String KEY_USER_CAN_MAKE_DCR_CALL = "makeDCRCall";
    private static final String KEY_CALL_DONE_FROM_TP = "callDoneFromTP";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_DAY_END = "dayEnd";
    private static final String KEY_CURRENT_DATE = "currentDate";
    private static final String KEY_SERVER_DATE = "serverDate";
    private static final String DEVICE_NAME = "deviceName";

    private static final String KEY_PERMISSION_SALES_UPDATE = "salesUpdate";
    private static final String KEY_PERMISSION_SAMPLE_UPDATE = "sampleUpdate";

    private static final String KEY_PERMISSION_SALES_UPDATE_REPORT = "salesUpdateReport";
    private static final String KEY_PERMISSION_SAMPLE_UPDATE_REPORT = "sampleUpdateReport";

    private static final String KEY_MAKE_STK = "canUserMakeSTKEntry";
    private static final String KEY_STK_DONE = "isSTKREntryDone";

    private static final String KEY_DAILYWORK = "keyDailyWork";


    private static final String KEY_FCM_TOKEN = "FCMTokenId";

    private static final String SHOW_EXIT_DIALOG = "ShowExitDialog";

    private static final String IS_DATA_FETCHED = "isDataFetchedFromServer";

    private static final String IS_MANAGER_LOGGEDIN = "isManagerLoggedIn";

    // manager if leave select, restrict user
    private static final String MANAGER_IS_ACTION_PENDING = "isActionPendingManager";
    private static final String IS_ON_LEAVE = "isOnLeaveUnison";

    //For Report List
    private static final String ALL_REPORT_LINK = "allReportLink";

    //For Division
    private static final String DIVISION = "devisions";

    private static final String KEY_FIELD_WORK = "FieldWork";

    private static final String ADD_AREA_TP = "addAreaForTP";

    private static final String IS_SPECIAL_LOGIN = "isSpecialLogin";

    private static final String ONLY_SALES_PRODUCT = "onlySalesProduct";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        preferences = activity.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String userid,
                                   String email,
                                   String userName,
                                   String firstName,
                                   String lastName,
                                   String contactNumber,
                                   boolean isSpecialLogin) {
        editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMP_ID, userid);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_CONTACT_NUMBER, contactNumber);
        editor.putBoolean(IS_SPECIAL_LOGIN, isSpecialLogin);
        editor.commit();
    }

    public String getTokenId() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_FCM_TOKEN, ""));
    }

    public void saveTokenId(String token) {
        editor = preferences.edit();
        editor.putString(KEY_FCM_TOKEN, token);
        editor.commit();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setIsFirstTime(boolean b) {
        editor.putBoolean(IS_FIRST_TIME, b);
        editor.commit();
    }

    public String getContactNumber() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CONTACT_NUMBER, ""));
    }

    public void setUserType(String userType) {
        editor = preferences.edit();
        editor.putString(KEY_USER_TYPE, userType);
        editor.commit();
    }

    public String getUSerType() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_USER_TYPE, ""));
    }

    public void setOffDayOrAdminDay(String type) {
        editor = preferences.edit();
        editor.putString(KEY_DAILYWORK, type);
        editor.commit();
    }

    public String getOffDayOrAdminDay() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_DAILYWORK, ""));
    }

    public void setDeviceName(String deviceName) {
        editor = preferences.edit();
        editor.putString(DEVICE_NAME, deviceName);
        editor.commit();
    }

    public String getDeviceName() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(DEVICE_NAME, ""));
    }

    public String getFirstName() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_FIRSTNAME, ""));
    }

    public String getLastName() {
        return AppUtils.getValidAPIStringResponse(preferences.getString(KEY_LASTNAME, ""));
    }

    public boolean canApproveLeave() {
        return preferences.getBoolean(CAN_APPROVE_LEAVE, false);
    }

    public void setCanApproveLeave(boolean b) {
        editor.putBoolean(CAN_APPROVE_LEAVE, b);
        editor.commit();
    }

    public boolean showExitDialog() {
        return preferences.getBoolean(SHOW_EXIT_DIALOG, true);
    }

    public void setShowExitDialog(boolean b) {
        editor.putBoolean(SHOW_EXIT_DIALOG, b);
        editor.commit();
    }

    public int getFieldWork() {
        return preferences.getInt(KEY_FIELD_WORK, 0);
    }

    public void setFieldWork(int fieldWork) {
        editor = preferences.edit();
        editor.putInt(KEY_FIELD_WORK, fieldWork);
        editor.commit();
    }


    public String getArea() {
        return preferences.getString(ADD_AREA_TP, "");
    }

    public void setArea(String notPlannedArea) {
        editor = preferences.edit();
        editor.putString(ADD_AREA_TP, notPlannedArea);
        editor.commit();
    }

    public String getOnlySalesProduct() {
        return preferences.getString(ONLY_SALES_PRODUCT, "");
    }

    public void setOnlySalesProduct(String salesProductResponse) {
        editor = preferences.edit();
        editor.putString(ONLY_SALES_PRODUCT, salesProductResponse);
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // if user is not logged in, redirect him to Login page
            Intent i = new Intent(activity, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            activity.startActivity(i);
            activity.finish();
        }
    }

    public String getManagerId() {
        return preferences.getString(KEY_MANAGER_ID, "");
    }

    public void setManagerId(String name) {
        editor.putString(KEY_MANAGER_ID, name);
        editor.commit();
    }

    public String getUserCanMakeDcrCall() {
        return preferences.getString(KEY_USER_CAN_MAKE_DCR_CALL, "");
    }

    public void setUserCanMakeDcrCall(String name) {
        editor.putString(KEY_USER_CAN_MAKE_DCR_CALL, name);
        editor.commit();
    }

    public String getCallDoneFromTP() {
        return preferences.getString(KEY_CALL_DONE_FROM_TP, "");
    }

    public void setCallDoneFromTP(String name) {
        editor.putString(KEY_CALL_DONE_FROM_TP, name);
        editor.commit();
    }

    public String getCurrentDate() {
        return preferences.getString(KEY_CURRENT_DATE, "");
    }

    public void setCurrentDate(String name) {
        editor.putString(KEY_CURRENT_DATE, name);
        editor.commit();
    }

    public String getServerDate() {
        return preferences.getString(KEY_SERVER_DATE, "");
    }

    public void setServerDate(String name) {
        editor.putString(KEY_SERVER_DATE, name);
        editor.commit();
    }

    public String getDate() {
        return preferences.getString(KEY_DATE, "");
    }

    public void setDate(String name) {
        editor.putString(KEY_DATE, name);
        editor.commit();
    }

    public String getMktId() {
        return preferences.getString(KEY_MKT_ID, "");
    }

    public void setMktId(String name) {
        editor.putString(KEY_MKT_ID, name);
        editor.commit();
    }

    public String getDesignation() {
        return preferences.getString(KEY_DESIGNATION, "");
    }

    public void setDesignation(String name) {
        editor.putString(KEY_DESIGNATION, name);
        editor.commit();
    }

    public String getUser() {
        return preferences.getString(KEY_USER, "user");
    }

    public String getDivName() {
        return preferences.getString(KEY_DIV_NAME, "");
    }

    public void setDivName(String name) {
        editor.putString(KEY_DIV_NAME, name);
        editor.commit();
    }

    public String getStaffId() {
        return preferences.getString(KEY_STAFF_ID, "");
    }

    public void setStaffId(String name) {
        editor.putString(KEY_STAFF_ID, name);
        editor.commit();
    }

    public String getParentMktId() {
        return preferences.getString(KEY_PARENT_MKT_ID, "");
    }

    public void setParentMktId(String name) {
        editor.putString(KEY_PARENT_MKT_ID, name);
        editor.commit();
    }

    public String getMktPlace() {
        return preferences.getString(KEY_MKT_PLACE_NAME, "");
    }

    public void setMktPlace(String name) {
        editor.putString(KEY_MKT_PLACE_NAME, name);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public boolean isSpecialLogin() {
        return preferences.getBoolean(IS_SPECIAL_LOGIN, false);
    }

    public String getMktCode() {
        return preferences.getString(KEY_MKT_CODE, "");
    }

    public void setMktCode(String name) {
        editor.putString(KEY_MKT_CODE, name);
        editor.commit();
    }

    public String getUserId() {
        return preferences.getString(KEY_EMP_ID, "");
    }

    public void setUserId(String name) {
        editor.putString(KEY_EMP_ID, name);
        editor.commit();
    }

    public String getUserName() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public void setUserName(String name) {
        editor.putString(KEY_USERNAME, name);
        editor.commit();
    }

    public String getUserPhoneNumber() {
        return preferences.getString(KEY_USER_PHONE_NUMBER, "");
    }

    public void setUserPhoneNumber(String name) {
        editor.putString(KEY_USER_PHONE_NUMBER, name);
        editor.commit();
    }

    public String getTerritoryCode() {
        return preferences.getString(KEY_TERRITORY_CODE, "");
    }

    public void setTerritoryCode(String name) {
        editor.putString(KEY_TERRITORY_CODE, name);
        editor.commit();
    }

    public String getDivId() {
        return preferences.getString(KEY_DIV_ID, "");
    }

    public void setDivId(String name) {
        editor.putString(KEY_DIV_ID, name);
        editor.commit();
    }

    public String getSalesPermission() {
        return preferences.getString(KEY_PERMISSION_SALES_UPDATE, "");
    }

    public void setSalesPermission(String name) {
        editor.putString(KEY_PERMISSION_SALES_UPDATE, name);
        editor.commit();
    }

    public String getSalesReportPermission() {
        return preferences.getString(KEY_PERMISSION_SALES_UPDATE_REPORT, "");
    }

    public void setSalesReportPermission(String name) {
        editor.putString(KEY_PERMISSION_SALES_UPDATE_REPORT, name);
        editor.commit();
    }

    public String getSamplePermission() {
        return preferences.getString(KEY_PERMISSION_SAMPLE_UPDATE, "");
    }

    public void setSamplePermission(String name) {
        editor.putString(KEY_PERMISSION_SAMPLE_UPDATE, name);
        editor.commit();
    }

    public String getSampleReportPermission() {
        return preferences.getString(KEY_PERMISSION_SAMPLE_UPDATE_REPORT, "");
    }

    public void setSampleReportPermission(String name) {
        editor.putString(KEY_PERMISSION_SAMPLE_UPDATE_REPORT, name);
        editor.commit();
    }

    public String getCanSTK() {
        return preferences.getString(KEY_MAKE_STK, "0");
    }

    public void setCanSTK(String canMakeStk) {
        editor.putString(KEY_MAKE_STK, canMakeStk);
        editor.commit();
    }

    public boolean isSTKDone() {
        return preferences.getBoolean(KEY_STK_DONE, false);
    }

    public void setIsSTKDone(boolean isSTKEntryDone) {
        editor.putBoolean(KEY_STK_DONE, isSTKEntryDone);
        editor.commit();
    }

    public String getDayEnd() {
        return preferences.getString(KEY_DAY_END, "");
    }

    public void setDayEnd(String name) {
        editor.putString(KEY_DAY_END, name);
        editor.commit();
    }

    public void setAllReportLink(String reportResponse) {
        editor.putString(ALL_REPORT_LINK, reportResponse);
        editor.commit();
    }

    public String getAllReportLink() {
        return preferences.getString(ALL_REPORT_LINK, "");
    }

    public void setDivisions(String divisions) {
        editor.putString(DIVISION, divisions);
        editor.commit();
    }

    public String getDivisions() {
        return preferences.getString(DIVISION, "");
    }

    public void logoutUser() {
        //showLogoutDialog();
        showBottomSheetLogoutDialg();
    }

    public void logoutOnDestroy() {
        editor.clear();
        editor.commit();

        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        setIsForLogoutOnDestroy(true);

        //For stop JobService when user logout
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                    JobSchedulerHelper.cancelJob(context, 11, true);
                }
                if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                    JobSchedulerHelper.cancelJob(context, 22, true);
                }
            } else {
                Intent myService = new Intent(context, MYService.class);
                context.stopService(myService);

                Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                context.stopService(threeOurService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIsForLogoutOnDestroy(boolean isForLogoutOnDestroy) {
        editor = preferences.edit();
        editor.putBoolean("isForLogout", isForLogoutOnDestroy);
        editor.commit();
    }

    public boolean isLogoutOnDestroy() {
        return preferences.getBoolean("isForLogout", false);
    }

    public void logoutWithoutDialog() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                    JobSchedulerHelper.cancelJob(context, 11, true);
                }
                if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                    JobSchedulerHelper.cancelJob(context, 22, true);
                }
            } else {
                Intent myService = new Intent(context, MYService.class);
                context.stopService(myService);

                Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                context.stopService(threeOurService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Hide Keyboard
    public void hidekeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //	check Internet connection
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public boolean isActionPendingManager() {
        return preferences.getBoolean(MANAGER_IS_ACTION_PENDING, false);
    }

    public void setActionPendingManager(boolean b) {
        editor.putBoolean(MANAGER_IS_ACTION_PENDING, b);
        editor.commit();
    }

    public int isOnLeave() {
        return preferences.getInt(IS_ON_LEAVE, 0);
    }

    public void setOnLeave(int isOnLeave) {
        editor.putInt(IS_ON_LEAVE, isOnLeave);
        editor.commit();
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = null;
            if (activity != null && context == null) {
                manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            } else if (activity == null && context != null) {
                manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            }

            if (manager != null) {
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serviceClass.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isDataFetched() {
        return preferences.getBoolean(IS_DATA_FETCHED, false);
    }

    public void setIsDataFetched(boolean isFetched) {
        editor.putBoolean(IS_DATA_FETCHED, isFetched);
        editor.commit();
    }

    public boolean isManagerLoggedIN() {
        return preferences.getBoolean(IS_MANAGER_LOGGEDIN, false);
    }

    public void setIsManagerLoggedIn(boolean isManagerLoggedIN) {
        editor.putBoolean(IS_MANAGER_LOGGEDIN, isManagerLoggedIN);
        editor.commit();
    }

    private void showBottomSheetLogoutDialg() {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeLogout);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
        dialog.setContentView(sheetView);

        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ApiInterface apiSrvice = ApiClient.getClient().create(ApiInterface.class);
                SessionManager sessionManager = new SessionManager(activity);
                if (sessionManager.isNetworkAvailable()) {
                    retrofit2.Call<CommonResponse> logoutCall = apiSrvice.logout(sessionManager.getUserId());
                    logoutCall.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                                            JobSchedulerHelper.cancelJob(context, 11, true);
                                        }
                                        if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                                            JobSchedulerHelper.cancelJob(context, 22, true);
                                        }
                                    } else {
                                        Intent myService = new Intent(context, MYService.class);
                                        context.stopService(myService);

                                        Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                                        context.stopService(threeOurService);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (response.body().getSuccess() == 1) {
                                    editor.clear();
                                    editor.commit();

                                    AppUtils.showToast(activity, "You have successfully logged out.");
                                    Intent i = new Intent(context, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                } else {
                                    editor.clear();
                                    editor.commit();

                                    AppUtils.showToast(activity, "You have successfully logged out.");
                                    Intent i = new Intent(context, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }
                            } else {

                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                                            JobSchedulerHelper.cancelJob(context, 11, true);
                                        }
                                        if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                                            JobSchedulerHelper.cancelJob(context, 22, true);
                                        }
                                    } else {
                                        Intent myService = new Intent(context, MYService.class);
                                        context.stopService(myService);

                                        Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                                        context.stopService(threeOurService);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                editor.clear();
                                editor.commit();

                                AppUtils.showToast(activity, "You have successfully logged out.");
                                Intent i = new Intent(context, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<CommonResponse> call, Throwable t) {

                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                                        JobSchedulerHelper.cancelJob(context, 11, true);
                                    }
                                    if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                                        JobSchedulerHelper.cancelJob(context, 22, true);
                                    }
                                } else {
                                    Intent myService = new Intent(context, MYService.class);
                                    context.stopService(myService);

                                    Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                                    context.stopService(threeOurService);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            editor.clear();
                            editor.commit();

                            AppUtils.showToast(activity, "You have successfully logged out.");
                            Intent i = new Intent(context, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    });

                } else {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (JobSchedulerHelper.isJobServiceRunning(context, 11)) {
                                JobSchedulerHelper.cancelJob(context, 11, true);
                            }
                            if (JobSchedulerHelper.isJobServiceRunning(context, 22)) {
                                JobSchedulerHelper.cancelJob(context, 22, true);
                            }
                        } else {
                            Intent myService = new Intent(context, MYService.class);
                            context.stopService(myService);

                            Intent threeOurService = new Intent(context, MYServiceForThreeHoursInterval.class);
                            context.stopService(threeOurService);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    editor.clear();
                    editor.commit();

                    AppUtils.showToast(activity, "You have successfully logged out.");
                    Intent i = new Intent(context, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
        dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
