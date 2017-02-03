package com.github.bnottingham.webviewheader.example.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.bnottingham.webviewheader.example.R;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderClient;
import com.github.bnottingham.webviewheader.widget.HeaderWebView;

/**
 * @author Brett Nottingham on 5/12/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    private HeaderWebView mHeaderWebView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHeaderWebView = (HeaderWebView) findViewById(R.id.header_web_view);
        mWebView = mHeaderWebView.getWebView();

        mWebView.setWebViewClient(new WebViewHeaderClient());
        mWebView.loadUrl("http://500px.com");

        setWebSettings();
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
