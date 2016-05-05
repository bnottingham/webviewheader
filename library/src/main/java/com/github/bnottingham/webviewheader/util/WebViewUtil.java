package com.github.bnottingham.webviewheader.util;

import android.webkit.WebView;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Utility class for a WebView
 */
public class WebViewUtil {
    public static boolean isAtBottomOfWebView(WebView webView, int currentY) {
        //Check if we are at the bottom of the view
        int tek = (int) Math.floor(webView.getContentHeight() * webView.getScale());
        return ((tek - currentY) <= webView.getHeight());
    }

    /**
     * Determine if the right edge of the web page is visible in the display window
     *
     * @return true if web page is on the right edge, false otherwise
     */
    public static boolean isOnRightEdge(WebView webView) {
        return !webView.canScrollHorizontally(1);
    }

    public static boolean canScrollRight(WebView webView) {
        return webView.canScrollHorizontally(1);
    }

    public static boolean canScrollUp(WebView webView) {
        return webView.canScrollVertically(1);
    }

    public static boolean canScrollVertically(WebView webView) {
        return (webView.canScrollVertically(-1) || webView.canScrollVertically(1));
    }
}
