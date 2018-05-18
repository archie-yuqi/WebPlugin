package com.pub.internal.hybrid.provider;

import android.graphics.Bitmap;
import android.net.http.SslError;

import pub.hybrid.HybridResourceResponse;
import pub.hybrid.HybridView;
import pub.hybrid.HybridViewClient;
import pub.hybrid.SslErrorHandler;

public abstract class AbsWebViewClient {

    protected HybridViewClient mHybridViewClient;
    protected HybridView mHybridView;

    public AbsWebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        mHybridViewClient = hybridViewClient;
        mHybridView = hybridView;
    }

    public Object getWebViewClient() {
        return null;
    }

    public void onPageStarted(HybridView view, String url, Bitmap favicon) {
    }

    public void onPageFinished(HybridView view, String url) {
    }

    public HybridResourceResponse shouldInterceptRequest(HybridView view, String url) {
        return null;
    }

    public boolean shouldOverrideUrlLoading(final HybridView view, String url) {
        return false;
    }

    public void onReceivedSslError(HybridView view, SslErrorHandler handler, SslError error) {
    }

    public void onReceivedError(HybridView view, int errorCode, String description, String failingUrl) {
    }

    public void onReceivedLoginRequest(HybridView view, String realm, String account, String args) {
    }
}
