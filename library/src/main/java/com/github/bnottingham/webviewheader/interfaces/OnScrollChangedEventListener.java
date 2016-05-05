package com.github.bnottingham.webviewheader.interfaces;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic interface that listens for scroll events from the WebView
 */
public interface OnScrollChangedEventListener {
    OnScrollChangedEventListener NO_OP = new OnScrollChangedEventListener() {
        @Override
        public void onScrollChangedEvent(int x, int y, int oldX, int oldY) {
        }
    };

    void onScrollChangedEvent(int x, int y, int oldX, int oldY);
}
