package com.pub.internal.hybrid.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.pub.internal.hybrid.HybridManager;
import com.pub.internal.hybrid.webkit.WebkitFactoryProvider;

/**
 * Top level factory, used creating the main WebView implementation classes.
 */
public class WebViewFactory {

    private static WebViewFactoryProvider sProviderInstance;
    private static final Object sProviderLock = new Object();
    private static final String META_DATA_KEY = "com.miui.sdk.hybrid.webview";

    public static WebViewFactoryProvider getProvider(Context context) {
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                return sProviderInstance;
            }

            String value = null;
            try {
                Bundle bundle = context.getPackageManager().getApplicationInfo(
                        context.getPackageName(),
                        PackageManager.GET_META_DATA).metaData;

                if (bundle != null) {
                    value = bundle.getString(META_DATA_KEY);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (value != null) {
                try {
                    Class<?> providerClass = Class.forName(value, false, context.getClassLoader());
                    sProviderInstance = (WebViewFactoryProvider) providerClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sProviderInstance == null) {
                sProviderInstance = new WebkitFactoryProvider();
            }
            if (Log.isLoggable(HybridManager.TAG, Log.DEBUG)) {
                Log.d(HybridManager.TAG, "loaded provider:" + sProviderInstance);
            }

            return sProviderInstance;
        }
    }
}
