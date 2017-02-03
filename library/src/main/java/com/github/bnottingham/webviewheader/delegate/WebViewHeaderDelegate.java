package com.github.bnottingham.webviewheader.delegate;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import com.github.bnottingham.webviewheader.interfaces.WebViewContentInterface;
import com.github.bnottingham.webviewheader.interfaces.impl.BaseGestureListener;
import com.github.bnottingham.webviewheader.interfaces.impl.FlingRunnable;
import com.github.bnottingham.webviewheader.util.WebViewUtil;

/**
 * @author Brett Nottingham on 8/14/15
 *         Copyright (c) 2015 Nottingham, Inc. All rights reserved.
 *         <p/>
 *         Handles touch, scroll, and draw event to control a WebView with a header
 */
public class WebViewHeaderDelegate {
    //Basic Constructor Values
    private Context mContext;
    private View mScrollableLayout;
    private WebView mWebView;
    private WebViewContentInterface mWebViewContentInterface;

    //Header Values
    private int mHeaderHeight;

    //Gesture Detection
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private FlingRunnable mFlingRunnable;

    //MotionEvent Handling
    private MotionEvent mDownEvent;
    private boolean mIsScrolling;
    private boolean mIgnoreScrollEvent;
    private boolean mIsNativeTouchCanceled;
    private boolean mIsDispatchedToNative;
    private float mCurrentScaleFactor;
    private int mLastScrollX;
    private int mLastScrollY;
    private int mTouchSlopSquare;

    @SuppressWarnings("deprecation")
    public WebViewHeaderDelegate(Context context, WebView webView, int headerHeight) {
        super();
        mContext = context;
        mWebView = webView;
        mHeaderHeight = headerHeight;

        try {
            mWebViewContentInterface = (WebViewContentInterface) webView;
        } catch (ClassCastException e) {
            throw new ClassCastException(webView.toString() + " must implement ViewContentInterface");
        }

        //Set up gesture detectors
        MyGestureListener gestureListener = new MyGestureListener();
        mDetector = new GestureDetector(context, gestureListener);
        mDetector.setOnDoubleTapListener(gestureListener);
        mScaleDetector = new ScaleGestureDetector(context, gestureListener);
        mCurrentScaleFactor = mWebView.getScale();

        mTouchSlopSquare = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mTouchSlopSquare *= mTouchSlopSquare;
    }

    public void setScrollableLayout(View scrollableLayout) {
        mScrollableLayout = scrollableLayout;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Capture the down event for use later
                mDownEvent = MotionEvent.obtain(event);

                //Make sure we stop any fling that is happening
                if (mFlingRunnable != null) {
                    mFlingRunnable.stopScroller();
                    mFlingRunnable = null;
                }
                mIsScrolling = false;
                mIsNativeTouchCanceled = false;
                mIsDispatchedToNative = false;
                break;

            case MotionEvent.ACTION_UP:
                mDownEvent.recycle();
                mDownEvent = null;
                mIsNativeTouchCanceled = false;
                mIsDispatchedToNative = false;
                mIgnoreScrollEvent = false;
                break;
        }

        //Check if we are zooming
        mScaleDetector.onTouchEvent(event);

        //If we can't scroll vertically then send all touch events to the WebView until the next action up is detected, if getScrollY() == 0, however, that means that the view was scrollable (like a zoomed photo that is now zoomed out),
        //in that case let the delegate handle it so that the header can be scrolled back into view
        if (!WebViewUtil.canScrollVertically(mWebView) && mScrollableLayout.getScrollY() == 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mIsDispatchedToNative = true;
                mIgnoreScrollEvent = true;
            }
            return dispatchNativeOnTouchEvent(event);
        }
        //If there is no scrollable layout or header, then give the native view all the touches
        else if (mScrollableLayout == null || mHeaderHeight == 0) {
            return dispatchNativeOnTouchEvent(event);
        }
        //Don't intercept if we are current zooming
        else if (mScaleDetector.isInProgress()) {
            //If we previously cancelled last down event, then send a new one now so that pinch/zoom works
            if (mIsNativeTouchCanceled) {
                MotionEvent downEvent = MotionEvent.obtain(event);
                downEvent.setAction(MotionEvent.ACTION_DOWN);
                dispatchNativeOnTouchEvent(downEvent);
                mIsNativeTouchCanceled = false;
            }
            dispatchNativeOnTouchEvent(event);
        } else {
            //If this is true then what happened is that the content couldn't scroll (because it was loading) but now it can.
            //So we need to give the detector a new on down event and start the scrolling from there.
            if (mIsDispatchedToNative) {
                mDownEvent = MotionEvent.obtain(event);
                mDownEvent.setAction(MotionEvent.ACTION_DOWN);
                mDetector.onTouchEvent(mDownEvent);
                mIsDispatchedToNative = false;
            }

            mDetector.onTouchEvent(event);

            if (shouldDispatchNativeTouchCanceled(event)) {
                mIsNativeTouchCanceled = true;
                MotionEvent cancel = MotionEvent.obtain(event);
                cancel.setAction(MotionEvent.ACTION_CANCEL);
                dispatchNativeOnTouchEvent(cancel);
            }
            //Unless we are scrolling then send all the events to the native WebView.  This will allow it to handle single taps and long clicks
            else if (event.getAction() != MotionEvent.ACTION_MOVE) {
                dispatchNativeOnTouchEvent(event);
            }
        }
        return true;
    }

    public boolean shouldDispatchNativeTouchCanceled(MotionEvent event) {
        if (mIsNativeTouchCanceled) {
            return false;
        } else if (mIsScrolling) {
            return true;
        } else if (mDownEvent != null) {
            float deltaX = event.getX() - mDownEvent.getX();
            float deltaY = event.getY() - mDownEvent.getY();

            if ((deltaX * deltaX + deltaY * deltaY) >= mTouchSlopSquare) {
                return true;
            }
        }
        return false;
    }

    public boolean onScrollEvent(int x, int y, int oldX, int oldY) {
        if (!mScaleDetector.isInProgress() && !mIgnoreScrollEvent) {
            dispatchOnScrollChangedEvent(x - oldX, y - oldY);
        }
        return false;
    }

    public void onScaleChangedEvent(float oldScale, float newScale) {
        mCurrentScaleFactor = newScale;
    }

    private void scrollBy(int x, int y) {
        mIgnoreScrollEvent = true;
        mWebView.scrollBy(x, y);
        mIgnoreScrollEvent = false;
    }

    private void scrollTo(int x, int y) {
        mIgnoreScrollEvent = true;
        mWebView.scrollTo(x, y);
        mIgnoreScrollEvent = false;
    }

    public void setScrollY(int scrollY) {
        int x = (int) mWebView.getX();
        if (scrollY <= mHeaderHeight) {
            mScrollableLayout.scrollTo(0, scrollY);
            scrollTo(x, 0);
        } else {
            mScrollableLayout.scrollTo(0, mHeaderHeight);
            scrollTo(x, scrollY - mHeaderHeight);
        }
        mLastScrollX = x;
        mLastScrollY = scrollY;
    }

    private int getContentWidth() {
        return mWebViewContentInterface.getContentWidth();
    }

    private int getContentHeight() {
        return mWebViewContentInterface.getContentHeight();
    }

    private int getScrollX() {
        return mWebView.getScrollX();
    }

    public int getScrollY() {
        return (mScrollableLayout.getScrollY() + mWebView.getScrollY());
    }

    private boolean dispatchNativeOnTouchEvent(MotionEvent event) {
        int offset = -(mHeaderHeight - mScrollableLayout.getScrollY());
        MotionEvent offsetEvent = MotionEvent.obtain(event);
        offsetEvent.offsetLocation(0, offset);
        boolean result = mWebView.onTouchEvent(offsetEvent);
        offsetEvent.recycle();
        return result;
    }

    private synchronized void dispatchOnScrollChangedEvent(int distanceX, int distanceY) {
        int newX = mLastScrollX + distanceX;
        int newY = mLastScrollY + distanceY;

        if (newY < 0) {
            newY = 0;
        }

        if (newX < 0) {
            newX = 0;
        }

        mWebViewContentInterface.onScrollChangedEvent(newX, newY, mLastScrollX, mLastScrollY);
        mLastScrollX = newX;
        mLastScrollY = newY;
    }

    public void setHeaderHeight(float headerHeight) {
        mHeaderHeight = (int) headerHeight;
    }

    private class MyGestureListener extends BaseGestureListener
            implements FlingRunnable.FlingListener {
        private int calculateScrollX(int distanceX) {
            int finalX = mWebView.getScrollX() + distanceX;
            if (finalX < 0) {
                return -mWebView.getScrollX();
            } else if (distanceX > 0 && !WebViewUtil.canScrollRight(mWebView)) {
                return 0;
            }
            return distanceX;
        }

        private void onScrollUp(int distanceX, int distanceY) {
            int currentScrollY = mScrollableLayout.getScrollY();
            int finalY = currentScrollY + distanceY;

            int scrollableScrollY = distanceY;
            int webViewScrollY = 0;
            int webViewScrollX = calculateScrollX(distanceX);

            if (finalY > mHeaderHeight) {
                scrollableScrollY = mHeaderHeight - currentScrollY;
                webViewScrollY = distanceY - scrollableScrollY;
            }

            if (!WebViewUtil.canScrollUp(mWebView)) {
                webViewScrollY = 0;
            }
            mScrollableLayout.scrollBy(0, scrollableScrollY);
            scrollBy(webViewScrollX, webViewScrollY);
            dispatchOnScrollChangedEvent(webViewScrollX, scrollableScrollY + webViewScrollY);
        }

        private void onScrollDown(int distanceX, int distanceY) {
            int currentScrollY = mScrollableLayout.getScrollY();
            int currentWebViewScrollY = mWebView.getScrollY();
            int finalY = currentScrollY + currentWebViewScrollY + distanceY;

            int scrollableScrollY = 0;
            int webViewScrollY = distanceY;
            int webViewScrollX = calculateScrollX(distanceX);

            if (finalY < mHeaderHeight) {
                webViewScrollY = -mWebView.getScrollY();
                scrollableScrollY = Math.max(distanceY - webViewScrollY, -currentScrollY);
            }

            mScrollableLayout.scrollBy(0, scrollableScrollY);
            scrollBy(webViewScrollX, webViewScrollY);
            dispatchOnScrollChangedEvent(webViewScrollX, scrollableScrollY + webViewScrollY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(int distanceX, int distanceY) {
            mIsScrolling = mIsScrolling | distanceY != 0;

            if (distanceY > 0) {
                onScrollUp(distanceX, distanceY);
            } else if (distanceY < 0) {
                onScrollDown(distanceX, distanceY);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityY) > ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity()) {
                mIsScrolling = true;
                int maxX = (int) (getContentWidth() * mCurrentScaleFactor * 2);
                int maxY = (int) (getContentHeight() * mCurrentScaleFactor * 2);

                mFlingRunnable = new FlingRunnable(mWebView, this);
                mFlingRunnable.onContentFling(getScrollX(), getScrollY(), velocityX, velocityY, maxX, maxY);
            }
            return true;
        }

        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScaleBegin(final ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(final MotionEvent e) {
            mWebViewContentInterface.onSingleTap();
            return false;
        }
    }
}
