package com.unisonpharmaceuticals.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MitsUtils {
    private static final String TAG = "MitsUtilityClass";

    public MitsUtils() {
    }

    public static void closeKeyboard(Activity activity, View view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void closeKeyboard(Activity activity, EditText view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void closeKeyboard(Activity activity, AutoCompleteTextView view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void openKeyboard(View view, Activity activity) {
        try {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
            imm.toggleSoftInput(2, 0);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void openKeyboard(EditText editText, Activity activity) {
        try {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
            imm.toggleSoftInput(2, 0);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void openKeyboard(AutoCompleteTextView view, Activity activity) {
        try {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
            imm.toggleSoftInput(2, 0);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static Double roundOffTo2DecimalPlaces(String d) {
        double data = 0.0D;

        try {
            DecimalFormat f = new DecimalFormat("##.00");
            data = Double.parseDouble(f.format(Double.parseDouble(d)));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return data;
    }

    @SuppressLint({"DefaultLocale"})
    public static String roundOffTo2DecimalPlaces(float val) {
        return String.format("%.2f", val);
    }

    @SuppressLint({"DefaultLocale"})
    public static String roundOffTo2DecimalPlaces(double val) {
        return String.format("%.2f", val);
    }

    public static Double roundOff(String d) {
        double data = 0.0D;

        try {
            data = (double)Math.round(Double.parseDouble(d));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return data;
    }

    public static String readFileFromAsstes(Activity activity, String assetFilePath) {
        StringBuilder buf = new StringBuilder();

        try {
            InputStream json = activity.getAssets().open(assetFilePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;
            while((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return buf.toString();
    }

    public static void writeToFile(String data, String path, String fileName) {
        try {
            File myFile = new File(Environment.getExternalStorageDirectory() + path);
            if (!myFile.exists()) {
                myFile.mkdirs();
            }

            myFile = new File(Environment.getExternalStorageDirectory() + path + fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public static String getIPAddress() {
        String ip = "192.168.1.1";

        try {
            ip = ("" + ip()).replace("/", "");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return ip;
    }

    public static InetAddress ip() throws SocketException {
        Enumeration nis = NetworkInterface.getNetworkInterfaces();

        while(true) {
            NetworkInterface ni;
            do {
                do {
                    if (!nis.hasMoreElements()) {
                        return null;
                    }

                    ni = (NetworkInterface)nis.nextElement();
                } while(ni.isLoopback());
            } while(!ni.isUp());

            Iterator var3 = ni.getInterfaceAddresses().iterator();

            while(var3.hasNext()) {
                InterfaceAddress ia = (InterfaceAddress)var3.next();
                if (ia.getAddress().getAddress().length == 4) {
                    return ia.getAddress();
                }
            }
        }
    }

    public static String toDisplayCase(String s) {
        String strToReturn = "";

        try {
            StringBuffer res = new StringBuffer();
            String[] strArr = s.split(" ");
            String[] var15 = strArr;
            int var14 = strArr.length;

            for(int var13 = 0; var13 < var14; ++var13) {
                String str = var15[var13];
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");
            }

            strToReturn = res.toString();
        } catch (Exception var10) {
            var10.printStackTrace();
            String ACTIONABLE_DELIMITERS = " -/";
            StringBuilder sb = new StringBuilder();
            boolean capNext = true;
            char[] var9;
            int var8 = (var9 = s.toCharArray()).length;

            for(int var7 = 0; var7 < var8; ++var7) {
                char c = var9[var7];
                c = capNext ? Character.toUpperCase(c) : Character.toLowerCase(c);
                sb.append(c);
                capNext = " -/".indexOf(c) >= 0;
            }

            strToReturn = sb.toString();
        }

        return strToReturn;
    }

    public static void removeDuplicateFromArrayList(ArrayList arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        Iterator iter = arlList.iterator();

        while(iter.hasNext()) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }

        arlList.clear();
        arlList.addAll(newList);
    }

    public static void setListHeight(ListView listView, BaseAdapter adapter) {
        try {
            if (adapter == null) {
                return;
            }

            int totalHeight = 0;

            for(int i = 0; i < adapter.getCount(); ++i) {
                View listItem = adapter.getView(i, (View)null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static <T> void setListHeight(ListView listView, ArrayAdapter<T> adapter) {
        try {
            if (adapter == null) {
                return;
            }

            int totalHeight = 0;

            for(int i = 0; i < adapter.getCount(); ++i) {
                View listItem = adapter.getView(i, (View)null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static void setListHeightForSameHeightOfViews(ListView listView, BaseAdapter adapter) {
        try {
            if (adapter == null) {
                return;
            }

            View listItem = adapter.getView(0, (View)null, listView);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();
            int totalHeight = height * adapter.getCount();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static <T> void setListHeightForSameHeightOfViews(ListView listView, ArrayAdapter<T> adapter) {
        try {
            if (adapter == null) {
                return;
            }

            View listItem = adapter.getView(0, (View)null, listView);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();
            int totalHeight = height * adapter.getCount();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void setGridHeight(GridView gridView, BaseAdapter adapter, Integer numColumn, Activity activity) {
        try {
            if (adapter == null) {
                return;
            }

            int height = adapter.getView(0, (View)null, gridView).getMeasuredHeight();
            int count = adapter.getCount();

            for(int i = 0; i < count; ++i) {
                View listItem = adapter.getView(i, (View)null, gridView);
                listItem.measure(0, 0);
                height = listItem.getMeasuredHeight();
            }

            Resources r = activity.getResources();
            float px = TypedValue.applyDimension(1, 10.0F, r.getDisplayMetrics());
            int column = count / numColumn;
            int module = count % numColumn;
            if (module > 0) {
                ++column;
            }

            int totalHeight = height * column + (int)px * column;
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
            gridView.requestLayout();
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    public static int dpToPx(Resources res, int dp) {
        return (int)TypedValue.applyDimension(1, (float)dp, res.getDisplayMetrics());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(1, dp, resources.getDisplayMetrics());
        return (int)px;
    }

    public static int dpToPx(int dp) {
        return (int)((float)dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int)((float)px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setMaxLengthOfTextView(TextView textView, int maxLength) {
        if (textView == null) {
            throw new NullPointerException("TextView cannot be null");
        } else {
            InputFilter[] fArray = new InputFilter[]{new LengthFilter(maxLength)};
            textView.setFilters(fArray);
        }
    }

    public static boolean isSdCardMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals("mounted");
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        } else {
            String base64StringOfBitmap = null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, stream);
            byte[] imageBitmap = stream.toByteArray();
            base64StringOfBitmap = Base64.encodeToString(imageBitmap, 0);
            return base64StringOfBitmap;
        }
    }

    public static byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap resizeImage(Bitmap sourceBitmap, int newWidth, int newHeight, boolean filter) {
        if (sourceBitmap == null) {
            throw new NullPointerException("Bitmap to be resized cannot be null");
        } else {
            Bitmap resized = null;
            if (sourceBitmap.getWidth() < sourceBitmap.getHeight()) {
                Bitmap.createScaledBitmap(sourceBitmap, newHeight, newWidth, true);
            } else {
                Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, true);
            }

            resized = Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, true);
            return resized;
        }
    }


    public static String getApplicationVersionNumber(Context ctx) {
        String versionName = null;

        try {
            versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return versionName;
    }

    public static int getApplicationVersionCode(Context ctx) {
        int versionCode = 0;

        try {
            versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return versionCode;
    }

    public static String getOsVersion() {
        return VERSION.RELEASE;
    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher != null ? matcher.matches() : false;
    }

    public static boolean isValidateEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidURL(String url) {
        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException var4) {
            return false;
        }

        try {
            u.toURI();
            return true;
        } catch (URISyntaxException var3) {
            return false;
        }
    }

    public static String getApplicationName(Context ctx) throws NameNotFoundException {
        if (ctx == null) {
            throw new NullPointerException("Context cannot be null");
        } else {
            PackageManager packageMgr = ctx.getPackageManager();
            ApplicationInfo appInfo = null;

            try {
                appInfo = packageMgr.getApplicationInfo(ctx.getPackageName(), 0);
            } catch (NameNotFoundException var4) {
                throw new NameNotFoundException(var4.getMessage());
            }

            String applicationName = (String)(appInfo != null ? packageMgr.getApplicationLabel(appInfo) : "UNKNOWN");
            return applicationName;
        }
    }

    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        return Color.argb(255, red, green, blue);
    }

    public static String formatSize(long size) {
        if (size <= 0L) {
            return "0";
        } else {
            String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int)(Math.log10((double)size) / Math.log10(1024.0D));
            return (new DecimalFormat("#,##0.#")).format((double)size / Math.pow(1024.0D, (double)digitGroups)) + " " + units[digitGroups];
        }
    }

    @SuppressLint({"DefaultLocale"})
    public static String formatSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < (long)unit) {
            return bytes + " B";
        } else {
            int exp = (int)(Math.log((double)bytes) / Math.log((double)unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
            return String.format("%.1f %sB", (double)bytes / Math.pow((double)unit, (double)exp), pre);
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

    public static String getSha512Hash(String stringToHash) {
        return stringToHash == null ? null : getSha512Hash(stringToHash.getBytes());
    }

    public static String getSha512Hash(byte[] dataToHash) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512");
            if (md != null) {
                md.update(dataToHash);
                byte[] byteData = md.digest();
                String base64 = Base64.encodeToString(byteData, 0);
                return base64;
            }
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getExtension(File file) {
        String ext = null;

        try {
            String s = file.getName();
            int i = s.lastIndexOf(46);
            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return ext;
    }

    public static boolean isNetworkOnline(Activity activity) {
        boolean status = false;

        try {
            ConnectivityManager cm = (ConnectivityManager)activity.getSystemService("connectivity");
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                Runtime runtime = Runtime.getRuntime();

                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    if (exitValue == 0) {
                        status = true;
                    } else {
                        status = false;
                    }
                } catch (IOException var7) {
                    var7.printStackTrace();
                    status = false;
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                    status = false;
                }
            } else {
                status = false;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            status = false;
        }

        return status;
    }

    public static boolean isNetworkOnline(Context context) {
        boolean status = false;

        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                Runtime runtime = Runtime.getRuntime();

                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    if (exitValue == 0) {
                        status = true;
                    } else {
                        status = false;
                    }
                } catch (IOException var7) {
                    var7.printStackTrace();
                    status = false;
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                    status = false;
                }
            } else {
                status = false;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            status = false;
        }

        return status;
    }

    public static NodeList readLiveXMLUsingPOST(String xmlUrl, String mainTag, HashMap<String, String> params) {
        NodeList nodelist = null;

        try {
            StringBuilder builder = new StringBuilder();
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(xmlUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            boolean isError = connection.getResponseCode() >= 400;
            InputStream inputStream;
            BufferedReader rd;
            String line;
            if (isError) {
                inputStream = connection.getErrorStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while((line = rd.readLine()) != null) {
                    builder.append(line);
                }

                Log.e("MitsUtils readLiveXMLUsingPOST ERROR STREAM", builder.toString());
            } else {
                inputStream = connection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while((line = rd.readLine()) != null) {
                    builder.append(line);
                }

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(new StringReader(builder.toString())));
                nodelist = doc.getElementsByTagName(mainTag);
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        return nodelist;
    }

    public static NodeList readLiveXMLUsingGET(String xmlUrl, String mainTag) {
        NodeList nodelist = null;

        try {
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            System.setProperty("http.keepAlive", "false");
            URL url = new URL(xmlUrl);
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(con.getInputStream()));
            nodelist = doc.getElementsByTagName(mainTag);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return nodelist;
    }

    public static NodeList readLocalXMLFromAsstes(Activity activity, String assetsPath, String mainTag) {
        NodeList nodelist = null;

        try {
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(activity.getAssets().open(assetsPath)));
            nodelist = doc.getElementsByTagName(mainTag);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return nodelist;
    }

    public static NodeList readLocalXMLFromFilePath(String filePath, String mainTag) {
        NodeList nodelist = null;

        try {
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            InputStream mInputStream = null;
            mInputStream = new FileInputStream(new File(filePath));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(mInputStream));
            nodelist = doc.getElementsByTagName(mainTag);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return nodelist;
    }

    public static NodeList readXMLFromString(String xmlStr, String mainTag) {
        NodeList nodelist = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlStr)));
            nodelist = doc.getElementsByTagName(mainTag);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return nodelist;
    }

    public static String getElementValue(Node elem) {
        try {
            if (elem != null && elem.hasChildNodes()) {
                for(Node child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == 4 || child.getNodeType() == 3) {
                        return child.getNodeValue();
                    }
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return "";
    }

    public static String readJSONServiceUsingGET(String urlString) {
        StringBuilder builder = new StringBuilder();

        try {
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while((line = rd.readLine()) != null) {
                builder.append(line);
            }
        } catch (ConnectException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return builder.toString();
    }

    public static String readJSONServiceUsingPOST(String urlString, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();

        try {
            ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            boolean isError = connection.getResponseCode() >= 400;
            InputStream inputStream;
            BufferedReader rd;
            String line;
            if (isError) {
                inputStream = connection.getErrorStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while((line = rd.readLine()) != null) {
                    builder.append(line);
                }

                Log.e("MitsUtils readJSONServiceUsingPOST ERROR STREAM", builder.toString());
            } else {
                inputStream = connection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while((line = rd.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (ConnectException var12) {
            var12.printStackTrace();
        } catch (IOException var13) {
            var13.printStackTrace();
        }

        return builder.toString();
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        try {
            boolean first = true;
            Iterator var4 = params.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, String> entry = (Entry)var4.next();
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode((String)entry.getValue(), "UTF-8"));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result.toString();
    }

    public static void freeMemory(Activity activity) {
        try {
            deleteDirectoryTree(activity.getCacheDir());
            System.runFinalization();
            Runtime.getRuntime().freeMemory();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void deleteDirectoryTree(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory()) {
                File[] var4;
                int var3 = (var4 = fileOrDirectory.listFiles()).length;

                for(int var2 = 0; var2 < var3; ++var2) {
                    File child = var4[var2];
                    deleteDirectoryTree(child);
                }
            }

            fileOrDirectory.delete();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
