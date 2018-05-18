package com.pub.internal.hybrid.webkit;

public class ValueCallback<T> implements pub.hybrid.ValueCallback<T> {

    private android.webkit.ValueCallback<T> mValueCallback;

    public ValueCallback(android.webkit.ValueCallback<T> valueCallback) {
        mValueCallback = valueCallback;
    }

    @Override
    public void onReceiveValue(T value) {
        mValueCallback.onReceiveValue(value);
    }
}
