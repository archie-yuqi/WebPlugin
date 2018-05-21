package com.pub.internal.hybrid.provider;

import android.content.Context;
import android.view.View;

import pub.hybrid.HybridBackForwardList;
import pub.hybrid.HybridSettings;
import pub.hybrid.ui.HybridView;

public abstract class AbsWebView {

    protected Context mContext;
    protected HybridView mHybridView;

    public AbsWebView(Context context, HybridView hybridView) {
        mContext = context;
        mHybridView = hybridView;
    }

    public View getBaseWebView(){
        return null;
    }

    public void setWebViewClient(AbsWebViewClient hybridViewClient) {
    }

    public void setWebChromeClient(AbsWebChromeClient hybridChromeClient) {
    }

    public void loadUrl(String url) {
    }

    public void addJavascriptInterface(Object obj, String name) {
    }

    public HybridSettings getSettings() {
        return null;
    }

    public void destroy() {
    }

    public void reload() {
    }

    public void clearCache(boolean includeDiskFiles) {
    }

    public boolean canGoBack() {
        return false;
    }

    public boolean canGoForward() {
        return false;
    }

    public void goBack() {
    }

    public String getUrl() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public int getContentHeight() {
        return 0;
    }

    public float getScale() {
        return 1f;
    }

    public Context getContext() {
        return null;
    }

    public void setVisibility(int visibility) {
    }

    public View getRootView() {
        return null;
    }

    public HybridBackForwardList copyBackForwardList() {
        return null;
    }
}
