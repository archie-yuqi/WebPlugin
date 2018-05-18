package com.pub.internal.hybrid.webkit;

import pub.hybrid.HybridSettings;

public class WebSettings extends HybridSettings {

    private android.webkit.WebSettings mWebSettings;

    public WebSettings(android.webkit.WebSettings webSettings) {
        mWebSettings = webSettings;
    }

    @Override
    public void setJavaScriptEnabled(boolean flag) {
        mWebSettings.setJavaScriptEnabled(flag);
    }

    @Override
    public void setUserAgentString(String ua) {
        mWebSettings.setUserAgentString(ua);
    }

    @Override
    public String getUserAgentString() {
        return mWebSettings.getUserAgentString();
    }

    @Override
    public void setUseWideViewPort(boolean use) {
        mWebSettings.setUseWideViewPort(use);
    }

    @Override
    public void setSupportMultipleWindows(boolean support) {
        mWebSettings.setSupportMultipleWindows(support);
    }

    @Override
    public void setLoadWithOverviewMode(boolean overview) {
        mWebSettings.setLoadWithOverviewMode(overview);
    }

    @Override
    public void setDomStorageEnabled(boolean flag) {
        mWebSettings.setDomStorageEnabled(flag);
    }

    @Override
    public void setDatabaseEnabled(boolean flag) {
        mWebSettings.setDatabaseEnabled(flag);
    }

    @Override
    public void setAllowFileAccessFromFileURLs(boolean flag) {
        mWebSettings.setAllowFileAccessFromFileURLs(flag);
    }

    @Override
    public void setAllowUniversalAccessFromFileURLs(boolean flag) {
        mWebSettings.setAllowUniversalAccessFromFileURLs(flag);
    }

    @Override
    public void setCacheMode(int mode) {
        mWebSettings.setCacheMode(mode);
    }

    @Override
    public void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(flag);
    }

    @Override
    public void setTextZoom(int textZoom) {
        mWebSettings.setTextZoom(textZoom);
    }

    @Override
    public void setGeolocationEnabled(boolean flag) {
        mWebSettings.setGeolocationEnabled(flag);
    }

    @Override
    public void setAppCacheEnabled(boolean flag) {
        mWebSettings.setAppCacheEnabled(flag);
    }

    @Override
    public void setAppCachePath(String appCachePath) {
        mWebSettings.setAppCachePath(appCachePath);
    }

    @Override
    public void setGeolocationDatabasePath(String databasePath) {
        mWebSettings.setGeolocationDatabasePath(databasePath);
    }
}
