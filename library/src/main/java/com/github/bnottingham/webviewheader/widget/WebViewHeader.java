package com.github.bnottingham.webviewheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.bnottingham.webviewheader.R;
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
public class WebViewHeader extends WebView
        implements WebViewHeaderCoreInterface {
    private WebViewHeaderDelegate mWebViewHeaderDelegate;
    private WebViewHeaderClient mWebViewHeaderClient;
    private WebViewHeaderChromeClient mWebViewHeaderChromeClient;
    private OnScrollChangedEventListener mScrollChangedEventListener = OnScrollChangedEventListener.NO_OP;
    private LinearLayout mScrollableLayout;
    private int mHeaderHeight;

    public WebViewHeader(final Context context) {
        super(context);
        init(null);
    }

    public WebViewHeader(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WebViewHeader(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(final AttributeSet attrs) {
        //Get any attributes
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WebViewHeader);
        mHeaderHeight = (int) typedArray.getDimension(R.styleable.WebViewHeader_webViewHeaderHeight, 0);
        typedArray.recycle();

        //Set up the delegate
        mWebViewHeaderDelegate = new WebViewHeaderDelegate(getContext(), this, mHeaderHeight);
        mWebViewHeaderDelegate.setScrollableLayout(mScrollableLayout);

        //Set default clients
        setWebViewClient(new WebViewHeaderClient());
        setWebChromeClient(new WebViewHeaderChromeClient());
    }

    @Override
    public void setScrollableLayout(final LinearLayout scrollableLayout) {
        mScrollableLayout = scrollableLayout;
        if (mWebViewHeaderDelegate != null) {
            mWebViewHeaderDelegate.setScrollableLayout(scrollableLayout);
        }
    }

    @Override
    public void setHeaderHeight(final int headerHeight) {
        mHeaderHeight = headerHeight;
        if (mWebViewHeaderDelegate != null) {
            mWebViewHeaderDelegate.setHeaderHeight(headerHeight);
        }
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
    public void onScrollChangedEvent(int x, int y, int oldX, int oldY) {
        mScrollChangedEventListener.onScrollChangedEvent(x, y, oldX, oldY);
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
        mScrollChangedEventListener = listener;
        if (mScrollChangedEventListener == null) {
            mScrollChangedEventListener = OnScrollChangedEventListener.NO_OP;
        }
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
}
