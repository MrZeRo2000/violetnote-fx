package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NotePresenter  implements Initializable {

    private static final Logger log = LogManager.getLogger(NotePresenter.class);

    @FXML
    private ComboBox<PassCategoryFX> categoryComboBox;

    @FXML
    private TextField systemTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private PasswordField passwordRetypeTextField;

    @FXML
    private TextField urlTextField;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        noteModel.modalResult = ButtonType.OK;
        closeStage();
    }

    @FXML
    private void cancelButtonClick() {
        log.debug("Cancel button clicked");
        Invoker.invokeModelOperation(this, ModelOperationType.UNLOAD);
        noteModel.modalResult = ButtonType.CANCEL;
        closeStage();
    }

    @Model
    private NoteModel noteModel;

    private void closeStage() {
        Stage stage = (Stage)categoryComboBox.getScene().getWindow();
        stage.close();
    }

    private void setupComboBox() {
        categoryComboBox.setItems(noteModel.getPassCategoryData());

        categoryComboBox.setCellFactory((lp) ->
            new ListCell<PassCategoryFX>() {
                @Override
                protected void updateItem(PassCategoryFX item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getPathDisplayValue());
                    }
                }
        });

        categoryComboBox.setConverter(new StringConverter<PassCategoryFX>() {
            @Override
            public String toString(PassCategoryFX object) {
                return object.getPathDisplayValue();
            }

            @Override
            public PassCategoryFX fromString(String string) {
                return noteModel.getPassCategoryFXFromPathDisplayValue(string);
            }
        });
    }

    private void bindProperties() {
        okButton.disableProperty().bind(
                noteModel.categoryComboBox_valueProperty.isNull().or(
                        noteModel.passwordTextField_textProperty.isNotEqualTo(noteModel.passwordRetypeTextField_textProperty).or(
                                noteModel.userTextField_textProperty.isEmpty().or(
                                        noteModel.passwordTextField_textProperty.isEmpty()
                                )
                        )
                )
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBox();
        bindProperties();
    }
}
