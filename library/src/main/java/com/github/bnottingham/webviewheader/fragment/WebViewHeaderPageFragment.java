package com.github.bnottingham.webviewheader.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.github.bnottingham.webviewheader.R;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderChromeClient;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderClient;
import com.github.bnottingham.webviewheader.widget.HeaderWebView;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic fragment that implements a WebViewCore and loads an url
 */
public class WebViewHeaderPageFragment extends Fragment {
    private View mView;
    private HeaderWebView mWebView;
    private String mUrl;

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.layout_webviewheader, container, false);
        init();
        return mView;
    }

    private void init() {
        mWebView = (HeaderWebView) mView.findViewById(R.id.web_view_header);
        setWebSettings();

        mWebView.setWebChromeClient(new WebViewHeaderChromeClient());
        mWebView.setWebViewClient(new WebViewHeaderClient());
        mWebView.loadUrl(mUrl);
        //mWebView.loadData("<html><body><h1>First Line</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>hello world</h1><h1>Last Line</h1></body></html>", "text/html", "UTF-8");

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebSettings() {
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDisplayZoomControls(false);
        settings.setDefaultFontSize(16);
        settings.setDefaultFixedFontSize(13);
    }
}
