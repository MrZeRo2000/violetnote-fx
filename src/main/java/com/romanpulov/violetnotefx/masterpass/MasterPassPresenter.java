package com.romanpulov.violetnotefx.masterpass;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassPresenter implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(MasterPassPresenter.class);

    private MasterPassModel masterPassModel = new MasterPassModel();

    @FXML
    PasswordField passwordField;

    @FXML
    private Button okButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        log.debug("password=" + masterPassModel.password.get());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("initializing presenter in initialize() with URL and resources method");
        log.debug("pass=" + passwordField.getText());
        //masterPassModel.password.bindBidirectional(passwordField.textProperty());
        try {
            Field modelField = masterPassModel.getClass().getDeclaredField("password");
            Field presenterField = this.getClass().getDeclaredField("passwordField");


            //Method bindMethod = masterPassModel.password.getClass().getDeclaredMethod("bindBidirectional", pc.newInstance().getClass());
            //bindMethod.invoke(masterPassModel.password, passwordField.textProperty());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
