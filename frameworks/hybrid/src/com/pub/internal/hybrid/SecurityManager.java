package com.pub.internal.hybrid;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SecurityManager {

    private static final String KEY_FILE_NAME = "hybrid_key.pem";

    private static String sPublicKey;

    private Config mConfig;

    private long mExpiredTime;

    private String mSign;
    private Boolean mValidSignature;

    public SecurityManager(Config config, Context context) {
        mConfig = config;
        if (config != null && config.getSecurity() != null) {
            mExpiredTime = config.getSecurity().getTimestamp();
            mSign = config.getSecurity().getSignature();
        }
        if (sPublicKey == null) {
            sPublicKey = getPublicKey(context);
        }
    }

    public boolean isExpired() {
        return 0 < mExpiredTime && mExpiredTime < System.currentTimeMillis();
    }

    public boolean isValidSignature() {
        if (mValidSignature == null) {
            try {
                mValidSignature = isValidSignature(ConfigUtils.getRawConfig(mConfig), mSign);
            } catch (Exception e) {
                mValidSignature = false;
            }
        }
        return mValidSignature;
    }

    private boolean isValidSignature(String content, String sign) throws Exception {
        return sign != null && SignUtils.verify(content, SignUtils.getPublicKey(sPublicKey), sign);
    }

    private String getPublicKey(Context context) {
        File keyFile = new File(getHybridBaseFolder(context), KEY_FILE_NAME);
        BufferedReader br = null;
        try {
            InputStream is = null;
            if (keyFile.exists()) {
                is = new FileInputStream(keyFile);
            } else {
                is = context.getResources().getAssets().open("keys/" + KEY_FILE_NAME);
            }
            br = new BufferedReader(new InputStreamReader(is));
            return readPublicKey(br);
        } catch (IOException e) {
            throw new IllegalStateException("cannot read hybrid key.");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readPublicKey(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String content = null;
        while ((content = br.readLine()) != null) {
            if ("".equals(content.trim()) || content.startsWith("-----")) {
                continue;
            }
            sb.append(content);
            sb.append('\r');
        }
        return sb.substring(0, sb.length() - 1);
    }

    private File getHybridBaseFolder(Context context) {
        return new File(context.getFilesDir(), "miuisdk");
    }
}
