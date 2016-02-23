package com.romanpulov.violetnotefx.categoryname;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNamePresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CategoryNamePresenter.class);

    @FXML
    private TextField categoryNameField;

    @Model
    private CategoryNameModel categoryNameModel;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        categoryNameModel.modelResult.set(1);

        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        log.debug("Cancel button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        categoryNameModel.modelResult.set(0);

        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage)categoryNameField.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryNameField.textProperty().bindBidirectional(categoryNameModel.categoryName);
    }
}
