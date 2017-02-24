package com.ocellus.platform.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtil {
    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }


    /**
     * 获取obj对象以及在其超类classToStop之下所有子类的方法
     *
     * @param obj
     * @param fieldName
     * @return List<Method>
     */
    public static List<Method> getMethods(Object obj, Class<?> classToStop) {
        if (classToStop == null) {
            classToStop = Object.class;
        }
        List<Method> list = new ArrayList<Method>();
        for (Class<?> superClass = obj.getClass(); (superClass != classToStop && classToStop != Object.class); superClass = superClass.getSuperclass()) {
            list.addAll(Arrays.asList(superClass.getDeclaredMethods()));
        }
        return list;
    }

    /**
     * @param obj
     * @param methodName
     * @param clazzToStop
     * @return
     */
    public static Method getMethodByName(Object obj, String methodName, Class<?> clazzToStop) {
        return getMethodByName(obj, methodName, clazzToStop, null);
    }

    /**
     * 获取obj对象以及在其超类classToStop之下所有子类的方法
     *
     * @param obj
     * @param fieldName
     * @param clazzToStop
     * @param paramClazz
     * @return List<Method>
     */
    public static Method getMethodByName(Object obj, String methodName, Class<?> clazzToStop, Class<?> paramClazz) {
        if (clazzToStop == null) {
            clazzToStop = Object.class;
        }
        for (Class<?> superClass = obj.getClass(); (superClass != clazzToStop && superClass != Object.class); superClass = superClass.getSuperclass()) {
            try {
                if (paramClazz != null) {
                    return superClass.getDeclaredMethod(methodName, paramClazz);
                } else {
                    return superClass.getDeclaredMethod(methodName);
                }
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

}