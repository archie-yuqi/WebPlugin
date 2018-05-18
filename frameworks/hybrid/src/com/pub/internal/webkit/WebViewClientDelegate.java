package com.pub.internal.webkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.pub.internal.hybrid.provider.AbsWebView;
import com.pub.internal.util.UrlResolverHelper;

import pub.util.UrlResolver;
public class WebViewClientDelegate {

    public static final int FEATURE_DEEP_LINK = 0x1;

    public static final int FEATURE_AUTO_LOGIN = 0x2;

    private static final int FEATURE_ALL = 0xFFFFFFFF;

    private boolean mSupportDeepLink;
    private boolean mSupportAutoLogin;

    private DeviceAccountLogin mAccountLogin;

    private enum LoginState {
        LOGIN_START,
        LOGIN_INPROGRESS,
        LOGIN_FINISHED
    }

    private LoginState mLoginState = LoginState.LOGIN_FINISHED;

    public WebViewClientDelegate() {
        this(FEATURE_ALL);
    }

    public WebViewClientDelegate(int features) {
        this(features, FEATURE_ALL);
    }

    public WebViewClientDelegate(int features, int mask) {
        int enabled = (features & mask) | (FEATURE_ALL & ~mask);
        mSupportDeepLink = (FEATURE_DEEP_LINK & enabled) != 0;
        mSupportAutoLogin = (FEATURE_AUTO_LOGIN & enabled) != 0;
    }

    public boolean shouldOverrideUrlLoading(AbsWebView view, String url) {
        if (!mSupportDeepLink) {
            return false;
        }
        if (UrlResolverHelper.isMiUrl(url)) {
            final Context context = view.getContext();
            PackageManager pm = context.getPackageManager();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            ResolveInfo ri = UrlResolver.checkMiuiIntent(context, pm, intent);

            if (ri == null) {
                return false;
            }
            if (ri.activityInfo != null) {
                context.startActivity(intent);
            }
            return true;
        }
        return false;
    }

    public void onPageStarted(AbsWebView view, String url, Bitmap favicon) {
        if (!mSupportAutoLogin) {
            return;
        }
        if (mLoginState == LoginState.LOGIN_START) {
            mLoginState = LoginState.LOGIN_INPROGRESS;
        }
    }

    public void onPageFinished(AbsWebView view, String url) {
        if (!mSupportAutoLogin) {
            return;
        }
        if (mLoginState == LoginState.LOGIN_INPROGRESS) {
            mLoginState = LoginState.LOGIN_FINISHED;
            mAccountLogin.onLoginPageFinished();
        }
    }

    public void onReceivedLoginRequest(AbsWebView view, String realm, String account, String args) {
        if (!mSupportAutoLogin) {
            return;
        }

        Activity activity = (Activity) view.getRootView().getContext();
        if (mAccountLogin == null) {
            mAccountLogin = new DefaultDeviceAccountLogin(activity, view);
        }
        if (view.canGoForward()) {
            if (view.canGoBack()) {
                view.goBack();
            } else {
                activity.finish();
            }
        } else {
            mLoginState = LoginState.LOGIN_START;
            view.setVisibility(View.INVISIBLE);

            mAccountLogin.login(realm, account, args);
        }
    }
}
