package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AreaResponse;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.DoctorResponse;
import com.unisonpharmaceuticals.model.GiftMonthCheckResponse;
import com.unisonpharmaceuticals.model.MonthResponse;
import com.unisonpharmaceuticals.model.PlannerEntryResponse;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentGiftEntry extends Fragment implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;
    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.edtEmployee)EditText edtEmployee;
    @BindView(R.id.edtMonth)EditText edtMonth;
    @BindView(R.id.edtYear)EditText edtYear;
    @BindView(R.id.inputSpeciality)TextInputLayout inputSpeciality;
    @BindView(R.id.edtSpeciality)EditText edtSpeciality;
    @BindView(R.id.tvSaveGift)TextView tvSaveGift;
    @BindView(R.id.tvConfirmGift)TextView tvConfirmGift;
    @BindView(R.id.tvApproveGift)TextView tvApproveGift;
    @BindView(R.id.tvCheck)TextView tvCheck;
    @BindView(R.id.rvGift)
    RecyclerView rvGift;
    @BindView(R.id.edtArea)EditText edtArea;
    @BindView(R.id.edtDoctor)EditText edtDoctor;
    @BindView(R.id.inputArea)
    TextInputLayout inputArea;
    @BindView(R.id.inputDoctor) TextInputLayout inputDoctor;
    @BindView(R.id.tvNoPlan)TextView tvNoPlan;
    @BindView(R.id.llAction)LinearLayout llAction;
    @BindView(R.id.inputGift)TextInputLayout inputGift;
    @BindView(R.id.edtGift) EditText edtGift;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private List<AreaResponse.AreasBean> listArea = new ArrayList<>();
    private List<AreaResponse.AreasBean> listAreaSearch = new ArrayList<>();

    private ArrayList<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private ArrayList<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private ArrayList<DoctorResponse.DoctorsBean> listDoctor = new ArrayList<>();
    private ArrayList<DoctorResponse.DoctorsBean> listDoctorSearch = new ArrayList<>();

    private ArrayList<GiftMonthCheckResponse.PlanBean> listGift = new ArrayList<>();
    private ArrayList<GiftMonthCheckResponse.PlanBean> listGiftSearch = new ArrayList<>();

    private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();
    private List<YearResponse.YearListBean> listYear = new ArrayList<>();
    private final String MONTH = "month";
    private final String YEAR = "year";
    private final String AREA = "area";
    private final String DOCTOR = "Doctor";
    private final String SPECIALITY = "speciality";
    private final String EMPLOYEE = "employee";
    private final String GIFT = "gift";

    private String selectedStaffId="",selectedMonth = "",selectedYear = "",selectedSpeciality = "",areaString = "",doctorString = "",currentYear = "",selectedGiftType="";

    private List<PlannerEntryResponse.PlanBean> listPlan = new ArrayList<>();
    private GiftAdapter plannerAdapter;

    int map_id = 0;

    private SpringyAdapterAnimator mAnimator;

    private boolean isLoading = false;

    private long mLastClickTime = 0;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
		rootView = inflater.inflate(R.layout.fragment_gift_entry, container, false);
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
		return rootView ;
    }

    private void initViews()
    {
        tvCheck.setVisibility(View.GONE);
        rvGift.setLayoutManager(new LinearLayoutManager(activity));
        edtEmployee.setOnClickListener(this);
        edtMonth.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        edtSpeciality.setOnClickListener(this);
        edtArea.setOnClickListener(this);
        edtDoctor.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        tvSaveGift.setOnClickListener(this);
        tvApproveGift.setOnClickListener(this);
        tvConfirmGift.setOnClickListener(this);
        edtGift.setOnClickListener(this);
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
                if(edtYear.getText().toString().equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select year");
                    return;
                }
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(MONTH);
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
            case R.id.edtArea:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(AREA);
                }
                break;
            case R.id.edtSpeciality:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(SPECIALITY);
                }
                break;
            case R.id.edtDoctor:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    if(listDoctor.size()>0)
                    {
                        showListDialog(DOCTOR);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No Doctors found!");
                    }
                }
                break;
            case R.id.tvCheck:
                checkGiftMonth();
                break;
            case R.id.edtGift:
                if(isLoading)
                {
                    AppUtils.showLoadingToast(activity);
                }
                else
                {
                    showListDialog(GIFT);
                }
                break;
            case R.id.tvSaveGift:
                if(sessionManager.isNetworkAvailable())
                {
                    saveGift();
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvConfirmGift:
                if(sessionManager.isNetworkAvailable())
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
                        confirmGift();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
            case R.id.tvApproveGift:
                if(sessionManager.isNetworkAvailable())
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
                    else if(listPlan.size()==0)
                    {
                        AppUtils.showToast(activity,"Please add gift plan");
                    }
                    else
                    {
                        approveGift();
                    }
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
        }
    }

    private void resetAllField()
    {
        selectedMonth = "";
        edtMonth.setText("");
        edtGift.setText("");
        selectedGiftType.equals("");
        inputGift.setVisibility(View.GONE);
        inputDoctor.setVisibility(View.GONE);
        edtDoctor.setText("");
        if(listDoctor.size()>0)
        {
            for (int i = 0; i < listDoctor.size(); i++)
            {
                DoctorResponse.DoctorsBean bean = listDoctor.get(i);
                bean.setSelected(false);
                listDoctor.set(i,bean);
            }
        }
        listPlan.clear();
        if(plannerAdapter!=null)
        {
            plannerAdapter.notifyDataSetChanged();
        }
        rvGift.setVisibility(View.GONE);
        listGift.clear();
    }

    private void saveGift()
    {
        if(selectedGiftType.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select Gift");
        }
        else
        {
            if(selectedGiftType.equalsIgnoreCase("General Gift"))
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
                else if(selectedGiftType.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select gift");
                }
                else if(doctorString.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select docotr");
                }
                else
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        llLoading.setVisibility(View.VISIBLE);
                        //commented this as riteshbhai told for make all the selected doctor checked
                        /*for (int i = 0; i < listDoctor.size(); i++)
                        {
                            DoctorResponse.DoctorsBean bean = listDoctor.get(i);
                            bean.setSelected(false);
                            listDoctor.set(i,bean);
                        }*/

                        //listDoctor.clear();
                        edtDoctor.setText("");
                        List<String> list= AppUtils.getListFromCommaSeperatedString(doctorString);
                        JSONArray drArray = new JSONArray();
                        for (int i = 0; i < list.size(); i++)
                        {
                            drArray.put(list.get(i).toString());
                        }

                        Call<CommonResponse> saveCall = apiService.saveGiftPlan(selectedMonth,
                                selectedYear,
                                drArray.toString(),
                                "true",
                                selectedStaffId,
                                map_id,sessionManager.getUserId());
                        saveCall.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                            {
                                AppUtils.showToast(activity,response.body().getMessage());
                                if(response.body().getSuccess()==1)
                                {
                                    getGiftPlanner();
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
            else
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
                else if(selectedSpeciality.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select speciality");
                }
                else if(areaString.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select area");
                }
                else if(doctorString.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select docotr");
                }
                else
                {
                    if(sessionManager.isNetworkAvailable())
                    {
                        llLoading.setVisibility(View.VISIBLE);
                        for (int i = 0; i < listDoctor.size(); i++)
                        {
                            DoctorResponse.DoctorsBean bean = listDoctor.get(i);
                            bean.setSelected(false);
                            listDoctor.set(i,bean);
                        }
                        edtDoctor.setText("");
                        List<String> list= AppUtils.getListFromCommaSeperatedString(doctorString);
                        JSONArray drArray = new JSONArray();
                        for (int i = 0; i < list.size(); i++)
                        {
                            drArray.put(list.get(i).toString());
                        }

                        Call<CommonResponse> saveCall = apiService.saveGiftPlan(selectedMonth,
                                selectedYear,
                                drArray.toString(),
                                "true",
                                selectedStaffId,
                                map_id,sessionManager.getUserId());
                        saveCall.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                            {
                                AppUtils.showToast(activity,response.body().getMessage());
                                if(response.body().getSuccess()==1)
                                {
                                    getGiftPlanner();
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
        }

        /*if(selectedStaffId.equalsIgnoreCase(""))
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
        else if(selectedSpeciality.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select speciality");
        }
        else if(areaString.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select area");
        }
        else if(doctorString.equalsIgnoreCase(""))
        {
            AppUtils.showToast(activity,"Please select docotr");
        }
        else
        {
            if(sessionManager.isNetworkAvailable())
            {
                llLoading.setVisibility(View.VISIBLE);
                listDoctor.clear();
                List<String> list= AppUtils.getListFromCommaSeperatedString(doctorString);
                JSONArray drArray = new JSONArray();
                for (int i = 0; i < list.size(); i++)
                {
                    drArray.put(list.get(i).toString());
                }

                Call<CommonResponse> saveCall = apiService.saveGiftPlan(selectedMonth,
                        selectedYear,
                        drArray.toString(),
                        "true",
                        selectedStaffId,
                        map_id,sessionManager.getUserId());
                saveCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        AppUtils.showToast(activity,response.body().getMessage());
                        if(response.body().getSuccess()==1)
                        {
                            getGiftPlanner();
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
        }*/
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
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity, "Could not get employee list.");
                activity.finish();
            }
        });

        isLoading = true;

        Call<SpecialistBean> specialityCall = apiService.getSpeciality("500",sessionManager.getUserId());
        specialityCall.enqueue(new Callback<SpecialistBean>() {
            @Override
            public void onResponse(Call<SpecialistBean> call, Response<SpecialistBean> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listSpeciality = (ArrayList<SpecialistBean.SpecialityBean>) response.body().getSpeciality();
                }
                else
                {
                    AppUtils.showToast(activity,response.body().getMessage());
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t)
            {
                isLoading =false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

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
            }

            @Override
            public void onFailure(Call<YearResponse> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity,"No year data found");
            }
        });

        llLoading.setVisibility(View.GONE);
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
                AppUtils.showToast(activity,"No year data found");
                llLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void checkGiftMonth()
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
            isLoading = true;
            Call<GiftMonthCheckResponse> checkCall = apiService.checkGiftMonth(selectedMonth,selectedYear,selectedStaffId,sessionManager.getUserId());
            checkCall.enqueue(new Callback<GiftMonthCheckResponse>() {
                @Override
                public void onResponse(Call<GiftMonthCheckResponse> call, Response<GiftMonthCheckResponse> response) {

                    try
                    {
                        if(response.body().getSuccess()==1)
                        {
                            listGift = (ArrayList<GiftMonthCheckResponse.PlanBean>) response.body().getPlan();
                            inputGift.setVisibility(View.VISIBLE);
                            rvGift.setVisibility(View.VISIBLE);
                            tvSaveGift.setVisibility(View.VISIBLE);
                            //inputDoctor.setVisibility(View.VISIBLE);
                            //inputSpeciality.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            AppUtils.showToast(activity,response.body().getMessage());

                            inputGift.setVisibility(View.GONE);
                            rvGift.setVisibility(View.GONE);
                            inputDoctor.setVisibility(View.GONE);
                            inputSpeciality.setVisibility(View.GONE);
                        }
                        isLoading = false;
                        //getGiftPlanner();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GiftMonthCheckResponse> call, Throwable t)
                {
                    isLoading = false;
                    t.printStackTrace();
                    inputGift.setVisibility(View.GONE);
                }
            });
        }

    }

    private void getAreaFromSpeciality()
    {
        isLoading = true;

        Call<AreaResponse> loginCall = apiService.getAreaFromSpeciality(selectedSpeciality,selectedStaffId,sessionManager.getUserId());
        loginCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {

                try {
                    if (response.body().getSuccess() == 1)
                    {
                        llAction.setVisibility(View.VISIBLE);
                        listArea = (ArrayList<AreaResponse.AreasBean>) response.body().getAreas();
                        if(listArea.size()>0)
                        {
                            inputArea.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            inputArea.setVisibility(View.GONE);
                            AppUtils.showToast(activity,"No Area fond for selected speciality.");
                        }

                    } else
                    {
                        inputArea.setVisibility(View.GONE);
                        AppUtils.showToast(activity,response.body().getMessage());
                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                isLoading = false;
            }
        });
    }

    private void getDoctorsFromArea()
    {

        isLoading = true;
        Log.e("Area String >>> ", "getDoctorsFromArea: "+areaString );

        llLoading.setVisibility(View.VISIBLE);
        listDoctor.clear();
        Call<DoctorResponse> doctorCall = apiService.getDoctorFromSpeciality(areaString,"5000",selectedSpeciality,selectedStaffId,sessionManager.getUserId());
        doctorCall.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listDoctor = (ArrayList<DoctorResponse.DoctorsBean>) response.body().getDoctors();
                    if(!listDoctor.isEmpty())
                    {
                        inputDoctor.setVisibility(View.VISIBLE);
                        tvSaveGift.setVisibility(View.VISIBLE);
                       /* if(!sessionManager.getUserId().equalsIgnoreCase(selectedStaffId))
                        {
                            tvApproveGift.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tvApproveGift.setVisibility(View.GONE);
                        }*/

                       /*if(listPlan.size()>0)
                       {
                           new Thread(new Runnable() {
                               @Override
                               public void run()
                               {
                                   for (int i = 0; i < listPlan.size(); i++)
                                   {
                                       for (int j = 0; j < listDoctor.size(); j++)
                                       {
                                           if(listPlan.get(i).getDoctor_id().equalsIgnoreCase(listDoctor.get(i).getDoctor_id()))
                                           {
                                               DoctorResponse.DoctorsBean bean = listDoctor.get(i);
                                               bean.setSelected(true);
                                               listDoctor.set(j,bean);
                                           }
                                       }
                                   }
                               }
                           }).start();
                       }*/
                    }
                    else
                    {
                        inputDoctor.setVisibility(View.GONE);
                        AppUtils.showToast(activity,"No Doctors found for selected area.");
                    }
                }
                else
                {
                    AppUtils.showToast(activity,response.body().getMessage());
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

        llLoading.setVisibility(View.GONE);
    }

    private void getGiftPlanner()
    {
        llLoading.setVisibility(View.VISIBLE);
        listPlan.clear();
        Call<PlannerEntryResponse> getPlanner = apiService.getGiftPlan(selectedMonth,selectedYear,selectedStaffId, String.valueOf(map_id),sessionManager.getUserId());
        getPlanner.enqueue(new Callback<PlannerEntryResponse>() {
            @Override
            public void onResponse(Call<PlannerEntryResponse> call, Response<PlannerEntryResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listPlan = response.body().getPlan();

                    plannerAdapter = new GiftAdapter(listPlan,rvGift);
                    rvGift.setAdapter(plannerAdapter);

                    if(listPlan.size()>0)
                    {
                        rvGift.setVisibility(View.VISIBLE);
                        tvNoPlan.setVisibility(View.GONE);

                        AppUtils.showHideBottomButtons(activity,
                                tvSaveGift,
                                tvConfirmGift,
                                tvApproveGift,
                                isConfirmed(),
                                isApproved(),
                                selectedStaffId);

                    }
                    else
                    {
                        rvGift.setVisibility(View.GONE);
                        edtEmployee.requestFocus();
                        tvConfirmGift.setVisibility(View.GONE);
                        tvNoPlan.setVisibility(View.VISIBLE);
                        tvNoPlan.setText(response.body().getMessage());
                        AppUtils.showToast(activity,"No Plan founds for selected date");
                    }

                }
                else
                {
                    rvGift.setVisibility(View.GONE);
                    AppUtils.showToast(activity,response.body().getMessage());
                    tvNoPlan.setVisibility(View.VISIBLE);
                    tvConfirmGift.setVisibility(View.GONE);
                    tvApproveGift.setVisibility(View.GONE);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PlannerEntryResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
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

    private void confirmGift()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> confirmGift = apiService.confirmGiftPlan(selectedMonth,selectedYear,selectedStaffId,sessionManager.getUserId());
        confirmGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                AppUtils.showToast(activity,response.body().getMessage());
                llLoading.setVisibility(View.GONE);
                if(response.body().getSuccess()==1)
                {
                    tvConfirmGift.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void approveGift()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveGiftPlan(selectedMonth,selectedYear,selectedStaffId,sessionManager.getUserId(),sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                AppUtils.showToast(activity,response.body().getMessage());

                if(response.body().getSuccess()==1)
                {
                    tvApproveGift.setVisibility(View.GONE);
                    rvGift.setVisibility(View.GONE);
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    DialogListAdapter areaAdapter;
    public void showListDialog(final String isFor)
    {
        listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);
        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);
        configureBottomSheetBehavior(sheetView);

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
        CheckBox cbSelectAll = listDialog.findViewById(R.id.cbSelectAll);

        final TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        if(isFor.equalsIgnoreCase(AREA) || isFor.equalsIgnoreCase(DOCTOR))
        {
            tvDone.setVisibility(View.VISIBLE);

            if(isFor.equalsIgnoreCase(AREA))
            {
                if(areaAdapter.getItemCount()>1)
                {
                    cbSelectAll.setVisibility(View.VISIBLE);
                }
                else
                {
                    cbSelectAll.setVisibility(View.GONE);
                }
            }
            else
            {
                if(selectedGiftType.equalsIgnoreCase("General Gift"))
                {
                    cbSelectAll.setVisibility(View.VISIBLE);
                }
                else
                {
                    cbSelectAll.setVisibility(View.GONE);
                }
            }

            listDialog.setCancelable(false);
            listDialog.findViewById(R.id.ivBack).setVisibility(View.GONE);
            cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isFor.equals(AREA))
                    {
                        if(isChecked)
                        {
                            areaAdapter.selectAllAreas(true);
                        }
                        else
                        {
                            areaAdapter.selectAllAreas(false);
                        }
                    }
                    else if(isFor.equals(DOCTOR))
                    {
                        if(isChecked)
                        {
                            areaAdapter.selectAllDoctors(true);
                        }
                        else
                        {
                            areaAdapter.selectAllDoctors(false);
                        }
                    }
                }
            });
        }
        else
        {
            tvDone.setVisibility(View.GONE);
            cbSelectAll.setVisibility(View.GONE);
        }

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < listPlan.size(); i++)
                    {
                        for (int j = 0; j < listDoctor.size(); j++)
                        {
                            if(listPlan.get(i).getDoctor_id().equalsIgnoreCase(listDoctor.get(j).getDoctor_id()))
                            {
                                DoctorResponse.DoctorsBean bean = listDoctor.get(j);
                                bean.setSelected(true);
                                listDoctor.set(j,bean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        areaAdapter = new DialogListAdapter(listDialog, isFor,false,"",rvListDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(areaAdapter);

        tvDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isFor.equalsIgnoreCase(AREA)&&areaAdapter !=null)
                {
                    areaString = areaAdapter.getSelectedAreaIds();
                    if(areaString.isEmpty())
                    {
                        AppUtils.showToast(activity,"Please select at least one area.");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(tvDone,activity);
                        edtArea.setText(areaAdapter.getSelectedAreaNames());
                        listDialog.dismiss();
                        listDialog.cancel();
                        getDoctorsFromArea();
                    }
                }
                else if(isFor.equalsIgnoreCase(DOCTOR)&&areaAdapter !=null)
                {
                    doctorString = areaAdapter.getSelectedDrIds();
                    if(doctorString.length()==0)
                    {
                        AppUtils.showToast(activity,"Please select at least one doctor.");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(tvDone,activity);
                        edtDoctor.setText(areaAdapter.getSelectedDrNames());
                        listDialog.dismiss();
                        listDialog.cancel();
                    }
                }
                else
                {
                    AppUtils.hideKeyboard(tvDone,activity);



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
                else if(isFor.equals(DOCTOR))
                {
                    listDoctorSearch.clear();
                    for (int i = 0; i < listDoctor.size(); i++)
                    {
                        if (textlength <= listDoctor.get(i).getDoctor().length())
                        {
                            if (listDoctor.get(i).getDoctor().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listDoctor.get(i).getDoctor_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listDoctorSearch.add(listDoctor.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(AREA))
                {
                    listAreaSearch.clear();
                    for (int i = 0; i < listArea.size(); i++)
                    {
                        if (textlength <= listArea.get(i).getArea().length())
                        {
                            if (listArea.get(i).getArea().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listAreaSearch.add(listArea.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(SPECIALITY))
                {
                    listSpecialitySearch.clear();
                    for (int i = 0; i < listSpeciality.size(); i++)
                    {
                        if (textlength <= listSpeciality.get(i).getSpeciality().length())
                        {
                            if (listSpeciality.get(i).getSpeciality().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listSpeciality.get(i).getSpeciality_code().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listSpecialitySearch.add(listSpeciality.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(GIFT))
                {
                    listGiftSearch.clear();
                    for (int i = 0; i < listGift.size(); i++)
                    {
                        if (textlength <= listGift.get(i).getItem().length())
                        {
                            if (listGift.get(i).getItem().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listGiftSearch.add(listGift.get(i));
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

    private void configureBottomSheetBehavior(View contentView)
    {
        final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (mBottomSheetBehavior != null)
        {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
            {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState)
                {
                    switch (newState)
                    {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });

                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset)
                {

                }
            });
        }
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>

    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false,isDone = false;
        String searchText = "";

        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyclerView)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
            //mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator,recyclerView);

        }

        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            //mAnimator.onSpringItemCreate(v);
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
            //mAnimator.onSpringItemBind(holder.itemView,position);
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
                        resetAllField();
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
                        inputGift.setVisibility(View.GONE);
                        edtGift.setText("");
                        checkGiftMonth();
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(YEAR))
            {
                holder.cb.setVisibility(View.GONE);
                holder.tvValue.setText(String.valueOf(listYear.get(position).getYear()));
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
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
            else if(isFor.equals(AREA))
            {
                holder.cb.setVisibility(View.VISIBLE);

                final AreaResponse.AreasBean getSet;
                if(isForSearch)
                {
                    getSet = listAreaSearch.get(position);
                }
                else
                {
                    getSet = listArea.get(position);
                }

                isDone = true;
                holder.cb.setChecked(getSet.isSelected());

                holder.tvValue.setVisibility(View.GONE);

                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));
                holder.cb.setText(getSet.getArea());

                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if(!isDone)
                        {
                            getSet.setSelected(isChecked);
                            if(isForSearch)
                            {
                                listAreaSearch.set(position,getSet);
                            }
                            else
                            {
                                listArea.set(position,getSet);
                            }
                            try {
                                notifyItemChanged(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

                isDone = false;
            }
            else if(isFor.equals(DOCTOR))
            {
                holder.cb.setVisibility(View.VISIBLE);

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
                holder.tvId.setVisibility(View.VISIBLE);
                holder.tvId.setText(getSet.getDoctor_id());

                holder.cb.setTypeface(AppUtils.getTypefaceRegular(activity));
                holder.cb.setText(getSet.getDoctor());

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

                isDone = false;
            }
            else if(isFor.equalsIgnoreCase(SPECIALITY))
            {
                final SpecialistBean.SpecialityBean getSet;
                if(isForSearch)
                {
                    getSet = listSpecialitySearch.get(position);
                }
                else
                {
                    getSet = listSpeciality.get(position);
                }

                holder.tvValue.setText(getSet.getSpeciality());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            llAction.setVisibility(View.GONE);
                            selectedSpeciality = getSet.getSpeciality_id();
                            edtSpeciality.setText(getSet.getSpeciality());
                            edtDoctor.setText("");
                            doctorString = "";
                            edtArea.setText("");
                            areaString = "";
                            tvNoPlan.setVisibility(View.GONE);
                            /*listPlan.clear();
                            if(plannerAdapter!=null)
                            {
                                plannerAdapter.notifyDataSetChanged();
                            }*/
                            //inputDoctor.setVisibility(View.GONE);
                            getAreaFromSpeciality();
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(GIFT))
            {
                holder.cb.setVisibility(View.GONE);

                final GiftMonthCheckResponse.PlanBean getSet;
                if(isForSearch)
                {
                    getSet = listGiftSearch.get(position);
                }
                else
                {
                    getSet = listGift.get(position);
                }


                holder.tvValue.setText(getSet.getItem());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try {
                            map_id = Integer.parseInt(getSet.getMap_id());
                            edtGift.setText(getSet.getItem());
                            dialog.dismiss();
                            dialog.cancel();

                            selectedGiftType = getSet.getType();

                            if(selectedGiftType.equalsIgnoreCase("General Gift"))
                            {
                                inputDoctor.setVisibility(View.GONE);
                                inputSpeciality.setVisibility(View.GONE);

                                getDoctorsFromArea();
                            }
                            else
                            {
                                inputDoctor.setVisibility(View.VISIBLE);
                                inputSpeciality.setVisibility(View.VISIBLE);
                            }

                            getGiftPlanner();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        public String getSelectedAreaIds()
        {
            String str = "";
            for (int i = 0; i < listArea.size(); i++)
            {
                if(listArea.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str = listArea.get(i).getArea_id().trim();
                    }
                    else
                    {
                        //str.append(","+listArea.get(i).getArea_id().trim());
                        str = str + ","+listArea.get(i).getArea_id().trim();
                    }
                }
            }

            return str;
        }

        public String getSelectedAreaNames()
        {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listArea.size(); i++)
            {
                if(listArea.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str.append(listArea.get(i).getArea());
                    }
                    else
                    {
                        str.append(","+listArea.get(i).getArea());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedDrIds()
        {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listDoctor.size(); i++)
            {
                if(listDoctor.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str.append(listDoctor.get(i).getDoctor_id().trim());
                    }
                    else
                    {
                        str.append(","+listDoctor.get(i).getDoctor_id().trim());
                    }
                }
            }

            return String.valueOf(str);
        }

        public String getSelectedDrNames()
        {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < listDoctor.size(); i++)
            {
                if(listDoctor.get(i).isSelected())
                {
                    if(str.length()==0)
                    {
                        str.append(listDoctor.get(i).getDoctor());
                    }
                    else
                    {
                        str.append(","+listDoctor.get(i).getDoctor());
                    }
                }
            }

            return String.valueOf(str);
        }

        public void selectAllDoctors(boolean isForCheck)
        {
            for (int i = 0; i < listDoctor.size(); i++)
            {
                if(isForCheck)
                {
                    listDoctor.get(i).setSelected(true);
                }
                else
                {
                    listDoctor.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

        public void selectAllAreas(boolean isForCheck)
        {
            for (int i = 0; i < listArea.size(); i++)
            {
                if(isForCheck)
                {
                    listArea.get(i).setSelected(true);
                }
                else
                {
                    listArea.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
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
            else if(isFor.equalsIgnoreCase(SPECIALITY))
            {
                if(isForSearch)
                {
                    return listSpecialitySearch.size();
                }
                else
                {
                    return listSpeciality.size();
                }
            }
            else if(isFor.equalsIgnoreCase(DOCTOR))
            {
                if(isForSearch)
                {
                    return listDoctorSearch.size();
                }
                else
                {
                    return listDoctor.size();
                }
            }
            else if(isFor.equalsIgnoreCase(GIFT))
            {
                if(isForSearch)
                {
                    return listGiftSearch.size();
                }
                else
                {
                    return listGift.size();
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

    private void AppendList(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea)
    {
        areaAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>
    {
        List<PlannerEntryResponse.PlanBean> listItems;

        GiftAdapter(List<PlannerEntryResponse.PlanBean> list,RecyclerView recyclerView)
        {
            this.listItems = list;
        }

        @Override
        public GiftAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_gift_entry_table, viewGroup, false);
            return new GiftAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final GiftAdapter.ViewHolder holder, final int position)
        {
            final PlannerEntryResponse.PlanBean getSet = listItems.get(position);
            if(position==0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }
            int srNo = position + 1;
            holder.tvSrno.setText(String.valueOf(srNo));
            holder.tvDrId.setText(getSet.getDoctor_id());
            holder.tvDrName.setText(getSet.getDoctor());
            if(getSet.getMon_business()==0)
            {
                holder.tvMonBusiness.setText(" - ");
            }
            else
            {
                holder.tvMonBusiness.setText(String.valueOf(getSet.getMon_business()));
            }

            if(getSet.getIs_approved().equals("") || getSet.getIs_approved().equalsIgnoreCase("No"))
            {
                holder.ivDelete.setVisibility(View.VISIBLE);
            }
            else if(getSet.getIs_approved().equalsIgnoreCase("Yes"))
            {
                holder.ivDelete.setVisibility(View.GONE);
            }

            if(getSet.getSpeciality().equals(""))
            {
                holder.tvSpeciality.setText(" - ");
            }
            else
            {
                holder.tvSpeciality.setText(getSet.getSpeciality());
            }

            holder.tvConfirm.setText(getSet.getIs_confirmed());

            holder.ivDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showConfirmationDialog(position,getSet);
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
            @BindView(R.id.tvSrno)TextView tvSrno;
            @BindView(R.id.tvDrId)TextView tvDrId;
            @BindView(R.id.tvDrName)TextView tvDrName;
            @BindView(R.id.tvMonBusiness)TextView tvMonBusiness;
            @BindView(R.id.tvConfirm)TextView tvConfirm;
            @BindView(R.id.tvSpeciality)TextView tvSpeciality;
            @BindView(R.id.llTitle)LinearLayout llTitle;

            @BindView(R.id.ivDelete)ImageView ivDelete;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    private void showConfirmationDialog(final int pos, final PlannerEntryResponse.PlanBean getSet)
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtHeader2 = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Delete");
            txtHeader2.setText("Are you sure you want to delete?");

            btnNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(v -> {
                try
                {
                    dialog.dismiss();
                    dialog.cancel();

                    llLoading.setVisibility(View.VISIBLE);
                    Call<CommonResponse> getPlanner = apiService.deleteGiftPlan(getSet.getMap_id(),sessionManager.getUserId());
                    getPlanner.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                        {
                            if(response.body().getSuccess()==1)
                            {
                                listPlan.remove(getSet);
                                if(listPlan.size()==0)
                                {
                                    tvNoPlan.setVisibility(View.VISIBLE);
                                    tvConfirmGift.setVisibility(View.GONE);
                                    new Thread(() -> {
                                        try
                                        {
                                            for (int j = 0; j < listDoctor.size(); j++)
                                            {
                                                DoctorResponse.DoctorsBean bean = listDoctor.get(j);
                                                bean.setSelected(false);
                                                listDoctor.set(j,bean);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }).start();
                                }
                                else
                                {
                                    tvNoPlan.setVisibility(View.GONE);
                                    tvConfirmGift.setVisibility(View.VISIBLE);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try
                                            {
                                                for (int i = 0; i < listPlan.size(); i++)
                                                {
                                                    for (int j = 0; j < listDoctor.size(); j++)
                                                    {
                                                        DoctorResponse.DoctorsBean bean = listDoctor.get(j);
                                                        if(listPlan.get(i).getDoctor_id().equalsIgnoreCase(listDoctor.get(j).getDoctor_id()))
                                                        {
                                                            bean.setSelected(true);
                                                        }
                                                        else
                                                        {
                                                            bean.setSelected(false);
                                                        }
                                                        listDoctor.set(j,bean);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }

                                if(areaAdapter!=null)
                                {
                                    doctorString = areaAdapter.getSelectedDrIds();
                                }

                                if(plannerAdapter!=null)
                                {
                                    plannerAdapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                AppUtils.showToast(activity,response.body().getMessage());
                            }
                            llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            llLoading.setVisibility(View.GONE);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    dialog.cancel();
                }

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
