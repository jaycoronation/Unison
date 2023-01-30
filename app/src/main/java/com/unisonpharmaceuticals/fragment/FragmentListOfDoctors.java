package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.unisonpharmaceuticals.activity.WebViewActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.SpecialistBean;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListOfDoctors extends Fragment implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;

    private ApiInterface apiService;
    
    private View rootView;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.edtSpeciality)
    EditText edtSpeciality;
    @BindView(R.id.tvGenerateReport)
    TextView tvGenerateReport;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

    private ArrayList<SpecialistBean.SpecialityBean> listSpeciality = new ArrayList<>();
    private ArrayList<SpecialistBean.SpecialityBean> listSpecialitySearch = new ArrayList<>();

    private final String EMPLOYEE = "employee";
    private final String SPECIALITY = "speciality";

    private String selectedStaffId="",selectedSpeciality="";

    private boolean isLoading = false;

    private long mLastClickTime = 0;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
		rootView = inflater.inflate(R.layout.fragment_dr_list, container, false);

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
        edtEmployee.setOnClickListener(this);
        edtSpeciality.setOnClickListener(this);
        tvGenerateReport.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
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

            case R.id.tvGenerateReport:
                if(selectedStaffId.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(edtSpeciality.getText().toString().trim().equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select speciality");
                }
                else
                {
                    String url = ApiClient.DR_LIST_REPORT + "staff_id="+selectedStaffId+"&spec="+selectedSpeciality+"&session_id="+sessionManager.getUserId();
                    Intent intent = new Intent(activity,WebViewActivity.class);
                    intent.putExtra("cameFrom",ApiClient.REPORT_DR_LIST);
                    intent.putExtra("report_url",url);
                    startActivity(intent);
                }
                break;
            case R.id.tvCancel:
                edtSpeciality.setText("");
                edtEmployee.setText("");
                selectedSpeciality= "";
                selectedStaffId = "";
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
                isLoading =  false;
            }

            @Override
            public void onFailure(Call<SpecialistBean> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });

        llLoading.setVisibility(View.GONE);
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
                else if(isFor.equals(SPECIALITY))
                {
                    listSpecialitySearch.clear();
                    for (int i = 0; i < listSpeciality.size(); i++)
                    {
                        if (textlength <= listSpeciality.get(i).getSpeciality().length())
                        {
                            if (listSpeciality.get(i).getSpeciality().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())
                                    || listSpeciality.get(i).getSpeciality_code().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim())
                                    || listSpeciality.get(i).getSpeciality_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listSpecialitySearch.add(listSpeciality.get(i));
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
                        selectedStaffId = getSet.getStaff_id();
                        edtEmployee.setText(getSet.getName());
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
            else if(isFor.equalsIgnoreCase(SPECIALITY))
            {
                holder.cb.setVisibility(View.GONE);
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        selectedSpeciality = getSet.getSpeciality_id();
                        edtSpeciality.setText(getSet.getSpeciality());
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

}
