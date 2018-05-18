package com.pub.internal.hybrid.webkit;

import pub.hybrid.GeolocationPermissions.Callback;

public class GeolocationPermissionsCallback implements Callback{

    private android.webkit.GeolocationPermissions.Callback mCallback;

    public GeolocationPermissionsCallback(android.webkit.GeolocationPermissions.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void invoke(String origin, boolean allow, boolean retain) {
        mCallback.invoke(origin, allow, retain);
    }
}
