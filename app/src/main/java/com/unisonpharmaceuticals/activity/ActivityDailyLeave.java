package com.unisonpharmaceuticals.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.LeaveResponse;
import com.unisonpharmaceuticals.model.LeaveTypePojo;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDailyLeave extends BaseClass
{
	private Activity activity;
	private SessionManager sessionManager;

	private TextView txtCasualTotal , txtCasualUsed , txtCasualPending , txtPrivilegeTotal , txtPrivilegeUsed , txtPrivilegePending , txtMedicalTotal , txtMedicalUsed , txtMedicalPending ,
			txtLWPTotal , txtLWPUsed , txtLWPPending ;

	//Apply for leave
	public static EditText edtStartDate , edtEndDate , edtReason , edtLeaveType ;

	private LinearLayout btnApply,llLoading;

	private ApiInterface apiService;

	public ProgressDialog pd ;

	public static String strCasualTotal = "" , strCasualUsed = "" , strCasualPending = "" , strPrivilegeTotal = "" , strPrivilegeUsed = "" , strPrivilegePending = "" ,
			strMedicalTotal = "" , strMedicalUsed = "" , strMedicalPending = "" , strLWPTotal = "" , strLWPUsed = "" , strLWPPending = "" , strReason = "" ,
			strLeaveId = "" , strLeaveType = "" , strStatus = "" ;

	//Calendar

	public static String strStartDate = "" , strEndDate = "" ;

	//DatePicker
	private Calendar calendar;
	public static int year, month, day;
	public  static boolean flagDate = false ;

	private Dialog listDialog;
	private ArrayList<String> listLeaveType = new ArrayList<>();

	private long mLastClickTime = 0;

	private boolean isAreaFound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_leave);
		activity = this;
		DashboardActivity.isAppRunning = true;
		sessionManager = new SessionManager(activity);
		apiService = ApiClient.getClient().create(ApiInterface.class);
		basicProcesses();
		calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		if (sessionManager.isNetworkAvailable())
		{
			getLeavesReport();
		}
		else
		{
			AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
		}
	}

	@Override
	public void initViews() {
		findViewById(R.id.llNotification).setVisibility(View.GONE);
		findViewById(R.id.llLogout).setVisibility(View.GONE);
		findViewById(R.id.llBack).setVisibility(View.GONE);
		findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
				finishActivityAnimation(activity);
			}
		});
		TextView txtTitle = findViewById(R.id.txtTitle);
		txtTitle.setText("Leave");

		llLoading = (LinearLayout) findViewById(R.id.llLoading);

		txtCasualTotal = (TextView) findViewById(R.id.txtCasualTotal);
		txtCasualUsed = (TextView) findViewById(R.id.txtCasualUsed);
		txtCasualPending = (TextView) findViewById(R.id.txtCasualPending);

		txtPrivilegeTotal = (TextView) findViewById(R.id.txtPrivilegeTotal);
		txtPrivilegeUsed = (TextView) findViewById(R.id.txtPrivilegeUsed);
		txtPrivilegePending = (TextView) findViewById(R.id.txtPrivilegePending);

		txtMedicalTotal = (TextView) findViewById(R.id.txtMedicalTotal);
		txtMedicalUsed = (TextView) findViewById(R.id.txtMedicalUsed);
		txtMedicalPending = (TextView) findViewById(R.id.txtMedicalPending);

		txtLWPTotal = (TextView) findViewById(R.id.txtLWPTotal);
		txtLWPUsed = (TextView) findViewById(R.id.txtLWPUsed);
		txtLWPPending = (TextView) findViewById(R.id.txtLWPPending);

		//Apply For Leave
		edtStartDate = (EditText) findViewById(R.id.edtStartDate);
		edtEndDate = (EditText) findViewById(R.id.edtEndDate);
		edtReason = (EditText) findViewById(R.id.edtReason);
		edtLeaveType = (EditText) findViewById(R.id.edtLeaveType);

		edtStartDate.setInputType(InputType.TYPE_NULL);
		edtEndDate.setInputType(InputType.TYPE_NULL);
		edtLeaveType.setInputType(InputType.TYPE_NULL);

		btnApply = (LinearLayout) findViewById(R.id.btnApply);

        /*listLeaveType.add("Casual Leave");
        listLeaveType.add("Privilege Leave");
        listLeaveType.add("Medical Leave");
        listLeaveType.add("Leave Without Pay");*/
	}

	@Override
	public void viewClick() {
		edtStartDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				flagDate = false ;
				showDatePickerDialog();
			}
		});

		edtEndDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				flagDate = true ;
				showDatePickerDialog();
			}
		});

		edtLeaveType.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				showListDialog("Leave Type");
			}
		});

		btnApply.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				submitForm();
			}
		});
	}

	@Override
	public void getDataFromServer() {

	}


	protected void submitForm()
	{
		strReason = edtReason.getText().toString().trim();
		strStartDate = edtStartDate.getText().toString().trim();
		strEndDate = edtEndDate.getText().toString().trim();

		Date dateStart = null;
		Date dateEnd = null;

		if(edtLeaveType.getText().toString().trim().length()==0)
		{
			AppUtils.showToast(activity,"Please select leave type.");
		}
		else if(strStartDate.length() == 0)
		{
			AppUtils.showToast(activity,"Please select start date.");
		}
		else if(strEndDate.length() == 0)
		{
			AppUtils.showToast(activity,"Please select end date.");
		}
		else if(strReason.length() == 0)
		{
			AppUtils.showToast(activity,"Please enter reason.");
		}
		else
		{

			if (sessionManager.isNetworkAvailable())
			{

				llLoading.setVisibility(View.VISIBLE);
				Call<CommonResponse> submitLeave = apiService.submitWorkType(sessionManager.getUserId(),
						"leave",
						strStartDate,
						strEndDate,
						edtReason.getText().toString().trim(),
						strLeaveType,
						"",
						sessionManager.getUserId());
				submitLeave.enqueue(new Callback<CommonResponse>() {
					@Override
					public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
					{
						Log.e("Response ?? ", "onResponse: "+ new Gson().toJson(response));
						if(response.body().getSuccess()==1)
						{
							AppUtils.showToast(activity,response.body().getMessage());
							activity.finish();
							startActivity(new Intent(activity,MainActivity.class));
							AppUtils.startActivityAnimation(activity);
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


				/*Call<CommonResponse> submitLeave = apiService.submitWorkType(sessionManager.getUserId(),
						"leave",
						strStartDate,
						strEndDate,
						edtReason.getText().toString().trim(),
						strLeaveType,
						"");
				submitLeave.enqueue(new Callback<CommonResponse>() {
					@Override
					public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
					{
						Log.e("Response ?? ", "onResponse: "+new Gson().toJson(response) );
						if(response.body().getSuccess()==1)
						{
							AppUtils.showToast(activity,response.body().getMessage());
							activity.finish();
							startActivity(new Intent(activity,MainActivity.class));
							AppUtils.startActivityAnimation(activity);
						}
						else
						{
							AppUtils.showToast(activity,response.body().getMessage());
						}
					}

					@Override
					public void onFailure(Call<CommonResponse> call, Throwable t) {

					}
				});*/
			}
			else
			{
				AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
			}
		}
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
			DatePickerDialog datepicker = new DatePickerDialog(getActivity(),R.style.DialogTheme,this, year, month, day);
			Calendar calendar = Calendar.getInstance();
			if(!flagDate)
			{
				/*Comment this because unison required all type of leave should be apply with back date*/
				/*Date today = calendar.getTime();
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				Date yesterday = calendar.getTime();
				if(!edtLeaveType.getText().toString().trim().equalsIgnoreCase("Privilege Leave") &&
						!edtLeaveType.getText().toString().trim().equalsIgnoreCase("Medical Leave"))
				{
					datepicker.getDatePicker().setMinDate(yesterday.getTime());
				}*/
			}
			else
			{
				if(edtLeaveType.getText().toString().trim().equalsIgnoreCase("Casual Leave"))
				{
					if(!strStartDate.equalsIgnoreCase(""))
					{
						int casualPending = Integer.parseInt(strCasualPending.toString());
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date minDate = sdf.parse(strStartDate);
							datepicker.getDatePicker().setMinDate(minDate.getTime());

							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(strStartDate));
							cal.add(Calendar.DAY_OF_YEAR,casualPending);
							Date maxDate = cal.getTime();
							Log.e("******** ", "onCreateDialog: "+maxDate.getTime() );
							datepicker.getDatePicker().setMaxDate(maxDate.getTime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else if(edtLeaveType.getText().toString().trim().equalsIgnoreCase("Privilege Leave"))
				{
					if(!strStartDate.equalsIgnoreCase(""))
					{
						int casualPending = Integer.parseInt(strPrivilegePending.toString());
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date minDate = sdf.parse(strStartDate);
							datepicker.getDatePicker().setMinDate(minDate.getTime());

							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(strStartDate));
							cal.add(Calendar.DAY_OF_YEAR,casualPending);
							Date maxDate = cal.getTime();
							Log.e("******** ", "onCreateDialog: "+maxDate.getTime() );
							datepicker.getDatePicker().setMaxDate(maxDate.getTime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else if(edtLeaveType.getText().toString().trim().equalsIgnoreCase("Medical Leave"))
				{
					if(!strStartDate.equalsIgnoreCase(""))
					{
						int casualPending = Integer.parseInt(strMedicalPending.toString());
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date minDate = sdf.parse(strStartDate);
							datepicker.getDatePicker().setMinDate(minDate.getTime());

							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(strStartDate));
							cal.add(Calendar.DAY_OF_YEAR,casualPending);
							Date maxDate = cal.getTime();
							Log.e("******** ", "onCreateDialog: "+maxDate.getTime() );
							datepicker.getDatePicker().setMaxDate(maxDate.getTime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else if(edtLeaveType.getText().toString().trim().equalsIgnoreCase("Leave Without Pay"))
				{
					if(!strStartDate.equalsIgnoreCase(""))
					{
						try
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date minDate = sdf.parse(strStartDate);
							datepicker.getDatePicker().setMinDate(minDate.getTime());

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
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
						//edtLeaveType.setText("");


						SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

						try {
							Date date1 = myFormat.parse(strStartDate);
							Date date2 = myFormat.parse(strEndDate);
							long diffLong = date2.getTime() - date1.getTime();
							int diffInt = Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diffLong, TimeUnit.MILLISECONDS)));
							if(diffInt>2)
							{
								edtLeaveType.setText("Privilege Leave");
								strLeaveType = "privilege_leave";
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}


					}
					else
					{
						Toast.makeText(getActivity(), "You end date is not before then start date.", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					strStartDate = sdf.format(d);
					edtStartDate.setText(tempsdf.format(d));
					// for:automatically select end date while selection of start date
					strEndDate = sdf.format(d);
					edtEndDate.setText(tempsdf.format(d));

					//edtLeaveType.setText("");
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

	private void getLeavesReport()
	{
		try {
			Call<LeaveResponse> leaveCall = apiService.getLeaves(sessionManager.getUserId(), String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),sessionManager.getUserId());
			leaveCall.enqueue(new Callback<LeaveResponse>() {
				@Override
				public void onResponse(Call<LeaveResponse> call, Response<LeaveResponse> response) {
					if (response.body().getSuccess() == 1)
					{
						LeaveResponse.LeaveBean bean = response.body().getLeave();

						strCasualTotal = bean.getCasual_leave().getTotal();
						strCasualUsed = bean.getCasual_leave().getUsed();
						strCasualPending = bean.getCasual_leave().getPending();

						strPrivilegeTotal = bean.getPrivilege_leave().getTotal();
						strPrivilegeUsed = bean.getPrivilege_leave().getUsed();
						strPrivilegePending = bean.getPrivilege_leave().getPending();


						strMedicalTotal = bean.getMedical_leave().getTotal();
						strMedicalUsed = bean.getMedical_leave().getUsed();
						strMedicalPending = bean.getMedical_leave().getPending();

						if (!strCasualTotal.equals("")) {
							txtCasualTotal.setText(strCasualTotal);
						} else {
							txtCasualTotal.setText(" - ");
						}

						if (!strCasualUsed.equals("")) {
							txtCasualUsed.setText(strCasualUsed);
						} else {
							txtCasualUsed.setText(" - ");
						}

						if (!strCasualPending.equals("")) {
							txtCasualPending.setText(strCasualPending);
						} else {
							txtCasualPending.setText(" - ");
						}


						if (!strPrivilegeTotal.equals("")) {
							txtPrivilegeTotal.setText(strPrivilegeTotal);
						} else {
							txtPrivilegeTotal.setText(" - ");
						}

						if (!strPrivilegeUsed.equals("")) {
							txtPrivilegeUsed.setText(strPrivilegeUsed);
						} else {
							txtPrivilegeUsed.setText(" - ");
						}

						if (!strPrivilegePending.equals("")) {
							txtPrivilegePending.setText(strPrivilegePending);
						} else {
							txtPrivilegePending.setText(" - ");
						}

						if (!strMedicalTotal.equals("")) {
							txtMedicalTotal.setText(strMedicalTotal);
						} else {
							txtMedicalTotal.setText(" - ");
						}

						if (!strMedicalUsed.equals("")) {
							txtMedicalUsed.setText(strMedicalUsed);
						} else {
							txtMedicalUsed.setText(" - ");
						}

						if (!strMedicalPending.equals("")) {
							txtMedicalPending.setText(strMedicalPending);
						} else {
							txtMedicalPending.setText(" - ");
						}


						if (!strLWPTotal.equals("")) {
							txtLWPTotal.setText(strLWPTotal);
						} else {
							txtLWPTotal.setText(" - ");
						}

						if (!strLWPUsed.equals("")) {
							txtLWPUsed.setText(strLWPUsed);
						} else {
							txtLWPUsed.setText(" - ");
						}


						if (!strLWPPending.equals("")) {
							txtLWPPending.setText(strLWPPending);
						} else {
							txtLWPPending.setText(" - ");
						}


						if(Integer.parseInt(bean.getCasual_leave().getPending())>0)
						{
							listLeaveType.add("Casual Leave");
						}

						if(Integer.parseInt(bean.getPrivilege_leave().getPending())>0)
						{
							listLeaveType.add("Privilege Leave");
						}

						if(Integer.parseInt(bean.getMedical_leave().getPending())>0)
						{
							listLeaveType.add("Medical Leave");
						}

						listLeaveType.add("Leave Without Pay");

					}
					else
					{
						AppUtils.showToast(activity, "No leave data found");
					}
				}

				@Override
				public void onFailure(Call<LeaveResponse> call, Throwable t) {
					AppUtils.showToast(activity, "No leave data found");
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void applyForLeave()
	{
		(new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected void onPreExecute()
			{
				try
				{
					pd = new ProgressDialog(activity);
					pd.setMessage("Loading...");
					pd.setCancelable(false);
					pd.show();

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params)
			{
				try
				{
					HashMap<String, String> data = new HashMap<String, String>();
					
					/*ApiTokenId:	
					mktCode:	
					fromDate:	
					toDate:	
					Reason:	
					LeaveTypeId:*/

					data.put("mktCode", sessionManager.getMktCode());
					data.put("fromDate", strStartDate);
					data.put("toDate", strEndDate);
					data.put("Reason", strReason);
					data.put("LeaveTypeId", strLeaveId);

					Log.e("API PARAMS", data + "");

					String serverResponse = MitsUtils.readJSONServiceUsingPOST("APPLY_FOR_LEAVE", data);
					Log.e("ServerResponse", serverResponse+" abcd");

					try
					{
						if (serverResponse != null)
						{
							try
							{
								JSONObject jsonObject = new JSONObject(serverResponse);

								strStatus = jsonObject.getString("status");
							}
							catch(JSONException e)
							{
								e.printStackTrace();
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);

				try
				{
					if(pd != null && pd.isShowing())
					{
						pd.dismiss();
						pd.cancel();
					}

					if(strStatus.equalsIgnoreCase("success"))
					{
						Toast.makeText(activity, "Successfully applied for leave.", Toast.LENGTH_LONG).show();

						edtEndDate.setText("");
						edtStartDate.setText("");
						edtReason.setText("");
						edtLeaveType.setText("");
					}
					else
					{
						Toast.makeText(activity, "Failed to applied for leave.", Toast.LENGTH_LONG).show();
						edtEndDate.setText("");
						edtStartDate.setText("");
						edtReason.setText("");
						edtLeaveType.setText("");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).execute();
	}

	public class LeaveTypeAdapter extends BaseAdapter
	{
		private Activity activity;
		ProgressDialog pd ;
		private LayoutInflater inflater=null;
		ArrayList<LeaveTypePojo> items ;

		String responseStatus = "" ;

		public LeaveTypeAdapter(Activity a, ArrayList<LeaveTypePojo> homeUniqueList)
		{
			this.activity = a;
			this.items = homeUniqueList ;
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			return items.size();
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		private class ViewHolder
		{
			private TextView txtLeaveName;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final LeaveTypeAdapter.ViewHolder holder;
			if(convertView == null)
			{
				holder = new LeaveTypeAdapter.ViewHolder();
				convertView = inflater.inflate(R.layout.row_view_leave_types, null);
				holder.txtLeaveName = (TextView) convertView.findViewById(R.id.txtLeaveName);

				convertView.setTag(holder);
			}
			else
			{
				holder = (LeaveTypeAdapter.ViewHolder) convertView.getTag();
			}

			holder.txtLeaveName.setText(items.get(position).getLeavetype());

			return convertView;
		}
	}

	private boolean validateMedicalLeave(String currentdate, String checkdate)
	{
		SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

		boolean b = false;

		try
		{
			try
			{
				if (dfDate.parse(currentdate).after(dfDate.parse(checkdate)))
				{
					b = true;  // If start date is before end date.
				}
				else if (dfDate.parse(currentdate).equals(dfDate.parse(checkdate)))
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

	AreaAdapter areaAdapter;
	public void showListDialog(final String isFor)
	{
		/*listDialog = new Dialog(activity);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		View promptView = layoutInflater.inflate(R.layout.dialog_list, null);

		listDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
		listDialog.setContentView(promptView);*/

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
				AppUtils.hideKeyboard(sheetView,activity);
			}
		});
		LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

		TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
		tvTitle.setText("Select "+isFor);

		TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

		if(isFor.equalsIgnoreCase("work with"))
		{
			tvDone.setVisibility(View.VISIBLE);
		}
		else
		{
			tvDone.setVisibility(View.GONE);
		}

		final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

		areaAdapter = new AreaAdapter();
		rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
		rvListDialog.setAdapter(areaAdapter);

		tvDone.setVisibility(View.GONE);

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
		edtSearchDialog.setVisibility(View.GONE);
		listDialog.findViewById(R.id.inputSearch).setVisibility(View.GONE);

		listDialog.show();
	}

	private class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder>
	{
		@Override
		public AreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
			return new AreaAdapter.ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(AreaAdapter.ViewHolder holder, final int position)
		{

			if(position == getItemCount()-1)
			{
				holder.viewLine.setVisibility(View.GONE);
			}
			else
			{
				holder.viewLine.setVisibility(View.VISIBLE);
			}
			holder.cb.setVisibility(View.GONE);
			holder.tvValue.setText(listLeaveType.get(position));
			holder.itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(listLeaveType.get(position).equals("Casual Leave"))
					{
						strLeaveType = "casual_leave";
						edtEndDate.setEnabled(true);
					}
					else if(listLeaveType.get(position).equals("Privilege Leave"))
					{
						strLeaveType = "privilege_leave";
						edtEndDate.setEnabled(true);
					}
					else if(listLeaveType.get(position).equals("Medical Leave"))
					{
						strLeaveType = "medical_leave";
						edtEndDate.setEnabled(false);
					}
					else if(listLeaveType.get(position).equals("Leave Without Pay"))
					{
						strLeaveType = "leave_without_pay";
						edtEndDate.setEnabled(true);
					}

					edtLeaveType.setText(listLeaveType.get(position));

					strStartDate = "";
					strEndDate = "";
					edtStartDate.setText("");
					edtEndDate.setText("");

					listDialog.dismiss();
					listDialog.cancel();
				}
			});
		}

		@Override
		public int getItemCount()
		{
			return listLeaveType.size();
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
	
	

	@Override
	public void onBackPressed() 
	{
		try
		{
			activity.finish();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy()
	{
		DashboardActivity.isAppRunning = false;
		super.onDestroy();
	}
}
