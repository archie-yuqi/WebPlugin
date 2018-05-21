package com.pub.internal.hybrid.provider;

import android.net.Uri;

import pub.hybrid.GeolocationPermissions;
import pub.hybrid.HybridChromeClient;
import pub.hybrid.ui.HybridView;
import pub.hybrid.JsResult;
import pub.hybrid.ValueCallback;

public abstract class AbsWebChromeClient {

    protected HybridChromeClient mHybridChromeClient;
    protected HybridView mHybridView;

    public AbsWebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        mHybridChromeClient = hybridChromeClient;
        mHybridView = hybridView;
    }

    public Object getWebChromeClient() {
        return null;
    }

    public boolean onJsAlert(HybridView view, String url, String message, final JsResult result) {
        return false;
    }

    public boolean onJsConfirm(HybridView view, String url, String message, final JsResult result) {
        return false;
    }

    public void onProgressChanged(HybridView view, int progress) {
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
    }

    public void onReceivedTitle(HybridView view, String title) {
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
    }
}
