package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.ViewDCREntryActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.views.BottomSheetListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDCR extends Fragment
{
	public static Activity activity;
	View rootView;

	private LinearLayout llLoading;
	public static EditText edtDate,edtEmployee;
	LinearLayout llGenerateReport;
	ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
	private ApiInterface apiService;
	public static String entrydate = "";
	private String selectedStaffId = "";
	
	SessionManager sessionManager;

	private boolean isLoading = false;

	public static DatePickerDialog datepicker;

	private long mLastClickTime = 0;

	String date_time = "";
	int mYear;
	int mMonth;
	int mDay;

	String selectedDate = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.dcr, container, false);
		
		activity = getActivity();
		sessionManager = new SessionManager(activity);

		apiService = ApiClient.getClient().create(ApiInterface.class);

		llLoading = (LinearLayout) rootView.findViewById(R.id.llLoading);
		edtDate = (EditText) rootView.findViewById(R.id.edtDate_DCR);
		edtEmployee = (EditText) rootView.findViewById(R.id.edtEmployee_DCR);


		llGenerateReport = (LinearLayout) rootView.findViewById(R.id.llGenerateReport);

		if (sessionManager.isNetworkAvailable())
		{
			getEmployeeList();
		}
		else
		{
			AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
		}

		onClickListener();
		
		return rootView;
	}
	
	private void onClickListener() 
	{
		edtEmployee.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();

				if (sessionManager.isNetworkAvailable())
				{
					if(isLoading)
					{
						AppUtils.showLoadingToast(activity);
					}
					else
					{
						try
						{
							showDetailsDialog();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
				}
			}
		});
	
		edtDate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				/*DialogFragment newFragment = new SelectDateFragment();
				newFragment.show(getFragmentManager(), "DatePicker");*/

				datePicker(edtDate);
			}
		});

		llGenerateReport.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				if (sessionManager.isNetworkAvailable())
				{
					if (edtEmployee.getText().toString().length() < 1) 
					{
						edtEmployee.setError("Please Select Employee");
					}
					else if (edtDate.getText().toString().length() < 1) 
					{
						edtDate.setError("Please Select Date");
					}
					else
					{
						Intent intent = new Intent(activity,ViewDCREntryActivity.class);
						intent.putExtra("date", edtDate.getText().toString());
						intent.putExtra("id",selectedStaffId);
						startActivity(intent);
						AppUtils.startActivityAnimation(activity);
					}
				}
				else
				{
					AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
				}	
			}
		});
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


	public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
		    
			datepicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);
			
			try 
			{
				datepicker.getDatePicker().setMaxDate(new Date().getTime());
				return datepicker;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}
		
		public void onDateSet(DatePicker view, int yy, int mm, int dd)
		{
			populateSetDate(yy, mm + 1, dd);
		}
	}
	
	public static void populateSetDate(int year, int month, int day)
	{
		String date = month + "/" + day + "/" + year;
		try
		{
//			Selected Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
			Date dtSelected = dateFormat.parse(date);
			SimpleDateFormat formatNeeded = new SimpleDateFormat("yyyy-MM-dd");
			date = formatNeeded.format(dtSelected);
			
//			Current Date
			Date d=new Date();
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
			Date dtCurrent = dateFormat2.parse(d.toString());
			
//			Comparison of both dates
			if (dtSelected.before(dtCurrent))
			{
				edtDate.setText(date);
				
				entrydate = date;
			}
			else
			{
				edtDate.setText("");
				
				entrydate = "";
				
				Toast toast = Toast.makeText(activity, "Please Select Proper Date", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER,0, 0);
				toast.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	ArrayList<StaffResponse.StaffBean> listEmpSearch = new ArrayList<>();
	DialogListAdapter listAdapter;
	protected void showDetailsDialog() 
	{
		try {
			/*LayoutInflater layoutInflater = LayoutInflater.from(activity);
			View promptView = layoutInflater.inflate(R.layout.dialog_listview, null);

			final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(promptView);*/


			final Dialog dialog= new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_listview, null);
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
			ViewHolder holder = null;
			
	        View rowView = convertView;
	        if(convertView == null)
	        {
	        	try
	    		{
	        		rowView = inflater.inflate(R.layout.rowview_mkt_code, null);
	        		
	        		holder = new ViewHolder();
	        		
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
		        holder = (ViewHolder) rowView.getTag();
		    }
	        
	        holder.txtmktCode.setText(items.get(position).getName());
	        
	        rowView.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
					selectedStaffId = items.get(position).getStaff_id();
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
}
