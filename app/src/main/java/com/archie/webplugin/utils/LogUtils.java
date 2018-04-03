package com.archie.webplugin.utils;

import android.util.Log;

public class LogUtils {

    // need to run "adb shell setprop log.tag.WebPlugin DEBUG"
    public final static String TAG = "WebPlugin";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(TAG + "." + tag, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable ex) {
        Log.e(TAG, msg, ex);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(TAG + "." + tag, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(TAG + "." + tag, msg);
    }

    public static void v(String msg) {
        Log.d(TAG, msg);
    }

}
