package com.romanpulov.violetnotefx.injection;

import com.romanpulov.violetnotefx.annotation.ModelOperation;
import com.romanpulov.violetnotefx.annotation.ModelOperationType;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by rpulov on 25.01.2016.
 */
public class Invoker {
    public static void invokeModelOperationMethod(Class<?> clazz, Object instance, ModelOperationType operationType) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method method : declaredMethods) {
            if (method.isAnnotationPresent(ModelOperation.class)) {
                ModelOperation modelOperation = method.getAnnotation(ModelOperation.class);
                if (modelOperation.operationType().equals(operationType)) {
                    boolean oldAccessible = method.isAccessible();
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch(InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        method.setAccessible(oldAccessible);
                    }
                }
            }
        }
    }
}
