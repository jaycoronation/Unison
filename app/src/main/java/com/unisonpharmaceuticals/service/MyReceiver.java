package com.unisonpharmaceuticals.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.JobSchedulerHelper;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent1)
	{
		Log.e("My Receiver Called", "Called");
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		{
			/*if(!JobSchedulerHelper.isJobServiceRunning(context,33))
			{
				JobSchedulerHelper.startJobForPeriodicTask(context,MyJobService.class,33,1000*60*15);
			}*/
		}
		else
		{
			/*if(!AppUtils.isServiceRunning(MYService.class,context.getApplicationContext()))
			{
				Calendar cal = Calendar.getInstance();
				Intent intent = new Intent(context, MYService.class);
				PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
				AlarmManager contactAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				contactAlarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), (60*1000), pendingIntent);
				//Forcefully start the serviceMYService
				Intent contactService = new Intent(context, MYService.class);
				context.startService(contactService);
			}*/
		}
	}
}
