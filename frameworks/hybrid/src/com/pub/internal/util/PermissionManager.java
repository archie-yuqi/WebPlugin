package com.pub.internal.util;

import android.net.Uri;

import com.pub.internal.hybrid.Config;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {

    private Map<String, Boolean> mValidMap = new HashMap<String, Boolean>();

    private Config mConfig;

    public PermissionManager(Config config) {
        mConfig = config;
    }

    public boolean isValid(String url) {
        if (!mValidMap.containsKey(url)) {
            mValidMap.put(url, initPermission(url));
        }
        return mValidMap.get(url);
    }

    private boolean initPermission(String url) {
        // TODO support more patterns
        boolean valid = false;
        Uri targetUri = Uri.parse(url);
        String targetScheme = targetUri.getScheme();
        String targetHost = null;
        if ("file".equals(targetScheme)) {
            targetHost = "*";
        } else {
            targetHost = targetUri.getHost();
        }
        for (Map.Entry<String, Permission> entry : mConfig.getPermissions().entrySet()) {
            Permission permission = entry.getValue();
            String uri = permission.getUri();
            String host = null;
            if ("*".equals(uri)) {
                host = "*";
            } else {
                host = Uri.parse(uri).getHost();
            }
            if (permission.isApplySubdomains()) {
                String[] parts = host.split("\\.");
                String[] targetParts = targetHost.split("\\.");
                if (targetParts.length >= parts.length) {
                    valid = true;
                    for (int i = 1; i <= parts.length; i++) {
                        if (!targetParts[targetParts.length - i].equals(parts[parts.length - i])) {
                            valid = false;
                            break;
                        }
                    }
                }
            } else {
                valid = targetHost.equals(host);
            }
            if (valid) {
                break;
            }
        }
        return valid;
    }
}
