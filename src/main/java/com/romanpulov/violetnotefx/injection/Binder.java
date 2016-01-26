package com.romanpulov.violetnotefx.injection;

import com.romanpulov.violetnotefx.annotation.BoundProperty;
import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by rpulov on 26.01.2016.
 */
public class Binder {
    public static void bindPresenterProperties(Object instance) {
        List<Field> viewFields = Injector.getFieldsWithAnnotation(instance.getClass(), FXML.class);
        List<Field> propertyFields = Injector.getFieldsWithAnnotation(instance.getClass(), BoundProperty.class);
        for (Field f: viewFields)
            System.out.println("field:" + f.toString());
        for (Field f: propertyFields)
            System.out.println("bound property:" + f.toString());

        for (Field viewField : viewFields) {
            for (Field propertyField : propertyFields) {
                if (propertyField.getName().startsWith(viewField.getName())) {
                    System.out.println("found match between " + viewField + " and " + propertyField);
                }
            }
        }
    }
}
