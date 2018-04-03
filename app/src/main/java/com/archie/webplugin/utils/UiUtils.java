package com.archie.webplugin.utils;

import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class UiUtils {

    private UiUtils() {
        // Add a private constructor to hide the implicit public one.
    }

    public static void showToast(Context context, int descRes) {
        showToast(context, context.getString(descRes));
    }

    public static void showToast(Context context, String descRes) {
        Toast toast = Toast.makeText(context, descRes, Toast.LENGTH_LONG);
        try {
            // 修改Toast可以在锁屏上显示
            WindowManager.LayoutParams params =
                    (WindowManager.LayoutParams) ReflectUtil.callObjectMethod(toast, "getWindowParams", null);
            if (params != null) {
                params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LogUtils.e("getWindowParams failed", e);
        }
        toast.show();
    }
}
