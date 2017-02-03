package com.github.bnottingham.webviewheader.interfaces.impl;

import android.view.View;

/**
 * Created by brettn on 2/2/17.
 */

public abstract class OnLayoutHeightChangedListener implements View.OnLayoutChangeListener {
    private int mPrevHeight = -1;

    public abstract void onLayoutHeightChanged(int prevHeight, int newHeight);

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int newHeight = bottom - top;
        int prevHeight = mPrevHeight;

        if (prevHeight != newHeight) {
            mPrevHeight = newHeight;
            onLayoutHeightChanged(prevHeight, newHeight);
        }
    }
}
