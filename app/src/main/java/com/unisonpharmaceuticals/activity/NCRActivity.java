package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentMakeEntry;
import com.unisonpharmaceuticals.model.CityDistrictResponse;
import com.unisonpharmaceuticals.model.NCREmployeeResponse;
import com.unisonpharmaceuticals.model.QualificationResponse;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.model.TourAreaResponse;
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

public class NCRActivity extends BaseClass implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;

    @BindView(R.id.llLoading) LinearLayout llLoading;
    @BindView(R.id.llLoadingTransparent) LinearLayout llLoadingTransparent;

    @BindView(R.id.inputDoctorName)
    TextInputLayout inputDoctorName;
    @BindView(R.id.inputQualification) TextInputLayout inputQualification;
    @BindView(R.id.inputSpeciality) TextInputLayout inputSpeciality;
    @BindView(R.id.inputDrType) TextInputLayout inputDrType;
    @BindView(R.id.inputAddress1) TextInputLayout inputAddress1;
    @BindView(R.id.inputAddress2) TextInputLayout inputAddress2;
    @BindView(R.id.inputAddress3) TextInputLayout inputAddress3;
    @BindView(R.id.inputAddress4) TextInputLayout inputAddress4;
    @BindView(R.id.inputDistrict) TextInputLayout inputDistrict;
    @BindView(R.id.inputCity) TextInputLayout inputCity;
    @BindView(R.id.inputArea) TextInputLayout inputArea;
    @BindView(R.id.inputPinCode) TextInputLayout inputPinCode;

    @BindView(R.id.edtDoctor) EditText edtDoctor;
    @BindView(R.id.edtQualification) EditText edtQualification;
    @BindView(R.id.edtSpeciality) EditText edtSpeciality;
    @BindView(R.id.edtDrType) EditText edtDrType;
    @BindView(R.id.edtAddress1) EditText edtAddress1;
    @BindView(R.id.edtAddress2) EditText edtAddress2;
    @BindView(R.id.edtAddress3) EditText edtAddress3;
    @BindView(R.id.edtAddress4) EditText edtAddress4;
    @BindView(R.id.edtDistrict) EditText edtDistrict;
    @BindView(R.id.edtCity) EditText edtCity;
    @BindView(R.id.edtArea) EditText edtArea;
    @BindView(R.id.edtPinCode) EditText edtPinCode;

    @BindView(R.id.edtVisit) EditText edtVisit;
    @BindView(R.id.rvDepartment)
    RecyclerView rvDepartment;

    @BindView(R.id.btnSubmit) TextView btnSubmit;

    ListAdapter adapter = null;

    private Dialog listDialog;

    private ApiInterface apiService;

    private ArrayList<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private ArrayList<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private ArrayList<QualificationResponse.QualificationsBean> listQualification = new ArrayList<>();
    private ArrayList<QualificationResponse.QualificationsBean> listQualificationSearch = new ArrayList<>();

    private List<TourAreaResponse.TourAreaBean> listArea = new ArrayList<>();
    private List<TourAreaResponse.TourAreaBean> listAreaSearch = new ArrayList<>();

    private ArrayList<String> listVisit = new ArrayList<>();

    private List<NCREmployeeResponse.DivisionEmployeesBean> listDivEmployee = new ArrayList<>();
    private DivEmployeeAdapter divEmployeeAdapter;

    private ArrayList<String> listDrType = new ArrayList<>();
    private String districtId = "",cityId = "",areaId = "",qualificationId = "",specialityId = "",doctorTypeId = "",selectedVisitAt = "";

    private final String AREA = "Area";
    private final String QUALIFICATION = "Qualification";
    private final String SPECIALITY = "Speciality";
    private final String DRTYPOE = "Doctor Type";
    private final String VISIT = "Visit At";

    private final String DIV_EMP = "Employee";

    private String jsonString = "";

    private int divPos = 0;
    private String selectedDivision = "",selectedDivisionId = "";
    private EditText edtDiv;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncr);
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        basicProcesses();
    }

    @Override
    public void initViews()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                finishActivityAnimation(activity);
            }
        });
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Add New Doctor");

        listVisit.add("Morning");
        listVisit.add("Evening");

        rvDepartment.setLayoutManager(new LinearLayoutManager(activity));

        removeError(edtDoctor,inputDoctorName);
        removeError(edtQualification,inputQualification);
        removeError(edtSpeciality,inputSpeciality);
        removeError(edtDrType,inputDrType);
        removeError(edtAddress1,inputAddress1);
        removeError(edtAddress2,inputAddress2);
        removeError(edtAddress3,inputAddress3);
        removeError(edtAddress4,inputAddress4);
        removeError(edtDistrict,inputDistrict);
        removeError(edtCity,inputCity);
        removeError(edtArea,inputArea);
        removeError(edtPinCode,inputPinCode);
    }

    @Override
    public void viewClick()
    {
        btnSubmit.setOnClickListener(this);
        edtDistrict.setOnClickListener(this);
        edtCity.setOnClickListener(this);
        edtArea.setOnClickListener(this);
        edtQualification.setOnClickListener(this);
        edtSpeciality.setOnClickListener(this);
        edtDrType.setOnClickListener(this);
        edtVisit.setOnClickListener(this);

        listDrType.add("CONS");
        listDrType.add("GP");
        listDrType.add("RESI");
        listDrType.add("AP");
    }

    @Override
    public void onClick(View v)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId()) {

            case R.id.btnSubmit:
                if(checkValidation())
                {

                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put("district_id",districtId);
                        jsonObject.put("city_id",cityId);
                        jsonObject.put("area_id",areaId);
                        jsonObject.put("doctor",edtDoctor.getText().toString().trim());
                        jsonObject.put("address_1",edtAddress1.getText().toString().trim());
                        jsonObject.put("address_2",edtAddress2.getText().toString().trim());
                        jsonObject.put("address_3",edtAddress3.getText().toString().trim());
                        jsonObject.put("address_4",edtAddress4.getText().toString().trim());
                        jsonObject.put("city",cityId);
                        jsonObject.put("pin_code",edtPinCode.getText().toString());
                        jsonObject.put("qualification",qualificationId);
                        jsonObject.put("speciality",specialityId);
                        jsonObject.put("doctor_type",doctorTypeId);
                        jsonObject.put("visit",selectedVisitAt);
                        jsonObject.put("otherMrs",divEmployeeAdapter.getEmpData());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(FragmentMakeEntry.handlerNCR1!=null)
                    {
                        Message message = Message.obtain();
                        message.what=111;
                        message.obj = jsonObject.toString();
                        FragmentMakeEntry.handlerNCR1.sendMessage(message);
                    }

                    if(FragmentMakeEntry.handlerNCR!=null)
                    {
                        Message message = Message.obtain();
                        message.what=111;
                        message.obj = edtDoctor.getText().toString().trim();
                        FragmentMakeEntry.handlerNCR.sendMessage(message);
                    }
                    showToast(activity,"Doctor Added !");
                    activity.finish();
                }
                break;
            case R.id.edtArea:
                showListDialog(AREA);
                break;

            case R.id.edtQualification:
                showListDialog(QUALIFICATION);
                break;

            case R.id.edtSpeciality:
                showListDialog(SPECIALITY);
                break;

            case R.id.edtDrType:
                showListDialog(DRTYPOE);
                break;

            case R.id.edtVisit:
                showListDialog(VISIT);
                break;

            default:
                break;
        }

    }

    @Override
    public void getDataFromServer()
    {
        llLoading.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t)
            {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });


        Call<QualificationResponse> qualificationCall = apiService.getQualification("200",sessionManager.getUserId());
        qualificationCall.enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listQualification = (ArrayList<QualificationResponse.QualificationsBean>) response.body().getQualifications();
                }
                else
                {
                    AppUtils.showToast(activity,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t)
            {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

        Call<TourAreaResponse> tpAreaCall = apiService.getTPArea(sessionManager.getUserId(),sessionManager.getUserId());
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
            }

            @Override
            public void onFailure(Call<TourAreaResponse> call, Throwable t) {
                AppUtils.showToast(activity,"No data found");
            }
        });

        llLoading.setVisibility(View.GONE);
    }

    private void getCityDistrictFromArea()
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);
        Call<CityDistrictResponse> call = apiService.getCityAndDistFromArea(areaId);
        call.enqueue(new Callback<CityDistrictResponse>() {
            @Override
            public void onResponse(Call<CityDistrictResponse> call, Response<CityDistrictResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        areaId = response.body().getArea().getArea_id();
                        cityId = response.body().getArea().getCity_id();
                        districtId = response.body().getArea().getDistrict_id();

                        edtCity.setText(response.body().getArea().getCity());
                        edtDistrict.setText(response.body().getArea().getDistrict());

                        Log.e(">>>>>>>>>>>", "onResponse: "+response.body().getArea().getDistrict() );
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoadingTransparent.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CityDistrictResponse> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    public void showListDialog(final String isFor)
    {
        /*LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_list, null);

        listDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setContentView(promptView);*/

        listDialog= new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

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

        final RecyclerView rvReason = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);
        final EditText edtSearch = (EditText) listDialog.findViewById(R.id.edtSearchDialog);

        adapter = new ListAdapter(listDialog,isFor,false,"");
        rvReason.setLayoutManager(new LinearLayoutManager(activity));
        rvReason.setAdapter(adapter);

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        if(!isFor.equalsIgnoreCase(VISIT))
        {
            inputSearch.setVisibility(View.VISIBLE);
        }
        else
        {
            inputSearch.setVisibility(View.GONE);
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                int textlength = edtSearch.getText().length();

                if(isFor.equals(AREA))
                {
                    listAreaSearch.clear();
                    for (int i = 0; i < listArea.size(); i++)
                    {
                        if (textlength <= listArea.get(i).getArea_name().length())
                        {
                            if (listArea.get(i).getArea_name().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listAreaSearch.add(listArea.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(QUALIFICATION))
                {
                    listQualificationSearch.clear();
                    for (int i = 0; i < listQualification.size(); i++)
                    {
                        if (textlength <= listQualification.get(i).getQualification().length())
                        {
                            if (listQualification.get(i).getQualification().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listQualificationSearch.add(listQualification.get(i));
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
                            if (listSpeciality.get(i).getSpeciality().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listSpeciality.get(i).getSpeciality_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listSpeciality.get(i).getSpeciality_id().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listSpecialitySearch.add(listSpeciality.get(i));
                            }
                        }
                    }
                }

                AppendListForArea(listDialog,isFor,true,rvReason);
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
        adapter = new ListAdapter(dialog,isFor,true,"");
        rvArea.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        private boolean isForSearch;
        private String searchText = "";

        ListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText)
        {

            this.isFor = isFor;
            this.dialog = dialog;
            this.isForSearch = isForSearch;
            this.searchText = searchText;
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {

            if(position == getItemCount()-1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }


            if(isFor.equalsIgnoreCase(AREA))
            {
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

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            areaId = getSet.getArea_id();
                            edtArea.setText(getSet.getArea_name());
                            getDepartmentEmployee(getSet.getArea_id());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(QUALIFICATION))
            {
                final QualificationResponse.QualificationsBean getSet;
                if(isForSearch)
                {
                    getSet = listQualificationSearch.get(position);
                }
                else
                {
                    getSet = listQualification.get(position);
                }

                holder.tvValue.setText(getSet.getQualification());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                           dialog.dismiss();
                           qualificationId = getSet.getQualification_id();
                           edtQualification.setText(getSet.getQualification());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
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
                            dialog.dismiss();
                            specialityId = getSet.getSpeciality_id();
                            edtSpeciality.setText(getSet.getSpeciality());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(DRTYPOE))
            {
                holder.tvValue.setText(listDrType.get(position));


                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            doctorTypeId = listDrType.get(position);
                            edtDrType.setText(listDrType.get(position));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(VISIT))
            {
                holder.tvValue.setText(listVisit.get(position));

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            selectedVisitAt = listVisit.get(position).toLowerCase();
                            edtVisit.setText(listVisit.get(position));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(DIV_EMP))
            {
                holder.tvValue.setText((listDivEmployee.get(divPos).getEmployees().get(position).getEmployee()));
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            edtDiv.setText((listDivEmployee.get(divPos).getEmployees().get(position).getEmployee()));
                            divEmployeeAdapter.selectEmployee((listDivEmployee.get(divPos).getEmployees().get(position).getEmployee_id()));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }

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
            else if(isFor.equalsIgnoreCase(QUALIFICATION))
            {
                if(isForSearch)
                {
                    return listQualificationSearch.size();
                }
                else
                {
                    return listQualification.size();
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
            else if(isFor.equalsIgnoreCase(DRTYPOE))
            {
                return listDrType.size();
            }
            else if(isFor.equalsIgnoreCase(VISIT))
            {
                return listVisit.size();
            }
            else if(isFor.equalsIgnoreCase(DIV_EMP))
            {
                return listDivEmployee.get(divPos).getEmployees().size();
            }
            else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvValue;
            private View viewLine;
            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }

    private void getDepartmentEmployee(String areaId)
    {
        llLoadingTransparent.setVisibility(View.VISIBLE);

        getCityDistrictFromArea();

        Call<NCREmployeeResponse> employeeCall = apiService.getNCREmployee(sessionManager.getUserId(),areaId);
        employeeCall.enqueue(new Callback<NCREmployeeResponse>() {
            @Override
            public void onResponse(Call<NCREmployeeResponse> call, Response<NCREmployeeResponse> response) {
                if(response!=null)
                {
                    listDivEmployee = response.body().getDivision_employees();
                    divEmployeeAdapter = new DivEmployeeAdapter();
                    rvDepartment.setAdapter(divEmployeeAdapter);
                }
                llLoadingTransparent.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NCREmployeeResponse> call, Throwable t) {
                llLoadingTransparent.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkValidation()
    {
        boolean isValid = true;

        if(edtDoctor.getText().toString().trim().length()==0)
        {
            inputDoctorName.setError("Please Enter Doctor Name.");
            isValid = false;
        }
        else if(qualificationId.equalsIgnoreCase(""))
        {
            inputQualification.setError("Please Select Doctor Qualification.");
            isValid = false;
        }
        else if(specialityId.equalsIgnoreCase(""))
        {
            inputSpeciality.setError("Please Select Doctor Speciality.");
            isValid = false;
        }
        else if(doctorTypeId.equalsIgnoreCase(""))
        {
            inputDrType.setError("Please Select Doctor Type.");
            isValid = false;
        }
        else if(edtAddress1.getText().toString().trim().length()==0)
        {
            inputAddress1.setError("Please Enter Address Line 1.");
            isValid = false;
        }
        else if(edtAddress2.getText().toString().trim().length()==0)
        {
            inputAddress2.setError("Please Enter Address Line 2.");
            isValid = false;
        }
        else if(areaId.equalsIgnoreCase(""))
        {
            inputArea.setError("Please Select Area.");
            isValid = false;
        }
        else if(districtId.equalsIgnoreCase(""))
        {
            inputDistrict.setError("Please Select District.");
            isValid = false;
        }
        else if(cityId.equalsIgnoreCase(""))
        {
            inputCity.setError("Please Select City.");
            isValid = false;
        }
        else if(edtPinCode.getText().toString().trim().length()==0)
        {
            inputPinCode.setError("Please Enter Pincode.");
            isValid = false;
        }
        else if(edtPinCode.getText().toString().trim().length()!=6)
        {
            inputPinCode.setError("Please Enter Proper Pincode.");
            isValid = false;
        }

        return isValid;
    }

    public class DivEmployeeAdapter extends RecyclerView.Adapter<DivEmployeeAdapter.ViewHolder>
    {
        @Override
        public DivEmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_divemployee, viewGroup, false);
            return new DivEmployeeAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DivEmployeeAdapter.ViewHolder viewHolder, final int i)
        {
            final NCREmployeeResponse.DivisionEmployeesBean getSet = listDivEmployee.get(i);

            viewHolder.edtDivEmp.setHint("Select "+getSet.getDivision()+" employee");

            viewHolder.edtDivEmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtDiv = viewHolder.edtDivEmp;
                    divPos = i;
                    selectedDivision = getSet.getDivision();
                    selectedDivisionId = getSet.getDivision_id();

                    if(getSet.getEmployees().size()>0)
                    {
                        showListDialog(DIV_EMP);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No employee available for this division.");
                    }

                }
            });
        }

        private void selectEmployee(String empId)
        {
            for (int i = 0; i < listDivEmployee.get(divPos).getEmployees().size(); i++)
            {
                if(listDivEmployee.get(divPos).getEmployees().get(i).getEmployee_id().equalsIgnoreCase(empId))
                {
                    NCREmployeeResponse.DivisionEmployeesBean mainBean = listDivEmployee.get(divPos);
                    NCREmployeeResponse.DivisionEmployeesBean.EmployeesBean empBean = listDivEmployee.get(divPos).getEmployees().get(i);
                    empBean.setSelected(true);
                    listDivEmployee.get(divPos).getEmployees().set(i,empBean);
                    mainBean.setEmployees(listDivEmployee.get(divPos).getEmployees());
                    listDivEmployee.set(divPos,mainBean);
                }
                else
                {
                    NCREmployeeResponse.DivisionEmployeesBean mainBean = listDivEmployee.get(divPos);
                    NCREmployeeResponse.DivisionEmployeesBean.EmployeesBean empBean = listDivEmployee.get(divPos).getEmployees().get(i);
                    empBean.setSelected(false);
                    listDivEmployee.get(divPos).getEmployees().set(i,empBean);
                    mainBean.setEmployees(listDivEmployee.get(divPos).getEmployees());
                    listDivEmployee.set(divPos,mainBean);
                }
            }
        }

        private JSONObject getEmpData()
        {
            JSONObject otherObj = new JSONObject();
            if(listDivEmployee.size()>0)
            {
                for (int i = 0; i < listDivEmployee.size(); i++)
                {
                    if(listDivEmployee.get(i).getEmployees().size()>0)
                    {
                        for (int j = 0; j < listDivEmployee.get(i).getEmployees().size(); j++)
                        {
                            if(listDivEmployee.get(i).getEmployees().get(j).isSelected())
                            {
                                try {
                                    otherObj.put(listDivEmployee.get(i).getDivision_id(),listDivEmployee.get(i).getEmployees().get(j).getEmployee_id());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            return otherObj;
        }

        @Override
        public int getItemCount() {
            return listDivEmployee.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextInputLayout inputDivEmp;
            EditText edtDivEmp;

            ViewHolder(View v)
            {
                super(v);

                inputDivEmp = (TextInputLayout) v.findViewById(R.id.inputDivEmp);
                edtDivEmp = (EditText) v.findViewById(R.id.edtDivEmp);
            }
        }
    }
}
