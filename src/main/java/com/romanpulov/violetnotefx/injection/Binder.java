package com.romanpulov.violetnotefx.injection;

import com.romanpulov.violetnotefx.annotation.BoundProperty;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.fxml.FXML;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by rpulov on 26.01.2016.
 */
public class Binder {
    public static void bindFXMLProperties(Object FXMLHolder, Object propertyHolder) {
        List<Field> viewFields = Injector.getFieldsWithAnnotation(FXMLHolder.getClass(), FXML.class);
        List<Field> propertyFields = Injector.getFieldsWithAnnotation(propertyHolder.getClass(), BoundProperty.class);
        for (Field f: viewFields)
            System.out.println("field:" + f.toString());
        for (Field f: propertyFields)
            System.out.println("bound property:" + f.toString());

        for (Field viewField : viewFields) {
            for (Field propertyField : propertyFields) {
                if (propertyField.getName().startsWith(viewField.getName())) {
                    System.out.println("found match between " + viewField + " and " + propertyField);
                    String propertyName = propertyField.getName().substring(viewField.getName().length() + 1);
                    System.out.println("propertyName=" + propertyName);

                    //find proper method
                    Method propertyMethod = null;
                    Object propertyMethodValue = null;
                    boolean oldAccessible = propertyField.isAccessible();
                    try {
                        viewField.setAccessible(true);
                        Class<?> currentClass = viewField.get(FXMLHolder).getClass();
                        while ((propertyMethod == null) && (currentClass != null)) {
                            try {
                                System.out.println("looking for textproperty method in " + currentClass.toString());
                                propertyMethod = currentClass.getDeclaredMethod(propertyName);
                                propertyMethodValue =  propertyMethod.invoke(viewField.get(FXMLHolder));
                            } catch (NoSuchMethodException e) {
                                System.out.println(" not found ...");
                                currentClass = currentClass.getSuperclass();
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    } finally {
                        viewField.setAccessible(oldAccessible);
                    }

                    if (propertyMethod != null) {
                        System.out.println("found property method!");
                        oldAccessible = propertyField.isAccessible();
                        try {
                            propertyField.setAccessible(true);
                            Object propertyValue = propertyField.get(propertyHolder);
                            if (propertyValue != null) {
                                System.out.println("property value=" + propertyValue);
                                Method bindingsMethod = Bindings.class.getDeclaredMethod("bindBidirectional", Property.class, Property.class);
                                if (bindingsMethod != null) {
                                    bindingsMethod.invoke(null, propertyMethodValue, propertyField.get(propertyHolder));
                                }
                            }
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        } finally {
                            propertyField.setAccessible(oldAccessible);
                        }
                    }
                }
            }
        }
    }
}
