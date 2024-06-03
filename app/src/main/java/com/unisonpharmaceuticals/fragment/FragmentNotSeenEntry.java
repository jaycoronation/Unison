package com.unisonpharmaceuticals.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.ParseException;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.MonthResponse;
import com.unisonpharmaceuticals.model.NotSeenResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotSeenEntry extends Fragment implements View.OnClickListener {
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.edtYear)
    EditText edtYear;
    @BindView(R.id.edtMonth)
    EditText edtMonth;
    @BindView(R.id.edtCategory)
    EditText edtCategory;
    @BindView(R.id.tvGenerateReport)
    TextView tvGenerateReport;
    @BindView(R.id.rvNoteSeen)
    RecyclerView rvNoteSeen;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvApproveNotSeen)
    TextView tvApproveNotSeen;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.llBottomSection)
    LinearLayout llBottomSection;

    @BindView(R.id.llSummary)
    LinearLayout llSummary;

    @BindView(R.id.tvEmployee)
    TextView tvEmployee;
    @BindView(R.id.tvHQ)
    TextView tvHQ;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvDivision)
    TextView tvDivision;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.tvMonth)
    TextView tvMonth;

    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvGP)
    TextView tvGP;
    @BindView(R.id.tvCons)
    TextView tvCons;
    @BindView(R.id.tvIMP)
    TextView tvIMP;
    @BindView(R.id.tvPOT)
    TextView tvPOT;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();
    private List<YearResponse.YearListBean> listYear = new ArrayList<>();
    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();
    private ArrayList<String> listCategory = new ArrayList<>();
    private List<NotSeenResponse.NotseenBean> listNotSeen = new ArrayList<>();
    private List<String> listReason = new ArrayList<>();
    private List<String> listNotVisitReason = new ArrayList<>();
    private final String MONTH = "month";
    private final String YEAR = "year";
    private final String EMPLOYEE = "employee";
    private final String CATEGORY = "category";
    private final String REASON = "reason";
    private final String NOT_VISIT_REASON = "notVisitReason";

    private boolean isLoading = false;

    private long mLastClickTime = 0;

    private String selectedStaffId = "", selectedCategory = "", selectedYear = "", selectedMonth = "", status = "", currentYear = "";

    private NotSeenAdapter notSeenAdapter;

    public static Handler handler;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        rootView = inflater.inflate(R.layout.fragment_notseen_entry, container, false);
        ButterKnife.bind(this, rootView);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        initViews();
        if (sessionManager.isNetworkAvailable()) {
            getEmployeeList();
        }
        else {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
            activity.finish();
        }
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 11) {
                    if (notSeenAdapter != null) {
                        notSeenAdapter.updateSelectedDate(String.valueOf(msg.obj), msg.arg1);
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    private void initViews() {
        rvNoteSeen.setLayoutManager(new LinearLayoutManager(activity));
        edtCategory.setOnClickListener(this);
        edtEmployee.setOnClickListener(this);
        edtMonth.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        tvGenerateReport.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvApproveNotSeen.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        listCategory.add("ALL");
        listCategory.add("I");
        listCategory.add("P");
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
                }
                else {
                    showListDialog(EMPLOYEE, new EditText(activity), 0);
                }
                break;
            case R.id.edtCategory:
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                }
                else {
                    showListDialog(CATEGORY, new EditText(activity), 0);
                }
                break;
            case R.id.edtMonth:
                if (edtYear.getText().toString().equalsIgnoreCase("")) {
                    AppUtils.showToast(activity, "Please select year");
                    return;
                }
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                }
                else {
                    showListDialog(MONTH, new EditText(activity), 0);
                }
                break;
            case R.id.edtYear:
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                }
                else {
                    showListDialog(YEAR, new EditText(activity), 0);
                }
                break;
            case R.id.tvGenerateReport:
                if (selectedStaffId.equalsIgnoreCase("")) {
                    AppUtils.showToast(activity, "Please select employee");
                }
                else if (selectedCategory.equalsIgnoreCase("")) {
                    AppUtils.showToast(activity, "Please select category");
                }
                else if (selectedYear.equalsIgnoreCase("")) {
                    AppUtils.showToast(activity, "Please select year");
                }
                else if (selectedMonth.equalsIgnoreCase("")) {
                    AppUtils.showToast(activity, "Please select month");
                }
                else {
                    if (sessionManager.isNetworkAvailable()) {
                        getNotSeenReport();
                    }
                    else {
                        AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                    }
                }
                break;
            case R.id.tvSave:
                if (sessionManager.isNetworkAvailable()) {
                    saveNotSeenEntry();
                }
                else {
                    AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvConfirm:
                if (sessionManager.isNetworkAvailable()) {
                    if (selectedStaffId.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select employee");
                    }
                    else if (selectedCategory.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select category");
                    }
                    else if (selectedYear.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select year");
                    }
                    else if (selectedMonth.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select month");
                    }
                    else {
                        if (sessionManager.isNetworkAvailable()) {
                            confirmNotSeenEntry();
                        }
                        else {
                            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                        }
                    }
                }
                else {
                    AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvApproveNotSeen:
                if (sessionManager.isNetworkAvailable()) {
                    if (selectedStaffId.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select employee");
                    }
                    else if (selectedCategory.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select category");
                    }
                    else if (selectedYear.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select year");
                    }
                    else if (selectedMonth.equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select month");
                    }
                    else {
                        if (sessionManager.isNetworkAvailable()) {
                            approveNotSeen();
                        }
                        else {
                            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                        }
                    }
                }
                else {
                    AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvCancel:
                edtEmployee.setText("");
                edtCategory.setText("");
                edtYear.setText("");
                edtMonth.setText("");
                selectedMonth = "";
                selectedYear = "";
                selectedStaffId = "";
                selectedCategory = "";

                llSummary.setVisibility(View.GONE);
                llBottomSection.setVisibility(View.GONE);
                rvNoteSeen.setVisibility(View.GONE);

                break;

        }
    }

    private void approveNotSeen() {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveNotSeen(selectedMonth, edtYear.getText().toString().trim(), selectedStaffId, sessionManager.getUserId(), sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                AppUtils.showToast(activity, response.body().getMessage());
                if (response.body().getSuccess() == 1) {
                    status = "approved";
                    tvApproveNotSeen.setVisibility(View.GONE);
                    rvNoteSeen.setVisibility(View.GONE);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
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
                    }
                    else {
                        AppUtils.showToast(activity, "Could not get employee list.");
                        activity.finish();
                    }
                    isLoading = false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, "Could not get employee list.");
                activity.finish();
            }
        });

        isLoading = true;
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
                }
                else {
                    AppUtils.showToast(activity, "No year data found");
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t) {
                AppUtils.showToast(activity, "No year data found");
                isLoading = false;
            }
        });

        llLoading.setVisibility(View.GONE);
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
                    if (currentYear.equalsIgnoreCase(selectedYear)) {
                        if (listMonthTemp.get(i).getType().equalsIgnoreCase(ApiClient.PAST) || listMonthTemp.get(i).getType().equalsIgnoreCase(ApiClient.CURRENT)) {
                            listMonth.add(listMonthTemp.get(i));
                        }
                    }
                    else {
                        listMonth.add(listMonthTemp.get(i));
                    }
                }

                isLoading = false;
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, "No month data found");
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getNotSeenReport() {
        llLoading.setVisibility(View.VISIBLE);
        Call<NotSeenResponse> reportCall = apiService.getNotSeenReport(selectedStaffId, selectedCategory, selectedYear, selectedMonth, sessionManager.getUserId());
        reportCall.enqueue(new Callback<NotSeenResponse>() {
            @Override
            public void onResponse(Call<NotSeenResponse> call, final Response<NotSeenResponse> response) {
                if (response.body().getSuccess() == 1) {
                    //listNotSeen.clear();
                    listNotSeen = new ArrayList<>();
                    listNotSeen = response.body().getNotseen();
                    status = response.body().getStatus();

                    llLoading.setVisibility(View.GONE);

                    if (listNotSeen.size() > 0) {

                        llSummary.setVisibility(View.VISIBLE);
                        llBottomSection.setVisibility(View.VISIBLE);
                        rvNoteSeen.setVisibility(View.VISIBLE);

                        notSeenAdapter = new NotSeenAdapter(listNotSeen);
                        rvNoteSeen.setAdapter(notSeenAdapter);

                        listReason = response.body().getReasons();
                        listNotVisitReason = response.body().getDate_reasons();
                    }

                    if (status.equalsIgnoreCase("pending")) {
                        //llBottomSection.setVisibility(View.VISIBLE);
                        tvConfirm.setVisibility(View.VISIBLE);
                        if (!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                        }
                        tvSave.setVisibility(View.VISIBLE);
                    }
                    else if (status.equalsIgnoreCase("confirmed")) {
                        if (!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                            tvSave.setVisibility(View.VISIBLE);
                        }
                        tvConfirm.setVisibility(View.GONE);
                    }
                    else {
                        tvConfirm.setVisibility(View.GONE);
                        tvApproveNotSeen.setVisibility(View.GONE);
                        tvSave.setVisibility(View.GONE);
                    }

                    llSummary.setVisibility(View.VISIBLE);
                    tvEmployee.setText(response.body().getStaff_summary().getName());
                    tvHQ.setText(response.body().getStaff_summary().getHead_quarters());
                    tvCategory.setText(edtCategory.getText().toString().trim());
                    tvDivision.setText(response.body().getStaff_summary().getDivision());
                    tvYear.setText(response.body().getStaff_summary().getYear());
                    tvMonth.setText(response.body().getStaff_summary().getMonth());

                    tvTotal.setText(String.valueOf(response.body().getDoctor_summary().getTotal_doctors()));
                    tvGP.setText(String.valueOf(response.body().getDoctor_summary().getGp_doctors()));
                    tvCons.setText(String.valueOf(response.body().getDoctor_summary().getCons_doctors()));
                    tvIMP.setText(String.valueOf(response.body().getDoctor_summary().getImp_doctors()));
                    tvPOT.setText(String.valueOf(response.body().getDoctor_summary().getPot_doctors()));
                }
                else {
                    AppUtils.showToast(activity, response.body().getMessage());
                    llLoading.setVisibility(View.GONE);
                    llSummary.setVisibility(View.GONE);
                    rvNoteSeen.setVisibility(View.GONE);
                    tvConfirm.setVisibility(View.GONE);
                    tvApproveNotSeen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NotSeenResponse> call, Throwable t) {
                t.printStackTrace();
                llLoading.setVisibility(View.GONE);
                llSummary.setVisibility(View.GONE);
                rvNoteSeen.setVisibility(View.GONE);
            }
        });
    }

    private void saveNotSeenEntry() {
        llLoading.setVisibility(View.VISIBLE);
        String other_date_reasons = "", date_reasons = "", reasons = "", other_reasons = "";

        JSONObject reasonObject = new JSONObject();
        JSONObject notVisitReasonObject = new JSONObject();
        JSONObject otherReasonObject = new JSONObject();
        JSONObject otherNotVisitReasonObject = new JSONObject();

        for (int i = 0; i < listNotSeen.size(); i++) {
            try {
                NotSeenResponse.NotseenBean bean = listNotSeen.get(i);
                reasonObject.put(bean.getDoctor_id(), bean.getReason());
                notVisitReasonObject.put(bean.getDoctor_id(), bean.getDate_reason());
                otherReasonObject.put(bean.getDoctor_id(), bean.getOther_reason());
                otherNotVisitReasonObject.put(bean.getDoctor_id(), bean.getOther_date_reason());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        other_date_reasons = otherNotVisitReasonObject.toString();
        date_reasons = notVisitReasonObject.toString();
        reasons = reasonObject.toString();
        other_reasons = otherReasonObject.toString();

        Log.e("other_date_reasons", "saveNotSeenEntry: " + other_date_reasons.toString());
        Log.e("date_reasons", "saveNotSeenEntry: " + date_reasons.toString());
        Log.e("reasons", "saveNotSeenEntry: " + reasons.toString());
        Log.e("other_reasons", "saveNotSeenEntry: " + other_reasons.toString());

        Call<CommonResponse> saveCall = apiService.saveNotSeenEntry(selectedStaffId, selectedYear, selectedMonth, reasons, other_reasons, date_reasons, other_date_reasons, "true", sessionManager.getUserId());
        saveCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                AppUtils.showToast(activity, response.body().getMessage());
                if (response.body().getSuccess() == 1) {
                    status = response.body().getStatus();

                    if (status.equalsIgnoreCase("pending")) {
                        if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER)) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                            tvConfirm.setVisibility(View.GONE);
                        }
                        else {
                            tvApproveNotSeen.setVisibility(View.GONE);
                            tvConfirm.setVisibility(View.VISIBLE);
                        }
                        tvSave.setVisibility(View.VISIBLE);
                    }
                    else if (status.equalsIgnoreCase("confirmed")) {
                        if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER)) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvApproveNotSeen.setVisibility(View.GONE);
                        }
                        tvSave.setVisibility(View.VISIBLE);
                        tvConfirm.setVisibility(View.GONE);
                    }
                    else {
                        tvConfirm.setVisibility(View.GONE);
                        tvApproveNotSeen.setVisibility(View.GONE);
                        tvSave.setVisibility(View.VISIBLE);
                    }
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void confirmNotSeenEntry() {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> confirmCall = apiService.confirmNotSeenEntry(selectedStaffId, selectedYear, selectedMonth, sessionManager.getUserId(), sessionManager.getUserId());
        confirmCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                AppUtils.showToast(activity, response.body().getMessage());
                if (response.body().getSuccess() == 1) {
                    status = "confirmed";
                    if (status.equalsIgnoreCase("pending")) {
                        //llBottomSection.setVisibility(View.VISIBLE);
                        if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR)) {
                            tvConfirm.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvConfirm.setVisibility(View.GONE);
                        }
                        if (!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                        }
                        tvSave.setVisibility(View.VISIBLE);
                    }
                    else if (status.equalsIgnoreCase("confirmed")) {
                        if (!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId())) {
                            tvApproveNotSeen.setVisibility(View.VISIBLE);
                        }
                        tvSave.setVisibility(View.VISIBLE);
                        tvConfirm.setVisibility(View.GONE);
                    }
                    else {
                        tvConfirm.setVisibility(View.GONE);
                        tvApproveNotSeen.setVisibility(View.GONE);
                        tvSave.setVisibility(View.VISIBLE);
                    }
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    DialogListAdapter areaAdapter;

    public void showListDialog(final String isFor, final EditText editText, final int mainListPos) {
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

        areaAdapter = new DialogListAdapter(listDialog, isFor, false, "", editText, mainListPos, rvListDialog);
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

        if (isFor.equalsIgnoreCase(EMPLOYEE)) {
            inputSearch.setVisibility(View.VISIBLE);
        }
        else {
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
                AppendList(listDialog, isFor, true, rvListDialog, mainListPos);
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
        private String isFor = "";
        private Dialog dialog;
        private boolean isForSearch = false;
        private String searchText = "";
        private EditText editText;
        private int mainPos = 0;

        DialogListAdapter(Dialog dialog, String isFor, boolean isForSearch, String searchText, EditText editText, int mainListPos, RecyclerView recyclerView) {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.editText = editText;
            this.mainPos = mainListPos;
        }

        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position) {
            if (position == getItemCount() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            }
            else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if (isFor.equals(EMPLOYEE)) {
                holder.cb.setVisibility(View.GONE);
                final StaffResponse.StaffBean getSet;
                if (isForSearch) {
                    getSet = listEmployeeSearch.get(position);
                }
                else {
                    getSet = listEmployee.get(position);
                }
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(v -> {
                    edtEmployee.setText(getSet.getName());
                    selectedStaffId = getSet.getStaff_id();
                    dialog.dismiss();
                    dialog.cancel();
                });
            }
            else if (isFor.equalsIgnoreCase(MONTH)) {
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
            }
            else if (isFor.equalsIgnoreCase(YEAR)) {
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
            }
            else if (isFor.equalsIgnoreCase(CATEGORY)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(listCategory.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCategory = listCategory.get(position).toLowerCase();
                        edtCategory.setText(listCategory.get(position));
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(REASON)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(listReason.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(listReason.get(position));
                        if (notSeenAdapter != null) {
                            notSeenAdapter.updateReason(listReason.get(position), mainPos);
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(NOT_VISIT_REASON)) {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(listNotVisitReason.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(listNotVisitReason.get(position));
                        if (notSeenAdapter != null) {
                            notSeenAdapter.updateNotVisitReason(listNotVisitReason.get(position), mainPos);
                        }
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
                }
                else {
                    return listEmployee.size();
                }
            }
            else if (isFor.equalsIgnoreCase(MONTH)) {
                return listMonth.size();
            }
            else if (isFor.equalsIgnoreCase(YEAR)) {
                return listYear.size();
            }
            else if (isFor.equalsIgnoreCase(CATEGORY)) {
                return listCategory.size();
            }
            else if (isFor.equalsIgnoreCase(REASON)) {
                return listReason.size();
            }
            else if (isFor.equalsIgnoreCase(NOT_VISIT_REASON)) {
                return listNotVisitReason.size();
            }
            else {
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

    private void AppendList(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea, int pos) {
        areaAdapter = new DialogListAdapter(dialog, isFor, true, "", new EditText(activity), pos, rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    public class NotSeenAdapter extends RecyclerView.Adapter<NotSeenAdapter.ViewHolder> {

        List<NotSeenResponse.NotseenBean> listItems;
        private ViewHolder viewHolder;


        NotSeenAdapter(List<NotSeenResponse.NotseenBean> list) {
            this.listItems = list;
        }

        @Override
        public NotSeenAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_notseen, viewGroup, false);
            return new NotSeenAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NotSeenAdapter.ViewHolder holder, final int position) {
            viewHolder = holder;
            if (position == 0) {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else {
                holder.llTitle.setVisibility(View.GONE);
            }

            final NotSeenResponse.NotseenBean getSet = listItems.get(position);
            int newPos = position + 1;
            holder.tvSrNo.setText(String.valueOf(newPos));
            holder.tvDrId.setText(getSet.getDoctor_id());
            holder.tvDrName.setText(getSet.getDoctor());
            holder.tvDegree.setText(getSet.getDegree());
            holder.tvCat.setText(getSet.getCategory());
            holder.tvPlace.setText(getSet.getArea());
            holder.tvReason.setText(getSet.getReason());
            holder.tvLastVisitedDate.setText(getSet.getLast_visit());
            holder.tvNotVisitReason.setText(getSet.getDate_reason());

            if (getSet.getReason().equalsIgnoreCase("Other")) {
                holder.llOtherReason.setVisibility(View.VISIBLE);
                holder.edtOtherReason.setText(getSet.getOther_reason());
                holder.edtOtherReason.setSelection(getSet.getOther_reason().length());
                holder.edtOtherReason.setEnabled(true);

            }
            else {
                holder.llOtherReason.setVisibility(View.GONE);
            }

            if (getSet.getDate_reason().equalsIgnoreCase("Other")) {
                holder.llOtherNotVisitReason.setVisibility(View.VISIBLE);
                holder.edtOtherNotVisitReason.setText(getSet.getOther_date_reason());
                holder.edtOtherNotVisitReason.setSelection(getSet.getOther_date_reason().length());
                holder.edtOtherNotVisitReason.setEnabled(true);
            }
            else {
                holder.llOtherNotVisitReason.setVisibility(View.GONE);
            }

            if (status.equalsIgnoreCase("approved")) {
                holder.edtOtherReason.setFocusable(false);
                holder.edtOtherNotVisitReason.setFocusable(false);
                holder.edtOtherReason.setEnabled(false);
                holder.edtOtherNotVisitReason.setEnabled(false);
            }
            else {
                holder.edtOtherReason.setEnabled(true);
                holder.edtOtherNotVisitReason.setEnabled(true);
                holder.edtOtherReason.setFocusable(true);
                holder.edtOtherNotVisitReason.setFocusable(true);
            }

           /* holder.edtOtherReason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getSet.setOther_reason(s.toString());
                    listItems.set(position, getSet);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/

          /*  holder.edtOtherNotVisitReason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getSet.setOther_date_reason(s.toString());
                    listItems.set(position, getSet);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/


        }

        public void updateReason(String reason, int mainListPos) {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            LinearLayout llOther = view.findViewById(R.id.llOtherReason);
            if (reason.equalsIgnoreCase("Other")) {
                llOther.setVisibility(View.VISIBLE);
                listItems.get(mainListPos).setReason(reason);
            }
            else {
                llOther.setVisibility(View.GONE);
                listItems.get(mainListPos).setReason(reason);
                notifyItemChanged(mainListPos);
            }
        }

        public void updateOtherReason(String reason, int mainListPos) {
            listItems.get(mainListPos).setOther_reason(reason);
            notifyItemChanged(mainListPos);
        }

        public void updateDateReason(String reason, int mainListPos) {
            listItems.get(mainListPos).setOther_date_reason(reason);
            notifyItemChanged(mainListPos);
        }

        public void updateNotVisitReason(String reason, int mainListPos) {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            LinearLayout llOther = view.findViewById(R.id.llOtherNotVisitReason);
            if (reason.equalsIgnoreCase("Other") || reason.equalsIgnoreCase("Select Date")) {
                llOther.setVisibility(View.VISIBLE);
                if (reason.equalsIgnoreCase("Other")) {
                    listItems.get(mainListPos).setDate_reason(reason);
                }
                else {
                    showDatePickerDialog(viewHolder.edtOtherNotVisitReason, listItems, mainListPos);
                }
            }
            else {
                llOther.setVisibility(View.GONE);
                listItems.get(mainListPos).setDate_reason(reason);
                notifyItemChanged(mainListPos);
            }
        }

        public void updateSelectedDate(String date, int mainListPos) {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            EditText edtReason = view.findViewById(R.id.edtOtherNotVisitReason);
            edtReason.setText(date);
        }

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
            @BindView(R.id.tvDrId)
            TextView tvDrId;
            @BindView(R.id.tvDrName)
            TextView tvDrName;
            @BindView(R.id.tvDegree)
            TextView tvDegree;
            @BindView(R.id.tvCat)
            TextView tvCat;
            @BindView(R.id.tvPlace)
            TextView tvPlace;
            @BindView(R.id.tvReason)
            EditText tvReason;
            @BindView(R.id.tvLastVisitedDate)
            TextView tvLastVisitedDate;
            @BindView(R.id.tvNotVisitReason)
            EditText tvNotVisitReason;
            @BindView(R.id.edtOtherNotVisitReason)
            EditText edtOtherNotVisitReason;
            @BindView(R.id.edtOtherReason)
            EditText edtOtherReason;
            @BindView(R.id.llOtherNotVisitReason)
            LinearLayout llOtherNotVisitReason;
            @BindView(R.id.llOtherReason)
            LinearLayout llOtherReason;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
                tvReason.setOnClickListener(this);
                tvNotVisitReason.setOnClickListener(this);
                edtOtherReason.setOnClickListener(this);
                edtOtherNotVisitReason.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (v.getId()) {
                    case R.id.edtOtherReason:
                        showOtherDialog("reason", getAdapterPosition(), listItems.get(getAdapterPosition()).getDoctor(), listItems.get(getAdapterPosition()).getArea(), "Reason", listItems.get(getAdapterPosition()).getOther_reason());
                        break;
                    case R.id.edtOtherNotVisitReason:
                        showOtherDialog("notSeenReason", getAdapterPosition(), listItems.get(getAdapterPosition()).getDoctor(), listItems.get(getAdapterPosition()).getArea(), "Not Seen Reason", listItems.get(getAdapterPosition()).getOther_date_reason());
                        break;
                    case R.id.tvReason:
                        if (status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("confirmed")) {
                            showListDialog(REASON, tvReason, getAdapterPosition());
                        }
                        break;
                    case R.id.tvNotVisitReason:
                        if (status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("confirmed")) {
                            showListDialog(NOT_VISIT_REASON, tvNotVisitReason, getAdapterPosition());
                        }
                        break;
                }
            }
        }
    }


    /*public class NotSeenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        NotSeenAdapter.ItemViewHolder holderNew;

        List<NotSeenResponse.NotseenBean> mList = new ArrayList<>();

        public NotSeenAdapter(List<NotSeenResponse.NotseenBean> mList) {

            this.mList = mList;
        }


        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
            {
                return TYPE_HEADER;

            }
            else
            {
                return TYPE_ITEM;
            }
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {

            if (viewType == TYPE_ITEM)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_notseen_new, viewGroup, false);
                return new NotSeenAdapter.ItemViewHolder(view);

            }
            else
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notseen_header, viewGroup, false);
                return new NotSeenAdapter.HeaderViewHolder(view);
            }
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof NotSeenAdapter.HeaderViewHolder)
            {

            }
            else
            {
                NotSeenAdapter.ItemViewHolder viewHolder = (NotSeenAdapter.ItemViewHolder) holder;
                final int pos = position-1;
                holderNew = viewHolder;
                final NotSeenResponse.NotseenBean getSet = mList.get(pos);
                int newPos = pos + 1;
                viewHolder.tvSrNo.setText(String.valueOf(newPos));
                viewHolder.tvDrId.setText(getSet.getDoctor_id());
                viewHolder.tvDrName.setText(getSet.getDoctor());
                viewHolder.tvDegree.setText(getSet.getDegree());
                viewHolder.tvCat.setText(getSet.getCategory());
                viewHolder.tvPlace.setText(getSet.getArea());
                viewHolder.tvReason.setText(getSet.getReason());
                viewHolder.tvLastVisitedDate.setText(getSet.getLast_visit());
                viewHolder.tvNotVisitReason.setText(getSet.getDate_reason());

                if (getSet.getReason().equalsIgnoreCase("Other")) {
                    viewHolder.llOtherReason.setVisibility(View.VISIBLE);
                    viewHolder.edtOtherReason.setText(getSet.getOther_reason());
                    viewHolder.edtOtherReason.setSelection(getSet.getOther_reason().length());
                    viewHolder.edtOtherReason.setEnabled(true);

                } else {
                    viewHolder.llOtherReason.setVisibility(View.GONE);
                }

                if (getSet.getDate_reason().equalsIgnoreCase("Other")) {
                    viewHolder.llOtherNotVisitReason.setVisibility(View.VISIBLE);
                    viewHolder.edtOtherNotVisitReason.setText(getSet.getOther_date_reason());
                    viewHolder.edtOtherNotVisitReason.setSelection(getSet.getOther_date_reason().length());
                    viewHolder.edtOtherNotVisitReason.setEnabled(true);
                } else {
                    viewHolder.llOtherNotVisitReason.setVisibility(View.GONE);
                }

                if (status.equalsIgnoreCase("approved")) {
                    viewHolder.edtOtherReason.setFocusable(false);
                    viewHolder.edtOtherNotVisitReason.setFocusable(false);
                    viewHolder.edtOtherReason.setEnabled(false);
                    viewHolder.edtOtherNotVisitReason.setEnabled(false);
                } else {
                    viewHolder.edtOtherReason.setEnabled(true);
                    viewHolder.edtOtherNotVisitReason.setEnabled(true);
                    viewHolder.edtOtherReason.setFocusable(true);
                    viewHolder.edtOtherNotVisitReason.setFocusable(true);
                }

                viewHolder.edtOtherReason.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getSet.setOther_reason(s.toString());
                        mList.set(pos, getSet);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                viewHolder.edtOtherNotVisitReason.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getSet.setOther_date_reason(s.toString());
                        mList.set(pos, getSet);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }

        public void updateReason(String reason, int mainListPos)
        {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            LinearLayout llOther = view.findViewById(R.id.llOtherReason);
            if (reason.equalsIgnoreCase("Other")) {
                llOther.setVisibility(View.VISIBLE);
                mList.get(mainListPos).setReason(reason);
               *//* showOtherDialog("reason",
                        mainListPos,
                        listItems.get(mainListPos).getDoctor(),
                        listItems.get(mainListPos).getArea(),
                        "Reason",
                        reason);*//*
            } else {
                llOther.setVisibility(View.GONE);
                mList.get(mainListPos).setReason(reason);
                notifyItemChanged(mainListPos);
            }
        }

        public void updateOtherReason(String reason, int mainListPos)
        {
            mList.get(mainListPos).setOther_reason(reason);
            notifyItemChanged(mainListPos);
        }
        public void updateDateReason(String reason, int mainListPos)
        {
            mList.get(mainListPos).setOther_date_reason(reason);
            notifyItemChanged(mainListPos);
        }

        public void updateNotVisitReason(String reason, int mainListPos) {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            LinearLayout llOther = view.findViewById(R.id.llOtherNotVisitReason);
            if (reason.equalsIgnoreCase("Other") || reason.equalsIgnoreCase("Select Date"))
            {
                llOther.setVisibility(View.VISIBLE);
                if (reason.equalsIgnoreCase("Other"))
                {
                    mList.get(mainListPos).setDate_reason(reason);
                    *//*showOtherDialog("notSeenReason",
                            mainListPos,
                            listItems.get(mainListPos).getDoctor(),
                            listItems.get(mainListPos).getArea(),
                            "Not Seen Reason",
                            reason);*//*
                }
                else
                {
                    showDatePickerDialog(holderNew.edtOtherNotVisitReason, mList, mainListPos);
                }
            } else {
                llOther.setVisibility(View.GONE);
                mList.get(mainListPos).setDate_reason(reason);
                notifyItemChanged(mainListPos);
            }
        }

        public void updateSelectedDate(String date,int mainListPos) {
            View view = rvNoteSeen.findViewHolderForAdapterPosition(mainListPos).itemView;
            EditText edtReason = view.findViewById(R.id.edtOtherNotVisitReason);
            edtReason.setText(date);
        }


        @Override
        public int getItemCount() {
            // Add one more counts to accomodate header
            return this.mList.size() + 1;
        }


        // The ViewHolders for Header, Item and Footer
        class HeaderViewHolder extends RecyclerView.ViewHolder
        {
            public View View;
            public HeaderViewHolder(View itemView) {
                super(itemView);
                View = itemView;
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
            public View View;

            @BindView(R.id.tvSrNo)
            TextView tvSrNo;
            @BindView(R.id.tvDrId)
            TextView tvDrId;
            @BindView(R.id.tvDrName)
            TextView tvDrName;
            @BindView(R.id.tvDegree)
            TextView tvDegree;
            @BindView(R.id.tvCat)
            TextView tvCat;
            @BindView(R.id.tvPlace)
            TextView tvPlace;
            @BindView(R.id.tvReason)
            EditText tvReason;
            @BindView(R.id.tvLastVisitedDate)
            TextView tvLastVisitedDate;
            @BindView(R.id.tvNotVisitReason)
            EditText tvNotVisitReason;
            @BindView(R.id.edtOtherNotVisitReason)
            EditText edtOtherNotVisitReason;
            @BindView(R.id.edtOtherReason)
            EditText edtOtherReason;
            @BindView(R.id.llOtherNotVisitReason)
            LinearLayout llOtherNotVisitReason;
            @BindView(R.id.llOtherReason)
            LinearLayout llOtherReason;

            public ItemViewHolder(View v) {
                super(v);
                View = v;
                ButterKnife.bind(this,v);
                tvReason.setOnClickListener(this);
                tvNotVisitReason.setOnClickListener(this);
                edtOtherReason.setOnClickListener(this);
                edtOtherNotVisitReason.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (v.getId())
                {
                    case R.id.edtOtherReason:
                        showOtherDialog("reason",
                                getAdapterPosition(),
                                mList.get(getAdapterPosition()-1).getDoctor(),
                                mList.get(getAdapterPosition()-1).getArea(),
                                "Reason",
                                mList.get(getAdapterPosition()-1).getOther_reason());
                        break;
                    case R.id.edtOtherNotVisitReason:
                        showOtherDialog("notSeenReason",
                                getAdapterPosition(),
                                mList.get(getAdapterPosition()-1).getDoctor(),
                                mList.get(getAdapterPosition()-1).getArea(),
                                "Not Seen Reason",
                                mList.get(getAdapterPosition()-1).getOther_date_reason());
                        break;
                    case R.id.tvReason:
                        if (status.equalsIgnoreCase("pending") ||
                                status.equalsIgnoreCase("confirmed")) {
                            showListDialog(REASON, tvReason, getAdapterPosition()-1);
                        }
                        break;
                    case R.id.tvNotVisitReason:
                        if (status.equalsIgnoreCase("pending") ||
                                status.equalsIgnoreCase("confirmed")) {
                            showListDialog(NOT_VISIT_REASON, tvNotVisitReason, getAdapterPosition()-1);
                        }
                        break;
                }
            }

        }

        public void updateList(List<NotSeenResponse.NotseenBean> list)
        {
            mList = list;
            notifyDataSetChanged();
        }
    }*/


    private void showOtherDialog(final String isFor, final int position, String doctor, String area, String reasonTitle, String otherReason) {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_notseen_other, null);
            dialog.setContentView(sheetView);

            /*dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);*/

            TextView tvTitle = dialog.findViewById(R.id.tvTitle);

            tvTitle.setText(reasonTitle);

            TextView tvArea = (TextView) dialog.findViewById(R.id.tvArea);
            TextView tvDoctor = (TextView) dialog.findViewById(R.id.tvDoctor);
            TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
            final EditText edtOther = (EditText) dialog.findViewById(R.id.edtOther);

            final NotSeenResponse.NotseenBean bean = listNotSeen.get(position);
            tvArea.setText(bean.getArea());
            tvDoctor.setText(bean.getDoctor());
            if (isFor.equalsIgnoreCase("reason")) {
                edtOther.setText(bean.getOther_reason());
                edtOther.setSelection(bean.getOther_reason().length());
            }
            else {
                edtOther.setText(bean.getOther_date_reason());
                edtOther.setSelection(bean.getOther_date_reason().length());
            }

            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppUtils.hideKeyboard(sheetView, activity);
                    dialog.dismiss();
                    dialog.cancel();

                    if (isFor.equalsIgnoreCase("reason"))
                    {
                        notSeenAdapter.updateOtherReason(edtOther.getText().toString().trim(), position);
                    }
                    else
                    {
                        notSeenAdapter.updateDateReason(edtOther.getText().toString().trim(), position);
                    }


                }
            });

            dialog.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Calendar calendar;
    private static int year = 2000, month = 1, day = 1;

    private void showDatePickerDialog(EditText editText, List<NotSeenResponse.NotseenBean> list, int mainListPos) {
        DialogFragment newFragment = new DatePickerFragment(editText, list, mainListPos, notSeenAdapter);
        newFragment.show(activity.getFragmentManager(), "datePicker");
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener {

        EditText editText;
        List<NotSeenResponse.NotseenBean> list = new ArrayList<>();
        int mainListPos = 0;
        NotSeenAdapter notSeenAdapter;


        @SuppressLint("ValidFragment")
        public DatePickerFragment(EditText editText, List<NotSeenResponse.NotseenBean> list, int mainListPos, NotSeenAdapter notSeenAdapter) {
            this.editText = editText;
            this.list = list;
            this.mainListPos = mainListPos;
            this.notSeenAdapter = notSeenAdapter;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datepicker = new DatePickerDialog(getActivity(), this, year, month, day);
            Calendar startCal = Calendar.getInstance();
            startCal.set(Calendar.DAY_OF_MONTH, startCal.getActualMinimum(Calendar.DAY_OF_MONTH));

            Calendar endCal = Calendar.getInstance();
            endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));

            datepicker.getDatePicker().setMinDate(startCal.getTime().getTime());
            datepicker.getDatePicker().setMaxDate(endCal.getTime().getTime());

            datepicker.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                    }
                    return true;
                }
            });
            datepicker.setCancelable(false);
            return datepicker;
        }

        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String str = (selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
            SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            Date d;
            try {
                d = sdf.parse(str);
                sdf.applyPattern("dd-MMM-yyyy");
                editText.setText(sdf.format(d));
                list.get(mainListPos).setDate_reason(sdf.format(d));
                if (handler != null) {
                    Message message = Message.obtain();
                    message.what = 11;
                    message.obj = sdf.format(d);
                    message.arg1 = mainListPos;
                    handler.sendMessage(message);
                }
                //notSeenAdapter.notifyItemChanged(mainListPos);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
        }
    }
}
