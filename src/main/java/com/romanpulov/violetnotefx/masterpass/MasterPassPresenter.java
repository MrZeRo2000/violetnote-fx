package com.romanpulov.violetnotefx.masterpass;

import com.romanpulov.violetnotefx.core.DataProvider;
import com.romanpulov.violetnotefx.core.annotation.Data;
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
        dataProvider.setValue("Password", passwordField.getText());
        dataProvider.setValue("SceneResult", "Ok");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);

        Stage stage = (Stage)okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButtonClick() {
        log.debug("Cancel button clicked");
        dataProvider.setValue("SceneResult", "Cancel");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);

        Stage stage = (Stage)okButton.getScene().getWindow();
        stage.close();
    }

    @Model
    private MasterPassModel masterPassModel;

    @Data
    private DataProvider dataProvider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.textProperty().setValue((String)dataProvider.getValue("Password"));
        log.debug("initializing presenter in initialize() with URL and resources method");
        log.debug("from dataProvider InitialPassword:" + (String)dataProvider.getValue("Password"));
    }
}
