package com.unisonpharmaceuticals.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.ActivityDailyCallReport;
import com.unisonpharmaceuticals.activity.ActivityPendingEntry;
import com.unisonpharmaceuticals.activity.NCRActivity;
import com.unisonpharmaceuticals.activity.ViewSubmittedEntryActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.databse.UnisonDatabaseHelper;
import com.unisonpharmaceuticals.model.AddEntryGetSet;
import com.unisonpharmaceuticals.model.AreaResponse;
import com.unisonpharmaceuticals.model.DayEndResponse;
import com.unisonpharmaceuticals.model.DoctorResponse;
import com.unisonpharmaceuticals.model.DrDcrGetSet;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.model.ReasonResponse;
import com.unisonpharmaceuticals.model.ReportResponse;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.SubmittedResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.WorkWithResponse;
import com.unisonpharmaceuticals.model.for_sugar.DBArea;
import com.unisonpharmaceuticals.model.for_sugar.DBDoctor;
import com.unisonpharmaceuticals.model.for_sugar.DBPlanner;
import com.unisonpharmaceuticals.model.for_sugar.DBReason;
import com.unisonpharmaceuticals.model.for_sugar.DBReports;
import com.unisonpharmaceuticals.model.for_sugar.DBSpeciality;
import com.unisonpharmaceuticals.model.for_sugar.DBStaff;
import com.unisonpharmaceuticals.model.for_sugar.DBVariation;
import com.unisonpharmaceuticals.model.for_sugar.DBWorkWith;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.MitsAutoHeightListView;
import com.unisonpharmaceuticals.utils.MitsUtils;
import com.unisonpharmaceuticals.views.BottomSheetListView;
import com.unisonpharmaceuticals.views.RegularEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint({"InlinedApi", "NewApi", "SimpleDateFormat"})
@SuppressWarnings("unused")
public class FragmentMakeEntry extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Activity activity;

    private View rootView;

    private SessionManager sessionManager;
    private TextView txtAddCount;
    private String dateCurrent = "";
    private ScrollView scrollview;

    public static DatePickerDialog datepicker;

    private UnisonDatabaseHelper udbh;
    private SQLiteDatabase sqlDB;

    private String entryAdded = "false";
    private String callDoneFromTP = "false";
    private String doctorType = "";

    private LinearLayout llMain, llLoading, llNewCycle, llSave, llView, llSubmit, llEntry;
    private ImageView iv_leftDrawer;

    private CheckBox cbNewCycle;
    private ImageView ivNewCycle;

    boolean newCycle = false;

    private final String AREA = "area";
    private final String SPECIALITY = "speciality";
    private final String DOCTOR = "doctor";
    private final String WORKWITH = "work with";
    private final String EMPLOYEE = "employee";
    private final String ADD_AREA = "addArea";
    private final String DOCTOR_PLANNER = "Planned Doctors";

    // New parameter
    private EditText edtDBC, edtDoctor, edtWorkWith, edtArea,
             edtInternee, edtSpeciality, edtEmployee,
            edtAdvice;
    public static EditText edtDate;

    private EditText edtRemarks;

    private LinearLayout llSampleDetails, llAdvice,llPlannedEntry;

    private View viewPlanner;

    private TextInputLayout inputArea, inputSpeciality, inputDBC, inputWorkWith, inputDoctor,
            inputRMK, inputInternee, inputEmployee, inputDate,
            inputAdvice;

    private CheckBox cbWorkWith;
    private ImageView ivEmployee;
    private LinearLayout llWorkWith;

    private View viewLine;


    private String dbs = "", workWithCode = "", doctorCode = "", product = "", unit = "", reason = "", cycle = "", empId = "", empName = "", workWithString = "";
    private boolean isDoctor = true, isCycle = true;

    private ArrayList<AddEntryGetSet> listEntry;

    private ImageView ivAdd, ivMinus;

    PendingIntent pendingIntent;

    ProgressDialog pd;

    ArrayList<String> listDoctorCode;

    private int pendingEntryCount = 0;

    private String msg = "";

    private ProductAdapter productAdapter;
    //private FocusedAdapter focusedAdapter;

    //List For check reason is not morethan 2
    private ArrayList<String> listRestriction = new ArrayList<>();
    private int countNew = 0;
    private int countEnhance = 0;
    private String focusForString = "";

    private String product1 = "", product2 = "", product3 = "", product4 = "";

    public static Handler handlerNCR;
    public static Handler handlerNCR1, areaHandler;

    /*New*/
    private BottomSheetDialog listDialog;
    //private Dialog listDialog;
    private ApiInterface apiService;
    private String selectedAreaId = "", selectedSpecialityId = "", selectedDoctorId = "", selectedReportCode = "", selectedEmployeeID = "";
    private int enable_focus = 0;
    private ArrayList<AreaResponse.AreasBean> listArea = new ArrayList<>();
    private ArrayList<AreaResponse.AreasBean> listAreaAll = new ArrayList<>();
    private ArrayList<AreaResponse.AreasBean> listAreaForAdd = new ArrayList<>();
    private ArrayList<AreaResponse.AreasBean> listAreaSearch = new ArrayList<>();

    private ArrayList<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private ArrayList<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private ArrayList<DoctorResponse.DoctorsBean> listDoctor = new ArrayList<>();
    private ArrayList<DoctorResponse.DoctorsBean> listDoctorSearch = new ArrayList<>();

    private ArrayList<ReportResponse.ReportsBean> listReports = new ArrayList<>();
    private ArrayList<ReportResponse.ReportsBean> listReportSearch = new ArrayList<>();

    private ArrayList<WorkWithResponse.StaffBean> listWorkWith = new ArrayList<>();
    private ArrayList<WorkWithResponse.StaffBean> listWorkWithSearch = new ArrayList<>();

    private ArrayList<VariationResponse.VariationsBean> listVariation = new ArrayList<>();
    private ArrayList<VariationResponse.VariationsBean> listVariationSearch = new ArrayList<>();

    private ArrayList<ReasonResponse.ReasonsBean> listReason = new ArrayList<>();
    private ArrayList<VariationResponse.VariationsBean> listSelectedProducts = new ArrayList<>();

    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<DBPlanner> listPlannedDoctor = new ArrayList<>();
    private ArrayList<DBPlanner> listPlannedDoctorSearch = new ArrayList<>();

    private RecyclerView rvVariation;
    private VariationAdapter variationAdapter;
    ProductUnitAdapter productUnitAdapter;

    private String NCRDrData = "";

    private boolean isListReasonItemClicked = false;

    //For SearchListing
    private CountDownTimer timer;

    private ArrayList<DrDcrGetSet> listSubmitedEntryOfDr = new ArrayList<>();

    int apiCounts = 0;

    private boolean isLoading = false , isPlannerClicked = false, isNCREntry = false;

    private long mLastClickTime = 0;

    /*New*/
    private LinearLayout llOffDay;

    public FragmentMakeEntry() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_make_field_entry_new, container, false);

        activity = getActivity();

        sessionManager = new SessionManager(activity);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        setUpViews();

        apiCounts = 0;

        onClickListeners();

        handlerNCR1 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 111)
                {
                    NCRDrData = String.valueOf(msg.obj);
                    isNCREntry = true;
                }
                return false;
            }
        });

        areaHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 111)
                {
                    int size = (int) msg.obj;
                    if (size > 0) {
                        ActivityDailyCallReport.ivAddArea.setVisibility(View.VISIBLE);
                    } else {
                        ActivityDailyCallReport.ivAddArea.setVisibility(View.GONE);
                    }
                }
                else if(msg.what==112)
                {
                    if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER)
                            && listArea.size()>0)//Working manager
                    {
                        Call<SubmittedResponse> dataCall = apiService.getSubmittedEntry(AppUtils.currentDateForApi(), sessionManager.getUserId(), sessionManager.getUserId());
                        dataCall.enqueue(new Callback<SubmittedResponse>() {
                            @Override
                            public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) {
                                try {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        List<SubmittedResponse.ReportBean.DataBean> listData = response.body().getReport().getData();
                                        if (listData.size() > 0) {
                                            if (listSubmitedEntryOfDr != null) {
                                                sessionManager.setCallDoneFromTP("true");
                                                for (int i = 0; i < listData.size(); i++)
                                                {
                                                    DrDcrGetSet getSet = new DrDcrGetSet();
                                                    getSet.setDrId(listData.get(i).getDoctor_id());
                                                    getSet.setDrName(listData.get(i).getDoctor_name());
                                                    getSet.setReportType(listData.get(i).getReport_type());
                                                    listSubmitedEntryOfDr.add(getSet);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                                isLoading = false;
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        });
                    }
                    else if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
                    {
                        Call<SubmittedResponse> dataCall = apiService.getSubmittedEntry(AppUtils.currentDateForApi(), sessionManager.getUserId(), sessionManager.getUserId());
                        dataCall.enqueue(new Callback<SubmittedResponse>() {
                            @Override
                            public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) {
                                try {
                                    if (response.body().getSuccess() == 1) {
                                        List<SubmittedResponse.ReportBean.DataBean> listData = response.body().getReport().getData();
                                        if (listData.size() > 0) {
                                            if (listSubmitedEntryOfDr != null) {
                                                sessionManager.setCallDoneFromTP("true");
                                                for (int i = 0; i < listData.size(); i++) {
                                                    DrDcrGetSet getSet = new DrDcrGetSet();
                                                    getSet.setDrId(listData.get(i).getDoctor_id());
                                                    getSet.setDrName(listData.get(i).getDoctor_name());
                                                    getSet.setReportType(listData.get(i).getReport_type());
                                                    listSubmitedEntryOfDr.add(getSet);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                                isLoading = false;
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        });
                    }
                }
                else if(msg.what==113)
                {
                    try {
                        if (sessionManager.getDayEnd().equals("false"))
                        {
                            if (sessionManager.getCallDoneFromTP().equalsIgnoreCase("true"))
                            {
                                showListDialog(ADD_AREA);
                            }
                            else
                            {
                                AppUtils.showToast(activity, "To add area, You should have done atleast one call from your current tourplan.");
                            }
                        }
                        else
                        {
                            AppUtils.showToast(activity,"Your day has been ended by you.Kindly try again tomorrow.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        handlerNCR = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 111)
                {
                    inputDoctor.setVisibility(View.VISIBLE);
                    edtDoctor.setText((String) msg.obj);
                    inputDoctor.setEnabled(false);
                    edtDoctor.setEnabled(false);
                    edtDBC.setText("NCR : New Doctor Call");
                    dbs = "NCR";

                    ActivityDailyCallReport.ncrDoctorName = (String) msg.obj;
                    ActivityDailyCallReport.ncrDoctorId = "0";
                }
                else if (msg.what == 101)
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        Call<VariationResponse> variationCall = apiService.getVarioationProducts(sessionManager.getUserId(), sessionManager.getUserId(), "false");
                        variationCall.enqueue(new Callback<VariationResponse>() {
                            @Override
                            public void onResponse(Call<VariationResponse> call, Response<VariationResponse> response) {
                                try {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        listVariation = (ArrayList<VariationResponse.VariationsBean>) response.body().getVariations();
                                        try {

                                            DBVariation.deleteAll(DBVariation.class);
                                            for (int i = 0; i < listVariation.size(); i++) {
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
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<VariationResponse> call, Throwable t) {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        });
                    }
                    /*else
                    {
                        listVariation.clear();
                        List<DBVariation> listVariationFromDB = DBVariation.listAll(DBVariation.class);
                        for (int i = 0; i < listVariationFromDB.size(); i++) {
                            try {
                                VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                                bean.setReason_id(listVariationFromDB.get(i).getReason_id());
                                bean.setReason(listVariationFromDB.get(i).getReason());
                                bean.setReason_code(listVariationFromDB.get(i).getReason_code());
                                bean.setProduct_id(listVariationFromDB.get(i).getProduct_id());
                                bean.setVariation_id(listVariationFromDB.get(i).getVariation_id());
                                bean.setName(listVariationFromDB.get(i).getName());
                                bean.setItem_code(listVariationFromDB.get(i).getItem_code());
                                bean.setItem_id_code(listVariationFromDB.get(i).getItem_id_code());
                                bean.setStock(listVariationFromDB.get(i).getStock());
                                bean.setProduct_type(listVariationFromDB.get(i).getProduct_type());
                                listVariation.add(bean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }*/
                }
                return false;
            }
        });

        return rootView;
    }

    private void fillAllListFromDatabase()//For get All the list from databse
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                listArea.clear();
                List<DBArea> listAreaFromDb = DBArea.listAll(DBArea.class);
                for (int i = 0; i < listAreaFromDb.size(); i++) {
                    AreaResponse.AreasBean bean = new AreaResponse.AreasBean();
                    bean.setArea_id(listAreaFromDb.get(i).getArea_id());
                    bean.setArea(listAreaFromDb.get(i).getArea());
                    listArea.add(bean);
                }

                try {
                    if(!sessionManager.getArea().equalsIgnoreCase("") || sessionManager.getArea()!=null)
                    {
                        JSONArray jsonArray = new JSONArray(sessionManager.getArea());
                        if(jsonArray.length()>0)
                        {
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                if(object.getString("is_tour_plan").equalsIgnoreCase("0"))
                                {
                                    AreaResponse.AreasBean bean = new AreaResponse.AreasBean();
                                    bean.setArea_id(object.getString("area_id"));
                                    bean.setArea(object.getString("area"));
                                    listAreaForAdd.add(bean);
                                }
                            }
                        }
                        try {
                            Message message = Message.obtain();
                            message.what = 111;
                            message.obj = listAreaForAdd.size();
                            areaHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }




                /*dsdsd
                for (int i = 0; i < listArea.size(); i++)
                {
                    if (listArea.get(i).getIs_tour_plan().equalsIgnoreCase("1")) {
                        listArea.add(listArea.get(i));
                    } else {
                        listAreaForAdd.add(listArea.get(i));
                    }

                    listAreaAll.add(listArea.get(i));
                }

                try {
                    Message message = Message.obtain();
                    message.what = 111;
                    message.obj = listArea.size();
                    areaHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/



                listEmployee.clear();
                List<DBStaff> listEmployeeFromDb = DBStaff.listAll(DBStaff.class);
                String empName = "";
                for (int i = 0; i < listEmployeeFromDb.size(); i++) {
                    if (empName.contains(listEmployeeFromDb.get(i).getName())) {
                        continue;
                    }
                    try {
                        StaffResponse.StaffBean bean = new StaffResponse.StaffBean();
                        bean.setName(listEmployeeFromDb.get(i).getName());
                        bean.setStaff_id(listEmployeeFromDb.get(i).getStaff_id());
                        bean.setDesignation(listEmployeeFromDb.get(i).getDesignation());
                        if (i == 0) {
                            empName = listEmployeeFromDb.get(i).getName()+""+listEmployeeFromDb.get(i).getStaff_id();
                        } else {
                            empName = empName + "," + listEmployeeFromDb.get(i).getName()+""+listEmployeeFromDb.get(i).getStaff_id();
                        }

                        if (!bean.getStaff_id().equalsIgnoreCase(sessionManager.getUserId())) {
                            listEmployee.add(bean);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                listSpeciality.clear();
                List<DBSpeciality> listSpecialityFromDb = DBSpeciality.listAll(DBSpeciality.class);
                String speciality = "";
                for (int i = 0; i < listSpecialityFromDb.size(); i++) {
                    if (speciality.contains(listSpecialityFromDb.get(i).getSpeciality_id())) {
                        continue;
                    }
                    SpecialistBean.SpecialityBean specialityBean = new SpecialistBean.SpecialityBean();
                    specialityBean.setSpeciality(listSpecialityFromDb.get(i).getSpeciality());
                    specialityBean.setSpeciality_id(listSpecialityFromDb.get(i).getSpeciality_id());
                    if (i == 0) {
                        speciality = listSpecialityFromDb.get(i).getSpeciality_id()+""+listSpecialityFromDb.get(i).getSpeciality();
                    } else {
                        speciality = speciality + "," + listSpecialityFromDb.get(i).getSpeciality_id()+""+listSpecialityFromDb.get(i).getSpeciality();
                    }
                    listSpeciality.add(specialityBean);
                }

                listDoctor.clear();
                List<DBDoctor> listDocotrFromDb = DBDoctor.listAll(DBDoctor.class);
                for (int i = 0; i < listDocotrFromDb.size(); i++) {
                    try {
                        DoctorResponse.DoctorsBean bean = new DoctorResponse.DoctorsBean();
                        bean.setDoctor(listDocotrFromDb.get(i).getDoctor());
                        bean.setDoctor_id(listDocotrFromDb.get(i).getDoctor_id());
                        bean.setSpeciality_code(listDocotrFromDb.get(i).getSpeciality_code());
                        bean.setEnable_focus(listDocotrFromDb.get(i).getEnable_focus());
                        listDoctor.add(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                listReports.clear();
                List<DBReports> listReportsFromDb = DBReason.listAll(DBReports.class);
                for (int i = 0; i < listReportsFromDb.size(); i++) {
                    try {
                        ReportResponse.ReportsBean bean = new ReportResponse.ReportsBean();
                        bean.setReport_code(listReportsFromDb.get(i).getReport_code());
                        bean.setReport_id(listReportsFromDb.get(i).getReport_id());
                        bean.setReport_name(listReportsFromDb.get(i).getReport_name());
                       listReports.add(bean);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                listWorkWith.clear();
                List<DBWorkWith> listWorkWithDB = DBWorkWith.listAll(DBWorkWith.class);
                for (int i = 0; i < listWorkWithDB.size(); i++) {
                    try {
                        WorkWithResponse.StaffBean bean = new WorkWithResponse.StaffBean();
                        bean.setStaff_id(listWorkWithDB.get(i).getStaff_id());
                        bean.setName(listWorkWithDB.get(i).getName());
                        bean.setDesignation(listWorkWithDB.get(i).getDesignation());
                        listWorkWith.add(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                listVariation.clear();
                List<DBVariation> listVariationFromDB = DBVariation.listAll(DBVariation.class);
                for (int i = 0; i < listVariationFromDB.size(); i++) {
                    try {
                        VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                        bean.setReason_id(listVariationFromDB.get(i).getReason_id());
                        bean.setReason(listVariationFromDB.get(i).getReason());
                        bean.setReason_code(listVariationFromDB.get(i).getReason_code());
                        bean.setProduct_id(listVariationFromDB.get(i).getProduct_id());
                        bean.setVariation_id(listVariationFromDB.get(i).getVariation_id());
                        bean.setName(listVariationFromDB.get(i).getName());
                        bean.setItem_code(listVariationFromDB.get(i).getItem_code());
                        bean.setItem_id_code(listVariationFromDB.get(i).getItem_id_code());
                        bean.setStock(listVariationFromDB.get(i).getStock());
                        bean.setProduct_type(listVariationFromDB.get(i).getProduct_type());
                        listVariation.add(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                listReason.clear();
                List<DBReason> listRasonFromDB = DBReason.listAll(DBReason.class);
                for (int i = 0; i < listRasonFromDB.size(); i++) {
                    try {
                        ReasonResponse.ReasonsBean bean = new ReasonResponse.ReasonsBean();
                        bean.setReason(listRasonFromDB.get(i).getReason());
                        bean.setTimestamp(listRasonFromDB.get(i).getTimestamp());
                        bean.setReason_id(listRasonFromDB.get(i).getReason_id());
                        bean.setComment(listRasonFromDB.get(i).getComment());
                        bean.setReason_code(listRasonFromDB.get(i).getReason_code());
                        listReason.add(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                {
                    if (listArea.size() == 0)//Manager and blank if condition for working manager because working manager can male field entry
                    {
                        ArrayList<ReportResponse.ReportsBean> listTemp = new ArrayList<>();
                        for (int i = 0; i < listReports.size(); i++) {
                            ReportResponse.ReportsBean bean = listReports.get(i);
                            listTemp.add(bean);
                        }

                        for (int i = 0; i < listTemp.size(); i++) {
                            if (listTemp.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                listReports.clear();
                                ReportResponse.ReportsBean bean = listTemp.get(i);
                                listReports.add(bean);
                            }
                        }

                        edtDBC.setText("ADV : Advice");
                        dbs = "ADV";
                        selectedReportCode = "ADV";

                        inputEmployee.setVisibility(View.VISIBLE);
                        inputDate.setVisibility(View.VISIBLE);
                        llAdvice.setVisibility(View.VISIBLE);

                        inputArea.setVisibility(View.GONE);
                        inputSpeciality.setVisibility(View.GONE);
                        llWorkWith.setVisibility(View.GONE);
                        inputDoctor.setVisibility(View.GONE);
                        llNewCycle.setVisibility(View.GONE);
                        llSampleDetails.setVisibility(View.GONE);
                        rvVariation.setVisibility(View.GONE);
                    }//for off day
                    else
                    {
                        if(sessionManager.getOffDayOrAdminDay().equalsIgnoreCase("1"))
                        {
                            ArrayList<ReportResponse.ReportsBean> listTemp = new ArrayList<>();
                            for (int i = 0; i < listReports.size(); i++) {
                                ReportResponse.ReportsBean bean = listReports.get(i);
                                listTemp.add(bean);
                            }

                            for (int i = 0; i < listTemp.size(); i++) {
                                if (listTemp.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                    listReports.clear();
                                    ReportResponse.ReportsBean bean = listTemp.get(i);
                                    listReports.add(bean);
                                }
                            }

                            edtDBC.setText("ADV : Advice");
                            dbs = "ADV";
                            selectedReportCode = "ADV";

                            inputEmployee.setVisibility(View.VISIBLE);
                            inputDate.setVisibility(View.VISIBLE);
                            llAdvice.setVisibility(View.VISIBLE);

                            inputArea.setVisibility(View.GONE);
                            inputSpeciality.setVisibility(View.GONE);
                            llWorkWith.setVisibility(View.GONE);
                            inputDoctor.setVisibility(View.GONE);
                            llNewCycle.setVisibility(View.GONE);
                            llSampleDetails.setVisibility(View.GONE);
                            rvVariation.setVisibility(View.GONE);
                        }
                    }
                }

                /*if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                {
                    if (listArea.size() == 0)//Manager and blank if condition for working manager because working manager can male field entry
                    {
                        ArrayList<ReportResponse.ReportsBean> listTemp = new ArrayList<>();
                        for (int i = 0; i < listReports.size(); i++) {
                            ReportResponse.ReportsBean bean = listReports.get(i);
                            listTemp.add(bean);
                        }

                        for (int i = 0; i < listTemp.size(); i++) {
                            if (listTemp.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                listReports.clear();
                                ReportResponse.ReportsBean bean = listTemp.get(i);
                                listReports.add(bean);
                            }
                        }

                        edtDBC.setText("ADV : Advice");
                        dbs = "ADV";
                        selectedReportCode = "ADV";

                        inputEmployee.setVisibility(View.VISIBLE);
                        inputDate.setVisibility(View.VISIBLE);
                        llAdvice.setVisibility(View.VISIBLE);

                        inputArea.setVisibility(View.GONE);
                        inputSpeciality.setVisibility(View.GONE);
                        llWorkWith.setVisibility(View.GONE);
                        inputDoctor.setVisibility(View.GONE);
                        llNewCycle.setVisibility(View.GONE);
                        llSampleDetails.setVisibility(View.GONE);
                        rvVariation.setVisibility(View.GONE);
                    }
                }*/


                llLoading.setVisibility(View.GONE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);

    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                Call<AreaResponse> loginCall = apiService.getAreaFromUserId("1000", sessionManager.getUserId(), sessionManager.getUserId(),"");
                loginCall.enqueue(new Callback<AreaResponse>() {
                    @Override
                    public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1)
                            {
                                ArrayList<AreaResponse.AreasBean> list = (ArrayList<AreaResponse.AreasBean>) response.body().getAreas();

                                for (int i = 0; i < list.size(); i++)
                                {
                                    if (list.get(i).getIs_tour_plan().equalsIgnoreCase("1")) {
                                        listArea.add(list.get(i));
                                    } else {
                                        listAreaForAdd.add(list.get(i));
                                    }

                                    listAreaAll.add(list.get(i));
                                }

                                try {
                                    Message message = Message.obtain();
                                    message.what = 111;
                                    message.obj = listAreaForAdd.size();
                                    areaHandler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                //AppUtils.showToast(activity,"Area not found!");
                            }

                            isLoading = false;

                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<AreaResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getAreaFromUserId  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });

                isLoading = true;
                Call<ReportResponse> reportCall = apiService.getReportTypeList("", sessionManager.getUserId());
                reportCall.enqueue(new Callback<ReportResponse>() {
                    @Override
                    public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1) {
                                //listReports = (ArrayList<ReportResponse.ReportsBean>) response.body().getReports();

                                List<ReportResponse.ReportsBean> list = response.body().getReports();

                                for (int i = 0; i < list.size(); i++) {
                                    ReportResponse.ReportsBean bean = list.get(i);
                                    if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR)) {
                                        if (!list.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                            // If get is_lock_stk flag 0 in login than do not disply STK in list report
                                            if (list.get(i).getReport_code().equalsIgnoreCase("STK")) {
                                                if (sessionManager.getCanSTK().equalsIgnoreCase("1")) {
                                                    listReports.add(bean);
                                                }

                                            } else {

                                                listReports.add(bean);

                                            }
                                        }
                                    } else if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER)) {
                                        if (list.get(i).getReport_code().equalsIgnoreCase("STK")) {
                                            if (sessionManager.getCanSTK().equalsIgnoreCase("1")) {
                                                listReports.add(bean);
                                            }
                                        } else {
                                            listReports.add(bean);
                                        }
                                    }

                                    Log.e("<><> listReports", String.valueOf(listReports.size()));
                                }
                            } else {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                            isLoading = false;
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getReportTypeList  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });

                isLoading = true;
                Call<WorkWithResponse> workWithCall = apiService.getWorkWithList(sessionManager.getUserId(), sessionManager.getUserId(), "false");
                workWithCall.enqueue(new Callback<WorkWithResponse>() {
                    @Override
                    public void onResponse(Call<WorkWithResponse> call, Response<WorkWithResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1) {
                                listWorkWith = (ArrayList<WorkWithResponse.StaffBean>) response.body().getStaff();
                        /*WorkWithResponse.StaffBean bean = new WorkWithResponse.StaffBean();
                        bean.setDesignation("");
                        bean.setName("Self");
                        bean.setStaff_id("0");
                        listWorkWith.add(0,bean);*/
                            } else {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                                Log.e("getWorkWithList  ## ", "onResponse: 1");
                            }
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                            isLoading = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<WorkWithResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getWorkWithList  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });

                isLoading = true;
                Call<VariationResponse> variationCall = apiService.getVarioationProducts(sessionManager.getUserId(), sessionManager.getUserId(), "false");
                variationCall.enqueue(new Callback<VariationResponse>() {
                    @Override
                    public void onResponse(Call<VariationResponse> call, Response<VariationResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1) {
                                listVariation = (ArrayList<VariationResponse.VariationsBean>) response.body().getVariations();
                            } else {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                                Log.e("getVarioationProducts  ## ", "onResponse: 1");
                            }
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                            isLoading = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VariationResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getVarioationProducts  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });

                isLoading = true;
                Call<ReasonResponse> reasonCall = apiService.getReasonList("", sessionManager.getUserId());
                reasonCall.enqueue(new Callback<ReasonResponse>() {
                    @Override
                    public void onResponse(Call<ReasonResponse> call, Response<ReasonResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1) {
                                listReason = (ArrayList<ReasonResponse.ReasonsBean>) response.body().getReasons();

                                try {
                                    List<DBReason> books = DBReason.listAll(DBReason.class);
                                    if (books.size() <= 0) {
                                        for (int i = 0; i < listReason.size(); i++) {
                                            if (listReason.get(i).getReason_code().equalsIgnoreCase("R") && listReason.get(i).getReason().equalsIgnoreCase("Regular Sample")) {
                                                ApiClient.SAMPLE_REASON = listReason.get(i).getReason();
                                                ApiClient.SAMPLE_REASON_CODE = listReason.get(i).getReason_code();
                                                ApiClient.SAMPLE_REASON_ID = listReason.get(i).getReason_id();
                                            }

                                            ReasonResponse.ReasonsBean bean = listReason.get(i);
                                            DBReason reason = new DBReason(bean.getReason_id(),
                                                    bean.getReason(),
                                                    bean.getReason_code(),
                                                    bean.getComment(),
                                                    bean.getTimestamp());
                                            reason.save();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                                Log.e("getReasonList  ## ", "onResponse: 1");
                            }
                            isLoading = false;
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReasonResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getReasonList  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });
                isLoading = true;
                Call<SubmittedResponse> dataCall = apiService.getSubmittedEntry(AppUtils.currentDateForApi(), sessionManager.getUserId(), sessionManager.getUserId());
                dataCall.enqueue(new Callback<SubmittedResponse>() {
                    @Override
                    public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1)
                            {
                                List<SubmittedResponse.ReportBean.DataBean> listData = response.body().getReport().getData();
                                if (listData.size() > 0) {
                                    if (listSubmitedEntryOfDr != null) {
                                        sessionManager.setCallDoneFromTP("true");
                                        for (int i = 0; i < listData.size(); i++) {
                                            DrDcrGetSet getSet = new DrDcrGetSet();
                                            getSet.setDrId(listData.get(i).getDoctor_id());
                                            getSet.setDrName(listData.get(i).getDoctor_name());
                                            getSet.setReportType(listData.get(i).getReport_type());
                                            listSubmitedEntryOfDr.add(getSet);
                                        }
                                    }
                                }
                            }
                            isLoading = false;
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                        Log.e("getSubmittedEntry  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });
                isLoading = true;
                Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(), sessionManager.getUserId());
                empCall.enqueue(new Callback<StaffResponse>() {
                    @Override
                    public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response) {
                        try {
                            if (response.body().getSuccess() == 1) {
                                listEmployee = new ArrayList<>();

                                listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();

                                if (listEmployee.size() > 0) {
                                    for (int i = 0; i < listEmployee.size(); i++) {
                                        if (listEmployee.get(i).getStaff_id().equalsIgnoreCase(sessionManager.getUserId())) {
                                            listEmployee.remove(i);
                                        }
                                    }
                                }

                                if (listEmployee.size() == 1) {
                                    selectedEmployeeID = listEmployee.get(0).getStaff_id();
                                    edtEmployee.setText(listEmployee.get(0).getName());
                                }

                            } else {
                                AppUtils.showToast(activity, "Could not get employee list.");
                                Log.e("getStaffMembers  ## ", "onResponse: 1");
                            }
                            isLoading = false;
                            apiCounts = apiCounts + 1;
                            showLoader(apiCounts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<StaffResponse> call, Throwable t) {
                        isLoading = false;
                        AppUtils.showToast(activity, "Could not get employee list.");
                        Log.e("getStaffMembers  ## ", "onResponse: 2");
                        apiCounts = apiCounts - 1;
                        showLoader(apiCounts);
                    }
                });
            }
        }).start();
    }

    private void showLoader(final int counts) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("RUN----------------------------------------- ", "run: " + counts);

                if (counts == 7) {
                    llLoading.setVisibility(View.GONE);
                    if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER))
                    {

                        if (listAreaAll.size() == 0)
                        {
                            ArrayList<ReportResponse.ReportsBean> listTemp = new ArrayList<>();
                            for (int i = 0; i < listReports.size(); i++) {
                                ReportResponse.ReportsBean bean = listReports.get(i);
                                listTemp.add(bean);
                            }

                            for (int i = 0; i < listTemp.size(); i++) {
                                if (listTemp.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                    listReports.clear();
                                    ReportResponse.ReportsBean bean = listTemp.get(i);
                                    listReports.add(bean);
                                }
                            }

                            edtDBC.setText("ADV : Advice");
                            dbs = "ADV";
                            selectedReportCode = "ADV";

                            inputEmployee.setVisibility(View.VISIBLE);
                            inputDate.setVisibility(View.VISIBLE);
                            llAdvice.setVisibility(View.VISIBLE);

                            inputArea.setVisibility(View.GONE);
                            inputSpeciality.setVisibility(View.GONE);
                            llWorkWith.setVisibility(View.GONE);
                            inputDoctor.setVisibility(View.GONE);
                            llNewCycle.setVisibility(View.GONE);
                            llSampleDetails.setVisibility(View.GONE);
                            rvVariation.setVisibility(View.GONE);
                        }
                        else//for add manager
                        {
                            if(sessionManager.getOffDayOrAdminDay().equalsIgnoreCase("1"))
                            {
                                ArrayList<ReportResponse.ReportsBean> listTemp = new ArrayList<>();
                                for (int i = 0; i < listReports.size(); i++) {
                                    ReportResponse.ReportsBean bean = listReports.get(i);
                                    listTemp.add(bean);
                                }

                                for (int i = 0; i < listTemp.size(); i++) {
                                    if (listTemp.get(i).getReport_code().equalsIgnoreCase("ADV")) {
                                        listReports.clear();
                                        ReportResponse.ReportsBean bean = listTemp.get(i);
                                        listReports.add(bean);
                                    }
                                }

                                edtDBC.setText("ADV : Advice");
                                dbs = "ADV";
                                selectedReportCode = "ADV";

                                inputEmployee.setVisibility(View.VISIBLE);
                                inputDate.setVisibility(View.VISIBLE);
                                llAdvice.setVisibility(View.VISIBLE);

                                inputArea.setVisibility(View.GONE);
                                inputSpeciality.setVisibility(View.GONE);
                                llWorkWith.setVisibility(View.GONE);
                                inputDoctor.setVisibility(View.GONE);
                                llNewCycle.setVisibility(View.GONE);
                                llSampleDetails.setVisibility(View.GONE);
                                rvVariation.setVisibility(View.GONE);
                            }
                        }

                    }
                } else {
                    llLoading.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getSpecialityFromArea(final String areaCode) {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        listSpeciality.clear();
        Call<SpecialistBean> loginCall = apiService.getSpecialityFromArea("500", areaCode, sessionManager.getUserId());
        loginCall.enqueue(new Callback<SpecialistBean>() {
            @Override
            public void onResponse(Call<SpecialistBean> call, Response<SpecialistBean> response) {
                if (response.body().getSuccess() == 1) {
                    listSpeciality = (ArrayList<SpecialistBean.SpecialityBean>) response.body().getSpeciality();
                } else {
                    AppUtils.showToast(activity, response.body().getMessage());
                }
                isLoading = false;
                llLoading.setVisibility(View.GONE);
                if(listSpeciality.size()>0)
                {
                    clickSpeciality();
                }
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void getFilteredSpeciality(final String areaCode) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                Log.e("************", "doInBackground: "+areaCode );

                listSpeciality.clear();
                List<DBSpeciality> listSpecialityFromDb = DBSpeciality.listAll(DBSpeciality.class);
                ArrayList<SpecialistBean.SpecialityBean> listTempSpec = new ArrayList<>();
                List<DBDoctor> listDr = DBDoctor.listAll(DBDoctor.class);
                for (int j = 0; j < listDr.size(); j++)
                {

                    if (areaCode.equalsIgnoreCase(listDr.get(j).getArea_id()))
                    {
                        SpecialistBean.SpecialityBean specialityBean = new SpecialistBean.SpecialityBean();
                        specialityBean.setSpeciality(listDr.get(j).getSpeciality());
                        specialityBean.setSpeciality_id(listDr.get(j).getSpeciality_id());
                        specialityBean.setSpeciality_code(listDr.get(j).getSpeciality_code());
                        //listSpeciality.add(specialityBean);
                        listTempSpec.add(specialityBean);
                    }
                }

                String speciality = "";
                if(listTempSpec.size()>0)
                {
                    for (int i = 0; i < listTempSpec.size(); i++)
                    {
                        if (speciality.contains(listTempSpec.get(i).getSpeciality_id()+""+listTempSpec.get(i).getSpeciality())) {
                            continue;
                        }
                        SpecialistBean.SpecialityBean specialityBean = new SpecialistBean.SpecialityBean();
                        specialityBean.setSpeciality(listTempSpec.get(i).getSpeciality());
                        specialityBean.setSpeciality_id(listTempSpec.get(i).getSpeciality_id());
                        if (i == 0) {
                            speciality = listTempSpec.get(i).getSpeciality_id()+""+listTempSpec.get(i).getSpeciality();
                        } else {
                            speciality = speciality + " , " + listTempSpec.get(i).getSpeciality_id()+""+listTempSpec.get(i).getSpeciality();
                        }

                        listSpeciality.add(specialityBean);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoading.setVisibility(View.GONE);

                if(listSpeciality.size()>0)
                {
                    Collections.sort(listSpeciality, new Comparator<SpecialistBean.SpecialityBean>() {
                        @Override
                        public int compare(SpecialistBean.SpecialityBean item, SpecialistBean.SpecialityBean t1) {
                            String s1 = item.getSpeciality();
                            String s2 = t1.getSpeciality();
                            return s1.compareToIgnoreCase(s2);
                        }

                    });

                    clickSpeciality();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    public  ArrayList<SpecialistBean.SpecialityBean> removeDuplicates(List<SpecialistBean.SpecialityBean> l)
    {
        Set<SpecialistBean.SpecialityBean> s = new TreeSet<SpecialistBean.SpecialityBean>(new Comparator<SpecialistBean.SpecialityBean>()
        {
            @Override
            public int compare(SpecialistBean.SpecialityBean o1, SpecialistBean.SpecialityBean o2)
            {
                if(o1.getSpeciality_id().equalsIgnoreCase(o2.getSpeciality_id()))
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }

        });
        s.addAll(l);
        ArrayList newList = new ArrayList(s);
        return newList;
        //List<Object> res = Arrays.asList(s.toArray());
    }

    private void getFilteredDocotrs(String specialityId)
    {
        Log.e("**********", "getFilteredDocotrs: "+specialityId );

        llLoading.setVisibility(View.VISIBLE);
        listDoctor.clear();
        List<DBDoctor> listDr = DBDoctor.listAll(DBDoctor.class);
        for (int j = 0; j < listDr.size(); j++) {
            if (specialityId.equalsIgnoreCase(listDr.get(j).getSpeciality_id()) &&
                    selectedAreaId.equalsIgnoreCase(listDr.get(j).getArea_id()))
            {
                DoctorResponse.DoctorsBean bean = new DoctorResponse.DoctorsBean();
                bean.setSpeciality_code(listDr.get(j).getSpeciality_code());
                bean.setDoctor_id(listDr.get(j).getDoctor_id());
                bean.setDoctor(listDr.get(j).getDoctor());
                bean.setEnable_focus(listDr.get(j).getEnable_focus());
                listDoctor.add(bean);
            }
        }

        if(listDoctor.size()>0)
        {
            Collections.sort(listDoctor, new Comparator<DoctorResponse.DoctorsBean>() {
                @Override
                public int compare(DoctorResponse.DoctorsBean item, DoctorResponse.DoctorsBean t1) {
                    String s1 = item.getDoctor();
                    String s2 = t1.getDoctor();
                    return s1.compareToIgnoreCase(s2);
                }

            });
        }

        llLoading.setVisibility(View.GONE);
    }

    private void getDoctorFromSpeciality(String areaId, String specialityId)
    {
        isLoading = true;
        llLoading.setVisibility(View.VISIBLE);
        listDoctor.clear();
        Call<DoctorResponse> doctorCall = apiService.getDoctorFromSpeciality(areaId,
                "500",
                specialityId,
                sessionManager.getUserId(),
                sessionManager.getUserId());
        doctorCall.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response)
            {
                isLoading = false;

                if (response.body().getSuccess() == 1)
                {
                    listDoctor = (ArrayList<DoctorResponse.DoctorsBean>) response.body().getDoctors();
                }
                else
                {
                    AppUtils.showToast(activity, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

        llLoading.setVisibility(View.GONE);
    }

    public void onClickListeners()
    {
       /* ActivityDailyCallReport.ivAddArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityDailyCallReport.viewPager.getCurrentItem()==0)
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (sessionManager.getDayEnd().equals("false"))
                    {
                        if (sessionManager.getCallDoneFromTP().equalsIgnoreCase("true"))
                        {
                            showListDialog(ADD_AREA);
                        }
                        else
                        {
                            AppUtils.showToast(activity, "To add area, You should have done atleast one call from your current tourplan.");
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity,"Your day has been ended by you.Kindly try again tomorrow.");
                    }
                }
            }
        });*/

        try {
            llNewCycle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbNewCycle.performClick();
                }
            });

            llWorkWith.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    cbWorkWith.performClick();
                }
            });

            ivAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        if (variationAdapter != null)
                        {
                            if (listVariation.size() > 1)
                            {
                                Log.e("CODE::<< ", "onClick: "+selectedDoctorId  +"      "+selectedReportCode);

                                if(selectedReportCode.equalsIgnoreCase("DCR") ||
                                        selectedReportCode.equalsIgnoreCase("ACR") ||
                                        selectedReportCode.equalsIgnoreCase("JCR") ||
                                        selectedReportCode.equalsIgnoreCase("LCR") ||
                                        selectedReportCode.equalsIgnoreCase("NCR") ||
                                        selectedReportCode.equalsIgnoreCase("SRD") ||
                                        selectedReportCode.equalsIgnoreCase("TNS") ||
                                        selectedReportCode.equalsIgnoreCase("XCR") ||
                                        selectedReportCode.equalsIgnoreCase("ZCR"))
                                {
                                    if(isNCREntry)
                                    {
                                        showDialog("Product", 0);
                                    }
                                    else
                                    {
                                        if(selectedDoctorId.equalsIgnoreCase(""))
                                        {
                                            AppUtils.showToast(activity,"Please select doctor first.");
                                        }
                                        else
                                        {
                                            showDialog("Product", 0);
                                        }
                                    }
                                }
                                else if(selectedReportCode.equalsIgnoreCase("INT") ||
                                        selectedReportCode.equalsIgnoreCase("ROA") ||
                                        selectedReportCode.equalsIgnoreCase("ROR"))
                                {
                                    showDialog("Product", 0);
                                }
                                else
                                {
                                    showDialog("Product", 0);
                                }
                                //showDialog("Product", 0);
                            } else {
                                AppUtils.showToast(activity, "No Products Found.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            ivNewCycle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbNewCycle.performClick();
                }
            });

            cbNewCycle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        newCycle = true;
                        ivNewCycle.setSelected(true);
                    } else {
                        newCycle = false;
                        ivNewCycle.setSelected(false);
                    }
                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ivEmployee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cbWorkWith.performClick();
            }
        });

        cbWorkWith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View viewWorkWith = activity.findViewById(R.id.viewWorkWith);
                if (isChecked)
                {
                    inputWorkWith.setVisibility(View.GONE);
                    viewWorkWith.setVisibility(View.VISIBLE);
                    ivEmployee.setSelected(true);
                } else
                    {
                    inputWorkWith.setVisibility(View.VISIBLE);
                    viewWorkWith.setVisibility(View.GONE);
                    ivEmployee.setSelected(false);
                }
            }
        });

        edtArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (isLoading) {
                        AppUtils.showLoadingToast(activity);
                    } else {
                        if (listArea.size() > 0) {
                            showListDialog(AREA);
                        } else {
                            AppUtils.showToast(activity, "Please fill up Tour Plan first.");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        edtEmployee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (isLoading) {
                        AppUtils.showLoadingToast(activity);
                    } else {
                        if (listEmployee.size() > 0) {
                            showListDialog(EMPLOYEE);
                        } else {
                            AppUtils.showToast(activity, "Employee not found");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        edtSpeciality.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clickSpeciality();
            }
        });

        edtDBC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickReportType();
            }
        });


        edtDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    datePicker(edtDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        edtDoctor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDoctor();
            }
        });

        edtWorkWith.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickWorkWith();
            }
        });

        llSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkStoragePermission();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        llSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (sessionManager.getDayEnd().equals("false"))
                    {
                        if (validatePrimaryData())
                        {
                            Gson gson = new Gson();
                            String productJson = gson.toJson(listSelectedProducts);

                            boolean isWorkWith = true;
                            if (cbWorkWith.isChecked()) {
                                isWorkWith = true;
                            } else {
                                isWorkWith = false;
                            }

                            if(selectedReportCode.equalsIgnoreCase("INT") ||
                                    selectedReportCode.equalsIgnoreCase("ROR") ||
                                    selectedReportCode.equalsIgnoreCase("ROA"))
                            {
                                focusForString = "";
                            }



                            NewEntryGetSet getSet = new NewEntryGetSet(edtArea.getText().toString().trim(),
                                    selectedAreaId,
                                    edtSpeciality.getText().toString().trim(),
                                    selectedSpecialityId,
                                    selectedReportCode,
                                    edtWorkWith.getText().toString().trim(),
                                    workWithString,
                                    edtDoctor.getText().toString().trim(),
                                    selectedDoctorId,
                                    newCycle ? "1" : "0",
                                    edtRemarks.getText().toString().trim(),
                                    "",
                                    edtInternee.getText().toString().trim(),
                                    AppUtils.getStringFromArrayListVariations(listSelectedProducts),
                                    focusForString,
                                    isWorkWith,
                                    String.valueOf(System.currentTimeMillis() / 1000),
                                    NCRDrData,
                                    selectedEmployeeID,
                                    edtEmployee.getText().toString().trim(),
                                    edtDate.getText().toString().trim(),
                                    edtAdvice.getText().toString().trim(),
                                    sessionManager.getUserId());

                            getSet.save();

                            //Beacsue when came from planned entjry, the speciality AND area  may not available as regular dcr
                            if(isPlannerClicked)
                            {
                                edtSpeciality.setText("");
                                selectedSpecialityId = "";

                                edtArea.setText("");
                                selectedAreaId = "";
                            }

                            isPlannerClicked = false;

                            isNCREntry = false;

                            AppUtils.showToast(activity, "Entry Saved!");

                            AppUtils.storeJsonResponse(new Gson().toJson(getSet), selectedReportCode + "_Call");

                            if(selectedReportCode.equalsIgnoreCase("STK"))
                            {
                                sessionManager.setIsSTKDone(true);
                            }


                            if (sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER)
                                    && listArea.size()==0)//for manager
                            {
                                selectedEmployeeID = "";
                                edtEmployee.setText("");
                                edtDate.setText("");
                                edtAdvice.setText("");
                            }
                            else//for working manager and MR
                            {

                                if (listWorkWith != null && listWorkWith.size() > 0) {
                                    for (int i = 0; i < listWorkWith.size(); i++) {
                                        WorkWithResponse.StaffBean staffBean = listWorkWith.get(i);
                                        staffBean.setSelected(false);
                                        listWorkWith.set(i, staffBean);
                                    }
                                }

                                workWithString = "";
                                NCRDrData = "";
                                txtAddCount.setText("1");

                                //edtSpeciality.setText("");
                                edtInternee.setText("");
                                edtRemarks.setText("");
                                dbs = "DCR";
                                selectedReportCode = "DCR";
                                edtDBC.setText("DCR : Daily Call Report");
                                edtWorkWith.setText("");
                                selectedDoctorId = "";
                                enable_focus = 0;
                                edtDoctor.setText("");
                                cbNewCycle.setChecked(false);

                                selectedEmployeeID = "";
                                edtEmployee.setText("");
                                edtDate.setText("");
                                edtAdvice.setText("");

                                inputArea.setVisibility(View.VISIBLE);
                                inputDoctor.setVisibility(View.VISIBLE);

                                llWorkWith.setVisibility(View.VISIBLE);

                                if (cbWorkWith.isChecked()) {
                                    inputWorkWith.setVisibility(View.GONE);
                                } else {
                                    inputWorkWith.setVisibility(View.VISIBLE);
                                }

                                inputInternee.setVisibility(View.GONE);
                                edtDoctor.setText("");
                                inputDoctor.setEnabled(true);
                                inputRMK.setVisibility(View.GONE);
                                llAdvice.setVisibility(View.GONE);
                                inputSpeciality.setVisibility(View.VISIBLE);
                                llNewCycle.setVisibility(View.VISIBLE);

                                rvVariation.setVisibility(View.VISIBLE);
                                viewLine.setVisibility(View.GONE);
                                llSampleDetails.setVisibility(View.VISIBLE);

                                /*listSelectedProducts.clear();
                                listVariation.clear();
                                ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                                VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                                bean.setStock("");
                                bean.setItem_code("");
                                bean.setItem_id_code("");
                                bean.setProduct_id("0");
                                bean.setReason("Regular Sample");
                                bean.setReason_code("R");
                                bean.setName("Product");
                                listVariation.add(bean);
                                variationAdapter = new VariationAdapter(listVariation);
                                rvVariation.setAdapter(variationAdapter);*/


                                ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                                VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                                bean.setStock("");
                                bean.setItem_code("");
                                bean.setItem_id_code("");
                                bean.setProduct_id("0");
                                bean.setReason("Regular Sample");
                                bean.setReason_code("R");
                                bean.setName("Product");
                                listTemp.add(bean);
                                variationAdapter = new VariationAdapter(listTemp);
                                rvVariation.setAdapter(variationAdapter);

                                for (int i = 0; i < listVariation.size(); i++)
                                {
                                    VariationResponse.VariationsBean bean1 = listVariation.get(i);
                                    bean1.setStock("");
                                    bean1.setChecked(false);
                                    //Added for not take previous reason 7_3_19
                                    bean1.setReason("");
                                    bean1.setReason_code("R");
                                    listVariation.set(i, bean1);
                                }

                                Log.i("************* ", "onClick: "+listVariation.size());
                            }

                            if (handlerNCR != null) {
                                Message message = Message.obtain();
                                message.what = 101;
                                handlerNCR.sendMessage(message);
                            }
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Your day has been ended by you.Kindly try again tomorrow.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        llEntry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
                    ArrayList<NewEntryGetSet> listEntry = (ArrayList<NewEntryGetSet>) listOffline;
                    ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                    for (int i = 0; i < listEntry.size(); i++) {
                        if (listEntry.get(i).getUser_id().equals(sessionManager.getUserId())) {
                            listUserEntry.add(listEntry.get(i));
                        }
                    }

                    if (listUserEntry.size() > 0)
                    {
                        //sessionManager.setCallDoneFromTP("true");
                        Intent intent = new Intent(activity, ActivityPendingEntry.class);
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    } else {
                        AppUtils.showToast(activity, "No Pending Entry For Submission.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        llView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    if(sessionManager.isNetworkAvailable())
                    {
                        Intent intent = new Intent(activity, ViewSubmittedEntryActivity.class);
                        activity.startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        llPlannedEntry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showListDialog(DOCTOR_PLANNER);
            }
        });
    }

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    String selectedDate = "";

    private void datePicker(final EditText edtTaskName)
    {

        if (edtTaskName.getText().toString().trim().length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(edtTaskName.getText().toString().trim().toString(), "yyyy-MM-dd", "dd/MM/yyyy");
                String[] datearr = date.split("/");
                mDay = Integer.parseInt(datearr[0]);
                mMonth = Integer.parseInt(datearr[1]);
                mMonth = mMonth - 1;
                mYear = Integer.parseInt(datearr[2]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                view.setMinDate(new Date().getTime());

                date_time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedDate2 = new Date();
                try
                {
                    convertedDate2 = dateFormat.parse(date_time);
                    String showDate = df.format(convertedDate2);
                    Log.e("showDate", showDate.toString());
                    selectedDate = showDate;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                edtTaskName.setText(selectedDate);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void clickSpeciality()
    {
        try {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (edtArea.getText().toString().length() > 0) {
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                } else {
                    if (listSpeciality.size() > 0) {
                        showListDialog(SPECIALITY);
                    } else {
                        AppUtils.showToast(activity, "No Speciality Found.");
                    }
                }
            } else {
                AppUtils.showToast(activity, "Please Select Area First");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickReportType()
    {
        try {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (isLoading) {
                AppUtils.showLoadingToast(activity);
            } else {
                if (listReports.size() > 0) {
                    showListDialog("DBC");
                } else {
                    AppUtils.showToast(activity, "No Reports available.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickDoctor()
    {
        try {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (edtSpeciality.getText().toString().length() > 0)
            {
                if (isLoading) {
                    AppUtils.showLoadingToast(activity);
                } else {
                    if (listDoctor.size() > 0) {
                        showListDialog(DOCTOR);
                    } else {
                        AppUtils.showToast(activity, "No doctors Found.");
                    }
                }

            } else {
                AppUtils.showToast(activity, "Please Select Doctor Speciality First.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickWorkWith()
    {
        try {
            if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (isLoading) {
                AppUtils.showLoadingToast(activity);
            } else {
                if (listWorkWith.size() > 0) {
                    showListDialog(WORKWITH);
                } else {
                    AppUtils.showToast(activity, "No Work With List Available");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            datepicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);

            try {
                datepicker.getDatePicker().setMaxDate(new Date().getTime());
                return datepicker;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public static void populateSetDate(int year, int month, int day) {
        String date = month + "/" + day + "/" + year;
        try {
//			Selected Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
            Date dtSelected = dateFormat.parse(date);
            SimpleDateFormat formatNeeded = new SimpleDateFormat("yyyy-MM-dd");
            date = formatNeeded.format(dtSelected);

//			Current Date
            Date d = new Date();
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
            Date dtCurrent = dateFormat2.parse(d.toString());

//			Comparison of both dates
            if (dtSelected.before(dtCurrent)) {
                edtDate.setText(date);
            } else {
                edtDate.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog() {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtConfirmation = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Submit Details");

            if (pendingEntryCount == 1) {
                txtConfirmation.setText("Do you want to submit your " + pendingEntryCount + " entry?");
            } else {
                txtConfirmation.setText("Do you want to submit your " + pendingEntryCount + " entries?");
            }

            // For log purpose

            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                    }

                    if (sessionManager.isNetworkAvailable())
                    {
                        //apiTaskToken();
                        List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);

                        ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                        for (int i = 0; i < books.size(); i++)
                        {
                            if (books.get(i).getUser_id().equals(sessionManager.getUserId()))
                            {

                                listUserEntry.add(books.get(i));
                            }
                        }

                        if (listUserEntry.size() > 0)
                        {
                            submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager, listUserEntry),false);
                        }
                    } else {
                        AppUtils.showToast(activity, "Please check your internet connection.");
                        // For log purpose
                        try {
                            AppUtils.storeJsonResponse("No internet available while mannual submit", "NoInternet");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitPendingEntries(final String stringToPass, final boolean isForDayEnd)
    {
        new AsyncTask<Void, Void, Void>()
        {
            int success = 0;
            String message = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("field_entry", stringToPass);
                    hashMap.put("login_user_id", sessionManager.getUserId());
                    Log.e("Submit String Request >>> ", "doInBackground: " + hashMap.toString());

                    AppUtils.storeJsonResponse(hashMap.toString(), "SubmitRequest");

                    String response = "";
                    response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SUBMIT_ENTRIES, hashMap);
                    Log.e("Submit String Response >>> ", "doInBackground: " + response);

                    AppUtils.storeJsonResponse(response, "SubmitResponse");

                    JSONObject jsonObject = new JSONObject(response);
                    success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                    message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                llLoading.setVisibility(View.GONE);
                AppUtils.showToast(activity, message);
                if (success == 1)
                {
                    if(!isForDayEnd)
                    {
                        Call<SubmittedResponse> dataCall = apiService.getSubmittedEntry(AppUtils.currentDateForApi(), sessionManager.getUserId(), sessionManager.getUserId());
                        dataCall.enqueue(new Callback<SubmittedResponse>() {
                            @Override
                            public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) {
                                try
                                {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        List<SubmittedResponse.ReportBean.DataBean> listData = response.body().getReport().getData();
                                        if (listData.size() > 0)
                                        {
                                            sessionManager.setCallDoneFromTP("true");
                                            for (int i = 0; i < listData.size(); i++)
                                            {
                                                DrDcrGetSet getSet = new DrDcrGetSet();
                                                getSet.setDrId(listData.get(i).getDoctor_id());
                                                getSet.setDrName(listData.get(i).getDoctor_name());
                                                getSet.setReportType(listData.get(i).getReport_type());
                                                listSubmitedEntryOfDr.add(getSet);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                            }
                        });

                        sessionManager.setCallDoneFromTP("true");
                        NewEntryGetSet.deleteAll(NewEntryGetSet.class);
                    }
                    else
                    {
                        NewEntryGetSet.deleteAll(NewEntryGetSet.class);
                        sessionManager.logoutWithoutDialog();
                    }
                }
                else
                {
                    if(isForDayEnd)
                    {
                        NewEntryGetSet.deleteAll(NewEntryGetSet.class);
                        sessionManager.logoutWithoutDialog();
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void deleteEntry() {
        udbh = new UnisonDatabaseHelper(activity);
        sqlDB = udbh.getWritableDatabase();

        try {
			/*for (int i = 0; i < listApiId.size(); i++)
			{
				String whereClause = UnisonDatabaseHelper.ID+"='"+listApiId.get(i)+"'";
				sqlDB.delete(UnisonDatabaseHelper.DAILY_ENTRY_TABLE, null, null);
			}*/

            sqlDB.execSQL("delete from " + UnisonDatabaseHelper.DAILY_ENTRY_TABLE);

            sqlDB.close();
            udbh.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlDB != null && sqlDB.isOpen()) {
                sqlDB.close();
                udbh.close();
            }
        }
    }

    public boolean validatePrimaryData() {
        boolean isReturn = true;

        try {
            String dbs = selectedReportCode;

            if (!dbs.equalsIgnoreCase("STK"))
            {
                if (edtArea.getText().toString().length() < 1 &&
                        !dbs.equalsIgnoreCase("ADV") &&
                        !dbs.equalsIgnoreCase("STK") &&
                        !dbs.equalsIgnoreCase("NCR") &&
                        !dbs.equalsIgnoreCase("RMK")) {
                    AppUtils.showToast(activity, "Please Select Area");
                    isReturn = false;
                } else if (edtDBC.getText().toString().length() < 1) {
                    AppUtils.showToast(activity, "Please Select DBC");
                    isReturn = false;
                } else if (dbs.equals("DCR") ||
                        dbs.equals("LCR") ||
                        dbs.equals("ACR") ||
                        dbs.equals("XCR") ||
                        dbs.equals("TNS") ||
                        dbs.equals("JCR") ||
                        dbs.equals("SRD"))
                {
                    if (edtDBC.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select DBC");
                        isReturn = false;
                    } else if (edtSpeciality.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select Doctor Speciality");
                        isReturn = false;
                    } else if (!cbWorkWith.isChecked()) {
                        if (edtWorkWith.getText().toString().length() < 1) {
                            AppUtils.showToast(activity, "Please Select Work With");
                            isReturn = false;
                        }
                    } else if (edtDoctor.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select Doctor");
                        isReturn = false;
                    }
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("NCR")) {
                    if (edtDBC.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select DBC");
                        isReturn = false;
                    } else if (!cbWorkWith.isChecked()) {
                        if (edtWorkWith.getText().toString().length() < 1) {
                            AppUtils.showToast(activity, "Please Select Work With");
                            isReturn = false;
                        }
                    }
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("ROR")) {
                    if (edtDBC.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select DBC");
                        isReturn = false;
                    } else if (edtSpeciality.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select Doctor Speciality");
                        isReturn = false;
                    } else if (!cbWorkWith.isChecked()) {
                        if (edtWorkWith.getText().toString().length() < 1) {
                            AppUtils.showToast(activity, "Please Select Work With");
                            isReturn = false;
                        }
                    }
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("INT"))
                {
                    if (edtDBC.getText().toString().length() < 1)
                    {
                        AppUtils.showToast(activity, "Please Select DBC");
                        isReturn = false;
                    }
                    else if (!cbWorkWith.isChecked()) {
                        if (edtWorkWith.getText().toString().length() < 1) {
                            AppUtils.showToast(activity, "Please Select Work With");
                            isReturn = false;
                        }
                    } else if (edtInternee.getText().toString().length() == 0 ||
                            edtInternee.getText().toString().equalsIgnoreCase("0"))
                    {
                        AppUtils.showToast(activity, "Please Enter No of Internee");
                        isReturn = false;
                    }
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("DDT") || dbs.equals("RDT")) {
                    if (edtDBC.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select DBC");
                        isReturn = false;
                    } else if (!cbWorkWith.isChecked()) {
                        if (edtWorkWith.getText().toString().length() < 1) {
                            AppUtils.showToast(activity, "Please Select Work With");
                            isReturn = false;
                        }
                    }

                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("DBS")) {
                    if (edtDoctor.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Enter Doctor");
                        isReturn = false;
                    } else if (edtSpeciality.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Select Doctor Speciality");
                        isReturn = false;
                    }
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                } else if (dbs.equals("RMK")) {
                    if (edtRemarks.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Enter Remarks");
                        isReturn = false;
                    }
                } else if (dbs.equals("ADV")) {
                    if (edtEmployee.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Enter Employee Name");
                        isReturn = false;
                    } else if (edtDate.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Enter Date");
                        isReturn = false;
                    } else if (edtAdvice.getText().toString().length() < 1) {
                        AppUtils.showToast(activity, "Please Enter Advice");
                        isReturn = false;
                    }
                } else if (!dbs.equalsIgnoreCase("RMK")) {
                    if (!variationAdapter.isListFilled()) {
                        AppUtils.showToast(activity, "Please Fill Sample Details");
                        isReturn = false;
                    }
                }
            }
            else if (dbs.equals("STK"))
            {
                if (!variationAdapter.isListFilled()) {
                    AppUtils.showToast(activity, "Please Fill Sample Details");
                    isReturn = false;
                }
                //isReturn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isReturn;
    }

    public void setUpViews() {
        llMain = (LinearLayout) rootView.findViewById(R.id.llMain);
        llLoading = (LinearLayout) rootView.findViewById(R.id.llLoading);
        llLoading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llOffDay = (LinearLayout) rootView.findViewById(R.id.llOffDay);
        llOffDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        scrollview = (ScrollView) rootView.findViewById(R.id.scrollView);

        llNewCycle = (LinearLayout) rootView.findViewById(R.id.llNewCycle);

        llAdvice = (LinearLayout) rootView.findViewById(R.id.llAdvice);

        inputDoctor = (TextInputLayout) rootView.findViewById(R.id.inputDoctor);
        inputAdvice = (TextInputLayout) rootView.findViewById(R.id.inputAdvice);
        inputDate = (TextInputLayout) rootView.findViewById(R.id.inputDate);
        inputEmployee = (TextInputLayout) rootView.findViewById(R.id.inputEmployee);

        inputWorkWith = (TextInputLayout) rootView.findViewById(R.id.inputWorkWith);
        inputArea = (TextInputLayout) rootView.findViewById(R.id.inputArea);
        inputRMK = (TextInputLayout) rootView.findViewById(R.id.inputRMK);
        inputDBC = (TextInputLayout) rootView.findViewById(R.id.inputDBC);
        inputWorkWith = (TextInputLayout) rootView.findViewById(R.id.inputWorkWith);
        llSampleDetails = (LinearLayout) rootView.findViewById(R.id.llSampleDetails);

        llSave = (LinearLayout) rootView.findViewById(R.id.llSave);
        llSubmit = (LinearLayout) rootView.findViewById(R.id.llSubmit);
        llView = (LinearLayout) rootView.findViewById(R.id.llView);
        llEntry = (LinearLayout) rootView.findViewById(R.id.llAdd);
        inputSpeciality = (TextInputLayout) rootView.findViewById(R.id.inputSpeciality);

        cbWorkWith = (CheckBox) rootView.findViewById(R.id.cbWorkWith);
        ivEmployee = (ImageView) rootView.findViewById(R.id.ivEmployee);
        ivEmployee.setSelected(true);
        cbWorkWith.setTypeface(AppUtils.getTypefaceRegular(activity));
        llWorkWith = (LinearLayout) rootView.findViewById(R.id.llWorkWith);

        viewLine = (View) rootView.findViewById(R.id.view_line);

        txtAddCount = (TextView) rootView.findViewById(R.id.txtAddCount);

        cbNewCycle = (CheckBox) rootView.findViewById(R.id.cbNewCycle);

        ivNewCycle = (ImageView) rootView.findViewById(R.id.ivNewCycle);

        // New
        edtDBC = (EditText) rootView.findViewById(R.id.edtDBC);
        edtDBC.setText("DCR : Daily Call Report");
        selectedReportCode = "DCR";
        edtDoctor = (EditText) rootView.findViewById(R.id.edtDoctor);
        edtWorkWith = (EditText) rootView.findViewById(R.id.edtWorkWith);
        edtWorkWith.setText("");
        edtArea = (EditText) rootView.findViewById(R.id.edtArea);
        edtRemarks = (RegularEditText) rootView.findViewById(R.id.edtRemark);
        edtInternee = (EditText) rootView.findViewById(R.id.edtInternee);
        inputInternee = (TextInputLayout) rootView.findViewById(R.id.inputInternee);
        edtSpeciality = (EditText) rootView.findViewById(R.id.edtDoctorSpeciality);
        edtAdvice = (EditText) rootView.findViewById(R.id.edtAdvice);
        edtDate = (EditText) rootView.findViewById(R.id.edtDate);
        edtEmployee = (EditText) rootView.findViewById(R.id.edtEmployee);

        ivAdd = (ImageView) rootView.findViewById(R.id.ivAdd);

        rvVariation = (RecyclerView) rootView.findViewById(R.id.rvVariation);
        rvVariation.setLayoutManager(new LinearLayoutManager(activity));

        llPlannedEntry = (LinearLayout) rootView.findViewById(R.id.llPlannedEntry);
        viewPlanner = (View) rootView.findViewById(R.id.viewPlanner);

        VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
        bean.setStock("");
        bean.setItem_code("");
        bean.setItem_id_code("");
        bean.setProduct_id("0");
        bean.setReason("Regular Sample");
        bean.setReason_code("R");
        bean.setName("Product");
        listVariation.add(bean);
        variationAdapter = new VariationAdapter(listVariation);
        rvVariation.setAdapter(variationAdapter);

        if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
        {
            if(sessionManager.getOffDayOrAdminDay().equalsIgnoreCase("1"))
            {
                llOffDay.setVisibility(View.VISIBLE);
            }
            else
            {
                llOffDay.setVisibility(View.GONE);

                try {
                    listPlannedDoctor = (ArrayList<DBPlanner>) DBPlanner.listAll(DBPlanner.class);

                    if(listPlannedDoctor.size()>0)
                    {
                        llPlannedEntry.setVisibility(View.VISIBLE);
                        viewPlanner.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        llPlannedEntry.setVisibility(View.GONE);
                        viewPlanner.setVisibility(View.GONE);
                    }

                    Log.e("PLanner List >> ", "setUpViews: "+listPlannedDoctor.size() );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Check Internet and set data accordingly
                if (sessionManager.isNetworkAvailable())
                {
                    getDataFromServer();
                }
                else
                {
                    timer = new CountDownTimer(1000, 10000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            fillAllListFromDatabase();
                        }
                    };
                    timer.start();

                }
            }
        }
        else
        {

            try {
                listPlannedDoctor = (ArrayList<DBPlanner>) DBPlanner.listAll(DBPlanner.class);

                if(listPlannedDoctor.size()>0)
                {
                    llPlannedEntry.setVisibility(View.VISIBLE);
                    viewPlanner.setVisibility(View.VISIBLE);
                }
                else
                {
                    llPlannedEntry.setVisibility(View.GONE);
                    viewPlanner.setVisibility(View.GONE);
                }

                Log.e("PLanner List >> ", "setUpViews: "+listPlannedDoctor.size() );
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Check Internet and set data accordingly
            if (sessionManager.isNetworkAvailable())
            {
                getDataFromServer();
            }
            else
            {
                timer = new CountDownTimer(1000, 10000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        fillAllListFromDatabase();
                    }
                };
                timer.start();

            }
        }





        //For PLanner
        /*try {
            listPlannedDoctor = (ArrayList<DBPlanner>) DBPlanner.listAll(DBPlanner.class);

            if(listPlannedDoctor.size()>0)
            {
                llPlannedEntry.setVisibility(View.VISIBLE);
                viewPlanner.setVisibility(View.VISIBLE);
            }
            else
            {
                llPlannedEntry.setVisibility(View.GONE);
                viewPlanner.setVisibility(View.GONE);
            }

            Log.e("PLanner List >> ", "setUpViews: "+listPlannedDoctor.size() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check Internet and set data accordingly
        if (sessionManager.isNetworkAvailable())
        {
            getDataFromServer();
        }
        else
        {
            timer = new CountDownTimer(1000, 10000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    fillAllListFromDatabase();
                }
            };
            timer.start();

        }*/

    }

    private void initVariationAdapter() {
		/*if(handlerNCR!=null)
		{
			Message message = Message.obtain();
			message.what=101;
			handlerNCR.sendMessage(message);
		}*/

        dbs = "";
        cbNewCycle.setChecked(false);
        edtWorkWith.setText("");
        edtDoctor.setText("");
        edtInternee.setText("");
        edtRemarks.setText("");
        txtAddCount.setText("1");

        ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
        VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
        bean.setStock("");
        bean.setItem_code("");
        bean.setItem_id_code("");
        bean.setProduct_id("0");
        bean.setReason("Regular Sample");
        bean.setReason_code("R");
        bean.setName("Product");
        listTemp.add(bean);
        variationAdapter = new VariationAdapter(listTemp);
        rvVariation.setAdapter(variationAdapter);

        for (int i = 0; i < listVariation.size(); i++) {
            VariationResponse.VariationsBean getSet = listVariation.get(i);
            getSet.setStock("");
            getSet.setChecked(false);
            listVariation.set(i, getSet);
        }

		/*listVariation.clear();
		ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
		VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
		bean.setStock("");
		bean.setItem_code("");
		bean.setItem_id_code("");
		bean.setProduct_id("0");
		bean.setReason("Regular Sample");
		bean.setReason_code("R");
		bean.setName("Product");
		listVariation.add(bean);
		variationAdapter = new VariationAdapter(listVariation);
		rvVariation.setAdapter(variationAdapter);*/
    }

    private void showConfirmationDialogForDayEnd() {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txt_Dialog_Delete = (TextView) dialog.findViewById(R.id.tvDescription);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.tvHeader);
            TextView btnNo = (TextView) dialog.findViewById(R.id.tvCancel);
            TextView btnYes = (TextView) dialog.findViewById(R.id.tvConfirm);


            txt_Dialog_Delete.setText("Are you sure you want to end the day?");
            txtHeader.setText("Confirm");


            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();

                    confirmAgain();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogForFocused(final String doctorId) {
        try {
            listRestriction = new ArrayList<>();

			/*LayoutInflater layoutInflater = LayoutInflater.from(activity);
			View promptView = layoutInflater.inflate(R.layout.dialog_dcr_focused, null);

			final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(promptView);*/

            final Dialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_dcr_focused, null);
            dialog.setContentView(sheetView);

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView, activity);
                }
            });
            dialog.findViewById(R.id.ivBack).setVisibility(View.GONE);

            final EditText edttext1 = (EditText) dialog.findViewById(R.id.edtProduct1);
            final EditText edttext2 = (EditText) dialog.findViewById(R.id.edtProduct2);
            final EditText edttext3 = (EditText) dialog.findViewById(R.id.edtProduct3);
            final EditText edttext4 = (EditText) dialog.findViewById(R.id.edtProduct4);

            edttext3.setVisibility(View.GONE);
            edttext4.setVisibility(View.GONE);

            final EditText edtReason1 = (EditText) dialog.findViewById(R.id.edtReason1);
            final EditText edtReason2 = (EditText) dialog.findViewById(R.id.edtReason2);
            final EditText edtReason3 = (EditText) dialog.findViewById(R.id.edtReason3);
            final EditText edtReason4 = (EditText) dialog.findViewById(R.id.edtReason4);

            edtReason3.setVisibility(View.GONE);
            edtReason4.setVisibility(View.GONE);

            MitsAutoHeightListView listview = (MitsAutoHeightListView) dialog.findViewById(R.id.lvFocusedFor);
            TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            LinearLayout llsubmit = (LinearLayout) dialog.findViewById(R.id.llSubmit);
            LinearLayout llcancel = (LinearLayout) dialog.findViewById(R.id.llcancel);
            llcancel.setVisibility(View.GONE);


            listview.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.GONE);

            edttext1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (listVariation != null && listVariation.size() > 1) {
                        try {
                            showFocusedDialog(edttext1, "Product", "1");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AppUtils.showToast(activity, "Products Not Found.");
                    }
                }
            });

            edttext2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (listVariation != null && listVariation.size() > 1) {
                        try {
                            showFocusedDialog(edttext2, "Product", "2");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AppUtils.showToast(activity, "Products Not Found.");
                    }
                }
            });

            edttext3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (listVariation != null && listVariation.size() > 0) {
                        try {
                            showFocusedDialog(edttext3, "Product", "3");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            edttext4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (listVariation != null && listVariation.size() > 0) {
                        try {
                            showFocusedDialog(edttext4, "Product", "4");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            edtReason1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        showFocusedDialog(edtReason1, "Reason", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            edtReason2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        showFocusedDialog(edtReason2, "Reason", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            edtReason3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showFocusedDialog(edtReason3, "Reason", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            edtReason4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showFocusedDialog(edtReason4, "Reason", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            llsubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    listRestriction = new ArrayList<String>();

					/*int countNew=0;
					int countEnhance=0;*/

                    if (product1.isEmpty() && product2.isEmpty()) {
                        AppUtils.showToast(activity, "Please select product.");

                    }
					/*else if (edtReason1.getText().toString().trim().equalsIgnoreCase("") && edtReason2.getText().toString().trim().equalsIgnoreCase(""))
					{
						Toast toast = Toast.makeText(activity, "Please select product.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}*/
                    else if (!product1.isEmpty() && edtReason1.getText().toString().equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select type.");

                    } else if (!product2.isEmpty() && edtReason2.getText().toString().equalsIgnoreCase("")) {
                        AppUtils.showToast(activity, "Please select type.");
                    } else if (!edtReason1.getText().toString().equalsIgnoreCase("") && product1.isEmpty()) {
                        AppUtils.showToast(activity, "Please select product.");
                    } else if (!edtReason2.getText().toString().equalsIgnoreCase("") && product2.isEmpty()) {
                        AppUtils.showToast(activity, "Please select product.");
                    } else {
                        listRestriction.add(edtReason1.getText().toString().trim());
                        listRestriction.add(edtReason2.getText().toString().trim());
                        listRestriction.add(edtReason3.getText().toString().trim());
                        listRestriction.add(edtReason4.getText().toString().trim());

                        countNew = 0;
                        countEnhance = 0;

                        for (int i = 0; i < listRestriction.size(); i++) {
                            if (listRestriction.get(i).equalsIgnoreCase("New")) {
                                countNew = countNew + 1;
                            } else if (listRestriction.get(i).equalsIgnoreCase("Enhance")) {
                                countEnhance = countEnhance + 1;
                            }
                        }

                        if (countNew > 2) {
                            AppUtils.showToast(activity, "You can enter only 2 product as new.");
                        } else if (countEnhance > 2) {
                            AppUtils.showToast(activity, "You can enter only 2 product as enhance.");
                        } else {
                            //Toast.makeText(activity, "Slection is Okay", Toast.LENGTH_SHORT).show();

                            ArrayList<String> listString = new ArrayList<String>();

                            String tempProduct1 = "", tempProduct2 = "", tempProduct3 = "", tempProduct4 = "";

                            if (!product1.isEmpty()) {
                                tempProduct1 = doctorId + "#" + edttext1.getText().toString().trim() + "---" + edtReason1.getText().toString().trim();
                                listString.add(tempProduct1);
                            }
                            if (!product2.isEmpty()) {
                                tempProduct2 = doctorId + "#" + edttext2.getText().toString().trim() + "---" + edtReason2.getText().toString().trim();
                                listString.add(tempProduct2);
                            }
                            if (!edttext3.getText().toString().equalsIgnoreCase("")) {
                                tempProduct3 = doctorId + "#" + edttext3.getText().toString().trim() + "---" + edtReason3.getText().toString().trim();
                                listString.add(tempProduct3);
                            }
                            if (!edttext4.getText().toString().equalsIgnoreCase("")) {
                                tempProduct4 = doctorId + "#" + edttext4.getText().toString().trim() + "---" + edtReason4.getText().toString().trim();
                                listString.add(tempProduct4);
                            }


                            String finalString = "";
                            for (int i = 0; i < listString.size(); i++) {
                                if (finalString.length() == 0) {
                                    finalString = listString.get(i);
                                } else {
                                    finalString = finalString + "," + listString.get(i);
                                }
                            }

                            focusForString = finalString;

                            Log.e("FINAL STRING  ------>>>>", "onClick: " + finalString);

                            dialog.dismiss();

                        }
                    }
                }
            });

            llcancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    product1 = "";
                    product2 = "";
                    product3 = "";
                    product4 = "";
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    product1 = "";
                    product2 = "";
                    product3 = "";
                    product4 = "";
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmAgain() {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txt_Dialog_Delete = (TextView) dialog.findViewById(R.id.tvDescription);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.tvHeader);
            TextView btnNo = (TextView) dialog.findViewById(R.id.tvCancel);
            TextView btnYes = (TextView) dialog.findViewById(R.id.tvConfirm);


            txt_Dialog_Delete.setText("Please confirm again.");
            txtHeader.setText("Confirm");


            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();

                    if (sessionManager.isNetworkAvailable())
                    {
                        dayEnd();
//						getApiStringFromDataBase();
                    } else {
                        AppUtils.showToast(activity, "Please check your internet connection.");
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String strStatus = "";

    private void dayEnd()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<DayEndResponse> reportCall = apiService.dayEnd(sessionManager.getUserId(), sessionManager.getUserId());
        reportCall.enqueue(new Callback<DayEndResponse>() {
            @Override
            public void onResponse(Call<DayEndResponse> call, Response<DayEndResponse> response) {
                try
                {
                    if (response.body().getSuccess() == 1)
                    {
                        sessionManager.setDayEnd("true");

                        dayEndWithSubmitAndLogout();

                        //UnComment below after check DayEnd : kiran

                        /*AppUtils.showToast(activity, "Day end successfully.");
                        sessionManager.setDayEnd("true");
                        edtDBC.setText("DCR : Daily Call Report");
                        dbs = "DCR";

                        txtAddCount.setText("1");

                        edtArea.setText("");
                        edtSpeciality.setText("");
                        edtRemarks.setText("");
                        edtWorkWith.setText("");
                        edtDoctor.setText("");
                        cbNewCycle.setChecked(false);

                        inputArea.setVisibility(View.VISIBLE);
                        inputDoctor.setVisibility(View.VISIBLE);
                        inputWorkWith.setVisibility(View.VISIBLE);

                        inputInternee.setVisibility(View.GONE);
                        edtDoctor.setText("");
                        inputDoctor.setEnabled(true);
                        inputRMK.setVisibility(View.GONE);
                        llAdvice.setVisibility(View.GONE);
                        inputSpeciality.setVisibility(View.VISIBLE);
                        llNewCycle.setVisibility(View.VISIBLE);

                        rvVariation.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.GONE);
                        llSampleDetails.setVisibility(View.VISIBLE);

                        listSelectedProducts.clear();
                        listVariation.clear();
                        ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                        VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                        bean.setStock("");
                        bean.setItem_code("");
                        bean.setItem_id_code("");
                        bean.setProduct_id("0");
                        bean.setReason("Regular Sample");
                        bean.setReason_code("R");
                        bean.setName("Product");
                        listVariation.add(bean);
                        variationAdapter = new VariationAdapter(listVariation);
                        rvVariation.setAdapter(variationAdapter);

                        if (handlerNCR != null) {
                            Message message = Message.obtain();
                            message.what = 101;
                            handlerNCR.sendMessage(message);
                        }*/

                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DayEndResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
        llLoading.setVisibility(View.GONE);
    }


    private void dayEndWithSubmitAndLogout()
    {
        if (sessionManager.isNetworkAvailable())
        {
            //apiTaskToken();
            List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);
            ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
            for (int i = 0; i < books.size(); i++)
            {
                if (books.get(i).getUser_id().equals(sessionManager.getUserId()))
                {

                    listUserEntry.add(books.get(i));
                }
            }
            if (listUserEntry.size() > 0)
            {
                submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager, listUserEntry),true);
            }
        }
        else
        {
            AppUtils.showToast(activity, "Please check your internet connection.");
            // For log purpose
            try {
                AppUtils.storeJsonResponse("No internet available while mannual submit - DayEnd", "NoInternet-DayEnd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showFocusedDialog(final EditText edittext, final String cameFrom, final String pos) {
        try {
           /* LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View promptView = layoutInflater.inflate(R.layout.dialog_listview, null);

            final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(promptView);*/

            final Dialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_listview, null);
            dialog.setContentView(sheetView);

            dialog.findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });
            listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView, activity);
                }
            });
            final BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.lv_Dialog);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader_Dialog_ListView);

            final TextInputLayout inputSearch = (TextInputLayout) dialog.findViewById(R.id.inputSearch);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch_Dialog_ListView);

            final List<String> array = new ArrayList<String>();
            array.add("New");
            array.add("Enhance");

            if (cameFrom.equalsIgnoreCase("Product")) {
                /*Change*/
                //listVariation.addAll(listTempVariation);

                txtHeader.setText("Select Product");
                if (listVariation != null && listVariation.size() > 0) {
                    productAdapter = new ProductAdapter(activity, listVariation, "Product", dialog, edittext, pos, false);
                    listView.setAdapter(productAdapter);
                }

                inputSearch.setVisibility(View.VISIBLE);

                edtSearch.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                    {
                        int textlength = edtSearch.getText().length();

                        listVariationSearch.clear();
                        for (int i = 0; i < listVariation.size(); i++) {
                            if (listVariation.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listVariation.get(i).getItem_id_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim())) {
                                listVariationSearch.add(listVariation.get(i));
                            }
                        }

                        productAdapter = new ProductAdapter(activity, listVariation, "Product", dialog, edittext, pos, true);
                        listView.setAdapter(productAdapter);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }
                });
            } else {
                inputSearch.setVisibility(View.GONE);
                ArrayAdapter<String> mHistory = new ArrayAdapter<String>(activity, R.layout.rowview_text, array);

				/*ArrayAdapter<String> mHistory = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,array){
					@Override
					public View getView(int position, View convertView, ViewGroup parent){
						TextView item = (TextView) super.getView(position,convertView,parent);
						item.setTypeface(AppUtils.getTypefaceRegular(activity));
						return item;
					}
				};*/


                listView.setAdapter(mHistory);


                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //listRestriction.add(array.get(position));
                            edittext.setText(array.get(position));

                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ProductAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater = null;
        ArrayList<VariationResponse.VariationsBean> items;
        String spinnerRef;
        int pos = 0;
        private EditText view;
        Dialog dialog;
        String focusPosition = "";
        boolean isForSearch = false;

        public ProductAdapter(Activity a,
                              ArrayList<VariationResponse.VariationsBean> item,
                              String ref,
                              Dialog dialog,
                              EditText view,
                              String pos,
                              boolean iForSearch) {
            this.activity = a;
            ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
            for (int i = 0; i < item.size(); i++) {
                if (item.get(i).getProduct_type().equalsIgnoreCase("1") || item.get(i).getName().equalsIgnoreCase("product"))
                {
                }
                else
                {
                    listTemp.add(item.get(i));
                }
            }
            try
            {
                listTemp.addAll(DataUtils.getSalesProduct(sessionManager));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.items = listTemp;
            this.spinnerRef = ref;
            this.dialog = dialog;
            this.view = view;
            this.focusPosition = pos;
            this.isForSearch = iForSearch;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            if (isForSearch) {
                return listVariationSearch.size();
            } else {
                return items.size();
            }
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            View rowView = convertView;
            if (convertView == null)
            {
                try {
                    rowView = inflater.inflate(R.layout.rowview_mkt_code, null);

                    holder = new ViewHolder();

                    holder.txtmktCode = (TextView) rowView.findViewById(R.id.txtName);

                    rowView.setTag(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            final VariationResponse.VariationsBean getSet;
            if (isForSearch) {
                getSet = listVariationSearch.get(position);
            } else {
                getSet = items.get(position);
            }

            if (getSet.getProduct_type().equalsIgnoreCase("1")) {
                rowView.setVisibility(View.GONE);
            } else {
                rowView.setVisibility(View.VISIBLE);
            }

            try {
                if (spinnerRef.equals("Product")) {
                    if (!getSet.getVariation_id().equals("") ||
                            !getSet.getProduct_id().equals("")) {
                        holder.txtmktCode.setText(getSet.getItem_id_code() + " : " + getSet.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();

                        view.setText(getSet.getItem_id_code() + " : " + getSet.getName());

                        if (focusPosition.equalsIgnoreCase("1")) {
                            if (product2.equalsIgnoreCase(getSet.getItem_id_code())) {
                                AppUtils.showToast(activity, "Please select another product.");
                                view.setText("");
                            } else {
                                product1 = getSet.getItem_id_code();
                            }
                        } else if (focusPosition.equalsIgnoreCase("2")) {


                            if (product1.equalsIgnoreCase(getSet.getItem_id_code())) {
                                AppUtils.showToast(activity, "Please select another product.");
                                view.setText("");
                            } else {
                                product2 = getSet.getItem_id_code();
                            }

                        } else if (focusPosition.equalsIgnoreCase("3")) {
                            if (product1.equalsIgnoreCase(getSet.getProduct_id()) || product2.equalsIgnoreCase(getSet.getProduct_id()) || product4.equalsIgnoreCase(getSet.getProduct_id())) {
                                AppUtils.showToast(activity, "Please select another product.");
                                view.setText("");
                            } else {
                                product3 = getSet.getProduct_id();
                            }

                        } else if (focusPosition.equalsIgnoreCase("4")) {
                            if (product1.equalsIgnoreCase(getSet.getProduct_id()) || product2.equalsIgnoreCase(getSet.getProduct_id()) || product3.equalsIgnoreCase(getSet.getProduct_id())) {
                                AppUtils.showToast(activity, "Please select another product.");
                                view.setText("");
                            } else {
                                product4 = getSet.getProduct_id();
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            return rowView;
        }

        private class ViewHolder {
            TextView txtmktCode;
            ;
        }
    }

    InputFilter filter = new InputFilter()
    {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }
            return null;
        }
    };

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;

    private void checkStoragePermission()
    {
        try
        {
            int result;
            result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED)
            {
                List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);

                ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();

                for (int i = 0; i < books.size(); i++)
                {
                    if (books.get(i).getUser_id().equals(sessionManager.getUserId()))
                    {
                        listUserEntry.add(books.get(i));
                    }
                }

                if (listUserEntry.size() > 0)
                {
                    pendingEntryCount = listUserEntry.size();
                    showConfirmationDialog();
                }
                else
                {
                    AppUtils.showToast(activity, "No Pending Entry For Submission.");
                }

            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE_STORAGE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_STORAGE:
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);

                        ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
                        for (int i = 0; i < books.size(); i++) {
                            if (books.get(i).getUser_id().equals(sessionManager.getUserId())) {
                                listUserEntry.add(books.get(i));
                            }
                        }

                        if (listUserEntry.size() > 0) {
                            pendingEntryCount = listUserEntry.size();
                            showConfirmationDialog();
                        } else {
                            AppUtils.showToast(activity, "No Pending Entry For Submission.");
                        }
                    } else {
                        AppUtils.showToast(activity, "Permissions Denied!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*New*/
    AreaAdapter areaAdapter;

    public void showListDialog(final String isFor) {
        listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);

        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);
        //configureBottomSheetBehavior(sheetView);

        listDialog.findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(sheetView, activity);
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        final LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);

        final TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        if (isFor.equalsIgnoreCase(WORKWITH)) {
            tvDone.setVisibility(View.VISIBLE);
            listDialog.findViewById(R.id.ivBack).setVisibility(View.GONE);
            btnNo.setVisibility(View.GONE);
        } else {
            tvDone.setVisibility(View.GONE);
        }

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        areaAdapter = new AreaAdapter(listDialog, isFor, false, "", rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(areaAdapter);

        tvDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (isFor.equalsIgnoreCase(WORKWITH) && areaAdapter != null)
                {
                    workWithString = areaAdapter.getSelectedWorkWitIds();
                    if (workWithString.length() == 0) {
                        AppUtils.showToast(activity, "Please select at least one option.");
                    } else {
                        AppUtils.hideKeyboard(tvDone, activity);
                        edtWorkWith.setText(areaAdapter.getSelectedWorkWithName());
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

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(btnNo, activity);
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);

        edtSearchDialog.setVisibility(View.VISIBLE);

        edtSearchDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                int textlength = edtSearchDialog.getText().length();

                if (isFor.equals(AREA)) {
                    listAreaSearch.clear();
                    for (int i = 0; i < listArea.size(); i++) {
                        if (textlength <= listArea.get(i).getArea().length()) {
                            if (listArea.get(i).getArea().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listAreaSearch.add(listArea.get(i));
                            }
                        }
                    }
                } else if (isFor.equals(ADD_AREA)) {
                    listAreaSearch.clear();
                    for (int i = 0; i < listAreaForAdd.size(); i++) {
                        if (textlength <= listAreaForAdd.get(i).getArea().length()) {
                            if (listAreaForAdd.get(i).getArea().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listAreaSearch.add(listAreaForAdd.get(i));
                            }
                        }
                    }
                } else if (isFor.equals(EMPLOYEE)) {
                    listEmployeeSearch.clear();
                    for (int i = 0; i < listEmployee.size(); i++) {
                        if (textlength <= listEmployee.get(i).getName().length()) {
                            if (listEmployee.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listEmployeeSearch.add(listEmployee.get(i));
                            }
                        }
                    }
                }
                else if (isFor.equals(DOCTOR)) {
                    listDoctorSearch.clear();
                    for (int i = 0; i < listDoctor.size(); i++) {
                        if (textlength <= listDoctor.get(i).getDoctor().length()) {
                            if (listDoctor.get(i).getDoctor().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listDoctor.get(i).getSpeciality_code().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listDoctor.get(i).getDoctor_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listDoctorSearch.add(listDoctor.get(i));
                            }
                        }
                    }
                }
                else if (isFor.equals(DOCTOR_PLANNER)) {
                    listPlannedDoctorSearch.clear();
                    for (int i = 0; i < listPlannedDoctor.size(); i++) {
                        if (textlength <= listPlannedDoctor.get(i).getDoctor().length()) {
                            if (listPlannedDoctor.get(i).getDoctor().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listPlannedDoctor.get(i).getDoctor_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listPlannedDoctorSearch.add(listPlannedDoctor.get(i));
                            }
                        }
                    }
                }
                else if (isFor.equals("DBC")) {
                    listReportSearch.clear();
                    for (int i = 0; i < listReports.size(); i++) {
                        if (textlength <= listReports.get(i).getReport_name().length()) {
                            if (listReports.get(i).getReport_code().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listReports.get(i).getReport_name().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listReportSearch.add(listReports.get(i));
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
                } else if (isFor.equals(WORKWITH)) {
                    listWorkWithSearch.clear();
                    for (int i = 0; i < listWorkWith.size(); i++) {
                        if (textlength <= listWorkWith.get(i).getName().length()) {
                            if (listWorkWith.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())) {
                                listWorkWithSearch.add(listWorkWith.get(i));
                            }
                        }
                    }
                }


                AppendListForArea(listDialog, isFor, true, rvListDialog);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AppUtils.hideKeyboard(tvDone, activity);
                if (isFor.equalsIgnoreCase(WORKWITH) && areaAdapter != null)
                {
                    workWithString = areaAdapter.getSelectedWorkWitIds();
                    if (workWithString.length() == 0)
                    {
                        //AppUtils.showToast(activity, "Please select at least one option.");
                        edtWorkWith.setText("");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(tvDone, activity);
                        edtWorkWith.setText(areaAdapter.getSelectedWorkWithName());
                        listDialog.dismiss();
                        listDialog.cancel();
                    }
                }
                else
                {
                    AppUtils.hideKeyboard(tvDone, activity);
                    listDialog.dismiss();
                    listDialog.cancel();
                }
            }
        });

        listDialog.show();
    }/**/

    private void AppendListForArea(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea) {
        areaAdapter = new AreaAdapter(dialog, isFor, true, "", rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    private class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";

        AreaAdapter(Dialog dialog, String isFor, boolean isForSearch, String searchText, RecyclerView recyclerView) {
            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }

        @Override
        public AreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new AreaAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AreaAdapter.ViewHolder holder, final int position) {

            if (position == getItemCount() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            if (isFor.equalsIgnoreCase(AREA))
            {
                final AreaResponse.AreasBean getSet;

                if (isForSearch) {
                    getSet = listAreaSearch.get(position);
                } else {
                    getSet = listArea.get(position);
                }

                holder.tvValue.setAllCaps(true);
                holder.tvValue.setText(getSet.getArea());

                if (getSet.isSelected()) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_transparent));
                } else {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            edtArea.setText(getSet.getArea().toUpperCase());
                            selectedAreaId = getSet.getArea_id();

                            //For Recet whole form
                            workWithString = "";
                            NCRDrData = "";
                            txtAddCount.setText("1");

                            edtSpeciality.setText("");
                            edtInternee.setText("");
                            edtRemarks.setText("");
                            dbs = "DCR";
                            edtDBC.setText("DCR : Daily Call Report");
                            edtWorkWith.setText("");
                            edtDoctor.setText("");
                            cbNewCycle.setChecked(false);

                            inputArea.setVisibility(View.VISIBLE);
                            inputDoctor.setVisibility(View.VISIBLE);

                            if (cbWorkWith.isChecked()) {
                                inputWorkWith.setVisibility(View.GONE);
                            } else {
                                inputWorkWith.setVisibility(View.VISIBLE);
                            }

                            inputInternee.setVisibility(View.GONE);
                            edtDoctor.setText("");
                            inputDoctor.setEnabled(true);
                            inputRMK.setVisibility(View.GONE);
                            llAdvice.setVisibility(View.GONE);

                            inputSpeciality.setVisibility(View.VISIBLE);
                            llNewCycle.setVisibility(View.VISIBLE);

                            rvVariation.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.GONE);
                            llSampleDetails.setVisibility(View.VISIBLE);

                            //For get speciality
                            if (sessionManager.isNetworkAvailable()) {
                                getSpecialityFromArea(getSet.getArea_id());
                            } else {
                                getFilteredSpeciality(getSet.getArea_id());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (isFor.equalsIgnoreCase(ADD_AREA)) {
                final AreaResponse.AreasBean getSet;

                if (isForSearch) {
                    getSet = listAreaSearch.get(position);
                } else {
                    getSet = listAreaForAdd.get(position);
                }

                holder.tvValue.setAllCaps(true);
                holder.tvValue.setText(getSet.getArea());

                if (getSet.isSelected()) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_transparent));
                } else {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            edtArea.setText(getSet.getArea().toUpperCase());
                            selectedAreaId = getSet.getArea_id();

                            //For Recet whole form
                            workWithString = "";
                            NCRDrData = "";
                            txtAddCount.setText("1");

                            edtSpeciality.setText("");
                            edtInternee.setText("");
                            edtRemarks.setText("");
                            dbs = "DCR";
                            edtDBC.setText("DCR : Daily Call Report");
                            edtWorkWith.setText("");
                            edtDoctor.setText("");
                            cbNewCycle.setChecked(false);

                            inputArea.setVisibility(View.VISIBLE);
                            inputDoctor.setVisibility(View.VISIBLE);

                            if (cbWorkWith.isChecked()) {
                                inputWorkWith.setVisibility(View.GONE);
                            } else {
                                inputWorkWith.setVisibility(View.VISIBLE);
                            }

                            inputInternee.setVisibility(View.GONE);
                            edtDoctor.setText("");
                            inputDoctor.setEnabled(true);
                            inputRMK.setVisibility(View.GONE);
                            llAdvice.setVisibility(View.GONE);
                            inputSpeciality.setVisibility(View.VISIBLE);
                            llNewCycle.setVisibility(View.VISIBLE);

                            rvVariation.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.GONE);
                            llSampleDetails.setVisibility(View.VISIBLE);

                            //For get speciality
                            if (sessionManager.isNetworkAvailable()) {
                                getSpecialityFromArea(getSet.getArea_id());
                            } else {
                                getFilteredSpeciality(getSet.getArea_id());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (isFor.equalsIgnoreCase(EMPLOYEE)) {
                final StaffResponse.StaffBean getSet;

                if (isForSearch) {
                    getSet = listEmployeeSearch.get(position);
                } else {
                    getSet = listEmployee.get(position);
                }

                holder.itemView.setVisibility(View.VISIBLE);
                holder.tvValue.setAllCaps(true);

                holder.tvValue.setText(getSet.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            dialog.cancel();
                            edtEmployee.setText(getSet.getName());
                            selectedEmployeeID = getSet.getStaff_id();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            } else if (isFor.equalsIgnoreCase(SPECIALITY)) {
                final SpecialistBean.SpecialityBean getSet;
                if (isForSearch) {
                    getSet = listSpecialitySearch.get(position);
                } else {
                    getSet = listSpeciality.get(position);
                }

                holder.tvValue.setText(getSet.getSpeciality());
                holder.tvValue.setAllCaps(true);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            edtSpeciality.setText(getSet.getSpeciality().toUpperCase());
                            selectedSpecialityId = getSet.getSpeciality_id();

                            dbs = "DCR";
                            selectedReportCode = "DCR";
                            edtDBC.setText("DCR : Daily Call Report");

                            cbWorkWith.setChecked(true);
                            cbNewCycle.setChecked(false);

                            edtWorkWith.setText("");
                            workWithString = "";

                            if(listWorkWith!=null && listWorkWith.size()>0)
                            {
                                for (int i = 0; i < listWorkWith.size(); i++) {
                                    WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                                    bean.setSelected(false);
                                    listWorkWith.set(i,bean);
                                }
                            }

                            edtDoctor.setText("");
                            selectedDoctorId = "";
                            enable_focus = 0;
                            edtInternee.setText("");
                            edtRemarks.setText("");
                            cbNewCycle.setChecked(false);

                            txtAddCount.setText("1");
                            ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                            VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                            bean.setStock("");
                            bean.setItem_code("");
                            bean.setItem_id_code("");
                            bean.setProduct_id("0");
                            bean.setReason("Regular Sample");
                            bean.setReason_code("R");
                            bean.setName("Product");
                            listTemp.add(bean);
                            variationAdapter = new VariationAdapter(listTemp);
                            rvVariation.setAdapter(variationAdapter);

                            for (int i = 0; i < listVariation.size(); i++) {
                                VariationResponse.VariationsBean getSet = listVariation.get(i);
                                getSet.setStock("");
                                getSet.setChecked(false);
                                //Added for not take previous reason 7_3_19
                                getSet.setReason("");
                                getSet.setReason_code("R");
                                listVariation.set(i, getSet);
                            }


                            if (sessionManager.isNetworkAvailable())
                            {
                                getDoctorFromSpeciality(selectedAreaId, getSet.getSpeciality_id());
                            }
                            else
                            {
                                getFilteredDocotrs(getSet.getSpeciality_id());
                            }
                            clickReportType();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (isFor.equalsIgnoreCase("DBC")) {
                final ReportResponse.ReportsBean getSet;

                if (isForSearch) {
                    getSet = listReportSearch.get(position);
                } else {
                    getSet = listReports.get(position);
                }


                holder.tvValue.setText(getSet.getReport_code() + " : " + getSet.getReport_name());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try
                        {
                            AppUtils.hideKeyboard(holder.tvValue, activity);
                            dialog.dismiss();
                            //initVariationAdapter();

                            focusForString = "";

                            selectedReportCode = getSet.getReport_code();
                            edtDBC.setText(getSet.getReport_code() + " : " + getSet.getReport_name());

                            if(isPlannerClicked)
                            {
                                txtAddCount.setText("1");

                                ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                                VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                                bean.setStock("");
                                bean.setItem_code("");
                                bean.setItem_id_code("");
                                bean.setProduct_id("0");
                                bean.setReason("Regular Sample");
                                bean.setReason_code("R");
                                bean.setName("Product");
                                listTemp.add(bean);
                                variationAdapter = new VariationAdapter(listTemp);
                                rvVariation.setAdapter(variationAdapter);
                                for (int i = 0; i < listVariation.size(); i++) {
                                    VariationResponse.VariationsBean getSet = listVariation.get(i);
                                    getSet.setStock("");
                                    getSet.setChecked(false);
                                    //Added for not take previous reason 7_3_19
                                    getSet.setReason("");
                                    getSet.setReason_code("R");
                                    listVariation.set(i, getSet);
                                }
                            }
                            else
                            {
                                dbs = "";
                                cbNewCycle.setChecked(false);
                                edtWorkWith.setText("");
                                edtDoctor.setText("");
                                edtInternee.setText("");
                                edtRemarks.setText("");
                                txtAddCount.setText("1");

                                ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                                VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                                bean.setStock("");
                                bean.setItem_code("");
                                bean.setItem_id_code("");
                                bean.setProduct_id("0");
                                bean.setReason("Regular Sample");
                                bean.setReason_code("R");
                                bean.setName("Product");
                                listTemp.add(bean);
                                variationAdapter = new VariationAdapter(listTemp);
                                rvVariation.setAdapter(variationAdapter);

                                for (int i = 0; i < listVariation.size(); i++) {
                                    VariationResponse.VariationsBean getSet = listVariation.get(i);
                                    getSet.setStock("");
                                    getSet.setChecked(false);
                                    //Added for not take previous reason 7_3_19
                                    getSet.setReason("");
                                    getSet.setReason_code("R");
                                    listVariation.set(i, getSet);
                                }
                            }

                            clickOfDBC(getSet.getReport_code());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(DOCTOR))
            {
                final DoctorResponse.DoctorsBean getSet;
                if (isForSearch) {
                    getSet = listDoctorSearch.get(position);
                } else {
                    getSet = listDoctor.get(position);
                }

                holder.tvValue.setAllCaps(true);
                if(getSet.getSpeciality_code().equalsIgnoreCase(""))
                {
                    holder.tvValue.setText(getSet.getDoctor());
                }
                else
                {
                    holder.tvValue.setText(getSet.getDoctor() + " (" + getSet.getSpeciality_code() + ")");
                }
                holder.tvId.setText(getSet.getDoctor_id());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try
                        {
                            cbNewCycle.setChecked(false);

                            focusForString = "";

                            txtAddCount.setText("1");
                            ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                            VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                            bean.setStock("");
                            bean.setItem_code("");
                            bean.setItem_id_code("");
                            bean.setProduct_id("0");
                            bean.setReason("Regular Sample");
                            bean.setReason_code("R");
                            bean.setName("Product");
                            listTemp.add(bean);
                            variationAdapter = new VariationAdapter(listTemp);
                            rvVariation.setAdapter(variationAdapter);

                            for (int i = 0; i < listVariation.size(); i++) {
                                VariationResponse.VariationsBean getSet = listVariation.get(i);
                                getSet.setStock("");
                                getSet.setChecked(false);
                                //Added for not take previous reason 7_3_19
                                getSet.setReason("");
                                getSet.setReason_code("R");
                                listVariation.set(i, getSet);
                            }

                            if (isDoctorUsed(getSet.getDoctor_id(),selectedReportCode))
                            {
                                AppUtils.showToast(activity, "You already have submitted DCR entry with this doctor.");
                            }
                            else
                            {
                                dialog.dismiss();
                                edtDoctor.setText(getSet.getDoctor().toUpperCase());
                                selectedDoctorId = getSet.getDoctor_id();
                                enable_focus = getSet.getEnable_focus();
                                clickOfDBC("doctor");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(WORKWITH))
            {
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
            else if(isFor.equalsIgnoreCase(DOCTOR_PLANNER))
            {
                final DBPlanner getSet;
                if (isForSearch) {
                    getSet = listPlannedDoctorSearch.get(position);
                } else {
                    getSet = listPlannedDoctor.get(position);
                }

                holder.tvValue.setText(getSet.getDoctor()+"("+getSet.getDoctor_id()+")");
                holder.tvValue.setAllCaps(true);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        try
                        {
                            if(isDoctorUsed(getSet.getDoctor_id(),selectedReportCode))
                            {
                                AppUtils.showToast(activity, "You already have submitted DCR entry with this doctor.");
                            }
                            else
                            {
                                dialog.dismiss();

                                isPlannerClicked = true;

                                plannerDoctorClicked(getSet);

                                //edtDoctor.setText(getSet.getDoctor().toUpperCase());
                                //selectedDoctorId = getSet.getDoctor_id();
                                //clickOfDBC("doctor");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        private boolean isDoctorUsed(String drId,String reportType)
        {
            boolean b = false;

            List<NewEntryGetSet> listEntries = NewEntryGetSet.listAll(NewEntryGetSet.class);
            ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
            for (int i = 0; i < listEntries.size(); i++)
            {
                if (listEntries.get(i).getUser_id().equals(sessionManager.getUserId()))
                {
                    listUserEntry.add(listEntries.get(i));
                }
            }

            for (int i = 0; i < listUserEntry.size(); i++)
            {
                if (listUserEntry.get(i).getDr_id().equalsIgnoreCase(drId))
                {
                    b = true;
                    break;
                }
            }

            if (listSubmitedEntryOfDr != null && listSubmitedEntryOfDr.size() > 0)
            {
                for (int i = 0; i < listSubmitedEntryOfDr.size(); i++) {
                    if (listSubmitedEntryOfDr.get(i).getDrId().equals(drId))
                    {
                        b = true;
                        break;
                    }
                }
            }

            //New: Check for is JCR, XCR and ACR available : from Temp Entry
            /*if(!reportType.equalsIgnoreCase("JCR") ||
                    !reportType.equalsIgnoreCase("XCR") ||
                    !reportType.equalsIgnoreCase("ACR"))
            {
                String str = getAllReportTypeForDoctor(listUserEntry,drId);
                if(!str.equals(""))
                {
                    if(str.contains(reportType))
                    {
                        b = true;
                    }
                    else
                    {
                        //b = false;
                        if(str.contains("DCR") ||
                                str.contains("LCR") ||
                                str.contains("NCR") ||
                                str.contains("SRD") ||
                                str.contains("TNS") ||
                                str.contains("ZCR"))
                        {
                            b = true;
                        }
                        else
                        {
                            b = false;
                        }
                    }
                }
            }

            if(reportType.equalsIgnoreCase("JCR") ||
                    reportType.equalsIgnoreCase("XCR") ||
                    reportType.equalsIgnoreCase("ACR"))
            {
                b = false;
            }*/



            String str = getAllReportTypeForDoctor(listUserEntry,drId);
            if(!str.equals(""))
            {
                if(str.contains(reportType))
                {
                    b = true;
                }
                else
                {
                    b = false;
                    /*if(str.contains("DCR") ||
                            str.contains("LCR") ||
                            str.contains("NCR") ||
                            str.contains("SRD") ||
                            str.contains("TNS") ||
                            str.contains("ZCR"))
                    {
                        b = true;
                    }
                    else
                    {
                        b = false;
                    }*/
                }
            }

            return  b;
        }

        /*For get All avalable report list*/
        private String getAllReportTypeForDoctor(ArrayList<NewEntryGetSet> listUserEntry,String drId)
        {
            StringBuilder codes = new StringBuilder();
            if(listUserEntry.size()>0)
            {
                for (int i = 0; i < listUserEntry.size(); i++)
                {
                    if(listUserEntry.get(i).getDr_id().equalsIgnoreCase(drId))
                    {
                        codes.append(listUserEntry.get(i).getReport_type());
                        if ( i != listUserEntry.size()-1){
                            codes.append(",");
                        }
                    }
                }
            }

            Log.e("listSubmitted Entjry Size", "getAllReportTypeForDoctor: "+listSubmitedEntryOfDr.size() );
            if (listSubmitedEntryOfDr != null && listSubmitedEntryOfDr.size() > 0)
            {
                for (int i = 0; i < listSubmitedEntryOfDr.size(); i++) {
                    if (listSubmitedEntryOfDr.get(i).getDrId().equals(drId))
                    {
                        codes.append(listSubmitedEntryOfDr.get(i).getReportType());
                        if ( i != listSubmitedEntryOfDr.size()-1){
                            codes.append(",");
                        }
                    }
                }
            }

            return  AppUtils.removeLastComma(String.valueOf(codes));
        }

        private void plannerDoctorClicked(DBPlanner getSet)
        {
            try {
                JSONArray jsonArray = new JSONArray(getSet.getWork_with());

                Log.e("**********JsonArray>> ", "plannerDoctorClicked: "+jsonArray );

                if(listWorkWith.size()>0)
                {
                    try {
                        for (int i = 0; i < listWorkWith.size(); i++) {
                            WorkWithResponse.StaffBean bean = listWorkWith.get(i);
                            bean.setSelected(false);
                            listWorkWith.set(i,bean);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(jsonArray.length()>0)
                {
                    cbWorkWith.setChecked(false);

                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder workWithIDs = new StringBuilder();

                    try {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            if(listWorkWith.size()>0)
                            {
                                for (int j = 0; j < listWorkWith.size(); j++)
                                {
                                    JSONObject object = (JSONObject) jsonArray.get(i);
                                    if(object.getString("id").equalsIgnoreCase(listWorkWith.get(j).getStaff_id()))
                                    {
                                        WorkWithResponse.StaffBean bean = listWorkWith.get(j);
                                        bean.setSelected(true);
                                        listWorkWith.set(j,bean);

                                        if (stringBuilder.length() == 0) {
                                            stringBuilder.append(listWorkWith.get(j).getName());
                                            workWithIDs.append(listWorkWith.get(j).getStaff_id());
                                        } else {
                                            stringBuilder.append("," + listWorkWith.get(j).getName());
                                            workWithIDs.append("," + listWorkWith.get(j).getStaff_id());
                                        }

                                        workWithString = workWithIDs.toString();
                                        edtWorkWith.setText(stringBuilder.toString());
                                    }
                                }
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    cbWorkWith.setChecked(true);
                    for (int j = 0; j < listWorkWith.size(); j++)
                    {
                        try {
                            WorkWithResponse.StaffBean bean = listWorkWith.get(j);
                            bean.setSelected(false);
                            listWorkWith.set(j,bean);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            selectedAreaId = getSet.getArea_id();
            edtArea.setText(getSet.getArea());

            selectedSpecialityId = getSet.getSpeciality_id();
            edtSpeciality.setText(getSet.getSpeciality());

           /* dbs = "DCR";
            selectedReportCode = "DCR";
            edtDBC.setText("DCR : Daily Call Report");*/

            selectedDoctorId = getSet.getDoctor_id();
            enable_focus = getSet.getEnable_focus();
            edtDoctor.setText(getSet.getDoctor());

            txtAddCount.setText("1");

            ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
            VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
            bean.setStock("");
            bean.setItem_code("");
            bean.setItem_id_code("");
            bean.setProduct_id("0");
            bean.setReason("Regular Sample");
            bean.setReason_code("R");
            bean.setName("Product");
            listTemp.add(bean);
            variationAdapter = new VariationAdapter(listTemp);
            rvVariation.setAdapter(variationAdapter);

            for (int i = 0; i < listVariation.size(); i++) {
                VariationResponse.VariationsBean variationsBean = listVariation.get(i);
                variationsBean.setStock("");
                variationsBean.setChecked(false);
                //Added for not take previous reason 7_3_19
                variationsBean.setReason("");
                variationsBean.setReason_code("R");
                listVariation.set(i, variationsBean);
            }

            clickOfDBC("doctor");
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
            if (isFor.equalsIgnoreCase(AREA)) {
                if (isForSearch) {
                    return listAreaSearch.size();
                } else {
                    return listArea.size();
                }
            } else if (isFor.equalsIgnoreCase(ADD_AREA)) {
                if (isForSearch) {
                    return listAreaSearch.size();
                } else {
                    return listAreaForAdd.size();
                }
            } else if (isFor.equalsIgnoreCase(EMPLOYEE)) {
                if (isForSearch) {
                    return listEmployeeSearch.size();
                } else {
                    return listEmployee.size();
                }
            } else if (isFor.equalsIgnoreCase(SPECIALITY)) {
                if (isForSearch) {
                    return listSpecialitySearch.size();
                } else {
                    return listSpeciality.size();
                }
            } else if (isFor.equalsIgnoreCase("DBC")) {
                if (isForSearch) {
                    return listReportSearch.size();
                } else {
                    return listReports.size();
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
            }
            else if(isFor.equalsIgnoreCase(DOCTOR_PLANNER))
            {
                if (isForSearch) {
                    return listPlannedDoctorSearch.size();
                } else {
                    return listPlannedDoctor.size();
                }
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

    private void clickOfDBC(String dbsFromAdapter) {
        try
        {
            dbs = dbsFromAdapter;

            if(isPlannerClicked)
            {
                if (dbsFromAdapter.equals("DCR") ||
                        dbsFromAdapter.equals("LCR") ||
                        dbsFromAdapter.equals("ACR") ||
                        dbsFromAdapter.equals("XCR") ||
                        dbsFromAdapter.equals("JCR") ||
                        dbsFromAdapter.equals("ZCR"))
                {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    inputInternee.setVisibility(View.GONE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);

                    if(dbs.equalsIgnoreCase("ZCR") ||
                            dbs.equalsIgnoreCase("XCR"))
                    {
                        llNewCycle.setVisibility(View.GONE);
                    }
                    else
                    {
                        llNewCycle.setVisibility(View.VISIBLE);
                    }
                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                    if(!selectedSpecialityId.equals(""))
                    {
                        clickDoctor();
                    }

                } else if (dbsFromAdapter.equals("TNS") || dbs.equals("SRD")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }
                    inputInternee.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.VISIBLE);
                    inputDoctor.setEnabled(true);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                    if(!selectedSpecialityId.equals(""))
                    {
                        clickDoctor();
                    }

                } else if (dbsFromAdapter.equals("NCR")) {
                    dbs = "DCR";
                    edtDBC.setText("DCR : Daily Call Report");
                    if (sessionManager.getDayEnd().equals("false"))
                    {
                        startActivity(new Intent(activity, NCRActivity.class));
                        AppUtils.startActivityAnimation(activity);
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Your day has been ended.");
                    }
                } else if (dbsFromAdapter.equals("INT"))
                {
                    inputArea.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }
                    inputInternee.setVisibility(View.VISIBLE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);

                    inputDoctor.setEnabled(true);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                } else if (dbsFromAdapter.equals("DDT") || dbsFromAdapter.equals("RDT")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }

                    inputInternee.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                } else if (dbsFromAdapter.equals("DBS")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    llNewCycle.setVisibility(View.GONE);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);

                } else if (dbsFromAdapter.equals("RMK")) {
                    inputArea.setVisibility(View.GONE);
                    inputRMK.setVisibility(View.VISIBLE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);

                } else if (dbsFromAdapter.equals("DAYEND")) {
                    showConfirmationDialogForDayEnd();
                } else if (dbsFromAdapter.equals("ADV")) {
                    inputRMK.setVisibility(View.VISIBLE);
                    llAdvice.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);
                    inputArea.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);
                } else if (dbsFromAdapter.equals("ROR") || dbsFromAdapter.equals("ROA")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);

                    inputDoctor.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }
                    llNewCycle.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);
                }
                else if (dbsFromAdapter.equals("STK"))
                {
                    //For STK Onlye one time in a day
                    if(sessionManager.isSTKDone())
                    {
                        AppUtils.showToast(activity,"You already done STK entry.");
                        dbs = "DCR";
                        edtDBC.setText("DCR : Daily Call Report");
                        return;
                    }
                    else
                    {
                        inputArea.setVisibility(View.GONE);
                        inputRMK.setVisibility(View.GONE);
                        llAdvice.setVisibility(View.GONE);
                        inputDoctor.setVisibility(View.GONE);
                        llWorkWith.setVisibility(View.GONE);
                        if (cbWorkWith.isChecked()) {
                            inputWorkWith.setVisibility(View.GONE);
                        } else {
                            inputWorkWith.setVisibility(View.VISIBLE);
                        }
                        llNewCycle.setVisibility(View.GONE);
                        inputSpeciality.setVisibility(View.GONE);
                        inputInternee.setVisibility(View.GONE);
                        inputDoctor.setEnabled(true);

                        rvVariation.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.GONE);
                        llSampleDetails.setVisibility(View.VISIBLE);

                        listEntry = new ArrayList<AddEntryGetSet>();
                        AddEntryGetSet addEntryGetSet = new AddEntryGetSet();
                        addEntryGetSet.setProduct("");
                        addEntryGetSet.setProductCode("");
                        addEntryGetSet.setReason("Regular Sample");
                        addEntryGetSet.setReasonCode("R");
                        addEntryGetSet.setUnit("");

                        listEntry.add(addEntryGetSet);
                    }

                } else {
                    //For clicked select doctor editText

                    if(!selectedReportCode.equalsIgnoreCase("INT")&&
                            !selectedReportCode.equalsIgnoreCase("ROR")&&
                            !selectedReportCode.equalsIgnoreCase("ROA"))
                    {
                        if(enable_focus==1)
                        {
                            showDialogForFocused(selectedDoctorId);
                        }
                    }

                }
            }
            else
            {
                if (dbsFromAdapter.equals("DCR") ||
                        dbsFromAdapter.equals("LCR") ||
                        dbsFromAdapter.equals("ACR") ||
                        dbsFromAdapter.equals("XCR") ||
                        dbsFromAdapter.equals("JCR") ||
                        dbsFromAdapter.equals("ZCR"))
                {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }

                    inputInternee.setVisibility(View.GONE);
                    edtDoctor.setText("");
                    inputDoctor.setEnabled(true);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    //llNewCycle.setVisibility(View.VISIBLE);


                    if(dbs.equalsIgnoreCase("ZCR") ||
                            dbs.equalsIgnoreCase("XCR"))
                    {
                        llNewCycle.setVisibility(View.GONE);
                    }
                    else
                    {
                        llNewCycle.setVisibility(View.VISIBLE);
                    }


                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                    if(!selectedSpecialityId.equals(""))
                    {
                        clickDoctor();
                    }

                } else if (dbsFromAdapter.equals("TNS") || dbs.equals("SRD")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }

                    edtDoctor.setText("");
                    inputInternee.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.VISIBLE);
                    inputDoctor.setEnabled(true);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                    if(!selectedSpecialityId.equals(""))
                    {
                        clickDoctor();
                    }

                } else if (dbsFromAdapter.equals("NCR")) {
                    dbs = "DCR";
                    edtDBC.setText("DCR : Daily Call Report");
                    if (sessionManager.getDayEnd().equals("false"))
                    {
                        startActivity(new Intent(activity, NCRActivity.class));
                        AppUtils.startActivityAnimation(activity);
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Your day has been ended.");
                    }
                } else if (dbsFromAdapter.equals("INT")) {
                    inputArea.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }
                    inputInternee.setVisibility(View.VISIBLE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);

                    inputDoctor.setEnabled(true);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                    edtWorkWith.setText("");

                } else if (dbsFromAdapter.equals("DDT") || dbsFromAdapter.equals("RDT")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputSpeciality.setVisibility(View.VISIBLE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }

                    inputInternee.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                } else if (dbsFromAdapter.equals("DBS")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputDoctor.setVisibility(View.VISIBLE);
                    edtDoctor.setText("");
                    inputSpeciality.setVisibility(View.VISIBLE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    llNewCycle.setVisibility(View.GONE);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);

                } else if (dbsFromAdapter.equals("RMK")) {
                    inputArea.setVisibility(View.GONE);
                    inputRMK.setVisibility(View.VISIBLE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);
                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);

                } else if (dbsFromAdapter.equals("DAYEND")) {
                    showConfirmationDialogForDayEnd();
                } else if (dbsFromAdapter.equals("ADV")) {
                    inputRMK.setVisibility(View.VISIBLE);
                    llAdvice.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    inputDoctor.setVisibility(View.GONE);
                    inputWorkWith.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.GONE);
                    llNewCycle.setVisibility(View.GONE);
                    inputArea.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);
                    rvVariation.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.GONE);
                } else if (dbsFromAdapter.equals("ROR") || dbsFromAdapter.equals("ROA")) {
                    inputArea.setVisibility(View.VISIBLE);
                    inputRMK.setVisibility(View.GONE);
                    llAdvice.setVisibility(View.GONE);
                    inputSpeciality.setVisibility(View.VISIBLE);

                    inputDoctor.setVisibility(View.GONE);
                    llWorkWith.setVisibility(View.VISIBLE);
                    if (cbWorkWith.isChecked()) {
                        inputWorkWith.setVisibility(View.GONE);
                    } else {
                        inputWorkWith.setVisibility(View.VISIBLE);
                    }
                    llNewCycle.setVisibility(View.GONE);
                    inputDoctor.setEnabled(true);
                    inputInternee.setVisibility(View.GONE);

                    rvVariation.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    llSampleDetails.setVisibility(View.VISIBLE);

                } else if (dbsFromAdapter.equals("STK"))
                {

                    //For STK Onlye one time in a day
                    if(sessionManager.isSTKDone())
                    {
                        AppUtils.showToast(activity,"You already done STK entry.");
                        dbs = "DCR";
                        edtDBC.setText("DCR : Daily Call Report");
                        return;
                    }
                    else
                    {
                        inputArea.setVisibility(View.GONE);
                        inputRMK.setVisibility(View.GONE);
                        llAdvice.setVisibility(View.GONE);
                        inputDoctor.setVisibility(View.GONE);
                        llWorkWith.setVisibility(View.GONE);
                        if (cbWorkWith.isChecked()) {
                            inputWorkWith.setVisibility(View.GONE);
                        } else {
                            inputWorkWith.setVisibility(View.VISIBLE);
                        }
                        llNewCycle.setVisibility(View.GONE);
                        inputSpeciality.setVisibility(View.GONE);
                        inputInternee.setVisibility(View.GONE);
                        inputDoctor.setEnabled(true);
                        rvVariation.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.GONE);
                        llSampleDetails.setVisibility(View.VISIBLE);

                        listEntry = new ArrayList<AddEntryGetSet>();
                        AddEntryGetSet addEntryGetSet = new AddEntryGetSet();
                        addEntryGetSet.setProduct("");
                        addEntryGetSet.setProductCode("");
                        addEntryGetSet.setReason("Regular Sample");
                        addEntryGetSet.setReasonCode("R");
                        addEntryGetSet.setUnit("");

                        listEntry.add(addEntryGetSet);
                    }
                }
                else
                {
                    //For clicked select doctor editText
                    if(!selectedReportCode.equalsIgnoreCase("INT")&&
                            !selectedReportCode.equalsIgnoreCase("ROR")&&
                            !selectedReportCode.equalsIgnoreCase("ROA"))
                    {
                        if(enable_focus==1)
                        {
                            showDialogForFocused(selectedDoctorId);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class VariationAdapter extends RecyclerView.Adapter<VariationAdapter.ViewHolder>
    {
        ArrayList<VariationResponse.VariationsBean> listItems;

        VariationAdapter(ArrayList<VariationResponse.VariationsBean> list)
        {
            this.listItems = list;
        }

        @Override
        public VariationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_entry_new, viewGroup, false);

            return new VariationAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final VariationAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final VariationResponse.VariationsBean getSet = listItems.get(position);

                if (getSet.getName().equalsIgnoreCase("product")) {
                    holder.edtProduct.setText(getSet.getName());
                } else {
                    holder.edtProduct.setText(getSet.getItem_id_code() + " : " + getSet.getName());
                }

                if (getSet.getReason_code().equalsIgnoreCase("F"))//If reason f than set qty zero and disable qty edit
                {
                    holder.edtUnit.setText("0");
                    holder.edtUnit.setEnabled(false);
                }
                else
                {
                    holder.edtUnit.setEnabled(true);
                }

                if (getSet.getStock().equals("") || getSet.getStock().equals("0"))
                {
                    holder.edtUnit.setText("0");
                    getSet.setReason("Refuse Sample");
                    getSet.setReason_code("F");
                    getSet.setReason_id(ApiClient.REFUSE_REASON_ID);
                    holder.edtReason.setText("F");
                }
                else
                {
                    holder.edtUnit.setText(String.valueOf(getSet.getStock()));
                    holder.edtReason.setText(getSet.getReason_code());
                }

                holder.edtUnit.setSelection(holder.edtUnit.getText().length());

                if (dbs.equalsIgnoreCase("STK"))
                {
                    holder.edtReason.setVisibility(View.GONE);
                    //holder.edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
                } else {
                    holder.edtReason.setVisibility(View.VISIBLE);
                    //holder.edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(2)});
                }

                if (getSet.getProduct_type().equals("1"))
                {
                    if (getSet.getStock().equals("") || getSet.getStock().equals("0"))
                    {
                        holder.edtUnit.setText("0");
                        getSet.setReason("Refuse Sample");
                        getSet.setReason_code("F");
                        getSet.setReason_id(ApiClient.REFUSE_REASON_ID);
                        holder.edtReason.setText("F");

                    }
                    else
                    {
                        holder.edtReason.setText("G");
                        getSet.setReason_code("G");
                        getSet.setReason_id(getIdFromReasonCode("G"));
                    }
                }
                else
                {
                    holder.edtReason.setText(getSet.getReason_code());
                }

                holder.edtProduct.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        if (listVariation.size() > 1)
                        {
                            if(selectedReportCode.equalsIgnoreCase("DCR") ||
                                    selectedReportCode.equalsIgnoreCase("ACR") ||
                                    selectedReportCode.equalsIgnoreCase("JCR") ||
                                    selectedReportCode.equalsIgnoreCase("LCR") ||
                                    selectedReportCode.equalsIgnoreCase("NCR") ||
                                    selectedReportCode.equalsIgnoreCase("SRD") ||
                                    selectedReportCode.equalsIgnoreCase("TNS") ||
                                    selectedReportCode.equalsIgnoreCase("XCR") ||
                                    selectedReportCode.equalsIgnoreCase("ZCR") ||
                                    selectedReportCode.equalsIgnoreCase("STK"))
                            {

                                if(isNCREntry)
                                {
                                    showDialog("Product", 0);
                                }
                                else
                                {
                                    if(selectedReportCode.equalsIgnoreCase("STK"))
                                    {
                                        showDialog("Product", 0);
                                    }
                                    else
                                    {
                                        if(selectedDoctorId.equalsIgnoreCase(""))
                                        {
                                            AppUtils.showToast(activity,"Please select doctor first.");
                                        }
                                        else
                                        {
                                            showDialog("Product", 0);
                                        }
                                    }
                                }
                                /*if(selectedDoctorId.equalsIgnoreCase(""))
                                {
                                    AppUtils.showToast(activity,"Please select doctor first.");
                                }
                                else
                                {
                                    showDialog("Product", 0);
                                }*/
                            }
                            else if(selectedReportCode.equalsIgnoreCase("INT") ||
                                    selectedReportCode.equalsIgnoreCase("ROA") ||
                                    selectedReportCode.equalsIgnoreCase("ROR"))
                            {
                                showDialog("Product", 0);
                            }
                            //showDialog("Product", 0);
                        }
                        else
                        {
                            AppUtils.showToast(activity, "No Products Found.");
                        }
                        //JAY
                        Log.e("<><>REASON",getSet.getReason_code());
                    }
                });

                holder.edtUnit.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().equals(""))
                        {
                            /*//Original Code
                            VariationResponse.VariationsBean bean = listItems.get(position);
                            bean.setStock(s.toString());
                            listVariation.set(position, bean);*/

                            //Changed by kiran, Change given by Riteshbhai
                            VariationResponse.VariationsBean bean = listItems.get(position);
                            bean.setStock(s.toString());
                            if(Integer.parseInt(s.toString())==0)
                            {
                                Log.e("****>>>", "onBindViewHolder:                3  "  );

                                bean.setReason("Refuse Sample");
                                bean.setReason_code("F");
                                bean.setReason_id(ApiClient.REFUSE_REASON_ID);
                                holder.edtReason.setText("F");
                            }
                            else
                            {

                                Log.e("****>>>", "onBindViewHolder:                4  "  );

                                if(bean.getProduct_type().equalsIgnoreCase("0"))
                                {
                                    if(!bean.getReason_code().equalsIgnoreCase("F"))
                                    {
                                        bean.setReason(bean.getReason());
                                        bean.setReason_code(bean.getReason_code());
                                        holder.edtReason.setText(bean.getReason_code());
                                    }
                                    else
                                    {
                                        bean.setReason("Regular Sample");
                                        bean.setReason_code("R");
                                        holder.edtReason.setText("R");
                                    }
                                }
                                else
                                {
                                    bean.setReason("Gift Article");
                                    bean.setReason_code("G");
                                    holder.edtReason.setText("G");
                                }
                            }
                            listVariation.set(position, bean);
                        }
                        else
                        {
                            VariationResponse.VariationsBean bean = listItems.get(position);
                            bean.setStock("0");
                            bean.setReason("Refuse Sample");
                            bean.setReason_code("F");
                            bean.setReason_id(ApiClient.REFUSE_REASON_ID);
                            holder.edtReason.setText("F");
                            listVariation.set(position, bean);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.edtReason.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        if (holder.edtProduct.getText().toString().equals("") || holder.edtProduct.getText().toString().equals("Product")) {
                            AppUtils.showToast(activity, "Please select product!");
                        }
                        else {
                            if (listReports.size() > 0) {
                                showDialog("Reason", position);
                            } else {
                                AppUtils.showToast(activity, "No Sample Reasons Found.");
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getIdFromReasonCode(String reasonCode) {
            String id = "";
            for (int i = 0; i < listReason.size(); i++) {
                if (listReason.get(i).getReason_code().equalsIgnoreCase(reasonCode)) {
                    id = listReason.get(i).getReason_id();
                    break;
                }
            }
            return id;
        }


        public boolean isListFilled()
        {
            boolean isFilled = true;
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getName().equalsIgnoreCase("Product") ||
                        listItems.get(i).getName().equals("")) {
                    isFilled = false;
                }
            }
            return isFilled;
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private EditText edtProduct, edtUnit, edtReason;

            ViewHolder(View convertView) {
                super(convertView);
                edtProduct = (EditText) convertView.findViewById(R.id.edtProduct);
                edtUnit = (EditText) convertView.findViewById(R.id.edtUnit);
                edtReason = (EditText) convertView.findViewById(R.id.edtReason);
            }
        }
    }

    @SuppressWarnings("unused")
    private class ViewHolder {
        TextView txtProduct, txtReason;
        EditText edtUnit;
        LinearLayout llProduct, llReason;
        ImageView ivMinus;
        int ref;
    }

    protected void showDialog(final String clickFor, final int mainListPos) {
        try {
            final Dialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
            //final Dialog dialog = new Dialog(activity);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_sample, null);

            /*dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);*/

            dialog.setContentView(sheetView);

            dialog.findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView, activity);
                    if (productUnitAdapter.listSelected().size() > 0)
                    {
                        if (productUnitAdapter.isAllSelected())
                        {
                            dialog.dismiss();
                            if (listSelectedProducts == null) {
                                listSelectedProducts = new ArrayList<>();
                            }

                            listSelectedProducts.clear();

                            for (int i = 0; i < productUnitAdapter.listSelected().size(); i++)
                            {
                                listSelectedProducts.add(productUnitAdapter.listSelected().get(i));
                            }

                            txtAddCount.setText(String.valueOf(listSelectedProducts.size()));

                            variationAdapter = new VariationAdapter(listSelectedProducts);
                            rvVariation.setAdapter(variationAdapter);
                        }
                        else {
                            AppUtils.showToast(activity, "Please enter unit for all samples.");
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Please Select atleast One sample.");

                        txtAddCount.setText("1");
                        ArrayList<VariationResponse.VariationsBean> listTemp = new ArrayList<>();
                        VariationResponse.VariationsBean bean = new VariationResponse.VariationsBean();
                        bean.setStock("");
                        bean.setItem_code("");
                        bean.setItem_id_code("");
                        bean.setProduct_id("0");
                        bean.setReason("Regular Sample");
                        bean.setReason_code("R");
                        bean.setName("Product");
                        listTemp.add(bean);
                        variationAdapter = new VariationAdapter(listTemp);
                        rvVariation.setAdapter(variationAdapter);

                        for (int i = 0; i < listVariation.size(); i++) {
                            VariationResponse.VariationsBean bean1 = listVariation.get(i);
                            bean1.setStock("");
                            bean1.setChecked(false);
                            //Added for not take previous reason 7_3_19
                            bean1.setReason("");
                            bean1.setReason_code("R");
                            listVariation.set(i, bean1);
                        }
                    }
                }
            });
            dialog.setCanceledOnTouchOutside(false);

            LinearLayout llMainListLinear = (LinearLayout) dialog.findViewById(R.id.llMainListLinear);
            llMainListLinear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch_Dialog_ListView);
            TextInputLayout inputSearch = (TextInputLayout) dialog.findViewById(R.id.inputSearch);

            final BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.lv_Dialog);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader_Dialog_ListView);
            final TextView btnSubmit = (TextView) dialog.findViewById(R.id.txtSubmitDialog);

            if (clickFor.equals("Product"))
            {
                txtHeader.setText("Select Product");
                dialog.findViewById(R.id.ivBack).setVisibility(View.GONE);

                productUnitAdapter = new ProductUnitAdapter(listVariation, listReason, dialog, "Product", mainListPos, false,listView);
                listView.setAdapter(productUnitAdapter);
                btnSubmit.setVisibility(View.VISIBLE);
                //setListViewHeightBasedOnChildren(listView);
                //setListHeight(listView,productUnitAdapter,listVariation);

                inputSearch.setVisibility(View.VISIBLE);

                edtSearch.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        int textlength = edtSearch.getText().length();
                        listVariationSearch.clear();
                        for (int i = 0; i < listVariation.size(); i++) {
                            if (listVariation.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listVariation.get(i).getItem_id_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listVariation.get(i).getItem_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listVariationSearch.add(listVariation.get(i));
                            }
                        }
                        productUnitAdapter = new ProductUnitAdapter(listVariation, listReason, dialog, "Product", mainListPos, true,listView);
                        listView.setAdapter(productUnitAdapter);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }
                });
            } else if (clickFor.equals("Reason")) {
                dialog.findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
                inputSearch.setVisibility(View.GONE);
                txtHeader.setText("Select Reason");
                productUnitAdapter = new ProductUnitAdapter(listVariation, listReason, dialog, "Reason", mainListPos, false,listView);
                listView.setAdapter(productUnitAdapter);
                btnSubmit.setVisibility(View.INVISIBLE);
            }


            btnSubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (productUnitAdapter.listSelected().size() > 0)
                    {
                        if (productUnitAdapter.isAllSelected())
                        {
                            AppUtils.hideKeyboard(edtSearch, activity);
                            dialog.dismiss();
                            if (listSelectedProducts == null) {
                                listSelectedProducts = new ArrayList<>();
                            }

                            listSelectedProducts.clear();

                            for (int i = 0; i < productUnitAdapter.listSelected().size(); i++)
                            {
                                listSelectedProducts.add(productUnitAdapter.listSelected().get(i));
                            }

                            txtAddCount.setText(String.valueOf(listSelectedProducts.size()));

                            variationAdapter = new VariationAdapter(listSelectedProducts);
                            rvVariation.setAdapter(variationAdapter);
                        }
                        else {
                            AppUtils.showToast(activity, "Please enter unit for all samples.");
                        }
                    } else {
                        AppUtils.showToast(activity, "Please Select atleast One sample.");
                    }
                }
            });


            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListHeight(ListView listView, BaseAdapter adapter, ArrayList<VariationResponse.VariationsBean> list) {
        try {
            if (adapter == null) {
                return;
            }

            int totalHeight = 0;

            int TempCount = 0;

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProduct_type().equals("1")) {
                    TempCount = TempCount + 1;
                }
            }

            for (int i = 0; i < adapter.getCount(); ++i) {
                View listItem = adapter.getView(i, (View) null, listView);
                listItem.measure(0, 0);
                if (list.get(i).getProduct_type().equals("1")) {
                    totalHeight += listItem.getMeasuredHeight();
                }
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public class ProductUnitAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private ArrayList<VariationResponse.VariationsBean> listProducAdapter;
        private ArrayList<ReasonResponse.ReasonsBean> listReasonAdapter;
        private String isFor = "";
        Dialog dialog;
        private int mainListPos = 0;
        private boolean isForSearch = false;
        private BottomSheetListView bottomSheetListView;

        public ProductUnitAdapter(ArrayList<VariationResponse.VariationsBean> productList,
                                  ArrayList<ReasonResponse.ReasonsBean> reasonList,
                                  Dialog dialog,
                                  String isFor,
                                  int mainListPos,
                                  boolean isForSearch,
                                  BottomSheetListView bottomSheetListView) {
            //this.listProducAdapter = productList;
            this.listReasonAdapter = reasonList;
            this.dialog = dialog;
            this.isFor = isFor;
            this.mainListPos = mainListPos;
            this.isForSearch = isForSearch;
            this.bottomSheetListView = bottomSheetListView;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (selectedReportCode.equalsIgnoreCase("STK"))
            {
                this.listProducAdapter = productList;

                if (isFor.equalsIgnoreCase("Product")) {
                    for (int i = 0; i < listProducAdapter.size(); i++) {
                        VariationResponse.VariationsBean getSet = listProducAdapter.get(i);
                        getSet.setChecked(true);
                        listProducAdapter.set(i, getSet);
                    }
                }

            }
            else if (selectedReportCode.equalsIgnoreCase("TNS") || selectedReportCode.equalsIgnoreCase("SRD"))
            {
                this.listProducAdapter = new ArrayList<>();
                for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getProduct_type().equalsIgnoreCase("1")) {
                        this.listProducAdapter.add(productList.get(i));
                    }
                }
            }
            else
            {
                this.listProducAdapter = productList;
            }


            //For disaply only G and F if product reason is Gift
            if (isFor.equalsIgnoreCase("Reason"))
            {
                if (listSelectedProducts.get(mainListPos).getProduct_type().equalsIgnoreCase("1"))
                {
                    listReasonAdapter = DataUtils.getReasonsFroGift();
                }
                else
                {
                    listReasonAdapter = DataUtils.getReasonsFroNonGift();
                }
            }

        }

        public int getCount()
        {
            if (isFor.equalsIgnoreCase("Product")) {
                if (isForSearch) {
                    return listVariationSearch.size();
                } else {
                    return listProducAdapter.size();
                }

            } else {
                return listReasonAdapter.size();
            }
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, final ViewGroup parent)
        {
            final ProductUnitAdapter.ViewHolder holder;

            if (convertView == null) {
                holder = new ProductUnitAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.rowview_product_unit, null);
                holder.txtProduct = (TextView) convertView.findViewById(R.id.txtProduct);
                holder.txtProductCode = (TextView) convertView.findViewById(R.id.txtProductCode);
                holder.cbProduct = (CheckBox) convertView.findViewById(R.id.cb);
                holder.edtUnit = (EditText) convertView.findViewById(R.id.edtUnit);
                //holder.edtUnit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                convertView.setTag(holder);
            } else {
                holder = (ProductUnitAdapter.ViewHolder) convertView.getTag();
            }

            if (isFor.equalsIgnoreCase("Product"))
            {
                holder.pos = position;

                final VariationResponse.VariationsBean getSet;

                if (isForSearch) {
                    getSet = listVariationSearch.get(position);
                } else {
                    getSet = listProducAdapter.get(position);
                }

                if (getSet.getName().equalsIgnoreCase("product")) {
                    convertView.setVisibility(View.GONE);
                } else {
                    convertView.setVisibility(View.VISIBLE);
                }

                if (dbs.equalsIgnoreCase("STK")) {
                    holder.cbProduct.setChecked(true);
                    //ravi
                    // holder.edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
                    getSet.setChecked(true);
                } else {
                    //ravi
                    //  holder.edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(2)});
                    holder.cbProduct.setChecked(getSet.isChecked());
                }

                if (getSet.isChecked())
                {
                    holder.cbProduct.setChecked(true);
                    holder.edtUnit.setVisibility(View.VISIBLE);
                    if (getSet.getStock().equals("") || getSet.getStock().equals("0"))
                    {
                        if(getSet.getReason_code().equalsIgnoreCase("F"))
                        {
                            holder.edtUnit.setText("0");
                            holder.edtUnit.setSelection(holder.edtUnit.getText().length());
                        }
                        else
                        {
                            holder.edtUnit.setText("");
                        }


                    } else {
                        holder.edtUnit.setText(String.valueOf(getSet.getStock()));
                        holder.edtUnit.setSelection(holder.edtUnit.getText().length());
                    }

                } else {

                    holder.edtUnit.setVisibility(View.INVISIBLE);
                    holder.cbProduct.setChecked(false);
                }

                holder.cbProduct.setVisibility(View.VISIBLE);
                holder.txtProductCode.setText(getSet.getItem_id_code());
                holder.txtProduct.setText(getSet.getName());

                /*important*/
				/*if(dbs.equalsIgnoreCase("TNS") || dbs.equalsIgnoreCase("SRD") )
				{
					if(getSet.getProduct_type().equals("1"))
					{
						convertView.setVisibility(View.VISIBLE);
					}
					else
					{
						convertView.setVisibility(View.GONE);
					}
				}*/

                holder.edtUnit.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        try
                        {
                            if (isForSearch)
                            {
                                if (!s.toString().equals("")) {
                                    if (getSet.isChecked()) {
                                        listVariationSearch.get(holder.pos).setStock(s.toString());
                                    } else {
                                        listVariationSearch.get(holder.pos).setStock("");
                                    }

                                    listVariationSearch.set(holder.pos, listVariationSearch.get(holder.pos));

                                }
                            }
                            else
                            {
                                if (!s.toString().equals(""))
                                {
                                    /*if (getSet.isChecked())
                                    {
                                        listProducAdapter.get(holder.pos).setStock(s.toString());
                                    }
                                    else
                                    {
                                        listProducAdapter.get(holder.pos).setStock("");
                                    }

                                    listProducAdapter.set(holder.pos, listProducAdapter.get(holder.pos));*/

                                    //Changed on 12th jan 2018 evening
                                    VariationResponse.VariationsBean variationsBean =  listProducAdapter.get(holder.pos);
                                    variationsBean.setStock(s.toString());
                                    if(Integer.parseInt(s.toString())==0)
                                    {
                                        variationsBean.setReason("Refuse Sample");
                                        variationsBean.setReason_code("F");
                                        variationsBean.setReason_id(ApiClient.REFUSE_REASON_ID);
                                    }
                                    else
                                    {

                                        if(variationsBean.getProduct_type().equalsIgnoreCase("0"))
                                        {
                                            if(!variationsBean.getReason_code().equalsIgnoreCase("F"))
                                            {
                                                variationsBean.setReason(variationsBean.getReason());
                                                variationsBean.setReason_code(variationsBean.getReason_code());
                                            }
                                            else
                                            {
                                                variationsBean.setReason("Regular Sample");
                                                variationsBean.setReason_code("R");
                                            }
                                        }
                                        else
                                        {
                                            variationsBean.setReason("Gift Article");
                                            variationsBean.setReason_code("G");
                                        }
                                    }
                                    listProducAdapter.set(holder.pos,variationsBean);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                /* holder.edtUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            //ORIGINAL CODE
                            //holder.edtUnit.setFocusableInTouchMode(false);
                            //AppUtils.hideKeyboard(holder.edtUnit,activity);

                            //JAY CHANGED THIS FOR TESTInG
                            holder.edtUnit.setFocusableInTouchMode(true);

                        }
                    }
                });*/

               /* holder.cbProduct.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        AppUtils.hideKeyboard(holder.edtUnit,activity);
                      */
                /*  holder.edtUnit.setFocusableInTouchMode(true);
                        holder.edtUnit.post(() -> {
                            holder.edtUnit.requestFocus();
                            AppUtils.showKeyboard(holder.edtUnit,activity);
                        });*//*

                        if (dbs.equalsIgnoreCase("STK"))
                        {
                            holder.cbProduct.setChecked(true);
                            return;
                        }
                        else
                        {
                            getSet.setChecked(!getSet.isChecked());
                            if (getSet.isChecked())
                            {
                                holder.edtUnit.setVisibility(View.VISIBLE);
                                AppUtils.showKeyboard(holder.edtUnit,activity);
                            }
                            else
                            {
                                AppUtils.hideKeyboard(holder.edtUnit,activity);
                                getSet.setStock("");
                                holder.edtUnit.setVisibility(View.INVISIBLE);
                            }
                            if (isForSearch) {
                                listVariationSearch.set(position, getSet);
                            } else {
                                listProducAdapter.set(position, getSet);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.txtProduct.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.hideKeyboard(holder.edtUnit,activity);
                      *//*  holder.edtUnit.setFocusableInTouchMode(true);
                        holder.edtUnit.post(() -> {
                            holder.edtUnit.requestFocus();
                            AppUtils.showKeyboard(holder.edtUnit,activity);
                        });*//*

                        if (dbs.equalsIgnoreCase("STK"))
                        {
                            holder.cbProduct.setChecked(true);
                            return;
                        }
                        else
                        {
                            getSet.setChecked(!getSet.isChecked());
                            if (getSet.isChecked())
                            {
                                holder.edtUnit.setVisibility(View.VISIBLE);
                                AppUtils.showKeyboard(holder.edtUnit,activity);
                            }
                            else
                            {
                                AppUtils.hideKeyboard(holder.edtUnit,activity);
                                getSet.setStock("");
                                holder.edtUnit.setVisibility(View.INVISIBLE);
                            }
                            if (isForSearch) {
                                listVariationSearch.set(position, getSet);
                            } else {
                                listProducAdapter.set(position, getSet);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.txtProductCode.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.hideKeyboard(holder.edtUnit,activity);
                      *//*  holder.edtUnit.setFocusableInTouchMode(true);
                        holder.edtUnit.post(() -> {
                            holder.edtUnit.requestFocus();
                            AppUtils.showKeyboard(holder.edtUnit,activity);
                        });*//*

                        if (dbs.equalsIgnoreCase("STK"))
                        {
                            holder.cbProduct.setChecked(true);
                            return;
                        }
                        else
                        {
                            getSet.setChecked(!getSet.isChecked());
                            if (getSet.isChecked())
                            {
                                holder.edtUnit.setVisibility(View.VISIBLE);
                                AppUtils.showKeyboard(holder.edtUnit,activity);
                            }
                            else
                            {
                                AppUtils.hideKeyboard(holder.edtUnit,activity);
                                getSet.setStock("");
                                holder.edtUnit.setVisibility(View.INVISIBLE);
                            }
                            if (isForSearch) {
                                listVariationSearch.set(position, getSet);
                            } else {
                                listProducAdapter.set(position, getSet);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });*/

                convertView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AppUtils.hideKeyboard(holder.edtUnit,activity);
                      /*  holder.edtUnit.setFocusableInTouchMode(true);
                        holder.edtUnit.post(() -> {
                            holder.edtUnit.requestFocus();
                            AppUtils.showKeyboard(holder.edtUnit,activity);
                        });*/

                        if (dbs.equalsIgnoreCase("STK"))
                        {
                            holder.cbProduct.setChecked(true);
                            return;
                        }
                        else
                        {
                            getSet.setChecked(!getSet.isChecked());
                            if (getSet.isChecked())
                            {
                                holder.edtUnit.setVisibility(View.VISIBLE);
                                //JAY
                                //AppUtils.showKeyboard(holder.edtUnit,activity);
                            }
                            else
                            {
                                AppUtils.hideKeyboard(holder.edtUnit,activity);
                                getSet.setStock("");
                                holder.edtUnit.setVisibility(View.INVISIBLE);
                            }
                            if (isForSearch) {
                                listVariationSearch.set(position, getSet);
                            } else {
                                listProducAdapter.set(position, getSet);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

            }
            else
            {
                holder.cbProduct.setVisibility(View.GONE);
                final ReasonResponse.ReasonsBean getSet = listReasonAdapter.get(position);
                holder.cbProduct.setVisibility(View.GONE);
                holder.txtProductCode.setText(getSet.getReason_code());
                holder.txtProduct.setText(getSet.getReason());

                convertView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        isListReasonItemClicked = true;
                        dialog.dismiss();

                        for (int i = 0; i < listSelectedProducts.size(); i++)
                        {
                            if (i == mainListPos)
                            {
                                VariationResponse.VariationsBean bean = listSelectedProducts.get(i);
                                bean.setReason(getSet.getReason());
                                bean.setReason_id(getSet.getReason_id());
                                bean.setReason_code(getSet.getReason_code());

                                if (getSet.getReason_code().equalsIgnoreCase("F"))
                                {
                                    bean.setStock("0");
                                }
                                else
                                {
                                    if(bean.getStock().equalsIgnoreCase("0") ||
                                            bean.getStock().equalsIgnoreCase(""))
                                    {
                                        bean.setStock("1");
                                    }
                                    else
                                    {
                                        bean.setStock(bean.getStock());
                                    }

                                }
                                listSelectedProducts.set(mainListPos, bean);
                            }
                        }

                        variationAdapter = new VariationAdapter(listSelectedProducts);
                        rvVariation.setAdapter(variationAdapter);
                    }
                });

            }

            return convertView;
        }

        private void updateItemAtPosition(final int position, BottomSheetListView mListView, final VariationResponse.VariationsBean bean)
        {
            View v = mListView.getChildAt(position);

            if(v == null)
                return;

            CheckBox cbProduct = (CheckBox) v.findViewById(R.id.cb);
            EditText edtUnit = (EditText) v.findViewById(R.id.edtUnit);


            if (bean.isChecked())
            {
                cbProduct.setChecked(true);
            }
            else
            {
                cbProduct.setChecked(false);
            }
            if (dbs.equalsIgnoreCase("STK")) {
                cbProduct.setChecked(true);
                //ravi
               // edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
                bean.setChecked(true);
            } else {
                //ravi
              //  edtUnit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(2)});
                cbProduct.setChecked(bean.isChecked());
            }

            if (bean.isChecked())
            {
                edtUnit.setVisibility(View.VISIBLE);
                if (bean.getStock().equals("") || bean.getStock().equals("0"))
                {
                    if(bean.getReason_code().equalsIgnoreCase("F"))
                    {
                        edtUnit.setText("0");
                    }
                    else
                    {
                        edtUnit.setText("");
                    }


                } else {
                    edtUnit.setText(String.valueOf(bean.getStock()));
                }

            } else {
                edtUnit.setVisibility(View.INVISIBLE);
            }
        }

        public ArrayList<VariationResponse.VariationsBean> listSelected()
        {
            ArrayList<VariationResponse.VariationsBean> listReturn = new ArrayList<VariationResponse.VariationsBean>();

            for (int i = 0; i < listProducAdapter.size(); i++)
            {
                if (listProducAdapter.get(i).isChecked())
                {
                    listReturn.add(listProducAdapter.get(i));
                }
            }

            return listReturn;
        }

        public boolean isAllSelected()
        {
            boolean isSelected = true;

            if (dbs.equalsIgnoreCase("STK")) {
                isSelected = true;
                return true;
            }
            else
            {
                for (int i = 0; i < listProducAdapter.size(); i++)
                {
                    if (listProducAdapter.get(i).isChecked()) {
						/*if (listProducAdapter.get(i).getStock().equals("") ||
								listProducAdapter.get(i).getStock().equals("0"))
						{
							isSelected = false;
							break;
						}*/
                        if (listProducAdapter.get(i).getReason_code().equalsIgnoreCase("F")) {
                            isSelected = true;
                        }
                        else
                        {
                            //For allow zero
                            if (listProducAdapter.get(i).getStock().equals("") /*||
                                    listProducAdapter.get(i).getStock().equals("0")*/) {
                                isSelected = false;
                                break;
                            }
                        }
                    } else {
                        isSelected = true;
                    }
                }

            }

            return isSelected;
        }

        private class ViewHolder {
            TextView txtProduct, txtProductCode;
            EditText edtUnit;
            int pos = 0;
            CheckBox cbProduct;
        }
    }

    private String createJsonStringFromPendingEntryList(ArrayList<NewEntryGetSet> list) {
        String jsonString = "";
        JSONArray mainArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("area_id", list.get(i).getArea_id());
                jsonObject.put("speciality_id", list.get(i).getSpeciality_id());
                jsonObject.put("report_type", list.get(i).getReport_type());

                if (list.get(i).getReport_type().equals("NCR")) {
                    jsonObject.put("doctor", list.get(i).getNCRDrData());
                }

                if (list.get(i).isWorkWithSelf()) {
                    jsonObject.put("work_with_self", true);
                } else {
                    jsonObject.put("work_with_self", false);

                    List<String> workWithIds = Arrays.asList(list.get(i).getWork_with_id().split(","));
                    JSONArray workArray = new JSONArray();
                    for (int j = 0; j < workWithIds.size(); j++) {
                        workArray.put(Integer.parseInt(workWithIds.get(j)));
                    }

                    jsonObject.put("work_with", workArray);

                }
                jsonObject.put("visit_time", list.get(i).getDdtdate());
                jsonObject.put("doctor_id", list.get(i).getDr_id());
                jsonObject.put("remark", list.get(i).getRemark());

                jsonObject.put("adv_employee", list.get(i).getEmpId());
                jsonObject.put("advDate", list.get(i).getAdvDate());
                jsonObject.put("advice", list.get(i).getAdvice());

                jsonObject.put("no_of_internee", list.get(i).getInternee());
                jsonObject.put("is_new_cycle", list.get(i).getNew_cycle());
                jsonObject.put("staff_id", sessionManager.getUserId());

                ArrayList<VariationResponse.VariationsBean> newList = new ArrayList<>();
                List<DBVariation> mainProductList = DBVariation.listAll(DBVariation.class);
                ArrayList<VariationResponse.VariationsBean> listProducts = AppUtils.getArrayListFromJsonStringVariation(list.get(i).getProducts());

                for (int j = 0; j < mainProductList.size(); j++) {
                    VariationResponse.VariationsBean variationsBean = new VariationResponse.VariationsBean();
                    variationsBean.setProduct_id(mainProductList.get(j).getProduct_id());
                    variationsBean.setVariation_id(mainProductList.get(j).getVariation_id());
                    variationsBean.setName(mainProductList.get(j).getName());
                    variationsBean.setItem_code(mainProductList.get(j).getItem_code());
                    variationsBean.setItem_id_code(mainProductList.get(j).getItem_id_code());
                    variationsBean.setReason(mainProductList.get(j).getReason());
                    variationsBean.setReason_code(mainProductList.get(j).getReason_code());
                    variationsBean.setChecked(mainProductList.get(j).isChecked());
                    variationsBean.setStock(String.valueOf(mainProductList.get(j).getStock()));
                    variationsBean.setReason_id(mainProductList.get(j).getReason_id());
                    for (int k = 0; k < listProducts.size(); k++) {
                        if (listProducts.get(k).getVariation_id().equalsIgnoreCase(mainProductList.get(j).getVariation_id())) {
                            variationsBean.setStock(listProducts.get(k).getStock());
                            variationsBean.setReason_id(listProducts.get(k).getReason_id());
                            variationsBean.setReason(listProducts.get(k).getReason());
                            variationsBean.setReason_code(listProducts.get(k).getReason_code());
                        } else {
                            continue;
                        }
                    }
                    newList.add(variationsBean);

                }

                JSONArray productArray = new JSONArray();
                JSONArray reasonArray = new JSONArray();
                for (int j = 0; j < newList.size(); j++) {
                    JSONObject proObject = new JSONObject();
                    JSONObject reasonObject = new JSONObject();
                    proObject.put(newList.get(j).getVariation_id(), newList.get(j).getStock());
                    reasonObject.put(newList.get(j).getVariation_id(), newList.get(j).getReason_id());
                    productArray.put(proObject);
                    reasonArray.put(reasonObject);
                }

                jsonObject.put("products", productArray);
                jsonObject.put("reasons", reasonArray);


                if (list.get(i).getFocusProducts().length() > 0) {
                    List<String> items = new ArrayList<>();
                    items = Arrays.asList(list.get(i).getFocusProducts().split(","));
                    String[] tempAry = list.get(i).getFocusProducts().split(",");

                    if (tempAry != null && tempAry.length > 0) {
                        items = new ArrayList<>();

                        for (int l = 0; l < tempAry.length; l++) {
                            String[] temp = tempAry[l].split("#");
                            items.add(temp[1]);
                        }
                    }

                    //getView: ALL---New
                    /*For Get Id From VariationCode*/
                    ArrayList<String> listWithVariationCode = new ArrayList<>();//List Of Code
                    ArrayList<String> listTypes = new ArrayList<>();//List of type= New and Enhance
                    if (items.size() > 0) {
                        for (int j = 0; j < items.size(); j++) {
                            String[] prAry = items.get(j).split("---");
                            listWithVariationCode.add(prAry[0]);
                            listTypes.add(prAry[1]);
                        }
                    }

                    for (int k = 0; k < listWithVariationCode.size(); k++) {
                        String[] arrColon = listWithVariationCode.get(k).split(":");
                        listWithVariationCode.set(k, arrColon[0].toString().trim());
                    }

                    try {
                        for (int l = 0; l < newList.size(); l++) {
                            for (int j = 0; j < listWithVariationCode.size(); j++) {
                                if (newList.get(l).getItem_id_code().equalsIgnoreCase(listWithVariationCode.get(j))) {
                                    listWithVariationCode.remove(j);
                                    listWithVariationCode.add(newList.get(l).getVariation_id());
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (listWithVariationCode.size() == 1) {
                        jsonObject.put("focusForProduct1", listWithVariationCode.get(0));
                        jsonObject.put("focusForItemType1", listTypes.get(0));

                        jsonObject.put("focusForProduct2", "");
                        jsonObject.put("focusForItemType2", "");
                    } else if (listWithVariationCode.size() == 2) {
                        jsonObject.put("focusForProduct1", listWithVariationCode.get(0));
                        jsonObject.put("focusForItemType1", listTypes.get(0));

                        jsonObject.put("focusForProduct2", listWithVariationCode.get(1));
                        jsonObject.put("focusForItemType2", listTypes.get(1));
                    }
                } else {
                    jsonObject.put("focusForProduct1", "");
                    jsonObject.put("focusForItemType1", "");

                    jsonObject.put("focusForProduct2", "");
                    jsonObject.put("focusForItemType2", "");
                }

                mainArray.put(i, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonString = mainArray.toString();

        }

        return jsonString;
    }

    private void configureBottomSheetBehavior(View contentView) {
        final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            bottomSheet.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomSheet.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomSheet.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheet.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });

                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            bottomSheet.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }
    }

    private void plannedDoctorClick()
    {
        selectedDoctorId = "";
        enable_focus = 0;
        edtArea.setText("");

        selectedSpecialityId = "";
        edtSpeciality.setText("");

        dbs = "DCR";
        selectedReportCode = "DCR";
        edtDBC.setText("DCR : Daily Call Report");

        workWithString = "";
        edtWorkWith.setText("");

        selectedDoctorId = "";
        enable_focus = 0;
        edtDoctor.setText("");

        txtAddCount.setText("1");

        workWithString = "";
        NCRDrData = "";
    }
}
