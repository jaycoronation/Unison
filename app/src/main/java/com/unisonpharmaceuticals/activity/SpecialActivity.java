package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AreaResponse;
import com.unisonpharmaceuticals.model.CityResponse;
import com.unisonpharmaceuticals.model.DistrictResponse;
import com.unisonpharmaceuticals.model.SearchDoctorResponse;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialActivity extends BaseClass implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.llNoData)LinearLayout llNoData;

    @BindView(R.id.inputDoctorName)
    TextInputLayout inputDoctorName;
    @BindView(R.id.inputSpeciality)TextInputLayout inputSpeciality;
    @BindView(R.id.inputDistrict)TextInputLayout inputDistrict;
    @BindView(R.id.inputCity)TextInputLayout inputCity;
    @BindView(R.id.inputArea)TextInputLayout inputArea;

    @BindView(R.id.edtDoctorName)EditText edtDoctorName;
    @BindView(R.id.edtSpeciality) EditText edtSpeciality;
    @BindView(R.id.edtDistrict) EditText edtDistrict;
    @BindView(R.id.edtCity) EditText edtCity;
    @BindView(R.id.edtArea) EditText edtArea;

    @BindView(R.id.tvSearchDr)TextView tvSearchDr;
    @BindView(R.id.tvCancel)TextView tvCancel;
    @BindView(R.id.rvDoctor)
    RecyclerView rvDoctor;
    @BindView(R.id.tvNoDr)TextView tvNoDr;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private List<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private List<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private List<DistrictResponse.DistrictsBean> listDistrict = new ArrayList<>();
    private List<DistrictResponse.DistrictsBean> listDistrictSearch = new ArrayList<>();

    private List<CityResponse.CitiesBean> listCity = new ArrayList<>();
    private List<CityResponse.CitiesBean> listCitySearch = new ArrayList<>();

    private List<AreaResponse.AreasBean> listArea = new ArrayList<>();
    private List<AreaResponse.AreasBean> listAreaSearch = new ArrayList<>();

    private List<SearchDoctorResponse.DoctorsBean> listDoctor = new ArrayList<>();

    private String districtId = "",cityId = "",areaId = "",specialityId = "",doctorId="";

    private final String AREA = "Area";
    private final String CITY = "City";
    private final String DISTRICT = "District";
    private final String SPECIALITY = "Speciality";
    
    private BottomSheetDialog listDialog;
    private ListAdapter adapter;

    private DoctorAdapter doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        activity = this;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(activity);
        ButterKnife.bind(activity);
        basicProcesses();
    }

    @Override
    public void initViews() {
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llLogout).setVisibility(View.VISIBLE);
        findViewById(R.id.llLogout).setOnClickListener(v -> showBottomSheetLogoutDialog());
        findViewById(R.id.llBack).setVisibility(View.GONE);
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Search Doctor");

        rvDoctor.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
                return false;
            }
        });
    }

    @Override
    public void viewClick() {
        edtArea.setOnClickListener(this);
        edtCity.setOnClickListener(this);
        edtDistrict.setOnClickListener(this);
        edtSpeciality.setOnClickListener(this);
        edtDoctorName.setOnClickListener(this);
        tvSearchDr.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edtDistrict:
                showListDialog(DISTRICT);
                break;
            case R.id.edtCity:
                showListDialog(CITY);
                break;
            case R.id.edtArea:
                showListDialog(AREA);
                break;
            case R.id.edtSpeciality:
                showListDialog(SPECIALITY);
                break;
            case R.id.tvSearchDr:
                if(sessionManager.isNetworkAvailable())
                {
                    if(edtDoctorName.getText().toString().length()==0)
                    {
                        AppUtils.showToast(activity,"Please enter doctor name for search");
                    }
                    else if(edtDoctorName.getText().toString().length()<3)
                    {
                        AppUtils.showToast(activity,"Please enter minimum 3 character for search doctor name.");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(edtDoctorName,activity);
                        searchDoctor(edtDoctorName.getText().toString().trim());
                    }
                }
                else
                {
                    AppUtils.noInternetSnakeBar(activity);
                }
                break;
            case R.id.tvCancel:

                edtDistrict.setText("");
                districtId = "";

                listCity.clear();
                edtCity.setText("");
                cityId = "";

                listArea.clear();
                edtArea.setText("");
                areaId = "";

                listSpeciality.clear();
                edtSpeciality.setText("");
                specialityId = "";

                edtDoctorName.setText("");

                listDoctor.clear();
                if(doctorAdapter!=null)
                {
                    doctorAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    public void getDataFromServer()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<DistrictResponse> call = apiService.getDistrict("","","1","");
        call.enqueue(new Callback<DistrictResponse>() {
            @Override
            public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        listDistrict = response.body().getDistricts();

                        DistrictResponse.DistrictsBean bean = new DistrictResponse.DistrictsBean();
                        bean.setDistrict("Select District");
                        listDistrict.add(0,bean);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No District Found");
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DistrictResponse> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                llLoading.setVisibility(View.GONE);
            }
        });

        getCityFromDistrict();

        getAreaFromCity();

        getSpecialityFromArea();
    }

    private void getCityFromDistrict()
    {
        listCity.clear();
        llLoading.setVisibility(View.VISIBLE);
        Call<CityResponse> call = apiService.getCityFromDistrict(districtId,"","1","");
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        listCity = response.body().getCities();

                        CityResponse.CitiesBean bean = new CityResponse.CitiesBean();
                        bean.setName("Select City");
                        listCity.add(0,bean);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No City Found");
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getAreaFromCity()
    {
        listArea.clear();
        llLoading.setVisibility(View.VISIBLE);
        Call<AreaResponse> call = apiService.getAreaFromCity(cityId,"","1","");
        call.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        listArea = response.body().getAreas();

                        AreaResponse.AreasBean bean = new AreaResponse.AreasBean();
                        bean.setArea("Select Area");
                        listArea.add(0,bean);
                    }
                    else
                    {
                        AppUtils.showToast(activity,"No Area Found");
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getSpecialityFromArea() {
        llLoading.setVisibility(View.VISIBLE);
        listSpeciality.clear();
        Call<SpecialistBean> loginCall = apiService.getSpecialityFromArea("500", areaId, "");
        loginCall.enqueue(new Callback<SpecialistBean>() {
            @Override
            public void onResponse(Call<SpecialistBean> call, Response<SpecialistBean> response)
            {
                if(response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listSpeciality = response.body().getSpeciality();

                        SpecialistBean.SpecialityBean bean = new SpecialistBean.SpecialityBean();
                        bean.setSpeciality("Select Speciality");
                        listSpeciality.add(0,bean);

                    } else {
                        AppUtils.showToast(activity, response.body().getMessage());
                    }
                }
                else
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                llLoading.setVisibility(View.GONE);
            }
        });

    }

    public void showListDialog(final String isFor)
    {
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
                        if (textlength <= listArea.get(i).getArea().length())
                        {
                            if (listArea.get(i).getArea().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
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
                            if (listSpeciality.get(i).getSpeciality().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listSpeciality.get(i).getSpeciality_code().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()) ||
                                    listSpeciality.get(i).getSpeciality_id().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listSpecialitySearch.add(listSpeciality.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(CITY))
                {
                    listCitySearch.clear();
                    for (int i = 0; i < listCity.size(); i++)
                    {
                        if (textlength <= listCity.get(i).getName().length())
                        {
                            if (listCity.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listCitySearch.add(listCity.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(DISTRICT))
                {
                    listDistrictSearch.clear();
                    for (int i = 0; i < listDistrict.size(); i++)
                    {
                        if (textlength <= listDistrict.get(i).getDistrict().length())
                        {
                            if (listDistrict.get(i).getDistrict().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listDistrictSearch.add(listDistrict.get(i));
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

    private void AppendListForArea(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea)
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
                final AreaResponse.AreasBean getSet;
                if(isForSearch)
                {
                    getSet = listAreaSearch.get(position);
                }
                else
                {
                    getSet = listArea.get(position);
                }

                holder.tvValue.setText(getSet.getArea());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            areaId = getSet.getArea_id();
                            edtArea.setText(getSet.getArea());

                            edtSpeciality.setText("");
                            specialityId = "";

                            if(edtDoctorName.getText().toString().length()>0)
                            {
                                searchDoctor(edtDoctorName.getText().toString().trim());
                            }

                            getSpecialityFromArea();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(CITY))
            {
                final CityResponse.CitiesBean getSet;
                if(isForSearch)
                {
                    getSet = listCitySearch.get(position);
                }
                else
                {
                    getSet = listCity.get(position);
                }

                holder.tvValue.setText(getSet.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            cityId = getSet.getCity_id();
                            edtCity.setText(getSet.getName());

                            edtArea.setText("");
                            areaId = "";

                            edtSpeciality.setText("");
                            specialityId = "";

                            if(edtDoctorName.getText().toString().length()>0)
                            {
                                searchDoctor(edtDoctorName.getText().toString().trim());
                            }

                            getAreaFromCity();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(DISTRICT))
            {
                final DistrictResponse.DistrictsBean getSet;
                if(isForSearch)
                {
                    getSet = listDistrictSearch.get(position);
                }
                else
                {
                    getSet = listDistrict.get(position);
                }

                holder.tvValue.setText(getSet.getDistrict());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            dialog.dismiss();
                            districtId = getSet.getDisctrict_id();
                            edtDistrict.setText(getSet.getDistrict());

                            edtCity.setText("");
                            cityId = "";

                            edtArea.setText("");
                            areaId = "";

                            edtSpeciality.setText("");
                            specialityId = "";

                            getCityFromDistrict();
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

                            if(edtDoctorName.getText().toString().length()>0)
                            {
                                searchDoctor(edtDoctorName.getText().toString().trim());
                            }
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
            else if(isFor.equalsIgnoreCase(CITY))
            {
                if(isForSearch)
                {
                    return listCitySearch.size();
                }
                else
                {
                    return listCity.size();
                }
            }
            else if(isFor.equalsIgnoreCase(DISTRICT))
            {
                if(isForSearch)
                {
                    return listDistrictSearch.size();
                }
                else
                {
                    return listDistrict.size();
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
            else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvValue,tvId;
            private View viewLine;
            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }

    private void searchDoctor(final String doctorName)
    {
        listDoctor.clear();
        llLoading.setVisibility(View.VISIBLE);
        Call<SearchDoctorResponse> call = apiService.searchedDoctor(doctorName,specialityId,areaId,cityId);
        call.enqueue(new Callback<SearchDoctorResponse>() {
            @Override
            public void onResponse(Call<SearchDoctorResponse> call, Response<SearchDoctorResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                        listDoctor = response.body().getDoctors(); 
                        
                        if(listDoctor.size()>0)
                        {
                            scrollView.fullScroll(View.FOCUS_UP);

                            tvNoDr.setVisibility(View.GONE);
                            doctorAdapter = new DoctorAdapter(listDoctor);
                            rvDoctor.setAdapter(doctorAdapter);
                        }
                        else 
                        {
                            if(doctorAdapter!=null)
                            {
                                doctorAdapter.notifyDataSetChanged();
                            }
                            tvNoDr.setVisibility(View.VISIBLE);
                        }
                    }
                    else 
                    {
                        if(doctorAdapter!=null)
                        {
                            doctorAdapter.notifyDataSetChanged();
                        }
                        tvNoDr.setVisibility(View.VISIBLE);
                        AppUtils.showToast(activity,"No doctor found");    
                    }
                }
                else 
                {
                    AppUtils.apiFailedToast(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchDoctorResponse> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>
    {

        List<SearchDoctorResponse.DoctorsBean> listItems;

        DoctorAdapter(List<SearchDoctorResponse.DoctorsBean> list) {
            this.listItems = list;
        }

        @Override
        public DoctorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_search_doctor, viewGroup, false);
            return new DoctorAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DoctorAdapter.ViewHolder holder, final int position)
        {
            final SearchDoctorResponse.DoctorsBean bean = listItems.get(position);

            if (position == 0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }

            holder.tvSrNo.setText(String.valueOf(position+1));
            holder.tvName.setText(bean.getDoctor());
            holder.tvSpeciality.setText(bean.getSpeciality());
            holder.tvArea.setText(bean.getArea());
            holder.tvCity.setText(bean.getCity());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,DoctorDetailsActivity.class);
                    intent.putExtra("doctor_id",bean.getDoctor_id());
                    activity.startActivity(intent);
                }
            });
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
            @BindView(R.id.tvName)TextView tvName;
            @BindView(R.id.tvSpeciality)TextView tvSpeciality;
            @BindView(R.id.tvArea)TextView tvArea;
            @BindView(R.id.tvCity)TextView tvCity;
            @BindView(R.id.tvDist)TextView tvDist;
            @BindView(R.id.llValue)LinearLayout llValue;
            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }

            @Override
            public void onClick(View v)
            {
            }
        }
    }

    private void showBottomSheetLogoutDialog()
    {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
        dialog.setContentView(sheetView);

        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                AppUtils.showToast(activity, "You have successfully logged out.");
                Intent i = new Intent(activity, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
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
