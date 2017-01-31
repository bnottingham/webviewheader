package com.github.bnottingham.webviewheader.util;

import android.support.annotation.NonNull;
import android.view.View;

import com.github.bnottingham.webviewheader.widget.WebViewContainer;

/**
 * Created by brettn on 1/31/17.
 */
public class ViewUtil {
    public static WebViewContainer unwrapContainerView(@NonNull View view) {
        if (view == null) {
            throw new RuntimeException("View cannot be null, or HeaderWebView doesn't have a WebViewContainer parent");
        }

        if (view instanceof WebViewContainer) {
            return (WebViewContainer) view;
        }

        return unwrapContainerView((View) view.getParent());
    }
}
