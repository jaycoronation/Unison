package com.unisonpharmaceuticals.activity;

import static com.unisonpharmaceuticals.utils.AppUtils.showToast;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.OneTimeSession;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AppVersionResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends Activity
{
	private Activity activity;
	private SessionManager sm ;
	private OneTimeSession oneTimeSession;

	CountDownTimer timer ;
	
	public static String PACKAGE_NAME;
	
	Handler handler ;

	ImageView ivImage;

	public static boolean isVersionMismatch = false,isForcefulUpdate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		activity = SplashActivity.this;
		
		sm = new SessionManager(activity);
		oneTimeSession = new OneTimeSession(activity);

		//Log.e("Database URL", DebugDB.getAddressLog());

		ivImage = (ImageView) findViewById(R.id.ivImage);

		PACKAGE_NAME = getApplicationContext().getPackageName();

		checkNotificationPermission();

		try
		{
			sm.setDeviceName(android.os.Build.MODEL);

			String token = AppUtils.getValidAPIStringResponse(FirebaseInstanceId.getInstance().getToken());

			FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
				String token1 = AppUtils.getValidAPIStringResponse(task.getResult());
				Log.e("FIREBASE TOKEN", token1);
			});

			sm.saveTokenId(token);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if(sm.isNetworkAvailable())
		{
			checkForVersionUpdate();
		}
	}

	private void checkNotificationPermission()
	{
		try
		{
			int result;

			result = ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS);

			if (NotificationManagerCompat.from(activity).areNotificationsEnabled())
			{
				goThrough();
			}
			else
			{
				ActivityCompat.requestPermissions(activity,new String[] { Manifest.permission.POST_NOTIFICATIONS },1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1) {
			try {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					nextScreenIntent();
				}
				else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
				{
					showToast(activity,"Please allow notification permission first.");
					Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					Uri uri = Uri.fromParts("package", getPackageName(), null);
					intent.setData(uri);
					startActivity(intent);
					nextScreenIntent();
				}
				else
				{
					showToast(activity, "Permissions Denied!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void checkForVersionUpdate()
	{
		ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
		Call<AppVersionResponse> call = apiInterface.getAppVersion("android");
		call.enqueue(new Callback<AppVersionResponse>() {
			@Override
			public void onResponse(Call<AppVersionResponse> call, Response<AppVersionResponse> response)
			{
				if(response.isSuccessful())
				{
					if(response.body().getSuccess()==1)
					{
						try {
							PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
							String version = pInfo.versionName;
							String newVer = response.body().getVersion().replace(".","").trim();
							String curVer = version.replace(".","").trim();

							if(Integer.parseInt(curVer) < Integer.parseInt(newVer))
							{
								isVersionMismatch = true;
							}
							else
							{
								isVersionMismatch = false;
							}

							if(response.body().getForce_update().equalsIgnoreCase("1"))
							{
								isForcefulUpdate = true;
							}
							else
							{
								isForcefulUpdate = false;
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else
					{
						isVersionMismatch = false;
					}
				}
				else
				{
					showToast(activity,activity.getString(R.string.api_failed_message));
				}
			}

			@Override
			public void onFailure(Call<AppVersionResponse> call, Throwable t) {

			}
		});
	}
	
	private void goThrough()
	{
		timer = new CountDownTimer(3000, 10000)
		{
			@Override
			public void onTick(long millisUntilFinished)
			{

			}

			@Override
			public void onFinish()
			{
				nextScreenIntent();
			}
		};
		timer.start();
	}

	private void nextScreenIntent(){
		finish();
		if(sm.isSpecialLogin())
		{
			Intent intent = new Intent(activity,SpecialActivity.class);
			startActivity(intent);
			AppUtils.startActivityAnimation(activity);
		}
		else
		{
			if(oneTimeSession.isAuthenticate())
			{
				//New Added this If Condition
				if(sm.isLogoutOnDestroy())
				{
					Intent intent = new Intent(activity, LoginActivity.class);
					startActivity(intent);
				}
				else
				{
					if(sm.isLoggedIn())
					{
						if(sm.isOnLeave()==1)
						{
							Intent intent = new Intent(activity,PendingLeaveActivity.class);
							intent.putExtra("isFor","login");
							startActivity(intent);
						}
						else
						{
							Intent intent = new Intent(activity,DashboardActivity.class);
							//Intent intent = new Intent(activity,TempActivity.class);
							startActivity(intent);
						}
					}
					else
					{
						Intent intent = new Intent(activity,LoginActivity.class);
						startActivity(intent);
					}
				}
			}
			else
			{
				//intent = new Intent(activity,AuthenticateActivity.class);
				Intent intent = new Intent(activity,MobileNumberActivity.class);
				startActivity(intent);
			}
			AppUtils.startActivityAnimation(activity);
		}
	}

}
