package com.github.bnottingham.webviewheader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.bnottingham.webviewheader.delegate.WebViewHeaderDelegate;
import com.github.bnottingham.webviewheader.interfaces.OnScrollChangedEventListener;
import com.github.bnottingham.webviewheader.interfaces.WebViewHeaderCoreInterface;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderChromeClient;
import com.github.bnottingham.webviewheader.webkit.WebViewHeaderClient;


/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Base WebView widget that handles sending the delegate all the required events to handle the header scroll
 */
public class CustomWebView extends WebView implements WebViewHeaderCoreInterface {
    private WebViewHeaderDelegate mWebViewHeaderDelegate;
    private WebViewHeaderClient mWebViewHeaderClient;
    private WebViewHeaderChromeClient mWebViewHeaderChromeClient;

    private int mHeaderHeight;

    private OnScrollChangedEventListener mScrollChangedEventListener = OnScrollChangedEventListener.NO_OP;

    public CustomWebView(final Context context) {
        super(context);
        init(context, null);
    }

    public CustomWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        //Set up the delegate
        mWebViewHeaderDelegate = new WebViewHeaderDelegate(context, this, 0);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
        try {
            mWebViewHeaderClient = (WebViewHeaderClient) client;
            mWebViewHeaderClient.setWebViewClientListener(new WebViewHeaderClient.WebViewClientListener() {
                @Override
                public void onScaleChanged(WebView view, float oldScale, float newScale) {
                    if (mWebViewHeaderDelegate != null) {
                        mWebViewHeaderDelegate.onScaleChangedEvent(oldScale, newScale);
                    }
                }
            });
        } catch (ClassCastException e) {
            throw new ClassCastException(client.toString() + " must extend WebViewHeaderClient");
        }
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
        try {
            mWebViewHeaderChromeClient = (WebViewHeaderChromeClient) client;
            mWebViewHeaderChromeClient.setWebViewClientListener(new WebViewHeaderChromeClient.ChromeClientListener() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                }
            });
        } catch (ClassCastException e) {
            throw new ClassCastException(client.toString() + " must extend WebViewHeaderChromeClient");
        }
    }

    @Override
    public void setScrollableLayout(View view) {
        mWebViewHeaderDelegate.setScrollableLayout(view);
    }

    @Override
    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
        mWebViewHeaderDelegate.setHeaderHeight(mHeaderHeight);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mHeaderHeight == 0) {
            onScrollChangedEvent(x, y, oldX, oldY);
        } else {
            mWebViewHeaderDelegate.onScrollEvent(x, y, oldX, oldY);
        }
    }

    @Override
    public void setScrollY(final int value) {
        if (mHeaderHeight == 0) {
            super.setScrollY(value);
        } else {
            mWebViewHeaderDelegate.setScrollY(value);
        }
    }

    @Override
    public int getHeaderScrollY() {
        if (mHeaderHeight == 0) {
            return super.getScrollY();
        } else {
            return mWebViewHeaderDelegate.getScrollY();
        }
    }

    @Override
    public void setScrollEventListener(OnScrollChangedEventListener listener) {
        mScrollChangedEventListener = listener == null ? OnScrollChangedEventListener.NO_OP : listener;
    }

    @Override
    public int getContentWidth() {
        return computeHorizontalScrollRange();
    }

    @Override
    public int getContentHeight() {
        return super.getContentHeight();
    }

    @Override
    public int getVerticalRangeOffset() {
        return computeVerticalScrollRange();
    }

    @Override
    public void onScrollChangedEvent(int newX, int newY, int oldX, int oldY) {
        mScrollChangedEventListener.onScrollChangedEvent(newX, newY, oldX, oldY);
    }

    @Override
    public void onSingleTap() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mHeaderHeight == 0) {
            return super.dispatchTouchEvent(event);
        } else {
            return mWebViewHeaderDelegate.onTouchEvent(event);
        }
    }
}
