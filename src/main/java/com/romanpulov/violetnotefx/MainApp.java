package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesModel;
import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class MainApp extends Application {

    private static final Logger log = Logger.getLogger(MainApp.class);

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
        log.getParent().setLevel(Level.toLevel(propertiesManager.getProperty(PropertiesManager.LOG_LEVEL)));

        CategoryNotesStage categoryNotesStage = new CategoryNotesStage();
        categoryNotesStage.createStage();
        CategoryNotesModel categoryNotesModel = categoryNotesStage.getModel();
        if (params.size() > 0) {
            categoryNotesModel.setLoadFileName(params.get(0));
        }
        categoryNotesStage.show();
    }
}
