package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.CalendarDetailsActivity;
import com.unisonpharmaceuticals.activity.ViewTourPlanActivity;
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
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kiran Patel on 20-Aug-18.
 */
public class FragmentCalendar extends Fragment implements View.OnClickListener
{
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
    /*@BindView(R.id.edtYear)
    EditText edtYear;*/
    public  static EditText edtYear;
    @BindView(R.id.tvCreationDate)
    TextView tvCreationDate;
    @BindView(R.id.llBottomSection)
    LinearLayout llBottomSection;
    @BindView(R.id.tvGenerateForm)
    TextView tvGenerateForm;
    @BindView(R.id.tvResetForm)
    TextView tvResetForm;
    @BindView(R.id.tvViewTourPlan)
    TextView tvViewTourPlan;

    @BindView(R.id.tvAppRoveTourPlan) TextView tvAppRoveTourPlan;
    @BindView(R.id.tvMainSubmit) TextView tvMainSubmit;

    @BindView(R.id.calendar)com.github.sundeepk.compactcalendarview.CompactCalendarView calendar;

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

    public static String workWithString = "",selectedStaffId  = "",monthNumber = "",currentYear = "";

    private boolean isFormLoadFirstTime = false;

    private final String EMPLOYEE = "Employee";
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private final String AREA = "Area";

    private final String BASIC_DATE_FORMAT = "yyyy-MM-dd";
    public static int mainSelectedPosition = 0;

    public static Date dateForExtra;

    private String clickedDate = "";

    public static Handler handler;

    TPFormResponse.ExtradaysBean editBean;

    private boolean isForMoveToNext = false,showApproveButton = false,isLoading = false;
    private String dateForNextMove = "";

    private SpringyAdapterAnimator mAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(this, rootView);
        initViews();
        onClick(rootView);

        Calendar c = Calendar.getInstance();
        //dateForExtra = c.getTime();

        calendar.setUseThreeLetterAbbreviation(true);
        calendar.setCurrentDate(new Date());
        calendar.isScrollEnable(false);
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener()
        {
            @Override
            public void onDayClick(Date dateClicked)
            {
                //workWithString = "";

                long l = dateClicked.getTime();
                clickedDate = AppUtils.getDateCurrentTimeString(l/1000);
                dateForExtra = dateClicked;
                getDayDataFromList(dateClicked);

                Format formatter = new SimpleDateFormat("dd");
                String selectedDate = formatter.format(dateClicked);

                mainSelectedPosition = Integer.parseInt(selectedDate)-1;

                startActivity(new Intent(activity, CalendarDetailsActivity.class));
                AppUtils.startActivityAnimation(activity);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
            }
        });

        if(sessionManager.isNetworkAvailable())
        {
            getEmployeeList();
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
            activity.finish();
        }

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg)
            {
                if(msg.what==100)
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        isForMoveToNext = true;
                        dateForNextMove = String.valueOf(msg.obj);
                        Log.e("NextDate >> ", "handleMessage: " + dateForNextMove );
                        getFormData(edtYear.getText().toString().trim(),monthNumber);
                    }
                }
                else if(msg.what==101)
                {
                    llBottomSection.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return rootView;
    }

    private void getEmployeeList()
    {
        isLoading = true;

        llLoadingTransparent.setVisibility(View.VISIBLE);
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
                        }

                    }
                    else
                    {
                        AppUtils.showToast(activity, "Could not get employee list.");
                        activity.finish();
                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                llLoadingTransparent.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t)
            {
                llLoadingTransparent.setVisibility(View.GONE);
                AppUtils.showToast(activity, "Could not get employee list.");
                activity.finish();
                isLoading = false;
            }
        });

        llLoadingTransparent.setVisibility(View.VISIBLE);

        isLoading = true;
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
                        edtYear.setText(String.valueOf(listYear.get(0).getYear()));
                        getMonthData();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,"No year data found");
                }
                isLoading = false;
                llLoadingTransparent.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No year data found");
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }
        });

        llLoadingTransparent.setVisibility(View.VISIBLE);

        isLoading = true;
        Call<TourAreaResponse> areaCall = apiService.getTPArea(sessionManager.getUserId(),sessionManager.getUserId());
        areaCall.enqueue(new Callback<TourAreaResponse>() {
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
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No data found");
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }
        });

        llLoadingTransparent.setVisibility(View.VISIBLE);
        isLoading = true;
        Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(),sessionManager.getUserId(),"false");
        workWithCall.enqueue(new Callback<WorkWithResponse>() {
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

                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                llLoadingTransparent.setVisibility(View.GONE);
                isLoading = false;
            }
        });

        llLoadingTransparent.setVisibility(View.GONE);
    }

    private void initViews()
    {
        edtYear = (EditText) rootView.findViewById(R.id.edtYear);
        edtMonth.setOnClickListener(this);
        edtEmployee.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        tvResetForm.setOnClickListener(this);
        tvGenerateForm.setOnClickListener(this);
        llLoadingTransparent.setOnClickListener(this);
        tvViewTourPlan.setOnClickListener(this);

        tvAppRoveTourPlan.setOnClickListener(this);
        tvMainSubmit.setOnClickListener(this);
    }
    private void getDayDataFromList(Date date)
    {
        try
        {

            /*Format formatter = new SimpleDateFormat("dd");
            String selectedDate = formatter.format(date);

            mainSelectedPosition = Integer.parseInt(selectedDate)-1;*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
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
                    /*String startDateString = edtYear.getText().toString()+"-"+monthNumber+"-"+"01";
                    DateFormat df = new SimpleDateFormat(BASIC_DATE_FORMAT);
                    Date startDate;
                    try {
                        startDate = df.parse(startDateString);
                        calendar.setCurrentDate(startDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/

                    isFormLoadFirstTime = true;
                    getFormData(edtYear.getText().toString().trim(),monthNumber);
                }
                break;
            case R.id.tvAppRoveTourPlan:
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
                        approveTP();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvMainSubmit:
                showConfirmDialog();
                break;
            case R.id.tvViewTourPlan:
                Intent intent = new Intent(activity, ViewTourPlanActivity.class);
                intent.putExtra("emp_id",selectedStaffId);
                intent.putExtra("year",edtYear.getText().toString().trim());
                intent.putExtra("month",monthNumber);
                intent.putExtra("showApprove",showApproveButton);
                activity.startActivity(intent);
                AppUtils.startActivityAnimation(activity);
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
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private void getMonthData()
    {
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
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No month data found");
                llLoadingTransparent.setVisibility(View.GONE);
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
                                   final String dates)
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
                    hashMap.put("login_user_id",sessionManager.getUserId());

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
                    //monthNumber = "";
                    llBottomSection.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
    }

    private void passJsonToSaveEntry()
    {
        String finalJsonString = "";
        try
        {
            JSONObject mainObject = new JSONObject();
            mainObject.put("employee",sessionManager.getUserId());
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
                            if(!listTpEntry.get(i).getRoute_area_id().equals("") && !listTpEntry.get(i).getArea().equals(""))
                            {
                                areaStr = listTpEntry.get(i).getRoute_area_id()+"-"+listTpEntry.get(i).getArea();
                            }
                            else
                            {
                                areaStr = "";
                            }

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

            finalJsonString = mainObject.toString();

            saveTourPlan(sessionManager.getUserId(),
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
        String finalJsonString = "";
        try
        {
            JSONObject mainObject = new JSONObject();
            mainObject.put("employee",sessionManager.getUserId());
            mainObject.put("year",edtYear.getText().toString());// change this
            mainObject.put("month",monthNumber);// change this

            {
                JSONArray areaObj = new JSONArray();
                for (int i = 0; i < listExtraEntry.size(); i++)
                {
                    try
                    {
                        String areaStr = "";
                        if(!listExtraEntry.get(i).getRoute_area_id().equals("") && !listExtraEntry.get(i).getArea_id().equals(""))
                        {
                            areaStr = listExtraEntry.get(i).getRoute_area_id()+"-"+listExtraEntry.get(i).getArea_id();
                        }
                        areaObj.put(areaStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mainObject.put("area_id",areaObj);
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mainObject.put("staff_id",staffObject);
            }

            finalJsonString = mainObject.toString();

            Log.e("FINAL JSON STRING >> ", "passJsonToSaveExtraEntry: "+finalJsonString.toString() );

            saveExtraTourPlan(sessionManager.getUserId(),
                    edtYear.getText().toString(),
                    monthNumber,
                    mainObject.getJSONArray("area_id").toString(),
                    mainObject.getJSONArray("remark").toString(),
                    mainObject.getString("staff_id").toString(),
                    mainObject.getJSONArray("date").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getFormData(final String year, final String month)
    {
        Call<TPFormResponse> areaCall = apiService.getTPForm(selectedStaffId,month,"",year,sessionManager.getUserId());
        llLoadingTransparent.setVisibility(View.VISIBLE);
        areaCall.enqueue(new Callback<TPFormResponse>()
        {
            @Override
            public void onResponse(Call<TPFormResponse> call, Response<TPFormResponse> response)
            {
                listTpEntry = new ArrayList<>();
                listExtraEntry = new ArrayList<>();

                if(response.body().getSuccess()==1)
                {
                    listTpEntry = (ArrayList<TPFormResponse.DaysBean>) response.body().getDays();
                    listExtraEntry = (ArrayList<TPFormResponse.ExtradaysBean>) response.body().getExtradays();


                    //For automatic dispaly details when came first time and do not click any day of calendar

                    try
                    {
                        String startDateString = edtYear.getText().toString()+"-"+monthNumber+"-"+"01";
                        if(isForMoveToNext)
                        {
                            startDateString = AppUtils.universalDateConvert(dateForNextMove,"dd MMM, yyyy","yyyy-MM-dd");
                        }
                        else
                        {
                            startDateString = edtYear.getText().toString()+"-"+monthNumber+"-"+"01";
                        }

                        Log.e("startString >> ", "onResponse: " + startDateString);

                        DateFormat df = new SimpleDateFormat(BASIC_DATE_FORMAT);
                        Date startDate = new Date();
                        startDate = df.parse(startDateString);
                        clickedDate = AppUtils.getDateCurrentTimeString(startDate.getTime()/1000);
                        calendar.setCurrentDate(startDate);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    calendar.removeAllEvents();
                    if(listTpEntry.size()>0)
                    {
                        listDatesNo = new ArrayList<>();
                        for (int i = 0; i < listTpEntry.size(); i++)
                        {
                            if(!listTpEntry.get(i).getDay_id().equalsIgnoreCase(""))
                            {
                                Event event = new Event(Color.parseColor("#019ce4"),listTpEntry.get(i).getDate()*1000);
                                calendar.addEvent(event);
                            }
                        }

                    }
                    if(listExtraEntry.size()>0)
                    {
                        for (int i = 0; i < listExtraEntry.size(); i++)
                        {
                            TPFormResponse.ExtradaysBean getSet = listExtraEntry.get(i);

                            if(!getSet.getDay_id().equalsIgnoreCase(""))
                            {
                                Event event = new Event(Color.parseColor("#00b578"),Long.parseLong(listExtraEntry.get(i).getDate())*1000);
                                calendar.addEvent(event);
                            }
                        }

                    }

                    llBottomSection.setVisibility(View.VISIBLE);
                    llLoadingTransparent.setVisibility(View.GONE);

                    if(!sessionManager.getUserId().equalsIgnoreCase(selectedStaffId))
                    {
                        tvMainSubmit.setVisibility(View.GONE);
                        tvAppRoveTourPlan.setVisibility(View.GONE);
                        showApproveButton = true;
                    }
                    else
                    {
                        tvMainSubmit.setVisibility(View.VISIBLE);
                        tvAppRoveTourPlan.setVisibility(View.GONE);
                        showApproveButton = false;
                    }
                }
                else
                {
                    llLoadingTransparent.setVisibility(View.GONE);
                    AppUtils.showToast(activity,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<TPFormResponse> call, Throwable t)
            {
                Log.e("Exception > ", "onFailure: " + t.getMessage() );
                AppUtils.showToast(activity,"No data found");
                llLoadingTransparent.setVisibility(View.GONE);
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
    public void showListDialog(final String isFor, final TextView editText, final int listPostion)
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

        listDialogAdapter = new DialogListAdapter(listDialog, isFor,false,"",editText,listPostion,rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(listDialogAdapter);

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

        if(isFor.equalsIgnoreCase(MONTH))
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
                else if(isFor.equals(AREA))
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

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";
        TextView textView;
        int listPosition = 0;
        boolean isDone = false;

        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyclerView)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator,recyclerView);
        }
        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,TextView textView,int listPosition,RecyclerView recyclerView)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.textView = textView;
            this.listPosition = listPosition;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator,recyclerView);
        }

        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            mAnimator.onSpringItemCreate(v);
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
            mAnimator.onSpringItemBind(holder.itemView,position);
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
                        llBottomSection.setVisibility(View.GONE);
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
                        getMonthData();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
        }

        public String getSelectedWorkWitIds()
        {
            String str = "";

            for (int i = 0; i < listWorkWith.size(); i++)
            {
                if(listWorkWith.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str = listWorkWith.get(i).getStaff_id();
                    }
                    else
                    {
                        str = str + "," + listWorkWith.get(i).getStaff_id();
                    }
                }
            }

            return str;
        }

        public void disSelectAllWorkWithId()
        {
            if(listWorkWith!=null && listWorkWith.size()>0)
            {
                for (int i = 0; i < listWorkWith.size(); i++)
                {
                    WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                    bean.setSelected(false);
                    listWorkWith.set(i,bean);
                }
            }
        }

        public String getSelectedWorkWithName()
        {
            String str = "";

            if(mainSelectedPosition == listPosition)
            {
                for (int i = 0; i < listWorkWith.size(); i++)
                {
                    if(listWorkWith.get(i).isSelected())
                    {
                        if(str.length()==0)
                        {
                            str = listWorkWith.get(i).getName();
                        }
                        else
                        {
                            str = str + "," + listWorkWith.get(i).getName();
                        }
                    }
                }

            }
            return str;
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

    private void AppendListForArea(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea)
    {
        listDialogAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
        rvArea.setAdapter(listDialogAdapter);
        listDialogAdapter.notifyDataSetChanged();
    }
}
