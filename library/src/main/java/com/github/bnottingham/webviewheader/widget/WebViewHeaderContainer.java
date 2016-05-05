package com.github.bnottingham.webviewheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.bnottingham.webviewheader.R;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         FrameLayout parent of the WebViewHeader layout to initialize the UI, dispatch touch events, and handle configuration changes (landscape/portrait mode)
 */
public class WebViewHeaderContainer extends FrameLayout {
    private LinearLayout mWebViewScrollable;
    private WebViewHeader mWebView;
    private View mWebViewHeaderSizePlaceholder;
    private int mHeaderHeight;

    public WebViewHeaderContainer(Context context) {
        super(context);
    }

    public WebViewHeaderContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WebViewHeaderContainer(Context context, AttributeSet attrs, int defStyleAtts) {
        super(context, attrs, defStyleAtts);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WebViewHeader);
        mHeaderHeight = (int) typedArray.getDimension(R.styleable.WebViewHeader_webViewHeaderHeight, 0);
        typedArray.recycle();
    }

    private void initUI() {
        // Retrieve UI elements
        mWebViewScrollable = (LinearLayout) findViewById(R.id.web_view_header_scrollable);
        mWebViewHeaderSizePlaceholder = findViewById(R.id.web_view_header_content_size_placeholder);

        // Initialize the WebView if necessary
        if (mWebView == null) {
            // Create the WebView
            mWebView = (WebViewHeader) findViewById(R.id.web_view_header);
            mWebView.setHeaderHeight(mHeaderHeight);
            mWebView.setScrollableLayout(mWebViewScrollable);

            mWebViewHeaderSizePlaceholder.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            updateViewSize();
                        }
                    });
                }
            });
        }

        updateViewSize();
        requestLayout();
    }

    private void updateViewSize() {
        //Update LinearLayout Size
        LayoutParams scrollableLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        scrollableLayoutParams.height = mWebViewHeaderSizePlaceholder.getMeasuredHeight() + mHeaderHeight;
        mWebViewScrollable.setLayoutParams(scrollableLayoutParams);

        //Update WebView Placeholder Size
        LinearLayout.LayoutParams webViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webViewLayoutParams.height = mWebViewHeaderSizePlaceholder.getMeasuredHeight();
        mWebView.setLayoutParams(webViewLayoutParams);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initUI();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mWebView.dispatchTouchEvent(event);
    }
}
