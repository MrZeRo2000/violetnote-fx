package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NotePresenter  implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(NotePresenter.class);

    @FXML
    private ComboBox<PassCategoryFX> categoryComboBox;

    @FXML
    private TextField systemTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField passwordRetypeTextField;

    @FXML
    private TextField commentsTextField;

    @FXML
    private TextField customTextField;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        //categoryNameModel.modelResult.set(1);

        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        log.debug("Cancel button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        //categoryNameModel.modelResult.set(0);

        closeStage();
    }

    @Model
    private NoteModel noteModel;

    private void closeStage() {
        Stage stage = (Stage)categoryComboBox.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("initializing presenter in initialize() with URL and resources method");

        categoryComboBox.setItems(noteModel.getPassCategoryData());

        categoryComboBox.setCellFactory((lp) -> {
            return new ListCell<PassCategoryFX>() {
                @Override
                protected void updateItem(PassCategoryFX item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getPathDisplayValue());
                    }
                }
            };
        });
    }

}
