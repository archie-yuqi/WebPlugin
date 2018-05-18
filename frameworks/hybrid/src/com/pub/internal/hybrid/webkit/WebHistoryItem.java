package com.pub.internal.hybrid.webkit;

import android.graphics.Bitmap;

import pub.hybrid.HybridHistoryItem;

public class WebHistoryItem extends HybridHistoryItem {

    private android.webkit.WebHistoryItem mWebHistoryItem;

    public WebHistoryItem(android.webkit.WebHistoryItem webHistoryItem) {
        mWebHistoryItem = webHistoryItem;
    }

    @Override
    public String getUrl() {
        return mWebHistoryItem.getUrl();
    }

    @Override
    public String getOriginalUrl() {
        return mWebHistoryItem.getOriginalUrl();
    }

    @Override
    public String getTitle() {
        return mWebHistoryItem.getTitle();
    }

    @Override
    public Bitmap getFavicon() {
        return mWebHistoryItem.getFavicon();
    }
}
