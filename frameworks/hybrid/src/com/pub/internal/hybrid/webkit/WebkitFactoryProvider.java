package com.pub.internal.hybrid.webkit;

import android.content.Context;

import com.pub.internal.hybrid.provider.AbsWebChromeClient;
import com.pub.internal.hybrid.provider.AbsWebView;
import com.pub.internal.hybrid.provider.AbsWebViewClient;
import com.pub.internal.hybrid.provider.WebViewFactoryProvider;

import pub.hybrid.HybridChromeClient;
import pub.hybrid.ui.HybridView;
import pub.hybrid.HybridViewClient;

public class WebkitFactoryProvider extends WebViewFactoryProvider {

    @Override
    public AbsWebView createWebView(Context context, HybridView hybridView) {
        return new WebView(context, hybridView);
    }

    @Override
    public AbsWebViewClient createWebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        return new WebViewClient(hybridViewClient, hybridView);
    }

    @Override
    public AbsWebChromeClient createWebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        return new WebChromeClient(hybridChromeClient, hybridView);
    }
}
