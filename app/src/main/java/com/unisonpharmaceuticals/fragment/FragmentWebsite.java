package com.unisonpharmaceuticals.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;

import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Kiran Patel on 23-Jul-18.
 */
public class FragmentWebsite extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    private View rootView;

    private AdvancedWebView webView;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;
    private LinearLayout llLoading ;

    String report_url = "" ;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_website, container, false);
        ButterKnife.bind(this,rootView);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        report_url = ApiClient.WEBSITE_URL;
        apiService = ApiClient.getClient().create(ApiInterface.class);

        customViewContainer = (FrameLayout) rootView.findViewById(R.id.customViewContainer);
        webView = (AdvancedWebView) rootView.findViewById(R.id.webView);
        llLoading = (LinearLayout) rootView.findViewById(R.id.llLoading);

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
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.loadUrl(report_url);

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

        return rootView ;
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
            //return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.

            if(url.equalsIgnoreCase("http://reporting.unisonpharmaceuticals.com/login"))
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);
            }

            return false;
        }
    }

}
