package com.pub.internal.hybrid.webkit;

public class JsResult extends pub.hybrid.JsResult {

    private android.webkit.JsResult mJsResult;

    public JsResult(android.webkit.JsResult jsResult) {
        mJsResult = jsResult;
    }

    @Override
    public void confirm() {
        mJsResult.confirm();
    }

    @Override
    public void cancel() {
        mJsResult.cancel();
    }
}