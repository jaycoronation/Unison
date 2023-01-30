package com.unisonpharmaceuticals.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.DashboardActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.DataUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MYService extends Service
{

	private static final String TAG = "UNISON SERVICE";
	private static final String ADMIN_CHANNEL_ID = "admin_channel";
	private NotificationManager notificationManager;

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
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			String CHANNEL_ID = "my_channel_01";
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
					"Unison",
					NotificationManager.IMPORTANCE_DEFAULT);

			((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

			Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
					.setContentTitle("")
					.setContentText("").build();

			startForeground(1, notification);
		}*/
    }

	@SuppressWarnings("deprecation")
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);
       try
	   {

	   		sessionManager = new SessionManager(this);
		   Calendar calendar = Calendar.getInstance();
		   calendar.set(Calendar.HOUR_OF_DAY, 23);
		   calendar.set(Calendar.MINUTE, 55);
		   calendar.set(Calendar.SECOND, 0);
		   Date d = new Date();

		   Calendar calendar3Hour = Calendar.getInstance();
		   calendar3Hour.set(Calendar.HOUR_OF_DAY, 12);
		   calendar3Hour.set(Calendar.MINUTE, 00);
		   calendar3Hour.set(Calendar.SECOND, 0);
		   Date d3Hour = new Date();

		   String currentTime = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
		   String selectedTime = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(calendar.getTime().toString()));
		   String selectedTime3Hour = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(calendar3Hour.getTime().toString()));

		   Log.e("MyService START >>>", "onStart: " + "Current >> "+currentTime+"    -    Selected: "+selectedTime +"    "+(selectedTime.compareTo(currentTime) < 0));

		   if (selectedTime.compareTo(currentTime) <= 0)
		   {
		   		if(sessionManager.isNetworkAvailable())//Submit All Pending entries on 23:55
				{

					if(sessionManager.isLoggedIn())
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
								submitPendingEntries(DataUtils.getJsonStringFromPendingEntry(sessionManager,(ArrayList<NewEntryGetSet>) books));
							}
						}
						else
						{
							try
							{
								stopService(new Intent(this,MYService.class));
								stopService(new Intent(this,MYServiceForThreeHoursInterval.class));
								if(DashboardActivity.isAppRunning)
								{
									sessionManager.logoutWithoutDialog();
								}
								else
								{
									sessionManager.logoutOnDestroy();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				else //if network not available at 23:55 than remove all entries from local database
				{

					try {
						AppUtils.storeJsonResponse("No internet available while auto submit","NoInternet");
						stopService(new Intent(this,MYService.class));
						stopService(new Intent(this,MYServiceForThreeHoursInterval.class));
						NewEntryGetSet.deleteAll(NewEntryGetSet.class);
						if(DashboardActivity.isAppRunning)
						{
							sessionManager.logoutWithoutDialog();
						}
						else
						{
							sessionManager.logoutOnDestroy();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		   }

		   /*Log.e("MyService 3 Hours START >>>", "onStart: " + "Current >> "+currentTime+"    -    Selected: "+selectedTime3Hour +"    "+(selectedTime3Hour.compareTo(currentTime)));

		   if (selectedTime3Hour.compareTo(currentTime) < 0)//3 hour service  ://Now its 4 hours
		   {
			   try
			   {
				   Log.e("************>>> ", "onStart: "+AppUtils.isServiceRunning(MYServiceForThreeHoursInterval.class,this) );

			   		if(!AppUtils.isServiceRunning(MYServiceForThreeHoursInterval.class,this))
					{
						Log.e("3 hours service Called", "callMakeEntryService: ");
						Calendar calThree = Calendar.getInstance();
						Intent intentThree = new Intent(this, MYServiceForThreeHoursInterval.class);
						PendingIntent pendingIntentThree = PendingIntent.getService(this, 0, intentThree, 0);
						AlarmManager contactAlarmThree = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
						contactAlarmThree.setRepeating(AlarmManager.RTC_WAKEUP, calThree.getTimeInMillis(), 1000*60*240, pendingIntentThree);
						//Forcefully start the serviceMYServiceForThreeHour
						Intent contactServiceThree = new Intent(this, MYServiceForThreeHoursInterval.class);
						this.startService(contactServiceThree);
					}
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		   }*/
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

			if(sessionManager.isServiceRunning(MYService.class))
			{
				stopService(new Intent(this,MYService.class));
				stopService(new Intent(this,MYServiceForThreeHoursInterval.class));
			}
			if(DashboardActivity.isAppRunning)
			{
				sessionManager.logoutWithoutDialog();
			}
			else
			{
				sessionManager.logoutOnDestroy();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void sendNotification()
	{
		try
		{
			Random generator = new Random();

			int notifRandomId = 1000 - generator.nextInt(1000);

			Intent intent = null;


			/*if (pushNotificationGetSet.getContent_id().length() > 0)
			{
				if (pushNotificationGetSet.getOperation().equalsIgnoreCase("New Press Note Uploaded"))
				{
					intent = new Intent(this, FileListActivity.class);
					intent.putExtra("post_type", "document");
					intent.putExtra("categoryName", "Press Releases");
					intent.putExtra("parent_id", "");
					intent.putExtra("isFromNotification", true);
				}
				else
				{
					intent = new Intent(this, FileListActivity.class);
					intent.putExtra("post_type", "video");
					intent.putExtra("categoryName", "Press Video Clips");
					intent.putExtra("parent_id", "");
					intent.putExtra("isFromNotification", true);
				}
			}
			else
			{
			}*/

			intent = new Intent(this, DashboardActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_ONE_SHOT);

			Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			CharSequence boldTitle = Html.fromHtml("<b>" + "Unison" + "</b> ");

			Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setAutoCancel(true).setContentTitle(boldTitle).setContentText("Message from Unison").setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL).setLargeIcon(icon).setContentIntent(pendingIntent);

			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
			bigTextStyle.setBigContentTitle(boldTitle);
			/*bigTextStyle.bigText(pushNotificationGetSet.getMessage());*/
			notificationBuilder.setStyle(bigTextStyle);

			notificationManager.notify(notifRandomId, notificationBuilder.build());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressLint("WrongConstant")
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void setupChannelsForNormal()
	{
		Random generator = new Random();
		int notifRandomId = 1000 - generator.nextInt(1000);

		Intent intent = null;

		try
		{
			/*if (pushNotificationGetSet.getContent_id().length() > 0)
			{
				if (pushNotificationGetSet.getOperation().equalsIgnoreCase("New Press Note Uploaded"))
				{
					intent = new Intent(this, FileListActivity.class);
					intent.putExtra("post_type", "document");
					intent.putExtra("categoryName", "Press Releases");
					intent.putExtra("parent_id", "");
					intent.putExtra("isFromNotification", true);
				}
				else
				{
					intent = new Intent(this, FileListActivity.class);
					intent.putExtra("post_type", "video");
					intent.putExtra("categoryName", "Press Video Clips");
					intent.putExtra("parent_id", "");
					intent.putExtra("isFromNotification", true);
				}
			}
			else
			{
				intent = new Intent(this, HomeActivityNew.class);
			}*/

			intent = new Intent(this, DashboardActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent pIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			CharSequence boldTitle = Html.fromHtml("<b>" + "Unison" + "</b> ");

			CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
			String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

			NotificationChannel adminChannel;
			adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
			adminChannel.setDescription(adminChannelDescription);
			adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
			adminChannel.setShowBadge(true);
			adminChannel.enableLights(true);


			AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
			adminChannel.setLightColor(0xff0000ff);

			adminChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

			adminChannel.enableVibration(true);
			adminChannel.setVibrationPattern(new long[]{100, 1000});

			if (notificationManager != null)
			{
				notificationManager.createNotificationChannel(adminChannel);

				Notification notification = new Notification.Builder(this, ADMIN_CHANNEL_ID).setContentTitle(boldTitle).setContentText("Message from unison").setColor(Color.parseColor("#019EEB")).setStyle(new Notification.BigTextStyle().bigText("Message from unison")).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(pIntent).build();

				notificationManager.notify(notifRandomId, notification);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}