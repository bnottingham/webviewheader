package com.github.bnottingham.webviewheader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.bnottingham.webviewheader.R;
import com.github.bnottingham.webviewheader.interfaces.OnScrollChangedEventListener;
import com.github.bnottingham.webviewheader.interfaces.impl.OnLayoutHeightChangedListener;
import com.github.bnottingham.webviewheader.util.ViewUtil;

/**
 * @author Brett Nottingham on 2/1/16
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Container that holds a custom WebView and a Header Placeholder
 */
public class HeaderWebView extends FrameLayout {
    private View mWebViewContainer;
    private CustomWebView mWebView;
    private View mWebViewHeaderPlaceholder;
    private OnScrollChangedEventListener mOnScrollChangedEventListener = OnScrollChangedEventListener.NO_OP;

    private int mHeaderHeight;

    public HeaderWebView(final Context context) {
        super(context);
        init(context, null);
    }

    public HeaderWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeaderWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setHeaderContentView(final View view) {
        mWebView.setScrollEventListener(new OnScrollChangedEventListener() {
            @Override
            public void onScrollChangedEvent(int x, int y, int oldX, int oldY) {
                view.setTranslationY(-y);
            }
        });
    }

    public void setScrollEventListener(OnScrollChangedEventListener listener) {
        mOnScrollChangedEventListener = listener == null ? OnScrollChangedEventListener.NO_OP : listener;
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHeaderHeight > 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mWebView.dispatchTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mWebViewContainer = findViewById(R.id.web_view_container);
        mWebViewHeaderPlaceholder = findViewById(R.id.web_view_header_placeholder);
        mWebView = (CustomWebView) findViewById(R.id.web_view);

        mWebView.setScrollableLayout(mWebViewContainer);

        addOnLayoutChangeListener(new OnLayoutHeightChangedListener() {
            @Override
            public void onLayoutHeightChanged(int prevHeight, int newHeight) {
                updateLayoutHeight();
            }
        });

        updateHeader();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewUtil.unwrapContainerView(this).onWebViewAttachedToWindow();
    }

    private void init(final Context context, final AttributeSet attrs) {
        inflate(context, R.layout.layout_webview, this);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderWebView);
        mHeaderHeight = (int) typedArray.getDimension(R.styleable.HeaderWebView_webViewHeaderHeight, 0);
        typedArray.recycle();

        setId(R.id.header_web_view);
    }

    private void updateHeader() {
        mWebView.setHeaderHeight(mHeaderHeight);
        mWebViewHeaderPlaceholder.getLayoutParams().height = mHeaderHeight;
    }

    private void updateLayoutHeight() {
        FrameLayout.LayoutParams scrollableLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        scrollableLayoutParams.height = getMeasuredHeight() + mHeaderHeight;
        mWebViewContainer.setLayoutParams(scrollableLayoutParams);

        LinearLayout.LayoutParams webViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webViewLayoutParams.height = getMeasuredHeight();
        mWebView.setLayoutParams(webViewLayoutParams);
    }
}
