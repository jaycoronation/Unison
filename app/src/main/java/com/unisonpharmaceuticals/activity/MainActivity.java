package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.classes.SimpleDividerItemDecoration;
import com.unisonpharmaceuticals.model.AreaResponse;
import com.unisonpharmaceuticals.model.CommonGetSet;
import com.unisonpharmaceuticals.model.DailyPlannerResponse;
import com.unisonpharmaceuticals.model.GroupNotificationResponse;
import com.unisonpharmaceuticals.model.LoginResponse;
import com.unisonpharmaceuticals.model.ReasonResponse;
import com.unisonpharmaceuticals.model.ReportResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.WorkWithResponse;
import com.unisonpharmaceuticals.model.for_sugar.DBArea;
import com.unisonpharmaceuticals.model.for_sugar.DBDoctor;
import com.unisonpharmaceuticals.model.for_sugar.DBDoctorResponse;
import com.unisonpharmaceuticals.model.for_sugar.DBPlanner;
import com.unisonpharmaceuticals.model.for_sugar.DBReason;
import com.unisonpharmaceuticals.model.for_sugar.DBReports;
import com.unisonpharmaceuticals.model.for_sugar.DBSpeciality;
import com.unisonpharmaceuticals.model.for_sugar.DBStaff;
import com.unisonpharmaceuticals.model.for_sugar.DBVariation;
import com.unisonpharmaceuticals.model.for_sugar.DBWorkWith;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.service.MYService;
import com.unisonpharmaceuticals.service.MYServiceForThreeHoursInterval;
import com.unisonpharmaceuticals.service.MyJobService;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.JobSchedulerHelper;
import com.unisonpharmaceuticals.views.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.rvDashboard) RecyclerView rvDashboard;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.llLoading) LinearLayout llLoading;
    @BindView(R.id.tvLoading) TextView tvLoading;
    @BindView(R.id.frameMain)FrameLayout frameMain;
    @BindView(R.id.ivLogout) ImageView ivLogout;
    @BindView(R.id.ivRefresh)ImageView ivRefresh;
    @BindView(R.id.ivTerritory)ImageView ivTerritory;
    @BindView(R.id.frameNotification)FrameLayout frameNotification;
    @BindView(R.id.llLoadingTransparent)LinearLayout llLoadingTransparent;
    @BindView(R.id.indicator) AVLoadingIndicatorView indicator;
    @BindView(R.id.llNotification)LinearLayout llNotification;
    @BindView(R.id.tvNotification)TextView tvNotification;

    private ArrayList<CommonGetSet> listDashBoard = new ArrayList<>();
    private DashBoardAdapter dashBoardAdapter;

    private boolean isSuccessApiCalled = true;
    private  int toolbarHeight = 0,navigationBarHeight = 0;

    private String selectedUserId = "",selectedTerritoryId = "",selectedUserType = "",is_stk_done = "";
    private boolean isDivisionSelected = false,isDayEnd = false;

    private int mainNotifCounts = 0,loadingCounts = 0;
    private String countTP = "0",countDCR ="0",countGift="0",countTA="0",countNS="0",countsOther = "0";

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DashboardActivity.isAppRunning = true;
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        //If only one territory code than no need to display change territoy icon
        try
        {
            JSONArray divArray = new JSONArray(sessionManager.getDivisions());
            if(divArray.length()!=0)
            {
                if(divArray.length()==1)
                {
                    ivTerritory.setVisibility(View.GONE);
                }
                else
                {
                    ivTerritory.setVisibility(View.VISIBLE);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        basicProcesses();
    }

    @Override
    public void initViews()
    {
        //toolbarHeight = toolbar.getHeight();

        indicator.setIndicatorColor(Color.parseColor("#49ffffff"));

        tvLoading.setVisibility(View.VISIBLE);
        tvLoading.setText("Please wait while downloading data");

        int actionBarHeight = 0;
        final TypedArray styledAttributes = activity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        toolbarHeight = actionBarHeight;

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }

        rvDashboard.setLayoutManager(new GridLayoutManager(activity,2));
        rvDashboard.addItemDecoration(new SimpleDividerItemDecoration(activity));
        callMakeEntryService();
        setDashboardData();
    }

    public void callMakeEntryService()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Log.e("JobService >> ", " Running ? "+JobSchedulerHelper.isJobServiceRunning(activity,11));
            Log.e("JObThreeHourService >> ", " Running ? "+JobSchedulerHelper.isJobServiceRunning(activity,22));

            if(!JobSchedulerHelper.isJobServiceRunning(activity,11))
            {
                JobSchedulerHelper.startJobForPeriodicTask(activity,MyJobService.class,11,1000*60*15);
            }

            /*if(!JobSchedulerHelper.isJobServiceRunning(activity,22))
            {
                JobSchedulerHelper.startJobForPeriodicTask(activity,MyJobServiceForThreeHours.class,22,1000*60*240);
            }*/
        }
        else
        {
            Log.e("Service >> ", " Running ? "+AppUtils.isServiceRunning(MYService.class,getApplicationContext()));
            Log.e("ThreeHourService >> ", " Running ? "+AppUtils.isServiceRunning(MYServiceForThreeHoursInterval.class,getApplicationContext()));

            if(!AppUtils.isServiceRunning(MYService.class,getApplicationContext()))
            {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(activity, MYService.class);
                PendingIntent pendingIntent = PendingIntent.getService(activity, 0, intent, 0);
                AlarmManager contactAlarm = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
                contactAlarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), (60*1000), pendingIntent);
                //Forcefully start the serviceMYService
                Intent contactService = new Intent(activity, MYService.class);
                activity.startService(contactService);
            }

            /*if(!AppUtils.isServiceRunning(MYServiceForThreeHoursInterval.class,getApplicationContext()))
            {
                try
                {
                    Calendar calThree = Calendar.getInstance();
                    Intent intentThree = new Intent(activity, MYServiceForThreeHoursInterval.class);
                    PendingIntent pendingIntentThree = PendingIntent.getService(activity, 0, intentThree, 0);
                    AlarmManager contactAlarmThree = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
                    contactAlarmThree.setRepeating(AlarmManager.RTC_WAKEUP, calThree.getTimeInMillis(), 1000*60*240, pendingIntentThree);
                    //Forcefully start the serviceMYServiceForThreeHour
                    Intent contactServiceThree = new Intent(activity, MYServiceForThreeHoursInterval.class);
                    activity.startService(contactServiceThree);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(sessionManager.isLoggedIn())
        {
            getAllNotifGroupCounts();
        }
        else
        {
            activity.finishAffinity();
            startActivity(new Intent(activity,LoginActivity.class));
            AppUtils.startActivityAnimation(activity);
        }
    }

    @Override
    public void viewClick()
    {
       /* llLoadingTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        llLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                sessionManager.logoutUser();
            }
        });

        ivTerritory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showDivisionDialog();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                getFirstTimeDataFromServer();
            }
        });

        frameNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(sessionManager.isNetworkAvailable())
                {
                    Intent intent = new Intent(activity,NotificationActivity.class);
                    intent.putExtra("type","NOTIFICATIONS");
                    startActivity(intent);
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
            }
        });
    }

    private void showConfirmationDialogForDayEnd()
    {
        try
        {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//	    	dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;
            dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
            dialog.setContentView(R.layout.dialog_confirm_logout);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            TextView txt_Dialog_Delete = (TextView)dialog.findViewById(R.id.txtHeader2_Dialog_Delete);
            TextView txtHeader = (TextView)dialog.findViewById(R.id.txtHeader_Dialog_Delete);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo_Dialog_Confirm);
            Button btnYes = (Button) dialog.findViewById(R.id.btnYes_Dialog_Confirm);

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
                    dialog.dismiss();
                    dialog.cancel();
                    sessionManager.logoutUser();
                }
            });
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getDataFromServer()//Get Data From server and store in local database
    {
        Log.e("IsDataFetched >> ", "getDataFromServer: "+sessionManager.isDataFetched() );


        if(!sessionManager.isDataFetched())
        {
            if(sessionManager.isNetworkAvailable())
            {
                llLoading.setVisibility(View.VISIBLE);


                //New Added for planner flow
                try {
                    Call<DailyPlannerResponse> getPlanner = apiService.getDailyPlannerForOffline(sessionManager.getUserId(),
                            String.valueOf( System.currentTimeMillis()/1000), sessionManager.getUserId());
                    getPlanner.enqueue(new Callback<DailyPlannerResponse>() {
                        @Override
                        public void onResponse(Call<DailyPlannerResponse> call, final Response<DailyPlannerResponse> response)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DBPlanner.deleteAll(DBPlanner.class);
                                    if(response.isSuccessful())
                                    {
                                        if (response.body().getSuccess() == 1)
                                        {
                                            List<DailyPlannerResponse.PlanBean> listPlan = response.body().getPlan();
                                            for (int i = 0; i < listPlan.size(); i++)
                                            {
                                                try {
                                                    Gson gson = new Gson();
                                                    String workWithString = gson.toJson(listPlan.get(i).getWork_array());
                                                    DBPlanner planner = new DBPlanner(listPlan.get(i).getDoctor_id(),
                                                            listPlan.get(i).getDoctor(),
                                                            listPlan.get(i).getSpeciality_id(),
                                                            listPlan.get(i).getSpeciality(),
                                                            listPlan.get(i).getArea_id(),
                                                            listPlan.get(i).getArea(),
                                                            workWithString,
                                                            listPlan.get(i).getEnable_focus());
                                                    planner.save();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }

                        @Override
                        public void onFailure(Call<DailyPlannerResponse> call, Throwable t) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Call<AreaResponse> areaCall = apiService.getAreaFromUserId("1000", sessionManager.getUserId(),sessionManager.getUserId(),"");
                areaCall.enqueue(new Callback<AreaResponse>() {
                    @Override
                    public void onResponse(Call<AreaResponse> call, final Response<AreaResponse> response) {
                        try
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        DBArea.deleteAll(DBArea.class);
                                        ArrayList<AreaResponse.AreasBean> list = (ArrayList<AreaResponse.AreasBean>) response.body().getAreas();

                                        Gson gson = new Gson();
                                        String areaString = gson.toJson(response.body().getAreas());
                                        sessionManager.setArea(areaString);

                                        for (int i = 0; i < list.size(); i++)
                                        {
                                            if(list.get(i).getIs_tour_plan().equalsIgnoreCase("1")) {
                                                DBArea area = new DBArea(list.get(i).getArea_id(),list.get(i).getArea());
                                                area.save();
                                            }
                                        }

                                    } else
                                    {
                                        DBArea.deleteAll(DBArea.class);
                                    }
                                }
                            }).start();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<AreaResponse> call, Throwable t) {
                        DBArea.deleteAll(DBArea.class);
                    }
                });

                Call<DBDoctorResponse> drCall = apiService.getDoctors("","1","1000",sessionManager.getUserId());
                drCall.enqueue(new Callback<DBDoctorResponse>() {
                    @Override
                    public void onResponse(Call<DBDoctorResponse> call, final Response<DBDoctorResponse> response)
                    {
                        try
                        {
                            if (response.body().getSuccess() == 1)
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //List OF all Doctors
                                        List<DBDoctorResponse.DoctorsBean> listDoctrosMain = response.body().getDoctors();

                                        if(listDoctrosMain.size()>0)
                                        {
                                            //Get Area and speciality from doctor list

                                            //DBArea.deleteAll(DBArea.class);
                                            DBSpeciality.deleteAll(DBSpeciality.class);
                                            DBDoctor.deleteAll(DBDoctor.class);

                                            for (int i = 0; i <listDoctrosMain.size(); i++)
                                            {

                                                DBSpeciality speciality = new DBSpeciality(listDoctrosMain.get(i).getSpeciality(),
                                                        listDoctrosMain.get(i).getSpeciality_id(),
                                                        "");
                                                speciality.save();//save speciality list to database

                                                DBDoctor dbDoctor = new DBDoctor(listDoctrosMain.get(i).getDoctor_name(),
                                                        listDoctrosMain.get(i).getDoctor_master_id(),
                                                        "",
                                                        listDoctrosMain.get(i).getArea_id(),
                                                        listDoctrosMain.get(i).getSpeciality_id(),
                                                        listDoctrosMain.get(i).getSpeciality(),
                                                        listDoctrosMain.get(i).getEnable_focus());
                                                dbDoctor.save();//Save Doctor list to database
                                            }
                                        }
                                        else
                                        {
                                            isSuccessApiCalled = false;
                                        }
                                    }
                                }).start();

                            }
                            else
                            {
                                isSuccessApiCalled = false;
                                AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            }

                            loadingCounts = loadingCounts + 1;
                            manageLoadingView(loadingCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DBDoctorResponse> call, Throwable t)
                    {
                        isSuccessApiCalled = false;
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                    }
                });

                Call<VariationResponse> variationCall = apiService.getVarioationProducts(sessionManager.getUserId(),sessionManager.getUserId(),"false");
                variationCall.enqueue(new Callback<VariationResponse>() {
                    @Override
                    public void onResponse(Call<VariationResponse> call, final Response<VariationResponse> response)
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try
                                        {
                                            List<VariationResponse.VariationsBean> listVariation = response.body().getVariations();

                                            if(listVariation.size()>0)
                                            {
                                                DBVariation.deleteAll(DBVariation.class);
                                                for (int i = 0; i < listVariation.size(); i++)
                                                {
                                                    VariationResponse.VariationsBean bean = listVariation.get(i);
                                                    DBVariation variation = new DBVariation(bean.getVariation_id(),
                                                            bean.getProduct_id(),
                                                            bean.getName(),
                                                            bean.getItem_code(),
                                                            bean.getReason(),
                                                            bean.getReason_code(),
                                                            bean.getStock(),
                                                            bean.isChecked(),
                                                            bean.getReason_id(),
                                                            bean.getProduct_type(),
                                                            bean.getItem_id_code());
                                                    variation.save();
                                                }
                                            }
                                            else
                                            {
                                                isSuccessApiCalled = false;
                                            }

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            }
                            else
                            {
                                isSuccessApiCalled = false;
                                AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            }


                            loadingCounts = loadingCounts + 1;
                            manageLoadingView(loadingCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VariationResponse> call, Throwable t)
                    {
                        isSuccessApiCalled = false;
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                    }
                });

                Call<VariationResponse> salevariationCall = apiService.getVariationSaleProducts(sessionManager.getUserId(),sessionManager.getUserId(),"false");
                salevariationCall.enqueue(new Callback<VariationResponse>() {
                    @Override
                    public void onResponse(Call<VariationResponse> call, final Response<VariationResponse> response)
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                Gson gson = new Gson();
                                sessionManager.setOnlySalesProduct(gson.toJson(response.body().getVariations()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VariationResponse> call, Throwable t)
                    {

                    }
                });

                Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
                empCall.enqueue(new Callback<StaffResponse>() {
                    @Override
                    public void onResponse(Call<StaffResponse> call, final Response<StaffResponse> response)
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DBStaff.deleteAll(DBStaff.class);
                                        ArrayList<StaffResponse.StaffBean>listEmpp = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();

                                        if(listEmpp.size()>0)
                                        {
                                            for (int i = 0; i < listEmpp.size(); i++)
                                            {
                                                DBStaff dbStaff= new DBStaff(listEmpp.get(i).getStaff_id(),
                                                        listEmpp.get(i).getName(),
                                                        listEmpp.get(i).getDesignation());
                                                dbStaff.save();
                                            }
                                        }
                                        else
                                        {
                                            isSuccessApiCalled = false;
                                        }
                                    }
                                }).start();
                            }
                            else
                            {
                                AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            }

                            loadingCounts = loadingCounts + 1;
                            manageLoadingView(loadingCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<StaffResponse> call, Throwable t)
                    {
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                    }
                });

                Call<ReportResponse> reportCall = apiService.getReportTypeList("",sessionManager.getUserId());
                reportCall.enqueue(new Callback<ReportResponse>() {
                    @Override
                    public void onResponse(Call<ReportResponse> call, final Response<ReportResponse> response)
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                new Thread(() -> {
                                    DBReports.deleteAll(DBReports.class);

                                    ArrayList<ReportResponse.ReportsBean>listReports = (ArrayList<ReportResponse.ReportsBean>) response.body().getReports();

                                    if(listReports.size()>0)
                                    {
                                        for (int i = 0; i < listReports.size(); i++)
                                        {
                                            DBReports reportBean = new DBReports(listReports.get(i).getReport_name(),
                                                    listReports.get(i).getReport_code(),
                                                    listReports.get(i).getReport_id());
                                            reportBean.save();
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }).start();
                            }
                            else
                            {
                                isSuccessApiCalled = false;
                                AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            }

                            loadingCounts = loadingCounts + 1;
                            manageLoadingView(loadingCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportResponse> call, Throwable t)
                    {
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                        isSuccessApiCalled = false;
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                    }
                });

                Call<ReasonResponse> reasonCall = apiService.getReasonList("",sessionManager.getUserId());
                reasonCall.enqueue(new Callback<ReasonResponse>() {
                    @Override
                    public void onResponse(Call<ReasonResponse> call, final Response<ReasonResponse> response)
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<ReasonResponse.ReasonsBean> listReason = (ArrayList<ReasonResponse.ReasonsBean>) response.body().getReasons();

                                        if(listReason.size()>0)
                                        {

                                            try
                                            {
                                                DBReason.deleteAll(DBReason.class);
                                                for (int i = 0; i < listReason.size(); i++)
                                                {
                                                    ReasonResponse.ReasonsBean bean = listReason.get(i);
                                                    DBReason reason = new DBReason(bean.getReason_id(),
                                                            bean.getReason(),
                                                            bean.getReason_code(),
                                                            bean.getComment(),
                                                            bean.getTimestamp());
                                                    reason.save();
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            isSuccessApiCalled = false;
                                        }
                                    }
                                }).start();
                            }
                            else
                            {
                                isSuccessApiCalled = false;
                                AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            }

                            loadingCounts = loadingCounts + 1;
                            manageLoadingView(loadingCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReasonResponse> call, Throwable t)
                    {
                        isSuccessApiCalled = false;
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                    }
                });

                Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(),sessionManager.getUserId(),"false");
                workWithCall.enqueue(new Callback<WorkWithResponse>() {
                    @Override
                    public void onResponse(Call<WorkWithResponse> call, final Response<WorkWithResponse> response)
                    {

                        if(response.body().getSuccess()==1)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<WorkWithResponse.StaffBean> listWorkWith = response.body().getStaff();

                                    if(listWorkWith.size()>0)
                                    {
                                        DBWorkWith.deleteAll(DBWorkWith.class);

                                        for (int i = 0; i < listWorkWith.size(); i++)
                                        {
                                            DBWorkWith dbWorkWith = new DBWorkWith(listWorkWith.get(i).getStaff_id(),
                                                    listWorkWith.get(i).getName(),
                                                    listWorkWith.get(i).getDesignation());
                                            dbWorkWith.save();
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }
                            }).start();
                        }
                        else
                        {
                            AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                            isSuccessApiCalled = false;
                        }
                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);
                    }

                    @Override
                    public void onFailure(Call<WorkWithResponse> call, Throwable t)
                    {
                        AppUtils.showToastInsideThread(activity,activity.getString(R.string.api_failed_message));
                        isSuccessApiCalled = false;
                        loadingCounts = loadingCounts - 1;
                        manageLoadingView(loadingCounts);
                    }
                });

                if(isSuccessApiCalled)
                {
                    sessionManager.setIsDataFetched(true);
                }
            }
            else
                {
                    showToast(activity,activity.getString(R.string.network_failed_message));
                }
        }
    }

    private void manageLoadingView(int counts)
    {
        Log.e("isSuccessful >> ", "manageLoadingView: ********************************************  "+isSuccessApiCalled );

        if(counts<6)
        {
            llLoading.setVisibility(View.VISIBLE);
        }
        else
        {
            llLoading.setVisibility(View.GONE);
        }
    }

    private void getFirstTimeDataFromServer()
    {
        if(sessionManager.isNetworkAvailable())
        {
            llLoading.setVisibility(View.VISIBLE);

            //New Added for planner flow
            try {
                Call<DailyPlannerResponse> getPlanner = apiService.getDailyPlannerForOffline(
                        sessionManager.getUserId(),
                        String.valueOf( System.currentTimeMillis()/1000),
                        sessionManager.getUserId()
                );
                getPlanner.enqueue(new Callback<DailyPlannerResponse>() {
                    @Override
                    public void onResponse(Call<DailyPlannerResponse> call, final Response<DailyPlannerResponse> response)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBPlanner.deleteAll(DBPlanner.class);
                                if(response.isSuccessful())
                                {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        List<DailyPlannerResponse.PlanBean> listPlan = response.body().getPlan();
                                        for (int i = 0; i < listPlan.size(); i++)
                                        {
                                            try {
                                                Gson gson = new Gson();
                                                String workWithString = gson.toJson(listPlan.get(i).getWork_array());
                                                DBPlanner planner = new DBPlanner(listPlan.get(i).getDoctor_id(),
                                                        listPlan.get(i).getDoctor(),
                                                        listPlan.get(i).getSpeciality_id(),
                                                        listPlan.get(i).getSpeciality(),
                                                        listPlan.get(i).getArea_id(),
                                                        listPlan.get(i).getArea(),
                                                        workWithString,
                                                        listPlan.get(i).getEnable_focus());
                                                planner.save();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Call<DailyPlannerResponse> call, Throwable t) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            loadingCounts = 0;

            Call<AreaResponse> areaCall = apiService.getAreaFromUserId("500", sessionManager.getUserId(),sessionManager.getUserId(),"");
            areaCall.enqueue(new Callback<AreaResponse>() {
                @Override
                public void onResponse(Call<AreaResponse> call, final Response<AreaResponse> response) {
                    try
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.body().getSuccess() == 1)
                                {
                                    DBArea.deleteAll(DBArea.class);
                                    ArrayList<AreaResponse.AreasBean> list = (ArrayList<AreaResponse.AreasBean>) response.body().getAreas();

                                    Gson gson = new Gson();
                                    String areaString = gson.toJson(response.body().getAreas());
                                    sessionManager.setArea(areaString);

                                    for (int i = 0; i < list.size(); i++)
                                    {
                                        if(list.get(i).getIs_tour_plan().equalsIgnoreCase("1")) {
                                            DBArea area = new DBArea(list.get(i).getArea_id(),list.get(i).getArea());
                                            area.save();
                                        }
                                        else
                                        {

                                        }
                                    }

                                } else
                                {
                                    DBArea.deleteAll(DBArea.class);
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AreaResponse> call, Throwable t) {
                    DBArea.deleteAll(DBArea.class);
                }
            });

            Call<DBDoctorResponse> drCall = apiService.getDoctors("","1","500",sessionManager.getUserId());
            drCall.enqueue(new Callback<DBDoctorResponse>() {
                @Override
                public void onResponse(Call<DBDoctorResponse> call, final Response<DBDoctorResponse> response)
                {
                    try
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<DBDoctor> tasks1 = DBDoctor.listAll(DBDoctor.class);
                                    Log.e("******before Size 1>>> ", "onResponse: "+tasks1.size() );

                                    //List OF all Doctors
                                    List<DBDoctorResponse.DoctorsBean> listDoctrosMain = response.body().getDoctors();

                                    Log.e("***************", "run: "+response.body().getDoctors().size() );

                                    if(listDoctrosMain.size()>0)
                                    {
                                        //Get Area and speciality from doctor list

                                        //DBArea.deleteAll(DBArea.class);
                                        DBSpeciality.deleteAll(DBSpeciality.class);
                                        DBDoctor.deleteAll(DBDoctor.class);

                                        List<DBDoctor> tasks2 = DBDoctor.listAll(DBDoctor.class);
                                        Log.e("******before Size 1>>> ", "onResponse: "+tasks2.size() );

                                        for (int i = 0; i <listDoctrosMain.size(); i++)
                                        {
                                    /*DBArea area = new DBArea(listDoctrosMain.get(i).getArea_id(),listDoctrosMain.get(i).getArea_name());
                                    area.save();*/

                                            DBSpeciality speciality = new DBSpeciality(listDoctrosMain.get(i).getSpeciality(),
                                                    listDoctrosMain.get(i).getSpeciality_id(),
                                                    "");
                                            speciality.save();//save speciality list to database

                                            DBDoctor dbDoctor = new DBDoctor(listDoctrosMain.get(i).getDoctor_name(),
                                                    listDoctrosMain.get(i).getDoctor_master_id(),
                                                    "",
                                                    listDoctrosMain.get(i).getArea_id(),
                                                    listDoctrosMain.get(i).getSpeciality_id(),
                                                    listDoctrosMain.get(i).getSpeciality(),
                                                    listDoctrosMain.get(i).getEnable_focus());
                                            dbDoctor.save();//Save Doctor list to database
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }
                            }).start();
                        }
                        else
                        {
                            isSuccessApiCalled = false;
                            AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        }

                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<DBDoctorResponse> call, Throwable t)
                {
                    isSuccessApiCalled = false;
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                }
            });

            Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
            empCall.enqueue(new Callback<StaffResponse>() {
                @Override
                public void onResponse(Call<StaffResponse> call, final Response<StaffResponse> response)
                {
                    try {

                        if(response.body().getSuccess()==1)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DBStaff.deleteAll(DBStaff.class);
                                    ArrayList<StaffResponse.StaffBean>listEmpp = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();

                                    if(listEmpp.size()>0)
                                    {
                                        for (int i = 0; i < listEmpp.size(); i++)
                                        {
                                            DBStaff dbStaff= new DBStaff(listEmpp.get(i).getStaff_id(),
                                                    listEmpp.get(i).getName(),
                                                    listEmpp.get(i).getDesignation());
                                            dbStaff.save();
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }
                            }).start();
                        }
                        else
                        {
                            AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        }
                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<StaffResponse> call, Throwable t)
                {
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                }
            });

            Call<VariationResponse> variationCall = apiService.getVarioationProducts(sessionManager.getUserId(),sessionManager.getUserId(),"false");
            variationCall.enqueue(new Callback<VariationResponse>() {
                @Override
                public void onResponse(Call<VariationResponse> call, final Response<VariationResponse> response)
                {
                    try {
                        if(response.body().getSuccess()==1)
                        {
                            try
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<VariationResponse.VariationsBean> listVariation = response.body().getVariations();

                                        if(listVariation.size()>0)
                                        {
                                            DBVariation.deleteAll(DBVariation.class);
                                            for (int i = 0; i < listVariation.size(); i++)
                                            {
                                                VariationResponse.VariationsBean bean = listVariation.get(i);
                                                DBVariation variation = new DBVariation(bean.getVariation_id(),
                                                        bean.getProduct_id(),
                                                        bean.getName(),
                                                        bean.getItem_code(),
                                                        bean.getReason(),
                                                        bean.getReason_code(),
                                                        bean.getStock(),
                                                        bean.isChecked(),
                                                        bean.getReason_id(),
                                                        bean.getProduct_type(),
                                                        bean.getItem_id_code());
                                                variation.save();
                                            }
                                        }
                                        else
                                        {
                                            isSuccessApiCalled = false;
                                        }
                                    }
                                }).start();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            isSuccessApiCalled = false;
                            AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        }
                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<VariationResponse> call, Throwable t)
                {
                    isSuccessApiCalled = false;
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                }
            });

            Call<VariationResponse> salevariationCall = apiService.getVariationSaleProducts(sessionManager.getUserId(),sessionManager.getUserId(),"false");
            salevariationCall.enqueue(new Callback<VariationResponse>() {
                @Override
                public void onResponse(Call<VariationResponse> call, final Response<VariationResponse> response)
                {
                    try {
                        if(response.body().getSuccess()==1)
                        {
                            Gson gson = new Gson();
                            sessionManager.setOnlySalesProduct(gson.toJson(response.body().getVariations()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<VariationResponse> call, Throwable t)
                {

                }
            });

            Call<ReportResponse> reportCall = apiService.getReportTypeList("",sessionManager.getUserId());
            reportCall.enqueue(new Callback<ReportResponse>() {
                @Override
                public void onResponse(Call<ReportResponse> call, final Response<ReportResponse> response)
                {
                    try
                    {
                        if(response.body().getSuccess()==1)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DBReports.deleteAll(DBReports.class);

                                    ArrayList<ReportResponse.ReportsBean>listReports = (ArrayList<ReportResponse.ReportsBean>) response.body().getReports();

                                    if(listReports.size()>0)
                                    {
                                        for (int i = 0; i < listReports.size(); i++)
                                        {
                                            DBReports reportBean = new DBReports(listReports.get(i).getReport_name(),
                                                    listReports.get(i).getReport_code(),
                                                    listReports.get(i).getReport_id());
                                            reportBean.save();
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }
                            }).start();
                        }
                        else
                        {
                            isSuccessApiCalled = false;
                            AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        }
                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ReportResponse> call, Throwable t)
                {
                    isSuccessApiCalled = false;
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                }

            });

            Call<ReasonResponse> reasonCall = apiService.getReasonList("",sessionManager.getUserId());
            reasonCall.enqueue(new Callback<ReasonResponse>() {
                @Override
                public void onResponse(Call<ReasonResponse> call, final Response<ReasonResponse> response)
                {
                    try
                    {
                        if(response.body().getSuccess()==1)
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<ReasonResponse.ReasonsBean> listReason = (ArrayList<ReasonResponse.ReasonsBean>) response.body().getReasons();

                                    if(listReason.size()>0)
                                    {
                                        try
                                        {
                                            DBReason.deleteAll(DBReason.class);
                                            for (int i = 0; i < listReason.size(); i++)
                                            {
                                                ReasonResponse.ReasonsBean bean = listReason.get(i);
                                                DBReason reason = new DBReason(bean.getReason_id(),
                                                        bean.getReason(),
                                                        bean.getReason_code(),
                                                        bean.getComment(),
                                                        bean.getTimestamp());
                                                reason.save();
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        isSuccessApiCalled = false;
                                    }
                                }
                            }).start();
                        }
                        else
                        {
                            isSuccessApiCalled = false;
                            AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        }
                        loadingCounts = loadingCounts + 1;
                        manageLoadingView(loadingCounts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ReasonResponse> call, Throwable t)
                {
                    isSuccessApiCalled = false;
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                }
            });

            Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(),sessionManager.getUserId(),"false");
            workWithCall.enqueue(new Callback<WorkWithResponse>() {
                @Override
                public void onResponse(Call<WorkWithResponse> call, final Response<WorkWithResponse> response)
                {

                    if(response.body().getSuccess()==1)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<WorkWithResponse.StaffBean> listWorkWith = response.body().getStaff();

                                if(listWorkWith.size()>0)
                                {
                                    DBWorkWith.deleteAll(DBWorkWith.class);

                                    for (int i = 0; i < listWorkWith.size(); i++)
                                    {
                                        DBWorkWith dbWorkWith = new DBWorkWith(listWorkWith.get(i).getStaff_id(),
                                                listWorkWith.get(i).getName(),
                                                listWorkWith.get(i).getDesignation());
                                        dbWorkWith.save();
                                    }
                                }
                                else
                                {
                                    isSuccessApiCalled = false;
                                }
                            }
                        }).start();
                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        isSuccessApiCalled = false;
                    }

                    loadingCounts = loadingCounts + 1;
                    manageLoadingView(loadingCounts);
                }

                @Override
                public void onFailure(Call<WorkWithResponse> call, Throwable t)
                {
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    isSuccessApiCalled = false;
                    loadingCounts = loadingCounts - 1;
                    manageLoadingView(loadingCounts);
                }
            });

            if(isSuccessApiCalled)
            {
                sessionManager.setIsDataFetched(true);
            }
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
    }

    private void getAllNotifGroupCounts()
    {
        if(sessionManager.isNetworkAvailable())
        {
            //llLoadingTransparent.setVisibility(View.VISIBLE);
            Call<GroupNotificationResponse> groupNotifCall = apiService.getAllGroupCounts(sessionManager.getUserId(),sessionManager.getUserId());
            groupNotifCall.enqueue(new Callback<GroupNotificationResponse>() {
                @Override
                public void onResponse(Call<GroupNotificationResponse> call, Response<GroupNotificationResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        if(response!=null)
                        {
                            if(response.body().getSuccess()==1)
                            {
                                countTP = response.body().getNotifications().getTourPlan();
                                countDCR = response.body().getNotifications().getDcr();
                                countGift = response.body().getNotifications().getGift();
                                countTA = response.body().getNotifications().getTravelling();
                                countNS = response.body().getNotifications().getNotSeen();
                                countsOther = response.body().getNotifications().getOther();

                                mainNotifCounts = response.body().getTotal();
                            }

                            updateCounts();
                            if(mainNotifCounts <= 0)
                            {
                                llNotification.setVisibility(View.GONE);
                            }
                            else
                            {
                                llNotification.setVisibility(View.VISIBLE);
                                tvNotification.setText(String.valueOf(mainNotifCounts));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GroupNotificationResponse> call, Throwable t)
                {

                }
            });
        }
        else
        {
            //setDashboardData();
        }
    }

    private void updateCounts()
    {
        CommonGetSet p5 = listDashBoard.get(0);
        p5.setNotifCounts(Integer.parseInt(countTP));
        listDashBoard.set(0,p5);

        CommonGetSet p6 = listDashBoard.get(1);
        p6.setNotifCounts(Integer.parseInt(countDCR));
        listDashBoard.set(1,p6);

        CommonGetSet p7 = listDashBoard.get(2);
        p7.setNotifCounts(Integer.parseInt(countGift));
        listDashBoard.set(2,p7);

        CommonGetSet p8 = listDashBoard.get(3);
        p8.setNotifCounts(Integer.parseInt(countTA));
        listDashBoard.set(3,p8);

        CommonGetSet p9 = listDashBoard.get(4);
        p9.setNotifCounts(Integer.parseInt(countNS));
        listDashBoard.set(4,p9);

        CommonGetSet p10 = listDashBoard.get(5);
        p10.setNotifCounts(Integer.parseInt(countsOther));
        listDashBoard.set(5,p10);

        dashBoardAdapter.notifyDataSetChanged();
    }



    private void setDashboardData()
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);

        listDashBoard = new ArrayList<>();

        CommonGetSet p5 = new CommonGetSet();
        p5.setId("1");
        p5.setText("Tour Plan\nLeave Report");
        p5.setDrawable(getResources().getDrawable(R.drawable.ic_tour_plan));
        p5.setNotifDisplay(false);
        p5.setNotifCounts(Integer.parseInt(countTP));
        listDashBoard.add(p5);

        CommonGetSet p6 = new CommonGetSet();
        p6.setId("2");
        p6.setText("Daily Call");
        if(Build.VERSION.SDK_INT >= 21)
        {
            p6.setDrawable(getResources().getDrawable(R.drawable.ic_daily_call, getTheme()));
        } else {
            p6.setDrawable(getResources().getDrawable(R.drawable.ic_daily_call));
        }
        p6.setNotifDisplay(false);
        p6.setNotifCounts(Integer.parseInt(countDCR));
        listDashBoard.add(p6);

        CommonGetSet p7 = new CommonGetSet();
        p7.setId("3");
        p7.setText("Gift");

        if(Build.VERSION.SDK_INT >= 21)
        {
            p7.setDrawable(getResources().getDrawable(R.drawable.ic_gift, getTheme()));
        } else {
            p7.setDrawable(getResources().getDrawable(R.drawable.ic_gift));
        }


        p7.setNotifDisplay(false);
        p7.setNotifCounts(Integer.parseInt(countGift));
        listDashBoard.add(p7);

        CommonGetSet p8 = new CommonGetSet();
        p8.setId("4");
        p8.setText("Travelling\nAllowance");
        if(Build.VERSION.SDK_INT >= 21)
        {
            p8.setDrawable(getResources().getDrawable(R.drawable.ic_travelling_allowance, getTheme()));
        } else {
            p8.setDrawable(getResources().getDrawable(R.drawable.ic_travelling_allowance));
        }
        p8.setNotifDisplay(false);
        p8.setNotifCounts(Integer.parseInt(countTA));
        listDashBoard.add(p8);

        CommonGetSet p9 = new CommonGetSet();
        p9.setId("5");
        p9.setText("Not Seen");
        if(Build.VERSION.SDK_INT >= 21)
        {
            p9.setDrawable(getResources().getDrawable(R.drawable.ic_not_seen, getTheme()));
        } else {
            p9.setDrawable(getResources().getDrawable(R.drawable.ic_not_seen));
        }
        p9.setNotifDisplay(false);
        p9.setNotifCounts(Integer.parseInt(countNS));
        listDashBoard.add(p9);

        CommonGetSet p10 = new CommonGetSet();
        p10.setId("6");
        p10.setText("Other");
        if(Build.VERSION.SDK_INT >= 21)
        {
            p10.setDrawable(getResources().getDrawable(R.drawable.ic_other, getTheme()));
        } else {
            p10.setDrawable(getResources().getDrawable(R.drawable.ic_other));
        }
        p10.setNotifCounts(Integer.parseInt(countsOther));
        p10.setNotifDisplay(false);
        listDashBoard.add(p10);

        dashBoardAdapter = new DashBoardAdapter(listDashBoard);
        rvDashboard.setAdapter(dashBoardAdapter);

        llLoadingTransparent.setVisibility(View.GONE);
    }

    private class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder>
    {
        ArrayList<CommonGetSet> listItems;
        DashBoardAdapter(ArrayList<CommonGetSet> list)
        {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_dashboard, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final CommonGetSet getSet = listItems.get(position);
            holder.tvTitle.setText(getSet.getText());
            holder.image.setImageDrawable(getSet.getDrawable());

            if (position == listItems.size() - 1 || position == listItems.size() - 2)
            {
                holder.viewBottom.setVisibility(View.GONE);
            }
            else
            {
                holder.viewBottom.setVisibility(View.VISIBLE);
            }

            if(getSet.getNotifCounts()>0)
            {
                holder.tvNotification.setVisibility(View.VISIBLE);
                holder.tvNotification.setText(String.valueOf(getSet.getNotifCounts()) + " Notification");
            }
            else
            {
                holder.tvNotification.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();


                        if(getSet.getId().equalsIgnoreCase("2"))
                        {
                            Intent intent = new Intent(activity,ActivityDailyCallReport.class);
                            startActivity(intent);
                            startActivityAnimation(activity);
                        }
                        else if(getSet.getId().equalsIgnoreCase("1"))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                Intent intent = new Intent(activity,TourPlanActivity.class);
                                startActivity(intent);
                                startActivityAnimation(activity);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                        else if(getSet.getId().equalsIgnoreCase("3"))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                Intent intent = new Intent(activity,GiftActivity.class);
                                startActivity(intent);
                                startActivityAnimation(activity);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                        else if(getSet.getId().equalsIgnoreCase("4"))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                Intent intent = new Intent(activity,TravellingAllowanceActivity.class);
                                startActivity(intent);
                                startActivityAnimation(activity);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                        else if(getSet.getId().equalsIgnoreCase("5"))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                Intent intent = new Intent(activity,NotSeenActivity.class);
                                startActivity(intent);
                                startActivityAnimation(activity);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                        else if(getSet.getId().equalsIgnoreCase("6"))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                Intent intent = new Intent(activity,OtherActivity.class);
                                startActivity(intent);
                                startActivityAnimation(activity);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.tvNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        Intent intent = new Intent(activity,NotificationActivity.class);
                        if(getSet.getId().equalsIgnoreCase("2"))//dcr
                        {
                            intent.putExtra("type",ApiClient.NOTIF_DCR);
                        }
                        else if(getSet.getId().equalsIgnoreCase("1"))//tp
                        {
                            intent.putExtra("type",ApiClient.NOTIF_TP);
                        }
                        else if(getSet.getId().equalsIgnoreCase("3"))//gift
                        {
                            intent.putExtra("type",ApiClient.NOTIF_GIFT);
                        }
                        else if(getSet.getId().equalsIgnoreCase("4"))//ta
                        {
                            intent.putExtra("type",ApiClient.NOTIF_TA);
                        }
                        else if(getSet.getId().equalsIgnoreCase("5"))//not seen
                        {
                            intent.putExtra("type",ApiClient.NOTIF_NS);
                        }
                        else if(getSet.getId().equalsIgnoreCase("6"))//other
                        {
                            intent.putExtra("type",ApiClient.NOTIF_OTHER);
                        }
                        startActivity(intent);
                        startActivityAnimation(activity);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView tvTitle,tvNotification;
            ImageView image;
            View viewBottom;
            private LinearLayout llTop;
            ViewHolder(View convertView)
            {
                super(convertView);
                tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                tvNotification = (TextView) convertView.findViewById(R.id.tvNotification);
                image = (ImageView) convertView.findViewById(R.id.image);
                viewBottom = convertView.findViewById(R.id.viewBottom);
                llTop = (LinearLayout) itemView.findViewById(R.id.llTop);

                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                height = height - navigationBarHeight;

                int newHeight = height - (toolbarHeight - getStatusBarHeight());

                int finalHeight = newHeight / 3;

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,finalHeight); // or set height to any fixed value you want

                llTop.setLayoutParams(lp);
            }
        }

    }

    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private void showDivisionDialog()
    {
        try
        {

            final Dialog dialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_select_division, null);
            dialog.setContentView(sheetView);

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView,activity);
                }
            });

            final RecyclerView rvDivision = dialog.findViewById(R.id.rvDivision);
            rvDivision.setLayoutManager(new LinearLayoutManager(activity));
            final TextView tvSubmit = dialog.findViewById(R.id.tvSubmit);
            final TextView tvCancel = dialog.findViewById(R.id.tvCancel);

            List<LoginResponse.AdminBean.TerritoryCodeBean> listTerritoryCode = new ArrayList<>();

            try
            {
                JSONArray divArray = new JSONArray(sessionManager.getDivisions());
                if(divArray.length()>0)
                {
                    for (int i = 0; i < divArray.length(); i++)
                    {
                        JSONObject obj = (JSONObject) divArray.get(i);
                        LoginResponse.AdminBean.TerritoryCodeBean bean = new LoginResponse.AdminBean.TerritoryCodeBean();
                        bean.setCode(obj.getString("code"));
                        bean.setDesignation(obj.getString("designation"));
                        bean.setEmployee_id(obj.getString("employee_id"));
                        bean.setUser_type(obj.getString("user_type"));
                        bean.setIs_lock_stk(obj.getString("is_lock_stk"));
                        bean.setIs_stk_done(obj.getString("is_stk_done"));
                        bean.setDivision(obj.getString("division"));
                        bean.setIs_day_end(obj.getBoolean("is_day_end"));
                        bean.setIs_sample_entry(obj.getString("is_sample_entry"));
                        bean.setIs_sample_report(obj.getString("is_sample_report"));
                        bean.setIs_sales_entry(obj.getString("is_sales_entry"));
                        bean.setIs_sales_report(obj.getString("is_sales_report"));

                        listTerritoryCode.add(bean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DivisionAdapter divisionAdapter = new DivisionAdapter(listTerritoryCode);
            rvDivision.setAdapter(divisionAdapter);

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if(isDivisionSelected)
                    {

                        sessionManager.setUserId(selectedUserId);
                        sessionManager.setTerritoryCode(selectedTerritoryId);
                        sessionManager.setUserType(selectedUserType);

                        if(is_stk_done.equals("1"))
                        {
                            sessionManager.setIsSTKDone(true);
                        }
                        else
                        {
                            sessionManager.setIsSTKDone(false);
                        }

                        AppUtils.updateDeviceTokenForFCM(activity,apiService);

                        dialog.dismiss();
                        dialog.cancel();
                        if(sessionManager.isNetworkAvailable())
                        {
                            getFirstTimeDataFromServer();
                        }
                        else
                        {
                            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity,"Please select Territory");
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

    private class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.ViewHolder>
    {
        List<LoginResponse.AdminBean.TerritoryCodeBean> listItems;

        DivisionAdapter(List<LoginResponse.AdminBean.TerritoryCodeBean> listItems)
        {

            this.listItems = listItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_division, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final LoginResponse.AdminBean.TerritoryCodeBean bean = listItems.get(position);

            if(position == getItemCount()-1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            holder.rbDivision.setChecked(bean.isSelected());
            holder.rbDivision.setText(bean.getCode()+" "+bean.getDivision());
            holder.rbDivision.setTypeface(AppUtils.getTypefaceRegular(activity));
            holder.rbDivision.setOnClickListener(view -> {
                isDivisionSelected = true;
                selectedTerritoryId = bean.getCode();
                selectedUserId = bean.getEmployee_id();
                selectedUserType = bean.getUser_type();
                is_stk_done = bean.getIs_stk_done();
                isDayEnd = bean.getIs_day_end();

                sessionManager.setDayEnd(String.valueOf(bean.getIs_day_end()));
                sessionManager.setSalesPermission(bean.getIs_sales_entry());
                sessionManager.setSalesReportPermission(bean.getIs_sales_report());
                sessionManager.setSamplePermission(bean.getIs_sample_entry());
                sessionManager.setSampleReportPermission(bean.getIs_sample_report());
                sessionManager.setCanSTK(bean.getIs_lock_stk());
                sessionManager.setOffDayOrAdminDay(bean.getIs_offday());

                setSelectedRadioButton(position,bean,holder);
            });
        }

        private void setSelectedRadioButton(int position, LoginResponse.AdminBean.TerritoryCodeBean getSet,ViewHolder holder)
        {
            listItems.get(0).setSelected(false);
            listItems.get(position).setSelected(true);
            holder.rbDivision.setChecked(true);
            for (int i = 0; i < listItems.size(); i++)
            {
                if(i==position)
                {
                    listItems.get(i).setSelected(true);
                }
                else
                {
                    listItems.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private RadioButton rbDivision;
            private View viewLine;
            public ViewHolder(View itemView)
            {
                super(itemView);
                rbDivision = (RadioButton) itemView.findViewById(R.id.rbDivision);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }


    @Override
    protected void onDestroy()
    {
        DashboardActivity.isAppRunning = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        activity.finish();
        Intent intent = new Intent(activity, DashboardActivity.class);
        startActivity(intent);
        finishActivityAnimation(activity);
    }
}
