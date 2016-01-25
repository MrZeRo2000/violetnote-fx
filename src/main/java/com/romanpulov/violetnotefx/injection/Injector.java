package com.romanpulov.violetnotefx.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by rpulov on 25.01.2016.
 */
public class Injector {
    public static Field getFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotation))
                return field;
        }
        return null;
    }

    public static void injectFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation, Object instance, Object injection) {
        Field injectField = Injector.getFieldWithAnnotation(clazz, annotation);
        injectFieldWithAnnotation(injectField, instance, injection);
    }

    public static void injectFieldWithAnnotation(Field injectField, Object instance, Object injection) {
        if (injectField != null) {
            boolean oldAccessible = injectField.isAccessible();
            try {
                injectField.setAccessible(true);
                injectField.set(instance, injection);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                injectField.setAccessible(oldAccessible);
            }
        }
    }
}
