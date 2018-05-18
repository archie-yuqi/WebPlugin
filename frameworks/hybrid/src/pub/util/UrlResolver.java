package pub.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;

import com.pub.internal.util.UrlResolverHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @hide
 */
public class UrlResolver {

    private static Method mResolveIntent = null;

    private UrlResolver() {
    }

    public static ResolveInfo checkMiuiIntent(Context context, PackageManager pm, Intent intent) {
        return checkMiuiIntent(context, true, pm, intent, null, 0, null, 0);
    }

    private static ResolveInfo checkMiuiIntent(Context context, boolean fromWebView,
                                               PackageManager pm, Intent intent, String resolvedType, int flags,
                                               List<ResolveInfo> query, int userId) {

        int miBrowserIndex = -1;
        if (fromWebView) {
            // query resolve info first when called by webview.
            query = pm.queryIntentActivities(intent, 0);
        }

        List<ResolveInfo> matchedSystemApps = new ArrayList();
        // try to find system apps.
        for (int i = 0; i < query.size(); i++) {
            final ResolveInfo r = query.get(i);
            if (r.activityInfo.packageName.equals("com.android.browser")) {
                miBrowserIndex = i;
            } else if (UrlResolverHelper.isWhiteListPackage(r.activityInfo.packageName)) {
                // Use the resolve info when package name of activity is in white list.
                matchedSystemApps.add(r);
            } else {
                PackageInfo pi = null;
                try {
                    // 防止跨线程启动hybrid，pm在调用线程获取packageInfo会崩溃
                    long token = Binder.clearCallingIdentity();
                    pi = pm.getPackageInfo(r.activityInfo.packageName, 0);
                    Binder.restoreCallingIdentity(token);
                } catch (NameNotFoundException e) {
                    continue;
                }
                // 判断是否为系统应用
                if (pi.applicationInfo != null && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // Use the resolve info when the activity belongs to system app.
                    matchedSystemApps.add(r);
                }
            }
        }

        if (matchedSystemApps.size() == 1) {
            return matchedSystemApps.get(0);
        } else if (matchedSystemApps.size() > 1) {
            return null;
        }

        Uri uri = intent.getData();
        if (uri == null) {
            // don't handle the intent without data.
            return null;
        }
        String host = uri.getHost();
        if (host == null) {
            // don't handle the intent which doesn't have host.
            return null;
        }
        if (!UrlResolverHelper.isMiHost(host)) {
            // don't handle the intent which host is not xiaomi related.
            return null;
        }

        // get fallback uri.
        String fallback = UrlResolverHelper.getFallbackParameter(uri);
        if (fallback != null) {
            Uri fallbackUri = Uri.parse(fallback);
            final String scheme = fallbackUri.getScheme();
            boolean browserFallback = false;

            if (UrlResolverHelper.isBrowserFallbackScheme(scheme)) {
                browserFallback = true;
                fallbackUri = UrlResolverHelper.getBrowserFallbackUri(fallback);
            }
            intent.setData(fallbackUri);

            if (fromWebView) {
                if (browserFallback) {
                    // jump out webview,send the intent to system to handle it.
                    context.startActivity(intent);
                    return new ResolveInfo();
                } else {
                    // recursion call,use the intent with fallback uri;
                    return checkMiuiIntent(context, pm, intent);
                }
            } else {
                // recursion call, let the system handle the intent with the fallback uri.
                final ResolveInfo ri = null;

//                if (Build.VERSION.SDK_INT > 20) {
//                    // deal the situation that PackageManagerService not in the class path of the
//                    // package and the boot class path.
//                    try {
//                        if (mResolveIntent == null) {
//                            Class clazz = context.getClassLoader().loadClass("com.android.server.pm.PackageManagerService");
//                            mResolveIntent = clazz.getDeclaredMethod("resolveIntent", Intent.class, String.class, int.class, int.class);
//                        }
//                        ri = (ResolveInfo) mResolveIntent.invoke(pms, intent, resolvedType, flags, userId);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                } else {
//                    ri = pms.resolveIntent(intent, resolvedType, flags, userId);
//                }
                return ri;
            }
        } else if (miBrowserIndex != -1 && !fromWebView) {
            // when system and white list app can't hanle the intent,and the uri doesn't have
            // fallback, further more not called by webview,use the browser to handle the intent.
            return query.get(miBrowserIndex);
        } else {
            // let the system or webview hanlde the uri.
            return null;
        }
    }
}
