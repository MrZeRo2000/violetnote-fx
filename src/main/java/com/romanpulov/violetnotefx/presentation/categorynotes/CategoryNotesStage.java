package com.romanpulov.violetnotefx.presentation.categorynotes;

import com.romanpulov.violetnotefx.model.Document;
import com.romanpulov.violetnotefx.presentation.DialogsHelper;
import com.romanpulov.violetnotefx.presentation.base.AppStage;
import com.romanpulov.violetnotefx.PropertiesManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;

import java.io.File;



/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNotesStage extends AppStage<CategoryNotesModel, CategoryNotesPresenter> {

    private void saveProperties() {
        PropertiesManager propertiesManager = PropertiesManager.getInstance();
        propertiesManager.setProperty(PropertiesManager.DOCUMENT_FILE_NAME, Document.getInstance().getFileName().getValue());
        propertiesManager.setProperty(PropertiesManager.LOG_LEVEL, LogManager.getRootLogger().getLevel().toString());
        propertiesManager.save();
    }

    private void performStartup() {
        File f = null;

        // first check command line
        if (model.getLoadFileName() != null) {
            f = new File(model.getLoadFileName());
        }

        // check configuration file
        if ((f == null) || !f.exists())    {
            String documentFileName = PropertiesManager.getInstance().getProperty(PropertiesManager.DOCUMENT_FILE_NAME);
            if (documentFileName != null ) {
                f = new File(documentFileName);
                model.setLoadFileName(documentFileName);
            }
        }

        // load from found file
        if ((f != null) && (f.exists()))
            controller.loadFile(f, model.getLoadFileType());

    }

    private void performCloseAction() {
        saveProperties();
        getController().shutdownExecutorService();
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        stage.setOnCloseRequest((e) -> {
            if (model.getInvalidatedData().getValue()) {
                if (!DialogsHelper.queryUnsavedData()) {
                    e.consume();
                } else
                    performCloseAction();
            } else {
                performCloseAction();
            }
        });

        stage.getScene().setOnKeyPressed((event -> {
            if ((event.isControlDown()) && (event.getCode() == KeyCode.F))
                controller.activateSearch();
        }));

        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        /*
        switch (model.getLoadFileType()) {
            case FT_IMPORT:
                controller.importPINS(new File(model.getLoadFileName()));
                break;
            case FT_VNF:
                controller.loadVNF(new File(model.getLoadFileName()));
                break;
        }
        */

        stage.setOnShown(e -> performStartup());
    }
}
