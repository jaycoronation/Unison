package com.unisonpharmaceuticals.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.utils.MitsUtils;
import com.unisonpharmaceuticals.views.InputfieldValidateUtils;

import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by Tushar Vataliya on 6/14/2018.
 */
public abstract class BaseClass  extends FragmentActivity
{
    public abstract void initViews();
    public abstract void viewClick();
    public abstract void getDataFromServer();

    public void basicProcesses()
    {
        initViews();
        viewClick();
        getDataFromServer();
    }

    /*Toast And snackbar*/
    public void showToast(Activity activity, String msg)
    {
        try {
            if(activity != null)
            {
                LayoutInflater inflater = activity.getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, null);

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(msg);

                Toast toast = new Toast(activity);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);

                if(toast.getView().isShown())
                {
                    toast.cancel();
                }
                else
                {
                    toast.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void snackBar(View v, String message, Activity activity)
    {
        try {
            int color = ContextCompat.getColor(activity, R.color.colorAccent);

            Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTypeface(getTypefaceRegular(activity));
            sbView.setBackgroundColor(color);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void noInternetSnakeBar(Activity activity)
    {
        snackBar(activity.getCurrentFocus(),activity.getString(R.string.network_failed_message),activity);
    }

    /*fOR GETTING VALID VARIABLES*/
    public String getValidAPIStringResponse(JSONObject jsonObject,String value)
    {
        if(jsonObject.has(value))
        {
            if(value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("<null>"))
            {
                value = "";
            }
        }
        else
        {
            value = "";
        }
        return value.trim();
    }

    public String getValidAPIStringResponse(String value)
    {
        if(value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("<null>"))
        {
            value = "";
        }
        return value.trim();
    }

    public String removeDiacriticalMarks(String string)
    {
        try {
            return getValidAPIStringResponse(Normalizer.normalize(string, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return getValidAPIStringResponse(string);
        }
    }

    public boolean getValidAPIBooleanResponse(String value)
    {
        boolean flag = false;
        try {
            value = getValidAPIStringResponse(value);

            if(value.equalsIgnoreCase("true"))
            {
                flag = true;
            }
        } catch (Exception e) {
        }

        return flag;
    }

    public int getValidAPIIntegerResponse(String value)
    {
        int val = 0;
        value = getValidAPIStringResponse(value);

        if(value.contains("."))
        {
            try {
                float f = Float.parseFloat(value);
                val = (int) f;
            } catch (NumberFormatException e) {
                val = 0;
            }
        }
        else
        {
            try {
                val = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                val = 0;
            }
        }

        return val;
    }

    public double getValidAPIDoubleResponse(String value)
    {
        double val = 0.0;
        value = getValidAPIStringResponse(value);

        try {
            val = Double.parseDouble(value);
        } catch (NumberFormatException e1) {
            val = 0.0;
        }

        return val;
    }

    public long getValidAPILongResponse(String value)
    {
        long l = 0;
        try {
            value = getValidAPIStringResponse(value);
            if(value.length() == 0)
            {
                l = 0;
            }
            else
            {
                l = Long.parseLong(value);
            }
        } catch (Exception e) {
        }
        return l;
    }


    /*For activity transition api*/
    public void startActivityAnimation(Activity activity)
    {

        hideKeyboard(activity);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void finishActivityAnimation(Activity activity)
    {
        hideKeyboard(activity);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /*For View*/
    public void removeError(EditText edt, final TextInputLayout layout) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hideKeyboard(View view, Activity activity)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager)context.getSystemService("input_method");
            inputManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 2);
        } catch (Exception var2) {
            Log.e("MitsUtilityClass", "Sigh, cant even hide keyboard " + var2.getMessage());
        }

    }

    public void showKeyboard(EditText editText, Activity activity)
    {
        try
        {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String toDisplayCase(String s)
    {
        String strToReturn = "";
        try {
            final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following to be capitalized

            StringBuilder sb = new StringBuilder();
            boolean capNext = true;

            for (char c : s.toCharArray()) {
                c = (capNext)
                        ? Character.toUpperCase(c)
                        : Character.toLowerCase(c);
                sb.append(c);
                capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
            }
            strToReturn = sb.toString();
        } catch (Exception e) {
            strToReturn = s;
            e.printStackTrace();
        }
        return strToReturn;
    }

    public String toFirstCapitalString(String s)
    {
        String returnString = "";
        try {
            returnString = s.substring(0,1).toUpperCase() + s.substring(1, s.length());
            return returnString;
        } catch (Exception e) {
            return s;
        }
    }

    public String getCapitalText(String text)
    {
        String desiredText = "";
        try {
            desiredText = text.toUpperCase(Locale.ENGLISH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desiredText;
    }

    /*For Typeface*/
    public Typeface getTypefaceRegular(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_regular));
    }
    public Typeface getTypefaceSemiBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_semibold));
    }
    public Typeface getTypefaceBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_bold));
    }

    /*Date Conversations*/
    public String universalDateConvert(String str, String FromFormat, String ToFormat) {
        String newString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(FromFormat);
        Date d;
        try {
            d = sdf.parse(str);
            sdf.applyPattern(ToFormat);
            newString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newString;
    }
    @SuppressLint("SimpleDateFormat")
    public String getCurrentDateString()
    {
        String str = "";
        try {
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            str = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    @SuppressLint("SimpleDateFormat")
    public String convertDateToString(long l)
    {
        String str = "";
        try {
            Date date = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            str = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    @SuppressLint("SimpleDateFormat")
    public String convertDateToTime(long l)
    {
        String str = "";
        try {
            Date date = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm: aa");
            dateFormat.setTimeZone(TimeZone.getDefault());
            str = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    public long getLocalTimestamp(final long time)
    {
        long timestamp = 0;
        try {
            Date localTime = new Date(time);
            String format = "dd MMM, yyyy HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            // Convert Local Time to UTC (Works Fine)
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            @SuppressWarnings("deprecation")
            Date gmtTime = new Date(sdf.format(localTime));
            // Convert UTC to Local Time
//			Date fromGmt = new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
//			System.out.println("LOCAL TIME : " + fromGmt.toString());
            timestamp = gmtTime.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * get relative date time like Yesterday, 4:10 PM or 2 hours ago
     * @param activity
     * @param timestamp timestamp to convert
     * @return relative datetime
     */
    public String getRelativeDateTime(final Activity activity, final long timestamp)
    {
        String datetime = "";
        try {
            datetime = DateUtils.getRelativeDateTimeString(activity, timestamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();

            if(datetime.contains("/"))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, h:mm a");
                Date date = new Date(timestamp*1000);
                datetime = sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public String getDateCurrentTimeString(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, h:mm a");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    public String getRelativeDateTimeForChat(final Activity activity, final long date)
    {
        String relativeDateTime = "";

        try
        {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");

            SimpleDateFormat dateCheck = new SimpleDateFormat("MM-dd-yyyy");
            Calendar calendar = Calendar.getInstance();

            String currentYear = String.valueOf(calendar.get(Calendar.YEAR));

            long today = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            long yesterday = calendar.getTimeInMillis();

            if(dateCheck.format(date).equals(dateCheck.format(today)))
            {
                relativeDateTime = df.format(date);
            }
            else if(dateCheck.format(date).equals(dateCheck.format(yesterday)))
            {
                relativeDateTime = "Yesterday";
            }
            else
            {
                SimpleDateFormat newdateformat = new SimpleDateFormat("dd MMM, yyyy");
                relativeDateTime = dateCheck.format(date);
                relativeDateTime = newdateformat.format(dateCheck.parse(relativeDateTime));

                try {
                    if(relativeDateTime.contains(currentYear))
                    {
                        String year = ", " + currentYear;
                        relativeDateTime = relativeDateTime.replace(year, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            relativeDateTime = getRelativeDateTime(activity, date);
            e.printStackTrace();
        }

        return relativeDateTime;
    }

    /*Validation*/
    public boolean validateEmail(final Activity activity, final EditText edtEmail, final TextInputLayout inputLayout, final String errorEmpty, final String errorInvalid)
    {
        if (edtEmail.getText().toString().trim().length() == 0)
        {
            inputLayout.setError(errorEmpty);
            InputfieldValidateUtils.requestFocus(edtEmail, activity);
            return false;
        }
        else if (!validateEmail(edtEmail.getText().toString().trim()))
        {
            inputLayout.setError(errorInvalid);
            InputfieldValidateUtils.requestFocus(edtEmail, activity);
            return false;
        }
        else
        {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }
    public boolean validateEmail(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean validateWebUrl(CharSequence url)
    {
        try {
            return android.util.Patterns.WEB_URL.matcher(url).matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean validPassword(String password)
    {
        Pattern[] checks =
                {
                        // special character
//	        Pattern.compile("[!@#\\$%^&*()~`\\-=_+\\[\\]{}|:\\\";',\\./<>?]"),
                        // small character
//	        Pattern.compile("[a-z]"),
                        // capital character
//	        Pattern.compile("[A-Z]"),
                        // numeric character
//	        Pattern.compile("\\d"),
                        // 6 to 40 character length
                        Pattern.compile("^.{6,40}$")
                };
        boolean ok = true;
        for (Pattern check : checks)
        {
            ok = ok && check.matcher(password).find();
        }

        return ok;
    }
}
