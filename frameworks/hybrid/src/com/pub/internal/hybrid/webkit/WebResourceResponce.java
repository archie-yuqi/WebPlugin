package com.pub.internal.hybrid.webkit;

import pub.hybrid.HybridResourceResponse;

public class WebResourceResponce extends android.webkit.WebResourceResponse {

    public WebResourceResponce(HybridResourceResponse hybridResourceResponse) {
        super(hybridResourceResponse.getMimeType(), hybridResourceResponse.getEncoding(),
                hybridResourceResponse.getData());
    }
}
