package com.pub.internal.hybrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

import pub.hybrid.R;

public class WebContainerView extends FrameLayout {

    private boolean mPullable;
    private boolean mIsPulling = false;

    private View mWebView;

    private int mTouchSlop;
    private float mLastX;
    private float mLastY;

    public WebContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.HybridViewStyle, 0, 0);
        mPullable = a.getBoolean(R.styleable.HybridViewStyle_hybridPullable, true);
        a.recycle();

        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setWebView(View webView) {
        if (webView == null || mWebView == webView) {
            return;
        }
        if (mWebView != null) {
            removeView(mWebView);
        }
        mWebView = webView;

        addView(webView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .MATCH_PARENT));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mWebView == null || !mPullable) {
            return false;
        }

        final int action = ev.getActionMasked();
        final float x = ev.getRawX();
        final float y = ev.getRawY();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsPulling = false;
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsPulling) {
                    return true;
                }

                final float xDelta = mLastX - x;
                final float yDelta = mLastY - y;

                final float xDistance = Math.abs(xDelta);
                final float yDistance = Math.abs(yDelta);

                mLastX = x;
                mLastY = y;

                if (mWebView.getScrollY() == 0 && yDelta < 0 && yDistance > xDistance &&
                        yDistance > mTouchSlop) {
                    // Start pulling!
                    mIsPulling = true;
                    return true;
                }
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mPullable || !mIsPulling) {
            return false;
        }

        final float y = ev.getRawY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float yDiff = y - mLastY;
                setTranslationY(getTranslationY() + yDiff * 0.5f);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsPulling = false;
                springBack();
                break;
        }
        return false;
    }

    private void springBack() {
        if (getTranslationY() == 0) {
            return;
        }
        ViewPropertyAnimator animator = animate();
        animator.translationY(0f);
        animator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        animator.start();
    }
}
