package com.pub.internal.hybrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import pub.hybrid.R;

public class HybridProgressView extends ImageView {

    private static final int MAX_PROGRESS = 10000;
    private static final int MSG_UPDATE = 42;
    private static final int STEPS = 10;
    private static final int DELAY = 40;
    private static final int MAX_CUR_PROGRESS = 9500;
    private static final int MIN_CUR_PROGRESS = 800;

    private int mCurrentProgress;
    private int mTargetProgress;
    private int mIncrement;
    private Rect mBounds;
    private Handler mHandler;
    private Context mContext;
    private Drawable mReverseDrawable;
    private Rect mReverseBounds;


    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HybridProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public HybridProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     */
    public HybridProgressView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context ctx) {
        mContext = ctx;
        mBounds = new Rect(0, 0, 0, 0);
        mCurrentProgress = 0;
        mTargetProgress = 0;
        mReverseDrawable = mContext.getResources().getDrawable(R.drawable.hybrid_progress_reverse);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE) {

                    if (mCurrentProgress < mTargetProgress) {
                        mCurrentProgress = Math.min(mTargetProgress,
                                mCurrentProgress + mIncrement);
                        mBounds.right = getWidth() * mCurrentProgress / MAX_PROGRESS;
                        invalidate();
                        sendMessageDelayed(mHandler.obtainMessage(MSG_UPDATE), DELAY);
                    } else if (mCurrentProgress <= MAX_CUR_PROGRESS && mCurrentProgress >= MIN_CUR_PROGRESS) {
                        mCurrentProgress = mCurrentProgress + STEPS * 3;
                        mBounds.right = getWidth() * mCurrentProgress / MAX_PROGRESS;
                        invalidate();
                        sendMessageDelayed(mHandler.obtainMessage(MSG_UPDATE), DELAY);
                    }
                }
            }
        };
        mReverseBounds = new Rect(0, 0, 0, 0);
    }

    @Override
    public void onLayout(boolean f, int l, int t, int r, int b) {
        mBounds.left = 0;
        mBounds.right = (r - l) * mCurrentProgress / MAX_PROGRESS;
        mBounds.top = 0;
        mBounds.bottom = b - t;
    }

    public void setProgress(int progress) {
        progress *= 100;
        if (mTargetProgress <= MIN_CUR_PROGRESS) {
            mCurrentProgress = mTargetProgress;
        }
        mTargetProgress = progress;
        mIncrement = (mTargetProgress - mCurrentProgress) / STEPS;

        mHandler.removeMessages(MSG_UPDATE);
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }

    public int getMax() {
        return MAX_PROGRESS / 100;
    }

    @Override
    public void onDraw(Canvas canvas) {

        Drawable d = getDrawable();
        d.setBounds(mBounds);
        d.draw(canvas);

        float translateX = getWidth() - (float) getWidth() * mCurrentProgress / MAX_PROGRESS;
        canvas.save();
        canvas.translate(-translateX, 0);
        mReverseBounds.set(0, 0, getWidth(), getHeight());
        mReverseDrawable.setBounds(mReverseBounds);
        mReverseDrawable.draw(canvas);
        canvas.translate(translateX, 0);
        canvas.restore();
    }
}
