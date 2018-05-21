package com.pub.internal.hybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.pub.internal.util.PermissionManager;
import com.pub.internal.util.SecurityManager;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.hybrid.Callback;
import pub.hybrid.HybridChromeClient;
import pub.hybrid.HybridFeature;
import pub.hybrid.HybridFeature.Mode;
import pub.hybrid.HybridSettings;
import pub.hybrid.ui.HybridView;
import pub.hybrid.HybridViewClient;
import pub.hybrid.LifecycleListener;
import pub.hybrid.NativeInterface;
import pub.hybrid.PageContext;
import pub.hybrid.Request;
import pub.hybrid.Response;

public class HybridManager {

    public static final String TAG = "hybrid";

    private static ExecutorService sPool = Executors.newCachedThreadPool();
    private static String sUserAgent;

    private Activity mActivity;
    private HybridView mView;
    private boolean mDetached;

    private NativeInterface mNativeInterface;

    private FeatureManager mFM;
    private PermissionManager mPM;

    private PageContext mPageContext;

    private Set<LifecycleListener> mListeners = new CopyOnWriteArraySet<LifecycleListener>();

    public HybridManager(Activity activity, HybridView view) {
        mActivity = activity;
        mView = view;
    }

    public void init(int configResId, String url) {
        mNativeInterface = new NativeInterface(this);
        Config config = loadConfig(configResId);
        config(config, false);
        initView();
        if (url == null && !TextUtils.isEmpty(config.getContent())) {
            url = resolveUri(config.getContent());
        }
        if (url != null) {
            mView.loadUrl(url);
        }
    }

    private Config loadConfig(int configResId) {
        try {
            XmlConfigParser parser = null;
            if (configResId == 0) {
                parser = XmlConfigParser.create(mActivity);
            } else {
                parser = XmlConfigParser.createFromResId(mActivity, configResId);
            }
            return parser.parse(null);
        } catch (HybridException e) {
            throw new RuntimeException("cannot load config: " + e.getMessage());
        }
    }

    private String config(Config config, boolean checkSecurity) {
        if (checkSecurity) {
            SecurityManager sm = new SecurityManager(config, mActivity.getApplicationContext());
            if (sm.isExpired() || !sm.isValidSignature()) {
                return new Response(Response.CODE_SIGNATURE_ERROR).toString();
            }
        }
        mFM = new FeatureManager(config, mActivity.getClassLoader());
        mPM = new PermissionManager(config);
        return new Response(Response.CODE_SUCCESS).toString();
    }

    public String config(String config) {
        try {
            ConfigParser parser = JsonConfigParser.createFromString(config);
            return config(parser.parse(null), true);
        } catch (HybridException e) {
            return new Response(Response.CODE_CONFIG_ERROR, e.getMessage()).toString();
        }
    }

    public void setPageContext(PageContext pageContext) {
        mPageContext = pageContext;
    }

    private void initView() {
        HybridSettings settings = mView.getSettings();
        initSettings(settings);

        HybridViewClient viewClient = new HybridViewClient();
        mView.setHybridViewClient(viewClient);

        HybridChromeClient chromeClient = new HybridChromeClient();
        mView.setHybridChromeClient(chromeClient);

        JsInterface jsInterface = new JsInterface(this);
        mView.addJavascriptInterface(jsInterface, JsInterface.INTERFACE_NAME);

        mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewAttachedToWindow(View v) {
                mDetached = false;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                mDetached = true;
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSettings(HybridSettings settings) {
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(getUserAgent(settings.getUserAgentString()));
    }

    private String getUserAgent(String original) {
        if (sUserAgent == null) {
            StringBuilder sb = new StringBuilder(original);
            sb.append(" XiaoMi/HybridView/");
            sb.append(getPackageInfo(mActivity, mActivity.getPackageName()).versionName);
            sb.append(" ");
            sb.append(mActivity.getPackageName());
            sb.append("/");
            sb.append(getPackageInfo(mActivity, mActivity.getPackageName()).versionName);
            sUserAgent = sb.toString();
        }
        return sUserAgent;
    }

    private static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    private String resolveUri(String original) {
        Pattern schemeRegex = Pattern.compile("^[a-z-]+://");
        Matcher matcher = schemeRegex.matcher(original);
        if (matcher.find()) {
            return original;
        } else {
            if (original.charAt(0) == '/') {
                original = original.substring(1);
            }
            return "file:///android_asset/hybrid/" + original;
        }
    }

    private String convertToMiuiPub(String feature) {
        if (feature.startsWith("miui.")) {
            return feature.replace("miui.", "pub.");
        }

        return feature;
    }

    private HybridFeature buildFeature(String feature) throws HybridException {
        if (!mPM.isValid(mPageContext.getUrl())) {
            throw new HybridException(Response.CODE_PERMISSION_ERROR, "feature not permitted: " + feature);
        }
        return mFM.lookupFeature(feature);
    }

    private Request buildRequest(String feature, String action, String rawParams) {
        Request request = new Request();
        request.setAction(action);
        request.setRawParams(rawParams);
        request.setPageContext(mPageContext);
        request.setView(mView);
        request.setNativeInterface(mNativeInterface);
        return request;
    }

    public String lookup(String feature, String action) {
        String featureName = convertToMiuiPub(feature);
        HybridFeature f = null;
        try {
            f = buildFeature(featureName);
        } catch (HybridException e) {
            Response response = e.getResponse();
            return response.toString();
        }

        Request request = buildRequest(featureName, action, null);

        if (f.getInvocationMode(request) == null) {
            return new Response(Response.CODE_ACTION_ERROR, "action not supported: " + action).toString();
        } else {
            return new Response(Response.CODE_SUCCESS).toString();
        }
    }

    public String invoke(String feature, String action, String rawParams, String jsCallback) {
        String featureName = convertToMiuiPub(feature);

        HybridFeature f = null;
        try {
            f = buildFeature(featureName);
        } catch (HybridException e) {
            Response response = e.getResponse();
            callback(response, mPageContext, jsCallback);
            return response.toString();
        }

        Request request = buildRequest(featureName, action, rawParams);

        Mode mode = f.getInvocationMode(request);
        if (mode == Mode.SYNC) {
            callback(new Response(Response.CODE_SYNC), mPageContext, jsCallback);
            return f.invoke(request).toString();
        } else if (mode == Mode.ASYNC) {
            sPool.execute(new AsyncInvocation(f, request, jsCallback));
            return new Response(Response.CODE_ASYNC).toString();
        } else {
            Callback callback = new Callback(this, mPageContext, jsCallback);
            request.setCallback(callback);
            sPool.execute(new AsyncInvocation(f, request, jsCallback));
            return new Response(Response.CODE_CALLBACK).toString();
        }
    }

    public boolean isDetached() {
        return mDetached;
    }

    private class AsyncInvocation implements Runnable {

        private HybridFeature mFeature;
        private Request mRequest;
        private String mJsCallback;

        public AsyncInvocation(HybridFeature feature, Request request, String jsCallback) {
            mFeature = feature;
            mRequest = request;
            mJsCallback = jsCallback;
        }

        @Override
        public void run() {
            Response response = mFeature.invoke(mRequest);
            if (mFeature.getInvocationMode(mRequest) == Mode.ASYNC) {
                callback(response, mRequest.getPageContext(), mJsCallback);
            }
        }
    }

    public void callback(Response response, PageContext pageContext, String jsCallback) {
        if (response != null && !TextUtils.isEmpty(jsCallback) && pageContext.equals(mPageContext)
                && !mDetached && !mActivity.isFinishing()) {
            if (Log.isLoggable(HybridManager.TAG, Log.DEBUG)) {
                Log.d(TAG, "non-blocking response is " + response.toString());
            }
            mActivity.runOnUiThread(new JsInvocation(response, jsCallback));
        }
    }

    private class JsInvocation implements Runnable {

        private Response mResponse;
        private String mJsCallback;

        public JsInvocation(Response response, String jsCallback) {
            mResponse = response;
            mJsCallback = jsCallback;
        }

        @Override
        public void run() {
            String callback = buildCallbackJavascript(mResponse, mJsCallback);
            mView.loadUrl("javascript:" + callback);
            // mView.evaluateJavascript(callback, null); // kitkat+
        }
    }

    private String buildCallbackJavascript(Response response, String callback) {
        if (TextUtils.isEmpty(callback)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(callback);
        sb.append("('");
        sb.append(response.toString().replace("\\", "\\\\").replace("'", "\\'"));
        sb.append("');");
        return sb.toString();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        mListeners.add(listener);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        mListeners.remove(listener);
    }

    public void onPageChange() {
        for (LifecycleListener listener : mListeners) {
            listener.onPageChange();
        }
    }

    public void onStart() {
        for (LifecycleListener listener : mListeners) {
            listener.onStart();
        }
    }

    public void onResume() {
        for (LifecycleListener listener : mListeners) {
            listener.onResume();
        }
    }

    public void onPause() {
        for (LifecycleListener listener : mListeners) {
            listener.onPause();
        }
    }

    public void onStop() {
        for (LifecycleListener listener : mListeners) {
            listener.onStop();
        }
    }

    public void onDestroy() {
        for (LifecycleListener listener : mListeners) {
            listener.onDestroy();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (LifecycleListener listener : mListeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public HybridView getHybridView() {
        return mView;
    }
}
