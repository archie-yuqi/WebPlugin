package com.pub.internal.hybrid;

import com.pub.internal.util.Permission;
import com.pub.internal.util.Security;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private Security security;
    private String vendor;
    private String content;
    private Map<String, Feature> features = new HashMap<String, Feature>();
    private Map<String, String> preferences = new HashMap<String, String>();
    private Map<String, Permission> permissions = new HashMap<String, Permission>();

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Feature> features) {
        this.features = features;
    }

    public void clearFeatures() {
        features.clear();
    }

    public Feature getFeature(String name) {
        return features.get(name);
    }

    public void addFeature(Feature feature) {
        features.put(feature.getName(), feature);
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    public void clearPreferences() {
        preferences.clear();
    }

    public String getPreference(String key) {
        return preferences.get(key);
    }

    public void setPreference(String key, String value) {
        preferences.put(key, value);
    }

    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    public void clearPermissions() {
        permissions.clear();
    }

    public Permission getPermission(String url) {
        return permissions.get(url);
    }

    public void addPermission(Permission permission) {
        permissions.put(permission.getUri(), permission);
    }
}
