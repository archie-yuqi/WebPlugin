package com.pub.internal.hybrid;

import java.util.HashMap;
import java.util.Map;

import pub.hybrid.HybridFeature;
import pub.hybrid.Response;

public class FeatureManager {

    private Map<String, HybridFeature> mFeatures = new HashMap<String, HybridFeature>();

    private Config mConfig;
    private ClassLoader mLoader;

    public FeatureManager(Config config, ClassLoader loader) {
        mConfig = config;
        mLoader = loader;
    }

    public HybridFeature lookupFeature(String name) throws HybridException {
        HybridFeature f = mFeatures.get(name);
        if (f == null) {
            Feature feature = mConfig.getFeature(name);
            if (feature == null) {
                throw new HybridException(Response.CODE_FEATURE_ERROR, "feature not declared: " + name);
            } else {
                f = initFeature(name);
                f.setParams(feature.getParams());
                mFeatures.put(name, f);
            }
        }
        return f;
    }

    private HybridFeature initFeature(String name) throws HybridException {
        try {
            @SuppressWarnings("unchecked")
            Class<HybridFeature> hfc = (Class<HybridFeature>) mLoader.loadClass(name);
            return hfc.newInstance();
        } catch (ClassNotFoundException e) {
            throw new HybridException(Response.CODE_FEATURE_ERROR, "feature not found: " + name);
        } catch (InstantiationException e) {
            throw new HybridException(Response.CODE_FEATURE_ERROR, "feature cannot be instantiated: " + name);
        } catch (IllegalAccessException e) {
            throw new HybridException(Response.CODE_FEATURE_ERROR, "feature cannot be accessed: " + name);
        }
    }
}
