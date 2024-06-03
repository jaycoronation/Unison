package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.OneTimeSession;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AdminResponse;
import com.unisonpharmaceuticals.model.CirclularNotifResponse;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.DashboardResponse;
import com.unisonpharmaceuticals.model.LoginResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.WorkWithResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;
    private OneTimeSession oneTimeSession;

    @BindView(R.id.fabGo)
    ImageView fabGo;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDate) TextView tvDate;

    @BindView(R.id.frameMain)FrameLayout frameMain;

    /*Cons*/
    @BindView(R.id.pbConsultant) ProgressBar pbConsultant;
    @BindView(R.id.tvCons) TextView tvCons;
    @BindView(R.id.tvConsPer) TextView tvConsPer;
    /*Imp*/
    @BindView(R.id.pbImportant) ProgressBar pbImportant;
    @BindView(R.id.tvImp) TextView tvImp;
    @BindView(R.id.tvImpRes) TextView tvImpRes;
    /*pot*/
    @BindView(R.id.pbPotential) ProgressBar pbPotential;
    @BindView(R.id.tvPot) TextView tvPot;
    @BindView(R.id.tvPotPer) TextView tvPotPer;
    /*gp*/
    @BindView(R.id.pbGP) ProgressBar pbGP;
    @BindView(R.id.tvPg) TextView tvPg;
    @BindView(R.id.tvPgRes) TextView tvPgRes;
    /*Total*/
    @BindView(R.id.tvTotalCoverage)TextView tvTotalCoverage;
    @BindView(R.id.tvAvgCoverage)TextView tvAvgCoverage;

    @BindView(R.id.inputEmployee)
    TextInputLayout inputEmployee;
    @BindView(R.id.edtEmployee)EditText edtEmployee;

    @BindView(R.id.tvDays)TextView tvDays;
    @BindView(R.id.tvCallCoverage)TextView tvCallCoverage;

    private boolean isDivisionSelected = false;

    private Dialog listDialog;
    private String workWithString = "";

    private ArrayList<WorkWithResponse.StaffBean> listWorkWith = new ArrayList<>();
    private ArrayList<WorkWithResponse.StaffBean> listWorkWithSearch = new ArrayList<>();

    private ApiInterface apiService;

    public static boolean isAppRunning = false;

    ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    public static Handler handler;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        isAppRunning = true;
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        oneTimeSession = new OneTimeSession(activity);

        //For check is logout called when daily service executed.
        if(sessionManager.isLogoutOnDestroy())
        {
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
        }


        Log.e("userType >>>> ", "onCreate: "+sessionManager.getUSerType() );

        if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
        {
            inputEmployee.setVisibility(View.VISIBLE);
        }
        else
        {
            inputEmployee.setVisibility(View.GONE);
        }

        if(sessionManager.getTerritoryCode().equalsIgnoreCase(""))
        {
            showDivisionDialog();
        }
        else
        {
            getAllBasicDashBoardData();
        }

        basicProcesses();

        if(SplashActivity.isVersionMismatch)
        {
            AppUtils.showVersionMismatchDialog(activity,SplashActivity.isForcefulUpdate);
        }

        handler = new Handler(msg -> {
            if(msg.what==111)
            {
                if(sessionManager.isNetworkAvailable())
                {
                    getAllBasicDashBoardData();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
            }
            return false;
        });

    }

    @Override
    public void initViews()
    {
        ivBack.setImageResource(R.drawable.ic_logout);
    }

    @Override
    public void viewClick()
    {
        fabGo.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD)
            {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
            startActivityAnimation(activity);
        });
        ivBack.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            sessionManager.logoutUser();
        });
        edtEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showListDialog("EMPLOYEE");
            }
        });
    }

    @Override
    public void getDataFromServer()
    {
        if(sessionManager.isNetworkAvailable())
        {
            frameMain.setVisibility(View.GONE);
            llLoading.setVisibility(View.VISIBLE);
            Call<CirclularNotifResponse> circularCall = apiService.getCircularNotification(sessionManager.getUserId(),sessionManager.getUserId());
            circularCall.enqueue(new Callback<CirclularNotifResponse>() {
                @Override
                public void onResponse(Call<CirclularNotifResponse> call, Response<CirclularNotifResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body().getSuccess()==1)
                        {
                            if(!response.body().getCircular_notice().isIs_read())
                            {
                                showCirclularNotice(response.body().getCircular_notice().getTitle(),
                                        response.body().getCircular_notice().getMessage(),
                                        response.body().getCircular_notice().getTime(),
                                        response.body().getCircular_notice().getNotification_id());
                            }
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedToast(activity);
                    }
                    llLoading.setVisibility(View.GONE);
                    frameMain.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<CirclularNotifResponse> call, Throwable t) {
                    llLoading.setVisibility(View.GONE);
                    frameMain.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedToast(activity);
                }
            });
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
    }

    private void getAllBasicDashBoardData()
    {
        if(sessionManager.isNetworkAvailable())
        {
            llLoading.setVisibility(View.VISIBLE);
            getDashBoardData(sessionManager.getUserId());
            Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(),sessionManager.getUserId(),"true");
            workWithCall.enqueue(new Callback<WorkWithResponse>()
            {
                @Override
                public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                    if(response.isSuccessful())
                    {
                        try
                        {
                            if (response.body().getSuccess() == 1) {
                                listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                            } else {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedToast(activity);
                    }
                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    t.printStackTrace();
                    llLoading.setVisibility(View.GONE);
                }
            });

            llLoading.setVisibility(View.VISIBLE);
            listEmployee = new ArrayList<>();
            Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
            empCall.enqueue(new Callback<StaffResponse>() {
                @Override
                public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        try {
                            if(response.body().getSuccess()==1)
                            {
                                listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();
                            }
                            else
                            {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedToast(activity);
                    }

                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<StaffResponse> call, Throwable t)
                {
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    t.printStackTrace();
                    llLoading.setVisibility(View.GONE);
                }
            });


            getAdminData();
        }
        else
        {
            tvTitle.setText(sessionManager.getFirstName() +" "+sessionManager.getLastName());

            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
    }

    private void getAdminData()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AdminResponse> adminCall = apiService.getAdminDetails(sessionManager.getUserId(),sessionManager.getUserId());
        adminCall.enqueue(new Callback<AdminResponse>() {
            @Override
            public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        sessionManager.setFieldWork(response.body().getAdmin().getField_work());

                        if(sessionManager.getFieldWork()==0)
                        {
                            showDailyActionDialog();
                        }
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdminResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getDashBoardData(String userID)
    {
        Call<DashboardResponse> dashboardCall = apiService.getDashboardData(userID, AppUtils.currentDateForApi(),sessionManager.getUserId());
        dashboardCall.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response)
            {
                if(response.isSuccessful())
                {
                    try
                    {
                        if(response.body().getSuccess()==1)
                        {
                            tvTitle.setText(response.body().getReport().getStaff_summary().getName());
                            tvDate.setText("As On "+response.body().getReport().getStaff_summary().getDate());

                            pbConsultant.setMax(response.body().getReport().getCons().getTotal());
                            pbConsultant.setProgress(response.body().getReport().getCons().getCovered());
                            tvCons.setText(response.body().getReport().getCons().getCovered() +" / "+response.body().getReport().getCons().getTotal());
                            tvConsPer.setText(response.body().getReport().getCons().getAvg()+" %");

                            pbImportant.setMax(response.body().getReport().getImp().getTotal());
                            pbImportant.setProgress(response.body().getReport().getImp().getCovered());
                            tvImp.setText(response.body().getReport().getImp().getCovered() +" / "+response.body().getReport().getImp().getTotal());
                            tvImpRes.setText(response.body().getReport().getImp().getAvg()+" %");

                            pbPotential.setMax(response.body().getReport().getPot().getTotal());
                            pbPotential.setProgress(response.body().getReport().getPot().getCovered());
                            tvPot.setText(response.body().getReport().getPot().getCovered() +" / "+response.body().getReport().getPot().getTotal());
                            tvPotPer.setText(response.body().getReport().getPot().getAvg()+" %");

                            pbGP.setMax(response.body().getReport().getGp().getTotal());
                            pbGP.setProgress(response.body().getReport().getGp().getCovered());
                            tvPg.setText(response.body().getReport().getGp().getCovered() +" / "+response.body().getReport().getGp().getTotal());
                            tvPgRes.setText(response.body().getReport().getGp().getAvg()+" %");

                            tvAvgCoverage.setText(String.valueOf(response.body().getReport().getTotal().getCall_average())+" %");
                            tvTotalCoverage.setText(response.body().getReport().getTotal().getCall_coverage() +" / "+response.body().getReport().getTotal().getTotal_coverage());

                            tvDays.setText(String.valueOf(response.body().getReport().getStaff_summary().getWork_day())+" / "+String.valueOf(response.body().getReport().getStaff_summary().getTotal_days()));

                            tvCallCoverage.setText(String.valueOf(response.body().getReport().getStaff_summary().getDay_avg())+" %");

                        }
                        else
                        {
                            showToast(activity,activity.getString(R.string.network_failed_message));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t)
            {
                t.printStackTrace();
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void showDailyActionDialog()
    {
        try
        {

            final Dialog dialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);



            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_action_pending_manager, null);
            dialog.setContentView(sheetView);

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(edtEmployee,activity);
                }
            });

            final RadioGroup rgAction = (RadioGroup) dialog.findViewById(R.id.rgAction);
            final RadioButton rbIndividual = (RadioButton) dialog.findViewById(R.id.rbIndividual);
            rbIndividual.setTypeface(AppUtils.getTypefaceRegular(activity));
            final RadioButton rbJoint = (RadioButton) dialog.findViewById(R.id.rbJoint);
            rbJoint.setTypeface(AppUtils.getTypefaceRegular(activity));
            final RadioButton rbLeave = (RadioButton) dialog.findViewById(R.id.rbLeave);
            rbLeave.setTypeface(AppUtils.getTypefaceRegular(activity));
            final RadioButton rbLogout = (RadioButton) dialog.findViewById(R.id.rbLogout);
            rbLogout.setTypeface(AppUtils.getTypefaceRegular(activity));
            final RadioButton rbHOMeeting = (RadioButton) dialog.findViewById(R.id.rbHOMeeting);
            rbHOMeeting.setTypeface(AppUtils.getTypefaceRegular(activity));
            final RadioButton rbAdminWork = (RadioButton) dialog.findViewById(R.id.rbAdminWork);
            rbAdminWork.setTypeface(AppUtils.getTypefaceRegular(activity));

            final RadioButton rbOffDay = (RadioButton) dialog.findViewById(R.id.rbOffDay);
            rbOffDay.setTypeface(AppUtils.getTypefaceRegular(activity));

            final RadioButton rbAdminDay = (RadioButton) dialog.findViewById(R.id.rbAdminDay);
            rbAdminDay.setTypeface(AppUtils.getTypefaceRegular(activity));

            rbHOMeeting.setVisibility(View.VISIBLE);
            rbOffDay.setVisibility(View.VISIBLE);

            //New Added 27 Dec 18 after meeting
            if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
            {
                rbAdminDay.setVisibility(View.VISIBLE);
            }
            else
            {
                rbAdminDay.setVisibility(View.GONE);
            }

           /* rbAdminDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        sessionManager.setOffDayOrAdminDay("adminDay");
                    }
                    else
                    {
                        sessionManager.setOffDayOrAdminDay("");
                    }
                }
            });

            rbOffDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        sessionManager.setOffDayOrAdminDay("offDay");
                    }
                    else
                    {
                        sessionManager.setOffDayOrAdminDay("");
                    }
                }
            });*/

            rbJoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        showListDialog("work with");
                    }
                }
            });
            rbLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        rbLeave.setSelected(false);
                        rbIndividual.setSelected(true);

                        Intent intent = new Intent(activity,ActivityDailyLeave.class);
                        startActivity(intent);
                        startActivityAnimation(activity);
                    }
                }
            });
            rbLogout.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked)
                {
                    sessionManager.logoutUser();
                }
            });

            final LinearLayout llSubmit = (LinearLayout) dialog.findViewById(R.id.llSubmit);
            llSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        //UnComment Below for submit work type
                        String type = "";
                        if(rbIndividual.isChecked())
                        {
                            type = "individual_work";
                            sessionManager.setOffDayOrAdminDay("");
                        }
                        else if(rbJoint.isChecked())
                        {
                            type = "joint_work";
                            if(areaAdapter!=null)
                            {
                                workWithString = areaAdapter.getSelectedWorkWitIds();
                                if(workWithString.length()==0)
                                {
                                    showToast(activity,"Please select work with options.");
                                    return;
                                }
                            }
                            sessionManager.setOffDayOrAdminDay("");
                        }
                        else if(rbLeave.isChecked())
                        {
                            type = "leave";
                            sessionManager.setOffDayOrAdminDay("");
                        }
                        else if(rbHOMeeting.isChecked())
                        {
                            type = "HO_meeting";
                            sessionManager.setOffDayOrAdminDay("");
                        }
                        else if(rbOffDay.isChecked())
                        {
                            type = "off_day";
                            sessionManager.setOffDayOrAdminDay("1");
                        }
                        else if(rbAdminDay.isChecked())
                        {
                            type = "admin_day";
                            sessionManager.setOffDayOrAdminDay("");
                        }
                        else
                        {
                            sessionManager.setOffDayOrAdminDay("");
                        }


                        /*JSONArray wwArray = new JSONArray();
                        if(listWorkWith!=null&&listWorkWith.size()>0)
                        {
                            for (int i = 0; i < listWorkWith.size(); i++)
                            {
                                if(listWorkWith.get(i).isSelected())
                                {
                                    wwArray.put(listWorkWith.get(i).getStaff_id());
                                }
                            }
                        }*/
                        StringBuilder commaSepValueBuilder = new StringBuilder();
                        if(listWorkWith!=null && listWorkWith.size()>0)
                        {
                            for ( int i = 0; i< listWorkWith.size(); i++)
                            {
                                if(listWorkWith.get(i).isSelected())
                                {
                                    commaSepValueBuilder.append(listWorkWith.get(i).getStaff_id());
                                    if ( i != listWorkWith.size()-1){
                                        commaSepValueBuilder.append(",");
                                    }
                                }
                            }
                        }




                        /*dialog.dismiss();
                        dialog.cancel();*/

                        Log.e("Json Array >> ", "onClick: " + commaSepValueBuilder.toString());

                        subMitWorkType(type,commaSepValueBuilder.toString(),dialog);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showCirclularNotice(String title, String message, String timeAgo, final String notifId)
    {
        try
        {

            final Dialog dialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_circular_notification, null);
            dialog.setContentView(sheetView);

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(edtEmployee,activity);
                }
            });

            TextView tvTitle,tvMessage,tvTimeAgo;
            tvTitle = (TextView) sheetView.findViewById(R.id.tvTitle);
            tvMessage = (TextView) sheetView.findViewById(R.id.tvMessage);
            tvTimeAgo = (TextView) sheetView.findViewById(R.id.tvTimeAgo);

            tvTimeAgo.setText(timeAgo);
            tvTitle.setText(title);
            tvMessage.setText(message);

            sheetView.findViewById(R.id.llSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        updateReadStatus(notifId,dialog);
                    }
                    else
                    {
                        showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
            });


            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateReadStatus(final String notifId, final Dialog dialog)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Call<CommonResponse> notifResponse = apiService.readNotifStatusUpdate(sessionManager.getUserId(), notifId,sessionManager.getUserId());
                notifResponse.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        if(response.isSuccessful())
                        {
                            if(response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                dialog.cancel();

                                getDataFromServer();
                            }
                            else
                            {
                                showToast(activity,response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        showToast(activity,"Something went wrong!");
                    }
                });
            }
        }).start();
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

                        bean.setIs_day_end(obj.getBoolean("is_day_end"));
                        bean.setIs_sample_entry(obj.getString("is_sample_entry"));
                        bean.setIs_sample_report(obj.getString("is_sample_report"));
                        bean.setIs_sales_entry(obj.getString("is_sales_entry"));
                        bean.setIs_sales_report(obj.getString("is_sales_report"));
                        bean.setDivision(obj.getString("division"));

                        listTerritoryCode.add(bean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DivisionAdapter divisionAdapter = new DivisionAdapter(listTerritoryCode);
            rvDivision.setAdapter(divisionAdapter);

            Log.e("Terr Size >> ", "showDivisionDialog: " + listTerritoryCode.size() );


            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if(isDivisionSelected)
                    {
                        dialog.dismiss();
                        dialog.cancel();
                        //for Leave
                        if(sessionManager.isOnLeave()==1)
                        {
                            Intent intent = new Intent(activity,PendingLeaveActivity.class);
                            intent.putExtra("isFor","dashboard");
                            startActivity(intent);
                        }
                        else
                        {
                            if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                            {
                                inputEmployee.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                inputEmployee.setVisibility(View.GONE);
                            }
                            getAllBasicDashBoardData();
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
    
    AreaAdapter areaAdapter;
    public void showListDialog(final String isFor)
    {
        /*listDialog = new Dialog(activity);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_list, null);

        listDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setContentView(promptView);*/

        listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);

        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AppUtils.hideKeyboard(sheetView,activity);
            }
        });


        LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select "+isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        if(isFor.equalsIgnoreCase("work with"))
        {
            tvDone.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDone.setVisibility(View.GONE);
        }

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        areaAdapter = new AreaAdapter(listDialog, isFor,false,"");
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(areaAdapter);

        tvDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isFor.equalsIgnoreCase("work with")&&areaAdapter !=null)
                {
                    {
                        workWithString = areaAdapter.getSelectedWorkWitIds();
                        if(workWithString.length()==0)
                        {
                            showToast(activity,"Please select at least one option.");
                        }
                        else
                        {
                            listDialog.dismiss();
                            listDialog.cancel();
                        }
                    }
                }
                else
                {
                    listDialog.dismiss();
                    listDialog.cancel();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);

        edtSearchDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                int textlength = edtSearchDialog.getText().length();
                if(isFor.equals("work with"))
                {
                    listWorkWithSearch.clear();
                    for (int i = 0; i < listWorkWith.size(); i++)
                    {
                        if (textlength <= listWorkWith.get(i).getName().length())
                        {
                            if (listWorkWith.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listWorkWithSearch.add(listWorkWith.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals("EMPLOYEE"))
                {
                    listEmployeeSearch.clear();
                    for (int i = 0; i < listEmployee.size(); i++)
                    {
                        if (textlength <= listEmployee.get(i).getName().length())
                        {
                            if (listEmployee.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listEmployee.get(i).getStaff_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listEmployeeSearch.add(listEmployee.get(i));
                            }
                        }
                    }
                }

                AppendListForArea(listDialog,isFor,true,rvListDialog);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listDialog.show();
    }

    private void AppendListForArea(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea)
    {
        areaAdapter = new AreaAdapter(dialog,isFor,true,"");
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    private class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";

        AreaAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }

        @Override
        public AreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new AreaAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AreaAdapter.ViewHolder holder, final int position)
        {

            if(position == getItemCount()-1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            if(isFor.equalsIgnoreCase("work with"))
            {
                holder.cb.setVisibility(View.VISIBLE);

                final WorkWithResponse.StaffBean getSet;
                if(isForSearch)
                {
                    getSet = listWorkWithSearch.get(position);
                }
                else
                {
                    getSet = listWorkWith.get(position);
                }

                holder.cb.setChecked(getSet.isSelected());

                holder.tvValue.setVisibility(View.GONE);

                holder.cb.setText(getSet.getName() + " ("+getSet.getDesignation()+")");
                holder.cb.setTypeface(getTypefaceRegular(activity));

                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        getSet.setSelected(isChecked);
                        if(isForSearch)
                        {
                            listWorkWithSearch.set(position,getSet);
                        }
                        else
                        {
                            listWorkWith.set(position,getSet);
                        }

                    }
                });

            }
            else if(isFor.equalsIgnoreCase("EMPLOYEE"))
            {
                holder.cb.setVisibility(View.GONE);

                final StaffResponse.StaffBean getSet;
                if(isForSearch)
                {
                    getSet = listEmployeeSearch.get(position);
                }
                else
                {
                    getSet = listEmployee.get(position);
                }

                holder.tvValue.setVisibility(View.VISIBLE);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        edtEmployee.setText(getSet.getName());
                        dialog.dismiss();
                        dialog.cancel();
                        getDashBoardData(getSet.getStaff_id());
                    }
                });
            }
        }

        public String getSelectedWorkWitIds()
        {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listWorkWith.size(); i++)
            {
                if(listWorkWith.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str.append(listWorkWith.get(i).getStaff_id());
                    }
                    else
                    {
                        str.append(","+listWorkWith.get(i).getStaff_id());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedWorkWithName()
        {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listWorkWith.size(); i++)
            {
                if(listWorkWith.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str.append(listWorkWith.get(i).getName());
                    }
                    else
                    {
                        str.append(","+listWorkWith.get(i).getName());
                    }
                }
            }

            return String.valueOf(str);
        }

        @Override
        public int getItemCount()
        {
            if(isFor.equalsIgnoreCase("work with"))
            {
                if(isForSearch)
                {
                    return listWorkWithSearch.size();
                }
                else
                {
                    return listWorkWith.size();
                }
            }
            if(isFor.equalsIgnoreCase("EMPLOYEE"))
            {
                if(isForSearch)
                {
                    return listEmployeeSearch.size();
                }
                else
                {
                    return listEmployee.size();
                }
            }
            else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvValue,tvId;
            private CheckBox cb;
            private View viewLine;
            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                viewLine = itemView.findViewById(R.id.viewLine);
                cb = (CheckBox) itemView.findViewById(R.id.cb);
                cb.setTypeface(AppUtils.getTypefaceRegular(activity));
            }
        }
    }

    private void subMitWorkType(final String workType,final String workWith,final Dialog dialog)
    {
        if(sessionManager.isNetworkAvailable())
        {
            Call<CommonResponse> submitLeave = apiService.submitWorkType(sessionManager.getUserId(),
                    workType,
                    "",
                    "",
                    "",
                    "",
                    AppUtils.removeLastComma(workWith),
                    sessionManager.getUserId());
            submitLeave.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body().getSuccess()==1)
                        {
                            sessionManager.setFieldWork(1);
                            AppUtils.showToast(activity,response.body().getMessage());
                            dialog.dismiss();
                            dialog.cancel();
                    /*activity.finish();
                    startActivity(new Intent(activity,MainActivity.class));
                    AppUtils.startActivityAnimation(activity);*/
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
                            AppUtils.showToast(activity,response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t)
                {
                    t.printStackTrace();
                    llLoading.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
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
        public DivisionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_division, parent, false);
            return new DivisionAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DivisionAdapter.ViewHolder holder, final int position)
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
            holder.rbDivision.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    isDivisionSelected = true;
                    sessionManager.setUserId(bean.getEmployee_id());
                    sessionManager.setTerritoryCode(bean.getCode());
                    sessionManager.setUserType(bean.getUser_type());
                    sessionManager.setDayEnd(String.valueOf(bean.getIs_day_end()));

                    sessionManager.setSalesPermission(bean.getIs_sales_entry());
                    sessionManager.setSalesReportPermission(bean.getIs_sales_report());
                    sessionManager.setSamplePermission(bean.getIs_sample_entry());
                    sessionManager.setSampleReportPermission(bean.getIs_sample_report());
                    sessionManager.setCanSTK(bean.getIs_lock_stk());
                    sessionManager.setOffDayOrAdminDay(bean.getIs_offday());

                    if(bean.getIs_stk_done().equals("1"))
                    {
                        sessionManager.setIsSTKDone(true);
                    }
                    else
                    {
                        sessionManager.setIsSTKDone(false);
                    }

                    setSelectedRadioButton(position,bean,holder);
                    AppUtils.updateDeviceTokenForFCM(activity,apiService);
                }
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
        isAppRunning = false;
        Log.d("Destroy: is App Running", isAppRunning+"");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView tvHeader,tvDescription,tvConfirm;
            tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
            tvDescription = (TextView) dialog.findViewById(R.id.tvDescription);
            tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
            tvHeader.setText("Exit");
            tvDescription.setText("Are you sure you want to exit from Unison App?");
            tvConfirm.setText("EXIT");

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    dialog.cancel();
                    activity.finishAffinity();
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
        return false;
    }
}
