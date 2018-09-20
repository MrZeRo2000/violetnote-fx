package com.romanpulov.violetnotefx.presentation.masterpass;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassPresenter implements Initializable {
    private static final int MIN_PASS_LENGTH = 6;

    @FXML
    PasswordField passwordField;

    @FXML
    private Button okButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void okButtonClick() {
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        masterPassModel.modalResult = ButtonType.OK;

        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        masterPassModel.modalResult = ButtonType.CANCEL;

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
        okButton.disableProperty().bind(passwordField.textProperty().length().lessThan(MIN_PASS_LENGTH));
    }
}
