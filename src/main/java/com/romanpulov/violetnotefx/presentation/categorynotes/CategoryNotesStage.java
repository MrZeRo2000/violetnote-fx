package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Core.dialogs.AlertDialogs;
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import com.romanpulov.violetnotefx.PropertiesManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.Optional;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNotesStage extends AppStage<CategoryNotesModel, CategoryNotesPresenter> {

    private void saveProperties() {
        PropertiesManager.getInstance().getProperties().setProperty(PropertiesManager.DOCUMENT_FILE_NAME, Document.getInstance().getFileName().getValue());
        PropertiesManager.getInstance().save();
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        stage.setOnCloseRequest((e) -> {
            if (model.getInvalidatedData().getValue()) {
                Optional<ButtonType> result = new AlertDialogs.ConfirmationAlertBuilder()
                        .setContentText("You have unsaved data. Are you sure?")
                        .setDefaultButton(ButtonType.CANCEL)
                        .buildAlert()
                        .showAndWait();
                if (!(result.get().equals(ButtonType.OK))) {
                    e.consume();
                } else
                    saveProperties();
            } else {
                saveProperties();
            }
        });

        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (model.getLoadFileType()) {
            case FT_IMPORT:
                controller.importPINS(new File(model.getLoadFileName()));
                break;
            case FT_VNF:
                controller.loadVNF(new File(model.getLoadFileName()));
                break;
        }

        PropertiesManager.getInstance().load();
        String documentFileName = PropertiesManager.getInstance().getProperties().getProperty(PropertiesManager.DOCUMENT_FILE_NAME);
        if (documentFileName != null ) {
            File f = new File(documentFileName);
            if (f.exists())
                controller.loadVNF(f);
        }
    }
}
