package com.pub.internal.hybrid.webkit;

import android.net.Uri;
import android.webkit.WebView;

import com.pub.internal.hybrid.provider.AbsWebChromeClient;

import pub.hybrid.GeolocationPermissions;
import pub.hybrid.HybridChromeClient;
import pub.hybrid.HybridView;
import pub.hybrid.JsResult;
import pub.hybrid.ValueCallback;

public class WebChromeClient extends AbsWebChromeClient {

    public WebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        super(hybridChromeClient, hybridView);
    }

    @Override
    public Object getWebChromeClient() {
        return new InternalWebChromeClient();
    }

    @Override
    public boolean onJsAlert(HybridView view, String url, String message, JsResult result) {
        return mHybridChromeClient.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(HybridView view, String url, String message, JsResult result) {
        return mHybridChromeClient.onJsConfirm(view, url, message, result);
    }

    @Override
    public void onProgressChanged(HybridView view, int progress) {
        mHybridChromeClient.onProgressChanged(view, progress);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        mHybridChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onReceivedTitle(HybridView view, String title) {
        mHybridChromeClient.onReceivedTitle(view, title);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        mHybridChromeClient.openFileChooser(uploadFile, acceptType, capture);
    }

    class InternalWebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit
                .JsResult result) {
            JsResult jsResult = new com.pub.internal.hybrid.webkit.JsResult(result);
            return WebChromeClient.this.onJsAlert(mHybridView, url, message, jsResult);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final
            android.webkit.JsResult result) {
            JsResult jsResult = new com.pub.internal.hybrid.webkit.JsResult(result);
            return WebChromeClient.this.onJsConfirm(mHybridView, url, message, jsResult);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            WebChromeClient.this.onProgressChanged(mHybridView, progress);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, android.webkit
                .GeolocationPermissions.Callback callback) {
            WebChromeClient.this.onGeolocationPermissionsShowPrompt(origin, new
                    GeolocationPermissionsCallback(callback));
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            WebChromeClient.this.onReceivedTitle(mHybridView, title);
        }

        public void openFileChooser(android.webkit.ValueCallback<Uri> uploadFile, String
                acceptType, String capture) {
            ValueCallback<Uri> valueCallback = new com.pub.internal.hybrid.webkit.ValueCallback<Uri>(uploadFile);
            WebChromeClient.this.openFileChooser(valueCallback, acceptType, capture);
        }
    }
}
