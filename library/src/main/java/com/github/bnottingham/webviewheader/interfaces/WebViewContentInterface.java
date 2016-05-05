package com.github.bnottingham.webviewheader.interfaces;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic interface that allows the WebViewHeaderDelegate to access various events/methods in the WebViewCore
 */
public interface WebViewContentInterface {
    /**
     * Get the WebView content width
     *
     * @return width in pixels of the content scrollable range
     */
    int getContentWidth();

    /**
     * Get the WebView content height
     *
     * @return height in pixels of the content scrollable range
     */
    int getContentHeight();

    /**
     * Get the WebView vertical range offset
     *
     * @return the vertical range offset (@link WebView.computeVerticalScrollRange())
     */
    int getVerticalRangeOffset();

    /**
     * Called when the scroll changes in the WebView
     *
     * @param newX
     * @param newY
     * @param oldX
     * @param oldY
     */
    void onScrollChangedEvent(int newX, int newY, int oldX, int oldY);

    /**
     * Called when a single tap event occurs in the WebView.
     */
    void onSingleTap();
}
