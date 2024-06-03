package com.unisonpharmaceuticals.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tushar Vataliya on 21-Jun-18.
 */
public class ApiClient
{
    public static final String WEBSITE_URL = "http://www.unisonpharmaceuticals.com/";

    //public static final String MAIN_URL = "http://192.168.50.62/unison/";
    public static final String MAIN_URL = "https://reporting.unisonpharmaceuticals.com/"; //for live

    public static final String BASE_URL = MAIN_URL +"api/services/"; // for live
    //public static final String BASE_URL = MAIN_URL +"api/services_v3/"; // for demo

    /*UserType*/
    public static String MANAGER = "manager";
    public static String ADMIN = "admin";
    public static String MR = "mr";

    /*Report Names*/
    public static String TOURPLAN_REPORT = "Tour Plan Report";

    /*Report Direct Links*/
    public static String DCR_REPORT = MAIN_URL + "reports/dcr.php?from_app=true&";
    public static String TOURPLANMAIN_REPORT = MAIN_URL + "reports/tour_plan.php?from_app=true&";
    public static String PLANNER_ENTRY_REPORT = MAIN_URL + "reports/planner_report.php?from_app=true&";
    public static String NOT_SEEN_REPORT = MAIN_URL + "reports/not_seen.php?from_app=true&";
    public static String GIFT_REPORT = MAIN_URL + "reports/gift_report.php?from_app=true&";
    public static String TRAVELLING_REPORT = MAIN_URL + "reports/travelling_report.php?from_app=true&";
    public static String TRAVELLING_REPORT_DOWNLOAD = MAIN_URL + "reports/travelling_report_pdf.php?from_app=true&";
    public static String DR_SUMMARY_REPORT = MAIN_URL + "reports/summaryOfDoctor.php?from_app=true&";
    public static String DR_LIST_REPORT = MAIN_URL + "reports/doctorList.php?from_app=true&";
    public static String SAMPLE_SUMMARY_REPORT = MAIN_URL + "reports/sample_summary.php?from_app=true&";
    public static String SUBMIT_ENTRIES = BASE_URL + "dailycall/multiple";
    public static String SALES_UPDATE = BASE_URL + "sales_update/update";
    public static String SAMPLE_UPDATE = BASE_URL + "sample_update/update";
    public static String SAVE_TOURPLAN = BASE_URL + "tourplan/saveTourplan";
    public static String SAVE_EXTRA_TOURPLAN = BASE_URL + "tourplan/saveTourplanExtra";
    public static String CONFIRM_TOURPLAN = BASE_URL + "tourplan/confirmTourplan";
    public static String APPLY_LEAVE = BASE_URL + "admin/applyLeave";
    public static String SAVE_DAILY_PLANNER = BASE_URL + "dailyCall/saveTourPlanner";

    //For DestroySession- this is used for display report view counts in web reports
    public static String DESTROY_SESSION = BASE_URL + "admin/distroySession";
    public static String GET_SESSION = BASE_URL + "admin/getSession";
    public static String STORE_SESSION = BASE_URL + "admin/storeSession";

    public static String SAMPLE_REASON_ID = "4";
    public static String SAMPLE_REASON_CODE = "R";
    public static String SAMPLE_REASON = "Regular Reason";

    public static String REFUSE_REASON_ID = "11";
    public static String REFUSE_REASON_CODE = "F";
    public static String REFUSE_REASON = "Refuse sample";

    private static Retrofit retrofit = null;
    public static Retrofit getClient()
    {
        if (retrofit==null)
            {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100,TimeUnit.SECONDS)
                        .build();


                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

        return retrofit;
    }

    //Other Important
    /*0: normal*/
    /*1 : extra work*/
    /*2 : leave*/
    /*3 : holiday*/

    /*Month*/
    public static String PAST = "Past";
    public static String CURRENT = "Current";
    public static String COMING = "Coming";

    /*For Notification*/
    public static final String NOTIF_TP = "tourPlan";
    public static final String NOTIF_DCR = "dcr";
    public static final String NOTIF_GIFT = "gift";
    public static final String NOTIF_TA = "travelling";
    public static final String NOTIF_NS = "notSeen";
    public static final String NOTIF_OTHER = "other";

    /*For ReportTitle*/
    public static final String REPORT_DRC = "DCR Report";
    public static final String REPORT_PLANNER= "Planner Report";
    public static final String REPORT_TP = "Tour Plan Report";
    public static final String REPORT_GIFT = "Gift Report";
    public static final String REPORT_TA = "Travelling Allowance Report";
    public static final String REPORT_NOTSEEN = "Not Seen Report";
    public static final String REPORT_DR_SUMMARY = "Doctor Summary Report";
    public static final String REPORT_SAMPLE = "Sample Report";
    public static final String REPORT_DR_LIST = "Doctor List Report";

    /*For Avoid multiple click on same view*/
    public static final int CLICK_THRESHOLD = 1000;
}
