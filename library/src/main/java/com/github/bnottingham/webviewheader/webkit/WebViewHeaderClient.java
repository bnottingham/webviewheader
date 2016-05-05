package com.github.bnottingham.webviewheader.webkit;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic WebViewClient that the WebViewCore can use to implement any methods it needs.
 *         Any implementation of WebViewCore that sets the WebViewClient must extends this class.
 */
public class WebViewHeaderClient extends WebViewClient {
    private WebViewClientListener mWebViewClientListener;

    public interface WebViewClientListener {
        public void onScaleChanged(WebView view, float oldScale, float newScale);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (mWebViewClientListener != null) {
            mWebViewClientListener.onScaleChanged(view, oldScale, newScale);
        }
    }

    public void setWebViewClientListener(WebViewClientListener webViewClientListener) {
        mWebViewClientListener = webViewClientListener;
    }
}
