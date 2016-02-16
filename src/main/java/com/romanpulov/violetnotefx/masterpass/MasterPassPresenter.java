package com.romanpulov.violetnotefx.masterpass;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MasterPassPresenter.class);

    @FXML
    PasswordField passwordField;

    @FXML
    private Button okButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);

        Stage stage = (Stage)okButton.getScene().getWindow();
        stage.close();
    }

    @Model
    private MasterPassModel masterPassModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("initializing presenter in initialize() with URL and resources method");
        log.debug("pass=" + passwordField.getText());
        /*
        try {
            Field modelField = masterPassModel.getClass().getDeclaredField("password");
            Field presenterField = this.getClass().getDeclaredField("passwordField");

            Field passwordFieldField = this.getClass().getDeclaredField("passwordField");
            Object passwordFieldValue = passwordFieldField.get(this);
            log.debug("found password field value:"+ passwordFieldValue.getClass().toString());

            Method textPropertyMethod = null;
            Class<?> currentClass = passwordFieldValue.getClass();
            while ((textPropertyMethod == null) && (currentClass != null)) {
                try {
                    log.debug("looking for textproperty method in " + currentClass.toString());
                    textPropertyMethod = currentClass.getDeclaredMethod("textProperty", null);
                } catch (NoSuchMethodException e) {
                    log.debug(" not found ...");
                    currentClass = currentClass.getSuperclass();
                }
            }

            if (textPropertyMethod != null)
                log.debug("found textproperty method");

            Object textPropertyInvoke = textPropertyMethod.invoke(passwordFieldValue);
            log.debug("textPropertyInvoke returned " + textPropertyInvoke.toString());

            ((Property<String>)textPropertyInvoke).setValue(masterPassModel.password.getValue());
            Bindings.bindBidirectional(masterPassModel.password, (Property<String>)textPropertyInvoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }
}
