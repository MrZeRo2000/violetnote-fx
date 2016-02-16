package com.romanpulov.violetnotefx.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rpulov on 16.02.2016.
 */
@Target({ElementType.FIELD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Data {
}
