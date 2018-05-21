package com.pub.internal.hybrid;

import com.pub.internal.util.Permission;
import com.pub.internal.util.Security;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import pub.hybrid.Response;

public class JsonConfigParser implements ConfigParser {

    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_TIMESTAMP = "timestamp";

    private static final String KEY_VENDOR = "vendor";

    private static final String KEY_CONTENT = "content";

    private static final String KEY_FEATURES = "features";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";

    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_ORIGIN = "origin";
    private static final String KEY_SUBDOMAINS = "subdomains";

    private JSONObject mJson;

    private JsonConfigParser(JSONObject json) {
        mJson = json;
    }

    public static JsonConfigParser createFromString(String content) throws HybridException {
        JSONObject json = null;
        try {
            json = new JSONObject(content);
        } catch (JSONException e) {
            throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
        }
        return createFromJSONObject(json);
    }

    public static JsonConfigParser createFromJSONObject(JSONObject json) {
        return new JsonConfigParser(json);
    }

    @Override
    public Config parse(Map<String, Object> metaData) throws HybridException {
        Config config = new Config();
        try {
            JSONObject jsonRoot = mJson;

            Security security = new Security();
            security.setSignature(jsonRoot.getString(KEY_SIGNATURE));
            security.setTimestamp(jsonRoot.getLong(KEY_TIMESTAMP));
            config.setSecurity(security);

            config.setVendor(jsonRoot.getString(KEY_VENDOR));
            config.setContent(jsonRoot.optString(KEY_CONTENT));

            parseFeatures(config, jsonRoot);
            parsePermissions(config, jsonRoot);
        } catch (JSONException e) {
            throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
        }
        return buildCompleteConfig(config, metaData);
    }

    private void parseFeatures(Config config, JSONObject jsonRoot) throws JSONException {
        JSONArray jsonFeatures = jsonRoot.optJSONArray(KEY_FEATURES);
        if (jsonFeatures != null) {
            for (int i = 0; i < jsonFeatures.length(); i++) {
                JSONObject jsonFeature = jsonFeatures.getJSONObject(i);
                Feature feature = new Feature();
                feature.setName(jsonFeature.getString(KEY_NAME));
                JSONArray jsonParams = jsonFeature.optJSONArray(KEY_PARAMS);
                if (jsonParams != null) {
                    for (int j = 0; j < jsonParams.length(); j++) {
                        JSONObject jsonParam = jsonParams.getJSONObject(j);
                        feature.setParam(jsonParam.getString(KEY_NAME), jsonParam.getString(KEY_VALUE));
                    }
                }
                config.addFeature(feature);
            }
        }
    }

    private void parsePermissions(Config config, JSONObject jsonRoot) throws JSONException {
        JSONArray jsonPermissions = jsonRoot.optJSONArray(KEY_PERMISSIONS);
        if (jsonPermissions != null) {
            for (int i = 0; i < jsonPermissions.length(); i++) {
                JSONObject jsonPermission = jsonPermissions.getJSONObject(i);
                Permission permission = new Permission();
                permission.setUri(jsonPermission.getString(KEY_ORIGIN));
                permission.setApplySubdomains(jsonPermission.optBoolean(KEY_SUBDOMAINS));
                config.addPermission(permission);
            }
        }
    }

    private Config buildCompleteConfig(Config config, Map<String, Object> metaData) {
        return config;
    }
}
