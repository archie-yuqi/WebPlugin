package com.pub.internal.hybrid;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * The interface exposed to JavaScript.
 */
public class JsInterface {

    public static final String INTERFACE_NAME = "MiuiJsBridge";

    private HybridManager mManager;

    public JsInterface(HybridManager manager) {
        mManager = manager;
    }

    /**
     * Used by JavaScript to pass the configuration of current page.
     * 
     * @param config the configuration in JSON format.
     * @return invocation response.
     */
    @JavascriptInterface
    public String config(String config) {
        String response = mManager.config(config);
        if (Log.isLoggable(HybridManager.TAG, Log.DEBUG)) {
            Log.d(HybridManager.TAG, "config response is " + response);
        }
        return response;
    }

    /**
     * Used by JavaScript to lookup whether specified feature and action are supported.
     * 
     * @param feature feature name.
     * @param action action name in specified feature.
     * @return invocation response.
     */
    @JavascriptInterface
    public String lookup(String feature, String action) {
        String response = mManager.lookup(feature, action);
        if (Log.isLoggable(HybridManager.TAG, Log.DEBUG)) {
            Log.d(HybridManager.TAG, "lookup response is " + response);
        }
        return response;
    }

    /**
     * Used by JavaScript to invoke specified action in specified feature.
     * 
     * @param feature feature name.
     * @param action action name in specified feature.
     * @param rawParams raw parameters.
     * @param callback callback identifier.
     * @return invocation response.
     */
    @JavascriptInterface
    public String invoke(String feature, String action, String rawParams, String callback) {
        String response = mManager.invoke(feature, action, rawParams, callback);
        if (Log.isLoggable(HybridManager.TAG, Log.DEBUG)) {
            Log.d(HybridManager.TAG, "blocking response is " + response);
        }
        return response;
    }
}
