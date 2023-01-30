package com.unisonpharmaceuticals.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.unisonpharmaceuticals.utils.AppUtils;

/**
 * Created by Tushar Vataliya on 4/19/2018.
 */

public class OneTimeSession
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "unisonOneTimeSession";
    private static final String IS_AUTHENTICATE = "isAuthenticate";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String OTP = "otp";

    private Context context;

    public OneTimeSession(Activity activity)
    {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        preferences = activity.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public OneTimeSession(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }
    public void clearOneTimeSession()
    {
        editor.clear();
        editor.commit();
    }

    public void setIsAuthenticate(boolean isAuthenticate)
    {
        editor = preferences.edit();
        editor.putBoolean(IS_AUTHENTICATE,isAuthenticate);
        editor.commit();
    }

    public boolean isAuthenticate()
    {
        return preferences.getBoolean(IS_AUTHENTICATE, false);
    }

    public void setMobileNumber(String mobile)
    {
        editor = preferences.edit();
        editor.putString(MOBILE_NUMBER,mobile);
        editor.commit();
    }

    public String getMobileNumber()
    {
        return preferences.getString(MOBILE_NUMBER, "");
    }

    public void setOTP(String otp)
    {
        editor = preferences.edit();
        editor.putString(OTP,otp);
        editor.commit();
    }

    public String getOTP()
    {
        return preferences.getString(OTP, "");
    }
}
