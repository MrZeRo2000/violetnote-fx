package com.romanpulov.violetnotefx.core.views;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Binder;
import com.romanpulov.violetnotefx.core.injection.Injector;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by rpulov on 23.01.2016.
 */
public class FXMLAbstractView {
    public static final String DEFAULT_CLASS_NAME_ENDING = "view";
    public static final String DEFAULT_RESOURCE_PREFIX = "/fxml/";
    public static final String DEFAULT_RESOURCE_EXTENSION = ".fxml";

    private static final Logger log = LoggerFactory.getLogger(FXMLAbstractView.class);

    private Object modelInstance;

    public String getConventionalResourceName() {
        String conventionalCore = this.getClass().getSimpleName().toLowerCase();
        if (conventionalCore.endsWith(DEFAULT_CLASS_NAME_ENDING)) {
            int endingIndex = conventionalCore.lastIndexOf(DEFAULT_CLASS_NAME_ENDING);
            conventionalCore = conventionalCore.substring(0, endingIndex);
        }
        return DEFAULT_RESOURCE_PREFIX + conventionalCore + DEFAULT_RESOURCE_EXTENSION;
    }

    public Parent getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getConventionalResourceName()));
        loader.setControllerFactory((Class<?> p) ->  {
                log.debug("In setControllerFactory: p = " + p.toString());
                try {
                    //presenter
                    Object controller = p.newInstance();

                    //inject model support
                    Field modelField = Injector.getFieldWithAnnotation(controller.getClass(), Model.class);
                    if (modelField != null) {

                        // instantiate model
                        Class<?> modelClazz = modelField.getType();
                        modelInstance = modelClazz.newInstance();

                        //inject model
                        Injector.injectFieldWithAnnotation(controller.getClass(), Model.class, controller, modelInstance);

                        //modelOperation - allow to load
                        Invoker.invokeModelOperationMethod(modelInstance.getClass(), modelInstance, ModelOperationType.LOAD);
                    }
                    return controller;
                } catch(IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        );

        try {
            Parent view = loader.load();
            if (modelInstance != null)
                Binder.bindFXMLProperties(loader.getController(), modelInstance);
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
