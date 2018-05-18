package com.pub.internal.util;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class UrlResolverHelper {
    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    private static final String BROWSER_SCHEME_PREFIX = "mi";
    private static final String BROWSER_HTTP_SCHEME = BROWSER_SCHEME_PREFIX + HTTP_SCHEME;
    private static final String BROWSER_HTTPS_SCHEME = BROWSER_SCHEME_PREFIX + HTTPS_SCHEME;

    private static final String FALLBACK_PARAMETER = "mifb";

    private static final String[] MI_LIST = new String[]{
            "xiaomi.com",
            "mi.com",
            "miui.com"
    };

    private static final String[] WHITE_LIST = new String[]{
            "duokan.com"
    };


    private static final String[] WHITE_PACKAGE_LIST = new String[]{
            "com.duokan.reader",
            "com.xiaomi.shop"
    };

    private static Set<String> sBrowserFallbackSchemeSet = new HashSet<String>();

    static {
        sBrowserFallbackSchemeSet.add(BROWSER_HTTP_SCHEME);
        sBrowserFallbackSchemeSet.add(BROWSER_HTTPS_SCHEME);
    }

    private static Set<String> sFallbackSchemeSet = new HashSet<String>();

    static {
        sFallbackSchemeSet.add(HTTP_SCHEME);
        sFallbackSchemeSet.add(HTTPS_SCHEME);
        sFallbackSchemeSet.addAll(sBrowserFallbackSchemeSet);
    }

    public static boolean isMiUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        Uri uri = Uri.parse(url);
        if (!HTTP_SCHEME.equals(uri.getScheme()) && !HTTPS_SCHEME.equals(uri.getScheme())) {
            return false;
        }

        return isMiHost(uri.getHost());
    }

    public static boolean isMiHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }

        for (String h : MI_LIST) {
            if (host.contains(h)) {
                return true;
            }
        }
        for (String h : WHITE_LIST) {
            if (host.contains(h)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWhiteListPackage(String packageName) {
        for (String name : WHITE_PACKAGE_LIST) {
            if (name.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBrowserFallbackScheme(String scheme) {
        return sBrowserFallbackSchemeSet.contains(scheme);
    }

    public static Uri getBrowserFallbackUri(String fallback) {
        return Uri.parse(fallback.substring(UrlResolverHelper.BROWSER_SCHEME_PREFIX.length()));
    }

    public static String getFallbackParameter(Uri uri) {
        String fallback = getFallbackParameter(uri, 0, null);
        if (fallback != null) {
            Uri fallbackUri = Uri.parse(fallback);
            String scheme = fallbackUri.getScheme();
            if (sFallbackSchemeSet.contains(scheme)) {
                return fallback;
            }
        }
        return null;
    }

    private static String getFallbackParameter(Uri uri, int parameterSuffix, String
            lastFallbackParameter) {
        String fallbackParameter = uri.getQueryParameter(FALLBACK_PARAMETER + (parameterSuffix ==
                0 ? "" : parameterSuffix));
        if (fallbackParameter != null) {
            parameterSuffix++;
            return getFallbackParameter(uri, parameterSuffix, fallbackParameter);
        }
        return lastFallbackParameter;
    }
}
