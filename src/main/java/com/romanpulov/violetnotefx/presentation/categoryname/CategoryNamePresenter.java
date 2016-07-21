package com.romanpulov.violetnotefx.Presentation.categoryname;

import com.romanpulov.violetnotefx.Core.annotation.Model;
import com.romanpulov.violetnotefx.Core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.Core.injection.Invoker;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNamePresenter implements Initializable {
    @FXML
    private TextField categoryNameField;

    @FXML
    private Button okButton;

    @Model
    private CategoryNameModel categoryNameModel;

    @FXML
    private void okButtonClick() {
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        categoryNameModel.modalResult = ButtonType.OK;

        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        categoryNameModel.modalResult = ButtonType.CANCEL;

        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage)categoryNameField.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryNameField.textProperty().bindBidirectional(categoryNameModel.categoryName);
        okButton.disableProperty().bind(categoryNameField.textProperty().isEmpty());
    }
}
