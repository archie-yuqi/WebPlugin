package com.pub.internal.hybrid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.pub.internal.util.Permission;
import com.pub.internal.util.Security;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pub.hybrid.Response;

public class XmlConfigParser implements ConfigParser {

    private static final String META_DATA_KEY = "com.miui.sdk.hybrid.config";
    private static final String CONFIG_FILE_NAME = "miui_hybrid_config";

    private static final String ELEMENT_WIDGET = "widget";

    private static final String ELEMENT_CONTENT = "content";

    private static final String ELEMENT_FEATURE = "feature";
    private static final String ELEMENT_PARAM = "param";

    private static final String ELEMENT_PREFERENCE = "preference";

    private static final String ELEMENT_ACCESS = "access";

    private static final String ATTRIBUTE_SRC = "src";

    private static final String ATTRIBUTE_ORIGIN = "origin";
    private static final String ATTRIBUTE_SUBDOMAINS = "subdomains";

    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";

    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_VENDOR = "vendor";

    private XmlResourceParser mParser;

    private XmlConfigParser(XmlResourceParser parser) {
        mParser = parser;
    }

    public static XmlConfigParser create(Context context) throws HybridException {
        int resId = 0;
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            if (bundle != null) {
                resId = bundle.getInt(META_DATA_KEY);
            }
            if (resId == 0) {
                resId = context.getResources().getIdentifier(CONFIG_FILE_NAME, "xml", context.getPackageName());
            }
        } catch (NameNotFoundException e) {
            throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
        }
        return createFromResId(context, resId);
    }

    public static XmlConfigParser createFromResId(Context context, int resId) throws HybridException {
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getXml(resId);
        } catch (NotFoundException e) {
            throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
        }
        return createFromXmlParser(parser);
    }

    public static XmlConfigParser createFromXmlParser(XmlResourceParser parser) {
        return new XmlConfigParser(parser);
    }

    @Override
    public Config parse(Map<String, Object> metaData) throws HybridException {
        if (metaData == null) {
            metaData = new HashMap<String, Object>();
        }
        Config config = new Config();
        if (mParser != null) {
            try {
                XmlResourceParser parser = mParser;

                int type = 0;
                while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
                    continue;
                }

                String tagName = parser.getName();
                if (ELEMENT_WIDGET.equals(tagName)) {
                    parseWidgetElement(config, parser);
                }
            } catch (XmlPullParserException e) {
                throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
            } catch (IOException e) {
                throw new HybridException(Response.CODE_CONFIG_ERROR, e.getMessage());
            } finally {
                mParser.close();
            }
        }
        return buildCompleteConfig(config, metaData);
    }

    private void parseWidgetElement(Config config, XmlResourceParser parser)
            throws XmlPullParserException, IOException {
        int type = 0;
        int outerDepth = parser.getDepth();
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if (ELEMENT_CONTENT.equals(tagName)) {
                parseContentElement(config, parser);
            } else if (ELEMENT_FEATURE.equals(tagName)) {
                parseFeatureElement(config, parser);
            } else if (ELEMENT_PREFERENCE.equals(tagName)) {
                parsePreferenceElement(config, parser);
            } else if (ELEMENT_ACCESS.equals(tagName)) {
                parseAccessElement(config, parser);
            }
        }
    }

    private void parseContentElement(Config config, XmlResourceParser parser) {
        config.setContent(parser.getAttributeValue(null, ATTRIBUTE_SRC));
    }

    private void parseFeatureElement(Config config, XmlResourceParser parser)
            throws XmlPullParserException, IOException {
        Feature feature = new Feature();
        feature.setName(parser.getAttributeValue(null, ATTRIBUTE_NAME));
        int type = 0;
        int outerDepth = parser.getDepth();
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if (ELEMENT_PARAM.equals(tagName)) {
                parseParamElement(feature, parser);
            }
        }
        config.addFeature(feature);
    }

    private void parseParamElement(Feature feature, XmlResourceParser parser)
            throws XmlPullParserException, IOException {
        String key = parser.getAttributeValue(null, ATTRIBUTE_NAME).toLowerCase();
        String value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
        feature.setParam(key, value);
    }

    private void parsePreferenceElement(Config config, XmlResourceParser parser) {
        String key = parser.getAttributeValue(null, ATTRIBUTE_NAME).toLowerCase();
        String value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
        if (KEY_SIGNATURE.equals(key)) {
            getSecurity(config).setSignature(value);
        } else if (KEY_TIMESTAMP.equals(key)) {
            getSecurity(config).setTimestamp(Long.parseLong(value));
        } else if (KEY_VENDOR.equals(key)) {
            config.setVendor(value);
        } else {
            config.setPreference(key, value);
        }
    }

    private Security getSecurity(Config config) {
        Security security = config.getSecurity();
        if (security == null) {
            security = new Security();
            config.setSecurity(security);
        }
        return security;
    }

    private void parseAccessElement(Config config, XmlResourceParser parser) {
        Permission permission = new Permission();
        permission.setUri(parser.getAttributeValue(null, ATTRIBUTE_ORIGIN));
        permission.setApplySubdomains(parser.getAttributeBooleanValue(null, ATTRIBUTE_SUBDOMAINS, false));
        permission.setForbidden(false);
        config.addPermission(permission);
    }

    private Config buildCompleteConfig(Config config, Map<String, Object> metaData) {
        return config;
    }
}
