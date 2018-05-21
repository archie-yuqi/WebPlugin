package com.pub.internal.hybrid.provider;

import android.content.Context;

import pub.hybrid.HybridChromeClient;
import pub.hybrid.ui.HybridView;
import pub.hybrid.HybridViewClient;

/**
 * This is the main entry-point into the WebView back end implementations, which used to
 * instantiate all the other objects as needed. The backend must provide an implementation of this
 * class, and make it available to the WebView via mechanism TBD.
 */
public class WebViewFactoryProvider {

    public AbsWebView createWebView(Context context, HybridView hybridView) {
        return null;
    }

    public AbsWebViewClient createWebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        return null;
    }

    public AbsWebChromeClient createWebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        return null;
    }
}
