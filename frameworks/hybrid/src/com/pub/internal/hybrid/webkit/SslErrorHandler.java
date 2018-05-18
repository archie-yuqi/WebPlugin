package com.pub.internal.hybrid.webkit;

public class SslErrorHandler extends pub.hybrid.SslErrorHandler {

    private android.webkit.SslErrorHandler mSslErrorHandler;

    public SslErrorHandler(android.webkit.SslErrorHandler sslErrorHandler) {
        mSslErrorHandler = sslErrorHandler;
    }

    @Override
    public void proceed() {
        mSslErrorHandler.proceed();
    }

    @Override
    public void cancel() {
        mSslErrorHandler.cancel();
    }
}
