package com.github.bnottingham.webviewheader.interfaces;

import android.widget.LinearLayout;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 */
public interface WebViewHeaderCoreInterface extends WebViewContentInterface {
    /**
     * Set the ViewGroup parent that contains the header and the WebView
     * This parent will be scrolled instead of the WebView until the header is gone
     * After the header is gone, the WebView will be scrolled instead
     *
     * @param view to set
     */
    void setScrollableLayout(final LinearLayout view);

    /**
     * Set the height of header
     *
     * @param headerHeight to set
     */
    void setHeaderHeight(final int headerHeight);

    /**
     * Because the WebView doesn't let you override the getScrollY() method, this method is used instead.
     * This is required because web the scrollable layout y = header height, the WebView is still at y = 0
     * Any outside controller needs to know the scrollY with the header offset
     * For accurate readings of the current scrollY, you must use this method, not the default getScrollY() of the WebView
     *
     * @return the current scrollY of the WebViewHeader
     */
    int getHeaderScrollY();

    /**
     * Set listener for scrolling events
     *
     * @param listener
     */
    void setScrollEventListener(OnScrollChangedEventListener listener);
}
