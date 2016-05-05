package com.github.bnottingham.webviewheader.interfaces.impl;

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Basic fling runnable that will start a scroller and initiate a callback as scrollX and scrollY changes
 *         This fling runnable is based on an infinite scroll, not on a final destination, such that with a linear interpolator
 *         every fling will run for the same amount of time regardless of where the view's scroll exists at start time.
 */

public class FlingRunnable implements Runnable {
    public interface FlingListener {
        boolean onScroll(int distanceX, int distanceY);
    }

    private View mView;
    private Scroller mScroller;
    private int mLastScrollX;
    private int mLastScrollY;
    private boolean mIsFlinging;
    private FlingListener mFlingListener;

    public FlingRunnable(View view, FlingListener listener) {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        //Create scroller and assign a interpolator to it.
        mScroller = new Scroller(view.getContext(), linearInterpolator);
        mView = view;
        mFlingListener = listener;
    }

    public void stopScroller() {
        mIsFlinging = false;
        mScroller.abortAnimation();
    }

    @Override
    public void run() {
        if (!mIsFlinging) {
            return;
        }

        //If we are still scrolling get the new x,y values.
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();

            if (mLastScrollX != currX || mLastScrollY != currY) {
                int distanceX = currX - mLastScrollX;
                int distanceY = currY - mLastScrollY;

                if (mFlingListener != null) {
                    mFlingListener.onScroll(distanceX, distanceY);
                }

                mLastScrollX = currX;
                mLastScrollY = currY;
            }

            //If we have not reached our scroll limit,then run this Runnable again on UI event thread.
            if (mIsFlinging) {
                mView.post(this);
            }
        }
    }

    public boolean onContentFling(int x, int y, float velocityX, float velocityY, int maxX, int maxY) {
        mIsFlinging = true;

        mLastScrollX = x;
        mLastScrollY = y;
        mScroller.fling(x, y, (int) -velocityX, (int) -velocityY, -maxX, maxX, -maxY, maxY);
        mView.post(this);
        return true;
    }
}
