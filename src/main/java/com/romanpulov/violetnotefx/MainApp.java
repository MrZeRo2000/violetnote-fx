package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.presentation.categorynotes.CategoryNotesModel;
import com.romanpulov.violetnotefx.presentation.categorynotes.CategoryNotesStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.List;

public class MainApp extends Application {

    private static final Logger log = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        // read command line parameters
        List<String> params = getParameters().getRaw();
        log.info("params:" + params.toString());

        // read properties
        PropertiesManager propertiesManager = PropertiesManager.getInstance();
        propertiesManager.load();

        // set up logger level
        Configurator.setRootLevel(Level.toLevel(propertiesManager.getProperty(PropertiesManager.LOG_LEVEL)));

        CategoryNotesStage categoryNotesStage = new CategoryNotesStage();
        categoryNotesStage.createStage();
        CategoryNotesModel categoryNotesModel = categoryNotesStage.getModel();
        if (params.size() > 0) {
            categoryNotesModel.setLoadFileName(params.get(0));
        }
        categoryNotesStage.show();
    }
}
