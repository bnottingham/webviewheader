package com.github.bnottingham.webviewheader.example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.bnottingham.webviewheader.example.R;
import com.github.bnottingham.webviewheader.interfaces.OnScrollChangedEventListener;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderClient;
import com.github.bnottingham.webviewheader.widget.WebViewHeader;

/**
 * @author Brett Nottingham on 5/12/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View header = findViewById(R.id.header);
        final WebViewHeader webView = (WebViewHeader) findViewById(R.id.web_view_header);

        webView.setWebViewClient(new WebViewHeaderClient());
        webView.loadUrl("http://500px.com");
        webView.setScrollEventListener(new OnScrollChangedEventListener() {
            @Override
            public void onScrollChangedEvent(int x, int y, int oldX, int oldY) {
                header.setTranslationY(-y);
            }
        });
    }
}
