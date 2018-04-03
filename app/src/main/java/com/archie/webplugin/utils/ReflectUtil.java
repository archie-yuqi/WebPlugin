package com.archie.webplugin.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Object callObjectMethod(Object target, String method, Class<?>[] parameterTypes, Object... values)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<? extends Object> clazz = target.getClass();
        Method declaredMethod = clazz.getDeclaredMethod(method, parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(target, values);
    }

    public static Class<?> getClassFromName(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LogUtils.e("getClassFromName:" + className, e);
        }
        return clazz;
    }

    public static Object newInstance(String className, Class<?>[] parameterTypes, Object... values) {
        Object instance = null;
        Class<?> clazz = getClassFromName(className);
        if (clazz != null) {
            instance = newInstance(clazz, parameterTypes, values);
        }
        return instance;
    }

    public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object... values) {
        Object instance = null;
        try {
            Constructor constructor = clazz.getConstructor(parameterTypes);
            instance = constructor.newInstance(values);
        } catch (Exception e) {
            LogUtils.e("newInstance failed", e);
        }
        return instance;
    }

}
