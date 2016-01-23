package com.romanpulov.violetnotefx.masterpass;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassPresenter implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(MasterPassPresenter.class);

    @FXML
    PasswordField passwordField;

    public StringProperty passwordProperty = new SimpleStringProperty();

    @FXML
    private Button okButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void initialize() {
        log.debug("initializing presenter in initialize() method");
        log.debug("resources=" + resources);
    }

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        log.debug("password=" + passwordProperty.get());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("initializing presenter in initialize() with URL and resources method");
        log.debug("pass=" + passwordField.getText());
        passwordProperty.bindBidirectional(passwordField.textProperty());
    }
}
