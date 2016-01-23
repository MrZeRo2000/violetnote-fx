package com.romanpulov.violetnotefx.masterpass;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassView {
    private static final Logger log = LoggerFactory.getLogger(MasterPassView.class);

    public Parent getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/masterpass.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            public Object call(Class<?> p)  {
                log.debug("In setControllerFactory: p = " + p.toString());
                try {
                    Object result = p.newInstance();
                    String modelClassName = p.getName().replace("Presenter", "Model");
                    Class<?> modelClazz;
                    try {
                        modelClazz = Class.forName(modelClassName);
                        Object modelClassInstance = modelClazz.newInstance();
                        if (modelClassInstance != null)
                            log.debug("Model class instance created");
                    } catch (ClassNotFoundException e) {
                        log.debug("class name " + modelClassName + " not found");
                        return null;
                    }
                    log.debug("class name = " + p.getName());
                    return result;
                } catch(IllegalAccessException | InstantiationException e) {
                    return null;
                }
            }
        });

        try {
            //return (Parent) loader.load(getClass().getResourceAsStream("/fxml/masterpass.fxml"));
            Parent view = loader.load();
            return view ;
        } catch (IOException e) {
            return null;
        }
    }
}
