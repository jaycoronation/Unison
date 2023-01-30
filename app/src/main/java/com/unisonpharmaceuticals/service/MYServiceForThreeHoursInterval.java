package com.unisonpharmaceuticals.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONObject;
import java.util.*;

public class MYServiceForThreeHoursInterval extends Service
{
	SessionManager sessionManager;

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onCreate()
    {
       super.onCreate();
	}
 
   @SuppressWarnings("deprecation")
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);
       
       try 
		{
			sessionManager = new SessionManager(this);
			if(sessionManager.isLoggedIn())
			{
				if(sessionManager.isNetworkAvailable())//Submit All Pending entries on 23:55
				{
					List<NewEntryGetSet> books = NewEntryGetSet.listAll(NewEntryGetSet.class);

					ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
					for (int i = 0; i < books.size(); i++)
					{
						if(books.get(i).getUser_id().equals(sessionManager.getUserId()))
						{
							listUserEntry.add(books.get(i));
						}
					}

					if (listUserEntry.size() > 0)
					{
						if(sessionManager.getDayEnd().equals("false"))
						{
							submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager,listUserEntry));
						}
					}
				}
			}
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
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
			JSONObject jsonObject = new JSONObject(response);
			int success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
			String message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
			if(success ==1)
			{
				sessionManager.setCallDoneFromTP("true");
				NewEntryGetSet.deleteAll(NewEntryGetSet.class);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
 
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
    }

}