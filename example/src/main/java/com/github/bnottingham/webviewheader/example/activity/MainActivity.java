package com.github.bnottingham.webviewheader.example.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.bnottingham.webviewheader.example.R;
import com.github.bnottingham.webviewheader.interfaces.OnScrollChangedEventListener;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderClient;
import com.github.bnottingham.webviewheader.widget.HeaderWebView;

/**
 * @author Brett Nottingham on 5/12/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    private HeaderWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View header = findViewById(R.id.header);
        mWebView= (HeaderWebView) findViewById(R.id.web_view_header);

        mWebView.setWebViewClient(new WebViewHeaderClient());
        mWebView.loadUrl("http://500px.com");
        mWebView.setScrollEventListener(new OnScrollChangedEventListener() {
            @Override
            public void onScrollChangedEvent(int x, int y, int oldX, int oldY) {
                header.setTranslationY(-y);
            }
        });

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
