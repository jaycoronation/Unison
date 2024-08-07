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
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
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

public class PendingLeaveActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;

    private TextView txtCasualTotal , txtCasualUsed , txtCasualPending , txtPrivilegeTotal , txtPrivilegeUsed , txtPrivilegePending , txtMedicalTotal , txtMedicalUsed , txtMedicalPending ,
            txtLWPTotal , txtLWPUsed , txtLWPPending ;

    //Apply for leave
    public static EditText edtStartDate , edtEndDate , edtReason , edtLeaveType ;

    private LinearLayout btnApply,llLoading,llLogout;

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
    private final ArrayList<String> listLeaveType = new ArrayList<>();

    private String isFor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_leave);
        activity = this;
        isFor = getIntent().getStringExtra("isFor");
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

        if(SplashActivity.isVersionMismatch)
        {
            AppUtils.showVersionMismatchDialog(activity,SplashActivity.isForcefulUpdate);
        }
    }

    @Override
    public void initViews() {
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        llLogout = findViewById(R.id.llLogout);
        llLogout.setVisibility(View.GONE);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setImageResource(R.drawable.ic_logout);
        findViewById(R.id.llBack).setVisibility(View.VISIBLE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Leave");

        llLoading = findViewById(R.id.llLoading);

        txtCasualTotal = findViewById(R.id.txtCasualTotal);
        txtCasualUsed = findViewById(R.id.txtCasualUsed);
        txtCasualPending = findViewById(R.id.txtCasualPending);

        txtPrivilegeTotal = findViewById(R.id.txtPrivilegeTotal);
        txtPrivilegeUsed = findViewById(R.id.txtPrivilegeUsed);
        txtPrivilegePending = findViewById(R.id.txtPrivilegePending);

        txtMedicalTotal = findViewById(R.id.txtMedicalTotal);
        txtMedicalUsed = findViewById(R.id.txtMedicalUsed);
        txtMedicalPending = findViewById(R.id.txtMedicalPending);

        txtLWPTotal = findViewById(R.id.txtLWPTotal);
        txtLWPUsed = findViewById(R.id.txtLWPUsed);
        txtLWPPending = findViewById(R.id.txtLWPPending);

        //Apply For Leave
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        edtReason = findViewById(R.id.edtReason);
        edtLeaveType = findViewById(R.id.edtLeaveType);

        edtStartDate.setInputType(InputType.TYPE_NULL);
        edtEndDate.setInputType(InputType.TYPE_NULL);
        edtLeaveType.setInputType(InputType.TYPE_NULL);

        btnApply = findViewById(R.id.btnApply);

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
                flagDate = false ;
                showDatePickerDialog();
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flagDate = true ;
                showDatePickerDialog();
            }
        });

        edtLeaveType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog("Leave Type");
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(sessionManager.isLoggedIn())
                {
                    submitForm();
                }
                else
                {
                    AppUtils.showToast(activity,"Your login session expired! Please login again");
                }
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                new AsyncTask<Void,Void,Void>()
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
                        try
                        {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("staff_id",sessionManager.getUserId());
                            hashMap.put("fromDate",strStartDate);
                            hashMap.put("toDate",strEndDate);
                            hashMap.put("reason",edtReason.getText().toString().trim());
                            hashMap.put("leave_type",strLeaveType);
                            hashMap.put("login_user_id",sessionManager.getUserId());

                            Log.e("Leave REQUEST", "doInBackground: "+ hashMap);

                            String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiClient.APPLY_LEAVE,hashMap);

                            JSONObject jsonObject = new JSONObject(serverResponse);
                            success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
                            message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));

                            Log.e("Leave RESPONSE", "doInBackground: "+serverResponse );
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        llLoading.setVisibility(View.GONE);

                        AppUtils.showToast(activity,message);

                        if(success==1)
                        {
                            sessionManager.setOnLeave(0);

                            edtEndDate.setText("");
                            edtReason.setText("");
                            edtStartDate.setText("");
                            edtLeaveType.setText("");

                            if(isFor.equalsIgnoreCase("login"))
                            {
                                startActivity(new Intent(activity,DashboardActivity.class));
                                activity.finish();
                            }
                            else
                            {
                                if(DashboardActivity.handler!=null)
                                {
                                    Message message = Message.obtain();
                                    message.what = 111;
                                    DashboardActivity.handler.sendMessage(message);
                                }
                                activity.finish();
                            }

                        }
                        else
                        {
                            AppUtils.showToast(activity,activity.getString(R.string.api_failed_message));
                        }

                    }
                }.execute();

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

			/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				dateStart = df.parse(strStartDate);
				dateEnd = df.parse(strEndDate);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			long validDiff = 24 * 60 * 60 * 1000;
			long diff = dateEnd.getTime() - dateStart.getTime();

			boolean isMedicalLeave = (edtLeaveType.getText().toString().trim().equalsIgnoreCase("Medical Leave")) ? true : false;

			if((dateStart.getTime() > dateEnd.getTime()) || (isMedicalLeave && (diff >= validDiff)))
			{
				Toast.makeText(activity, "Please select valid end date.", Toast.LENGTH_SHORT).show();
			}
			else
			{

			}*/
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
                Date today = calendar.getTime();
                //calendar.add(Calendar.DAY_OF_YEAR, -1);
                //Date yesterday = calendar.getTime();
                //datepicker.getDatePicker().setMinDate(yesterday.getTime());
            }
            else
            {
                if(edtLeaveType.getText().toString().trim().equalsIgnoreCase("Casual Leave"))
                {
                    if(!strStartDate.equalsIgnoreCase(""))
                    {
                        int casualPending = Integer.parseInt(strCasualPending);
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
                        int casualPending = Integer.parseInt(strPrivilegePending);
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
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            Call<LeaveResponse> leaveCall = apiService.getLeaves(sessionManager.getUserId(), String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),sessionManager.getUserId());
            leaveCall.enqueue(new Callback<LeaveResponse>() {
                @Override
                public void onResponse(Call<LeaveResponse> call, Response<LeaveResponse> response)
                {
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

                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<LeaveResponse> call, Throwable t) {
                    AppUtils.showToast(activity, "No leave data found");
                    llLoading.setVisibility(View.GONE);
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
        private final Activity activity;
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
                holder.txtLeaveName = convertView.findViewById(R.id.txtLeaveName);

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
                // If start date is after the end date.
                if (dfDate.parse(currentdate).after(dfDate.parse(checkdate)))
                {
                    b = true;  // If start date is before end date.
                }
                else b = dfDate.parse(currentdate).equals(dfDate.parse(checkdate));  // If two dates are equal.
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
                // If start date is after the end date.
                if (dfDate.parse(startDate).before(dfDate.parse(endDate)))
                {
                    b = true;  // If start date is before end date.
                }
                else b = dfDate.parse(startDate).equals(dfDate.parse(endDate));  // If two dates are equal.
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
        LinearLayout btnNo = listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select "+isFor);

        TextView tvDone = listDialog.findViewById(R.id.tvDone);

        if(isFor.equalsIgnoreCase("work with"))
        {
            tvDone.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDone.setVisibility(View.GONE);
        }

        final RecyclerView rvListDialog = listDialog.findViewById(R.id.rvDialog);

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

        final EditText edtSearchDialog = listDialog.findViewById(R.id.edtSearchDialog);
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
            private final TextView tvValue;
            private final TextView tvId;
            private final CheckBox cb;
            private final View viewLine;
            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = itemView.findViewById(R.id.tvValue);
                tvId = itemView.findViewById(R.id.tvId);
                viewLine = itemView.findViewById(R.id.viewLine);
                cb = itemView.findViewById(R.id.cb);
                cb.setTypeface(AppUtils.getTypefaceRegular(activity));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            activity.finishAffinity();
            //return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
