package com.unisonpharmaceuticals.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.unisonpharmaceuticals.activity.DashboardActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentMakeEntry;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.JobSchedulerHelper;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Kiran Patel on 25-Sep-18.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService
{
    private SessionManager sessionManager;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        try
        {
            sessionManager = new SessionManager(getApplicationContext());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            Date d = new Date();

           /* Calendar calendar3Hour = Calendar.getInstance();
            calendar3Hour.set(Calendar.HOUR_OF_DAY, 12);
            calendar3Hour.set(Calendar.MINUTE, 00);
            calendar3Hour.set(Calendar.SECOND, 0);
            Date d3Hour = new Date();*/

            String currentTime = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
            String selectedTime = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(calendar.getTime().toString()));
            /*String selectedTime3Hour = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(calendar3Hour.getTime().toString()));*/

            Log.e("MyJobService START >>>", "onStart: " + "Current >> "+currentTime+"    -    Selected: "+selectedTime +"    "+selectedTime.compareTo(currentTime));
            if (selectedTime.compareTo(currentTime) <= 0)
            {
                if(sessionManager.isLoggedIn())
                {
                    if(sessionManager.getDayEnd().equalsIgnoreCase("false"))
                    {

                        if(DataUtils.listUserEntries(sessionManager).size()>0)
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager,DataUtils.listUserEntries(sessionManager)),true);
                            }
                            else
                            {
                                if(DashboardActivity.isAppRunning)
                                {
                                    sessionManager.logoutWithoutDialog();
                                }
                                else
                                {
                                    sessionManager.logoutOnDestroy();
                                }

                                if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),11))
                                {
                                    JobSchedulerHelper.cancelJob(getApplicationContext(),11,true);
                                }

                                if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),22))
                                {
                                    JobSchedulerHelper.cancelJob(getApplicationContext(),22,true);
                                }

                                /*if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),33))
                                {
                                    JobSchedulerHelper.cancelJob(getApplicationContext(),33,true);
                                }*/
                            }
                        }
                        else
                        {
                            AppUtils.storeJsonResponse("List Is Empty","EmptyList");

                            if(DashboardActivity.isAppRunning)
                            {
                                sessionManager.logoutWithoutDialog();
                            }
                            else
                            {
                                sessionManager.logoutOnDestroy();
                            }

                            if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),11))
                            {
                                JobSchedulerHelper.cancelJob(getApplicationContext(),11,true);
                            }

                            if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),22))
                            {
                                JobSchedulerHelper.cancelJob(getApplicationContext(),22,true);
                            }

                            /*if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),33))
                            {
                                JobSchedulerHelper.cancelJob(getApplicationContext(),33,true);
                            }*/
                        }
                    }
                }
            }
            else
            {
                /*if(sessionManager.getDayEnd().equalsIgnoreCase("false"))
                {
                    if(DataUtils.listUserEntries(sessionManager).size()>0)
                    {
                        if(sessionManager.isNetworkAvailable())
                        {
                            submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager,DataUtils.listUserEntries(sessionManager)),false);
                        }
                    }
                }*/
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void submitPendingEntries(final String stringToPass, final boolean isAutoLogout)
    {
        try
        {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("field_entry",stringToPass);
            hashMap.put("login_user_id",sessionManager.getUserId());
            Log.e("Submit Request >>> ", "doInBackground: " + hashMap.toString() );
            AppUtils.storeJsonResponse(hashMap.toString(),"AutoSubmitRequest");
            String response = "";
            response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SUBMIT_ENTRIES,hashMap);
            AppUtils.storeJsonResponse(response,"AutoSubmitResponse");
            Log.e("Submit Response >>> ", "doInBackground: " + response );
            JSONObject jsonObject = new JSONObject(response);
            int success= AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
            if(success ==1)
            {
                sessionManager.setCallDoneFromTP("true");
                NewEntryGetSet.deleteAll(NewEntryGetSet.class);
            }

            if(isAutoLogout)
            {
                if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),11))
                {
                    JobSchedulerHelper.cancelJob(getApplicationContext(),11,true);
                }
                if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),22))
                {
                    JobSchedulerHelper.cancelJob(getApplicationContext(),22,true);
                }
                /*if(JobSchedulerHelper.isJobServiceRunning(getApplicationContext(),33))
                {
                    JobSchedulerHelper.cancelJob(getApplicationContext(),33,true);
                }*/

                if(DashboardActivity.isAppRunning)
                {
                    sessionManager.logoutWithoutDialog();
                }
                else
                {
                    sessionManager.logoutOnDestroy();
                }
            }
            else
            {
                if(FragmentMakeEntry.areaHandler!=null)
                {
                    Message msg = Message.obtain();
                    msg.what = 112;
                    FragmentMakeEntry.areaHandler.sendMessage(msg);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
