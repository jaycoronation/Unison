package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.DoctorDetailsResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailsActivity extends BaseClass implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.llNoData)LinearLayout llNoData;
    @BindView(R.id.rvDivision)
    RecyclerView rvDivision;
    @BindView(R.id.tvDrName)TextView tvDrName;
    @BindView(R.id.tvSpeciality)TextView tvSpeciality;
    @BindView(R.id.tvBusiness)TextView tvBusiness;
    @BindView(R.id.tvDivisionTitle)TextView tvDivisionTitle;
    @BindView(R.id.tvAddress)TextView tvAddress;

    private String doctor_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(activity);
        doctor_id = getIntent().getStringExtra("doctor_id");
        basicProcesses();
    }

    @Override
    public void initViews() {
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llBack).setVisibility(View.VISIBLE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                AppUtils.finishActivityAnimation(activity);
            }
        });
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Doctor Details");

        rvDivision.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void viewClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getDataFromServer()
    {
        if(sessionManager.isNetworkAvailable())
        {
            llLoading.setVisibility(View.VISIBLE);
            Call<DoctorDetailsResponse> call = apiService.getSearchedDrDetails(doctor_id);
            call.enqueue(new Callback<DoctorDetailsResponse>() {
                @Override
                public void onResponse(Call<DoctorDetailsResponse> call, Response<DoctorDetailsResponse> response) {
                    if(response.isSuccessful())
                    {
                        tvDrName.setText(response.body().getDoctor().getName());
                        tvBusiness.setText(String.valueOf(response.body().getDoctor().getBusiness()));
                        tvSpeciality.setText(response.body().getDoctor().getSpeciality()+"("+response.body().getDoctor().getQualification()+")");
                        String address = "";

                        try {
                            if(!response.body().getDoctor().getAddress_1().equals(""))
                            {
                                address = response.body().getDoctor().getAddress_1()+"\n";
                            }

                            if(!response.body().getDoctor().getAddress_1().equals(""))
                            {
                                address = address + response.body().getDoctor().getAddress_2()+"\n";
                            }

                            if(!response.body().getDoctor().getAddress_3().equals(""))
                            {
                                address = address + response.body().getDoctor().getAddress_3()+"\n";
                            }

                            if(!response.body().getDoctor().getAddress_4().equals(""))
                            {
                                address = address + response.body().getDoctor().getAddress_4()+"\n";
                            }

                            address = address + response.body().getDoctor().getCity()+".";

                            tvAddress.setText(address);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        List<DoctorDetailsResponse.DivisionsBean> listDivision = response.body().getDivisions();
                        if(listDivision.size()>0)
                        {
                            tvDivisionTitle.setVisibility(View.VISIBLE);
                            DivisionAdapter divisionAdapter = new DivisionAdapter(listDivision);
                            rvDivision.setAdapter(divisionAdapter);
                        }
                        else
                        {
                            tvDivisionTitle.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedToast(activity);
                    }
                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<DoctorDetailsResponse> call, Throwable t) {
                    AppUtils.apiFailedToast(activity);
                    llLoading.setVisibility(View.GONE);
                    llNoData.setVisibility(View.GONE);
                }
            });
        }
    }

    public class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.ViewHolder>
    {

        List<DoctorDetailsResponse.DivisionsBean> listItems;

        DivisionAdapter(List<DoctorDetailsResponse.DivisionsBean> list) {
            this.listItems = list;
        }

        @Override
        public DivisionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_doctor_details, viewGroup, false);
            return new DivisionAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DivisionAdapter.ViewHolder holder, final int position)
        {
            DoctorDetailsResponse.DivisionsBean bean = listItems.get(position);
            holder.tvDivision.setText(bean.getDivision());
            holder.tvEmployee.setText(bean.getEmployee());
            holder.tvBusiness.setText(bean.getBusiness());
            holder.tvLastVisited.setText(AppUtils.universalDateConvert(bean.getLast_visit(),"dd/MM/yyyy","dd MMM, yyyy"));

            if(bean.getSamples().size()==0 && bean.getSales().size()==0)
            {
                holder.view1.setVisibility(View.GONE);
                holder.view2.setVisibility(View.GONE);
            }
            else
            {
                holder.view1.setVisibility(View.VISIBLE);
                holder.view2.setVisibility(View.VISIBLE);
            }


            if(bean.getSamples().size()>0)
            {
                holder.llSample.setVisibility(View.VISIBLE);
                SampleAdapter sampleAdapter = new SampleAdapter(bean.getSamples());
                holder.rvSample.setAdapter(sampleAdapter);
            }
            else
            {
                holder.llSample.setVisibility(View.GONE);
            }

            if(bean.getSales().size()>0)
            {
                holder.llSales.setVisibility(View.VISIBLE);
                SalesAdapter salesAdapter= new SalesAdapter(bean.getSales());
                holder.rvSales.setAdapter(salesAdapter);
            }
            else
            {
                holder.llSales.setVisibility(View.GONE);
            }

        }
        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            @BindView(R.id.tvDivision)TextView tvDivision;
            @BindView(R.id.tvEmployee)TextView tvEmployee;
            @BindView(R.id.tvBusiness)TextView tvBusiness;
            @BindView(R.id.tvLastVisited)TextView tvLastVisited;

            @BindView(R.id.llSample)LinearLayout llSample;
            @BindView(R.id.llSales)LinearLayout llSales;

            @BindView(R.id.rvSample)RecyclerView rvSample;
            @BindView(R.id.rvSales)RecyclerView rvSales;

            @BindView(R.id.view1)View view1;
            @BindView(R.id.view2)View view2;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);

                rvSales.setLayoutManager(new LinearLayoutManager(activity));
                rvSample.setLayoutManager(new LinearLayoutManager(activity));
            }

            @Override
            public void onClick(View v)
            {
            }
        }
    }


    public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder>
    {

        List<DoctorDetailsResponse.DivisionsBean.SamplesBean> listItems;

        SampleAdapter(List<DoctorDetailsResponse.DivisionsBean.SamplesBean> list) {
            this.listItems = list;
        }

        @Override
        public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_sample_details, viewGroup, false);
            return new SampleAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SampleAdapter.ViewHolder holder, final int position)
        {
            DoctorDetailsResponse.DivisionsBean.SamplesBean bean = listItems.get(position);
            if(position == listItems.size()-1)
            {
                holder.viewBottom.setVisibility(View.GONE);
            }
            else
            {
                holder.viewBottom.setVisibility(View.VISIBLE);
            }
            holder.tvItem.setText(bean.getItem());
            holder.tvQty.setText(bean.getQuantity());
        }
        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            @BindView(R.id.tvItem)TextView tvItem;
            @BindView(R.id.tvQty)TextView tvQty;
            @BindView(R.id.viewBottom)View viewBottom;

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

    public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder>
    {

        List<DoctorDetailsResponse.DivisionsBean.SalesBean> listItems;

        SalesAdapter(List<DoctorDetailsResponse.DivisionsBean.SalesBean> list) {
            this.listItems = list;
        }

        @Override
        public SalesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_sample_details, viewGroup, false);
            return new SalesAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SalesAdapter.ViewHolder holder, final int position)
        {
            DoctorDetailsResponse.DivisionsBean.SalesBean bean = listItems.get(position);
            holder.tvItem.setText(bean.getItem());
            holder.tvQty.setText(bean.getQuantity());
            if(position == listItems.size()-1)
            {
                holder.viewBottom.setVisibility(View.GONE);
            }
            else
            {
                holder.viewBottom.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            @BindView(R.id.tvItem)TextView tvItem;
            @BindView(R.id.tvQty)TextView tvQty;
            @BindView(R.id.viewBottom)View viewBottom;

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
}
