package com.unisonpharmaceuticals.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Kiran Patel on 26-Sep-18.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobServiceForThreeHours extends JobService
{
    private SessionManager sessionManager;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        try
        {
            sessionManager = new SessionManager(getApplicationContext());
            if(sessionManager.isLoggedIn())
            {
                if(sessionManager.getDayEnd().equalsIgnoreCase("false"))
                {
                    if(DataUtils.listUserEntries(sessionManager).size()>0)
                    {
                        if(sessionManager.isNetworkAvailable())
                        {
                            submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager,DataUtils.listUserEntries(sessionManager)));
                        }
                    }
                }
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

    private void submitPendingEntries(final String stringToPass)
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
            if(response.length()>0)
            {
                JSONObject jsonObject = new JSONObject(response);
                int success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));

                if(success ==1)
                {
                    sessionManager.setCallDoneFromTP("true");
                    NewEntryGetSet.deleteAll(NewEntryGetSet.class);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
