package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.ViewSampleActivity;
import com.unisonpharmaceuticals.activity.WebViewActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.MonthResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.TAResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentTANew extends Fragment implements View.OnClickListener {
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.edtMonth)
    EditText edtMonth;
    @BindView(R.id.edtYear)
    EditText edtYear;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvGenerateReport)
    TextView tvGenerateReport;
    @BindView(R.id.tvDownload)
    TextView tvDownload;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.rvPlan)
    RecyclerView rvPlan;

    @BindView(R.id.llSummary)
    LinearLayout llSummary;

    @BindView(R.id.tvEmployee)
    TextView tvEmployee;
    @BindView(R.id.tvHQ)
    TextView tvHQ;
    @BindView(R.id.tvDivision)
    TextView tvDivision;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.tvMonth)
    TextView tvMonth;

    @BindView(R.id.tvTotalFare)
    TextView tvTotalFare;
    @BindView(R.id.tvTotalHQ)
    TextView tvTotalHQ;
    @BindView(R.id.tvTotalUD)
    TextView tvTotalUD;
    @BindView(R.id.tvTotalON)
    TextView tvTotalON;
    @BindView(R.id.edtSundryTotal)
    EditText edtSundryTotal;
    @BindView(R.id.edtChemistTotal)
    EditText edtChemistTotal;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvTotalDistance)
    TextView tvTotalDistance;
    @BindView(R.id.tvMobileEx)
    TextView tvMobileEx;
    @BindView(R.id.tvInternetEx)
    TextView tvInternetEx;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.edtAdjustment)
    EditText edtAdjustment;
    @BindView(R.id.tvFinalTotal)
    TextView tvFinalTotal;
    @BindView(R.id.tvTotalBusi)
    TextView tvTotalBusi;
    @BindView(R.id.tvTOtalDr)
    TextView tvTOtalDr;

    @BindView(R.id.llListDetails)
    LinearLayout llListDetails;

    @BindView(R.id.tvApproveTA)
    TextView tvApproveTA;

    @BindView(R.id.tvConfirmedBy)
    TextView tvConfirmedBy;
    @BindView(R.id.tvApprovedBy)
    TextView tvApprovedBy;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();
    private List<YearResponse.YearListBean> listYear = new ArrayList<>();

    private List<TAResponse.DaysBean> listPlan = new ArrayList<>();

    private List<String> listRemarkReasons = new ArrayList<>();

    private final String EMPLOYEE = "employee";
    private final String MONTH = "month";
    private final String YEAR = "year";
    private final String SRC = "SRC";
    private final String REMARK_REASON = "Remark Reason";

    private String selectedStaffId = "", selectedMonth = "", selectedYear = "", currentYear = "";

    private PlanAdapter planAdapter;

    private boolean isLoading = false;

    private int clickDayPosition = 0;

    private TAResponse.ExpencesBean expencesBean = new TAResponse.ExpencesBean();

    private long mLastClickTime = 0;

    private String TAStatus = "";

    private boolean isDownloadBeforeApprove = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ta_new, container, false);
        ButterKnife.bind(this, rootView);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initViews();
        if (sessionManager.isNetworkAvailable()) {
            getEmployeeList();
        } else {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }
        return rootView;
    }

    private void initViews() {


        edtAdjustment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equalsIgnoreCase("")) {
                    return;
                } else {
                    try {
                        int gTotal = Integer.parseInt(tvGrandTotal.getText().toString());
                        int fTotal = Integer.parseInt(s.toString()) + gTotal;
                        tvFinalTotal.setText(String.valueOf(fTotal));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEmployee.setOnClickListener(this);
        edtMonth.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        tvGenerateReport.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        /*rvPlan.setLayoutManager(new LinearLayoutManager(activity));*/
        rvPlan.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
                return false;
            }
        });
        tvApproveTA.setOnClickListener(this);

        if (selectedStaffId.equals(sessionManager.getStaffId()))
        {
            tvDownload.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.edtEmployee:
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                } else {
                    showListDialog(EMPLOYEE);
                }
                break;
            case R.id.edtMonth:
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                } else {
                    if (selectedYear.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select year");
                    } else {
                        showListDialog(MONTH);
                    }
                }
                break;
            case R.id.edtYear:
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                } else {
                    showListDialog(YEAR);
                }
                break;
            case R.id.tvGenerateReport:
                if (selectedStaffId.equals("")) {
                    AppUtils.showToast(activity, "Please select employee");
                } else if (selectedYear.equals("")) {
                    AppUtils.showToast(activity, "Please select year");
                } else if (selectedMonth.equals("")) {
                    AppUtils.showToast(activity, "Please select month");
                } else {
                    if (sessionManager.isNetworkAvailable()) {
                        getTravellingAllowance();
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                    }
                }
                break;
            case R.id.tvDownload:
                if (selectedStaffId.equals("")) {
                    AppUtils.showToast(activity, "Please select employee");
                } else if (selectedYear.equals("")) {
                    AppUtils.showToast(activity, "Please select year");
                } else if (selectedMonth.equals("")) {
                    AppUtils.showToast(activity, "Please select month");
                } else {
                    if (sessionManager.isNetworkAvailable()) {
                        String url = ApiClient.TRAVELLING_REPORT_DOWNLOAD + "staff_id="+selectedStaffId+"&month="+selectedMonth+"&year="+selectedYear+"&session_id="+sessionManager.getUserId();

                        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);*/

                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("report_url",url);
                        startActivity(intent);
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                    }
                }
                break;
            case R.id.tvCancel:
                edtEmployee.setText("");
                edtYear.setText("");
                edtMonth.setText("");
                selectedYear = "";
                selectedMonth = "";
                selectedStaffId = "";
                listPlan.clear();
                if (planAdapter != null) {
                    planAdapter.notifyDataSetChanged();
                }

                llSummary.setVisibility(View.GONE);
                llListDetails.setVisibility(View.GONE);
                tvSave.setVisibility(View.GONE);
                tvApproveTA.setVisibility(View.GONE);
                tvConfirm.setVisibility(View.GONE);
                break;
            case R.id.tvSave:
                saveTravellingAllowance(false);
                break;
            case R.id.tvConfirm:
                if (selectedStaffId.equals("")) {
                    AppUtils.showToast(activity, "Please select employee");
                } else if (selectedYear.equals("")) {
                    AppUtils.showToast(activity, "Please select year");
                } else if (selectedMonth.equals("")) {
                    AppUtils.showToast(activity, "Please select month");
                } else {
                    if (sessionManager.isNetworkAvailable()) {
                        if (planAdapter != null) {
                            Log.e("********", "onClick: " + planAdapter.isFormFill());

                            if (planAdapter.isFormFill()) {
                                //Change by kiran on 8-10_20
                                saveTravellingAllowance(true);
                                //confirmTravellingAllowance();
                            } else {
                                AppUtils.showToast(activity, "Please select SRC for all date to confirm TA");
                            }
                        }
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                    }
                }
                break;
            case R.id.tvApproveTA:
                if (selectedStaffId.equals("")) {
                    AppUtils.showToast(activity, "Please select employee");
                } else if (selectedYear.equals("")) {
                    AppUtils.showToast(activity, "Please select year");
                } else if (selectedMonth.equals("")) {
                    AppUtils.showToast(activity, "Please select month");
                } else {
                    if (sessionManager.isNetworkAvailable())
                    {
                        if (TAStatus.equalsIgnoreCase("Confirmed")) {
                            approveTravellingAllowance();
                        } else if (TAStatus.equalsIgnoreCase("Approved")) {
                            AppUtils.showToast(activity, "TA is already Approved");
                        } else {
                            AppUtils.showToast(activity, "TA is not confirmed");
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                    }
                }
                break;
        }
    }

    private void getEmployeeList() {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        listEmployee = new ArrayList<>();
        Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(), sessionManager.getUserId());
        empCall.enqueue(new Callback<StaffResponse>() {
            @Override
            public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response) {
                try {
                    if (response.body().getSuccess() == 1) {
                        listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();
                        if (listEmployee.size() == 1) {
                            selectedStaffId = listEmployee.get(0).getStaff_id();
                            edtEmployee.setText(listEmployee.get(0).getName());
                        }
                    } else {
                        AppUtils.showToast(activity, "Could not get employee list.");
                        activity.finish();
                    }
                    isLoading = false;
                    llLoading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, "Could not get employee list.");
                llLoading.setVisibility(View.GONE);
                activity.finish();
            }
        });
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        Call<YearResponse> yearCall = apiService.getLastYearList("", sessionManager.getUserId());
        yearCall.enqueue(new Callback<YearResponse>() {
            @Override
            public void onResponse(Call<YearResponse> call, Response<YearResponse> response) {

                if (response.body().getSuccess() == 1) {
                    currentYear = response.body().getCurrent_year();
                    listYear = response.body().getYearList();
                    if (listYear.size() == 1) {
                        selectedYear = String.valueOf(listYear.get(0).getYear());
                        edtYear.setText(String.valueOf(listYear.get(0).getYear()));
                        getMonthData();
                    }
                } else {
                    AppUtils.showToast(activity, "No year data found");
                }
                isLoading = false;
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t) {
                AppUtils.showToast(activity, "No year data found");
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });

    }

    private void getMonthData() {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        Call<MonthResponse> yearCall = apiService.getListMonth("", sessionManager.getUserId());
        yearCall.enqueue(new Callback<MonthResponse>() {
            @Override
            public void onResponse(Call<MonthResponse> call, Response<MonthResponse> response) {
                listMonth.clear();
                List<MonthResponse.MonthListBean> listMonthTemp = response.body().getMonthList();
                for (int i = 0; i < listMonthTemp.size(); i++) {
                    if (currentYear.equalsIgnoreCase(edtYear.getText().toString().trim())) {
                        if (!listMonthTemp.get(i).getType().equalsIgnoreCase(ApiClient.COMING)) {
                            listMonth.add(listMonthTemp.get(i));
                        }
                    } else {
                        listMonth.add(listMonthTemp.get(i));
                    }
                }
                isLoading = false;

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                AppUtils.showToast(activity, "No month data found");
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void getTravellingAllowance() {
        llLoading.setVisibility(View.VISIBLE);
        Call<TAResponse> TACall = apiService.getTravellingAllowanceNew(selectedMonth, selectedYear, selectedStaffId, sessionManager.getUserId());
        TACall.enqueue(new Callback<TAResponse>() {
            @Override
            public void onResponse(Call<TAResponse> call, Response<TAResponse> response) {
                if (response != null) {
                    if (response.body().getSuccess() == 1) {

                        listRemarkReasons = new ArrayList<>();
                        listRemarkReasons = response.body().getReasons();

                        TAStatus = response.body().getStatus();

                        llSummary.setVisibility(View.VISIBLE);
                        tvEmployee.setText(response.body().getStaff().getName());
                        tvHQ.setText(response.body().getStaff().getHq());
                        tvDivision.setText(response.body().getStaff().getDivision());
                        tvYear.setText(response.body().getStaff().getYear());
                        tvMonth.setText(response.body().getStaff().getMonth());

                        listPlan = response.body().getDays();

                        for (int i = 0; i < listPlan.size(); i++) {
                            TAResponse.DaysBean bean = listPlan.get(i);
                            for (int j = 0; j < listPlan.get(i).getDay_options().size(); j++) {
                                if (listPlan.get(i).getDay_options().get(j).getIs_default().equals("1")) {
                                    bean.setSelected_src_id(listPlan.get(i).getDay_options().get(j).getSrc_id());
                                }
                            }

                            listPlan.set(i, bean);

                        }

                        planAdapter = new PlanAdapter(listPlan);
                        rvPlan.setAdapter(planAdapter);
                        tvSave.setVisibility(View.VISIBLE);
                        tvConfirm.setVisibility(View.VISIBLE);
                        llListDetails.setVisibility(View.VISIBLE);

                        Log.e("checkUserType >> ", "onResponse: " + sessionManager.getUSerType() + " <><> ");
                        Log.e("checkIds >> ", "onResponse: " + selectedStaffId + " == " + sessionManager.getUserId() + " <><> ");
                        Log.e("checkStatus >> ", "onResponse: " + response.body().getStatus() + " <><> ");


                        if(response.body().getIs_manager_approved().equals("true"))
                        {
                            tvSave.setVisibility(View.GONE);
                            tvConfirm.setVisibility(View.GONE);
                            tvApproveTA.setVisibility(View.GONE);
                        }
                        else
                         {
                            if (response.body().getStatus().equalsIgnoreCase("confirmed") && selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                                tvSave.setVisibility(View.GONE);
                                tvConfirm.setVisibility(View.GONE);
                            }

                            if (!sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR) && !selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()) && response.body().getStatus().equalsIgnoreCase("confirmed")) {
                                tvApproveTA.setVisibility(View.VISIBLE);
                            } else {
                                tvApproveTA.setVisibility(View.GONE);
                            }
                        }

                        /*if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
                        {
                            if(response.body().getStatus().equalsIgnoreCase("pending"))
                            {
                                tvSave.setVisibility(View.VISIBLE);
                                tvConfirm.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tvSave.setVisibility(View.GONE);
                                tvConfirm.setVisibility(View.GONE);
                            }

                            tvApproveTA.setVisibility(View.GONE);
                        }
                        else
                        {
                            if(selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()))
                            {
                                tvApproveTA.setVisibility(View.GONE);
                                if(response.body().getStatus().equalsIgnoreCase("Pending"))
                                {
                                    tvConfirm.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    tvConfirm.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                if(response.body().getStatus().equalsIgnoreCase("pending"))
                                {
                                    tvConfirm.setVisibility(View.VISIBLE);
                                    tvApproveTA.setVisibility(View.GONE);
                                }
                                else if(response.body().getStatus().equalsIgnoreCase("confirmed"))
                                {
                                    tvConfirm.setVisibility(View.GONE);
                                    tvApproveTA.setVisibility(View.VISIBLE);
                                }
                                else if(response.body().getStatus().equalsIgnoreCase("approved"))
                                {
                                    tvConfirm.setVisibility(View.GONE);
                                    tvApproveTA.setVisibility(View.GONE);
                                }
                            }
                        }*/

                        tvMobileEx.setText(String.valueOf(response.body().getExpences().getMobile_expence()));
                        tvInternetEx.setText(String.valueOf(response.body().getExpences().getInternet_expence()));
                        edtAdjustment.setText(String.valueOf(response.body().getExpences().getAdjustment()));
                        if (planAdapter != null) {
                            try {

                                expencesBean = response.body().getExpences();

                                setTotalData();

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            llListDetails.setVisibility(View.GONE);
                        }

                        tvApprovedBy.setText(response.body().getStaff().getApproved_by() != null && !response.body().getStaff().getApproved_by().equals("") ?
                                response.body().getStaff().getApproved_by() : "-");

                        tvConfirmedBy.setText(response.body().getStaff().getName() != null && !response.body().getStaff().getName().equals("") ?
                                response.body().getStaff().getName() : "-");
                    } else {
                        llSummary.setVisibility(View.GONE);
                        llListDetails.setVisibility(View.GONE);
                        AppUtils.showToast(activity, response.body().getMessage());
                        tvSave.setVisibility(View.GONE);
                        tvConfirm.setVisibility(View.GONE);
                        tvApproveTA.setVisibility(View.GONE);
                    }
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TAResponse> call, Throwable t) {
                t.printStackTrace();
                llSummary.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                tvSave.setVisibility(View.GONE);
                tvConfirm.setVisibility(View.GONE);
                llListDetails.setVisibility(View.GONE);
                tvApproveTA.setVisibility(View.GONE);
            }
        });
    }

    private void setTotalData() {
        tvTotalFare.setText(String.valueOf(planAdapter.getTotalFare()));
        tvTotalHQ.setText(String.valueOf(planAdapter.getTotalHQ()));
        tvTotalUD.setText(String.valueOf(planAdapter.getTotalUD()));
        tvTotalON.setText(String.valueOf(planAdapter.getTotalON()));
        edtSundryTotal.setText(String.valueOf(planAdapter.getTotalSundry()));
        edtChemistTotal.setText(String.valueOf(planAdapter.getTotalChemist()));
        tvTotal.setText(String.valueOf(planAdapter.getTotal()));
        tvTotalDistance.setText(String.valueOf(planAdapter.getTotalDist()));
        tvTotalBusi.setText(String.valueOf(planAdapter.getTotalBusiness()));
        tvTOtalDr.setText(String.valueOf(planAdapter.getTotalDr()));

        Log.e("Total Dr >> ", "setTotalData: " + planAdapter.getTotalDr());

        int a = expencesBean.getMobile_expence() + expencesBean.getInternet_expence();
        tvGrandTotal.setText(String.valueOf(planAdapter.getTotal() + a));

        int gtotal = Integer.parseInt(tvGrandTotal.getText().toString().trim());
        int adjsutment = 0;
        if (edtAdjustment.getText().toString().trim().equalsIgnoreCase("")) {
            adjsutment = 0;
        } else {
            adjsutment = Integer.parseInt(edtAdjustment.getText().toString());
        }
        tvFinalTotal.setText(String.valueOf(gtotal + adjsutment));
    }

    private void saveTravellingAllowance(final Boolean isForConfirmClick) {
        if (selectedStaffId.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select employee");
        } else if (selectedYear.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select year");
        } else if (selectedMonth.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select month");
        } else {
            if (sessionManager.isNetworkAvailable()) {
                llLoading.setVisibility(View.VISIBLE);

                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < listPlan.size(); i++) {
                    try {
                        JSONObject subObj = new JSONObject();
                        if (listPlan.get(i).getDay_options().size() > 0)
                        {
                            for (int j = 0; j < listPlan.get(i).getDay_options().size(); j++) {
                                try {
                                    if (listPlan.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1")) {
                                        subObj.put("src_id", listPlan.get(i).getDay_options().get(j).getSrc_id());
                                        subObj.put("sundry", listPlan.get(i).getSundry());
                                        subObj.put("chemist", listPlan.get(i).getChemist());
                                        subObj.put("remark", listPlan.get(i).getRemark());
                                        subObj.put("is_leave", listPlan.get(i).getLeave());
                                        subObj.put("is_holiday", listPlan.get(i).getHoliday());
                                        subObj.put("day_type", listPlan.get(i).getDay_type());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                subObj.put("src_id", "");
                                subObj.put("sundry", listPlan.get(i).getSundry());
                                subObj.put("chemist", listPlan.get(i).getChemist());
                                subObj.put("remark", listPlan.get(i).getRemark());
                                subObj.put("is_leave", listPlan.get(i).getLeave());
                                subObj.put("is_holiday", listPlan.get(i).getHoliday());
                                subObj.put("day_type", listPlan.get(i).getDay_type());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        jsonObject.put(String.valueOf(listPlan.get(i).getDate()), subObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("Day Json >>> ", "saveTravellingAllowance: " + jsonObject.toString());

                Call<CommonResponse> saveCall = apiService.saveTravellingAllowance(selectedStaffId,
                        selectedMonth,
                        selectedYear,
                        jsonObject.toString(),
                        edtAdjustment.getText().toString().trim(),
                        sessionManager.getUserId());
                saveCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (isForConfirmClick) {
                            confirmTravellingAllowance();
                        } else {
                            llLoading.setVisibility(View.GONE);
                            AppUtils.showToast(activity, response.body().getMessage());
                            if (response.body().getSuccess() == 1) {
                                getTravellingAllowance();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        llLoading.setVisibility(View.GONE);
                    }
                });
            } else {
                AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                llLoading.setVisibility(View.GONE);
            }
        }
    }

    private void approveTravellingAllowance() {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveTravellingAllowance(selectedMonth, edtYear.getText().toString().trim(), selectedStaffId, sessionManager.getUserId(), sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                AppUtils.showToast(activity, response.body().getMessage());
                if (response.body().getSuccess() == 1) {
                    TAStatus = "Approved";
                    llSummary.setVisibility(View.GONE);
                    llListDetails.setVisibility(View.GONE);
                    tvSave.setVisibility(View.GONE);
                    tvApproveTA.setVisibility(View.GONE);
                    tvConfirm.setVisibility(View.GONE);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void confirmTravellingAllowance() {
        if (selectedStaffId.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select employee");
        } else if (selectedYear.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select year");
        } else if (selectedMonth.equalsIgnoreCase("")) {
            AppUtils.showToast(activity, "Please select month");
        } else {
            if (sessionManager.isNetworkAvailable()) {
                llLoading.setVisibility(View.VISIBLE);

                Call<CommonResponse> confirm = apiService.confirmTravellingAllowance(selectedMonth,
                        selectedYear,
                        selectedStaffId, sessionManager.getUserId());
                confirm.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body().getSuccess() == 1) {
                            TAStatus = "Confirmed";
                            tvConfirm.setVisibility(View.GONE);

                            if (!sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR)) {
                                if (selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                                    tvApproveTA.setVisibility(View.GONE);
                                } else {
                                    tvApproveTA.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                        AppUtils.showToast(activity, response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                });
                llLoading.setVisibility(View.GONE);
            } else {
                AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
            }
        }
    }


    DialogListAdapter areaAdapter;

    public void showListDialog(final String isFor) {
        listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
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
                AppUtils.hideKeyboard(edtEmployee, activity);
            }
        });
        LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        areaAdapter = new DialogListAdapter(listDialog, isFor, false, "", rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(areaAdapter);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);
        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

        if (isFor.equalsIgnoreCase(MONTH) || isFor.equalsIgnoreCase(YEAR) || isFor.equalsIgnoreCase(SRC) || isFor.equalsIgnoreCase(REMARK_REASON)) {
            inputSearch.setVisibility(View.GONE);
        }

        edtSearchDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                int textlength = edtSearchDialog.getText().length();

                if (isFor.equals(EMPLOYEE)) {
                    listEmployeeSearch.clear();
                    for (int i = 0; i < listEmployee.size(); i++) {
                        if (textlength <= listEmployee.get(i).getName().length()) {
                            if (listEmployee.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listEmployeeSearch.add(listEmployee.get(i));
                            }
                        }
                    }
                }
                AppendList(listDialog, isFor, true, rvListDialog);
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

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";

        DialogListAdapter(Dialog dialog, String isFor, boolean isForSearch, String searchText, RecyclerView recyclerView) {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position == getItemCount() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if (isFor.equals(EMPLOYEE)) {
                holder.cb.setVisibility(View.GONE);
                final StaffResponse.StaffBean getSet;
                if (isForSearch) {
                    getSet = listEmployeeSearch.get(position);
                } else {
                    getSet = listEmployee.get(position);
                }
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtEmployee.setText(getSet.getName());
                        selectedStaffId = getSet.getStaff_id();

                        if (selectedStaffId.equals(sessionManager.getUserId()))
                        {
                            tvDownload.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvDownload.setVisibility(View.VISIBLE);
                        }

                        dialog.dismiss();
                        dialog.cancel();

                        selectedMonth = "";
                        edtMonth.setText("");
                        llSummary.setVisibility(View.GONE);
                        llListDetails.setVisibility(View.GONE);
                        tvSave.setVisibility(View.GONE);
                        tvApproveTA.setVisibility(View.GONE);
                        tvConfirm.setVisibility(View.GONE);

                    }
                });
            } else if (isFor.equalsIgnoreCase(MONTH)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(listMonth.get(position).getMonth());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedMonth = listMonth.get(position).getNumber();
                        edtMonth.setText(listMonth.get(position).getMonth());
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            } else if (isFor.equalsIgnoreCase(YEAR)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(String.valueOf(listYear.get(position).getYear()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedYear = String.valueOf(listYear.get(position).getYear());
                        edtYear.setText(String.valueOf(listYear.get(position).getYear()));
                        edtMonth.setText("");
                        selectedMonth = "";
                        getMonthData();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            } else if (isFor.equalsIgnoreCase(SRC)) {
                holder.cb.setVisibility(View.GONE);

                final TAResponse.DaysBean.DayOptionsBean bean = listPlan.get(clickDayPosition).getDay_options().get(position);

                holder.tvValue.setText(bean.getSrc_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Src Clicked
                        tvSrcDialog.setText(bean.getSrc_name());
                        planAdapter.selectAndUpdateSrc(bean.getSrc_id());
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            } else if (isFor.equalsIgnoreCase(REMARK_REASON)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(String.valueOf(listRemarkReasons.get(position)));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtRemarkReasonDialog.setText(listRemarkReasons.get(position));
                        tvRemoveReasonDialog.setVisibility(View.VISIBLE);
                        if (listRemarkReasons.get(position).equalsIgnoreCase("others")) {
                            llOtherRemarkDialog.setVisibility(View.VISIBLE);
                        } else {
                            llOtherRemarkDialog.setVisibility(View.GONE);
                        }
                        planAdapter.setSelectedRemark(listRemarkReasons.get(position));
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (isFor.equalsIgnoreCase(EMPLOYEE)) {
                if (isForSearch) {
                    return listEmployeeSearch.size();
                } else {
                    return listEmployee.size();
                }
            } else if (isFor.equalsIgnoreCase(MONTH)) {
                return listMonth.size();
            } else if (isFor.equalsIgnoreCase(YEAR)) {
                return listYear.size();
            } else if (isFor.equalsIgnoreCase(SRC)) {
                return listPlan.get(clickDayPosition).getDay_options().size();
            } else if (isFor.equalsIgnoreCase(REMARK_REASON)) {
                return listRemarkReasons.size();
            } else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvValue, tvId;
            private CheckBox cb;
            private View viewLine;

            public ViewHolder(View itemView) {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                viewLine = itemView.findViewById(R.id.viewLine);
                cb = (CheckBox) itemView.findViewById(R.id.cb);
                cb.setTypeface(AppUtils.getTypefaceRegular(activity));
            }
        }
    }

    private void AppendList(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea) {
        areaAdapter = new DialogListAdapter(dialog, isFor, true, "", rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

        List<TAResponse.DaysBean> listItems;
        boolean isForDelete = false;

        PlanAdapter(List<TAResponse.DaysBean> list) {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_ta_new, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            if (position == 0) {
                holder.llTitle.setVisibility(View.VISIBLE);
            } else {
                holder.llTitle.setVisibility(View.GONE);
            }

            final TAResponse.DaysBean getSet = listItems.get(position);
            int newPos = position + 1;
            holder.tvSrNo.setText(String.valueOf(newPos));
            holder.tvDay.setText(getSet.getDay_name());
            holder.tvDate.setText(AppUtils.getDateCurrentTimeString(getSet.getDate()));
            holder.tvTPArea.setText(getSet.getTp_name());

            TAResponse.DaysBean.DayOptionsBean dayOptionsBean = new TAResponse.DaysBean.DayOptionsBean();

            if (getSet.getDay_options().size() > 0)
            {
                for (int i = 0; i < getSet.getDay_options().size(); i++) {
                    if (getSet.getDay_options().get(i).getIs_default().equals("1")) {
                        dayOptionsBean = getSet.getDay_options().get(i);
                        holder.tvSrc.setText(getSet.getDay_options().get(i).getSrc_name());
                        holder.tvSrc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);
                        break;
                    }
                }
            } else {
                holder.tvSrc.setText("-----");
                holder.tvSrc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            //If day is sunday than do not allow to change sundry and chemist
            if (getSet.getDay_name().equalsIgnoreCase("sun") ||
                    getSet.getDay_name().equalsIgnoreCase("sunday") ||
                    getSet.getLeave().equalsIgnoreCase("true") ||
                    getSet.getHoliday().equalsIgnoreCase("true"))
            {
                holder.llValue.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_sunday));
            }
            else
            {
                if (getSet.getWork_miss_match().equalsIgnoreCase("1"))
                {
                    holder.llValue.setBackgroundColor(ContextCompat.getColor(activity, R.color.yellow));
                }
                else
                {
                    holder.llValue.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
            }

            holder.edtSundry.setText(String.valueOf(getSet.getSundry()));
            holder.edtSundry.setSelection(String.valueOf(getSet.getSundry()).length());

            holder.edtRemarkReason.setText(getSet.getRemark());

            /*if(getSet.getRemark().equalsIgnoreCase(""))
            {
                if(getSet.getRemark_other().equalsIgnoreCase(""))
                {
                    holder.edtRemarkReason.setText("others");
                }
                else
                {
                    holder.edtRemarkReason.setText(getSet.getRemark_other());
                }
            }
            else
            {
                holder.edtRemarkReason.setText(getSet.getRemark());
            }*/

            holder.tvTotal.setText(String.valueOf(getSet.getTotal()));
            holder.tvDist.setText(dayOptionsBean.getDistance());
            holder.tvTMode.setText(dayOptionsBean.getTravelling_mode());
            holder.tvFare.setText(dayOptionsBean.getFare());
            holder.tvHQ.setText(dayOptionsBean.getHq());
            holder.tvUD.setText(dayOptionsBean.getUd());
            holder.tvON.setText(dayOptionsBean.getOn());
            holder.tvDR.setText(String.valueOf(getSet.getDoctors()));
            holder.tvBusi.setText(String.valueOf(getSet.getBusiness()));

            holder.edtSundry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    clickDayPosition = position;
                    showTADialog(AppUtils.getDateCurrentTimeString(getSet.getDate()));
                }
            });

            holder.edtRemarkReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    clickDayPosition = position;
                    showTADialog(AppUtils.getDateCurrentTimeString(getSet.getDate()));
                }
            });

            holder.tvSrc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    clickDayPosition = position;
                    showTADialog(AppUtils.getDateCurrentTimeString(getSet.getDate()));
                }
            });
        }

        public void setSelectedRemark(String remark) {
            final TAResponse.DaysBean getSet = listItems.get(clickDayPosition);
            getSet.setRemark(remark);
            //notifyDataSetChanged();
            notifyItemChanged(clickDayPosition);
        }

        public void selectAndUpdateSrc(String src_id) {
            int fare = 0, hq = 0, ud = 0, on = 0, sundry = 0, chemist = 0;
            List<TAResponse.DaysBean.DayOptionsBean> list = listItems.get(clickDayPosition).getDay_options();
            for (int i = 0; i < list.size(); i++) {
                TAResponse.DaysBean.DayOptionsBean bean = list.get(i);
                if (list.get(i).getSrc_id().equalsIgnoreCase(src_id)) {
                    fare = Integer.parseInt(bean.getFare());
                    hq = Integer.parseInt(bean.getHq());
                    ud = Integer.parseInt(bean.getUd());
                    on = Integer.parseInt(bean.getOn());

                    bean.setIs_default("1");
                    list.set(i, bean);
                } else {
                    bean.setIs_default("0");
                    list.set(i, bean);
                }
            }

            TAResponse.DaysBean daysBean = listItems.get(clickDayPosition);
            sundry = daysBean.getSundry();
            chemist = daysBean.getChemist();
            daysBean.setDay_options(list);
            daysBean.setSelected_src_id(src_id);
            daysBean.setTotal(fare + hq + ud + on + sundry + chemist);

            listPlan.set(clickDayPosition, daysBean);
            notifyDataSetChanged();
            setTotalData();
        }

        public int getTotalDist() {
            int sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getDay_options().size(); j++) {
                    if (listItems.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1") &&
                            !listItems.get(i).getDay_options().get(j).getDistance().equalsIgnoreCase("0")) {
                        sum += Float.parseFloat(listItems.get(i).getDay_options().get(j).getDistance());
                    }
                }
            }
            return sum;
        }

        public int getTotalBusiness() {
            int sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getBusiness() != 0) {
                    sum += listItems.get(i).getBusiness();
                }
            }
            return sum;
        }

        public int getTotalDr() {
            int sum = 0;
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getDoctors() != 0) {
                    sum += listItems.get(i).getDoctors();
                }
            }
            return sum;
        }

        public int getTotalFare() {
            int sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getDay_options().size(); j++) {
                    if (listItems.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1") &&
                            !listItems.get(i).getDay_options().get(j).getFare().equalsIgnoreCase("0")) {
                        sum += Float.parseFloat(listItems.get(i).getDay_options().get(j).getFare());
                    }
                }
            }
            return sum;
        }

        public float getTotalHQ() {
            float sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getDay_options().size(); j++) {
                    if (listItems.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1") &&
                            !listItems.get(i).getDay_options().get(j).getHq().equalsIgnoreCase("0")) {
                        sum += Float.parseFloat(listItems.get(i).getDay_options().get(j).getHq());
                    }
                }
            }
            return sum;
        }

        public int getTotalUD() {
            int sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getDay_options().size(); j++) {
                    if (listItems.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1") &&
                            !listItems.get(i).getDay_options().get(j).getUd().equalsIgnoreCase("0")) {
                        sum += Float.parseFloat(listItems.get(i).getDay_options().get(j).getUd());
                    }
                }
            }
            return sum;
        }

        public int getTotalON() {
            int sum = 0;

            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getDay_options().size(); j++) {
                    if (listItems.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1") &&
                            !listItems.get(i).getDay_options().get(j).getOn().equalsIgnoreCase("0")) {
                        sum += Float.parseFloat(listItems.get(i).getDay_options().get(j).getOn());
                    }
                }
            }

            return sum;
        }

        public int getTotalSundry() {
            int sum = 0;
            for (int i = 0; i < listItems.size(); i++) {
                sum += listItems.get(i).getSundry();
            }
            return sum;
        }

        public int getTotalChemist() {
            int sum = 0;
            for (int i = 0; i < listItems.size(); i++) {
                sum += listItems.get(i).getChemist();
            }
            return sum;
        }

        public int getTotal() {
            int sum = 0;
            for (int i = 0; i < listItems.size(); i++) {
                sum += listItems.get(i).getTotal();
            }
            return sum;
        }

        public boolean isFormFill() {
            boolean b = true;
            try {
                for (int i = 0; i < listItems.size(); i++) {
                    if (listItems.get(i).getSelected_src_id().equals("")) {
                        if (listItems.get(i).getHoliday().equalsIgnoreCase("true") ||
                                listItems.get(i).getLeave().equalsIgnoreCase("true") ||
                                listItems.get(i).getDay_name().equalsIgnoreCase("sun")) {
                            b = true;
                        } else {
                            b = false;
                            break;
                        }
                    } else {
                        b = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return b;
        }

        /*public boolean isFormFill()
        {
            boolean b = true;
            for (int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getHoliday().equalsIgnoreCase("true") ||
                        !listItems.get(i).getLeave().equalsIgnoreCase("true"))
                {
                    if(listPlan.get(i).getDay_options().size()>0)
                    {
                        for (int j = 0; j < listPlan.get(i).getDay_options().size(); j++)
                        {

                            if(listPlan.get(i).getDay_options().get(j).getIs_default().equalsIgnoreCase("1"))
                            {
                                Log.i("***********", "isFormFill: "+listPlan.get(i).getDay_name());
                                b = true;
                            }
                            else
                            {
                                b = false;
                            }
                        }
                    }
                    else
                    {
                        b = true;
                    }
                }
            }

            return b;
        }*/

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.llTitle)
            LinearLayout llTitle;
            @BindView(R.id.tvSrNo)
            TextView tvSrNo;
            @BindView(R.id.tvDate)
            TextView tvDate;
            @BindView(R.id.tvDay)
            TextView tvDay;
            @BindView(R.id.tvTPArea)
            TextView tvTPArea;
            @BindView(R.id.tvSrc)
            EditText tvSrc;
            @BindView(R.id.edtSundry)
            EditText edtSundry;
            @BindView(R.id.edtRemarkReason)
            EditText edtRemarkReason;
            @BindView(R.id.tvDist)
            TextView tvDist;
            @BindView(R.id.tvTMode)
            TextView tvTMode;
            @BindView(R.id.tvFare)
            TextView tvFare;
            @BindView(R.id.tvHQ)
            TextView tvHQ;
            @BindView(R.id.tvUD)
            TextView tvUD;
            @BindView(R.id.tvON)
            TextView tvON;
            @BindView(R.id.tvTotal)
            TextView tvTotal;
            @BindView(R.id.tvDR)
            TextView tvDR;
            @BindView(R.id.tvBusi)
            TextView tvBusi;
            @BindView(R.id.llValue)
            LinearLayout llValue;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }

            @Override
            public void onClick(View v) {
            }
        }
    }

    LinearLayout llSrcDialog, llOtherRemarkDialog;
    TextView tvSrcDialog, tvSubmitDialog, tvRemoveReasonDialog;
    EditText edtSundryDialog, edtRemarkReasonDialog, edtRemarkDialog;

    private void showTADialog(String date) {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_ta_details, null);
            dialog.setContentView(sheetView);

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            TextView tvTitle = dialog.findViewById(R.id.tvTitle);

            tvTitle.setText("TA For : " + date);

            llSrcDialog = (LinearLayout) dialog.findViewById(R.id.llSrc);
            llOtherRemarkDialog = (LinearLayout) dialog.findViewById(R.id.llOtherRemark);
            tvSrcDialog = (TextView) dialog.findViewById(R.id.tvSrc);
            tvSubmitDialog = (TextView) dialog.findViewById(R.id.tvSubmit);
            edtSundryDialog = (EditText) dialog.findViewById(R.id.edtSundry);
            edtRemarkReasonDialog = (EditText) dialog.findViewById(R.id.edtRemarkReason);
            edtRemarkDialog = (EditText) dialog.findViewById(R.id.edtRemark);
            tvRemoveReasonDialog = (TextView) dialog.findViewById(R.id.tvRemoveReason);

            final TAResponse.DaysBean dayOptionsBean = listPlan.get(clickDayPosition);

            Log.e("********", "showTADialog: " + new Gson().toJson(dayOptionsBean));

            edtSundryDialog.setText(String.valueOf(dayOptionsBean.getSundry()));


            if (dayOptionsBean.getDay_options().size() > 0) {
                for (int i = 0; i < dayOptionsBean.getDay_options().size(); i++) {
                    if (dayOptionsBean.getDay_options().get(i).getIs_default().equals("1")) {

                        tvSrcDialog.setText(dayOptionsBean.getDay_options().get(i).getSrc_name());
                        break;
                    }
                }
            } else {
                tvSrcDialog.setText("-----");
            }

            llSrcDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dayOptionsBean.getDay_options().size() > 0) {
                        showListDialog(SRC);
                    }
                }
            });

            edtSundryDialog.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {

                    if (!s.toString().isEmpty()) {
                        try {
                            TAResponse.DaysBean.DayOptionsBean dbean = new TAResponse.DaysBean.DayOptionsBean();
                            if (dayOptionsBean.getDay_options().size() > 0) {
                                for (int i = 0; i < dayOptionsBean.getDay_options().size(); i++) {
                                    if (dayOptionsBean.getDay_options().get(i).getIs_default().equals("1")) {
                                        dbean = dayOptionsBean.getDay_options().get(i);

                                        int fare = Integer.parseInt(dbean.getFare());
                                        int hq = Integer.parseInt(dbean.getHq());
                                        int ud = Integer.parseInt(dbean.getUd());
                                        int on = Integer.parseInt(dbean.getOn());

                                        int total = fare + hq + ud + on;

                                        final int sundry = Integer.parseInt(s.toString());

                                        dayOptionsBean.setTotal(sundry + total);
                                        dayOptionsBean.setSundry(sundry);
                                        listPlan.set(clickDayPosition, dayOptionsBean);
                                        //planAdapter.notifyDataSetChanged();
                                        setTotalData();

                                        break;
                                    }
                                }
                            } else {
                                final int sundry = Integer.parseInt(s.toString());
                                Log.e("*********", "afterTextChanged:  CALLED   " + sundry);
                                dayOptionsBean.setTotal(sundry);
                                dayOptionsBean.setSundry(sundry);
                                listPlan.set(clickDayPosition, dayOptionsBean);
                                //planAdapter.notifyDataSetChanged();
                                setTotalData();
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {

                            TAResponse.DaysBean.DayOptionsBean dbean = new TAResponse.DaysBean.DayOptionsBean();
                            if (dayOptionsBean.getDay_options().size() > 0) {
                                for (int i = 0; i < dayOptionsBean.getDay_options().size(); i++) {
                                    if (dayOptionsBean.getDay_options().get(i).getIs_default().equals("1")) {
                                        dbean = dayOptionsBean.getDay_options().get(i);

                                        int fare = Integer.parseInt(dbean.getFare());
                                        int hq = Integer.parseInt(dbean.getHq());
                                        int ud = Integer.parseInt(dbean.getUd());
                                        int on = Integer.parseInt(dbean.getOn());

                                        int total = fare + hq + ud + on;

                                        dayOptionsBean.setTotal(total);
                                        dayOptionsBean.setSundry(0);
                                        listPlan.set(clickDayPosition, dayOptionsBean);
                                        //planAdapter.notifyDataSetChanged();
                                        setTotalData();
                                        break;
                                    }
                                }
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            edtRemarkReasonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //selectedRemarkPosition = getAdapterPosition();
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showListDialog(REMARK_REASON);
                }
            });

            //edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());

           /* if(!dayOptionsBean.getRemark_other().equalsIgnoreCase(""))
            {
                llOtherRemarkDialog.setVisibility(View.VISIBLE);
                edtRemarkDialog.setText(dayOptionsBean.getRemark_other());
                edtRemarkDialog.setSelection(dayOptionsBean.getRemark_other().length());

                edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());
                edtRemarkReasonDialog.setSelection(dayOptionsBean.getRemark().length());
            }
            else
            {
                llOtherRemarkDialog.setVisibility(View.GONE);
                edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());
            }*/

            if (dayOptionsBean.getRemark().equalsIgnoreCase("")) {
                llOtherRemarkDialog.setVisibility(View.GONE);
                edtRemarkReasonDialog.setText("");
                tvRemoveReasonDialog.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < listRemarkReasons.size(); i++) {
                    Log.i("******", "showTADialog: " + listRemarkReasons.get(i).toString().trim() + "   " + dayOptionsBean.getRemark());

                   /*if(listRemarkReasons.get(i).toString().trim().equalsIgnoreCase(dayOptionsBean.getRemark()))
                   {
                       llOtherRemarkDialog.setVisibility(View.GONE);
                       edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());
                   }
                   else
                   {
                       llOtherRemarkDialog.setVisibility(View.VISIBLE);
                       edtRemarkReasonDialog.setText("others");
                       edtRemarkDialog.setText(dayOptionsBean.getRemark());
                   }*/

                    if (listRemarkReasons.contains(dayOptionsBean.getRemark())) {
                        if (dayOptionsBean.getRemark().equalsIgnoreCase("others")) {
                            llOtherRemarkDialog.setVisibility(View.VISIBLE);
                            edtRemarkReasonDialog.setText("others");
                            edtRemarkDialog.setText(dayOptionsBean.getRemark());
                            tvRemoveReasonDialog.setVisibility(View.VISIBLE);
                        } else {
                            llOtherRemarkDialog.setVisibility(View.GONE);
                            edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());
                            tvRemoveReasonDialog.setVisibility(View.VISIBLE);
                        }
                    } else {
                        llOtherRemarkDialog.setVisibility(View.VISIBLE);
                        edtRemarkReasonDialog.setText("others");
                        edtRemarkDialog.setText(dayOptionsBean.getRemark());
                    }

                }


               /*llOtherRemarkDialog.setVisibility(View.GONE);
               edtRemarkReasonDialog.setText(dayOptionsBean.getRemark());*/
            }


            edtRemarkDialog.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    TAResponse.DaysBean getSet = listPlan.get(clickDayPosition);
                    getSet.setRemark(s.toString());
                    listPlan.set(clickDayPosition, getSet);
                }
            });

            tvSubmitDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppUtils.hideKeyboard(sheetView, activity);

                    TAResponse.DaysBean getSet = listPlan.get(clickDayPosition);
                    if (dayOptionsBean.getRemark().equalsIgnoreCase("others")) {
                        if (edtRemarkDialog.getText().toString().trim().equals("")) {
                            getSet.setRemark("");
                        } else {
                            getSet.setRemark(edtRemarkDialog.getText().toString());
                        }
                        listPlan.set(clickDayPosition, getSet);
                    }
                    planAdapter.notifyItemChanged(clickDayPosition);
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            tvRemoveReasonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llOtherRemarkDialog.setVisibility(View.GONE);
                    edtRemarkReasonDialog.setText("");
                    edtRemarkDialog.setText("");
                    tvRemoveReasonDialog.setVisibility(View.GONE);
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOtherReason(String reason) {
        boolean isOther = false;
        for (int i = 0; i < listRemarkReasons.size(); i++) {
            if (listRemarkReasons.get(i).toString().trim().equalsIgnoreCase(reason)) {
                isOther = false;
            } else {
                isOther = true;
            }
        }

        return isOther;
    }

}
