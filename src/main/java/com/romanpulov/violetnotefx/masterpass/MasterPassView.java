package com.romanpulov.violetnotefx.masterpass;

import com.romanpulov.violetnotefx.annotation.Model;
import com.romanpulov.violetnotefx.annotation.ModelOperation;
import com.romanpulov.violetnotefx.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.injection.Injector;
import com.romanpulov.violetnotefx.injection.Invoker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassView {
    private static final Logger log = LoggerFactory.getLogger(MasterPassView.class);

    public Parent getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/masterpass.fxml"));
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
                        Object modelClassInstance = modelClazz.newInstance();

                        //inject model
                        Injector.injectFieldWithAnnotation(controller.getClass(), Model.class, controller, modelClassInstance);

                        //modelOperation - allow to load
                        Invoker.invokeModelOperationMethod(modelClazz, modelClassInstance, ModelOperationType.LOAD);
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
            return view ;
        } catch (IOException e) {
            return null;
        }
    }
}
