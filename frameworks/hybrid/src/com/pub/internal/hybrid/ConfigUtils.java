package com.pub.internal.hybrid;

import java.util.Set;
import java.util.TreeSet;

public class ConfigUtils {

    private static final String KEY_TIMESTAMP = "timestamp";

    private static final String KEY_VENDOR = "vendor";

    private static final String KEY_FEATURES = "features";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";

    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_ORIGIN = "origin";
    private static final String KEY_SUBDOMAINS = "subdomains";

    private ConfigUtils() {
    }

    public static String getRawConfig(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append(KEY_TIMESTAMP);
        sb.append(":");
        sb.append(config.getSecurity().getTimestamp());
        sb.append(",");

        sb.append(KEY_VENDOR);
        sb.append(":");
        sb.append("\"");
        sb.append(config.getVendor());
        sb.append("\"");
        sb.append(",");

        sb.append(buildFeatures(config));
        sb.append(",");
        sb.append(buildPermissions(config));
        sb.append("}");
        return sb.toString();
    }

    private static String buildFeatures(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_FEATURES);
        sb.append(":");
        sb.append("[");
        sb.append(buildFeature(config));
        sb.append("]");
        return sb.toString();
    }

    private static String buildFeature(Config config) {
        StringBuilder sb = new StringBuilder();
        Set<String> names = new TreeSet<String>(config.getFeatures().keySet());
        if (names.isEmpty()) {
            return "";
        }
        for (String name : names) {
            sb.append("{");

            sb.append(KEY_NAME);
            sb.append(":");
            sb.append("\"");
            sb.append(name);
            sb.append("\"");
            sb.append(",");

            sb.append(KEY_PARAMS);
            sb.append(":");
            sb.append("[");
            sb.append(buildParam(config.getFeature(name)));
            sb.append("]");

            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String buildParam(Feature feature) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = new TreeSet<String>(feature.getParams().keySet());
        if (keys.isEmpty()) {
            return "";
        }
        for (String key : keys) {
            sb.append("{");

            sb.append(KEY_NAME);
            sb.append(":");
            sb.append("\"");
            sb.append(key);
            sb.append("\"");
            sb.append(",");

            sb.append(KEY_VALUE);
            sb.append(":");
            sb.append("\"");
            sb.append(feature.getParam(key));
            sb.append("\"");

            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String buildPermissions(Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_PERMISSIONS);
        sb.append(":");
        sb.append("[");
        sb.append(buildPermission(config));
        sb.append("]");
        return sb.toString();
    }

    private static Object buildPermission(Config config) {
        StringBuilder sb = new StringBuilder();
        Set<String> uris = new TreeSet<String>(config.getPermissions().keySet());
        if (uris.isEmpty()) {
            return "";
        }
        for (String uri : uris) {
            sb.append("{");

            sb.append(KEY_ORIGIN);
            sb.append(":");
            sb.append("\"");
            sb.append(uri);
            sb.append("\"");
            sb.append(",");

            sb.append(KEY_SUBDOMAINS);
            sb.append(":");
            sb.append(config.getPermission(uri).isApplySubdomains());

            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
