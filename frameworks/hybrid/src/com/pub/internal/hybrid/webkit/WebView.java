package com.pub.internal.hybrid.webkit;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.pub.internal.hybrid.provider.AbsWebChromeClient;
import com.pub.internal.hybrid.provider.AbsWebView;
import com.pub.internal.hybrid.provider.AbsWebViewClient;

import pub.hybrid.HybridBackForwardList;
import pub.hybrid.HybridSettings;
import pub.hybrid.ui.HybridView;

public class WebView extends AbsWebView {

    protected android.webkit.WebView mWebView;

    public WebView(Context context, HybridView hybridView) {
        super(context, hybridView);
        mWebView = new android.webkit.WebView(mContext);
    }

    @Override
    public View getBaseWebView() {
        return mWebView;
    }

    @Override
    public void setWebViewClient(AbsWebViewClient wrapper) {
        mWebView.setWebViewClient((WebViewClient) wrapper.getWebViewClient());
    }

    @Override
    public void setWebChromeClient(AbsWebChromeClient wrapper) {
        mWebView.setWebChromeClient((WebChromeClient) wrapper.getWebChromeClient());
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void addJavascriptInterface(Object obj, String name) {
        mWebView.addJavascriptInterface(obj, name);
    }

    @Override
    public HybridSettings getSettings() {
        return new WebSettings(mWebView.getSettings());
    }

    @Override
    public void destroy() {
        mWebView.destroy();
    }

    @Override
    public void reload() {
        mWebView.reload();
    }

    @Override
    public void clearCache(boolean includeDiskFiles) {
        mWebView.clearCache(includeDiskFiles);
    }

    @Override
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    @Override
    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public String getUrl() {
        return mWebView.getUrl();
    }

    @Override
    public String getTitle() {
        return mWebView.getTitle();
    }

    @Override
    public int getContentHeight() {
        return mWebView.getContentHeight();
    }

    @Override
    public float getScale() {
        return mWebView.getScale();
    }

    @Override
    public Context getContext() {
        return mWebView.getContext();
    }

    @Override
    public void setVisibility(int visibility) {
        mWebView.setVisibility(visibility);
    }

    @Override
    public View getRootView() {
        return mWebView.getRootView();
    }

    @Override
    public HybridBackForwardList copyBackForwardList() {
        return new WebBackForwardList(mWebView.copyBackForwardList());
    }
}
