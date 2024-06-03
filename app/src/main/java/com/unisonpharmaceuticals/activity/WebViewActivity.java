package com.unisonpharmaceuticals.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Kiran Patel on 06-Aug-18.
 */
public class WebViewActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    private AdvancedWebView webView;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;
    private LinearLayout llLoading ;

    static String report_url = "";
    String cameFrom = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_webview);
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        report_url = getIntent().getStringExtra("report_url");
        cameFrom = getIntent().getStringExtra("cameFrom");
        Log.e("Report URL >>>  ", "onCreate: "+report_url );
        apiService = ApiClient.getClient().create(ApiInterface.class);
        basicProcesses();
    }

    @Override
    public void initViews()
    {

        AppUtils.storeSession(activity,apiService);

        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.finish();
                //finishActivityAnimation(activity);
                onBackPressed();
            }
        });
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(cameFrom);

        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
        webView = (AdvancedWebView) findViewById(R.id.webView);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);

        llLoading.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        mWebViewClient = new myWebViewClient();
        webView.setWebViewClient(mWebViewClient);

        mWebChromeClient = new myWebChromeClient();
        webView.setWebChromeClient(mWebChromeClient);
        webView.setCookiesEnabled(true);
        webView.setThirdPartyCookiesEnabled(true);
        webView.setMixedContentAllowed(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.loadUrl(report_url);

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
//            AppUtils.writeFileOnInternalStorage(activity,"TA_",contentDisposition);
            Log.e("contentDisposition",contentDisposition);
            Log.e("contentLength", String.valueOf(contentLength));
            Log.e("url", String.valueOf(url));
            Log.e("userAgent", String.valueOf(userAgent));

            startDownload(activity,url,contentDisposition,mimetype);
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                System.out.println("progress = " + progress);
                if(progress == 100)
                {
                    llLoading.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
                else
                {
                    llLoading.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }
            }
        });
    }

    private static void startDownload(Context context, String url, String contentDisposition, String mimetype) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(report_url));
        request.setTitle("Downloading file");
        request.setDescription("Downloading file...");
        request.setMimeType(mimetype);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    @Override
    public void viewClick() {

    }

    @Override
    public void getDataFromServer() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if(webView != null)
            {
                webView.onActivityResult(requestCode, resultCode, intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean inCustomView()
    {
        return (mCustomView != null);
    }

    public void hideCustomView()
    {
        mWebChromeClient.onHideCustomView();
    }

    @SuppressWarnings("unused")
    class myWebChromeClient extends WebChromeClient
    {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback)
        {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view,CustomViewCallback callback)
        {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null)
            {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getVideoLoadingProgressView()
        {
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView()
        {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            webView.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class myWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    @Override
    protected void onDestroy()
    {
        DashboardActivity.isAppRunning = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
