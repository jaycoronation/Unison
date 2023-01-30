package com.unisonpharmaceuticals.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.WebViewActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.views.BottomSheetListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPlannerReport extends Fragment implements View.OnClickListener
{
    Activity activity ;
    SessionManager sessionManager;
    View rootView ;

    private ApiInterface apiService;

    @BindView(R.id.llLoading) LinearLayout llLoading;
    @BindView(R.id.edtEmployee)
    EditText edtEmployee;

    @BindView(R.id.edtPlanDate)
    EditText edtPlanDate;

    @BindView(R.id.tvGenerateReport)
    TextView tvGenerateReport;

    private Calendar calendar;
    public static int year, month, day;
    public static long selectedTime = 0L;
    private String selectedStaffID = "";

    ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();

    private boolean isLoading = false;

    private long mLastClickTime = 0;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
		rootView = inflater.inflate(R.layout.fragment_planner_report, container, false);
		ButterKnife.bind(this,rootView);
		activity = getActivity();
		sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
		
		initView();
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

    private void initView() {
        edtEmployee.setOnClickListener(this);
        edtPlanDate.setOnClickListener(this);
        tvGenerateReport.setOnClickListener(this);
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
                    showDetailsDialog();
                }
                break;
            case R.id.edtPlanDate:
                showDatePickerDialog();
                break;
            case R.id.tvGenerateReport:
                if(sessionManager.isNetworkAvailable())
                {
                    if(edtPlanDate.getText().toString().equals(""))
                    {
                        AppUtils.showToast(activity,"Please select date");
                    }
                    else
                    {
                        String url = ApiClient.PLANNER_ENTRY_REPORT + "staff_id="+selectedStaffID+"&date="+String.valueOf(selectedTime)+"&session_id="+sessionManager.getUserId();
                        Intent intent = new Intent(activity,WebViewActivity.class);
                        intent.putExtra("cameFrom",ApiClient.REPORT_PLANNER);
                        intent.putExtra("report_url",url);
                        startActivity(intent);
                    }
                }
                else
                {
                    AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                }
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
                            selectedStaffID = listEmployee.get(0).getStaff_id();
                            edtEmployee.setText(listEmployee.get(0).getName());
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
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
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
        llLoading.setVisibility(View.GONE);
    }
    

    ArrayList<StaffResponse.StaffBean> listEmpSearch = new ArrayList<>();
    DialogListAdapter listAdapter;
    protected void showDetailsDialog()
    {
        try
        {

            final Dialog dialog= new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_listview, null);
            dialog.setContentView(sheetView);

            dialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtils.hideKeyboard(sheetView,activity);
                }
            });
            final BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.lv_Dialog);
            TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader_Dialog_ListView);

            final TextInputLayout inputSearch = (TextInputLayout) dialog.findViewById(R.id.inputSearch);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch_Dialog_ListView);

            try
            {
                txtHeader.setText("Select Employee");
                listAdapter = new DialogListAdapter(activity, listEmployee,dialog);
                listView.setAdapter(listAdapter);

                if(listEmployee.size()>10)
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

                        listEmpSearch.clear();
                        for (int i = 0; i < listEmployee.size(); i++)
                        {
                            if (listEmployee.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                listEmpSearch.add(listEmployee.get(i));
                            }
                        }

                        listAdapter = new DialogListAdapter(activity, listEmpSearch,dialog);
                        listView.setAdapter(listAdapter);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DialogListAdapter extends BaseAdapter
    {
        private Activity activity;
        private LayoutInflater inflater=null;
        ArrayList<StaffResponse.StaffBean> items;
        Dialog dialog;

        public DialogListAdapter(Activity a, ArrayList<StaffResponse.StaffBean> item, Dialog dialog)
        {
            this.activity = a;
            this.items = item;
            this.dialog = dialog;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount()
        {
            return items.size();
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            DialogListAdapter.ViewHolder holder = null;

            View rowView = convertView;
            if(convertView == null)
            {
                try
                {
                    rowView = inflater.inflate(R.layout.rowview_mkt_code, null);

                    holder = new DialogListAdapter.ViewHolder();

                    holder.txtmktCode = (TextView)rowView.findViewById(R.id.txtName);

                    rowView.setTag(holder);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                holder = (DialogListAdapter.ViewHolder) rowView.getTag();
            }

            holder.txtmktCode.setText(items.get(position).getName());

            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    selectedStaffID = items.get(position).getStaff_id();
                    edtEmployee.setText(items.get(position).getName());
                }
            });

            return rowView;
        }

        private class ViewHolder
        {
            TextView txtmktCode;
        }
    }

    private void showDatePickerDialog()
    {
        android.app.DialogFragment newFragment = new DatePickerFragment(edtPlanDate);
        newFragment.show(activity.getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends android.app.DialogFragment implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener
    {
        private EditText editText;
        @SuppressLint("ValidFragment")
        public DatePickerFragment(EditText editText)
        {
            this.editText = editText;
        }
        public DatePickerFragment()
        {
         
        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            DatePickerDialog datepicker = new DatePickerDialog(getActivity(), this, year, month, day);
            //datepicker.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
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
                editText.setText(sdf.format(d));
                selectedTime = d.getTime()/1000;
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
}
