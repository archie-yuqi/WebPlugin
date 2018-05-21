package com.pub.internal.hybrid.webkit;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.pub.internal.hybrid.provider.AbsWebViewClient;

import pub.hybrid.HybridResourceResponse;
import pub.hybrid.ui.HybridView;
import pub.hybrid.HybridViewClient;
import pub.hybrid.SslErrorHandler;

public class WebViewClient extends AbsWebViewClient {

    public WebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        super(hybridViewClient, hybridView);
    }

    @Override
    public Object getWebViewClient() {
        return new InternalWebViewClient();
    }

    @Override
    public void onPageStarted(HybridView view, String url, Bitmap favicon) {
        mHybridViewClient.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(HybridView view, String url) {
        mHybridViewClient.onPageFinished(view, url);
    }

    @Override
    public HybridResourceResponse shouldInterceptRequest(HybridView view, String url) {
        return mHybridViewClient.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(HybridView view, String url) {
        return mHybridViewClient.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedSslError(HybridView view, SslErrorHandler handler, SslError error) {
        mHybridViewClient.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedError(HybridView view, int errorCode, String description, String
            failingUrl) {
        mHybridViewClient.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedLoginRequest(HybridView view, String realm, String account, String args) {
        mHybridViewClient.onReceivedLoginRequest(view, realm, account, args);
    }

    /**
     * Internal WebViewClient implementation which only used to trigger the callbacks.
     */
    class InternalWebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebViewClient.this.onPageStarted(mHybridView, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            WebViewClient.this.onPageFinished(mHybridView, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            HybridResourceResponse hybridResourceResponse = WebViewClient.this.shouldInterceptRequest(mHybridView, url);
            return hybridResourceResponse == null ? null : new WebResourceResponce(hybridResourceResponse);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return WebViewClient.this.shouldOverrideUrlLoading(mHybridView, url);
        }

        @Override
        public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, SslError error) {
            SslErrorHandler sslErrorHandler = new com.pub.internal.hybrid.webkit.SslErrorHandler(handler);
            WebViewClient.this.onReceivedSslError(mHybridView, sslErrorHandler, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            WebViewClient.this.onReceivedError(mHybridView, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            WebViewClient.this.onReceivedLoginRequest(mHybridView, realm, account, args);
        }
    }
}
