package com.pub.internal.hybrid;

import pub.hybrid.Response;

public class HybridException extends Exception {

    private static final long serialVersionUID = 1L;

    private Response mResponse;

    public HybridException() {
        super(new Response(Response.CODE_GENERIC_ERROR).toString());
        mResponse = new Response(Response.CODE_GENERIC_ERROR);
    }

    public HybridException(String detailMessage) {
        super(new Response(Response.CODE_GENERIC_ERROR, detailMessage).toString());
        mResponse = new Response(Response.CODE_GENERIC_ERROR, detailMessage);
    }

    public HybridException(int errorCode, String detailMessage) {
        super(new Response(errorCode, detailMessage).toString());
        mResponse = new Response(errorCode, detailMessage);
    }

    public HybridException(Response response) {
        super(response.toString());
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}
