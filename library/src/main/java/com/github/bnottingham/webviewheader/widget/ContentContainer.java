package com.github.bnottingham.webviewheader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.bnottingham.webviewheader.R;
import com.github.bnottingham.webviewheader.interfaces.HeaderViewContainerInterface;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         FrameLayout parent that should contain as a child the Header (R.id.header_container) layout to translate and the HeaderWebView (R.id.header_web_view) layout
 */
public class ContentContainer extends FrameLayout implements HeaderViewContainerInterface {
    private HeaderWebView mHeaderWebView;
    private View mHeader;

    public ContentContainer(Context context) {
        super(context);
        init();
    }

    public ContentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public ContentContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // Intentional no-op
    }

    public void onWebViewAttachedToWindow() {
        mHeaderWebView = (HeaderWebView) findViewById(R.id.header_web_view);
        mHeader = findViewById(R.id.header_container);

        mHeaderWebView.setHeaderContentView(mHeader);
    }
}
