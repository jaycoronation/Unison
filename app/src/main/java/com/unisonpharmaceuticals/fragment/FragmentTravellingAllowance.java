package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
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
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.TravellingAllowanceResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentTravellingAllowance extends Fragment implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;
    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.edtEmployee)EditText edtEmployee;
    @BindView(R.id.edtMonth)EditText edtMonth;
    @BindView(R.id.edtYear)EditText edtYear;
    @BindView(R.id.tvCancel)TextView tvCancel;
    @BindView(R.id.tvGenerateReport)TextView tvGenerateReport;
    @BindView(R.id.tvSave)TextView tvSave;
    @BindView(R.id.tvConfirm)TextView tvConfirm;
    @BindView(R.id.rvPlan)
    RecyclerView rvPlan;

    @BindView(R.id.llSummary)LinearLayout llSummary;

    @BindView(R.id.tvEmployee)TextView tvEmployee;
    @BindView(R.id.tvHQ)TextView tvHQ;
    @BindView(R.id.tvDivision)TextView tvDivision;
    @BindView(R.id.tvYear)TextView tvYear;
    @BindView(R.id.tvMonth)TextView tvMonth;

    @BindView(R.id.tvTotalFare) TextView tvTotalFare;
    @BindView(R.id.tvTotalHQ) TextView tvTotalHQ;
    @BindView(R.id.tvTotalUD) TextView tvTotalUD;
    @BindView(R.id.tvTotalON) TextView tvTotalON;
    @BindView(R.id.edtSundryTotal) EditText edtSundryTotal;
    @BindView(R.id.edtChemistTotal) EditText edtChemistTotal;
    @BindView(R.id.tvTotal) TextView tvTotal;
    @BindView(R.id.tvTotalDistance) TextView tvTotalDistance;
    @BindView(R.id.tvMobileEx) TextView tvMobileEx;
    @BindView(R.id.tvInternetEx) TextView tvInternetEx;
    @BindView(R.id.tvGrandTotal) TextView tvGrandTotal;
    @BindView(R.id.edtAdjustment) EditText edtAdjustment;
    @BindView(R.id.tvFinalTotal) TextView tvFinalTotal;
    @BindView(R.id.tvTotalBusi) TextView tvTotalBusi;
    @BindView(R.id.tvTOtalDr) TextView tvTOtalDr;

    @BindView(R.id.llListDetails) LinearLayout llListDetails;

    @BindView(R.id.tvApproveTA)TextView tvApproveTA;


    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();
    private List<YearResponse.YearListBean> listYear = new ArrayList<>();

    private List<TravellingAllowanceResponse.DaysBean> listPlan = new ArrayList<>();

    private List<String> listRemarkReasons = new ArrayList<>();

    private final String EMPLOYEE = "employee";
    private final String MONTH = "month";
    private final String YEAR = "year";

    private String selectedStaffId="",selectedMonth = "",selectedYear = "",currentYear = "";

    private PlanAdapter planAdapter;

    private Timer timer;

    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_travelling_allowance, container, false);
        ButterKnife.bind(this,rootView);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initViews();
        if(sessionManager.isNetworkAvailable())
        {
            getEmployeeList();
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
        return rootView;
    }

    private void initViews()
    {
        edtAdjustment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().trim().equalsIgnoreCase(""))
                {
                    return;
                }
                else
                {
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
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        rvPlan.setLayoutManager(new LinearLayoutManager(activity));
        tvApproveTA.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) 
    {
        switch (v.getId())
        {
            case R.id.edtEmployee:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(EMPLOYEE);
                }
                break;
            case R.id.edtMonth:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    if(selectedYear.equalsIgnoreCase(""))
                    {
                        AppUtils.showToast(activity,"Please select year");
                    }
                    else
                    {
                        showListDialog(MONTH);
                    }
                }
                break;
            case R.id.edtYear:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(YEAR);
                }
                break;
            case R.id.tvGenerateReport:
                if(selectedStaffId.equals(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(selectedYear.equals(""))
                {
                    AppUtils.showToast(activity,"Please select year");
                }
                else if(selectedMonth.equals(""))
                {
                    AppUtils.showToast(activity,"Please select month");
                }
                else
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        getTravellingAllowance();
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
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
                    if(planAdapter!=null)
                    {
                        planAdapter.notifyDataSetChanged();
                    }
                    tvSave.setVisibility(View.GONE);
                break;
            case R.id.tvSave:
                saveTravellingAllowance();
                break;
            case R.id.tvConfirm:
                if(selectedStaffId.equals(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(selectedYear.equals(""))
                {
                    AppUtils.showToast(activity,"Please select year");
                }
                else if(selectedMonth.equals(""))
                {
                    AppUtils.showToast(activity,"Please select month");
                }
                else
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        confirmTravellingAllowance();
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
                break;
            case R.id.tvApproveTA:
                if(selectedStaffId.equals(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(selectedYear.equals(""))
                {
                    AppUtils.showToast(activity,"Please select year");
                }
                else if(selectedMonth.equals(""))
                {
                    AppUtils.showToast(activity,"Please select month");
                }
                else
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        approveTravellingAllowance();
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
                break;
        }
    }

    private void getEmployeeList()
    {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        listEmployee = new ArrayList<>();
        Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
        empCall.enqueue(new Callback<StaffResponse>() {
            @Override
            public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response)
            {
                try {
                    if(response.body().getSuccess()==1)
                    {
                        listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();
                        if(listEmployee.size()==1)
                        {
                            selectedStaffId = listEmployee.get(0).getStaff_id();
                            edtEmployee.setText(listEmployee.get(0).getName());
                            /*if(!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()))
                            {
                                tvConfirm.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tvConfirm.setVisibility(View.GONE);
                            }*/
                        }
                    }
                    else
                    {
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
            public void onFailure(Call<StaffResponse> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity, "Could not get employee list.");
                llLoading.setVisibility(View.GONE);
                activity.finish();
            }
        });
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        Call<YearResponse> yearCall = apiService.getLastYearList("",sessionManager.getUserId());
        yearCall.enqueue(new Callback<YearResponse>() {
            @Override
            public void onResponse(Call<YearResponse> call, Response<YearResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    currentYear = response.body().getCurrent_year();
                    listYear = response.body().getYearList();
                    if(listYear.size()==1)
                    {
                        selectedYear = String.valueOf(listYear.get(0).getYear());
                        edtYear.setText(String.valueOf(listYear.get(0).getYear()));
                        getMonthData();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,"No year data found");
                }
                isLoading = false;
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No year data found");
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });

    }

    private void getMonthData()
    {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        Call<MonthResponse> yearCall = apiService.getListMonth("",sessionManager.getUserId());
        yearCall.enqueue(new Callback<MonthResponse>() {
            @Override
            public void onResponse(Call<MonthResponse> call, Response<MonthResponse> response)
            {
                listMonth.clear();
                List<MonthResponse.MonthListBean> listMonthTemp = response.body().getMonthList();
                for (int i = 0; i < listMonthTemp.size(); i++)
                {
                    if(currentYear.equalsIgnoreCase(edtYear.getText().toString().trim()))
                    {
                        if(!listMonthTemp.get(i).getType().equalsIgnoreCase(ApiClient.PAST))
                        {
                            listMonth.add(listMonthTemp.get(i));
                        }
                    }
                    else
                    {
                        listMonth.add(listMonthTemp.get(i));
                    }
                }
                isLoading = false;

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No month data found");
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void getTravellingAllowance()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<TravellingAllowanceResponse> TACall = apiService.getTravellingAllowance(selectedMonth,selectedYear,selectedStaffId,sessionManager.getUserId());
        TACall.enqueue(new Callback<TravellingAllowanceResponse>() {
            @Override
            public void onResponse(Call<TravellingAllowanceResponse> call, Response<TravellingAllowanceResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    llSummary.setVisibility(View.VISIBLE);
                    tvEmployee.setText(response.body().getStaff().getName());
                    tvHQ.setText(response.body().getStaff().getHq());
                    tvDivision.setText(response.body().getStaff().getDivision());
                    tvYear.setText(response.body().getStaff().getYear());
                    tvMonth.setText(response.body().getStaff().getMonth());

                    listPlan = response.body().getDays();
                    Log.e("listPLan Size >> ", "onResponse: "+listPlan.size() );
                    planAdapter = new PlanAdapter(listPlan);
                    rvPlan.setAdapter(planAdapter);
                    tvSave.setVisibility(View.VISIBLE);
                    llListDetails.setVisibility(View.VISIBLE);

                    if(!selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()))
                    {
                        tvConfirm.setVisibility(View.VISIBLE);
                        tvApproveTA.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvConfirm.setVisibility(View.GONE);
                        tvApproveTA.setVisibility(View.GONE);
                    }
                    tvMobileEx.setText(response.body().getExpences().getMobile_expence());
                    tvInternetEx.setText(response.body().getExpences().getInternet_expence());
                    edtAdjustment.setText(response.body().getExpences().getAdjustment());
                    if(planAdapter!=null)
                    {
                        try {

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

                            int a = Integer.parseInt(response.body().getExpences().getMobile_expence()) + Integer.parseInt(response.body().getExpences().getInternet_expence());
                            tvGrandTotal.setText(String.valueOf(planAdapter.getTotal() + a));

                            int gtotal = Integer.parseInt(tvGrandTotal.getText().toString().trim());
                            int adjsutment = Integer.parseInt(response.body().getExpences().getAdjustment());
                            tvFinalTotal.setText(String.valueOf(gtotal + adjsutment));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        llListDetails.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llSummary.setVisibility(View.GONE);
                    llListDetails.setVisibility(View.GONE);
                    AppUtils.showToast(activity,response.body().getMessage());
                    tvSave.setVisibility(View.GONE);
                    tvConfirm.setVisibility(View.GONE);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TravellingAllowanceResponse> call, Throwable t) {
                t.printStackTrace();
                llSummary.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                tvSave.setVisibility(View.GONE);
                tvConfirm.setVisibility(View.GONE);
                llListDetails.setVisibility(View.GONE);
            }
        });
    }

    private void saveTravellingAllowance()
    {
        if(selectedStaffId.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select employee");
        }
        else if(selectedYear.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select year");
        }
        else if(selectedMonth.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select month");
        }
        else
        {
            if(sessionManager.isNetworkAvailable())
            {
                llLoading.setVisibility(View.VISIBLE);

                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < listPlan.size(); i++)
                {
                    try
                    {
                        JSONObject subObj = new JSONObject();
                        subObj.put("sundry",listPlan.get(i).getSundry());
                        subObj.put("chemist",listPlan.get(i).getChemist());
                        subObj.put("remark",listPlan.get(i).getRemark());
                        jsonObject.put(listPlan.get(i).getDay_id(),subObj);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("Day Json >>> ", "saveTravellingAllowance: "+jsonObject.toString() );

                //Call<CommonResponse> saveCall = apiService.saveTravellingAllowance(jsonObject.toString(),edtAdjustment.getText().toString().trim(),sessionManager.getUserId());
                Call<CommonResponse> saveCall = apiService.saveTravellingAllowance(selectedStaffId,
                        selectedMonth,
                        selectedYear,
                        jsonObject.toString(),
                        edtAdjustment.getText().toString().trim(),
                        sessionManager.getUserId());
                saveCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        AppUtils.showToast(activity,response.body().getMessage());
                        if(response.body().getSuccess()==1)
                        {
                            getTravellingAllowance();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                });
                llLoading.setVisibility(View.GONE);
            }
            else
            {
                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
            }
        }
    }

    private void approveTravellingAllowance()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveTravellingAllowance(selectedMonth,edtYear.getText().toString().trim(),selectedStaffId,sessionManager.getUserId(),sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    tvApproveTA.setVisibility(View.GONE);
                }
                AppUtils.showToast(activity,response.body().getMessage());
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void confirmTravellingAllowance()
    {
        if(selectedStaffId.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select employee");
        }
        else if(selectedYear.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select year");
        }
        else if(selectedMonth.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select month");
        }
        else
        {
            if(sessionManager.isNetworkAvailable())
            {
                llLoading.setVisibility(View.VISIBLE);

                Call<CommonResponse> confirm = apiService.confirmTravellingAllowance(selectedMonth,
                        selectedYear,
                        selectedStaffId,sessionManager.getUserId());
                confirm.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        if(response.body().getSuccess()==1)
                        {
                            Log.e("Response >> ", "onResponse: " + response.body() );

                            tvConfirm.setVisibility(View.GONE);
                        }
                        AppUtils.showToast(activity,response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                });
                llLoading.setVisibility(View.GONE);
            }
            else
            {
                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
            }
        }
    }


    DialogListAdapter areaAdapter;
    public void showListDialog(final String isFor)
    {
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
                AppUtils.hideKeyboard(edtEmployee,activity);
            }
        });
        LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select "+isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        areaAdapter = new DialogListAdapter(listDialog, isFor,false,"",rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(areaAdapter);

        tvDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
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
        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

        if(isFor.equalsIgnoreCase(MONTH) || isFor.equalsIgnoreCase(YEAR))
        {
            inputSearch.setVisibility(View.GONE);
        }

        edtSearchDialog.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                int textlength = edtSearchDialog.getText().length();

                if(isFor.equals(EMPLOYEE))
                {
                    listEmployeeSearch.clear();
                    for (int i = 0; i < listEmployee.size(); i++)
                    {
                        if (textlength <= listEmployee.get(i).getName().length())
                        {
                            if (listEmployee.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listEmployeeSearch.add(listEmployee.get(i));
                            }
                        }
                    }
                }
                AppendList(listDialog,isFor,true,rvListDialog);
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

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";

        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyclerView)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }

        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
        {
            if(position == getItemCount()-1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if(isFor.equals(EMPLOYEE))
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
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        edtEmployee.setText(getSet.getName());
                        selectedStaffId = getSet.getStaff_id();

                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(MONTH))
            {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(listMonth.get(position).getMonth());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        selectedMonth = listMonth.get(position).getNumber();
                        edtMonth.setText(listMonth.get(position).getMonth());
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(YEAR))
            {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(String.valueOf(listYear.get(position).getYear()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
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

        }

        @Override
        public int getItemCount()
        {
            if(isFor.equalsIgnoreCase(EMPLOYEE))
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
            else if(isFor.equalsIgnoreCase(MONTH))
            {
                return listMonth.size();
            }
            else if(isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
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

    private void AppendList(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea)
    {
        areaAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder>
    {

        List<TravellingAllowanceResponse.DaysBean> listItems;


        PlanAdapter(List<TravellingAllowanceResponse.DaysBean> list) {
            this.listItems = list;
        }

        @Override
        public PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_travelling_allowance, viewGroup, false);
            return new PlanAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final PlanAdapter.ViewHolder holder, final int position)
        {

            if (position == 0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }

            final TravellingAllowanceResponse.DaysBean getSet = listItems.get(position);
            int newPos = position + 1;
            holder.tvSrNo.setText(String.valueOf(newPos));
            holder.tvDay.setText(getSet.getDay_name());
            holder.tvDate.setText(AppUtils.getDateCurrentTimeString(getSet.getDate()));
            holder.tvTPArea.setText(getSet.getTp_name());
            holder.tvSrc.setText(getSet.getSrc());
            holder.edtSundry.setText(String.valueOf(getSet.getSundry()));
            holder.edtSundry.setSelection(String.valueOf(getSet.getSundry()).length());
            holder.edtChemist.setText(String.valueOf(getSet.getChemist()));
            holder.edtChemist.setSelection(String.valueOf(getSet.getChemist()).length());
            holder.edtRemark.setText(getSet.getRemark());
            holder.edtRemark.setSelection(getSet.getRemark().length());
            holder.tvTotal.setText(String.valueOf(getSet.getTotal()));
            holder.tvDist.setText(getSet.getDistance());
            holder.tvTMode.setText(getSet.getTravelling_mode());
            holder.tvFare.setText(getSet.getFare());
            holder.tvHQ.setText(String.valueOf(getSet.getHQ()));
            holder.tvUD.setText(String.valueOf(getSet.getUD()));
            holder.tvON.setText(getSet.getON());
            holder.tvDR.setText(getSet.getDoctors());
            holder.tvBusi.setText(getSet.getBusiness());

            holder.edtSundry.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (timer != null) {
                        timer.cancel();
                    }
                }

                @Override
                public void afterTextChanged(final Editable s)
                {
                    // user typed: start the timer
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // do your actual work here
                            if(!s.toString().equals(""))
                            {
                                try {
                                    final int sundry = Integer.parseInt(s.toString());
                                    final int total = getSet.getTotal();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            holder.tvTotal.setText(String.valueOf(sundry+total));
                                            getSet.setTotal(sundry+total);
                                            getSet.setSundry(sundry);
                                        }
                                    });
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.tvTotal.setText(String.valueOf(getSet.getTotal()));
                                    }
                                });
                            }
                        }
                    }, 300); // 300ms delay before the timer executes the „run“ method from TimerTask
                }
            });

            holder.edtSundry.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL)
                    {
                        if(!holder.edtSundry.getText().toString().equals(""))
                        {
                            int lastAmount = Integer.parseInt(holder.edtSundry.getText().toString().trim());
                            int total = getSet.getTotal();
                            Log.e("CALLED >> ", "onKey: "+(total - lastAmount));
                        }
                    }
                    /*if(keyCode == KeyEvent.KEYCODE_DEL) {
                        //this is for backspace
                        Log.e("CALLED >> ", "onKey: "+holder.edtSundry.getText());
                    }*/
                    return false;
                }
            });
        }

        public int getTotalDist()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getDistance().equalsIgnoreCase(""))
                {

                    sum += Float.parseFloat(listItems.get(i).getDistance());
                }
            }
            return sum;
        }

        public int getTotalBusiness()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getBusiness().equalsIgnoreCase(""))
                {

                    sum += Float.parseFloat(listItems.get(i).getBusiness());
                }
            }
            return sum;
        }

        public int getTotalDr()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getDoctors().equalsIgnoreCase(""))
                {

                    sum += Float.parseFloat(listItems.get(i).getDoctors());
                }
            }
            return sum;
        }

        public int getTotalFare()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                Log.e("FARE >>> ", "getTotalFare: "+ listItems.get(i).getFare());

                if(!listItems.get(i).getFare().equalsIgnoreCase(""))
                {
                    sum += Float.parseFloat(listItems.get(i).getFare());

                }
            }
            return sum;
        }

        public float getTotalHQ()
        {
            float sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getHQ().equalsIgnoreCase(""))
                {
                    sum += Float.parseFloat(listItems.get(i).getHQ());

                }
            }
            return sum;
        }

        public int getTotalUD()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getUD().equalsIgnoreCase(""))
                {
                    sum += Float.parseFloat(listItems.get(i).getUD());

                }
            }
            return sum;
        }

        public int getTotalON()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                if(!listItems.get(i).getON().equalsIgnoreCase(""))
                {
                    sum += Float.parseFloat(listItems.get(i).getON());
                }
            }
            return sum;
        }

        public int getTotalSundry()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                sum += listItems.get(i).getSundry();
            }
            return sum;
        }

        public int getTotalChemist()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                sum += listItems.get(i).getChemist();
            }
            return sum;
        }

        public int getTotal()
        {
            int sum = 0;
            for(int i = 0; i < listItems.size(); i++)
            {
                sum += listItems.get(i).getTotal();
            }
            return sum;
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }


        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            @BindView(R.id.llTitle)LinearLayout llTitle;
            @BindView(R.id.tvSrNo)TextView tvSrNo;
            @BindView(R.id.tvDate)TextView tvDate;
            @BindView(R.id.tvDay)TextView tvDay;
            @BindView(R.id.tvTPArea)TextView tvTPArea;
            @BindView(R.id.tvSrc)TextView tvSrc;
            @BindView(R.id.edtSundry)EditText edtSundry;
            @BindView(R.id.edtChemist)EditText edtChemist;
            @BindView(R.id.edtRemark)EditText edtRemark;
            @BindView(R.id.tvDist)TextView tvDist;
            @BindView(R.id.tvTMode)TextView tvTMode;
            @BindView(R.id.tvFare)TextView tvFare;
            @BindView(R.id.tvHQ)TextView tvHQ;
            @BindView(R.id.tvUD)TextView tvUD;
            @BindView(R.id.tvON)TextView tvON;
            @BindView(R.id.tvTotal)TextView tvTotal;
            @BindView(R.id.tvDR)TextView tvDR;
            @BindView(R.id.tvBusi)TextView tvBusi;
            
            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);

                edtRemark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        TravellingAllowanceResponse.DaysBean getSet = listItems.get(getAdapterPosition());
                        getSet.setRemark(s.toString());
                        listItems.set(getAdapterPosition(),getSet);
                    }
                });
            }

            @Override
            public void onClick(View v)
            {
            }
        }
    }
}
