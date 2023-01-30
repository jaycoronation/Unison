package com.unisonpharmaceuticals.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.ParseException;
import android.os.AsyncTask;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AreaResponse;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.DoctorResponse;
import com.unisonpharmaceuticals.model.PlannerEntryResponse;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.WorkWithResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPlannerEntry extends Fragment implements View.OnClickListener {
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;

    @BindView(R.id.llLoadingTransparent)
    LinearLayout llLoadingTransparent;

    @BindView(R.id.edtPlanDate)
    EditText edtPlanDate;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.edtArea)
    EditText edtArea;
    @BindView(R.id.edtDoctorSpeciality)
    EditText edtDoctorSpeciality;
    @BindView(R.id.tvAddPlan)
    TextView tvAddPlan;
    @BindView(R.id.tvConfirmPlan)
    TextView tvConfirmPlan;
    @BindView(R.id.tvCancelPlan)
    TextView tvCancelPlan;
    @BindView(R.id.tvApprovePlan)
    TextView tvApprovePlan;
    @BindView(R.id.rvPlan)
    RecyclerView rvPlan;
    @BindView(R.id.inputDoctor)
    TextInputLayout inputDoctor;
    @BindView(R.id.edtDoctor)
    EditText edtDoctor;
    @BindView(R.id.tvNoPlan)
    TextView tvNoPlan;
    @BindView(R.id.llWorkWith)
    LinearLayout llWorkWith;
    @BindView(R.id.cbWorkWith)
    CheckBox cbWorkWith;
    @BindView(R.id.viewWorkWith)
    View viewWorkWith;
    @BindView(R.id.inputWorkWith)
    TextInputLayout inputWorkWith;
    @BindView(R.id.edtWorkWith)
    EditText edtWorkWith;
    @BindView(R.id.ivEmployee)
    ImageView ivEmployee;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<WorkWithResponse.StaffBean> listWorkWith = new ArrayList<>();
    private ArrayList<WorkWithResponse.StaffBean> listWorkWithSearch = new ArrayList<>();

    private List<AreaResponse.AreasBean> listArea = new ArrayList<>();
    private List<AreaResponse.AreasBean> listAreaSearch = new ArrayList<>();

    private ArrayList<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private ArrayList<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private ArrayList<DoctorResponse.DoctorsBean> listDoctor = new ArrayList<>();
    private ArrayList<DoctorResponse.DoctorsBean> listDoctorSearch = new ArrayList<>();

    private final String EMPLOYEE = "Employee";
    private final String AREA = "Area";
    private final String SPECIALITY = "Speciality";
    private final String DOCTOR = "Doctor";
    private final String WORKWITH = "Work with";

    private String selectedStaffId = "";
    private String selectedSpecialityId = "", selectedAreaId = "", selectedDoctorId = "", doctorString = "", workWithString = "";

    //DatePicker
    private Calendar calendar;
    public static int year = 2000, month = 1, day = 1;
    public static long selectedTime = 0L;

    public static String dateString = "";

    private List<PlannerEntryResponse.PlanBean> listPlan = new ArrayList<>();
    private PlannerAdapter plannerAdapter;

    public static Handler handler;

    private SpringyAdapterAnimator mAnimator;

    private boolean isLoading = false;

    private long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planner_entry, container, false);
        ButterKnife.bind(this, rootView);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initView();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (sessionManager.isNetworkAvailable()) {
            getDataFromServer();
        } else {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 121)
                {
                    edtArea.setText("");
                    selectedAreaId = "";

                    edtDoctorSpeciality.setText("");
                    edtDoctor.setText("");

                    selectedSpecialityId = "";
                    selectedDoctorId = "";
                    doctorString = "";

                    cbWorkWith.setChecked(true);
                    edtWorkWith.setText("");
                    if(listWorkWith.size()>0)
                    {
                        for (int i = 0; i < listWorkWith.size(); i++)
                        {
                            WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                            bean.setSelected(false);
                            listWorkWith.set(i,bean);
                        }
                    }
                    workWithString = "";

                    /*if(!selectedStaffId.equalsIgnoreCase(""))
                    {
                        getAreaFromEmployee(false);
                    }*/

                    if(sessionManager.isNetworkAvailable())
                    {
                        getPlannerReport();
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
                else if(msg.what==122)
                {
                    try {
                        if(listPlan.size()>0)
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                getAreaFromEmployee(true);
                            }
                            else
                            {
                                AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                            }
                        }
                        else
                        {
                            AppUtils.showToast(activity,"To add area, you should have add at least one plan from your current Tour Plan area.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    private void initView() {
        rvPlan.setLayoutManager(new LinearLayoutManager(activity));
        edtArea.setOnClickListener(this);
        edtEmployee.setOnClickListener(this);
        edtDoctorSpeciality.setOnClickListener(this);
        edtPlanDate.setOnClickListener(this);
        edtDoctor.setOnClickListener(this);
        tvAddPlan.setOnClickListener(this);
        tvCancelPlan.setOnClickListener(this);
        tvConfirmPlan.setOnClickListener(this);
        tvApprovePlan.setOnClickListener(this);
        edtWorkWith.setOnClickListener(this);
        ivEmployee.setOnClickListener(this);
        llWorkWith.setOnClickListener(this);

        cbWorkWith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View viewWorkWith = activity.findViewById(R.id.viewWorkWith);

                if (isChecked) {
                    inputWorkWith.setVisibility(View.GONE);
                    viewWorkWith.setVisibility(View.VISIBLE);
                    ivEmployee.setSelected(true);
                } else {
                    inputWorkWith.setVisibility(View.VISIBLE);
                    viewWorkWith.setVisibility(View.GONE);
                    ivEmployee.setSelected(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.edtEmployee:
                employeeClick();
                break;
            case R.id.edtArea:
               areaClick();
                break;
            case R.id.edtDoctorSpeciality:
               specialityClick();
                break;
            case R.id.edtDoctor:
                doctorClick();
                break;
            case R.id.edtWorkWith:
                workWithClick();
                break;
            case R.id.llWorkWith:
                cbWorkWith.performClick();
                break;
            case R.id.edtPlanDate:
                showDatePickerDialog();
                break;
            case R.id.tvAddPlan:
                if (isApproved()) {
                    AppUtils.showToast(activity, "Plan is approved");
                } else {
                    saveTourPlanEntry();
                }
                break;
            case R.id.tvConfirmPlan:
                if(sessionManager.isNetworkAvailable())
                {
                    confirmPlanner();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvCancelPlan:
                rvPlan.setVisibility(View.GONE);
                selectedTime = 0;
                selectedStaffId = "";
                selectedAreaId = "";
                selectedSpecialityId = "";
                doctorString = "";
                edtPlanDate.setText("");
                edtEmployee.setText("");
                edtArea.setText("");
                edtDoctorSpeciality.setText("");
                edtDoctor.setText("");
                listPlan = new ArrayList<>();
                if (plannerAdapter != null) {
                    plannerAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tvApprovePlan:
                if(sessionManager.isNetworkAvailable())
                {
                    approvePlanner();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.llLoadingTransparent:
                break;
            case R.id.ivEmployee:
                cbWorkWith.performClick();
                break;
        }
    }

    private void employeeClick()
    {
        if (isLoading) {
            AppUtils.showLoadingToast(activity);
        } else {
            showListDialog(EMPLOYEE, edtEmployee, 0);
        }
    }
    private void areaClick()
    {
        if (isLoading)
        {
            AppUtils.showLoadingToast(activity);
        }
        else if(edtPlanDate.getText().toString().equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select date");
        }
        else
        {
            if (!selectedStaffId.equals(""))
            {
                if(listArea.size()>0)
                {
                    showListDialog(AREA, edtArea, 0);
                }
                else
                {
                    AppUtils.showToast(activity,"Please fill tour plan first.");
                }
            } else {
                AppUtils.showToast(activity, "Please select employee");
            }
        }
    }
    private void specialityClick()
    {
        if (isLoading) {
            AppUtils.showLoadingToast(activity);
        } else {
            if (!selectedStaffId.equals("")) {
                if (selectedAreaId.equals("")) {
                    AppUtils.showToast(activity, "Please select area");
                } else {
                    showListDialog(SPECIALITY, edtDoctorSpeciality, 0);
                }
            } else {
                AppUtils.showToast(activity, "Please select employee");
            }


        }
    }
    private void doctorClick()
    {
        if (isLoading) {
            AppUtils.showLoadingToast(activity);
        } else {
            if (!selectedStaffId.equals("")) {
                if (selectedAreaId.equals("")) {
                    AppUtils.showToast(activity, "Please select area");
                } else {
                    if (selectedSpecialityId.equals("")) {
                        AppUtils.showToast(activity, "Please select speciality");
                    } else {
                        showListDialog(DOCTOR, edtDoctor, 0);
                    }
                }
            } else {
                AppUtils.showToast(activity, "Please select employee");
            }


        }
    }
    private void workWithClick()
    {
        if (isLoading) {
            AppUtils.showLoadingToast(activity);
        } else {
            if (selectedDoctorId.equals("")) {
                AppUtils.showToast(activity, "Please select doctor first");
            } else {
                showListDialog(WORKWITH, edtWorkWith, 0);
            }
        }
    }


    private void saveTourPlanEntry() {
        if (sessionManager.isNetworkAvailable()) {
            if (edtPlanDate.getText().toString().equals("")) {
                AppUtils.showToast(activity, "Please select planning date");
            } else if (selectedStaffId.equals("")) {
                AppUtils.showToast(activity, "Please select employee");
            } else if (selectedAreaId.equals("")) {
                AppUtils.showToast(activity, "Please select area");
            } else if (selectedSpecialityId.equals("")) {
                AppUtils.showToast(activity, "Please select speciality");
            } else if (selectedDoctorId.equals("")) {
                AppUtils.showToast(activity, "Please select doctor.");
            }
            else if(!cbWorkWith.isChecked() && workWithString.trim().equals(""))
            {
                AppUtils.showToast(activity, "Please select work with.");
            }
            else {
                try
                {

                    Log.e("<><><>", "saveTourPlanEntry: "+workWithString);

                    final JSONArray workArray = new JSONArray();
                    if(workWithString.trim().length()>0)
                    {
                        List<String> workWithIds = Arrays.asList(workWithString.split(","));
                        if(workWithIds.size()>0)
                        {
                            for (int j = 0; j < workWithIds.size(); j++)
                            {
                                workArray.put(Integer.parseInt(workWithIds.get(j)));
                            }
                        }
                    }

                    new AsyncTask<Void, Void, Void>() {
                        int success = 0;
                        String message = "";

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            llLoadingTransparent.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("staff_id", selectedStaffId);
                                /*hashMap.put("date", String.valueOf(selectedTime / 1000));*/
                                hashMap.put("date",dateString);
                                hashMap.put("isMobile", "true");
                                hashMap.put("doctor_id", selectedDoctorId);
                                hashMap.put("products", "");
                                hashMap.put("reasons", "");
                                hashMap.put("work_with", workArray.toString());
                                hashMap.put("login_user_id", sessionManager.getUserId());
                                hashMap.put("is_app", "true");

                                Log.e("PLANNER Request", "doInBackground: " + hashMap.toString());

                                String response = "";

                                response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SAVE_DAILY_PLANNER, hashMap);

                                Log.e("PLANNER Response >>> ", "doInBackground: " + response);

                                JSONObject jsonObject = new JSONObject(response);
                                success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                                message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            llLoadingTransparent.setVisibility(View.GONE);
                            AppUtils.showToast(activity, message);
                            if (success == 1) {

                                /*selectedAreaId = "";
                                selectedSpecialityId = "";*/
                                doctorString = "";
                                /*edtArea.setText("");
                                edtDoctorSpeciality.setText("");*/
                                edtDoctor.setText("");

                                if (plannerAdapter != null) {
                                    plannerAdapter.notifyDataSetChanged();
                                }

                                cbWorkWith.setChecked(true);
                                edtWorkWith.setText("");
                                if(listWorkWith.size()>0)
                                {
                                    for (int i = 0; i < listWorkWith.size(); i++)
                                    {
                                        WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                                        bean.setSelected(false);
                                        listWorkWith.set(i,bean);
                                    }
                                }
                                workWithString = "";

                                getPlannerReport();
                            }
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);

                    llLoadingTransparent.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }
    }

    private void getSpecialityFromArea(final String areaCode, final String selectedStaffId) {
        isLoading = true;
        llLoadingTransparent.setVisibility(View.VISIBLE);
        listSpeciality.clear();
        Log.e("AreaCode> ", "getSpecialityFromArea: " + areaCode);
        Call<SpecialistBean> loginCall = apiService.getSpecialityFromArea("500", areaCode, selectedStaffId);
        loginCall.enqueue(new Callback<SpecialistBean>() {
            @Override
            public void onResponse(Call<SpecialistBean> call, Response<SpecialistBean> response) {
                if (response.body().getSuccess() == 1) {
                    listSpeciality = (ArrayList<SpecialistBean.SpecialityBean>) response.body().getSpeciality();
                } else {
                    AppUtils.showToast(activity, response.body().getMessage());
                }
                isLoading = false;
                llLoadingTransparent.setVisibility(View.GONE);
                specialityClick();
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }
        });

    }

    private void getDoctorFromSpeciality(String areaId, String specialityId) {
        isLoading = true;
        llLoadingTransparent.setVisibility(View.VISIBLE);
        listDoctor.clear();
        Call<DoctorResponse> doctorCall = apiService.getDoctorFromSpeciality(areaId, "1000", specialityId, selectedStaffId, sessionManager.getUserId());
        doctorCall.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                if (response.body().getSuccess() == 1) {
                    listDoctor = (ArrayList<DoctorResponse.DoctorsBean>) response.body().getDoctors();
                } else {
                    AppUtils.showToast(activity, response.body().getMessage());
                }
                isLoading = false;
                llLoadingTransparent.setVisibility(View.GONE);
                if(listDoctor.size()>0)
                {
                    doctorClick();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private void getDataFromServer() {
        isLoading = true;
        llLoadingTransparent.setVisibility(View.VISIBLE);
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
                            //getAreaFromEmployee();
                        }
                    } else {
                        AppUtils.showToast(activity, "Could not get employee list.");
                        activity.finish();
                    }
                    isLoading = false;
                } catch (Exception e) {
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

        llLoadingTransparent.setVisibility(View.GONE);
    }

    private void getAreaFromEmployee(final boolean isForAddArea)
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        isLoading = true;
        Call<AreaResponse> loginCall = apiService.getAreaFromUserId("500", selectedStaffId, sessionManager.getUserId(),dateString);
        loginCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response)
            {
                listArea.clear();
                try
                {
                    if (response.body().getSuccess() == 1)
                    {
                        List<AreaResponse.AreasBean> list = response.body().getAreas();

                        Log.e("listPLan size >> ", "onResponse: "+listPlan.size() );

                        if(listPlan.size()==0)
                        {
                            for (int i = 0; i < list.size(); i++)
                            {
                                if(list.get(i).getIs_tour_plan().equalsIgnoreCase("1"))
                                {
                                    listArea.add(list.get(i));
                                }
                            }
                        }
                        else
                        {
                            listArea.addAll(list);
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                    llLoadingTransparent.setVisibility(View.GONE);
                    isLoading = false;

                    if(isForAddArea)
                    {
                        areaClick();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                isLoading = false;
                llLoadingTransparent.setVisibility(View.GONE);
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

      /*  isLoading = true;
        Call<TourAreaResponse> tpAreaCall = apiService.getTPArea(selectedStaffId,sessionManager.getUserId());
        tpAreaCall.enqueue(new Callback<TourAreaResponse>()
        {
            @Override
            public void onResponse(Call<TourAreaResponse> call, Response<TourAreaResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    List<TourAreaResponse.TourAreaBean> list= response.body().getTour_area();
                    for (int i = 0; i < list.size(); i++)
                    {
                        AreaResponse.AreasBean bean = new AreaResponse.AreasBean();
                        bean.setArea(list.get(i).getArea_name());
                        bean.setArea_id(list.get(i).getArea_id());
                        listArea.add(bean);
                    }
                }
                else
                {
                    AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No data found");
                isLoading = false;
            }
        });*/

        isLoading = true;
        Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(selectedStaffId, sessionManager.getUserId(), "false");
        workWithCall.enqueue(new Callback<WorkWithResponse>() {
            @Override
            public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                try {
                    if (response.body().getSuccess() == 1) {
                        listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                    } else {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getWorkWithList  ## ", "onResponse: 1");
                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
    }

    private void getPlannerReport()
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        Call<PlannerEntryResponse> getPlanner = apiService.getTourPlanner(selectedStaffId,
                String.valueOf(selectedTime / 1000), sessionManager.getUserId());
        getPlanner.enqueue(new Callback<PlannerEntryResponse>()
        {
            @Override
            public void onResponse(Call<PlannerEntryResponse> call, Response<PlannerEntryResponse> response)
            {
                listPlan.clear();

                if (response.body().getSuccess() == 1)
                {
                    listPlan = response.body().getPlan();

                    if (listPlan.size() > 0)
                    {
                        rvPlan.setVisibility(View.VISIBLE);
                        tvNoPlan.setVisibility(View.GONE);
                        plannerAdapter = new PlannerAdapter(listPlan, rvPlan);
                        rvPlan.setAdapter(plannerAdapter);


                        AppUtils.showHideBottomButtons(activity,tvAddPlan,tvConfirmPlan,tvApprovePlan,isConfirmed(),isApproved(),selectedStaffId);


                        /*if (isApproved())
                        {
                            tvAddPlan.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvAddPlan.setVisibility(View.VISIBLE);
                        }

                        if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
                        {
                            if(isConfirmed())
                            {
                                tvConfirmPlan.setVisibility(View.GONE);
                            }
                            else
                            {
                                tvConfirmPlan.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            tvConfirmPlan.setVisibility(View.GONE);
                        }

                        if (!selectedStaffId.equals(sessionManager.getUserId()))
                        {
                            if (isApproved())
                            {
                                tvApprovePlan.setVisibility(View.GONE);
                                tvAddPlan.setVisibility(View.GONE);
                            }
                            else
                            {
                                tvApprovePlan.setVisibility(View.VISIBLE);
                                tvAddPlan.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            tvApprovePlan.setVisibility(View.GONE);
                        }*/

                    }
                    else
                    {
                        tvAddPlan.setVisibility(View.VISIBLE);
                        tvConfirmPlan.setVisibility(View.GONE);
                        tvApprovePlan.setVisibility(View.GONE);
                        tvNoPlan.setVisibility(View.VISIBLE);
                        tvNoPlan.setText(response.body().getMessage());
                        AppUtils.showToast(activity, "No Plan founds for selected date");

                    }
                }
                else {
                    AppUtils.showToast(activity, response.body().getMessage());
                    tvAddPlan.setVisibility(View.VISIBLE);
                    tvNoPlan.setVisibility(View.VISIBLE);
                    tvConfirmPlan.setVisibility(View.GONE);
                    tvApprovePlan.setVisibility(View.GONE);
                    rvPlan.setVisibility(View.GONE);
                }
                llLoadingTransparent.setVisibility(View.GONE);

                if(!selectedStaffId.equalsIgnoreCase(""))
                {
                    getAreaFromEmployee(false);
                }
            }

            @Override
            public void onFailure(Call<PlannerEntryResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private boolean isConfirmOrApproved() {
        boolean b = false;
        if (listPlan.size() > 0) {
            for (int i = 0; i < listPlan.size(); i++) {
                if (listPlan.get(i).getIs_approved().equalsIgnoreCase("Yes") || listPlan.get(i).getIs_confirmed().equalsIgnoreCase("Yes")) {
                    b = true;
                }
            }
        } else {
            b = false;
        }

        return b;
    }

    private boolean isConfirmed() {
        boolean b = false;
        if (listPlan.size() > 0) {
            for (int i = 0; i < listPlan.size(); i++) {
                if (listPlan.get(i).getIs_confirmed().equalsIgnoreCase("Yes")) {
                    b = true;
                }
            }
        } else {
            b = false;
        }

        return b;
    }

    private boolean isApproved() {
        boolean b = false;
        if (listPlan.size() > 0) {
            for (int i = 0; i < listPlan.size(); i++) {
                if (listPlan.get(i).getIs_approved().equalsIgnoreCase("Yes")) {
                    b = true;
                }
            }
        } else {
            b = false;
        }

        return b;
    }

    private void confirmPlanner() {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        final Call<CommonResponse> getPlanner = apiService.confirmTourPlanner(selectedStaffId, String.valueOf(selectedTime / 1000), sessionManager.getUserId());
        getPlanner.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                AppUtils.showToast(activity, response.body().getMessage());
                llLoadingTransparent.setVisibility(View.GONE);
                getPlannerReport();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private void approvePlanner() {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        final Call<CommonResponse> getPlanner = apiService.approvePlanner(selectedStaffId, String.valueOf(selectedTime / 1000), sessionManager.getUserId(), sessionManager.getUserId());
        getPlanner.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                AppUtils.showToast(activity, response.body().getMessage());
                llLoadingTransparent.setVisibility(View.GONE);
                getPlannerReport();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    DialogListAdapter listDialogAdapter;

    public void showListDialog(final String isFor, final EditText editText, final int listPostion) {
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
                AppUtils.hideKeyboard(sheetView, activity);
            }
        });
        LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);

        final TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        listDialogAdapter = new DialogListAdapter(listDialog, isFor, false, "", listPostion, rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(listDialogAdapter);

        final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);
        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

        inputSearch.setVisibility(View.VISIBLE);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });


        if (isFor.equalsIgnoreCase(WORKWITH)) {
            tvDone.setVisibility(View.VISIBLE);
            listDialog.findViewById(R.id.ivBack).setVisibility(View.GONE);

        }

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


               /* if(isFor.equalsIgnoreCase(DOCTOR)&&listDialogAdapter !=null)
                {
                    doctorString = listDialogAdapter.getSelectedDrIds();
                    if(doctorString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one doctor.");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(tvDone,activity);
                        edtDoctor.setText(listDialogAdapter.getSelectedDrNames().toUpperCase());
                        listDialog.dismiss();
                        listDialog.cancel();
                    }
                }*/
                if (isFor.equalsIgnoreCase(WORKWITH) && listDialogAdapter != null) {
                    workWithString = listDialogAdapter.getSelectedWorkWitIds();
                    if (workWithString.length() == 0) {
                        AppUtils.showToast(activity, "Please select at least one option.");
                    } else {
                        AppUtils.hideKeyboard(tvDone, activity);
                        edtWorkWith.setText(listDialogAdapter.getSelectedWorkWithName());
                        listDialog.dismiss();
                        listDialog.cancel();
                    }
                } else {
                    AppUtils.hideKeyboard(tvDone, activity);

                    listDialog.dismiss();
                    listDialog.cancel();
                }

            }
        });

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
                } else if (isFor.equals(AREA)) {
                    listAreaSearch.clear();
                    for (int i = 0; i < listArea.size(); i++) {
                        if (textlength <= listArea.get(i).getArea().length()) {
                            if (listArea.get(i).getArea().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listAreaSearch.add(listArea.get(i));
                            }
                        }
                    }
                } else if (isFor.equals(SPECIALITY)) {
                    listSpecialitySearch.clear();
                    for (int i = 0; i < listSpeciality.size(); i++) {
                        if (textlength <= listSpeciality.get(i).getSpeciality().length()) {
                            if (listSpeciality.get(i).getSpeciality().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listSpecialitySearch.add(listSpeciality.get(i));
                            }
                        }
                    }
                } else if (isFor.equals(DOCTOR)) {
                    listDoctorSearch.clear();
                    for (int i = 0; i < listDoctor.size(); i++) {
                        if (textlength <= listDoctor.get(i).getDoctor().length()) {
                            if (listDoctor.get(i).getDoctor().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) || listDoctor.get(i).getDoctor_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listDoctorSearch.add(listDoctor.get(i));
                            }
                        }
                    }
                } else if (isFor.equals(WORKWITH)) {
                    listWorkWithSearch.clear();
                    for (int i = 0; i < listWorkWith.size(); i++) {
                        if (textlength <= listWorkWith.get(i).getName().length()) {
                            if (listWorkWith.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) || listWorkWith.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listWorkWithSearch.add(listWorkWith.get(i));
                            }
                        }
                    }
                }
                AppendListForArea(listDialog, isFor, true, rvListDialog, edtSearchDialog);
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
        int listPosition = 0;
        boolean isDone = false;

        DialogListAdapter(Dialog dialog, String isFor, boolean isForSearch, String searchText, RecyclerView recyclerView) {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator, recyclerView);
        }

        DialogListAdapter(Dialog dialog, String isFor, boolean isForSearch, String searchText, int listPosition, RecyclerView recyclerView) {
            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.listPosition = listPosition;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator, recyclerView);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);

            if (!isFor.equals(DOCTOR)) {
                mAnimator.onSpringItemCreate(v);
            }
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position == getItemCount() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if (!isFor.equals(DOCTOR)) {
                mAnimator.onSpringItemBind(holder.itemView, position);
            }
            if (isFor.equals(EMPLOYEE))
            {
                holder.cb.setVisibility(View.GONE);
                final StaffResponse.StaffBean getSet;
                if (isForSearch) {
                    getSet = listEmployeeSearch.get(position);
                } else {
                    getSet = listEmployee.get(position);
                }
                holder.tvValue.setText(getSet.getName().toUpperCase());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtEmployee.setText(getSet.getName().toUpperCase());

                        selectedStaffId = getSet.getStaff_id();

                        if (!getSet.getStaff_id().equals(sessionManager.getUserId())) {
                            tvApprovePlan.setVisibility(View.VISIBLE);
                        } else {
                            tvApprovePlan.setVisibility(View.GONE);
                        }


                        edtArea.setText("");
                        edtDoctorSpeciality.setText("");
                        edtDoctor.setText("");

                        selectedAreaId = "";
                        selectedSpecialityId = "";
                        selectedDoctorId = "";
                        doctorString = "";

                        if(!edtPlanDate.getText().toString().equalsIgnoreCase(""))
                        {
                            getAreaFromEmployee(false);
                        }
                        getPlannerReport();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            } else if (isFor.equals(AREA)) {
                holder.cb.setVisibility(View.GONE);
                final AreaResponse.AreasBean getSet;
                if (isForSearch) {
                    getSet = listAreaSearch.get(position);
                } else {
                    getSet = listArea.get(position);
                }
                holder.tvValue.setText(getSet.getArea().toUpperCase());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtArea.setText(getSet.getArea().toUpperCase());
                        selectedAreaId = getSet.getArea_id();

                        edtDoctorSpeciality.setText("");
                        edtDoctor.setText("");

                        selectedSpecialityId = "";
                        selectedDoctorId = "";
                        doctorString = "";

                        cbWorkWith.setChecked(true);
                        edtWorkWith.setText("");
                        if(listWorkWith.size()>0)
                        {
                            for (int i = 0; i < listWorkWith.size(); i++)
                            {
                                WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                                bean.setSelected(false);
                                listWorkWith.set(i,bean);
                            }
                        }
                        workWithString = "";


                        dialog.dismiss();
                        dialog.cancel();
                        getSpecialityFromArea(getSet.getArea_id(), selectedStaffId);

                    }
                });
            } else if (isFor.equalsIgnoreCase(SPECIALITY)) {
                final SpecialistBean.SpecialityBean getSet;
                if (isForSearch) {
                    getSet = listSpecialitySearch.get(position);
                } else {
                    getSet = listSpeciality.get(position);
                }

                holder.tvValue.setText(getSet.getSpeciality().toUpperCase());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            edtDoctorSpeciality.setText(getSet.getSpeciality().toUpperCase());
                            selectedSpecialityId = getSet.getSpeciality_id();
                            edtDoctor.setText("");
                            selectedDoctorId = "";
                            doctorString = "";

                            cbWorkWith.setChecked(true);
                            edtWorkWith.setText("");
                            if(listWorkWith.size()>0)
                            {
                                for (int i = 0; i < listWorkWith.size(); i++)
                                {
                                    WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                                    bean.setSelected(false);
                                    listWorkWith.set(i,bean);
                                }
                            }
                            workWithString = "";

                            getDoctorFromSpeciality(selectedAreaId, getSet.getSpeciality_id());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (isFor.equalsIgnoreCase(DOCTOR)) {
                final DoctorResponse.DoctorsBean getSet;
                if (isForSearch) {
                    getSet = listDoctorSearch.get(position);
                } else {
                    getSet = listDoctor.get(position);
                }

                holder.tvValue.setAllCaps(true);
                holder.tvValue.setText(getSet.getDoctor());
                holder.tvId.setText(getSet.getDoctor_id());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            edtDoctor.setText(getSet.getDoctor().toUpperCase());
                            selectedDoctorId = getSet.getDoctor_id();

                            cbWorkWith.setChecked(true);
                            edtWorkWith.setText("");
                            if(listWorkWith.size()>0)
                            {
                                for (int i = 0; i < listWorkWith.size(); i++)
                                {
                                    WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                                    bean.setSelected(false);
                                    listWorkWith.set(i,bean);
                                }
                            }
                            workWithString = "";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



                /*holder.cb.setVisibility(View.VISIBLE);

                final DoctorResponse.DoctorsBean getSet;
                if(isForSearch)
                {
                    getSet = listDoctorSearch.get(position);
                }
                else
                {
                    getSet = listDoctor.get(position);
                }

                isDone = true;
                holder.cb.setChecked(getSet.isSelected());

                holder.tvValue.setVisibility(View.INVISIBLE);

                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));
                holder.cb.setText(getSet.getDoctor().toUpperCase());
                holder.cb.setAllCaps(true);

                holder.tvId.setVisibility(View.VISIBLE);
                holder.tvId.setText(getSet.getDoctor_id());

                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if(!isDone)
                        {
                            getSet.setSelected(isChecked);
                            if(isForSearch)
                            {
                                listDoctorSearch.set(position,getSet);
                            }
                            else
                            {
                                listDoctor.set(position,getSet);
                            }
                            try {
                                notifyItemChanged(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

                isDone = false;*/
            } else if (isFor.equals(WORKWITH)) {
                holder.cb.setVisibility(View.VISIBLE);

                final WorkWithResponse.StaffBean getSet;
                if (isForSearch) {
                    getSet = listWorkWithSearch.get(position);
                } else {
                    getSet = listWorkWith.get(position);
                }

                holder.cb.setChecked(getSet.isSelected());

                holder.tvValue.setVisibility(View.GONE);

                holder.cb.setAllCaps(true);
                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));
                holder.cb.setText(getSet.getName() + " (" + getSet.getDesignation() + ")");

                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        getSet.setSelected(isChecked);
                        if (isForSearch) {
                            listWorkWithSearch.set(position, getSet);
                        } else {
                            listWorkWith.set(position, getSet);
                        }

                    }
                });

            }
        }

        public String getSelectedDrIds() {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listDoctor.size(); i++) {
                if (listDoctor.get(i).isSelected()) {
                    if (str.length() == 0) {
                        str.append(listDoctor.get(i).getDoctor_id());
                    } else {
                        str.append("," + listDoctor.get(i).getDoctor_id());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedDrNames() {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listDoctor.size(); i++) {
                if (listDoctor.get(i).isSelected()) {
                    if (str.length() == 0) {
                        str.append(listDoctor.get(i).getDoctor());
                    } else {
                        str.append("," + listDoctor.get(i).getDoctor());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedWorkWitIds() {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listWorkWith.size(); i++) {
                if (listWorkWith.get(i).isSelected()) {
                    if (str.length() == 0) {
                        str.append(listWorkWith.get(i).getStaff_id());
                    } else {
                        str.append("," + listWorkWith.get(i).getStaff_id());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedWorkWithName() {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listWorkWith.size(); i++) {
                if (listWorkWith.get(i).isSelected()) {
                    if (str.length() == 0) {
                        str.append(listWorkWith.get(i).getName());
                    } else {
                        str.append("," + listWorkWith.get(i).getName());
                    }
                }
            }

            return String.valueOf(str);
        }

        @Override
        public int getItemCount() {
            if (isFor.equalsIgnoreCase(EMPLOYEE)) {
                if (isForSearch) {
                    return listEmployeeSearch.size();
                } else {
                    return listEmployee.size();
                }
            } else if (isFor.equalsIgnoreCase(AREA)) {
                if (isForSearch) {
                    return listAreaSearch.size();
                } else {
                    return listArea.size();
                }
            } else if (isFor.equalsIgnoreCase(SPECIALITY)) {
                if (isForSearch) {
                    return listSpecialitySearch.size();
                } else {
                    return listSpeciality.size();
                }
            } else if (isFor.equalsIgnoreCase(DOCTOR)) {
                if (isForSearch) {
                    return listDoctorSearch.size();
                } else {
                    return listDoctor.size();
                }
            } else if (isFor.equalsIgnoreCase(WORKWITH)) {
                if (isForSearch) {
                    return listWorkWithSearch.size();
                } else {
                    return listWorkWith.size();
                }
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

    private void AppendListForArea(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea, EditText editText) {
        listDialogAdapter = new DialogListAdapter(dialog, isFor, true, "", rvArea);
        rvArea.setAdapter(listDialogAdapter);
        listDialogAdapter.notifyDataSetChanged();
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment(edtPlanDate, selectedStaffId);
        newFragment.show(activity.getFragmentManager(), "datePicker");
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener {
        private EditText editText;
        private String sId;

        @SuppressLint("ValidFragment")
        public DatePickerFragment(EditText editText, String sid) {
            this.editText = editText;
            this.sId = sid;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datepicker = new DatePickerDialog(getActivity(), this, year, month, day);
            Calendar calendar = Calendar.getInstance();
            SessionManager sessionManager = new SessionManager(getActivity());
            if(!sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
            {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            try
            {
                //Calendar cal = Calendar.getInstance();
                Date tomorrow = calendar.getTime();
                datepicker.getDatePicker().setMinDate(tomorrow.getTime()-1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String str = (selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
            SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            Date d;
            try {
                d = sdf.parse(str);
                sdf.applyPattern("yyyy-MM-dd");
                selectedTime = d.getTime();
                dateString = sdf.format(d);
                editText.setText(sdf.format(d));
                if (!sId.equalsIgnoreCase(""))
                {
                    if (handler != null) {
                        Message message = Message.obtain();
                        message.what = 121;
                        handler.sendMessage(message);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
        }
    }

    public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {
        List<PlannerEntryResponse.PlanBean> listItems;

        PlannerAdapter(List<PlannerEntryResponse.PlanBean> list, RecyclerView recyclerView) {
            this.listItems = list;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator, recyclerView);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_planner_entry_table, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final PlannerEntryResponse.PlanBean getSet = listItems.get(position);

            if (position == 0) {
                holder.llTitle.setVisibility(View.VISIBLE);
            } else {
                holder.llTitle.setVisibility(View.GONE);
            }

            int srNo = position + 1;
            holder.tvSrno.setText(String.valueOf(srNo));
            holder.tvDrId.setText(getSet.getDoctor_id());
            holder.tvDrName.setText(getSet.getDoctor());
            holder.tvWorkWith.setText(getSet.getWork_with());
            holder.tvAddedDate.setText(getSet.getAdded_date());

            if (getSet.getMon_business() == 0) {
                holder.tvMonBusiness.setText(" - ");
            } else {
                holder.tvMonBusiness.setText(String.valueOf(getSet.getMon_business()));
            }

            if (getSet.getCategory().equals("")) {
                holder.tvCategory.setText(" - ");
            } else {
                holder.tvCategory.setText(getSet.getCategory());
            }

            if (getSet.getFocus_enhence().length() == 0) {
                holder.tvFocusEnhance.setText(" - ");
            } else {
                holder.tvFocusEnhance.setText(getSet.getFocus_enhence());
            }

            if (getSet.getFocus_new().length() == 0) {
                holder.tvFocusNew.setText(" - ");
            } else {
                holder.tvFocusNew.setText(getSet.getFocus_new());
            }

            if (getSet.getLast_visit().equals("")) {
                holder.tvLastVisited.setText(" - ");
            } else {
                holder.tvLastVisited.setText(getSet.getLast_visit());
            }

            if (getSet.getApproved_by().equals("")) {
                holder.tvIsApprovedBy.setText(" - ");
            } else {
                holder.tvIsApprovedBy.setText(getSet.getApproved_by());
            }

            if (getSet.getIs_approved().equals("") || getSet.getIs_approved().equalsIgnoreCase("No")) {
                holder.ivDelete.setVisibility(View.VISIBLE);
            } else if (getSet.getIs_approved().equalsIgnoreCase("Yes")) {
                holder.ivDelete.setVisibility(View.GONE);
            }

            holder.tvIsApproved.setText(getSet.getIs_approved());
            holder.tvConfirm.setText(getSet.getIs_confirmed());
            mAnimator.onSpringItemBind(holder.itemView, position);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showConfirmationDialog(position, getSet.getMap_id());
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvSrno)
            TextView tvSrno;
            @BindView(R.id.tvDrId)
            TextView tvDrId;
            @BindView(R.id.tvDrName)
            TextView tvDrName;
            @BindView(R.id.tvWorkWith)
            TextView tvWorkWith;
            @BindView(R.id.tvAddedDate)
            TextView tvAddedDate;
            @BindView(R.id.tvMonBusiness)
            TextView tvMonBusiness;
            @BindView(R.id.tvCategory)
            TextView tvCategory;
            @BindView(R.id.tvLastVisited)
            TextView tvLastVisited;
            @BindView(R.id.tvConfirm)
            TextView tvConfirm;
            @BindView(R.id.tvIsApproved)
            TextView tvIsApproved;
            @BindView(R.id.tvIsApprovedBy)
            TextView tvIsApprovedBy;
            @BindView(R.id.llTitle)
            LinearLayout llTitle;
            @BindView(R.id.tvFocusNew)
            TextView tvFocusNew;
            @BindView(R.id.tvFocusEnhance)
            TextView tvFocusEnhance;

            @BindView(R.id.ivDelete)
            ImageView ivDelete;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }

    }

    private void showConfirmationDialog(final int pos, final String mapId) {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtHeader2 = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Delete");
            txtHeader2.setText("Are you sure you want to delete?");

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                        dialog.cancel();

                        llLoadingTransparent.setVisibility(View.VISIBLE);
                        Call<CommonResponse> getPlanner = apiService.deletePlan(mapId, sessionManager.getUserId());
                        getPlanner.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                if (response.body().getSuccess() == 1)
                                {
                                    listPlan.remove(pos);
                                    if (listPlan.size() == 0)
                                    {
                                        tvNoPlan.setVisibility(View.VISIBLE);
                                        tvConfirmPlan.setVisibility(View.GONE);
                                        getAreaFromEmployee(false);
                                    }
                                    else
                                    {
                                        tvNoPlan.setVisibility(View.GONE);
                                        tvConfirmPlan.setVisibility(View.VISIBLE);
                                    }

                                    if (plannerAdapter != null) {
                                        plannerAdapter.notifyDataSetChanged();
                                    }
                                }
                                else
                                {
                                    AppUtils.showToast(activity, response.body().getMessage());
                                }
                                llLoadingTransparent.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                llLoadingTransparent.setVisibility(View.GONE);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        dialog.cancel();
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
