package com.unisonpharmaceuticals.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.model.for_sugar.DisplayReports;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SimpleDateFormat")
public class AppUtils
{
	/**
	 * check if string contains characters only. Use for name validation
	 * @param name
	 * @return false if contains non character number or special character, true if valid
	 */
	public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
	
	/**
	 * check if entered email address is valid
	 * @param email email address
	 * @return true if valid else false
	 */
	public static boolean validateEmail(CharSequence email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static String currentDateForApi()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Log.e("Current Date >> ", "currentDate: " +  sdf.format(cal.getTime()));

		return sdf.format(cal.getTime());
	}

	public static HashMap<String, String> getEmailListFromDB(final Context context)
	{
		HashMap<String, String> hashmapEmail = new HashMap<>();

		try
		{
			final String[] PROJECTIONEMAIL = new String[] {
					ContactsContract.CommonDataKinds.Email.CONTACT_ID,
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Email.DATA
			};

			ContentResolver cr = context.getContentResolver();
			Cursor cursorEmail = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTIONEMAIL, null, null, null);
			if (cursorEmail != null)
			{
				try
				{
					final int contactIdIndex = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
					final int emailIndex = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
					long contactId;
					String address;
					while (cursorEmail.moveToNext())
					{
						contactId = cursorEmail.getLong(contactIdIndex);
						address = cursorEmail.getString(emailIndex);
						hashmapEmail.put(String.valueOf(contactId), address);
/*Log.v("CONTACTS DETAIL", contactId + " ** " + displayName + " ** " + address);*/
					}
				} finally {
					cursorEmail.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hashmapEmail;
	}
	
	/**
	 * use to replace null value with blank value 
	 */
	public static String getValidAPIStringResponse(String value)
	{
		if(value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("<null>"))
		{
			value = "";
		}
		return value.trim();
	}

	public static String removeDiacriticalMarks(String string)
	{
		try {
			return getValidAPIStringResponse(Normalizer.normalize(string, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return getValidAPIStringResponse(string);
		}
	}
	
	/**
	 * use to get valid boolean from string 
	 */
	public static boolean getValidAPIBooleanResponse(String value)
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
	
	/**
	 * use to get valid integer from string 
	 * @param value
	 * @return
	 */
	public static int getValidAPIIntegerResponse(String value)
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
	
	/**
	 * use to get valid double from string 
	 * @param value
	 * @return
	 */
	public static double getValidAPIDoubleResponse(String value)
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
	
	/**
	 * use to get valid long from string 
	 * @param value
	 * @return
	 */
	public static long getValidAPILongResponse(String value)
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
	
	/**
	 * get possibility in boolean from 0 or 1
	 * @param number 0 or 1
	 * @return true if number is 1 else false
	 */
	public static boolean getFlagFromInt(int number)
	{
		boolean flag = (number == 1) ? true : false;
		return flag;
	}
	
	/**
	 * get current date in dd MMM, yyyy format
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateString()
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
	
	/**
	 * get string in dd MMM, yyyy format from millis
	 * @param l millis
	 * @return date string
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertDateToString(long l)
	{
		String str = "";
		try {
			Date date = new Date(l);
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm: aa dd MMM, yyyy");
			str = dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	@SuppressLint("SimpleDateFormat")
	public static String convertDateToTime(long l)
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
	
	/**
	 * get local time timestamp
	 * @param time timestamp to convert
	 * @return local timestamp
	 */
	public static long getLocalTimestamp(final long time)
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
	public static String getRelativeDateTime(final Activity activity, final long timestamp)
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

	public static String getDateCurrentTimeString(long timestamp) {
		try{
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getDefault();
			calendar.setTimeInMillis(timestamp * 1000);
			calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			Date currenTimeZone = (Date) calendar.getTime();
			return sdf.format(currenTimeZone);
		}catch (Exception e) {
		}
		return "";
	}

	public static String getDateFromTimestamp(long timestamp) {
		try{
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getDefault();
			calendar.setTimeInMillis(timestamp * 1000);
			calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date currenTimeZone = (Date) calendar.getTime();
			return sdf.format(currenTimeZone);
		}catch (Exception e) {
		}
		return "";
	}

	public static String getRelativeDateTimeForChat(final Activity activity, final long date)
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
	
	/**
	 * check if url is valid or not
	 * @param url
	 * @return true if valid else false
	 */
	public static boolean validateWebUrl(CharSequence url)
	{
		try {
			return android.util.Patterns.WEB_URL.matcher(url).matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * check if password is valid or not
	 * @param password
	 * @return true if valid else false
	 */
	public static boolean validPassword(String password)
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
	
	/**
	 * hide virtual keyboard
	 * @param view edittext
	 * @param activity context
	 */
	public static void hideKeyboard(View view, Activity activity)
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

	public static void hideKeyboardNew(Activity activity)
	{
		try
		{
			//val view = activity.findViewById<View>(android.R.id.content)
			View view = activity.getCurrentFocus();
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * show virtual keyboard
	 * @param activity context
	 */
	public static void showKeyboard(EditText editText, Activity activity)
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

	public static void showKeyboardNew(EditText editText, Activity activity)
	{
		try
		{
			editText.requestFocus();
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * get string with every first letter of word in capital
	 * @param s
	 * @return string
	 */
	public static String toDisplayCase(String s)
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

	public static String toFirstCapitalString(String s)
	{
		String returnString = "";
		try {
			returnString = s.substring(0,1).toUpperCase() + s.substring(1, s.length());
			return returnString;
		} catch (Exception e) {
			return s;
		}
	}
	
	public static ArrayList<String> sortStringList(ArrayList<String> result)
	{
		try 
		{
			Collections.sort(result, new Comparator<String>()
			{
				@Override
				public int compare(String o1, String o2)
				{
					return (o1.toLowerCase(Locale.getDefault()).compareTo(o2.toLowerCase(Locale.getDefault())));
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/*public static ArrayList<ConversationPojo> sortConversationsDateWise(ArrayList<ConversationPojo> result)
	{
		try 
		{
			Collections.sort(result, new Comparator<ConversationPojo>()
			{
				@Override
				public int compare(ConversationPojo o1, ConversationPojo o2)
				{
					if(o1.getLastMessageTime() > o2.getLastMessageTime())
					{
						return -1;
					}
					else if(o1.getLastMessageTime() < o2.getLastMessageTime())
					{
						return +1;
					}
			        return 0;
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<MessagePojo> sortMessagesDateWise(ArrayList<MessagePojo> result)
	{
		try
		{
			Collections.sort(result, new Comparator<MessagePojo>()
			{
				@Override
				public int compare(MessagePojo o1, MessagePojo o2)
				{
					if(o1.getLastMessageTime() > o2.getLastMessageTime())
					{
						return +1;
					}
					else if(o1.getLastMessageTime() < o2.getLastMessageTime())
					{
						return -1;
					}
					return 0;
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<MonthPojo> sortMonthDateWise(ArrayList<MonthPojo> result)
	{
		try
		{
			Collections.sort(result, new Comparator<MonthPojo>()
			{
				@Override
				public int compare(MonthPojo o1, MonthPojo o2)
				{
					if(o1.getMonthMillis() > o2.getMonthMillis())
					{
						return -1;
					}
					else if(o1.getMonthMillis() < o2.getMonthMillis())
					{
						return +1;
					}
					return 0;
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}*/
	
	/**
	 * get dp value from pixels
	 * @param context activity context
	 * @param px pixels
	 * @return dp
	 */
	public static float dpFromPx(final Context context, final float px) {
	    return px / context.getResources().getDisplayMetrics().density;
	}

	/**
	 * get pixels value from dp
	 * @param context activity context
	 * @param dp dp
	 * @return pixels
	 */
	public static float pxFromDp(final Context context, final float dp) {
	    return dp * context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * get text in capital style
	 * @param text
	 * @return capital text
	 */
	public static String getCapitalText(String text)
	{
		String desiredText = "";
		try {
			desiredText = text.toUpperCase(Locale.ENGLISH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desiredText;
	}
	
	/**
	 * get encoded string in md5 format
	 * @param input text to encode
	 * @return encoded string
	 */
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	public static String universalDateConvert(String str, String FromFormat, String ToFormat) {
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
	public static String getLastBitFromUrl(final String url)
	{
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}

	public static Typeface getTypefaceRegular(final Activity activity)
	{
		return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_regular));
	}

	public static Typeface getTypefaceRegular(final Context context)
	{
		return Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular));
	}

	public static Typeface getTypefaceSemiBold(final Activity activity)
	{
		return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_semibold));
	}
	
	public static Typeface getTypefaceBold(final Activity activity)
	{
		return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_bold));
	}

	private static final int WIDTH_INDEX = 0;
	private static final int HEIGHT_INDEX = 1;

	public static int[] getScreenSize(Context context) {
		int[] widthHeight = new int[2];
		widthHeight[WIDTH_INDEX] = 0;
		widthHeight[HEIGHT_INDEX] = 0;

		try {
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();

			Point size = new Point();
			display.getSize(size);
			widthHeight[WIDTH_INDEX] = size.x;
			widthHeight[HEIGHT_INDEX] = size.y;

			if (!isScreenSizeRetrieved(widthHeight))
            {
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                widthHeight[0] = metrics.widthPixels;
                widthHeight[1] = metrics.heightPixels;
            }

			// Last defense. Use deprecated API that was introduced in lower than API 13
			if (!isScreenSizeRetrieved(widthHeight)) {
                widthHeight[0] = display.getWidth(); // deprecated
                widthHeight[1] = display.getHeight(); // deprecated
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return widthHeight;
	}

	private static boolean isScreenSizeRetrieved(int[] widthHeight) {
		return widthHeight[WIDTH_INDEX] != 0 && widthHeight[HEIGHT_INDEX] != 0;
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;
        try 
        {
			if (drawable instanceof BitmapDrawable)
			{
			    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			    if(bitmapDrawable.getBitmap() != null) {
			        return bitmapDrawable.getBitmap();
			    }
			}
			if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) 
			{
			    bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
			}
			else 
			{
			    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			}
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return bitmap;
    }
	
	public static void shareCodeSpecificApp(final String title, final String message, final Activity activity)
	{
		try {
			Intent emailIntent = new Intent();
			emailIntent.setAction(Intent.ACTION_SEND);
			// Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
			emailIntent.putExtra(Intent.EXTRA_TEXT, message);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
			emailIntent.setType("message/rfc822");

			PackageManager pm = activity.getPackageManager();
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");

			Intent openInChooser = Intent.createChooser(emailIntent, "Share via");

			List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
			List<LabeledIntent> intentList = new ArrayList<>();
			for (int i = 0; i < resInfo.size(); i++)
			{
				// Extract the label, append it, and repackage it in a LabeledIntent
				ResolveInfo ri = resInfo.get(i);
				String packageName = ri.activityInfo.packageName;
				if(packageName.contains("android.email"))
				{
					emailIntent.setPackage(packageName);
				}
				else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("whatsapp")
						|| packageName.contains("com.jio.join") || packageName.contains("com.google.android.apps.messaging") || packageName.contains("com.google.android.talk"))
				{
					Intent intent = new Intent();
					intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
					intent.setAction(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TEXT, message);

					intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
				}
			}

			// convert intentList to array
			LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

			openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
			activity.startActivity(openInChooser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showSnackBar(Activity activity, View v, String message)
	{
		try {
			int color = ContextCompat.getColor(activity, R.color.colorAccent);

			Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);

			View sbView = snackbar.getView();
			TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
			textView.setTextColor(Color.parseColor("#FFFFFF"));
			textView.setTypeface(AppUtils.getTypefaceRegular(activity));
			sbView.setBackgroundColor(color);

			snackbar.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showToast(Activity activity, String msg)
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

	public static void apiFailedToast(Activity activity)
	{
		try {
			if(activity != null)
			{
				LayoutInflater inflater = activity.getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast, null);

				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(activity.getString(R.string.api_failed_message));

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

	public static String getEncodedText(String text)
	{
		String encodedText = "";

		try {
			byte[] data = text.getBytes("UTF-8");
			encodedText = Base64.encodeToString(data, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
			encodedText = text;
		}

		return encodedText;
	}

	public static String getDecodedText(String text)
	{
		String decodedText = "";

		try {
			byte[] data = Base64.decode(text, Base64.DEFAULT);
			decodedText = new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			decodedText = text;
		}

		return decodedText;
	}

	/*public static void saveDeviceInfo(final Activity activity)
	{
		try {
			final SessionManager sessionManager = new SessionManager(activity);

			try
			{
				PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
				String currentAppVersion = pInfo.versionName;
				sessionManager.setDeviceAppVersion(currentAppVersion);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			String android_id = AppUtils.getValidAPIStringResponse(sessionManager.getUserDeviceId());
			if(android_id.length() == 0)
			{
				android_id = AppUtils.getValidAPIStringResponse(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
				sessionManager.setUserDeviceId(android_id);
			}

			String phoneModel = AppUtils.getValidAPIStringResponse(sessionManager.getDeviceModelName());
			if(phoneModel.length() == 0)
			{
				// Device model
				phoneModel = Build.MODEL;
				sessionManager.setDeviceModelName(phoneModel);
			}

			String androidVersion = AppUtils.`lidAPIStringResponse(sessionManager.getDeviceOS());
			if(androidVersion.length() == 0)
			{
				// Android version
				androidVersion = Build.VERSION.RELEASE;
				sessionManager.setDeviceOS(androidVersion);
			}
			*//*Toast.makeText(activity, "Model : " + phoneModel + " & OS : " + androidVersion, Toast.LENGTH_LONG).show();*//*
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static void writeToCustomFileHashmap(final String data, final String filename)
	{
		try
		{
			File myFileDir = new File(Environment.getExternalStorageDirectory() + "/DentalApp/text/");
			if(!myFileDir.exists())
			{
				myFileDir.mkdirs();
			}

			File myFile = new File(Environment.getExternalStorageDirectory() + "/DentalApp/text/" +  filename);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
			outputStreamWriter.write(data);
			outputStreamWriter.close();
			// Toast.makeText(getApplicationContext(), "File saved on " + myFile.getAbsolutePath() + " ", Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
		}
	}

	public static void animateHide(final Activity activity, final View view)
	{
		try {
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.activity_fade_out);
			view.startAnimation(animation);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					try {
						view.setVisibility(View.GONE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void animateShow(final Activity activity, final View view)
	{
		try {
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.activity_fade_in);
			animation.setDuration(500);
			view.startAnimation(animation);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation)
				{
					try {
						view.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onAnimationEnd(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeError(EditText edt, final TextInputLayout layout) {
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

	public static void noInternetSnakeBar(Activity activity)
	{
		showSnackBarNEW(activity.getCurrentFocus(),activity.getString(R.string.network_failed_message),activity);
	}

	//this method is for fragments
	public static void noInternetSnakeBar(Activity activity, View view)
	{
		showSnackBarNEW(view,activity.getString(R.string.network_failed_message),activity);
	}

	public static void showSnackBarNEW(View v, String message, Activity activity)
	{
		try {
			int color = ContextCompat.getColor(activity, R.color.colorAccent);

			Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);

			View sbView = snackbar.getView();
			TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
			textView.setTextColor(Color.parseColor("#FFFFFF"));
			textView.setTypeface(AppUtils.getTypefaceRegular(activity));
			sbView.setBackgroundColor(color);

			snackbar.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startActivityAnimation(Activity activity)
	{
		MitsUtils.hideKeyboard(activity);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public static void finishActivityAnimation(Activity activity)
	{
		MitsUtils.hideKeyboard(activity);
		activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

	}

	public static String getCommaSaperatedStringFromArrayList(ArrayList<String> list)
	{
		StringBuilder commaSepValueBuilder = new StringBuilder();

		//Looping through the list

		if(list.size()>0)
		{
			for ( int i = 0; i< list.size(); i++)
			{
				//append the value into the builder
				commaSepValueBuilder.append(list.get(i));

				//if the value is not the last element of the list
				//then append the comma(,) as well
				if ( i != list.size()-1){
					commaSepValueBuilder.append(",");
				}
			}

			return commaSepValueBuilder.toString().trim();
		}
		else
		{
			return "";
		}
	}

	public static List<String> getListFromCommaSeperatedString(String str)
	{
		List<String> newList = new ArrayList<>();
		if(str.equalsIgnoreCase(""))
		{
			return newList;
		}
		else
		{
			return  Arrays.asList(str.split(","));
		}
	}

	public static void storeJsonResponse(String response , String filename)
	{
		try
		{
			String folderName = AppUtils.getCurrentDateString();
			File f1 = new File(Environment.getExternalStorageDirectory() + "/" + "Unison", folderName);//Make directory like : Unison -> 07 Dec, 2018
			if (!f1.exists()) {
				f1.mkdirs();
			}
			String time = convertDateToString(System.currentTimeMillis());
			File myFile = new File(f1.getPath()+"/"+filename+"_"+time+"_"+System.currentTimeMillis()+".txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(response.toString());
			myOutWriter.close();
			fOut.close();
			Log.i("Store Response", "Successfully Stored.");
		}
		catch (Exception e)
		{
			Log.e("Store Response", "Failed to Stored.");
			e.printStackTrace();
		}
	}

	/*For Store list in GSON Format*/
	public static String getStringFromArrayListVariations(ArrayList<VariationResponse.VariationsBean> list)
	{
		return new Gson().toJson(list);
	}

	public static ArrayList<VariationResponse.VariationsBean> getArrayListFromJsonStringVariation(String jsonString)
	{
		return new Gson().fromJson(jsonString, new TypeToken<List<VariationResponse.VariationsBean>>(){}.getType());
	}

	public static String removeLastComma(String str)
	{
		if(str.equalsIgnoreCase(""))
		{
			return  "";
		}
		else
		{
			if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',')
			{
				str = str.substring(0, str.length() - 1);
			}
			return str;
		}
	}

	public static DisplayReports.DataBean dataOfReport(String reportName,Activity activity)
	{
		DisplayReports.DataBean bean = new DisplayReports.DataBean();
		SessionManager sessionManager = new SessionManager(activity);
		try
		{
			if(!sessionManager.getAllReportLink().equalsIgnoreCase(""))
			{
				Log.e("FROM SESSION >>>> ", "dataOfReport: "+sessionManager.getAllReportLink() );

				JSONObject jsonObject = new JSONObject(sessionManager.getAllReportLink());
				JSONArray reportArray = jsonObject.getJSONArray("data");
				if(reportArray.length()>0)
				{
					for (int i = 0; i < reportArray.length(); i++)
					{
						JSONObject objReport = (JSONObject) reportArray.get(i);
						if(objReport.getString("report").equalsIgnoreCase(reportName))
						{
							String report = objReport.getString("report");
							String url = objReport.getString("url");
							JSONArray array = objReport.getJSONArray("params");
							List<String> listParams = new ArrayList<>();
							for (int j = 0; j < array.length(); j++)
							{
								listParams.add(array.getString(j));
							}
							bean.setParams(listParams);
							bean.setReport(report);
							bean.setUrl(url);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/*For store session*/

	public static void storeSession(final Activity activity,final ApiInterface apiInterface)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				SessionManager sessionManager = new SessionManager(activity);

				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("employee_id",sessionManager.getUserId());
				hashMap.put("login_user_id",sessionManager.getUserId());
				String string = MitsUtils.readJSONServiceUsingPOST(ApiClient.STORE_SESSION,hashMap);
				Log.e("String REs > ", "run: "+string );
				try {
					JSONObject jsonObject = new JSONObject(string);

					if(jsonObject.optInt("success")==1)
					{

						String str = MitsUtils.readJSONServiceUsingGET(ApiClient.GET_SESSION);

						Log.e("getSession Res >> ", "onResponse: "+str );
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				/*Call<CommonResponse> storeSessionCall = apiInterface.storeSession(sessionManager.getUserId());
				storeSessionCall.enqueue(new Callback<CommonResponse>()
				{
					@Override
					public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        Log.e("Response >> ", "onResponse: "+new Gson().toJson(response.body()));

                        String str = MitsUtils.readJSONServiceUsingGET(ApiClient.GET_SESSION);

						Log.e("getSession Res >> ", "onResponse: "+str );
					}

					@Override
					public void onFailure(Call<CommonResponse> call, Throwable t) {
						Log.e("Failed  >> ", "onResponse: " +t.getMessage());
					}
				});*/
			}
		}).start();
	}

	public static void destroySession()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				MitsUtils.readJSONServiceUsingGET(ApiClient.DESTROY_SESSION);
			}
		}).start();
	}

	public static void updateDeviceTokenForFCM(final Activity activity,final ApiInterface apiInterface)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				String token = FirebaseInstanceId.getInstance().getToken();
				SessionManager sessionManager = new SessionManager(activity);
				sessionManager.saveTokenId(token);
				Call<CommonResponse> storeSessionCall = apiInterface.updateDeviceToken(sessionManager.getUserId(),sessionManager.getTokenId(),"android",sessionManager.getUserId());
				storeSessionCall.enqueue(new Callback<CommonResponse>()
				{
					@Override
					public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
					{

					}

					@Override
					public void onFailure(Call<CommonResponse> call, Throwable t) {
						Log.e("Failed  >> ", "onResponse: " +t.getMessage());
					}
				});
			}
		}).start();
	}

	public static String getNextDate(String  curDate)
	{
		String nextDate = "";
		final SimpleDateFormat sformat;
		final Calendar calendar;
		try {
			sformat = new SimpleDateFormat("dd MMM, yyyy");
			final Date date = sformat.parse(curDate);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			nextDate = sformat.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return nextDate;
	}
	public static boolean isLastDateOfMonth(String  curDate)
	{
		boolean isLastDate = false;
		final SimpleDateFormat sformat;
		final Calendar calendar;
		try {
			sformat = new SimpleDateFormat("dd MMM, yyyy");
			final Date date = sformat.parse(curDate);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			int lastDate = calendar.getActualMaximum(Calendar.DATE);

			Log.e("lastDate >> ", "isLastDateOfMonth: "+lastDate );

			String currentDate = AppUtils.universalDateConvert(curDate,"dd MMM, yyyy","dd");
			int dateInt = Integer.parseInt(currentDate);


			Log.e("dateInt >> ", "isLastDateOfMonth: "+dateInt );

			if(lastDate==dateInt)
			{
				isLastDate = true;
			}
			else
			{
				isLastDate = false;
			}


		} catch (ParseException e) {
			e.printStackTrace();
		}

		return isLastDate;
	}

	public static void showToastInsideThread(final Activity activity, final String text)
	{
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToast(activity,text);
			}
		});
	}

	public static int getTotalHoursBetweenTimes(final String startTime,final String endTime)
	{
		int hours = 0;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			Date date1 = simpleDateFormat.parse(startTime);
			Date date2 = simpleDateFormat.parse(endTime);

			long difference = date2.getTime() - date1.getTime();
			int days = (int) (difference / (1000*60*60*24));
			hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
			int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
			hours = (hours < 0 ? -hours : hours);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return hours;
	}

	public static boolean isJobServiceOn( Context context,int JOB_ID)
	{
		boolean hasBeenScheduled = false ;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

			for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
				if ( jobInfo.getId() == JOB_ID ) {
					hasBeenScheduled = true ;
					break ;
				}
			}
		}
		return hasBeenScheduled ;
	}

	public static SpringyAdapterAnimator springLibAnimForRecyclerView(SpringyAdapterAnimator mAnimator, RecyclerView recyclerView)
	{
		mAnimator = new SpringyAdapterAnimator(recyclerView);
		//mAnimator.setSpringAnimationType( SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
		//mAnimator.addConfig(85,15);
		return mAnimator;
	}

	public static boolean isAppRunning(final Context context, final String packageName) {
		final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		if (procInfos != null) {
			for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) { 
				if (processInfo.processName.equals(packageName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isServiceRunning(Class<?> serviceClass,Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void showLoadingToast(Activity activity)
	{
		showToast(activity,"Loading Please Wait...");
	}

	public static void showVersionMismatchDialog(final Activity activity,final boolean isForcefullyUpdate)
	{
		final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
		dialog.setContentView(sheetView);

		TextView tvHeader = sheetView.findViewById(R.id.tvHeader);
		tvHeader.setText("Update Require");
		TextView tvDescription = sheetView.findViewById(R.id.tvDescription);
		tvDescription.setText("New Version of unison is available on play store, please click 'Update' to download it.");

		TextView tvConfirm = sheetView.findViewById(R.id.tvConfirm);
		tvConfirm.setText("Update");
		TextView tvCancel = sheetView.findViewById(R.id.tvCancel);
		tvCancel.setText("Cancel");

		final SessionManager sessionManager = new SessionManager(activity);

		if(isForcefullyUpdate)
		{
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
		}
		else
		{
			dialog.setCanceledOnTouchOutside(true);
			dialog.setCancelable(true);
		}

		dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(isForcefullyUpdate)
				{
					if(sessionManager.isNetworkAvailable())
					{
						String PACKAGE_NAME = activity.getApplicationContext().getPackageName();
						activity.finishAffinity();
						activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME )));
					}
					else
					{
						AppUtils.noInternetSnakeBar(activity);
					}
				}
				else
				{
					dialog.dismiss();
					if(sessionManager.isNetworkAvailable())
					{
						String PACKAGE_NAME = activity.getApplicationContext().getPackageName();

						activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME )));
					}
					else
					{
						AppUtils.noInternetSnakeBar(activity);
					}
				}
			}
		});

		dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if(isForcefullyUpdate)
				{

				}
				else
				{
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}

	public static void showHideBottomButtons(Activity activity,
									   TextView tvSave,
									   TextView tvConfirm,
									   TextView tvApprove,
									   boolean isConfirm,
									   boolean isApprove,
									   String selectedStaffId)
	{
		SessionManager sessionManager = new SessionManager(activity);

		if(isApprove)
		{
			tvSave.setVisibility(View.GONE);
		}
		else
		{
			tvSave.setVisibility(View.VISIBLE);
		}

		if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
		{
			tvApprove.setVisibility(View.GONE);

			if(isConfirm)
			{
				tvConfirm.setVisibility(View.GONE);
			}
			else
			{
				tvConfirm.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			if(sessionManager.getUserId().equalsIgnoreCase(selectedStaffId))
			{
				if(isConfirm)
				{
					tvConfirm.setVisibility(View.GONE);
				}
				else
				{
					tvConfirm.setVisibility(View.VISIBLE);
				}
				tvApprove.setVisibility(View.GONE);
			}
			else
			{
				if(isApprove)
				{
					tvApprove.setVisibility(View.GONE);
				}
				else
				{
					tvApprove.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	public static String getDeviceInfo()
	{
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String versionRelease = Build.VERSION.RELEASE;

		return manufacturer+" "+model+" ("+versionRelease+")";
	}

	public static String getRoundupLastTwoDigits(Double d)
	{
		String value = "0";
		DecimalFormat f = null;
		try {
			f = new DecimalFormat("##.00");
			value = f.format(d);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}