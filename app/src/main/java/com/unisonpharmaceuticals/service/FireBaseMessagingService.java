package com.unisonpharmaceuticals.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.DashboardActivity;
import com.unisonpharmaceuticals.activity.LoginActivity;
import com.unisonpharmaceuticals.activity.NotificationActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.PushNotificationGetSet;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;
    private SessionManager sessionManager;
    private String contentTitle = "Unison";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        try
        {
            sessionManager = new SessionManager(this);

            if(sessionManager.isLoggedIn())// Display notification if user logged in
            {
                Map<String, String> map = remoteMessage.getData();
                Log.e("##### NOTI JSON", "======> " + remoteMessage.getData().toString());
                JSONObject object = new JSONObject(map);
                PushNotificationGetSet pushNotificationGetSet = new PushNotificationGetSet();

                pushNotificationGetSet.setContent_id(Integer.parseInt(object.getString("content_id")));
                pushNotificationGetSet.setTitle(object.getString("title"));
                pushNotificationGetSet.setMessage(object.getString("message"));
                pushNotificationGetSet.setNotification_type(object.getString("notification_type"));

                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if(pushNotificationGetSet.getNotification_type().equalsIgnoreCase("force_logout"))
                {
                    sessionManager.setIsForLogoutOnDestroy(true);
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
                    if(sessionManager.isLoggedIn())
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            setupChannelsForNormal(pushNotificationGetSet);
                        }
                        else
                        {
                            sendNotification(pushNotificationGetSet);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannelsForNormal(PushNotificationGetSet pushNotificationGetSet)
    {
        try
        {

            Random generator = new Random();
            int notifRandomId = 1000 - generator.nextInt(1000);
            Intent intent;
            if(pushNotificationGetSet.getNotification_type().equalsIgnoreCase("force_logout"))
            {
                intent = new Intent(this, LoginActivity.class);
            }
            else
            {
                if(sessionManager.isLoggedIn())
                {
                    intent = new Intent(this, NotificationActivity.class);
                }
                else
                {
                    intent = new Intent(this, LoginActivity.class);
                }
            }
            intent.putExtra("type","NOTIFICATIONS");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            CharSequence boldTitle = Html.fromHtml("<b>" + contentTitle + "</b> ");
            CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
            String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            adminChannel.setShowBadge(true);

            /*Set Light Work*/
            adminChannel.enableLights(true);

            /*Set Sound Work*/
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            adminChannel.setLightColor(0xff0000ff);

            adminChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            /*Set Vibration Work*/
            adminChannel.enableVibration(true);
            adminChannel.setVibrationPattern(new long[]{100, 1000});

            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(adminChannel);

                Notification notification = new Notification.Builder(this, ADMIN_CHANNEL_ID)
                        .setContentText(pushNotificationGetSet.getMessage())
                        .setColor(Color.parseColor("#019ce4"))
                        .setStyle(new Notification.BigTextStyle()
                        .bigText(pushNotificationGetSet.getMessage()))
                        .setSmallIcon(R.drawable.ic_notification_unison)
                        .setAutoCancel(true).setContentIntent(pIntent).build();

                notificationManager.notify(notifRandomId, notification);


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendNotification(PushNotificationGetSet pushNotificationGetSet)
    {
        try
        {
            Random generator = new Random();
            int notifRandomId = 1000 - generator.nextInt(1000);

            Intent intent;

            if(pushNotificationGetSet.getNotification_type().equalsIgnoreCase("force_logout"))
            {
                intent = new Intent(this, LoginActivity.class);
            }
            else
            {
                if(sessionManager.isLoggedIn())
                {
                    intent = new Intent(this, NotificationActivity.class);
                }
                else
                {
                    intent = new Intent(this, LoginActivity.class);
                }
            }

            intent.putExtra("type","NOTIFICATIONS");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            CharSequence boldTitle = Html.fromHtml("<b>" + contentTitle + "</b> ");

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setAutoCancel(true)
                    .setColor(Color.parseColor("#019ce4"))
                    .setContentTitle(boldTitle)
                    .setContentText(pushNotificationGetSet.getMessage())
                    .setSmallIcon(R.drawable.ic_notification_unison)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pIntent);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(boldTitle);
            bigTextStyle.bigText(pushNotificationGetSet.getMessage());
            notificationBuilder.setStyle(bigTextStyle);

            notificationManager.notify(notifRandomId, notificationBuilder.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}