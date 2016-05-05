package com.github.bnottingham.webviewheader.webkit;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic WebViewChromeClient that the WebViewCore can use to implement any methods it needs.
 *         Any implementation of WebViewCore that sets the WebChromeClient must extends this class.
 */
public class WebViewHeaderChromeClient extends WebChromeClient {
    private ChromeClientListener mChromeClientListener;

    public interface ChromeClientListener {
        public void onProgressChanged(WebView view, int newProgress);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mChromeClientListener != null) {
            mChromeClientListener.onProgressChanged(view, newProgress);
        }
    }

    public void setWebViewClientListener(ChromeClientListener chromeClientListener) {
        mChromeClientListener = chromeClientListener;
    }
}
