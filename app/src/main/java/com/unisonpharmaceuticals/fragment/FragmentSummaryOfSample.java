package com.unisonpharmaceuticals.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSummaryOfSample extends Fragment implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;
    @BindView(R.id.tvGenerateReport)
    TextView tvGenerateReport;
    @BindView(R.id.tvCancel)TextView tvCancel;

    public static EditText edtStartDate,edtEndDate;

    //DatePicker
    private Calendar calendar;
    public static int year, month, day;
    public  static boolean flagDate = false ;
    public static String strStartDate = "" , strEndDate = "" ;
    public static long startDateTimeStamp = 0;

    private Dialog listDialog;
    private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
    private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();
    private ArrayList<String> listMonth = new ArrayList<>();
    private final String START_DATE = "StartDate";
    private final String END_DATE = "EndDate";
    private final String EMPLOYEE = "employee";

    private String selectedStaffId = "";

    private boolean isLoading = false;

    private long mLastClickTime = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
		rootView = inflater.inflate(R.layout.fragment_sample_summary, container, false);

		ButterKnife.bind(this,rootView);
		apiService = ApiClient.getClient().create(ApiInterface.class);
		activity = getActivity();
		sessionManager = new SessionManager(activity);
        initViews();

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


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
        edtStartDate = (EditText) rootView.findViewById(R.id.edtStartDate);
        edtEndDate = (EditText) rootView.findViewById(R.id.edtEndDate);
        edtEmployee.setOnClickListener(this);
        edtStartDate.setOnClickListener(this);
        edtEndDate.setOnClickListener(this);
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
            case R.id.edtStartDate:
                flagDate = false ;
                showDatePickerDialog();
                break;
            case R.id.edtEndDate:
                flagDate = true ;
                showDatePickerDialog();
                break;
            case R.id.tvGenerateReport:
                if(selectedStaffId.equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select employee");
                }
                else if(edtStartDate.getText().toString().equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select start date");
                }
                else if(edtEndDate.getText().toString().equalsIgnoreCase(""))
                {
                    AppUtils.showToast(activity,"Please select end date");
                }
                else
                {
                    String url = ApiClient.SAMPLE_SUMMARY_REPORT + "staff_id="+selectedStaffId+"&fromDate="+AppUtils.universalDateConvert(edtStartDate.getText().toString().trim(),"dd-MM-yyyy","yyyy-MM-dd")
                            +"&toDate="+AppUtils.universalDateConvert(edtEndDate.getText().toString().trim(),"dd-MM-yyyy","yyyy-MM-dd")+"&session_id="+sessionManager.getUserId();
                    Intent intent = new Intent(activity,WebViewActivity.class);
                    intent.putExtra("cameFrom",ApiClient.REPORT_SAMPLE);
                    intent.putExtra("report_url",url);
                    startActivity(intent);
                }
                break;
            case R.id.tvCancel:
                edtStartDate.setText("");
                edtEndDate.setText("");
                edtEmployee.setText("");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<StaffResponse> call, Throwable t)
            {
                isLoading = false;
                AppUtils.showToast(activity, "Could not get employee list.");
                activity.finish();
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
                        edtEmployee.setText(getSet.getName());
                        selectedStaffId = getSet.getStaff_id();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }
        }

        @Override
        public int getItemCount()
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

    private void AppendList(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea)
    {
        areaAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    private void showDatePickerDialog()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            DatePickerDialog datepicker = new DatePickerDialog(getActivity(), this, year, month, day);

            if(flagDate)//End Date
            {
                datepicker.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datepicker.getDatePicker().setMinDate(startDateTimeStamp);
            }
            else
            {
                datepicker.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            }

            datepicker.setOnKeyListener(new DialogInterface.OnKeyListener()
            {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

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
            SimpleDateFormat tempsdf = new SimpleDateFormat("d-M-yyyy");
            Date d;
            try
            {
                d = sdf.parse(str);
                sdf.applyPattern("yyyy-MM-dd");
                tempsdf.applyPattern("dd-MM-yyyy");
                if(flagDate)
                {
                    if(validateDateRange(edtStartDate.getText().toString(), sdf.format(d)))
                    {
                        strEndDate = sdf.format(d);
                        edtEndDate.setText(tempsdf.format(d));
                    }
                    else
                    {
                        //Toast.makeText(getActivity(), "You end date is not before then start date.", Toast.LENGTH_SHORT).show();
                        AppUtils.showToast(getActivity(),"Please select start date first.");
                    }
                }
                else
                {
                    startDateTimeStamp = d.getTime();
                    strStartDate = sdf.format(d);
                    edtStartDate.setText(tempsdf.format(d));
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            catch (java.text.ParseException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog)
        {
            super.onCancel(dialog);
        }
    }

    public static boolean validateDateRange(String startDate, String endDate)
    {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

        boolean b = false;

        try
        {
            try
            {
                if (dfDate.parse(startDate).before(dfDate.parse(endDate)))
                {
                    b = true;  // If start date is before end date.
                }
                else if (dfDate.parse(startDate).equals(dfDate.parse(endDate)))
                {
                    b = true;  // If two dates are equal.
                }
                else
                {
                    b = false; // If start date is after the end date.
                }
            }
            catch (java.text.ParseException e)
            {
                e.printStackTrace();
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return b;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startDateTimeStamp = 0;
    }
}
