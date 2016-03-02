package com.romanpulov.violetnotefx.presentation.masterpass;

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
        masterPassModel.modelResult.set(1);

        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        log.debug("Cancel button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        masterPassModel.modelResult.set(0);

        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage)passwordField.getScene().getWindow();
        stage.close();
    }

    @Model
    private MasterPassModel masterPassModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("initializing presenter in initialize() with URL and resources method");

    }
}