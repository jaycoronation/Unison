package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.DayGetSet;
import com.unisonpharmaceuticals.model.MonthResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.TPFormResponse;
import com.unisonpharmaceuticals.model.TourAreaResponse;
import com.unisonpharmaceuticals.model.WorkWithResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTourPlanEntry extends Fragment implements View.OnClickListener {
    private View rootView;
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llLoadingTransparent)
    LinearLayout llLoadingTransparent;
    @BindView(R.id.inputEmployee)
    TextInputLayout inputEmployee;
    @BindView(R.id.inputMonth)
    TextInputLayout inputMonth;
    @BindView(R.id.inputYear)
    TextInputLayout inputYear;

    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.edtMonth)
    EditText edtMonth;
    @BindView(R.id.edtYear)
    EditText edtYear;

    @BindView(R.id.tvCreationDate)
    TextView tvCreationDate;
    @BindView(R.id.rvTPEntry)
    RecyclerView rvTPEntry;
    @BindView(R.id.rvExtraEntry)
    RecyclerView rvExtraEntry;
    @BindView(R.id.tvExtraTitle)
    TextView tvExtraTitle;

    @BindView(R.id.llBottomSection)
    LinearLayout llBottomSection;
    @BindView(R.id.tvGenerateForm)
    TextView tvGenerateForm;
    @BindView(R.id.tvResetForm)
    TextView tvResetForm;

    public static NestedScrollView scrollView;
    public static LinearLayout llTopSection;

    @BindView(R.id.tvMainSave) TextView tvMainSave;
    @BindView(R.id.tvMainSubmit) TextView tvMainSubmit;
    @BindView(R.id.tvMainReset) TextView tvMainReset;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<WorkWithResponse.StaffBean> listWorkWith = new ArrayList<>();
    private ArrayList<WorkWithResponse.StaffBean> listWorkWithSearch = new ArrayList<>();

    private List<YearResponse.YearListBean> listYear = new ArrayList<>();
    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();
    private ArrayList<TPFormResponse.ExtradaysBean> listExtraEntry = new ArrayList<>();
    private ArrayList<TPFormResponse.DaysBean> listTpEntry = new ArrayList<>();

    private ArrayList<DayGetSet> listDatesNo = new ArrayList<>();

    private List<TourAreaResponse.TourAreaBean> listArea = new ArrayList<>();
    private List<TourAreaResponse.TourAreaBean> listAreaSearch = new ArrayList<>();

    public TPEntryAdapter entryAdapter;
    public ExtraEntryAdapter extraEntryAdapter;

    private String workWithString = "",selectedStaffId  = "",monthNumber = "",currentYear = "",selectedStaffIdForSubmitReport="";

    private boolean isFormLoadFirstTime = false;

    private final String EMPLOYEE = "Employee";
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private final String AREA = "Area";
    private final String WW = "Work With";
    private final String EXTRA_DATE = "Extra Date";
    private final String EXTRA_AREA = "Extra Area";
    private final String EXTRA_WW = "Extra Work With";

    private boolean isLoading = false;

    private long mLastClickTime = 0;

    private String TPStatus = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tourplan_entry, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(this, rootView);
        initViews();
        onClick(rootView);
        if(sessionManager.isNetworkAvailable())
        {
            getEmployeeList();
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
            activity.finish();
        }
        return rootView;
    }

    private void getEmployeeList()
    {
        isLoading = true;
        llLoadingTransparent.setVisibility(View.VISIBLE);
        listEmployee = new ArrayList<>();
        Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
        empCall.enqueue(new Callback<StaffResponse>()
        {
            @Override
            public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response)
            {
                try {
                    if(response.body().getSuccess()==1)
                    {
                        listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();

                        Log.e("<><> ListEmployee",String.valueOf(listEmployee.size()));

                        if(listEmployee.size()==1)
                        {
                            selectedStaffId = listEmployee.get(0).getStaff_id();
                            selectedStaffIdForSubmitReport = listEmployee.get(0).getStaff_id();
                            edtEmployee.setText(listEmployee.get(0).getName());
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Could not get employee list.");
                        activity.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t)
            {
                AppUtils.showToast(activity, "Could not get employee list.");
                activity.finish();
                isLoading = false;
            }
        });

        isLoading = true;
        Call<YearResponse> yearCall = apiService.getLastYearList("",sessionManager.getUserId());
        yearCall.enqueue(new Callback<YearResponse>()
        {
            @Override
            public void onResponse(Call<YearResponse> call, Response<YearResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    currentYear = response.body().getCurrent_year();
                    listYear = response.body().getYearList();
                    if(listYear.size()==1)
                    {
                        edtYear.setText(String.valueOf(listYear.get(0).getYear()));
                        getMonthData();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,"No year data found");
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No year data found");
                isLoading = false;
            }
        });

      /*  isLoading = true;
        Call<TourAreaResponse> areaCall = apiService.getTPArea(sessionManager.getUserId(),selectedStaffIdForSubmitReport);
        areaCall.enqueue(new Callback<TourAreaResponse>()
        {
            @Override
            public void onResponse(Call<TourAreaResponse> call, Response<TourAreaResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listArea = response.body().getTour_area();
                }
                else
                {
                    AppUtils.showToast(activity,"No data found");
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No data found");
                isLoading = false;
            }
        });*/

       /* isLoading = true;
        Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(),sessionManager.getUserId(),"false");
        workWithCall.enqueue(new Callback<WorkWithResponse>()
        {
            @Override
            public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                try {
                    if (response.body().getSuccess() == 1) {
                        listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                isLoading = false;
            }
        });*/

        llLoadingTransparent.setVisibility(View.GONE);
    }

    private void initViews()
    {
        /*if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
        {
            tvMainSubmit.setText("Approve");
        }
        else
        {
            tvMainSubmit.setText("Submit");
        }*/

        llTopSection = (LinearLayout) rootView.findViewById(R.id.llTopSection);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);

        edtMonth.setOnClickListener(this);
        edtEmployee.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        tvResetForm.setOnClickListener(this);
        tvGenerateForm.setOnClickListener(this);
        llLoadingTransparent.setOnClickListener(this);

        tvMainReset.setOnClickListener(this);
        tvMainSave.setOnClickListener(this);
        tvMainSubmit.setOnClickListener(this);

        //rvTPEntry.setLayoutManager(new LinearLayoutManager(activity));
        rvTPEntry.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        //rvExtraEntry.setLayoutManager(new LinearLayoutManager(activity));
        rvExtraEntry.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onClick(View v)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId()){
            case R.id.edtEmployee:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(EMPLOYEE,new EditText(activity),0);
                }
                break;
            case R.id.edtMonth:
                if(edtYear.getText().toString().equals(""))
                {
                    AppUtils.showToast(activity,"Please select year");
                }
                else
                {
                    if(isLoading)
                    {
                        AppUtils.showLoadingToast(activity);
                    }
                    else
                    {
                        showListDialog(MONTH,new EditText(activity),0);
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
                    showListDialog(YEAR,new EditText(activity),0);
                }
                break;
            case R.id.tvResetForm:
                edtEmployee.setText("");
                edtYear.setText("");
                edtMonth.setText("");
                monthNumber = "";
                llBottomSection.setVisibility(View.GONE);
                break;
            case R.id.tvGenerateForm:
                if(edtEmployee.getText().toString().equals(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(edtYear.getText().toString().trim().length()==0)
                {
                    AppUtils.showToast(activity,"Please select year");
                }
                else if(edtMonth.getText().toString().trim().length() == 0)
                {
                    AppUtils.showToast(activity,"Please select month");
                }
                else
                {
                    isFormLoadFirstTime = true;
                    getFormData(edtYear.getText().toString().trim(),monthNumber);
                }
                break;
            case R.id.tvMainSave:
                if(sessionManager.isNetworkAvailable())
                {
                    AppUtils.hideKeyboard(edtYear,activity);
                    passJsonToSaveEntry();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvMainSubmit:
                if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        if(edtEmployee.getText().toString().equals(""))
                        {
                            AppUtils.showToast(activity,"Please select employee");
                        }
                        else if(edtYear.getText().toString().trim().length()==0)
                        {
                            AppUtils.showToast(activity,"Please select year");
                        }
                        else if(edtMonth.getText().toString().trim().length() == 0)
                        {
                            AppUtils.showToast(activity,"Please select month");
                        }
                        else
                        {
                            if(selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()))
                            {
                                if(entryAdapter!=null && entryAdapter.isFormFill())
                                {
                                    showConfirmDialog();
                                }
                                else
                                {
                                    AppUtils.showToast(activity,"Please fill TP with all dates.");
                                }

                            }
                            else
                            {
                                if(TPStatus.equals("1"))
                                {
                                    if(entryAdapter!=null && entryAdapter.isFormFill())
                                    {
                                        approveTP();
                                    }
                                    else
                                    {
                                        AppUtils.showToast(activity,"Please fill TP with all dates.");
                                    }
                                }
                                else if(TPStatus.equals("2"))
                                {
                                    AppUtils.showToast(activity,"TP is already approved");
                                }
                                else
                                {
                                    AppUtils.showToast(activity,"TP is not confirmed");
                                }
                            }
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
                else
                {
                    if(entryAdapter!=null && entryAdapter.isFormFill())
                    {
                        showConfirmDialog();
                    }
                    else
                    {
                        AppUtils.showToast(activity,"Please fill TP with all dates.");
                    }
                }
                break;
            case R.id.tvMainReset:
                try
                {
                    if(listTpEntry.size()>0)
                    {
                        for (int i = 0; i < listTpEntry.size(); i++)
                        {
                            TPFormResponse.DaysBean bean = listTpEntry.get(i);
                            bean.setWork_withString("");
                            bean.setWork_with(new ArrayList<TPFormResponse.ExtradaysBean.WorkWithBean>());
                            bean.setWork_withName("");
                            bean.setArea_name("");
                            bean.setRoute_area_id("");
                            bean.setArea("");
                            bean.setRemark("");
                            bean.setLeave("false");
                            listTpEntry.set(i,bean);
                        }
                    }
                    if(listExtraEntry.size()>0)
                    {
                        for (int i = 0; i < listExtraEntry.size(); i++)
                        {
                            TPFormResponse.ExtradaysBean bean = listExtraEntry.get(i);
                            bean.setWork_withString("");
                            bean.setWork_with(new ArrayList<TPFormResponse.ExtradaysBean.WorkWithBean>());
                            bean.setWork_withName("");
                            bean.setArea_name("");
                            bean.setRoute_area_id("");
                            bean.setArea("");
                            bean.setRemark("");
                            bean.setDay_name("");
                            listExtraEntry.set(i,bean);
                        }
                    }

                    entryAdapter.notifyDataSetChanged();
                    extraEntryAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.llLoadingTransparent:
                break;
        }
    }

    private void approveTP()
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveTourPlan(monthNumber,edtYear.getText().toString().trim(),selectedStaffId,sessionManager.getUserId(),sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                AppUtils.showToast(activity,response.body().getMessage());
                llLoadingTransparent.setVisibility(View.GONE);
                llBottomSection.setVisibility(View.GONE);
                llTopSection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private void getMonthData()
    {
        isLoading = true;
        llLoadingTransparent.setVisibility(View.VISIBLE);
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

                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No month data found");
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }


    private void saveTourPlan(final String employee,
                              final String partner_id,
                              final String year,
                              final String month,
                              final String areaId,
                              final String remark,
                              final String leave,
                              final String holiday,
                              final String staff_id)
    {
        new AsyncTask<Void,Void,Void>()
        {
            int success = 0;
            String message = "";
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoadingTransparent.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("employee",employee);
                    hashMap.put("partner_id",partner_id);
                    hashMap.put("year",year);
                    hashMap.put("month",month);
                    hashMap.put("isMobile","true");
                    hashMap.put("area_id",areaId);
                    hashMap.put("remark",remark);
                    hashMap.put("leave",leave);
                    hashMap.put("holiday",holiday);
                    hashMap.put("staff_id",staff_id);
                    hashMap.put("login_user_id",sessionManager.getUserId());
                    hashMap.put("is_app","true");

                    Log.e("Request", "doInBackground: " + hashMap.toString() );

                    String response = "";

                    response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SAVE_TOURPLAN,hashMap);

                    Log.e("Response >>> ", "doInBackground: " + response );

                    JSONObject jsonObject = new JSONObject(response);
                    success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                    message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoadingTransparent.setVisibility(View.GONE);
                AppUtils.showToast(activity,message);
                if(success==1)
                {
                    passJsonToSaveExtraEntry();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
    }

    private void saveExtraTourPlan(final String employee,
                              final String year,
                              final String month,
                              final String areaId,
                              final String remark,
                              final String staff_id,
                                   final String dates,
                                   final String dayId)
    {
        new AsyncTask<Void,Void,Void>()
        {
            int success = 0;
            String message = "";
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoadingTransparent.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("employee",employee);
                    hashMap.put("year",year);
                    hashMap.put("month",month);
                    hashMap.put("isMobile","true");
                    hashMap.put("area_id",areaId);
                    hashMap.put("remark",remark);
                    hashMap.put("staff_id",staff_id);
                    hashMap.put("date",dates);
                    hashMap.put("day_id",dayId);
                    hashMap.put("login_user_id",sessionManager.getUserId());
                    hashMap.put("is_app","true");

                    Log.e("EXTRA Request", "doInBackground: " + hashMap.toString() );

                    String response = "";

                    response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SAVE_EXTRA_TOURPLAN,hashMap);

                    Log.e("EXTRA Response >>> ", "doInBackground: " + response );

                    JSONObject jsonObject = new JSONObject(response);
                    success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                    message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoadingTransparent.setVisibility(View.GONE);
                AppUtils.showToast(activity,message);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
    }

    private void confirmTourPlan(final String employee,
                                   final String year,
                                   final String month)
    {
        new AsyncTask<Void,Void,Void>()
        {
            int success = 0;
            String message = "";
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoadingTransparent.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("employee",employee);
                    hashMap.put("year",year);
                    hashMap.put("month",month);
                    hashMap.put("isMobile","true");
                    hashMap.put("login_user_id",sessionManager.getUserId());

                    Log.e("CONFIRM Request", "doInBackground: " + hashMap.toString() );

                    String response = "";

                    response = MitsUtils.readJSONServiceUsingPOST(ApiClient.CONFIRM_TOURPLAN,hashMap);

                    Log.e("CONFIRM Response >>> ", "doInBackground: " + response );

                    JSONObject jsonObject = new JSONObject(response);
                    success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                    message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoadingTransparent.setVisibility(View.GONE);
                AppUtils.showToast(activity,message);
                if(success ==1)
                {
                    llTopSection.setVisibility(View.VISIBLE);
                    llBottomSection.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
    }

    private void passJsonToSaveEntry()
    {
        try
        {
            JSONObject mainObject = new JSONObject();
            mainObject.put("employee",selectedStaffIdForSubmitReport);
            mainObject.put("partner_id","");
            mainObject.put("year",edtYear.getText().toString());// change this
            mainObject.put("month",monthNumber);// change this

            {
                JSONObject areaObj = new JSONObject();
                for (int i = 0; i < listTpEntry.size(); i++)
                {
                    try
                    {
                        String areaStr = "";
                        if(!listTpEntry.get(i).getHoliday().equalsIgnoreCase("true"))
                        {
                            areaStr = listTpEntry.get(i).getArea();
                        }
                        areaObj.put(String.valueOf(listTpEntry.get(i).getDate()),areaStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("area_id",areaObj);
            }

            {
                JSONObject remarkObj = new JSONObject();
                for (int i = 0; i < listTpEntry.size(); i++)
                {
                    remarkObj.put(String.valueOf(listTpEntry.get(i).getDate()),listTpEntry.get(i).getRemark());
                }

                mainObject.put("remark",remarkObj);
            }

            {
                JSONArray holidayArray = new JSONArray();
                for (int i = 0; i < listTpEntry.size(); i++)
                {
                    try {
                        if(listTpEntry.get(i).getHoliday().equals("true"))
                        {
                            holidayArray.put(String.valueOf(listTpEntry.get(i).getDate()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("holiday",holidayArray);
            }

            {
                JSONArray leaveArray = new JSONArray();
                for (int i = 0; i < listTpEntry.size(); i++)
                {
                    try {
                        if(listTpEntry.get(i).getLeave().equals("true"))
                        {
                            leaveArray.put(String.valueOf(listTpEntry.get(i).getDate()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("leave",leaveArray);
            }

            {
                JSONObject staffObject = new JSONObject();
                for (int i = 0; i < listTpEntry.size(); i++)
                {
                    try {
                        if(listTpEntry.get(i).getWork_withString().length()>0)
                        {
                            JSONArray wwArray = new JSONArray();
                            JSONObject obj = new JSONObject();

                            List<String> listWW = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(i).getWork_withString());
                            for (int j = 0; j < listWW.size(); j++)
                            {
                                wwArray.put(listWW.get(j));
                            }

                            staffObject.put(String.valueOf(listTpEntry.get(i).getDate()),wwArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mainObject.put("staff_id",staffObject);
            }

            saveTourPlan(selectedStaffIdForSubmitReport,
                    "",
                    edtYear.getText().toString(),
                    monthNumber,
                    mainObject.getString("area_id").toString(),
                    mainObject.getString("remark").toString(),
                    mainObject.getJSONArray("leave").toString(),
                    mainObject.getJSONArray("holiday").toString(),
                    mainObject.getString("staff_id").toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void passJsonToSaveExtraEntry()
    {
        try
        {
            JSONObject mainObject = new JSONObject();
            mainObject.put("employee",selectedStaffIdForSubmitReport);
            mainObject.put("year",edtYear.getText().toString());// change this
            mainObject.put("month",monthNumber);// change this

            {
                JSONArray areaObj = new JSONArray();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    try
                    {
                        areaObj.put(listExtraEntry.get(i).getArea());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("area_id",areaObj);
            }

            {
                JSONArray idObject = new JSONArray();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    try
                    {
                        idObject.put(listExtraEntry.get(i).getDay_id());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("day_id",idObject);
            }

            {
                JSONArray areaArray = new JSONArray();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    try
                    {
                        areaArray.put(String.valueOf(listExtraEntry.get(i).getDate()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("date",areaArray);
            }

            {
                JSONArray remarkObj = new JSONArray();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    remarkObj.put(listExtraEntry.get(i).getRemark());
                }

                mainObject.put("remark",remarkObj);
            }

            {
                JSONObject staffObject = new JSONObject();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    try {
                        if(listExtraEntry.get(i).getWork_withString().length()>0)
                        {
                            JSONArray wwArray = new JSONArray();
                            JSONObject obj = new JSONObject();

                            List<String> listWW = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(i).getWork_withString());
                            for (int j = 0; j < listWW.size(); j++)
                            {
                                wwArray.put(listWW.get(j));
                            }

                            staffObject.put(String.valueOf(i),wwArray);
                            //staffObject.put(listExtraEntry.get(i).getDate(),wwArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mainObject.put("staff_id",staffObject);
            }

            saveExtraTourPlan(selectedStaffIdForSubmitReport,
                    edtYear.getText().toString(),
                    monthNumber,
                    mainObject.getJSONArray("area_id").toString(),
                    mainObject.getJSONArray("remark").toString(),
                    mainObject.getString("staff_id").toString(),
                    mainObject.getJSONArray("date").toString(),
                    mainObject.getJSONArray("day_id").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getFormData(final String year, final String month)
    {

        isLoading = true;
        Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(selectedStaffIdForSubmitReport,sessionManager.getUserId(),"false");
        workWithCall.enqueue(new Callback<WorkWithResponse>()
        {
            @Override
            public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                try {
                    if (response.body().getSuccess() == 1) {
                        listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                isLoading = false;
            }
        });

        isLoading = true;
        Call<TourAreaResponse> tpAreaCall = apiService.getTPArea(selectedStaffIdForSubmitReport,sessionManager.getUserId());
        tpAreaCall.enqueue(new Callback<TourAreaResponse>()
        {
            @Override
            public void onResponse(Call<TourAreaResponse> call, Response<TourAreaResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listArea = response.body().getTour_area();
                }
                else
                {
                    AppUtils.showToast(activity,"No data found");
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No data found");
                isLoading = false;
            }
        });

        Call<TPFormResponse> areaCall = apiService.getTPForm(selectedStaffId,month,"",year,sessionManager.getUserId());
        llLoadingTransparent.setVisibility(View.VISIBLE);
        areaCall.enqueue(new Callback<TPFormResponse>()
        {
            @Override
            public void onResponse(Call<TPFormResponse> call, Response<TPFormResponse> response)
            {

                if(response.body().getSuccess()==1)
                {
                    llTopSection.setVisibility(View.GONE);

                    TPStatus = response.body().getStatus();

                    listTpEntry = (ArrayList<TPFormResponse.DaysBean>) response.body().getDays();
                    listExtraEntry = (ArrayList<TPFormResponse.ExtradaysBean>) response.body().getExtradays();

                    if(listTpEntry.size()>0)
                    {
                        listDatesNo = new ArrayList<>();
                        for (int i = 0; i < listTpEntry.size(); i++)
                        {
                            if(!listTpEntry.get(i).getHoliday().equals("true"))
                            {
                                DayGetSet dayGetSet = new DayGetSet();
                                dayGetSet.setDate(""+(i+1));
                                dayGetSet.setDay(listTpEntry.get(i).getDay_name());
                                dayGetSet.setTimeStamp(String.valueOf(listTpEntry.get(i).getDate()));
                                listDatesNo.add(dayGetSet);
                            }

                            TPFormResponse.DaysBean getSet = listTpEntry.get(i);
                            if(getSet.getWork_with().size()>0)
                            {
                                StringBuffer name = new StringBuffer();
                                StringBuffer ids = new StringBuffer();
                                for (int j = 0; j < getSet.getWork_with().size(); j++)
                                {
                                    name.append(getSet.getWork_with().get(j).getStaff()).append(",");
                                    ids.append(getSet.getWork_with().get(j).getStaff_id()).append(",");
                                }
                                name.deleteCharAt(name.lastIndexOf(","));
                                ids.deleteCharAt(ids.lastIndexOf(","));

                                getSet.setWork_withString(ids.toString());
                                getSet.setWork_withName(name.toString());
                            }

                            listTpEntry.set(i,getSet);

                        }

                        entryAdapter = new TPEntryAdapter(listTpEntry);
                        rvTPEntry.setAdapter(entryAdapter);
                    }

                    if(listExtraEntry.size()>0)
                    {
                        for (int i = 0; i < listExtraEntry.size(); i++)
                        {
                            TPFormResponse.ExtradaysBean getSet = listExtraEntry.get(i);
                            if(getSet.getWork_with().size()>0)
                            {
                               /* String names = "",ids = "";
                                for (int j = 0; j < getSet.getWork_with().size(); j++)
                                {
                                    if(names.equals(""))
                                    {
                                        names = getSet.getWork_with().get(j).getStaff();
                                    }
                                    else
                                    {
                                        names = names + ","+getSet.getWork_with().get(j).getStaff();
                                    }

                                    if(ids.equals(""))
                                    {
                                        ids = getSet.getWork_with().get(j).getStaff_id();
                                    }
                                    else
                                    {
                                        ids = ids +  ","+getSet.getWork_with().get(j).getStaff_id();
                                    }
                                }
                                getSet.setWork_withString(ids);
                                getSet.setWork_withName(names);*/
                                StringBuffer name = new StringBuffer();
                                StringBuffer ids = new StringBuffer();
                                for (int j = 0; j < getSet.getWork_with().size(); j++)
                                {
                                    name.append(getSet.getWork_with().get(j).getStaff()).append(",");
                                    ids.append(getSet.getWork_with().get(j).getStaff_id()).append(",");
                                }
                                name.deleteCharAt(name.lastIndexOf(","));
                                ids.deleteCharAt(ids.lastIndexOf(","));



                                getSet.setWork_withString(ids.toString());
                                getSet.setWork_withName(name.toString());

                            }

                            listExtraEntry.set(i,getSet);
                        }

                        extraEntryAdapter  = new ExtraEntryAdapter(listExtraEntry);
                        rvExtraEntry.setAdapter(extraEntryAdapter);
                    }

                    llBottomSection.setVisibility(View.VISIBLE);
                    llLoadingTransparent.setVisibility(View.GONE);

                    if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                    {
                        if(selectedStaffId.equalsIgnoreCase(sessionManager.getUserId()))
                        {
                            tvMainSubmit.setText("Submit");
                        }
                        else
                        {
                            tvMainSubmit.setText("Approve");
                        }
                    }
                    else
                    {
                        tvMainSubmit.setText("Submit");
                    }
                }
                else
                {
                    llLoadingTransparent.setVisibility(View.GONE);
                    llBottomSection.setVisibility(View.GONE);
                    AppUtils.showToast(activity,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<TPFormResponse> call, Throwable t)
            {
                Log.e("Exception > ", "onFailure: " + t.getMessage() );
                AppUtils.showToast(activity,"No data found");
                llLoadingTransparent.setVisibility(View.GONE);
                llBottomSection.setVisibility(View.GONE);
            }
        });


    }

    private void showConfirmDialog()
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtConfirmation = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Submit Form");

            txtConfirmation.setText("Are you sure? Once you confirm your Tour Plan, you will not be able to change it later?");

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
                        confirmTourPlan(selectedStaffId,
                                edtYear.getText().toString().trim(),
                                monthNumber);
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

    DialogListAdapter listDialogAdapter;
    public void showListDialog(final String isFor, final EditText editText, final int listPostion)
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
                AppUtils.hideKeyboard(sheetView,activity);
                disSelectAllTheStaff();
            }
        });
        LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select "+isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        if(isFor.equalsIgnoreCase(WW) || isFor.equalsIgnoreCase(EXTRA_WW))
        {
            tvDone.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.GONE);
            listDialog.findViewById(R.id.ivBack).setVisibility(View.GONE);
        }
        else
        {
            tvDone.setVisibility(View.GONE);
        }

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        listDialogAdapter = new DialogListAdapter(listDialog, isFor,false,"",editText,listPostion);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(listDialogAdapter);

        tvDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isFor.equalsIgnoreCase(WW)&&listDialogAdapter !=null)
                {
                    workWithString = listDialogAdapter.getSelectedWorkWitIds();
                    entryAdapter.updateListWW(listPostion,listDialogAdapter.getSelectedWorkWitIds(),listDialogAdapter.getSelectedWorkWithName());
                    listDialog.dismiss();
                    listDialog.cancel();










                    /*if(workWithString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one option.");
                    }
                    else
                    {
                        //editText.setText(listDialogAdapter.getSelectedDrNames());
                        entryAdapter.updateListWW(listPostion,listDialogAdapter.getSelectedWorkWitIds(),listDialogAdapter.getSelectedWorkWithName());
                        listDialog.dismiss();
                        listDialog.cancel();
                    }*/
                }
                else if(isFor.equalsIgnoreCase(EXTRA_WW)&&listDialogAdapter !=null)
                {
                    extraEntryAdapter.updateListWW(listPostion,listDialogAdapter.getSelectedWorkWitIds(),listDialogAdapter.getSelectedWorkWithName());
                    listDialog.dismiss();
                    listDialog.cancel();

                    disSelectAllTheStaff();


                    /*String extraWorkWithString = "";
                    extraWorkWithString = listDialogAdapter.getSelectedWorkWitIds();
                    if(extraWorkWithString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one option.");
                    }
                    else
                    {
                        //editText.setText(listDialogAdapter.getSelectedDrNames());

                        Log.e("*******  POSITION ", "onClick: "+ listPostion);
                        Log.e("*******  ID ", "onClick: "+ listDialogAdapter.getSelectedWorkWitIds());
                        Log.e("*******  NAME ", "onClick: "+listDialogAdapter.getSelectedWorkWithName() );

                        extraEntryAdapter.updateListWW(listPostion,listDialogAdapter.getSelectedWorkWitIds(),listDialogAdapter.getSelectedWorkWithName());
                        listDialog.dismiss();
                        listDialog.cancel();

                        disSelectAllTheStaff();
                    }*/
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
        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

        if(isFor.equalsIgnoreCase(MONTH) || isFor.equalsIgnoreCase(YEAR))
        {
            inputSearch.setVisibility(View.GONE);
        }
        else
        {
            inputSearch.setVisibility(View.VISIBLE);
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
                else if(isFor.equals(AREA) || isFor.equals(EXTRA_AREA))
                {
                    listAreaSearch.clear();
                    for (int i = 0; i < listArea.size(); i++)
                    {
                        if (textlength <= listArea.get(i).getArea_name().length())
                        {
                            if (listArea.get(i).getArea_name().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listAreaSearch.add(listArea.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(WW))
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
                AppendListForArea(listDialog,isFor,true,rvListDialog,editText,listPostion);
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

    private void disSelectAllTheStaff()
    {
        for (int i = 0; i < listWorkWith.size(); i++)
        {
            WorkWithResponse.StaffBean bean = listWorkWith.get(i);
            bean.setSelected(false);
            listWorkWith.set(i,bean);
        }
    }


    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";
        EditText editText;
        int listPosition = 0;

        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,EditText editText)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.editText  = editText;
        }
        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,EditText editText,int listPosition)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.editText = editText;
            this.listPosition = listPosition;
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
                        selectedStaffIdForSubmitReport = getSet.getStaff_id();
                        dialog.dismiss();
                        dialog.cancel();

                        edtYear.setText("");
                        edtMonth.setText("");
                        currentYear="";
                        monthNumber="";

                        llBottomSection.setVisibility(View.GONE);
                        listTpEntry.clear();
                        listExtraEntry.clear();
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
                        edtMonth.setText(listMonth.get(position).getMonth());
                        dialog.dismiss();
                        dialog.cancel();
                        monthNumber =listMonth.get(position).getNumber();
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
                        edtYear.setText(String.valueOf(listYear.get(position).getYear()));
                        edtMonth.setText("");

                        dialog.dismiss();
                        dialog.cancel();

                        getMonthData();
                    }
                });
            }
            else if(isFor.equals(AREA) || isFor.equals(EXTRA_AREA))
            {
                holder.cb.setVisibility(View.GONE);
                final TourAreaResponse.TourAreaBean getSet;
                if(isForSearch)
                {
                    getSet = listAreaSearch.get(position);
                }
                else
                {
                    getSet = listArea.get(position);
                }
                holder.tvValue.setText(getSet.getArea_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        editText.setText(getSet.getArea_name());
                        if(isFor.equals(AREA))
                        {
                            entryAdapter.updateListArea(listPosition,getSet);
                        }
                        else if(isFor.equals(EXTRA_AREA))
                        {
                            extraEntryAdapter.updateListArea(listPosition, getSet);
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(WW) || isFor.equalsIgnoreCase(EXTRA_WW))
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

                //Extra
                if(isFor.equalsIgnoreCase(WW))
                {
                    /*if(listTpEntry.size()>0)
                    {
                        try {
                            if(!listTpEntry.get(listPosition).getWork_withString().equals(""))
                            {
                                List<String> list = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(listPosition).getWork_withString());
                                if(list.size()>0)
                                {
                                    for (int i = 0; i < list.size(); i++)
                                    {
                                        try {
                                            if(list.get(i).equalsIgnoreCase(getSet.getStaff_id()))
                                            {
                                                getSet.setSelected(true);
                                                holder.cb.setChecked(true);
                                            }
                                            else
                                            {
                                                getSet.setSelected(false);
                                                //holder.cb.setChecked(false);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }*/
                }
                else if(isFor.equalsIgnoreCase(EXTRA_WW))
                {
                   /* try {
                        if(listExtraEntry.size()>0)
                        {
                            if(!listExtraEntry.get(listPosition).getWork_withString().equals(""))
                            {
                                List<String> list = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(listPosition).getWork_withString());
                                if(list.size()>0)
                                {
                                    for (int i = 0; i < list.size(); i++)
                                    {
                                        try {
                                            if(list.get(i).equalsIgnoreCase(getSet.getStaff_id()))
                                            {
                                                getSet.setSelected(true);
                                                holder.cb.setChecked(true);
                                            }
                                            else
                                            {
                                                getSet.setSelected(false);
                                                holder.cb.setChecked(false);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }

                if(getSet.isSelected())
                {
                    holder.cb.setChecked(true);
                }
                else
                {
                    holder.cb.setChecked(false);
                }



                holder.tvValue.setVisibility(View.GONE);

                holder.cb.setText(getSet.getName() + " ("+getSet.getDesignation()+")");
                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));

                holder.cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(getSet.isSelected())
                        {
                            getSet.setSelected(false);
                        }
                        else
                        {
                            getSet.setSelected(true);
                        }
                        if(isForSearch)
                        {
                            listWorkWithSearch.set(position,getSet);
                        }
                        else
                        {
                            listWorkWith.set(position,getSet);
                        }
                        //listWorkWith.set(position,getSet);
                        //notifyDataSetChanged();
                    }
                });

                /*holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                });*/

            }
            else if(isFor.equalsIgnoreCase(EXTRA_DATE))
            {
                holder.cb.setVisibility(View.GONE);
                final DayGetSet getSet = listDatesNo.get(position);
                holder.tvValue.setText(getSet.getDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //editText.setText(getSet);
                        if(extraEntryAdapter!=null)
                        {
                            extraEntryAdapter.updateListDates(listPosition,getSet);
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }

        }

        public void setCheckedIds(String isFor,int pos)
        {
            List<String> list = new ArrayList<>();
            if(isFor.equals("regular"))
            {
                list = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(pos).getWork_withString());
            }
            else
            {
                list = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(pos).getWork_withString());
            }

            Log.e("##############", "setCheckedIds: "+list.size() );

            if(isForSearch)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    for (int j = 0; j < listWorkWithSearch.size(); j++)
                    {
                        if(list.get(i).equalsIgnoreCase(listWorkWithSearch.get(j).getStaff_id()))
                        {
                            WorkWithResponse.StaffBean bean = listWorkWithSearch.get(j);
                            bean.setSelected(true);
                            listWorkWithSearch.set(j,bean);
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < list.size(); i++)
                {
                    for (int j = 0; j < listWorkWith.size(); j++)
                    {
                        if(list.get(i).equalsIgnoreCase(listWorkWith.get(j).getStaff_id()))
                        {
                            WorkWithResponse.StaffBean bean = listWorkWith.get(j);
                            bean.setSelected(true);
                            listWorkWith.set(j,bean);
                        }
                    }
                }
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
            else if(isFor.equalsIgnoreCase(AREA))
            {
                if(isForSearch)
                {
                    return listAreaSearch.size();
                }
                else
                {
                    return listArea.size();
                }
            }
            else if(isFor.equalsIgnoreCase(WW))
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
            else if(isFor.equalsIgnoreCase(EXTRA_DATE))
            {
                return listDatesNo.size();
            }
            else if(isFor.equalsIgnoreCase(EXTRA_WW))
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
            else if(isFor.equalsIgnoreCase(EXTRA_AREA))
            {
                if(isForSearch)
                {
                    return listAreaSearch.size();
                }
                else
                {
                    return listArea.size();
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

    private void AppendListForArea(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea,EditText editText, int listPosition)
    {
        listDialogAdapter = new DialogListAdapter(dialog,isFor,true,"",editText,listPosition);
        rvArea.setAdapter(listDialogAdapter);
        listDialogAdapter.notifyDataSetChanged();
    }

    public class TPEntryAdapter extends RecyclerView.Adapter<TPEntryAdapter.ViewHolder>
    {

        List<TPFormResponse.DaysBean> listItems;
        boolean isDone = false;


        TPEntryAdapter(List<TPFormResponse.DaysBean> list) {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_tp_table, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final TPFormResponse.DaysBean getSet = listItems.get(position);
            int newPos = position + 1;
            holder.tvDate.setText(newPos+"  "+getSet.getDay_name());

            if(position==0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }

            if(getSet.getLeave().equals("false") && getSet.getHoliday().equals("false"))
            {

                holder.edtTPArea.setEnabled(true);
                holder.edtTPWW.setEnabled(true);
                holder.edtRemark.setEnabled(true);
                holder.edtTPArea.setFocusable(true);
                holder.edtTPWW.setFocusable(true);
                holder.edtRemark.setFocusable(true);

                holder.edtTPArea.setText(getSet.getArea_name());
                String nameToDisplay = getSet.getWork_withName();
                if(nameToDisplay.endsWith(","))
                {
                    nameToDisplay = nameToDisplay.substring(0,nameToDisplay.length() - 1);
                }
                holder.edtTPWW.setText(nameToDisplay);
                holder.edtRemark.setText(getSet.getRemark());
                holder.edtRemark.setSelection(getSet.getRemark().length());
            }
            else
            {

                holder.edtTPArea.setEnabled(false);
                holder.edtTPWW.setEnabled(false);
                holder.edtTPArea.setFocusable(false);
                holder.edtTPWW.setFocusable(false);

                holder.edtTPArea.setText("");
                holder.edtTPWW.setText("");

                holder.edtRemark.setText(getSet.getRemark());
                holder.edtRemark.setSelection(getSet.getRemark().length());
            }

            if(getSet.getDay_type()==3 && getSet.getDay_name().equalsIgnoreCase("Sun"))
            {
                holder.card.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_sunday));
                //holder.edtTPArea.setVisibility(View.INVISIBLE);
                //holder.edtTPWW.setVisibility(View.INVISIBLE);
                holder.cbLeave.setEnabled(false);
                holder.cbHoliday.setEnabled(false);
            }
            else if(getSet.getDay_type()==3)
            {
                holder.card.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_holiday));
                //holder.edtTPArea.setVisibility(View.INVISIBLE);
                //holder.edtTPWW.setVisibility(View.INVISIBLE);
                holder.cbLeave.setEnabled(true);
                holder.cbHoliday.setEnabled(true);
            }
            else if(getSet.getDay_type()==2)
            {
                holder.card.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_leave));
            }
            else if(getSet.getDay_type()==1)
            {
                holder.card.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_extra_tp_area));
            }
            else
            {
                holder.card.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
                holder.edtTPArea.setVisibility(View.VISIBLE);
                holder.edtTPWW.setVisibility(View.VISIBLE);
                holder.cbLeave.setEnabled(true);
                holder.cbHoliday.setEnabled(true);
            }

            if(getSet.getNo_data().equalsIgnoreCase("true"))
            {
                holder.cbLeave.setEnabled(false);
                holder.cbHoliday.setEnabled(false);
            }
            else
            {
                holder.cbLeave.setEnabled(true);
                holder.cbHoliday.setEnabled(true);
            }


            isDone = true;
            holder.cbLeave.setChecked(Boolean.valueOf(getSet.getLeave()));
            holder.cbHoliday.setChecked(Boolean.valueOf(getSet.getHoliday()));
            holder.cbLeave.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(!isDone)
                {
                    if(isChecked)
                    {
                        getSet.setLeave("true");
                        getSet.setArea("");
                        getSet.setArea_name("");
                        getSet.setDay_type(2);
                        getSet.setWork_withString("");
                        getSet.setWork_with(new ArrayList<  >());
                        getSet.setWork_withName("");
                        getSet.setRemark("");
                    }
                    else
                    {
                        getSet.setLeave("false");

                        if(getSet.getDay_type()!=0)
                        {
                            getSet.setDay_type(0);
                        }
                    }

                    listTpEntry.set(position,getSet);
                    notifyItemChanged(position);
                }

            });

            holder.cbHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(!isDone)
                    {
                        if(isChecked)
                        {
                            getSet.setHoliday("true");
                            getSet.setArea("");
                            getSet.setArea_name("");
                            getSet.setDay_type(3);
                            getSet.setWork_withString("");
                            getSet.setWork_with(new ArrayList<TPFormResponse.ExtradaysBean.WorkWithBean>());
                            getSet.setWork_withName("");
                            getSet.setRemark("");
                        }
                        else
                        {
                            getSet.setHoliday("false");

                            if(getSet.getDay_type()!=0)
                            {
                                getSet.setDay_type(0);
                            }
                        }

                        listTpEntry.set(position,getSet);
                        notifyItemChanged(position);
                    }

                }
            });

            isDone = false;
        }

        private boolean isFormFill()
        {
            boolean b = true;

            for (int i = 0; i < listTpEntry.size(); i++)
            {

                if(!listItems.get(i).getHoliday().equalsIgnoreCase("true") &&
                        !listItems.get(i).getLeave().equalsIgnoreCase("true"))
                {
                    if(listItems.get(i).getArea_name().equals(""))
                    {
                        b = false;
                    }
                }

            }

            return b;
        }

        public void updateListArea(int position,TourAreaResponse.TourAreaBean bean)
        {
            TPFormResponse.DaysBean newBean = listItems.get(position);
            newBean.setArea_name(bean.getArea_name());
            newBean.setArea(bean.getArea_id());
            listItems.set(position,newBean);
            notifyItemChanged(position);
        }
        public void updateListWW(int position,String wwString,String wwName)
        {
            TPFormResponse.DaysBean newBean = listItems.get(position);
            newBean.setWork_withString(wwString);
            newBean.setWork_withName(wwName);
            listItems.set(position,newBean);
            notifyItemChanged(position);
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }


        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.edtTPArea)
            EditText edtTPArea;

            @BindView(R.id.edtTPWW)
            EditText edtTPWW;

            @BindView(R.id.edtRemark)
            EditText edtRemark;

            @BindView(R.id.cbHoliday)
            CheckBox cbHoliday;

            @BindView(R.id.cbLeave)
            CheckBox cbLeave;

            @BindView(R.id.tvDate)
            TextView tvDate;

            @BindView(R.id.card)
            LinearLayout card;

            @BindView(R.id.llTitle)
            LinearLayout llTitle;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
                edtTPArea.setOnClickListener(this);
                edtTPWW.setOnClickListener(this);
                edtRemark.setOnClickListener(this);
               /* cbHoliday.setOnClickListener(this);
                cbLeave.setOnClickListener(this);*/

                /*edtRemark.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        v.setEnabled(true);
                        v.setFocusable(true);
                        v.setFocusableInTouchMode(true);
                        return false;
                    }
                });

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
                        TPFormResponse.DaysBean getSet = listItems.get(getAdapterPosition());
                        getSet.setRemark(s.toString());
                        listItems.set(getAdapterPosition(),getSet);
                    }
                });*/



            }

            @Override
            public void onClick(View v)
            {

                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                TPFormResponse.DaysBean getSet = listItems.get(getAdapterPosition());
                switch (v.getId())
                {
                    case R.id.edtTPArea:
                        if(getSet.getLeave().equals("false") && getSet.getHoliday().equals("false"))
                        {
                            showListDialog(AREA,edtTPArea,getAdapterPosition());
                        }
                        break;
                    case R.id.edtTPWW:
                        if(getSet.getLeave().equals("false") && getSet.getHoliday().equals("false"))
                        {
                            listDialogAdapter.setCheckedIds("regular",getAdapterPosition());
                            showListDialog(WW,edtTPWW,getAdapterPosition());
                        }
                        break;
                    case R.id.edtRemark:
                        showTPRemarkDialog("regular",getAdapterPosition(),AppUtils.getDateCurrentTimeString(listTpEntry.get(getAdapterPosition()).getDate()));
                        break;
                }
            }
        }
    }

    public class ExtraEntryAdapter extends RecyclerView.Adapter<ExtraEntryAdapter.ViewHolder> {

        List<TPFormResponse.ExtradaysBean> listItems;

        ExtraEntryAdapter(List<TPFormResponse.ExtradaysBean> list) {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_tpextra_table, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final TPFormResponse.ExtradaysBean getSet = listItems.get(position);

            if(position==0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }

           if(getSet.getDate().length()>0 || !getSet.getDate().equals(""))
           {
               holder.edtDate.setText(AppUtils.universalDateConvert(AppUtils.getDateCurrentTimeString(Long.parseLong(getSet.getDate())),"dd MMM yyyy","dd"));
           }
            holder.tvDay.setText(getSet.getDay_name());
            holder.edtTPArea.setText(getSet.getArea_name());
            String nameToDisplay = getSet.getWork_withName();
            if(nameToDisplay.endsWith(","))
            {
                nameToDisplay = nameToDisplay.substring(0,nameToDisplay.length() - 1);
            }
            holder.edtTPWW.setText(nameToDisplay);
            //holder.edtTPWW.setText(getSet.getWork_withName());
            holder.edtRemark.setText(getSet.getRemark());
            holder.edtRemark.setSelection(getSet.getRemark().length());
            int num = position + 1 ;
            holder.tvNumber.setText(""+num);
        }

        public void updateListWW(int position,String wwString,String wwName)
        {
            TPFormResponse.ExtradaysBean newBean = listItems.get(position);
            newBean.setWork_withString(wwString);
            newBean.setWork_withName(wwName);
            listItems.set(position,newBean);
            notifyItemChanged(position);
        }

        public void updateListArea(int position,TourAreaResponse.TourAreaBean bean)
        {
            TPFormResponse.ExtradaysBean newBean = listItems.get(position);
            newBean.setArea_id(bean.getArea_id());
            newBean.setArea(bean.getArea_id());
            newBean.setArea_name(bean.getArea_name());
            newBean.setRoute_area_id(bean.getRoute_area_id());
            listItems.set(position,newBean);
            notifyItemChanged(position);
        }

        public void updateListDates(int position,DayGetSet getSet)
        {
            TPFormResponse.ExtradaysBean newBean = listItems.get(position);
            newBean.setSr_no(getSet.getDate());
            newBean.setDay_name(getSet.getDay());
            try {
                String dateString = getSet.getDate()+"-"+monthNumber+"-"+edtYear.getText().toString();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = (Date)formatter.parse(dateString);
                newBean.setDate(String.valueOf(date.getTime()/1000));
                listItems.set(position,newBean);
                notifyItemChanged(position);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.edtTPArea)
            EditText edtTPArea;

            @BindView(R.id.edtTPWW)
            EditText edtTPWW;

            @BindView(R.id.edtRemark)
            EditText edtRemark;

            @BindView(R.id.edtDate)
            EditText edtDate;

            @BindView(R.id.card)
            LinearLayout card;

            @BindView(R.id.llTitle)
            LinearLayout llTitle;

            @BindView(R.id.tvNumber)
            TextView tvNumber;

            @BindView(R.id.ivDelete)
            ImageView ivDelete;

            @BindView(R.id.tvDay)
            TextView tvDay;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
                edtDate.setOnClickListener(this);
                edtTPArea.setOnClickListener(this);
                edtTPWW.setOnClickListener(this);
                ivDelete.setOnClickListener(this);
                edtRemark.setOnClickListener(this);

                /*edtRemark.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        v.setEnabled(true);
                        v.setFocusable(true);
                        v.setFocusableInTouchMode(true);
                        return false;
                    }
                });

                edtRemark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        TPFormResponse.ExtradaysBean newBean = listItems.get(getAdapterPosition());
                        newBean.setRemark(s.toString());
                        listItems.set(getAdapterPosition(),newBean);
                    }
                });*/
            }

            @Override
            public void onClick(View v)
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                switch (v.getId())
                {
                    case R.id.edtDate:
                        edtRemark.setFocusable(false);
                        edtRemark.clearFocus();
                        showListDialog(EXTRA_DATE,edtDate,getAdapterPosition());
                        break;
                    case R.id.edtTPArea:
                        edtRemark.setFocusable(false);
                        edtRemark.clearFocus();
                        showListDialog(EXTRA_AREA,edtTPArea,getAdapterPosition());
                        break;
                    case R.id.edtTPWW:
                        edtRemark.setFocusable(false);
                        edtRemark.clearFocus();
                        listDialogAdapter.setCheckedIds("extra",getAdapterPosition());
                        showListDialog(EXTRA_WW,edtTPWW,getAdapterPosition());
                        break;
                    case R.id.ivDelete:
                        TPFormResponse.ExtradaysBean newBean = listItems.get(getAdapterPosition());
                        newBean.setArea_id("");
                        newBean.setArea_name("");
                        newBean.setRoute_area_id("");
                        newBean.setSr_no("");
                        newBean.setRemark("");
                        newBean.setWork_withName("");
                        newBean.setWork_withString("");
                        newBean.setDay_name("");
                        listItems.set(getAdapterPosition(),newBean);
                        notifyItemChanged(getAdapterPosition());
                        break;
                    case R.id.edtRemark:
                        String date = "";
                        if(!listExtraEntry.get(getAdapterPosition()).getDate().equalsIgnoreCase(""))
                        {
                            long time = Long.parseLong(listExtraEntry.get(getAdapterPosition()).getDate());
                            date = AppUtils.getDateCurrentTimeString(time);
                        }
                        else
                        {
                            date = "";
                        }
                        showTPRemarkDialog("extra",getAdapterPosition(),date);
                        break;
                }
            }
        }
    }

    private void showTPRemarkDialog(final String isFor, final int position, String date)
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_tp, null);
            dialog.setContentView(sheetView);

            /*dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);*/

            TextView tvTitle = dialog.findViewById(R.id.tvTitle);

            tvTitle.setText(date);

            TextView tvArea = (TextView) dialog.findViewById(R.id.tvArea);
            TextView tvWorkWith = (TextView) dialog.findViewById(R.id.tvWorkWith);
            TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
            final EditText edtRemark = (EditText) dialog.findViewById(R.id.edtRemark);

            if(isFor.equalsIgnoreCase("regular"))
            {
                TPFormResponse.DaysBean bean = listTpEntry.get(position);
                edtRemark.setText(bean.getRemark());
                edtRemark.setSelection(bean.getRemark().length());
                if (bean.getArea_name().equals("")) {
                    tvArea.setText("---");
                } else {
                    tvArea.setText(bean.getArea_name());
                }
                if (bean.getWork_withName().equals("")) {
                    tvWorkWith.setText("---");
                } else {
                    tvWorkWith.setText(bean.getWork_withName());
                }
            }
            else
            {
                TPFormResponse.ExtradaysBean bean = listExtraEntry.get(position);
                edtRemark.setText(bean.getRemark());
                edtRemark.setSelection(bean.getRemark().length());
                if (bean.getArea_name().equals("")) {
                    tvArea.setText("---");
                } else {
                    tvArea.setText(bean.getArea_name());
                }
                if (bean.getWork_withName().equals("")) {
                    tvWorkWith.setText("---");
                } else {
                    tvWorkWith.setText(bean.getWork_withName());
                }
            }


            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    AppUtils.hideKeyboard(sheetView,activity);

                    if(isFor.equalsIgnoreCase("regular"))
                    {
                        TPFormResponse.DaysBean bean = listTpEntry.get(position);
                        bean.setRemark(edtRemark.getText().toString().trim());
                        listTpEntry.set(position,bean);
                        entryAdapter.notifyItemChanged(position);
                    }
                    else
                    {
                        TPFormResponse.ExtradaysBean bean = listExtraEntry.get(position);
                        bean.setRemark(edtRemark.getText().toString().trim());
                        listExtraEntry.set(position,bean);
                        extraEntryAdapter.notifyItemChanged(position);
                    }
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
