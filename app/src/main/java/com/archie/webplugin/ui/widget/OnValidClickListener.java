package com.archie.webplugin.ui.widget;

import android.view.View;

import com.archie.webplugin.R;
import com.archie.webplugin.utils.UiUtils;

public abstract class OnValidClickListener implements View.OnClickListener {

    private static final int VIEW_VALID_DOUBLE_CLICK_TIME_INTERVAL = 1024;

    @Override
    public final void onClick(View view) {
        if (isValidClick(view)) {
            onValidClick(view);
        }
    }

    private boolean isValidClick(View view) {
        if (view == null) {
            return false;
        }
        boolean isValidClick = true;
        long curTime = System.currentTimeMillis();//获取当前时间
        Object viewTag = view.getTag(R.id.view_tag_click_time);
        view.setTag(R.id.view_tag_click_time, curTime);
        if (viewTag instanceof Long) {
            long viewLastClickTime = (long) viewTag;
            isValidClick = curTime - viewLastClickTime > VIEW_VALID_DOUBLE_CLICK_TIME_INTERVAL;
        }
        if (!isValidClick) {
            UiUtils.showToast(view.getContext(), R.string.invalid_click_hint);
        }
        return isValidClick;
    }

    protected abstract void onValidClick(View view);
}
