package com.romanpulov.violetnotefx.masterpass;

import com.romanpulov.violetnotefx.annotation.ModelOperation;
import com.romanpulov.violetnotefx.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.injection.Invoker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
                    Object result = p.newInstance();
                    String modelClassName = p.getName().replace("Presenter", "Model");
                    Class<?> modelClazz;
                    Object modelClassInstance;
                    try {
                        modelClazz = Class.forName(modelClassName);
                        modelClassInstance = modelClazz.newInstance();
                        if (modelClassInstance != null)
                            log.debug("Model class instance created");
                    } catch (ClassNotFoundException e) {
                        log.debug("class name " + modelClassName + " not found");
                        return null;
                    }
                    log.debug("class name = " + p.getName());

                    //postConstruct
                    /*
                    Method[] declaredMethods = modelClazz.getDeclaredMethods();
                    for (final Method method : declaredMethods) {
                        if (method.isAnnotationPresent(PostConstruct.class)) {
                            try {
                                boolean oldAccessible = method.isAccessible();
                                method.setAccessible(true);
                                method.invoke(modelClassInstance);
                                method.setAccessible(oldAccessible);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    */

                    //modelOperation
                    Invoker.invokeModelOperationMethod(modelClazz, modelClassInstance, ModelOperationType.LOAD);

                    /*
                    declaredMethods = modelClazz.getDeclaredMethods();
                    for (final Method method : declaredMethods) {
                        if (method.isAnnotationPresent(ModelOperation.class)) {
                            try {
                                ModelOperation modelOperation = method.getAnnotation(ModelOperation.class);
                                System.out.println("operation type = " + modelOperation.operationType());
                                boolean oldAccessible = method.isAccessible();
                                method.setAccessible(true);
                                method.invoke(modelClassInstance);
                                method.setAccessible(oldAccessible);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    */

                    return result;
                } catch(IllegalAccessException | InstantiationException e) {
                    return null;
                }
            }
        );

        try {
            //return (Parent) loader.load(getClass().getResourceAsStream("/fxml/masterpass.fxml"));
            Parent view = loader.load();
            return view ;
        } catch (IOException e) {
            return null;
        }
    }
}
