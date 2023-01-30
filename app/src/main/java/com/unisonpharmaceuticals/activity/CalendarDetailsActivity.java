package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Message;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentCalendar;
import com.unisonpharmaceuticals.model.*;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarDetailsActivity extends AppCompatActivity implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llLoadingTransparent)
    LinearLayout llLoadingTransparent;

    @BindView(R.id.tvSaveSingle)
    TextView tvSaveSingle;

    @BindView(R.id.fabAdd)ImageView fabAdd;
    @BindView(R.id.llWorkWith)LinearLayout llWorkWith;
    @BindView(R.id.llTPArea)LinearLayout llTPArea;
    @BindView(R.id.llRemarks)LinearLayout llRemarks;
    @BindView(R.id.tvWorkWith)TextView tvWorkWith;
    @BindView(R.id.tvTPArea)TextView tvTPArea;
    @BindView(R.id.edtRemark)EditText edtRemark;
    @BindView(R.id.cbLeave)
    CheckBox cbLeave;
    @BindView(R.id.cbHoliday) CheckBox cbHoliday;
    @BindView(R.id.rvExtraEntry) RecyclerView rvExtraEntry;
    @BindView(R.id.llTitleExtra) LinearLayout llTitleExtra;

    private TextView tvDateTitle;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<WorkWithResponse.StaffBean> listWorkWith = new ArrayList<>();
    private ArrayList<WorkWithResponse.StaffBean> listWorkWithSearch = new ArrayList<>();

    private List<YearResponse.YearListBean> listYear = new ArrayList<>();
    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();

    private ArrayList<TPFormResponse.ExtradaysBean> listExtraEntry = new ArrayList<>();
    private ArrayList<TPFormResponse.DaysBean> listTpEntry = new ArrayList<>();

    private ArrayList<TPFormResponse.ExtradaysBean> listNewExtra = new ArrayList<>();

    private ArrayList<DayGetSet> listDatesNo = new ArrayList<>();

    private List<TourAreaResponse.TourAreaBean> listArea = new ArrayList<>();
    private List<TourAreaResponse.TourAreaBean> listAreaSearch = new ArrayList<>();

    private String workWithString = "";

    private boolean isFormLoadFirstTime = false;

    private final String AREA = "Area";
    private final String WW = "Work With";
    private final String EXTRA_DATE = "Extra Date";
    private final String EXTRA_AREA = "Extra Area";
    private final String EXTRA_WW = "Extra Work With";

    private final String BASIC_DATE_FORMAT = "yyyy-MM-dd";

    private int mainSelectedPosition = 0;

    private String clickedDate = "";

    TPFormResponse.ExtradaysBean editBean;

    private ExtraEntryAdapter extraEntryAdapter;

    private boolean isEditClick = false,isLoading = false;

    private int loadingCounts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_details);
        activity = CalendarDetailsActivity.this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        mainSelectedPosition = FragmentCalendar.mainSelectedPosition;

        Log.e("MainSelectiopn Postion ", "onCreate: " + mainSelectedPosition );

        ButterKnife.bind(this);
        initViews();
        if(sessionManager.isNetworkAvailable())
        {
            getFormData(FragmentCalendar.edtYear.getText().toString().trim(),FragmentCalendar.monthNumber);
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
    }
    private void initViews()
    {
        tvDateTitle = (TextView) findViewById(R.id.txtTitle);
        try
        {
            String currentdate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(FragmentCalendar.dateForExtra.toString()));
            tvDateTitle.setText(currentdate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llBack).setOnClickListener(this);

        rvExtraEntry.setLayoutManager(new LinearLayoutManager(activity));

        llLoadingTransparent.setOnClickListener(this);
        fabAdd.setOnClickListener(this);
        llWorkWith.setOnClickListener(this);
        llRemarks.setOnClickListener(this);
        llTPArea.setOnClickListener(this);
        tvSaveSingle.setOnClickListener(this);

        cbLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    cbHoliday.setEnabled(false);
                    llWorkWith.setEnabled(false);
                    llWorkWith.setClickable(false);
                    llTPArea.setEnabled(false);
                    llTPArea.setClickable(false);

                    listTpEntry.get(mainSelectedPosition).setWork_withName("");
                    listTpEntry.get(mainSelectedPosition).setWork_withString("");
                    listTpEntry.get(mainSelectedPosition).setArea_name("");
                    listTpEntry.get(mainSelectedPosition).setArea("");
                    tvWorkWith.setText("Work with self");
                    tvTPArea.setText("Click to select area");

                    fabAdd.setVisibility(View.GONE);
                }
                else
                {

                    cbHoliday.setEnabled(true);
                    llWorkWith.setEnabled(true);
                    llWorkWith.setClickable(true);
                    llTPArea.setEnabled(true);
                    llTPArea.setClickable(true);

                    fabAdd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    cbLeave.setEnabled(false);
                    llWorkWith.setEnabled(false);
                    llWorkWith.setClickable(false);
                    llTPArea.setEnabled(false);
                    llTPArea.setClickable(false);

                    listTpEntry.get(mainSelectedPosition).setWork_withName("");
                    listTpEntry.get(mainSelectedPosition).setWork_withString("");
                    listTpEntry.get(mainSelectedPosition).setArea_name("");
                    listTpEntry.get(mainSelectedPosition).setArea("");
                    tvWorkWith.setText("Work with self");
                    tvTPArea.setText("Click to select area");

                    fabAdd.setVisibility(View.GONE);
                }
                else
                {
                    cbLeave.setEnabled(true);
                    llWorkWith.setEnabled(true);
                    llWorkWith.setClickable(true);
                    llTPArea.setEnabled(true);
                    llTPArea.setClickable(true);

                    fabAdd.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    private void getDayDataFromList(Date date)
    {
        try
        {

            Format formatter = new SimpleDateFormat("dd");
            String selectedDate = formatter.format(date);

            //mainSelectedPosition = Integer.parseInt(selectedDate)-1;

            TPFormResponse.DaysBean bean = listTpEntry.get(mainSelectedPosition);

            Log.e("WWW>>>>>>> ", "getDayDataFromList: "+bean.getWork_withName() );

            if(bean.getWork_withName().trim().equalsIgnoreCase(""))
            {
                tvWorkWith.setText("Work with self");
            }
            else
            {
                tvWorkWith.setText(bean.getWork_withName());
            }

            if(bean.getArea_name().equalsIgnoreCase(""))
            {
                tvTPArea.setText("Click to select area");
            }
            else
            {
                tvTPArea.setText(bean.getArea_name());
            }

            edtRemark.setText(bean.getRemark());
            edtRemark.setSelection(bean.getRemark().length());

            if(bean.getLeave().equalsIgnoreCase("true"))
            {
                cbLeave.setChecked(true);
            }
            else
            {
                cbLeave.setChecked(false);
            }

            if(bean.getHoliday().equalsIgnoreCase("true"))
            {
                cbHoliday.setChecked(true);
            }
            else
            {
                cbHoliday.setChecked(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){

            /*case R.id.tvMainSave:
                if(sessionManager.isNetworkAvailable())
                {
                    passJsonToSaveEntry();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;*/

           /* case R.id.tvMainReset:
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

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;*/

            case R.id.llBack:
                AppUtils.hideKeyboard(edtRemark,activity);
                activity.finish();
                AppUtils.finishActivityAnimation(activity);
                break;
            case R.id.llWorkWith:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(WW,tvWorkWith,mainSelectedPosition);
                }
                break;
            case R.id.llTPArea:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    if(listArea.size()>0)
                    {
                        showListDialog(AREA,tvTPArea,mainSelectedPosition);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No area available");
                    }
                }
                break;
            case R.id.llRemarks:
                break;
            case R.id.fabAdd:

                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    isEditClick = false;
                    showExtraRouteDialog();
                }

                break;
            case R.id.tvSaveSingle:
                if(sessionManager.isNetworkAvailable())
                {
                    AppUtils.hideKeyboard(tvSaveSingle,activity);
                    TPFormResponse.DaysBean bean = listTpEntry.get(mainSelectedPosition);
                    String leave = "", holiday = "", extra = "";
                    if (cbLeave.isChecked()) {
                        leave = "1";
                    } else {
                        leave = "0";
                    }

                    if (cbHoliday.isChecked()) {
                        holiday = "1";
                    } else {
                        holiday = "0";
                    }

                    if(cbHoliday.isChecked() || cbLeave.isChecked())
                    {
                        saveSingleDay(FragmentCalendar.selectedStaffId,
                                FragmentCalendar.edtYear.getText().toString(),
                                FragmentCalendar.monthNumber,
                                bean.getDate(),
                                "",
                                edtRemark.getText().toString(),
                                leave,
                                holiday,
                                "0",
                                "",
                                listTpEntry.get(mainSelectedPosition).getDay_id());
                    }
                    else
                    {
                        if (tvTPArea.getText().toString().equalsIgnoreCase("") ||
                                tvTPArea.getText().toString().equalsIgnoreCase("Click to select area"))
                        {
                            AppUtils.showToast(activity, "Please select TP Area");
                        }
                        else
                        {
                            saveSingleDay(FragmentCalendar.selectedStaffId,
                                    FragmentCalendar.edtYear.getText().toString(),
                                    FragmentCalendar.monthNumber,
                                    bean.getDate(),
                                    bean.getArea(),
                                    edtRemark.getText().toString(),
                                    leave,
                                    holiday,
                                    "0",
                                    bean.getWork_withString(),
                                    listTpEntry.get(mainSelectedPosition).getDay_id());
                        }
                    }
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
            case R.id.llLoadingTransparent:
                break;
        }
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
                    if(FragmentCalendar.currentYear.equalsIgnoreCase(FragmentCalendar.edtYear.getText().toString().trim()))
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

    LinearLayout d_llWorkWith,d_llTPArea,d_llRemarks;
    TextView d_tvWorkWith,d_tvTPArea,d_tvSubmit,d_tvCancel;
    EditText d_edtRemark;

    private void showExtraRouteDialog()
    {
        try
        {
            //kiran

            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_extra_route, null);
            dialog.setContentView(sheetView);

            LinearLayout llTitleExtra = (LinearLayout) dialog.findViewById(R.id.llTitleExtra);

            TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            String currentdate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(FragmentCalendar.dateForExtra.toString()));
            tvTitle.setText("Add extra route for "+currentdate);

            d_llWorkWith = (LinearLayout) dialog.findViewById(R.id.llWorkWith);
            d_llTPArea = (LinearLayout) dialog.findViewById(R.id.llTPArea);
            d_llRemarks = (LinearLayout) dialog.findViewById(R.id.llRemarks);

            d_tvWorkWith = (TextView) dialog.findViewById(R.id.tvWorkWith);
            d_tvTPArea = (TextView) dialog.findViewById(R.id.tvTPArea);
            d_tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
            d_tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

            d_edtRemark = (EditText) dialog.findViewById(R.id.edtRemark);

            editBean = listExtraEntry.get(mainSelectedPosition);

            Format formatter = new SimpleDateFormat("dd");
            String selectedDate = formatter.format(FragmentCalendar.dateForExtra);

            d_llWorkWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    showListDialog(EXTRA_WW,d_tvWorkWith,mainSelectedPosition);
                }
            });

            d_llTPArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(listArea.size()>0)
                    {
                        showListDialog(EXTRA_AREA,d_tvTPArea,mainSelectedPosition);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No area available");
                    }

                }
            });

            d_tvCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            d_tvSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        if (d_tvTPArea.getText().toString().equalsIgnoreCase("") || d_tvTPArea.getText().toString().equalsIgnoreCase("Click to select area"))
                        {
                            AppUtils.showToast(activity, "Please select TP Area");
                        }
                        else
                        {

                            String dayId = "";
                            if(isEditClick)
                            {
                                dayId = editBean.getDay_id();
                            }
                            else
                            {
                                dayId = "";
                            }

                            AppUtils.hideKeyboard(d_tvWorkWith, activity);
                            dialog.dismiss();
                            dialog.cancel();
                            saveSingleDay(FragmentCalendar.selectedStaffId,
                                    FragmentCalendar.edtYear.getText().toString(),
                                    FragmentCalendar.monthNumber,
                                    listTpEntry.get(mainSelectedPosition).getDate(),
                                    editBean.getArea_id(),
                                    d_edtRemark.getText().toString(),
                                    "0",
                                    "0",
                                    "1",
                                    editBean.getWork_withString(),
                                    dayId);
                        }
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
                    FragmentCalendar.monthNumber = "";
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
            mainObject.put("year",FragmentCalendar.edtYear.getText().toString());// change this
            mainObject.put("month",FragmentCalendar.monthNumber);// change this

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
                    FragmentCalendar.edtYear.getText().toString(),
                    FragmentCalendar.monthNumber,
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
            mainObject.put("year",FragmentCalendar.edtYear.getText().toString());// change this
            mainObject.put("month",FragmentCalendar.monthNumber);// change this

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
                    FragmentCalendar.edtYear.getText().toString(),
                    FragmentCalendar.monthNumber,
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

        isLoading = true;
        listWorkWith = new ArrayList<>();
        Call<WorkWithResponse> workWithCall = apiService.getTourplanWW(FragmentCalendar.selectedStaffId,sessionManager.getUserId());
        workWithCall.enqueue(new Callback<WorkWithResponse>() {
            @Override
            public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                try {
                    if (response.body().getSuccess() == 1)
                    {
                        listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                    isLoading = false;
                    loadingCounts = loadingCounts + 1;
                    manageLoadingView(loadingCounts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                loadingCounts = loadingCounts - 1;
                manageLoadingView(loadingCounts);
            }
        });

        isLoading = true;
        Call<TourAreaResponse> areaCall = apiService.getTPArea(FragmentCalendar.selectedStaffId,sessionManager.getUserId());
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
                isLoading = false;
                loadingCounts = loadingCounts + 1;
                manageLoadingView(loadingCounts);
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t)
            {
                Log.e("listArea", "onFailure: 3 "+listArea.size() + " "+t.getMessage());
                isLoading = false;
                AppUtils.showToast(activity,"No data found");

                loadingCounts = loadingCounts - 1;
                manageLoadingView(loadingCounts);
            }
        });
        isLoading = true;
        Call<TPFormResponse> formCall = apiService.getTPForm(FragmentCalendar.selectedStaffId,month,"",year,sessionManager.getUserId());
        formCall.enqueue(new Callback<TPFormResponse>()
        {
            @Override
            public void onResponse(Call<TPFormResponse> call, Response<TPFormResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listTpEntry.clear();
                    listExtraEntry.clear();

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
                                listDatesNo.add(dayGetSet);

                            }

                            TPFormResponse.DaysBean getSet = listTpEntry.get(i);

                            if(getSet.getWork_with().size()>0)
                            {
                                String names = "",ids = "";
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
                                getSet.setWork_withName(names);
                            }

                            String date_no = AppUtils.getDateFromTimestamp(listTpEntry.get(i).getDate());
                            getSet.setDate_no(AppUtils.universalDateConvert(date_no,"yyyy-MM-dd","dd"));

                            listTpEntry.set(i,getSet);

                        }
                        getDayDataFromList(FragmentCalendar.dateForExtra);
                    }
                    if(listExtraEntry.size()>0)
                    {
                        for (int i = 0; i < listExtraEntry.size(); i++)
                        {
                            TPFormResponse.ExtradaysBean getSet = listExtraEntry.get(i);
                            //check
                            if(getSet.getWork_with().size()>0)
                            {
                                String names = "",ids = "";
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
                                        ids = ids + ","+getSet.getWork_with().get(j).getStaff_id();
                                    }
                                }
                                getSet.setWork_withString(ids);
                                getSet.setWork_withName(names);
                            }

                            listExtraEntry.set(i,getSet);
                        }

                    }

                    listNewExtra = new ArrayList<>();

                    Log.e("SELCTED STRING >> ", "showExtraRouteDialog: " + listTpEntry.get(mainSelectedPosition).getDate() );

                    for (int i = 0; i < listExtraEntry.size(); i++)
                    {
                        Log.e("EXTRA>  STRING >> ", "showExtraRouteDialog: " + listExtraEntry.get(i).getDate() );

                        if(listExtraEntry.get(i).getDate().equalsIgnoreCase(String.valueOf(listTpEntry.get(mainSelectedPosition).getDate())))
                        {
                            listNewExtra.add(listExtraEntry.get(i));
                        }
                    }

                    if(listNewExtra.size()>0)
                    {
                        llTitleExtra.setVisibility(View.VISIBLE);
                        extraEntryAdapter = new ExtraEntryAdapter(listNewExtra);
                        rvExtraEntry.setAdapter(extraEntryAdapter);
                    }
                    else
                    {
                        llTitleExtra.setVisibility(View.GONE);
                    }

                    TPFormResponse.DaysBean bean = listTpEntry.get(mainSelectedPosition);
                    if(bean.getDay_name().equalsIgnoreCase("Sun")||
                            bean.getDay_name().equalsIgnoreCase("Sunday") ||
                            bean.getHoliday().equalsIgnoreCase("true") ||
                            bean.getLeave().equalsIgnoreCase("true"))
                    {
                        cbHoliday.setEnabled(false);
                        cbLeave.setEnabled(false);
                    }
                    else
                    {
                        cbHoliday.setEnabled(true);
                        cbLeave.setEnabled(true);
                    }

                    if(bean.getHoliday().equalsIgnoreCase("true")
                            || bean.getLeave().equalsIgnoreCase("true"))
                    {
                        fabAdd.setVisibility(View.GONE);
                    }
                    else
                    {
                        fabAdd.setVisibility(View.VISIBLE);
                    }

                    llLoadingTransparent.setVisibility(View.GONE);
                }
                else
                {
                    llLoadingTransparent.setVisibility(View.GONE);
                    AppUtils.showToast(activity,response.body().getMessage());
                }
                isLoading = false;
                loadingCounts = loadingCounts + 1;
                manageLoadingView(loadingCounts);
            }

            @Override
            public void onFailure(Call<TPFormResponse> call, Throwable t)
            {
                Log.e("Exception > ", "onFailure: " + t.getMessage() );
                isLoading = false;
                AppUtils.showToast(activity,"No data found");
                llLoadingTransparent.setVisibility(View.GONE);
                loadingCounts = loadingCounts - 1;
                manageLoadingView(loadingCounts);
            }
        });

    }

    private void manageLoadingView(int counts)
    {
        if(counts==3)
        {
            llLoadingTransparent.setVisibility(View.GONE);
        }
        else
        {
            llLoadingTransparent.setVisibility(View.VISIBLE);
        }
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
                        confirmTourPlan(sessionManager.getUserId(),
                                FragmentCalendar.edtYear.getText().toString().trim(),
                                FragmentCalendar.monthNumber);
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
    public void showListDialog(final String isFor, final TextView textView, final int listPostion)
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
            public void onDismiss(DialogInterface dialog)
            {
                AppUtils.hideKeyboard(sheetView,activity);

                for (int i = 0; i < listWorkWith.size(); i++)
                {
                    WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                    bean.setSelected(false);
                    listWorkWith.set(i,bean);
                }

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

        listDialogAdapter = new DialogListAdapter(listDialog, isFor,false,"",textView,listPostion);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(listDialogAdapter);

        tvDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isFor.equalsIgnoreCase(WW))
                {
                    workWithString = listDialogAdapter.getSelectedWorkWitIds();
                    Log.e("WW STRING LENGTH >> ", "onClick: " + workWithString );
                    if(workWithString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one option.");
                    }
                    else
                    {
                        try {
                            TPFormResponse.DaysBean bean = listTpEntry.get(listPostion);
                            bean.setRemark(edtRemark.getText().toString());
                            List<String> list = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(mainSelectedPosition).getWork_withString());
                            List<String> listNames = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(mainSelectedPosition).getWork_withName());
                            String names = "",ids = "";
                            for (int i = 0; i < list.size(); i++)
                            {
                                if(names.equals(""))
                                {
                                    names = listNames.get(i);
                                }
                                else
                                {
                                    names = names + ","+listNames.get(i);
                                }

                                if(ids.equals(""))
                                {
                                    ids = list.get(i);
                                }
                                else
                                {
                                    ids = ids + ","+list.get(i);
                                }
                            }

                            bean.setWork_withString(listDialogAdapter.getSelectedWorkWitIds());
                            bean.setWork_withName(listDialogAdapter.getSelectedWorkWithName());
                            tvWorkWith.setText(bean.getWork_withName());
                            listTpEntry.set(listPostion,bean);

                            listDialog.dismiss();
                            listDialog.cancel();

                            listDialogAdapter.disSelectAllWorkWithId();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if(isFor.equalsIgnoreCase(EXTRA_WW)&&listDialogAdapter !=null)
                {
                    workWithString = listDialogAdapter.getSelectedWorkWitIds();
                    Log.e("WW STRING LENGTH >> ", "onClick: " + workWithString );
                    if(workWithString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one option.");
                    }
                    else
                    {
                        try {
                            TPFormResponse.ExtradaysBean bean = listExtraEntry.get(listPostion);
                            bean.setRemark(d_edtRemark.getText().toString());

                            List<String> list = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(mainSelectedPosition).getWork_withString());
                            List<String> listNames = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(mainSelectedPosition).getWork_withName());

                            String names = "",ids = "";
                            for (int i = 0; i < list.size(); i++)
                            {
                                if(names.equals(""))
                                {
                                    names = listNames.get(i);
                                }
                                else
                                {
                                    names = names + ","+listNames.get(i);
                                }

                                if(ids.equals(""))
                                {
                                    ids = list.get(i);
                                }
                                else
                                {
                                    ids = ids + ","+list.get(i);
                                }
                            }

                            bean.setWork_withString(listDialogAdapter.getSelectedWorkWitIds());
                            bean.setWork_withName(listDialogAdapter.getSelectedWorkWithName());
                            d_tvWorkWith.setText(bean.getWork_withName());
                            listExtraEntry.set(listPostion,bean);

                            listDialog.dismiss();
                            listDialog.cancel();

                            listDialogAdapter.disSelectAllWorkWithId();
                        } catch (Exception e) {
                            e.printStackTrace();
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
        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

        if(listWorkWith.size()>10)
        {
            inputSearch.setVisibility(View.VISIBLE);
        }
        else
        {
            inputSearch.setVisibility(View.GONE);
        }

        edtSearchDialog.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                int textlength = edtSearchDialog.getText().length();

               if(isFor.equals(AREA) || isFor.equals(EXTRA_AREA))
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
                else if(isFor.equals(WW) || isFor.equalsIgnoreCase(EXTRA_WW))
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

                AppendListForArea(listDialog,isFor,true,rvListDialog,edtSearchDialog.getText().toString().trim(),textView);
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

       /* DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }*/
        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,TextView textView,int listPosition)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            this.textView = textView;
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
           if(isFor.equals(AREA) || isFor.equals(EXTRA_AREA))
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
                        if(isFor.equals(AREA))
                        {
                            TPFormResponse.DaysBean bean = listTpEntry.get(mainSelectedPosition);
                            bean.setArea_name(getSet.getArea_name());
                            bean.setArea(getSet.getArea_id());
                            bean.setRoute_area_id(getSet.getRoute_area_id());
                            listTpEntry.set(mainSelectedPosition,bean);
                        }
                        else if(isFor.equals(EXTRA_AREA))
                        {
                            /*TPFormResponse.ExtradaysBean bean = listExtraEntry.get(extraSelectionPosition);*/
                            TPFormResponse.ExtradaysBean bean = listExtraEntry.get(mainSelectedPosition);
                            bean.setArea_id(getSet.getArea_id());
                            bean.setArea_name(getSet.getArea_name());
                            bean.setRoute_area_id(getSet.getRoute_area_id());
                            /*listExtraEntry.set(extraSelectionPosition,bean);*/
                            listExtraEntry.set(mainSelectedPosition,bean);
                        }

                        textView.setText(getSet.getArea_name());
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

                //in some cases, it will prevent unwanted situations
                holder.cb.setOnCheckedChangeListener(null);


                if(isFor.equalsIgnoreCase(WW))
                {
                    if(listTpEntry.size()>0)
                    {
                        try
                        {
                            List<String> list = AppUtils.getListFromCommaSeperatedString(listTpEntry.get(mainSelectedPosition).getWork_withString());
                            for (int i = 0; i < list.size(); i++)
                            {
                                if(getSet.getStaff_id().equalsIgnoreCase(list.get(i).toString().trim()))
                                {
                                    getSet.setSelected(true);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    holder.cb.setChecked(getSet.isSelected());

                    holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            if(isChecked)
                            {
                                getSet.setSelected(true);
                            }
                            else
                            {
                                getSet.setSelected(false);
                            }

                            listWorkWith.set(position,getSet);

                            //notifyItemChanged(position);
                        }
                    });

                }
                else if(isFor.equalsIgnoreCase(EXTRA_WW))
                {
                    try
                    {
                        /*List<String> list = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(extraSelectionPosition).getWork_withString());*/
                        List<String> list = AppUtils.getListFromCommaSeperatedString(listExtraEntry.get(mainSelectedPosition).getWork_withString());
                        for (int i = 0; i < list.size(); i++)
                        {
                            if(getSet.getStaff_id().equalsIgnoreCase(list.get(i).toString().trim()))
                            {
                                getSet.setSelected(true);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    holder.cb.setChecked(getSet.isSelected());

                    holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            if(isChecked)
                            {
                                getSet.setSelected(true);
                            }
                            else
                            {
                                getSet.setSelected(false);
                            }

                            listWorkWith.set(position,getSet);

                            //notifyItemChanged(position);
                        }
                    });
                }

                holder.tvValue.setVisibility(View.GONE);

                holder.cb.setText(getSet.getName() + " ("+getSet.getDesignation()+")");
                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));

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
                        textView.setText(getSet.getDate());
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
            if(isFor.equalsIgnoreCase(AREA))
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

    private void AppendListForArea(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea,String searchTExt,TextView textView)
    {
        listDialogAdapter = new DialogListAdapter(dialog,isFor,true,searchTExt,textView,0);
        rvArea.setAdapter(listDialogAdapter);
        listDialogAdapter.notifyDataSetChanged();
    }

    private void saveSingleDay(String employee,
                               String year,
                               String month,
                               long day,
                               String area,
                               String remark,
                               String is_leave,
                               String is_holiday,
                               String is_extra_day,
                               String staff_id,
                               String day_id)
    {
        Call<CommonResponse> saveCall = apiService.saveTourPlanSingle(employee,
                year,month,day,area,remark,is_leave,is_holiday,is_extra_day,staff_id,day_id,sessionManager.getUserId());
        saveCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    activity.finish();
                    if(!AppUtils.isLastDateOfMonth(tvDateTitle.getText().toString().trim()))
                    {
                        if(FragmentCalendar.handler!=null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            message.obj = AppUtils.getNextDate(tvDateTitle.getText().toString().trim());
                            FragmentCalendar.handler.sendMessage(message);
                        }
                    }
                }

                AppUtils.showToast(activity,response.body().getMessage());
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public class ExtraEntryAdapter extends RecyclerView.Adapter<ExtraEntryAdapter.ViewHolder>
    {

        List<TPFormResponse.ExtradaysBean> listItems;

        ExtraEntryAdapter(List<TPFormResponse.ExtradaysBean> list) {
            this.listItems = list;
        }

        @Override
        public ExtraEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_tp_extra_new, viewGroup, false);
            return new ExtraEntryAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ExtraEntryAdapter.ViewHolder holder, final int position)
        {
            final TPFormResponse.ExtradaysBean getSet = listItems.get(position);
           /* holder.edtDate.setText(getSet.getSr_no());
            holder.edtTPWW.setText(getSet.getWork_withName());
            holder.edtTPArea.setText(getSet.getArea());
            holder.edtRemark.setText(getSet.getRemark());
            holder.edtRemark.setSelection(getSet.getRemark().length());*/

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
            holder.tvNumber.setText(clickedDate);
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
            listItems.set(position,newBean);
            notifyItemChanged(position);
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

            @BindView(R.id.card)
            CardView card;

            @BindView(R.id.tvNumber)
            TextView tvNumber;

            @BindView(R.id.ivDelete)
            ImageView ivDelete;

            @BindView(R.id.ivEdit)
            ImageView ivEdit;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
                edtTPArea.setOnClickListener(this);
                edtTPWW.setOnClickListener(this);
                ivDelete.setOnClickListener(this);
                ivEdit.setOnClickListener(this);

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
                });
            }

            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.edtTPArea:
                        //showListDialog(EXTRA_AREA,edtTPArea,getAdapterPosition());
                        break;
                    case R.id.edtTPWW:
                        //showListDialog(EXTRA_WW,edtTPWW,getAdapterPosition());
                        break;
                    case R.id.ivDelete:

                        confirmDeleteExtraDay(getAdapterPosition());

                        /*TPFormResponse.ExtradaysBean newBean = listItems.get(getAdapterPosition());
                        newBean.setArea_id("");
                        newBean.setArea_name("");
                        newBean.setRoute_area_id("");
                        newBean.setSr_no("");
                        newBean.setRemark("");
                        newBean.setWork_withName("");
                        newBean.setWork_withString("");
                        newBean.setDay_name("");
                        listItems.set(getAdapterPosition(),newBean);
                        notifyItemChanged(getAdapterPosition());*/
                        break;
                    case R.id.ivEdit:

                        isEditClick = true;

                        showExtraRouteDialog();

                        ArrayList<TPFormResponse.ExtradaysBean> listExtra = new ArrayList<>();
                        for (int i = 0; i < listExtraEntry.size(); i++)
                        {
                            if(listExtraEntry.get(i).getDate().equalsIgnoreCase(String.valueOf(listTpEntry.get(mainSelectedPosition).getDate())))
                            {
                                listExtra.add(listExtraEntry.get(i));
                            }
                        }

                        for (int i = 0; i < listExtra.size(); i++)
                        {
                            if(listExtra.get(i).getDay_id().equalsIgnoreCase(listItems.get(getAdapterPosition()).getDay_id()))
                            {
                                TPFormResponse.ExtradaysBean bean = listExtra.get(i);
                                d_tvWorkWith.setText(bean.getWork_withName());
                                d_tvTPArea.setText(bean.getArea_name());
                                d_edtRemark.setText(bean.getRemark());
                                d_edtRemark.setSelection(bean.getRemark().length());

                                editBean.setWork_withName(bean.getWork_withName());
                                editBean.setWork_withString(bean.getWork_withString());
                                editBean.setArea_name(bean.getArea_name());
                                editBean.setArea_id(bean.getArea_id());
                                editBean.setRoute_area_id(bean.getRoute_area_id());
                                editBean.setDate(bean.getDate());
                                editBean.setDay_id(bean.getDay_id());
                                editBean.setRemark(bean.getRemark());
                            }
                        }
                        break;
                }
            }
        }
    }

    private void confirmDeleteExtraDay(final int position)
    {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
        dialog.setContentView(sheetView);

        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText("Delete Day");
        TextView tvDescription = (TextView) dialog.findViewById(R.id.tvDescription);
        tvDescription.setText("Are you sure you want to delete this day?");

        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sessionManager.isNetworkAvailable())
                {
                    dialog.dismiss();
                    dialog.cancel();


                    Log.e("Deleted Id >> ", "onClick: "+listExtraEntry.get(position).getDay_id() );

                    Call<CommonResponse> removeCall = apiService.removeTourPlanSingle(listExtraEntry.get(position).getDay_id(),sessionManager.getUserId());
                    removeCall.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                        {
                            if(response.body().getSuccess()==1)
                            {
                                listNewExtra.remove(position);
                                if(extraEntryAdapter!=null)
                                {
                                    extraEntryAdapter.notifyDataSetChanged();
                                }
                                if(listNewExtra.size()>0)
                                {
                                    llTitleExtra.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    llTitleExtra.setVisibility(View.GONE);
                                }
                            }

                            AppUtils.showToast(activity,response.body().getMessage());
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Log.e("Error while deleet", "onFailure: "+t.toString() );
                        }
                    });
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
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
