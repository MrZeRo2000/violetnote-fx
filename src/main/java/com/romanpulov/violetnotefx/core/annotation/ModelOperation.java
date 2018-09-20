package com.romanpulov.violetnotefx.Core.annotation;

/**
 * Created by rpulov on 25.01.2016.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ModelOperation {
    ModelOperationType operationType() default ModelOperationType.NONE;
}
