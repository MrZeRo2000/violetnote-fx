package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.core.annotation.ModelOperationType;
import com.romanpulov.violetnotefx.core.injection.Invoker;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NotePresenter {

    private static final Logger log = LoggerFactory.getLogger(NotePresenter.class);

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

    private void closeStage() {
        //Stage stage = (Stage)categoryNameField.getScene().getWindow();
        //stage.close();
    }


}
