package com.pub.internal.hybrid;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignUtils {

    private SignUtils() {
    }

    public static final String KEY_RSA = "RSA";

    public static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";

    public static final String SIGNATURE_SHA1_WITH_RSA = "SHA1withRSA";
    public static final String SIGNATURE_MD5_WITH_RSA = "MD5withRSA";

    public static final int FLAG_DEFAULT = Base64.DEFAULT;
    public static final int FLAG_NO_PADDING = Base64.NO_PADDING;
    public static final int FLAG_NO_WRAP = Base64.NO_WRAP;
    public static final int FLAG_CRLF = Base64.CRLF;
    public static final int FLAG_URL_SAFE = Base64.URL_SAFE;
    public static final int FLAG_NO_CLOSE = Base64.NO_CLOSE;

    public static PublicKey getPublicKey(KeySpec keySpec) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PublicKey getPublicKey(String key, int flags) throws Exception {
        byte[] keyBytes = Base64.decode(key, flags);
        return getPublicKey(new X509EncodedKeySpec(keyBytes));
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        return getPublicKey(key, FLAG_DEFAULT);
    }

    public static PrivateKey getPrivateKey(KeySpec keySpec) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PrivateKey getPrivateKey(String key, int flags) throws Exception {
        byte[] keyBytes = Base64.decode(key, flags);
        return getPrivateKey(new PKCS8EncodedKeySpec(keyBytes));
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        return getPrivateKey(key, FLAG_DEFAULT);
    }

    public static byte[] sign(byte[] data, PrivateKey key, String algorithm) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] sign(byte[] data, PrivateKey key) throws Exception {
        return sign(data, key, SIGNATURE_SHA1_WITH_RSA);
    }

    public static String sign(String content, PrivateKey key, String algorithm) throws Exception {
        return new String(Base64.encode(sign(content.getBytes(), key, algorithm), FLAG_NO_WRAP));
    }

    public static String sign(String content, PrivateKey key) throws Exception {
        return sign(content, key, SIGNATURE_SHA1_WITH_RSA);
    }

    public static boolean verify(byte[] data, PublicKey key, byte[] sign, String algorithm) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verify(byte[] data, PublicKey key, byte[] sign) throws Exception {
        return verify(data, key, sign, SIGNATURE_SHA1_WITH_RSA);
    }

    public static boolean verify(String content, PublicKey key, String sign, String algorithm) throws Exception {
        return verify(content.getBytes(), key, Base64.decode(sign, FLAG_NO_WRAP), algorithm);
    }

    public static boolean verify(String content, PublicKey key, String sign) throws Exception {
        return verify(content, key, sign, SIGNATURE_SHA1_WITH_RSA);
    }

}
